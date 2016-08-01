package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.api.errors.ISPACException;

import java.util.Iterator;

import org.apache.log4j.Logger;


/**
 * 
 * @author teresa
 * @date 17/11/2009
 * @propósito Inicializa el expediente de propuesta asociado al expediente de Convocatoria Negociado de contratación actual.
 * Asocia el documento zip de Contenido de la propuesta al expediente de Propuesta relacionado.
 * Es el mismo fichero físico zip en el repositorio de documentos
 */
public class InitPropuestaNegRule implements IRule {

	protected String STR_entidad = "";
	protected String STR_extracto = "";
	protected String STR_area = "";
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(InitPropuestaNegRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{

			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
			
			//Obtención del expediente de propuesta relacionado
			String strExpPropuesta = rulectx.getNumExp();

			//Obtención del expediente de negociado de contratación
			String strQuery = "WHERE NUMEXP_HIJO='" + strExpPropuesta + "'";
			IItemCollection collExpRel = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator itExpRel = collExpRel.iterator();
			if (! itExpRel.hasNext()) {
				logger.warn("No se encuentra el expediente de propuesta relacionado");
				return new Boolean(false);
			}
			IItem iExpRels = ((IItem)itExpRel.next());
			String strExpNegociado = iExpRels.getString("NUMEXP_PADRE");
			
	        //Inicializar los datos de la propuesta
			IItemCollection col = entitiesAPI.getEntities(STR_entidad, strExpNegociado);
	        Iterator it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        IItem entidad = (IItem)it.next();
	        //String strArea = entidad.getString("AREA");
			IItem propuesta = entitiesAPI.createEntity("SECR_PROPUESTA", strExpPropuesta);
			if (propuesta != null)
			{
				//propuesta.set("ORIGEN", strArea);
				propuesta.set("ORIGEN", STR_area);
				propuesta.set("EXTRACTO", STR_extracto);
				propuesta.store(cct);
			}
			
			//Actualizar el campo "estado" de la entidad para
			//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
	        entidad.set("ESTADO", "Propuesta");
	        entidad.store(cct);
			
			//Añadir el ZIP con el contenido de la propuesta
	        //---------------------------------------------

	        // Obtener el documento zip "Contenido de la propuesta" del expediente de Convocatoria de contratación
			IItemCollection documentsCollection = entitiesAPI.getDocuments(strExpNegociado, "NOMBRE='Contenido de la propuesta'", "FDOC DESC");
			IItem contenidoPropuesta = null;
			if (documentsCollection!=null && documentsCollection.next()){
				contenidoPropuesta = (IItem)documentsCollection.iterator().next();
			}else{
				throw new ISPACInfo("No se ha encontrado el documento de Contenido de la propuesta");
			}

			//Obtener la fase del expediente de Propuesta a partir de su NUMEXP en la tabla de spac_fases
			String strQueryAux = "WHERE NUMEXP='" + strExpPropuesta + "'";
			IItemCollection collExpsAux = entitiesAPI.queryEntities("SPAC_FASES", strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				logger.warn("No se encuentra el expediente asociado de propuesta con numexp: "+strExpPropuesta);
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			
			// Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
			if (contenidoPropuesta!=null){

				// Crear el documento del mismo tipo que el Contenido de la propuesta pero asociado al nuevo expediente de Propuesta
				IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase,contenidoPropuesta.getInt("ID_TPDOC"));

				String infopag = contenidoPropuesta.getString("INFOPAG");
				String infopagrde = contenidoPropuesta.getString("INFOPAG_RDE");
				String repositorio = contenidoPropuesta.getString("REPOSITORIO");
				String descripcion = contenidoPropuesta.getString("DESCRIPCION");
				String extension = contenidoPropuesta.getString("EXTENSION");			
				int idPlantilla = contenidoPropuesta.getInt("ID_PLANTILLA");

				nuevoDocumento.set("INFOPAG", infopag);
				nuevoDocumento.set("INFOPAG_RDE", infopagrde);
				nuevoDocumento.set("REPOSITORIO", repositorio);
				nuevoDocumento.set("DESCRIPCION", descripcion);
				nuevoDocumento.set("EXTENSION", extension);
				if (String.valueOf(idPlantilla)!=null && String.valueOf(idPlantilla).trim().length()!=0){
					nuevoDocumento.set("ID_PLANTILLA", idPlantilla);
				}
				try
				{
					String codVerificacion = contenidoPropuesta.getString("COD_VERIFICACION");
					nuevoDocumento.set("COD_VERIFICACION", codVerificacion);
				}
				catch(ISPACException e)
				{
					//No existe el campo
				}
				
				nuevoDocumento.store(cct);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;

	}


	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}




}

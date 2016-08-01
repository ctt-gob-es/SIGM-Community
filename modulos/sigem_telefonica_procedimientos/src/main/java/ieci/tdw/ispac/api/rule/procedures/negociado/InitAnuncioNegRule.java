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
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.api.errors.ISPACException;

import java.util.Iterator;

import org.apache.log4j.Logger;


/**
 * 
 * @author teresa
 * @date 03/03/2010
 * @propósito Inicializa el expediente de Solicitud de Inserción del Anuncio en el BOP asociado al expediente de Convocatoria Negociado
 * de contratación actual.
 * Asocia el documento DOC de Anexo a Solicitud al expediente de Solicitud de Inserción del Anuncio en el BOP relacionado.
 * Es el mismo fichero físico DOC en el repositorio de documentos
 */
public class InitAnuncioNegRule implements IRule {

	protected String STR_entidad = "SGN_NEGOCIADO";
	//protected String STR_extracto = "";
	//protected String STR_area = "";
	protected String STR_entidad_anuncio = "Contratación y compras";
	protected String STR_sumario = "Solicitud de anuncio generada desde el procedimiento de " +
										"Convocatoria de Negociado de Contratación Con Publicidad";
	protected String STR_observaciones = "Solicitud de anuncio generada desde el procedimiento de " +
											"Convocatoria de Negociado de Contratación Con Publicidad";
	protected String STR_urgencia = "Normal";
	protected String STR_anexo_solicitud = "Anexo a Solicitud";
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(InitAnuncioNegRule.class);

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
			
			//Obtención del expediente de anuncio relacionado
			String strExpAnuncio = rulectx.getNumExp();

			//Obtención del expediente de negociado de contratación
			String strQuery = "WHERE NUMEXP_HIJO='" + strExpAnuncio + "'";
			IItemCollection collExpRel = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator itExpRel = collExpRel.iterator();
			if (! itExpRel.hasNext()) {
				logger.warn("No se encuentra el expediente de propuesta relacionado");
				return new Boolean(false);
			}
			IItem iExpRels = ((IItem)itExpRel.next());
			String strExpNegociado = iExpRels.getString("NUMEXP_PADRE");
			
	        //Inicializar los datos del anuncio
			IItemCollection col = entitiesAPI.getEntities(STR_entidad, strExpNegociado);
	        Iterator it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        
	        IItem entidad = (IItem)it.next();
	        //String strArea = entidad.getString("AREA");
			IItem anuncio = entitiesAPI.createEntity("BOP_SOLICITUD", strExpAnuncio);
			if (anuncio != null)
			{
				anuncio.set("ENTIDAD", STR_entidad_anuncio);
				anuncio.set("SUMARIO", STR_sumario);
				anuncio.set("OBSERVACIONES", STR_observaciones);
				anuncio.set("URGENCIA", STR_urgencia);
				anuncio.store(cct);
			}
			
			//Actualizar el campo "estado" de la entidad para
			//que en el formulario se oculte el enlace de creación de Anuncio
	        entidad.set("ESTADO", "Anuncio");
	        entidad.store(cct);
			
			//Añadir el DOC con el contenido del anuncio
	        //---------------------------------------------

	        // Obtener el documento DOC "Anuncio de licitación" del expediente de Convocatoria de contratación
			IItemCollection documentsCollection = entitiesAPI.getDocuments(strExpNegociado, "NOMBRE='Anuncio de licitación'", "FDOC DESC");
			IItem docAnuncio = null;
			if (documentsCollection!=null && documentsCollection.next()){
				docAnuncio = (IItem)documentsCollection.iterator().next();
			}else{
				throw new ISPACInfo("No se ha encontrado el documento de Anuncio de licitación");
			}
			
			//Obtener la fase del expediente de Anuncio a partir de su NUMEXP en la tabla de spac_fases
			String strQueryAux = "WHERE NUMEXP='" + strExpAnuncio + "'";
			IItemCollection collExpsAux = entitiesAPI.queryEntities("SPAC_FASES", strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				logger.warn("No se encuentra el expediente asociado de propuesta con numexp: "+strExpAnuncio);
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			
			// Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
			if (docAnuncio!=null){
				// Crear el documento del mismo tipo que el Contenido de la propuesta pero asociado al nuevo expediente de Propuesta
				//IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase,docAnuncio.getInt("ID_TPDOC"));
				IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase,CommonFunctions.getIdTpDoc(rulectx, STR_anexo_solicitud));

				String infopag = docAnuncio.getString("INFOPAG");
				String infopagrde = docAnuncio.getString("INFOPAG_RDE");
				String repositorio = docAnuncio.getString("REPOSITORIO");
				String descripcion = docAnuncio.getString("DESCRIPCION");
				String extension = docAnuncio.getString("EXTENSION");			
				int idPlantilla = docAnuncio.getInt("ID_PLANTILLA");

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
					String codVerificacion = docAnuncio.getString("COD_COTEJO");
					nuevoDocumento.set("COD_COTEJO", codVerificacion);
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

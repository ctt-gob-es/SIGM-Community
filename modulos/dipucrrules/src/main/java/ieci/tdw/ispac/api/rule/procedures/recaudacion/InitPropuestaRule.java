package ieci.tdw.ispac.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class InitPropuestaRule implements IRule {

	protected String entidad = "";
	protected String extracto = "";
	
	private static final Logger LOGGER = Logger.getLogger(InitPropuestaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
    	try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------

	        //Obtiene el expediente de la entidad
	        String numexpProp = rulectx.getNumExp();	
	        String strQuery = "WHERE NUMEXP_HIJO='"+numexpProp+"'";
	        IItemCollection col = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
	        Iterator it = col.iterator();
	        if (!it.hasNext()) {
	        	return false;
	        }
        	IItem relacion = (IItem)it.next();
        	String numexpEnt = relacion.getString("NUMEXP_PADRE");
        	col = entitiesAPI.getEntities(entidad, numexpEnt);
	        it = col.iterator();
	        if (!it.hasNext()) {
	        	return false;
	        }
	        IItem entidad = (IItem)it.next();
	                	
	        //Inicializa los datos de la propuesta
			IItem propuesta = entitiesAPI.createEntity("SECR_PROPUESTA", numexpProp);
			if (propuesta != null) {
				propuesta.set("EXTRACTO", extracto);
				propuesta.store(cct);
			}
			
			//Actualiza el campo "estado" de la entidad para
			//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
	        entidad.set("ESTADO", "Propuesta");
	        entidad.store(cct);
			
			//Añade el ZIP con el contenido de la propuesta
	        //---------------------------------------------

	        // Obtener el documento zip "Contenido de la propuesta" del expediente de la entidad
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexpEnt, "NOMBRE='Contenido de la propuesta'", "FDOC DESC");
			IItem contenidoPropuesta = null;
			if (documentsCollection!=null && documentsCollection.next()){
				contenidoPropuesta = (IItem)documentsCollection.iterator().next();
			}else{
				throw new ISPACInfo("No se ha encontrado el documento de Contenido de la propuesta");
			}
			
			//Obtiene el número de fase de la propuesta
			String strQueryAux = "WHERE NUMEXP='" + numexpProp + "'";
			IItemCollection collExpsAux = entitiesAPI.queryEntities("SPAC_FASES", strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return false;
			}
			IItem iExpedienteAux = (IItem)itExpsAux.next();
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
				try {
					String codVerificacion = contenidoPropuesta.getString("COD_COTEJO");
					nuevoDocumento.set("COD_COTEJO", codVerificacion);
				} catch(ISPACException e) {
					LOGGER.info("No existe el campo código de verificación", e);
				}

				nuevoDocumento.store(cct);
			}
	        
        	return true;
        }
    	catch(Exception e) {
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta.",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
		// Empty method
    }

}

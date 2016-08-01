package ieci.tdw.ispac.api.rule.procedures.presupuesto;

import java.util.Iterator;
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
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class InitSolicitudBopPresupuestoRule implements IRule {

	protected String STR_entidad = "";
	protected String STR_BOP_entidad = "";
	protected String STR_BOP_urgencia = "";
	protected String STR_BOP_sumario = "";
	protected String STR_BOP_observaciones = "";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------

	        //Obtiene el expediente de la entidad
	        String numexp_solicitud = rulectx.getNumExp();	
	        String strQuery = "WHERE NUMEXP_HIJO='"+numexp_solicitud+"'";
	        IItemCollection col = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
	        Iterator it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem relacion = (IItem)it.next();
        	String numexp_ent = relacion.getString("NUMEXP_PADRE");
        	col = entitiesAPI.getEntities(STR_entidad, numexp_ent);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        IItem entidad = (IItem)it.next();

	        //Actualiza el campo "estado" de la entidad para
			//que en el formulario se oculte el enlace de creación del expediente
	        entidad.set("ESTADO", "Creado");
	        entidad.store(cct);
        	
	        //Inicializa los datos de la solicitud BOP
			IItem solicitud = entitiesAPI.createEntity("BOP_SOLICITUD", numexp_solicitud);
			if (solicitud != null)
			{
				solicitud.set("ENTIDAD", STR_BOP_entidad);
				solicitud.set("URGENCIA", STR_BOP_urgencia);
				solicitud.set("SUMARIO", STR_BOP_sumario);
				solicitud.set("OBSERVACIONES", STR_BOP_observaciones);
				solicitud.store(cct);
			}
			
			//Añade el anuncio en formato DOC
	        //-------------------------------

	        // Obtener el documento "Anuncio" del expediente de la entidad
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp_ent, "NOMBRE='Anuncio'", "FDOC DESC");
			IItem docAnuncio = null;
			if (documentsCollection!=null && documentsCollection.next()){
				docAnuncio = (IItem)documentsCollection.iterator().next();
			}else{
				throw new ISPACInfo("No se ha encontrado el documento de Anuncio");
			}
			
			//Obtiene el número de fase de la solicitud
			String strQueryAux = "WHERE NUMEXP='" + numexp_solicitud + "'";
			IItemCollection collExpsAux = entitiesAPI.queryEntities("SPAC_FASES", strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			

			// Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
			if (docAnuncio!=null){

				// Crear el documento asociado al nuevo expediente de Solicitud BOP
				// Debe ser de tipo "Anexo a Solicitud" y extension "DOC"
				int idDocSolicitud = CommonFunctions.getIdTpDoc(rulectx, "Anexo a Solicitud");
				if (idDocSolicitud != -1 && docAnuncio.getString("EXTENSION").toLowerCase().compareTo("doc")==0)
				{
					IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase,idDocSolicitud);
	
					String infopag = docAnuncio.getString("INFOPAG");
					String infopagrde = docAnuncio.getString("INFOPAG_RDE");
					String repositorio = docAnuncio.getString("REPOSITORIO");
					String descripcion = docAnuncio.getString("DESCRIPCION");
					String extension = "DOC"; //La extensión debe ser así, DOC en mayúsculas.			
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
			}

	        
        	return new Boolean(true);
        }
    	catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar la solicitud BOP.",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

}

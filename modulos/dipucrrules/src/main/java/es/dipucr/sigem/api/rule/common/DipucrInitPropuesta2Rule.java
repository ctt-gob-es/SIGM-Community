package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrInitPropuesta2Rule implements IRule {

	
	private static final Logger logger = Logger.getLogger(DipucrInitPropuesta2Rule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			//Obtiene el expediente de la entidad
	        String numexp_prop = rulectx.getNumExp();	
	        String strQuery = "WHERE NUMEXP_HIJO='"+numexp_prop+"'";
	        IItemCollection col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
	        Iterator it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem relacion = (IItem)it.next();
        	String numexp_ent = relacion.getString("NUMEXP_PADRE");
        	col = entitiesAPI.getEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, numexp_ent);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        IItem entidad = (IItem)it.next();
	        String strArea = entidad.getString("AREA");
	        
	        //Saco el titulo de la convocatoria para ponerlo en asunto y extracto
	        strQuery = "WHERE NUMEXP='"+numexp_ent+"'";
	        IItemCollection colConvoc = entitiesAPI.queryEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, strQuery);
	        Iterator itConvoc = colConvoc.iterator();
	        if (!itConvoc.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        IItem convocatoria = (IItem)itConvoc.next();
	        
	        String titulo = "";
	        String num_vicep = "";
	        String nombre_vicep = "";
	        if (convocatoria.getString("TITULO")!=null) titulo = convocatoria.getString("TITULO"); else titulo = "";
	        titulo = titulo.toLowerCase();
	        if (convocatoria.getString("NUM_VICEP")!=null) num_vicep = convocatoria.getString("NUM_VICEP"); else num_vicep = "";
	        if (convocatoria.getString("NOMBRE_VICEP")!=null) nombre_vicep = convocatoria.getString("NOMBRE_VICEP"); else nombre_vicep = "";
	        
	        IItemCollection colProp = entitiesAPI.getDocuments(numexp_ent, "UPPER(NOMBRE) LIKE '%"+Constants.CONSTANTES.PROPUESTA+"%'", "FDOC DESC");
	        Iterator itProp = colProp.iterator();
	        IItem iPropuesta = (IItem)itProp.next();
	        
	        Date fecha_aprobacion = null;
	        if (iPropuesta.getDate("FAPROBACION")!=null) fecha_aprobacion = iPropuesta.getDate("FAPROBACION"); else fecha_aprobacion = new Date();
	        
	        //Inicializa los datos de la propuesta
			IItem propuesta = entitiesAPI.createEntity(Constants.TABLASBBDD.SECR_PROPUESTA, numexp_prop);
			if (propuesta != null)
			{
				propuesta.set("ORIGEN", strArea);
				propuesta.set("EXTRACTO", titulo);
				propuesta.set("CARGO", num_vicep);
				propuesta.set("NOMBRE_PERSONA", nombre_vicep);
				propuesta.set("FECHA_EMISION", fecha_aprobacion);
				propuesta.store(cct);
			}
			
			IItem iPropuestaExp = ExpedientesUtil.getExpediente(cct, numexp_prop);

			if(iPropuestaExp != null) {
				iPropuestaExp.set("ASUNTO", titulo);
				iPropuestaExp.store(cct);
			}
			
			//Actualiza el campo "estado" de la entidad para
			//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
	        entidad.set("ESTADO", "Propuesta");
	        entidad.store(cct);
			
			//Añade el ZIP con el contenido de la propuesta
	        //---------------------------------------------
	        // Obtener el documento zip "Contenido de la propuesta" del expediente de la entidad
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp_ent, "NOMBRE = 'Documentación de Propuesta'", "FDOC DESC");
			IItem contenidoPropuesta = null;
			if (documentsCollection!=null && documentsCollection.next()){
				contenidoPropuesta = (IItem)documentsCollection.iterator().next();
			}else{
				throw new ISPACInfo("No se ha encontrado el documento de Contenido de la propuesta");
			}
			
			//Obtiene el número de fase de la propuesta
			String strQueryAux = "WHERE NUMEXP='" + numexp_prop + "'";
			IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");

			if (contenidoPropuesta!=null){
				//Se busca el id del tramite
				 String consulta = "WHERE ID = "+idFase;
				 IItemCollection iFases = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, consulta);
				 Iterator IFases = iFases.iterator();
				 int idFaseProp=0;
				 
				 while (IFases.hasNext()){
					 IItem fase = (IItem)IFases.next();
					 idFaseProp = fase.getInt("ID_FASE");
				 }
				 
				 consulta = "WHERE ID_FASE = "+idFaseProp;
				 IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, consulta);
				 Iterator ITramiteProp = iTramiteProp.iterator();
				 int idTramite=0;
				 while (ITramiteProp.hasNext()){
					 IItem tramite = (IItem)ITramiteProp.next();
					 idTramite = tramite.getInt("ID");
				 }

				//Creo el tramite contenido de propuesta
				int idTramitePropuesta = transaction.createTask(idFase, idTramite);
				
				String infopag = contenidoPropuesta.getString("INFOPAG");
				String descripcion = contenidoPropuesta.getString("DESCRIPCION");
				File docZip = DocumentosUtil.getFile(cct, infopag, null, null);
				
				IItem nuevoDocumento = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropuesta, contenidoPropuesta.getInt("ID_TPDOC"), descripcion, docZip, contenidoPropuesta.getString("EXTENSION"));
				nuevoDocumento.set("NOMBRE", Constants.TIPODOC.DOCUMENTACION_PROPUESTA);
				nuevoDocumento.store(cct);
				if(docZip != null & docZip.exists()) docZip.delete();
				
				//Creacion del documento de contenido de la propuesta				
				//Contenido de la propuesta				
				int taskIdExpedienteInicial = Integer.parseInt(cct.getSsVariable("taskId"));
		        colProp = entitiesAPI.getDocuments(numexp_ent, "UPPER(NOMBRE) LIKE '%"+Constants.CONSTANTES.PROPUESTA+"%' ID_TRAMITE != "+taskIdExpedienteInicial, "FDOC DESC");
		        itProp = colProp.iterator();
		        iPropuesta = (IItem)itProp.next();
				
		        //Copiar el documento de propuesta en otro documentp
				infopag = iPropuesta.getString("INFOPAG");
				File filePropuesta = DocumentosUtil.getFile(cct, infopag, null, null);
				DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropuesta, iPropuesta.getInt("ID_TPDOC"), Constants.TIPODOC.CONTENIDO_PROPUESTA, filePropuesta, iPropuesta.getString("EXTENSION"));

				filePropuesta.delete();
				//Actualiza el campo "estado" de la entidad para
				//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
				convocatoria.set("ESTADO", "Propuesta");
				convocatoria.store(cct);
				
				/**
				 * [Teresa] INICIO Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
				 * **/
				//Importar participantes.
				ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexp_ent, numexp_prop);
				/**
				 * [Teresa] FIN Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
				 * **/
			}
	        
        	return new Boolean(true);
        }
    	catch(Exception e) 
        {
    		logger.error("No se ha podido inicializar la propuesta. " + e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta. " + e.getMessage(),e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

}

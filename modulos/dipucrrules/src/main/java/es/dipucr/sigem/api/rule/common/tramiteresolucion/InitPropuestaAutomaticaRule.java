package es.dipucr.sigem.api.rule.common.tramiteresolucion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class InitPropuestaAutomaticaRule implements IRule {

	
	private static final Logger logger = Logger.getLogger(InitPropuestaAutomaticaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	String numexp = "";
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
	        //----------------------------------------------------------------------------------------------
				
			numexp = rulectx.getNumExp();
			String taskId = cct.getSsVariable("taskId");
			//logger.warn("taskId. "+taskId);
			
			//Obtiene el expediente de la entidad
	        String numexp_prop = rulectx.getNumExp();	
	        String strQuery = "WHERE NUMEXP_HIJO='"+numexp_prop+"'";
	        IItemCollection col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
	        //logger.warn("strQuery "+strQuery);
	        Iterator<IItem> it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem relacion = (IItem)it.next();
        	String numexp_ent = relacion.getString("NUMEXP_PADRE");
        	//logger.warn("numexp_ent "+numexp_ent);
        	col = entitiesAPI.getEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, numexp_ent);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        IItem entidad = (IItem)it.next();
	        String strArea = entidad.getString("AREA");
	        
	        //logger.warn("strArea "+strArea);
	        
	        //Saco el titulo de la convocatoria para ponerlo en asunto y extracto
	        strQuery = "WHERE NUMEXP='"+numexp_ent+"'";
	        IItemCollection colConvoc = entitiesAPI.queryEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, strQuery);
	        Iterator<IItem> itConvoc = colConvoc.iterator();
	        if (!itConvoc.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        
	        //Obtener el id del trámite del expediente padre.
	        
	        IItem convocatoria = (IItem)itConvoc.next();
	        
	        String titulo = "";
	        String num_vicep = "";
	        String nombre_vicep = "";
	        if (convocatoria.getString("TITULO")!=null) titulo = convocatoria.getString("TITULO"); else titulo = "";
	        titulo = titulo.toLowerCase();
	        if (convocatoria.getString("NUM_VICEP")!=null) num_vicep = convocatoria.getString("NUM_VICEP"); else num_vicep = "";
	        if (convocatoria.getString("NOMBRE_VICEP")!=null) nombre_vicep = convocatoria.getString("NOMBRE_VICEP"); else nombre_vicep = "";  
			
			//Actualizo el asunto de la tabla spac_expedientes
			strQuery = "WHERE NUMEXP='"+numexp_prop+"'";
			IItem iPropuestaExp = ExpedientesUtil.getExpediente(cct, numexp_prop);
			if(iPropuestaExp != null){
				iPropuestaExp.set("ASUNTO", titulo);
				iPropuestaExp.store(cct);
			}
			
			//Copio todos los participantes que tiene relacionado este expediente
			
			//Actualiza el campo "estado" de la entidad para
			//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
	        entidad.set("ESTADO", "Propuesta");
	        entidad.store(cct);

			//Obtiene el número de fase de la propuesta
			String strQueryAux = "WHERE NUMEXP='" + numexp_prop + "'";
			//logger.warn("strQueryAux "+strQueryAux);
			IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
			Iterator<IItem> itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return new Boolean(false);
			}
			
			int codContenidoPropuesta = TramitesUtil.crearTramite(cct, "CONT-TRAM", numexp_prop);
			logger.info("idTramitePropuesta "+codContenidoPropuesta);			
			logger.info("Creado el tramite");
			
			//Se busca el id del tramite
			String sQueryTramite = "WHERE ID_TRAM_EXP="+taskId+" AND FECHA_CIERRE IS NULL AND NUMEXP='"+numexp_ent+"' ORDER BY FECHA_INICIO";
			//logger.warn("sQueryTramite "+sQueryTramite);
			IItemCollection icTramite = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, sQueryTramite);
			Iterator<IItem> itTramite = icTramite.iterator();
			int tramiteExpPadre = 0;
			if(itTramite.hasNext()){
				IItem itemTramite = itTramite.next();
				tramiteExpPadre = itemTramite.getInt("ID_TRAM_EXP");
			}
			
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp_ent, "ID_TRAMITE = "+tramiteExpPadre, "FDOC DESC");
			
			Iterator<IItem> docuPro = documentsCollection.iterator();
			while(docuPro.hasNext()){
				IItem contenidoPropuesta = docuPro.next();
				String infopag = "";
				if(contenidoPropuesta.getString("INFOPAG_RDE")!=null){
					infopag = contenidoPropuesta.getString("INFOPAG_RDE");
				}
				else{
					infopag = contenidoPropuesta.getString("INFOPAG");
				}
				String descripcion = contenidoPropuesta.getString("DESCRIPCION");
				File fileFoliado = DocumentosUtil.getFile(cct, infopag, null, null);
				
				IItem nuevoDocumento = DocumentosUtil.generaYAnexaDocumento(rulectx, codContenidoPropuesta, contenidoPropuesta.getInt("ID_TPDOC"), descripcion, fileFoliado, contenidoPropuesta.getString("EXTENSION"));	
				nuevoDocumento.set("NOMBRE", Constants.TIPODOC.DOCUMENTACION_PROPUESTA);
				nuevoDocumento.store(cct);
				if(fileFoliado != null && fileFoliado.exists()) fileFoliado.delete();
				
				logger.warn("Creado el documento");
				
				// Crear el documento del mismo tipo que el Contenido de la propuesta pero asociado al nuevo expediente de Propuesta
				//IItem nuevoDocumento = (IItem)genDocAPI.createTaskDocument(codContenidoPropuesta, contenidoPropuesta.getInt("ID_TPDOC"));
				//logger.warn("Creado el documento");				
				
				/*String extension = contenidoPropuesta.getString("EXTENSION");			
				nuevoDocumento.set("INFOPAG", infopag);
				nuevoDocumento.set("NOMBRE", Constants.TIPODOC.DOCUMENTACION_PROPUESTA);
				nuevoDocumento.set("DESCRIPCION", descripcion);
				nuevoDocumento.set("EXTENSION", extension);
				nuevoDocumento.store(cct);*/
			}
			
			
			//Creacion del documento de contenido de la propuesta
			
			//creamos una tabla para saber en cada tramite de un expediente si es o no una propuesta
    		IItem iPropuesta = DocumentosUtil.getUltimaPropuestaFirmada(cct, numexp_ent);
    		if(iPropuesta != null){
	        	
	        	//Inicializa los datos de la propuesta
				IItem propuesta = entitiesAPI.createEntity(Constants.TABLASBBDD.SECR_PROPUESTA, numexp_prop);
				if (propuesta != null)
				{
					propuesta.set("ORIGEN", strArea);
					propuesta.set("EXTRACTO", titulo);
					propuesta.set("CARGO", num_vicep);
					propuesta.set("NOMBRE_PERSONA", nombre_vicep);
					propuesta.set("FECHA_EMISION", iPropuesta.getDate("FFIRMA"));
					propuesta.store(cct);
				}
	        
		        //Copiar el documento de propuesta en otro documentp
				String infopag = iPropuesta.getString("INFOPAG");
				String descripcion = iPropuesta.getString("DESCRIPCION");	
				
				File filePropuesta = DocumentosUtil.getFile(cct, infopag, null, null);
							
				IItem nuevoDocumento = DocumentosUtil.generaYAnexaDocumento(rulectx, codContenidoPropuesta, iPropuesta.getInt("ID_TPDOC"), descripcion, filePropuesta, extensionEntidad);	
				nuevoDocumento.set("NOMBRE", Constants.TIPODOC.CONTENIDO_PROPUESTA);
				nuevoDocumento.store(cct);
				if(filePropuesta != null && filePropuesta.exists()) filePropuesta.delete();
				
				//Actualiza el campo "estado" de la entidad para
				//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
				convocatoria.set("ESTADO", "Propuesta");
				convocatoria.store(cct);
	    	}
			else{
				throw new ISPACRuleException("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR");
			}
			
			/**
			 * [Teresa] INICIO Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
			 * **/
			//Importar participantes.
			ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexp_ent, numexp_prop);
			/**
			 * [Teresa] FIN Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
			 * **/
				
        	return new Boolean(true);
        }
    	catch(ISPACRuleException e) 
        {
    		logger.error("Error. No se ha podido iniciar la propuesta del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Error. No se ha podido iniciar la propuesta del expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (ISPACException e) {
        	logger.error("Error. No se ha podido iniciar la propuesta del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Error. No se ha podido iniciar la propuesta del expediente: " + numexp + ". " + e.getMessage(), e);
    	} catch (Exception e) {
    		logger.error("Error. No se ha podido iniciar la propuesta del expediente: " + numexp + ". " + e.getMessage(), e);
    		throw new ISPACRuleException("Error. No se ha podido iniciar la propuesta del expediente: " + numexp + ". " + e.getMessage(), e);
    	}
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

}

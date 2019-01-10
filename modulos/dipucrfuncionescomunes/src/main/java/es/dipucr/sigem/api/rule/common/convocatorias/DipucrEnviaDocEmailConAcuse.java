package es.dipucr.sigem.api.rule.common.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

public class DipucrEnviaDocEmailConAcuse implements IRule{
	private static final Logger LOGGER = Logger.getLogger(DipucrEnviaDocEmailConAcuse.class);

	private static final String FORMATO_FECHA_DD_MM_YYYY = "dd/MM/yyyy";

	public String VAR_EMAILS; //Variable de sistema de la que tomamos los emails 
	public String contenido; //Texto con el contenido del correo
	public String asunto; //Asunto del correo
	public boolean conDocumento; // Con esta opcion se puede decir si se quiere mandar los documentos o no
	public String logoCabecera = "logoCabecera.gif"; //Se puede especificar un logo para los correos específico, si se deja toma el por defecto.
	public String nombreNotif;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        IClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        LOGGER.info("INICIO - " + this.getClass().getName());
	        
	        String numexp = rulectx.getNumExp();
	        //[Ticket190] INICIO ALSIGM3 error en el ciruito de firma de la interventora al lanzar la regla EnvioAvisoEmailFinFirmaRule 
	        int tramiteId = 0;
	        if(numexp == null){
	        	//Si el numexp es null quiere decir que la regla se lanza desde el circuito de firmas
	        	int idDoc = rulectx.getInt("ID_DOCUMENTO");
				IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
	        	tramiteId = itemDocumento.getInt("ID_TRAMITE");
	        	numexp = itemDocumento.getString("NUMEXP");
	        } else {
	        	tramiteId = rulectx.getTaskId();
	        }	
	        
	        //[Ticket190] FIN ALSIGM3 error en el ciruito de firma de la interventora al lanzar la regla EnvioAvisoEmailFinFirmaRule 
	        
	        String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");

			Object[] imagen = {rutaImg, new Boolean(true), logoCabecera, "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
	        
	        String tipoDocumento = "";
	        
	        String nombreDocumento = ""; //Nombre del documento con extensión y todo
	        File fileDocumento = null;
	        
	        if(asunto == null){
	        	asunto = "";
	        }
	        if(contenido == null){
	        	contenido = "";
	        }
	        if(nombreNotif == null){
	        	nombreNotif = "";
	        }
	        	        
			//Recuperamos el adjunto
	        if(conDocumento){
	        	IItemCollection documentosCollection = entitiesAPI.getDocuments(numexp, DocumentosUtil.ID_TRAMITE + " = '" + tramiteId + "'", "");
				Iterator<?> documentosIterator = documentosCollection.iterator();
				
				if(documentosIterator.hasNext()){
					IItem documento = (IItem) documentosIterator.next();
					String infoPag = documento.getString(DocumentosUtil.INFOPAG_RDE);
					String extension = documento.getString(DocumentosUtil.EXTENSION_RDE);
					
					if(StringUtils.isEmpty(infoPag)){
						infoPag = documento.getString(DocumentosUtil.INFOPAG);
						extension = documento.getString(DocumentosUtil.EXTENSION);						
					}
					nombreDocumento = documento.getString(DocumentosUtil.DESCRIPCION);// + "." + extension;
					tipoDocumento = documento.getString(DocumentosUtil.NOMBRE);
					
					if(StringUtils.isEmpty(nombreDocumento)){
						nombreDocumento = documento.getString(DocumentosUtil.NOMBRE);// + "." + extension;
					}
					
					fileDocumento = DocumentosUtil.getFile(cct, infoPag, nombreDocumento, extension);
					
					Date faprobacionDate = documento.getDate(DocumentosUtil.FAPROBACION);
					if(faprobacionDate != null){
						String faprobacion = (new SimpleDateFormat(FORMATO_FECHA_DD_MM_YYYY)).format(faprobacionDate);
						if(faprobacion == null) faprobacion = "";
						contenido += "Fecha de aprobación: <b>" + faprobacion + "</b>. \n";
					}
				} else {
					throw new ISPACRuleException("No se ha podido recuperar ningún documento del tipo: "+ tipoDocumento);
				}
	        }
					
			try{
				IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
				contenido = contenido
						+ "<br/>"
						+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Expediente: <b>" + rulectx.getNumExp() + "</b>."
						+ "<br/>"
						+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Asunto: <b>" + expediente.getString("ASUNTO") + "</b>."
						+ "<br/>";
				
				contenido = "<img src='cid:escudo' width='200px'/>"
		        		+ "<p align=justify>"
		        		+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Estimado señor/a:" 
		        		+ "<br/> <br/>" 
		        		+ MailUtil.ESPACIADO_PRIMERA_LINEA +  contenido
		        		+ " </p>"
		        		+ "<br/> <br/>";
		        		
				asunto +=  expediente.getString(ExpedientesUtil.NOMBREPROCEDIMIENTO);
			} catch(Exception e){
				throw new ISPACRuleException("Error al recuperar el expediente: " + numexp, e);
			}				
    			
	        String emails = ConfigurationMgr.getVarGlobal(cct, VAR_EMAILS);
	        if(StringUtils.isEmpty(emails)){
	        	emails = VAR_EMAILS;
	        }
	        
	        if(StringUtils.isNotEmpty(emails)){
	        	emails = emails.replaceAll("#", MailUtil.DEFAULT_MAIL_SEPARATOR);
	        	try{
	        		MailUtil.enviarCorreoVarios(rulectx, emails, asunto, contenido, false, fileDocumento, imagenes);
	        	} catch(Exception e){
		        	String descripError =  "No se ha podido recuperar los correos electrónicos de la Variable de Sistema: " + VAR_EMAILS;
					DipucrCommonFunctions.insertarAcuseEmail(nombreNotif, new Date(), nombreDocumento, nombreDocumento, false, "", descripError, rulectx);
					LOGGER.error(descripError, e);
					throw new ISPACRuleException(descripError);
		        }
	        } else {	        	
	        	String descripError =  "No existe ningún participante para trasladar el documento.";
				DipucrCommonFunctions.insertarAcuseEmail(nombreNotif, new Date(), nombreDocumento, nombreDocumento, false, "", descripError, rulectx);
				LOGGER.error(descripError);
				throw new ISPACRuleException(descripError);
	        }
	        
	        //[Dipucr-Manu Ticket #327] INICIO - ALSIGM3 Cuando se envía documento por correo se deja el documento abandonado en la carpeta de temporales.
        	if(null != fileDocumento && fileDocumento.exists()){
        		fileDocumento.delete();
        	}
	        //[Dipucr-Manu Ticket #327] FIN - ALSIGM3 Cuando se envía documento por correo se deja el documento abandonado en la carpeta de temporales.

        	LOGGER.info("FIN - " + this.getClass().getName());
        	return true;
    		
        } catch(Exception e) {
        	LOGGER.error("Error al enviar email a: " + nombreNotif + ", en el expediente: " + rulectx.getNumExp() + ". "+ e.getMessage(), e);
        	if (e instanceof ISPACRuleException){
        		throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {		
	}
}
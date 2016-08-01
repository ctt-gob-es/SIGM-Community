package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.mensajes_cortos.ServicioMensajesCortos;
import ieci.tecdoc.sgm.core.services.mensajes_cortos.dto.Attachment;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.StringTokenizer;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class TrasladarRemisionDocumentosRule implements IRule {

	/**
	 * CONSTANTES con los textos de los correos	
	 */
	private static final String EMAIL_FROM_VAR_NAME = "AVISO_FIRMANTE_EMAIL_FROM";
	private static final String EMAIL_SUBJECT_VAR_NAME = "REMISION_DOCUMENTOS_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = "REMISION_DOCUMENTOS_EMAIL_CONTENT";
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		/**
		 * Variables que se utilizarán para insertar en la bbdd los datos 
		 * sobre el envío correcto o incorrecto del email.
		 * */
		String nombreNotif = "";
		Date fechaEnvío = null;
		String nombreDoc = "";
		String descripcionDoc = "";
		boolean enviadoEmail = false;
		String emailNotif = "";
		String descripError = "";
		try{
			
			//APIs
			//*******************************
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			//*******************************
			
			// 1. Obtener Participantes del expediente actual, con relación "Trasladado"
			String numexp = rulectx.getNumExp();
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numexp,  "ROL= 'TRAS'", "ID");
			
			if (participantes == null || participantes.toList().size() == 0){
				throw new ISPACRuleException("No se pueden enviar los traslados: No se han definido trasladados en la pestaña participantes.");
			}
			else{ //Hay participantes
				
				// 2. Obtener el documento decreto para anexarlo al email 
				int taskId = rulectx.getTaskId();
				String sqlQueryDoc = "ID_TRAMITE = " + taskId + "";
				IItemCollection documentos = entitiesAPI.getDocuments(numexp, sqlQueryDoc, "");
				
				if (documentos == null || documentos.toList().size()==0){
					throw new ISPACRuleException("No se pueden enviar los traslados: No hay ningún documento firmado en el trámite.");
				}else if (documentos.toList().size() > 1 ) {
					throw new ISPACRuleException("No se pueden enviar los traslados: Hay más de un documento anexado al trámite.");
				}else if (documentos.toList().size() == 1) {
					
					IItem doc = (IItem)documentos.iterator().next();
					if(!doc.get("ESTADOFIRMA").equals("04")){
						
						// 4. Enviar email con el decreto adjunto
						//Contenido y asunto
						String from = ConfigurationMgr.getVarGlobal(cct, EMAIL_FROM_VAR_NAME);
						String cAsunto = ConfigurationMgr.getVarGlobal(cct, EMAIL_SUBJECT_VAR_NAME);
						String cContenido = ConfigurationMgr.getVarGlobal(cct, EMAIL_CONTENT_VAR_NAME);
						
						// Fichero a adjuntar
						//IItem doc = (IItem)documentos.iterator().next();
						String infoPagRde = doc.getString("INFOPAG_RDE");
//						File file = this.getFile(rulectx,taskId,numexp,infoPagRde, nombreFichero);
						File file = DocumentosUtil.getFile(cct, infoPagRde, null, null);
						
						// Para cada participante seleccionado --> enviar email
						for (int i=0; i<participantes.toList().size(); i++){
							
							IItem participante = (IItem) participantes.toList().get(i);
							nombreNotif = participante.getString("NOMBRE");
							emailNotif = participante.getString("DIRECCIONTELEMATICA");
							nombreDoc = doc.getString("NOMBRE");
							descripcionDoc = doc.getString("DESCRIPCION");
							
							if (emailNotif != null){
								StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
	
								while (tokens.hasMoreTokens()) {
									
									String cCorreoDestino = tokens.nextToken();	
					
									if (StringUtils.isEmpty(cCorreoDestino)){
										rulectx.setInfoMessage("El participante -" + nombreNotif + "- no tiene correo"
												+ " electrónico definido. No se le enviará la Remisión de Informe.");
									}
									else{
				        	
							        	// Envío del email
//										MailUtil.enviarCorreo(rulectx, cCorreoDestino, cAsunto, cContenido, file);
										ServicioMensajesCortos svc = LocalizadorServicios.getServicioMensajesCortos();
										Attachment attachment = new Attachment();
										//Nombre del attachment
										attachment.setFileName("Remisión Informe.pdf");
										//Contenido del attachment										
										byte[] fileBArray = new byte[(int)file.length()];
										FileInputStream fis = new FileInputStream(file);
										fis.read(fileBArray);
										fis.close();
										attachment.setContent(fileBArray);
										//Enviamos el mail
										svc.sendMail(from, new String[] { cCorreoDestino }, null, null, cAsunto, 
												cContenido, new Attachment[] { attachment });
										
										// Acuse de recibo
										DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
								    }
								}
							}
							else{
								rulectx.setInfoMessage("El participante -" + nombreNotif + "- no tiene correo"
										+ " electrónico definido. No se le enviará el libro de extractos.");
							}
						}
						file = null;
					}
				}
			}
			return null;
			
		} catch(Exception e) {
        	throw new ISPACRuleException(e);
        }
	}
	
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

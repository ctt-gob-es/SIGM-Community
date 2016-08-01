package es.dipucr.sigem.api.rule.procedures.secretaria;

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
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class CierreConvocatoriaRule implements IRule {
	

	protected static final Logger logger = Logger.getLogger(CierreConvocatoriaRule.class);
	/**
	 * Variables que se utilizarán para insertar en la bbdd los datos 
	 * sobre el envío correcto o incorrecto del email.
	 * */
	String nombreNotif = "";
	Date fechaEnvío = null;
	String nombreDoc = "";
	boolean enviadoEmail = false;
	String emailNotif = "";
	String descripError = "";
	String descripcionDoc = "";
		
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //Comprobar que el documento esté firmado
			// --> Está ya hecho en ValidateFirmaConvocatoriaRule, ejecutada antes que esta regla

			//Obtener Participantes de la sesion
	        logger.warn("Cierre convocatoria. Inicio");
			String strNumExp = rulectx.getNumExp();
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, strNumExp, "", "ID");
			
			//Obtener el documento Certificado de acuerdos para anexarlo al email 
	        logger.warn("Cierre convocatoria. Obtener documento");
			int taskId = rulectx.getTaskId();
			String sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA IN ('02','03')";
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");

			if (documentos.toList().size() == 1) 
			{
				//Enviar email con la convocatoria adjunta

				// Fichero a adjuntar
				IItem doc = (IItem)documentos.iterator().next();
				String infoPag = doc.getString("INFOPAG_RDE");
				File file = getFile(rulectx,taskId,strNumExp,infoPag);
				
				// Para cada participante seleccionado enviar email
		        logger.warn("Cierre convocatoria. Recorrer participantes");
		     // Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
				if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
					for (int i=0; i<participantes.toList().size(); i++)
					{
						IItem participante = (IItem) participantes.toList().get(i);
						if (participante!=null)
						{
							logger.warn("Cierre convocatoria. Participante: " + participante.getString("NOMBRE"));
							nombreNotif = participante.getString("NOMBRE");
							emailNotif = participante.getString("DIRECCIONTELEMATICA");
							/**
							 * INICIO
							 * ##Ticket #147 SIGEM Documento que especifique a los notificados.
							* **/
							nombreDoc = doc.getString("NOMBRE");
							descripcionDoc = doc.getString("DESCRIPCION");
							/**
							 * FIN
							 * ##Ticket #147 SIGEM Documento que especifique a los notificados.
							* **/
							if (emailNotif != null)
							{
								StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
								while (tokens.hasMoreTokens()) 
								{
									String strDestinatario = tokens.nextToken();	
							        logger.warn("Cierre convocatoria. email: " + strDestinatario);
									if (participante!=null)
									{
							        	if (!strDestinatario.equals("")) 
							        	{
									        logger.warn("Enviar correo...");
							        		EnviarCorreo(rulectx,strDestinatario,file, entitiesAPI,cct);
							        		/**
							 				* INICIO
											* ##Ticket #147 SIGEM Documento que especifique a los notificados.
											* **/
							        		// Eliminar el fichero temporal una vez enviado por correo
											DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
									        /**
							 				* FIN
											* ##Ticket #147 SIGEM Documento que especifique a los notificados.
											* **/
									        logger.warn("Enviado");
								        }
							        	else{
											descripError = " El email esta vacío";
											enviadoEmail = false;
										}
								    }
								}
							}
							else{
								descripError = " El email esta vacío";
								enviadoEmail = false;
							}							
						}						
					}
				}
				else{
					descripError = " No existen participantes para este documento.";
					enviadoEmail = false;
				}
				
				file.delete();
			}
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido cerrar la convocatoria",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private void EnviarCorreo(IRuleContext rulectx, String strTo, File attachment, IEntitiesAPI entitiesAPI, ClientContext cct) throws ISPACException{
		try{
			//Se cambia la configuración del correo de ApplicationResources.properties a su archivo de configuración por entidad
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
			String cCorreoOrigen = correoConfig.get(CorreoConfiguration.CONV_FROM);
			
			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");

			String nombreImagen = ConfigurationMgr.getVarGlobal(cct, "SECRETARIA_EMAIL_NOMBRE_IMAGEN_CABECERA");
			Object[] imagen = {rutaImg, new Boolean(true), nombreImagen, "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
        	
			//String strContenido = getContenido(rulectx);
			IItem sesion = getSesion(rulectx);
			SimpleDateFormat dateformat1 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
			String strFecha = "";
			if(sesion.getDate("FECHA")!=null){
				strFecha = dateformat1.format(sesion.getDate("FECHA"));
			}
			else{
				descripError = "Falta por introducir la fecha y hora de la sesión";
			}
	        
			
			//Inserto los parametros del asunto
	        Map<String, String> variables = new HashMap<String, String>();
	        //String area =SecretariaUtil.getNombreAreaSesion(rulectx, rulectx.getNumExp());
	        String sesionOrgaCole = SecretariaUtil.getNombreOrganoSesion(rulectx, rulectx.getNumExp());
	        //Quitado porque queria Luis que apareciera sólo el nombre de COMISIÓN INFORMATIVA
	        //y el área de la comisión
	        //if(sesionOrgaCole.equals("COMISIÓN INFORMATIVA")){
	        	//sesionOrgaCole += " DE "+ area.toUpperCase();
	        //}
	        variables.put("SESION", sesionOrgaCole);
	        variables.put("NUMSESION", sesion.getString("NUMCONV"));
	        variables.put("FECHASESION", strFecha);
	        variables.put("HORASESION", sesion.getString("HORA"));
	        
	        String subject = ConfigurationMgr.getVarGlobal(cct, "SECRETARIA_EMAIL_DI_SUBJECT");
	        String content = ConfigurationMgr.getVarGlobal(cct, "SECRETARIA_EMAIL_DI_CONTENT");
	        if (StringUtils.isNotBlank(subject)) {
				subject = StringUtils.replaceVariables(subject, variables);
			}
	        if (StringUtils.isNotBlank(content)) {
				content = StringUtils.replaceVariables(content, variables);
			}
	        
	        if (StringUtils.isNotEmpty(strTo)){
				String dir[]= MailUtil.enviarCorreo(rulectx, cCorreoOrigen, strTo, subject, content, attachment, imagenes);

	       		String error = "";
				if (dir != null) 
				{
					for (int nI = 0; nI < dir.length; nI++)
					{
						error = error + '\r' + dir[nI];
					}
					descripError = "Error en el envío a '" + strTo + " - "+error+". ";
					enviadoEmail = false;
					DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
					
					throw new ISPACRuleException(error);
				}
				else{
					enviadoEmail = true;
					fechaEnvío = new Date();
				}
			} 

		}
		catch(ISPACException e){
			if(e.getCause() instanceof SendFailedException){
				descripError = "Error en el envío a '" + strTo + "'. ";
			}
			else if(e.getCause() instanceof AddressException){
				descripError = "Error en la dirección de correo '" + strTo + "'. ";				
			}
			else{
				if(StringUtils.isEmpty(descripError)){
					descripError = "Error al enviar el correo electrónico a '" + strTo + "'. ";
				}				
			}
			enviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
			throw new ISPACRuleException(descripError ,e);

		}	
		catch(Exception e) {
			descripError = "Error al enviar el correo electrónico de Convocatoria a '" + strTo + "'. ";
			enviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
						
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(descripError ,e);
        }
	}
	
	private IItem getSesion(IRuleContext rulectx) throws ISPACException{
		IItem iSesion = null;
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
		IItemCollection itemCollection = entitiesAPI.queryEntities("SECR_SESION", strQuery);
		Iterator<?> it = itemCollection.iterator();
		if (it.hasNext())
		{
			iSesion = (IItem)it.next();
		}
		return iSesion;
	}

	private File getFile(IRuleContext rulectx, int taskId, String numExp, String infoPag) throws ISPACException{
		
		// API
		IGenDocAPI gendocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();

		Object connectorSession = null;
		try {
			connectorSession = gendocAPI.createConnectorSession();
			File file = null;
			try{
				String extension = "pdf";
				
				//Se almacena documento
				//String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
				IItem sesion = getSesion(rulectx);
				String organo = getOrgano (rulectx);
				String numconv = sesion.getString("NUMCONV");
				String [] vNum = numconv.split("/");
				String nombreFichero = organo+"-"+"Convocatoria"+"-"+vNum[0]+"-"+vNum[1]+"."+extension;
				String fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + nombreFichero;
				
				OutputStream out = new FileOutputStream(fileName);
				gendocAPI.getDocument(connectorSession, infoPag, out);
								
				file = new File(fileName);
			
				return file;
			} catch (FileNotFoundException e) {
				throw new ISPACInfo("Error al intentar obtener el documento, no existe.", e);
			}
		}finally {
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
    	}
	} 
	
	/**
	*INICIO
	* [Teresa]Ticket #173#Secretaria Cambiar el asunto y el 
	*contenido de los email que se envían
	 * @throws ISPACRuleException 
	**/
	private String getOrgano (IRuleContext rulectx) throws ISPACRuleException{
		String organo = "";
		try {
			organo = SecretariaUtil.getOrgano(rulectx);
			if(organo.equals("MESA")){
				organo = "Mesa de Contratación";
			}
			if(organo.equals("JGOB")){
				organo = "Junta de Gobierno";
			}
			if(organo.equals("PLEN")){
				organo = "Pleno";
			}
			if(organo.equals("COMI")){
				String area;
				area = SecretariaUtil.getNombreAreaSesion(rulectx, rulectx.getNumExp());
				organo = "Comisión Informativa Permanente de "+area;
			}
		} catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return organo;
	}        
}
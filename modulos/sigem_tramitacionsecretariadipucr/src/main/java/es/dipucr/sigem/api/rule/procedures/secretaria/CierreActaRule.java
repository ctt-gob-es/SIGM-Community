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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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

public class CierreActaRule implements IRule {
	
	protected static final Logger logger = Logger.getLogger(CierreConvocatoriaRule.class);
	/**
	 * Variables que se utilizarán para insertar en la bbdd los datos 
	 * sobre el envío correcto o incorrecto del email.
	 * */
	
	private String nombreNotif = "";
	private Date fechaEnvío = null;
	private String nombreDoc = "";
	private boolean enviadoEmail = false;
	private String emailNotif = "";
	private String descripError = "";
	private String descripcionDoc = "";
	
	
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
			// --> Está ya hecho en ValidateFirmaActaRule, ejecutada antes que esta regla

			//Obtener Participantes de la sesion
	        logger.warn("Cierre acta. Inicio");
			String strNumExp = rulectx.getNumExp();
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, strNumExp, "", "ID");
			
			//Obtener el borrador de acta para anexarlo al email 
	        logger.warn("Cierre acta. Obtener documento");
			int taskId = rulectx.getTaskId();
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA IN ('02','03')", "");
			
			Iterator<?> iteDoc = documentos.iterator();
			while (iteDoc.hasNext()) 
			{
				//Enviar email por cada documento que haya firmado en el acta

				// Fichero a adjuntar
				IItem doc = (IItem)iteDoc.next();
				String infoPag = doc.getString("INFOPAG_RDE");
				File file = getFile(rulectx,taskId,strNumExp,infoPag);
				
				// Para cada participante seleccionado enviar email
		        logger.warn("Cierre acta. Recorrer participantes");
		     // Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
				if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
					for (int i=0; i<participantes.toList().size(); i++)
					{
			
						IItem participante = (IItem) participantes.toList().get(i);
						if (participante!=null)
						{
							logger.warn("Cierre acta. Participante: " + participante.getString("NOMBRE"));
							nombreNotif = participante.getString("NOMBRE");
							emailNotif = participante.getString("DIRECCIONTELEMATICA");
							nombreDoc = doc.getString("NOMBRE");
							descripcionDoc = doc.getString("DESCRIPCION");
							if (emailNotif != null)
							{
								StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
								while (tokens.hasMoreTokens()) 
								{
									String strDestinatario = tokens.nextToken();	
							        logger.warn("Cierre acta. email: " + strDestinatario);
									if (participante!=null)
									{
							        	if (!strDestinatario.equals("")) 
							        	{
									        logger.warn("Enviar correo...");
							        		EnviarCorreo(rulectx,strDestinatario,file, entitiesAPI,cct);
									        logger.warn("Enviado");// Eliminar el fichero temporal una vez enviado por correo
											DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
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
				
				
				// Eliminar el fichero temporal una vez enviado por correo
				file.delete();
			}
//			else{
//				logger.warn("No existe ningun documento a enviar "+sqlQueryDoc);
//			}
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido cerrar el borrador de acta",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private void EnviarCorreo(IRuleContext rulectx, String strTo, File attachment, IEntitiesAPI entitiesAPI, ClientContext cct) throws ISPACException{
		try{
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());

			String strFrom = correoConfig.get(CorreoConfiguration.CONV_FROM);
        	
			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");

        	Object[] imagen = {rutaImg, new Boolean(true), "logoCabeceraPatrimonio.bmp", "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
			
			String strContenido = getContenido(rulectx);
			/**
			*INICIO
			* [Teresa]Ticket #173#Secretaria Cambiar el asunto y el 
			*contenido de los email que se envían
			**/
			//String strAsunto = "[SIGEM] Borrador de acta de la sesión correspondiente a la convocatoria Nº"+getSesion(rulectx).getString("NUMCONV");
			
			String organo = getOrgano (rulectx);
			
			String strAsunto = "[SIGEM] Borrador del Acta de la sesión correspondiente a la "+organo+ " de la convocatoría Nº"+getSesion(rulectx).getString("NUMCONV");

			/**
			*FIN
			* [Teresa]Ticket #173#Secretaria Cambiar el asunto y el 
			*contenido de los email que se envían
			**/
			if (StringUtils.isNotEmpty(strTo)){
				logger.warn("Enviar correo...");
				String dir[]= MailUtil.enviarCorreo(rulectx, strFrom, strTo, strAsunto, strContenido, attachment, imagenes);
				
				// Enviar email
				logger.warn("Enviado");
				String error = "";
				if (dir != null){
					for (int nI = 0; nI < dir.length; nI++){
						error = error + '\r' + dir[nI];
					}
					descripError = error;
					enviadoEmail = false;
					DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
					throw new ISPACRuleException(error);
				}
				else{
					enviadoEmail = true;
					fechaEnvío = new Date();
				}
			} 
			else{
				descripError = 
					"No es posible enviar el correo electrónico con el borrador de acta a '" + strTo + "'" +
					"Por favor, póngase en contacto con el administrador del sistema";
				enviadoEmail = false;
				DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
				throw new ISPACInfo(descripError);
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
				descripError = "Error al enviar el correo electrónico a '" + strTo + "'. ";
			}
			enviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
			throw new ISPACRuleException(descripError ,e);

		}
		catch(Exception e){
			descripError = "Error al enviar el correo electrónico con el borrador de acta a '" + strTo + "'. ";
			enviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(descripError ,e);
        }
	}
	
	/**
			*INICIO
			* [Teresa]Ticket #173#Secretaria Cambiar el asunto y el 
			*contenido de los email que se envían
			**/
	private String getOrgano (IRuleContext rulectx){
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
				//String area = SecretariaUtil.getNombreAreaSesion(rulectx, rulectx.getNumExp());
					//organo = "Comisión Informativa Permanente de "+area;
				organo = "Comisión Informativa";
			}
		} catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
		}
		return organo;
	}
	
	/**
	*FIN
	* [Teresa]Ticket #173#Secretaria Cambiar el asunto y el 
	*contenido de los email que se envían
	**/
	private IItem getSesion(IRuleContext rulectx) throws ISPACException{
		IItem iSesion = null;
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
		IItemCollection itemCollection = entitiesAPI.queryEntities("SECR_SESION", strQuery);
		Iterator<?> it = itemCollection.iterator();
		if (it.hasNext()){
			iSesion = (IItem)it.next();
		}
		return iSesion;
	}

	private String getContenido(IRuleContext rulectx) throws ISPACException{
	/**
	*INICIO
	* [Teresa]Ticket #173#Secretaria Cambiar el asunto y el 
	*contenido de los email que se envían
	**/
		String organo = getOrgano (rulectx);
		String strContenido = "<img src='cid:escudo' width='200px'/>"
				+ "<p align=justify>"
				+ "&nbsp;&nbsp;&nbsp;Adjunto se envía el borrador del Acta correspondiente a la sesión celebrada, el día <b>";
		
		/**
		*FIN
		* [Teresa]Ticket #173#Secretaria Cambiar el asunto y el 
		*contenido de los email que se envían
		**/
		IItem iSesion = getSesion(rulectx);
		if (iSesion != null){
			Date dFecha = iSesion.getDate("FECHA");
			if (dFecha==null){
				throw new ISPACInfo("Error: No se ha introducido la fecha de la sesión.");				
			}
	        SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
	        String strFecha = dateformat.format(dFecha);
	        String strHora = iSesion.getString("HORA");
	        String cadenaTexto = "este";
	        if(EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()).equals("005")){
	        	cadenaTexto = "esta";
	        }
	        strContenido += strFecha+"</b> a las <b>"+strHora+" horas</b>, por <b>"+organo+" </b>de "+cadenaTexto+" <b>"+EntidadesAdmUtil.obtenerNombreLargoEntidadById(rulectx.getClientContext())+"</b>.</p>";
	        //strContenido += " Nº"+numConv+" para la sesión "+strTipo+" con fecha "+strFecha + " a las "+strHora+" horas.";
	        
		}
		else{
			throw new ISPACInfo("Se ha producido un error al obtener los datos de la sesión.");
		}
		return strContenido;
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
				String organo = getOrgano (rulectx);
				IItem sesion = getSesion(rulectx);
				String numconv = sesion.getString("NUMCONV");
				String [] vNum = numconv.split("/");
				String nombreFichero = organo+"-"+"Acta"+"-"+vNum[0]+"-"+vNum[1]+"."+extension;

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
}
package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
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

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class EnvioMailTesoreriaRule implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(EnvioMailTesoreriaRule.class);
	
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
	private static final String EMAIL_NOTIF_TESORERIA = "EMAIL_NOTIF_TESORERIA";
	private static final String NOMBRE_NOTIF_TESORERIA = "NOMBRE_NOTIF_TESORERIA";
	
	
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
			//Obtener el documento Certificado de asistencia para anexarlo al email 
			int taskId = rulectx.getTaskId();
			String sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA = '02' AND UPPER(NOMBRE) LIKE '%"+Constants.SECRETARIAPROC.docAsistencia+"%'";
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");

			if (documentos.toList().size() == 1) 
			{
				//Enviar email con la convocatoria adjunta

				// Fichero a adjuntar
				IItem doc = (IItem)documentos.iterator().next();
				String infoPag = doc.getString("INFOPAG_RDE");
				File file = DocumentosUtil.getFile(cct, infoPag, null, null);
				
				//Se adjunta la direccion de la persona de tesoreria a la que hay que mandarle el email
				//Maria del Carmen Villaverde Menchen TESORERIA
				emailNotif = ConfigurationMgr.getVarGlobal(cct, EMAIL_NOTIF_TESORERIA);
		        nombreNotif = ConfigurationMgr.getVarGlobal(cct, NOMBRE_NOTIF_TESORERIA);
				nombreDoc = "Certificación de asistencia";
				
				
				
        		EnviarCorreo(rulectx,emailNotif,file, entitiesAPI,cct);
        		
        		DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, nombreDoc, enviadoEmail, emailNotif, descripError, rulectx);

				// Eliminar el fichero temporal una vez enviado por correo
				file.delete();
			}
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	logger.error("No se ha podido cerrar la convocatoria. " +e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido cerrar la convocatoria. " +e.getMessage(), e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private void EnviarCorreo(IRuleContext rulectx, String strTo, File attachment, IEntitiesAPI entitiesAPI, ClientContext cct) throws ISPACException 
	{
		try
		{
			
			/**
			 * [Ticket 1222 Teresa] Sacar la configuración del correo a los ficheros de configuracion conf/SIGEMTramitacion
			 * INICIO
			 * **/
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
			
			String strHost = correoConfig.get(CorreoConfiguration.HOST_MAIL);
			String strPort = correoConfig.get(CorreoConfiguration.PORT_MAIL);
			String strFrom = correoConfig.get(CorreoConfiguration.CONV_FROM);
			String strLocation = correoConfig.get(CorreoConfiguration.CONV_LUGAR);
			
			//String strHost = Messages.getString("cHost_mail");
			//String strPort = Messages.getString("cPort_mail");
			//String strFrom = Messages.getString("cConv_From");
			//String strLocation = Messages.getString("cConv_Lugar");
			
			/**
			 * FIN
			 * **/			
			
			String strContenido = getContenido(rulectx);
			String strAsunto = "[SIGEM] Certificado de Asistencia Nº"+getSesion(rulectx).getString("NUMCONV");
			
			String strDtStart = getDtStart(rulectx);
			String strDtEnd = strDtStart;
	        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("es"));
			String strDtStamp = dateformat.format(new Date());	

			// Configurar correo
	        Properties props = new Properties();
	        props.put("mail.transport.protocol", "smtp");
	        props.put("mail.smtp.host", strHost);
	        props.put("mail.smtp.port", strPort);
	        props.put("mail.smtp.auth", "true");
	        Authenticator auth = new SMTPAuthenticator();
	        Session mailSession = Session.getDefaultInstance(props, auth);
	        //mailSession.setDebug(true);
	        Transport transport = mailSession.getTransport();

			// Definir mensaje
			MimeMessage message = new MimeMessage(mailSession);
			message.addHeaderLine("method=REQUEST");
			//message.addHeaderLine("charset=UTF-8");
			message.addHeaderLine("component=VEVENT");
			message.setFrom(new InternetAddress(strFrom));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(strTo));
			message.setSubject(strAsunto);

			// Fichero iCalencar (ics)
			StringBuffer sb = new StringBuffer();
			StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n"+
			"PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"+ 
			"VERSION:2.0\n" +
			"METHOD:REQUEST\n" +
			"BEGIN:VEVENT\n" +
			"ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+strTo+"\n" +
			"ORGANIZER:MAILTO:"+strFrom+"\n" +
			"DTSTART:"+strDtStart+"\n" +
			"DTEND:"+strDtEnd+"\n" +
			"LOCATION:"+strLocation+"\n" +
			"TRANSP:OPAQUE\n" +
			"SEQUENCE:0\n" +
			"UID:"+strDtStamp+"-"+strDtStart+"@"+strHost+"\n" +
			"DTSTAMP:"+strDtStamp+"\n" +
			//"CATEGORIES:Meeting\n" +
			"DESCRIPTION:"+strContenido+"\n\n" +
			"SUMMARY:"+strAsunto+"\n" +
			"PRIORITY:5\n" +
			"CLASS:PUBLIC\n" +
			//"BEGIN:VALARM\n" +
			//"TRIGGER:PT1440M\n" +
			//"ACTION:DISPLAY\n" +
			//"DESCRIPTION:Reminder\n" +
			//"END:VALARM\n" +
			"END:VEVENT\n" +
			"END:VCALENDAR");

			// Crear la parte con el texto y la cita 
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
			messageBodyPart.setHeader("Content-ID","calendar_message");
			messageBodyPart.setDataHandler(new DataHandler(
				new ByteArrayDataSource(buffer.toString(), "text/calendar")));
				
			// Crear la parte con el fichero adjunto
			MimeBodyPart attachFilePart = null;
			if (attachment != null)
			{
				attachFilePart = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(attachment);
				attachFilePart.setDataHandler(new DataHandler(fds));
				attachFilePart.setFileName("Certificado de Asistencia.pdf");
			}

			// Crear un Multipart y añadirlo al mensaje 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			if (attachment!=null) multipart.addBodyPart(attachFilePart);
			message.setContent(multipart);

			// Envío del mensaje
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
	        enviadoEmail = true;
			fechaEnvío = new Date();
		}
		catch(SendFailedException e)
		{
			descripError = "Error en el envío a '" + strTo + "'. ";
			enviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, nombreDoc, enviadoEmail, emailNotif, descripError, rulectx);
			throw new ISPACRuleException(descripError ,e);

		}
		catch(AddressException e)
		{
			descripError = "Error en la dirección de correo '" + strTo + "'. ";
			enviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, nombreDoc, enviadoEmail, emailNotif, descripError, rulectx);
        	throw new ISPACRuleException(descripError ,e);
		}
		catch(Exception e) {
			descripError = "Error al enviar el correo electrónico de Certificado de Asistencia a '" + strTo + "'. ";
			enviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvío, nombreDoc, nombreDoc, enviadoEmail, emailNotif, descripError, rulectx);
						
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(descripError ,e);
        }
	}
	
	@SuppressWarnings("rawtypes")
	private IItem getSesion(IRuleContext rulectx) throws ISPACException 
	{
		IItem iSesion = null;
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
		IItemCollection itemCollection = entitiesAPI.queryEntities("SECR_SESION", strQuery);
		Iterator it = itemCollection.iterator();
		if (it.hasNext())
		{
			iSesion = (IItem)it.next();
		}
		return iSesion;
	}

	private String getContenido(IRuleContext rulectx) throws ISPACException 
	{
		String strContenido = "Adjunto se envía la convocatoria";
		IItem iSesion = getSesion(rulectx);
		if (iSesion != null)
		{
			String numConv = iSesion.getString("NUMCONV");
			String strTipo = getTipoSesion(rulectx, iSesion);
			Date dFecha = iSesion.getDate("FECHA");
			if (dFecha==null)
			{
				throw new ISPACInfo("Error: No se ha introducido la fecha de la sesión.");				
			}
	        SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
	        String strFecha = dateformat.format(dFecha);
	        String strHora = iSesion.getString("HORA");
	        strContenido += " Nº"+numConv+" para la sesión "+strTipo+" con fecha "+strFecha + " a las "+strHora+" horas.";
		}
		else
		{
			throw new ISPACInfo("Se ha producido un error al obtener los datos de la sesión.");
		}
		return strContenido;
	}

	private String getDtStart(IRuleContext rulectx)  throws ISPACException 
	{
		String strDtStart = "";
		IItem iSesion = getSesion(rulectx);
		if (iSesion != null)
		{
			//Fecha
			Date dFecha = iSesion.getDate("FECHA");
	        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd", new Locale("es"));
	        strDtStart = dateformat.format(dFecha);

	        //Hora
	        boolean ok;
	        String strPartHora = "T";
	        String strHora = iSesion.getString("HORA");
			StringTokenizer tokens = new StringTokenizer(strHora, ".:',");
			DecimalFormat df = new DecimalFormat("00");

			//Dígitos de hora
			ok = false;
			if (tokens.hasMoreTokens())
			{
				String strPart = tokens.nextToken();
				int nPart = Integer.parseInt(strPart);
				if (nPart >= 0 && nPart <= 23) 
				{
					strPartHora += df.format(nPart);
					ok = true;
				}
			}
			if (!ok)
			{
				strPartHora += "10"; //Por defecto las 10 horas
			}
			
			//Dígitos de minutos
			ok = false;
			if (tokens.hasMoreTokens())
			{
				String strPart = tokens.nextToken();
				int nPart = Integer.parseInt(strPart);
				if (nPart >= 0 && nPart <= 60)
				{
					strPartHora += df.format(nPart);
					ok = true;
				}
			}
			if (!ok)
			{
				strPartHora += "00"; //Por defecto 00 minutos
			}
			
			//Dígitos de segundos
			ok = false;
			if (tokens.hasMoreTokens())
			{
				String strPart = tokens.nextToken();
				int nPart = Integer.parseInt(strPart);
				if (nPart >= 0 && nPart <= 60)
				{
					strPartHora += df.format(nPart);
					ok = true;
				}
			}
			if (!ok)
			{
				strPartHora += "00"; //Por defecto 00 segundos
			}
			
			strDtStart += strPartHora;
		}
		else
		{
			throw new ISPACInfo("Se ha producido un error al obtener la fecha.");
		}
		return strDtStart;
	}

	@SuppressWarnings("rawtypes")
	private String getTipoSesion(IRuleContext rulectx, IItem sesion)  throws ISPACException
	{
		String tipo = "";
		try
		{
			String strTipo = sesion.getString("TIPO"); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR='" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_TIPOSESION", strQuery);
	        Iterator it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return tipo;
	}
	
	private static class SMTPAuthenticator extends javax.mail.Authenticator {
    	public PasswordAuthentication getPasswordAuthentication() {
        	//String username = Messages.getString("cUsr_mail");
        	//String password = Messages.getString("cPwd_mail");
        	
        	CorreoConfiguration correoConfig;
        	String username =  "";
        	String password =  "";
			try {
				correoConfig = CorreoConfiguration.getInstance(new ClientContext());
				username =  correoConfig.get(CorreoConfiguration.USR_MAIL);
				password =  correoConfig.get(CorreoConfiguration.PWD_MAIL);
			} catch (ISPACRuleException e) {
				logger.error("Error al obtener el usuario y la contraseña"+ e.getMessage(), e);
			}

			
        	
        	return new PasswordAuthentication(username, password);
        }
    }
        

}

package es.dipucr.contratacion.rule;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.MailUtil;


public class TrasladarSubsanacionInforme  implements IRule {
	
	
	private static final Logger logger = Logger.getLogger(TrasladarSubsanacionInforme.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //---------------------------------------------------------------------------------------------- 
	
			String sqlQueryDoc = "NOMBRE = 'Subsanación del Informe' AND ID_TRAMITE="+rulectx.getTaskId()+" AND INFOPAG_RDE != ''";
			logger.warn("sqlQueryDoc "+sqlQueryDoc);
	
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");
			
			logger.warn("documentos.toList().size()."+documentos.toList().size());
			
			if (documentos.toList().size() == 0){
				rulectx.setInfoMessage("No existe ninguna Subsanación del Informe firmada");
				return false;
			}
			else{
				return true;
			}
				
		}catch(Exception e) 
			{
	        	if (e instanceof ISPACRuleException)
	        	{
				    throw new ISPACRuleException(e);
	        	}
	        	throw new ISPACRuleException(e);
	        }
			
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		try
		{
			/**
			 * Variables que se utilizarán para insertar en la bbdd los datos 
			 * sobre el envío correcto o incorrecto del email.
			 * */

			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //---------------------------------------------------------------------------------------------- 
	        logger.warn("INICIO TrasladarSubsanacionInforme. ");
        	
			//Obtener Participantes de la propuesta actual, con relación "Trasladado"
 			String sqlQueryPart = "WHERE ROL= 'TRAS' AND NUMEXP = '"+rulectx.getNumExp()+"' ORDER BY ID";
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			//Obtener el documento Certificado de acuerdos para anexarlo al email 

			String sqlQueryDoc = "NOMBRE = 'Subsanación del Informe' AND ID_TRAMITE="+rulectx.getTaskId()+" AND INFOPAG_RDE != ''";

			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");
			
			logger.warn("documentos.toList().size()."+documentos.toList().size());
			
			if (documentos.toList().size() == 1) 
			{
				//Enviar email con el decreto adjunto

				//[eCenpri-Felipe Ticket #306] Algunos servidores interpretan este from como span
				//String cCorreoOrigen = "SIGEM-DCR";
				String cContenido = "<br/>Subsanación del Informe del número de expediente: "+rulectx.getNumExp();
				String cAsunto= "[SIGEM] Subsanación del Informe: "+rulectx.getNumExp();
				
				
				// Fichero a adjuntar
				IItem doc = (IItem)documentos.iterator().next();
				String infoPag = doc.getString("INFOPAG_RDE");
				logger.warn("infoPag."+infoPag);
				int taskId = rulectx.getTaskId();
				
				File file = this.getFile(rulectx, taskId, rulectx.getNumExp(), infoPag, "Subsanación del Informe");
				// Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
				if(participantes != null && participantes.toList()!= null && participantes.toList().size() != 0){
					for (int i=0; i<participantes.toList().size(); i++)
					{
						IItem participante = (IItem) participantes.toList().get(i);
						String emailNotif = participante.getString("DIRECCIONTELEMATICA");
						
						if (emailNotif != null)
						{
							StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
							while (tokens.hasMoreTokens()) 
							{
								String cCorreoDestino = tokens.nextToken();	
								if (participante!=null)
								{
						        	if (!cCorreoDestino.equals("")) 
						        	{
						        		logger.warn("direccion telematica. "+cCorreoDestino);
							        	// Confeccionar el email
										MailUtil.enviarCorreo(rulectx, cCorreoDestino, cAsunto, cContenido, file);	
						        	}
								}
							}
						}
					}
				}
				file.delete();
			}
			logger.warn("FIN TrasladarSubsanacionInforme. ");
			
			return new Boolean(true);
		}
		catch(Exception e) 
		{
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
	}

	private File getFile(IRuleContext rulectx, int taskId, String numExp, String infoPag, String nombreFichero) throws ISPACException{
		
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
				String fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + nombreFichero +"."+ extension;
				
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
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

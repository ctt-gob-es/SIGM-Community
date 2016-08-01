package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import Saicar.Recursos.Correo.Correo;

/**
 * 
 * @author diezp
 * @proposito Envía un email a cada departamento/servicio seleccionado en la entidad Participantes,
 * 			  y que tenga la relación Trasladado.
 * 
 */
public class TrasladarDecretoRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			try{
				
				//Comprobar si el campo motivo rechazo tiene asignado un valor
				
				//----------------------------------------------------------------------------------------------
		        ClientContext cct = (ClientContext) rulectx.getClientContext();
		        IInvesflowAPI invesFlowAPI = cct.getAPI();
		        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		        //----------------------------------------------------------------------------------------------
		
		        IItem exp = null;
		        String motivoRechazo = null;
		        String numExp = rulectx.getNumExp();
		        String strQuery = "WHERE NUMEXP='" + numExp + "'";
		        IItemCollection collExps = entitiesAPI.queryEntities("SGD_RECHAZO_DECRETO", strQuery);
		        Iterator itExps = collExps.iterator();
		        if (itExps.hasNext()) 
		        {
		        	exp = (IItem)itExps.next();
		        	motivoRechazo = exp.getString("RECHAZO_DECRETO");
		        	
		        	if (motivoRechazo!=null && !motivoRechazo.equals("")){
		        		return null;
		        	}
				
		        }
			
			}catch (Exception e){
				try {
					throw new ISPACInfo("Se ha producido un error al ejecutar la regla de trasladar decreto.");
				} catch (ISPACInfo e1) {
					e1.printStackTrace();
				}
			}
			
			
			
			//APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
						
			// 1. Comprobar que el decreto esté firmado -->
			// --> Está ya hecho en ValidateFirmaDocRule, ejecutada antes que esta regla
			
			// 2. Obtener Participantes del expediente actual, con relación "Trasladado"
			String numExp = rulectx.getNumExp();
 			String sqlQueryPart = "WHERE ROL= 'TRAS' AND NUMEXP = '"+numExp+"' ORDER BY ID";
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			if (participantes != null && participantes.toList().size() > 0){
				
				// 3. Obtener el documento decreto para anexarlo al email 
				int taskId = rulectx.getTaskId();
				String sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA = '02'";
				IItemCollection documentos = entitiesAPI.getDocuments(numExp, sqlQueryDoc, "");
				
				if (documentos == null || documentos.toList().size()==0){
					throw new ISPACRuleException("No se pueden enviar los traslados: No hay ningún documento firmado en el trámite.");
				}else if (documentos.toList().size() > 1 ) {
					throw new ISPACRuleException("No se pueden enviar los traslados: Hay más de un documento anexado al trámite.");
				}else if (documentos.toList().size() == 1) {
					
					// 4. Enviar email con el decreto adjunto
	
					// Variables comunes para el envío de email
					String cHost_mail = Messages.getString("cHost_mail");
					String cPort_mail = Messages.getString("cPort_mail");
					String cUsr_mail = Messages.getString("cUsr_mail");
					String cPwd_mail = Messages.getString("cPwd_mail");
					//[eCenpri-Felipe Ticket #306] Algunos servidores interpretan este from como span
					//String cCorreoOrigen = "SIGEM-DCR";
					String cCorreoOrigen = Messages.getString("cDecr_From");
					
					int numDecreto = this.getNumeroDecreto(rulectx, numExp);
					

					// Fichero a adjuntar
					IItem doc = (IItem)documentos.iterator().next();
					String infoPagRde = doc.getString("INFOPAG_RDE");
					File file = this.getFile(rulectx,taskId,numExp,infoPagRde);
					
					//expediente
					IItem exp = entitiesAPI.getExpedient(rulectx.getNumExp());
					
					/**
					 * [Ticket #357#[Teresa] INICIO SIGEM Traslados cambiar el asuntos de los email que se mandan]
					 * **/
					
					String cContenido = "<br/> Número de expediente del decreto: "+rulectx.getNumExp() +
							", con asunto: "+exp.getString("ASUNTO") +
							", con fecha de aprobación: "+doc.getDate("FAPROBACION")+". \n"+
							" Adjunto se envía el Decreto Nº"+numDecreto;
					String cAsunto= "[SIGEM] Traslado de "+exp.getString("NOMBREPROCEDIMIENTO")+" Nº"+numDecreto;
					
					/**
					 * [Ticket #357#[Teresa] FIN SIGEM Traslados cambiar el asuntos de los email que se mandan]
					 * **/			
					
					// Para cada participante seleccionado --> enviar email y actualizar el campo DECRETO_TRASLADADO en la BBDD
					for (int i=0; i<participantes.toList().size(); i++){
						
						IItem participante = (IItem) participantes.toList().get(i);
						String direccionesCorreo = participante.getString("DIRECCIONTELEMATICA");
						if (direccionesCorreo != null){
							StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
	
							while (tokens.hasMoreTokens()) {
								
								String cCorreoDestino = tokens.nextToken();	
				
								if (participante!=null){
						        	if (!cCorreoDestino.equals("")) {
			        	
							        	// Confeccionar el email
										Correo oCorreo = new Correo(cHost_mail, Integer.parseInt(cPort_mail), cUsr_mail, cPwd_mail);
										oCorreo.ponerTo(0, cCorreoDestino);
	
										
										if (cCorreoOrigen != null && !cCorreoOrigen.equals("")) {
											oCorreo.ponerFrom(cCorreoOrigen);
											oCorreo.ponerAsunto(cAsunto);
											oCorreo.ponerContenido(cContenido, true);
											// Adjuntar fichero al email
											/**
											 * [Ticket #357#[Teresa] INICIO SIGEM Traslados cambiar el asuntos de los email que se mandan]
											 * **/
											Date fecha = doc.getDate("FAPROBACION");
											String nombreFichero=exp.getString("NOMBREPROCEDIMIENTO")+"-"+fecha.getYear()+"-"+numDecreto;
											oCorreo.adjuntar(file.getParent(), true, nombreFichero);
											/**
											 * [Ticket #357#[Teresa] FIN SIGEM Traslados cambiar el asuntos de los email que se mandan]
											 * **/
									
											//oCorreo.adjuntar(file.getParent(), true, file.getName());
											// Enviar email
											String dir[] = oCorreo.enviar();
											String error = "";
											if (dir != null) {
												for (int nI = 0; nI < dir.length; nI++)
													error = error + '\r' + dir[nI];
												
												throw new ISPACRuleException(error);
											}
										} else {
											String cTexto = "No es posible enviar el correo electrónico de Traslados de decretos."
													+ "Por favor, póngase en contacto con el administrador del sistema";
											throw new ISPACRuleException(cTexto);
										}
							        }
							    }
							}
							
							// Y actualizar el campo 'DECRETO_TRASLADADO' con valor 'Y'
							IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
							participanteAActualizar.set("DECRETO_TRASLADADO", "Y");
							participanteAActualizar.store(cct);
						}
					}
					// Eliminar el fichero temporal una vez enviado por correo
					file.delete();
				}
			}
			return null;
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
	}
	
	/**
	 * Obtiene los ficheros anexados al trámite actual 
	 *
	 * @param session
	 * @param infoPag
	 * @return
	 * @throws ISPACException
	 */
	private File getFile(IRuleContext rulectx, int taskId, String numExp, String infoPagRde) throws ISPACException{
		
		// API
		IGenDocAPI gendocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();

		Object connectorSession = null;
		try {
			connectorSession = gendocAPI.createConnectorSession();
			File file = null;
			try{
				String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, infoPagRde));
				
				//Se almacena documento
				String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
				fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
				
				OutputStream out = new FileOutputStream(fileName);
				gendocAPI.getDocument(connectorSession, infoPagRde, out);
								
				file = new File(fileName);
			
				return file;
			} catch (FileNotFoundException e) {
				throw new ISPACRuleException("Error al intentar obtener el documento, no existe.", e);
			}
		}finally {
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
    	}
	} 
	
	/**
	 * Obtiene el número de decreto del expediente actual
	 * @param session
	 * @param expediente
	 * @return
	 * @throws ISPACException
	 */
	private int getNumeroDecreto(IRuleContext rulectx, String numExp) throws ISPACException {
		
		// API de entidades
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		
		IItemCollection itemCollection = entitiesAPI.getEntities("SGD_DECRETO", numExp);
		
		if (itemCollection==null || itemCollection.toList().size()==0) {
			throw new ISPACRuleException("Se ha producido un error al obtener los datos del decreto.");
		}
		if (itemCollection!=null && itemCollection.toList().size()>1) {
			throw new ISPACRuleException("Se ha producido un error. Se han encontrado varios registros para la entidad Decreto.");
		}
		
		IItem item = (IItem)itemCollection.iterator().next();
		int numDecreto = item.getInt("NUMERO_DECRETO");
		
		if (numDecreto<=0) {
			throw new ISPACRuleException("Se ha producido un error al obtener el número de decreto.");
		}
		
		return numDecreto;
	}
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

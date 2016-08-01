package ieci.tdw.ispac.api.rule.procedures.secretaria;

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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import es.dipucr.sigem.api.rule.common.utils.MailUtil;

public class TrasladarAcuerdosRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
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
	        //----------------------------------------------------------------------------------------------
						
        	String strOrgano = CommonFunctions.getOrganoSesion(rulectx, null);
        	boolean esAcuerdo = (strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0 );

        	//Comprobar que el acuerdo esté firmado
			// --> Está ya hecho en ValidateFirmaRule, ejecutada antes que esta regla
			
	        //Obtener las propuestas y urgencias incluidas en la sesión
	        List listPropuestas = CommonFunctions.getPropuestas(rulectx, entitiesAPI);
	        Iterator it = listPropuestas.iterator();
	        IItem iProp = null;

	        //Para cada propuesta se envía un email a sus participantes trasladados
	        int orden = 0;
	        while (it.hasNext())
	        {
	        	orden++;
	        	iProp = (IItem)it.next();
	        	String numexp_origen = iProp.getString("NUMEXP_ORIGEN");
	        	String strTabla = esAcuerdo? "SECR_ACUERDO":"SECR_DICTAMEN";
				String numAcuerdo = getNumero(rulectx, numexp_origen, strTabla);
	        	
				//Obtener Participantes de la propuesta actual, con relación "Trasladado"
	 			String sqlQueryPart = "WHERE ROL= 'TRAS' AND NUMEXP = '"+numexp_origen+"' ORDER BY ID";
				IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
				
				//Obtener el documento Certificado de acuerdos para anexarlo al email 
				int taskId = rulectx.getTaskId();
				String sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA = '02' AND DESCRIPCION LIKE '%"+numAcuerdo+"%'";
				IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");
				
				if (documentos.toList().size() == 1) 
				{
					//Enviar email con el decreto adjunto
					
					
					String cContenido = "<br/>Adjunto se envía el Acuerdo Nº"+numAcuerdo;
					String cAsunto= "[SIGEM] Traslado de Acuerdo Nº"+numAcuerdo;
					if (!esAcuerdo)
					{
						cContenido = "<br/>Adjunto se envía el Dictamen Nº"+numAcuerdo;
						cAsunto= "[SIGEM] Traslado de Dictamen Nº"+numAcuerdo;
					}
					
					// Fichero a adjuntar
					IItem doc = (IItem)documentos.iterator().next();
					String infoPag = doc.getString("INFOPAG_RDE");
					File file = this.getFile(rulectx,taskId,numexp_origen,infoPag);
					
					// Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
					for (int i=0; i<participantes.toList().size(); i++)
					{
						IItem participante = (IItem) participantes.toList().get(i);
						String direccionesCorreo = participante.getString("DIRECCIONTELEMATICA");
						if (direccionesCorreo != null)
						{
							StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
							while (tokens.hasMoreTokens()) 
							{
								String cCorreoDestino = tokens.nextToken();	
								if (participante!=null)
								{
						        	if (!cCorreoDestino.equals("")) 
						        	{
						        		MailUtil.enviarCorreo(rulectx, direccionesCorreo, cAsunto, cContenido, file);
							        	// Confeccionar el email
										/*Correo oCorreo = new Correo(cHost_mail, Integer.parseInt(cPort_mail), cUsr_mail, cPwd_mail);
										oCorreo.ponerTo(0, cCorreoDestino);
										if (cCorreoOrigen != null && !cCorreoOrigen.equals("")) 
										{
											oCorreo.ponerFrom(cCorreoOrigen);
											oCorreo.ponerAsunto(cAsunto);
											oCorreo.ponerContenido(cContenido, true);
											// Adjuntar fichero al email
											oCorreo.adjuntar(file.getParent(), true, file.getName());
											
											
											
											// Enviar email
											String dir[] = oCorreo.enviar();
											String error = "";
											if (dir != null) 
											{
												for (int nI = 0; nI < dir.length; nI++)
												{
													error = error + '\r' + dir[nI];
												}
												throw new ISPACRuleException(error);
											}
										} 
										else 
										{
											String cTexto = 
												"No es posible enviar el correo electrónico de Traslado." + 
												"Por favor, póngase en contacto con el administrador del sistema";
											throw new ISPACInfo(cTexto);
										}*/
							        }
							    }
							}
							
							/*
							// Y actualizar el campo 'ACUERDO_TRASLADADO' con valor 'Y'
							IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
							participanteAActualizar.set("ACUERDO_TRASLADADO", "Y");
							participanteAActualizar.store(cct);
							*/
						}
					}
					// Eliminar el fichero temporal una vez enviado por correo
					file.delete();
				}
	        }
			return null;
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
				String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
				fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
				
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
	
	private String getNumero(IRuleContext rulectx, String numExp, String strTabla) throws ISPACException 
	{
		String numAcuerdo = "?";
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		String strQuery = "WHERE NUMEXP_ORIGEN='"+numExp+"'";
		IItemCollection itemCollection = entitiesAPI.queryEntities(strTabla, strQuery);
		Iterator it = itemCollection.iterator();
		if (it.hasNext())
		{
			IItem iAcuerdo = (IItem)it.next();
			numAcuerdo = iAcuerdo.getString("NUMERO") + "/" + iAcuerdo.getString("YEAR"); 
		}
		else
		{
			throw new ISPACInfo("Se ha producido un error al obtener el número de acuerdo o dictamen.");
		}
		return numAcuerdo;
	}
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

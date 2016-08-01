package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
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

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import Saicar.Recursos.Correo.Correo;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateTrasladosComiRule implements IRule {
	
	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(GenerateTrasladosComiRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
    		//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
					

        	//Comprobar que el acuerdo esté firmado
			// --> Está ya hecho en ValidateFirmaRule, ejecutada antes que esta regla
			
	        //Obtener las propuestas y urgencias incluidas en la sesión
	        List listPropuestas = SecretariaUtil.getPropuestas(rulectx, entitiesAPI);
	        Iterator it = listPropuestas.iterator();
	        //Para que no coja el borrador 
	        it.next();
	        IItem iProp = null;

	        //Para cada propuesta se envía un email a sus participantes trasladados
	        while (it.hasNext())
	        {
	        	iProp = (IItem)it.next();
	        	String numexp_origen_propuesta = iProp.getString("NUMEXP_ORIGEN");
	        	String strTabla = "SECR_DICTAMEN";
				String numAcuerdo = getNumero(rulectx, numexp_origen_propuesta, strTabla);
	        	
				//dEPARTAMENTO QUE HA INICIADO ESTa propuesta				
	 			String departamento = getDepartamento(rulectx, numexp_origen_propuesta);
	 			
	 			//email del jefe del departamento que ha iniciado la propuesta.
	 			String direccionesCorreo = getEmailJefeDepar(rulectx, departamento);
				
				//Obtener el documento Certificado de acuerdos para anexarlo al email 
				int taskId = rulectx.getTaskId();
				String sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA = '02' AND DESCRIPCION LIKE '%"+numAcuerdo+"%'";
				IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");
				
				if (documentos.toList().size() == 1) 
				{
					//Enviar email con el decreto adjunto

					// Variables comunes para el envío de email
					String cHost_mail = Messages.getString("cHost_mail");
					String cPort_mail = Messages.getString("cPort_mail");
					String cUsr_mail = Messages.getString("cUsr_mail");
					String cPwd_mail = Messages.getString("cPwd_mail");
					
					//[eCenpri-Felipe Ticket #306] Algunos servidores interpretan este from como span
					//String cCorreoOrigen = "SIGEM-DCR";
					String cCorreoOrigen = Messages.getString("cDecr_From");

					String cContenido = "<br/>Adjunto se envía el Dictamen Nº"+numAcuerdo;
					String cAsunto= "[SIGEM] Traslado de Dictamen Nº"+numAcuerdo;

					
					// Fichero a adjuntar
					IItem doc = (IItem)documentos.iterator().next();
					String infoPag = doc.getString("INFOPAG_RDE");
					File file = DocumentosUtil.getFile(cct, infoPag, null, null);
					

					if (direccionesCorreo != null)
					{
						StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
						while (tokens.hasMoreTokens()) 
						{
							String cCorreoDestino = tokens.nextToken();	

				        	if (!cCorreoDestino.equals("")) 
				        	{
					        	// Confeccionar el email
								Correo oCorreo = new Correo(cHost_mail, Integer.parseInt(cPort_mail), cUsr_mail, cPwd_mail);
								oCorreo.ponerTo(0, cCorreoDestino);
								if (cCorreoOrigen != null && !cCorreoOrigen.equals("")) 
								{
									oCorreo.ponerFrom(cCorreoOrigen);
									oCorreo.ponerAsunto(cAsunto);
									oCorreo.ponerContenido(cContenido, true);
									// Adjuntar fichero al email
									oCorreo.adjuntar(file.getParent(), true, file.getName());
									// Enviar email
									String[] dir = oCorreo.enviar();
									//logger.warn("correo enviado a la direccion. "+cCorreoDestino);
									String error = "";
									if (dir != null) 
									{
										for (int nI = 0; nI < dir.length; nI++)
										{
											error = error + '\n' + dir[nI];
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
			logger.error(e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException(e);
        }
	}
	
	@SuppressWarnings("rawtypes")
	private String getEmailJefeDepar(IRuleContext rulectx, String idDepartamento) throws ISPACException {
		
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		
		int idDep = getIdDepart(idDepartamento); 
		String strQuery ="WHERE ID="+idDep+"";
		
		IItemCollection collection = entitiesAPI.queryEntities("IUSERDEPTHDR", strQuery);
		
		String emailJefe="";
		
		if(collection != null){
			Iterator it = collection.iterator();
			
	        while (it.hasNext())
	        {
	        	IItem itemPropuesta = (IItem)it.next();
	        	emailJefe = itemPropuesta.getString("REMARKS");
	        }
		}

		return emailJefe;
	}

	private int getIdDepart(String idDepartamento) throws ISPACException {
		
		String sID = null;
	    //String sTYPE = null;
	    StringTokenizer tokens = new StringTokenizer( idDepartamento, "-");
	    
	    if (tokens.hasMoreTokens()) 
	    {
            tokens.nextToken();
            tokens.nextToken();
		    if (tokens.hasMoreTokens())
		    {
		        sID = tokens.nextToken();
		    }
	    }
	    
	    if (sID == null)
	    {
			throw new ISPACException( "UID erróneo");
	    }
		
		return Integer.parseInt( sID);
	}

	private String getDepartamento(IRuleContext rulectx, String numexp_origen_propuesta) throws ISPACException {
		
		String departamento = "";
		
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		
        IItem itemPropuesta = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numexp_origen_propuesta);

        if(itemPropuesta != null){
        	departamento = itemPropuesta.getString("idseccioniniciadora");
        }

		return departamento;
	}
	
	@SuppressWarnings("rawtypes")
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

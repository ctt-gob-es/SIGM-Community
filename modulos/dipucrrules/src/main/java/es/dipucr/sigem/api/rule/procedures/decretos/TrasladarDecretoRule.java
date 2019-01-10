package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
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
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

		
public class TrasladarDecretoRule implements IRule {

	private static final Logger LOGGER = Logger.getLogger(TrasladarDecretoRule.class);
	
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
		Date fechaEnvio = null;
		String nombreDoc = "";
		String descripcionDoc = "";
		boolean enviadoEmail = false;
		String emailNotif = "";
		String descripError = "";
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
		        Iterator<?> itExps = collExps.iterator();
		        if (itExps.hasNext()) {
		        	exp = (IItem)itExps.next();
		        	motivoRechazo = exp.getString("RECHAZO_DECRETO");
		        	
		        	if (motivoRechazo!=null && !"".equals(motivoRechazo)){
		        		return null;
		        	}
		        }
			}catch (ISPACInfo e){
	        	LOGGER.error("Se ha producido un error al ejecutar la regla de trasladar decreto en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
				throw new ISPACInfo("Se ha producido un error al ejecutar la regla de trasladar decreto.");
			}
			
			//APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			//Se cambia la configuración del correo de ApplicationResources.properties a su archivo de configuración por entidad
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
			String cCorreoOrigen = correoConfig.get(CorreoConfiguration.DECR_FROM);
			
			String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");

			Object[] imagen = {rutaImg, Boolean.TRUE, "logoCabecera.gif", "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
						
			// 1. Comprobar que el decreto esté firmado -->
			// --> Está ya hecho en ValidateFirmaDocRule, ejecutada antes que esta regla
			
			// 2. Obtener Participantes del expediente actual, con relación "Trasladado"
			String numExp = rulectx.getNumExp();
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL= 'TRAS'", "ID");
			
			if (participantes != null && participantes.toList().size() > 0){
				
				// 3. Obtener el documento decreto para anexarlo al email 
				int taskId = rulectx.getTaskId();
				IItemCollection documentos = entitiesAPI.getDocuments(numExp, "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA IN ('02','03','04')", "");
				
				if (documentos == null || documentos.toList().size()==0){
					throw new ISPACRuleException("No se pueden enviar los traslados: No hay ningún documento firmado en el trámite.");
				}else if (documentos.toList().size() > 1 ) {
					throw new ISPACRuleException("No se pueden enviar los traslados: Hay más de un documento anexado al trámite.");
				}else if (documentos.toList().size() == 1) {
					IItem doc = (IItem)documentos.iterator().next();
					if(!"04".equals(doc.get("ESTADOFIRMA"))){

						String numDecreto = this.getNumeroDecreto(rulectx, numExp);
						
						/**
						 * [Ticket #357#[Teresa] INICIO SIGEM Traslados cambiar el asuntos de los email que se mandan]
						 * **/
						
						//expediente
						IItem exp = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
						String cContenido = " <img src='cid:escudo' width='200px'/>"
								+ " <p align=justify>"
								+ "		<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Número de expediente del decreto: <b>"+rulectx.getNumExp() +"</b>."
								+ "		<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Asunto: <b>"+exp.getString("ASUNTO") +"</b>."
								+ "		<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Fecha de aprobación: <b>"+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(doc.getDate("FAPROBACION"))+"</b>."
								+ "		<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Adjunto se envía el Decreto Nº: <b>"+numDecreto +"</b>"
								+ "		<br/>"
								+ " </p>";
						String cAsunto= "[SIGEM] Traslado de " + exp.getString("NOMBREPROCEDIMIENTO") + " Nº " + numDecreto;
						
						/**
						 * [Ticket #357#[Teresa] FIN SIGEM Traslados cambiar el asuntos de los email que se mandan]
						 * **/			
						
						// Fichero a adjuntar
						String infoPagRde = doc.getString("INFOPAG_RDE");

						String nombreFichero=exp.getString("NOMBREPROCEDIMIENTO") + "-" + numDecreto;
						
						File file = DocumentosUtil.getFile(cct,infoPagRde, nombreFichero, null);
						
						// Para cada participante seleccionado --> enviar email y actualizar el campo DECRETO_TRASLADADO en la BBDD
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
					
									if (participante!=null){
							        	if (StringUtils.isNotEmpty(cCorreoDestino)){
											String dir[]= MailUtil.enviarCorreo(rulectx.getClientContext(), cCorreoOrigen, cCorreoDestino, cAsunto, cContenido, file, imagenes);

											String error = "";
											if (dir != null) {
												for (int nI = 0; nI < dir.length; nI++)
												{
													error = error + '\r' + dir[nI];
												}
												descripError = error;
												enviadoEmail = false;
												DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
												throw new ISPACRuleException(error);
											} else {
												enviadoEmail = true;
												fechaEnvio = new Date();
												// Eliminar el fichero temporal una vez enviado por correo
												DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
											}
										} else {											
											descripError = "No es posible enviar el correo electrónico de Traslados de decretos."
													+ "Por favor, póngase en contacto con el administrador del sistema";
											enviadoEmail = false;
											DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
											throw new ISPACInfo(descripError);
								        }
								    }
								}
								
								// Y actualizar el campo 'DECRETO_TRASLADADO' con valor 'Y'
								IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
								participanteAActualizar.set("DECRETO_TRASLADADO", "Y");
								participanteAActualizar.store(cct);
							} else {
								descripError = "El email esta vacío";
								enviadoEmail = false;
								DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,fechaEnvio, nombreDoc, descripcionDoc, enviadoEmail, emailNotif, descripError, rulectx);
							}
						}
						file.delete();
					}
				}
			}
			return null;
		} catch(Exception e) {
        	throw new ISPACRuleException("No se han podido insertar en BBDD los datos sobre el envío del mail", e);
        }
	}
	
	
	/**
	 * Obtiene el número de decreto del expediente actual
	 * @param session
	 * @param expediente
	 * @return
	 * @throws ISPACException
	 */
	private String getNumeroDecreto(IRuleContext rulectx, String numExp) throws ISPACException {
		
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
		String anio = item.getString("ANIO");
		if(StringUtils.isNotEmpty(anio)){
			anio = anio.replaceAll("\\.", "");
		}
		String numDecreto = item.getString("NUMERO_DECRETO");
		if(StringUtils.isNotEmpty(numDecreto)){
			numDecreto = numDecreto.replaceAll("\\.", "");
		}
		
		if (StringUtils.isEmpty(numDecreto)) {
			throw new ISPACRuleException("Se ha producido un error al obtener el número de decreto.");
		}
		
		return anio + "-"+ numDecreto;
	}
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Empty method
	}
}

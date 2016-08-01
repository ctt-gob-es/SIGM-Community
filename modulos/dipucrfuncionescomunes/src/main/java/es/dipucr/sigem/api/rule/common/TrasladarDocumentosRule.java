package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class TrasladarDocumentosRule implements IRule {
	
	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(TrasladarDocumentosRule.class);

	
//	private static final Logger logger = Logger.getLogger(TrasladarDocumentosRule.class);
	
	
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
		String nombreDoc = "";
		String emailNotif = "";
		try{
			
			//APIs
			//*******************************
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			//*******************************
			
			// 1. Obtener Participantes del expediente actual, con relación "Trasladado"
			String numexp = rulectx.getNumExp();
 			String sqlQueryPart = "WHERE ROL= 'TRAS' AND NUMEXP = '" + numexp + "' ORDER BY ID";
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			if (participantes == null || participantes.toList().size() == 0){
				throw new ISPACRuleException("No se pueden enviar los traslados: No se han definido trasladados en la pestaña participantes. ");
			}
			else{ //Hay participantes
				
				// 2. Obtener el documento decreto para anexarlo al email 
				int taskId = rulectx.getTaskId();
				//String sqlQueryDoc = "ID_TRAMITE = " + taskId + " AND ESTADOFIRMA IN ('02','03','04')";
				String sqlQueryDoc = "ID_TRAMITE = " + taskId + "";
				
				//[Dipucr-Teresa-Ticket#320]INICIO Modificar la regla de Trasladar documento para que seleccione el documento que se quiere trasladar
				String otrosDatos = TramitesUtil.getDatosEspecificosOtrosDatos(cct, rulectx.getTaskProcedureId());
				if(!otrosDatos.equals("")){
					sqlQueryDoc = sqlQueryDoc + " AND NOMBRE='"+otrosDatos+"'";
				}
				//[Dipucr-Teresa-Ticket#320]FIN Modificar la regla de Trasladar documento para que seleccione el documento que se quiere trasladar
				
				IItemCollection documentos = entitiesAPI.getDocuments(numexp, sqlQueryDoc, "");
				
				if (documentos == null || documentos.toList().size()==0){
					throw new ISPACRuleException("No se pueden enviar los traslados: No hay ningún documento firmado en el trámite.");
				//}else if (documentos.toList().size() > 1 ) {
					//throw new ISPACRuleException("No se pueden enviar los traslados: Hay más de un documento anexado al trámite.");
				}else {					
					IItem doc = (IItem)documentos.iterator().next();
					if(!doc.get("ESTADOFIRMA").equals("04")){
						
						// Fichero a adjuntar
						//IItem doc = (IItem)documentos.iterator().next();
						String infoPagRde = doc.getString("INFOPAG_RDE");
						//logger.warn(infoPagRde);
						if(infoPagRde == null){
							infoPagRde = doc.getString("INFOPAG");
						}
						//logger.warn("----------------------------------------------------------------------------------------"+infoPagRde);
//						File file = this.getFile(rulectx,taskId,numexp,infoPagRde, nombreFichero);
						File file = DocumentosUtil.getFile(cct, infoPagRde, null, null);
						
						// Para cada participante seleccionado --> enviar email
						for (int i=0; i<participantes.toList().size(); i++){
							
							IItem participante = (IItem) participantes.toList().get(i);
							nombreNotif = participante.getString("NOMBRE");
							emailNotif = participante.getString("DIRECCIONTELEMATICA");
							nombreDoc = doc.getString("NOMBRE");
							IItem exp = ExpedientesUtil.getExpediente(cct, numexp);
							String asunto = exp.getString("NOMBREPROCEDIMIENTO");
							
							String strContenido = "<img src='cid:escudo' width='200px'/>"
									+ "<p align=justify>"
									+ "&nbsp;&nbsp;&nbsp;Adjunto se envía el "+nombreDoc+" En el expediente "+asunto+" : "+rulectx.getNumExp()+" <b>";
							
							if (emailNotif != null){
								StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
	
								while (tokens.hasMoreTokens()) {
									
									String cCorreoDestino = tokens.nextToken();	
					
									if (StringUtils.isEmpty(cCorreoDestino)){
										rulectx.setInfoMessage("El participante -" + nombreNotif + "- no tiene correo electrónico definido. No se le enviará el documento del trámite.");
									}
									else{
										CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
										String strFrom = correoConfig.get(CorreoConfiguration.CONV_FROM);
										String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
							        	Object[] imagen = {rutaImg, new Boolean(true), "logoCabecera.gif", "escudo"};
										List<Object[]> imagenes = new ArrayList<Object[]>();
										imagenes.add(imagen);
										
										MailUtil.enviarCorreo(rulectx, strFrom, emailNotif, asunto, strContenido, file, imagenes);
								    }
								}
							}
							else{
								rulectx.setInfoMessage("El participante -" + nombreNotif + "- no tiene correo electrónico definido. No se le enviará el documento del trámite.");
							}
						}
						file = null;
					}
				}
			}
			return null;
			
		} catch(Exception e) {
			logger.error("Error al mandar el documento en el expediente. Numexp. "+rulectx.getNumExp()+" en el trámite. "+rulectx.getTaskId());
			throw new ISPACRuleException("Error al mandar el documento en el expediente. Numexp. "+rulectx.getNumExp()+" en el trámite. "+rulectx.getTaskId());
        }
	}
	
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

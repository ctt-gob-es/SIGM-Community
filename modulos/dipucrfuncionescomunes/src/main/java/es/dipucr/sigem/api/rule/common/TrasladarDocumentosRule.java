package es.dipucr.sigem.api.rule.common;

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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class TrasladarDocumentosRule implements IRule {
	
	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(TrasladarDocumentosRule.class);

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
			//*******************************
			
			// 1. Obtener Participantes del expediente actual, con relación "Trasladado"
			String numexp = rulectx.getNumExp();
			IItemCollection participantes = ParticipantesUtil.getParticipantesByRol(cct, numexp, ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil.ID);
			
			if (participantes == null || participantes.toList().size() == 0){
				throw new ISPACRuleException("No se pueden enviar los traslados: No se han definido trasladados en la pestaña participantes. ");
			} else { //Hay participantes
				
				String sqlQueryDoc = " ID_TRAMITE = " + rulectx.getTaskId() + "";
				
				//[Dipucr-Teresa-Ticket#320]INICIO Modificar la regla de Trasladar documento para que seleccione el documento que se quiere trasladar
				String otrosDatos = TramitesUtil.getDatosEspecificosOtrosDatos(cct, rulectx.getTaskProcedureId());
				if(StringUtils.isNotEmpty(otrosDatos)){
					sqlQueryDoc = sqlQueryDoc + " AND NOMBRE = '" + otrosDatos + "'";
				}
				//[Dipucr-Teresa-Ticket#320]FIN Modificar la regla de Trasladar documento para que seleccione el documento que se quiere trasladar
				
				IItemCollection documentos = DocumentosUtil.getDocumentos(cct, numexp, sqlQueryDoc, "");
				
				if (documentos == null || documentos.toList().size()==0){
					throw new ISPACRuleException("No se pueden enviar los traslados: No hay ningún documento firmado en el trámite.");
				} else {					
					IItem doc = (IItem)documentos.iterator().next();
					
					if(!"04".equals(doc.get(DocumentosUtil.ESTADOFIRMA))){
						
						// Fichero a adjuntar
						String infoPagRde = DocumentosUtil.getInfoPagRDEoInfoPag(rulectx, doc.getInt(DocumentosUtil.ID));
						File file = DocumentosUtil.getFile(cct, infoPagRde, null, null);
						
						// Para cada participante seleccionado --> enviar email
						Iterator<?> participantesIterator = participantes.iterator();
						
						while(participantesIterator.hasNext()){
							IItem participante = (IItem) participantesIterator.next();
							
							nombreNotif = participante.getString(ParticipantesUtil.NOMBRE);
							emailNotif = participante.getString(ParticipantesUtil.DIRECCIONTELEMATICA);
							nombreDoc = doc.getString(DocumentosUtil.NOMBRE);
							
							String asunto = ExpedientesUtil.getAsunto(cct, numexp);
							
							String strContenido = "<img src='cid:escudo' width='200px'/>"
									+ "<p align=justify>"
									+ "&nbsp;&nbsp;&nbsp;Adjunto se envía el " + nombreDoc + " En el expediente " + asunto + " : " + rulectx.getNumExp() + " <b>";
							
							if (emailNotif != null){
								StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
	
								while (tokens.hasMoreTokens()) {
									String cCorreoDestino = tokens.nextToken();	
					
									if (StringUtils.isEmpty(cCorreoDestino)){
										rulectx.setInfoMessage("El participante -" + nombreNotif + "- no tiene correo electrónico definido. No se le enviará el documento del trámite.");
									} else {
										CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
										String strFrom = correoConfig.get(CorreoConfiguration.CONV_FROM);
										String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
							        	Object[] imagen = {rutaImg, new Boolean(true), "logoCabecera.gif", "escudo"};
										List<Object[]> imagenes = new ArrayList<Object[]>();
										imagenes.add(imagen);
										
										MailUtil.enviarCorreo(rulectx.getClientContext(), strFrom, emailNotif, asunto, strContenido, file, imagenes);
								    }
								}
							} else {
								rulectx.setInfoMessage("El participante -" + nombreNotif + "- no tiene correo electrónico definido. No se le enviará el documento del trámite.");
							}
						}
						
						if(null != file && file.exists()){
			        		file.delete();
			        	}
					}
				}
			}
			return null;
			
		} catch(Exception e) {
			String error = "Error al mandar el documento en el expediente. Numexp. " + rulectx.getNumExp() + " en el trámite. " + rulectx.getTaskId() + ":" + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACRuleException(error, e);
        }
	}
		
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}

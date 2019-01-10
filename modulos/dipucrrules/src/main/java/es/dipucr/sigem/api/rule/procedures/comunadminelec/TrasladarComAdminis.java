package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class TrasladarComAdminis  implements IRule {
	
	private static final Logger logger = Logger.getLogger(TrasladarComAdminis.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	
		boolean terminar = false;
		
		/**
		 * Variables que se utilizarán para insertar en la bbdd los datos 
		 * sobre el envío correcto o incorrecto del email.
		 * */
		String nombreNotif = "";
		String emailNotif = "";
		
		IClientContext cct = rulectx.getClientContext();
		String asunto =  "";
		String contenido = "";
		
		try {
			//APIs
			
			String logoCabecera = "logoCabecera.gif"; //Se puede especificar un logo para los correos específico, si se deja toma el por defecto.	        
	        String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
			Object[] imagen = {rutaImg, new Boolean(true), logoCabecera, "escudo"};
			List<Object[]> imagenes = new ArrayList<Object[]>();
			imagenes.add(imagen);
			
			
			try{
				IItem expediente = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
				contenido = contenido
						+ "<br/>"
						+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Expediente: <b>" + rulectx.getNumExp() + "</b>."
						+ "<br/>"
						+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Asunto: <b>" + expediente.getString("ASUNTO") + "</b>."
						+ "<br/>"
						+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Adjunto se envía el documento de la Comunicación administrativa electrónica.";
				
				contenido = "<img src='cid:escudo' width='200px'/>"
		        		+ "<p align=justify>"
		        		+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Estimado señor/a:" 
		        		+ "<br/> <br/>" 
		        		+ MailUtil.ESPACIADO_PRIMERA_LINEA +  contenido
		        		+ " </p>"
		        		+ "<br/> <br/>";
		        		
				asunto =  "[SIGEM] Comunicación administrativa electrónica - Rte. "+EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct);
			}
			catch(Exception e){
				logger.error("Error al recuperar el expediente: " + rulectx.getNumExp() +" - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al recuperar el expediente: " + rulectx.getNumExp() +" - "+e.getMessage(), e);
			}	
			
			// 2. Obtener Participantes del expediente actual, con relación "Trasladado"
			String numExp = rulectx.getNumExp();
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL= 'TRAS'", "ID");
			
			if (participantes != null && participantes.toList().size() > 0){
				// 3. Obtener el documento decreto para anexarlo al email 
				String sql = "ID_TRAMITE=" + rulectx.getTaskId()+ " AND NREG IS NULL AND " 
						+ "(ESTADOFIRMA='"   + SignStatesConstants.FIRMADO + "'"
						+ "OR ESTADOFIRMA='" + SignStatesConstants.RECHAZADO + "')"; //[dipucr-Felipe #879]
				IItemCollection documentos = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), sql, "ID DESC");
				
				if (documentos == null || documentos.toList().size()==0){
					throw new ISPACRuleException("No se pueden enviar los traslados: El documento 'Plantilla Carta digital' no está firmado en el trámite.");
				}else if (documentos.toList().size() >= 1) {
					IItem doc = (IItem)documentos.iterator().next();
					if(doc.get("ESTADOFIRMA").equals(SignStatesConstants.FIRMADO)){
						// 4. Enviar email con la carta digital adjunta
						
						// Fichero a adjuntar
						//IItem doc = (IItem)documentos.iterator().next();
						String infoPagRde = doc.getString("INFOPAG_RDE");
						if(infoPagRde!=null){
							IItem exp = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
							//[Ticket365#ALSIGM] El nombre del documento que se manda en la carta digital no es explicativo
							String nombreFichero = "Comunicación administrativa electrónica";
							if(exp!=null){
								String nombreProc = "";
								if(exp.getString("NOMBREPROCEDIMIENTO")!=null) nombreProc = exp.getString("NOMBREPROCEDIMIENTO");
								if(exp.getString("ASUNTO")!=null) asunto  = asunto +" - "+exp.getString("ASUNTO");
								nombreFichero = nombreProc;
							}

							File file = DocumentosUtil.getFile(cct, infoPagRde, nombreFichero, null);
							
							// Para cada participante seleccionado --> enviar email y actualizar el campo DECRETO_TRASLADADO en la BBDD
							for (int i=0; i<participantes.toList().size(); i++){
								
								IItem participante = (IItem) participantes.toList().get(i);
								nombreNotif = participante.getString("NOMBRE");
								emailNotif = participante.getString("DIRECCIONTELEMATICA");
								if (emailNotif != null){
									StringTokenizer tokens = new StringTokenizer(emailNotif, ";");
		
									while (tokens.hasMoreTokens()) {
										
										String cCorreoDestino = tokens.nextToken();	
						
										if (participante!=null){											
											if(StringUtils.isNotEmpty(cCorreoDestino)){
									        	try{
									        		MailUtil.enviarCorreoVarios(rulectx, cCorreoDestino, asunto, contenido, false, file, imagenes);
									        	}
									        	catch(Exception e){
										        	String descripError =  "No se ha podido recuperar los correos electrónicos: " + cCorreoDestino;
													DipucrCommonFunctions.insertarAcuseEmail(nombreNotif, new Date(), nombreFichero, nombreFichero, false, "", descripError, rulectx);
													logger.error("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
													throw new ISPACRuleException("Error en el envío. "+rulectx.getNumExp()+" nombreNotif "+nombreNotif+ "descripcion error "+descripError+" - "+e.getMessage(), e);
										        }
									        }
									        else{	        	
									        	String descripError =  "No existe ningún participante para trasladar el documento.";
												DipucrCommonFunctions.insertarAcuseEmail(nombreNotif, new Date(), nombreFichero, nombreFichero, false, "", descripError, rulectx);
												logger.error(descripError);
												throw new ISPACRuleException(descripError);
									        }
									    }
									}
								}
							}
							file.delete();
						}						
					}
				}
			}			
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + " NUMEXP. "+rulectx.getNumExp()+"- . " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + " NUMEXP. "+rulectx.getNumExp()+"- . " + e.getMessage(), e);	
		} 
		
		
    	return new Boolean(terminar);
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {	
	}

}

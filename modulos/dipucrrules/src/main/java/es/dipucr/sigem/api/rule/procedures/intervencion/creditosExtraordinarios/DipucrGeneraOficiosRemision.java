package es.dipucr.sigem.api.rule.procedures.intervencion.creditosExtraordinarios;

import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * 
 * @author teresa
 * @date 09/09/2009
 * @propósito 
 * adjuntada en el primer trámite "Creación del decreto".
 */
public class DipucrGeneraOficiosRemision implements IRule {
	
	private static final Logger LOGGER = Logger.getLogger(DipucrGeneraOficiosRemision.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			LOGGER.info("INICIO - " + this.getClass().getName());
		
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			
			String numExp = rulectx.getNumExp();

	    	String id_ext = "";

	    	Object connectorSession = null;
			
			String plantillaDefecto = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
	    	
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, " (ROL != 'TRAS' OR ROL IS NULL)", "ID");
			for (Object oParticipante : participantes.toList()){
				try {
					connectorSession = gendocAPI.createConnectorSession();
					IItem participante = (IItem) oParticipante;

					cct.beginTX();
				
					if (participante!=null){
			        
						DocumentosUtil.setParticipanteAsSsVariable(cct, participante);	        	

						// Generar el documento a partir la plantilla
						IItem entityTemplate = DocumentosUtil.generarDocumento(rulectx, plantillaDefecto,null);
								
						String docref = entityTemplate.getString("INFOPAG");
						String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
						entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
						String templateDescripcion = entityTemplate.getString("DESCRIPCION");
						entityTemplate.set("DESCRIPCION", templateDescripcion+" - "+participante.getString(ParticipantesUtil.NOMBRE));
						entityTemplate.set("DESTINO", cct.getSsVariable("NOMBRE"));
						entityTemplate.set("DESTINO_ID", id_ext);

						entityTemplate.store(cct);
				        
						// Si todo ha sido correcto borrar las variables de la session
						DocumentosUtil.borraParticipanteSsVariable(cct);
				    }
				}catch (Exception e) {
					
					// Si se produce algún error se hace rollback de la transacción
					cct.endTX(false);
					
					String message = "exception.documents.generate";
					String extraInfo = null;
					Throwable eCause = e.getCause();
					
					if (eCause instanceof ISPACException) {
						
						if (eCause.getCause() instanceof NoConnectException) {
							extraInfo = "exception.extrainfo.documents.openoffice.off"; 
						} else {
							extraInfo = eCause.getCause().getMessage();
						}
					} else if (eCause instanceof DisposedException) {
						extraInfo = "exception.extrainfo.documents.openoffice.stop";
					} else {
						extraInfo = e.getMessage();
					}
	            	LOGGER.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
					throw new ISPACInfo(message, extraInfo);
					
				} finally {
					
					if (connectorSession != null) {
						gendocAPI.closeConnectorSession(connectorSession);
					}
				}
			}// for
			cct.endTX(true);
			LOGGER.info("FIN - " + this.getClass().getName());
		} catch(Exception e) {        	
        	throw new ISPACRuleException("No se han generado los oficios de remisión", e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Empty method
	}	
}

package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * 
 * @author diezp
 * @date 30/03/2009
 * @propósito Genera los acuses de recibo para el expediente de Decreto actual.
 */
public class DipucrGenerateAcuseVertical implements IRule {
	
	protected String strNombreDocAcuse  = "Acuse de Recibo Vertical";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
		
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			
			
			String numExp = rulectx.getNumExp();
	    	Object connectorSession = null;
	    	
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL != 'TRAS' OR ROL IS NULL", "ID");
			
			// 2. Comprobar que hay algún participante para el cual generar su acuse
			if (participantes!=null && participantes.toList().size()>=1) {
			 	// 3. Para cada participante generar un acuse de recibo
				for (int i=0;i<participantes.toList().size();i++){
					try {
						connectorSession = gendocAPI.createConnectorSession();
						IItem participante = (IItem) participantes.toList().get(i);
						// Abrir transacción para que no se pueda generar un documento sin fichero
				        cct.beginTX();
					
						if (participante!=null){
				        
							// Añadir a la session los datos para poder utilizar <ispatag sessionvar='var'> en la plantilla
							DocumentosUtil.setParticipanteAsSsVariable(cct, participante);	        	
				        	
				        	DocumentosUtil.generarDocumento(rulectx, strNombreDocAcuse, participante.getString(ParticipantesUtil.NOMBRE));
							
				        	// 5. Actualizar el campo 'Acuse_Generado' con valor 'Y'
							IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
							participanteAActualizar.set("ACUSE_GENERADO", "Y");
							participanteAActualizar.store(cct);
					        									
							// Si todo ha sido correcto borrar las variables de la session
							DocumentosUtil.borraParticipanteSsVariable(cct);
					        
					    }
					} catch (Exception e) {
						
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
						throw new ISPACInfo(message, extraInfo);
						
					}finally {
						
						if (connectorSession != null) {
							gendocAPI.closeConnectorSession(connectorSession);
						}
					}
				}// for
			}
			// Si todo ha sido correcto se hace commit de la transacción
			cct.endTX(true);
		} catch(Exception e) {
    		throw new ISPACRuleException("Error al generar los acuses de recibo del Decreto", e);   
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Empty method
	}

}


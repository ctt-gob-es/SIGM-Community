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
	
	protected String STR_NombreDocAcuse  = "Acuse de Recibo Vertical";

	
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
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
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
				        	if ((String)participante.get("NOMBRE")!=null) nombre = (String)participante.get("NOMBRE");
				        	if ((String)participante.get("DIRNOT")!=null) dirnot = (String)participante.get("DIRNOT");
				        	if ((String)participante.get("C_POSTAL")!=null) c_postal = (String)participante.get("C_POSTAL");
				        	if ((String)participante.get("LOCALIDAD")!=null) localidad = (String)participante.get("LOCALIDAD");
				        	if ((String)participante.get("CAUT")!=null) caut = (String)participante.get("CAUT");
				        	cct.setSsVariable("NOMBRE", nombre);
				        	cct.setSsVariable("DIRNOT", dirnot);
				        	cct.setSsVariable("C_POSTAL", c_postal);
				        	cct.setSsVariable("LOCALIDAD", localidad);
				        	cct.setSsVariable("CAUT", caut);				        	
				        	
				        	DocumentosUtil.generarDocumento(rulectx, STR_NombreDocAcuse, nombre);

							
				        	// 5. Actualizar el campo 'Acuse_Generado' con valor 'Y'
							IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
							participanteAActualizar.set("ACUSE_GENERADO", "Y");
							participanteAActualizar.store(cct);
					        									
							// Si todo ha sido correcto borrar las variables de la session
							cct.deleteSsVariable("NOMBRE");
							cct.deleteSsVariable("DIRNOT");
							cct.deleteSsVariable("C_POSTAL");
							cct.deleteSsVariable("LOCALIDAD");
							cct.deleteSsVariable("CAUT");
					        
					    }
					}catch (Throwable e) {
						
						// Si se produce algún error se hace rollback de la transacción
						cct.endTX(false);
						
						String message = "exception.documents.generate";
						String extraInfo = null;
						Throwable eCause = e.getCause();
						
						if (eCause instanceof ISPACException) {
							
							if (eCause.getCause() instanceof NoConnectException) {
								extraInfo = "exception.extrainfo.documents.openoffice.off"; 
							}
							else {
								extraInfo = eCause.getCause().getMessage();
							}
						}
						else if (eCause instanceof DisposedException) {
							extraInfo = "exception.extrainfo.documents.openoffice.stop";
						}
						else {
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
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}


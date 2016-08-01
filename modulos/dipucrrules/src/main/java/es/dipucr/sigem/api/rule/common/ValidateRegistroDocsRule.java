package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * 
 * @author manu
 * @date 27/08/2013
 * @propósito Valida que todos los documentos del trámite estén registrados, excepto los documentos sellados y los participantes comparece.
 * @propósito [Manu Ticket #526] SIGEM añadir regla en decretos UATA para comprobar el registro de salida de los documentos.
 */
public class ValidateRegistroDocsRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(ValidateRegistroDocsRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	@SuppressWarnings("rawtypes")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{	
			logger.info("INICIO - " + this.getClass().getName());
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItem item = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());
	        
	        if(item.getString("ESTADOADM") != null && item.getString("ESTADOADM").equals("RC")){
	        	return true;
	        }else{
	        	// Comprobar que se haya anexado un documento
				IItemCollection docsCollection = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
				
				if (docsCollection==null || docsCollection.toList().size()==0){
					rulectx.setInfoMessage("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que no se ha adjuntado un documento");
					return false;
				}else{
					Iterator docsIterator = docsCollection.iterator();
					while(docsIterator.hasNext()){
						IItem doc = (IItem) docsIterator.next();
						if((!doc.getString("SPAC_DT_DOCUMENTOS:NOMBRE").toUpperCase().trim().equals("DOCUMENTOS SELLADOS")) && (!doc.getString("SPAC_DT_DOCUMENTOS:NOMBRE").toUpperCase().trim().equals("PARTICIPANTES COMPARECE"))){
							String nreg = doc.getString("SPAC_DT_DOCUMENTOS:NREG");
							String freg = doc.getString("SPAC_DT_DOCUMENTOS:FREG");
							if(StringUtils.isEmpty(nreg) || StringUtils.isEmpty(freg)){
								rulectx.setInfoMessage("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que el documento '" + doc.getString("SPAC_DT_DOCUMENTOS:DESCRIPCION") + "' está sin registrar de salida");
								return false;
							}
						}
					}
					logger.info("FIN - " + this.getClass().getName());
					return true;
				}
	        }	        
		} catch (Exception e) {
			logger.error("Error al comprobar el estado de registro de los documentos. " + e.getMessage(), e);
	        throw new ISPACRuleException("Error al comprobar el estado de registro de los documentos", e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

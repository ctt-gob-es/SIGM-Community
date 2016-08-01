package es.dipucr.sigem.api.rule.common.registro;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.RegistroPresencialUtil;
import es.dipucr.sigem.api.rule.common.utils.RegistroTelematicoUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ActualizaDestinoRegistroEntrada implements IRule {
	
	private static final Logger logger = Logger.getLogger(ActualizaDestinoRegistroEntrada.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		logger.info("INICIO - " + this.getClass().getName());
		
		String numexp = "", nreg = "";		
		int idDepartamento = Integer.MIN_VALUE;
		
		try{
			numexp = rulectx.getNumExp();
			
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			nreg = ExpedientesUtil.getExpediente(cct, numexp).getString("NREG");
			
	    	IItemCollection procedimientoCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE id = '"+rulectx.getProcedureId()+"'");
	    	Iterator<?> procedimientoIterator = procedimientoCollection.iterator();
	    	
	    	if(procedimientoIterator.hasNext()){
	    		IItem procedimiento = (IItem) procedimientoIterator.next();
	    		idDepartamento = Integer.parseInt(procedimiento.getString("ORG_RSLTR").substring(2));
	    	}

	    	if(idDepartamento == Integer.MIN_VALUE){
	    		logger.info("No se ha encontrado departamento tramitador del procedimiento del expediente: " + numexp);
	    	}
	    	else{
	    		try{
	    			RegistroPresencialUtil.modificaDestinoRegistroEntrada(cct, nreg, RegistroPresencialUtil.getCodDepartamentoById(cct, idDepartamento));
	    		}
	    		catch(Exception e){
	    			logger.debug("Error al actualizar el destino con id " + idDepartamento + " del registro " + nreg + " del libro de entrada presencial. " + e.getMessage(), e);
	    		}
	    		try{
	    			RegistroTelematicoUtil.modificaDestinoRegistroEntrada(cct, nreg, "", RegistroPresencialUtil.getCodDepartamentoById(cct, idDepartamento));
	    		}
	    		catch(Exception e){
	    			logger.error("Error al actualizar el destino con id " + idDepartamento + " del registro " + nreg + " del registro telemático (tabla SGMRTREGISTRO de eTramitación). " + e.getMessage(), e);
	    		}
	    	}
		}
		catch(ISPACException e){
			logger.error("Error al actualizar el destino del registro: " + nreg + " del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al actualizar el destino del registro: " + nreg + " del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		catch(Exception e){
			logger.error("Error al actualizar el destino del registro: " + nreg + " del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al actualizar el destino del registro: " + nreg + " del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		
		logger.info("FIN - " + this.getClass().getName());
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}

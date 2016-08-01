package es.dipucr.sigem.api.rule.procedures.planesProvinciales;

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

public class DipucrInicializaAsuntoPlanesProvincialesRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrInicializaAsuntoPlanesProvincialesRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {		
		try{
			logger.info("INICIO - " + this.getClass().getName());
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			String numexp = rulectx.getNumExp();
			
			String anio = "";
			
			String consultaAnio = "WHERE NUMEXP = '"+numexp+"'";
			IItemCollection planesProvinciales = entitiesAPI.queryEntities("DIPUCRPLANESPROVINCIALES", consultaAnio);
			Iterator it1 = (Iterator) planesProvinciales.iterator();
			if(it1.hasNext()){
				anio = ((IItem)it1.next()).getString("ANIO");
			}
			if(anio == null){
				anio = "";
			}
			
			
			IItem iPropuestaExp = ExpedientesUtil.getExpediente(cct, numexp);
			if(iPropuestaExp != null){
				iPropuestaExp.set("ASUNTO", "Plan Provincial de Cooperación a las Obras y Servicios Municipales y de Carreteras y Plan Complementario "+anio );
				iPropuestaExp.store(cct);
			}
			logger.info("FIN - " + this.getClass().getName());
		}
		catch(ISPACRuleException e){
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (ISPACException e) {
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}

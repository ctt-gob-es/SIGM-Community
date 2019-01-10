package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;


public class InicializaSesionRule implements IRule {
	
	protected static final Logger logger = Logger.getLogger(InicializaSesionRule.class);
	
	protected String STR_Organo = "";
	protected String STR_Area = "";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        if(StringUtils.isEmpty(STR_Organo)){
	        	IItem itemExpediente = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
				int pcdId = itemExpediente.getInt("ID_PCD");
				IItem itemProcedimiento = procedureAPI.getProcedureById(pcdId);
				String sOrganoArea = itemProcedimiento.getString("CTPROCEDIMIENTOS:OBJETO");
				
				if (StringUtils.isEmpty(sOrganoArea)){
					rulectx.setInfoMessage("Se ha producido un error al iniciar la comisión.\n" + 
							"Es necesario configurar el Órgano en la definición del procedimiento.\n" +
							"Hable con su administrador.");
					return false;
				}
				else{
					String [] arrOrganoArea = sOrganoArea.split("-");
					STR_Organo = arrOrganoArea[0];
					
					if (arrOrganoArea.length > 1){
						STR_Area = arrOrganoArea[1];
					}
				}
	        }
	        
	        IItem sesion = entitiesAPI.createEntity("SECR_SESION", rulectx.getNumExp());
	        String numconv = SecretariaUtil.createNumConvocatoria(rulectx, STR_Organo, STR_Area);
	        sesion.set("NUMCONV", numconv);	        
	        sesion.set("ORGANO", STR_Organo);
	        sesion.set("AREA", STR_Area);
	        sesion.store(cct);
	        SecretariaUtil.createPropuestaAprobacionActaAnterior(rulectx);
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	logger.error("Error numexp " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error numexp " + rulectx.getNumExp() + " - " + e.getMessage(), e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}
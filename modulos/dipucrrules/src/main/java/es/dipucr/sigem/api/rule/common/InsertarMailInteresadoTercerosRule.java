package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

@Deprecated
public class InsertarMailInteresadoTercerosRule implements IRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
    {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
    	return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {        
    	String numexp = rulectx.getNumExp();
    	
    	try{ 
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesflowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IThirdPartyAPI thirdpartyAPI = cct.getAPI().getThirdPartyAPI();
	        
	        IItem itemExpedient = entitiesAPI.getExpedient(numexp);
	        String nif = itemExpedient.getString("NIFCIFTITULAR");
	        String email = itemExpedient.getString("DIRECCIONTELEMATICA");
	        
	        if (!StringUtils.isEmpty(email)){
		        IThirdPartyAdapter [] arrTerceros = thirdpartyAPI.lookup(nif);
		        
		        for (IThirdPartyAdapter tercero : arrTerceros){
		        	
		        	IElectronicAddressAdapter defaultMail = tercero.getDefaultDireccionElectronica();
	
		        	if (null == defaultMail){
		        		int idPerson = Integer.valueOf(tercero.getIdExt());
		        		thirdpartyAPI.insertDefaultEmail(idPerson, email);
		        	}
		        }
	        }
	        
	        return true;
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al avanzar fase del expediente. " +
	        		" Numexp:" + numexp, e);
	    } 
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {
    }

}

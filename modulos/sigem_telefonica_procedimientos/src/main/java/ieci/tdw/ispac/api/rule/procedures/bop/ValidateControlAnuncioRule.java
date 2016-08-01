package ieci.tdw.ispac.api.rule.procedures.bop;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

/**
 * 
 * @author teresa
 * @date 09/03/2010
 * @propósito Valida que en el trámite de Control del anuncio se haya asignado valor a los campos de Sumario, Coste y Tipo de facturación
 * de la entidad Solicitud BOP.
 */
public class ValidateControlAnuncioRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			
			//Comprobar si los campos Sumario, Coste y Tipo de facturación tienen asignado valor
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
	        IItem exp = null;
	        String sumario = null;
	        String coste = null;
	        String tipoFacturacion = null;
	        String numExp = rulectx.getNumExp();
	        String strQuery = "WHERE NUMEXP='" + numExp + "'";
	        IItemCollection collExps = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
	        Iterator itExps = collExps.iterator();
	        if (itExps.hasNext()){
	        	exp = (IItem)itExps.next();
	        	sumario = exp.getString("SUMARIO");
	        	coste = exp.getString("COSTE");
	        	tipoFacturacion = exp.getString("TIPO_FACTURACION");
	        	if (sumario==null || sumario.equals("") || coste==null || coste.equals("") || tipoFacturacion==null || tipoFacturacion.equals("")){
					rulectx.setInfoMessage("No se puede terminar el trámite ya que no se han completado los campos Sumario, Coste y" +
												" Tipo de facturación de la entidad Solicitud BOP");
					return false;
	        	}else{
	        		return true;
	        	}
			
	        }else{
	        	rulectx.setInfoMessage("No se puede terminar el trámite ya que no se han completado los campos Sumario, Coste y" +
											" Tipo de facturación de la entidad Solicitud BOP");
				return false;
	        }
		
		}catch (Exception e){
			try {
				throw new ISPACInfo("Se ha producido un error al comprobar los campos Sumario, Coste y" +
											" Tipo de facturación de la entidad Solicitud BOP");
			} catch (ISPACInfo e1) {
				e1.printStackTrace();
			}
		}
		return true;

	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

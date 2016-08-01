package aww.sigem.expropiaciones.rule.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.ExpropiacionesUtil;


public class TransferenciaOConsignaTagRule implements IRule {

	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(TransferenciaOConsignaTagRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			String marcar = rulectx.get("marcar");
			
			//logger.warn("Comienzo a ejecutar TransferenciaOConsignaTagRule");
			//logger.warn("marcar = "+marcar);
			if (marcar.equals("si")){					
				String tipo = rulectx.get("tipo");
				//logger.warn("tipo = "+tipo);
				// Tipo 1 para transferencia.
				if (tipo.equals("1")){
					if (ExpropiacionesUtil.campoTablaEEF("FECHA_TRANSFERENCIA", rulectx)!=null){
						return "X";
					}else{
						return "";
					}
				}
				// Tipo 2 para consigna.				
				else if (tipo.equals("2")){
					if (ExpropiacionesUtil.campoTablaEEF("FECHA_CONSIGNA", rulectx)!=null){
						return "X";
					}else{
						return "";
					}
				}				
			}
			else{
				if (ExpropiacionesUtil.campoTablaEEF("FECHA_TRANSFERENCIA", rulectx)!=null)	{	
					//logger.warn("es una transferencia ");
					return ExpropiacionesUtil.campoTablaEEF("FECHA_TRANSFERENCIA", rulectx);				
				}else {
					//logger.warn("es una consigna ");
					return ExpropiacionesUtil.campoTablaEEF("FECHA_CONSIGNA", rulectx);
				}	
			}
			
			//logger.warn("Fin TransferenciaOConsignaTagRule");
			return "";
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}
}

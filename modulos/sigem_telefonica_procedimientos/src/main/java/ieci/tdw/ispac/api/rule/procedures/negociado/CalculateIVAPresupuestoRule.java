package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @proposito Calcula el IVA (en función del porcentaje de iva seleccionado) y el presupuesto con IVA (presupuesto_licitacion)
 * a partir del presupuesto base (importe_sin_iva) y otros impuestos.
 * 
 */
public class CalculateIVAPresupuestoRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IItem item = rulectx.getItem();
			
			double importeSinIva = item.getDouble("IMPORTE_SIN_IVA");
			
			double tantoPorUnoIva = 0.0;
			
			try{
				tantoPorUnoIva = Double.parseDouble(item.getString("PORCENTAJE_IVA"))/100;
			}catch (Exception e){
				throw new ISPACInfo("El formato del porcentaje del iva es incorrecto");
			}
			double iva = importeSinIva*tantoPorUnoIva;
			
			double otrosImpuestos = item.getDouble("OTROS_IMPUESTOS");
			
			double presupuestoLicitacion = importeSinIva + iva + otrosImpuestos;
			
			item.set("IVA", iva);
			
			item.set("PRESUPUESTO_LICITACION",  presupuestoLicitacion);
			
			return null;
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}
}
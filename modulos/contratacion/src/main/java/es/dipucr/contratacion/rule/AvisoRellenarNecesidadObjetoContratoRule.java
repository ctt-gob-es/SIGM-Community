package es.dipucr.contratacion.rule;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class AvisoRellenarNecesidadObjetoContratoRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean rellenadoPestana = true;
		String query = "NUMEXP = '"+rulectx.getNumExp()+"'";
		StringBuilder mensaje = new StringBuilder();
		Iterator<IItem> itPeticion = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_PETICION", query);
		while(itPeticion.hasNext()){
			IItem peticion = itPeticion.next();
			try {
				if(StringUtils.isEmpty(peticion.getString("MOTIVO_PETICION"))){
					rellenadoPestana = false;
					mensaje.append("Motivo de la Petición;");
				}
				if(StringUtils.isEmpty(peticion.getString("TIPO_CONTRATO"))){
					rellenadoPestana = false;
					mensaje.append("Tipo de Contrato;");
				}
				if(StringUtils.isEmpty(peticion.getString("PRESUPUESTO"))){
					rellenadoPestana = false;
					mensaje.append("Presupuesto (Si no conoce el presupuesto especifique a 0);");
				}
				if(StringUtils.isEmpty(peticion.getString("IVA"))){
					rellenadoPestana = false;
					mensaje.append("IVA (Si no conoce el I.V.A especifique a 0);");
				}
				if(!rellenadoPestana){
					rulectx.setInfoMessage("Es obligatorio rellenar la pestaña 'Necesidad y Objeto del Contrato' campo: "+mensaje.toString());
				}
			} catch (ISPACException e) {
				 throw new ISPACRuleException("Error. Numexp:" + rulectx.getNumExp() + " - "+ e.getMessage(), e);
			}
		}
		return rellenadoPestana;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

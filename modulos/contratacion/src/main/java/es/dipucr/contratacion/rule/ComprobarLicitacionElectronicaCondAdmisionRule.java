package es.dipucr.contratacion.rule;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class ComprobarLicitacionElectronicaCondAdmisionRule implements IRule{
	
	protected static final Logger LOGGER = Logger.getLogger(ComprobarLicitacionElectronicaCondAdmisionRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean guardarEntidad = true;
		StringBuffer texto = new StringBuffer("");
		try {				
			IItem datosFormulario = rulectx.getItem();
			if(StringUtils.isNotEmpty(datosFormulario.getString("CAPAC_CONTRAT"))){
				if(StringUtils.isEmpty(datosFormulario.getString("CAP_CONT_VAL_TEXT")) && StringUtils.isEmpty(datosFormulario.getString("CAP_CONT_AUTORIZA")) && StringUtils.isEmpty(datosFormulario.getString("CAP_CONT_DOC"))){
					guardarEntidad = false;
					texto.append(" Es necesario en la 'Capacidad de Contratar' rellenar la acreditación seleccionando al menos uno.");
				}
			}
			
			if(!guardarEntidad && StringUtils.isNotEmpty(texto.toString())){
				rulectx.setInfoMessage(texto.toString());
			}
			
		} catch (ISPACException e) { 
			LOGGER.error("Error en el expediente. "+rulectx.getNumExp()+", en la pestaña Informacion Empresa "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente. "+rulectx.getNumExp()+", en la pestaña Informacion Empresa "+e.getMessage(), e);
		}
		return guardarEntidad;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

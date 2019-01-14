package es.dipucr.contratacion.rule;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class ComprobarLicitacionElectronicaSolvTecnRule implements IRule{
	
	/** Logger de la clase. */
	protected static final Logger LOGGER = Logger.getLogger(ComprobarLicitacionElectronicaSolvTecnRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean guardarEntidad = true;
		StringBuffer texto = new StringBuffer("");
		try {				
			boolean tecnica = true;
			IItem datosFormulario = rulectx.getItem();
			if(StringUtils.isNotEmpty(datosFormulario.getString("CRIT_SOLVENCIA"))){
				if(StringUtils.isEmpty(datosFormulario.getString("SOLV_TEC_LIC_VALOR_TEXTO")) && StringUtils.isEmpty(datosFormulario.getString("SOLV_TEC_CONSULTA_TELE_SIST")) && StringUtils.isEmpty(datosFormulario.getString("SOLV_TEC_LIC_DOCUM"))){
					tecnica = false;
					texto.append(" Es necesario en la 'Criterio de Solvencia Técnica' rellenar la acreditación seleccionando al menos uno.");
				}
			}
			
			boolean economica = true;
			if(StringUtils.isNotEmpty(datosFormulario.getString("CRIT_SOLVENCIA_ECO"))){
				if(StringUtils.isEmpty(datosFormulario.getString("SOLV_ECO_LIC_VALOR_TEXTO")) && StringUtils.isEmpty(datosFormulario.getString("SOLV_ECO_CONSULTA_TELE_SIST")) && StringUtils.isEmpty(datosFormulario.getString("SOLV_ECO_LIC_DOCUM"))){
					economica = false;
					texto.append(" Es necesario en la 'Criterios de solvencia económica y financiera' rellenar la acreditación seleccionando al menos uno.");
				}
			}
			
			guardarEntidad = tecnica && economica;
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

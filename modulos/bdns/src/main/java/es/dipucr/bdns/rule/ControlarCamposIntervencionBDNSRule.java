package es.dipucr.bdns.rule;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.objetos.EntidadConvocatoria;

public class ControlarCamposIntervencionBDNSRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(ControlarCamposIntervencionBDNSRule.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try {
			//Obtenemos la convocatoria, su id y su instrumento (son los mismos para todas las concesiones)
			EntidadConvocatoria convocatoria = BDNSDipucrFuncionesComunes.cargaEntidadConvocatoria(rulectx);
			
			//Datos económicos
			if (StringUtils.isEmpty(convocatoria.getAplicacion())){
				rulectx.setInfoMessage("Debe rellenar el campo 'Aplicación presupuestaria' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			if (StringUtils.isEmpty(convocatoria.getEjercicio())){
				rulectx.setInfoMessage("Debe rellenar el campo 'Ejercicio' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
			try{
				Integer.valueOf(convocatoria.getEjercicio());
			}
			catch(NumberFormatException ex){
				rulectx.setInfoMessage("El campo 'Ejercicio' en la pestaña 'Base de Datos Nacional de Subvenciones'"
						+ " no tiene un formato numérico correcto");
				return false;
			}
			if (StringUtils.isEmpty(convocatoria.getRefContable())){
				rulectx.setInfoMessage("Debe rellenar el campo 'Referencia Contable' en la pestaña 'Base de Datos Nacional de Subvenciones'");
				return false;
			}
		
		} catch (Exception e) {
			String error = "Error al controlar los campos que debe rellenar intervención: " + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		
		return true;
	}

}

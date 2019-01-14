package es.dipucr.contratacion.rule;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class ComprobarLicitacionElectronicaInformacionEmpresaRule implements IRule{
	
	/** Logger de la clase. */
	protected static final Logger LOGGER = Logger.getLogger(ComprobarLicitacionElectronicaInformacionEmpresaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean guardarEntidad = true;
		StringBuffer texto = new StringBuffer("");
		try {				
			boolean clasificacion = true;
			IItem datosFormulario = rulectx.getItem();
			if(StringUtils.isNotEmpty(datosFormulario.getString("CLAS_EMP"))){
				if(StringUtils.isEmpty(datosFormulario.getString("LIC_VALOR_TEXTO")) && StringUtils.isEmpty(datosFormulario.getString("CONSULTA_TELE_SIST")) && StringUtils.isEmpty(datosFormulario.getString("LIC_DOCUM"))){
					clasificacion = false;
					texto.append(" Es necesario en la 'Clasificación Empresarial' rellenar la acreditación seleccionando al menos uno.");
				}
			}
			
			boolean tituloHabilitante = true;
			if(StringUtils.isNotEmpty(datosFormulario.getString("TITULO_HABILITANTE"))){
				if(StringUtils.isEmpty(datosFormulario.getString("TIT_HAB_LIC_VALOR_TEXTO")) && StringUtils.isEmpty(datosFormulario.getString("TIT_HAB_CONSULTA_TELE_SIST")) && StringUtils.isEmpty(datosFormulario.getString("TIT_HAB_LIC_DOCUM"))){
					tituloHabilitante = false;
					texto.append(" Es necesario en la 'Habilitación empresarial o profesional' rellenar la acreditación seleccionando al menos uno.");
				}
			}
			
			boolean experiencia = true;
			if(StringUtils.isNotEmpty(datosFormulario.getString("OPERATINGYEARSQUANTITY"))){
				if(StringUtils.isEmpty(datosFormulario.getString("RECURSOS_LIC_VALOR_TEXTO")) && StringUtils.isEmpty(datosFormulario.getString("RECURSOS_CONSULTA_TELE_SIST")) && StringUtils.isEmpty(datosFormulario.getString("RECURSOS_LIC_DOCUM"))){
					experiencia = false;
					texto.append(" Es necesario en la 'Experiencia' rellenar la acreditación seleccionando al menos uno.");
				}
			}
			
			boolean empleados = true;
			if(StringUtils.isNotEmpty(datosFormulario.getString("EMPLOYEEQUANTITY"))){
				if(StringUtils.isEmpty(datosFormulario.getString("EMPLE_LIC_VALOR_TEXTO")) && StringUtils.isEmpty(datosFormulario.getString("EMPLE_CONSULTA_TELE_SIST")) && StringUtils.isEmpty(datosFormulario.getString("EMPLE_LIC_DOCUM"))){
					empleados = false;
					texto.append(" Es necesario en la 'Empleados' rellenar la acreditación seleccionando al menos uno.");
				}
			}
			
			boolean ccvv = true;
			if(StringUtils.isNotEmpty(datosFormulario.getString("CVV_DESCRIPCION"))){
				if(StringUtils.isEmpty(datosFormulario.getString("CVV_LIC_VALOR_TEXTO")) && StringUtils.isEmpty(datosFormulario.getString("CVV_CONSULTA_TELE_SIST")) && StringUtils.isEmpty(datosFormulario.getString("CVV_LIC_DOCUM"))){
					ccvv = false;
					texto.append(" Es necesario en la 'Requerimiento CCVV' rellenar la acreditación seleccionando al menos uno.");
				}
			}
			guardarEntidad = clasificacion && tituloHabilitante && experiencia && empleados && ccvv;
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

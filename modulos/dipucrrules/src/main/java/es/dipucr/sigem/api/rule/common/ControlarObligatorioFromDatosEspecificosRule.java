package es.dipucr.sigem.api.rule.common;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

/**
 * [dipucr-Felipe #439]
 * @since 24.02.17
 */
public class ControlarObligatorioFromDatosEspecificosRule implements IRule {
	
	public static final String SEPARATOR = ";";
	
	private static final Logger LOGGER = Logger.getLogger(ControlarObligatorioFromDatosEspecificosRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			String numexp = rulectx.getNumExp();
			
			String otrosDatos = TramitesUtil.getDatosEspecificosOtrosDatos(cct, rulectx.getTaskProcedureId());
			
			if (!StringUtils.isEmpty(otrosDatos)){
				String[] arrControlar = otrosDatos.split(SEPARATOR);
				
				if (arrControlar.length != 3){
					rulectx.setInfoMessage("Error de configuración. El campo 'Datos/Específicos - Otros Datos' del"
							+ " trámite debe tener el formato 'ENTIDAD;CAMPO;MENSAJE'. Ponga una incidencia a los administradores.");
					return false;
				}
				else{
					String nombreEntidad = arrControlar[0];
					String nombreCampo = arrControlar[1];
					String mensaje = arrControlar[2];
					
					IItemCollection colEntidad = entitiesAPI.getEntities(nombreEntidad, numexp);
					
					if (colEntidad.toList().size() == 0){
						rulectx.setInfoMessage(mensaje);
						return false;
					}
					else{
						IItem itemEntidad = colEntidad.value();
						String campo = itemEntidad.getString(nombreCampo);
						
						if (StringUtils.isEmpty(campo)){
							rulectx.setInfoMessage(mensaje);
							return false;
						}
					}
				}
			}
			else{
				rulectx.setInfoMessage("Error de configuración. El campo 'Datos/Específicos - Otros Datos' del"
						+ " trámite debe tener el formato 'ENTIDAD;CAMPO;MENSAJE' y está vacío. Ponga una incidencia a los administradores.");
				return false;
			}
		}
		catch (Exception ex){
			String error = "Error en la regla ControlarObligatorioFromDatosEspecificosRule";
			LOGGER.error(error, ex);
			throw new ISPACRuleException(error, ex);
		}
				
        return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}

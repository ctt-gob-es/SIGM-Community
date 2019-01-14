package es.dipucr.bdns.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.objetos.EntidadConvocatoria;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ControlarConcesionesPagadasBDNSRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(ControlarConcesionesPagadasBDNSRule.class);
	
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
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			EntidadConvocatoria convocatoria = BDNSDipucrFuncionesComunes.cargaEntidadConvocatoria(rulectx);
			
			if (null != convocatoria && !StringUtils.isEmpty(convocatoria.getIdConvocatoria())){
				
				StringBuffer sbQuery = new StringBuffer();
				sbQuery.append(" WHERE ID_CONVOCATORIA='" + convocatoria.getIdConvocatoria() + "'");
				sbQuery.append(" AND NUMEXP='" + rulectx.getNumExp() + "'");
				IItemCollection colConcesiones = entitiesAPI.queryEntities("BDNS_IGAE_CONCESION", sbQuery.toString());
				if (colConcesiones.toList().size() == 0){
					rulectx.setInfoMessage("No es posible realizar esta acción.\\n"
							+ "No se han enviado las concesiones a la Base de Datos Nacional de Concesiones.\\n"
							+ "Debe crear, comprobar y terminar el trámite 'Publicación ALTA Concesiones BDNS'");
					return false;
				}
				else{
					sbQuery.append(" AND PAGADA='" + Constants.VALIDACION.NO + "'");
					colConcesiones = entitiesAPI.queryEntities("BDNS_IGAE_CONCESION", sbQuery.toString());
					if(colConcesiones.toList().size() > 0){
						rulectx.setInfoMessage("No es posible realizar esta acción.\\nTodas las concesiones enviadas a la BDNS deben estar pagadas.\\n"
								+ "Debe crear el trámite 'Publicación ALTA Pagos BDNS' y, si ya está creado, esperar a que se realicen los pagos en Intervención/Tesorería.\\n"
								+ "Los pagos serán recuperados automáticamente por la aplicación de la Contabilidad cuando sean realizados.");
						return false;
					}
				}
			}
		
		} catch (Exception e) {
			String error = "Error al comprobar que existen concesiones y estas están pagadas " + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		
		return true;
	}

}

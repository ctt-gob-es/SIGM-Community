package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Date;
import java.util.List;

import es.dipucr.sigem.api.rule.common.utils.QueryUtils;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [eCenpri-Felipe ticket #1039] Regla que comprueba que el decreto
 * relacionado del expediente esté firmado.
 * 
 * @author Felipe
 * @since 23.01.14
 */
public class DipucrCompruebaDecRelacionadoFirmadoRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	/**
	 * Asignación del número de licencia Ejecutamos el código del execute en el
	 * método validate para poder mostrar los errores.
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		String numexp = null;
		IClientContext cct = rulectx.getClientContext();
		IEntitiesAPI entitiesAPI = null;

		// Código específico
		try {
			IInvesflowAPI invesflowAPI = cct.getAPI();
			entitiesAPI = invesflowAPI.getEntitiesAPI();
			numexp = rulectx.getNumExp();
			boolean bEncontrado = false;

			// Obtenemos el expediente de decreto (última resolución de los
			// relacionados)
			String query = "WHERE NUMEXP_PADRE = '" + numexp + "' " + QueryUtils.EXPRELACIONADOS.ORDER_DESC;
			IItemCollection collection = entitiesAPI.queryEntities(
					Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, query);

			@SuppressWarnings("unchecked")
			List<IItem> listRelacionados = collection.toList();
			if (collection.toList().size() == 0) {
				rulectx.setInfoMessage("El expediente no tiene ningún decreto relacionado");
				return false;
			}
			else {
				IItem itemRelacion = null;
				String numexpHijo = null;
				IItemCollection colDecreto = null;

				for (int i = 0; i < listRelacionados.size() && !bEncontrado; i++) {
					
					itemRelacion = listRelacionados.get(i);
					numexpHijo = itemRelacion.getString("NUMEXP_HIJO");

					//Todos los expedientes de tipo decreto, tendrán entidad sgd_decreto
					colDecreto = entitiesAPI.getEntities("SGD_DECRETO", numexpHijo);

					if (colDecreto.toList().size() > 0) {
						IItem itemDatosDecreto = (IItem) colDecreto.iterator().next();
						Date fechaDecreto = itemDatosDecreto.getDate("FECHA_DECRETO");
						String numDecreto = itemDatosDecreto.getString("NUMERO_DECRETO");

						if (null == fechaDecreto || StringUtils.isEmpty(numDecreto)) {
							rulectx.setInfoMessage("El expediente de decreto "
									+ "relacionado no tiene número de decreto asignado");
							return false;
						}
						else{
							bEncontrado = true;
						}
					}
				}
			}
			
			if (!bEncontrado) {
				rulectx.setInfoMessage("El expediente no tiene ningún decreto relacionado");
				return false;
			}

		}
		catch (ISPACException e) {
			throw new ISPACRuleException("Error al comprobar el decreto relacionado", e);
		}
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

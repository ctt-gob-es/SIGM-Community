package es.dipucr.sigem.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class DipucrCopyLiquidacionesIBIRule implements IRule {

	private static final Logger logger = Logger
			.getLogger(DipucrCopyLiquidacionesIBIRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

			String strEntidadSolicitud = "REC_BENEFICIO_IBI";
			String strEntidadLiquidaciones = "REC_DAT_LIQ";

			logger.info("Buscando la solicitud...");
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
			IItemCollection col1 = entitiesAPI.queryEntities(
					strEntidadSolicitud, strQuery);
			Iterator it1 = col1.iterator();
			if (it1.hasNext()) {
				IItem solicitud = (IItem) it1.next();

				logger.info("Buscando liquidaciones...");
				strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
				IItemCollection col2 = entitiesAPI.queryEntities(
						strEntidadLiquidaciones, strQuery);
				Iterator it2 = col2.iterator();
				if (!it2.hasNext()) {
					String strYears = solicitud.getString("YEARS");

					for (int i = 1; i <= 20; i++) {
						logger.info("Indice: " + i);
						String strIndex = String.valueOf(i);
						String key_mun = "MUN_" + 1;
						String key_liq = "LIQ_" + strIndex;
						String key_ref1 = "REF1_" + strIndex;
						String key_ref2 = "REF2_" + strIndex;
						String key_ref3 = "REF3_" + strIndex;
						String key_ref4 = "REF4_" + strIndex;
						String key_sit = "SIT_" + strIndex;
						String strLiq = solicitud.getString(key_liq);
						if (strLiq != null && strLiq.length() > 0) {
							// Copio los datos de la solicitud a la entidad de
							// liquidaciones
							logger.info("Copiando...");
							IItem item = entitiesAPI.createEntity(
									strEntidadLiquidaciones, rulectx
											.getNumExp());

							item.set("LIQUIDACION", strLiq);

							String strMun = solicitud.getString(key_mun);
							if (strMun != null) {
								item.set("MUNICIPIO", strMun);
							}

							String strSit = solicitud.getString(key_sit);
							if (strSit != null) {
								item.set("SITUACION", strSit);
							}

							if (strYears != null) {
								item.set("YEARS", strYears);
							}

							String strRef1 = solicitud.getString(key_ref1);
							String strRef2 = solicitud.getString(key_ref2);
							String strRef3 = solicitud.getString(key_ref3);
							String strRef4 = solicitud.getString(key_ref4);
							if (strRef1 != null && strRef2 != null
									&& strRef3 != null && strRef4 != null) {
								item.set("REF_CAT", strRef1 + "-" + strRef2
										+ "-" + strRef3 + "-" + strRef4);
							}

							logger.info("Guardando...");
							item.store(cct);
						}
					}
					logger.info("Copia terminada");
				} else {
					ArrayList<String> liquidaciones = new ArrayList<String>();
					while (it2.hasNext()) {
						IItem liq = (IItem) it2.next();
						liquidaciones.add(liq.getString("LIQUIDACION"));
					}
					String strYears = solicitud.getString("YEARS");

					for (int i = 1; i <= 20; i++) {
						logger.info("Indice: " + i);
						String strIndex = String.valueOf(i);
						String key_mun = "MUN_" + 1;
						String key_liq = "LIQ_" + strIndex;
						String key_ref1 = "REF1_" + strIndex;
						String key_ref2 = "REF2_" + strIndex;
						String key_ref3 = "REF3_" + strIndex;
						String key_ref4 = "REF4_" + strIndex;
						String key_sit = "SIT_" + strIndex;
						String strLiq = solicitud.getString(key_liq);

						if (strLiq != null && strLiq.length() > 0){
							if (!liquidaciones.contains(strLiq)){
								// Copio los datos de la solicitud a la entidad
								// de liquidaciones
								logger.info("Copiando...");
								IItem item = entitiesAPI.createEntity(
										strEntidadLiquidaciones, rulectx
												.getNumExp());

								item.set("LIQUIDACION", strLiq);

								String strMun = solicitud.getString(key_mun);
								if (strMun != null) {
									item.set("MUNICIPIO", strMun);
								}

								String strSit = solicitud.getString(key_sit);
								if (strSit != null) {
									item.set("SITUACION", strSit);
								}

								if (strYears != null) {
									item.set("YEARS", strYears);
								}

								String strRef1 = solicitud.getString(key_ref1);
								String strRef2 = solicitud.getString(key_ref2);
								String strRef3 = solicitud.getString(key_ref3);
								String strRef4 = solicitud.getString(key_ref4);
								if (strRef1 != null && strRef2 != null
										&& strRef3 != null && strRef4 != null) {
									item.set("REF_CAT", strRef1 + "-" + strRef2
											+ "-" + strRef3 + "-" + strRef4);
								}

								logger.info("Guardando...");
								item.store(cct);
							}
						}
					}
				}
			} else {
				logger.info("No se encuentra la solicitud");
			}
		} catch (Exception e) {
			throw new ISPACRuleException("Error en CopyLiquidacionesRule.", e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
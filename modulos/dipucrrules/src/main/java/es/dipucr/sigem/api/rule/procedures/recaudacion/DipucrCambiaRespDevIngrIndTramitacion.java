package es.dipucr.sigem.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

public class DipucrCambiaRespDevIngrIndTramitacion implements IRule {
	private static final Logger LOGGER = Logger.getLogger(DipucrCambiaRespDevIngrIndTramitacion.class);

	private String opcion;

	private final String MOTIVO_1_V = "3-470";
	private final String MOTIVO_2_V = "3-478";
	private final String MOTIVO_E = "3-471";
	private final String MOTIVO_3 = "3-472";
	private final String MOTIVO_4 = "3-473";
	private final String MOTIVO_5 = "3-474";
	private final String MOTIVO_6 = "3-475";
	private final String MOTIVO_7 = "3-476";
	private final String MOTIVO_8 = "3-477";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		LOGGER.info("INICIO - " + this.getClass().getName());
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			// Obtenemos la opción que se ha indicado en la solicitud haya participado

			String strQuery = "WHERE NUMEXP ='" + rulectx.getNumExp() + "'";

			IItemCollection datos = entitiesAPI.queryEntities("REC_DEVOLUCION", strQuery);

			if (datos.iterator().hasNext()) {
				opcion = ((IItem) datos.iterator().next()).getString("MOTIVO");
			} else{
				opcion = "";
			}

			if (StringUtils.isNotEmpty(opcion)) {
				IItem fase = (IItem) entitiesAPI.queryEntities("SPAC_FASES", strQuery).iterator().next();

				if (opcion.equals("MOTIVO_1_V")) {
					fase.set("ID_RESP", MOTIVO_1_V);
				} else if (opcion.equals("MOTIVO_1_E")) {
					fase.set("ID_RESP", MOTIVO_E);
				} else if (opcion.equals("MOTIVO_2_V")) {
					fase.set("ID_RESP", MOTIVO_2_V);
				} else if (opcion.equals("MOTIVO_2_E")) {
					fase.set("ID_RESP", MOTIVO_E);
				} else if (opcion.equals("MOTIVO_3")) {
					fase.set("ID_RESP", MOTIVO_3);
				} else if (opcion.equals("MOTIVO_4")) {
					fase.set("ID_RESP", MOTIVO_4);
				} else if (opcion.equals("MOTIVO_5")) {
					fase.set("ID_RESP", MOTIVO_5);
				} else if (opcion.equals("MOTIVO_6")) {
					fase.set("ID_RESP", MOTIVO_6);
				} else if (opcion.equals("MOTIVO_7")) {
					fase.set("ID_RESP", MOTIVO_7);
				} else if (opcion.equals("MOTIVO_8")) {
					fase.set("ID_RESP", MOTIVO_8);
				}
				fase.store(cct);
			}
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("FIN - " + this.getClass().getName());
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}
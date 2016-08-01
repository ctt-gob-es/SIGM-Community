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

import org.apache.log4j.Logger;

public class DipucrCambiaRespDevIngrIndFaseArchivo implements IRule {
	private static final Logger logger = Logger.getLogger(DipucrCambiaRespDevIngrIndFaseArchivo.class);

	private String opcion;

	// Mamen Alcaide
	private String id_mamen = "3-101";
	// Alberto Sánchez
	private String id_alberto = "3-100";
	// Paco Golderos
	private String id_pacog = "3-101";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			// Obtenemos la opción que se ha indicado en la solicitud haya
			// participado

			String strQuery = "WHERE NUMEXP ='" + rulectx.getNumExp() + "'";

			IItemCollection datos = entitiesAPI.queryEntities("REC_DEVOLUCION",
					strQuery);

			if (datos.iterator().hasNext()) {
				opcion = ((IItem) datos.iterator().next()).getString("MOTIVO");
			} else
				opcion = "";

			if (opcion != null && !opcion.equals("")) {
				IItem fase = (IItem) entitiesAPI.queryEntities("SPAC_FASES",
						strQuery).iterator().next();

				if (opcion.equals("MOTIVO_1_V")) {
					// Mamen Alcaide
					fase.set("ID_RESP", id_mamen);
				} else if (opcion.equals("MOTIVO_1_E")) {
					// Alberto Sánchez
					fase.set("ID_RESP", id_alberto);
				} else if (opcion.equals("MOTIVO_2_V")) {
					// Paco Golderos
					fase.set("ID_RESP", id_pacog);
				} else if (opcion.equals("MOTIVO_2_E")) {
					// Alberto Sánchez
					fase.set("ID_RESP", id_alberto);
				} else if (opcion.equals("MOTIVO_3")) {
					// Alberto Sánchez
					fase.set("ID_RESP", id_alberto);
				} else if (opcion.equals("MOTIVO_4")) {
					// Paco Golderos
					fase.set("ID_RESP", id_pacog);
				} else if (opcion.equals("MOTIVO_5")) {
					// Paco Golderos
					fase.set("ID_RESP", id_pacog);
				} else if (opcion.equals("MOTIVO_6")) {
					// Paco Golderos
					fase.set("ID_RESP", id_pacog);
				} else if (opcion.equals("MOTIVO_7")) {
					// Alberto Sánchez
					fase.set("ID_RESP", id_alberto);
				} else if (opcion.equals("MOTIVO_8")) {
					// Paco Golderos
					fase.set("ID_RESP", id_pacog);
				}
				fase.store(cct);
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("FIN - " + this.getClass().getName());
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}
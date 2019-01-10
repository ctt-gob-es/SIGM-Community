package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispactx.TXTransaction;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class CierraTramitesYAvanzaFaseExpedientesPadreRule implements IRule {

	private static final Logger LOGGER = Logger.getLogger(CierraTramitesYAvanzaFaseExpedientesPadreRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Nada que hacer al cancelar
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		LOGGER.info("INICIO CierraTramitesYAvanzaFaseExpedientesPadreRule");
		try {
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IItemCollection decretoRechazado = null;
			String numDec = "";
			// Si es decreto comprobamos si no ha sido rechazado
			boolean esDecreto = esProcTipo(entitiesAPI,
					rulectx.getProcedureId(), "DECRETO");
			if (esDecreto) {
				// Si el decreto ha sido rechazado o se cierra el expediente sin
				// haber sido firmado no hacemos nada
				String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
				decretoRechazado = entitiesAPI.queryEntities(
						"SGD_RECHAZO_DECRETO", strQuery);
				IItemCollection decreto = entitiesAPI.queryEntities(
						"SGD_DECRETO", strQuery);
				Iterator itDecreto = decreto.iterator();
				if (itDecreto.hasNext()){
					numDec = ((IItem) itDecreto.next()).getString("NUMERO_DECRETO");
				}
			}

			if (!esDecreto || (esDecreto && (decretoRechazado == null || !decretoRechazado.next()) && numDec != null && !"".equals(numDec))) {
				String consultaSQL = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
				IItemCollection itemCollection = entitiesAPI.queryEntities(41, consultaSQL);

				// Si no tenemos expediente padre tampoco entramos
				if ((itemCollection != null) && (itemCollection.next())) {
					Iterator it = itemCollection.iterator();
					IItem item = null;

					String numexpPadre = "";
					while (it.hasNext()) {
						item = (IItem) it.next();
						numexpPadre = item.getString("NUMEXP_PADRE");
					}
					cierraTramitesyAvanzaFase(cct, entitiesAPI, numexpPadre);
				} else {
					LOGGER.info("NO Entramos");
				}
			}
			// Si es decreto rechazado
			else {
				String consultaSQL = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
				IItemCollection itemCollection = entitiesAPI.queryEntities(41, consultaSQL);

				// Si no tenemos expediente padre tampoco entramos
				if ((itemCollection != null) && (itemCollection.next())) {
					Iterator it = itemCollection.iterator();
					IItem item = null;

					String numexpPadre = "";
					while (it.hasNext()) {
						item = (IItem) it.next();
						numexpPadre = item.getString("NUMEXP_PADRE");
						cierraTramites(cct, entitiesAPI, numexpPadre);
					}
				} else {
					LOGGER.info("NO Entramos");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error al avanzar de fase el expediente padre o al cerrar el trámite, " + e.getMessage(), e);
			throw new ISPACRuleException(e);
		}
		LOGGER.info("FIN CierraTramitesYAvanzaFaseExpedientesPadreRule");
		return null;
	}

	private void cierraTramitesyAvanzaFase(ClientContext cct, IEntitiesAPI entitiesAPI, String numexpPadre)
			throws ISPACRuleException {
		try {
			cierraTramites(cct, entitiesAPI, numexpPadre);
			ExpedientesUtil.avanzarFase(cct, numexpPadre);
		} catch (Exception e) {
			LOGGER.error("Error al avanzar la fase de expediente padre: " + numexpPadre + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al avanzar la fase de expediente padre: " + numexpPadre + ". " + e.getMessage(), e);
		}
	}

	private void cierraTramites(ClientContext cct, IEntitiesAPI entitiesAPI, String numexpPadre)
			throws ISPACRuleException {
		ITXTransaction transaction;
		try {
			transaction = new TXTransaction(cct);
			String strQuery = "WHERE NUMEXP='" + numexpPadre + "'";
			IItemCollection collectionTrams = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery);
			Iterator<?> itTrams = collectionTrams.iterator();
			IItem tram = null;
			while (itTrams.hasNext()) {
				tram = (IItem) itTrams.next();
				int idTram = tram.getInt("ID");
				transaction.closeTask(idTram);
			}
		} catch (Exception e) {
			LOGGER.error("Error al cerrar el trámite. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al cerrar el trámite. " + e.getMessage(), e);
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	private boolean esProcTipo(IEntitiesAPI entitiesAPI, int idProcedimiento,
			String tipo) throws ISPACRuleException {

		boolean resultado = false;
		try {
			IItem catalogo = entitiesAPI.getEntity(
					SpacEntities.SPAC_CT_PROCEDIMIENTOS, idProcedimiento);

			int id_padre = catalogo.getInt("ID_PADRE");
			if (id_padre != -1) {
				resultado = esProcTipo(entitiesAPI, id_padre, tipo);
			} else {
				String nom_pcd = "";
				if (catalogo.getString("NOMBRE") != null) {
					nom_pcd = catalogo.getString("NOMBRE");
					if (nom_pcd.toUpperCase().indexOf(tipo.toUpperCase()) >= 0) {
						resultado = true;
					}
				}
			}
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException(
					"Error al comprobar el tipo de procedimiento", e);
		}
		return resultado;
	}
}
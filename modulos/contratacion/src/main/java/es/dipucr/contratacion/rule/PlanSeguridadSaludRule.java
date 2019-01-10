package es.dipucr.contratacion.rule;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class PlanSeguridadSaludRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(PlanSeguridadSaludRule.class);
	public static String relacion = "";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/					
			
			String codProcSeguridadYSalud = "PLAN-SEG-SALUD"; // Por defecto
			String codigoProcedimientoPadre = "";
			
			// Cogemos el código del procedimiento padre
			IItemCollection procedimientoPadre = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE ID = " + rulectx.getProcedureId());			
			IItem proc = (IItem) procedimientoPadre.toList().get(0);
			codigoProcedimientoPadre = proc.getString("COD_PCD");
			
			if(codigoProcedimientoPadre == null) {
					throw new ISPACRuleException("Error no encuentro procedimiento padre de Tramitación de Contrato",new ISPACRuleException());
			}			
	
			// Si el procedimiento es 'Tramitación de contrato Vías y obras' o 'Tramitación de Contrato de Arquitectura'
			// iniciaremos un expediente relacionado de sus propios 'Plan de Seguridad y Salud (de vías y obras o de arquitectura)'	
			if ("TRAM-CONT-ARQ".equalsIgnoreCase(codigoProcedimientoPadre)) {
				codProcSeguridadYSalud = "PLAN-SEG-SAL-ARQ";
			} else if ("TRAM-CONT-VyO".equalsIgnoreCase(codigoProcedimientoPadre)) {
				codProcSeguridadYSalud = "PLAN-SEG-SAL-VYO";
			}
			
			//Obtenemos el id del procedimiento 'Plan de seguridad y salud'
			IItemCollection procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE COD_PCD = '" + codProcSeguridadYSalud + "'");
			Iterator <IItem> procsIterator = procedimientosDelDepartamento.iterator();
			int idCtProcedimientoNuevo = 0;
			while(procsIterator.hasNext()){
				IItem procs = (IItem) procsIterator.next();
				idCtProcedimientoNuevo = procs.getInt("ID");
			}
			
			IItem numexpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteRelacionadoHijo(cct, idCtProcedimientoNuevo, rulectx.getNumExp(), relacion, true, null);		
			numexpHijo.set("ASUNTO", relacion);
			numexpHijo.store(cct);
			/**Fin del expediente de 'Plan de seguridad y salud'**/
			
			TramitesUtil.cargarObservacionesTramite(cct, true,rulectx.getNumExp(), rulectx.getTaskId(), " Exp.Relacionado: "+numexpHijo.getString("NUMEXP"));
			
		} catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
		return new Boolean (true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}

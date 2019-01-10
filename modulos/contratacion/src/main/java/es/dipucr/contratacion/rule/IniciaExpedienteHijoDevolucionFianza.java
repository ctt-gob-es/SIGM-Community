package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class IniciaExpedienteHijoDevolucionFianza implements IRule{
	
	public static final Logger logger = Logger.getLogger(IniciaExpedienteHijoDevolucionFianza.class);


	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			 /***********************************************************************/
			/**Inicio de expediente de 'Certificación de obra'**/
					
			//Obtenemos el id del procedimiento 'Certificación obra'
			IItemCollection procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE COD_PCD = 'DEVOL-FIANZA'");
			Iterator <IItem> procsIterator = procedimientosDelDepartamento.iterator();
			int idCtProcedimientoNuevo = 0;
			while(procsIterator.hasNext()){
				IItem procs = (IItem) procsIterator.next();
				idCtProcedimientoNuevo = procs.getInt("ID");
			}
			String relacion = "Devolucion Fianza - "+ FechasUtil.getFormattedDate(new Date());
			IItem numexpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteRelacionadoHijo(cct, idCtProcedimientoNuevo, rulectx.getNumExp(), relacion, true, null);		
			if(null!=numexpHijo){
				
				numexpHijo.set("ASUNTO", relacion);
				numexpHijo.store(cct);
				
				TramitesUtil.cargarObservacionesTramite(cct, true,rulectx.getNumExp(), rulectx.getTaskId(), relacion+" - Exp.Relacionado: "+numexpHijo.getString("NUMEXP"));
			}

		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}

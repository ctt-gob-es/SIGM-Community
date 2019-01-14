package es.dipucr.sigem.api.rule.procedures.personal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class IniciaExpedienteProvPuestoTrabajoRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(IniciaExpedienteProvPuestoTrabajoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean resultado = false;
		String query = "NUMEXP='"+rulectx.getNumExp()+"'";
		Iterator<IItem> itProc = ConsultasGenericasUtil.queryEntities(rulectx, "PERSONAL_PROC_ENTIDAD", query);
		if (itProc.hasNext()){
			IItem proce = itProc.next();
			try {
				if(proce.getString("NOMBRE")!=null) resultado = true;
				else rulectx.setInfoMessage("Debe seleccionar el procedimiento que va a iniciar, en la pestaña Procedimiento.");
			} catch (ISPACException e) {
				logger.error("Error al generar el procedimiento de Provisión de Puestos de Trabajo en el numexp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
				throw new ISPACRuleException("Error al generar el procedimiento de Provisión de Puestos de Trabajo en el numexp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			}
		}
		return resultado;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*****************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			/*****************************************************************/
			String query = "NUMEXP='"+rulectx.getNumExp()+"'";
			Iterator<IItem> itProc = ConsultasGenericasUtil.queryEntities(rulectx, "PERSONAL_PROC_ENTIDAD", query);
			String codProc = "";
			while (itProc.hasNext()){
				IItem proce = itProc.next();				
				if(proce.getString("NOMBRE")!=null) codProc = proce.getString("NOMBRE");
			}
			
			// Obtener el código de procedimiento para el número de expediente
			Map<String, String> params = new HashMap<String, String>();
			//params.put("COD_PCD", "PUEST-TRABAJO");
			params.put("COD_PCD", codProc);
	
			// Calculo el id de ese procedimiento a partir de la tabla
			// spac_ct_procedimientos
			String consulta = "WHERE COD_PCD = '"+codProc+"'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", consulta);
			Iterator<IItem> it = collection.iterator();
			int id_procedimiento = 0;
			while (it.hasNext()) {
				IItem procExpediente = (IItem) it.next();
				if(procExpediente.getInt("ID")>0)id_procedimiento = procExpediente.getInt("ID");	
			}
			if(id_procedimiento>0){
				// Crear el proceso del expediente
				int nIdProcess2 = tx.createProcess(id_procedimiento, params);
				IProcess process;
		
				process = invesflowAPI.getProcess(nIdProcess2);
		
				// Num exp nuevo.
				String numExpHijo = process.getString("NUMEXP");
				IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
		
				registro.set("NUMEXP_PADRE", rulectx.getNumExp());
				registro.set("NUMEXP_HIJO", numExpHijo);
				registro.set("RELACION", "Provisión de Puesto de Trabajo");
		
				registro.store(cct);
				
				IItem expedienteAntiguo = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
				IItem expedienteNuevo = ExpedientesUtil.getExpediente(cct, numExpHijo);
				expedienteNuevo.set(ExpedientesUtil.ASUNTO, expedienteAntiguo.getString("ASUNTO"));
				expedienteNuevo.store(cct);
		
				cct.endTX(true);

				// Importar participantes.
				ParticipantesUtil.importarParticipantes(cct, entitiesAPI, rulectx.getNumExp(), numExpHijo);
			}
			else{
				throw new ISPACRuleException("El id de procedimiento es erróneo. "+id_procedimiento+", contacte con el administrador");
			}
			
			
		} catch (ISPACException e) { 
			logger.error("Error al generar el procedimiento de Provisión de Puestos de Trabajo en el numexp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el procedimiento de Provisión de Puestos de Trabajo en el numexp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}


		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

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
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class IniciaExpedienteHijoLotesRule implements IRule{
	
	public static final Logger LOGGER = Logger.getLogger(IniciaExpedienteHijoLotesRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			/***********************************************************************/
			
			IItem expediente = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
			
			if(expediente!=null && StringUtils.isNotEmpty(expediente.getString(ExpedientesUtil.CODPROCEDIMIENTO))){
				//Obtenemos el id del procedimiento 'Certificación obra'
				IItemCollection procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE COD_PCD = '"+expediente.getString(ExpedientesUtil.CODPROCEDIMIENTO)+"'");
				Iterator <?> procsIterator = procedimientosDelDepartamento.iterator();
				int idCtProcedimientoNuevo = 0;
				while(procsIterator.hasNext()){
					IItem procs = (IItem) procsIterator.next();
					idCtProcedimientoNuevo = procs.getInt("ID");					
				}
				int numLotes = 0;
				Iterator<IItem> itColection = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_DATOS_CONTRATO", "NUMEXP='"+rulectx.getNumExp()+"'");
				if(itColection!=null){
					while(itColection.hasNext()){
						IItem itDatosContrato = itColection.next();
						if(itDatosContrato!=null && itDatosContrato.getInt("LOTES_NUMERO")>0){
							numLotes = itDatosContrato.getInt("LOTES_NUMERO");							
						}
					}
				}
				StringBuffer textoImprimir = new StringBuffer("");
				for (int i = 0; i<numLotes; i++){
					String relacion = "Lote - "+ (i+1);
					IItem expHijo = ExpedientesRelacionadosUtil.iniciaExpedienteRelacionadoHijo(cct, idCtProcedimientoNuevo, rulectx.getNumExp(), relacion, true, null);		
					if(null!=expHijo){
						
						expHijo.set("ASUNTO", relacion);
						expHijo.store(cct);	
						textoImprimir.append("Lote "+(i+1)+" -> NumExp."+expHijo.getString("NUMEXP")+"\n");	
						
						ExpedientesUtil.avanzarFase(cct, expHijo.getString("NUMEXP"));
						
						//Añado el identificador del Lote.
						IItem itemLotes = entitiesAPI.createEntity("CONTRATACION_LOTES",expHijo.getString("NUMEXP"));
						itemLotes.set("NUM_LOTE", i+1);
						itemLotes.store(cct);
					}
				}
				TramitesUtil.cargarObservacionesTramite(cct, true,rulectx.getNumExp(), rulectx.getTaskId(), textoImprimir.toString());
			}
			

		}catch(ISPACRuleException e){
			LOGGER.error("NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

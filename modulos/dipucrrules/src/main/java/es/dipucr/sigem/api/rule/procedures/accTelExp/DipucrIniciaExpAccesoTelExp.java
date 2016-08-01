package es.dipucr.sigem.api.rule.procedures.accTelExp;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
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

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrIniciaExpAccesoTelExp implements IRule{
	
	public static final Logger logger = Logger.getLogger(DipucrIniciaExpAccesoTelExp.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexpSolGenerica = rulectx.getNumExp();
			String numexpExpAcceder = "";
			String numExpSolicitudEspecifica = "";
			
			//Recuperamos el numexp de la convocatoria
			IItemCollection solicitudCollection = entitiesAPI.getEntities("DPCR_ACC_TEL_EXP", numexpSolGenerica);
			Iterator solicitudIterator = solicitudCollection.iterator();
			if(solicitudIterator.hasNext()){
				IItem solicitudGenerica = (IItem)solicitudIterator.next();
				numexpExpAcceder = solicitudGenerica.getString("NUMEXP_ACC_TEL_EXP");

				if(StringUtils.isNotEmpty(numexpExpAcceder)){					
					IItem expedienteGenerico = ExpedientesUtil.getExpediente(cct, numexpSolGenerica);
					numExpSolicitudEspecifica = ExpedientesRelacionadosUtil.iniciaExpedienteHijoAccTelExp(cct, numexpExpAcceder, true, false, expedienteGenerico);
					if(expedienteGenerico != null){
						
						ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexpSolGenerica, numExpSolicitudEspecifica);

						IItemCollection documentosSolGenericoCollection = entitiesAPI.getDocuments(numexpSolGenerica, "", "");
						Iterator documentosSolGenericaIterator = documentosSolGenericoCollection.iterator();
						while (documentosSolGenericaIterator.hasNext()){
							IItem documento = (IItem)documentosSolGenericaIterator.next();
							documento.set("NUMEXP", numExpSolicitudEspecifica);
							documento.store(cct);
						}
					}
				}					
				if(numExpSolicitudEspecifica != null && !numExpSolicitudEspecifica.equals("")){
					solicitudGenerica.set("NUMEXP", numExpSolicitudEspecifica);
					solicitudGenerica.store(cct);
					tx.closeProcess(rulectx.getProcessId());
				}				
			}
		}
		catch(ISPACRuleException e){
			logger.error("Error al iniciar expediente específico. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar expediente específico. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al iniciar expediente específico. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar expediente específico. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al iniciar expediente específico. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar expediente específico. " + e.getMessage(), e);
		}
		
		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}

package es.dipucr.sigem.api.rule.procedures.bop.imprenta;

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

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrIniciaExpSolTrabImprenta implements IRule{
	
	protected static final Logger logger = Logger.getLogger(DipucrIniciaExpSolTrabImprenta.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			numexp = rulectx.getNumExp();
			
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			IItemCollection datosSolicitudCollection = entitiesAPI.getEntities("DPCR_DAT_PET_TRAB_IMPRENTA", numexp);
			if(datosSolicitudCollection.toList().size() == 0){
				IItem datosSolicitud = entitiesAPI.createEntity("DPCR_DAT_PET_TRAB_IMPRENTA", numexp);
				IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
				
				String sNombreDepartamento = "";
				
				if(expediente != null){
			        sNombreDepartamento = expediente.getString("SECCIONINICIADORA");
			        expediente.set("IDENTIDADTITULAR", sNombreDepartamento);
			        expediente.set("ROLTITULAR", "INT");
			        expediente.set("ASUNTO", expediente.getString("ASUNTO") + ": " + sNombreDepartamento);
			        expediente.store(cct);
				}
				
				if(StringUtils.isNotEmpty(sNombreDepartamento)){
					datosSolicitud.set("PETICIONARIO", sNombreDepartamento);
				}
				
				datosSolicitud.store(cct);			
			}
			else{
				ExpedientesUtil.avanzarFase(cct, numexp);
			}
		}
		catch(ISPACException e){
			logger.error("Error al rellenar la persona peticionaria en la solicitud de trabajos a imprenta con expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al rellenar la persona peticionaria en la solicitud de trabajos a imprenta con expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

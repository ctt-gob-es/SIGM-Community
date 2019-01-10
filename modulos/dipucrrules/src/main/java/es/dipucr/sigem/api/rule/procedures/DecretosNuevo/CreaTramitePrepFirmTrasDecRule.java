package es.dipucr.sigem.api.rule.procedures.DecretosNuevo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class CreaTramitePrepFirmTrasDecRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(CreaTramitePrepFirmTrasDecRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			IItemCollection itemDocumento = DocumentosUtil.getDocumentos(rulectx.getClientContext(), rulectx.getNumExp(), "(ID_TRAMITE="+rulectx.getTaskId()+" " +
					"AND ESTADOFIRMA='"+SignStatesConstants.RECHAZADO+"')", "");
			
			//Comprobamos que no este el trámite creado.
			IItem itTram = TramitesUtil.getTramiteByCode(rulectx, "PREP_FIRMAS_DEC");
			IItemCollection itemTramites = TramitesUtil.getTramites(rulectx.getClientContext(), rulectx.getNumExp(), "ID_TRAM_CTL = " + itTram.getInt("ID") + "", "");
			
			//Comprobamos que no tenga número de decreto
			String numDecreto = DecretosUtil.getNumDecreto(rulectx);
			
			if(!itemDocumento.iterator().hasNext() && !itemTramites.iterator().hasNext() && StringUtils.isEmpty(numDecreto)){
				TramitesUtil.crearTramite("PREP_FIRMAS_DEC", rulectx);
			}			
			
		}catch (Exception e) {
			logger.error("Error en la generación de los trámites y envío a firma de los decretos. " + e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de los decretos. " + e.getMessage(), e);
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

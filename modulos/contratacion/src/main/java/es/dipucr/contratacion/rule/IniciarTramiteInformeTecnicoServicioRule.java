package es.dipucr.contratacion.rule;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SpacExpRelacionadosInfoAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class IniciarTramiteInformeTecnicoServicioRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(IniciarTramiteInformeTecnicoServicioRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*****************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			/*****************************************************************/
			int idTramiteCodTramite = TramitesUtil.crearTramite(cct, "inf-tecn-contr", rulectx.getNumExp());
			
			SpacExpRelacionadosInfoAPI expRelacionadosInfos = new SpacExpRelacionadosInfoAPI(cct);
			
			IItem itemHijo = expRelacionadosInfos.getInfoSpacExpRelacionaHijoIdTramiteHijo(rulectx.getNumExp(), rulectx.getTaskId());
			
			expRelacionadosInfos.addSpacExpRelacionadosInfo(rulectx.getNumExp(), rulectx.getNumExp(), rulectx.getTaskId(), idTramiteCodTramite, null);
			
			IItem expediente = ExpedientesUtil.getExpediente(cct, itemHijo.getString("NUMEXP_PADRE"));
			String nombrePro = "";
			if(expediente!=null && StringUtils.isNotEmpty(expediente.getString("NOMBREPROCEDIMIENTO"))){
				nombrePro = expediente.getString("NOMBREPROCEDIMIENTO");
			}
			
			TramitesUtil.cargarObservacionesTramite(cct, true,rulectx.getNumExp(), rulectx.getTaskId(), nombrePro+" - Exp.Relacionado: "+itemHijo.getString("NUMEXP_PADRE"));
			
		}catch(ISPACRuleException e){
			LOGGER.error("Error al iniciar el trámite con el código inf-tecn-contr. NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar el trámite con el código inf-tecn-contr. NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Error al iniciar el trámite con el código inf-tecn-contr. NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar el trámite con el código inf-tecn-contr. NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

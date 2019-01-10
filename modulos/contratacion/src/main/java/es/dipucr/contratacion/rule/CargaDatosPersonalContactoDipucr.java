package es.dipucr.contratacion.rule;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class CargaDatosPersonalContactoDipucr implements IRule{
	
	private static final Logger logger = Logger.getLogger(CargaDatosPersonalContactoDipucr.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			/*************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			 /***********************************************************/
			
			Iterator<IItem> itPersCont = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_PERS_CONTACTO", "NUMEXP='"+rulectx.getNumExp()+"'");
			IItem itemPersCont = null;
			if(itPersCont.hasNext()){
				itemPersCont = itPersCont.next();
			}
			else{
				itemPersCont = entitiesAPI.createEntity("CONTRATACION_PERS_CONTACTO",rulectx.getNumExp());
			}
			itemPersCont.set("NUMEXP", rulectx.getNumExp());
			String nombreOC = ConfigurationMgr.getVarGlobal(cct, "NOMBRE_OC");
			if(StringUtils.isNotEmpty(nombreOC)){
				itemPersCont.set("NOMBRE", nombreOC);
			}
			String emailOC = ConfigurationMgr.getVarGlobal(cct, "EMAIL_OC");
			if(StringUtils.isNotEmpty(emailOC)){
				itemPersCont.set("EMAIL", emailOC); 
			}
			String calleOC = ConfigurationMgr.getVarGlobal(cct, "CALLE_OC");
			if(StringUtils.isNotEmpty(calleOC)){
				itemPersCont.set("CALLE", calleOC);
			}
			String cpOC = ConfigurationMgr.getVarGlobal(cct, "CP_OC");
			if(StringUtils.isNotEmpty(cpOC)){
				itemPersCont.set("CP", cpOC); 
			}
			String localidadOC = ConfigurationMgr.getVarGlobal(cct, "LOCALIDAD_OC");
			if(StringUtils.isNotEmpty(localidadOC)){
				itemPersCont.set("LOCALIDAD", localidadOC); 
			}
			String provinciaOC = ConfigurationMgr.getVarGlobal(cct, "PROVINCIA_OC");
			if(StringUtils.isNotEmpty(provinciaOC)){
				itemPersCont.set("PROVINCIA", provinciaOC);
			}
			String locgeograficaOC = ConfigurationMgr.getVarGlobal(cct, "LOCALIZACIONGEOGRAFICA_OC");
			if(StringUtils.isNotEmpty(locgeograficaOC)){
				itemPersCont.set("LOCALIZACIONGEOGRAFICA", locgeograficaOC);
			}
			String movilOC = ConfigurationMgr.getVarGlobal(cct, "MOVIL_OC");
			if(StringUtils.isNotEmpty(movilOC)){
				itemPersCont.set("MOVIL", movilOC);
			}
			
			//Secretaria
			String nombreSEC = ConfigurationMgr.getVarGlobal(cct, "NOMBRE_SEC");
			if(StringUtils.isNotEmpty(nombreSEC)){
				itemPersCont.set("NOMBRESECRE", nombreSEC);
			}
			String emailSEC = ConfigurationMgr.getVarGlobal(cct, "EMAIL_SEC");
			if(StringUtils.isNotEmpty(emailSEC)){
				itemPersCont.set("EMAILSECRE", emailSEC); 
			}
			String calleSEC = ConfigurationMgr.getVarGlobal(cct, "CALLE_SEC");
			if(StringUtils.isNotEmpty(calleSEC)){
				itemPersCont.set("CALLESECRE", calleSEC);
			}
			String cpSEC = ConfigurationMgr.getVarGlobal(cct, "CP_SEC");
			if(StringUtils.isNotEmpty(cpSEC)){
				itemPersCont.set("CPSECRE", cpSEC); 
			}
			String localidadSEC = ConfigurationMgr.getVarGlobal(cct, "LOCALIDAD_SEC");
			if(StringUtils.isNotEmpty(localidadSEC)){
				itemPersCont.set("LOCALIDADSECRE", localidadSEC); 
			}
			String provinciaSEC = ConfigurationMgr.getVarGlobal(cct, "PROVINCIA_SEC");
			if(StringUtils.isNotEmpty(provinciaSEC)){
				itemPersCont.set("PROVINCIASECRE", provinciaSEC);
			}
			String locgeograficaSEC = ConfigurationMgr.getVarGlobal(cct, "LOCALIZACIONGEOGRAFICA_SEC");
			if(StringUtils.isNotEmpty(locgeograficaSEC)){
				itemPersCont.set("LOCALIZACIONGEOGRAFICASECRE", locgeograficaSEC);
			}
			String movilSEC = ConfigurationMgr.getVarGlobal(cct, "MOVIL_SEC");
			if(StringUtils.isNotEmpty(movilSEC)){
				itemPersCont.set("MOVILSECRE", movilSEC);
			}
			
			itemPersCont.store(cct);
	        
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error a la hora de insertar el personal de contacto. ",e);
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

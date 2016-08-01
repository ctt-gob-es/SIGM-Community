package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GeneracionPropuestasJuntaActa extends GeneracionPropuestasActa {
	protected static final Logger logger = Logger.getLogger(GenerateActaJuntaRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		String organo = SecretariaUtil.getOrgano(rulectx);
		
		if(organo.equals("JGOB")){
			strNombreDocProp = "Propuesta";
	        strNombreDocCabProp = "Borrador de Acta de Pleno - Propuesta - Cabecera";
	        strNombreDocPieProp = "Borrador de Acta de Pleno - Propuesta - Pie - Junta";
	        
	        strNombreDocUrg = "Propuesta Urgencia";
	        strNombreDocCabUrg = "Borrador de Acta de Junta - Urgencias - Cabecera";
	        strNombreDocPieUrg = "Borrador de Acta de Junta - Urgencias - Pie - Junta";
		}
		
		if(organo.equals("PLEN")){
			strNombreDocProp = "Propuesta";
	        strNombreDocCabProp = "Borrador de Acta de Pleno - Propuesta - Cabecera";
	        strNombreDocPieProp = "Borrador de Acta de Pleno - Propuesta - Pie - Pleno";
	        
	        strNombreDocUrg = "Propuesta Urgencia";
	        strNombreDocCabUrg = "Borrador de Acta de Junta - Urgencias - Cabecera";
	        strNombreDocPieUrg = "Borrador de Acta de Junta - Urgencias - Pie - Pleno";
		}
		
		return true;
	}
}

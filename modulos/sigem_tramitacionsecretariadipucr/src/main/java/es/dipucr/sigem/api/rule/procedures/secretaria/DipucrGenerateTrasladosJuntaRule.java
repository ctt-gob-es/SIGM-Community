package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;


public class DipucrGenerateTrasladosJuntaRule extends DipucrGenerateTrasladosRule {
	
	protected static final Logger logger = Logger.getLogger(DipucrGenerateTrasladosJuntaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		String organo = SecretariaUtil.getOrgano(rulectx);
		
		if(organo.equals("JGOB")){
			strNombreDocCab = "Certificado de acuerdos - Cabecera";
		}
		if(organo.equals("PLEN")){
			strNombreDocCab = "Certificado de acuerdos - Cabecera - Pleno";
		}
		strNombreDoc = "Certificado de acuerdos";
		strNombreDocPie = "Certificado de acuerdos - Pie";
		return true;
	}
}
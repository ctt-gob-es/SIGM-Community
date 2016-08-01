package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class DipucrGenerateTrasladosComiRule extends DipucrGenerateTrasladosRule {
	protected static final Logger logger = Logger.getLogger(DipucrGenerateTrasladosComiRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		strNombreDoc = "Certificado de dictamen";
		strNombreDocCab = "Certificado de dictamen - Cabecera";
		strNombreDocPie = "Certificado de dictamen - Pie";
		
		String organo = SecretariaUtil.getOrgano(rulectx);
		
		if(organo.equals("MESA")){
			strNombreDocCab = "Certificado de dictamen - Mesa - Cabecera";
			strNombreDocPie = "Certificado de dictamen - Mesa - Pie";
		}
		/**
		 * [Teresa Ticket#201# INICIO Cambiar nombre de la COMI de bienestar social ]
		 * **/
		else{
			try {
				String area = SecretariaUtil.getNombreAreaSesion(rulectx, rulectx.getNumExp());

				if(area.equals("Igualdad")){
					strNombreDocCab = "Certificado de dictamen - BSocial - Cabecera";
				}
			}catch (ISPACException e) {
				logger.error("Se produjo una excepción", e);
				throw new ISPACRuleException(e);
			}
		}
		/**
		 * [Teresa Ticket#201# FIN Cambiar nombre de la COMI de bienestar social ]
		 * **/
		
		return true;
	}
}
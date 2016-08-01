package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GeneracionPropuestasComiActa extends GeneracionPropuestasActa {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		String organo = SecretariaUtil.getOrgano(rulectx);
		
		if(organo.equals("MESA")){
			strNombreDocProp = "Propuesta";
	        strNombreDocCabProp = "Borrador de Acta de Mesa - Propuesta - Cabecera";
	        strNombreDocPieProp = "Borrador de Acta de Mesa - Propuesta - Pie";
	        
	        strNombreDocUrg = "Propuesta Urgencia";
	        strNombreDocCabUrg = "Borrador de Acta de Junta - Urgencias - Cabecera";
	        strNombreDocPieUrg = "Borrador de Acta de Junta - Urgencias - Pie - Comi";
		}
		else{
			strNombreDocProp = "Propuesta";
	        strNombreDocCabProp = "Borrador de Acta de Pleno - Propuesta - Cabecera";
	        strNombreDocPieProp = "Borrador de Acta de Pleno - Propuesta - Pie - Comi";
	        
	        strNombreDocUrg = "Propuesta Urgencia";
	        strNombreDocCabUrg = "Borrador de Acta de Junta - Urgencias - Cabecera";
	        strNombreDocPieUrg = "Borrador de Acta de Junta - Urgencias - Pie - Comi";
		}

		return true;
	}

}

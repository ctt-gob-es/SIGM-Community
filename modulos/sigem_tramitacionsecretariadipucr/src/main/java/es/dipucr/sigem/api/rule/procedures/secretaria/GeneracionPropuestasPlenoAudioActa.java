package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

public class GeneracionPropuestasPlenoAudioActa extends GeneracionSinPropuestasActaPlenoRule {
	protected static final Logger logger = Logger.getLogger(GenerateActaJuntaRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		strNombreDocProp = "Propuesta";
        strNombreDocCabProp = "Propuesta - Cabecera";
        strNombreDocPieProp = "Propuesta - Pie";
        
        strNombreDocUrg = "Propuesta Urgencia";
        strNombreDocCabUrg = "Propuesta - Cabecera";
        strNombreDocPieUrg = "Propuesta Urgencia - Pie";
        
		return true;
	}
}

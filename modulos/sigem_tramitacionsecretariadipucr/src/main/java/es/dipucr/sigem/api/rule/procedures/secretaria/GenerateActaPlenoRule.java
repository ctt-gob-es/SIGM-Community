package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateActaPlenoRule extends GeneratePlenoRule {
	protected static final Logger logger = Logger.getLogger(GenerateActaPlenoRule.class);

	public boolean init(IRuleContext rctx) throws ISPACRuleException {
		
		//logger.warn("GenerateActaJuntaRule o pleno");

		STR_prefijo                 = "Acta de Pleno";
		STR_nombrePie               = STR_prefijo+ " - Pie";
		STR_propuestaBorrador       = STR_prefijo+" - Propuesta - Borrador";
		
		
		String sesion = SecretariaUtil.getSesion(rctx);

		STR_nombreTramite           	= "Carga audio y propuestas";
		if(sesion.equals("ORD")){
			STR_nombreCabecera 			= STR_prefijo+" - Cabecera - Ordi";
			STR_informes                = STR_prefijo+" - Informes - Ordi";
			STR_urgencias				= STR_prefijo+" - Urgencias - Ordi";
			STR_nombreRuegos            = STR_prefijo+" - Ruegos y preguntas - Ordi";
		}
		if(sesion.equals("EXTR")){
			STR_nombreCabecera 			= STR_prefijo+" - Cabecera - Extra";
			STR_informes                = "";
			STR_urgencias				= "";
			STR_nombreRuegos            = "";
			
		}
		if(sesion.equals("EXUR")){
			STR_nombreCabecera 			= STR_prefijo+" - Cabecera - ExtraUrg";
			STR_propuestaBorrador       = STR_prefijo+" - Propuesta - RatificacionUrgencia";
			STR_urgencias				= "";
			STR_informes                = "";
			STR_nombreRuegos            = "";
		}
		if(sesion.equals("CONS")){
			
		}
		
		
		return true;
	}

}


//<ispactag rule='PropertySubstituteRule' entity='SECR_SESION' property='TIPO' codetable='SECR_VLDTBL_TIPOSESION' code='VALOR' value='SUSTITUTO' />
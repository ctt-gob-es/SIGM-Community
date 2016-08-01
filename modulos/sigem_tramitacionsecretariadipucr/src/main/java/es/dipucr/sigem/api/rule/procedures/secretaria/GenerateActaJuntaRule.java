package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateActaJuntaRule extends GenerateActaBasePropuestaRule {
	protected static final Logger logger = Logger.getLogger(GenerateActaJuntaRule.class);

	public boolean init(IRuleContext rctx) throws ISPACRuleException {
		
		//logger.warn("GenerateActaJuntaRule o pleno");

		STR_prefijo                 = "Borrador de Acta de Junta";
		String STR_comun			= "Borrador de Acta de Pleno";
		STR_nombreTramite           = STR_prefijo + "";
		STR_nombrePie               = STR_prefijo + " - Pie";
		
		//Obtener la sesion del expediente
		String organo = SecretariaUtil.getOrgano(rctx);
		String sesion = SecretariaUtil.getSesion(rctx);
		//logger.warn("sesion "+sesion);
		if(organo.equals("JGOB")){
			STR_nombreTramite           	= STR_prefijo + "";
			if(sesion.equals("ORD")){
				STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Ordinaria";
				STR_propuestaBorrador       = STR_prefijo + " - Propuesta - Borrador";
				STR_urgencias				= STR_prefijo + " - Urgencias";
				STR_nombreRuegos            = STR_comun + " - Ruegos y preguntas";
			}
			if(sesion.equals("EXTR")){
				STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Extra";
				STR_propuestaBorrador       = STR_prefijo + " - Propuesta - Borrador";
				STR_urgencias				= "";
				STR_nombreRuegos            = "";
			}
			if(sesion.equals("EXUR")){
				STR_nombreCabecera 			= STR_prefijo + " - Cabecera - ExtraUrg";
				STR_propuestaBorrador       = STR_prefijo + " - Propuesta - RatificacionUrgencia";
				STR_urgencias				= "";
				STR_nombreRuegos            = "";
			}
			if(sesion.equals("CONS")){
				STR_constitutiva 			= "Borrador de Acta de Junta - Constitutiva";
			}
		}
		
		if(organo.equals("PLEN")){
			STR_nombreTramite           	= STR_comun + "";
			if(sesion.equals("ORD")){
				STR_nombreCabecera 			= "Borrador de Acta de Pleno - Cabecera - Ordinaria";
				STR_propuestaBorrador       = "Borrador de Acta de Pleno - Propuesta - Borrador";
				STR_urgencias				= "Borrador de Acta de Pleno - Urgencias";
				STR_nombreRuegos            = "Borrador de Acta de Pleno - Ruegos y preguntas - Pleno";
				STR_nombrePie				= "Borrador de Acta de Pleno - Pie";
			}
			if(sesion.equals("EXTR")){
				STR_nombreCabecera 			= "Borrador de Acta de Pleno - Cabecera - Extra";
				STR_propuestaBorrador       = "Borrador de Acta de Pleno - Propuesta - Borrador";
				STR_urgencias				= "";
				STR_nombreRuegos            = "";
				STR_nombrePie				= "Borrador de Acta de Pleno - Pie";
			}
			if(sesion.equals("EXUR")){
				STR_nombreCabecera 			= "Borrador de Acta de Pleno - Cabecera - ExtraUrg";
				STR_propuestaBorrador       = "Borrador de Acta de Pleno - Propuesta - RatificacionUrgencia";
				STR_urgencias				= "";
				STR_nombreRuegos            = "";
				STR_nombrePie				= "Borrador de Acta de Pleno - Pie";
			}
			if(sesion.equals("CONS")){
				STR_constitutiva 			= "Borrador de Acta de Pleno - Constitutiva";
			}
		}
		
		
		return true;
	}

}


//<ispactag rule='PropertySubstituteRule' entity='SECR_SESION' property='TIPO' codetable='SECR_VLDTBL_TIPOSESION' code='VALOR' value='SUSTITUTO' />
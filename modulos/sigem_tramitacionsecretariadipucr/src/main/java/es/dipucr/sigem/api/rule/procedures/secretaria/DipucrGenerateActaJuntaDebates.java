package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class DipucrGenerateActaJuntaDebates extends GenerateActaBasePropuestaRule{

	protected static final Logger logger = Logger.getLogger(DipucrGenerateActaJuntaDebates.class);

	public boolean init(IRuleContext rctx) throws ISPACRuleException {

		STR_prefijo                 = "Borrador de Acta de Junta";
		String STR_comun			= "Borrador de Acta de Pleno";
		STR_nombreTramite           = STR_prefijo + "";
		STR_nombrePie               = STR_prefijo + " - Pie";
		
		//Obtener la sesion del expediente
		String organo = SecretariaUtil.getOrgano(rctx);
		String sesion = SecretariaUtil.getSesion(rctx);
		if(organo.equals("JGOB")){
			STR_nombreTramite           	= STR_prefijo + "";
			if(sesion.equals("ORD")){
				STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Ordinaria";
				STR_propuestaBorrador       = STR_comun + " - Propuesta - Borrador";
				STR_urgencias				= STR_comun + " - Urgencias";
				STR_nombreRuegos            = STR_comun + " - Ruegos y preguntas";
			}
			if(sesion.equals("EXTR")){
				STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Extra";
				STR_propuestaBorrador       = STR_comun + " - Propuesta - Borrador";
				STR_urgencias				= "";
				STR_nombreRuegos            = "";
			}
			if(sesion.equals("EXUR")){
				STR_nombreCabecera 			= STR_prefijo + " - Cabecera - ExtraUrg";
				STR_propuestaBorrador       = STR_comun + " - Propuesta - RatificacionUrgencia";
				STR_urgencias				= "";
				STR_nombreRuegos            = "";
			}
			if(sesion.equals("CONS")){
				
			}
		}
		return true;
	}
}

package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateActaComiTomellosoRule extends GenerateActaBasePropuestaRule {
	protected static final Logger logger = Logger.getLogger(GenerateActaComiTomellosoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		STR_prefijo                 = "Borrador de Acta de Comisión";
		STR_nombreTramite           = STR_prefijo + "";
		STR_nombrePie               = STR_prefijo + " - Pie";
		
		String organo = SecretariaUtil.getOrgano(rulectx);
		logger.warn("organo "+organo);
		
		if(organo.equals("MESA")){
			STR_prefijo = "Borrador de Acta de Mesa";
			STR_nombreCabecera 			= STR_prefijo + " - Cabecera";
			STR_propuestaBorrador       = STR_prefijo + " - Propuesta - Borrador";
			STR_urgencias				= "Borrador de Acta de Junta - Urgencias - Comi";
			STR_nombreRuegos            = "";
			STR_nombreTramite           = STR_prefijo + "";
			STR_nombrePie               = STR_prefijo + " - Pie";
			
		}
		else{
			try {
				String area =SecretariaUtil.getNombreAreaSesion(rulectx, rulectx.getNumExp());
				String sesion = SecretariaUtil.getSesion(rulectx);
				logger.warn("area "+area);
				logger.warn("sesion "+sesion);
				////logger.warn("sesion "+sesion);
				////logger.warn("area "+area);
				if(area.contains("Desarrollo Sostenible de la Ciudad")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Desarr - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Desarr - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Desarr -  ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				/**
				 * [Teresa Ticket#201# Cambiar nombre de la COMI de bienestar social ]
				 * **/
				if(area.contains("Bienestar Social")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - BSocial - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - BSocial - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - BSocial -  ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				if(area.contains("Cultura")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Cultura - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Cultura - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Cultura - ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				if(area.contains("Hacienda")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Haci - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Haci - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Haci - ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				if(area.contains("Promoción Económica")){
					if(sesion.equals("ORD")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Promoc - Ordinaria";
					}
					if(sesion.equals("EXTR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Promoc - Extra";
					}
					if(sesion.equals("EXUR")){
						STR_nombreCabecera 			= STR_prefijo + " - Cabecera - Promoc - ExtraUrg";
					}
					if(sesion.equals("CONS")){
						
					}
				}
				if(sesion.equals("ORD")){

					STR_propuestaBorrador       = STR_prefijo + " - Propuesta - Borrador";
					STR_urgencias				= "Borrador de Acta de Junta - Urgencias - Comi";
					STR_nombreRuegos            = "Borrador de Acta de Comisión - Ruegos y preguntas - Comi";
				}
				if(sesion.equals("EXTR")){
			
					STR_propuestaBorrador       = STR_prefijo + " - Propuesta - Borrador";
					STR_urgencias				= "";
					STR_nombreRuegos            = "";
				}
				if(sesion.equals("EXUR")){

					STR_propuestaBorrador       = STR_prefijo + " - Propuesta - RatificacionUrgencia";
					STR_urgencias				= "";
					STR_nombreRuegos            = "";
				}
				if(sesion.equals("CONS")){
					
				}
			} catch (ISPACException e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		
		return true;
	}
}
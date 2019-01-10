package es.dipucr.sigem.api.rule.procedures.subvenciones.convocatorias;

import java.util.Calendar;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;



import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;

	public class DipuCrIniciaPropuesta2PlanEmpleo extends DipucrAutoGeneraDocIniTramiteRule{

		private static final Logger logger = Logger.getLogger(DipuCrIniciaPropuesta2PlanEmpleo.class);	

		public boolean init(IRuleContext rulectx) throws ISPACRuleException {
			logger.info("INICIO - " + this.getClass().getName());
			
			tipoDocumento = "Contenido de la propuesta";
			plantilla = "Propuesta Subvenciones Plan de Empleo";
			
			logger.info("FIN - " + this.getClass().getName());
			return true;
		}
		
		public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
			try {
				cct.setSsVariable("ANIO", ""+Calendar.getInstance().get(Calendar.YEAR));
			} catch (ISPACException e) {
				logger.error(e.getMessage(), e);
			}
		}

		public void deleteSsVariables(IClientContext cct) {	
			try {
				cct.deleteSsVariable("ANIO");
			} catch (ISPACException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
package es.dipucr.sigem.api.rule.procedures.viasObras.usosExcp;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrGeneraComInfUsosExc extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(DipucrGeneraComInfUsosExc.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());

		tipoDocumento = "Comunicación Informativa";
		plantilla = "Comunicación Informativa Autorización Usos Excepcionales";

		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			cct.setSsVariable("ANIO",
					"" + Calendar.getInstance().get(Calendar.YEAR));

			// ----------------------------------------------------------------------------------------------
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			String numexp = rulectx.getNumExp();

			IItemCollection partCol = ParticipantesUtil.getParticipantes( cct, numexp, "",
					"");
			Iterator partIt = partCol.iterator();

			if (partIt.hasNext()) {
				IItem part = (IItem) partIt.next();

				cct.setSsVariable("NOMBRE", part.getString("NOMBRE"));
				cct.setSsVariable("DIRNOT", part.getString("DIRNOT"));
				cct.setSsVariable("C_POSTAL", part.getString("C_POSTAL"));
				cct.setSsVariable("LOCALIDAD", part.getString("LOCALIDAD"));
				cct.setSsVariable("CAUT", part.getString("CAUT"));
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void deleteSsVariables(IClientContext cct) {
		try {
			cct.deleteSsVariable("ANIO");
			cct.deleteSsVariable("NOMBRE");
			cct.deleteSsVariable("DIRNOT");
			cct.deleteSsVariable("C_POSTAL");
			cct.deleteSsVariable("LOCALIDAD");
			cct.deleteSsVariable("CAUT");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
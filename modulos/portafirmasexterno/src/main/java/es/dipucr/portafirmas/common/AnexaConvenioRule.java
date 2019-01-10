package es.dipucr.portafirmas.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class AnexaConvenioRule implements IRule {

	private static final Logger logger = Logger
			.getLogger(AnexaConvenioRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			String numexp_padre = "";
			Vector<String> resultado = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, rulectx.getNumExp());
			for (int i = 0; i < resultado.size(); i++) {
				numexp_padre = resultado.get(i);
			}
			if(StringUtils.isNotEmpty(numexp_padre)){
				IItemCollection documentsCollection = DocumentosUtil.getDocumentos(cct, numexp_padre, "NOMBRE='Convenio'", "FDOC DESC");
				Iterator<IItem> exp_documentsCollection = documentsCollection.iterator();
				IItem contenido = null;
				while (exp_documentsCollection.hasNext()) {
					contenido = (IItem) exp_documentsCollection.next();
					String infopag_rde = contenido.getString("INFOPAG_RDE");
					File fileInfTecnRde = DocumentosUtil.getFile(cct, infopag_rde, null, null);
					
					DocumentosUtil.generaYAnexaDocumento(rulectx, contenido.getInt("ID_TPDOC"), contenido.getString("DESCRIPCION"), fileInfTecnRde, contenido.getString("EXTENSION_RDE"));
					
					if(fileInfTecnRde != null && fileInfTecnRde.exists()) fileInfTecnRde.delete();
				}
				ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexp_padre, rulectx.getNumExp());

			}
			
		} catch (ISPACException e) {
			logger.error("Error al generar la comunicación administrativa. NUMEXP. "+rulectx.getNumExp()+" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. NUMEXP. "+rulectx.getNumExp()+" - "+ e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al generar la comunicación administrativa. NUMEXP. "+rulectx.getNumExp()+" - "+ e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. NUMEXP. "+rulectx.getNumExp()+" - "+ e.getMessage(), e);
		}

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

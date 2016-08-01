package es.dipucr.sigem.api.rule.procedures.planesProvinciales;

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

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;

public class DipucrGeneraProvidenciaPlanesProvRule extends DipucrAutoGeneraDocIniTramiteRule{

	private static final Logger logger = Logger.getLogger(DipucrGeneraProvidenciaPlanesProvRule.class);	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		
		tipoDocumento = "Providencia";
		plantilla = "Providencia Planes Provinciales";
		
		logger.info("FIN - " + this.getClass().getName());
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		String numexp = "";
		try {
			numexp = rulectx.getNumExp();
			String anio = "";
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			String sqlQueryPart = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";	
			IItemCollection participantes = entitiesAPI.queryEntities("DIPUCRPLANESPROVINCIALES", sqlQueryPart);
			Iterator it = (Iterator) participantes.iterator();
			if(it.hasNext())
				anio = ((IItem)it.next()).getString("ANIO");
			if(anio == null) anio = "";
			cct.setSsVariable("ANIO", anio);
		} catch (ISPACException e) {				
        	logger.error("Error en el expediente: " + numexp + ". " + e.getMessage(), e);
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

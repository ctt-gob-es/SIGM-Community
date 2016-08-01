package es.dipucr.sigem.api.rule.procedures.bop.imprenta;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

public class DipucrCompruebaEmailContacto implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrCompruebaEmailContacto.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean resultado = true;
		String numexp = "";

		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			numexp = rulectx.getNumExp();
			
        	IItemCollection solicitudCollection = entitiesAPI.getEntities("DPCR_DAT_PET_TRAB_IMPRENTA", numexp);
	        if(solicitudCollection.toList().size() > 0){
		        String email = ((IItem) solicitudCollection.iterator().next()).getString("EMAILCONTACTO");
		        if(StringUtils.isEmpty(email)){
		        	rulectx.setInfoMessage("ERROR. Debe rellenar la 'Dirección Electrónica' de la 'Persona de contacto' en la pestaña 'Datos Petición de Trabajos a la Imprenta Provincial'.");
		        	resultado = false;
				}	
            }
	        else{
	        	rulectx.setInfoMessage("ERROR. Debe rellenar la pestaña 'Datos Petición de Trabajos a la Imprenta Provincial'.");
	        	resultado = false;
	        }
		} catch (ISPACException e) {
			logger.error("Error al comprobar los datos obligatorios en el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al comprobar los datos obligatorios en el expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return resultado;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}

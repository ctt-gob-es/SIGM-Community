package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWSProxy;


/**
 * [dipucr-Felipe #469]
 * @author FELIPE
 * Crea un beneficiario de ayudas sociales en la BBDD de beneficiarios
 */
public class CrearBeneficiarioAyudasssRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CrearBeneficiarioAyudasssRule.class);
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************

			String numexp = rulectx.getNumExp();
			IItemCollection colBeneficiarios = entitiesAPI.getEntities("AYUDASSS_BENEFICIARIO", numexp);
			IItem itemBeneficiario = (IItem) colBeneficiarios.iterator().next();
			
			AyudasSocialesWSProxy ws = new AyudasSocialesWSProxy();
			ws.crearBeneficiario
			(
					itemBeneficiario.getString("NIF_EMPLEADO"),
					itemBeneficiario.getString("PARENTESCO"),
					itemBeneficiario.getString("NOMBRE"),
					itemBeneficiario.getString("FECHA_NAC"),
					numexp
			);
			
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la creación del beneficiario de ayudas mediante WS. " + e.getMessage(), e);
		}
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

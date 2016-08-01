package es.dipucr.sigem.api.rule.procedures.etablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Date;

import org.directwebremoting.util.Logger;

/**
 * [eCenpri-Felipe ticket #504]
 * Valida que se hayan rellenado los datos de la publicación
 * @author Felipe
 * @since 15.03.2012
 */
public class ValidarCamposPublicacionRule implements IRule {
	
	public static final Logger logger = Logger.getLogger(ValidarCamposPublicacionRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validaciones
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			IItemCollection collection = entitiesAPI.getEntities("ETABLON_PUBLICACION", numexp);
			Date dFechaIniVigencia, dFechaFinVigencia;
			
			if (!collection.next()){
				rulectx.setInfoMessage("Es necesario rellenar todos los campos " +
					"de la publicación en la pestaña eTablón Publicación");
				return false;
			}
			else{
				IItem itemPub = (IItem)collection.iterator().next();
				if (StringUtils.isEmpty(itemPub.getString("TITULO")) ||
					StringUtils.isEmpty(itemPub.getString("DESCRIPCION")) ||
					StringUtils.isEmpty(itemPub.getString("COD_SERVICIO")) ||
					StringUtils.isEmpty(itemPub.getString("COD_CATEGORIA")) ||
					null == (dFechaIniVigencia = itemPub.getDate("FECHA_INI_VIGENCIA")) ||
					null == (dFechaFinVigencia = itemPub.getDate("FECHA_FIN_VIGENCIA")))
				{
					rulectx.setInfoMessage("Es necesario rellenar todos los campos " +
							"de la publicación en la pestaña eTablón Publicación");
					return false;
				}
				else if(!dFechaFinVigencia.after(dFechaIniVigencia)){
					rulectx.setInfoMessage("La fecha de fin de vigencia debe ser " +
							"posterior a la fecha de inicio de vigencia");
					return false;
				}
				else{
					return true;
				}
			}
		}
		catch (Exception e) {
        	logger.error("Error al validar los campos del tablón en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al validar los campos del tablón en el expediente: " + rulectx.getNumExp() + "." + e.getMessage(), e);
		}
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

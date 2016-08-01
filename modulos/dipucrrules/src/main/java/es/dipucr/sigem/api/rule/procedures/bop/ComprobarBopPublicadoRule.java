package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

/**
 * 
 * @author [dipucr-Felipe #1317]
 * @date 15.04.2015
 * @category Comprueba que el boletín esté publicado antes de permitir cerrar el trámite "Crear el BOP General"
 * 			 que cierra todos los espedientes de anuncio asociados y por tanto impide su publicación
 */
public class ComprobarBopPublicadoRule implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(ComprobarBopPublicadoRule.class);
	
	private static final String CODTRAM_PUBLICACION = "BOP_PUB";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		boolean bResult = false;
		
		try{
			
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String numexp = rulectx.getNumExp();
			
			IItem itemCtTramPub = TramitesUtil.getTramiteByCode(rulectx, CODTRAM_PUBLICACION);
			String query = "WHERE NUMEXP = '" + numexp + "' AND ID_TRAM_CTL = " 
					+ itemCtTramPub.getKeyInt() + " AND FECHA_CIERRE IS NOT NULL";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_TRAMITES", query);
			
			//Vemos si ya existe el trámite y está cerrado
			if (collection.toList().size() > 0){
				bResult = true; 
			}
			else{
				rulectx.setInfoMessage("No se puede terminar el trámite pues el boletín todavía no se ha publicado");
				bResult = false;
			}
		}
		catch(Exception e){
			throw new ISPACRuleException("Error al comprobar si el boletín se ha publicado", e);
		}
		return bResult;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

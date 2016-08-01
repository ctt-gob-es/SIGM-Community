package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author [eCenpri-Felipe #136]
 * @date 29.08.2013
 * @propósito Eliminar los duplicados producidos en la tabla BOP_SOLICITUD por el usuario BOP-pablo
 * No hemos conseguido averiguar la causa del error (qué hace mal), y hemos perdido mucho tiempo en este tema,
 * por lo que he decidido finalmente crear una regla que lo controle
 */
public class EliminarErroresDuplicadosBopRule implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(EliminarErroresDuplicadosBopRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			IItemCollection collection = null;
			String strQuery = "WHERE NUMEXP = '" + numexp + "' ORDER BY ID DESC";
			
			collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
			
			@SuppressWarnings("unchecked")
			List<IItem> listBopSolicitud = collection.toList();
			IItem itemBop = null;
			
			//Si hay más de una entidad BOP_SOLICITUD para el expediente, es que se ha producido error
			//Debemos dejar sólo una. La buena debe tener sumario
			if (listBopSolicitud.size() > 1){
				boolean bSumarioEncontrado = false;
				String sumario = null;
				//Recorremos la lista, borramos todas las que no tengan sumario
				//Si hay más de un registro con sumario, dejamos sólo el primero
				for (int i = 0; i < listBopSolicitud.size(); i++){
					itemBop = listBopSolicitud.get(i);
					sumario = itemBop.getString("SUMARIO");
					if (StringUtils.isEmpty(sumario) || bSumarioEncontrado){
						itemBop.set("NUMEXP", numexp.replace("DPCR", "ERROR"));
						itemBop.store(cct);
					}
					else{
						bSumarioEncontrado = true;
					}
				}
			}
			
		}
		catch(Exception e){
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

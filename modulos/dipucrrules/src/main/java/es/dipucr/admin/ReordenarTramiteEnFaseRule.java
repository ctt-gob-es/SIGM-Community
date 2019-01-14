package es.dipucr.admin;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisosMail.AvisoEmailNuevoTramiteRule;

public class ReordenarTramiteEnFaseRule implements IRule {

	private static final String DATOS_SEPARATOR = ";";
	
	protected String nombreTramite = null;
	protected int posicionFinal = Integer.MIN_VALUE;
	
	protected static final Logger logger = Logger.getLogger(AvisoEmailNuevoTramiteRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			ICatalogAPI catalogAPI = rulectx.getClientContext().getAPI().getCatalogAPI();
			IItem itemDatosEspecificos = catalogAPI.getDatosEspecificosTramite(rulectx.getTaskProcedureId());
			String otrosDatos = itemDatosEspecificos.getString("OTROS_DATOS");
					
			if (StringUtils.isEmpty(otrosDatos)){
				rulectx.setInfoMessage("Debe rellenar la pestaña de datos específicos del trámite con el patrón 'NOMBRE_TRÁMITE;POSICION_EN_FASE'");
				return false;
			}
			else{
				String[] arrOtrosDatos = otrosDatos.split(DATOS_SEPARATOR);
				if (arrOtrosDatos.length != 2){
					rulectx.setInfoMessage("Debe rellenar la pestaña de datos específicos del trámite con el patrón 'NOMBRE_TRÁMITE;POSICION_EN_FASE'");
					return false;
				}
				else{
					nombreTramite = arrOtrosDatos[0];
					posicionFinal = Integer.valueOf(arrOtrosDatos[1]);
				}
			}
		}
		catch(Exception ex){
			throw new ISPACRuleException(ex.getMessage(), ex);
		}
		
		return true;
	}
	
	
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		
		IClientContext cct = rulectx.getClientContext();
		
		try{
			InvesflowAPI invesflowAPI = (InvesflowAPI) cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IProcedureAPI procAPI = invesflowAPI.getProcedureAPI();
			
			String query1 = "WHERE NOMBRE = '" + nombreTramite + "'";
			IItemCollection collectionPTrams = entitiesAPI.queryEntities("SPAC_P_TRAMITES", query1);
			Iterator it = collectionPTrams.iterator();
			
			while (it.hasNext()){
				IItem itemPTram = (IItem) it.next();
				int idPTram = itemPTram.getKeyInt();
				int idPFase = itemPTram.getInt("ID_FASE");
				
				String query2 = "WHERE ID_FASE = " + idPFase + "ORDER BY ORDEN";
				IItemCollection collectionPTramsFase = entitiesAPI.queryEntities("SPAC_P_TRAMITES", query2);
				
				if (collectionPTramsFase.toList().size() > posicionFinal){
					Iterator it2 = collectionPTramsFase.iterator();
					int posicion = 0;
					
					//Recorremos los trámites de la fase
					while (it2.hasNext()){
						posicion++;
						IItem itemPTramFase = (IItem) it2.next();
						if (itemPTramFase.getKeyInt() == idPTram){
							for (int i = posicion; i > posicionFinal; i--){
								procAPI.moveTaskUp(idPTram);
							}
						}
					}
				}
			}
			
		}
		catch(Exception ex){
			throw new ISPACRuleException(ex.getMessage(), ex);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

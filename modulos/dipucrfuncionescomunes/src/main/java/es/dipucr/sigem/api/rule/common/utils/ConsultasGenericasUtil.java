package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ConsultasGenericasUtil {
	
	public static final Logger LOGGER = Logger.getLogger(ConsultasGenericasUtil.class);

	@Deprecated
	public static Iterator<IItem> queryEntities(IRuleContext rulectx, String nombreTabla, String strQuery) throws ISPACRuleException{
		return queryEntities(rulectx.getClientContext(), nombreTabla, strQuery);
	}
		
	@SuppressWarnings("unchecked")
	public static Iterator<IItem> queryEntities(IClientContext cct, String nombreTabla, String strQuery) throws ISPACRuleException{

		try{
			
			/************************************************************************/
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			IItemCollection itCollection = entitiesAPI.queryEntities(nombreTabla, "WHERE "+strQuery);
			
			return itCollection.iterator();	
			
		}catch(ISPACRuleException e){
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en el nombre de la tabla. "+nombreTabla+" en la consulta "+strQuery+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en el nombre de la tabla. "+nombreTabla+" en la consulta "+strQuery+": "+e.getMessage(),e);
		}
	}
	
	public static Iterator<ItemBean>  queryEntitiesMultivalor(IRuleContext rulectx, String nombreTabla, String strQuery) throws ISPACRuleException{
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			 /***********************************************************************/
			Vector<ItemBean> valores = new Vector<ItemBean>();
			String query="SELECT FIELD,REG_ID,VALUE FROM "+nombreTabla+" WHERE "+strQuery;
	        ResultSet datos = cct.getConnection().executeQuery(query).getResultSet();
	        if(datos!=null)
	      	{
	        	while(datos.next()){
	        		
	        		ItemBean  itemB = new ItemBean();
	          		if (datos.getString("FIELD")!=null) itemB.setProperty("FIELD", datos.getString("FIELD"));
	          		if (datos.getString("VALUE")!=null) itemB.setProperty("VALUE", datos.getString("VALUE"));
	          		valores.add(itemB);
	          	}
	      	}
			
			return valores.iterator();			
		}catch(ISPACRuleException e){
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Numexp: "+rulectx.getNumExp()+" Error en el nombre de la tabla. "+nombreTabla+" en la consulta "+strQuery+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Numexp: "+rulectx.getNumExp()+" Error en el nombre de la tabla. "+nombreTabla+" en la consulta "+strQuery+": "+e.getMessage(),e);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Numexp: "+rulectx.getNumExp()+" Error en el nombre de la tabla. "+nombreTabla+" en la consulta "+strQuery+": "+e.getMessage(),e);
		}
	}
	
	public static IItem createEntities (IRuleContext rulectx, String nombreTabla) throws ISPACRuleException{
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			return entitiesAPI.createEntity(nombreTabla, rulectx.getNumExp());
		}catch(ISPACRuleException e){
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en el nombre de la tabla. "+nombreTabla+" número de expediente "+rulectx.getNumExp()+": "+e.getMessage(),e);
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en el nombre de la tabla. "+nombreTabla+" número de expediente "+rulectx.getNumExp()+": "+e.getMessage(),e);
		}
	}
}

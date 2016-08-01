package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTEntityDAO;
import ieci.tdw.ispac.ispaclib.dao.entity.EntityFactoryDAO;
import ieci.tdw.ispac.ispaclib.dao.entity.MultivalueTable;
import ieci.tdw.ispac.ispaclib.db.DbResultSet;
import ieci.tdw.ispac.ispaclib.entity.def.EntityDef;
import ieci.tdw.ispac.ispaclib.entity.def.EntityField;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.sql.SQLException;

import org.apache.log4j.Logger;

public class PropertySubstituteUppercaseRule implements IRule
{
	private static final Logger logger = Logger.getLogger(PropertySubstituteUppercaseRule.class);

    public boolean init(IRuleContext rctx) throws ISPACRuleException
    {
        return true;
    }

    public boolean validate(IRuleContext rctx) throws ISPACRuleException
    {
        return true;
    }

    public Object execute(IRuleContext rctx) throws ISPACRuleException
    {        

        try
        {
			IClientContext cct = rctx.getClientContext();
			
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
            
            String entityname = rctx.get("entity");
            //logger.warn("entityname. "+entityname);
            String property = rctx.get("property");
            //logger.warn("property. "+property);
            String sqlquery = rctx.get("sqlquery");
            
            String multivaluefieldseparator = rctx.get("multivaluefieldseparator");
            
            String codetable = rctx.get("codetable");
            //logger.warn("codetable. "+codetable);
            String code = rctx.get("code");
            //logger.warn("code. "+code);
            String value = rctx.get("value");
            //logger.warn("value. "+value);
            
			// Obtener el registro de la entidad principal
			IItemCollection itemCollection = entitiesAPI.getEntities(entityname, rctx.getNumExp());
			//logger.warn("itemCollection. "+itemCollection.toList().size());

			if (itemCollection.next()) {
				
				IItem entity = itemCollection.value();
				
            	if (StringUtils.isBlank(code)) {
            		code = ICatalogAPI.VALOR_FIELD_NAME.toUpperCase();
            	}
            	
            	if (StringUtils.isBlank(value)) {
            		value = ICatalogAPI.SUSTITUTO_FIELD_NAME.toUpperCase();
            	}				
				
				CTEntityDAO ctentity=EntityFactoryDAO.getInstance().getCatalogEntityDAO(cct.getConnection(),entityname);
				EntityDef entityDef = EntityDef.parseEntityDef(ctentity.getDefinition());
				EntityField entityField = entityDef.getField(property);
				//Si el campo es multivalor se sacan todos los sustitutos de todos los campos 
				if (entityField.isMultivalue()){
					DbResultSet dbrs = null;
					String stmt = null; 
					StringBuffer buffer = new StringBuffer();
					try{
						String multivalueTable = MultivalueTable.getInstance().composeMultivalueTableName(ctentity.getName(), entityField.getType().getJdbcType());
						stmt = "SELECT  "+codetable+".SUSTITUTO " 
							  +" FROM  " + multivalueTable + ", " + codetable
							  +" WHERE " + multivalueTable + "." + MultivalueTable.FIELD_FIELD + " = '" + DBUtil.replaceQuotes(entityField.getPhysicalName()) 
							  +"'  AND " + multivalueTable + "." + MultivalueTable.FIELD_REG +" = " + entity.getKeyInt()
							  +"   AND " + multivalueTable + "." + MultivalueTable.FIELD_VALUE + " = " + codetable+"."+code;
						//logger.warn("stmt. "+stmt);
						dbrs = cct.getConnection().executeQuery(stmt);
						while(dbrs.getResultSet().next()){
							buffer.append(dbrs.getResultSet().getString(value));
							if (StringUtils.isEmpty(multivaluefieldseparator)){
								buffer.append("\n");
							}else{
								buffer.append(StringUtils.unescapeJava(multivaluefieldseparator));
							}
						}
					} catch (SQLException e) {
						throw new ISPACRuleException(e);
					}finally{
				        if (dbrs != null)
				        	dbrs.close();
				    }		
					return buffer.toString().toUpperCase();
				}
				
				// Obtener el valor del código en la entidad principal
				String codevalue = entity.getString(property);
				//logger.warn("codevalue. "+codevalue);
				if (StringUtils.isNotBlank(codevalue)) {
					
					// Obtener registro de sustituto
					/*
					DbCnt cnt = cct.getConnection();
					EntityDAO codedao = new EntityDAO(cnt, codetable, code, "");
					
					// Cargar el sustituto a partir del valor del código
					// ya que al crear el EntityDAO el código se ha establecido como PK
					codedao.set(code, codevalue);
					codedao.load(cnt);
					
					// Valor de sustituto
					return codedao.getString(value);
					*/
					

					
					String sqlQuery = "WHERE "
									+ code 
									+ " = '"
									+ DBUtil.replaceQuotes(codevalue)
									+ "'";
					//logger.warn("sqlQuery. "+sqlQuery);
					itemCollection = entitiesAPI.queryEntities(codetable, sqlQuery);
					//logger.warn("itemCollection. "+itemCollection.toList().size());
					if (itemCollection.next()) {
						
						IItem substitute = itemCollection.value();
						String subtituto = substitute.getString(value);
						//logger.warn("subtituto "+subtituto);
						return subtituto.toUpperCase();
					}
				}
			}
			
			return "";
        }
        catch(ISPACException e)
        {
            throw new ISPACRuleException("Error obteniendo el valor sustituto.",e);
        }
    }

    public void cancel(IRuleContext rctx) throws ISPACRuleException
    {

    }
}

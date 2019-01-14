package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.sql.ResultSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TablaValidacionUtil;

/**
 * [dipucr-Felipe #716] 
 * Para mostrar valores de campos multivalor mediante tag
 *
 * <ul>
 * Atributos
 * <li>entity: Entidad a la que pertenece la propiedad/campo que queremos
 * obtener (OBLIGATORIO)</li>
 * <li>property: Propiedad/Campo a mostrar (OBLIGATORIO)</li>
 * <li>vldtable: Tabla de validación dónde buscar el sustituto (OBLIGATORIO)</li>
 * <li>separator: Separador de campos (OPCIONAL)</li>
 * </ul>
 * <p>
 * Ejemplos
 *
 * <ispactag rule='PropertyRegMultivalueSubstituteRule' entity='BDNS_IGAE_CONVOCATORIA'
 * property='SECTOR' vldtable='BDNS_SECT_ECONOM'/>
 *
 */
public class PropertyRegMultivalueSubstituteRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(PropertyRegMultivalueSubstituteRule.class);
	
	private static final String DEFAULT_SEPARATOR = ";";
	private static final String MULTIVALUE_TABLE_SUFFIX = "_S";
	
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("unchecked")
	public String execute(IRuleContext rulectx) throws ISPACRuleException{
    	
    	StringBuffer valor = new StringBuffer("");
    	String entity = null;
		String property = null;
		String vldtable = null;
    	
        try{
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        
	        entity = rulectx.get("entity");
			property = rulectx.get("property");
			vldtable = rulectx.get("vldtable");
			String separator = rulectx.get("separator");
			if (StringUtils.isEmpty(separator)){
				separator = DEFAULT_SEPARATOR;
			}
	        
	        String consulta = "WHERE NUMEXP = '" + rulectx.getNumExp() +"'";
			IItemCollection collection = entitiesAPI.queryEntities(entity, consulta);
	        Iterator<IItem> it = collection.iterator();
	        
	        if (it.hasNext()){

				String subentity = entity.concat(MULTIVALUE_TABLE_SUFFIX);
	        	
	        	IItem contrato = (IItem)it.next();
	        	int id = contrato.getInt("ID");
	        	
	        	consulta="SELECT VALUE FROM " + subentity +" WHERE REG_ID = " + id 
	        			+ " AND FIELD = '" + property +"' ORDER BY id";
	        	LOGGER.warn("consulta "+consulta);
		        ResultSet datos = cct.getConnection().executeQuery(consulta).getResultSet();
		        String value = "";
		        if(datos!=null)
		      	{
		        	while(datos.next()){
		          		if (datos.getString("VALUE")!=null) value = datos.getString("VALUE"); else value="";
		          		String sustituto = TablaValidacionUtil.getSustituto(entitiesAPI, vldtable, value);
		          		LOGGER.warn("valor: " + value + "; sustituto: " + value);
		          		valor.append(sustituto);
		          		valor.append(separator);
		          		valor.append("\n");
		          	}
		      	}
	        }
            
        } catch(Exception e) {
        	return e.getMessage();
        }
        LOGGER.warn("valor.toString() " + valor.toString());
		return valor.toString();
    }


	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
}



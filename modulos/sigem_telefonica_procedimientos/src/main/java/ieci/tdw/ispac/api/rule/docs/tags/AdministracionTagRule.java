package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que contiene un listado de documentos a subsanar.
 *
 */
public class AdministracionTagRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(AdministracionTagRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException 
	{
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException 
	{
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
        try
        {
        	String clasificacion = null;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator it = null;
	        IItem item = null;
	        String administracion = null;
        	
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
//	        clasificacion = rulectx.get("CLASIFICACION");
	        
//	        if ((clasificacion == null) || clasificacion.equals(""))
//	        {
//		        strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
//		        logger.warn("Query: " + strQuery);
//		        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
//		        it = collection.iterator();
//		        if (it.hasNext())
//		        {
//		        	item = (IItem)it.next();
//		        	
//		        	clasificacion = item.getString("CLASIFICACION");
//		        }
//	        }
	        
	        strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        logger.warn("Query: " + strQuery);
	        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
	        it = collection.iterator();
	        if (it.hasNext())
	        {
	        	item = (IItem)it.next();
	        	
	        	clasificacion = item.getString("CLASIFICACION");
	        }

	        if (clasificacion != null)
	        {
	        	if (clasificacion.equals("DIPUTACIÓN PROVINCIAL") ||
	        		clasificacion.equals("AYUNTAMIENTOS") ||
	        		clasificacion.equals("MANCOMUNIDADES"))
	        	{
	        		administracion = "administración local";
	        	}
	        	if (clasificacion.equals("PRESIDENCIA DE LA JUNTA") ||
	        		clasificacion.equals("CONSEJERÍAS") ||
	        		clasificacion.equals("DELEGACIONES PROVINCIALES"))
	        	{
	        		administracion = "administración autonómica";
	        	}
	        	if (clasificacion.equals("JEFATURA DEL ESTADO") ||
	        		clasificacion.equals("PRESIDENCIA DEL GOBIERNO") ||
	        		clasificacion.equals("MINISTERIOS") ||
	        		clasificacion.equals("DIRECCIONES GENERALES") ||
	        		clasificacion.equals("DELEGACIÓN DEL GOBIERNO EN CASTILLA-LA MANCHA") ||
	        		clasificacion.equals("SUBDELEGACIONES DEL GOBIERNO") ||
	        		clasificacion.equals("DIRECCIONES PROVINCIALES Y SERVICIOS PERIFÉRICOS"))
	        	{
	        		administracion = "administración estatal";
	        	}
	        	if (clasificacion.equals("TRIBUNAL SUPREMO") ||
	        		clasificacion.equals("JUNTA ELECTORAL CENTRAL") ||
	        		clasificacion.equals("AUDIENCIA NACIONAL") ||
	        		clasificacion.equals("TRIBUNAL SUPERIOR DE JUSTICIA DE CASTILLA-LA MANCHA") ||
	        		clasificacion.equals("TRIBUNALES SUPERIORES DE JUSTICIA") ||
	        		clasificacion.equals("AUDIENCIA PROVINCIAL DE CIUDAD REAL") ||
	        		clasificacion.equals("AUDIENCIAS PROVINCIALES") ||
	        		clasificacion.equals("JUNTA ELECTORAL PROVINCIAL") ||
	        		clasificacion.equals("JUNTAS ELECTORALES DE ZONA") ||
	        		clasificacion.equals("JUZGADOS DE LO SOCIAL") ||
	        		clasificacion.equals("JUZGADOS DE LO PENAL") ||
	        		clasificacion.equals("JUZGADOS DE LO CONTENCIOSO-ADMINISTRATIVO") ||
	        		clasificacion.equals("JUZGADOS DE PRIMERA INSTANCIA E INSTRUCCIÓN") ||
	        		clasificacion.equals("AGRUPACIONES DE JUZGADOS DE PAZ "))
	        	{
	        		administracion = "administración de justicia";
	        	}
	        	if (clasificacion.equals("OTRAS ADMINISTRACIONES"))
	        	{
	        		administracion = "otras administraciones";
	        	}
	        	if (clasificacion.equals("ANUNCIOS PARTICULARES"))
	        	{
	        		administracion = "anuncios particulares";
	        	}
	        	if (administracion == null)
	        	{
	        		administracion = "otras administraciones";
	        	}
	        }

	        return administracion;
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error confecionando listado de documentos a subsanar.", e);
	    }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}

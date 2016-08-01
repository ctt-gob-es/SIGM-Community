package aww.sigem.expropiaciones.util;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.rule.tags.FechaAprobacionEEFTagRule;

public class ExpropiacionesUtil {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ExpropiacionesUtil.class);

	/**
	 */
	static public String campoTablaEEF (String campo, IRuleContext rulectx) throws ISPACException{
		try {
			//logger.warn("Ejecutando regla ExpropiacionesUtil");
			
			// Obtener los campos de EEF que mas se usan desde las plantillas del procedimiento expropiado.
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			// Obtenemos  mediante el numero de expediente del hijo el expediente con el que está relacionado.
			//logger.warn("rulectx:" + rulectx.getNumExp());
			String strQuery = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "' AND RELACION LIKE '%Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			
			// Aunque devuelva varios resultados, va a ser el mismo expediente de expropiacion, por lo que buscamos uno de ellos.
			Iterator itRelaciones = collection.iterator();
			
			if (!itRelaciones.hasNext()){
				//logger.warn("No se han encontrado relaciones.");				
				return ("No se han encontrado relaciones.");
			}			
			
			IItem item = (IItem)itRelaciones.next();
			
			// Obtenemos la columna que contiene el expediente padre.
			String numExprop= item.getString("NUMEXP_PADRE");
			
			//logger.warn("EEF = " + numExprop);
			
			// Buscamos si tiene expediente padre dentro de la tabla expedientes relacionados es porque era un expropiado.
			String strQueryExprop = "WHERE NUMEXP_HIJO = '" + numExprop + "'";
			IItemCollection collectionExprop = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQueryExprop);
			
			
			// Si existe debe devolver al menos un valor.
			Iterator itExprop = collectionExprop.iterator();
			String strQueryEEF;
			IItemCollection collectionEEF;
			
			// Si no tiene padre es porque ya es una expropiacion
			if (!itExprop.hasNext()){
				strQueryEEF = "WHERE NUMEXP= '" + numExprop + "'";
				collectionEEF = entitiesAPI.queryEntities("EXPR_EEF", strQueryEEF);
			}
			else{
			
				IItem itemExp = (IItem)itExprop.next();				
				// Obtenemos la columna que contiene el expediente de la finca.
				String numEEF= itemExp.getString("NUMEXP_PADRE");
				
				strQueryEEF = "WHERE NUMEXP= '" + numEEF + "'";
				collectionEEF = entitiesAPI.queryEntities("EXPR_EEF", strQueryEEF);	
			}
			
			Iterator itEEF = collectionEEF.iterator();
			if (!itEEF.hasNext()){
				//logger.warn("No se han encontrado EEF relacionado.");
				return ("No se ha encontrado EEF relacionado.");
			}
			
			IItem itemEEF = (IItem)itEEF.next();
			String nombreObra = itemEEF.getString(campo);
			return (nombreObra);			
			
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}
	
}

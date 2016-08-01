package es.dipucr.sigem.api.rule.common.documento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.QueryUtils;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * Regla que crea el documento específico de trámite al iniciar el trámite
 * Además recupera los datos del decreto relacionado y los carga en la sesión
 * @author Felipe
 *
 */
public class DipucrAutogeneraDocEspecificoDatosDecretoRule extends DipucrAutogeneraDocumentoEspecificoInitTramite{

	private static final Logger logger = Logger.getLogger(DipucrAutogeneraDocEspecificoDatosDecretoRule.class);	
	
	/**
	 * Seteo las variables en la sesión
	 */
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		
		//Llamamos a la carga de variables del padre. Correspondiente a los posibles participantes
		super.setSsVariables(cct, rulectx);
		
		//Código específico
		try {
			IInvesflowAPI invesflowAPI = cct.getAPI(); 
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			String numexp = rulectx.getNumExp();
        	boolean bEncontrado = false;
			
			//Obtenemos el expediente de decreto (última resolución de los relacionados)
			String query = "WHERE NUMEXP_PADRE='" + numexp + "' " + QueryUtils.EXPRELACIONADOS.ORDER_DESC;
	        IItemCollection collection = entitiesAPI.queryEntities
	        		(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, query);
	        
	        @SuppressWarnings("unchecked")
			List<IItem> listRelacionados = collection.toList();
	        if (collection.toList().size() == 0){
	        	cct.setSsVariable("NUM_DECRETO", "[ERROR: El expediente no tiene ningún decreto relacionado]");
	        }
	        else{
	        	IItem itemRelacion = null;
	        	String numexpHijo = null;
	        	IItemCollection colDecreto = null;
	        	
	        	for (int i = 0; i < listRelacionados.size() && !bEncontrado; i++){
	        		itemRelacion = listRelacionados.get(i);
	        		numexpHijo = itemRelacion.getString("NUMEXP_HIJO");
	        		
	        		colDecreto = entitiesAPI.getEntities("SGD_DECRETO", numexpHijo);
	        		
	        		if(colDecreto.toList().size() > 0){

	        			IItem itemDatosDecreto = (IItem) colDecreto.iterator().next();
	        			Date fechaDecreto = itemDatosDecreto.getDate("FECHA_DECRETO");
	        			String numDecreto = itemDatosDecreto.getString("NUMERO_DECRETO");
	        			
	        			if (null == fechaDecreto || StringUtils.isEmpty(numDecreto)){
	        				cct.setSsVariable("NUM_DECRETO", "[ERROR: El expediente de decreto " +
	        						"relacionado no tiene número de decreto asignado]");
	        			}
	        			else{
	        				bEncontrado = true;
	        				String anioDecreto = itemDatosDecreto.getString("ANIO");
	        				cct.setSsVariable("NUM_DECRETO", numDecreto + "/" + anioDecreto);
	        				cct.setSsVariable("NUM_DECRETO_ONLY", numDecreto);
	        				cct.setSsVariable("ANIO_DECRETO", anioDecreto);
	        				cct.setSsVariable("FECHA_DECRETO", FechasUtil.getFormattedDate(fechaDecreto, "d 'de' MMMM 'de' yyyy"));
	        				cct.setSsVariable("FECHA_DECRETO2", FechasUtil.getFormattedDate(fechaDecreto));
	        				cct.setSsVariable("EXTRACTO_DECRETO", itemDatosDecreto.getString("EXTRACTO_DECRETO"));
	        			}
	        		}
	        	}
	        }
	        
	        if(!bEncontrado){
		        cct.setSsVariable("NUM_DECRETO", "[ERROR: El expediente no tiene ningún decreto relacionado]");
	        }
				
		} catch (ISPACException e) {
			logger.error("Error al setear las variables de sesión de decretos. " + e.getMessage(), e);
		}
	}

	/**
	 * Borramos las variables de la sesión
	 */
	public void deleteSsVariables(IClientContext cct) {
		
		//Lamamos al método padre
		super.deleteSsVariables(cct);
		
		//Código específico
		try {
			cct.deleteSsVariable("NUM_DECRETO");
			cct.deleteSsVariable("NUM_DECRETO_ONLY");
			cct.deleteSsVariable("ANIO_DECRETO");
			cct.deleteSsVariable("FECHA_DECRETO");
			cct.deleteSsVariable("FECHA_DECRETO2");
			cct.deleteSsVariable("EXTRACTO_DECRETO");
		} catch (ISPACException e) {
			logger.error("Error al setear las variables de sesión de decretos. " + e.getMessage(), e);
		}
	}
}

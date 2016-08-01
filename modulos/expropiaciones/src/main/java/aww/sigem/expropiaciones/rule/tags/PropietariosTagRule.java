package aww.sigem.expropiaciones.rule.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class PropietariosTagRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(PropietariosTagRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla PropietariosTagRule");		
			
			//Obtener las fincas relacionadas con la Expropiacion
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			String textoTabla = propietariosFinca(entitiesAPI,rulectx.getNumExp());				
				
			//logger.warn("Tabla dibujada:\n" + textoTabla);
			//logger.warn("Fin regla ListaFincasTagRule");
			return textoTabla;
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	private String propietariosFinca(IEntitiesAPI entitiesAPI, String numExpFinca) throws Exception{

		//Buscar los expedientes de los Expropiados
		//logger.warn("Expediente de Finca: " + numExpFinca);
		String strQuery = "WHERE NUMEXP_PADRE = '" + numExpFinca + "' AND RELACION = 'Expropiado/Finca'";
		IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
		Iterator it = collection.iterator();
		IItem item = null;	
		List expExpropiados = new ArrayList();		
		
		while (it.hasNext()) {
		   item = (IItem)it.next();
		   expExpropiados.add(item.getString("NUMEXP_HIJO"));
		   //logger.warn("Expediente de Expropiado: " + item.getString("NUMEXP_HIJO"));			
		}
		
		//Si la lista de expropiados está vacía devolver Desconocido
		if(expExpropiados.isEmpty()){
			return "Desconocido";
		}
		
		//Obtiene los datos de los expropiados
		Iterator itExpExpropiados = expExpropiados.iterator();
		strQuery="WHERE NUMEXP = '" + itExpExpropiados.next() + "'";
		while (itExpExpropiados.hasNext()) {
			strQuery+=" OR NUMEXP = '" + itExpExpropiados.next() + "'";				 		
		}			
		
		//logger.warn("Expropiados a buscar: " + strQuery);	
		
		IItemCollection collectionExpropiados = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
		
		//Si la lista de expropiados está vacía devolver Desconocido
		if(collectionExpropiados.toList().isEmpty()){
			return "Desconocido";
		}
		
		Iterator itExpropiados = collectionExpropiados.iterator();
		
		String expropiados = "";
		
		while (itExpropiados.hasNext()) {			 
			item = (IItem)itExpropiados.next();
			expropiados+=item.getString("NOMBRE");
			//Descomentar esto para que salgan los datos completos de los expropiados
			
			expropiados+=" ";
			expropiados+=item.getString("DIRNOT");
			expropiados+=" ";
			expropiados+=item.getString("LOCALIDAD");
			expropiados+=" (";
			expropiados+=item.getString("C_POSTAL");
			expropiados+=") ";
			expropiados+=item.getString("CAUT");
			
			expropiados+="; ";		
			
		}			
		
		//Si no se encuentra, es desconocido
		return expropiados;
		
	}

}

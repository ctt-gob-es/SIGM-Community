package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;


/**
 * 
 * @author teresa
 * @date 30/03/2010
 * @propósito Validar el campo Transición de un expediente al terminar una fase
 */
public class ValidateStatusStagePlaneamientoRule implements IRule {
	
	private static String STR_Tram_Publicacion_Segunda = "PLA141";
	//private static String STR_Tram_Publicacion_Tercera = "PLA250";
	private static String STR_Tram_Propuesta_Resolucion = "PLA241";
	private static String STR_Tram_Aprobacion_Definitiva = "PLA242";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        IItem exp = null;
	        String numExp = rulectx.getNumExp();
	        
	        String strQuery = "WHERE NUMEXP='" + numExp + "'";
	        IItemCollection collExps = entitiesAPI.queryEntities("PLAN_POM", strQuery);
	        Iterator itExps = collExps.iterator();
	        String transicion = null;
	        if (itExps.hasNext()) 
	        {
	        	exp = (IItem)itExps.next();
	        	transicion = exp.getString("TRANSICION");
	        	if (ValidateStatusTaskPlaneamientoRule.isTramiteIniciado(transicion)){
	        		rulectx.setInfoMessage("No se puede avanzar de fase ya que existe un trámite en proceso");
	        		return false;
	        	}else{
	        		
	        		String inicio = ValidateStatusTaskPlaneamientoRule.getSegundoTramite(transicion);

	        		int idCtStage = rulectx.getStageProcedureId();
	        		
	        		IItem itStage = null;
	    	        String nameStage = null;
	    	        strQuery = "WHERE ID='" + idCtStage + "'";
	    	        IItemCollection collStages = entitiesAPI.queryEntities("SPAC_P_FASES", strQuery);
	    	        Iterator itStages = collStages.iterator();
	    	        if (itStages.hasNext()){
	    	        	itStage = (IItem)itStages.next();
	    	        	nameStage = itStage.getString("NOMBRE");
	    	        	
	    	        	if (nameStage != null && !nameStage.equals("")){
	    	        		
	    	        		if (inicio.equals(STR_Tram_Publicacion_Segunda) && nameStage.equals("Información pública y Resolución")){
	    	        			
	    	        			strQuery = "WHERE NUMEXP='" +numExp+ "' AND INICIO='" +STR_Tram_Propuesta_Resolucion+ "' AND FIN='" +STR_Tram_Aprobacion_Definitiva+ "'";
			    	        	
				    	        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
				    	        Iterator itTransiciones = collTransiciones.iterator();
				    	        if (!itTransiciones.hasNext()){
					        		rulectx.setInfoMessage("Error al comprobar el estado actual");
					        		return false;
				    	        }else{
				    	        	IItem itTransicion = (IItem)itTransiciones.next();
				    	        	if (itTransicion.getInt("EJECUCIONES") < 1){
						        		rulectx.setInfoMessage("No se puede avanzar de fase desde el estado actual");
						        		return false;
				    	        	}
				    	        }
				    	        
	    	        		}else{
	    	        			
	    	        			strQuery = "WHERE NUMEXP='" +numExp+ "' AND INICIO='" + inicio + "' AND FIN='" + nameStage + "'";
	    	        			
				    	        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
				    	        Iterator itTransiciones = collTransiciones.iterator();
				    	        if (!itTransiciones.hasNext()){
					        		rulectx.setInfoMessage("No se puede avanzar de fase desde el estado actual");
					        		return false;
				    	        }
	    	        			
	    	        		}
	    	        	}else{
	    	        		rulectx.setInfoMessage("Error al obtener el nombre de la fase en el catálogo");
			        		return false;
	    	        	}
	    	        }else{
		        		rulectx.setInfoMessage("No se ha encontrado la fase actual en el catálogo de procedimientos");
		        		return false;
	    	        }
	    	        
	        	}
	        }
	        
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}
		
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
}

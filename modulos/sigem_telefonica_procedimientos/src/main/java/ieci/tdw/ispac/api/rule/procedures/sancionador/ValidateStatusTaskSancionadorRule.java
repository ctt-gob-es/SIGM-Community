package ieci.tdw.ispac.api.rule.procedures.sancionador;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

/**
 * 
 * @author teresa
 * @date 24/03/2010
 * @propósito Validar el campo Transición de un expediente al iniciar un trámite
 */
public class ValidateStatusTaskSancionadorRule implements IRule {

	private static String STR_entidad = "URB_SANCIONADOR";
	private static String STR_iniciado = "_I";
	private static String STR_cerrado = "_C";
	
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
	        IItemCollection collExps = entitiesAPI.queryEntities(STR_entidad, strQuery);
	        Iterator itExps = collExps.iterator();
	        String transicion = null;
	        if (itExps.hasNext()) 
	        {
	        	exp = (IItem)itExps.next();
	        	transicion = exp.getString("TRANSICION");
	        	if (isTramiteIniciado(transicion)){
	        		rulectx.setInfoMessage("No se puede iniciar el trámite ya que existe otro en proceso");
	        		return false;
	        	}else{
	        		
	        		//String inicio = transicion.substring(6, 11);
	        		String inicio = getSegundoTramite(transicion);
	        		String fin = null;
	        		
	        		int idCtTramite = rulectx.getClientContext().getAPI().getTask(rulectx.getTaskId()).getInt("ID_CTTRAMITE");
	    	        IItem itTram = null;
	    	        String codTram = null;
	    	        strQuery = "WHERE ID='" + idCtTramite + "'";
	    	        IItemCollection collTrams = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
	    	        Iterator itTrams = collTrams.iterator();
	    	        if (itTrams.hasNext()){
	    	        	itTram = (IItem)itTrams.next();
	    	        	codTram = itTram.getString("COD_TRAM");
	    	        	
	    	        	if (codTram != null && !codTram.equals("")){
	    	        		
	    	        		fin = tramiteMultiple (codTram, transicion);
	    	        		if (fin == null){
	    	        			fin = codTram;
	    	        		}
	    	        		
		    	        	//IItem itTransicion = null;
		    	        	strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin + "'";
			    	        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
			    	        Iterator itTransiciones = collTransiciones.iterator();
			    	        if (!itTransiciones.hasNext()){
				        		rulectx.setInfoMessage("No se puede iniciar el trámite desde el estado actual");
				        		return false;
			    	        }
	    	        	}else{
	    	        		rulectx.setInfoMessage("El trámite a iniciar no tiene definido un código de trámite");
			        		return false;
	    	        	}
	    	        }else{
		        		rulectx.setInfoMessage("No se ha encontrado el trámite en el catálogo de procedimientos");
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
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        IItem exp = null;
	        String numExp = rulectx.getNumExp();
	        String strQuery = "WHERE NUMEXP='" + numExp + "'";
	        IItemCollection collExps = entitiesAPI.queryEntities(STR_entidad, strQuery);
	        Iterator itExps = collExps.iterator();
	        String transicion = null;
	        if (itExps.hasNext()){
	        	exp = (IItem)itExps.next();
	        	transicion = exp.getString("TRANSICION");
	        	//if (transicion.equals("INICIADO")){
	        	if (isTramiteIniciado(transicion)){
	        		throw new ISPACInfo("No se puede iniciar el trámite ya que existe otro en proceso");
	        	}else{
	        		
	        		//String inicio = transicion.substring(6, 11);
	        		String inicio = getSegundoTramite(transicion);
	        		String fin = null;
	        		
	        		int idCtTramite = rulectx.getClientContext().getAPI().getTask(rulectx.getTaskId()).getInt("ID_CTTRAMITE");
	    	        IItem itTram = null;
	    	        String codTram = null;
	    	        strQuery = "WHERE ID='" + idCtTramite + "'";
	    	        IItemCollection collTrams = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
	    	        Iterator itTrams = collTrams.iterator();
	    	        if (itTrams.hasNext()){
	    	        	itTram = (IItem)itTrams.next();
	    	        	codTram = itTram.getString("COD_TRAM");
	    	        	
	    	        	if (codTram != null && !codTram.equals("")){
	    	        		
	    	        		fin = tramiteMultiple (codTram, transicion);
	    	        		if (fin == null){
	    	        			fin = codTram;
	    	        		}
	    	        		
		    	        	IItem itTransicion = null;
		    	        	strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin + "'";
			    	        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
			    	        Iterator itTransiciones = collTransiciones.iterator();
			    	        if (itTransiciones.hasNext()){
			    	        	//Incrementar el número de ejecuciones de esa transición
			    	        	itTransicion = (IItem)itTransiciones.next();
			    	        	int ejecuciones = itTransicion.getInt("EJECUCIONES");
			    	        	itTransicion.set("EJECUCIONES", ejecuciones+1);
			    	        	itTransicion.store(cct);
			    	        	
			    	        	//Actualizar la transición anterior y la actual
			    	        	exp.set("TRANSICION_ANTERIOR", transicion);
			    	        	exp.set("TRANSICION", inicio+"-"+fin+STR_iniciado);
			    	        	exp.store(cct);
			    	        	
			    	        }else{
			    	        	throw new ISPACInfo("No se puede iniciar el trámite desde el estado actual");
			    	        }
	    	        	}else{
	    	        		throw new ISPACInfo("El trámite a iniciar no tiene definido un código de trámite");
	    	        	}
	    	        }else{
	    	        	throw new ISPACInfo("No se ha encontrado el trámite en el catálogo de procedimientos");
	    	        }
	        	}
	        }else{
	        	throw new ISPACInfo("No se ha inicializado el estado del expediente");
	        }
	        
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}
		
		return null;
		
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	private String tramiteMultiple (String codTram, String transicion){
		//Si el tramite codTram es múltiple (Trámite de Audiencia) hay que tener en cuenta desde que estado (transicion)
		//se está iniciando la actual transición
		String inicio = getSegundoTramite(transicion);
		String fin = null;
		if (codTram.equals("SAN05")){
			//if (transicion.substring(6, 11).equals("SAN08")){
			if (inicio.equals("SAN08")){
				fin = "SAN09";
			}else if (inicio.equals("SAN13")){
				fin = "SAN14";
			}
		}
		return fin;
	}

	/**
	 * 
	 * @param transicion
	 * @return Devuelve el código del primer trámite de la transición
	 */
	protected static String getPrimerTramite(String transicion){
		
		String primerTramite = null;
		
		int posGuion = transicion.indexOf("-");
			
		primerTramite = transicion.substring(0, posGuion);		
		
		
		return primerTramite;
	}
	
	/**
	 * 
	 * @param transicion
	 * @return Devuelve el código del segundo trámite de la transición
	 */
	protected static String getSegundoTramite(String transicion){
		
		String segundoTramite = null;
		
		int posGuion = transicion.indexOf("-");
		int posGuionBajo = transicion.indexOf("_");
			
		segundoTramite = transicion.substring(posGuion+1, posGuionBajo);		
		
		
		return segundoTramite;
	}

	/**
	 * 
	 * @param transicion
	 * @return Devuelve true si el trámite actual está iniciado o false si está cerrado
	 * @throws ISPACInfo 
	 */
	protected static boolean isTramiteIniciado(String transicion) throws ISPACInfo{
		
		if (transicion.endsWith(STR_iniciado)){
			return true;
		}else if (transicion.endsWith(STR_cerrado)){
			return false;
		}else{
			throw new ISPACInfo("Error al comprobar el estado del trámite actual");
		}

	}
}

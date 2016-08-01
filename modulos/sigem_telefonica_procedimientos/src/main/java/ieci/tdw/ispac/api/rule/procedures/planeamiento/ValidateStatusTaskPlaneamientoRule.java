package ieci.tdw.ispac.api.rule.procedures.planeamiento;

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
 * @date 30/03/2010
 * @propósito Validar el campo Transición de un expediente al iniciar un trámite
 */
public class ValidateStatusTaskPlaneamientoRule implements IRule {

	private static String STR_entidad = "PLAN_POM";
	private static String STR_iniciado = "_I";
	private static String STR_cerrado = "_C";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{

	        //Obtenemos la última transicion ejecutada
	        IItem exp = null;
	        String transicion = null;
	        try{
        		exp = getExpediente(rulectx);
        		transicion = exp.getString("TRANSICION");
	        }catch (Exception e){
        		rulectx.setInfoMessage("Error al obtener la información del expediente");
        		return false;
	        }
        	
	        //Comprobamos que el trámite actual no se encuentre en estado iniciado (sino cerrado)
        	if (isTramiteIniciado(transicion)){
        		rulectx.setInfoMessage("No se puede iniciar el trámite ya que existe otro en proceso");
        		return false;
        	}else{
        		//Comprobamos si la transición inicio-fin a ejecutar existe en la tabla de transiciones
        		String inicio = getSegundoTramite(transicion);
        		String fin = null;
        		
        		int idCtTramite = rulectx.getClientContext().getAPI().getTask(rulectx.getTaskId()).getInt("ID_CTTRAMITE");
        		String codTram = getCodTram(idCtTramite, rulectx);
    	        
	        	if (codTram != null && !codTram.equals("")){
	        		fin = tramiteMultiple (codTram, transicion, rulectx);
	        		if (fin == null){
	        			fin = codTram;
	        		}
	        		try{
	        			getTransicion(inicio, fin, rulectx);
	        		}catch (ISPACInfo Exception){
		        		rulectx.setInfoMessage("No se puede iniciar el trámite desde el estado actual");
		        		return false;
	        		}
	        	}else{
	        		rulectx.setInfoMessage("El trámite a iniciar no tiene definido un código de trámite");
	        		return false;
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
	        //----------------------------------------------------------------------------------------------
	        
	        //Obtenemos la última transicion ejecutada
	        IItem exp = null;
	        String transicion = null;
	        try{
        		exp = getExpediente(rulectx);
        		transicion = exp.getString("TRANSICION");
	        }catch (Exception e){
        		throw new ISPACInfo("Error al obtener la información del expediente");
	        }
        	
	        //Comprobamos que el trámite actual no se encuentre en estado iniciado (sino cerrado)
        	if (isTramiteIniciado(transicion)){
        		throw new ISPACInfo("No se puede iniciar el trámite ya que existe otro en proceso");
        	}else{
        		//Comprobamos si la transición inicio-fin a ejecutar existe en la tabla de transiciones
        		String inicio = getSegundoTramite(transicion);
        		String fin = null;
        		
        		int idCtTramite = rulectx.getClientContext().getAPI().getTask(rulectx.getTaskId()).getInt("ID_CTTRAMITE");
        		String codTram = getCodTram(idCtTramite, rulectx);
        		
	        	if (codTram != null && !codTram.equals("")){
	        		
	        		fin = tramiteMultiple (codTram, transicion, rulectx);
	        		if (fin == null){
	        			fin = codTram;
	        		}
	        		
    	        	IItem itTransicion = null;
    	        	
	        		try{
	        			itTransicion = getTransicion(inicio, fin, rulectx);
	        			//Incrementar el número de ejecuciones de esa transición
	    	        	int ejecuciones = itTransicion.getInt("EJECUCIONES");
	    	        	itTransicion.set("EJECUCIONES", ejecuciones+1);
	    	        	itTransicion.store(cct);
	    	        	
	    	        	//Actualizar la transición anterior y la actual
	    	        	exp.set("TRANSICION_ANTERIOR", transicion);
	    	        	exp.set("TRANSICION", inicio+"-"+fin+STR_iniciado);
	    	        	exp.store(cct);
	        		}catch (ISPACInfo Exception){
	        			throw new ISPACInfo("No se puede iniciar el trámite desde el estado actual");
	        		}
    	        	
	        	}else{
	        		throw new ISPACInfo("El trámite a iniciar no tiene definido un código de trámite");
	        	}
    	       
        	}

	        
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}
		
		return null;
		
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	private String tramiteMultiple (String codTram, String transicion, IRuleContext rulectx) throws ISPACException{
		//Si el tramite codTram es múltiple (Trámite de Audiencia) hay que tener en cuenta desde que estado (transicion)
		//se está iniciando la actual transición
		String inicio = getSegundoTramite(transicion);
		String fin = null;
		
		// Trámite de Providencia
		if (codTram.equals("PLA010")){
			//if (transicion.substring(6, 11).equals("SAN08")){
			if (inicio.equals("PLA000")){
				fin = "PLA010";
			}else if (inicio.equals("PLA030") || inicio.equals("PLA042")){
				fin = "PLA050";
			}else if (inicio.equals("PLA190")){
				fin = "PLA200";
			}
		// Trámite de Publicación	
		}else if (codTram.equals("SAN19")){
			if (inicio.equals("PLA050")){
				fin = "PLA061";
			}else if (inicio.equals("PLA130")){
				fin = "PLA141";
			}else if (inicio.equals("PLA242")){/////////////////
				fin = "PLA141"; //ó fin = "PLA250";
			}else if (inicio.equals("PLA200")){
				fin = "PLA141";
				int numTransiciones = getNumEjecuciones(inicio, fin, rulectx);
				
				fin = "PLA150";
				numTransiciones = numTransiciones + getNumEjecuciones(inicio, fin, rulectx);

				if (numTransiciones > 0){
					throw new ISPACInfo("Esta transición únicamente se puede realizar una vez");
				}else{
					fin = "PLA141";
				}
				
			}
		// Trámite de Alegaciones	
		}else if (codTram.equals("PLA062")){
			if (inicio.equals("PLA061")){
				fin = "PLA062";
			}else if (inicio.equals("PLA141")){
				fin = "PLA142";
			}
		// Trámite de Remisión a Consejería de Medio Ambiente	
		}else if (codTram.equals("PLA080")){
			if (inicio.equals("PLA070")){
				fin = "PLA080";
			}else if (inicio.equals("PLA160")){
				fin = "PLA170";
			}
		// Trámite de Consultas sectoriales
		}else if (codTram.equals("PLA150")){
			if (inicio.equals("PLA200")){
				fin = "PLA150";
				int numTransiciones = getNumEjecuciones(inicio, fin, rulectx);
				
				fin = "PLA141";
				numTransiciones = numTransiciones + getNumEjecuciones(inicio, fin, rulectx);

				if (numTransiciones > 0){
					throw new ISPACInfo("Esta transición únicamente se puede realizar una vez");
				}else{
					fin = "PLA150";
				}
				
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

	/**
	 * 
	 * @param inicio, fin, entitiesAPI
	 * @return Devuelve la transición del expediente desde inicio hasta fin
	 * @throws ISPACInfo 
	 */
	protected static IItem getTransicion(String inicio, String fin, IRuleContext rulectx) throws ISPACException{
		
		//----------------------------------------------------------------------------------------------
        ClientContext cct = (ClientContext) rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
		
        String numExp = rulectx.getNumExp();
        
		IItem itTransicion = null;
		
		String strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin + "'";
		
	    IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
	    Iterator itTransiciones = collTransiciones.iterator();
	    if (!itTransiciones.hasNext()){
			throw new ISPACInfo("Error al comprobar el estado actual");
	    }else{
	    	itTransicion = (IItem)itTransiciones.next();
	    }
	    
	    return itTransicion;

	}
	

	/**
	 * 
	 * @param inicio, fin, entitiesAPI
	 * @return Devuelve el número de veces que se ha ejecutado la transición del expediente desde inicio hasta fin
	 * @throws ISPACInfo 
	 */
	protected static int getNumEjecuciones(String inicio, String fin, IRuleContext rulectx) throws ISPACException{
		
		//----------------------------------------------------------------------------------------------
        ClientContext cct = (ClientContext) rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
		
        String numExp = rulectx.getNumExp();
        
		int numEjecuciones = -1;
        
        String strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin + "'";
		
	    IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
	    Iterator itTransiciones = collTransiciones.iterator();
	    if (!itTransiciones.hasNext()){
			throw new ISPACInfo("Error al comprobar el estado actual");
	    }else{
	    	IItem itTransicion = (IItem)itTransiciones.next();
	    	numEjecuciones = itTransicion.getInt("EJECUCIONES");
	    }
	    
	    return numEjecuciones;

	}
	
	/**
	 * 
	 * @param idCtTramite, entitiesAPI
	 * @return Devuelve el código de trámite correspondiente al identificador idCtTramite
	 * @throws ISPACInfo 
	 */
	protected static String getCodTram(int idCtTramite, IRuleContext rulectx) throws ISPACException{
		
		//----------------------------------------------------------------------------------------------
        ClientContext cct = (ClientContext) rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
		
		String codTram = null;
		
	    IItem itTram = null;
	    String strQuery = "WHERE ID='" + idCtTramite + "'";
	    IItemCollection collTrams = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
	    Iterator itTrams = collTrams.iterator();
	    if (itTrams.hasNext()){
	    	itTram = (IItem)itTrams.next();
	    	codTram = itTram.getString("COD_TRAM");
	    }else{
	    	throw new ISPACInfo("Error al obtener el código de trámite");
	    }
	    
	    return codTram;
	    
	}
	
	/**
	 * 
	 * @param numExp, entitiesAPI
	 * @return Devuelve el expediente con número de expediente numExp de la entidad STR_entidad
	 * @throws ISPACInfo 
	 */
	protected static IItem getExpediente(IRuleContext rulectx) throws ISPACException{
		
		//----------------------------------------------------------------------------------------------
        ClientContext cct = (ClientContext) rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String numExp = rulectx.getNumExp();
		
        IItem exp = null;
        
        String strQuery = "WHERE NUMEXP='" + numExp + "'";
        IItemCollection collExps = entitiesAPI.queryEntities(STR_entidad, strQuery);
        Iterator itExps = collExps.iterator();
        if (itExps.hasNext()) 
        {
        	exp = (IItem)itExps.next();
        }else{
        	throw new ISPACInfo("Error al obtener la información del expediente");
        }
        
        return exp;
	}
	
}

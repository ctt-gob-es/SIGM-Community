package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * @author teresa
 * @date 30/03/2010
 * @propósito Validar el campo Transición de un expediente al iniciar un trámite
 */
public class ValidateStatusTaskPlaneamientoRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(ValidateStatusTaskPlaneamientoRule.class);

    private static String strEntidad = "PLAN_POM";
    private static String strIniciado = "_I";
    private static String strCerrado = "_C";
    
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
                
            } catch (Exception e){
                LOGGER.info("No se mostraba mensaje alguno. Error al obtener la información del expediente. " + e.getMessage(), e);

                rulectx.setInfoMessage("Error al obtener la información del expediente");
                return false;
            }
            
            //Comprobamos que el trámite actual no se encuentre en estado iniciado (sino cerrado)
            if (isTramiteIniciado(transicion)) {
                rulectx.setInfoMessage("No se puede iniciar el trámite ya que existe otro en proceso");
                return false;
                
            } else {
                //Comprobamos si la transición inicio-fin a ejecutar existe en la tabla de transiciones
                String inicio = getSegundoTramite(transicion);
                String fin = null;
                
                int idCtTramite = rulectx.getClientContext().getAPI().getTask(rulectx.getTaskId()).getInt("ID_CTTRAMITE");
                String codTram = getCodTram(idCtTramite, rulectx);
                
                if (StringUtils.isNotEmpty(codTram)){
                    fin = tramiteMultiple (codTram, transicion, rulectx);
                    
                    if (fin == null){
                        fin = codTram;
                    }
                    
                    try{
                        getTransicion(inicio, fin, rulectx);
                        
                    } catch (ISPACInfo exception){
                        LOGGER.info("No se mostraba mensaje alguno. No se puede iniciar el tr´maite desde el estado actual. " + exception.getMessage(), exception);

                        rulectx.setInfoMessage("No se puede iniciar el trámite desde el estado actual");                        
                        return false;
                    }
                } else {
                    rulectx.setInfoMessage("El trámite a iniciar no tiene definido un código de trámite");
                    return false;
                }
                
            }
        } catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            //----------------------------------------------------------------------------------------------
            
            //Obtenemos la última transicion ejecutada
            IItem exp = null;
            String transicion = null;
            
            try{
                exp = getExpediente(rulectx);
                transicion = exp.getString("TRANSICION");
                
            } catch (Exception e){
                LOGGER.info("No se mostraba mensaje alguno. Error al obtener la información del expediente. " + e.getMessage(), e);
                throw new ISPACInfo("Error al obtener la información del expediente");
            }
            
            //Comprobamos que el trámite actual no se encuentre en estado iniciado (sino cerrado)
            if (isTramiteIniciado(transicion)){
                throw new ISPACInfo("No se puede iniciar el trámite ya que existe otro en proceso");
                
            } else {
                //Comprobamos si la transición inicio-fin a ejecutar existe en la tabla de transiciones
                String inicio = getSegundoTramite(transicion);
                String fin = null;
                
                int idCtTramite = rulectx.getClientContext().getAPI().getTask(rulectx.getTaskId()).getInt("ID_CTTRAMITE");
                String codTram = getCodTram(idCtTramite, rulectx);
                
                if (StringUtils.isNotEmpty(codTram)){
                    
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
                        exp.set("TRANSICION", inicio+"-"+fin+strIniciado);
                        exp.store(cct);
                        
                    } catch (ISPACInfo exception){
                        LOGGER.info("No se mostraba mensaje alguno. No se puede iniciar el trámite desde el estado actual. " + exception.getMessage(), exception);

                        throw new ISPACInfo("No se puede iniciar el trámite desde el estado actual");
                    }
                    
                } else {
                    throw new ISPACInfo("El trámite a iniciar no tiene definido un código de trámite");
                }
            }
            
        } catch(ISPACException e) {
            throw new ISPACRuleException(e);
        }
        return null;
        
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
    
    private String tramiteMultiple (String codTram, String transicion, IRuleContext rulectx) throws ISPACException{
        //Si el tramite codTram es múltiple (Trámite de Audiencia) hay que tener en cuenta desde que estado (transicion)
        //se está iniciando la actual transición
        String inicio = getSegundoTramite(transicion);
        String fin = null;
        
        // Trámite de Providencia
        if ("PLA010".equals(codTram)){
            //if (transicion.substring(6, 11).equals("SAN08")){
            if ("PLA000".equals(inicio)){
                fin = "PLA010";
            } else if ("PLA030".equals(inicio) || "PLA042".equals(inicio)){
                fin = "PLA050";
            } else if ("PLA190".equals(inicio)){
                fin = "PLA200";
            }
        // Trámite de Publicación    
        } else if ("SAN19".equals(codTram)){
            if ("PLA050".equals(inicio)){
                fin = "PLA061";
            } else if ("PLA130".equals(inicio)){
                fin = "PLA141";
            } else if ("PLA242".equals(inicio)){/////////////////
                fin = "PLA141"; //ó fin = "PLA250";
            } else if ("PLA200".equals(inicio)){
                fin = "PLA141";
                int numTransiciones = getNumEjecuciones(inicio, fin, rulectx);
                
                fin = "PLA150";
                numTransiciones = numTransiciones + getNumEjecuciones(inicio, fin, rulectx);

                if (numTransiciones > 0){
                    throw new ISPACInfo("Esta transición únicamente se puede realizar una vez");
                } else{
                    fin = "PLA141";
                }
                
            }
        // Trámite de Alegaciones    
        } else if ("PLA062".equals(codTram)){
            if ("PLA061".equals(inicio)){
                fin = "PLA062";
            } else if ("PLA141".equals(inicio)){
                fin = "PLA142";
            }
        // Trámite de Remisión a Consejería de Medio Ambiente    
        } else if ("PLA080".equals(codTram)){
            if ("PLA070".equals(inicio)){
                fin = "PLA080";
            } else if ("PLA160".equals(inicio)){
                fin = "PLA170";
            }
        // Trámite de Consultas sectoriales
        } else if ("PLA150".equals(codTram) && "PLA200".equals(inicio)){
            fin = "PLA150";
            int numTransiciones = getNumEjecuciones(inicio, fin, rulectx);
            
            fin = "PLA141";
            numTransiciones = numTransiciones + getNumEjecuciones(inicio, fin, rulectx);

            if (numTransiciones > 0){
                throw new ISPACInfo("Esta transición únicamente se puede realizar una vez");
            } else{
                fin = "PLA150";
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
        
        int posGuion = transicion.indexOf('-');
            
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
        
        int posGuion = transicion.indexOf('-');
        int posGuionBajo = transicion.indexOf('_');
            
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
        
        if (transicion.endsWith(strIniciado)){
            return true;
        } else if (transicion.endsWith(strCerrado)){
            return false;
        } else{
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
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String numExp = rulectx.getNumExp();
        
        IItem itTransicion = null;
        
        String strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin + "'";
        
        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
        Iterator<?> itTransiciones = collTransiciones.iterator();
        
        if (!itTransiciones.hasNext()){
            throw new ISPACInfo("Error al comprobar el estado actual");
        } else{
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
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String numExp = rulectx.getNumExp();
        
        int numEjecuciones = -1;
        
        String strQuery = "WHERE NUMEXP='" + numExp + "' AND INICIO='" + inicio + "' AND FIN='" + fin + "'";
        
        IItemCollection collTransiciones = entitiesAPI.queryEntities("TSOL_TRANSICIONES", strQuery);
        Iterator<?> itTransiciones = collTransiciones.iterator();
        
        if (!itTransiciones.hasNext()){
            throw new ISPACInfo("Error al comprobar el estado actual");
        } else{
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
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String codTram = null;
        
        IItem itTram = null;
        String strQuery = "WHERE ID='" + idCtTramite + "'";
        IItemCollection collTrams = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
        Iterator<?> itTrams = collTrams.iterator();
        
        if (itTrams.hasNext()){
            itTram = (IItem)itTrams.next();
            codTram = itTram.getString("COD_TRAM");
        
        } else {
            throw new ISPACInfo("Error al obtener el código de trámite");
        }
        
        return codTram;
        
    }
    
    /**
     * 
     * @param numExp, entitiesAPI
     * @return Devuelve el expediente con número de expediente numExp de la entidad strEntidad
     * @throws ISPACInfo 
     */
    protected static IItem getExpediente(IRuleContext rulectx) throws ISPACException{
        
        //----------------------------------------------------------------------------------------------
        IClientContext cct =  rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        //----------------------------------------------------------------------------------------------
        
        String numExp = rulectx.getNumExp();
        
        IItem exp = null;
        
        IItemCollection collExps = entitiesAPI.getEntities(strEntidad, numExp);
        Iterator<?> itExps = collExps.iterator();
        
        if (itExps.hasNext()) {
            exp = (IItem)itExps.next();
            
        } else {
            throw new ISPACInfo("Error al obtener la información del expediente");
        }
        
        return exp;
    }
    
}

package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.util.ListCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * 
 * @since 04.08.2010
 */
public class TramitesUtil {

	/** Logger de la clase. */
	protected static final Logger LOGGER = Logger.getLogger(TramitesUtil.class);
	
	public static final String SEPARADOR_PROPIEDADES_DATOS_ESPECIFICOS = "##";
	public static final String CORREOS_FIN_TRAMITE = "CORREOS_FIN_TRAMITE";
	
	public static final String SPAC_DT_TRAMITES = "SPAC_DT_TRAMITES";
	public static final String SPAC_DT_TRAMITES_H = "SPAC_DT_TRAMITES_H";
	
	//Mapeo de las columnas de la tablas SPAC_DT_TRAMITES y SPAC_DT_TRAMITES_H
	public static final String NUMEXP = "NUMEXP";
	public static final String ID_TRAM_PCD = "ID_TRAM_PCD";
	public static final String NOMBRE = "NOMBRE";
	public static final String FECHA_CIERRE = "FECHA_CIERRE";
	
	
	//Mapeo de las propiedades que se incluyen en los datos específicos.
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA1 = "TABLA1";
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA2 = "TABLA2";
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA3 = "TABLA3";
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA4 = "TABLA4";
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA5 = "TABLA5";
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA6 = "TABLA6";
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA7 = "TABLA7";
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA8 = "TABLA8";
	public static final String DATOS_ESPECIFICOS_PROPIEDAD_TABLA9 = "TABLA9";
	
	public static void cargarObservacionesTramite (ClientContext cct, boolean campoFormateo, String numexp, int tramite, String texto) throws ISPACRuleException, ISPACException{
		try{
	        IItem itTram = TramitesUtil.getTramite(cct, numexp, tramite);
			String observaciones = "";
			if(StringUtils.isNotEmpty(itTram.getString("OBSERVACIONES"))){
				observaciones = itTram.getString("OBSERVACIONES");			
			}
			if(campoFormateo){
				observaciones=texto;
			}
			else{
				observaciones=observaciones+"\n"+texto;
			}
			itTram.set("OBSERVACIONES", observaciones);
			itTram.store(cct);
		
		} catch(ISPACException e){
			LOGGER.error("Error al cargar las observaciones en el numExp " + numexp+ " . " + e.getMessage(), e);
			throw new ISPACException("Error al cargar las observaciones en el numExp " + numexp+ " . " + e.getMessage(), e);
		}
	}
	public static IItem getPTramiteById (IRuleContext rulectx, int idTramite) throws ISPACRuleException{
		IClientContext cct = rulectx.getClientContext();
		return getPTramiteById(cct, idTramite);
	}
	public static IItem getPTramiteById (IClientContext cct, int idTramite) throws ISPACRuleException{
		IItem itTramite  = null;
		
		try{
			//----------------------------------------------------------------------------------------------	        
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
		
			//Obtenemos el id del trámite de "Preparación del Anuncio" en el Catálogo a partir de su código
			IItemCollection pTramitesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, "WHERE ID = " + idTramite);
			Iterator<?> it = pTramitesCollection.iterator();
	        if (it.hasNext()){
	        	itTramite = (IItem)it.next();
	        }
		} catch(Exception e){
			LOGGER.error("Error al recuperar el trámite con id " + idTramite+ " . " + e.getMessage(), e);
			throw new ISPACRuleException("Error al recuperar el trámite con id " + idTramite+ " . " + e.getMessage(), e);
		}
		
		return itTramite;		
	}
	
	@Deprecated
	public static IItem getTramiteByCode (IRuleContext rulectx, String codTramite) throws ISPACRuleException {
		return getTramiteByCode(rulectx.getClientContext(), rulectx.getNumExp(), codTramite);
	}
		
	public static IItem getTramiteByCode (IClientContext cct, String numexp, String codTramite) throws ISPACRuleException {
		IItem itTramite  = null;
		
		try{
			//----------------------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
		
			//Obtenemos el id del trámite de "Preparación del Anuncio" en el Catálogo a partir de su código
			String strQuery = "WHERE COD_TRAM = '"+ codTramite +"'";
			LOGGER.debug("strQuery: "+strQuery);
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
			Iterator<?> it = collection.iterator();
	        if (it.hasNext()){
	        	itTramite = (IItem)it.next();
	        }else{
	        	throw new ISPACInfo("No se ha encontrado el trámite con código " + codTramite);
	        }
	        
		} catch(Exception e){
			LOGGER.error("Error al crear el trámite " + codTramite + " en el expediente " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al crear el trámite " + codTramite + " en el expediente " + numexp + ". " + e.getMessage(), e);
		}
		
		
		return itTramite;		
	}
	
	@SuppressWarnings("rawtypes")
	public static int crearTramite(String codTramite, IRuleContext rulectx) throws ISPACRuleException{
		
		int idTask = Integer.MIN_VALUE;
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			int stageId = rulectx.getStageId();//Id de la fase en el proceso (en ejecución)
			int taskIdCat = 0;
			
			//Obtener el taskId (id del trámite) del trámite de "Preparación del anuncio"
			
			//Obtenemos el id del trámite de "Preparación del Anuncio" en el Catálogo a partir de su código
			String strQuery = "WHERE COD_TRAM = '"+ codTramite +"'";
			LOGGER.debug("strQuery: "+strQuery);
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
			Iterator it = collection.iterator();
	        if (it.hasNext()){
	        	taskIdCat = ((IItem)it.next()).getInt("ID");
	        }else{
	        	throw new ISPACInfo("No se ha encontrado el trámite con código " + codTramite);
	        }
			
			//Obtenemos el id de la fase actual "Preparación" en el procedimiento, no en el catálogo
	        int idStagePcd = 0;
	        strQuery = "WHERE ID = '"+ stageId +"'";
			LOGGER.debug("strQuery: "+strQuery);
	        collection = entitiesAPI.queryEntities(SpacEntities.SPAC_FASES, strQuery);
	        it = collection.iterator();
	        if (it.hasNext()){
	        	idStagePcd = ((IItem)it.next()).getInt("ID_FASE");
	        }else{
	        	throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
	        }
	        
			//Obtenemos el id del trámite de "Preparación del Anuncio" en el procedimiento, no en el catálogo
	        int idTaskPcd = 0;
	        strQuery = "WHERE ID_FASE = '"+ idStagePcd +"' AND ID_CTTRAMITE = '"+ taskIdCat +"'";
			LOGGER.debug("strQuery: "+strQuery);
	        collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
	        it = collection.iterator();
	        if (it.hasNext()){
	        	idTaskPcd = ((IItem)it.next()).getInt("ID");
	        }else{
	        	throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
	        }
	        
	        //Creación del trámite
			idTask = tx.createTask(stageId, idTaskPcd);
			
		} catch(Exception e){
			LOGGER.error("Error al crear el trámite " + codTramite + " en el expediente " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al crear el trámite " + codTramite + " en el expediente " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		
		return idTask;
	}
	
	public static int crearTramiteDesdeRegistro(String codTramite, IRuleContext rulectx) throws ISPACRuleException{
		
		int idTask = Integer.MIN_VALUE;
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			IItem itemCtTramite = TramitesUtil.getTramiteByCode(rulectx, codTramite);
			String nombreTramite = itemCtTramite.getString("NOMBRE");
			
			String strQuery = null;
			IItemCollection collection = null;
			int pid = rulectx.getProcessId();
			
			//Necesitamos sacar la fase y la fasePcd, que no vienen en el contexto
			IItemCollection stages = invesFlowAPI.getStagesProcess(pid);
			IItem itemFase = (IItem) stages.toList().get(0);
			int idFase = Integer.valueOf(itemFase.getString("ID_FASE_BPM"));
			int idFasePcd = itemFase.getInt("ID_FASE");
			
			//Obtenemos primero el id del trámite en el diseño, tabla spac_p_tramites
			strQuery = "WHERE ID_FASE = " + idFasePcd + " AND NOMBRE = '" + nombreTramite + "'";
			collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQuery);
			IItem itemTramite = (IItem)collection.iterator().next();
			int idTramitePcd = itemTramite.getInt("ID"); 
			
			//Creamos el trámite
			tx.createTask(idFase, idTramitePcd);
			
		} catch(Exception e){
			LOGGER.error("Error al crear el trámite " + codTramite + " en el expediente " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al crear el trámite " + codTramite + " en el expediente " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		
		return idTask;
	}
	
	/**
	 * [eCenpri-Teresa 08.10.2013] Crea un tramita a partir del código del trámite
	 * 
	 * @param cct
	 * @param codTramite
	 * @param numexp
	 * @throws ISPACRuleException
	 */
	@SuppressWarnings("unchecked")
	public static int crearTramite(ClientContext cct, String codTramite, String numexp) throws ISPACRuleException{
		
		int idTask = Integer.MIN_VALUE;
		
		try{
			//----------------------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			
			IProcess itemProcess = invesFlowAPI.getProcess(numexp);
        	int idProcess = itemProcess.getInt("ID");
			IItemCollection collExpsAux = invesFlowAPI.getStagesProcess(idProcess);
			Iterator<IItem> itExpsAux = collExpsAux.iterator();
			
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			int Id_fase_spac_p_tramites = iExpedienteAux.getInt("ID_FASE"); 
			
			
			//Obtenemos idTramite
			String strQuery = "WHERE COD_TRAM = '"+ codTramite +"'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
			Iterator<IItem> it = collection.iterator();
			int taskIdCat = 0;
	        if (it.hasNext()){
	        	taskIdCat = ((IItem)it.next()).getInt("ID");
	        }else{
	        	throw new ISPACInfo("No se ha encontrado el trámite de Prescripciones técnicas.");
	        }
	        
	        strQuery = "WHERE ID_FASE="+Id_fase_spac_p_tramites+" AND ID_CTTRAMITE = '"+ taskIdCat +"'";
			LOGGER.debug("strQuery: "+strQuery);
	        collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
	        it = collection.iterator();
	        int idTramite = 0;
	        
	        if (it.hasNext()){
	        	idTramite = ((IItem)it.next()).getInt("ID");
	        }else{
	        	throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
	        }
			
			
			idTask = tx.createTask(idFase, idTramite);
			
		} catch(Exception e){
			LOGGER.error("Error al crear el trámite " + codTramite + " en el expediente " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al crear el trámite " + codTramite + " en el expediente " + numexp + ". " + e.getMessage(), e);
		}
		
		return idTask;
	}
	
	@SuppressWarnings("rawtypes")
	public static int crearTramite(int idTramite, IRuleContext rulectx) throws ISPACRuleException{
		
		int idTask = Integer.MIN_VALUE;
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			int stageId = rulectx.getStageId();//Id de la fase en el proceso (en ejecución)
			int taskIdCat = 0;
			
			//Obtener el taskId (id del trámite) del trámite de "Preparación del anuncio"
			
			//Obtenemos el id del trámite de "Preparación del Anuncio" en el Catálogo a partir de su código
			String strQuery = "WHERE ID = "+ idTramite +"";
			LOGGER.debug("strQuery: "+strQuery);
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
			Iterator it = collection.iterator();
	        if (it.hasNext()){
	        	taskIdCat = ((IItem)it.next()).getInt("ID");
	        }else{
	        	throw new ISPACInfo("No se ha encontrado el trámite con el id " + idTramite);
	        }
			
			//Obtenemos el id de la fase actual "Preparación" en el procedimiento, no en el catálogo
	        int idStagePcd = 0;
	        strQuery = "WHERE ID = '"+ stageId +"'";
			LOGGER.debug("strQuery: "+strQuery);
	        collection = entitiesAPI.queryEntities(SpacEntities.SPAC_FASES, strQuery);
	        it = collection.iterator();
	        if (it.hasNext()){
	        	idStagePcd = ((IItem)it.next()).getInt("ID_FASE");
	        }else{
	        	throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
	        }
	        
			//Obtenemos el id del trámite de "Preparación del Anuncio" en el procedimiento, no en el catálogo
	        int idTaskPcd = 0;
	        strQuery = "WHERE ID_FASE = '"+ idStagePcd +"' AND ID_CTTRAMITE = '"+ taskIdCat +"'";
			LOGGER.debug("strQuery: "+strQuery);
	        collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
	        it = collection.iterator();
	        if (it.hasNext()){
	        	idTaskPcd = ((IItem)it.next()).getInt("ID");
	        }else{
	        	throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
	        }
	        
	        //Creación del trámite
			idTask = tx.createTask(stageId, idTaskPcd);
			
		} catch(Exception e){
			LOGGER.error("Error al crear el trámite " + idTramite + " en el expediente " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al crear el trámite " + idTramite + " en el expediente " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		
		return idTask;
	}
	
	/**
	 * Cierra el trámite a partir de su id
	 * @param idTramBpm
	 * @param rulectx
	 * @throws ISPACRuleException
	 */
	@Deprecated
	public static void cerrarTramite(int idTramite, IRuleContext rulectx) throws ISPACRuleException{
		cerrarTramite(rulectx.getClientContext(), idTramite, rulectx.getNumExp());
	}
	
	/**
	 * Cierra el trámite a partir de su id
	 * @param idTramBpm
	 * @param rulectx
	 * @throws ISPACRuleException
	 */
	public static void cerrarTramite(IClientContext cct, int idTramite, String numexp) throws ISPACRuleException{
		
		try{
			//----------------------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			//Cerramos el trámite
	        String strQuery = "WHERE ID = '" + idTramite + "'";
	        IItemCollection collectionTrams = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery);
	        Iterator<?> itTrams = collectionTrams.iterator();
        	IItem tram = null;
	        if (itTrams.hasNext()){
	        	tram = ((IItem)itTrams.next());
	        	int idTram = tram.getInt("ID");
	        	tx.closeTask(idTram);
	        }
			
		} catch(Exception e) {
			LOGGER.error("Error al cerrar el trámite " + idTramite + " en el expediente " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al cerrar el trámite " + idTramite + " en el expediente " + numexp + ". " + e.getMessage(), e);
		}
	}
	
	/**
	 * Tramites abiertos
	 * @param query
	 * @param rulectx
	 * @throws ISPACRuleException
	 */
	@SuppressWarnings("rawtypes")
	public static IItemCollection tramitesAbiertos(IRuleContext rulectx, String query) throws ISPACRuleException{
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			//Cerramos el trámite
	        IItemCollection collectionTrams = entitiesAPI.queryEntities("SPAC_TRAMITES", query);
	        Iterator itTrams = collectionTrams.iterator();
        	IItem tram = null;
	        if (itTrams.hasNext()) 
	        {
	        	tram = ((IItem)itTrams.next());
	        	int idTram = tram.getInt("ID");
	        	tx.closeTask(idTram);
	        }
	        return collectionTrams;
			
		} catch(Exception e){
			LOGGER.error("Error en la consulta " + query + " en el expediente " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error en la consulta " + query + " en el expediente " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		
	}
	
	
	/**
	 * Recupera el código del trámite a partir de su id en el catálogo
	 * @param idTramiteCtl
	 * @param rulectx
	 * @throws ISPACRuleException
	 */
	@SuppressWarnings("rawtypes")
	public static String getCodTram(int idTramiteCtl, IClientContext cct) throws ISPACRuleException{
		
		try{
			//----------------------------------------------------------------------------------------------	        
	        IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String codTram = null;
			
			//Obtenemos los datos del trámite en el catálogo
			String strQuery = "WHERE ID = "+ idTramiteCtl +"";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
			Iterator it = collection.iterator();
	        if (it.hasNext()){
	        	codTram = ((IItem)it.next()).getString("COD_TRAM");
	        }
	        else{
	        	throw new ISPACInfo("No se ha encontrado el trámite con el id " + idTramiteCtl);
	        }
	        
	        return codTram;
			
		} catch(Exception e){
			LOGGER.error("Error al recuperar el código del trámite " + idTramiteCtl + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al recuperar el código del trámite " + idTramiteCtl + ". " + e.getMessage(), e);
		}
	}
	
	/**
	 * Devuelve 'true' si ya existía con anterioridad otro trámite con el nombre 'tramite'
	 * @param rulectx
	 * @param tramite
	 * @return
	 * @throws ISPACException
	 */	
	public static boolean existeTramite(IRuleContext rulectx, String nombreTramite) throws ISPACException{
		boolean existe = false;
		try{
			IClientContext cct = rulectx.getClientContext();
			
	        IItemCollection tramitesCollection = TramitesUtil.getTramites(cct, rulectx.getNumExp(), " NOMBRE = '" + nombreTramite + "'", "");
	        for(Object tramiteItem : tramitesCollection.toList()){
	        	//Parece que existe pero puede ser este mismo trámite
	        	String strId = ((IItem)tramiteItem).getString("ID");
	        	int id = Integer.parseInt(strId);
	        	existe = id != rulectx.getTaskId();
	        }
	        
		} catch(Exception e) {
            LOGGER.error("Error al obtener el existeTramite. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
            throw new ISPACException("Error al obtener el existeTramite. "+rulectx.getNumExp()+" - "+e.getMessage(), e);        
        }
		
		return existe;
	}
	
	public static void copiaDtTramites(IClientContext cct, String tabla, IItem tramite_viejo, String numexp) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		IItem tramite_nuevo = entitiesAPI.createEntity(tabla, numexp);

		tramite_nuevo.set("ID", tramite_viejo.getInt("ID"));
		tramite_nuevo.set("NUMEXP", tramite_viejo.getString("NUMEXP"));
		tramite_nuevo.set("NOMBRE", tramite_viejo.getString("NOMBRE"));
		tramite_nuevo.set("ESTADO", tramite_viejo.getInt("ESTADO"));
		tramite_nuevo.set("ID_TRAM_PCD", tramite_viejo.getInt("ID_TRAM_PCD"));
		tramite_nuevo.set("ID_FASE_PCD", tramite_viejo.getInt("ID_FASE_PCD"));
		tramite_nuevo.set("ID_FASE_EXP", tramite_viejo.getInt("ID_FASE_EXP"));
		tramite_nuevo.set("ID_TRAM_EXP", tramite_viejo.getInt("ID_TRAM_EXP"));

		tramite_nuevo.set("ID_TRAM_CTL", tramite_viejo.getInt("ID_TRAM_CTL"));
		tramite_nuevo.set("NUM_ACTO", tramite_viejo.getString("NUM_ACTO"));
		tramite_nuevo.set("COD_ACTO", tramite_viejo.getString("COD_ACTO"));
		tramite_nuevo.set("ESTADO_INFO", tramite_viejo.getString("ESTADO_INFO"));
		tramite_nuevo.set("FECHA_INICIO", tramite_viejo.getDate("FECHA_INICIO"));
		tramite_nuevo.set("FECHA_CIERRE", tramite_viejo.getDate("FECHA_CIERRE"));
		tramite_nuevo.set("FECHA_LIMITE", tramite_viejo.getDate("FECHA_LIMITE"));
		tramite_nuevo.set("FECHA_FIN", tramite_viejo.getDate("FECHA_FIN"));

		tramite_nuevo.set("FECHA_INICIO_PLAZO",	tramite_viejo.getDate("FECHA_INICIO_PLAZO"));
		
		if( tramite_viejo.getInt("PLAZO") != Integer.MIN_VALUE)
			tramite_nuevo.set("PLAZO", tramite_viejo.getInt("PLAZO"));
		
		tramite_nuevo.set("UPLAZO", tramite_viejo.getString("UPLAZO"));
		tramite_nuevo.set("OBSERVACIONES",tramite_viejo.getString("OBSERVACIONES"));
		tramite_nuevo.set("DESCRIPCION", tramite_viejo.getString("DESCRIPCION"));
		tramite_nuevo.set("UND_RESP", tramite_viejo.getString("UND_RESP"));
		tramite_nuevo.set("FASE_PCD", tramite_viejo.getString("FASE_PCD"));
		
		if( tramite_viejo.getInt("ID_SUBPROCESO") != Integer.MIN_VALUE)
			tramite_nuevo.set("ID_SUBPROCESO", tramite_viejo.getInt("ID_SUBPROCESO"));
		
		tramite_nuevo.set("ID_RESP_CLOSED", tramite_viejo.getString("ID_RESP_CLOSED"));

		tramite_nuevo.store(cct);
	}
	
	public static IItem getTramite(IClientContext cct, String numexp, int idTramite)throws ISPACException{
		IItem resultado = null;
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		IItemCollection tramites = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, numexp, "ID_TRAM_EXP="+idTramite);	        
		
		if(tramites == null || tramites.toList().size() == 0){
			tramites = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES_H, numexp, "ID_TRAM_EXP="+idTramite);	        
		}
		Iterator <?> itTramites = tramites.iterator();
		if(itTramites.hasNext()){
			resultado = (IItem) itTramites.next();
		}

		return resultado;
	}
	
	public static IItemCollection getTramites(IClientContext cct, String numexp)throws ISPACException{
		IItemCollection resultado;
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		resultado = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, numexp, null);	        
		
		if(resultado == null || resultado.toList().size() == 0){
			resultado = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES_H, numexp, null);	        
		}

		return resultado;
	}
	
	public static IItemCollection getTramites(IClientContext cct, String numexp, String sqlQuery, String order)throws ISPACException{
		IItemCollection resultado;
		
		String consulta = "";
		
		if(StringUtils.isNotEmpty(numexp)) consulta += "WHERE NUMEXP = '" + numexp + "' ";

		if(StringUtils.isNotEmpty(sqlQuery)){
			if(StringUtils.isNotEmpty(numexp)) consulta += " AND ";
			else consulta += " WHERE ";
			consulta += "  ( " + sqlQuery + " ) ";
		}
		if(StringUtils.isNotEmpty(order)) consulta += " ORDER BY " + order;
		
		resultado = TramitesUtil.queryTramites(cct, consulta);

		return resultado;
	}
	
	public static IItemCollection queryTramites(IClientContext cct, String consulta) throws ISPACException{
		ListCollection resultado = null;
		ArrayList<IItem> part = new ArrayList<IItem>();
		
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			IItemCollection tramites = (ListCollection) entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, consulta);
			for(Object tramite : tramites.toList()){
				part.add((IItem)tramite);
			}
			
			IItemCollection tramites_h = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES_H, consulta);
			for(Object tramite : tramites_h.toList()){
				part.add((IItem)tramite);
			}
			
			resultado = new ListCollection(part);
		}
		catch(Exception e){
			LOGGER.error("Error al recuperar los trámites. consulta: " + consulta + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar los trámites. " + e.getMessage(), e);
		}
		
		return resultado;
	}
	
	/**
	 * Recuperar el campo "otros datos" de la pestaña de datos específicos del trámite
	 * @param rulectx
	 * @return
	 */
	public static String getDatosEspecificosOtrosDatos(IClientContext cct, int procedureTaskId){
		
		String otrosDatos = "";			
		try {
			IItemCollection datosEspecificosCollection = cct.getAPI().getEntitiesAPI().queryEntities
					("SPAC_P_TRAM_DATOSESPECIFICOS", "WHERE ID_TRAM_PCD = " + procedureTaskId);
			Iterator<?> plantillasDefectoIterator = datosEspecificosCollection.iterator();
			
			if (plantillasDefectoIterator.hasNext()) {
				otrosDatos = ((IItem) plantillasDefectoIterator
						.next()).getString("OTROS_DATOS");
			}
		} catch (Exception e) {
			LOGGER.error("ERROR al recuperar los datos específicos del trámite. "	+ e.getMessage(), e);
		}
		return otrosDatos;
	}
	
	/**
	 * Recupera una propiedad que se pasa como parámetro dentro de los datos específicos (campo otros datos) del trámite inidicado. Se supone que las propiedades de los datos específicos se separan por ##
	 * 
	 * @param cct
	 * @param procedureTaskId
	 * @param propiedad
	 * @return
	 */
	public static String getPropiedadDatosEspecificos(IClientContext cct, int procedureTaskId, String propiedad){
		
		String valorPropiedad = "";
		try {
			String otrosDatos = TramitesUtil.getDatosEspecificosOtrosDatos(cct, procedureTaskId);
			if(StringUtils.isNotEmpty(otrosDatos)){
				String [] propiedades = otrosDatos.split(TramitesUtil.SEPARADOR_PROPIEDADES_DATOS_ESPECIFICOS);
				for(int i = 0; i < propiedades.length; i++){
					String propiedadEspecifica = propiedades[i];
					if(StringUtils.isNotEmpty(propiedadEspecifica)){
						String [] datos = propiedadEspecifica.split("=");
						if(StringUtils.isNotEmpty(datos[0]) && propiedad.equals(datos[0])){
							valorPropiedad = datos[1];
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("ERROR al recuperar la propiedad " + propiedad + " de los datos específicos del trámite " + procedureTaskId + ".	" + e.getMessage(), e);
		}
		return valorPropiedad;
	}
	
	/**
	 * Va a hacer uso de una propiedad en los datos específicos llamada CORREOS_FIN_TRAMITE
	 * donde se almacenarán las direcciones a las que enviar un correo al terminar el trámite.
	 * Los datos específicos serán de la forma = ##CORREOS_FIN_TRAMITE=coreo1;correo2;etc##
	 * 
	 */
	
	public static String getCorreosFinTramiteDatosEspecificos(IClientContext cct, int procedureTaskId){
		
		String sCorreos = "";		
		try {
			sCorreos = TramitesUtil.getPropiedadDatosEspecificos(cct, procedureTaskId, TramitesUtil.CORREOS_FIN_TRAMITE);			
		} catch (Exception e) {
			LOGGER.error("ERROR al recuperar los datos específicos del trámite. "	+ e.getMessage(), e);
		}
		return sCorreos;
	}
	
	public static List<String> getCorreosFinTramiteLista(IClientContext cct, int procedureTaskId){
		
		List<String> lCorreos = new ArrayList<String>();		
		try {
			String sCorreos = TramitesUtil.getCorreosFinTramiteDatosEspecificos(cct, procedureTaskId);
			if(StringUtils.isNotEmpty(sCorreos)){
				String[] splitCorreos = sCorreos.split(";");
				for(int i = 0; i < splitCorreos.length; i++){
					lCorreos.add(splitCorreos[i].trim());
				}
			}
		} catch (Exception e) {
			LOGGER.error("ERROR al recuperar los datos específicos del trámite. "	+ e.getMessage(), e);
		}
		return lCorreos;
	}
	
	public static int cuentaTramites(IClientContext cct, String numexp, int id_tram_pcd){
		int numero_tramites = 0;
		IItemCollection tramitesCollection;
		try {
            tramitesCollection = TramitesUtil.getTramites(cct, numexp, TramitesUtil.ID_TRAM_PCD + " = '" + id_tram_pcd +"'", "");

			List<?> tramitesList = tramitesCollection.toList();
			if(null != tramitesList){
				numero_tramites = tramitesList.size();
			}
		} catch (ISPACException e) {
			LOGGER.error("ERROR al contar los trámites iguales del expediente: " + numexp + ", " + TramitesUtil.ID_TRAM_PCD  + ": " + id_tram_pcd + ". "	+ e.getMessage(), e);
		}
        return numero_tramites;
	}
	
	public static int cuentaTramitesByNombreTramite(IClientContext cct, String numexp, String nombreTramite){
		int numero_tramites = 0;
		IItemCollection tramitesCollection;
		try {
            tramitesCollection = TramitesUtil.getTramites(cct, numexp, TramitesUtil.NOMBRE + " = '" + nombreTramite + "'", "");
            List <?> tramitesList = tramitesCollection.toList();

			if(null != tramitesList){
				numero_tramites = tramitesList.size();
			}
		} catch (ISPACException e) {
			LOGGER.error("ERROR al contar los trámites iguales del expediente: " + numexp + ", " + TramitesUtil.NOMBRE + ": " + nombreTramite + ". "	+ e.getMessage(), e);
		}
        return numero_tramites;
	}
	public static String getNombreTramite(IClientContext cct, int taskId) {
		
		String nombreTramite = "";

		try {
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IItem processTask = entitiesAPI.getTask(taskId);
			nombreTramite = processTask.getString(TramitesUtil.NOMBRE);
			
		} catch (ISPACException e) {
			LOGGER.error("ERROR al recuperar el nombre del trámite con id: " + taskId + ". " + e.getMessage(), e);
			nombreTramite = "";
		}
        
		return nombreTramite;
	}
}

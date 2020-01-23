package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.item.util.ListCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [ecenpri-Felipe]
 * Clase con funciones comunes para tratar con expedientes
 * @since 30.06.2011
 * @author Felipe
 */
public class ExpedientesUtil {
	
	private static final Logger LOGGER = Logger.getLogger(ExpedientesUtil.class);
	
	public static final String SPAC_EXPEDIENTES = "SPAC_EXPEDIENTES";
	public static final String SPAC_EXPEDIENTES_H = "SPAC_EXPEDIENTES_H";
	
	//Mapeo de las columnas de la tablas SPAC_EXPEDINTES y SPAC_EXPEDIENTES_H
	public static final String ID = "ID";
	public static final String NUMEXP = "NUMEXP";
	public static final String ASUNTO = "ASUNTO";
	public static final String ESTADOADM = "ESTADOADM";
	public static final String IDENTIDADTITULAR = "IDENTIDADTITULAR";
	public static final String NIFCIFTITULAR = "NIFCIFTITULAR";
	public static final String NOMBREPROCEDIMIENTO = "NOMBREPROCEDIMIENTO";
	public static final String CIUDAD = "CIUDAD";
	public static final String CODPROCEDIMIENTO = "CODPROCEDIMIENTO";
	public static final String FCIERRE = "FCIERRE";
	public static final String OBSERVACIONES = "OBSERVACIONES";
	public static final String CPOSTAL = "CPOSTAL";
	public static final String REGIONPAIS = "REGIONPAIS";
	public static final String DOMICILIO = "DOMICILIO";
	public static final String TIPODIRECCIONINTERESADO = "TIPODIRECCIONINTERESADO";
	public static final String TFNOFIJO = "TFNOFIJO";
	public static final String ROLTITULAR = "ROLTITULAR";
	public static final String IDDIRECCIONPOSTAL = "IDDIRECCIONPOSTAL";
	public static final String DIRECCIONTELEMATICA = "DIRECCIONTELEMATICA";
	public static final String TFNOMOVIL = "TFNOMOVIL";
	public static final String TIPOPERSONA = "TIPOPERSONA";	
	
	public static final String NREG = "NREG";
	public static final String FREG = "FREG";
	public static final String ID_PCD = "ID_PCD";
	
	//Expediente iniciado de forma manual
	public static final String _TIPO_MANUAL = "MANUAL";
	//Expediente iniciado de forma automática (desde RT)
	public static final String _TIPO_AUTO = "AUTO";

	
	//Valores del campo ESTADOADM
	public interface EstadoADM{
        String RS = "RS";
        String AP = "AP";
        String AP25 = "AP25";
        String RC = "RC";
		String RN = "RN";
		String NR = "NR";
		String RNN = "RNN";
		String NT = "NT";
		String NE = "NE";
		String JF = "JF";
		String JS = "JS";
		String JI = "JI";
		String CR = "CR";
		String NT25 = "NT25";
		String DI = "DI";
		String DF = "DF";		
    }
	
	
	public static void cerrarExpedientes (IClientContext cct, Vector<String> numexpCerrar) throws ISPACRuleException{
		
		for (int i = 0; i < numexpCerrar.size(); i++) {
				try {
					ExpedientesUtil.avanzarFase(cct, numexpCerrar.get(i));			
				} catch (ISPACRuleException e) {
					LOGGER.error(e.getMessage(), e);
					throw new ISPACRuleException("No se puede cerrar el expediente " +numexpCerrar.get(i)+". Error: "+ e.getMessage(), e);
				}
		}	        					
	}
	
	
	/**
	 * Método que cierra el expediente y sus trámites abiertos
	 * @param cct
	 * @param numexp
	 * @throws ISPACRuleException
	 */
	@SuppressWarnings("rawtypes")
	public static void cerrarExpediente(IClientContext cct, String numexp) throws ISPACRuleException{
	
		try{
			//APIs
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			
			//Buscamos el expediente
			IProcess itemProcess = invesflowAPI.getProcess(numexp);
        	int idProcess = itemProcess.getInt("ID");
			
//	        cct.beginTX();
	        	
        	//Cerramos el trámite
	        String strQuery = "WHERE NUMEXP='" + numexp + "'";
	        IItemCollection collectionTrams = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery);
	        Iterator itTrams = collectionTrams.iterator();
        	IItem tram = null;
	        while (itTrams.hasNext()) 
	        {
	        	tram = ((IItem)itTrams.next());
	        	int idTram = tram.getInt("ID");
	        	tx.closeTask(idTram);
	        }
        	
	        //Avanzamos fase
        	IItemCollection collectionStages = cct.getAPI().getStagesProcess(idProcess);
        	Iterator itStages = collectionStages.iterator();
        	if (itStages.hasNext()) 
	        {
        		IStage stage = (IStage) itStages.next();
        		int idPcdStage = stage.getInt("ID_FASE");
        		int idStage = stage.getInt("ID");
        		tx.deployNextStage(idProcess, idPcdStage, idStage);
	        }
        	
//        	cct.endTX(true);
	        		
		}
		catch (Exception e) {
			LOGGER.error("Error al cerrar el expediente de Licencias, expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al cerrar el expediente de Licencias, expediente: " + numexp + ". " + e.getMessage(), e);
		}
	}
	
	/**
	 * Método que avanza fase del expediente
	 * Si es la última fase, el expediente se cierra
	 * @param cct
	 * @param numexp
	 * @throws ISPACRuleException
	 */
	public static IStage avanzarFase(IClientContext cct, String numexp) throws ISPACRuleException{
		IStage stage = null;
		try{
			//APIs
			IInvesflowAPI invesflowAPI = cct.getAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			
			//Buscamos el expediente
			IProcess itemProcess = invesflowAPI.getProcess(numexp);
        	int idProcess = itemProcess.getInt("ID");
			
        	boolean ongoingTX = cct.ongoingTX();//[dipucr-Felipe Manuel #884]
        	
        	if (!ongoingTX){
        		cct.beginTX();
        	}
        	
	        //Avanzamos fase
        	IItemCollection collectionStages = cct.getAPI().getStagesProcess(idProcess);
			Iterator<?> itStages = collectionStages.iterator();
        	if (itStages.hasNext()) 
	        {
        		stage = (IStage) itStages.next();
        		int idPcdStage = stage.getInt("ID_FASE");
        		int idStage = stage.getInt("ID");
        		tx.deployNextStage(idProcess, idPcdStage, idStage);
	        }
        	
        	if (!ongoingTX){
        		cct.endTX(true);
        	}
	        		
		}
		catch (Exception e) {
			LOGGER.error("Error al cerrar el expediente de Licencias, expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al cerrar el expediente de Licencias, expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return stage;
	}

	/**
	 * [eCenpri-Felipe #787]
	 * Devuelve true si el procedimiento ha sido iniciado por un grupo
	 * Normalmente coincide con procedimientos automáticos iniciados desde RT,
	 * pues si no habría sido iniciado por un usuario
	 * @param rulectx
	 * @return
	 */
	public static boolean esIniciadoPorGrupo(IRuleContext rulectx){
		
		//[eCenpri-Manu] Para evitar dependencias cíclicas se quita SicresIntray.GROUP y se pone directamente un "3", 
		//ya que únicamente por esta constante hay que incluir una dependencia a un proyecto y encima produce un ciclo.
		//Es posible que se pueda recuperar dicho valor de un servicio (LocalizadorServicios) pero no lo encuentro.
		String codGrupo = String.valueOf("3");
		String idResp = rulectx.getClientContext().getRespId();
		
		if (null != idResp && idResp.startsWith(codGrupo)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * [eCenpri-Felipe #787]
	 * Actualiza el estado de un expediente
	 * @param rulectx
	 * @return
	 * @throws ISPACException 
	 */
	public static void updateEstadoExpediente(IRuleContext rulectx, String estado) throws ISPACException{
		
		//*********************************************
		IClientContext cct = rulectx.getClientContext();
		//*********************************************
		
		//Recuperamos los datos del solicitante
		String numexp = rulectx.getNumExp();
		IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
		itemExpediente.set("ESTADOINFO", estado);
		itemExpediente.set("FESTADO", new Date());
		itemExpediente.store(cct);
	}
	
	/**
	 * [eCenpri-Agustin #958]
	 * Actualiza el estado de un expediente
	 * @param rulectx
	 * @return
	 * @throws ParseException 
	 * @throws ISPACException 
	 */	
	public static String dameFechaInicioExp(IClientContext cct, String numexp) throws ParseException{
	    	
			String fechaExp = null;
			
			Date d = null;		    
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    SimpleDateFormat sdfr = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);		    
		
			try {
    		   	IItem exp = getExpediente(cct, numexp);
    				
    		    if (exp != null){
    		    	fechaExp = exp.getString("FAPERTURA");    	
    		    }    		    	
    	    	
    	    } catch (ISPACException e) {
    			LOGGER.error("Error al recuperar la fecha de inicio del expediente: " + numexp + ". " + e.getMessage(), e);
    		}
			
			if(fechaExp != null){
				d = sdf.parse(fechaExp); 
				fechaExp = sdfr.format(d);
			}		
			
			LOGGER.info("La fecha de inicio del expediente: " + numexp + " es " + fechaExp);
			
			return fechaExp;
			
	}

	public static void retrocederFase(IClientContext cct, String numexp) throws ISPACException {
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			ITXTransaction itxTransaction = invesflowAPI.getTransactionAPI();
			
			//Buscamos el expediente
			IProcess itemProcess = invesflowAPI.getProcess(numexp);
        	int idProcess = itemProcess.getInt("ID");
			
	        cct.beginTX();
        	
        	IItemCollection collectionStages = cct.getAPI().getStagesProcess(idProcess);
			Iterator<?> itStages = collectionStages.iterator();
        	if (itStages.hasNext()) 
	        {
        		IStage stage = (IStage) itStages.next();
        		int idPcdStage = stage.getInt("ID_FASE");
        		int idStage = stage.getInt("ID");
    			itxTransaction.redeployPreviousStage(idProcess, idPcdStage, idStage);
	        }
        	
        	cct.endTX(true);
		}
		catch(ISPACException e){
			LOGGER.error("Error al retroceder el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACException("Error al retroceder el expediente: " + numexp + ". " + e.getMessage(), e);
		}
	}
	
	public static void copiaExpediente(IClientContext cct, String tabla, IItem expediente_viejo, String numexp) throws ISPACException{

		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();

		IItem expediente_nuevo = entitiesAPI.createEntity(tabla, numexp);
	
		expediente_nuevo.set("ID", expediente_viejo.getInt("ID"));
		expediente_nuevo.set("ID_PCD", expediente_viejo.getInt("ID_PCD"));
		expediente_nuevo.set("NUMEXP", expediente_viejo.getString("NUMEXP"));
		expediente_nuevo.set("REFERENCIA_INTERNA", expediente_viejo.getString("REFERENCIA_INTERNA"));
		expediente_nuevo.set("NREG", expediente_viejo.getString("NREG"));
		expediente_nuevo.set("FREG", expediente_viejo.getDate("FREG"));
		expediente_nuevo.set("ESTADOINFO", expediente_viejo.getString("ESTADOINFO"));
		expediente_nuevo.set("FESTADO", expediente_viejo.getDate("FESTADO"));
		expediente_nuevo.set("NIFCIFTITULAR", expediente_viejo.getString("NIFCIFTITULAR"));
		expediente_nuevo.set("IDTITULAR", expediente_viejo.getString("IDTITULAR"));
		expediente_nuevo.set("DOMICILIO", expediente_viejo.getString("DOMICILIO"));
		expediente_nuevo.set("CIUDAD", expediente_viejo.getString("CIUDAD"));
		expediente_nuevo.set("REGIONPAIS", expediente_viejo.getString("REGIONPAIS"));
		expediente_nuevo.set("CPOSTAL", expediente_viejo.getString("CPOSTAL"));
		expediente_nuevo.set("IDENTIDADTITULAR", expediente_viejo.getString("IDENTIDADTITULAR"));
		expediente_nuevo.set("TIPOPERSONA", expediente_viejo.getString("TIPOPERSONA"));
		expediente_nuevo.set("ROLTITULAR", expediente_viejo.getString("ROLTITULAR"));
		expediente_nuevo.set("ASUNTO", expediente_viejo.getString("ASUNTO"));
		expediente_nuevo.set("FINICIOPLAZO", expediente_viejo.getDate("FINICIOPLAZO"));
		expediente_nuevo.set("POBLACION", expediente_viejo.getString("POBLACION"));
		expediente_nuevo.set("MUNICIPIO", expediente_viejo.getString("MUNICIPIO"));
		expediente_nuevo.set("LOCALIZACION", expediente_viejo.getString("LOCALIZACION"));
		expediente_nuevo.set("EXPRELACIONADOS", expediente_viejo.getString("EXPRELACIONADOS"));
		expediente_nuevo.set("CODPROCEDIMIENTO", expediente_viejo.getString("CODPROCEDIMIENTO"));
		expediente_nuevo.set("NOMBREPROCEDIMIENTO", expediente_viejo.getString("NOMBREPROCEDIMIENTO"));
		expediente_nuevo.set("PLAZO", expediente_viejo.getInt("PLAZO"));
		expediente_nuevo.set("UPLAZO", expediente_viejo.getString("UPLAZO"));
		expediente_nuevo.set("FORMATERMINACION", expediente_viejo.getString("FORMATERMINACION"));
		expediente_nuevo.set("UTRAMITADORA", expediente_viejo.getString("UTRAMITADORA"));
		expediente_nuevo.set("FUNCIONACTIVIDAD", expediente_viejo.getString("FUNCIONACTIVIDAD"));
		expediente_nuevo.set("MATERIAS", expediente_viejo.getString("MATERIAS"));
		expediente_nuevo.set("SERVPRESACTUACIONES", expediente_viejo.getString("SERVPRESACTUACIONES"));
		expediente_nuevo.set("TIPODEDOCUMENTAL", expediente_viejo.getString("TIPODEDOCUMENTAL"));
		expediente_nuevo.set("PALABRASCLAVE", expediente_viejo.getString("PALABRASCLAVE"));
		expediente_nuevo.set("FCIERRE", expediente_viejo.getDate("FCIERRE"));
		expediente_nuevo.set("ESTADOADM", expediente_viejo.getString("ESTADOADM"));
		expediente_nuevo.set("HAYRECURSO", expediente_viejo.getString("HAYRECURSO"));
		expediente_nuevo.set("EFECTOSDELSILENCIO", expediente_viejo.getString("EFECTOSDELSILENCIO"));
		expediente_nuevo.set("FAPERTURA", expediente_viejo.getDate("FAPERTURA"));
		expediente_nuevo.set("OBSERVACIONES", expediente_viejo.getString("OBSERVACIONES"));
		expediente_nuevo.set("IDUNIDADTRAMITADORA", expediente_viejo.getString("IDUNIDADTRAMITADORA"));
		expediente_nuevo.set("IDPROCESO", expediente_viejo.getInt("IDPROCESO"));
		expediente_nuevo.set("TIPODIRECCIONINTERESADO", expediente_viejo.getString("TIPODIRECCIONINTERESADO"));
		expediente_nuevo.set("NVERSION", expediente_viejo.getString("NVERSION"));
		expediente_nuevo.set("IDSECCIONINICIADORA", expediente_viejo.getString("IDSECCIONINICIADORA"));
		expediente_nuevo.set("SECCIONINICIADORA", expediente_viejo.getString("SECCIONINICIADORA"));
		expediente_nuevo.set("TFNOFIJO", expediente_viejo.getString("TFNOFIJO"));
		expediente_nuevo.set("TFNOMOVIL", expediente_viejo.getString("TFNOMOVIL"));
		expediente_nuevo.set("DIRECCIONTELEMATICA", expediente_viejo.getString("DIRECCIONTELEMATICA"));
		expediente_nuevo.set("NUMEXP", expediente_viejo.getString("NUMEXP"));
		expediente_nuevo.set("VERSION", expediente_viejo.getString("VERSION"));
		expediente_nuevo.set("FECHA_APROBACION", expediente_viejo.getDate("FECHA_APROBACION"));
		expediente_nuevo.set("TIPOEXP", expediente_viejo.getString("TIPOEXP")); //[dipucr-Felipe #908]
	
		expediente_nuevo.store(cct);
	}
	
	public static IItem getExpediente(IClientContext cct, String numexp) throws ISPACException{
		IItem resultado;
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		try{
			resultado = entitiesAPI.getExpedient(numexp);
			
		} catch(Exception e){
			LOGGER.debug("No se ha recuperado ningún expediente de SPAC_EXPEDIENTES con numexp: " + numexp);
			resultado = null;
		}
		
		if(resultado == null){
			if(EntidadesAdmUtil.tieneEntidadTablaHistoricos()){
				IItemCollection itemset = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES_H, numexp, null);
		        if (itemset.next()){
		        	resultado = itemset.value();
		        }
			}
		}

		return resultado;
	}
	
	public static IItemCollection getExpediente(IClientContext cct, String numexp, String sqlQuery, String order)throws ISPACException{
		IItemCollection resultado;

		String consulta = "";
		
		if(StringUtils.isNotEmpty(numexp)) consulta += "WHERE NUMEXP = '" + numexp + "' ";

		if(StringUtils.isNotEmpty(sqlQuery)){
			if(StringUtils.isNotEmpty(numexp)) consulta += " AND ";
			else consulta += " WHERE ";
			consulta += "  ( " + sqlQuery + " ) ";
		}
		if(StringUtils.isNotEmpty(order)) consulta += " ORDER BY " + order;
		
		resultado = ExpedientesUtil.queryExpedientes(cct, consulta);

		return resultado;
	}
	
	public static IItemCollection queryExpedientes(IClientContext cct, String consulta) throws ISPACException{
		ListCollection resultado = null;
		ArrayList<IItem> part = new ArrayList<IItem>();
		
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			IItemCollection expedientes = (ListCollection) entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, consulta);
			for(Object expediente : expedientes.toList()){
				part.add((IItem)expediente);
			}
			if(EntidadesAdmUtil.tieneEntidadTablaHistoricos()){
				IItemCollection expedientes_h = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES_H, consulta);
				for(Object expediente : expedientes_h.toList()){
					part.add((IItem)expediente);
				}
			}
			
			resultado = new ListCollection(part);
		}
		catch(Exception e){
			LOGGER.error("Error al recuperar los participantes. consulta: " + consulta + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar los participantes. " + e.getMessage(), e);
		}
		
		return resultado;
	}

	public static boolean estaEnHistorico(IClientContext cct, String numexp) throws ISPACException{
		if(EntidadesAdmUtil.tieneEntidadTablaHistoricos()){
			IItemCollection itemset = cct.getAPI().getEntitiesAPI().getEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES_H, numexp, null);
			return itemset.next();
		}
		else{
			return false;
		}
        
	}
	
	public static boolean esMayor(String numexp1, String numexp2){
		boolean resultado = false;
		
		int numexp1anio, numexp2anio, numexp1num, numexp2num;
		
		try{
			String numexp1Ini = numexp1.substring(numexp1.indexOf("/") - 4 , numexp1.indexOf("/"));
			numexp1anio = Integer.parseInt(numexp1Ini);
		}
		catch(Exception e){
			numexp1anio = 0;
		}
		try{
			String numexp1Fin = numexp1.substring(numexp1.indexOf("/")+1);
			numexp1num = Integer.parseInt(numexp1Fin);
		}
		catch(Exception e){
			numexp1num = 0;
		}
		
		try{
			String numexp2Ini = numexp2.substring(numexp2.indexOf("/") - 4, numexp2.indexOf("/"));
			numexp2anio = Integer.parseInt(numexp2Ini);
		}
		catch(Exception e){
			numexp2anio = 0;
		}
		try{
			String numexp2Fin = numexp2.substring(numexp2.indexOf("/")+1);
			numexp2num = Integer.parseInt(numexp2Fin);
		}
		catch(Exception e){
			numexp2num = 0;
		}
				
		if(numexp1anio > numexp2anio) resultado = true;
		else if (numexp1anio == numexp2anio){
			if(numexp1num > numexp2num) resultado = true;			
		}
		
		return resultado;
	}

	public static String getAsunto(IClientContext cct, String numexp) throws ISPACException {
		try {
			return ExpedientesUtil.getExpediente(cct, numexp).getString(ExpedientesUtil.ASUNTO);
		} catch (ISPACException e) {
			LOGGER.error("Error al recuperar el asunto del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el asunto del expediente: " + numexp + ". " + e.getMessage(), e);
		}
	}


	public static String getEstadoAdm(IClientContext cct, String numexp) {
		String estadoAdm = "";
		
		try{
			IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
			estadoAdm = expediente.getString(ExpedientesUtil.ESTADOADM);
			
		} catch (ISPACException e) {
			LOGGER.error("Error al recuperar el estado administrativo del expediente: " + numexp + ". " + e.getMessage(), e);
			estadoAdm = "";
		}
		
        return estadoAdm;
	}


	public static void setTerceroAsInteresadoPrincipal(IClientContext cct, String numExp, IThirdPartyAdapter tercero) {
		try{
			IItem expediente = ExpedientesUtil.getExpediente(cct, numExp);
	        
	        expediente.set(ExpedientesUtil.NIFCIFTITULAR, tercero.getIdentificacion());
	        expediente.set(ExpedientesUtil.IDENTIDADTITULAR, tercero.getNombreCompleto());
	
	        if (null != tercero.getDefaultDireccionPostal()){
	            expediente.set(ExpedientesUtil.CPOSTAL, tercero.getDefaultDireccionPostal().getCodigoPostal());
	            expediente.set(ExpedientesUtil.DOMICILIO,tercero.getDefaultDireccionPostal().getDireccionPostal());
	            expediente.set(ExpedientesUtil.CIUDAD, tercero.getDefaultDireccionPostal().getMunicipio());
	            expediente.set(ExpedientesUtil.REGIONPAIS,tercero.getDefaultDireccionPostal().getProvincia());
	            expediente.set(ExpedientesUtil.TFNOFIJO,tercero.getDefaultDireccionPostal().getTelefono());
	        }
	        
	        expediente.set(ExpedientesUtil.ROLTITULAR, "INT");
	        
	        expediente.store(cct);
	        
		} catch(ISPACException e){
			LOGGER.error("ERROR al rellenar el interesado principal del expediente: " + numExp + ". " + e.getMessage(), e);
		}
	}
}


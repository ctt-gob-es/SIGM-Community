package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;

/**
 * [eCenpri-Felipe #871]
 * Biblioteca de funciones para el control de bloqueos en la
 * firma simultánea de ciertos tipos de documentos (ej. Decretos)
 * @author Felipe
 * @since 19.04.13
 */
public class GestorDecretos {
	
	protected static final Logger logger = Logger.getLogger("loggerFirma");


	public static String SEQUENCE_NUM_DECRETOS_AUX = "SPAC_SQ_NUMDECRETO";
	public static String SEQUENCE_NUM_DECRETOS_REAL = "SPAC_SQ_NUMDECRETO_FIRMADO";
	public static String CATALOG_SPAC_TRAMITES_COD_TRAMITE_DECRETOS = "PREP_FIRMAS_DEC";
	
	//Separador para los números de decreto
	public static String NUM_DECRETO_SEPARATOR = "/";
	
	//Bloqueos de firma
	//Tipo de bloqueo para documentos de decretos
	public static final String TIPO_DOC_DECRETO = "DEC";
	
	//Estado de los bloqueos
	public static final int ESTADO_BLOQUEO_ACTIVO = 1;
	public static final int ESTADO_BLOQUEO_INACTIVO = 0;
	
	//Tipo de firma de decretos
	public static final int TIPO_FIRMA_NORMAL = 0;
	public static final int TIPO_FIRMA_PRESIDENTE = 1;
	public static final int TIPO_FIRMA_FEDATARIO = 2;
	
	public static final String CLAVE_FIRMA_FEDATARIO = "F";
	
	/**
	 * 
	 * @param cct
	 * @param idDoc
	 * @return
	 * @throws ISPACException
	 */
	public static boolean esFirmaPresidente(IClientContext cct, int idDoc) throws ISPACException{
		
		int tipoFirma = comprobarFirmaDecretos(cct, idDoc);
		return (tipoFirma == TIPO_FIRMA_PRESIDENTE);
	}
	
	/**
	 * Comprueba la firma de los documentos de decretos
	 * @param cct
	 * @param idDoc
	 * @return
	 * @throws ISPACException 
	 */
	public static int comprobarFirmaDecretos(IClientContext cct, int idDoc) throws ISPACException{
		
		int tipoFirma = 0;
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		//AJM OTRO CONTROL, ¿¿ESTOY EN EL SEGUNDO TRAMITE?? SI CUANDO EL ID_TRAM_CTL = 31
		IItem itemDoc = entitiesAPI.getDocument(idDoc);
		int taskProcessId = (Integer) itemDoc.get("ID_TRAMITE");
		IItem itemTram = entitiesAPI.getTask(taskProcessId);	
		int idTaskTramCtl = (Integer) itemTram.get("ID_TRAM_CTL");
		String codTram = TramitesUtil.getCodTram(idTaskTramCtl, cct);

		// Estado adm. "Esperando firmas"
		if (CATALOG_SPAC_TRAMITES_COD_TRAMITE_DECRETOS.equals(codTram)) {

			String sqlQuery = "WHERE ID_DOCUMENTO = " + idDoc + " ORDER BY ID_PASO ASC";
			IItemCollection collection = entitiesAPI.queryEntities(
					"SPAC_CTOS_FIRMA", sqlQuery);

			IItem itemPaso = (IItem) collection.iterator().next();
			if (null != itemPaso && itemPaso.getInt("ESTADO") != 2) {
				tipoFirma = TIPO_FIRMA_PRESIDENTE;
			}
			else{
				tipoFirma = TIPO_FIRMA_FEDATARIO;
			}
		}
		return tipoFirma;
	}
	
	
	/**
	 * Devuelve el número de decreto
	 * @param cct
	 * @param bActual
	 *  true  -> Devuelve el nº de decreto actual
	 *  false -> (uso habitual) Devuelve el siguiente nº de decreto 
	 * @return
	 * @throws ISPACException 
	 */
	public static String getNumDecreto(IClientContext cct, boolean bActual) throws ISPACException{
		
		int numDecreto = 0;
		GregorianCalendar gc = new GregorianCalendar();
		int anio = gc.get(Calendar.YEAR);

		if (bActual){
			numDecreto = IdSequenceMgr.currVal(cct.getConnection(), SEQUENCE_NUM_DECRETOS_AUX);
		}
		else{
			numDecreto = IdSequenceMgr.nextVal(cct.getConnection(), SEQUENCE_NUM_DECRETOS_AUX);
		}
		
		String sNumDecreto = anio + NUM_DECRETO_SEPARATOR + numDecreto;
		return sNumDecreto;
	}
	
	/**
	 * Devuelve el último número de decreto en la BBDD
	 * Si no hay decretos en el año actual, devuelve un 0
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static int getUltimoNumDecreto(IClientContext cct) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		int numDecreto = 0;
		IItem itemUltimoDecreto = null;
		
		GregorianCalendar gc = new GregorianCalendar();
		int anio = gc.get(Calendar.YEAR);
		
		StringBuffer sbQueryDecreto = new StringBuffer();
		sbQueryDecreto.append("WHERE ANIO = ");
		sbQueryDecreto.append(anio);
		sbQueryDecreto.append(" ORDER BY NUMERO_DECRETO DESC");
		IItemCollection colDecretosInAnio = entitiesAPI.queryEntities
				("SGD_DECRETO", sbQueryDecreto.toString());
		
		if (null == colDecretosInAnio || colDecretosInAnio.toList().size() == 0){
			numDecreto = 0;
		}
		else{
			itemUltimoDecreto = (IItem) colDecretosInAnio.iterator().next();
			numDecreto = itemUltimoDecreto.getInt("NUMERO_DECRETO");
		}
		return numDecreto;
	}
	
	/**
	 * A partir de un número de decreto devuelve sólo el número
	 * @param sNumDecreto
	 * @return
	 */
	public static String getOnlyNumero(String sNumDecreto){
		return sNumDecreto.split(NUM_DECRETO_SEPARATOR)[1];
	}
	
	/**
	 * A partir de un número de decreto devuelve sólo el año
	 * @param sNumDecreto
	 * @return
	 */
	public static String getOnlyAnio(String sNumDecreto){
		return sNumDecreto.split(NUM_DECRETO_SEPARATOR)[0];
	}
	
	/**
	 * Devuelve el bloqueo actual para el tipo decretos
	 * @param cct
	 * @return
	 * @throws ISPACException
	 */
	protected static IItem getBloqueo(IClientContext cct, int estado) throws ISPACException{
		
		IItem itemBloqueoFirmas = null;
		
		ISignAPI signAPI = cct.getAPI().getSignAPI();
		IItemCollection collection = signAPI.getBloqueoFirmaDocs(TIPO_DOC_DECRETO, ESTADO_BLOQUEO_ACTIVO);
		
		if (collection.next()){
			itemBloqueoFirmas = (IItem) collection.iterator().next();
		}
		return itemBloqueoFirmas;
	}
	
	
	/**
	 * Devuelve true si el usuario puede firmar decretos
	 * @param cct
	 * @return
	 * @throws ISPACException 
	 */
	public static boolean puedeFirmar(IClientContext cct) throws ISPACException{
		
		try{
			String usuarioActual = cct.getResponsible().getName();
			
			//Si no hay bloqueo, lo insertamos y devolvemos true
			IItem itemBloqueoFirmas = getBloqueo(cct, ESTADO_BLOQUEO_ACTIVO);
			if (null == itemBloqueoFirmas){
				return true;
			}
			else{//Hay bloqueo
				String usuarioBloqueo = itemBloqueoFirmas.getString("USUARIO");
				
				//Si lo tiene bloqueado el mismo, actualizamos el tiempo y firmamos
				if (usuarioActual.equals(usuarioBloqueo)){
					return true;
				}
				else{
					//Si está bloqueado por otro usuario debemos mirar la última hora de bloqueo
					Date dLastUpdate = itemBloqueoFirmas.getDate("LAST_UPDATE");
					int maxSeconds = Integer.valueOf(FirmaConfiguration.
							getInstance(cct).get("bloqueofirma.decretos.maxseconds"));//[dipucr-Felipe 3#99]
					Date dMaxDate = FechasUtil.addSegundos(dLastUpdate, maxSeconds);
					Date dFechaActual = new Date();
					
					//Si se ha sobrepasado la hora límite, el nuevo firmante coge el bloqueo y puede firmar
					if (dFechaActual.after(dMaxDate)){
						return true;
					}
					else{
						return false;
					}
					
				}
			}
		}
		catch(Exception ex){
			throw new ISPACException("Error al comprobar si el usuario puede realizar la firma" +
					"de los decretos " + cct.getResponsible().getName(), ex);
		}
	}
	
	/**
	 * Bloquea la firma de decretos para el usuario actual
	 * @param cct
	 * @throws ISPACException
	 */
	public static void bloquearFirmaDecretos(IClientContext cct) throws ISPACException{

		ISignAPI signAPI = cct.getAPI().getSignAPI();
		String usuario = cct.getResponsible().getName();
		
		signAPI.updateBloqueoFirmaDocs(TIPO_DOC_DECRETO, usuario, new Date(), ESTADO_BLOQUEO_ACTIVO);
	}
	
	/**
	 * Liberar el bloqueo la firma de decretos para el usuario actual
	 * @param cct
	 * @throws ISPACException
	 */
	public static void liberarBloqueoFirmaDecretos(IClientContext cct) throws ISPACException{

		ISignAPI signAPI = cct.getAPI().getSignAPI();
		String usuario = cct.getResponsible().getName();
		
		signAPI.updateBloqueoFirmaDocs(TIPO_DOC_DECRETO, usuario, new Date(), ESTADO_BLOQUEO_INACTIVO);
	}
	
	
	/**
	 * Actualiza los valores del decreto dependiendo de si se trata del presidente o del fedatario
	 * @param cct
	 * @param signDocument
	 * @param numDecreto
	 * @throws ISPACException 
	 */
	public static void actualizarValoresDecreto(IClientContext cct, SignDocument signDocument, String numDecreto) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		String numExp = signDocument.getNumExp();
		
		IItemCollection colDecretos = entitiesAPI.getEntities("SGD_DECRETO", numExp);
		
		// Si aún no se ha creado la entidad Decreto, aunque esto nunca debe de suceder
		IItem itemDecreto = null;
		
		if (null == colDecretos || colDecretos.toList().size() == 0){
			itemDecreto = entitiesAPI.createEntity("SGD_DECRETO", numExp);
		}
		else if (null != colDecretos && colDecretos.toList().size() == 1){
			// Sólo hay un registro para la entidad Decreto
			itemDecreto = (IItem)colDecretos.iterator().next();
		}else {
	        throw new ISPACRuleException("Error al seleccionar el registro Decreto");
		}
		
		GregorianCalendar calendar = new GregorianCalendar();
		Date fecha = calendar.getTime();
		
		String nombreFirmante = cct.getResponsible().getName();
		
		if (numDecreto.equals(CLAVE_FIRMA_FEDATARIO)){
			itemDecreto.set("NOMBRE_FEDATARIO", nombreFirmante);
			itemDecreto.set("FECHA_FEDATARIO", fecha);
		}
		else{
			GregorianCalendar gc = new GregorianCalendar();
			itemDecreto.set("ANIO", getOnlyAnio(numDecreto));
			Date fechaDecreto = gc.getTime();
			itemDecreto.set("FECHA_DECRETO", fechaDecreto);
			itemDecreto.set("NUMERO_DECRETO", getOnlyNumero(numDecreto));
			
			itemDecreto.set("NOMBRE_PRESIDENTE", nombreFirmante);
			itemDecreto.set("FECHA_PRESIDENTE", fecha);
		}
		itemDecreto.store(cct);
	}
	
	/**
	 * Actualiza los valores del decreto dependiendo de si se trata del presidente o del fedatario
	 * @param cct
	 * @param signDocument
	 * @param numDecreto
	 * @throws ISPACException 
	 */
	public static void deshacerValoresDecreto(IClientContext cct, String numExp, String numDecreto) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		IItemCollection colDecretos = entitiesAPI.getEntities("SGD_DECRETO", numExp);
		IItem itemDecreto = (IItem)colDecretos.iterator().next();
		
		Object objNull = null;
		if (numDecreto.equals(CLAVE_FIRMA_FEDATARIO)){
			itemDecreto.set("NOMBRE_FEDATARIO", objNull);
			itemDecreto.set("FECHA_FEDATARIO", objNull);
		}
		else{
			itemDecreto.set("ANIO", objNull);
			itemDecreto.set("FECHA_DECRETO", objNull);
			itemDecreto.set("NUMERO_DECRETO", objNull);
			
			itemDecreto.set("NOMBRE_PRESIDENTE", objNull);
			itemDecreto.set("FECHA_PRESIDENTE", objNull);
		}
		itemDecreto.store(cct);
	}
	
	
	/**
	 * Actualiza la secuencia auxiliar al valor de la real
	 * @param cct
	 * @throws ISPACException
	 */
	public static void actualizarSecuenciaAuxiliar(IClientContext cct) throws ISPACException{
		
		DbCnt conn = cct.getConnection();
		try{
			int numDecretoReal = IdSequenceMgr.currVal(conn, GestorDecretos.SEQUENCE_NUM_DECRETOS_REAL);
			IdSequenceMgr.setVal(conn, GestorDecretos.SEQUENCE_NUM_DECRETOS_AUX, numDecretoReal);
		} finally {
			cct.releaseConnection(conn);
		}
	}
	
	/**
	 * Actualiza la secuencia auxiliar al valor pasado como parámetro
	 * @param cct
	 * @param valor
	 * @throws ISPACException
	 */
	public static void actualizarSecuenciaAuxiliar(IClientContext cct, int valor) throws ISPACException{
		
		DbCnt conn = cct.getConnection();
		try{
			IdSequenceMgr.setVal(conn, GestorDecretos.SEQUENCE_NUM_DECRETOS_AUX, valor);
		} finally {
			cct.releaseConnection(conn);
		}
	}
	
	/**
	 * Actualiza la secuencia real al valor de la auxiliar
	 * @param cct
	 * @throws ISPACException
	 */
	public static void actualizarSecuenciaReal(IClientContext cct) throws ISPACException{
		
		DbCnt conn = cct.getConnection();
		try{
			int numDecretoReal = IdSequenceMgr.currVal(conn, GestorDecretos.SEQUENCE_NUM_DECRETOS_AUX);
			IdSequenceMgr.setVal(conn, GestorDecretos.SEQUENCE_NUM_DECRETOS_REAL, numDecretoReal);
		} finally {
			cct.releaseConnection(conn);
		}
	}
	
	/**
	 * Actualiza la secuencia real al valor de la auxiliar
	 * @param cct
	 * @param valor
	 * @throws ISPACException
	 */
	public static void actualizarSecuenciaReal(IClientContext cct, int valor) throws ISPACException{
		
		DbCnt conn = cct.getConnection();
		try{
			IdSequenceMgr.setVal(conn, GestorDecretos.SEQUENCE_NUM_DECRETOS_REAL, valor);
		} finally {
			 cct.releaseConnection(conn);
		}
	}
	
	/**
	 * Genera un id de transacción para la Diputación de Ciudad Real
	 * Concatena idDoc + hora actual en milisegundos
	 * @param signDocument
	 * @return
	 * @throws ISPACException 
	 */
	public static String generarIdTransaccionFirma(SignDocument signDocument) throws ISPACException{
		
		StringBuffer sbIdTransaccion = new StringBuffer();
		int idDoc = signDocument.getItemDoc().getKeyInt();
		sbIdTransaccion.append(idDoc);
		sbIdTransaccion.append(new Date().getTime());
		return sbIdTransaccion.toString();
	}
	
}

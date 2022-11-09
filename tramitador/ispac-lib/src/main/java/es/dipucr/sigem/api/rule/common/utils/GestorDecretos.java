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
import ieci.tdw.ispac.ispaclib.sign.SignDetailEntry;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.ServicioEstructuraOrganizativa;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.UsuarioData;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;

/**
 * [eCenpri-Felipe #871]
 * Biblioteca de funciones para el control de bloqueos en la
 * firma simultánea de ciertos tipos de documentos (ej. Decretos)
 * @author Felipe
 * @since 19.04.13
 */
public class GestorDecretos {
	
	protected static final Logger LOGGER = Logger.getLogger("loggerFirma");


	public static final String SEQUENCE_NUM_DECRETOS_AUX = "SPAC_SQ_NUMDECRETO";
	public static final String SEQUENCE_NUM_DECRETOS_REAL = "SPAC_SQ_NUMDECRETO_FIRMADO";
	public static final String CATALOG_SPAC_TRAMITES_COD_TRAMITE_DECRETOS = "PREP_FIRMAS_DEC";
	public static final String CATALOG_SPAC_TRAMITES_COD_TRAMITE_DECRETO_DIRECTO = "DECRETO-DIRECTO";
	
	//Separador para los números de decreto
	public static final String NUM_DECRETO_SEPARATOR = "/";
	
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
	@Deprecated //Portafirmas
	public static boolean esFirmaPresidente(IClientContext cct, int idDoc) throws ISPACException{
		
		int tipoFirma = comprobarFirmaDecretos(cct, idDoc);
		return (tipoFirma == TIPO_FIRMA_PRESIDENTE);
	}
	
	/**
	 * [dipucr-Felipe #1564]
	 * @param cct
	 * @param idDoc
	 * @return
	 * @throws ISPACException
	 */
	public static boolean esFirmaAlcaldiaPresidencia(IClientContext cct, int idDoc) throws ISPACException{
		
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			ISignAPI signAPI = cct.getAPI().getSignAPI();
			
			IItem itemDoc = entitiesAPI.getDocument(idDoc);
			String sIdCircuito = itemDoc.getString("ID_CIRCUITO");
			
			if (!StringUtils.isEmpty(sIdCircuito)){
				//Vemos si el primer firmante del circuito tiene el rol alcalde/presidente
				IItem itemPasoCircuito = signAPI.getCircuitStep(Integer.valueOf(sIdCircuito), 1);
				String idFirmante = itemPasoCircuito.getString("ID_FIRMANTE");
				
				if (esUsuarioAsignaNumDecreto(cct, idFirmante)){
					return true;
				}
				else{
					return false;
				}
			}
			else{
				//Enviar a mi firma. Hay casos que damos por buena la firma de decreto?
				return false;
			}
		}
		catch(Exception ex){
			String error = "Error al comprobar si el circuito del documento puede asignar número de decreto";
			LOGGER.error(error, ex);
			throw new ISPACException(error, ex);
		}
	}
	
	/**
	 * [dipucr-Felipe #1564]
	 * @param cct
	 * @param idFirmante
	 * @return
	 */
	public static boolean esUsuarioAsignaNumDecreto(IClientContext cct, String idUsuario) throws ISPACException{
		
		try{
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			ServicioEstructuraOrganizativa servicioEstructuraOrganizativa = LocalizadorServicios.getServicioEstructuraOrganizativa();
			int id = Integer.parseInt(idUsuario.replace("1-", ""));
			Usuario usuario = servicioEstructuraOrganizativa.getUsuario(id, entidad);
			UsuarioData datosUsuario = usuario.getUserData();
			return datosUsuario.isAsignaNumDecreto();
		}
		catch(Exception ex){
			String error = "Error al comprobar si el usuario tiene activado el check de asignación de número de decretos";
			LOGGER.error(error, ex);
			throw new ISPACException(error, ex);
		}
	}

	/**
	 * Comprueba la firma de los documentos de decretos
	 * @param cct
	 * @param idDoc
	 * @return
	 * @throws ISPACException 
	 */
	@Deprecated //Portafirmas. No se comprueba la firma presidente-fedatario ni tampoco los circuitos
	public static int comprobarFirmaDecretos(IClientContext cct, int idDoc) throws ISPACException{
		
		int tipoFirma = 0;
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		// Estado adm. "Esperando firmas"
		if (esTramiteFirmaDecreto(cct, idDoc)) {

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
	 * [dipucr-Felipe #1246]
	 * Sacamos este trámtie para el Portafirmas, pues ya no tenemos que mirar la tabla de circuitos
	 * @return
	 */
	public static boolean esTramiteFirmaDecreto(IClientContext cct, int idDoc) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		// Comprobamos que estasmo en el trámite de Preparación de Firmas
		IItem itemDoc = entitiesAPI.getDocument(idDoc);
		int taskProcessId = (Integer) itemDoc.get("ID_TRAMITE");
		IItem itemTram = entitiesAPI.getTask(taskProcessId);	
		int idTaskTramCtl = (Integer) itemTram.get("ID_TRAM_CTL");
		String codTram = TramitesUtil.getCodTram(idTaskTramCtl, cct);

		// Estado adm. "Esperando firmas"
		return CATALOG_SPAC_TRAMITES_COD_TRAMITE_DECRETOS.equals(codTram) || CATALOG_SPAC_TRAMITES_COD_TRAMITE_DECRETO_DIRECTO.equals(codTram);
		
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
	@Deprecated // Portafirmas. Ya no usamos las secuencias 
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
	 * [dipucr-Felipe #1246] Recupera un nuevo número de decreto
	 * @param cct
	 * @param bActual
	 * @return
	 * @throws ISPACException
	 */
	public static String getNuevoNumDecreto(IClientContext cct) throws ISPACException{
		
		GregorianCalendar gc = new GregorianCalendar();
		int anio = gc.get(Calendar.YEAR);

		int numDecreto = getUltimoNumDecreto(cct) + 1;
		
		String sNumDecreto = anio + NUM_DECRETO_SEPARATOR + numDecreto;
		return sNumDecreto;
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
	@Deprecated //Portafirmas
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
	 * [dipucr-Felipe #1246]
	 * Actualiza los valores del decreto dependiendo de si se trata del presidente o del fedatario
	 * @param cct
	 * @param signDocument
	 * @param numDecreto
	 * @param listFirmas
	 * @throws ISPACException 
	 */
	public static void actualizarValoresDecretoPortafirmas
		(IClientContext cct, SignDocument signDocument, String numDecreto, List<SignDetailEntry> listFirmas) throws ISPACException{

		String numExp = signDocument.getNumExp();
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			//Primero buscamos si existe decreto directo, si es así, que haga lo de siempre pero con este (solo puede haber uno, etc. etc.)
			//Si no hay directo, hace lo de siempre.
			String idTramiteDoc = signDocument.getItemDoc().getString(DocumentosUtil.ID_TRAMITE);
			StringBuilder consulta = new StringBuilder(" WHERE ");
			consulta.append(" NUMEXP = '" + numExp + "' ");
			consulta.append(" AND ID_TRAMITE = " + idTramiteDoc);
			
			IItemCollection colDecretos = entitiesAPI.queryEntities("SGD_DECRETO", consulta.toString());
			
			if(null == colDecretos || colDecretos.toList().size() == 0){
				colDecretos = entitiesAPI.getEntities("SGD_DECRETO", numExp);
			}
			
			
			// Si aún no se ha creado la entidad Decreto, aunque esto nunca debe de suceder
			IItem itemDecreto = null;
			
			if (null == colDecretos || colDecretos.toList().size() == 0){
				itemDecreto = entitiesAPI.createEntity("SGD_DECRETO", numExp);
			}
			else if (null != colDecretos && colDecretos.toList().size() == 1){
				itemDecreto = (IItem)colDecretos.iterator().next();
			}else {
		        throw new ISPACRuleException("Error al seleccionar el registro Decreto");
			}
			
			// Controlamos si no tiene ya asignado un número de decreo
			if (StringUtils.isEmpty(itemDecreto.getString("NUMERO_DECRETO"))){
				
				GregorianCalendar gc = new GregorianCalendar();
				itemDecreto.set("ANIO", getOnlyAnio(numDecreto));
				Date fechaDecreto = gc.getTime();
				itemDecreto.set("FECHA_DECRETO", fechaDecreto);
				itemDecreto.set("NUMERO_DECRETO", getOnlyNumero(numDecreto));
	
				SignDetailEntry firmaPresidente = listFirmas.get(0);
				itemDecreto.set("NOMBRE_PRESIDENTE", firmaPresidente.getAuthor());
				Date fechaPresidente = FechasUtil.convertToDate(firmaPresidente.getSignDate(), FechasUtil.SIMPLE_DATE_PATTERN_HOUR);
				itemDecreto.set("FECHA_PRESIDENTE", fechaPresidente);
				
				if (listFirmas.size() > 1){//Hay firma de fedatario
					SignDetailEntry firmaFedatario = listFirmas.get(1);
					itemDecreto.set("NOMBRE_FEDATARIO", firmaFedatario.getAuthor());
					Date fechaFedatario = FechasUtil.convertToDate(firmaFedatario.getSignDate(), FechasUtil.SIMPLE_DATE_PATTERN_HOUR);
					itemDecreto.set("FECHA_FEDATARIO", fechaFedatario);
				}
				
				itemDecreto.store(cct);
			}
		}
		catch(Exception ex){
			String error = "Error al actualizar los valores del decreto. Num.Decreto: " + numDecreto 
					+ ". Numexp: " + numExp + " - " + ex.getMessage();
			LOGGER.error(error, ex);
			throw new ISPACException(error, ex);
		}
	}
	
	public static void actualizarValoresDecretoFirmaSello (IClientContext cct, String numexp, int idTramite, String numDecreto) throws ISPACException{
		try{
			//Se recupera el decreto del expediente
			IItem itemDecreto = DecretosUtil.getDecreto(cct, numexp, idTramite);
			
			// Controlamos si no tiene ya asignado un número de decreo
			if (StringUtils.isEmpty(itemDecreto.getString(DecretosUtil.DecretoTabla.NUMERO_DECRETO))){
				
				GregorianCalendar gc = new GregorianCalendar();
				itemDecreto.set(DecretosUtil.DecretoTabla.ANIO, GestorDecretos.getOnlyAnio(numDecreto));
				Date fechaDecreto = gc.getTime();
				itemDecreto.set(DecretosUtil.DecretoTabla.FECHA_DECRETO, fechaDecreto);
				itemDecreto.set(DecretosUtil.DecretoTabla.NUMERO_DECRETO, GestorDecretos.getOnlyNumero(numDecreto));
				
				itemDecreto.set(DecretosUtil.DecretoTabla.NOMBRE_PRESIDENTE, SelladoUtil.NOMBRE_FIRMANTE_SELLO);
				itemDecreto.set(DecretosUtil.DecretoTabla.FECHA_PRESIDENTE, fechaDecreto);
				
				itemDecreto.store(cct);
			}
		}
		catch(Exception ex){
			String error = "Error al actualizar los valores del decreto. Num.Decreto: " + numDecreto + ". Numexp: " + numexp + " - " + ex.getMessage();
			LOGGER.error(error, ex);
			throw new ISPACException(error, ex);
		}
	}
	
	/**
	 * [dipucr-Felipe #1246]
	 * Devuelve el número de decreto actual para el expediente, si lo tuviera
	 * @param cct
	 * @param signDocument
	 * @throws ISPACException 
	 */
	public static String getNumDecretoActual(IClientContext cct, SignDocument signDocument) throws ISPACException{
		return getNumDecretoActual(cct, signDocument.getNumExp(), signDocument.getItemDoc().getInt(DocumentosUtil.ID_TRAMITE));
	}

	//Se sobrecarga el método para que se pueda usar en las reglas también
	public static String getNumDecretoActual(IClientContext cct, String numexp, int idTramiteDoc) throws ISPACException{
	
		String numDecreto = null;
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();

			//Primero buscamos si existe decreto directo, si es así, que haga lo de siempre pero con este (mira si ya tiene número de decreto, si es así lo devuelve, si no, devuelve null)
			//Si no hay directo, hace lo de siempre.
			StringBuilder consulta = new StringBuilder(" WHERE ");
			consulta.append(" NUMEXP = '" + numexp + "' ");
			consulta.append(" AND ID_TRAMITE = " + idTramiteDoc);
			
			IItemCollection colDecretos = entitiesAPI.queryEntities("SGD_DECRETO", consulta.toString());
			
			if(null == colDecretos || colDecretos.toList().size() == 0){
				colDecretos = entitiesAPI.getEntities("SGD_DECRETO", numexp);
			}
			
			if(colDecretos.next()){
				IItem itemDecreto = colDecretos.value();
				String numero = itemDecreto.getString("NUMERO_DECRETO");
				if (!StringUtils.isEmpty(numero)){
					String anio = itemDecreto.getString("ANIO");
					numDecreto = anio + NUM_DECRETO_SEPARATOR + numero;
				}
			}
		}
		catch(Exception ex){
			String error = "Error al recuperar el num.decreto actual del exp " + numexp + " - " + ex.getMessage();
			LOGGER.error(error, ex);
			throw new ISPACException(error, ex);
		}
		return numDecreto;
	}
	
	/**
	 * Actualiza los valores del decreto dependiendo de si se trata del presidente o del fedatario
	 * @param cct
	 * @param signDocument
	 * @param numDecreto
	 * @throws ISPACException 
	 */
	@Deprecated //Portafirmas
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
	 * [dipucr-Felipe #1246] Deshacer valores decretos portafirmas
	 * @param cct
	 * @param numExp
	 * @param numDecreto
	 * @throws ISPACException
	 */
	public static void deshacerValoresDecretoPortafirmas(IClientContext cct, String numExp, String numDecreto) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		IItemCollection colDecretos = entitiesAPI.getEntities("SGD_DECRETO", numExp);
		IItem itemDecreto = (IItem)colDecretos.iterator().next();
		
		Object objNull = null;
		itemDecreto.set("NOMBRE_FEDATARIO", objNull);
		itemDecreto.set("FECHA_FEDATARIO", objNull);
		itemDecreto.set("ANIO", objNull);
		itemDecreto.set("FECHA_DECRETO", objNull);
		itemDecreto.set("NUMERO_DECRETO", objNull);
		itemDecreto.set("NOMBRE_PRESIDENTE", objNull);
		itemDecreto.set("FECHA_PRESIDENTE", objNull);
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
	
	/**
	 * [dipucr-Felipe #1246] Copiado de TelefonicaSignConnector
	 * @param SignDocument signDocument
	 * @return True: El documento es de "Decreto" 
	 *  
	 */
	public static boolean isDocDecreto (SignDocument signDocument){
		
		String nombreDoc = "";
		try {
			nombreDoc = signDocument.getItemDoc().getString("NOMBRE");
		} catch (ISPACException e) {
			LOGGER.error("ERROR. " + e.getMessage(), e);
			return false;
		}
		
		if (nombreDoc.equals("Decreto")){
			return true;
		}
		
		return false;
	}
	
}

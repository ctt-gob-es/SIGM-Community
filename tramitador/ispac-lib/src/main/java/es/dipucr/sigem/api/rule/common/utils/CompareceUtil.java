package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.log4j.Logger;

import es.dipucr.notificador.model.EnvioWS;
import es.dipucr.notificador.model.TerceroWS;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.jccm.notificador.ws.AplicacionesServiceProxy;

public class CompareceUtil {
	private static Logger logger = Logger.getLogger(CompareceUtil.class);

	public static boolean envioDocComparece(IClientContext ctx, String numexp, int idDoc, Vector<String> dnisRepresentantesComparece, String destinatario, IItem documentoItem, byte[] documentContent, String extension)
			throws ISPACRuleException {
		
		String nombreProc = "";
		String nombreDoc = "";
		String codigoCotejo = "";
		String descripcionDocumento = "";
		int taskId = 0;	
		
		// Acceso al servicio web de comparece
		AplicacionesServiceProxy asp = new AplicacionesServiceProxy();

		Date fecha = new Date();
		fecha = FechasUtil.addDias(fecha, 10);
		FechaDesglosada fechaDesglosada = FechasUtil.getFechaDesglosada(fecha);

		int diaCaducidad = fechaDesglosada.getDia();
		int mesCaducidad = fechaDesglosada.getMes();
		int anyoCaducidad = fechaDesglosada.getAnio();

		try {
			// Sacar el hash del documento Documento a firmar
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			
			IItem expedienteIItem = ExpedientesUtil.getExpediente(ctx, numexp);
			if(expedienteIItem != null){
				nombreProc = expedienteIItem.getString("NOMBREPROCEDIMIENTO");
			}
				
			if (documentoItem.getString("NOMBRE") != null)
				nombreDoc = DipucrCommonFunctions.limpiarCaracteresEspeciales(documentoItem.getString("NOMBRE"));
			else
				nombreDoc = "";
			
			if (documentoItem.getString("DESCRIPCION") != null)
				descripcionDocumento = documentoItem.getString("DESCRIPCION");
			else
				descripcionDocumento = "";
			
			if (documentoItem.getString("COD_COTEJO") != null)
				codigoCotejo = documentoItem.getString("COD_COTEJO");
			else
				codigoCotejo = "";
			
			if (documentoItem.getString("ID_TRAMITE") != null)
				taskId = documentoItem.getInt("ID_TRAMITE");
			else
				taskId= 0;
			
			for (int i = 0; i < dnisRepresentantesComparece.size(); i++) {

				if (!comprobarEnvioComparece(entitiesAPI, numexp, idDoc, dnisRepresentantesComparece.get(i), taskId)) {
					String[] dniEnvio = new String[1];
					dniEnvio[0] = dnisRepresentantesComparece.get(i);
					String entidad = EntidadesAdmUtil.obtenerEntidad(ctx);
					
					//Se va a mandar el documento original					
					DataHandler notificacion = new DataHandler(new ByteArrayDataSource(documentContent));
					String hashDocument = generateHashCode(documentContent);
					logger.debug("HASHDOCUMENTO " + hashDocument);

					EnvioWS[] envio = asp.notificar(entidad, dniEnvio, nombreProc, numexp, descripcionDocumento, diaCaducidad, 
							mesCaducidad, anyoCaducidad, notificacion, nombreDoc, extension, hashDocument, codigoCotejo);
					logger.debug("Envio a " + dnisRepresentantesComparece.get(i) + ": " + envio);
					
					//[Manu Ticket #110] - INICIO - ALSIGM3 Cambiar registro de salida para que actualice el campo ESTADONOTIFICACION
					String consulta = "WHERE NUMEXP = '" + numexp + "' AND IDENT_DOC = '" + idDoc + "' AND TRAMITE=" + taskId + "";
					logger.debug("Si ya se ha enviado alguno no actualizamos es ESTADONOTIFICACION. Consulta: " + consulta);
					IItemCollection collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.DPCR_ACUSES_COMPARECE, consulta);
					Iterator<?> it = collection.iterator();
					if (!it.hasNext()) {
						documentoItem.set("ESTADONOTIFICACION", "PR");
						documentoItem.store(ctx);
					}
		      		//[Manu Ticket #110] - FIN - ALSIGM3 Cambiar registro de salida para que actualice el campo ESTADONOTIFICACION
					
					for (int j = 0; j < envio.length; j++) {
						logger.debug("Notificación enviada.");
						logger.debug("Id de la notificación: " + envio[j].getIdNotificacion());
						logger.debug("DNI del notificado: " + envio[j].getDni());
						
						if (envio[j].getIdNotificacion() > 0) {
							// Enviar a dipcr_aviso_comparece
							String destino = DocumentosUtil.getAutorUID(entitiesAPI, idDoc);						
							insertarAcuseCompare(ctx, numexp, envio[j].getIdNotificacion(), destino, fecha, idDoc, dnisRepresentantesComparece.get(i), taskId);
						} else {
							logger.warn("envio[i].isEmail() " + envio[j].isEmail());
							logger.warn("envio[i].isNotificacion() " + envio[j].isNotificacion());
							logger.warn("envio[i].isSms() " + envio[j].isSms());

							logger.warn("Error a la hora de enviar la notificacion por COMPARECE.");
							logger.warn("numexp " + numexp + ", al destinatario: " + dniEnvio + ", documento: " + descripcionDocumento);
						}
					}
				}
			}
		} catch (RemoteException e) {
			logger.error("Error al enviar la notificación. Error en el sistema COMPARECE. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar la notificación. Error en el sistema COMPARECE. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al enviar la notificación al COMPARECE. Error interno." + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar la notificación al COMPARECE. Error interno." + e.getMessage(), e);
		}
		
		return true;
	}
	
	public static boolean esUsuarioComparece(IClientContext ctx, String numexp, String destinatario, String destinatarioId, Vector<String> dnisRepresentantesComparece, String descripcion)	throws RemoteException, ISPACRuleException{
		
		boolean esUsuarioComp = false;

		try {
			
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			String sqlQuery = "";
			if (StringUtils.isEmpty(destinatarioId) || destinatarioId.equals("0")) {
				sqlQuery = "NOMBRE='" + destinatario + "'";
			} else {
				sqlQuery = "ID_EXT='" + destinatarioId + "'";
			}

			logger.debug("sqlQueryPart " + sqlQuery);
			
			IItemCollection participantes = ParticipantesUtil.getParticipantes(ctx, numexp, sqlQuery, "");
			// Si es = a 0 quiere decir que esta en sus expedientes relacionados
			// entonces miro en cualquier otro expediente para sacar si dni
			if (participantes.toList().size() == 0) {

				if (StringUtils.isEmpty(destinatarioId) || destinatarioId.equals("0")) {
					sqlQuery = "NOMBRE='" + destinatario + "'";
				} else {
					sqlQuery = "ID_EXT='" + destinatarioId + "'";
				}

				participantes = ParticipantesUtil.getParticipantes(ctx, "", sqlQuery, "ID DESC");
			}

			if (participantes.toList().size() != 0) {
				Iterator<?> itParticipantes = participantes.iterator();

				IItem partic = null;
				String dniDestinatario = "";
				
				while (itParticipantes.hasNext() && StringUtils.isEmpty(dniDestinatario)) {
					partic = (IItem) itParticipantes.next();
					if (partic.getString("NDOC") != null) {
						dniDestinatario = partic.getString("NDOC");
						logger.debug("Se encuntra NDOC, se sale con valor: " + dniDestinatario);
					}
				}
				logger.debug("Se tiene NDOC: " + dniDestinatario);
				AplicacionesServiceProxy asp = new AplicacionesServiceProxy();
				
				// Compruebo si ese dni pertenece a algun ayuntamiento y tiene representantes.
				Vector<String> dnisRepresentantes = new Vector<String>();
				boolean tieneRepresentantes = existeRepresentante(ctx, entitiesAPI, dniDestinatario, dnisRepresentantes);
				
				logger.debug("¿El destinatario con DNI " + dniDestinatario + " tiene representantes? " + tieneRepresentantes);
				String entidad = EntidadesAdmUtil.obtenerEntidad(ctx);
				
				if (tieneRepresentantes) {
					for (int i = 0; i < dnisRepresentantes.size(); i++) {
						TerceroWS tercero = asp.consultarDNI(entidad,(String) dnisRepresentantes.get(i));
						if (tercero != null) {
							esUsuarioComp = true;
							dnisRepresentantesComparece.add((String) dnisRepresentantes.get(i));
						} 
						logger.debug("¿La persona con DNI " + (String) dnisRepresentantes.get(i) + " y que es representante del destinatario " + dniDestinatario + " está dada de alta en COMPARECE?" + esUsuarioComp);
					}
				} 
				else {
					TerceroWS tercero = null;
					try{
						tercero = asp.consultarDNI(entidad, dniDestinatario);
					} catch (NullPointerException e) {
						esUsuarioComp = false;
						logger.error("Error en la aplicación del comparece debe estar caido." + e.getMessage(), e);						
					}
					if (tercero != null) {
						esUsuarioComp = true;
						dnisRepresentantesComparece.add(dniDestinatario);
					}
					logger.debug("¿La persona con DNI " + dniDestinatario + " es usuario de COMPARECE?" + esUsuarioComp);
				}
			}
			/*else{
				logger.error("Error. No se ha podido encontrar al participante por ningún sitio.");
				throw new ISPACRuleException("Error. No se ha podido encontrar al participante por ningún sitio.");
			}*/
		} catch (ISPACException e) {
			logger.error("Error al comprobar si es usuario comparece." + e.getMessage(), e);
			throw new ISPACRuleException("Error al comprobar si es usuario comparece." + e.getMessage(), e);
		}
		return esUsuarioComp;
	}
	
	public static boolean existeRepresentante(IClientContext ctx, IEntitiesAPI entitiesAPI, String dniDestinatario, Vector<String> dnisRepresentantes) throws ISPACException {
		boolean tieneRepresentante = false;

		// nombre procedimiento PCD-51=Datos del Tercero y Gestión de  Representantes BOP
		String consulta = "WHERE NIFCIFTITULAR ='" + dniDestinatario + "' AND (CODPROCEDIMIENTO='CR-TERC-01' OR CODPROCEDIMIENTO='PCD-222' OR CODPROCEDIMIENTO='PCD-223') AND FCIERRE IS NULL";

		logger.debug("Consulta es representante: " + consulta);

		IItemCollection itemCollectionpCD = ExpedientesUtil.queryExpedientes(ctx, consulta);
		
		if (itemCollectionpCD != null && itemCollectionpCD.next()){
			IItem expediente = ((IItem) itemCollectionpCD.iterator().next());
			
			String numexp = expediente.getString("NUMEXP");
			consulta = "WHERE NUMEXP='" + numexp + "'";
			
			itemCollectionpCD = entitiesAPI.queryEntities(Constants.TABLASBBDD.DPCR_PARTICIPANTES_COMPARECE, consulta);
			Iterator<?> it = itemCollectionpCD.iterator();
			
			while (it.hasNext()) {
				IItem partiComp = (IItem) it.next();
				String notificacion = "";
				if (partiComp.getString("NOTIFICACION") != null)
					notificacion = partiComp.getString("NOTIFICACION");
				else
					notificacion = "";
				if (notificacion.equals("SI")) {
					tieneRepresentante = true;
					String dniRepres = "";
					if (partiComp.getString("DNI_PARTICIPANTE") != null)
						dniRepres = partiComp.getString("DNI_PARTICIPANTE");
					else
						dniRepres = "";
					dnisRepresentantes.add(dniRepres);
				}
			}
		}

		return tieneRepresentante;
	}
	
	/**
	 * [ecenpri-Teresa Ticket #405] SIGEM Comparece-Sigem no permitir mandar la
	 * misma notificación a la misma persona
	 * 
	 * @since 06.07.2011
	 * @author Teresa
	 * @throws ISPACRuleException
	 */
	public static boolean comprobarEnvioComparece(IEntitiesAPI entitiesAPI, String numexp, int idDoc, String dniRepresentanteComparece, int taskId) throws ISPACRuleException {

		boolean mandado = false;

		try {
			String consulta = "WHERE NUMEXP = '" + numexp + "' AND IDENT_DOC = '" + idDoc + "' AND DNI_NOTIFICADO='" + dniRepresentanteComparece + "' AND TRAMITE=" + taskId + "";
			logger.debug("Consulta comprobarEnvioComparece: " + consulta);
			IItemCollection collection = entitiesAPI.queryEntities(Constants.TABLASBBDD.DPCR_ACUSES_COMPARECE, consulta);
			Iterator<?> it = collection.iterator();
			if (it.hasNext()) {
				mandado = true;
			}
		} catch (ISPACException e) {
			logger.error("Error al comprobar si se ya se ha enviado la notificación del expediente " + numexp + " al destinatario " + dniRepresentanteComparece + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al comprobar si se ya se ha enviado la notificación del expediente " + numexp + " al destinatario " + dniRepresentanteComparece + ". " + e.getMessage(), e);
		}

		logger.debug("¿La notificación a " + dniRepresentanteComparece + " del expediente " + numexp + " en el trámite " + taskId + " ya había sido enviada? " + mandado);

		return mandado;
	}
	
	
	
	
	public static String generateHashCode(byte[] documentContent) throws ISPACException {

		String contentBase64 = new String(documentContent);

		// Crea un digest con el algoritmo SHA-1
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error al obtener la instancia de MessageDigest. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener la instancia de MessageDigest. " + e.getMessage(), e);
		}

		// Genera el código hash de la cadena
		byte[] hash = md.digest(contentBase64.getBytes());
		logger.debug("hexHash byte " + hash.toString());

		String hexHash = toHexadecimal(hash);
		logger.debug("hexHash " + hexHash);

		return hexHash;
	}
	
	public static void insertarAcuseCompare(IClientContext ctx, String numexp,	int idDocumenNot, String idResponsable, Date fechaFin, int idDoc,
			String dniNotificado, int taskId) throws ISPACRuleException {

		IItem notice;
		try {
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			notice = entitiesAPI.createEntity( Constants.TABLASBBDD.DPCR_ACUSES_COMPARECE, "");
			notice.set("NUMEXP", numexp);
			notice.set("ID_PROC", invesflowAPI.getProcess(numexp).getInt("ID"));
			notice.set("ID_NOTIFICACION", idDocumenNot);
			notice.set("ID_RESPONSABLE", idResponsable);
			notice.set("IDENT_DOC", idDoc);
			notice.set("ESTADO", 0);
			notice.set("DNI_NOTIFICADO", dniNotificado);
			notice.set("TRAMITE", taskId);

			notice.store(ctx);

		} catch (ISPACException e) {
			logger.error("Error al insertarAcuseCompare " + numexp + " al dniNotificado " + dniNotificado + " Fecha "+fechaFin+" idDoc "+idDoc+". " + e.getMessage(), e);
			throw new ISPACRuleException("Error. ", e);			
		}
	}
	
	public static String toHexadecimal(byte[] datos) {
		String resultado = "";
		ByteArrayInputStream input = new ByteArrayInputStream(datos);
		String cadAux;
		int leido = input.read();
		while (leido != -1) {
			cadAux = Integer.toHexString(leido);
			if (cadAux.length() < 2) // Hay que añadir un 0
				resultado += "0";
			resultado += cadAux;
			leido = input.read();
		}
		return resultado;
	}
	
	public static boolean comprobarUsuario(String dni, String entidad) throws ISPACRuleException{
		boolean existe = false;
		AplicacionesServiceProxy asp = new AplicacionesServiceProxy();
		try {
			TerceroWS tercero = asp.consultarDNI(entidad, dni);
			if(tercero != null){
				existe = true;
			}
		} catch (RemoteException e) {
			logger.error("Error al entidad " + entidad + " al dni " + dni + ". " + e.getMessage(), e);
			throw new ISPACRuleException(e);
		}
		return existe;
	}
	
	public static boolean altaTercero(String dni, String email, String telefono, String entidad) throws ISPACRuleException{
		boolean correcto = true;
		AplicacionesServiceProxy asp = new AplicacionesServiceProxy();
		try {
			correcto = asp.altaTercero(entidad, dni, email, telefono);
		} catch (RemoteException e) {
			logger.error("Error al entidad " + entidad + " al dni " + dni + ". " + e.getMessage(), e);
			throw new ISPACRuleException(e);
		}

		return correcto;
	}
}

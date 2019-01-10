package es.dipucr.sigem.api.rt;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.catalogo.Documentos;
import ieci.tecdoc.sgm.core.services.catalogo.ServicioCatalogoTramites;
import ieci.tecdoc.sgm.core.services.catalogo.Tramite;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.sesion.ServicioSesionUsuario;
import ieci.tecdoc.sgm.core.services.telematico.PeticionDocumento;
import ieci.tecdoc.sgm.core.services.telematico.PeticionDocumentos;
import ieci.tecdoc.sgm.core.services.telematico.RegistroPeticion;
import ieci.tecdoc.sgm.core.services.telematico.ServicioRegistroTelematico;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebSecurityConfiguration;


public class DipucrSigemRegistroTelematicoAPI {

	private static final Logger LOGGER = Logger.getLogger(DipucrSigemRegistroTelematicoAPI.class);
	
	public static final String PLANTILLA_JASPER_NAME = "plantilla.jasper";
	
	public static byte[] registrarFacturaAndIniciarExpediente
		(Entidad entidad, String codTramite, String cifnif, String nombre, String xmlPeticionRegistro, 
				String xmlDatosEspecificos, List<DipucrFileAdjuntoRT> listAdjuntos) throws Exception
	{

		try {
			// Obtenemos el trámite
			Tramite sigemTramite = getTramite(entidad, codTramite);

			// Creamos la sesión de usuario
			String sessionId = login(entidad, cifnif, nombre);

			// Creación de la petición de factura
			// Se suben los ficheros de anexos
			// ***********************************
			RegistroPeticion registroPeticion = crearPeticionRegistro
					(sessionId, sigemTramite, xmlPeticionRegistro, listAdjuntos);

			// Creación de la petición
			byte[] registryRequest = crearPeticionRegistroB64(entidad, sessionId, registroPeticion);
			
			// Registro e inicio del expediente
			byte[] respuestaSigem = registrarTelematicoAndIniciarExpediente
			(
					entidad,
					sessionId,
					codTramite,
					registryRequest,
					xmlDatosEspecificos,
					sigemTramite
			);
			
			//Logout
			logout(entidad, sessionId);
			
			return respuestaSigem;

		} catch (Exception ex) {
			String error = "Error al crear el registro e iniciar el expediente en SIGEM";
			LOGGER.error(error, ex);
			throw new ISPACException(error, ex);
		}
	}
	
	/**
	 * Método de login para recuperar una sesión
	 * @param entidad
	 * @param cifnif
	 * @param nombre
	 * @return
	 * @throws Exception
	 */
	private static String login(Entidad entidad, String cifnif, String nombre) throws Exception 
	{
		ServicioSesionUsuario servicioSesion = LocalizadorServicios.getServicioSesionUsuario();
		return servicioSesion.login(null, nombre, "", "", cifnif, entidad);
	}
	
	/**
	 * Método de logout
	 * @param entidad
	 * @param sessionId
	 * @throws Exception
	 */
	private static void logout(Entidad entidad, String sessionId) throws Exception {
		
		ServicioSesionUsuario servicioSesion = LocalizadorServicios.getServicioSesionUsuario();
		servicioSesion.logout(sessionId, entidad);
	}
	
	/**
	 * Devuelve el trámite de SIGEM con sus datos de configuración
	 * @param entidad 
	 * @param codTramite
	 * @return
	 */
	private static Tramite getTramite(Entidad entidad, String codTramite) throws Exception
	{
		Tramite sigemTramite = null;
		ServicioCatalogoTramites servicioCatalago = LocalizadorServicios.getServicioCatalogoTramites();
		boolean loadDocuments = true;
		sigemTramite = servicioCatalago.getProcedure(codTramite, loadDocuments, entidad);

		Documentos documentos = servicioCatalago.getProcedureDocuments(codTramite, entidad);
		sigemTramite.setDocuments(documentos);
		
		return sigemTramite;
	}
	
	/**
	 * Creación de la petición de registro del anuncio BDNS
	 * @param sessionId
	 * @param sigemTramite
	 * @param listAdjuntos 
	 * @param anuncio
	 * @return
	 * @throws Exception
	 */
	private static RegistroPeticion crearPeticionRegistro
		(String sessionId, Tramite sigemTramite, String xmlPeticionRegistro, List<DipucrFileAdjuntoRT> listAdjuntos) 
				throws Exception
	{
		PeticionDocumentos peticionDocumentos = new PeticionDocumentos();

		if (null != peticionDocumentos){
			for (DipucrFileAdjuntoRT fileAdjunto : listAdjuntos){
				PeticionDocumento peticionDoc = new PeticionDocumento();
				peticionDoc.setCode(fileAdjunto.getCodAdjunto());
				peticionDoc.setExtension(fileAdjunto.getExtensionAdjunto());
				peticionDoc.setFileName(fileAdjunto.getNombre());
				peticionDoc.setLocation(fileAdjunto.getFile().getPath());
				peticionDocumentos.add(peticionDoc);
			}
		}
	
		RegistroPeticion registroPeticion = new RegistroPeticion();
		registroPeticion.setProcedureId(sigemTramite.getId());
		registroPeticion.setDocuments(peticionDocumentos);
		registroPeticion.setAddressee(sigemTramite.getAddressee());
		registroPeticion.setSpecificData(xmlPeticionRegistro);
		
		return registroPeticion;
	}
	
	/**
	 * Recuper
	 * @param entidadSigem
	 * @param sessionId
	 * @param registroPeticion
	 * @return
	 * @throws Exception
	 */
	private static byte[] crearPeticionRegistroB64(Entidad entidadSigem,
			String sessionId, RegistroPeticion registroPeticion) throws Exception {
		
		ServicioRegistroTelematico servicioRT = LocalizadorServicios.getServicioRegistroTelematico();
		byte[] registryRequest = servicioRT.crearPeticionRegistro
				(sessionId, registroPeticion, "es", null, null, entidadSigem);

		return registryRequest;
	}


	private static byte[] registrarTelematicoAndIniciarExpediente(
			Entidad entidad, String sessionId, String codTramite, byte[] registryRequest,
			String datosEspecificos, Tramite sigemTramite) throws Exception {
		
		String certificado = ServiciosWebSecurityConfiguration.getInstance().getCompletePath
				(ServiciosWebSecurityConfiguration.REGTEL_WS.SELLO_CERT_NAME);
		String plantilla = getPathPlantilla(entidad.getIdentificador(), codTramite);

		ServicioRegistroTelematico servicioRT = LocalizadorServicios.getServicioRegistroTelematico();
		byte[] respuestaSigem = servicioRT.registrarTelematicoAndIniciarExpediente
				(
						sessionId,
						registryRequest,
						datosEspecificos,
						"es",
						sigemTramite.getOficina(),
						plantilla,
						certificado,
						sigemTramite.getId(),
						entidad
				);

		return respuestaSigem;
	}
	
	
	private static String getPathPlantilla(String idEntidad, String codTramite) throws ISPACException{
		
		String tramitesSubpath = ISPACConfiguration.getInstance().get(ISPACConfiguration.TRAMITES_RT_SUBPATH);
		String completePath = System.getProperty("catalina.base") + File.separator + "webapps" + File.separator + tramitesSubpath +
				File.separator + idEntidad + File.separator + codTramite + File.separator + PLANTILLA_JASPER_NAME;
		return completePath;
	}


	
}

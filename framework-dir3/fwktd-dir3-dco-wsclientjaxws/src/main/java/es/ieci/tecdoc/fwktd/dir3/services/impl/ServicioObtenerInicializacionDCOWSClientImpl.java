package es.ieci.tecdoc.fwktd.dir3.services.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;

import javax.naming.NamingException;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import es.ieci.tecdoc.fwktd.dir3.exception.ObtencionFicheroInicializacionDCOException;
import es.ieci.tecdoc.fwktd.dir3.services.ServicioObtenerInicializacionDCO;
import es.ieci.tecdoc.fwktd.dir3.util.Base64Utils;
import es.ieci.tecdoc.fwktd.dir3.util.ZipUtils;
import es.map.directorio.manager.impl.SC01UNVolcadoDatosBasicosService;
import es.map.directorio.manager.impl.SC11OFVolcadoDatosBasicosService;
import es.map.directorio.manager.impl.wsexport.RespuestaWS;

/**
 * Implementación del servicio de inicialización de dco.
 * 
 */
public class ServicioObtenerInicializacionDCOWSClientImpl implements
		ServicioObtenerInicializacionDCO {
	
	/**
	 * Servicio para llamar al WS SC01UNVolcadoDatosBasicos - Inicializacion de Unidades
	 * Organicas
	 */
	protected SC01UNVolcadoDatosBasicosService servicioVolcadoUnidades;
	
	/**
	 * URL Servicio para llamar al WS SC01UNVolcadoDatosBasicos - Inicializacion de
	 * Unidades Organicas
	 */
	protected String servicioVolcadoUnidadesURL;
	
	/**
	 * Servicio para llamar al WS SC11OFVolcadoDatosBasicos - Inicializacion de Oficinas
	 */
	protected SC11OFVolcadoDatosBasicosService servicioVolcadoOficinas;
	
	/**
	 * Servicio para llamar al WS SC11OFVolcadoDatosBasicos - Inicializacion de Oficinas
	 */
	protected String servicioVolcadoOficinasURL;
	
	/**
	 * Directorio para dejar los ficheros
	 */
	protected String tempFilesDir;
	/**
	 * Login del servicio del DCO proporcionado por el ministerio
	 */
	protected String login;
	/**
	 * Password del servicio del DCO proporcionada por el ministerio
	 */
	protected String pass;
	/**
	 * Formato en el que obtener las actualizaciones
	 */
	protected String fileFormat;
	/**
	 * Tipo de consulta para las oficinas
	 */
	protected String oficinasQueryType;
	/**
	 * Tipo de consulta para las unidades organicas
	 */
	protected String unidadesQueryType;
	
	private final String VOLCADO_OFICINAS_FILE_NAME = "datosBasicosOficina.xml";
	private final String VOLCADO_UORGANICAS_FILE_NAME =
			"datosBasicosUOrganica.xml";
	private static final Logger logger = Logger
			.getLogger(ServicioObtenerInicializacionDCOWSClientImpl.class);
	
	/**
	 * Contructor por defecto que carga la configuracion del servicio
	 * 
	 * @throws MalformedURLException
	 * @throws NamingException
	 */
	public void initServices() throws MalformedURLException {
		if (servicioVolcadoUnidades == null) {
			URL wsdlLocationVU = new URL(servicioVolcadoUnidadesURL);
			QName serviceNameVU =
					new QName("http://impl.manager.directorio.map.es",
							"SC01UN_VolcadoDatosBasicosService");
			servicioVolcadoUnidades =
					new SC01UNVolcadoDatosBasicosService(wsdlLocationVU,
							serviceNameVU);
		}
		if (servicioVolcadoOficinas == null) {
			URL wsdlLocationVO = new URL(servicioVolcadoOficinasURL);
			QName serviceNameVO =
					new QName("http://impl.manager.directorio.map.es",
							"SC11OF_VolcadoDatosBasicosService");
			servicioVolcadoOficinas =
					new SC11OFVolcadoDatosBasicosService(wsdlLocationVO,
							serviceNameVO);
		}
	}
	
	/**
	 * Obtiene el fichero para actualizar las oficinas.
	 * Retorna el path al fichero xml
	 * 
	 * @return nombre del fichero de actualización de Oficinas
	 * 
	 */
	public String getFicheroInicializarOficinasDCO() {
		String finalFileName = null;
		try {
			initServices();
			File tempZipFile =
					File.createTempFile("dec", "zip", new File(
							getTempFilesDir()));
			RespuestaWS respuesta =
					getServicioVolcadoOficinas().getSC11OFVolcadoDatosBasicos()
							.exportar(getLogin(), getPass(), getFileFormat(),
									getOficinasQueryType(), "", "", "", "", "",
									"", "", "", "", "", "", "", "", "", "", "");
			Base64Utils.getInstance().decodeToFile(respuesta.getFichero(),
					tempZipFile.getAbsolutePath());
			List<String> filesUnzipped =
					ZipUtils.getInstance().unzipFile(
							tempZipFile.getAbsolutePath(), getTempFilesDir());
			
			Iterator<String> itr = filesUnzipped.listIterator();
			String fileName;
			while (itr.hasNext()) {
				fileName = itr.next();
				if (fileName.endsWith(VOLCADO_OFICINAS_FILE_NAME)) {
					finalFileName = fileName;
				}
			}
		}
		catch (ZipException zipEx) {
			logger.error(
					"ServicioInicializacionDCOWSClientImpl::getFicheroInicializarUnidadesDCO - Error al descomprimir el fichero retornado por el DCO.",
					zipEx);
			
		}
		catch (IOException ioEx) {
			logger.error(
					"ServicioInicializacionDCOWSClientImpl::getFicheroInicializarUnidadesDCO - Error al crear los ficheros temporales.",
					ioEx);
			
		}
		catch (Exception e) {
			logger.error("Error inesperado", e);
		}
		
		if (finalFileName == null) {
			throw new ObtencionFicheroInicializacionDCOException(
					"El proceso ha finalizado sin errores pero no se encuentra el fichero esperado con nombre: "
							+ VOLCADO_OFICINAS_FILE_NAME);
		}
		return finalFileName;
	}
	
	/**
	 * Obtiene el fichero para actualizar las unidades.
	 * Retorna el path al fichero xml
	 * 
	 * @return nombre del fichero de actualización de unidades
	 * 
	 */
	public String getFicheroInicializarUnidadesDCO() {
		String finalFileName = null;
		try {
			initServices();
			File tempZipFile =
					File.createTempFile("dec", "zip", new File(
							getTempFilesDir()));
			RespuestaWS respuesta =
					getServicioVolcadoUnidades().getSC01UNVolcadoDatosBasicos()
							.exportar(getLogin(), getPass(), getFileFormat(),
									getUnidadesQueryType(), "", "", "", "", "",
									"", "", "", "");
			Base64Utils.getInstance().decodeToFile(respuesta.getFichero(),
					tempZipFile.getAbsolutePath());
			List<String> filesUnzipped =
					ZipUtils.getInstance().unzipFile(
							tempZipFile.getAbsolutePath(), getTempFilesDir());
			
			Iterator<String> itr = filesUnzipped.listIterator();
			String fileName;
			while (itr.hasNext()) {
				fileName = itr.next();
				if (fileName.endsWith(VOLCADO_UORGANICAS_FILE_NAME)) {
					finalFileName = fileName;
				}
			}
		}
		catch (ZipException zipEx) {
			logger.error(
					"ServicioInicializacionDCOWSClientImpl::getFicheroInicializarUnidadesDCO - Error al descomprimir el fichero retornado por el DCO.",
					zipEx);
			
		}
		catch (IOException ioEx) {
			logger.error(
					"ServicioInicializacionDCOWSClientImpl::getFicheroInicializarUnidadesDCO - Error al crear los ficheros temporales.",
					ioEx);
			
		}
		catch (Exception e) {
			logger.error("Error inesperado", e);
		}
		if (finalFileName == null) {
			throw new ObtencionFicheroInicializacionDCOException(
					"El proceso ha finalizado sin errores pero no se encuentra el fichero esperado con nombre: "
							+ VOLCADO_UORGANICAS_FILE_NAME);
		}
		return finalFileName;
	}
	
	/**
	 * Obtiene el valor del parámetro servicioVolcadoUnidadesURL.
	 * 
	 * @return servicioVolcadoUnidadesURL valor del campo a obtener.
	 */
	public String getServicioVolcadoUnidadesURL() {
		return servicioVolcadoUnidadesURL;
	}
	
	/**
	 * Guarda el valor del parámetro servicioVolcadoUnidadesURL.
	 * 
	 * @param servicioVolcadoUnidadesURL
	 *            valor del campo a guardar.
	 */
	public void
			setServicioVolcadoUnidadesURL(String servicioVolcadoUnidadesURL) {
		this.servicioVolcadoUnidadesURL = servicioVolcadoUnidadesURL;
	}
	
	/**
	 * Obtiene el valor del parámetro servicioVolcadoOficinasURL.
	 * 
	 * @return servicioVolcadoOficinasURL valor del campo a obtener.
	 */
	public String getServicioVolcadoOficinasURL() {
		return servicioVolcadoOficinasURL;
	}

	/**
	 * Guarda el valor del parámetro servicioVolcadoOficinasURL.
	 * 
	 * @param servicioVolcadoOficinasURL
	 *            valor del campo a guardar.
	 */
	public void
			setServicioVolcadoOficinasURL(String servicioVolcadoOficinasURL) {
		this.servicioVolcadoOficinasURL = servicioVolcadoOficinasURL;
	}

	/**
	 * Obtiene el valor del parámetro tempFilesDir.
	 * 
	 * @return tempFilesDir valor del campo a obtener.
	 */
	public String getTempFilesDir() {
		return tempFilesDir;
	}

	/**
	 * Guarda el valor del parámetro tempFilesDir.
	 * 
	 * @param tempFilesDir
	 *            valor del campo a guardar.
	 */
	public void setTempFilesDir(String tempFilesDir) {
		this.tempFilesDir = tempFilesDir;
	}

	/**
	 * Obtiene el valor del parámetro servicioVolcadoUnidades.
	 * 
	 * @return servicioVolcadoUnidades valor del campo a obtener.
	 */
	public SC01UNVolcadoDatosBasicosService getServicioVolcadoUnidades() {
		return servicioVolcadoUnidades;
	}

	/**
	 * Guarda el valor del parámetro servicioVolcadoUnidades.
	 * 
	 * @param servicioVolcadoUnidades
	 *            valor del campo a guardar.
	 */
	public void setServicioVolcadoUnidades(
			SC01UNVolcadoDatosBasicosService servicioVolcadoUnidades) {
		this.servicioVolcadoUnidades = servicioVolcadoUnidades;
	}

	/**
	 * Obtiene el valor del parámetro servicioVolcadoOficinas.
	 * 
	 * @return servicioVolcadoOficinas valor del campo a obtener.
	 */
	public SC11OFVolcadoDatosBasicosService getServicioVolcadoOficinas() {
		return servicioVolcadoOficinas;
	}

	/**
	 * Guarda el valor del parámetro servicioVolcadoOficinas.
	 * 
	 * @param servicioVolcadoOficinas
	 *            valor del campo a guardar.
	 */
	public void setServicioVolcadoOficinas(
			SC11OFVolcadoDatosBasicosService servicioVolcadoOficinas) {
		this.servicioVolcadoOficinas = servicioVolcadoOficinas;
	}

	/**
	 * Obtiene el valor del parámetro login.
	 * 
	 * @return login valor del campo a obtener.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Guarda el valor del parámetro login.
	 * 
	 * @param login
	 *            valor del campo a guardar.
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Obtiene el valor del parámetro pass.
	 * 
	 * @return pass valor del campo a obtener.
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * Guarda el valor del parámetro pass.
	 * 
	 * @param pass
	 *            valor del campo a guardar.
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * Obtiene el valor del parámetro fileFormat.
	 * 
	 * @return fileFormat valor del campo a obtener.
	 */
	public String getFileFormat() {
		return fileFormat;
	}

	/**
	 * Guarda el valor del parámetro fileFormat.
	 * 
	 * @param fileFormat
	 *            valor del campo a guardar.
	 */
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * Obtiene el valor del parámetro oficinasQueryType.
	 * 
	 * @return oficinasQueryType valor del campo a obtener.
	 */
	public String getOficinasQueryType() {
		return oficinasQueryType;
	}

	/**
	 * Guarda el valor del parámetro oficinasQueryType.
	 * 
	 * @param oficinasQueryType
	 *            valor del campo a guardar.
	 */
	public void setOficinasQueryType(String oficinasQueryType) {
		this.oficinasQueryType = oficinasQueryType;
	}

	/**
	 * Obtiene el valor del parámetro unidadesQueryType.
	 * 
	 * @return unidadesQueryType valor del campo a obtener.
	 */
	public String getUnidadesQueryType() {
		return unidadesQueryType;
	}

	/**
	 * Guarda el valor del parámetro unidadesQueryType.
	 * 
	 * @param unidadesQueryType
	 *            valor del campo a guardar.
	 */
	public void setUnidadesQueryType(String unidadesQueryType) {
		this.unidadesQueryType = unidadesQueryType;
	}
	
}

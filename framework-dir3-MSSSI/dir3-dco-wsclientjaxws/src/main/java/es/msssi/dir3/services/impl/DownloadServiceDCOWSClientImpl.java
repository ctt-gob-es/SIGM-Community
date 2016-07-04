/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipException;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import es.map.directorio.manager.impl.SD01UNDescargaUnidadesService;
import es.map.directorio.manager.impl.SD02OFDescargaOficinasService;
import es.map.directorio.manager.impl.wsexport.FormatoFichero;
import es.map.directorio.manager.impl.wsexport.OficinasWs;
import es.map.directorio.manager.impl.wsexport.RespuestaWS;
import es.map.directorio.manager.impl.wsexport.TipoConsultaOF;
import es.map.directorio.manager.impl.wsexport.TipoConsultaUO;
import es.map.directorio.manager.impl.wsexport.UnidadesWs;
import es.map.directorio.ws.manager.impl.SD03NODescargaUnNoOrganicasService;
import es.map.directorio.ws.manager.impl.wsexport.UnNoOrganicasWs;
import es.msssi.dir3.exception.GetFileDownloadDCOException;
import es.msssi.dir3.services.DownloadServiceDCO;
import es.msssi.dir3.util.Base64Utils;

/**
 * Implementación del servicio de obtención del fichero de actualización
 * mediante los WS del DCO.
 * 
 * @author cmorenog.
 */
public class DownloadServiceDCOWSClientImpl implements DownloadServiceDCO, Serializable {
/**
     * 
     */
    private static final long serialVersionUID = -8123470569432193562L;

/**
 * 
 */
    private static final Logger LOG = Logger.getLogger(DownloadServiceDCOWSClientImpl.class);

    /**
     * Servicio para llamar al WS SD01UNDescargaUnidades - Actualización de
     * Unidades Organicas.
     */
    protected SD01UNDescargaUnidadesService downloadServiceUnits;

    /**
     * URL Servicio para llamar al WS SD01UNDescargaUnidades - Actualización de
     * Unidades Organicas.
     */
    protected String downloadServiceUnitsURL;

    /**
     * Servicio para llamar al WS SD02OFDescargaOficinas - Actualización de
     * Oficinas.
     */
    protected SD02OFDescargaOficinasService downloadServiceOffices;

    /**
     * URL Servicio para llamar al WS SD02OFDescargaOficinas - Actualización de
     * Oficinas.
     */
    protected String downloadServiceOfficesURL;

    /**
     * Directorio para dejar los ficheros.
     */
    protected String tempFilesDir;
    /**
     * Login del servicio del DCO proporcionado por el ministerio.
     */
    protected String login;
    /**
     * Password del servicio del DCO proporcionada por el ministerio.
     */
    protected String pass;
    /**
     * Formato en el que obtener las actualizaciones.
     */
    protected String fileFormat;
    /**
     * Tipo de consulta para las oficinas.
     */
    protected String oficinasQueryType;
    /**
     * Tipo de consulta para las unidades organicas.
     */
    protected String unidadesQueryType;
   
    /**
     * Servicio para llamar al WS SD03NO_DescargaUnNoOrganicas - Actualización de
     * Unidades no Organicas.
     */
    protected SD03NODescargaUnNoOrganicasService downloadServiceNotOrgUnits;

    /**
     * URL Servicio para llamar al WS SD03NO_DescargaUnNoOrganicas - Actualización de
     * Unidades no Organicas.
     */
    protected String downloadServiceNotOrgUnitsURL;

    /**
     * Contructor por defecto que carga la configuracion del servicio.
     * 
     * @throws MalformedURLException.
     */
    public void initServices()
	throws MalformedURLException {
	LOG.info("Inicializamos servicios");
	LOG.info("Servicio de descarga de unidades:" +
	    downloadServiceUnitsURL);
	if (downloadServiceUnits == null) {
	    URL wsdlLocationIU = new URL(
		downloadServiceUnitsURL);
	    QName serviceNameIU = new QName(
		"http://impl.manager.directorio.map.es", "SD01UN_DescargaUnidadesService");
	    downloadServiceUnits = new SD01UNDescargaUnidadesService(
		wsdlLocationIU, serviceNameIU);
	}
	LOG.info("Servicio de descarga de oficinas:" +
	    downloadServiceOfficesURL);
	if (downloadServiceOffices == null) {
	    URL wsdlLocationVOF = new URL(
		downloadServiceOfficesURL);
	    QName serviceNameVOF = new QName(
		"http://impl.manager.directorio.map.es", "SD02OF_DescargaOficinasService");
	    downloadServiceOffices = new SD02OFDescargaOficinasService(
		wsdlLocationVOF, serviceNameVOF);
	}
	LOG.info("Servicio de descarga de unidades no organicas:" +
		downloadServiceNotOrgUnitsURL);
		if (downloadServiceNotOrgUnits == null) {
		    URL wsdlLocationIU = new URL(
			    downloadServiceNotOrgUnitsURL);
		    QName serviceNameIU = new QName(
			"http://impl.manager.ws.directorio.map.es", "SD03NO_DescargaUnNoOrganicasService");
		    downloadServiceNotOrgUnits = new SD03NODescargaUnNoOrganicasService(
			wsdlLocationIU, serviceNameIU);
		}
    }

    /**
     * Obtiene el fichero para actualizar las oficinas. Retorna el path al
     * fichero xml.
     * 
     * @param dateLastUpdate
     *            Fecha de última actualización.
     * @return nombre del fichero de actualización de Oficinas.
     * @throws GetFileDownloadDCOException.
     * 
     */
    public String getFileUpdateOfficesDCO(
	Date dateLastUpdate)
	throws GetFileDownloadDCOException {
	String nombre = null;
	LOG.info("Init getFileUpdateOfficesDCO");
	try {
	    initServices();
	    if (null == dateLastUpdate) {
		LOG.error("ServicioActualizacionDCOWSClientImpl::"
		    + "getFicheroActualizarOficinasDCO "
		    + "- Error la fecha de última actualización es nula");
		throw new Exception(
		    "No hay fecha de última actualización");
	    }

	    SimpleDateFormat dateFormat = new SimpleDateFormat(
		"dd/MM/yyyy");
	    LOG.info("Fichero temporal:" +
		    getTempFilesDir());
	    File tempZipFile = File.createTempFile(
		"ofic", ".zip", new File(
		    getTempFilesDir()));
	    LOG.info("Fichero temporal:" +
		tempZipFile.getAbsolutePath());
	    OficinasWs oficinaWs = new OficinasWs();
	    oficinaWs.setClave(getPass());
	    oficinaWs.setUsuario(getLogin());
	    oficinaWs.setTipoConsulta(TipoConsultaOF.fromValue(getOficinasQueryType()));
	    oficinaWs.setFormatoFichero(FormatoFichero.fromValue(getFileFormat()));
	    oficinaWs.setFechaInicio(dateFormat.format(dateLastUpdate));
	    oficinaWs.setFechaFin(dateFormat.format(Calendar.getInstance().getTime()));

	    RespuestaWS respuesta =
		getDownloadServiceOffices().getSD02OFDescargaOficinas().exportar(
		    oficinaWs);
	    LOG.info("Respuesta del servicio :" +
		respuesta != null
		? respuesta.getCodigo() : "null");
	    if (respuesta != null &&
		"01".equals(respuesta.getCodigo())) {
		Base64Utils.getInstance().decodeToFile(
		    respuesta.getFichero(), tempZipFile.getAbsolutePath());
		nombre = tempZipFile.getName();
		LOG.info("Nombre del fichero de actualización de oficinas :" +
		    nombre);
	    }
	    LOG.info("Fin getFileUpdateOfficesDCO");
	    return nombre;
	}
	catch (ZipException zipEx) {
	    LOG.error(
		"ServicioDescargaDCOWSClientImpl::getFicheroDescargaOficinasDCO - "
		    + "Error al descomprimir el fichero retornado por el DCO.", zipEx);
	    throw new GetFileDownloadDCOException(
		zipEx.getMessage());
	}
	catch (IOException ioEx) {
	    LOG.error(
		"ServicioDescargaDCOWSClientImpl::getFicheroDescargaOficinasDCO "
		    + "- Error al crear los ficheros temporales.", ioEx);
	    throw new GetFileDownloadDCOException(
		ioEx.getMessage());
	}
	catch (Exception e) {
	    LOG.error(
		"Error inesperado", e);
	    throw new GetFileDownloadDCOException(
		e.getMessage());
	}
    }

    /**
     * Retorna el path al fichero xml.
     * 
     * @param fechaUltimaActualizacion
     *            Fecha de última actualización.
     * @return nombre del fichero de actualización de organismos.
     * @throws GetFileDownloadDCOException.
     */
    public String getFileUpdateUnitsDCO(
	Date dateLastUpdate)
	throws GetFileDownloadDCOException {
	String nombre = null;
	try {
	    LOG.info("Init getFileUpdateUnitsDCO");
	    initServices();
	    if (null == dateLastUpdate) {
		LOG.error("ServicioDescargaDCOWSClientImpl::"
		    + "getFicheroActualizarUnidadesDCO "
		    + "- Error la fecha de última actualización es nula");
		throw new Exception(
		    "No hay fecha de última actualización");
	    }

	    SimpleDateFormat dateFormat = new SimpleDateFormat(
		"dd/MM/yyyy");
	    File tempZipFile = File.createTempFile(
		"unid", ".zip", new File(
		    getTempFilesDir()));
	    LOG.info("Fichero temporal:" +
		tempZipFile.getAbsolutePath());
	    UnidadesWs unidadesWs = new UnidadesWs();
	    unidadesWs.setUsuario(getLogin());
	    unidadesWs.setClave(getPass());
	    unidadesWs.setFormatoFichero(FormatoFichero.fromValue(getFileFormat()));
	    unidadesWs.setTipoConsulta(TipoConsultaUO.fromValue(getOficinasQueryType()));
	    unidadesWs.setFechaInicio(dateFormat.format(dateLastUpdate));
	    unidadesWs.setFechaFin(dateFormat.format(Calendar.getInstance().getTime()));

	    RespuestaWS respuesta = getDownloadServiceUnits().getSD01UNDescargaUnidades().exportar(
		unidadesWs);
	    LOG.info("Respuesta del servicio :" +
		respuesta != null
		? respuesta.getCodigo() : "null");
	    if (respuesta != null &&
		"01".equals(respuesta.getCodigo())) {
		Base64Utils.getInstance().decodeToFile(
		    respuesta.getFichero(), tempZipFile.getAbsolutePath());

		nombre = tempZipFile.getName();
		
		LOG.info("Nombre del fichero de actualización de unidades :" +
		    nombre);
	    }
	    LOG.info("Fin getFileUpdateUnitsDCO");
	    return nombre;
	}
	catch (ZipException zipEx) {
	    LOG.error(
		"ServicioDescargaDCOWSClientImpl::getFicheroDescargaUnidadesDCO "
		    + "- Error al descomprimir el fichero retornado por el DCO.", zipEx);
	    throw new GetFileDownloadDCOException(
		zipEx.getMessage());
	}
	catch (IOException ioEx) {
	    LOG.error(
		"ServicioDescargaDCOWSClientImpl::getFicheroDescargaUnidadesDCO "
		    + "- Error al crear los ficheros temporales.", ioEx);
	    throw new GetFileDownloadDCOException(
		ioEx.getMessage());
	}
	catch (Exception e) {
	    LOG.error(
		"Error inesperado", e);
	    throw new GetFileDownloadDCOException(
		e.getMessage());
	}

    }

    /**
     * Retorna el path al fichero xml.
     * 
     * @param fechaUltimaActualizacion
     *            Fecha de última actualización.
     * @return nombre del fichero de actualización de organismos.
     * @throws GetFileDownloadDCOException.
     */
    public String getFileUpdateNotOrgUnitsDCO(
	Date dateLastUpdate)
	throws GetFileDownloadDCOException {
	String nombre = null;
	try {
	    LOG.info("Init getFileUpdateNotOrgUnitsDCO");
	    initServices();
	    if (null == dateLastUpdate) {
		LOG.error("ServicioDescargaDCOWSClientImpl::"
		    + "getFicheroActualizarUnidadesNoOrgDCO "
		    + "- Error la fecha de última actualización es nula");
		throw new Exception(
		    "No hay fecha de última actualización");
	    }

	    SimpleDateFormat dateFormat = new SimpleDateFormat(
		"dd/MM/yyyy");
	    File tempZipFile = File.createTempFile(
		"unidnoorg", ".zip", new File(
		    getTempFilesDir()));
	    LOG.info("Fichero temporal:" +
		tempZipFile.getAbsolutePath());
	    UnNoOrganicasWs unidadesNoOrgWs = new UnNoOrganicasWs();
	    unidadesNoOrgWs.setUsuario(getLogin());
	    unidadesNoOrgWs.setClave(getPass());
	    unidadesNoOrgWs.setFormatoFichero(
		    es.map.directorio.ws.manager.impl.wsexport.FormatoFichero.fromValue(getFileFormat()));
	    unidadesNoOrgWs.setTipoConsulta(
		    es.map.directorio.ws.manager.impl.wsexport.TipoConsultaNO.fromValue(getOficinasQueryType()));
	    unidadesNoOrgWs.setFechaInicio(dateFormat.format(dateLastUpdate));
	    unidadesNoOrgWs.setFechaFin(dateFormat.format(Calendar.getInstance().getTime()));

	    es.map.directorio.ws.manager.impl.wsexport.RespuestaWS respuesta = 
		    getDownloadServiceNotOrgUnits().getSD03NODescargaUnNoOrganicas().exportar(
		    unidadesNoOrgWs);
	    LOG.info("Respuesta del servicio :" +
		respuesta != null
		? respuesta.getCodigo() : "null");
	    if (respuesta != null &&
		"01".equals(respuesta.getCodigo())) {
		Base64Utils.getInstance().decodeToFile(
		    respuesta.getFichero(), tempZipFile.getAbsolutePath());
		nombre = tempZipFile.getName();
		LOG.info("Nombre del fichero de actualización de unidades no organicas:" +
		    nombre);
	    }
	    LOG.info("Fin getFileUpdateNotOrgUnitsDCO");
	    return nombre;
	}
	catch (ZipException zipEx) {
	    LOG.error(
		"ServicioDescargaDCOWSClientImpl::getFileUpdateNotOrgUnitsDCO "
		    + "- Error al descomprimir el fichero retornado por el DCO.", zipEx);
	    throw new GetFileDownloadDCOException(
		zipEx.getMessage());
	}
	catch (IOException ioEx) {
	    LOG.error(
		"ServicioDescargaDCOWSClientImpl::getFileUpdateNotOrgUnitsDCO "
		    + "- Error al crear los ficheros temporales.", ioEx);
	    throw new GetFileDownloadDCOException(
		ioEx.getMessage());
	}
	catch (Exception e) {
	    LOG.error(
		"Error inesperado", e);
	    throw new GetFileDownloadDCOException(
		e.getMessage());
	}

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
     * Obtiene el valor del parámetro servicioDescargaUnidades.
     * 
     * @return downloadServiceUnits valor del campo a obtener.
     */
    public SD01UNDescargaUnidadesService getDownloadServiceUnits() {
	return downloadServiceUnits;
    }

    /**
     * Guarda el valor del parámetro servicioDescargaUnidades.
     * 
     * @param downloadServiceUnits
     *            valor del campo a guardar.
     */
    public void setDownloadServiceUnits(
	SD01UNDescargaUnidadesService downloadServiceUnits) {
	this.downloadServiceUnits = downloadServiceUnits;
    }

    /**
     * Obtiene el valor del parámetro servicioDescargaUnidadesURL.
     * 
     * @return downloadServiceUnitsURL valor del campo a obtener.
     */
    public String getDownloadServiceUnitsURL() {
	return downloadServiceUnitsURL;
    }

    /**
     * Guarda el valor del parámetro servicioDescargaUnidadesURL.
     * 
     * @param downloadServiceUnitsURL
     *            valor del campo a guardar.
     */
    public void setDownloadServiceUnitsURL(
	String downloadServiceUnitsURL) {
	this.downloadServiceUnitsURL = downloadServiceUnitsURL;
    }

    /**
     * Obtiene el valor del parámetro servicioDescargaOficinas.
     * 
     * @return downloadServiceOffices valor del campo a obtener.
     */
    public SD02OFDescargaOficinasService getDownloadServiceOffices() {
	return downloadServiceOffices;
    }

    /**
     * Guarda el valor del parámetro servicioDescargaOficinas.
     * 
     * @param servicioDescargaOficinas
     *            valor del campo a guardar.
     */
    public void setDownloadServiceOffices(
	SD02OFDescargaOficinasService downloadServiceOffices) {
	this.downloadServiceOffices = downloadServiceOffices;
    }

    /**
     * Obtiene el valor del parámetro servicioDescargaOficinasURL.
     * 
     * @return downloadServiceOfficesURL valor del campo a obtener.
     */
    public String getDownloadServiceOfficesURL() {
	return downloadServiceOfficesURL;
    }

    /**
     * Guarda el valor del parámetro servicioDescargaOficinasURL.
     * 
     * @param downloadServiceOfficesURL
     *            valor del campo a guardar.
     */
    public void setDownloadServiceOfficesURL(
	String downloadServiceOfficesURL) {
	this.downloadServiceOfficesURL = downloadServiceOfficesURL;
    }

    /**
     * Guarda el valor del parámetro tempFilesDir.
     * 
     * @param tempFilesDir
     *            valor del campo a guardar.
     */
    public void setTempFilesDir(
	String tempFilesDir) {
	this.tempFilesDir = tempFilesDir;
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
    public void setLogin(
	String login) {
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
    public void setPass(
	String pass) {
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
    public void setFileFormat(
	String fileFormat) {
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
    public void setOficinasQueryType(
	String oficinasQueryType) {
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
    public void setUnidadesQueryType(
	String unidadesQueryType) {
	this.unidadesQueryType = unidadesQueryType;
    }
    /**
     * Obtiene el valor del parámetro downloadServiceNotOrgUnits.
     * 
     * @return downloadServiceNotOrgUnits valor del campo a obtener.
     */
    public SD03NODescargaUnNoOrganicasService getDownloadServiceNotOrgUnits() {
        return downloadServiceNotOrgUnits;
    }
    /**
     * Guarda el valor del parámetro downloadServiceNotOrgUnits.
     * 
     * @param downloadServiceNotOrgUnits
     *            valor del campo a guardar.
     */
    public void setDownloadServiceNotOrgUnits(
    	SD03NODescargaUnNoOrganicasService downloadServiceNotOrgUnits) {
        this.downloadServiceNotOrgUnits = downloadServiceNotOrgUnits;
    }
    /**
     * Obtiene el valor del parámetro downloadServiceNotOrgUnitsURL.
     * 
     * @return downloadServiceNotOrgUnitsURL valor del campo a obtener.
     */
    public String getDownloadServiceNotOrgUnitsURL() {
        return downloadServiceNotOrgUnitsURL;
    }
    /**
     * Guarda el valor del parámetro downloadServiceNotOrgUnitsURL.
     * 
     * @param downloadServiceNotOrgUnitsURL
     *            valor del campo a guardar.
     */
    public void setDownloadServiceNotOrgUnitsURL(String downloadServiceNotOrgUnitsURL) {
        this.downloadServiceNotOrgUnitsURL = downloadServiceNotOrgUnitsURL;
    }

}

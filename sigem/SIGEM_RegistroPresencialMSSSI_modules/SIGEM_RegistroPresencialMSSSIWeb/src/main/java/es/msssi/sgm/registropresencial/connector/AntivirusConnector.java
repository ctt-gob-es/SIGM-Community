package es.msssi.sgm.registropresencial.connector;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import es.msssi.sgm.registropresencial.connector.antivirus.beans.AntivirusPortType;
import es.msssi.sgm.registropresencial.connector.antivirus.beans.AntivirusServices;
import es.msssi.sgm.registropresencial.connector.antivirus.beans.Resultado;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;


@Service
public class AntivirusConnector {
	 
    private static final Logger LOGGER_APP = Logger.getLogger(AntivirusConnector.class);
    private String FORMATO_FECHA_ARCHIVO = "yyyyMMddHHmmssSSS";
    private int ANTIVIRUS_ESTADO_OK = 0;

    private AntivirusServices servicioAntivirus;

    private String idAplicacion;

    public void setIdAplicacion(String idAplicacion) {
	this.idAplicacion = idAplicacion;
    }

    public void setServicioAntivirus(AntivirusServices servicioAntivirus) {
	this.servicioAntivirus = servicioAntivirus;
    }

    public AntivirusPortType getPort() {
	return servicioAntivirus.getAntivirusPortTypeSoapPort();
    }

    public void scan(String nombreFichero, byte[] byteArrayfile) throws RPGenericException, RPRegisterException {
	String ficheroBase64 = null;
	String filename =
		new SimpleDateFormat(FORMATO_FECHA_ARCHIVO).format(new Date()) + "-"
			+ nombreFichero;
	String peticionString = getPeticionString(idAplicacion, filename);
	ficheroBase64 = Base64.encodeBase64String(byteArrayfile);
	Resultado resultado = null;
	String response = null;
	try {
	    LOGGER_APP.info("Llamando al servicio web antivirus...(fichero: " + filename + ")");
	    response = getPort().scanBase64(peticionString, ficheroBase64);
	    LOGGER_APP.info("Respuesta al servicio web antivirus...(fichero: " + filename + ")");

	}
	catch (Exception e) {
	    LOGGER_APP.error(ExceptionUtils.getStackTrace(e));
	    throw new RPRegisterException(RPRegisterErrorCode.SCAN_FILE_ANTIVIRUS_CALL_ERROR,
		ErrorConstants.SCAN_FILE_ANTIVIRUS_CALL_ERROR_MESSAGE, e);
	}

	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance(Resultado.class);

	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    resultado = (Resultado) jaxbUnmarshaller.unmarshal(new StringReader(response));
	}
	catch (JAXBException e) {
	    LOGGER_APP.error(ExceptionUtils.getStackTrace(e));
	    throw new RPGenericException(RPGenericErrorCode.GET_PROPERTY_ERROR, e);
	}
	
	if (resultado != null && resultado.getEstado().getCodEstado() != ANTIVIRUS_ESTADO_OK) {
	    LOGGER_APP.error(ErrorConstants.SCAN_FILE_WITH_VIRUS_ERROR_MESSAGE);
	    throw new RPGenericException(RPGenericErrorCode.SCAN_FILE_WITH_VIRUS_ERROR,
		    ErrorConstants.SCAN_FILE_WITH_VIRUS_ERROR_MESSAGE);	    
	    
	    
	}

    }

    private static String getPeticionString(String appId, String fileName) {
	StringBuffer xmlPeticion = new StringBuffer();
	xmlPeticion.append("<peticion xmlns=\"http://xml.antivirus.msps/Peticion\">");
	xmlPeticion.append("<acceso>");
	xmlPeticion.append("<id_aplicacion>" + appId + "</id_aplicacion>");
	xmlPeticion.append("</acceso>");
	xmlPeticion.append("<fichero>");
	xmlPeticion.append("<nombre>" + fileName + "</nombre>");
	xmlPeticion.append("</fichero>");
	xmlPeticion.append("</peticion>");
	return xmlPeticion.toString();
    }
 

	
}

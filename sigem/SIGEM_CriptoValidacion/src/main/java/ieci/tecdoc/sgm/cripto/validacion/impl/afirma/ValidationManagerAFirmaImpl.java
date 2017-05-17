package ieci.tecdoc.sgm.cripto.validacion.impl.afirma;

import ieci.tecdoc.sgm.core.config.impl.spring.MultiEntityContextHolder;
import ieci.tecdoc.sgm.core.services.cripto.firma.ResultadoValidacionFirma;
import ieci.tecdoc.sgm.core.services.cripto.validacion.CriptoValidacionException;
import ieci.tecdoc.sgm.core.services.cripto.validacion.InfoCertificado;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ResultadoValidacion;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ServicioCriptoValidacion;
import ieci.tecdoc.sgm.cripto.validacion.impl.afirma.config.AFirmaValidacionConfiguration;
import ieci.tecdoc.sgm.cripto.validacion.impl.afirma.xml.Codigo;
import ieci.tecdoc.sgm.cripto.validacion.impl.afirma.xml.InformacionError;
import ieci.tecdoc.sgm.cripto.validacion.impl.afirma.xml.ResultadoValidarCertificado;
import ieci.tecdoc.sgm.cripto.validacion.impl.afirma.xml.Tipo;
import ieci.tecdoc.sgm.cripto.validacion.impl.afirma.xml.ValidarCertificado;
import ieci.tecdoc.sgm.cripto.validacion.impl.afirma.xml.Version;
import ieci.tecdoc.sgm.cripto.validacion.impl.config.ConfigValidacionLoader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.tsaServiceInvoker.ws.DSSServicesManager;
import es.gob.afirma.utils.UtilsBase64;


public class ValidationManagerAFirmaImpl implements ServicioCriptoValidacion {

	private Map configuracionesPorEntidad = new HashMap();

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ValidationManagerAFirmaImpl.class);

    public String createHash(String psBase64Document) throws CriptoValidacionException {

		try {
			AFirmaValidacionConfiguration config = loadConfig();
		    byte[]res = crearResumenReal(config.getAlgorithm(), config.getProvider(), psBase64Document);
		    //BASE64Encoder encoder = new BASE64Encoder();
		    //String b641 = encoder.encodeBuffer(res);
		    String b64 = UtilsBase64.encodeBytes(res);		    
		    return b64;
		} catch (Exception e) {
		    log.error("Error al crear hash de documento [createHash] [Exception]", e.fillInStackTrace());
		    throw new CriptoValidacionException(CriptoValidacionException.EXC_GENERIC_EXCEPCION, e.getMessage(), e);
	    }
	}

	public ResultadoValidacion validateSignature(String psB64Firma)	throws CriptoValidacionException {
		
		//[DipuCR-Agustin #341] Validar firma
		// Hasta que no se retorne la peticion la validacion se contempla erronea
		ResultadoValidacion oResult = new ResultadoValidacion();
		Map<String, Object> propertiesResult = null;
		oResult.setResultadoValidacion(ResultadoValidacion.VALIDACION_ERROR);
		
		DSSServicesManager dsssm = new DSSServicesManager();
    	
    	try {
			
    		propertiesResult = dsssm.doDSSAfirmaVerify(psB64Firma);
    		oResult.setResultadoValidacion(ResultadoValidacion.VALIDACION_OK);  
    		oResult.setPropertiesResult(propertiesResult);
		} catch (Exception e) {
		    log.error("Error al validar la firma del formulario, el certificado de firma no es válido [validateCertificate][Exception]", e.fillInStackTrace());
		    throw new CriptoValidacionException(CriptoValidacionException.EXC_GENERIC_EXCEPCION, e.getMessage(), e);
		}		 
		  	
		return oResult;
	}
	public ResultadoValidacion validateCertificate(String psB64Certificate)	throws CriptoValidacionException {

		//[DipuCR-Agustin #341] Validar certificado		
		// Hasta que no se retorne la peticion la validacion se contempla erronea
		ResultadoValidacion oResult = new ResultadoValidacion();
		Map<String, Object> propertiesResult = null;
		oResult.setResultadoValidacion(ResultadoValidacion.VALIDACION_ERROR);
		InfoCertificado ic = new InfoCertificado();
		
		DSSServicesManager dsssm = new DSSServicesManager();
    	
    	try {
			
propertiesResult = dsssm.doDSSAfirmaVerifyCertificate(psB64Certificate);
    		oResult.setResultadoValidacion(ResultadoValidacion.VALIDACION_OK); 
    		oResult.setPropertiesResult(propertiesResult);    		
    		
		} catch (Exception e) {
		    log.error("Error al validar certificado de usuario [validateCertificate][Exception]", e.fillInStackTrace());
		    throw new CriptoValidacionException(CriptoValidacionException.EXC_GENERIC_EXCEPCION, e.getMessage(), e);
		}		 
		  	
		return getResultadoValidacionServicio(oResult);
	}
    	

	public boolean validateHash(String psBase64Document, String psB64Hash) throws CriptoValidacionException {

		try {
			AFirmaValidacionConfiguration config = loadConfig();
		    String resumenDoc = new String(crearResumenReal(config.getAlgorithm(), config.getProvider(), psBase64Document));
		    if (resumenDoc.equals(psB64Hash)) {
		    	return true;
		    } else {
		    	return false;
		    }
		} catch (Exception e) {
		    log.error("Error al validar hash de documento [validateHash] [Exception]", e.fillInStackTrace());
		    throw new CriptoValidacionException(CriptoValidacionException.EXC_GENERIC_EXCEPCION, e.getMessage(), e);
	    }
	}

    private byte[] crearResumenReal(String tipo, String provider, String documento) throws NoSuchAlgorithmException, NoSuchProviderException,CriptoValidacionException {

    	MessageDigest md=MessageDigest.getInstance(tipo, provider);
    	md.update(documento.getBytes());
    	return md.digest();
    }

	private ResultadoValidacion getResultadoValidacionServicio(ResultadoValidacion oResult) throws CriptoValidacionException{

		try {
		
			Map<String, String> certificateInfo = (Map<String, String>) oResult.getPropertiesResult().get(TransformersFacade.getInstance().getParserParameterValue("CertificateInfo"));
			
			// Procesar la respuesta	
			InfoCertificado oInfo = new InfoCertificado();
			
			oInfo.setCif(certificateInfo.get("NIFResponsable"));
			oInfo.setCorporateName(certificateInfo.get("NombreApellidosResponsable"));
			oInfo.setName(certificateInfo.get("NombreApellidosResponsable"));
			oInfo.setFirstname(certificateInfo.get("nombreResponsable"));
			oInfo.setIssuer(certificateInfo.get("OrganizacionEmisora"));;
			oInfo.setLastname1(certificateInfo.get("primerApellidoResponsable"));
			oInfo.setLastname2(certificateInfo.get("segundoApellidoResponsable"));
			oInfo.setNif(certificateInfo.get("NIFResponsable"));
			oInfo.setSerialNumber(certificateInfo.get("numeroSerie"));
			oInfo.setSubject(certificateInfo.get("subject"));		
			
			oResult.setCertificado(oInfo);
		
		} catch (Exception e) {
		    log.error("Error al obtener datos del certificado [validateCertificate][Exception]", e.fillInStackTrace());
		    throw new CriptoValidacionException(CriptoValidacionException.EXC_GENERIC_EXCEPCION, e.getMessage(), e);
		}	
		
		return oResult;
		
		
	}

	public AFirmaValidacionConfiguration loadConfig() throws CriptoValidacionException {

		String idEntidad = MultiEntityContextHolder.getEntity();

		// Cacheo de configuraciones
		AFirmaValidacionConfiguration config = (AFirmaValidacionConfiguration)configuracionesPorEntidad.get(idEntidad);
		if(config == null) {

			ConfigValidacionLoader configLoader = new ConfigValidacionLoader();
			config = configLoader.loadAFirmaConfiguration(idEntidad);
			configuracionesPorEntidad.put(idEntidad, config);
		}

		return config;
	}

}

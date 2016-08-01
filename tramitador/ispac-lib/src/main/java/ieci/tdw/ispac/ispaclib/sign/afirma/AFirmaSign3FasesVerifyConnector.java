package ieci.tdw.ispac.ispaclib.sign.afirma;


import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.messages.MessagesFormatter;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.sign.DefaultSignConnector;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.sign.exception.InvalidSignatureValidationException;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.cert.CertStore;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.util.encoders.Base64;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import es.dipucr.sigem.api.firma.xml.peticion.ObjectFactoryFirmaLotesPeticion;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;

/**
 * Conector de firma con @firma.
 *
 */
public class AFirmaSign3FasesVerifyConnector extends DefaultSignConnector {

	/**
	 * Ruta por defecto de la imagen de fondo del PDF.
	 */
	private static final String DEFAULT_PDF_BG_IMAGE_PATH = "firma/fondo.gif";
	
	/**
	 * Ruta por defecto del fichero con el texto de la banda lateral del PDF.
	 */
	private static final String DEFAULT_PDF_BG_DATA_PATH = "firma/datosFirma.gif";
	
	//Propiedades de configuración para el aspecto de la banda gris con la información del firmante incrustada en el pdf
	public final static String MARGIN_BAND 			= "MARGIN_BAND";
	public final static String POSITIONY_BAND		= "POSITIONY_BAND";
	public final static String FONT_BAND			= "FONT_BAND";
	public final static String ENCODING_BAND		= "ENCODING_BAND";
	public final static String FONTSIZE_BAND 		= "FONTSIZE_BAND";
	public final static String BAND					= "BAND";
	public final static String SIZE_BAND			= "SIZE_BAND";
	public final static String SIZE_INICIAL_BAND	="SIZE_INICIAL_BAND";
	public final static String SIZE_OTHER_BAND		="SIZE_OTHER_BAND";
	public final static String PATH_IMAGEN_BAND		="PATH_IMAGE_BAND";
	public final static String PATH_TEXT			="PATH_TEXT";
	
	/**
	 * Constante para el salto de línea en JavaScript.
	 */
	protected static final String CR = "\n";
	
	/** 
	 * Nombre del fichero de recursos. 
	 */
	private static String BUNDLE_NAME = "ieci.tdw.ispac.ispaclib.sign.afirma.AFirmaSignConnectorMessages";
	
	
	/**
	 * Realiza una validación de una firma o un hash.
	 * 
	 * @param signatureValue Valor de la firma
	 * @param signedContentB64 Contenido firmado en base 64
	 * @throws InvalidSignatureValidationException
	 *             Si la firma no es válida.
	 * @return Detalles de la validación.
	 * 
	 */
	public  Map verify(String signatureValue, String signedContentB64) {
		boolean firmaVerificada=false;
		//byte [] signPk7 = Base64.decode(signatureValue);
		
		CMSSignedData signature;
		Map result = new HashMap();
		
		try {
			
			byte signedContent [] = Base64.decode(signedContentB64.getBytes());
			CMSProcessableByteArray signedContentCMS = new CMSProcessableByteArray(signedContent);
			byte [] signPk7 = Base64.decode(signatureValue);
			signature = new CMSSignedData(signedContentCMS, new ByteArrayInputStream(signPk7));
			SignerInformation signer = (SignerInformation) signature.getSignerInfos().getSigners().iterator().next();
			CertStore cs = signature.getCertificatesAndCRLs("Collection", "BC");
			Iterator iter = cs.getCertificates(signer.getSID()).iterator();
			X509Certificate  certificate = (X509Certificate) iter.next();
			result=getDetailsSign(signature, certificate);
			// Comprobar integridad de la firma
			firmaVerificada = signer.verify(certificate, "BC");
	
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			result.put(ISignAPI.INTEGRIDAD, ISignAPI.INTEGRIDAD_ERROR);
		}
		if(firmaVerificada){
		result.put(ISignAPI.INTEGRIDAD, ISignAPI.INTEGRIDAD_OK);
		}
		else{
			result.put(ISignAPI.INTEGRIDAD, ISignAPI.INTEGRIDAD_STRANGER);
		}
		return result;
	}
		
	
	/**
	 * Obtiene el código HTML para incluir en la página.
	 * @param locale Información del idioma del cliente.
	 * @param baseURL URL base.
	 * @param hashCode Código HASH del documento a firmar.
	 * @return Código HTML.
	 * @throws ISPACException si ocurre algún error.
	 */		
	public String getHTMLCode(Locale locale, String baseURL, String hashCode) throws ISPACException {
		
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		String entityId = info.getOrganizationId();
		FirmaConfiguration fc = FirmaConfiguration.getInstanceNoSingleton(entityId);
		String host = fc.getProperty("firmar.ip.https");
		
		String head = "";
		
		//[DipuCR-Agustin #94] modifico las llamadas al servidor de firma para firma 3 fases por lotes
		
		head += "<script type=\"text/javascript\" language=\"javascript\">" + CR;		
		head += "//var path=\"" + baseURL + "/ispac/applets\";" + CR;

		/*
			Ruta al los instalables.
			Si no se establece, supone que estan en el mismo directorio(que el HTML).
		*/ 
		head += "//var baseDownloadURL=path;" + CR;

		/*
			Ruta al instalador.
			Si no se establece, supone que estan en el mismo directorio(que el HTML).
		*/
		head += "//var base=path;" + CR;
		head += "</script>" + CR;
		
		//Scripts necesarios para la firma
		head += "<script type=\"text/javascript\" language=\"javascript\" src=\""+ baseURL + "/ispac/applets/afirma/common-js/deployJava.js\"></script>" + CR;
		head += "<script type=\"text/javascript\" language=\"javascript\" src=\""+ baseURL + "/ispac/applets/afirma/common-js/miniapplet.js\"></script>" + CR;
		head += "<script type=\"text/javascript\" language=\"javascript\" src=\""+ baseURL + "/ispac/applets/afirma/common-js/firma3f.js\"></script>" + CR;


		head += "<script type=\"text/javascript\" language=\"javascript\">" + CR;		
		
		head += "function sign() {" + CR;
		head += "  //Antigua llamada al applet" + CR;
		head += "  //cargarAppletFirma('MEDIA', true);" + CR;
		head += "  cargarMiniApplet3f("+host+");" + CR;
		head += "  if (MiniApplet == undefined) {" + CR;
		head += "    jAlert(\"" + getString(locale, "afirma.error.appletNotLoad") + "\",\""+getString(locale,"forms.button.ok")+ "\",\""+getString(locale,"forms.button.cancel")+ "\");" + CR;
		head += "    // Recargar para reintentar la carga del applet" + CR;
		head += "    location.reload(true);" + CR;
		head += "    return false;" + CR;
		head += "  }" + CR;
		head += "  //clienteFirma.initialize();" + CR; 
		head += "  firmar3f();" + CR; 
		//head += "  resultField.value = signature;" + CR;
		head += "  return true;" + CR;
		head += "}" + CR;	

		head += "</script>" + CR;
		
		//FIN [DipuCR-Agustin #94] modifico las llamadas al servidor de firma para firma 3 fases por lotes

		return head;
	}

	/**
	 * Obtiene el código HTML para incluir en la página.
	 * @param locale Información del idioma del cliente.
	 * @param baseURL URL base.
	 * @param hashCodes Códigos HASH de los documentos a firmar.
	 * @return Código HTML.
	 * @throws ISPACException si ocurre algún error.
	 */
	public String getHTMLCode(Locale locale, String baseURL, String [] hashCodes) throws ISPACException {
		
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		String entityId = info.getOrganizationId();
		FirmaConfiguration fc = FirmaConfiguration.getInstanceNoSingleton(entityId);
		String host = fc.getProperty("firmar.ip.https");
		
		//[DipuCR-Agustin #94] modifico las llamadas al servidor de firma para firma 3 fases por lotes
				
		String head = "";
		
		head += "<script type=\"text/javascript\" language=\"javascript\">" + CR;
		head += "//var path=\"" + baseURL + "/ispac/applets\";" + CR;

		/*
			Ruta al los instalables.
			Si no se establece, supone que estan en el mismo directorio(que el HTML).
		*/ 
		head += "//var baseDownloadURL=path;" + CR;

		/*
			Ruta al instalador.
			Si no se establece, supone que estan en el mismo directorio(que el HTML).
		*/
		head += "//var base=path;" + CR;

		head += "//showErrors = 'false'" + CR;
		head += "</script>" + CR;
		
		//Scripts necesarios para la firma
		head += "<script type=\"text/javascript\" language=\"javascript\" src=\""+ baseURL + "/ispac/applets/afirma/common-js/deployJava.js\"></script>" + CR;
		head += "<script type=\"text/javascript\" language=\"javascript\" src=\""+ baseURL + "/ispac/applets/afirma/common-js/miniapplet.js\"></script>" + CR;
		head += "<script type=\"text/javascript\" language=\"javascript\" src=\""+ baseURL + "/ispac/applets/afirma/common-js/firma3f.js\"></script>" + CR;
				
		head += "<script type=\"text/javascript\" language=\"javascript\">" + CR;

		head += "function initSign() {" + CR;		
		//Inicio [Ticket #367 Agustin]
		head += "  //Antigua llamada al applet" + CR;
		head += "  //cargarAppletFirma('MEDIA', true);" + CR;
		head += "  cargarMiniApplet3f("+host+");" + CR;
		//Fin [Ticket #367 Agustin]
		head += "}" + CR;
		
		head += "function sign() {" + CR;
		head += "  if (MiniApplet == undefined) {" + CR;
		head += "    jAlert(\"" + getString(locale, "afirma.error.appletNotLoad") + "\",\""+getString(locale,"forms.button.ok")+ "\",\""+getString(locale,"forms.button.cancel")+ "\");" + CR;
		head += "    // Recargar para reintentar la carga del applet" + CR;
		head += "    location.reload(true);" + CR;
		head += "    return false;" + CR;
		head += "  }" + CR;
		head += "  //clienteFirma.initialize();" + CR; 
		head += "  return firmar3f();" + CR; 
						
		//head += "  clienteFirma.sign();" + CR;
		//head += "  if(!clienteFirma.isError()) {" + CR;
		//head += "    //cadena con el conjunto de firmas separadas por el caracter '!'" + CR;
		//head += "    var sign = document.getElementById(\"sign\");" + CR;
		//head += "    sign.value = clienteFirma.getSignaturesBase64Encoded();" + CR;
		//head += "    var signCertificate = document.getElementById(\"signCertificate\");" + CR;
		//head += "    signCertificate.value = clienteFirma.getSignCertificateBase64Encoded();" + CR;
		//head += "    return true; // Enviar" + CR;
		//head += "  } else {" + CR;
		//head += "    jAlert(\"" + getString(locale, "afirma.error.batch") + "\",\""+getString(locale,"forms.button.accept")+ "\",\""+getString(locale,"forms.button.cancel")+ "\");" + CR;
		//head += "    return false;" + CR;
		//head += "  }" + CR;
		
		//head += "   return true; // Enviar" + CR;		
		head += "}" + CR;

//		head += "function activarEnvio() {" + CR;
//		head += "  if (clienteFirma != undefined){" + CR;
//		head += "    var _submit = document.getElementById(\"submit\");" + CR;
//		head += "    _submit.disabled = false;" + CR;
//		head += "  }" + CR;
//		head += "}" + CR;

		head += "</script>" + CR;

		//FIN [DipuCR-Agustin #94] modifico las llamadas al servidor de firma para firma 3 fases por lotes
		
		return head;
	}
	
	/**
	 * Obtiene el texto del recurso especificado.
	 * @param language Idioma
	 * @param key Clave del texto.
	 * @return Texto.
	 */
	protected String getString(Locale locale, String key) {
		try {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	
	/**
	 * Inserta en la imagen los datos a visualizar y configura cómo se ha de
	 * mostrar la imagen.
	 * 
	 * @param dateFirma
	 *            Fecha de la firma
	 * @param pdfContentByte
	 *            Contenido
	 * @param font
	 *            Fuente a utilizar
	 * @param encoding
	 *            codificacion
	 * @param fontSize
	 *            Tamaño de la fuente
	 * @param margen
	 *            Margen
	 * @param posicionY
	 *            Posicion y de la imagen
	 * @param vh
	 *            Indica si es vertica o no
	 * @param x
	 *            Posicion x de la imagen
	 * @param path
	 *            Ruta donde se encuentra la imagen
	 * @param numberOfPages
	 *            Numero de páginas totales
	 * @param pageActual
	 *            Página actual
	 * @throws ISPACException si ocurre algún error.
	 */
	protected void getImagen(String dateFirma, PdfContentByte pdfContentByte,
			float margen, boolean vh, float x, int numberOfPages, int pageActual) throws ISPACException {
		
		try {
		
			String font = ISPACConfiguration.getInstance().getProperty(FONT_BAND);
			String encoding = ISPACConfiguration.getInstance().getProperty(ENCODING_BAND);
			float fontSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(FONTSIZE_BAND));
			//float positionY = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(MARGIN_BAND));
			
			BaseFont bf = BaseFont.createFont(font, encoding, false);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(bf, fontSize);

			BufferedReader br = new BufferedReader(new FileReader(getDataFile()));
			String sCadena = null;
			int i = 0;
			while ((sCadena = br.readLine()) != null) {
				if (vh) {
					pdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, x, margen);
					if (i == 0) {
						// pdfContentByte.showText(sCadena + codCotejo);
						pdfContentByte.showText(sCadena + signDocument.getHash());
					} else {
	                    //pdfContentByte.showText(sCadena + numberOfPages + " folios. Folio " + pageActual + " de " + numberOfPages + ".");
						pdfContentByte.showText(sCadena 
								+ MessagesFormatter.format(getString(clientContext.getLocale(), "pdf.band.pageInfo"), new String[] {
									String.valueOf(numberOfPages), 
									String.valueOf(pageActual), 
									String.valueOf(numberOfPages) }));
					}
					i++;
					x += fontSize;
				} else {
	                pdfContentByte.setTextMatrix(margen, x);
	                if(i == 0) {
	                    //pdfContentByte.showText(sCadena + codCotejo);
	                	pdfContentByte.showText(sCadena + signDocument.getHash());
	                } else if(i == 1) {
	                    //pdfContentByte.showText(sCadena + numberOfPages + " folios. Folio " + pageActual + " de " + numberOfPages + ".");
						pdfContentByte.showText(sCadena 
								+ MessagesFormatter.format(getString(clientContext.getLocale(), "pdf.band.pageInfo"), new String[] {
									String.valueOf(numberOfPages), 
									String.valueOf(pageActual), 
									String.valueOf(numberOfPages) }));
	                } else {
	                    pdfContentByte.showText(sCadena);
	                }
	                i++;
	                x -= fontSize;
	            }
			}
			
			br.close();
			pdfContentByte.endText();
			
		} catch (Exception e) {
			logger.error("Error al componer la imagen de la banda lateral", e);
			throw new ISPACException(e);
		}
	}

	protected File getDataFile() throws ISPACException {
		
		// Ruta relativa del texto de la banda lateral
		String dataPath = ISPACConfiguration.getInstance().getProperty(PATH_TEXT);
		if (StringUtils.isBlank(dataPath)) {
			dataPath = DEFAULT_PDF_BG_DATA_PATH;
		}

		String basename = null;
		String ext = null;
		int dotIx = dataPath.lastIndexOf(".");
		if (dotIx > 0) {
			basename = dataPath.substring(0, dotIx);
			ext = dataPath.substring(dotIx);
		} else {
			basename = dataPath;
		}
		
		// Ruta absoluta del texto localizado de la banda lateral
		String dataFullPath = ConfigurationHelper.getConfigFilePath(basename + "_" + clientContext.getLocale().getLanguage() + ext);
		if (StringUtils.isBlank(dataFullPath)) {
			
			// Ruta absoluta del texto de la banda lateral
			dataFullPath = ConfigurationHelper.getConfigFilePath(dataPath);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Texto de la banda lateraldel PDF: " + dataFullPath);
		}

		return new File(dataFullPath);
	}

	/**
	 * Obtiene la imagen a utilizar como fondo.
	 * 
	 * @return Imagen de fondo.
	 * @throws ISPACException
	 *             si ocurre algún error.
	 */
	protected Image createBgImage() throws ISPACException {

		// Ruta relativa de la imagen de fondo
		String imagePath = ISPACConfiguration.getInstance().getProperty(PATH_IMAGEN_BAND);
		if (StringUtils.isBlank(imagePath)) {
			imagePath = DEFAULT_PDF_BG_IMAGE_PATH;
		}

		try {
			
			// Ruta absoluta de la imagen de fondo
			String imageFullPath = ConfigurationHelper.getConfigFilePath(imagePath);
			if (logger.isInfoEnabled()) {
				logger.info("Imagen de fondo del PDF: " + imageFullPath);
			}
			
			// Construir la imagen de fondo
			Image image = Image.getInstance(imageFullPath);
			image.setAbsolutePosition(200F, 400F);			
			return image;
			
		} catch (Exception e) {
			logger.error("Error al leer la imagen de fondo del PDF firmado: " + imagePath, e);
			throw new ISPACException(e);
		}
	}
	
	public void sign(boolean changeState) throws ISPACException{
		
	}
	
	public String presign(boolean changeState) throws ISPACException{
		
		return null;	
		
	}

	public String postsign(String pathFicheroTempFirmado, boolean changeState)
			throws ISPACException {
		// TODO Auto-generated method stub
		return null;
	}
	
		
	
}

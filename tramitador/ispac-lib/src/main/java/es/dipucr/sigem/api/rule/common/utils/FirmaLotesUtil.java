package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.ServicioEstructuraOrganizativa;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;

import java.math.BigInteger;
import java.util.Date;
import java.util.Properties;

import es.dipucr.sigem.api.firma.xml.peticion.ObjectFactoryFirmaLotesPeticion;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch.Singlesign;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;

public class FirmaLotesUtil {

	/**
	 * Variables firma lotes
	 */
	public final static String DATASOURCE_WEBSERVICE = "webservice";
	public final static String EXTRAPARAM_DOC_CODIGO_ENTIDAD = "codent=";
	public final static String EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER = "guid=";
	public final static String EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE = "datasource=";
	
	interface LOTES{
		public final static String EXTRAPARAMS = "firmar.lotes.extraparams";
		public final static String EXTRAPARAMS_SEPARATOR = "\n";
		
		public final static String INITIAL_BOTTOM = "firmar.lotes.initial_bottom";
		public final static String INITIAL_TOP = "firmar.lotes.initial_top";
		public final static String HEIGHT = "firmar.lotes.height";
		public final static String DEFAULT_PAGE = "firmar.lotes.default_page";
		public final static String FONT_SIZE = "firmar.lotes.font_size";
		
		public final static String SIGNSAVER = "firmar.lotes.signsaver";
		public final static String SIGNSAVER_CONFIG = "firmar.lotes.signsaver.config";
		public final static String FORMAT = "firmar.lotes.format";
		public final static String SUBOPERATION = "firmar.lotes.suboperation";
		public final static String ALGORITH = "firmar.lotes.algorithm";
		public final static String STOPONERROR = "firmar.lotes.stoponerror";
		public final static String CURRENTTIMEOUT ="firmar.lotes.concurrenttimeout";
		
		interface TAG{
			public final static String CARGO = "[CARGO]";
			public final static String NOMBRE = "[NOMBRE]";
			public final static String FECHA_FIRMA = "[FECHA_FIRMA]";
			public final static String BOTTOM_POSITION = "[BOTTOM_POSITION]";
			public final static String TOP_POSITION = "[TOP_POSITION]";
			public final static String PAGE = "[PAGE]";
			public final static String FONT_SIZE = "[FONT_SIZE]";
		}
	}
	
	interface EXTRAPARAMS{
		interface SIGNATURE{
			public final static String PAGE = "signaturePage"; 
			public final static String POSITION_LOWERLEFT_X = "signaturePositionOnPageLowerLeftX";
			public final static String POSITION_LOWERLEFT_Y = "signaturePositionOnPageLowerLeftY";
			public final static String POSITION_UPPERRIGHT_X = "signaturePositionOnPageUpperRightX";
			public final static String POSITION_UPPERRIGHT_Y = "signaturePositionOnPageUpperRightY";
			public final static String TEXT = "layer2Text";
			public final static String FONT_SIZE = "layer2FontSize";
			public final static String FONT_FAMILY = "layer2FontFamily";
			public final static String RUBRIC_IMAGE = "signatureRubricImage";
		}
	}
	
	public final static String DESHABILITAR_FIRMA = "firmar.deshabilitar";
	
	public interface ERROR{
		
		public interface DESHABILITAR{
			public final static String TITULO = "firmar.error.deshabilitar.titulo";
			public final static String TEXTO = "firmar.error.deshabilitar.texto";
		}
		
		public interface NOCONFIGURADO{
			public final static String TITULO = "firmar.error.noconfigurado.titulo";
			public final static String TEXTO = "firmar.error.noconfigurado.texto";
		}

		public interface FIRMAPREVIA{
			public final static String TITULO = "firmar.error.firmaprevia.titulo";
			public final static String TEXTO = "firmar.error.firmaprevia.texto";
		}
		
		public interface DECRETOS{
			public final static String TITULO = "firmar.error.decretos.titulo";
			public final static String TEXTO = "firmar.error.decretos.texto";
		}
	}
	
	public final static int END_PAGE = -1;
	
	public final static int MAX_TEXT_SIZE1 = 100;
	public final static int MAX_TEXT_SIZE2 = 80;
	
	
	/**
	 * Devuelve la configuración de extraparams para el documento
	 * @param entityId
	 * @param cct
	 * @param docRef
	 * @param numberOfSigners
	 * @param signerPosition
	 * @return
	 * @throws Exception
	 */
	public static String getFirmaExtraParams(String entityId, IClientContext cct, InfoFirmante infofirmante, 
			String documentTypeId, String docRef, int numberOfSigners, int signerPosition, Date dFechaFirma) throws Exception{
		
		return getFirmaExtraParams(entityId, cct, infofirmante, documentTypeId, docRef, numberOfSigners, signerPosition, dFechaFirma, false);
	}
	
	public static String getFirmaExtraParams(String entityId, IClientContext cct, InfoFirmante infofirmante, 
			String documentTypeId, String docRef, int numberOfSigners, int signerPosition, Date dFechaFirma, boolean bAddImagen) throws Exception{

		FirmaConfiguration fc = FirmaConfiguration.getInstanceNoSingleton(entityId);
		String firmarLotesExtraparams = fc.getProperty(LOTES.EXTRAPARAMS);
		if (bAddImagen){
			firmarLotesExtraparams += "signatureRubricImage=/9j/4AAQSkZJRgABAQEBLAEsAAD//gAUQ3JlYXRlZCB3aXRoIEdJTVAA/9sAQwACAQECAQECAgICAgICAgMFAwMDAwMGBAQDBQcGBwcHBgcHCAkLCQgICggHBwoNCgoLDAwMDAcJDg8NDA4LDAwM/9sAQwECAgIDAwMGAwMGDAgHCAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwM/8AAEQgAFAISAwEiAAIRAQMRAf/EAB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAgQEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX29/j5+v/aAAwDAQACEQMRAD8A/c74n/E3Qfg14A1zxZ4o1S10Xw74dtJdQ1G+uH2x20MYyxJ+g4H3iSAOTX5//wDBKf4nfEj/AIKAftleMv2kpPE3jLQfhPJbXPhSz8EahNttIbtGt2iMUIZ4nVYCJZp9sc4upZIAzxRstcz/AMFrviN44/bG/a2+H37Gfw/abS/+EhgtvFPim/ntn8h7Uyz+QzISvm2tsLWWaUqds04tINwzME+j/hT8ffAn7MH7JOn+Cfh7oN5o9z4Nc+H9M0S+i8u6mnLvuvLjacs0khkllY/O00hBJd1LGecQZbw/lirY6aVWupcqe/LHe3eUnoutjzYxniMTb7MN/OXT5JH2DFzu5704V5v+zN4K8ReBPhrbQeKtWvdY1y+le8unuX3tbGQ7vKB6YXphflUkgAADPpAFefluKnicLTr1IODkr8r3Xk/O256Q6iiivQAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAPOfhN8DND+GWv+LNbsze3mueNNfu9Z1XUL2UTTSSusNuiRjASKOO1sbKBUjVQyWkbSeZLvlaS/8AgN4U1b4x2fi6bSYW161iytwGZVdwu1XdAdruqkhWYEqMYPyrgorxc+w9Kt7P20VLlqQauk7a9L7Co/Cd8nBWpKKK9oYUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFAH//2Q==";
		}

		//Sustuituimos nombre y cargo
		String cargo = infofirmante.getCargo();
		if (!StringUtils.isEmpty(cargo)){
			cargo += " ";
		}
		firmarLotesExtraparams = firmarLotesExtraparams.replace(LOTES.TAG.CARGO, cargo);
		firmarLotesExtraparams = firmarLotesExtraparams.replace(LOTES.TAG.NOMBRE, infofirmante.getNombreFirmante());
		String sFechaFirma = FechasUtil.getFormattedDate(dFechaFirma, FechasUtil.SIMPLE_DATE_PATTERN);
		firmarLotesExtraparams = firmarLotesExtraparams.replace(LOTES.TAG.FECHA_FIRMA, sFechaFirma);
		
		//Sustituimos las posiciones
		int initialBottom = Integer.valueOf(fc.getProperty(LOTES.INITIAL_BOTTOM));
		IItem itemTipoDocumento = null;
		String especificDocPosition = null;
		itemTipoDocumento = getTipoDocumento(cct, documentTypeId);
		especificDocPosition = itemTipoDocumento.getString("SIGN_POSITION");
		
		if (!StringUtils.isEmpty(especificDocPosition)){
			initialBottom = Integer.valueOf(especificDocPosition);
		}
		
		int height = Integer.valueOf(fc.getProperty(LOTES.HEIGHT));
		int initialTop = initialBottom + height;
		int numPositions = numberOfSigners - signerPosition;
		int bottomPosition = initialBottom + (numPositions * height);
		int topPosition = initialTop + (numPositions * height);
		
		int page = getSignPage(cct, itemTipoDocumento);
		firmarLotesExtraparams = firmarLotesExtraparams.replace(LOTES.TAG.PAGE, String.valueOf(page));
		firmarLotesExtraparams = firmarLotesExtraparams.replace(LOTES.TAG.BOTTOM_POSITION, String.valueOf(bottomPosition));
		firmarLotesExtraparams = firmarLotesExtraparams.replace(LOTES.TAG.TOP_POSITION, String.valueOf(topPosition));
		
		//Tamaño demasiado grande
		int fontSize = Integer.valueOf(fc.getProperty(LOTES.FONT_SIZE));
		String nombreCompleto = cargo.concat(infofirmante.getNombreFirmante());
		if (nombreCompleto.length() > MAX_TEXT_SIZE1){
			fontSize = fontSize - 2;
		}
		else if (nombreCompleto.length() > MAX_TEXT_SIZE2){
			fontSize = fontSize - 1;
		}
		firmarLotesExtraparams = firmarLotesExtraparams.replace(LOTES.TAG.FONT_SIZE, String.valueOf(fontSize));
		
		String firmarLotesSignsaverConfigPath = fc.getProperty(LOTES.SIGNSAVER_CONFIG);
		firmarLotesExtraparams = firmarLotesExtraparams.concat(LOTES.EXTRAPARAMS_SEPARATOR);
		
		String firmarLotesExtraparamsSigem = "";		
		firmarLotesExtraparamsSigem = firmarLotesExtraparamsSigem.concat(EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE);
		firmarLotesExtraparamsSigem = firmarLotesExtraparamsSigem.concat
				(firmarLotesSignsaverConfigPath.substring(firmarLotesSignsaverConfigPath.indexOf("=") + 1));				
	
		firmarLotesExtraparamsSigem = firmarLotesExtraparamsSigem.concat(LOTES.EXTRAPARAMS_SEPARATOR);
		firmarLotesExtraparamsSigem = firmarLotesExtraparamsSigem.concat(EXTRAPARAM_DOC_CODIGO_ENTIDAD);
		firmarLotesExtraparamsSigem = firmarLotesExtraparamsSigem.concat(entityId);
		
		firmarLotesExtraparamsSigem = firmarLotesExtraparamsSigem.concat(LOTES.EXTRAPARAMS_SEPARATOR);
		firmarLotesExtraparamsSigem = firmarLotesExtraparamsSigem.concat(EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER);
		if (!StringUtils.isEmpty(docRef)){
			firmarLotesExtraparamsSigem = firmarLotesExtraparamsSigem.concat(docRef);
		}
			
		firmarLotesExtraparams = firmarLotesExtraparams.concat(firmarLotesExtraparamsSigem);	
		
		return firmarLotesExtraparams;
	}
	
	public static Properties getPropertiesFirmaExtraParams(String entityId, IClientContext cct, InfoFirmante infofirmante, 
			String documentTypeId, String docRef, int numberOfSigners, int signerPosition, Date dFechaFirma) throws Exception{
		
		String extraparams = getFirmaExtraParams(entityId, cct, infofirmante, 
				documentTypeId, docRef, numberOfSigners, signerPosition, dFechaFirma, true);
		
		Properties propertiesExtra = new Properties();
		String[] arrExtraparams = extraparams.split("\\n");
		for (String param : arrExtraparams){
			if (!StringUtils.isEmpty(param)){
				String[] arrParam = param.split("=");
				if (arrParam.length > 1){
					propertiesExtra.put(arrParam[0], arrParam[1]);
				}
			}
		}
		return propertiesExtra;
	}

	/**
	 * Devuelve el tipo de documento a partir del signDocument
	 * @param cct
	 * @param documentTypeId
	 * @return
	 * @throws ISPACException
	 */
	private static IItem getTipoDocumento(IClientContext cct, String documentTypeId) throws ISPACException {
		
		ICatalogAPI catalogAPI = cct.getAPI().getCatalogAPI();
		String query = " WHERE ID = " + documentTypeId;
		IItemCollection tpdocIterator = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_TYPEDOC, query);
		IItem itemTipoDocumento = tpdocIterator.value();
		return itemTipoDocumento;
	}

	/**
	 * Devuelve la página por defecto dónde se añaden las páginas del documento
	 * @param cct
	 * @param itemTipoDocumento
	 * @return
	 * @throws ISPACException
	 */
	public static int getSignPage(IClientContext cct, IItem itemTipoDocumento) throws ISPACException {
		
		FirmaConfiguration fc = FirmaConfiguration.getInstance(cct);
		int page = Integer.valueOf(fc.getProperty(LOTES.DEFAULT_PAGE));
		if (null != itemTipoDocumento){
			String especificPage = itemTipoDocumento.getString("SIGN_PAGE");
			if (!StringUtils.isEmpty(especificPage)){
				page = Integer.valueOf(especificPage);
			}
		}
		return page;
	}
	
	public static int getSignPage(IClientContext cct, String documentTypeId) throws ISPACException {
		return getSignPage(cct, getTipoDocumento(cct, documentTypeId));
	}
	
	/**
	 * Devuelve el objeto signBatch de la firma
	 * @param cct
	 * @param peticion
	 * @return
	 * @throws Exception
	 */
	public static Signbatch getSignBatch(IClientContext cct, ObjectFactoryFirmaLotesPeticion peticion) throws Exception {
		
		FirmaConfiguration fc = FirmaConfiguration.getInstance(cct);
		String firmarLotesAlgorithm = fc.getProperty(LOTES.ALGORITH);
		String firmarLotesStoponerror = fc.getProperty(LOTES.STOPONERROR);
		String firmarLotesConcurrenttimeout = fc.getProperty(LOTES.CURRENTTIMEOUT);
		
		BigInteger concurrenttimeout = new BigInteger(firmarLotesConcurrenttimeout);	
						
		//Objeto raiz signbatch
		Signbatch signbatch = peticion.createSignbatch();
		signbatch.setAlgorithm(firmarLotesAlgorithm);
		signbatch.setStoponerror(firmarLotesStoponerror);
		signbatch.setConcurrenttimeout(concurrenttimeout);
		
		return signbatch;
	}
	
	/**
	 * Devuelve el objeto singleSign para cada documento que se va a firmar
	 * @param cct
	 * @param peticion
	 * @param codEntidad
	 * @param hashCode
	 * @param docCount
	 * @return
	 * @throws Exception
	 */
	public static Singlesign getSingleSign(ClientContext cct, ObjectFactoryFirmaLotesPeticion peticion, String codEntidad,
			int docCount, String firmaLotesExtraParams) throws Exception {
		
		FirmaConfiguration fc = FirmaConfiguration.getInstance(cct);
		String firmarLotesFormat = fc.getProperty(LOTES.FORMAT);
		String firmarLotesSuboperation = fc.getProperty(LOTES.SUBOPERATION);
		String firmarLotesSignsaver = fc.getProperty(LOTES.SIGNSAVER);
		String firmarLotesSignsaverConfig = fc.getProperty(LOTES.SIGNSAVER_CONFIG);
		
		// URL del documento
		String url = FirmaLotesUtil.DATASOURCE_WEBSERVICE;

		byte[] extraparamsBase64 = firmaLotesExtraParams.getBytes();
		byte[] configSignSaberBase64 = firmarLotesSignsaverConfig.getBytes();
		
		//Objeto signSaver 		
		Signbatch.Singlesign.Signsaver signsaver = peticion.createSignbatchSinglesignSignsaver();
		signsaver.setClazz(firmarLotesSignsaver);
		signsaver.setConfig(configSignSaberBase64);
		
		Signbatch.Singlesign singlesign;
		singlesign = peticion.createSignbatchSinglesign();
		singlesign.setId(Integer.toString(docCount));
		singlesign.setDatasource(url);
		singlesign.setFormat(firmarLotesFormat);
		singlesign.setSuboperation(firmarLotesSuboperation);
		singlesign.setExtraparams(extraparamsBase64);
		singlesign.setSignsaver(signsaver);
		
		return singlesign;
	}
	
	/**
	 * Recuera la información del firmante de la BBDD
	 * @param cct
	 * @return
	 * @throws ISPACException
	 */
	public static InfoFirmante getInfoFirmante(ClientContext cct, String entityId) throws Exception {
		
		String docIdentidad = UsuariosUtil.getDni(cct);
		String nombreFirmante = UsuariosUtil.getNombreFirma(cct);
		String numeroSerie = UsuariosUtil.getNumeroSerie(cct);
		
		String name = cct.getResponsible().getName();
		ServicioEstructuraOrganizativa servEstOrg = LocalizadorServicios.getServicioEstructuraOrganizativa();
		Usuario user = servEstOrg.getUsuario(name, entityId);
        String cargo = user.get_description();
		
		InfoFirmante infoFirmante = new InfoFirmante();
		infoFirmante.setDocIdentidadCertificado(docIdentidad);
		infoFirmante.setNombreFirmante(nombreFirmante);
		infoFirmante.setNumeroSerie(numeroSerie);
		infoFirmante.setCargo(cargo);
		
		return infoFirmante;
	}
	
	/**
	 * 
	 * @param codEntidad
	 * @return
	 * @throws Exception
	 */
	public static FirmaLotesError getErrorDeshabilitar(String codEntidad) throws Exception{
		
		FirmaConfiguration fc = FirmaConfiguration.getInstanceNoSingleton(codEntidad);
		
		String sDeshabilitarFirma = fc.getProperty(DESHABILITAR_FIRMA);
		boolean bDeshabilitarFirma = false;
		if (StringUtils.isNotEmpty(sDeshabilitarFirma)){
			bDeshabilitarFirma = Boolean.valueOf(sDeshabilitarFirma).booleanValue();
		}
		
		FirmaLotesError error = null;
		if (bDeshabilitarFirma){

			String titulo = fc.getProperty(ERROR.DESHABILITAR.TITULO);
			String texto = fc.getProperty(ERROR.DESHABILITAR.TEXTO);
			error = new FirmaLotesError(titulo, texto);
		}
		return error;
	}

	/**
	 * 
	 * @param codEntidad
	 * @param infoFirmante
	 * @return
	 * @throws ISPACRuleException
	 */
	public static FirmaLotesError getErrorFirmanteNoConfigurado
		(String codEntidad, InfoFirmante infoFirmante) throws ISPACRuleException {

		FirmaConfiguration fc = FirmaConfiguration.getInstance(codEntidad);
		
		FirmaLotesError error = null;
		if (StringUtils.isEmpty(infoFirmante.getNombreFirmante()) || 
	    	StringUtils.isEmpty(infoFirmante.getNumeroSerie())) {
//	    	StringUtils.isEmpty(infoFirmante.getDocIdentidadCertificado())) { 
			
			String titulo = fc.getProperty(ERROR.NOCONFIGURADO.TITULO);
			String texto = fc.getProperty(ERROR.NOCONFIGURADO.TEXTO);
			error = new FirmaLotesError(titulo, texto);
		}
		return error;
	}
	
}

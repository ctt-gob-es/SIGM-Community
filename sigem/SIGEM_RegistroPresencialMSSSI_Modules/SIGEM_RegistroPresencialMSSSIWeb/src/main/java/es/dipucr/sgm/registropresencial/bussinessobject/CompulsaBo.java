package es.dipucr.sgm.registropresencial.bussinessobject;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;
import ieci.tecdoc.sgm.core.services.gestioncsv.CodigosAplicacionesConstants;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSV;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSVForm;
import ieci.tecdoc.sgm.core.services.gestioncsv.ServicioGestionCSV;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sgm.registropresencial.beans.CompulsaBean;
import es.dipucr.sigem.api.firma.xml.peticion.ObjectFactoryFirmaLotesPeticion;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.msssi.sgm.registropresencial.arboldocumentos.Document;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.IGenericBo;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeErrorCode;

public class CompulsaBo implements IGenericBo, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(CompulsaBo.class);
	
	public static final String INFODOCUMENTO_CSV_TIPO_MIME = "application/pdf";
	
	public static final String PATH_UPLOAD = "/FileUploadCompulsaDipucr";
	public static final String DESTINO_UPLOAD = "REGISTRO";
	
	public static final String ARRIBA = "ARRIBA";
	public static final String ABAJO = "ABAJO";
	public static final String IZQUIERDA = "IZQUIERDA";
	public static final String DERECHA = "DERECHA";
		
	private static final String DATASOURCE_WEBSERVICE = "webservice";
	private static final String EXTRAPARAM_DOC_CODIGO_ENTIDAD = "codent=";
	private static final String EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER = "guid=";
	private static final String EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE = "datasource=";
	
	private static final String IMG_FONDO = "fondo.png";
	private static final String IMG_LOGO_CABECERA = "logoCabecera.gif";
	private static final String IMG_PIE = "pie.jpg";
	
	private static final String SUFIJO_PAGINA_COMPULSA = "_pagCompulsa.pdf";
	
	//Constantes del sello
	private static final String REGISTRO = "Registro: ";
	private static final String TIPO_REGISTRO_ENTRADA_SELLO = "E";
	private static final String TIPO_REGISTRO_SALIDA_SELLO = "S";
	private static final String CVE = "CVE: ";
	private static final Font FUENTE_SELLO = FontFactory.getFont(FontFactory.COURIER, 8);
	
	//Constantes de la página resumen de la compulsa
	
	// Fuentes de los textos
	private static final Font FUENTE_TITULO = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);
	private static final Font FUENTE_NEGRITA = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
	private static final Font FUENTE_NORMAL = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
			
	private static final String TIPO_REGISTRO_ENTRADA = "ENTRADA";
	private static final String TIPO_REGISTRO_SALIDA = "SALIDA";
	private static final String FORMATO_FECHA = "dd/MM/yyyy HH:mm:ss";
	private static final String TITULO_COMPULSA = "Copia auténtica";
	private static final String ENTIDAD = "Entidad: ";
	private static final String TIPO_REGISTRO = "Tipo de registro: ";
	private static final String NUMERO_REGISTRO = "Número de registro: ";
	private static final String FECHA_REGISTRO = "Fecha de registro: ";
	private static final String ORIGEN = "Origen: ";
	private static final String DESTINO = "Destino: ";
	private static final String FUNCIONARIO_HABILITADO = "Funcionario habilitado: ";
	private static final String INTERESADOS = "Interesado/s: ";
	private static final String ESTADO_ELABORACIÓN = "Estado de elaboración: ";
	private static final String TIPO_DOCUMENTAL= "Tipo de documento: ";
	
	
	public static void sellaDocumento(CompulsaBean compulsaBean, File ficheroTemp, File ficheroFirmar, Integer pageId, Integer fileId) {
		try {			
			ServicioGestionCSV servicioGestionCSV = LocalizadorServicios.getServicioGestionCSV();
			ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
			
			InfoDocumentoCSVForm infoDocumentoForm = new InfoDocumentoCSVForm();
			infoDocumentoForm.setCodigoAplicacion(CodigosAplicacionesConstants.REGISTRO_PRESENCIAL_CODE);
			infoDocumentoForm.setDisponible(true);
			infoDocumentoForm.setFechaCaducidad(null);
			infoDocumentoForm.setFechaCreacion(new Date());
			infoDocumentoForm.setNombre(ficheroFirmar.getName());
			infoDocumentoForm.setTipoMime(INFODOCUMENTO_CSV_TIPO_MIME);			

			// Generar el CSV
			ieci.tecdoc.sgm.core.services.entidades.Entidad entidadAdm = servicioEntidades.obtenerEntidad(compulsaBean.getUseCaseConf().getEntidadId());
			Entidad entidad = new Entidad();
			entidad.setIdentificador(compulsaBean.getUseCaseConf().getEntidadId());
			entidad.setNombreCorto(entidadAdm.getNombreCorto());
			entidad.setNombreLargo(entidadAdm.getNombreLargo());
			InfoDocumentoCSV infoDocumento = servicioGestionCSV.generarCSV(entidad, infoDocumentoForm);
			String cve = infoDocumento.getCsv();

			PdfReader reader = new PdfReader(new FileInputStream(ficheroTemp));
			int nPags = reader.getNumberOfPages();
			
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(ficheroFirmar));
			
			for (int i = 1; i <= nPags; i++){
				Rectangle tamanioPag = reader.getPageSizeWithRotation(i);		
				PdfContentByte content = stamper.getOverContent(i);
				
				String strSello = REGISTRO + entidad.getNombreLargo() + " - ";
				if(compulsaBean.getRegisterBean() instanceof InputRegisterBean){					
					strSello += TIPO_REGISTRO_ENTRADA_SELLO + " - " + ((InputRegisterBean)compulsaBean.getRegisterBean()).getFld1();
				} else if(compulsaBean.getRegisterBean() instanceof OutputRegisterBean){					
					strSello += TIPO_REGISTRO_SALIDA_SELLO + " - " + ((OutputRegisterBean)compulsaBean.getRegisterBean()).getFld1();
				} 
				
				strSello +=  " - " + CVE + cve + " " + i + " de " + nPags;
				
				ponSelloCompulsa(compulsaBean, tamanioPag, content, strSello);
			}
			
			stamper.close();
			reader.close();
		} catch (IOException e) {
			LOGGER.error("ERROR al sellar el documento. " + e.getMessage(), e);
		} catch (DocumentException e) {
			LOGGER.error("ERROR al sellar el documento. " + e.getMessage(), e);
		} catch (SigemException e) {
			LOGGER.error("ERROR al sellar el documento. " + e.getMessage(), e);
		}
	}
	
	public static void ponSelloCompulsa(CompulsaBean compulsaBean, Rectangle tamanioPag, PdfContentByte content, String strSello){
		float[] posicion;
		int rotacion = 0;
		
		if(ABAJO.equals(compulsaBean.getPosicionSello())){
			posicion = getPosicionSelloAbajo(tamanioPag, compulsaBean.getAlineacionSello());
						
		} else if(IZQUIERDA.equals(compulsaBean.getPosicionSello())){			
			posicion = getPosicionSelloIzquierda(tamanioPag, compulsaBean.getAlineacionSello());
			rotacion = 90;
			
		} else if(DERECHA.equals(compulsaBean.getPosicionSello())){								
			posicion = getPosicionSelloDerecha(tamanioPag, compulsaBean.getAlineacionSello());
			rotacion = 90;
			
		} else {
			posicion = getPosicionSelloArriba(tamanioPag, compulsaBean.getAlineacionSello());
		}

		ColumnText.showTextAligned(content, compulsaBean.getAlineacionSello(), new Phrase(strSello, FUENTE_SELLO), posicion[0], posicion[1], rotacion);
	}

	public static float[] getPosicionSelloArriba(Rectangle tamanioPag, int alineacionSello){
		float posX = tamanioPag.getBottom() + 8;
		if(Element.ALIGN_RIGHT == alineacionSello){
			posX = tamanioPag.getWidth() - 8;
		}
		
		float posY = tamanioPag.getTop() - 8;
		
		float[] posicion = {posX, posY};
		
		return posicion;
	}
	
	public static float[] getPosicionSelloAbajo(Rectangle tamanioPag, int alineacionSello){
		float posX = tamanioPag.getBottom() + 8;
		if(Element.ALIGN_RIGHT == alineacionSello){
			posX = tamanioPag.getWidth() - 8;
		}
		
		float posY = tamanioPag.getBottom() + 8;
		
		float [] posicion = {posX, posY};
		
		return posicion;
	}
	
	public static float[] getPosicionSelloIzquierda(Rectangle tamanioPag, int alineacionSello){
		float posX = tamanioPag.getBottom() + 8;
		
		float posY = tamanioPag.getBottom() + 8;
		if(Element.ALIGN_RIGHT == alineacionSello){
			posY = tamanioPag.getHeight() - 8;
		}
		
		float [] posicion = {posX, posY};
		
		return posicion;
	}
	
	public static float[] getPosicionSelloDerecha(Rectangle tamanioPag, int alineacionSello){
		float posX = tamanioPag.getWidth() - 8;
		
		float posY = tamanioPag.getBottom() + 8;
		if(Element.ALIGN_RIGHT == alineacionSello){
			posY = tamanioPag.getHeight() - 8;
		}
		
		float [] posicion = {posX, posY};
		
		return posicion;
	}
	
	public static void iniciaPeticionFirma(CompulsaBean compulsaBean, FirmaConfiguration fc, Signbatch signbatch, ObjectFactoryFirmaLotesPeticion peticion) {
		
		String nombreFirmante = compulsaBean.getNombreFirmante();
		String docIdentidad = compulsaBean.getDniFirmante();
		
		InfoFirmante infoFirmante = new InfoFirmante();		
		infoFirmante.setDocIdentidadCertificado(docIdentidad);
		infoFirmante.setNombreFirmante(nombreFirmante);
		
		String firmar_lotes_algorithm = fc.getProperty("firmar.lotes.algorithm");
		String firmar_lotes_stoponerror = fc.getProperty("firmar.lotes.stoponerror");
		String firmar_lotes_concurrenttimeout = fc.getProperty("firmar.lotes.concurrenttimeout");
					
		BigInteger concurrenttimeout = new BigInteger(firmar_lotes_concurrenttimeout);	
						
		//Objeto raiz signbatch
		signbatch.setAlgorithm(firmar_lotes_algorithm);
		signbatch.setStoponerror(firmar_lotes_stoponerror);
		signbatch.setConcurrenttimeout(concurrenttimeout);
	}
	
	public static void prepararFirmaDoc(CompulsaBean compulsaBean, Document documento) {
		
		FirmaConfiguration fc;
		ObjectFactoryFirmaLotesPeticion peticion;
		Signbatch signbatch;

		try {
			if(null != documento){
				fc = FirmaConfiguration.getInstanceNoSingleton(compulsaBean.getUseCaseConf().getEntidadId());
				
				peticion = new ObjectFactoryFirmaLotesPeticion();
				signbatch = peticion.createSignbatch();
				
				iniciaPeticionFirma(compulsaBean, fc, signbatch, peticion);
				
				preparaDocumentosFirma(0, compulsaBean, fc, signbatch, peticion, documento);
	
				compulsaBean.setListaDocsFirmar(generaPeticionFirma(signbatch));
			}
		} catch (ISPACRuleException e) {
			LOGGER.error("ERROR al generar la lista de documentos a firmar. " + e.getMessage(), e);
		}
		
	}
	
	public static int preparaDocumentosFirma(int numDoc, CompulsaBean compulsaBean, FirmaConfiguration fc, Signbatch signbatch, ObjectFactoryFirmaLotesPeticion peticion, Document documento) {
		if(documento.isEsDocumento()){
			if (null != documento && !documento.isEsFirmado()){				
				preparaDocumentoFirma(numDoc, compulsaBean, fc, signbatch, peticion, documento);
				numDoc++;
			}
		} else {				
	    	for (Document doc : documento.getListaDocumentos()) {
	    		numDoc = preparaDocumentosFirma(numDoc, compulsaBean, fc, signbatch, peticion, doc);	    		
	    	}
		}
		
		return numDoc;
	}

	public static void preparaDocumentoFirma(int i, CompulsaBean compulsaBean, FirmaConfiguration fc, Signbatch signbatch, ObjectFactoryFirmaLotesPeticion peticion, Document documento) {
		
		File ficheroFirmar = null;
		String docRef = null;
		
		String url = DATASOURCE_WEBSERVICE;
		
		byte[] extraparamsBase64 = null;
		byte[] configSignSaberBase64 = null;
		
		String firmar_lotes_format = fc.getProperty("firmar.lotes.format");
		String firmar_lotes_suboperation = fc.getProperty("firmar.lotes.suboperation");
		String firmar_lotes_signsaver = fc.getProperty("firmar.lotes.signsaver");
		
		ficheroFirmar = getFicheroCompulsar(compulsaBean, documento);
    	
    	docRef = ficheroFirmar.getName();
		
		String firmar_lotes_extraparams = fc.getProperty("firmar.lotes.extraparams");
		String firmar_lotes_signsaver_config_path = fc.getProperty("firmar.lotes.signsaver.config");
		firmar_lotes_extraparams = firmar_lotes_extraparams.concat("\n");
		
		String firmar_lotes_extraparams_sigem = "";		
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE);
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(firmar_lotes_signsaver_config_path.substring(firmar_lotes_signsaver_config_path.indexOf("=")+1));				
	
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("\n");
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(EXTRAPARAM_DOC_CODIGO_ENTIDAD);
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(compulsaBean.getUseCaseConf().getEntidadId());
		
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("\n");
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER);
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("/" + docRef);
		
		firmar_lotes_extraparams = firmar_lotes_extraparams.concat(firmar_lotes_extraparams_sigem);		
		
		extraparamsBase64 = firmar_lotes_extraparams.getBytes();		
		configSignSaberBase64 = firmar_lotes_signsaver_config_path.getBytes();
		
		Signbatch.Singlesign singlesign;
		Signbatch.Singlesign.Signsaver signsaver;
						
		signsaver =  peticion.createSignbatchSinglesignSignsaver();
		signsaver.setClazz(firmar_lotes_signsaver);
		signsaver.setConfig(configSignSaberBase64);
				
		singlesign = peticion.createSignbatchSinglesign();
		singlesign.setId(Integer.toString(i));
		singlesign.setDatasource(url);
		singlesign.setFormat(firmar_lotes_format);
		singlesign.setSuboperation(firmar_lotes_suboperation);
		singlesign.setExtraparams(extraparamsBase64);
		singlesign.setSignsaver(signsaver);
				
		signbatch.getSinglesign().add(singlesign);
	}
	
	public static File getFicheroCompulsar(CompulsaBean compulsaBean, Document documentoArbol) {
		File ficheroFirmar = null;		
		File ficheroTemp = null;
		
		Axpageh documento = documentoArbol.getDocumento();
		
		String nombreFichero = compulsaBean.getUseCaseConf().getEntidadId() + "_" + compulsaBean.getBookId() + "_" + documento.getFdrid() + "_" + documento.getDocId() + "_" + documento.getId() + "." + FilenameUtils.getExtension(documento.getName());
		
		try {
			ficheroFirmar = new File(FileTemporaryManager.getInstance().getFileTemporaryPath(), nombreFichero);
			ficheroTemp = new File(FileTemporaryManager.getInstance().getFileTemporaryPath(), "tmp_" + nombreFichero);			
		} catch (ISPACException e1) {
			LOGGER.error("ERROR al recuperar el documento temporal en el proceso de Compulsa de los adjuntos del registro presencial: " + documento.getName() + ". " + e1.getMessage(), e1);
		}
        
        byte[] content = null;
        BookUseCase bookUseCase = new BookUseCase();
        
    	try {
    	    content = bookUseCase.getFile(compulsaBean.getUseCaseConf(), compulsaBean.getBookId(), documento.getFdrid(), documento.getDocId(), documento.getId());
    	    
    	    FileOutputStream fos = new FileOutputStream(ficheroTemp);
    	    fos.write(content);
    	    fos.close();
    	    
    	} catch (ValidationException e) {
    	    LOGGER.error(RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE, e);		        	    
    	} catch (BookException e) {
    	    LOGGER.error(RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE, e);
    	} catch (IOException e) {
    		LOGGER.error(RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE, e);
		} catch (SessionException e) {
			LOGGER.error(RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE, e);
		}
    	
    	File pagInfoCompulsa = generaPagInfoCompulsa(compulsaBean, documentoArbol);
    	ArrayList<File> archivos = new ArrayList<File>();
    	archivos.add(ficheroTemp);
    	archivos.add(pagInfoCompulsa);
    	
    	try {
			ficheroTemp = PdfUtil.concatenarArchivos(archivos);
		} catch (ISPACException e) {
			LOGGER.error("ERROR al añadir la información de registro. " + e.getMessage(), e);
		}		        	
    	
    	sellaDocumento(compulsaBean, ficheroTemp, ficheroFirmar, documento.getPageId(), documento.getFileId());
    	
    	if(null != ficheroTemp && ficheroTemp.exists()){
    		ficheroTemp.delete();
    	}
    	if(null != pagInfoCompulsa && pagInfoCompulsa.exists()){
    		pagInfoCompulsa.delete();
    	}
		
		return ficheroFirmar;
	}

	public static String generaPeticionFirma(Signbatch signbatch) {
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(ObjectFactoryFirmaLotesPeticion.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				
			//Obtener XML en un String
			java.io.StringWriter sw = new StringWriter();
			//Indico esto por que no se cual es la funcion del atributo standalone asi que lo pongo a false para que no aparezca, asi esta en la demo de integracion del servidor de firma
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.marshal(signbatch, sw);				
			result = result.concat(sw.toString());
			
		} catch (JAXBException e) {
			LOGGER.error("ERROR al generar la lista de documentos a firmar. " + e.getMessage(), e);
		}		
		
		return result;
	} 
	
	public static File generaPagInfoCompulsa(CompulsaBean compulsaBean, Document documentoArbol){
		File pagInfoCompulsa = null;
		
		String dir = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + compulsaBean.getUseCaseConf().getEntidadId() + File.separator + "img_exp_fol" + File.separator, "/SIGEM_TramitacionWeb");
		String urlImgFondo = dir + IMG_FONDO;
		String urlImgLogoCabecera = dir + IMG_LOGO_CABECERA;
		String urlImgPie = dir + IMG_PIE;
		
		String num_reg = "";
		String tipoRegistro = "";
		String fechaRegistro = "";
		List<Interesado> interesados = null;
		ScrOrg destino = null;
		ScrOrg origen = null;
		
		if(compulsaBean.getRegisterBean() instanceof InputRegisterBean){
			num_reg = ((InputRegisterBean)compulsaBean.getRegisterBean()).getFld1();
			tipoRegistro = TIPO_REGISTRO_ENTRADA;
			fechaRegistro = new SimpleDateFormat(FORMATO_FECHA).format(((InputRegisterBean)compulsaBean.getRegisterBean()).getFld2());
			interesados = ((InputRegisterBean)compulsaBean.getRegisterBean()).getInteresados();
			origen = ((InputRegisterBean)compulsaBean.getRegisterBean()).getFld7();
			destino = ((InputRegisterBean)compulsaBean.getRegisterBean()).getFld8();
			
		} else if(compulsaBean.getRegisterBean() instanceof OutputRegisterBean){
			num_reg = ((OutputRegisterBean)compulsaBean.getRegisterBean()).getFld1();
			tipoRegistro = TIPO_REGISTRO_SALIDA;
			fechaRegistro = new SimpleDateFormat(FORMATO_FECHA).format(((OutputRegisterBean)compulsaBean.getRegisterBean()).getFld2());
			interesados = ((OutputRegisterBean)compulsaBean.getRegisterBean()).getInteresados();
			origen = ((OutputRegisterBean)compulsaBean.getRegisterBean()).getFld7();
			destino = ((OutputRegisterBean)compulsaBean.getRegisterBean()).getFld8();
		}
		
		try {
			ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
			ieci.tecdoc.sgm.core.services.entidades.Entidad entidadAdm = servicioEntidades.obtenerEntidad(compulsaBean.getUseCaseConf().getEntidadId());
			
			pagInfoCompulsa = new File(FileTemporaryManager.getInstance().getFileTemporaryPath(), compulsaBean.getUseCaseConf().getEntidadId() + "_" + tipoRegistro + "_" + num_reg + SUFIJO_PAGINA_COMPULSA);
			
			FileOutputStream fos = new FileOutputStream(pagInfoCompulsa);
			
			com.lowagie.text.Document documento = new com.lowagie.text.Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(documento, fos);
			documento.open();
			
			Image imagen = null;
			
			try {
				imagen = Image.getInstance(urlImgLogoCabecera);
				imagen.setAbsolutePosition(50, documento.getPageSize().getHeight() - 100);
				imagen.scalePercent(50);
				documento.add(imagen);
			} catch (Exception e) {
				LOGGER.error( "ERROR no se ha encontrado la imagen de logo de la entidad: " + urlImgLogoCabecera + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de logo de la entidad: " + urlImgLogoCabecera + ". " + e.getMessage(), e);
			}

			// Añadimos la imagen del fondo
			try {
				imagen = Image.getInstance(urlImgFondo);
				imagen.setAbsolutePosition(250, 50);
				imagen.scalePercent(70);
				documento.add(imagen);
			} catch (Exception e) {
				LOGGER.error("ERROR no se ha encontrado la imagen de fondo: " + urlImgFondo + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de fondo: " + urlImgFondo + ". " + e.getMessage(), e);
			}

			// Añadimos el pie de página de la diputación
			try {
				imagen = Image.getInstance(urlImgPie);
				imagen.setAbsolutePosition( documento.getPageSize().getWidth() - 550, 15);
				imagen.scalePercent(80);
				documento.add(imagen);
			} catch (Exception e) {
				LOGGER.error( "ERROR no se ha encontrado la imagen de pie de página: " + urlImgPie + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de pie de página: " + urlImgPie + ". " + e.getMessage(), e);
			}
									
			documento.add(new Paragraph(new Phrase("\n")));
			documento.add(new Paragraph(new Phrase("\n")));
			
			Paragraph parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_CENTER);
			parrafo.add(new Phrase(TITULO_COMPULSA, FUENTE_TITULO));			
			documento.add(parrafo);
			
			documento.add(new Paragraph(new Phrase("\n")));
			documento.add(new Paragraph(new Phrase("\n")));
			
			parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
			parrafo.add(new Phrase(ENTIDAD, FUENTE_NEGRITA));
			parrafo.add(new Phrase(entidadAdm.getNombreLargo(), FUENTE_NORMAL));
			documento.add(parrafo);
			
			parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
			parrafo.add(new Phrase(TIPO_REGISTRO, FUENTE_NEGRITA));
			parrafo.add(new Phrase(tipoRegistro, FUENTE_NORMAL));
			documento.add(parrafo);
			
			parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
			parrafo.add(new Phrase(NUMERO_REGISTRO, FUENTE_NEGRITA));
			parrafo.add(new Phrase(num_reg, FUENTE_NORMAL));
			documento.add(parrafo);
			
			parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
			parrafo.add(new Phrase(FECHA_REGISTRO, FUENTE_NEGRITA));
			parrafo.add(new Phrase(fechaRegistro, FUENTE_NORMAL));		
			documento.add(parrafo);
			
			parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
			parrafo.add(new Phrase(FUNCIONARIO_HABILITADO, FUENTE_NEGRITA));
			parrafo.add(new Phrase(compulsaBean.getDniFirmante(), FUENTE_NORMAL));		
			documento.add(parrafo);
			
			if(null != origen){
				parrafo = new Paragraph();
				parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
				parrafo.add(new Phrase(ORIGEN, FUENTE_NEGRITA));
				parrafo.add(new Phrase(origen.getName(), FUENTE_NORMAL));		
				documento.add(parrafo);	
			}
			
			if(null != destino){
				parrafo = new Paragraph();
				parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
				parrafo.add(new Phrase(DESTINO, FUENTE_NEGRITA));
				parrafo.add(new Phrase(destino.getName(), FUENTE_NORMAL));		
				documento.add(parrafo);	
			}
			
			if(null != interesados && 0 < interesados.size()){
				parrafo = new Paragraph();
				parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
				parrafo.add(new Phrase(INTERESADOS, FUENTE_NEGRITA));
				documento.add(parrafo);
				
				for(Interesado interesado : interesados){
					String nombreCompleto = "";
					
					if(StringUtils.isNotEmpty(interesado.getDocIdentidad())){
						nombreCompleto += interesado.getDocIdentidad() + " - ";
					}
					
					if(StringUtils.isNotEmpty(interesado.getNombre())){
						nombreCompleto += interesado.getNombre();
					}
					if(StringUtils.isNotEmpty(interesado.getPapellido())){
						nombreCompleto += " " + interesado.getPapellido();
					}
					if(StringUtils.isNotEmpty(interesado.getSapellido())){
						nombreCompleto += " " +interesado.getSapellido();
					}
					
					parrafo = new Paragraph();
					parrafo.setAlignment(Element.ALIGN_JUSTIFIED);				
					parrafo.add(new Phrase(nombreCompleto, FUENTE_NORMAL));		
					documento.add(parrafo);
				}
			}
			
			parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
			parrafo.add(new Phrase(TIPO_DOCUMENTAL, FUENTE_NEGRITA));
			parrafo.add(new Phrase(documentoArbol.getMetadatos().getTipoDocumental(), FUENTE_NORMAL));
			documento.add(parrafo);
			
			parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
			parrafo.add(new Phrase(ESTADO_ELABORACIÓN, FUENTE_NEGRITA));
			parrafo.add(new Phrase(documentoArbol.getMetadatos().getEstadoElaboracion(), FUENTE_NORMAL));
			documento.add(parrafo);
						
			documento.close();
			
		} catch (ISPACException e) {
			LOGGER.error("ERROR al generar la página con la información de la consulta. " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error("ERROR al generar la página con la información de la consulta. " + e.getMessage(), e);
		} catch (DocumentException e) {
			LOGGER.error("ERROR al generar la página con la información de la consulta. " + e.getMessage(), e);
		} catch (SigemException e) {
			LOGGER.error("ERROR al generar la página con la información de la consulta. " + e.getMessage(), e);
		}
		
		return pagInfoCompulsa;
	}
	
	public static String getUrlCompulsa(CompulsaBean compulsaBean) {
		String urlCompulsa = "";
		
		Integer folderId = getFdrId(compulsaBean);
		String sessionPId = compulsaBean.getUseCaseConf().getSessionID();

		String urlServer = compulsaBean.getUrlServer();		

		String sessionId = compulsaBean.getSessionId();
		String urlCheckForUpdates = Configurator.getInstance().getProperty(ConfigurationKeys.KEY_URL_CHECK_FOR_UPDATES_COMPULSA);
		String locale = compulsaBean.getUseCaseConf().getLocale().getLanguage();

		String argumentosInvocacionCompulsa = "config=false" + "&" +
				"bookId=" + compulsaBean.getBookId() + "&" +
				"folderId=" + folderId + "&" +
				"sessionPId=" + sessionPId + "&" +				
				"destinoUpload=" + DESTINO_UPLOAD + "&" +
				"sessionId=" + sessionId + "&" +
				"urlCheckForUpdates=" + urlCheckForUpdates + "&" +
				"entidadId=" + compulsaBean.getUseCaseConf().getEntidadId() + "&" +				
				"lang=" + locale;
									 			
		urlCompulsa = urlServer + PATH_UPLOAD + "?" + argumentosInvocacionCompulsa;
			
		return urlCompulsa;
	}
	
	public static Integer getFdrId(CompulsaBean compulsaBean){
		
		Integer folderId = new Integer(-1);
		
		if(compulsaBean.getRegisterBean() instanceof InputRegisterBean){
			folderId = ((InputRegisterBean)compulsaBean.getRegisterBean()).getFdrid();
			
		} else if(compulsaBean.getRegisterBean() instanceof OutputRegisterBean){
			folderId = ((OutputRegisterBean)compulsaBean.getRegisterBean()).getFdrid();
		}
		
		return folderId;
	}

	public static boolean hayDocumentosParaCompulsar(Document documento) {
		boolean hayDocs = false;
		
		if(null != documento && documento.isEsDocumento()){
			hayDocs = !documento.isEsFirmado();
		} else {
			List<Document> docs = documento.getListaDocumentos();
			
			for (int i = 0; i < docs.size() && !hayDocs; i++) {
	    		hayDocs = hayDocumentosParaCompulsar(docs.get(i));	    		
	    	}
		}
		
		return hayDocs;
	}
}

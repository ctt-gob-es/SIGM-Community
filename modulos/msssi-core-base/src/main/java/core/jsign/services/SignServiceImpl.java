package core.jsign.services;

import ieci.tecdoc.sgm.autenticacion.MessagesUtil;
import ieci.tecdoc.sgm.autenticacion.vo.ReceiptVO;
import ieci.tecdoc.sgm.base.base64.Base64Util;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.cripto.firma.CertificadoX509Info;
import ieci.tecdoc.sgm.core.services.cripto.firma.ServicioFirmaDigital;
import ieci.tecdoc.sgm.core.services.cripto.validacion.InfoCertificado;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ResultadoValidacion;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ServicioCriptoValidacion;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.gestioncsv.CodigosAplicacionesConstants;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSV;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSVForm;
import ieci.tecdoc.sgm.core.services.gestioncsv.ServicioGestionCSV;
import ieci.tecdoc.sgm.core.services.tiempos.ServicioTiempos;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import beans.SignResponse;
import beans.ValidateSignResponse;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignature;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;

import core.ws.jsign.SignRequest;
import core.ws.jsign.ValidateSignRequest;

public class SignServiceImpl {
    
    private static final Logger LOGGER = Logger.getLogger(SignServiceImpl.class);
    
    protected static final String DEFAULT_LOCATION = "Es";
    protected static final String INFODOCUMENTO_CSV_TIPO_MIME = "application/pdf";
    
    public static final String DOCUMENTO_BANDA_LATERAL_BACKGROUND_IMAGE_DEFAULT = "/ieci/tecdoc/sgm/autenticacion/resources/fondo.gif";
    
    private static final int SIZE = 32000;
    
    // Recursos para la banda lateral a incluir en el justificante de registro
    protected static final String DOCUMENTO_RESOURCE_DOCUMENTO_FIRMADO_POR_KEY = "documento.confirmacion.documentoFirmadoPor";
    protected static final String DOCUMENTO_RESOURCE_FECHA_FIRMA_POR_KEY = "documento.confirmacion.fechaFirma";
    protected static final String DOCUMENTO_RESOURCE_CSV_KEY = "documento.confirmacion.csv";
    protected static final String DOCUMENTO_RESOURCE_PAGINACION_KEY = "documento.confirmacion.paginacion";
    protected static final String DOCUMENTO_RESOURCE_FIRMA_RAZON_KEY="documento.confirmacion.firma.razon";

    
    public static SignResponse sign(SignRequest rq){
        SignResponse srp = new SignResponse();
        
        try {
            ReceiptVO receiptVO = sign(rq.getFileData(), rq.getFileName(), rq.getEntidadId()); 
                    
            srp.setSignData(receiptVO.getContent());
            srp.setDocumentId(receiptVO.getCsv());
            
            setFechaFirma(receiptVO, srp);
            
        } catch (Exception e) {
            LOGGER.error("Error. " + e.getMessage(), e);
        }
        return srp;
    }

    private static void setFechaFirma(ReceiptVO receiptVO, SignResponse srp) {
        try{
            if(null != receiptVO.getFechaFirma()){
                srp.setFechaFirma(receiptVO.getFechaFirma());
            }
        } catch(Exception e){
            LOGGER.info("No hace nada, solo captura el posible error: " + e.getMessage(), e);
        }
    }

    public static ValidateSignResponse validateSign(ValidateSignRequest request) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static ReceiptVO sign(byte[] data, String fileName, String idEntidad) throws SigemException { 
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("sign(InputStream, String, String, CertificadoFirmaX509Info) - start");
        }

        /*
         * 1.- Generar el justificante sin firmar
         *
         * 2.- Generar el CSV del justificante
         *
         * 3.- Añadir la banda lateral con el CSV
         *
         * 4.- Añadir la firma electrónica al pdf
         */


        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfStamper pdfStamper = null;
        PdfReader pdfReader = null;
        Rectangle signatureRectangle = new Rectangle(230, 45, 380, 75);
        PdfSignatureAppearance pdfSignatureAppearance;

        ServicioFirmaDigital servicioFirmaDigital = LocalizadorServicios.getServicioFirmaDigital();
        ServicioTiempos servicioTiempos = LocalizadorServicios.getServicioTiempos();
        ServicioGestionCSV servicioGestionCSV = LocalizadorServicios.getServicioGestionCSV();        

        // Obtener la entidad:
        LOGGER.debug("JustificantePDF.sign: Entidad obtenida del objeto MultiEntityContextHolder: [" + idEntidad + "]");
        Entidad entidad = new Entidad();
        entidad.setIdentificador(idEntidad);

        ReceiptVO receiptVO = new ReceiptVO();

        Calendar signDate = Calendar.getInstance();
        signDate.setTime(servicioTiempos.getCurrentDate());

        byte[] bytesFirma = null;

        try {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("JustificantePDF.sign: Generado el justificante sin firmar satisfactoriamente.");
            }
            

            /*
             * 2.- Generar el CSV
             */
            InfoDocumentoCSVForm infoDocumentoForm = new InfoDocumentoCSVForm();
            infoDocumentoForm.setCodigoAplicacion(CodigosAplicacionesConstants.REGISTRO_PRESENCIAL_CODE);
            infoDocumentoForm.setDisponible(true);
            // La fecha de caducidad es null porque nunca caduca
            infoDocumentoForm.setFechaCaducidad(null);
            infoDocumentoForm.setFechaCreacion(signDate.getTime());
            infoDocumentoForm.setNombre(fileName);
            infoDocumentoForm.setTipoMime(INFODOCUMENTO_CSV_TIPO_MIME);

            // Generar el CSV
            InfoDocumentoCSV infoDocumento = servicioGestionCSV.generarCSV(entidad,    infoDocumentoForm);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("JustificantePDF.sign: Generado el CSV: [" + infoDocumento.getCsv() + "] para el documento ID: [" + infoDocumento.getId() + "].");
            }

            receiptVO.setCsv(infoDocumento.getCsv());
            receiptVO.setContent(data);
            if(null != signDate){
                receiptVO.setFechaFirma(signDate.getTime());
            }

            /*
             * 3.- Generar la banda lateral con el CSV
             */
            receiptVO = incluirBandaLateralDocumentoPDF(signDate.getTime(), receiptVO);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("JustificantePDF.sign: Generada la banda lateral satisfactoriamente.");
            }

            /*
             * 4.- Generar la firma
             */
            pdfReader = new PdfReader(receiptVO.getContent());

            pdfStamper = PdfStamper.createSignature(pdfReader, output, '\0');
            pdfSignatureAppearance = pdfStamper.getSignatureAppearance();

            pdfSignatureAppearance.setLayer2Font(new Font(Font.HELVETICA, 12, Font.BOLDITALIC, new Color(0, 0, 0)));
            pdfSignatureAppearance.setLayer2Text(" ");
            pdfSignatureAppearance.setLayer4Text(" ");
            pdfSignatureAppearance.setVisibleSignature(signatureRectangle, pdfReader.getNumberOfPages(), null);


            pdfSignatureAppearance.setSignDate(signDate);

            Locale locale = new Locale("ES", "es");
            String justificanteConfirmacionMessage = MessagesUtil.getMessage(DOCUMENTO_RESOURCE_FIRMA_RAZON_KEY, new Object[] {}, locale);
            pdfSignatureAppearance.setReason(justificanteConfirmacionMessage);
            pdfSignatureAppearance.setLocation(DEFAULT_LOCATION);

            PdfSignature pdfSignature = new PdfSignature(PdfName.ADOBE_PPKMS, PdfName.ADBE_PKCS7_DETACHED);

            if (pdfSignatureAppearance.getReason() != null){
                pdfSignature.setReason(pdfSignatureAppearance.getReason());
            }
            if (pdfSignatureAppearance.getLocation() != null){
                pdfSignature.setLocation(pdfSignatureAppearance.getLocation());
            }

            pdfSignature.setDate(new PdfDate(pdfSignatureAppearance.getSignDate()));

            pdfSignatureAppearance.setCryptoDictionary(pdfSignature);

            HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
            exc.put(PdfName.CONTENTS, Integer.valueOf(SIZE * 2 + 2));

            pdfSignatureAppearance.preClose(exc);

            byte[] datosAFirmar = streamToByteArray(pdfSignatureAppearance.getRangeStream());

            bytesFirma = Base64Util.decode(servicioFirmaDigital.firmar(datosAFirmar));

            byte[] outc = new byte[SIZE];
            System.arraycopy(bytesFirma, 0, outc, 0, bytesFirma.length);
            PdfDictionary dic2 = new PdfDictionary();
            dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
            pdfSignatureAppearance.close(dic2);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("JustificantePDF.sign: Generada la firma electrónica");
            }

        } catch (Exception e) {

            // Si se produce algún error
            // anular el CSV si ya fue generado
            if (receiptVO.getCsv() != null) {

                // Eliminar el CSV
                try {
                    InfoDocumentoCSV infoDocumentoCSV = servicioGestionCSV.getInfoDocumentoByCSV(entidad, receiptVO.getCsv());
                    if (infoDocumentoCSV != null) {
                        // Eliminar la información de CSV del documento
                        servicioGestionCSV.deleteInfoDocumento(entidad, infoDocumentoCSV.getId());
                    }
                } catch (Exception ex) {
                    LOGGER.error("JustificantePDF.sign: Error al eliminar el CSV", ex);
                }
            }

            LOGGER.error("JustificantePDF.sign: Error en la firma del justificante", e);
            throw new SigemException("ERROR al firmar: " + e.getMessage(), e);
        } finally {
            if (pdfReader != null){
                pdfReader.close();
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("sign(InputStream, String, String, CertificadoFirmaX509Info) - end");
        }

        receiptVO.setContent(output.toByteArray());

        return receiptVO;
    }
    
    /**
     * Añade una banda lateral con el CSV en el documento pdf del justificante
     *
     * @param fechaFirma
     *            Fecha en la que se genera el CSV
     * @param receiptVO
     *            Contenido del justificante del documento con su CSV
     * @return Contenido del justificante del documento con su CSV
     * @throws SigemException 
     */
    private static ReceiptVO incluirBandaLateralDocumentoPDF(Date fechaFirma, ReceiptVO receiptVO) throws SigemException{
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("incluirBandaLateralDocumentoPDF(Date, ReceiptVO) - start");
        }

        float bandSize = 40F;
        float bandMargin = 10F;
        PdfReader reader = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InfoCertificado infoCertificado = null;

        try {

            // Obtenemos la información del certificado
            ServicioFirmaDigital firmaDigital = LocalizadorServicios.getServicioFirmaDigital();
            CertificadoX509Info certificadoX509Info = firmaDigital.getcertInfo();

            if (certificadoX509Info != null) {
                ServicioCriptoValidacion servicioCriptoValidacion = LocalizadorServicios.getServicioCriptoValidacion("SIGEM_ServicioValidacion.SIGEM.API");
                
                String psB64Certificate = Base64.encodeBase64String(certificadoX509Info.getCertificate().getEncoded());

                ResultadoValidacion resultado = servicioCriptoValidacion
                        .validateCertificate(psB64Certificate);

                if (resultado.getResultadoValidacion() == ResultadoValidacion.VALIDACION_ERROR) {
                    LOGGER.info("Justificante de Registro - incluirBandaLateralDocumentoPDF: El certificado de la firma no es valido. Certificado: ["
                            + certificadoX509Info.getCertificate() + "]");
                } else {
                    infoCertificado = resultado.getCertificado();
                }
            }

            // Leer el documento PDF de justificante de registro
            reader = new PdfReader(receiptVO.getContent());
            int numberOfPages = reader.getNumberOfPages();
            int pageHeight = (int) reader.getPageSize(numberOfPages).getHeight();

            // Documento PDF con la banda lateral
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, output);

            // Abrir el documento PDF para incluir el documento de justificante
            // de registro junto con la banda lateral
            document.open();

            // Tamaño de página del documento PDF con la banda lateral
            Rectangle documentPageSize = document.getPageSize();

            // Locale para los recursos a incluir en la banda lateral
            Locale locale = new Locale("ES", "es");

            // Incluir la banda lateral en todas las páginas del documento
            // original
            for (int i = 1; i <= numberOfPages; i++) {
                // Página del documento original
                PdfImportedPage importedPage = writer.getImportedPage(reader, i);
                // Escalar la imagen de la página original para dejar espacio
                // para la banda lateral
                Image imageImportedPage = Image.getInstance(importedPage);
                imageImportedPage.setAbsolutePosition(bandSize, 0.0F);
                imageImportedPage.scaleAbsoluteWidth(documentPageSize.getWidth() - bandSize);
                imageImportedPage.scaleAbsoluteHeight(documentPageSize.getHeight());
                // Página original escalada en el documento final
                document.add(imageImportedPage);

                // Obtener la imagen de fondo para la banda lateral
                Image imageBandBackground = obtenerImagenFondoBandaLateral();

                // Establecer el fondo de la banda lateral
                if (imageBandBackground != null) {

                    // Rotar la imagen de fondo para el lateral
                    imageBandBackground.setRotationDegrees(90F);

                    for (int yPosition = 0; yPosition < pageHeight; yPosition = (int) ((float) yPosition + imageBandBackground
                            .getWidth())) {

                        // Incluir repetidamente la imagen de fondo una
                        // detrás de otra hasta completar el alto de la
                        // página y escalar al ancho de la banda lateral
                        imageBandBackground.setAbsolutePosition(0.0F, yPosition);
                        imageBandBackground.scaleAbsoluteHeight(bandSize);
                        // Fondo de la banda escalado en el documento final
                        document.add(imageBandBackground);
                    }
                }

                // Contenido del PDF sobre el que se escribirá el texto de la
                // banda lateral
                PdfContentByte overPdfContentByte = writer.getDirectContent();
                overPdfContentByte.beginText();

                // Fuente para el texto
                String font = "Helvetica";
                String encoding = BaseFont.WINANSI;
                float fontSize = 8F;

                BaseFont baseFont = BaseFont.createFont(font, encoding, false);
                overPdfContentByte.setFontAndSize(baseFont, fontSize);

                float xPosition = bandMargin;

                overPdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, xPosition, bandMargin);

                // Establecer el texto de la banda lateral

                // Certificado con el que se firma el justificante:
                if (infoCertificado != null) {
                    overPdfContentByte.showText(MessagesUtil.getMessage(
                            DOCUMENTO_RESOURCE_DOCUMENTO_FIRMADO_POR_KEY, new Object[] {
                                    StringUtils.defaultIfEmpty(infoCertificado.getName(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getNif(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getCorporateName(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getCif(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getIssuer(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getSerialNumber(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getSubject(), "")
                              },
                            locale));

                    xPosition += fontSize;
                    overPdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, xPosition, bandMargin);

                }

                // Fecha junto con el certificado con el que se firma el justificante:
                if (infoCertificado != null) {
                    overPdfContentByte.showText(MessagesUtil.getMessage(
                            DOCUMENTO_RESOURCE_FECHA_FIRMA_POR_KEY, new Object[] {
                                    StringUtils.defaultIfEmpty(infoCertificado.getName(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getNif(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getCorporateName(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getCif(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getIssuer(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getSerialNumber(), ""),
                                    StringUtils.defaultIfEmpty(infoCertificado.getSubject(), ""),
                                    formatearFechaFirma(fechaFirma)
                              },
                            locale));
                } else {
                    overPdfContentByte.showText(MessagesUtil.getMessage(
                            DOCUMENTO_RESOURCE_FECHA_FIRMA_POR_KEY,
                            new Object[] {"", "", "", "", "", "", "", formatearFechaFirma(fechaFirma) }, locale));
                }

                // CSV:
                xPosition += fontSize;
                overPdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, xPosition, bandMargin);
                overPdfContentByte.showText(MessagesUtil.getMessage(DOCUMENTO_RESOURCE_CSV_KEY,
                        new Object[] { receiptVO.getCsv() }, locale));

                // Paginación:
                xPosition += fontSize;
                overPdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, xPosition, bandMargin);

                overPdfContentByte.showText(MessagesUtil.getMessage(
                        DOCUMENTO_RESOURCE_PAGINACION_KEY, 
                        new Object[] { i, numberOfPages },
                        locale));

                overPdfContentByte.endText();

                // Indicador de fin de página para añadir una nueva página en el
                // documento PDF final con la banda lateral
                document.newPage();
            }

            document.close();

            // Actualizamos el contenido del documento
            receiptVO.setContent(output.toByteArray());

        } catch (Exception e) {

            LOGGER.error("JustificantePDF.incluirBandaLateralDocumentoPDF: Error al generar la banda lateral del justificante", e);
            throw new SigemException("Error al incluir la banda lateral: " + e.getMessage(), e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("incluirBandaLateralDocumentoPDF(Date, ReceiptVO) - end");
        }
        return receiptVO;
    }
    
    private static byte[] streamToByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        byte[] buffer1 = new byte[8192];
        int c = stream.read(buffer1);
        while (c > 0) {
            byteArray.write(buffer1, 0, c);
            
            c = stream.read(buffer1);
        }
        byteArray.flush();
        return byteArray.toByteArray();
    }
    
    /**
     * Obtener la imagen de fondo para la banda lateral del justificante de
     * registro. Inicialmente, la imagen se busca a partir de la configuración
     * (subdirectorio de recursos e imagen), y si ésta no existe, se carga una
     * imagen por defecto.
     *
     * @return Imagen para el fondo de la banda lateral si existe, en caso
     *         contrario, devuelve null.
     * @throws IOException 
     * @throws BadElementException 
     * @throws Exception
     *             Si se produce algún error.
     */
    protected static Image obtenerImagenFondoBandaLateral() throws IOException, BadElementException {

        Image imageBandBackground = null;

        InputStream inputStream = SignServiceImpl.class
                .getResourceAsStream(DOCUMENTO_BANDA_LATERAL_BACKGROUND_IMAGE_DEFAULT);

        byte[] bytes = IOUtils.toByteArray(inputStream);
        imageBandBackground = Image.getInstance(bytes);

        return imageBandBackground;
    }
    
    /**
     * Formatear la fecha de emisión de la firma a cadena.
     *
     * @param fechaFirma
     *            Fecha de emisión de la firma.
     * @return Fecha formateada.
     */
    protected static String formatearFechaFirma(Date fechaFirma) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:ss");

        return simpleDateFormat.format(fechaFirma);
    }
    
    private SignServiceImpl(){
    }

}

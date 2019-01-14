package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.lowagie.text.Image;
import com.lowagie.text.Jpeg;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.codec.Base64;

import es.dipucr.sigem.sgm.tram.sign.DipucrSignConnector;

public class PdfFirmaUtils {
	
	public static final String[] FONT_FAMILIES = {"Courier", "Helvetica", "Times Roman", "Symbol", "ZapfDingBats"};
	
	/**
	 * Logger
	 */
	public static final Logger LOGGER = Logger.getLogger(PdfFirmaUtils.class);


	public static void addImagenFirma(IClientContext cct, String entityId, InfoFirmante infofirmante, IItem itemDocument, 
			PdfStamper stp, int numberOfPages, FileOutputStream fileOut, int numberOfSigners, int signerPosition, Date dFechaFirma) throws Exception{

		String documentTypeId = itemDocument.getString("ID_TPDOC");
		Properties propertiesExtra = FirmaLotesUtil.getPropertiesFirmaExtraParams
				(entityId, cct, infofirmante, documentTypeId, null, numberOfSigners, signerPosition, dFechaFirma);
		
		String imageDataBase64 = propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.RUBRIC_IMAGE);
		final byte[] image = Base64.decode(imageDataBase64);
		int page = Integer.valueOf(propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.PAGE));
		int lowerLeftX = Integer.valueOf(propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.POSITION_LOWERLEFT_X));
		int lowerLeftY = Integer.valueOf(propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.POSITION_LOWERLEFT_Y));
		int upperRigthX = Integer.valueOf(propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.POSITION_UPPERRIGHT_X));
		int upperRightY = Integer.valueOf(propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.POSITION_UPPERRIGHT_Y));
		
		int left = lowerLeftX;
		int bottom = lowerLeftY;
		int width = upperRigthX - lowerLeftX;
		int height = upperRightY - lowerLeftY;
		
		if (page == FirmaLotesUtil.END_PAGE){
			page = numberOfPages;
		}
		addImagen(image, width, height, left, bottom, page, stp);

		String texto = propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.TEXT);
		int ifontFamily = Integer.valueOf(propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.FONT_FAMILY));
		String fontFamily = FONT_FAMILIES[ifontFamily];
		int fontSize = Integer.valueOf(propertiesExtra.getProperty(FirmaLotesUtil.EXTRAPARAMS.SIGNATURE.FONT_SIZE)); 
		
		//pequeño desplazamiento para cuadrar con la firma
		left = left + 2;
		bottom = bottom + 2;
		escribeTexto(stp, left, false, bottom, page, texto, fontFamily, fontSize);
		
//		stp.close();
	}
	
	public static void addImagen(final byte[] jpegImage,
            final int width,
            final int height,
            final int left,
            final int bottom,
            final int pageNum,
            final PdfStamper stp) throws IOException {
		
		final PdfContentByte content = stp.getOverContent(pageNum);
		try {
			final Image image = new Jpeg(jpegImage);
			
			content.addImage(image, // Image
					width, // Image width
					0, 0, height, // Image height
					left, // Lower left X position of the image
					bottom, // Lower left Y position of the image
					false // Inline
			);
		} catch (Exception e) {
			throw new IOException(
					"Error durante la insercion de la imagen en el PDF: " + e, e); //$NON-NLS-1$
		}
	}
	
	
	/**
	 * 
	 * @param pdfContentByte
	 * @param margen
	 * @param rotate
	 * @param bottom
	 * @param pageActual
	 * @param texto
	 * @param font
	 * @param fontSize
	 * @throws ISPACException
	 */
	protected static void escribeTexto(PdfStamper stp, float left, boolean rotate, float bottom, 
			int pageActual, String texto, String fontFamily, float fontSize) throws ISPACException {
		
		try {
		
			String encoding = ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.ENCODING_BAND);
			
			PdfContentByte pdfContentByte = stp.getOverContent(pageActual);
			BaseFont bf = BaseFont.createFont(fontFamily, encoding, false);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(bf, fontSize);
			
			fontSize += 3F;
			
			if (rotate){
				pdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, bottom, left);
				pdfContentByte.showText(texto);
			}
			else{
	            pdfContentByte.setTextMatrix(left, bottom);
	            pdfContentByte.showText(texto);
			}
			pdfContentByte.endText();
			
		} catch (Exception e) {
			LOGGER.error("Error al componer la imagen de la firma", e);
			throw new ISPACException(e);
		}
	}
	
	
}

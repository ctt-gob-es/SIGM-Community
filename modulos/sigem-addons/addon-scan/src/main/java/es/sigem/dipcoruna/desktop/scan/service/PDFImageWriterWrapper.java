package es.sigem.dipcoruna.desktop.scan.service;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Ojo. Esta clase manitene información de estado, nunca debe llamarse de forma concurrente.
 *
 */
@Component("pdfImageWriterWrapper")
public class PDFImageWriterWrapper implements ImageWriterWrapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(PDFImageWriterWrapper.class);

	private Document document;
	private PdfWriter writer;
	private File tmpFile;
	private boolean seEscribioFichero;


	@Override
	public void init() {
	    try {	        
	        document = new Document(PageSize.A4, 5, 5, 5, 5);
	        tmpFile = File.createTempFile("sigem-scan", ".tmp.pdf");
	        FileOutputStream fos = new FileOutputStream(tmpFile);	        
	        writer = PdfWriter.getInstance(document, fos);
	        
	        document.open();
	        writer.open();
	        
	        seEscribioFichero = false;	        
        } catch (final Exception e) {
            LOGGER.error("Error al inicializar el writer", e);
        }
	}


	@Override
	public void write(final BufferedImage bufferedImage) {
	    LOGGER.debug("Se va a escribir la imagen capturada al documento {}", bufferedImage);
        try {
          Image image = getImageEscalada(bufferedImage);                   
          document.add(image);
          seEscribioFichero = true;
        }
        catch (Exception e) {
            LOGGER.error("Error al convertir la imagen adquirda a PDF", e);
        }        
	}
	
	private Image getImageEscalada(final BufferedImage bufferedImage) throws IOException, BadElementException {	    
	    final ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        os.close();
        
        LOGGER.debug("Se ha convertido la imagen capturada a un outputStream de tamaño {} bytes", os.size()); 
        
        Image image = Image.getInstance(os.toByteArray());
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / image.getWidth()) * 100;
        image.scalePercent(scaler);
    
        return image;
	}


	@Override
	public void finishWrite(){
	    LOGGER.debug("Finalizando captura de documento");
		try {
		    document.close();
	        writer.close();	    
		} catch (final Exception e) {
			LOGGER.error("Error al finalizar la secuencia de escritura", e);
		}				
	}

	@Override
	public boolean seEscribioFichero() {
		return seEscribioFichero;
	}


	@Override
	public File getFile() {
		return tmpFile;
	}
}

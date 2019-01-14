package es.sigem.dipcoruna.desktop.scan.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

@Service("pdfService")
public class PdfServiceImpl implements PdfService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfServiceImpl.class);
    
    @Override
    public File joinPdfs(List<File> files) {
        LOGGER.info("Se van a agrupar en un único PDF los siguientes archivos: {}", files);
        try {
            File destino = File.createTempFile("sigem-scan", ".tmp.pdf");
            OutputStream os = new FileOutputStream(destino);        
            doMerge(files, os);
            return destino;
        }
        catch(Exception e) {
            LOGGER.info("Error al unir los siguientes archivos: {}", files, e);
            throw new RuntimeException("Error al unir pdfs", e);
        }
        
    }

    
    
    private void doMerge(List<File> files, OutputStream outputStream) throws DocumentException, IOException {
        final Document document = new Document();
        final PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        final PdfContentByte pdfContentByte = writer.getDirectContent();
        
        for (File file : files) {
            InputStream is = new FileInputStream(file);
            PdfReader reader = new PdfReader(is);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                PdfImportedPage page = writer.getImportedPage(reader, i);
                pdfContentByte.addTemplate(page, 0, 0);
            }            
            is.close();
        }

        outputStream.flush();
        document.close();
        outputStream.close();
    }

}

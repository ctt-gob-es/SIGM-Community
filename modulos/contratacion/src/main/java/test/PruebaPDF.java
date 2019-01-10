package test;

import ieci.tdw.ispac.api.errors.ISPACException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfAcroForm;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class PruebaPDF {
	
	private static final Logger LOGGER = Logger.getLogger(PruebaPDF.class);
	
	public static boolean _NIVEL_SEGURIDAD_POR_DEFECTO = PdfWriter.STRENGTH128BITS;
	
	public static int _PERMISOS_TOTALES = PdfWriter.AllowCopy | PdfWriter.AllowPrinting
	| PdfWriter.AllowModifyContents | PdfWriter.AllowScreenReaders | PdfWriter.AllowModifyAnnotations
	| PdfWriter.AllowFillIn | PdfWriter.AllowAssembly | PdfWriter.AllowDegradedPrinting
	| PdfWriter.AllowDegradedPrinting | PdfWriter.AllowDegradedPrinting;


	public static void main(String[] args) {
		
		try {
			
			Document doc = new Document();
	        PdfWriter writer = PdfWriter.getInstance(doc,new FileOutputStream("EncryptedPDFAllowAssemblyFillInScreenReadersModifyContents.pdf"));
	       // writer.removeUsageRights();
//	        PdfAcroForm acroForm = writer.getAcroForm();
	        doc.open();
	        doc.add(new Paragraph("Hello World"));
//	        PdfContentByte cb = writer.getDirectContent();
//	        cb.beginText();
//	        acroForm.addSignature("Contratista", 90, 100, 290, 200); 
//	        cb.endText();
	        doc.close();
			
			//encriptarPdf(new File("EncryptedPDFAllowAssemblyFillInScreenReadersModifyContents.pdf"), _PERMISOS_TOTALES, null);
			
		}catch (FileNotFoundException e) {
			LOGGER.error("ERROR. " + e.getMessage(), e);
		} catch (DocumentException e) {
			LOGGER.error("ERROR. " + e.getMessage(), e);
		} 
			    
	    
	  }
	
	
	private static void encriptarPdf(File file, int permissions, String password) throws ISPACException {
		
		try{
			InputStream is = (InputStream) new FileInputStream(file);
			PdfReader reader = new PdfReader(is);
			
			if (null != password){
				byte[] bPassword = password.getBytes();
				PdfEncryptor.encrypt(reader, new FileOutputStream(file), bPassword, bPassword, 
	                  permissions, _NIVEL_SEGURIDAD_POR_DEFECTO);
			}
			else{
				PdfEncryptor.encrypt(reader, new FileOutputStream(file), null, null, 
	                    permissions, false);
			}
            reader.close();

		}
		catch (Exception e) {
			throw new ISPACException("Error al encriptar el archivo pdf " + file.getName(), e);
		}
	}
	 

}

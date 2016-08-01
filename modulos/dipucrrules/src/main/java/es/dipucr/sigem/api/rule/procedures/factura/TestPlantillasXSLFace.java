package es.dipucr.sigem.api.rule.procedures.factura;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

public class TestPlantillasXSLFace {

	public static void main (String[] args) throws Exception{
		
		String rutaBase = "C:\\home\\archivos\\FACe\\";
		String nombreFactura = "facturaNumCuenta";
		String rutaFacturaXml = rutaBase + nombreFactura + ".xsig";
		String rutaFacturaPDF = rutaBase + nombreFactura + ".pdf";
		
		File fileFacturaPdf = new File(rutaFacturaPDF);
		generaFactura(rutaBase, rutaFacturaXml, rutaFacturaPDF);
		Desktop.getDesktop().open(fileFacturaPdf);
	}
	
	/**
	 * Método que genera la factura a partir del XML
	 * @param rutaBase
	 * @param rutaXml
	 * @param rutaDestino 
	 * @throws Exception
	 */
	public static void generaFactura(String rutaBase, String rutaXml, String rutaDestino) throws Exception 
	{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document documento = builder.parse(new File(rutaXml));
		
		String versionp=documento.getElementsByTagName("SchemaVersion").item(0).getTextContent();
		
		FileInputStream fis = new FileInputStream(rutaXml);
		byte [] in = new byte [fis.available()];
		fis.read(in);
		fis.close();
			
		String rutaXSL = "";
		//INICIO [dipucr-Felipe (Tisa-Ibiza) #1229]
		if(versionp.trim().toUpperCase().equals("3.2".trim().toUpperCase())){
			rutaXSL = rutaBase + "/UNEDOCS_32_PDF_ES.xsl";
		}
		else if(versionp.trim().toUpperCase().equals("3.2.1".trim().toUpperCase())){
			rutaXSL = rutaBase + "/UNEDOCS_321_PDF_ES.xsl";
		}
		else{
			throw new Exception("Formato de factura no reconocido");
		}//FIN [dipucr-Felipe (Tisa-Ibiza) #1229]
		
		byte [] out= make(in, rutaXSL);
		FileOutputStream fox = new FileOutputStream(rutaDestino);
		fox.write(out);
		fox.close();
	}
	
	/**
	 * 
	 * @param is
	 * @param xslt
	 * @return
	 * @throws Exception
	 */
	public static byte [] make(byte [] is,String xslt) throws Exception
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		make(bais, baos, xslt);
		byte [] out = baos.toByteArray();
		return(out);
	}
	
	/**
	 * Generamos las facturas en PDF
	 * @param is
	 * @param os
	 * @param xslt
	 * @return
	 * @throws Exception
	 */
	public static String make(InputStream is, OutputStream os,String xslt) throws Exception
	{
		try
		{
	      	File xsltFile = new File(xslt);
	        FopFactory fopFactory = FopFactory.newInstance();
	        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            foUserAgent.setAuthor("T-Systems");
            foUserAgent.setCreator("T-Systems");
            
            try {
                // Construct fop with desired output format
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, os);
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));
                // Set the value of a <param> in the stylesheet
                transformer.setParameter("cv", "XXXXXXX");
            
                // Setup input for XSLT transformation
                Source src = new StreamSource(is);
            
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
    
                // Start XSLT transformation and FOP processing                
                transformer.transform(src, res);
                
            }
            finally{
            	os.close();
            }
		}
		catch(Exception e)
		{
	      	throw(e);
		}
		return("1");
	}
}

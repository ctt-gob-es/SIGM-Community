package es.dipucr.arangi.test;


import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.xml.DOMConfigurator;
import org.bouncycastle.ocsp.OCSPRespStatus;

import es.accv.arangi.base.document.InputStreamDocument;
import es.accv.arangi.base.exception.certificate.CertificateCANotFoundException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.InvalidCertificateException;
import es.accv.arangi.base.exception.signature.PDFDocumentException;
import es.accv.arangi.base.exception.signature.RetrieveOCSPException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.exception.signature.SignatureNotFoundException;
import es.accv.arangi.base.signature.PAdESLTVSignature;
import es.accv.arangi.base.signature.PDFSignature;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.OCSPClient;
public class PAdESLTVSignatureTest {
	
	public static void main (String args[]){
    	
    	try {
    		
    		DOMConfigurator.configure("conf/log4j.xml");
    		
    		CAList caList = new CAList(new File("C:/sigem3/arangi/conf/caCertificates"));
    		PDFSignature signature= new  PDFSignature(new File("G:/doc/PADES_Signature.pdf"));    	    
    	    
    		//TSA ACCV
    		PAdESLTVSignature padesSignature = PAdESLTVSignature.completeToPAdESLTV(signature, new URL("http://tss.accv.es:8318/tsa"), caList);
    		//TSA safecreative.org
    		//PAdESLTVSignature padesSignature = PAdESLTVSignature.completeToPAdESLTV(signature, new URL("http://tsa.safecreative.org"), caList);
    		//TSA @firma    		
    		//PAdESLTVSignature padesSignature = PAdESLTVSignature.completeToPAdESLTV(signature, new URL("https://des-afirma.redsara.es:8443/tsamap/TspHttpServer"), caList);
    		padesSignature.save(new File("resultado.pdf"));
    	    
			
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RetrieveOCSPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidCertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NormalizeCertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PDFDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateCANotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HashingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
 
 }

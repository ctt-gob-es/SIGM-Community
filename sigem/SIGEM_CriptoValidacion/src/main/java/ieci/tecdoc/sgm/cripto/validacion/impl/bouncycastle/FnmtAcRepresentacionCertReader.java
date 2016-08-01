package ieci.tecdoc.sgm.cripto.validacion.impl.bouncycastle;

import ieci.tecdoc.sgm.core.services.cripto.validacion.InfoCertificado;




import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;

/**
 * author [DipuCR-Agustin]  #310
 */
public class FnmtAcRepresentacionCertReader implements IReaderCert {
	
	final static String CADENA = "CN=AC Representación";
    final static String CN = "CN=";
    final static String SERIALNUMBER = "SERIALNUMBER=";
    final static String SURNAME = "SURNAME=";
    final static String SURNAME_SEPARADOR = " ";
    final static String GIVENNAME = "GIVENNAME=";
    final static String SEPARADOR_CAMPOS_SUBJECT_DN = ", ";
    final static String SEPARADOR = "-";       

	public InfoCertificado getInfo(X509Certificate cert) {
		
		String nombre="";
		String nombre_sinapellidos;
		String apellidos;	
	    String nif;
	    String cif;
	    String numeroSerie;
	    FNMTCertifParser cp = new FNMTCertifParser();
	    try {
			Map aux = cp.parse(cert);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    nombre_sinapellidos= cp.dameNombre();
	    apellidos = cp.dameApellidos();
	    nif = cp.dameDNI();
	    cif = cp.dameCIF();
	    nombre = nombre_sinapellidos + " " + apellidos;
	    numeroSerie = String.valueOf(cert.getSerialNumber());
	    
	    String issuer = cert.getIssuerDN().toString();
	    String asunto = cert.getSubjectDN().toString(); 		

        InfoCertificado infoCertificado = new InfoCertificado();              
        
        infoCertificado.setCif(cif);
	    infoCertificado.setIssuer(issuer);
	    infoCertificado.setName(nombre);
	    infoCertificado.setFirstname(nombre_sinapellidos);
	    infoCertificado.setNif(nif);
	    infoCertificado.setSerialNumber(numeroSerie);
	    infoCertificado.setSubject(asunto);
	    
	    return infoCertificado;
	}

	public boolean isTypeOf(X509Certificate cert) {
		return cert.getIssuerDN().toString().startsWith(CADENA);
	}	

}

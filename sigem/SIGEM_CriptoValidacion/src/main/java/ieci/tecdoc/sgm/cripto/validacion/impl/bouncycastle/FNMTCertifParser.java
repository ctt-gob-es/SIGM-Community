package ieci.tecdoc.sgm.cripto.validacion.impl.bouncycastle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;


/**
 * author [DipuCR-Agustin]  #310
 */
public class FNMTCertifParser extends ASN1CertParser {
	
	/**
	 * Nombre
	 */
	public static final String FIRST_NAME_OID = "1.3.6.1.4.1.5734.1.1";
	
	/**
	 * Primer apellido
	 */
	public static final String SURNAME_OID = "1.3.6.1.4.1.5734.1.2";
	
	/**
	 * Segundo apellido
	 */
	public static final String SECOND_SURNAME_OID = "1.3.6.1.4.1.5734.1.3";
	
	/**
	 * Nif
	 */
	public static final String DNI_OID = "1.3.6.1.4.1.5734.1.4";
	
	/**
	 * Cif
	 */
	public static final String CIF_OID = "1.3.6.1.4.1.5734.1.7";
	
	/**
	 * Persona Física/Jurídica
	 */
	public static final String TIPO_CERTIFICATE_OID = "1.3.6.1.4.1.5734.1.33";
	public final static String SEPARADOR = "-";  
	
	Map oids = null;
	
	
	public Map parse(X509Certificate x509Cert) throws IOException{
		
		this.oids = this.readPropertiesOid(x509Cert);

	
		return oids;	
	}
	
	public String dameNombre(){
		
		return (String) this.oids.get(FIRST_NAME_OID);
		
	}
	
    public String dameApellidos(){
    	
    	return (String) this.oids.get(SURNAME_OID) + " " + (String) this.oids.get(SECOND_SURNAME_OID) ;
    	
	}
    
    public String dameDNI(){
    	
    	String aux = (String) this.oids.get(DNI_OID);
    	String [] auxObj = aux.split(SEPARADOR);    	
    	return auxObj[1];
    	
	}
    
    public String dameCIF(){
    	
    	String aux = (String) this.oids.get(CIF_OID);
    	String [] auxObj = aux.split(SEPARADOR);    	
    	return auxObj[1];
    	
	}	
	
	/***
	 * Parsea un certificado X509 para extraer todos sus oids
	 * 
	 * @param certificadoX509
	 * @return
	 * @throws IOException 
	 */
	public Map readPropertiesOid(X509Certificate certificadoX509) throws IOException {
		Map propiedadesOid = new HashMap();
		// obtengo los Oids
		Set oids = certificadoX509.getNonCriticalExtensionOIDs();
		
		if (oids != null) {
			// iteramos sobre los Oids // TODO ( este es el mecanismo para FNMT)
			  Iterator itr= oids.iterator();
				while (itr.hasNext()) {
					String oid= (String) itr.next();
				ASN1InputStream aIn = new ASN1InputStream(
							new ByteArrayInputStream(certificadoX509.getExtensionValue(oid)));
				ASN1OctetString extValue = (ASN1OctetString) aIn.readObject();
				aIn = new ASN1InputStream(new ByteArrayInputStream(extValue.getOctets()));
					
				super.readPropiedadesOid(oid, extValue, propiedadesOid);
			}
		}

		// retornamos el conjunto de oids recuperados.
		return propiedadesOid;
	}

	
}
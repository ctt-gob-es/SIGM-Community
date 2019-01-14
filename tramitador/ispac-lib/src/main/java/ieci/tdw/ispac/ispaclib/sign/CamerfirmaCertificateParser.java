package ieci.tdw.ispac.ispaclib.sign;

import ieci.tdw.ispac.api.ISignAPI;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;


/**
 * @author antoniomaria_sanchez at ieci.es
 * @since 08/01/2009
 * 
 */
public class CamerfirmaCertificateParser extends ASN1Parser {
	
		
	/**
	 * Nombre
	 */
	public static final String FIRST_NAME_OID = "1.3.6.1.4.1.17326.30.7";
	
	/**
	 * Primer apellido
	 */
	public static final String SURNAME_OID = "1.3.6.1.4.1.17326.30.8";
	
	/**
	 * Segundo apellido
	 */
	public static final String SECOND_SURNAME_OID = "1.3.6.1.4.1.17326.30.9";
	
	/**
	 * Nif
	 */
	public static final String DNI_OID = "";
	
	/**
	 * Persona Física/Jurídica
	 */
	public static final String TIPO_CERTIFICATE_OID = "";	
	
	
	
	public static final String IDENTIFICADOR_CAMERFIRMA_OID = "2.5.4.10";	
	
	public static final String POLITICA_CAMERFIRMA_OID ="1.3.6.1.5.5.7.2.1";
	
	public static final String POLITICA_CAMERFIRMA_URL_OID ="https://policy.camerfirma.com";
	
	

	
}

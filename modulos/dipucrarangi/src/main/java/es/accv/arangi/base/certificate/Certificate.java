/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.certificate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.ietf.ldap.LDAPDN;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.certificate.data.CertificateDataService;
import es.accv.arangi.base.certificate.validation.OCSPClient;
import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationServiceResult;
import es.accv.arangi.base.exception.certificate.CertificateFieldException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.util.AlternativeNameElement;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase base de todos los certificados de Arangi. Ofrece métodos para trabajar
 * de forma cómoda con los diferentes campos del certificado.<br><br>
 * 
 * Los certificados permitidos son aquellos que cumplen el estándar definido en
 * la <a href="http://tools.ietf.org/rfc/rfc5280.txt" target="rfc">RFC-5280</a>: x.509 v3.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class Certificate extends ArangiObject{

	/**
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(Certificate.class);
	
	/**
	 * OID del campo OCSP de la extensión AIA
	 */
	public static final String OID_OCSP_AIA_EXTENSION	= "1.3.6.1.5.5.7.48.1";
	
	/**
	 * OID del campo id-at-commonName
	 */
	public static final String OID_ID_AT_COMMONNAME	= "2.5.4.3";
	
	/**
	 * OID del campo Userid
	 */
	public static final String OID_USERID	= "0.9.2342.19200300.100.1.1";
	
    private static final String[] DN_OBJECTS = {
        "unstructuredaddress", "unstructuredname", "emailaddress", "e", "email", "uid", "cn", "sn", "serialnumber", "gn", "givenname",
        "initials", "surname", "t", "ou", "o", "l", "st", "dc", "c"
    };
    
	/*
	 * Date parser
	 */
	protected static SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
	
	/**
	 * Objeto certificado en X.509 al que recubre esta clase
	 */
	protected X509Certificate certificate;
	
	/**
	 * Representación del certificado en ASN1
	 */
	org.bouncycastle.asn1.x509.Certificate asn1Certificate;
	
	/**
	 * Flag que indica si el certificado está autofirmado
	 */
	private Boolean selfSigned;
	
    /** BC X509Name contains some lookup tables that could maybe be used here. */
    private static final HashMap<String,ASN1ObjectIdentifier> oids = new HashMap<String,ASN1ObjectIdentifier>();

    static {
        oids.put("c", BCStyle.C);
        oids.put("dc", BCStyle.DC);
        oids.put("st", BCStyle.ST);
        oids.put("l", BCStyle.L);
        oids.put("o", BCStyle.O);
        oids.put("ou", BCStyle.OU);
        oids.put("t", BCStyle.T);
        oids.put("surname", BCStyle.SURNAME);
        oids.put("initials", BCStyle.INITIALS);
        oids.put("givenname", BCStyle.GIVENNAME);
        oids.put("gn", BCStyle.GIVENNAME);
        oids.put("sn", BCStyle.SN);
        oids.put("serialnumber", BCStyle.SN);
        oids.put("cn", BCStyle.CN);
        oids.put("uid", BCStyle.UID);
        oids.put("emailaddress", BCStyle.EmailAddress);
        oids.put("e", BCStyle.EmailAddress);
        oids.put("email", BCStyle.EmailAddress);
        oids.put("unstructuredname", BCStyle.UnstructuredName); //unstructuredName 
        oids.put("unstructuredaddress", BCStyle.UnstructuredAddress); //unstructuredAddress
    }
    
    private static final String[] dNObjects = {
        "unstructuredaddress", "unstructuredname", "emailaddress", "e", "email", "uid", "cn", "sn", "serialnumber", "gn", "givenname",
        "initials", "surname", "t", "ou", "o", "l", "st", "dc", "c"
    };
    
	/**
	 * Constructor que inicializa la clase mediante un certificado en formato
	 * X.509
	 * 
	 * @param certificate Certificado en formato X.509
	 * @throws NormalizeCertificateException No se ha podido normalizar el certificado para
	 * 	ser usado con el proveedor criptográfico de Arangi
	 */
	public Certificate(X509CertificateHolder certificate) throws NormalizeCertificateException {
		super();
		
		try {
			initialize (Util.getCertificate(certificate));
		} catch (CertificateException e) {
			logger.info("[Certificate]::No se puede pasar de la estructura al certificado X.509");
			throw new NormalizeCertificateException("No se puede pasar de la estructura al certificado X.509");
		}
	}
	
	/**
	 * Constructor que inicializa la clase mediante un certificado en formato
	 * X.509
	 * 
	 * @param certificate Certificado en formato X.509
	 * @throws NormalizeCertificateException No se ha podido normalizar el certificado para
	 * 	ser usado con el proveedor criptográfico de Arangi
	 */
	public Certificate(X509Certificate certificate) throws NormalizeCertificateException {
		super();
		
		initialize (certificate);
	}
	
	/**
	 * Constructor que inicializa la clase mediante un fichero. El fichero debe contener
	 * un certificado X.509 (tal cual o en formato PEM)
	 * 
	 * @param fileCertificate Fichero que contiene un certificado en formato X.509
	 * @throws NormalizeCertificateException No se ha podido normalizar el certificado para
	 * 	ser usado con el proveedor criptográfico de Arangi
	 * @throws FileNotFoundException El fichero no existe
	 */
	public Certificate(File fileCertificate) throws NormalizeCertificateException, FileNotFoundException {
		super();
		
		//-- Obtener el certificado
		X509Certificate certificate = Util.getCertificate(fileCertificate);
		if (certificate == null) {
			logger.info("[Certificate]::El fichero no es un certificado digital");
			throw new NormalizeCertificateException("El fichero no es un certificado digital");
		}
		
		//-- Cargar el certificado
		this.certificate = normalize (certificate);
		
		//-- Inicializar
		initialize(certificate);
	}
	
	/**
	 * Constructor que inicializa la clase a partir de un stream de lectura. El stream debe 
	 * contener un certificado X.509 (tal cual o en formato PEM)
	 * 
	 * @param isCertificate Stream de lectura a un certificado en formato X.509
	 * @throws NormalizeCertificateException No se ha podido normalizar el certificado para
	 * 	ser usado con el proveedor criptográfico de Arangi
	 */
	public Certificate(InputStream isCertificate) throws NormalizeCertificateException {
		super();
		
		//-- Obtener el certificado
		X509Certificate certificate = Util.getCertificate(isCertificate);
		if (certificate == null) {
			logger.info("[Certificate]::El fichero no es un certificado digital");
			throw new NormalizeCertificateException("El fichero no es un certificado digital");
		}
		
		//-- Cargar el certificado
		this.certificate = normalize (certificate);
		
		//-- Inicializar
		initialize(certificate);
	}
	
	/**
	 * Constructor que inicializa la clase a partir de un array de bytes. Los bytes deben 
	 * ser el contenido de un certificado X.509 (tal cual o en formato PEM)
	 * 
	 * @param contenidoCertificado Contenido de un certificado en formato X.509
	 * @throws NormalizeCertificateException No se ha podido normalizar el certificado para
	 * 	ser usado con el proveedor criptográfico de Arangi
	 */
	public Certificate(byte[] contenidoCertificado) throws NormalizeCertificateException {
		super();
		
		//-- Obtener el certificado
		ByteArrayInputStream bais = new ByteArrayInputStream (contenidoCertificado);
		X509Certificate certificate = Util.getCertificate(bais);
		if (certificate == null) {
			logger.info("[Certificate]::El fichero no es un certificado digital");
			throw new NormalizeCertificateException("El fichero no es un certificado digital");
		}
		
		//-- Cargar el certificado
		this.certificate = normalize (certificate);
		
		//-- Inicializar
		initialize(certificate);
	}
	

	//-- Métodos públicos
	
	/**
	 * Método que devuelve si el certificado es autofirmado
	 * 
	 * @return Cierto si está autofirmado
	 * @throws NormalizeCertificateException Errores en la firma del certificado
	 */
	public boolean isSelfSigned() throws NormalizeCertificateException {
		logger.debug("[Certificate.isSelfSigned]:: Inicio ");

		if (this.selfSigned == null) {
			PublicKey pubKey = certificate.getPublicKey();
			try {
				certificate.verify(pubKey, CRYPTOGRAPHIC_PROVIDER_NAME);
				logger.debug("[Certificate.isSelfSigned]::Es autofirmado ");
				
				this.selfSigned = new Boolean (true);
	
			} catch (InvalidKeyException e) {
				// Is not self signed
				logger.debug("[Certificate.isSelfSigned]::No es autofirmado");
				this.selfSigned = new Boolean (false);
			} catch (CertificateException e) {
				logger.info ("[Certificate.isSelfSigned]::La firma del certificado no está correctamente codificada", e);
				throw new NormalizeCertificateException ("La firma del certificado no está correctamente codificada", e);
			} catch (NoSuchAlgorithmException e) {
				logger.info ("[Certificate.isSelfSigned]::El algoritmo de la firma del certificado no está soportado", e);
				throw new NormalizeCertificateException ("El algoritmo de la firma del certificado no está soportado", e);
			} catch (NoSuchProviderException e) {
				//-- No se debería dar
				logger.info ("[Certificate.isSelfSigned]::No existe el proveedor criptográfico de Arangi", e);
				throw new NormalizeCertificateException ("No existe el proveedor criptográfico de Arangi", e);
			} catch (SignatureException e) {
				// Is not self signed
				logger.debug("[Certificate.isSelfSigned]::No es autofirmado");
				this.selfSigned = new Boolean (false);
			} 
		}
		
		return this.selfSigned.booleanValue();
	}

	/**
	 * Método que obtiene el OID de la política del certificado. 
	 * 
	 * @return OID de la política del certificado o nulo si no existe la
	 * 	extensión CertificatePolicies
	 */
	public String getPolicyOID() {
		
		logger.debug("[Certificate.getPolicyOID]::Entrada");

		List<String> policyOIDs = getPolicyOIDs();
		if (policyOIDs.isEmpty()) {
			return null;
		}
		
		return policyOIDs.get(0);
	}
	
	/**
	 * Método que obtiene los OIDs de la política del certificado. 
	 * 
	 * @return OIDs de la política del certificado
	 */
	public List<String> getPolicyOIDs() {
		
		logger.debug("[Certificate.getPolicyOIDs]::Entrada");
		
		List<String> policyOIDs = new ArrayList<String>();

		//-- Accedemos a la extensión del certificatePolicies.
		byte[] extension = certificate.getExtensionValue(Extension.certificatePolicies.getId());
		
		if (extension == null) {
			return policyOIDs;
		}
		//-- Obtenemos las políticas
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(extension);
			ASN1InputStream asn1IS = new ASN1InputStream(bais);
			
			bais = new ByteArrayInputStream(((ASN1OctetString) asn1IS.readObject()).getOctets());
			asn1IS = new ASN1InputStream(bais);
			
			Enumeration enumeration = ((ASN1Sequence) asn1IS.readObject()).getObjects();
			while (enumeration.hasMoreElements()) {
				Enumeration enumeration2 = ((ASN1Sequence) enumeration.nextElement()).getObjects();
			
				String policy = ((DERObjectIdentifier) enumeration2.nextElement()).getId();
				logger.debug("[Certificate.getPolicyOIDs]::Una política del certificado es" + policy);
				policyOIDs.add(policy);
			}
			
			return policyOIDs;
			
		} catch (Exception e) {
			logger.info("[Certificate.getPolicyOIDs]::No es posible obtener la extensión del certificado", e);
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Método que obtiene el Issuer Key Identifier (IKI) del certificado
	 * 
	 * @return Issuer Key Identifier o nulo si éste no se pudo obtener del certificado
	 */
	public String getIssuerKeyIdentifier()  {
		return Certificate.getIssuerKeyIdentifier(this.certificate);
	}

	/**
	 * Método que obtiene el Subject Key Identifier (SKI) a partir del certificado
	 * 
	 * @return Subject Key Identifier o nulo si éste no se pudo obtener del certificado
	 */
	public String getSubjectKeyIdentifier() { 
		return Certificate.getSubjectKeyIdentifier(this.certificate);
	}
	
	/**
	 * El método obtiene las URLs de las CRLs indicadas en el certificado. Para ello consulta
	 * la extensión CRL Distribution Points (CDP). Si dicha extensión no existe se devolverá
	 * un array sin ningún elemento. 
	 * 
	 * @return URLs de las CRLs del certificado
	 * @throws CertificateFieldException Existe la extensión CDP pero no se puede leer 
	 */
	public String [] getCrlUrls() throws CertificateFieldException{
		
		logger.debug("[Certificate.getCrlUrls]::Entrada");
		
		DERIA5String lDERObjCRL_URL;
		byte[] extBytes = certificate.getExtensionValue(Extension.cRLDistributionPoints.getId());
		
		// En caso de no tener contenido, devolvemos un array vacío.
		if (extBytes == null) {
			logger.debug("[Certificate.getCrlUrls]::No existe la extensión CDP en el certificado");
			return new String[0];
		}
		
		// Load ASN1 object
		ASN1InputStream ais = new ASN1InputStream(new ByteArrayInputStream(extBytes));
		
		try {
			DEROctetString oct = (DEROctetString) ais.readObject();
			ais = new ASN1InputStream(new ByteArrayInputStream(oct.getOctets()));
			CRLDistPoint cdp = CRLDistPoint.getInstance((ASN1Sequence) ais.readObject());
			// [BC-135] - point.getDistributionPoints();
			DistributionPoint[] points = cdp.getDistributionPoints();
			
			ArrayList alURLs = new ArrayList();
			for (int i = 0; i < points.length; i++) {
				DistributionPoint point = points[i];
				// [BC-135] - point.getDistributionPoint();
				DistributionPointName name = point.getDistributionPoint();
				// [BC-135] - getName() y getNames()
				GeneralName[] gns = ((GeneralNames) name.getName()).getNames();
				for (int j = 0; j < gns.length; j++) {
					GeneralName gn = gns[j];
					// Si lo que nos llega es una URL, se manda la URL extraida
					// [BC-135] - GeneralName.uniformResourceIdentifier
					if (gn.getTagNo() == GeneralName.uniformResourceIdentifier) {
						lDERObjCRL_URL = (DERIA5String) ((DERTaggedObject)gn.toASN1Primitive().toASN1Object()).getObject(); 
						alURLs.add (lDERObjCRL_URL.getString());
					} else if (gn.getTagNo() == GeneralName.directoryName) {
					}
				}
			}
			
			//-- Return list of CRL URLs
			return (String []) alURLs.toArray(new String [0]);
		
		} catch (IOException e) {
			logger.info("[Certificate.getCrlUrls]::La extensión CDP en el certificado existe pero no se puede leer", e);
			throw new CertificateFieldException ("La extensión CDP en el certificado existe pero no se puede leer", e);
		}
	}
	
	/**
	 * El método obtiene las URLs de los OCSPs que se indican en el certificado. Para ello consulta
	 * la extensión Authority Info Access (AIA). Si dicha extensión no existe se devolverá
	 * un array sin ningún elemento. 
	 * 
	 * @return URLs de los OCSPs del certificado
	 * @throws CertificateFieldException Existe la extensión AIA pero no se puede leer 
	 */
	public String [] getOcspUrls() throws CertificateFieldException{
		
		byte[] extBytes = certificate.getExtensionValue(Extension.authorityInfoAccess.getId());
		
		// En caso de no tener contenido, devolvemos una cadena vacia. Deberíamos lanzar una excepcion.
		if (extBytes == null) {
			logger.debug("[Certificate.getCrlUrls]::No existe la extensión AIA en el certificado");
			return new String[0];
		}
		
		// Load ASN1 object
		ASN1InputStream ais = new ASN1InputStream(new ByteArrayInputStream(extBytes));
		
		try {
			DEROctetString oct = (DEROctetString) ais.readObject();
			ais = new ASN1InputStream(new ByteArrayInputStream(oct.getOctets()));
			AuthorityInformationAccess aia = AuthorityInformationAccess.getInstance((ASN1Sequence) ais.readObject());
			List<String> urls = new ArrayList<String>();
			for (int i=0; i<aia.getAccessDescriptions().length; i++) {
				AccessDescription accessDescription = aia.getAccessDescriptions() [i];
				if (accessDescription.getAccessLocation().getTagNo() == GeneralName.uniformResourceIdentifier &&
						accessDescription.getAccessMethod().getId().equals(OID_OCSP_AIA_EXTENSION)) {
					DERIA5String lDERObjCRL_URL = (DERIA5String) ((DERTaggedObject)accessDescription.getAccessLocation().toASN1Object()).getObject();
					urls.add(lDERObjCRL_URL.getString());
				}
			}

			return (String[]) urls.toArray(new String[0]);
			
		} catch (IOException e) {
			logger.info("[Certificate.getCrlUrls]::La extensión AIA en el certificado existe pero no se puede leer", e);
			throw new CertificateFieldException ("La extensión AIA en el certificado existe pero no se puede leer", e);
		}
	}
	
	/**
	 * Método que comprueba la validez del certificado respecto a la fecha y hora
	 * actual del sistema. 
	 * 
	 * @return cierto si el fichero está dentro de su intervalo de validez
	 */
	public boolean isActive() {
		
		logger.debug ("[Certificate.isCertificateInValidityInterval]::Entrada");
		
		try {
			certificate.checkValidity();
		} catch (CertificateNotYetValidException e) {
			logger.debug ("[Certificate.isCertificateInValidityInterval]::El certificado aun no es válido");
			return false;
		} catch (CertificateExpiredException e) {
			logger.debug ("[Certificate.isCertificateInValidityInterval]::El certificado ya no es válido");
			return false;
	    }

	    return true;
	}
	
	/**
	 * Método que comprueba si el certificado aun no ha llegado a su periodo de
	 * validez.
	 * 
	 * @return cierto si el fichero aún no ha llegado a su periodo de validez
	 */
	public boolean isNotYetActive() {
		
		logger.debug ("[Certificate.isNotYetActive]::Entrada");
		
		try {
			certificate.checkValidity();
		} catch (CertificateNotYetValidException e) {
			logger.debug ("[Certificate.isNotYetActive]::El certificado aun no es válido");
			return true;
		} catch (CertificateExpiredException e) {
			logger.debug ("[Certificate.isNotYetActive]::El certificado ya no es válido");
			return false;
	    }

	    return false;
	}
	
	/**
	 * Método que comprueba si el certificado ya está caducado: ha sobrepasado su periodo
	 * de validez
	 * 
	 * @return cierto si el fichero está caducado
	 */
	public boolean isExpired() {
		
		logger.debug ("[Certificate.isExpired]::Entrada");
		
		try {
			certificate.checkValidity();
		} catch (CertificateNotYetValidException e) {
			logger.debug ("[Certificate.isExpired]::El certificado aun no es válido");
			return false;
		} catch (CertificateExpiredException e) {
			logger.debug ("[Certificate.isExpired]::El certificado ya no es válido");
			return true;
	    }

	    return false;
	}
	
	/**
	 * Obtiene la fecha de comienzo del periodo de validez del certificado
	 * 
	 * @return Fecha de comienzo del periodo de validez del certificado
	 */
	public Date getValidityPeriodBeginning () {
		
		logger.debug ("[Certificate.getValidityPeriodBeginning]::Entrada");
		
		return certificate.getNotBefore();
	}
	
	/**
	 * Obtiene la fecha de finalización del periodo de validez del certificado
	 * 
	 * @return Fecha de finalización del periodo de validez del certificado
	 */
	public Date getValidityPeriodEnd () {
		
		logger.debug ("[Certificate.getValidityPeriodEnd]::Entrada");
		
		return certificate.getNotAfter();
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave la firma digital
	 * 
	 * @return Cierto si el certificado tiene como uso de clave la firma digital
	 */
	public boolean isKeyUsageDigitalSignature () {
		
		logger.debug ("[Certificate.isKeyUsageDigitalSignature]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[0];
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave el no repudio
	 * 
	 * @return Cierto si el certificado tiene como uso de clave el no repudio
	 */
	public boolean isKeyUsageNonRepudiation () {
		
		logger.debug ("[Certificate.isKeyUsageNonRepudiation]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[1];
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave el cifrado de claves
	 * 
	 * @return Cierto si el certificado tiene como uso de clave el cifrado de claves
	 */
	public boolean isKeyUsageKeyEncipherment () {
		
		logger.debug ("[Certificate.isKeyUsageKeyEncipherment]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[2];
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave el cifrado de datos
	 * 
	 * @return Cierto si el certificado tiene como uso de clave el cifrado de datos
	 */
	public boolean isKeyUsageDataEncipherment () {
		
		logger.debug ("[Certificate.isKeyUsageDataEncipherment]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[3];
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave el acuerdo de claves
	 * 
	 * @return Cierto si el certificado tiene como uso de clave el acuerdo de claves
	 */
	public boolean isKeyUsageKeyAgreement () {
		
		logger.debug ("[Certificate.isKeyUsageKeyAgreement]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[4];
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave la firma de certificados
	 * 
	 * @return Cierto si el certificado tiene como uso de clave la firma de certificados
	 */
	public boolean isKeyUsageKeyCertSign () {
		
		logger.debug ("[Certificate.isKeyUsageKeyCertSign]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[5];
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave la firma de CRLs
	 * 
	 * @return Cierto si el certificado tiene como uso de clave la firma de CRLs
	 */
	public boolean isKeyUsageCRLSign () {
		
		logger.debug ("[Certificate.isKeyUsageCRLSign]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[6];
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave sólo el cifrado
	 * 
	 * @return Cierto si el certificado tiene como uso de clave sólo el cifrado
	 */
	public boolean isKeyUsageEncipherOnly () {
		
		logger.debug ("[Certificate.isKeyUsageEncipherOnly]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[7];
	}
	
	/**
	 * Comprueba si el certificado tiene como uso de clave sólo el descifrado
	 * 
	 * @return Cierto si el certificado tiene como uso de clave sólo el descifrado
	 */
	public boolean isKeyUsageDecipherOnly () {
		
		logger.debug ("[Certificate.isKeyUsageDecipherOnly]::Entrada");
		
		if (certificate.getKeyUsage() == null) {
			return true;
		}
		
		return certificate.getKeyUsage()[8];
	}
	
	/**
	 * Obtiene la lista de usos de clave extendidos. Las constantes con los tipos
	 * definidos en el estándar se encuentran en org.bouncycastle.asn1.x509.KeyPurposeId.
	 * Por ejemplo, si queremos saber si el certificado tiene la extensión de firma de
	 * OCSP:
	 * <code>
	 * 	if (certificate.getExtendedKeyUsage().contains (KeyPurposeId.id_kp_OCSPSigning.getId())) { ... }
	 * </code>
	 * 
	 * @return Lista de usos de clave extendidos
	 * @throws CertificateException La extensión no puede ser descodificada
	 */
	public List<String> getExtendedKeyUsage () throws es.accv.arangi.base.exception.CertificateException {
		
		logger.debug ("[Certificate.getExtendedKeyUsage]::Entrada");
		
		try {
			List<String> result = certificate.getExtendedKeyUsage();
			if (result == null) {
				return new ArrayList<String>();
			}
			return result;
		} catch (CertificateParsingException e) {
			logger.info("[Certificate.getExtendedKeyUsage]::La extensión no puede ser descodificada", e);
			throw new es.accv.arangi.base.exception.CertificateException("La extensión no puede ser descodificada", e);
		} 
	}
	
	/**
	 * Método que devuelve la huella Digital (FingerPrint) del certificado. Es
	 * el mismo resultado de llamar a {@link #getDigest(java.lang.String) getDigest} más
	 * una transformación del resultado a hexadecimal.<br><br>
	 * 
	 * El FingerPrint ha sido creado utilizando como algoritmo de hashing "SHA1".
	 * 
	 * @return String que contienen la huella digital del certificado.
	 */
	public String getFingerPrint() {
		return getFingerPrint(DEFAULT_HASHING_ALGORITHM);
	}
	
	/**
	 * Método que devuelve la huella Digital (FingerPrint) del certificado. Es
	 * el mismo resultado de llamar a {@link #getDigest(java.lang.String) getDigest} más
	 * una transformación del resultado a hexadecimal.
	 * 
	 * @param hashingAlgorithm Algoritmo de hash a utilizar
	 * @return String que contienen la huella digital del certificado.
	 */
	public String getFingerPrint(String hashingAlgorithm) {
		
		logger.debug ("[Certificate.getFingerPrint]::Entrada");

	    try {
	    	// Obtenemos el hash del certificado
	    	byte[] lBytesHash = getDigest(hashingAlgorithm);
	    	
	    	// Convertimos a Hexadecimal...
	    	byte[] lBytesHash_HEX = Hex.encode(lBytesHash);
	    	
	    	// Obtenemos la representación en String del Hash en Hexadecimal...
	    	String lStrHash = new String(lBytesHash_HEX);
	    	
	    	return lStrHash.toUpperCase();
	    } catch (CertificateEncodingException e0) {
	    	logger.info ("[Certificate.getFingerPrint]::Error de codificación del certificado");
	    	return "";
	    } catch (NoSuchAlgorithmException e2) {
	    	logger.info ("[Certificate.getFingerPrint]::No se encuentra el algoritmo " + DEFAULT_HASHING_ALGORITHM);
	    	return "";
	    }
	}
	
	/**
	 * Método que devuelve el digest del certificado.<br><br>
	 * 
	 * El digest ha sido creado utilizando con el algoritmo de hashing "SHA1".
	 * 
	 * @return String que contienen el digest del certificado.
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hashing para
	 * 	el proveedor criptográfico de Arangí
	 * @throws CertificateEncodingException Error obteniendo la codificación
	 * 	DER del documento
	 */
	public byte[] getDigest() throws CertificateEncodingException, NoSuchAlgorithmException {
		return getDigest(DEFAULT_HASHING_ALGORITHM);
	}
	
	/**
	 * Método que devuelve el digest del certificado.
	 * 
	 * @param hashingAlgorithm Algoritmo de hash a utilizar
	 * @return String que contienen el digest del certificado.
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hashing para
	 * 	el proveedor criptográfico de Arangí
	 * @throws CertificateEncodingException Error obteniendo la codificación
	 * 	DER del documento
	 */
	public byte[] getDigest(String hashingAlgorithm) throws NoSuchAlgorithmException, CertificateEncodingException {
		
		logger.debug ("[Certificate.getDigest]::Entrada::" + hashingAlgorithm);

    	// Obtenemos un objeto para crear "hash" de datos utilizando una
    	MessageDigest lObjHashMaker = MessageDigest.getInstance(hashingAlgorithm);
    	
    	// Obtenemos el hash del certificado
    	return lObjHashMaker.digest(certificate.getEncoded());
	    	
	}
	
	/**
	 * Obtiene elementos del Asunto (Subject) del certificado en base a su OID.<br><br>
	 * 
	 * Por ejemplo, para obtener el campo GIVENNAME (G) del Asunto (Subject):<br>
	 * <code>
	 * 	certificate.getElementSubject(X509Name.GIVENNAME)
	 * </code>
	 * 
	 * @param oid OID dele elemento buscado
	 * @return Valor del elemento
	 */
	public String getElementSubject (ASN1ObjectIdentifier oid) {
		
		logger.debug ("[Certificate.getElementSubject]::Buscando OID '" + oid.getId() + "' en el subject");

		X500Name subjectName = this.asn1Certificate.getSubject();
		RDN[] rdns = subjectName.getRDNs(oid);
		if (rdns == null || rdns.length == 0) {
			logger.debug ("[Certificate.getElementSubject]::Para el OID '" + oid.getId() + "' no hay ningún elemento en el subject");
			return null;
		}
		return rdns[0].getFirst().getValue().toString();
	}

	/**
	 * Obtiene el common name que se encuentra en el asunto del certificado
	 * 
	 * @return Common Name
	 */
	public String getCommonName () {
		
		logger.debug ("[Certificate.getCommonName]::Entrada");
		
		return getElementSubject(BCStyle.CN);
	}
	
	/**
	 * Obtiene el país que se encuentra en el asunto del certificado
	 * 
	 * @return País
	 */
	public String getCountry () {
		
		logger.debug ("[Certificate.getCountry]::Entrada");
		
		return getElementSubject(BCStyle.C);
	}
	
	/**
	 * Obtiene la cadena de texto contenida en el Subject del certificado
	 * 
	 * @return Name del Distinguished Name que forma el Subject del certificado
	 */
	public String getSubjectDN () {
		return this.certificate.getSubjectDN().getName();
	}
	
	/**
	 * Obtiene la cadena de texto contenida en el Issuer del certificado
	 * 
	 * @return Name del Distinguished Name que forma el Issuer del certificado
	 */
	public String getIssuerDN () {
		return this.certificate.getIssuerDN().getName();
	}
	
	/**
	 * Obtiene el common name del Issuer del certificado
	 * 
	 * @return Common name del Issuer del certificado
	 */
	public String getIssuerCommonName () {
		
		logger.debug ("[Certificate.getIssuerCommonName]::Entrada");
		
		String dn = this.certificate.getIssuerDN().getName();
		StringTokenizer st = new StringTokenizer(dn, ",");
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.startsWith("CN=")) {
				return token.substring(3);
			}
		}
		
		return null;
	}
	
	/**
	 * Obtiene una lista que representa el contenido de la extensión Subject Alternative 
	 * Name (Nombre Alternativo del Sujeto). Cada elemento es de tipo 
	 * {@link es.accv.arangi.base.util.AlternativeNameElement AlternativeNameElement} 
	 * 
	 * @return Representación del Subject Alternative Name
	 * @throws CertificateFieldException Error leyendo el certificado
	 */
	public List getSubjectAlternativeName() throws CertificateFieldException {
		
		logger.debug ("[Certificate.getSubjectAlternativeName]::Entrada");
		
		byte[] extVal = certificate.getExtensionValue(Extension.subjectAlternativeName.getId());

		try {
			return getAlternativeNames(extVal);
		} catch (CertificateParsingException e) {
			logger.info("[Certificate.getSubjectAlternativeNames]::Error leyendo la extensión SubjectAlternativeName", e);
			throw new CertificateFieldException ("Error leyendo la extensión SubjectAlternativeName", e);
		}
	}
	
	/**
	 * Obtiene el SAN en formato de texto 
	 * 
	 * @return Representación del Subject Alternative Name
	 * @throws CertificateFieldException Error leyendo el certificado
	 */
	public String getSubjectAlternativeNameString() throws CertificateFieldException {
		
		logger.debug ("[Certificate.getSubjectAlternativeNameString]::Entrada");
		
		List<AlternativeNameElement> elementos = getSubjectAlternativeName();
		StringBuffer sb = new StringBuffer("");
		int i=0;
		for (AlternativeNameElement elemento : elementos) {
			switch (elemento.getType()) {
				case GeneralName.ediPartyName:
					break;
				case GeneralName.x400Address:
					break;
				case GeneralName.otherName:
					break;
				case GeneralName.directoryName:
					if (i>0) { sb.append(","); }
					sb.append("directoryName=");
					Map<String,String> dn = (Map<String,String>) elemento.getValue();
					int campos = 0;
					StringBuffer directoryName = new StringBuffer("");
					for(String key : dn.keySet()) {
						if (campos > 0) {directoryName.append(",");}
						directoryName.append("OID." + key + "=" + dn.get(key));
						campos++;
					}
					sb.append(LDAPDN.escapeRDN(directoryName.toString()));
					i++;
					break;
				case GeneralName.dNSName:
					if (i>0) { sb.append(","); }
					sb.append("dNSName=" + elemento.getValue());
					i++;
					break;
				case GeneralName.rfc822Name:
					if (i>0) { sb.append(","); }
					sb.append("rfc822Name=" + elemento.getValue());
					i++;
					break;
				case GeneralName.uniformResourceIdentifier:
					break;
				case GeneralName.registeredID:
					break;
				case GeneralName.iPAddress:
					break;
			}

		}
		
		return sb.toString();
		
	}

	/**
	 * Busca un elemento dentro de la extensión Subject Alternative Name. Normalmente
	 * el vector sólo contendrá el elemento que estamos buscando.<br><br>
	 * Por ejemplo:<br>
	 * <code>
	 * 	(String) certificate.getSubjectAlternativeNameElement("0.9.2342.19200300.100.1.1").get(0)
	 * </code>
	 * 
	 * @param oid OID del elemento a Buscar
	 * @return Valor del elemento
	 * @throws CertificateFieldException Error leyendo el certificado
	 */
	public String getSubjectAlternativeNameElement (String oid) throws CertificateFieldException {
		
		logger.debug ("[Certificate.getSubjectAlternativeNameElement]::Entrada::" + oid);
		
		//-- Obtener la lista
		List lNames = getSubjectAlternativeName();
		
		//-- Iterar
		for (Iterator iterator = lNames.iterator(); iterator.hasNext();) {
			AlternativeNameElement element = (AlternativeNameElement) iterator.next();
			if (element.getValue() instanceof Map) {
				Map<String,String> mapElement = (Map<String,String>) element.getValue();
				if (mapElement.containsKey(oid)) {
					logger.debug ("[Certificate.getSubjectAlternativeNameElement]::Encontrado el elemento::" + mapElement.get(oid));
					return mapElement.get(oid);
				}
			}
		}
		
		logger.debug ("[Certificate.getSubjectAlternativeNameElement]::No se ha encontrado el elemento para el oid " + oid);
		return null;
	}
	
	/**
	 * Obtiene una lista que representa el contenido de la extensión Issuer Alternative 
	 * Name (Nombre Alternativo del Emisor). Cada elemento es de tipo 
	 * {@link es.accv.arangi.base.util.AlternativeNameElement AlternativeNameElement} 
	 * 
	 * @return Representación del Issuer Alternative Name
	 * @throws CertificateFieldException Error leyendo el certificado
	 */
	public List getIssuerAlternativeName() throws CertificateFieldException {
		
		logger.debug ("[Certificate.getIssuerAlternativeNames]::Entrada");
		
		byte[] extVal = certificate.getExtensionValue(Extension.issuerAlternativeName.getId());

		try {
			return getAlternativeNames(extVal);
		} catch (CertificateParsingException e) {
			logger.info("[Certificate.getIssuerAlternativeNames]::Error leyendo la extensión IssuerAlternativeName", e);
			throw new CertificateFieldException ("Error leyendo la extensión IssuerAlternativeName", e);
		}
	}

	/**
	 * Busca un elemento dentro de la extensión Issuer Alternative Name 
	 * 
	 * @param oid OID del elemento a Buscar
	 * @return Vector con la información del elemento
	 * @throws CertificateFieldException Error leyendo el certificado
	 */
	public Vector getIssuerAlternativeNameElement (String oid) throws CertificateFieldException {
		
		logger.debug ("[Certificate.getIssuerAlternativeNameElement]::Entrada::" + oid);
		
		//-- Obtener la lista
		List lNames = getIssuerAlternativeName();
		
		//-- Iterar
		for (Iterator iterator = lNames.iterator(); iterator.hasNext();) {
			AlternativeNameElement element = (AlternativeNameElement) iterator.next();
			if (element.getValue() instanceof Map) {
				Map mapElement = (Map) element.getValue();
				if (mapElement.containsKey(oid)) {
					logger.debug ("[Certificate.getIssuerAlternativeNameElement]::Encontrado el elemento::" + (Vector) mapElement.get(oid));
					return (Vector) mapElement.get(oid);
				}
			}
		}
		
		logger.debug ("[Certificate.getIssuerAlternativeNameElement]::No se ha encontrado el elemento para el oid " + oid);
		return null;
	}
	
	/**
	 * Método que devuelve el Número de Serie del certificado (Hexadecimal)
	 * 
	 * @return Número de Serie del certificado.
	 * 
	 */
	public String getSerialNumber() {

		logger.debug ("[Certificate.getSerialNumber]::Entrada");
		
		return certificate.getSerialNumber().toString(16).toUpperCase();
	}
	
	/**
	 * Método que devuelve el Número de Serie del certificado (BigInteger)
	 * 
	 * @return Número de Serie del certificado.
	 * 
	 */
	public BigInteger getSerialNumberBigInteger() {

		logger.debug ("[Certificate.getSerialNumberBigInteger]::Entrada");
		
		return certificate.getSerialNumber();
	}
	
	/**
	 * Obtiene el issuer and serial number. En algunos casos se utiliza este
	 * valor para identificar el certificado en lugar del subject key
	 * identifier.
	 * 
	 * @return Issuer and serial number
	 * @throws CertificateFieldException No se ha podido leer correctamente la 
	 * 	información del certificado
	 */
	public IssuerAndSerialNumber getIssuerAndSerialNumber() throws CertificateFieldException {
		
		logger.debug ("[Certificate.getIssuerAndSerialNumber]::Entrada");
		
		try {
	        ASN1InputStream inputStreamTbsCertificate = new ASN1InputStream(new ByteArrayInputStream(toX509Certificate().getTBSCertificate()));
	        ASN1Sequence seqAux = (ASN1Sequence)inputStreamTbsCertificate.readObject();
	        ASN1Primitive tbsCertificateDER = (ASN1Primitive)seqAux.getObjectAt(seqAux.getObjectAt(0) instanceof DERTaggedObject ? 3 : 2);
			return new IssuerAndSerialNumber(X500Name.getInstance((ASN1Sequence)tbsCertificateDER),
			   		toX509Certificate().getSerialNumber());
		} catch (CertificateEncodingException e) {
			logger.info("[Certificate.toDER]::Error obteniendo la información del certificado en formato DER", e);
			throw new CertificateFieldException ("Error obteniendo la información del certificado en formato DER", e);
		} catch (IOException e) {
			logger.info("[Certificate.toDER]::Error leyendo la información del certificado en formato DER", e);
			throw new CertificateFieldException ("Error leyendo la información del certificado en formato DER", e);
		} 
	}

	/**
	 * Obtiene la clave pública asociada al certificado
	 * 
	 * @return Clave pública del certificado
	 */
	public PublicKey getPublicKey () {
		
		logger.debug ("[Certificate.getPublicKey]::Entrada");
		
		return certificate.getPublicKey();
	}
	
	/**
	 * Devuelve el certificado en formato X509Certificate
	 * 
	 * @return X509 Certificate
	 */
	public X509Certificate toX509Certificate () {
		return certificate;
	}
	
	/**
	 * Devuelve el certificado en formato DER
	 * 
	 * @return Certificado en formato DER
	 * @throws NormalizeCertificateException No se ha podido codificar el certificado en 
	 * 	formato DER
	 */
	public byte[] toDER () throws NormalizeCertificateException {
		try {
			return certificate.getEncoded();
		} catch (CertificateEncodingException e) {
			logger.info("[Certificate.toDER]::Error codificando el certificado en formato DER", e);
			throw new NormalizeCertificateException ("Error codificando el certificado en formato DER", e);
		}
	}
	
	/**
	 * Obtiene la estructura del certificado en un objeto X509CertificateHolderç
	 * de Bouncy Castle.
	 * 
	 * @return X509CertificateHolder
	 */
	public X509CertificateHolder toX509CertificateHolder () {
		return new X509CertificateHolder(asn1Certificate);
	}
	
	/**
	 * Devuelve el certificado en formato PEM
	 * 
	 * @return Certificado en formato PEM
	 * @throws NormalizeCertificateException No se ha podido codificar el certificado en 
	 * 	formato DER antes de pasarlo a PEM
	 */
	public String toPEM () throws NormalizeCertificateException {
		
		return "-----BEGIN CERTIFICATE-----\n" + Util.encodeBase64(toDER()) + 
			"\n-----END CERTIFICATE-----";
	}
	
	/**
	 * Crea un fichero PKCS#7 en el que almacena el certificado y su cadena de confianza.
	 * 
	 * @param chain Cadena de confianza. Si se pasa un valor nulo sólo se almacenará el
	 * 	certificado.
	 * @return Fichero PKCS#7 en formato DER
	 * @throws SignatureException Errores creando el almacén PKCS#7
	 */
    public byte[] toPKCS7(Certificate[] chain) throws SignatureException {

    	logger.debug("[Certificate.toPKCS7]::Entrada::" + (chain==null?"null":chain.length));
    	
    	//-- Objeto PKCS#7 
    	CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
    	
    	//-- Construir lista de certificados
    	X509Certificate certificate = toX509Certificate();
    	List<X509Certificate> certList = new ArrayList<X509Certificate>();
    	certList.add(certificate);
    	if (chain != null) {
	    	for (int i = 0; i < chain.length; i++) {
				certList.add(chain[i].toX509Certificate());
			}
    	}
    	
    	//-- Unirlo todo y devolver en formato DER
    	try {
    		gen.addCertificates(new JcaCertStore(certList));
    		CMSProcessableByteArray process = new CMSProcessableByteArray(null); //no hay firmas
    		CMSSignedData data = gen.generate(process);
    		ContentInfo contentInfo = data.toASN1Structure();
    		return contentInfo.getEncoded(ASN1Encoding.DER);
    	} catch (CMSException e) {
			logger.info ("[Certificate.toPKCS7]::El certificado no puede ser almacenado en formato PKCS#7", e);
			throw new SignatureException ("El certificado no puede ser almacenado en formato PKCS#7", e);
		} catch (IOException e) {
			logger.info ("[Certificate.toPKCS7]::Excepción de entrada/salida", e);
			throw new SignatureException ("Excepción de entrada/salida", e);
		} catch (CertificateEncodingException e) {
			logger.info ("[Certificate.toPKCS7]::El certificado no puede ser almacenado", e);
			throw new SignatureException ("El certificado no puede ser almacenado", e);
		} 

    }    
    
	/**
	 * Guarda el certificado en formato DER en el fichero que se pasa como parámetro
	 * 
	 * @param file Fichero donde se guardará el certificado
	 * @throws Exception No se ha podido guardar el fichero
	 * @throws NormalizeCertificateException No se ha podido codificar el certificado en 
	 * 	formato DER
	 */
	public void save (File file) throws NormalizeCertificateException, Exception {
		Util.saveFile(file, toDER());
	}
	
	/**
	 * Guarda el certificado en formato PEM en el fichero que se pasa como parámetro
	 * 
	 * @param file Fichero donde se guardará el certificado
	 * @throws Exception No se ha podido guardar el fichero
	 * @throws NormalizeCertificateException No se ha podido codificar el certificado en 
	 * 	formato DER antes de pasarlo a PEM
	 */
	public void saveToPEM (File file) throws NormalizeCertificateException, Exception {
		Util.saveFile(file, toPEM().getBytes());
	}
	
	/**
	 * Devuelve en texto la información más importante del certificado.
	 * 
	 * @return Información del certificado
	 */
	public String toString () {
		return certificate.toString();
	}
	
	//-- Métodos estáticos
	
	/**
	 * Normaliza los certificados de acuerdo al proveedor criptográfico de Arangi.
	 * 
	 * @param originalCert Certificado a normalizar
	 * @return X509Certificate Certificado normalizado de acuerdo al proveedor criptográfico 
	 * de Arangi
	 * 
	 * @throws Exception Error durante la normalización
	 */
	public static X509Certificate normalize (X509Certificate originalCert) throws NormalizeCertificateException {
		
		byte[] bytes;
		try {
			bytes = originalCert.getEncoded();
		} catch (CertificateEncodingException e) {
			logger.info ("[Certificate.normalize]::Error de codificación del certificado", e);
			throw new NormalizeCertificateException ("Error de codificación del certificado", e);
		}
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		CertificateFactory cf;
		try {
			cf = CertificateFactory.getInstance("X.509", CRYPTOGRAPHIC_PROVIDER);
		} catch (CertificateException e) {
			logger.info ("[Certificate.normalize]::El tipo del certificado no está disponible para el proveedor criptográfico de Arangi: " + CRYPTOGRAPHIC_PROVIDER, e);
			throw new NormalizeCertificateException ("El tipo del certificado no está disponible para el proveedor criptográfico de Arangi: " + CRYPTOGRAPHIC_PROVIDER, e);
		} 
		
		X509Certificate normalizedCertificate;
		try {
			normalizedCertificate = (X509Certificate) cf.generateCertificate(bais);
		} catch (CertificateException e) {
			logger.info ("[Certificate.normalize]::Error generando el certificado", e);
			throw new NormalizeCertificateException ("Error generando el certificado", e);
		}
		try {
			bais.close();
		} catch (IOException e) {}
		
		return normalizedCertificate;
		
	}
	
	/**
	 * Método estático que obtiene el Issuer Key Identifier (IKI) a partir de un certificado
	 * X.509.
	 * 
	 * @param certificate Certificado X.509
	 * @return Issuer Key Identifier o nulo si éste no se pudo obtener del certificado
	 */
	public static String getIssuerKeyIdentifier(X509Certificate certificate)  {
		
		byte[] extension = certificate.getExtensionValue(Extension.authorityKeyIdentifier.getId());
		if (extension == null) {
			return null;
		}
	
		ASN1InputStream is1 = null, is2 = null;
		try {
			is1 = new ASN1InputStream(extension);
			byte[] authBytes = ((ASN1OctetString)is1.readObject()).getOctets();
			is2 = new ASN1InputStream(authBytes);
			AuthorityKeyIdentifier aki = AuthorityKeyIdentifier.getInstance(is2.readObject());
			return Util.encodeBase64(aki.getKeyIdentifier());
		} catch (Exception e) {
			logger.debug("[Certificate.getIssuerKeyIdentifier]::No es posible obtener el IKI del certificado: " + certificate.getSubjectDN(), e);
			return null;
		} finally {
			if (is1 != null) { try { is1.close(); } catch (IOException e) { } }
			if (is2 != null) { try { is2.close(); } catch (IOException e) { } }
		}
	}

	/**
	 * Método estático que obtiene el Subject Key Identifier (SKI) a partir de un certificado
	 * X.509.
	 * 
	 * @param certificate X.509 Certificate
	 * @return Subject Key Identifier o nulo si éste no se pudo obtener del certificado
	 */
	public static String getSubjectKeyIdentifier(X509Certificate certificate) { 
		
		byte[] extension = certificate.getExtensionValue(Extension.subjectKeyIdentifier.getId());
		if (extension == null) {
			return null;
		}
		
		ASN1InputStream is1 = null, is2 = null;
		try {
			is1 = new ASN1InputStream(extension);
			byte[] authBytes = ((ASN1OctetString)is1.readObject()).getOctets();
			is2 = new ASN1InputStream(authBytes);
			SubjectKeyIdentifier ski = SubjectKeyIdentifier.getInstance(is2.readObject());
			return Util.encodeBase64(ski.getKeyIdentifier());
		} catch (Exception e) {
			logger.debug("[Certificate.getSubjectKeyIdentifier]::No es posible obtener el SKI del certificado: " + certificate.getSubjectDN(), e);
			return null;
		} finally {
			if (is1 != null) { try { is1.close(); } catch (IOException e) { } }
			if (is2 != null) { try { is2.close(); } catch (IOException e) { } }
		}
	}
	
    /**
     * Crea un objeto (Bouncycastle) X509Name a partir de un String con un DN. OIDs conocidos
     * (en orden) son:
     * <code> EmailAddress, UID, CN, SN (SerialNumber), GivenName, Initials, SurName, T, OU,
     * O, L, ST, DC, C </code>
     * 
     * @param dn String que contiene un DN que puede ser transformado en un X509Name. El String
     *          DN tiene el formato "CN=zz,OU=yy,O=foo,C=SE". OIDs desconocidos en el String
     *          se añadirán al final del array de OIDs
     * 
     * @return X509Name o null si el parámetro es nulo
     */
    public static X500Name stringToBcX500Name(String dn) {

      // log.debug(">stringToBcX509Name: " + dn);
      if (dn == null)
        return null;

      List<RDN> lRDNs = new ArrayList<RDN>();
      StringTokenizer st = new StringTokenizer(dn, ",");

      while (st.hasMoreTokens()) {
        // This is a pair (CN=xx)
        String pair = st.nextToken();
        int ix = pair.indexOf("=");

        if (ix != -1) {
          String key = pair.substring(0, ix).toLowerCase();
          String val = pair.substring(ix + 1);

          // -- First search the OID by name in declared OID's
          ASN1ObjectIdentifier oid = oids.get(key.toLowerCase());

          // -- If isn't declared, we try to create it
          if (oid == null) {
            oid = new ASN1ObjectIdentifier(key);
          }

          RDN rdn = new RDN(oid, new DERUTF8String(val));
          lRDNs.add(rdn);

        } else {
          logger.info("[Certificate.stringToBcX509Name]:: Algún par OID=valor no es correcto: " + dn);
        }
      }

      return getOrderedX500Name(new X500Name(lRDNs.toArray(new RDN[0])), getDefaultX500FieldOrder());
    } 
    
    /**
     * Genera un certificado autofirmado con los parámetros que se le pasan. El periodo de
     * validez comienza en el instante actual.
     *
     * @param dn DN del emisor/issuer (es mismo que el del asunto/subject) 
     * @param validity Días de validez
     * @param policyId String con la política o nulo
     * @param privKey Clave privada con la que se firmará el certificado
     * @param pubKey Clave pública
     * @param isCA Si cierto se marcará el certificado como de una autoridad de certificación
     *
     * @return X509Certificate auto firmado
     * @throws NoSuchAlgorithmException No existe el algoritmo SHA-1 Pseudo-Random Number 
     * 	Generation, necesario para obtener el número de serie del certificado
     * @throws SignatureException Error firmando el certificado
     * @throws IllegalStateException 
     * @throws InvalidKeyException La clave privada no es válida
     * @throws CertificateEncodingException Error codificando el nuevo certificado
     */
    public static X509Certificate generateSelfCertificate(String dn, long validity, String policyId,
        PrivateKey privKey, PublicKey pubKey, boolean isCA) throws NoSuchAlgorithmException, CertificateEncodingException, InvalidKeyException, IllegalStateException, SignatureException
    {
    	return generateSelfCertificate(dn, validity, policyId, privKey, pubKey, isCA, ArangiObject.DEFAULT_SIGNING_ALGORITHM);
    }
    
    /**
     * Genera un certificado autofirmado con los parámetros que se le pasan. El periodo de
     * validez comienza en el instante actual.
     *
     * @param dn DN del emisor/issuer (es mismo que el del asunto/subject) 
     * @param validity Días de validez
     * @param policyId String con la política o nulo
     * @param privKey Clave privada con la que se firmará el certificado
     * @param pubKey Clave pública
     * @param isCA Si cierto se marcará el certificado como de una autoridad de certificación
     * @param signatureAlgorithm Algoritmo de la firma del certificado
     *
     * @return X509Certificate auto firmado
     * @throws NoSuchAlgorithmException No existe el algoritmo SHA-1 Pseudo-Random Number 
     * 	Generation, necesario para obtener el número de serie del certificado
     * @throws CertificateEncodingException Error codificando el nuevo certificado
     */
    public static X509Certificate generateSelfCertificate(String dn, long validity, String policyId,
        PrivateKey privKey, PublicKey pubKey, boolean isCA, String signatureAlgorithm) throws NoSuchAlgorithmException, CertificateEncodingException {
    	
        // Create self signed certificate
        Date firstDate = new Date();

        // Set back startdate ten minutes to avoid some problems with wrongly set clocks.
        firstDate.setTime(firstDate.getTime() - (10 * 60 * 1000));

        Date lastDate = new Date();

        // Serialnumber is random bits, where random generator is initialized with Date.getTime() when this
        // bean is created.
        byte[] serno = new byte[8];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed((new Date().getTime()));
        random.nextBytes(serno);
        
       // validity in days = validity*24*60*60*1000 milliseconds
        lastDate.setTime(lastDate.getTime() + (validity * (24 * 60 * 60 * 1000)));
        
        SubjectPublicKeyInfo spki;
		try {
			spki = new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(
			        new ByteArrayInputStream(pubKey.getEncoded())).readObject());
		} catch (IOException e) {
			logger.info("[Certificate.generateSelfCertificate]::No es posible obtener el subject public key info", e);
			throw new CertificateEncodingException ("No es posible obtener el subject public key info", e);
		}
        
        X509v3CertificateBuilder certgen = new X509v3CertificateBuilder(
        		stringToBcX500Name(dn),
        		new java.math.BigInteger(serno),
        		firstDate,
        		lastDate,
        		stringToBcX500Name(dn),
        		spki);

        // Basic constranits is always critical and MUST be present at-least in CA-certificates.
        BasicConstraints bc = new BasicConstraints(isCA);
        try {
			certgen.addExtension(Extension.basicConstraints, true, bc);
		} catch (CertIOException e) {
			logger.info("[Certificate.generateSelfCertificate]:: No se puede añadir la extensión basicConstraints", e);
			throw new CertificateEncodingException ("No se puede añadir la extensión basicConstraints", e);
		}

        // Put critical KeyUsage in CA-certificates
        if (isCA) {
            int keyusage = X509KeyUsage.keyCertSign + X509KeyUsage.cRLSign;
            X509KeyUsage ku = new X509KeyUsage(keyusage);
            try {
				certgen.addExtension(Extension.keyUsage, true, ku);
			} catch (CertIOException e) {
				logger.info("[Certificate.generateSelfCertificate]:: No se puede añadir la extensión keyusage", e);
				throw new CertificateEncodingException ("No se puede añadir la extensión keyusage", e);
			}
        }

        // Subject and Authority key identifier is always non-critical and MUST be present for certificates to verify in Mozilla.
        try {
            if (isCA) {
                SubjectKeyIdentifier ski = new SubjectKeyIdentifier(spki.getEncoded(ASN1Encoding.DER));

                SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(
                            new ByteArrayInputStream(pubKey.getEncoded())).readObject());
                AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(apki);

                certgen.addExtension(Extension.subjectKeyIdentifier, false, ski);
                certgen.addExtension(Extension.authorityKeyIdentifier, false, aki);
            }
        } catch (IOException e) { // do nothing
        }

        // CertificatePolicies extension if supplied policy ID, always non-critical
        if (policyId != null) {
            PolicyInformation pi = new PolicyInformation(new ASN1ObjectIdentifier(policyId));
            DERSequence seq = new DERSequence(pi);
            try {
				certgen.addExtension(Extension.certificatePolicies, false, seq);
			} catch (CertIOException e) {
				logger.debug("[Certificate.generateSelfCertificate]:: No se puede añadir la extensión certificatePolicies", e);
			}
        }

		try {
			ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider(CRYPTOGRAPHIC_PROVIDER_NAME).build(privKey);
	        return Util.getCertificate(certgen.build(sha1Signer));
		} catch (OperatorCreationException e) {
			logger.info("[Certificate.generateSelfCertificate]:: No se puede construir el nuevo certificado", e);
			throw new CertificateEncodingException ("No se puede construir el nuevo certificado", e);
		} catch (CertificateException e) {
			logger.info("[Certificate.generateSelfCertificate]:: No se puede parsear la estructura del nuevo certificado", e);
			throw new CertificateEncodingException ("No se puede parsear la estructura del nuevo certificado", e);
		}
    } 
    
    /**
     * Devolverá cierto si el certificado contiene la extensión "Comprobación 
     * de no revocación". Esta extensión se suele utilizar en certificados de OCSP.
     * 
     * @return Cierto si el certificado contiene la extensión "Comprobación 
     * 	de no revocación". Falso en caso contrario.
     */
    public boolean hasNoRevocationCheck () {
    	logger.debug("[Certificate.hasNoRevocationCheck]::Entrada");
    	boolean result = certificate.getExtensionValue(OCSPObjectIdentifiers.id_pkix_ocsp_nocheck.getId()) != null;
    	logger.debug("[Certificate.hasNoRevocationCheck]::Resultado: " + result);
    	
    	return result;
    }

    /**
     * Valida el certificado a través de servicios de validación como, por ejemplo, 
     * &#64;Firma.
     * 
     * @param validationServices Lista de servicios de validación
     * @return Objeto que incluye el resultado y un map con los campos del certificado.
     * 	Si no es posible realizar la validación se devolverá null.
     */
    public CertificateValidationServiceResult validate (List<CertificateValidationService> validationServices) {
    	return validate (validationServices, new Date());
    }
    
    /**
     * Valida el certificado a través de servicios de validación como, por ejemplo, 
     * &#64;Firma.
     * 
     * @param validationServices Lista de servicios de validación
     * @param validationDate Fecha para la que se realiza la validación
     * @return Objeto que incluye el resultado y un map con los campos del certificado.
     */
    public CertificateValidationServiceResult validate (List<CertificateValidationService> validationServices, Date validationDate) {
    	logger.debug("[Certificate.validate]::Entrada::" + Arrays.asList(new Object[] { validationServices, validationDate } ));
    	
    	CertificateValidationServiceResult result = null;
    	
		//-- Comprobar si está en su periodo de validez
		if (getValidityPeriodBeginning().after(validationDate)) {
			logger.debug ("[Certificate.validate] :: El certificado con subject DN=" + getSubjectDN().toString() + 
				" no estará activo hasta " + dateFormat.format(getValidityPeriodBeginning()) + ".");
			return new CertificateValidationServiceResult (ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE, null);
		}
		if (getValidityPeriodEnd().before(validationDate)) {
			logger.debug ("[Certificate.validate] :: El certificado con subject DN=" + getSubjectDN().toString() + 
				" ha caducado en " + dateFormat.format(getValidityPeriodEnd()) + ".");
			return new CertificateValidationServiceResult (ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE, null);
		}
		
    	//-- Comprobar si es autofirmado
		try {
			if (isSelfSigned()) {
				logger.debug ("[Certificate.validate] :: Certificado con subject DN=" + getSubjectDN().toString() +  
						" es válido y autofirmado.");
				return new CertificateValidationServiceResult (ValidationResult.RESULT_VALID, null);
			}
		} catch (NormalizeCertificateException e1) {
			// Esta excepción ya se habrá dado en la inicialización, por lo que no se habría iniciado este objeto
		}

    	logger.debug("[Certificate.validate] :: El certificado no ha caducado ni es autofirmado, ir a los servicios de validación a validar.");
    	for (CertificateValidationService service : validationServices) {
			if (service != null) {
				try {
					result = service.validate(this, null);
				} catch (Exception e) {
			    	logger.info("[Certificate.validate]::No se ha podido validar en " + service);
			    	continue;
				} 
				if (result.getResult() == ValidationResult.RESULT_VALID) {
					break;
				}
			}
		}
    	
    	//-- Si el certificado está revocado comparar la fecha de revocación con la fecha de comprobación
    	if (result != null && result.getResult() == ValidationResult.RESULT_CERTIFICATE_REVOKED) {
    		if (result.getRevocationDate().after(validationDate)) {
    			logger.debug("[Certificate.validate] :: Aunque el certificado está revocado no lo estaba en la fecha de la validación. Fecha de revocación: " + result.getRevocationDate());
    			result.setResult(ValidationResult.RESULT_VALID);
    		}
    	}
    	
    	//-- Si no se ha podido validar se devuelve el código adecuado
    	if (result == null) {
			logger.debug("[Certificate.validate] :: No se ha podido validar el certificado");
    		result = new CertificateValidationServiceResult(ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED, null);
    	}
    	
       	logger.debug("[Certificate.validate]::resultado: " + result.getResult());
    	return result;
    }
    
    /**
     * Obtiene información del certificado mediante llamadas a servicios externos.
     * 
     * @param dataServices Servicios de obtención de información de certificados
     * @return Información o null si no es posible obtenerla
     */
    public Map<String,String> getData (List<CertificateDataService> dataServices) {
    	logger.debug("[Certificate.getData]::Entrada::" + Arrays.asList(new Object[] { dataServices } ));
    	
    	for(CertificateDataService service : dataServices) {
    		try {
				Map<String,String> data = service.getData(this, null);
				if (data != null) {
					logger.debug("[Certificate.getData]::Obtenidos los campos: " + data);
					return data;
				}
			} catch (Exception e) {
		    	logger.info("[Certificate.getData]::No se han podido obtener los campos en " + service);
			} 
    	}
    	
		logger.debug("[Certificate.getData]::No se han podido obtener los campos");
		return null;
    }
    
	/**
	 * Método que devuelve la dirección de correo electrónico del certificado
	 * 
	 * @return E-mail del certificado
	 */
	public String getCertificateEmail () {
		
		logger.debug ("[Certificate.getCertificateEmail]::Entrada");
		
		List altNames;
		try {
			altNames = getSubjectAlternativeName();
		} catch (CertificateFieldException e) {
			logger.info ("[Certificate.getCertificateEmail]::No ha sido posible obtener el e-mail", e);
			return null;
		}
		
		//-- Si no hay san no hay email
		if (altNames == null || altNames.isEmpty()) {
			return null;
		}
	
		//-- Es el primer elemento del nombre alternativo
		return (String) ((AlternativeNameElement) altNames.get(0)).getValue();
	}
	
	/**
	 * Método que obtiene todos los clientes OCSP que pueden validar este 
	 * certificado. <br><br>
	 *
	 * Se devuelven los clientes OCSP que apuntan a las URLs indicadas
	 * en la extensión Authority Information Access (AIA) del certificado.
	 * 
	 * @return Clientes OCSP para validar este certificado
	 */
	public OCSPClient[] getOCSPClients () {
		
		logger.debug ("[Certificate.getOCSPClients]::Entrada");
		
		List lOCSPClients = new ArrayList ();
		
		//-- Buscar los OCSPs en base al campo del certificado
		String urlsOCSP [] = null;
		try {
			urlsOCSP = getOcspUrls();
			for (int i = 0; i < urlsOCSP.length; i++) {
				try {
					lOCSPClients.add (new OCSPClient (new URL (urlsOCSP[i])));
				} catch (MalformedURLException e1) {
					//-- No se añade a la lista porque la URL no es válida
					logger.debug("[Certificate.getOCSPClients]::La URL no es válida::" + urlsOCSP[i]);
				}
			}
			
			return (OCSPClient[]) lOCSPClients.toArray(new OCSPClient[0]);
		} catch (Exception e) {
			//-- No se han encontrado, devolver lista vacía
			logger.debug("[Certificate.getOCSPClients]::No se han encontrado URLs para OCSP ni en " +
					"un fichero validation.xml ni en el campo AIA");
			return new OCSPClient [0];
		}
	}
	
	/**
	 * Obtiene una respuesta OCSP de validación de este certificado. Si no se
	 * puede obtener una respuesta será por alguno de estos motivos:
	 * <ul>
	 * 	<li>El certificado no contiene la URL de ningún servidor de OCSP. El
	 * 	motivo de esta ausencia de información puede ser debido a que se trata 
	 *  de un certificado autofirmado o es un certificado utilizado en la firma 
	 *  de OCSPs. En cualquier otro caso, si se desea obtener una respuesta OCSP 
	 *  se deberá utilizar la clase {@link es.accv.arangi.base.certificate.validation.OCSPClient OCSPClient}
	 *  con la URL del servidor OCSP que valide este tipo de certificados.</li>
	 * 	<li>No es posible la conexión a los servidores OCSP indicados en el
	 * 	certificado o éstos no están respondiendo.</li>
	 * </ul>
	 * 
	 * @return Respuesta OCSP o null si ésta no se puede obtener
	 */
	public OCSPResponse getOCSPResponse () {
		
		logger.debug ("[Certificate.getOCSPResponse]::Entrada");
		
		OCSPClient[] ocspClients = getOCSPClients();
		OCSPResponse response = null;
		for (int i = 0; i < ocspClients.length; i++) {
			try {
				response = ocspClients[i].getOCSPResponse(this);
				break;
			} catch (Exception e) {
				logger.info("[Certificate.getOCSPResponse]::No se puede obtener respuesta OCSP de " + ocspClients[i].getURL(), e);
			} 
		}
		
		return response;		
	}
	
    @Override
    public boolean equals(Object obj) {
    	
    	//-- Si no son del mismo tipo no son iguales
    	if (!(obj instanceof Certificate)) {
    		return false;
    	}
    	
    	//-- Son iguales si tienen el mismo issuerAndSerialNumber
    	Certificate certificateToCompare = (Certificate) obj;
    	IssuerAndSerialNumber issuerSN1;
    	IssuerAndSerialNumber issuerSN2;
		try {
			issuerSN1 = certificateToCompare.getIssuerAndSerialNumber();
			issuerSN2 = getIssuerAndSerialNumber();
		} catch (CertificateFieldException e) {
			return false;
		}
    	
    	if (issuerSN1.getName().equals(issuerSN2.getName()) &&
    			issuerSN1.getSerialNumber().getValue().equals(issuerSN2.getSerialNumber().getValue())) {
    		return true;
    	}
    	
    	return false;
    }
    
	//-- Métodos privados
	
	/**
	 * Método que inicializa la clase
	 * 
	 * @throws NormalizeCertificateException No se puede normalizar el certificado
	 */
	private void initialize (X509Certificate certificate) throws NormalizeCertificateException {
		
		//-- Cargar el certificado
		this.certificate = normalize (certificate);

		//-- Cargar el certificado en ASN.1
		try {
			ASN1InputStream ais = new ASN1InputStream(certificate.getEncoded());
			ASN1Primitive obj = ais.readObject();
			ASN1Sequence seq = (ASN1Sequence)obj;
			ais.close();
			this.asn1Certificate = org.bouncycastle.asn1.x509.Certificate.getInstance(seq);
		} catch (Exception e) {
			logger.info("[Certificate.initialize]::No ha sido posible obtener una representación ASN.1 del certificado", e);
			throw new NormalizeCertificateException ("No ha sido posible obtener una representación ASN.1 del certificado", e);
		}

	}

	/*
	 * Reune los valores de la extensión en una lista en la que cada elemento es una lista
	 */
	private List getAlternativeNames(byte[] extVal) throws CertificateParsingException {
		List result = new ArrayList ();
		if (extVal == null) {
			return result;
		}
		try {
			byte[] extnValue = DEROctetString.getInstance(ASN1Primitive.fromByteArray(extVal)).getOctets();
			Enumeration it = DERSequence.getInstance(ASN1Primitive.fromByteArray(extnValue)).getObjects();
			while (it.hasMoreElements()) {
				GeneralName genName = GeneralName.getInstance(it.nextElement());
				AlternativeNameElement element = new AlternativeNameElement ();
				element.setType(genName.getTagNo());
				String value;
				switch (genName.getTagNo()) {
					case GeneralName.ediPartyName:
					case GeneralName.x400Address:
					case GeneralName.otherName:
						element.setValue(genName.getName().toASN1Primitive());
						break;
					case GeneralName.directoryName:
						element.setValue(X500Name.getInstance(genName.getName()));
						break;
					case GeneralName.dNSName:
					case GeneralName.rfc822Name:
					case GeneralName.uniformResourceIdentifier:
						element.setValue(((ASN1String)genName.getName()).getString());
						break;
					case GeneralName.registeredID:
						element.setValue(DERObjectIdentifier.getInstance(genName.getName()).getId());
						break;
					case GeneralName.iPAddress:
						element.setValue(new String (DEROctetString.getInstance(genName.getName()).getOctets()));
						break;
					default:
						throw new IOException("Bad tag number: " + genName.getTagNo());
				}

				result.add(element);
			}
		} catch (Exception e) {
			throw new CertificateParsingException(e.getMessage());
		}
		return result;
	}

    /*
     * Obtiene un vector con el DERObjectIdentifiers para los DNObjects names
     * 
     * @return
     */
    private static List<ASN1ObjectIdentifier> getDefaultX500FieldOrder(){
      
    	List<ASN1ObjectIdentifier> fieldOrder = new ArrayList<ASN1ObjectIdentifier>();
      
      for (int i = 0; i < dNObjects.length; i++) {
        fieldOrder.add(oids.get(dNObjects[i].toLowerCase()));
      }
      
      return fieldOrder;
      
    }
    
    /**
     * Obtiene un x500Name reordenado, si algunos campos del x500Name original no
     * aparecen en el parámetro 'ordering' se añadirán al final
     *   
     * @param x500Name
     * @param ordering, Vector de DERObjectIdentifier
     * @return
     */
    private static X500Name getOrderedX500Name( X500Name x500Name, List<ASN1ObjectIdentifier> ordering ){
      
      //-- Null prevent
      if ( ordering == null ){ ordering = new ArrayList<ASN1ObjectIdentifier>(); }
      
      //-- New order for the X509 Fields
      List<RDN> newRDNs  = new ArrayList<RDN>();
      
      HashMap<ASN1ObjectIdentifier,Object> ht = new HashMap<ASN1ObjectIdentifier,Object>();
      Iterator<ASN1ObjectIdentifier> it = ordering.iterator();
      
      logger.debug("Add ordered fields: ");
      //-- Add ordered fields
      while( it.hasNext() ) {
        ASN1ObjectIdentifier oid = it.next();
        
        if ( !ht.containsKey(oid) ){
        	List<RDN> valueList = getX500NameFields(x500Name, oid);
    
          //-- Only add the OID if has not null value
          if ( valueList != null ){
            Iterator<RDN> itVals = valueList.iterator();
            
            while( itVals.hasNext() ){
            	RDN value = itVals.next();
            	ht.put(oid, value);
            	newRDNs.add(value);
             }
            
          }
        }
      }
      
      RDN[] rDNs = x500Name.getRDNs();
      
      logger.debug("Add unespected fields to the end: ");
      //-- Add unespected fields to the end
      for ( int i=0; i<rDNs.length; i++ ) {
        
        ASN1ObjectIdentifier oid = rDNs[i].getFirst().getType();

        if ( !ht.containsKey(oid) ){
        	List<RDN> valueList = getX500NameFields(x500Name, oid);
    
          //-- Only add the OID if has not null value
          if ( valueList != null ){
            Iterator<RDN> itVals = valueList.iterator();
            
            while( itVals.hasNext() ){
            	RDN value = itVals.next();
            	ht.put(oid, value);
            	newRDNs.add(value);
            	logger.debug("added --> " + oid + " val: " + value);
            }
            
          }
        }
        
      } 
      
      //-- Create X509Name with the ordered fields
      X500Name orderedName = new X500Name(newRDNs.toArray(new RDN[0]));
      logger.debug("[getOrderedX509Name.retorna]:: " + orderedName);
      
      return orderedName;
      
    }

    /*
     * Obtiene el valor para un campo DN a partir de un X509Name, o nulo si
     *  el campo no existe.
     * 
     * @param name
     * @param id
     * @return
     */
    private static List<RDN> getX500NameFields( X500Name name, DERObjectIdentifier id ){
      
      RDN[] rDNs = name.getRDNs();
      List<RDN> vRet = null;
      
	  for (int j = 0; j < rDNs.length; j++) {
	        if ( rDNs[j].getFirst().getType().equals(id)) {
	            if ( vRet == null ){ vRet = new ArrayList<RDN>(); }
	            vRet.add(rDNs[j]);
	          }

	  }
      
      return vRet;
      
    }


}

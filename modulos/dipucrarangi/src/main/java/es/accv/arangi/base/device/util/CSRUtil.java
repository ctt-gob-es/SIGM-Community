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
package es.accv.arangi.base.device.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.PolicyQualifierId;
import org.bouncycastle.asn1.x509.PolicyQualifierInfo;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.UserNotice;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.util.encoders.Hex;

public class CSRUtil {

	/**
	 * Loggger de clase
	 */
	private static Logger logger = Logger.getLogger(CSRUtil.class);

	/**
	 * Obtenemos los usos de la clave del certificado.
	 * 
	 * @param keyUsages,
	 *            Conjunto de constantes de la clase X509KeyUsage que se
	 *            utilizará. Ejemplo:<br>
	 *            new int[]{ X509KeyUsage.digitalSignature } <br>
	 * @return
	 */
	private static KeyUsage getKeyUsage(int[] keyUsages) {
	  
    if ( keyUsages == null ){
      return null;
    }
    
		int bcku = 0;
		for (int i = 0; i < keyUsages.length; i++) {
			bcku = bcku | keyUsages[i];
		}

		KeyUsage keyUsage = new KeyUsage(bcku);

		return keyUsage;
	}

	protected static DERSequence getKeyUsageSequence(KeyUsage ku) throws IOException {

		// logger.debug("\n=================\n 1:\n" + ASN1Dump.dumpAsString(ku)
		// );
    if ( ku == null ){ return null; }
    
    	ASN1EncodableVector v = new ASN1EncodableVector();

		v.add(Extension.keyUsage);
		v.add(DERBoolean.getInstance(true));
		v.add(new DEROctetString(ku));

		DERSequence keyUsageSequence = new DERSequence(v);

		return keyUsageSequence;
	}

	/**
	 * Obtenemos los usos extendidos de la clave del certificado.
	 * 
	 * @param extendedKeyUsages,
	 *            Conjunto de constantes de la clase KeyPurposeId que se
	 *            utilizará. Ejemplo:<br>
	 *            new KeyPurposeId[]{ KeyPurposeId.id_kp_clientAuth } <br>
	 * @return
	 */
	private static ExtendedKeyUsage getExtendedKeyUsage(
			KeyPurposeId[] extendedKeyUsages) {
	  
    if ( extendedKeyUsages == null ){ return null; }
    
		ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(extendedKeyUsages);

		return extendedKeyUsage;
	}

	protected static DERSequence getExtendedKeyUsageSequence(
			ExtendedKeyUsage eku) throws IOException {

		// logger.debug("\n=================\n 2:\n" +
		// ASN1Dump.dumpAsString(eku) );

    if ( eku == null ){ return null; }
    
		ASN1EncodableVector vExtendedKeyUsages = new ASN1EncodableVector();
		vExtendedKeyUsages.add(Extension.extendedKeyUsage);
		vExtendedKeyUsages.add(new DEROctetString(eku));

		DERSequence lObjExtKey = new DERSequence(vExtendedKeyUsages);

		return lObjExtKey;

	}

	/**
	 * Obtiene el bloque de CRLDistributionPoints a partir de un directory name
	 * 
	 * @param crlDN
	 * @return
	 * @throws Exception
	 */
	protected static DERSequence getCRLDistributionPointsSequence(String crlDN)
			throws Exception {

    if ( crlDN == null ){ return null; }
    
		DERTaggedObject taggedObject = null;

		try {

			URL crlUrl = new URL(crlDN);

			DEROctetString octetURL = new DEROctetString(crlUrl.toString()
					.getBytes());

			// -- Creamos el TaggedObject del la URI
			taggedObject = new DERTaggedObject(false, 6, octetURL);

		} catch (MalformedURLException e) {

			// -- Si el parametro crlDN no es un URL, intentamos crear
			// -- el DirectoryName.

			// -- Orden especifico para la CA de UNICERT
			List<ASN1ObjectIdentifier> camposOrdenados = new ArrayList<ASN1ObjectIdentifier>();
			camposOrdenados.add(BCStyle.C);
			camposOrdenados.add(BCStyle.O);
			camposOrdenados.add(BCStyle.OU);
			camposOrdenados.add(BCStyle.CN);

			// -- Obtenemos el X509Name ordenado y solo con los campos de la
			// ordenacion
			X500Name orderedName = getOrderedX500Name(crlDN, camposOrdenados,
					true);

			// -- Creamos el TaggedObject del DirectoryName
			taggedObject = new DERTaggedObject(true, 4, orderedName);

		}

		// logger.debug("taggedObject:\n" +
		// ASN1Dump.dumpAsString(taggedObject) );

		return getCRLDistributionPointsSequence(taggedObject);

	}

	/**
	 * Obtiene el bloque de CRLDistributionPoints a partir de un directory name
	 * DERSequence DERObjectIdentifier(2.5.29.31) DERSequence DERTaggedObject
	 * [0] DERTaggedObject [0]
	 * 
	 * {** para el caso de URI **} DERTaggedObject [6] IMPLICIT
	 * DEROctetString("http://www.pki.gva.es/gestcert/cagva.crl")
	 * 
	 * {** para el caso de directoryName **} DERTaggedObject [4] DERSequence
	 * DERSet DERSequence DERObjectIdentifier(2.5.4.6) DERPrintableString(es)
	 * DERSet DERSequence DERObjectIdentifier(2.5.4.10)
	 * DERPrintableString(Generalitat Valenciana) DERSet DERSequence
	 * DERObjectIdentifier(2.5.4.11) DERPrintableString(PKIGVA) DERSet
	 * DERSequence DERObjectIdentifier(2.5.4.3) DERPrintableString(pol2101)
	 * 
	 * @param crlDirectoryName
	 * @return
	 * @throws IOException 
	 */
	protected static DERSequence getCRLDistributionPointsSequence(
			DERTaggedObject taggedDistributionPoint) throws IOException {

		// Código común para ambos formatos, antiguo y nuevo.
		DERTaggedObject taggedCrlDN = new DERTaggedObject(true, 0,
				taggedDistributionPoint);

		ASN1EncodableVector vTags = new ASN1EncodableVector();
		vTags.add(new DERTaggedObject(true, 0, taggedCrlDN));
		DERSequence seqVTags = new DERSequence(vTags);

		ASN1EncodableVector vSeqVTags = new ASN1EncodableVector();
		vSeqVTags.add(seqVTags);
		DERSequence seqVSeqVTags = new DERSequence(vSeqVTags);

		// logger.debug("\n=================\n seqVSeqVTags:\n" +
		// ASN1Dump.dumpAsString(seqVSeqVTags) );

		// -- Agregamos el OID
		ASN1EncodableVector vCRLDistributionPoints = new ASN1EncodableVector();
		vCRLDistributionPoints.add(Extension.cRLDistributionPoints);
		vCRLDistributionPoints.add(new DEROctetString(seqVSeqVTags));

		DERSequence lObjSequenceCRL = new DERSequence(vCRLDistributionPoints);

		return lObjSequenceCRL;

	}

	/**
	 * Obtene la secuencia del SubjectAlternativeName para la peticion.
	 * 
	 * Estructura de SubjectAlternativeName:
	 * 
	 * DERSequence DERObjectIdentifier(2.5.29.17) DEROctetString( DERSequence
	 * DERTaggedObject [1] IMPLICIT DEROctetString. DERTaggedObject [4] { se
	 * obtiene llamando a getSanDirectoryName} )
	 * 
	 * @param X509Name
	 *            name, debe incluir una direccion de correo para el campo
	 *            EmailAddress.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected static DERSequence getSubjectAlternativeNameSequence(
			String subjectAlternativeDN) throws Exception {
	  
		if ( subjectAlternativeDN == null ){ return null; }
    
		GeneralNames san = getGeneralNamesFromAltName(subjectAlternativeDN);
		if (san == null) { return null; }
		
		return new DERSequence(san);

	}

	/**
	 * Obtiene el bloque con el policyInformation (o certificatePolicies)
	 * 
	 * @param policyOID,
	 *            OID de la politica de la peticion
	 * @param cps,
	 *            url al documento con la politica
	 * @param unotice,
	 *            texto de advertencia
	 * @return
	 */
	private static PolicyInformation getPolicyInformation(String policyOID,
			String cps, String unotice) {

    if ( policyOID == null ){ return null; }
    
		ASN1EncodableVector qualifiers = new ASN1EncodableVector();

		if (unotice != null && !"".equals(unotice)) {
			UserNotice un = new UserNotice(null, new DisplayText(
					DisplayText.CONTENT_TYPE_BMPSTRING, unotice));
			PolicyQualifierInfo pqiUNOTICE = new PolicyQualifierInfo(
					PolicyQualifierId.id_qt_unotice, un);
			qualifiers.add(pqiUNOTICE);
		}

		if (cps != null && !"".equals(cps)) {
			PolicyQualifierInfo pqiCPS = new PolicyQualifierInfo(cps);
			qualifiers.add(pqiCPS);
		}

		PolicyInformation policyInformation = new PolicyInformation(
				new ASN1ObjectIdentifier(policyOID), new DERSequence(qualifiers));

		return policyInformation;

	}

	/**
	 * Obtiene la secuencia de las Certificate Policies para UNICERT.
	 * 
	 * Estructura de Certificate Policies:
	 * 
	 * DERSequence DERObjectIdentifier(2.5.29.32) DEROctetString( DERSequence
	 * DERSequence DERObjectIdentifier(1.3.6.1.4.1.8149.3.6.1.0) DERSequence
	 * DERSequence DERObjectIdentifier(1.3.6.1.5.5.7.2.2) DERSequence
	 * DERBMPString(Certificado...) DERSequence
	 * DERObjectIdentifier(1.3.6.1.5.5.7.2.1)
	 * DERIA5String(http://www.pki.gva.es/legislacion_c.htm) )
	 * 
	 * @param policyInformation
	 * @return
	 * @throws IOException 
	 */
	protected static DERSequence getCertificatePoliciesSequence(
			PolicyInformation policyInformation) throws IOException {

    if ( policyInformation == null ){ return null; }
    
		DERSequence seqPolicyInformation = new DERSequence(policyInformation.toASN1Primitive());
		// logger.debug("\n=================\n seqPolicyInformation:\n" +
		// ASN1Dump.dumpAsString(seqPolicyInformation) );

		ASN1EncodableVector vCertificatePolicies = new ASN1EncodableVector();
		vCertificatePolicies.add(Extension.certificatePolicies);
		vCertificatePolicies.add(new DEROctetString(seqPolicyInformation));

		DERSequence seqCertificatePolicies = new DERSequence(
				vCertificatePolicies);

		return seqCertificatePolicies;
	}

	/**
	 * Codificacion especial para UNICERT del AuthoritInformationAccess.
	 * 
	 * Estructura:
	 * 
	 * DERSequence DERObjectIdentifier(1.3.6.1.5.5.7.1.1) DEROctetString(
	 * DERSequence DERSequence DERObjectIdentifier(1.3.6.1.5.5.7.48.1)
	 * DERTaggedObject [6] IMPLICIT DEROctetString )
	 * 
	 * @param ocspUrl
	 * @return
	 * @throws IOException 
	 */
	protected static DERSequence getAuthorityInfoAccessSequence(String ocspUrl) throws IOException {

    if ( ocspUrl == null ){ return null; }
    
		DERTaggedObject tag6 = new DERTaggedObject(false, 6,
				new DEROctetString(ocspUrl.getBytes()));

		ASN1EncodableVector vTag6 = new ASN1EncodableVector();
		vTag6.add(X509ObjectIdentifiers.ocspAccessMethod);
		vTag6.add(tag6);

		DERSequence secSecTag6 = new DERSequence(new DERSequence(vTag6));

		// logger.debug("\n=================\n secSecTag6:\n" +
		// ASN1Dump.dumpAsString(secSecTag6) );

		ASN1EncodableVector vAuthorityInfoAccess = new ASN1EncodableVector();
		vAuthorityInfoAccess.add(Extension.authorityInfoAccess);
		vAuthorityInfoAccess.add(new DEROctetString(secSecTag6));

		DERSequence seqAuthorityInfoAccess = new DERSequence(
				vAuthorityInfoAccess);

		return seqAuthorityInfoAccess;

	}

	/**
	 * Obtiene el identificador de clave ce la CA.
	 * 
	 * @param caPublicKey
	 * @return
	 * @throws IOException
	 */
	private static AuthorityKeyIdentifier getAuthorityKeyIdentifier(
			PublicKey caPublicKey) throws IOException {

		SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo(
				(ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(
						caPublicKey.getEncoded())).readObject());

		AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(apki);

		return aki;

	}

	/**
	 * Obtiene la secuencia asociada al AuthorityKeyIdentifier para la peticion
	 * a UNICERT.
	 * 
	 * @param aki
	 * @return
	 * @throws IOException 
	 */
	protected static DERSequence getAuthorityKeyIdentifierSequence(
			AuthorityKeyIdentifier aki) throws IOException {

		// logger.debug("\n=================\n aki:\n" +
		// ASN1Dump.dumpAsString(aki) );

    if ( aki == null ){ return null; }
    
		ASN1EncodableVector vAuthorityKeyIdentifier = new ASN1EncodableVector();
		vAuthorityKeyIdentifier.add(Extension.authorityKeyIdentifier);
		vAuthorityKeyIdentifier.add(new DEROctetString(aki));

		DERSequence secAuthorityKeyIdentifier = new DERSequence(
				vAuthorityKeyIdentifier);

		return secAuthorityKeyIdentifier;

	}

	/**
	 * Obtiene el bloque de codificacion para SubjectKeyIdentifier
	 * 
	 * @param caPublicKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException 
	 */
	protected static DERSequence getSubjectKeyIdentifierSequence(
			PublicKey caPublicKey) throws NoSuchAlgorithmException, IOException {

    if ( caPublicKey == null ){ return null; }
    
		MessageDigest lObjDigest = MessageDigest.getInstance("SHA1");
		lObjDigest.update(caPublicKey.getEncoded());

		SubjectKeyIdentifier ski = new SubjectKeyIdentifier(lObjDigest.digest());

		ASN1EncodableVector lObjAux = new ASN1EncodableVector();
		lObjAux.add(Extension.subjectKeyIdentifier);
		lObjAux.add(new DEROctetString(ski));

		DERSequence seqSubjectKeyIdentifier = new DERSequence(lObjAux);

		return seqSubjectKeyIdentifier;

	}

	/**
	 * Obtiene el valor de un campo de un X509Name, o null en caso de que
	 * no exista.
	 * 
	 * @param name X509Name
	 * @param id OID a buscar dentro del X509Name
	 * @return Objeto indicado por el OID
	 */
	public static ASN1Encodable getX500NameField(X500Name name, ASN1ObjectIdentifier id) {

		for (int i = 0; i < name.getRDNs().length; i++) {

			if (id.equals(name.getRDNs()[i].getFirst().getType())) {
				return name.getRDNs()[i].getFirst().getValue();
			}
		}

		return null;

	}

	/**
	 * Crea un X509Name a partir de la cadena del DN, y lo ordena con el vector
	 * de oids de ordenación.
	 * 
	 * @param dN Distinguished name
	 * @param camposOrdenados
	 * @return X509Name ordenado
	 * @throws Exception
	 */
	public static X500Name getOrderedX500Name(String dN,
			List<ASN1ObjectIdentifier> camposOrdenados, boolean allOIDMustExist) throws Exception {

		// -- Creamos el Name a partir de la cadena
		X500Name name = new X500Name(dN);

		List<RDN> lRDNs = new ArrayList<RDN>();

		for (int i = 0; i < camposOrdenados.size(); i++) {

			ASN1ObjectIdentifier id = (ASN1ObjectIdentifier) camposOrdenados.get(i);
			ASN1Encodable val = getX500NameField(name, id);

			// logger.debug("id: " + id + " val: " + val);

			if (allOIDMustExist) {
				if (val == null) {
					throw new Exception(
							"Falta un campo en el directoryName del CRLDistributionPoint.");
				}
			}

			// -- Solo añadimos el OID si tiene valor
			if (val != null) {
				RDN rdn = new RDN(id, val);
				lRDNs.add(rdn);
			}

		}

		// -- Creamos el X500Name solo con los campos de la ordenacion
		X500Name orderedName = new X500Name(lRDNs.toArray(new RDN[0]));

		return orderedName;

	}

	public static DERSet getPKCS10Set(PublicKey userPublicKey,
			PublicKey caPublicKey, Map map) throws Exception {

		// -- Recogemos parametros
		int[] aKeyUsage = (int[]) map.get("keyUsages");
		KeyPurposeId[] keyPurposeIds = (KeyPurposeId[]) map
				.get("keyPurposeIds");
		String ocspUrl = (String) map.get("ocspUrl");

		String subjectAlternativeNameDN = (String) map
				.get("subjectAlternativeNameDN");
		String policyOID = (String) map.get("policyOID");
		String cps = (String) map.get("cps");
		String unotice = (String) map.get("unotice");
		String distributionPoint = (String) map.get("distributionPoint");

		// -- Generacion de Extensiones
		KeyUsage keyUsage = getKeyUsage(aKeyUsage);
		ExtendedKeyUsage extendedKeyUsage = getExtendedKeyUsage(keyPurposeIds);
		PolicyInformation policyInformation = getPolicyInformation(policyOID,
				cps, unotice);
		AuthorityKeyIdentifier aki = getAuthorityKeyIdentifier(caPublicKey);

		// -- Generacion de Secuencias de Extensiones
		DERSequence seqKeyUsage = getKeyUsageSequence(keyUsage);
		DERSequence seqExtendedKeyUsage = getExtendedKeyUsageSequence(extendedKeyUsage);
		DERSequence seqCRLDistributionPoints = getCRLDistributionPointsSequence(distributionPoint);
		DERSequence seqSubjectAlternativeName = getSubjectAlternativeNameSequence(subjectAlternativeNameDN);
		DERSequence seqCertificatePolicies = getCertificatePoliciesSequence(policyInformation);
		DERSequence seqAuthorityInfoAccess = getAuthorityInfoAccessSequence(ocspUrl);
		DERSequence seqAuthorityKeyIdentifier = getAuthorityKeyIdentifierSequence(aki);
		DERSequence seqSubjectKeyIdentifier = getSubjectKeyIdentifierSequence(userPublicKey);

		// Ya tenemos todas las extensiones, añadimos las extensiones a nuestra
		// petición.
		ASN1EncodableVector vExtensions = new ASN1EncodableVector();
		if ( seqKeyUsage != null ) { vExtensions.add(seqKeyUsage); }
	    if ( seqExtendedKeyUsage != null ) { vExtensions.add(seqExtendedKeyUsage); }
	    if ( seqCRLDistributionPoints != null ) { vExtensions.add(seqCRLDistributionPoints); }
	    if ( seqSubjectAlternativeName != null ) { vExtensions.add(seqSubjectAlternativeName); }
	    if ( seqCertificatePolicies != null ) { vExtensions.add(seqCertificatePolicies); }
	    if ( seqAuthorityInfoAccess != null ) { vExtensions.add(seqAuthorityInfoAccess); }
	    if ( seqAuthorityKeyIdentifier != null ) { vExtensions.add(seqAuthorityKeyIdentifier); }
	    if ( seqSubjectKeyIdentifier != null ) { vExtensions.add(seqSubjectKeyIdentifier); }

		// logger.debug("\n=================\n 1:\n" +
		// ASN1Dump.dumpAsString(seqKeyUsage) );
		// logger.debug("\n=================\n 2:\n" +
		// ASN1Dump.dumpAsString(seqExtendedKeyUsage) );
		// logger.debug("\n=================\n 3:\n" +
		// ASN1Dump.dumpAsString(seqCRLDistributionPoints) );
		// logger.debug("\n=================\n 4:\n" +
		// ASN1Dump.dumpAsString(seqSubjectAlternativeName) );
		// logger.debug("\n=================\n 5:\n" +
		// ASN1Dump.dumpAsString(seqCertificatePolicies) );
		// logger.debug("\n=================\n 6:\n" +
		// ASN1Dump.dumpAsString(seqAuthorityInfoAccess) );
		// logger.debug("\n=================\n 7:\n" +
		// ASN1Dump.dumpAsString(seqAuthorityKeyIdentifier) );
		// logger.debug("\n=================\n 8:\n" +
		// ASN1Dump.dumpAsString(seqSubjectKeyIdentifier) );

		// -- Creamos la secuencia del bloque de extensiones
		ASN1EncodableVector vSecExtensions = new ASN1EncodableVector();
		vSecExtensions.add(new DERSequence(vExtensions));
		DERSet setExtensions = new DERSet(vSecExtensions);

		// -- Creamos el bloque de Cerificate Extension Request
		ASN1EncodableVector bloqueExtensiones = new ASN1EncodableVector();
		bloqueExtensiones.add(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest);
		bloqueExtensiones.add(setExtensions);

		DERSet setExtensiones = new DERSet(new DERSequence(bloqueExtensiones));

		return setExtensiones;
	}

    public static GeneralNames getGeneralNamesFromAltName(String altName) {
        List<GeneralName> lGNs = new ArrayList<GeneralName>();

        String email = getEmailFromDN(altName);
        if (email != null) {
            GeneralName gn = new GeneralName(1, new DERIA5String(email));
            lGNs.add(gn);
        }
        
        ArrayList<String> dns = getPartsFromDN(altName, "dNSName");
        if (!dns.isEmpty()) {            
            Iterator<String> iter = dns.iterator();
            while (iter.hasNext()) {
                GeneralName gn = new GeneralName(2, new DERIA5String((String)iter.next()));
                lGNs.add(gn);
            }
        }
        
        //-- Patch DirectoryName
        String directoryName = getDirectoryStringFromAltName(altName);
        if (directoryName != null) {
          X500Name x500DirectoryName = new X500Name(directoryName);
          GeneralName gn = new GeneralName(4, x500DirectoryName);
          lGNs.add(gn);
        }
        //-- /Patch DirectoryName
                                
        ArrayList<String> uri = getPartsFromDN(altName, "uniformResourceIdentifier");
        if (!uri.isEmpty()) {            
            Iterator<String> iter = uri.iterator();
            while (iter.hasNext()) {
                GeneralName gn = new GeneralName(6, new DERIA5String((String)iter.next()));
                lGNs.add(gn);
            }
        }

        uri = getPartsFromDN(altName, "uri");
        if (!uri.isEmpty()) {            
            Iterator<String> iter = uri.iterator();
            while (iter.hasNext()) {
                GeneralName gn = new GeneralName(6, new DERIA5String((String)iter.next()));
                lGNs.add(gn);
            }
        }
        
                
        ArrayList<String> ipstr = getPartsFromDN(altName, "iPAddress");
        if (!ipstr.isEmpty()) {            
            Iterator<String> iter = ipstr.iterator();
            while (iter.hasNext()) {
                byte[] ipoctets = ipStringToOctets((String)iter.next());
                GeneralName gn = new GeneralName(7, new DEROctetString(ipoctets));
                lGNs.add(gn);
            }
        }
                    
        ArrayList<String> upn =  getPartsFromDN(altName, "upn");
        if (!upn.isEmpty()) {            
            Iterator<String> iter = upn.iterator();             
            while (iter.hasNext()) {
            	ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("1.3.6.1.4.1.311.20.2.3");
            	ASN1Encodable value = new DERUTF8String((String)iter.next());
                GeneralName gn = new GeneralName(new X500Name(new RDN[] {new RDN(oid, value)}));
                lGNs.add(gn);
            }
        }
        
        
        ArrayList<String> guid =  getPartsFromDN(altName, "guid");
        if (!guid.isEmpty()) {            
            Iterator<String> iter = guid.iterator();                
            while (iter.hasNext()) {                    
                ASN1EncodableVector v = new ASN1EncodableVector();
                byte[] guidbytes = Hex.decode((String)iter.next());
                if (guidbytes != null) {
                	ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("1.3.6.1.4.1.311.25.1");
                    ASN1Encodable value = new DEROctetString(guidbytes);
                    GeneralName gn = new GeneralName(new X500Name(new RDN[] {new RDN(oid, value)}));
                    lGNs.add(gn);                    
                } else {
                    logger.info("Cannot decode hexadecimal guid: "+guid);
                }
            }
        }
        GeneralNames ret = null; 
        if (lGNs.size() > 0) {
            ret = new GeneralNames(lGNs.toArray(new GeneralName[0]));
        }
        return ret;
    }
    
    /*
     * Convenience method for getting an email address from a DN. Uses {@link
     * getPartFromDN(String,String)} internally, and searches for {@link EMAIL}, {@link EMAIL1},
     * {@link EMAIL2}, {@link EMAIL3} and returns the first one found.
     *
     * @param dn the DN
     *
     * @return the found email address, or <code>null</code> if none is found
     */
    private static String getEmailFromDN(String dn) {
        logger.debug(">getEmailFromDN(" + dn + ")");
        String email = null;
        String [] emailIds = new String [] { "rfc822name", "email", "EmailAddress", "E" };
        for (int i = 0; (i < emailIds.length) && (email == null); i++) {
            email = getPartFromDN(dn, emailIds[i]);
        }
        logger.debug("<getEmailFromDN(" + dn + "): " + email);
        return email;
    }
    
    /**
	 * Gets a specified parts of a DN. Returns all occurences as an ArrayList, also works if DN contains several
	 * instances of a part (i.e. cn=x, cn=y returns {x, y, null}).
	 *
	 * @param dn String containing DN, The DN string has the format "C=SE, O=xx, OU=yy, CN=zz".
	 * @param dnpart String specifying which part of the DN to get, should be "CN" or "OU" etc.
	 *
	 * @return ArrayList containing dnparts or empty list if dnpart is not present
	 */
	public static ArrayList<String> getPartsFromDN(String dn, String dnpart) {
		logger.debug(">getPartsFromDN: dn:'" + dn + "', dnpart=" + dnpart);
		ArrayList<String> parts = new ArrayList<String>();
		if ((dn != null) && (dnpart != null)) {
			StringTokenizer st = new StringTokenizer(dn, ",");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.startsWith(dnpart)) {
					parts.add(token.substring(dnpart.length() + 1));
				}
			}
		}
		logger.debug("<getpartsFromDN: resulting DN part=" + parts.toString());
		return parts;
	} 
	
    /**
     * Gets a specified part of a DN. Specifically the first occurrence it the DN contains several
     * instances of a part (i.e. cn=x, cn=y returns x).
     *
     * @param dn String containing DN, The DN string has the format "C=SE, O=xx, OU=yy, CN=zz".
     * @param dnpart String specifying which part of the DN to get, should be "CN" or "OU" etc.
     *
     * @return String containing dnpart or null if dnpart is not present
     */
    public static String getPartFromDN(String dn, String dnpart) {
        logger.debug(">getPartFromDN: dn:'" + dn + "', dnpart=" + dnpart);
        String part = null;
        if ((dn != null) && (dnpart != null)) {
    		StringTokenizer st = new StringTokenizer(dn, ",");
    		while(st.hasMoreTokens()) {
    			String token = st.nextToken();
    			if (token.toLowerCase().startsWith(dnpart.toLowerCase())) {
    		        logger.debug("<getpartFromDN: resulting DN part=" + part);
    				return token.substring(dnpart.length() + 1);
    			}
    		}
        }

		return null;
    } 
    
    /**
     * Obtain the directory string for the directoryName generation
     * form the Subject Alternative Name String.
     * 
     * @param altName
     * @return
     */
    private static String getDirectoryStringFromAltName(String altName) {
      
    	DNFieldExtractor dnfe = new DNFieldExtractor(altName, DNFieldExtractor.TYPE_SUBJECTALTNAME);
    	String directoryName = dnfe.getField(DNFieldExtractor.DIRECTORYNAME, 0);
    	
    	/** TODO: Validate or restrict the directoryName Fields? */
      
    	return ( "".equals(directoryName) ? null : directoryName );
    }

    /* Converts an IP-address string to octets of binary ints. 
     * ip is of form a.b.c.d, i.e. at least four octets
     * @param str string form of ip-address
     * @return octets, null if input format is invalid
     */
    private static byte[] ipStringToOctets(String str) {
        String[] toks = str.split("[.:]");
        if (toks.length == 4) {
            // IPv4 address
            byte[] ret = new byte[4];
            for (int i = 0;i<toks.length;i++) {
                int t = Integer.parseInt(toks[i]);
                if (t>255) {
                    logger.info("IPv4 address '"+str+"' contains octet > 255.");
                    return null;
                }
                ret[i] = (byte)t;
            }
            return ret;
        }
        if (toks.length == 8) {
            // IPv6 address
            byte[] ret = new byte[16];
            int ind = 0;
            for (int i = 0;i<toks.length;i++) {
                int t = Integer.parseInt(toks[i]);
                if (t>0xFFFF) {
                    logger.info("IPv6 address '"+str+"' contains part > 0xFFFF.");
                    return null;
                }
                int b1 = t & 0x00FF;
                ret[ind++] = (byte)b1;
                int b2 = t & 0xFF00;
                ret[ind++] = (byte)b2;
            }
        }
        logger.info("Not a IPv4 or IPv6 address.");
        return null;
    }

}

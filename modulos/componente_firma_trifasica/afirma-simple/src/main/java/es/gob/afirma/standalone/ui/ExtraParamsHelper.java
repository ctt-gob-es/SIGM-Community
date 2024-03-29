package es.gob.afirma.standalone.ui;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.core.signers.AOSignConstants;
import es.gob.afirma.core.signers.AOSigner;
import es.gob.afirma.signers.pades.AOPDFSigner;
import es.gob.afirma.signers.tsp.pkcs7.TsaParams;
import es.gob.afirma.signers.tsp.pkcs7.TsaRequestExtension;
import es.gob.afirma.signers.xades.AOFacturaESigner;
import es.gob.afirma.signers.xades.AOXAdESSigner;
import es.gob.afirma.standalone.ui.preferences.PreferencesManager;

final class ExtraParamsHelper {

	private ExtraParamsHelper() {
		// No permitimos la instanciacion
	}

	final static Properties loadExtraParamsForSigner(final AOSigner signer) {

		final Properties p;
		if (signer instanceof AOFacturaESigner) {
        	p = loadFacturaEExtraParams();
        }
		else if (signer instanceof AOXAdESSigner) {
        	p = loadXAdESExtraParams();
        }
        else if (signer instanceof AOPDFSigner) {
        	p = loadPAdESExtraParams();
        }
        else {
        	p = loadCAdESExtraParams();
        }

		return p;
	}

	/** Obtiene la configuraci&oacute;n para las firmas Factura-E.
	 * @return Propiedades para la configuraci&oacute;n de las firmas Factura-E. */
	private static Properties loadFacturaEExtraParams() {
		final Properties p = new Properties();

		// Metadatos sobre la "produccion" de la firma de la factura
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNATURE_PRODUCTION_CITY, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionCity", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNATURE_PRODUCTION_CITY, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNATURE_PRODUCTION_PROVINCE, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionProvince", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNATURE_PRODUCTION_PROVINCE, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNATURE_PRODUCTION_POSTAL_CODE, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionPostalCode", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNATURE_PRODUCTION_POSTAL_CODE, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNATURE_PRODUCTION_COUNTRY, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionCountry", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNATURE_PRODUCTION_COUNTRY, "") //$NON-NLS-1$
			);
        }

        // Papel del firmante de la factura, es un campo acotado
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNER_ROLE, "").trim().isEmpty()) { //$NON-NLS-1$
        	p.put(
        		"signerClaimedRoles", //$NON-NLS-1$
        		PreferencesManager.get(PreferencesManager.PREFERENCE_FACTURAE_SIGNER_ROLE, "") //$NON-NLS-1$
    		);
        }

		return p;
	}

	/** Obtiene la configuraci&oacute;n para las firmas XAdES.
	 * @return Propiedades para la configuraci&oacute;n de las firmas XAdES. */
	private static Properties loadXAdESExtraParams() {

		final Properties p = new Properties();
        p.put("ignoreStyleSheets", "false"); //$NON-NLS-1$ //$NON-NLS-2$

        // Preferencias de politica de firma
        final String policyId = PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_POLICY_IDENTIFIER, ""); //$NON-NLS-1$

        if (!policyId.trim().isEmpty()) {
        	p.put(
        		"policyIdentifier", //$NON-NLS-1$
        		policyId
        	);

        	if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_POLICY_IDENTIFIER_HASH, "").trim().isEmpty()) { //$NON-NLS-1$
        		p.put(
        			"policyIdentifierHash", //$NON-NLS-1$
        			PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_POLICY_IDENTIFIER_HASH, "") //$NON-NLS-1$
        		);
        	}
        	if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_POLICY_IDENTIFIER_HASH_ALGORITHM, "").trim().isEmpty()) { //$NON-NLS-1$
        		p.put(
        			"policyIdentifierHashAlgorithm", //$NON-NLS-1$
        			PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_POLICY_IDENTIFIER_HASH_ALGORITHM, "") //$NON-NLS-1$
        		);
        	}
        	if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_POLICY_QUALIFIER, "").trim().isEmpty()) { //$NON-NLS-1$
        		p.put(
        			"policyQualifier", //$NON-NLS-1$
        			PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_POLICY_QUALIFIER, "") //$NON-NLS-1$
        		);
        	}
        }

        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNER_CLAIMED_ROLE, "").trim().isEmpty()) { //$NON-NLS-1$
            p.put(
        		"signerClaimedRoles", //$NON-NLS-1$
        		PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNER_CLAIMED_ROLE, "") //$NON-NLS-1$
    		);
        }

        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNATURE_PRODUCTION_CITY, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionCity", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNATURE_PRODUCTION_CITY, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNATURE_PRODUCTION_PROVINCE, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionProvince", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNATURE_PRODUCTION_PROVINCE, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNATURE_PRODUCTION_POSTAL_CODE, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionPostalCode", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNATURE_PRODUCTION_POSTAL_CODE, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNATURE_PRODUCTION_COUNTRY, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionCountry", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGNATURE_PRODUCTION_COUNTRY, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGN_FORMAT, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"format", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_XADES_SIGN_FORMAT, "") //$NON-NLS-1$
			);
        }

		return p;
	}

	/** Obtiene la configuraci&oacute;n para las firmas PAdES.
	 * @return Propiedades para la configuraci&oacute;n de las firmas PAdES. */
	private static Properties loadPAdESExtraParams() {

		final Properties p = new Properties();
        p.put("allowSigningCertifiedPdfs", "false"); //$NON-NLS-1$ //$NON-NLS-2$

        // Preferencias de politica de firma PAdES
        final String policyId = PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_POLICY_IDENTIFIER, ""); //$NON-NLS-1$
        if (!policyId.trim().isEmpty()) {
        	p.put(
        		"policyIdentifier", //$NON-NLS-1$
        		policyId
        	);
        	if (!PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_POLICY_IDENTIFIER_HASH, "").trim().isEmpty()) { //$NON-NLS-1$
        		p.put(
        			"policyIdentifierHash", //$NON-NLS-1$
        			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_POLICY_IDENTIFIER_HASH, "") //$NON-NLS-1$
        		);
        	}
        	if (!PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_POLICY_IDENTIFIER_HASH_ALGORITHM, "").trim().isEmpty()) { //$NON-NLS-1$
        		p.put(
        			"policyIdentifierHashAlgorithm", //$NON-NLS-1$
        			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_POLICY_IDENTIFIER_HASH_ALGORITHM, "") //$NON-NLS-1$
        		);
        	}
        	if (!PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_POLICY_QUALIFIER, "").trim().isEmpty()) { //$NON-NLS-1$
        		p.put(
        			"policyQualifier", //$NON-NLS-1$
        			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_POLICY_QUALIFIER, "") //$NON-NLS-1$
        		);
        	}
        }

        // Metadatos PAdES
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_SIGN_REASON, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signReason", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_SIGN_REASON, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_SIGN_PRODUCTION_CITY, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signatureProductionCity", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_SIGN_PRODUCTION_CITY, "") //$NON-NLS-1$
			);
        }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_SIGNER_CONTACT, "").trim().isEmpty()) {  //$NON-NLS-1$
        	p.put(
    			"signerContact", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_SIGNER_CONTACT, "") //$NON-NLS-1$
			);
        }

        // PAdES BES/Basic
        if (PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_FORMAT, null) != null) {
        	p.put(
    			"signatureSubFilter", //$NON-NLS-1$
    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_FORMAT, AOSignConstants.PADES_SUBFILTER_BASIC)
			);
        }

        // Nivel de certificacion
        if (PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_CERTIFICATION_LEVEL, null) != null) {
        	p.put("certificationLevel", PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_CERTIFICATION_LEVEL, null)); //$NON-NLS-1$
        }

        // Sellos de tiempo
        if (PreferencesManager.getBoolean(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_CONFIGURE, false)) {

        	TsaRequestExtension req;
        	if (
    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_EXTENSION_OID, null) != null &&
    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_EXTENSION_VALUE, null) != null &&
    			Base64.isBase64(PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_EXTENSION_VALUE, null))
			) {
        		try {
					req = new TsaRequestExtension(
						PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_EXTENSION_OID, null),
						PreferencesManager.getBoolean(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_OID_CRITICAL, false),
						Base64.decode(PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_EXTENSION_VALUE, null))
					);
				}
        		catch (final Exception e) {
					req = null;
				}
        	}
        	else {
        		req = null;
        	}

        	URI uri;
        	try {
				uri = new URI(PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_TSA_URL, null));
			}
        	catch (final URISyntaxException e) {
				uri = null;
			}

        	if (uri != null) {
	        	final Properties timestampExtraParams = new TsaParams(
	    			PreferencesManager.getBoolean(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_CERT_REQUIRED, true),
	    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_STAMP_POLICY, null),
	    			uri,
	    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_TSA_USR, null),
	    			PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_TSA_PWD, null),
	    			req == null ? null : new TsaRequestExtension[] { req },
					PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_HASHALGORITHM, null),
	    			null, // sslKeyStoreFile
	    			null, // sslKeyStorePassword
	    			null, // sslKeyStoreType
	    			null, // sslTrustStore
	    			null, // sslTrustStorePassword
	    			null, // sslTrustStoreType
	    			true
				).getExtraParams();
	        	p.putAll(timestampExtraParams);
	        	p.put("tsType", PreferencesManager.get(PreferencesManager.PREFERENCE_PADES_TIMESTAMP_STAMP_TYPE, "2"));  //$NON-NLS-1$//$NON-NLS-2$
        	}
        }

		return p;
	}

	/** Obtiene la configuraci&oacute;n para las firmas CAdES.
	 * @return Propiedades para la configuraci&oacute;n de las firmas CAdES. */
	private static Properties loadCAdESExtraParams() {
		final Properties p = new Properties();

        // Preferencias de politica de firma
        final String policyId = PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_POLICY_IDENTIFIER, ""); //$NON-NLS-1$
        if (!policyId.trim().isEmpty()) {
			p.put("policyIdentifier", policyId); //$NON-NLS-1$
	    }
        if (!PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_POLICY_HASH, "").trim().isEmpty()) { //$NON-NLS-1$
			p.put(
		    	"policyIdentifierHash", //$NON-NLS-1$
		        PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_POLICY_HASH, "") //$NON-NLS-1$
		    );
	    }
	    if (!PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_POLICY_HASH_ALGORITHM, "").trim().isEmpty()) { //$NON-NLS-1$
			p.put(
		    	"policyIdentifierHashAlgorithm", //$NON-NLS-1$
		        PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_POLICY_HASH_ALGORITHM, "") //$NON-NLS-1$
			);
	    }
	    if (!PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_POLICY_QUALIFIER, "").trim().isEmpty()) { //$NON-NLS-1$
			p.put(
		    	"policyQualifier", //$NON-NLS-1$
		        PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_POLICY_QUALIFIER, "") //$NON-NLS-1$
		    );
	    }

        // Preferencias de CAdES
        // Esta propiedad se comparte con otros formatos, hay que comprobar que signer tenemos
        p.put(
    		"mode", //$NON-NLS-1$
    		PreferencesManager.getBoolean(PreferencesManager.PREFERENCE_CADES_IMPLICIT, true) ?
				"implicit" : //$NON-NLS-1$
					"explicit" //$NON-NLS-1$
		);

        if (PreferencesManager.getBoolean(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_CONFIGURE, false)) {

        	TsaRequestExtension req;
        	if (
    			PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_EXTENSION_OID, null) != null &&
    			PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_EXTENSION_VALUE, null) != null &&
    			Base64.isBase64(PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_EXTENSION_VALUE, null))
			) {
        		try {
					req = new TsaRequestExtension(
						PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_EXTENSION_OID, null),
						PreferencesManager.getBoolean(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_OID_CRITICAL, false),
						Base64.decode(PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_EXTENSION_VALUE, null))
					);
				}
        		catch (final Exception e) {
					req = null;
				}
        	}
        	else {
        		req = null;
        	}

        	URI uri;
        	try {
				uri = new URI(PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_TSA_URL, null));
			}
        	catch (final URISyntaxException e) {
				uri = null;
			}

        	if (uri != null) {
	        	final Properties timestampExtraParams = new TsaParams(
	    			PreferencesManager.getBoolean(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_CERT_REQUIRED, true),
	    			PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_STAMP_POLICY, null),
	    			uri,
	    			PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_TSA_USR, null),
	    			PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_TSA_PWD, null),
	    			req == null ? null : new TsaRequestExtension[] { req },
					PreferencesManager.get(PreferencesManager.PREFERENCE_CADES_TIMESTAMP_HASHALGORITHM, null),
	    			null, // sslKeyStoreFile
	    			null, // sslKeyStorePassword
	    			null, // sslKeyStoreType
	    			null, // sslTrustStore
	    			null, // sslTrustStorePassword
	    			null, // sslTrustStoreType
	    			true
				).getExtraParams();
	        	p.putAll(timestampExtraParams);
        	}
        }

        return p;
	}
}

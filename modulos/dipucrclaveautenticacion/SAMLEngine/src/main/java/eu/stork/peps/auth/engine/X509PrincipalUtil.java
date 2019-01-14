package eu.stork.peps.auth.engine;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.jce.X509Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class used to decrease complexity of comparison of 2 X509principal
 *
 * @author vanegdi
 * @version $Revision: 1.00 $, $Date: 2013-05-24 20:53:51 $
 */
public final class X509PrincipalUtil{

    private static final ASN1ObjectIdentifier[] DER_OBJECT_IDENTIFIERS_ARRAY= {
            X509Principal.CN,
            X509Principal.OU,
            X509Principal.O,
            X509Principal.L,
            X509Principal.ST,
            X509Principal.C,
            X509Principal.E
    };
    
    private static final Logger LOG = LoggerFactory.getLogger(eu.stork.peps.auth.engine.X509PrincipalUtil.class);

    /**
     * Compares 2 X509Principals to detect if they equals
     * @param principal1
     * @param principal2
     * @return true if arguments are not null and equals
     */
    public static boolean equals(X509Principal principal1, X509Principal principal2) {
        boolean continueProcess = true;
        if (principal1 == null || principal2 == null){
            return false;
        }
        
        int cpt = 0;
        while(continueProcess && cpt < DER_OBJECT_IDENTIFIERS_ARRAY.length){
            continueProcess = continueProcess && x509ValuesByIdentifierEquals(principal1, principal2, DER_OBJECT_IDENTIFIERS_ARRAY[cpt]);
            cpt++;
        }
        return continueProcess;
    }
    
    public static boolean equals2(X509Principal principal1, X509Principal principal2) {
        
        if (principal1 == null || principal2 == null){
            return false;
        }
        
        if (principal1.getName().equals(principal2.getName()))
        	return true;
        else
        	return false;
        
    }

    private static boolean x509ValuesByIdentifierEquals(X509Principal principal1, X509Principal principal2, ASN1ObjectIdentifier identifier){
        return principal1.getValues(identifier).equals(principal2.getValues(identifier));
    }

    private X509PrincipalUtil(){
        // default contructor
        LOG.error("Fake X509PrincipalUtil : never be called");
    }
}

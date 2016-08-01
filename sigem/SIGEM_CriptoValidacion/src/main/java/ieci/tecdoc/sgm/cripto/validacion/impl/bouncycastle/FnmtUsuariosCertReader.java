package ieci.tecdoc.sgm.cripto.validacion.impl.bouncycastle;

import ieci.tecdoc.sgm.core.services.cripto.validacion.InfoCertificado;

import java.security.cert.X509Certificate;

/**
 * author [DipuCR-Agustin]  #130 
 */
public class FnmtUsuariosCertReader implements IReaderCert {
	
	final static String CADENA = "CN=AC FNMT Usuarios, OU=Ceres, O=FNMT-RCM, C=ES";
    final static String CN = "CN=";
    final static String SERIALNUMBER = "SERIALNUMBER=";
    final static String SURNAME = "SURNAME=";
    final static String SURNAME_SEPARADOR = " ";
    final static String GIVENNAME = "GIVENNAME=";
    final static String SEPARADOR_CAMPOS_SUBJECT_DN = ", ";
    final static String SEPARADOR = " - ";       

	public InfoCertificado getInfo(X509Certificate cert) {
		
		String nombre;
		String nombre_sinapellidos;		
	    String nif;
	    String cif;
	    String numeroSerie;
	    	    
	    String issuer = cert.getIssuerDN().toString();
	    String asunto = cert.getSubjectDN().toString();
	    
	    String [] datosDni = asunto.split(SEPARADOR_CAMPOS_SUBJECT_DN);	 
	    
	    //Nombre y dni
	    String nombreDni = datosDni[0];
		String [] vNombreDni = nombreDni.split(SEPARADOR);
		nombre = vNombreDni[0].split(CN)[1];
		nif = vNombreDni[1];
		
		//Nombre sin apellidos
		String nombre_sinapellidos_temp = datosDni[1];
		String [] vNombreSinApellidos = nombre_sinapellidos_temp.split(GIVENNAME);
		nombre_sinapellidos = vNombreSinApellidos[1];		
		
		//En el asunto el serial number viene con el dni, lo asigno como cif
		String [] cifSeriarNumber = datosDni[3].split(SERIALNUMBER);		
		cif = cifSeriarNumber[1];    	
    	
		//Numero de serie del certificado
    	numeroSerie = cert.getSerialNumber().toString();

        InfoCertificado infoCertificado = new InfoCertificado();
        
        if (cif!=null){   
        	cif = cif.replace("\"", "");
        	cif = cif.replace("DNI", "");
        }
        
        if (issuer!=null)
        	issuer = issuer.replace("\"", "");
        
        if (nombre!=null)
        	nombre = nombre.replace("\"", "");
        
        if (nombre_sinapellidos!=null)
        	nombre_sinapellidos = nombre_sinapellidos.replace("\"", "");
        
        if (nif!=null){
        	nif = nif.replace("\"", "");
        	nif = nif.replace("DNI", "");
        }
        
        if (numeroSerie!=null)
        	numeroSerie = numeroSerie.replace("\"", "");
        
        if (asunto!=null)
        	asunto = asunto.replace("\"", "");        
        
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
		return CADENA.equals(cert.getIssuerDN().toString());
	}

}

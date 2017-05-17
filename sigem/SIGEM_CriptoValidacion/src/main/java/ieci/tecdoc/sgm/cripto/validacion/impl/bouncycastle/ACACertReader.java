package ieci.tecdoc.sgm.cripto.validacion.impl.bouncycastle;

import ieci.tecdoc.sgm.core.services.cripto.validacion.InfoCertificado;

import java.security.cert.X509Certificate;

/**
 * author [DipuCR-Agustin]  #311
 */
public class ACACertReader implements IReaderCert {
	
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
	    	    
	    String issuer = cert.getIssuerDN().toString();
	    String asunto = cert.getSubjectDN().toString();
	    
	    String [] datosDni = asunto.split(SEPARADOR_CAMPOS_SUBJECT_DN);	 
	    
	    //Nombre y dni
	    //String nombreDni = datosDni[0];
		//String [] vNombreDni = nombreDni.split(SEPARADOR);
		//nombre = vNombreDni[0].split(CN)[1];
		//nif = vNombreDni[1];
		
		//Nombre sin apellidos
		String nombre_sinapellidos_temp = datosDni[6];
		String [] vNombreSinApellidos = nombre_sinapellidos_temp.split(GIVENNAME);
		nombre_sinapellidos = vNombreSinApellidos[1];
		
		//Apellidos
		String apellidos_temp = datosDni[5];
		String [] vApellidos = apellidos_temp.split(SURNAME);
		apellidos = vApellidos[1];
		
		if (nombre_sinapellidos!=null && apellidos!=null){
			nombre = nombre.concat(nombre_sinapellidos);
			nombre = nombre.concat(" ");
			nombre = nombre.concat(apellidos);
		}
		
		//En el asunto el serial number viene con el dni, lo asigno como cif
		String [] serialNumberMasDni = datosDni[7].split(SERIALNUMBER);
		numeroSerie = serialNumberMasDni[1];
		String [] serialNumberDni = serialNumberMasDni[1].split(SEPARADOR);  
		nif = serialNumberDni[1];  
		cif = nif;  
    	
		//Numero de serie del certificado
    	//numeroSerie = cert.getSerialNumber().toString();

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

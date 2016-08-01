package ieci.tecdoc.sgm.cripto.validacion.impl.bouncycastle;

import ieci.tecdoc.sgm.core.services.cripto.validacion.InfoCertificado;

import java.security.cert.X509Certificate;

/**
 * author [DipuCR-Agustin]  #130 
 */
public class FnmtAcAdministracionPublicaCertReader implements IReaderCert {
	
	final static String CADENA = "CN=AC Administración Pública";
    final static String CN = "CN=";
    final static String SERIALNUMBER = "SERIALNUMBER=";
    final static String O = "O=";
    final static String OU = "OU=";
    final static String SURNAME = "SURNAME=";
    final static String SURNAME_SEPARADOR = " ";
    final static String GIVENNAME = "GIVENNAME=";
    final static String SEPARADOR_CAMPOS_SUBJECT_DN = ", ";
    final static String SEPARADOR = " - ";       

	public InfoCertificado getInfo(X509Certificate cert) {
		
		String nombre="";
		String nombre_sinapellidos="";		
	    String nif="";
	    String cif="";
	    String numeroSerie="";
	    String nombre_entidad="";
	    	    
	    String issuer = cert.getIssuerDN().toString();
	    String asunto = cert.getSubjectDN().toString();
	    
	    String [] datosDni = asunto.split(SEPARADOR_CAMPOS_SUBJECT_DN);	 
	    
	    //Nombre y dni
	    String nombreDni = datosDni[0];
		String [] vNombreDni = nombreDni.split(SEPARADOR);
		nombre = vNombreDni[0].split(CN)[1];
		
		try{
			nif = vNombreDni[1];
		}
		catch(Exception e){
			nif ="";
		}				
		
		
		//Nombre sin apellidos
		String nombre_sinapellidos_temp = datosDni[1];
		String [] vNombreSinApellidos = nombre_sinapellidos_temp.split(GIVENNAME);
		
		try{
			nombre_sinapellidos = vNombreSinApellidos[1];
		}
		catch(Exception e){
			nombre_sinapellidos = nombre;
		}
		
		//En el asunto el serial number viene con el dni, lo asigno como cif
		String [] cifSeriarNumber;
		String [] nombreEntidad;
		String [] ou_nombreEntidad;
		
		try{
			
			cifSeriarNumber= datosDni[3].split(SERIALNUMBER);		
			cif = cifSeriarNumber[1];		
					
		}
		catch(Exception e){//Para el caso del sello electronico de Diputacion
			cifSeriarNumber = datosDni[1].split(SERIALNUMBER);
			cif = cifSeriarNumber[1];
			nombreEntidad = datosDni[3].split(O);
			ou_nombreEntidad  = datosDni[2].split(OU);
			nombre_entidad = nombreEntidad[1].concat(" - ").concat(ou_nombreEntidad[1]);			
		}
		
		if(nif.equals("")){
			nif = cif;
		}
		
		if(nombre_entidad.equals("")){
			nombre_entidad = nombre;
		}
    	
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
	    infoCertificado.setCorporateName(nombre_entidad);
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

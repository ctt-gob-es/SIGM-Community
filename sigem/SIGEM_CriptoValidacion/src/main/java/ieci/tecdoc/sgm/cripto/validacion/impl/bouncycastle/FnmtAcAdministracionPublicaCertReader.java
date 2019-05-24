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
	    
		/* UPNA-002 Incluimos aquí nueva forma de búsqueda con bucle for e ifs para buscar los campos independiente de posiciones fijas para no tener problemas con
							cambios en el orden de la información de los certificados al renovarlos (ha pasado con el de Sello de Organo que trae un nuevo campo).
		*/
		String [] nombreEntidad={""};
		String [] ou_nombreEntidad={""};
		
		for (int i=0;i<datosDni.length;i++){
			//Nombre y dni
			if (datosDni[i].indexOf(CN)>-1){
			    String nombreDni = datosDni[i];
				String [] vNombreDni = nombreDni.split(SEPARADOR);
				nombre = vNombreDni[0].split(CN)[1];
				
				try{
					nif = vNombreDni[1];
				}
				catch(Exception e){
					nif ="";
				}	
			}
			//Nombre sin apellidos
			if (datosDni[i].indexOf(GIVENNAME)>-1){
				String nombre_sinapellidos_temp = datosDni[i];
				String [] vNombreSinApellidos = nombre_sinapellidos_temp.split(GIVENNAME);
				
				try{
					nombre_sinapellidos = vNombreSinApellidos[1];
				}
				catch(Exception e){
					nombre_sinapellidos = nombre;
				}
			}
			//En el asunto el serial number viene con el dni, lo asigno como cif
			if (datosDni[i].indexOf(SERIALNUMBER)>-1){
			String [] cifSeriarNumber;
				
				cifSeriarNumber= datosDni[i].split(SERIALNUMBER);		
				cif = cifSeriarNumber[1];					
			}
			if (datosDni[i].indexOf(O)>-1){
				nombreEntidad = datosDni[i].split(O);
			}
			if (datosDni[i].indexOf(OU)>-1){
				ou_nombreEntidad  = datosDni[i].split(OU);		
			}
		}
		nombre_entidad = nombreEntidad[1].concat(" - ").concat(ou_nombreEntidad[1]);
		
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

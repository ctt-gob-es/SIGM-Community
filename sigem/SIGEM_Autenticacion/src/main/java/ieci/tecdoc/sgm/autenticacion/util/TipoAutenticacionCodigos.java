package ieci.tecdoc.sgm.autenticacion.util;

public class TipoAutenticacionCodigos {
	public static final int NONE = 0;
	public static final int WEB_USER = 1;
	public static final int X509_CERTIFICATE = 2;
	public static final int WEB_USER_AND_CERTIFICATE = 3;
	
	//[eCenpri-Manu Ticket #295] +* ALSIGM3 Nuevo proyecto Árbol Documental.
	public static final int SIN_AUTENTICACION = 4;
	
	//[DipuCR-Agustin] #548 integrar Cl@ve autentificacion
	public static final int CLAVE = 5;
	public static final int WEB_USER_AND_CLAVE = 6;
	public static final int CERTIFICATE_AND_CLAVE = 7;
	public static final int WEB_USER_CERT_AND_CLAVE = 8;
	
	
}
package es.dipucr.bdns.exception;

@SuppressWarnings("serial")
public class BDNSException extends Exception {
	
	// Constantes númericas
	public static final Integer COD_ERROR_CONVO = 0;
	public static final Integer COD_ERROR_PERSO = 1;
	public static final Integer COD_ERROR_CONCE = 2;
	public static final Integer COD_ERROR_PAGOS = 3;
	public static final Integer COD_ERROR_DEVOL = 3;
	
	public static final String DESC_ERROR_CONVO = "Error en el servicio web de CONVOCATORIAS.";
	public static final String DESC_ERROR_PERSO = "Error en el servicio web de DATOS PERSONALES.";
	public static final String DESC_ERROR_CONCE = "Error en el servicio web de CONCESIONES.";
	public static final String DESC_ERROR_PAGOS = "Error en el servicio web de PAGOS.";
	public static final String DESC_ERROR_DEVOL = "Error en el servicio web de DEVOLUCIONES/REINTEGROS.";
	
	private static final String [] ERR = {
		DESC_ERROR_CONVO,
		DESC_ERROR_PERSO,
		DESC_ERROR_CONCE,
		DESC_ERROR_PAGOS,
		DESC_ERROR_DEVOL
	};
	
	private String codEstado;
	private String codEstadoSecundario;
	private String literalError;
	
	public BDNSException(int codApp, String codEstado, String codEstadoSecundario, String literalError){
		super(ERR[codApp] + "Error " + codEstadoSecundario + ": " + literalError);
		this.codEstado = codEstado;
		this.codEstadoSecundario = codEstadoSecundario;
		this.literalError = literalError;
	}

	public BDNSException(int codApp, String codEstadoSecundario, String literalError){
		this(codApp, null, codEstadoSecundario, literalError);
	}

	public String getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}

	public String getCodEstadoSecundario() {
		return codEstadoSecundario;
	}

	public void setCodEstadoSecundario(String codEstadoSecundario) {
		this.codEstadoSecundario = codEstadoSecundario;
	}

	public String getLiteralError() {
		return literalError;
	}

	public void setLiteralError(String literalError) {
		this.literalError = literalError;
	}
	
}

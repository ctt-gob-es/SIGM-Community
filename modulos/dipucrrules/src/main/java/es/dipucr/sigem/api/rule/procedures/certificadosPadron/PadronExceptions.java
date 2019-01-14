package es.dipucr.sigem.api.rule.procedures.certificadosPadron;

import java.util.HashMap;

public class PadronExceptions {

	private static HashMap<String , String> hashExcepciones = new HashMap<String, String>();
	
	interface CODIGO{
		public static final String COD001 = "PMH_DOC_001";
		public static final String COD002 = "PMH_DOC_002";
		public static final String COD003 = "PMH_DOC_003";
		public static final String COD004 = "PMH_DOC_004";
		public static final String COD005 = "PMH_DOC_005";
		public static final String COD006 = "PMH_DOC_006";
		public static final String COD007 = "PMH_DOC_007";
	}
	
	interface DESCRIPCION{
		public static final String DESC001 = "Error general. Si el error es persistente contacte con atm2 para más información";
		public static final String DESC002 = "Identificador de habitante incorrecto";
		public static final String DESC003 = "Habitante con bloqueo de impresión";
		public static final String DESC004 = "Campos obligatorios no completados";
		public static final String DESC005 = "La Entidad indicada no existe o no está configurada";
		public static final String DESC006 = "El habitante no existe para la Entidad indicada";
		public static final String DESC007 = "Formato incorrecto de algún campo";
	}
	
	static{
		hashExcepciones.put(CODIGO.COD001, DESCRIPCION.DESC001);
		hashExcepciones.put(CODIGO.COD002, DESCRIPCION.DESC002);
		hashExcepciones.put(CODIGO.COD003, DESCRIPCION.DESC003);
		hashExcepciones.put(CODIGO.COD004, DESCRIPCION.DESC004);
		hashExcepciones.put(CODIGO.COD005, DESCRIPCION.DESC005);
		hashExcepciones.put(CODIGO.COD006, DESCRIPCION.DESC006);
		hashExcepciones.put(CODIGO.COD007, DESCRIPCION.DESC007);
	}
	
	public static String getDescripcion(String codigo){
		String error = "[" + codigo + "] " + hashExcepciones.get(codigo);
		return error;
	}
}


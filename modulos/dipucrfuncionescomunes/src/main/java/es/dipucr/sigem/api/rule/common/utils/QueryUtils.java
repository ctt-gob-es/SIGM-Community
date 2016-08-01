package es.dipucr.sigem.api.rule.common.utils;

public class QueryUtils {
	
	//Queries para obtener las partes del NUMEXP
	public static final String NUMEXP_YEAR = "SUBSTRING(SPLIT_PART(NUMEXP, '/',1) FROM LENGTH(SPLIT_PART(NUMEXP, '/',1)) - 3)::INT";
	public static final String NUMEXP_NUMBER = "SPLIT_PART(NUMEXP, '/',2)::INT";
	
	//Expedientes relacionados
	public interface EXPRELACIONADOS{
		public static final String NUMEXPHIJO_YEAR = "SUBSTRING(SPLIT_PART(NUMEXP_HIJO, '/',1) FROM LENGTH(SPLIT_PART(NUMEXP_HIJO, '/',1)) - 3)::INT";
		public static final String NUMEXPHIJO_NUMBER = "SPLIT_PART(NUMEXP_HIJO, '/',2)::INT";
		public static final String ORDER_DESC = "ORDER BY " + NUMEXPHIJO_YEAR + " DESC, " + NUMEXPHIJO_NUMBER + " DESC";
		public static final String ORDER_ASC =  "ORDER BY " + NUMEXPHIJO_YEAR + " ASC, "  + NUMEXPHIJO_NUMBER + " ASC";
	}

}

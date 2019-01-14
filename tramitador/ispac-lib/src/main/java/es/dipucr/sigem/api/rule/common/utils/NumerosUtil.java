package es.dipucr.sigem.api.rule.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumerosUtil {

	public static double redondear(double valor, int numDecimales){
		
		BigDecimal bd = new BigDecimal(valor);
		bd = bd.setScale(2, RoundingMode.HALF_DOWN);
		return bd.doubleValue();
	}
	
	public static double redondear(double valor){
		
		return redondear(valor, 2);
	}
	
	
}

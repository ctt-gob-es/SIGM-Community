package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

public class AyudasssUtil {

	@SuppressWarnings("unused")
	private static final String TIPO_FUNCIONARIO = "F";
	private static final String TIPO_LABORAL = "L";
	private static final String TIPO_LABORAL_DESC = "LABORAL";
	
	//[dipucr-Felipe #715]
	private static final String TIPO_LABORAL_INDEFINIDO = "N";
	private static final String TIPO_CONTRATADO = "C";
	
	private static final String DESC_FUNCIONARIO = "Funcionario, Interino y Eventual";
	private static final String DESC_LABORAL = "Laboral";
	
	private static final String CONVENIO_FUNCIONARIO = "Acuerdo Marco del Personal Funcionario";
	private static final String CONVENIO_LABORAL = "Convenio Colectivo del Personal Laboral";
	
	private static final String RECURSO_FUNCIONARIOS = "Pers.Fis.-Empr.";
	private static final String RECURSO_LABORALES = "Asun.Der.Laboral";
	
	/**
	 * Devuelve el recurso por tipo régimen
	 * @param tipoRegimen
	 * @return
	 */
	public static String getRecurso(String tipoRegimen){
		String recurso = RECURSO_FUNCIONARIOS;
		if (esTipoLaboral(tipoRegimen)){
			recurso = RECURSO_LABORALES;
		}
		return recurso;
	}
	
	/**
	 * Devuelve la descripción del tipo de regimen
	 * @param tipoRegimen
	 * @return
	 */
	public static String getDescRegimen(String tipoRegimen){
		
		String descRegimen;
		if (esTipoLaboral(tipoRegimen)){
			descRegimen = DESC_LABORAL;
		}
		else{
			descRegimen = DESC_FUNCIONARIO;
		}
		return descRegimen;
	}
	
	/**
	 * Devuelve la convenio del tipo de regimen
	 * @param tipoRegimen
	 * @return
	 */
	public static String getDescConvenio(String tipoRegimen){
		
		String convenio;
		if (esTipoLaboral(tipoRegimen)){
			convenio = CONVENIO_LABORAL;
		}
		else{
			convenio = CONVENIO_FUNCIONARIO;
		}
		return convenio;
	}
	
	/**
	 * Devuelve si el tipo régimen es Laboral
	 * Compara con el código 'L' o con la descripción 'LABORAL'
	 * [dipucr-Felipe #715] Añadimos el tipo contratado 'C' y el laboral indefinido 'N'
	 * @param tipoRegimen
	 * @return
	 */
	public static boolean esTipoLaboral(String tipoRegimen){
		
		return TIPO_LABORAL.equals(tipoRegimen) || TIPO_LABORAL_INDEFINIDO.equals(tipoRegimen) 
				|| TIPO_CONTRATADO.equals(tipoRegimen) 
				|| TIPO_LABORAL_DESC.equals(tipoRegimen);
	}

}

package aww.sigem.expropiaciones.catastro.entidades;
/**
 * Clase que representa una subparcela
 * @author root
 *
 */
public class Subparcela {

	/**
	 * Campos a almacenar de una subparcela
	 * 
	 */
	private static final String IDENTIFICACION_SUBPARCELA="SUB";
	private String identificacion_subparcela; 
	private static final String CULTIVO = "CUL";
	private String cultivo;
	private static final String INTENSIDAD = "INT";
	private String intensidad;
	private static final String SUPERFICIE_SUBPARCELA ="SUP";
	private String superficie_subparcela;
	private static final String VALOR_CATASTRAL_SUBPARCELA="VCS";
	private String valor_catastra_subparcela;
	
	public boolean equals(Object obj) {
		if(obj instanceof Subparcela){
			Subparcela subparcela = (Subparcela)obj;
			if(this.identificacion_subparcela!=null&&this.identificacion_subparcela.equals(subparcela.getIdentificacion_subparcela())){
				return true;
			}else{
				return false;
			}
		}
		
		
		return super.equals(obj);
	}

	public void setValue(String tagName, String value){
		if(tagName!=null&&tagName.equals(IDENTIFICACION_SUBPARCELA)){
			this.identificacion_subparcela=value;
		}else if(tagName!=null&&tagName.equals(CULTIVO)){
			this.cultivo = value;
		}else if(tagName!=null&&tagName.equals(INTENSIDAD)){
			this.intensidad=value;
		}else if(tagName!=null&&tagName.equals(SUPERFICIE_SUBPARCELA)){
			this.superficie_subparcela=value;
		}else if(tagName!=null&&tagName.equals(VALOR_CATASTRAL_SUBPARCELA)){
			this.valor_catastra_subparcela=value;
		}
	}

	public String getIdentificacion_subparcela() {
		return identificacion_subparcela;
	}

	public void setIdentificacion_subparcela(String identificacion_subparcela) {
		this.identificacion_subparcela = identificacion_subparcela;
	}

	public String getCultivo() {
		return cultivo;
	}

	public void setCultivo(String cultivo) {
		this.cultivo = cultivo;
	}

	public String getIntensidad() {
		return intensidad;
	}
	
	public void setIntensidad(String intensidad) {
		this.intensidad = intensidad;
	}
	
	public String getSuperficie_subparcela() {
		return superficie_subparcela;
	}

	public void setSuperficie_subparcela(String superficie_subparcela) {
		this.superficie_subparcela = superficie_subparcela;
	}

	public String getValor_catastra_subparcela() {
		return valor_catastra_subparcela;
	}

	public void setValor_catastra_subparcela(String valor_catastra_subparcela) {
		this.valor_catastra_subparcela = valor_catastra_subparcela;
	}
	
	public String toString(){
		StringBuffer sbf = new StringBuffer();
		sbf.append("=========================================");
		sbf.append("\n").append("= IDENTIFICACION SUBPARCELA: ").append((identificacion_subparcela!=null)?identificacion_subparcela:"");
		sbf.append("\n").append("= CULTIVO: ").append((cultivo!=null)?cultivo:"");
		sbf.append("\n").append("= INTENSIDAD: ").append((intensidad!=null)?intensidad:"");
		sbf.append("\n").append("= SUPERFICIE SUBPARCELA: ").append((superficie_subparcela!=null)?superficie_subparcela:"");
		sbf.append("\n").append("= VALOR CATASTRAL SUBPARCELA: ").append((valor_catastra_subparcela!=null)?valor_catastra_subparcela:"");
		sbf.append("\n").append("=========================================\n");
		return sbf.toString();
	}
}

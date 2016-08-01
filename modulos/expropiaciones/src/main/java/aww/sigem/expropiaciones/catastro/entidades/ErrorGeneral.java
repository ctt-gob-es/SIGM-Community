package aww.sigem.expropiaciones.catastro.entidades;

public class ErrorGeneral {
	/**
	 * COD
	 */
	private static final String COD="COD";
	private String codigo; 
	/**
	 * DES
	 */
	private static final String DES = "DES";
	private String descripcion;
	
	public void setValue(String tagName, String value){
		if(tagName!=null&&tagName.equals(COD)){
			this.codigo=value;
		}else if(tagName!=null&&tagName.equals(DES)){
			this.descripcion=value;
		}		
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	

}

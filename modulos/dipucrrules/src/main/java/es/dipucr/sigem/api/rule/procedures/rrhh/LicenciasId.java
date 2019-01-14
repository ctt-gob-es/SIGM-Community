package es.dipucr.sigem.api.rule.procedures.rrhh;

public class LicenciasId {

	public static final String ID_SEPARATOR = "-";
	
	private String nif;
	private int anio;
	private String nlic;
	
	public LicenciasId(){}
	
	public LicenciasId(String idCompleto){
		//Desglosamos el nif, el año y el nlic
		String [] arrIdCompleto = idCompleto.split(ID_SEPARATOR);
		nif = arrIdCompleto[0];
		anio = Integer.valueOf(arrIdCompleto[1]);
		nlic = arrIdCompleto[2];
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getNlic() {
		return nlic;
	}

	public void setNlic(String nlic) {
		this.nlic = nlic;
	}
	
}

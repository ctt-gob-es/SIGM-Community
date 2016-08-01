package es.dipucr.sigem.api.rule.common;

public class DatosPlantilla {

	protected String nombrePlantilla;
	protected String tipoDoc;
	
	public DatosPlantilla(String nombrePlantilla, String tipoDoc) {
		super();
		this.nombrePlantilla = nombrePlantilla;
		this.tipoDoc = tipoDoc;
	}

	public String getNombrePlantilla() {
		return nombrePlantilla;
	}

	public void setNombrePlantilla(String nombrePlantilla) {
		this.nombrePlantilla = nombrePlantilla;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	
		
}

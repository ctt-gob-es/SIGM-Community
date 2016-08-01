package es.dipucr.bdns.objetos;

import java.math.BigDecimal;

public class Tipo {
	private String tipoFinanciacion;
	private BigDecimal importeFinanciacion;
	public String getTipoFinanciacion() {
		return tipoFinanciacion;
	}
	public void setTipoFinanciacion(String tipoFinanciacion) {
		this.tipoFinanciacion = tipoFinanciacion;
	}
	public BigDecimal getImporteFinanciacion() {
		return importeFinanciacion;
	}
	public void setImporteFinanciacion(BigDecimal importeFinanciacion) {
		this.importeFinanciacion = importeFinanciacion;
	}
}

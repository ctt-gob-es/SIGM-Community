package es.dipucr.contratacion.objeto;

import java.util.Date;

import es.dipucr.contratacion.client.beans.Campo;

public class Garantia {
	private String amountRate;
	private Date constitutionPeriod;
	private Campo tipoGarantia;
	private String importeGarantia;
	private String periodoGarantia;
	public String getAmountRate() {
		return amountRate;
	}
	public void setAmountRate(String amountRate) {
		this.amountRate = amountRate;
	}
	public Date getConstitutionPeriod() {
		return constitutionPeriod;
	}
	public void setConstitutionPeriod(Date constitutionPeriod) {
		this.constitutionPeriod = constitutionPeriod;
	}
	public Campo getTipoGarantia() {
		return tipoGarantia;
	}
	public void setTipoGarantia(Campo tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
	public String getImporteGarantia() {
		return importeGarantia;
	}
	public void setImporteGarantia(String importeGarantia) {
		this.importeGarantia = importeGarantia;
	}
	public String getPeriodoGarantia() {
		return periodoGarantia;
	}
	public void setPeriodoGarantia(String periodoGarantia) {
		this.periodoGarantia = periodoGarantia;
	}
}

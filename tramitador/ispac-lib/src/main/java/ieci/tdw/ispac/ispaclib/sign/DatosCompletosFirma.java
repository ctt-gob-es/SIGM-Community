package ieci.tdw.ispac.ispaclib.sign;

import java.util.Date;

public class DatosCompletosFirma {

	protected InfoFirmante firmante;
	protected Date fechaFirma;
	
	public DatosCompletosFirma(InfoFirmante firmante, Date fechaFirma) {
		super();
		this.firmante = firmante;
		this.fechaFirma = fechaFirma;
	}

	public InfoFirmante getFirmante() {
		return firmante;
	}

	public void setFirmante(InfoFirmante firmante) {
		this.firmante = firmante;
	}

	public Date getFechaFirma() {
		return fechaFirma;
	}

	public void setFechaFirma(Date fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
	
}

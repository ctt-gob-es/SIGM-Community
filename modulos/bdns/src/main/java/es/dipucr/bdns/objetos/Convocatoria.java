package es.dipucr.bdns.objetos;

import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos;

public class Convocatoria {
	private String idConvocatoria;
	private DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov datosGeneralesCov;
	private DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora datosBaseReguladora;
	private DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion datosSolicitudJustificacionFinanciacion;
	private DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos otrosDatos;
	private DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto extracto;
	private DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos otrosDocumentos;
	
	public String getIdConvocatoria() {
		return idConvocatoria;
	}
	public void setIdConvocatoria(String idConvocatoria) {
		this.idConvocatoria = idConvocatoria;
	}
	public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov getDatosGeneralesCov() {
		return datosGeneralesCov;
	}
	public void setDatosGeneralesCov(
			DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCov datosGeneralesCov) {
		this.datosGeneralesCov = datosGeneralesCov;
	}
	public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora getDatosBaseReguladora() {
		return datosBaseReguladora;
	}
	public void setDatosBaseReguladora(
			DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora datosBaseReguladora) {
		this.datosBaseReguladora = datosBaseReguladora;
	}
	public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion getDatosSolicitudJustificacionFinanciacion() {
		return datosSolicitudJustificacionFinanciacion;
	}
	public void setDatosSolicitudJustificacionFinanciacion(
			DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacion datosSolicitudJustificacionFinanciacion) {
		this.datosSolicitudJustificacionFinanciacion = datosSolicitudJustificacionFinanciacion;
	}
	public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos getOtrosDatos() {
		return otrosDatos;
	}
	public void setOtrosDatos(
			DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatos otrosDatos) {
		this.otrosDatos = otrosDatos;
	}
	public DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto getExtracto() {
		return extracto;
	}
	public void setExtracto(
			DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto extracto) {
		this.extracto = extracto;
	}
	public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos getOtrosDocumentos() {
		return otrosDocumentos;
	}
	public void setOtrosDocumentos(
			DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos otrosDocumentos) {
		this.otrosDocumentos = otrosDocumentos;
	}
}

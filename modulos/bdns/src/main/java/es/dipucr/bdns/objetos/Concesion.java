package es.dipucr.bdns.objetos;

import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;

import java.lang.reflect.Field;
import java.util.Date;

import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades;

public class Concesion {

	private String idConvocatoria;
	private IThirdPartyAdapter tercero;
	private String referenciaConcesion;
	private String instrumentoAyuda;
    private Date fechaConcesion;
    private Double costeConcesion;
    private Double subvencionConcesion;
    private Double ayudaConcesion;
    private Double ayudaEquivalenteConcesion;
    private Double devolucionConcesion;
    private String objetivoConcesion;
    private DatosAnualidadesAnualidades [] arrAnualidades;
    
	public Concesion() {
		super();
	}

	public String getIdConvocatoria() {
		return idConvocatoria;
	}

	public void setIdConvocatoria(String idConvocatoria) {
		this.idConvocatoria = idConvocatoria;
	}

	public IThirdPartyAdapter getTercero() {
		return tercero;
	}

	public void setTercero(IThirdPartyAdapter tercero) {
		this.tercero = tercero;
	}

	public String getReferenciaConcesion() {
		return referenciaConcesion;
	}

	public void setReferenciaConcesion(String referenciaConcesion) {
		this.referenciaConcesion = referenciaConcesion;
	}

	public String getInstrumentoAyuda() {
		return instrumentoAyuda;
	}

	public void setInstrumentoAyuda(String instrumentoAyuda) {
		this.instrumentoAyuda = instrumentoAyuda;
	}

	public Date getFechaConcesion() {
		return fechaConcesion;
	}

	public void setFechaConcesion(Date fechaConcesion) {
		this.fechaConcesion = fechaConcesion;
	}

	public Double getCosteConcesion() {
		return costeConcesion;
	}

	public void setCosteConcesion(Double costeConcesion) {
		this.costeConcesion = costeConcesion;
	}

	public Double getSubvencionConcesion() {
		return subvencionConcesion;
	}

	public void setSubvencionConcesion(Double subvencionConcesion) {
		this.subvencionConcesion = subvencionConcesion;
	}

	public Double getAyudaConcesion() {
		return ayudaConcesion;
	}

	public void setAyudaConcesion(Double ayudaConcesion) {
		this.ayudaConcesion = ayudaConcesion;
	}

	public Double getAyudaEquivalenteConcesion() {
		return ayudaEquivalenteConcesion;
	}

	public void setAyudaEquivalenteConcesion(Double ayudaEquivalenteConcesion) {
		this.ayudaEquivalenteConcesion = ayudaEquivalenteConcesion;
	}

	public Double getDevolucionConcesion() {
		return devolucionConcesion;
	}

	public void setDevolucionConcesion(Double devolucionConcesion) {
		this.devolucionConcesion = devolucionConcesion;
	}

	public java.lang.String getObjetivoConcesion() {
		return objetivoConcesion;
	}

	public void setObjetivoConcesion(java.lang.String objetivoConcesion) {
		this.objetivoConcesion = objetivoConcesion;
	}
	
	public DatosAnualidadesAnualidades[] getArrAnualidades() {
		return arrAnualidades;
	}

	public void setArrAnualidades(DatosAnualidadesAnualidades[] arrAnualidades) {
		this.arrAnualidades = arrAnualidades;
	}

	public String toStringLine() throws Exception {
		StringBuffer sbConcesion = new StringBuffer();
		sbConcesion.append("[");
		Field[] fields = Concesion.class.getDeclaredFields();
		for (Field field : fields) {
			sbConcesion.append(field.getName());
			sbConcesion.append("=");
			sbConcesion.append(field.get(this));
			sbConcesion.append("; ");
		}
		sbConcesion.append("]");
		return sbConcesion.toString();
	}
}

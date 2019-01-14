package es.dipucr.bdns.objetos;

import java.lang.reflect.Field;
import java.util.Date;

public class Devolucion {

	private String idConvocatoria;
	private String numexpConvocatoria;
	private String cifBeneficiario;
	private String referenciaConcesion;
	private String referenciaDevolucion;
	private Double importeDevolucion;
	private Double importeIntereses;
    private Date fechaDevolucion;
    private String codigoCausa;
    private String causa;
    
	public Devolucion() {
		super();
	}
	
	public Devolucion(String idConvocatoria,
			String numexpConvocatoria, String cifBeneficiario,
			String referenciaConcesion, String referenciaDevolucion,
			Double importeDevolucion, Double importeIntereses,
			Date fechaDevolucion, String codigoCausa, String causa) {
		
		super();
		this.idConvocatoria = idConvocatoria;
		this.numexpConvocatoria = numexpConvocatoria;
		this.cifBeneficiario = cifBeneficiario;
		this.referenciaConcesion = referenciaConcesion;
		this.referenciaDevolucion = referenciaDevolucion;
		this.importeDevolucion = importeDevolucion;
		this.fechaDevolucion = fechaDevolucion;
	}



	public String getIdConvocatoria() {
		return idConvocatoria;
	}

	public void setIdConvocatoria(String idConvocatoria) {
		this.idConvocatoria = idConvocatoria;
	}
	
	public String getNumexpConvocatoria() {
		return numexpConvocatoria;
	}

	public void setNumexpConvocatoria(String numexpConvocatoria) {
		this.numexpConvocatoria = numexpConvocatoria;
	}

	public String getCifBeneficiario() {
		return cifBeneficiario;
	}

	public void setCifBeneficiario(String cifBeneficiario) {
		this.cifBeneficiario = cifBeneficiario;
	}

	public String getReferenciaConcesion() {
		return referenciaConcesion;
	}

	public void setReferenciaConcesion(String referenciaConcesion) {
		this.referenciaConcesion = referenciaConcesion;
	}

	public String getReferenciaDevolucion() {
		return referenciaDevolucion;
	}

	public void setReferenciaDevolucion(String referenciaDevolucion) {
		this.referenciaDevolucion = referenciaDevolucion;
	}

	public Double getImporteDevolucion() {
		return importeDevolucion;
	}

	public void setImporteDevolucion(Double importeDevolucion) {
		this.importeDevolucion = importeDevolucion;
	}

	public Double getImporteIntereses() {
		return importeIntereses;
	}

	public void setImporteIntereses(Double importeIntereses) {
		this.importeIntereses = importeIntereses;
	}

	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public String getCodigoCausa() {
		return codigoCausa;
	}

	public void setCodigoCausa(String codigoCausa) {
		this.codigoCausa = codigoCausa;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public String toStringLine() throws Exception {
		StringBuffer sbDevolucion = new StringBuffer();
		sbDevolucion.append("[");
		Field[] fields = Devolucion.class.getDeclaredFields();
		for (Field field : fields) {
			sbDevolucion.append(field.getName());
			sbDevolucion.append("=");
			sbDevolucion.append(field.get(this));
			sbDevolucion.append("; ");
		}
		sbDevolucion.append("]");
		return sbDevolucion.toString();
	}
}

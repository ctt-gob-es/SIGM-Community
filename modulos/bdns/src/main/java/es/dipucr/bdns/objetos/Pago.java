package es.dipucr.bdns.objetos;

import java.lang.reflect.Field;
import java.util.Date;

public class Pago {

	private String idConvocatoria;
	private String numexpConvocatoria;
	private String cifBeneficiario;
	private String referenciaConcesion;
	private String referenciaPago;
	private Double importePagado;
    private Date fechaPago;
    private boolean bRetencion;
    
	public Pago() {
		super();
	}
	
	public Pago(String idConvocatoria, String numexpConvocatoria,
			String cifBeneficiario, String referenciaConcesion,
			String referenciaPago, Double importePagado, Date fechaPago,
			boolean bRetencion) {
		
		super();
		this.idConvocatoria = idConvocatoria;
		this.numexpConvocatoria = numexpConvocatoria;
		this.cifBeneficiario = cifBeneficiario;
		this.referenciaConcesion = referenciaConcesion;
		this.referenciaPago = referenciaPago;
		this.importePagado = importePagado;
		this.fechaPago = fechaPago;
		this.bRetencion = bRetencion;
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

	public String getReferenciaPago() {
		return referenciaPago;
	}

	public void setReferenciaPago(String referenciaPago) {
		this.referenciaPago = referenciaPago;
	}

	public Double getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(Double importePagado) {
		this.importePagado = importePagado;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public boolean isbRetencion() {
		return bRetencion;
	}

	public void setbRetencion(boolean bRetencion) {
		this.bRetencion = bRetencion;
	}

	public String toStringLine() throws Exception {
		StringBuffer sbPago = new StringBuffer();
		sbPago.append("[");
		Field[] fields = Pago.class.getDeclaredFields();
		for (Field field : fields) {
			sbPago.append(field.getName());
			sbPago.append("=");
			sbPago.append(field.get(this));
			sbPago.append("; ");
		}
		sbPago.append("]");
		return sbPago.toString();
	}
}

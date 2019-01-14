/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.ws.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FullInfoRegister {

	
	public static final String FDRID = "fdrid";
	
	public static final String NUM_REGISTRO = "fld1";
	public static final String FECHA_REGISTRO = "fld2";
	
	// ENTRADA/SALIDA
	public static final String OFICINA_REGISTRO_IN_OUT = "fld5";
	public static final String ESTADO_REGISTRO_IN_OUT = "fld6";
	public static final String ORG_ORIGEN_IN_OUT = "fld7";
	public static final String ORG_DESTINO_IN_OUT = "fld8";
	public static final String FECHA_ORIGINAL_IN_OUT = "fld12";
	
	public static final String TIPO_REGISTRO_ORIGINAL = "fld11";
	
	
	
	// ENTRADA
	public static final String REG_ORIGINAL_IN = "fld13";
	public static final String TRANSPORTE_IN = "fld14";
	public static final String NUM_TRANSPORTE_IN = "fld15";
	public static final String ASUNTO_IN = "fld16";
	public static final String RESUMEN_IN = "fld17";
//	public static final String COMENTARIO_IN = "fld18";
//	public static final String REF_EXPEDIENTE_IN = "fld19";
//	public static final String FECHA_DOCUMENTACION_IN = "fld20";
	
	// SALIDA
	// no tiene:  REG_ORIGINAL_OUT = 9;
	public static final String TRANSPORTE_OUT = "fld10";
	public static final String NUM_TRANSPORTE_OUT = "fld11";
	public static final String ASUNTO_OUT = "fld12";
	public static final String RESUMEN_OUT = "fld13";
	public static final String COMENTARIO_OUT = "fld14";
	public static final String FECHA_DOCUMENTACION_OUT = "fld15";	

	
	public static final String BOOK_REG_ELETRONICO_IN = "6";
	public static final String BOOK_REG_ELETRONICO_OUT = "7";

	
	private String fdrid;
	private String numRegistro;
	private Date fechaRegistro;
	private String estado;
	
	private String oficinaRegistro;
	private String organoOrigen;
	private String organoDestino;
	private Date fechaOriginal;
	
	private String transporte;
	private String numTransporte;
	private String asunto;
	private String resumen;
	
	///
	private String comentario;
	private String expediente;
	private Date fechaDocumentacion;
	
	private String bookTipo;
	
	private Map<String, String> extendidos = new HashMap<String, String>();

	private Acuse acuse;
	
	
	public FullInfoRegister() {
	}
	
	public void setFdrid(String fdrid) {
		this.fdrid = fdrid;
	}
	public String getFdrid() {
		return fdrid;
	}

	public void setNumRegistro(String numRegistro) {
		this.numRegistro = numRegistro;
	}
	public String getNumRegistro() {
		return numRegistro;
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getOficinaRegistro() {
		return oficinaRegistro;
	}

	public void setOficinaRegistro(String oficinaRegistro) {
		this.oficinaRegistro = oficinaRegistro;
	}

	public String getOrganoOrigen() {
		return organoOrigen;
	}

	public void setOrganoOrigen(String organoOrigen) {
		this.organoOrigen = organoOrigen;
	}

	public String getOrganoDestino() {
		return organoDestino;
	}

	public void setOrganoDestino(String organoDestino) {
		this.organoDestino = organoDestino;
	}

	public Date getFechaOriginal() {
		return fechaOriginal;
	}

	public void setFechaOriginal(Date fechaOriginal) {
		this.fechaOriginal = fechaOriginal;
	}

	public void setTrasporte(String transporte) {
		this.transporte = transporte;
	}
	public String getTransporte() {
		return transporte;
	}
	
	public String getNumTransporte() {
		return numTransporte;
	}

	public void setNumTransporte(String numTransporte) {
		this.numTransporte = numTransporte;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getExpediente() {
		return expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}

	public Date getFechaDocumentacion() {
		return fechaDocumentacion;
	}

	public void setFechaDocumentacion(Date fechaDocumentacion) {
		this.fechaDocumentacion = fechaDocumentacion;
	}

	public void setBookTipo(String bookTipo) {
		this.bookTipo = bookTipo;
	}
	public String getBookTipo() {
		return bookTipo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void addExtendido(String key, String value){
		extendidos.put(key, value);		
	}
	
	public Map<String, String> getExtendidos() {
		return extendidos;
	}
	
	public void setAcuse(Acuse acuse) {
		this.acuse = acuse;
	}
	
	public Acuse getAcuse() {
		return acuse;
	}
	
	

	@Override
	public String toString() {
		return "FullInfoRegister [fdrid=" + fdrid + ", numRegistro=" + numRegistro
				+ ", fechaRegistro=" + fechaRegistro + ", oficinaRegistro=" + oficinaRegistro
				+ ", organoOrigen=" + organoOrigen + ", organoDestino=" + organoDestino
				+ ", fechaOriginal=" + fechaOriginal + ", transporte=" + transporte
				+ ", numTransporte=" + numTransporte + ", asunto=" + asunto + ", resumen="
				+ resumen + ", bookTipo=" + bookTipo + ", extendidos=" + extendidos + "]";
	}

	
	
}

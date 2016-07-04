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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoDatosInteresados;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registro", propOrder = {
	    "numeroRegistro",
	    "fechaRegistro",
	    "estadoRegistro",
	    "origen",
	    "destino",
	    "tipoRegistro",
	    "oficina",
	    "tipoRegistroOriginal",
	    "fechaRegistroOriginal",
	    "numeroRegistroOriginal",
	    "tipoTransporte",
	    "numeroTransporte",
	    "tipoAsunto",	    
	    "resumen",
	    "refExpediente",
	    "comentario",
	    "interesados",
	    "historicoIntercambioRegistral",
	    "historicoDistribucion",
	    "historicoMovimiento",
	    "documentos"
	})
public class Registro {
 
		public static final String FDRID = "fdrid";
		
		public static final String NUM_REGISTRO = "fld1";
		public static final String FECHA_REGISTRO = "fld2";
		
		// ENTRADA/SALIDA
		public static final String OFICINA_REGISTRO = "fld5";
		public static final String ESTADO_REGISTRO = "fld6";
		public static final String ORG_ORIGEN = "fld7";
		public static final String ORG_DESTINO = "fld8";
		
		public static final String NUM_REG_ORIGINAL = "fld10";
		public static final String TIPO_REG_ORIGINAL = "fld11";
		public static final String FECHA_REG_ORIGINAL = "fld12";
		
		// ENTRADA
		public static final String TRANSPORTE_IN = "fld14";
		public static final String NUM_TRANSPORTE_IN = "fld15";
		public static final String ASUNTO_IN = "fld16";
		public static final String RESUMEN_IN = "fld17";
		public static final String COMENTARIO_IN = "fld18";
		public static final int COMENTARIO_POS_IN = 18;
		public static final int REF_EXPEDIENTE_POS_IN = 19;
		
		// SALIDA
		public static final String TRANSPORTE_OUT = "fld10";
		public static final String NUM_TRANSPORTE_OUT = "fld11";
		public static final String ASUNTO_OUT = "fld12";
		public static final String RESUMEN_OUT = "fld13";
		public static final String COMENTARIO_OUT = "fld14";
		public static final int COMENTARIO_POS_OUT = 14;

		
		@XmlElement(required = true)
	    protected String numeroRegistro;
	    @XmlElement(required = true)
	    protected String fechaRegistro;
	    @XmlElement(required = true)
	    protected String estadoRegistro;
	    @XmlElement(required = true)
	    protected String origen;
	    @XmlElement(required = true)
	    protected String destino;
	    
	    
	    protected String tipoRegistro;
	    protected String oficina;
	    protected String numeroRegistroOriginal;
	    protected String fechaRegistroOriginal;
	    protected String tipoRegistroOriginal;
	    protected String tipoTransporte;
	    protected String numeroTransporte;
	    protected String tipoAsunto;
	    protected String resumen;
	    protected String refExpediente;	    
	    protected String comentario;	    
	    

	    
	    protected ElementoDatosInteresados interesados;
	    
	    protected HistoricoIntercambioRegistral historicoIntercambioRegistral;
	    protected HistoricoDistribucion historicoDistribucion;
	    protected HistoricoMovimiento historicoMovimiento;

	    
	    protected Documentos documentos;
	    
	    
		public String getNumeroRegistro() {
			return numeroRegistro;
		}
		public void setNumeroRegistro(String numeroRegistro) {
			this.numeroRegistro = numeroRegistro;
		}
		public String getFechaRegistro() {
			return fechaRegistro;
		}
		public void setFechaRegistro(String fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
		}
		public String getEstadoRegistro() {
			return estadoRegistro;
		}
		public void setEstadoRegistro(String estadoRegistro) {
			this.estadoRegistro = estadoRegistro;
		}
		public String getOrigen() {
			return origen;
		}
		public void setOrigen(String origen) {
			this.origen = origen;
		}
		public String getDestino() {
			return destino;
		}
		public void setDestino(String destino) {
			this.destino = destino;
		}
		
		public void setResumen(String resumen) {
			this.resumen = resumen;
		}
		public String getResumen() {
			return resumen;
		}
		
		
		public String getTipoRegistro() {
			return tipoRegistro;
		}
		public void setTipoRegistro(String tipoRegistro) {
			this.tipoRegistro = tipoRegistro;
		}
		public String getOficina() {
			return oficina;
		}
		public void setOficina(String oficina) {
			this.oficina = oficina;
		}
		public String getNumeroRegistroOriginal() {
			return numeroRegistroOriginal;
		}
		public void setNumeroRegistroOriginal(String numeroRegistroOriginal) {
			this.numeroRegistroOriginal = numeroRegistroOriginal;
		}
		
		public String getFechaRegistroOriginal() {
			return fechaRegistroOriginal;
		}
		public void setFechaRegistroOriginal(String fechaRegistroOriginal) {
			this.fechaRegistroOriginal = fechaRegistroOriginal;
		}
		public String getTipoRegistroOriginal() {
			return tipoRegistroOriginal;
		}
		public void setTipoRegistroOriginal(String tipoRegistroOriginal) {
			this.tipoRegistroOriginal = tipoRegistroOriginal;
		}
		public String getTipoTransporte() {
			return tipoTransporte;
		}
		public void setTipoTransporte(String tipoTransporte) {
			this.tipoTransporte = tipoTransporte;
		}
		public String getNumeroTransporte() {
			return numeroTransporte;
		}
		public void setNumeroTransporte(String numeroTransporte) {
			this.numeroTransporte = numeroTransporte;
		}
		public String getTipoAsunto() {
			return tipoAsunto;
		}
		public void setTipoAsunto(String tipoAsunto) {
			this.tipoAsunto = tipoAsunto;
		}
		public String getRefExpediente() {
			return refExpediente;
		}
		public void setRefExpediente(String refExpediente) {
			this.refExpediente = refExpediente;
		}
		
		public void setComentario(String comentario) {
			this.comentario = comentario;
		}
		public String getComentario() {
			return comentario;
		}

		public HistoricoIntercambioRegistral getHistoricoIntercambioRegistral() {
			return historicoIntercambioRegistral;
		}
		public void setHistoricoIntercambioRegistral(
				HistoricoIntercambioRegistral historicoIntercambioRegistral) {
			this.historicoIntercambioRegistral = historicoIntercambioRegistral;
		}

		public void setHistoricoDistribucion(HistoricoDistribucion historicoDistribucion) {
			this.historicoDistribucion = historicoDistribucion;
		}
		public HistoricoDistribucion getHistoricoDistribucion() {
			return historicoDistribucion;
		}

		public void setHistoricoMovimiento(HistoricoMovimiento historicoMovimiento) {
			this.historicoMovimiento = historicoMovimiento;
		}
		public HistoricoMovimiento getHistoricoMovimiento() {
			return historicoMovimiento;
		}
		 
		
		public void setDocumentos(Documentos documentos) {
			this.documentos = documentos;
		}
		public Documentos getDocumentos() {
			return documentos;
		}
		

		 public void setInteresados(ElementoDatosInteresados interesados) {
			this.interesados = interesados;
		}
		 
		 public ElementoDatosInteresados getInteresados() {
			return interesados;
		}

}

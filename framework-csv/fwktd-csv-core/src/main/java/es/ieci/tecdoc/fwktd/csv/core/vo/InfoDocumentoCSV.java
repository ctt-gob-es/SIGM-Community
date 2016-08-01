package es.ieci.tecdoc.fwktd.csv.core.vo;

import java.util.Date;

/**
 * Información de un documento con su CSV generado.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class InfoDocumentoCSV extends InfoDocumentoCSVForm {

	private static final long serialVersionUID = 2089217516716819621L;

	/**
	 * Identificador del documento.
	 */
	private String id = null;

	/**
	 * Código Seguro de Verificación del documento.
	 */
	private String csv = null;

	/**
	 * Fecha de generación del CSV.
	 */
	private Date fechaCSV = null;

	/**
	 * Nombre de la aplicación que almacena el documento.
	 */
	private String nombreAplicacion = null;
	
	/**
	 *  [Manu Ticket #625] CVE Consulta de documentos - Añadir campos para registros de salida 
	 */
	/**
	 * Número de Registro del documento 
	 */
	private String numeroRegistro = null;
	/**
	 * Fecha de Registro del documento
	 */
	private Date fechaRegistro = null;
	/**
	 * Origne del registro
	 */
	private String origenRegistro = null;
	/**
	 * Destino del registro
	 */
	private String destinoRegistro = null;
	/**
	 *  [Manu Ticket #625] CVE Consulta de documentos - Añadir campos para registros de salida 
	 */

	/**
	 * Constructor.
	 */
	public InfoDocumentoCSV() {
		super();
	}
	
	/**
	 *  [Manu Ticket #625] CVE Consulta de documentos - Añadir campos para registros de salida 
	 */
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getOrigenRegistro() {
		return origenRegistro;
	}

	public void setOrigenRegistro(String origenRegistro) {
		this.origenRegistro = origenRegistro;
	}

	public String getDestinoRegistro() {
		return destinoRegistro;
	}

	public void setDestinoRegistro(String destinoRegistro) {
		this.destinoRegistro = destinoRegistro;
	}
	/**
	 *  [Manu Ticket #625] CVE Consulta de documentos - Añadir campos para registros de salida 
	 */

	public String getCsv() {
		return csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	public Date getFechaCSV() {
		return fechaCSV;
	}

	public void setFechaCSV(Date fechaCSV) {
		this.fechaCSV = fechaCSV;
	}

	public String getNombreAplicacion() {
		return nombreAplicacion;
	}

	public void setNombreAplicacion(String nombreAplicacion) {
		this.nombreAplicacion = nombreAplicacion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

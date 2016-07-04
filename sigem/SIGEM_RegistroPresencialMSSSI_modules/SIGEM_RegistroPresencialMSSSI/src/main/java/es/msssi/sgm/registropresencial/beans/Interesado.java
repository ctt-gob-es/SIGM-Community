/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans;

import java.io.Serializable;

/**
 * Bean que guarda los datos del eresado.
 * 
 * @author cmorenog
 */
public class Interesado implements Serializable {
	private static final long serialVersionUID = 1L;
	/** P:Persona física; J: persona jurídica.*/
	private String tipo ="P";
	private Integer id;
	private Integer idTercero;
	private String tipodoc;
	private String docIndentidad;
	private String razonSocial;  
	private String nombre;
	private String papellido;
	private String sapellido;

	private Representante representante;

	
	/**
	 * Constructor.
	 */
	public Interesado() {
		representante = new Representante();
	}
	
	/**
	 * Obtiene el valor del parámetro tipodoc.
	 * 
	 * @return tipodoc valor del campo a obtener.
	 */
	public String getTipodoc() {
		return tipodoc;
	}
	
	/**
	 * Guarda el valor del parámetro tipodoc.
	 * 
	 * @param tipodoc
	 *            valor del campo a guardar.
	 */
	public void setTipodoc(String tipodoc) {
		this.tipodoc = tipodoc;
	}
	
	/**
	 * Obtiene el valor del parámetro docIndentidad.
	 * 
	 * @return docIndentidad valor del campo a obtener.
	 */
	public String getDocIndentidad() {
		return docIndentidad;
	}
	
	/**
	 * Guarda el valor del parámetro docIndentidad.
	 * 
	 * @param docIndentidad
	 *            valor del campo a guardar.
	 */
	public void setDocIndentidad(String docIndentidad) {
		this.docIndentidad = docIndentidad;
	}
	
	/**
	 * Obtiene el valor del parámetro razonSocial.
	 * 
	 * @return razonSocial valor del campo a obtener.
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	
	/**
	 * Guarda el valor del parámetro razonSocial.
	 * 
	 * @param razonSocial
	 *            valor del campo a guardar.
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	/**
	 * Obtiene el valor del parámetro nombre.
	 * 
	 * @return nombre valor del campo a obtener.
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Guarda el valor del parámetro nombre.
	 * 
	 * @param nombre
	 *            valor del campo a guardar.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	/**
	 * Obtiene el valor del parámetro papellido.
	 * 
	 * @return papellido valor del campo a obtener.
	 */
	public String getPapellido() {
		return papellido;
	}
	
	/**
	 * Guarda el valor del parámetro papellido.
	 * 
	 * @param papellido
	 *            valor del campo a guardar.
	 */
	public void setPapellido(String papellido) {
		this.papellido = papellido;
	}
	
	/**
	 * Obtiene el valor del parámetro sapellido.
	 * 
	 * @return sapellido valor del campo a obtener.
	 */
	public String getSapellido() {
		return sapellido;
	}
	
	/**
	 * Guarda el valor del parámetro sapellido.
	 * 
	 * @param sapellido
	 *            valor del campo a guardar.
	 */
	public void setSapellido(String sapellido) {
		this.sapellido = sapellido;
	}
	/**
	 * Obtiene el valor del parámetro tipo.
	 * 
	 * @return tipo valor del campo a obtener.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * Guarda el valor del parámetro tipo.
	 * 
	 * @param tipo
	 *            valor del campo a guardar.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * Obtiene el valor del parámetro representante.
	 * 
	 * @return representante valor del campo a obtener.
	 */
	public Representante getRepresentante() {
		return representante;
	}
	/**
	 * Guarda el valor del parámetro representante.
	 * 
	 * @param representante
	 *            valor del campo a guardar.
	 */
	public void setRepresentante(Representante representante) {
		this.representante = representante;
	}
	
	/**
	 * Obtiene el valor del parámetro id.
	 * 
	 * @return id valor del campo a obtener.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Guarda el valor del parámetro id.
	 * 
	 * @param id
	 *            valor del campo a guardar.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * Obtiene el valor del parámetro idTercero.
	 * 
	 * @return idTercero valor del campo a obtener.
	 */
	public Integer getIdTercero() {
		return idTercero;
	}
	/**
	 * Guarda el valor del parámetro idTercero.
	 * 
	 * @param idTercero
	 *            valor del campo a guardar.
	 */
	public void setIdTercero(Integer idTercero) {
		this.idTercero = idTercero;
	}
}
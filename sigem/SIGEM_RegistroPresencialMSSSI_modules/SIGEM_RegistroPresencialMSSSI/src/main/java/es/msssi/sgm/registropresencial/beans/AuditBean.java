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
 * Bean que guarda la auditoria.
 * 
 * @author cmorenog
 */
public class AuditBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer idAudit = null;
	private String name = null;
	private String register = null;
	private Integer fldid = null;
	private Integer idBook = null;
	private Integer idOffice = null;
	private Integer modifType = null;
	private String valueOld = null;
	private String valueNew = null;
	
	/**
	 * Constructor.
	 */
	public AuditBean() {
	}
	

	/**
	 * Obtiene el valor del parámetro name.
	 * 
	 * @return name valor del campo a obtener.
	 */
	public String getName() {
	    return name;
	}

	/**
	 * Guarda el valor del parámetro name.
	 * 
	 * @param name
	 *            valor del campo a guardar.
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * Obtiene el valor del parámetro idAudit.
	 * 
	 * @return idAudit valor del campo a obtener.
	 */
	public Integer getIdAudit() {
	    return idAudit;
	}

	/**
	 * Guarda el valor del parámetro idAudit.
	 * 
	 * @param idAudit
	 *            valor del campo a guardar.
	 */
	public void setIdAudit(Integer idAudit) {
	    this.idAudit = idAudit;
	}

	/**
	 * Obtiene el valor del parámetro register.
	 * 
	 * @return register valor del campo a obtener.
	 */
	public String getRegister() {
	    return register;
	}

	/**
	 * Guarda el valor del parámetro register.
	 * 
	 * @param register
	 *            valor del campo a guardar.
	 */
	public void setRegister(String register) {
	    this.register = register;
	}

	/**
	 * Obtiene el valor del parámetro fldid.
	 * 
	 * @return fldid valor del campo a obtener.
	 */
	public Integer getFldid() {
	    return fldid;
	}

	/**
	 * Guarda el valor del parámetro fldid.
	 * 
	 * @param fldid
	 *            valor del campo a guardar.
	 */
	public void setFldid(Integer fldid) {
	    this.fldid = fldid;
	}

	/**
	 * Obtiene el valor del parámetro idBook.
	 * 
	 * @return idBook valor del campo a obtener.
	 */
	public Integer getIdBook() {
	    return idBook;
	}

	/**
	 * Guarda el valor del parámetro idBook.
	 * 
	 * @param idBook
	 *            valor del campo a guardar.
	 */
	public void setIdBook(Integer idBook) {
	    this.idBook = idBook;
	}

	/**
	 * Obtiene el valor del parámetro idOffice.
	 * 
	 * @return idOffice valor del campo a obtener.
	 */
	public Integer getIdOffice() {
	    return idOffice;
	}

	/**
	 * Guarda el valor del parámetro idOffice.
	 * 
	 * @param idOffice
	 *            valor del campo a guardar.
	 */
	public void setIdOffice(Integer idOffice) {
	    this.idOffice = idOffice;
	}

	/**
	 * Obtiene el valor del parámetro modifType.
	 * 
	 * @return modifType valor del campo a obtener.
	 */
	public Integer getModifType() {
	    return modifType;
	}

	/**
	 * Guarda el valor del parámetro modifType.
	 * 
	 * @param modifType
	 *            valor del campo a guardar.
	 */
	public void setModifType(Integer modifType) {
	    this.modifType = modifType;
	}

	/**
	 * Obtiene el valor del parámetro modifType.
	 * 
	 * @return modifType valor del campo a obtener.
	 */
	public String getValueOld() {
	    return valueOld;
	}

	/**
	 * Guarda el valor del parámetro valueNew.
	 * 
	 * @param valueNew
	 *            valor del campo a guardar.
	 */
	public void setValueOld(String valueOld) {
	    this.valueOld = valueOld;
	}

	/**
	 * Obtiene el valor del parámetro valueNew.
	 * 
	 * @return valueNew valor del campo a obtener.
	 */
	public String getValueNew() {
	    return valueNew;
	}

	/**
	 * Guarda el valor del parámetro modifType.
	 * 
	 * @param modifType
	 *            valor del campo a guardar.
	 */
	public void setValueNew(String valueNew) {
	    this.valueNew = valueNew;
	}

}
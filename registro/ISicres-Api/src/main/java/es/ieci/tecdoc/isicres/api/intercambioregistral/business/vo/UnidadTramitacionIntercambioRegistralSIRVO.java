/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo;

/**
 * Clase  que representa la unidad de tramitación correspondiente con el directorio comun
 * Incialmente lo mapearemos en nuestro modelo de datos en la tabla scr_tramunit
 * @author Iecisa
 *
 */
public class UnidadTramitacionIntercambioRegistralSIRVO extends
		BaseIntercambioRegistralVO {

	/**
	 * idetificador unico
	 */
	protected String id;


	/**
	 * codigo correspondiente a la entidad registral en el del directorio comun
	 */
	protected String codeEntity;

	/**
	 * nombre correspondiente a la entidad registral en el del directorio comun
	 */
	protected String nameEntity;

	/**
	 * codigo correspondiente a la unidad de tramitación en el directorio común
	 */
	protected String codeTramunit;
	/**
	 * nombre correspondiente a la unidad de tramitación en el directorio común
	 */
	protected String nameTramunit;


	/**
	 * identificador de mapeo correpondiente con el id de la tabla scr_orgs
	 */
	protected String idOrgs;
	/**
	 * identificador de mapeo correpondiente con el nombre del padre de la tabla scr_orgs
	 */
	protected String nameOrgsFather;

	/**
	 * identificador de mapeo correpondiente con el codigo del padre de la tabla scr_orgs
	 */
	protected String codeOrgsFather;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodeEntity() {
		return codeEntity;
	}

	public void setCodeEntity(String code) {
		this.codeEntity = code;
	}

	public String getNameEntity() {
		return nameEntity;
	}

	public void setNameEntity(String name) {
		this.nameEntity = name;
	}

	public String getIdOrgs() {
		return idOrgs;
	}

	public void setIdOrgs(String idOrgs) {
		this.idOrgs = idOrgs;
	}

	public String getCodeTramunit() {
		return codeTramunit;
	}

	public void setCodeTramunit(String codeTramunit) {
		this.codeTramunit = codeTramunit;
	}

	public String getNameTramunit() {
		return nameTramunit;
	}

	public void setNameTramunit(String nameTramunit) {
		this.nameTramunit = nameTramunit;
	}
	public String getNameOrgsFather() {
	    return nameOrgsFather;
	}

	public void setNameOrgsFather(
	    String nameOrgsFather) {
	    this.nameOrgsFather = nameOrgsFather;
	}

	public String getCodeOrgsFather() {
	    return codeOrgsFather;
	}

	public void setCodeOrgsFather(
	    String codeOrgsFather) {
	    this.codeOrgsFather = codeOrgsFather;
	}

}

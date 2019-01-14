/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.vo;

import org.apache.commons.lang.StringUtils;

public class UnidadRegistralVO {

	protected int id;
	protected String code_tramunit;
	protected String name_tramunit;
	protected int id_orgs;
	protected String code_entity;
	protected String name_entity;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getCode_tramunit() {
		return code_tramunit;
	}
	public void setCode_tramunit(String code_tramunit) {
		this.code_tramunit = code_tramunit;
	}
	public String getName_tramunit() {
		return name_tramunit;
	}
	public void setName_tramunit(String name_tramunit) {
		this.name_tramunit = name_tramunit;
	}
	public int getId_orgs() {
		return id_orgs;
	}
	public void setId_orgs(int id_orgs) {
		this.id_orgs = id_orgs;
	}
	public String getCode_entity() {
		return code_entity;
	}
	public void setCode_entity(String code_entity) {
		this.code_entity = code_entity;
	}
	public String getName_entity() {
		return name_entity;
	}
	public void setName_entity(String name_entity) {
		this.name_entity = name_entity;
	}
	/**
	 * Metodo que valida si los datos basicos de la unidad registral vienen rellenos
	 *
	 * @return TRUE - si la unidad registral no tiene los datos rellenos
	 *         FALSE - si la unidad registral tiene alguno de los datos completo
	 */
	public boolean isUnidadRegistralVacia(){
		boolean result = false;

		if (StringUtils.isBlank(this.code_entity)
				&& StringUtils.isBlank(this.name_entity)
				&& StringUtils.isBlank(this.code_tramunit)
				&& StringUtils.isBlank(this.name_tramunit)) {
			result = true;
		}

		return result;
	}

}

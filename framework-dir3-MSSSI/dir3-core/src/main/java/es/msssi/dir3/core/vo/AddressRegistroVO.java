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

public class AddressRegistroVO extends Entity {

    	protected Integer idOrganismo;
	protected String id_orgs;
	protected String address;
	protected String city;
	protected String zip;
	protected String country;
	protected String telephone;
	protected String fax;
	protected String email;

	
	public String getId_orgs() {
	    return id_orgs;
	}


	public void setId_orgs(String id_orgs) {
	    this.id_orgs = id_orgs;
	}


	public String getAddress() {
	    return address;
	}


	public void setAddress(String address) {
	    this.address = address;
	}


	public String getCity() {
	    return city;
	}


	public void setCity(String city) {
	    this.city = city;
	}


	public String getZip() {
	    return zip;
	}


	public void setZip(String zip) {
	    this.zip = zip;
	}


	public String getCountry() {
	    return country;
	}


	public void setCountry(String country) {
	    this.country = country;
	}


	public String getTelephone() {
	    return telephone;
	}


	public void setTelephone(String telephone) {
	    this.telephone = telephone;
	}


	public String getFax() {
	    return fax;
	}


	public void setFax(String fax) {
	    this.fax = fax;
	}


	public String getEmail() {
	    return email;
	}


	public void setEmail(String email) {
	    this.email = email;
	}


	/**
	 * Metodo que valida si los datos basicos de la unidad registral vienen rellenos
	 *
	 * @return TRUE - si la unidad registral no tiene los datos rellenos
	 *         FALSE - si la unidad registral tiene alguno de los datos completo
	 */
	public boolean isUnidadRegistralVacia(){
		boolean result = false;

		if (StringUtils.isBlank(this.id_orgs)
				&& StringUtils.isBlank(this.address)
				&& StringUtils.isBlank(this.telephone)
				&& StringUtils.isBlank(this.city)
				&& StringUtils.isBlank(this.zip)
				&& StringUtils.isBlank(this.country)
				&& StringUtils.isBlank(this.fax)
				&& StringUtils.isBlank(this.email)) {
			result = true;
		}

		return result;
	}


	public Integer getIdOrganismo() {
	    return idOrganismo;
	}


	public void setIdOrganismo(Integer idOrganismo) {
	    this.idOrganismo = idOrganismo;
	}

}

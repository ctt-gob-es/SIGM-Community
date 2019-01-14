/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.daos;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.beans.ibatis.DirOrgs;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que realiza las operaciones de las unidades a base de datos.
 * 
 * @author cmorenog
 */
public class UnitsDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(UnitsDAO.class.getName());

   
    /**
     * Devuelve la dirección completa de un organismo.
     * 
     * @param idOrg
     *            id del organismo.
     * @return la dirección del organismo.
     */
    @SuppressWarnings("unchecked")
    public DirOrgs getAddressOrg(Integer idOrg) {
	LOG.trace("Entrando en ReportDAO.getAddressOrg()");
	String address = null;
	String addressOrg = "";
	String city = null;
	String zip = null;
	String country = null;
	String telephone = null;
	String fax = null;
	String mail = null;
	DirOrgs result = null;
	List<DirOrgs> dirOrgsList = new ArrayList<DirOrgs>();
	dirOrgsList =
		(List<DirOrgs>) getSqlMapClientTemplate().queryForList("DirOrgs.selectDirOrgs",
			idOrg);
	if (dirOrgsList != null && dirOrgsList.size() > 0) {
	    for (DirOrgs dirOrgs : dirOrgsList) {
		address = dirOrgs.getAddress();
		city = dirOrgs.getCity();
		zip = dirOrgs.getZip();
		country = dirOrgs.getCountry();
		telephone = dirOrgs.getTelephone();
		fax = dirOrgs.getFax();
		mail = dirOrgs.getEmail();
		result = dirOrgs;
	    }
	    /*if (address != null) {
		addressOrg += address;
	    }
	    if (city != null) {
		addressOrg += ", " + city;
	    }
	    if (zip != null) {
		addressOrg += " (" + zip + ")";
	    }
	    if (country != null) {
		addressOrg += " " + country;
	    }
	    if (telephone != null) {
		addressOrg += " Tef:" + telephone;
	    }
	    if (fax != null) {
		addressOrg += " Fax:" + fax;
	    }
	    if (mail != null) {
		addressOrg += " Email:" + mail;
	    }*/
	}
	LOG.trace("Saliendo en ReportDAO.getAddressOrg()");
	return result;
    }

}
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;

import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que realiza las operaciones de departamentos a base de datos.
 * 
 * @author cmorenog
 */
public class IuserdepthdrDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(IuserdepthdrDAO.class.getName());

   
    /**
     * Devuelve lista departartamentos.
     * 
    * @param idDepart
     *            id depart.
     * @param idsorg
     *            ids de organismos.
     * @return la dirección del organismo.
     */
    @SuppressWarnings("unchecked")
    public List<Iuserdepthdr> getDeparts(Integer idDepart, List<Integer> idsorg) {
	LOG.trace("Entrando en IuserdepthdrDAO.getDeparts()");

	List<Iuserdepthdr> result = null;
	Map <String, Object> param = new HashMap<String, Object>();
	if (idDepart != null){
	    param.put("deptId", idDepart);
	}
	if (idsorg != null && idsorg.size()>0){
	    param.put("idsorg", idsorg);
	}
	result =
		(List<Iuserdepthdr>) getSqlMapClientTemplate().queryForList("IUserDept.selectDeparts",
			param);

	LOG.trace("Saliendo en IuserdepthdrDAO.getDeparts()");
	return result;
    }

    /**
     * Devuelve un departamento por el id.
     * 
    * @param idDepart
     *            id depart.
     * @return la dirección del organismo.
     */
    public Iuserdepthdr getDepart(Integer idDepart) {
	LOG.trace("Entrando en IuserdepthdrDAO.getDepart()");

	Iuserdepthdr result = null;
	Map <String, Object> param = new HashMap<String, Object>();
	if (idDepart != null){
	    param.put("deptId", idDepart);
	}
	
	result =
		(Iuserdepthdr) getSqlMapClientTemplate().queryForObject("IUserDept.getDepart",
			param);

	LOG.trace("Saliendo en IuserdepthdrDAO.getDepart()");
	return result;
    }
}
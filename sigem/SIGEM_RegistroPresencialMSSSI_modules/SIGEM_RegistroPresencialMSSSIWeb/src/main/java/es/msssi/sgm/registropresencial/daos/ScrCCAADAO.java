/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
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

import es.msssi.sgm.registropresencial.beans.ibatis.ScrCCAA;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que contiene los métodos para recuperar las ccaa.
 * 
 * @author cmorenog
 * 
 */
public class ScrCCAADAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(ScrCCAADAO.class.getName());

    /**
     * Obtiene la lista de comunidades autónomas.
     * 
     * @return list la lista de ccaa.
     */
    @SuppressWarnings("unchecked")
    public List<ScrCCAA> getListCCAA() {
	LOG.trace("Entrando en ScrCCAADAO.getListCCAA");

	List<ScrCCAA> result = new ArrayList<ScrCCAA>();
	result =
		(List<ScrCCAA>) getSqlMapClientTemplate().queryForList("scrCCAA.listCCAA");
	
	LOG.trace("Saliendo de ScrCCAADAO.getListCCAA");
	return result;
    }

}
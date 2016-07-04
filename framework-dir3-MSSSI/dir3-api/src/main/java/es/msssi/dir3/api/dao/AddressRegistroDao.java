/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.dao;

import java.sql.SQLException;

import es.msssi.dir3.api.type.CriterionEnum;
import es.msssi.dir3.core.vo.AddressRegistroVO;

/**
 * Interfaz de los DAOs de datos básicos de direcciones.
 * 
 * @author cmorenog
 * 
 */
public interface AddressRegistroDao extends BaseDao<AddressRegistroVO, String, CriterionEnum> {

    /**
     * Método borrar una dirección. 
     * 
     * @param orgId
     *         dirección a insertar.
     * @throws SQLException .
     */
    public void delete(
	String orgId)
	throws SQLException;


}

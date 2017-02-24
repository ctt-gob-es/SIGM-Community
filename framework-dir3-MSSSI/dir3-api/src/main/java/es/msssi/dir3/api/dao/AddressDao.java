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
import es.msssi.dir3.api.vo.AddressVO;

/**
 * Interfaz de los DAOs de datos básicos de direcciones.
 * 
 * @author cmorenog
 * 
 */
public interface AddressDao extends BaseDao<AddressVO, Integer, CriterionEnum> {

    /**
     * Método para guardar una dirección. 
     * 
     * @param address
     *         dirección a insertar.
     * @return id del elemento insertado.
     * @throws SQLException .
     */
    public Integer saveReturn(
	AddressVO address)
	throws SQLException;

    /**
     * Método borrar una dirección. 
     * 
     * @param addressId
     *         dirección a insertar.
     * @throws SQLException .
     */
    public void delete(
	Integer addressId)
	throws SQLException;

    /**
     * Método  que devuelve el id de la dirección de la oficina.
     * 
     * @param id
     *            id de la oficina.
     * @return id de la dirección.
     * @throws SQLException .
     */
    public Integer getDirectionOfi(
	String id)
	throws SQLException;

    /**
     * Método  que devuelve el id de la dirección del organismo.
     * 
     * @param id
     *            id del organismo.
     * @return id de la dirección.
     * @throws SQLException
     */
    Integer getDirectionOrg(
	String id)
	throws SQLException;
}

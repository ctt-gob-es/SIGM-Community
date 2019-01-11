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

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que realiza las operaciones de configuracion de usuario a base de
 * datos.
 * 
 * @author cmorenog
 */
public class ContadorSIRDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(ContadorSIRDAO.class.getName());

    /**
     * Devuelve el contador.
     * @param codigoEntidad 
     * 
     * @param idUser
     *            id user.
     * @return la configuracion del usuario.
     */
    public int getContador(String codigoEntidad) {
	LOG.trace("Entrando en ContadorSIRDAO.getContador()");

	int result = 0;

	result = (Integer) getSqlMapClientTemplate().queryForObject("contadorSIR.contador", codigoEntidad);

	LOG.trace("Saliendo en ContadorSIRDAO.getContador()");
	return result;
    }

}
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

import java.util.List;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.beans.DistributionMail;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que realiza las operaciones de configuracion de usuario a base de
 * datos.
 * 
 * @author cmorenog
 */
public class DistributionMailDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(DistributionMailDAO.class.getName());

    /**
     * Devuelve la lista de mails a los que se enviara la distribucion con tipo
     * destino departamento.
     * 
     * @param idDept
     *            id departamento.
     * @return los mails a los que se enviara la distribucion.
     */
    @SuppressWarnings("unchecked")
    public List<DistributionMail> getDepartDistributionMail(Integer idDept) {
	LOG.trace("Entrando en DistributionMailDAO.getDistributionMail()");

	List<DistributionMail> result = null;

	result =
		(List<DistributionMail>) getSqlMapClientTemplate().queryForList(
			"distributionMail.getMailsDepartament", idDept);

	LOG.trace("Saliendo en DistributionMailDAO.getDistributionMail()");
	return result;
    }
    
    /**
     * Devuelve la lista de mails a los que se enviara la distribucion rechazada con tipo
     * destino departamento.
     * 
     * @param idDept
     *            id departamento.
     * @return los mails a los que se enviara la distribucion.
     */
    @SuppressWarnings("unchecked")
    public List<DistributionMail> getDepartDistributionRejectedMail(Integer idDept) {
	LOG.trace("Entrando en DistributionMailDAO.getDistributionRejectedMail()");

	List<DistributionMail> result = null;

	result =
		(List<DistributionMail>) getSqlMapClientTemplate().queryForList(
			"distributionMail.getMailsRejectedDepartament", idDept);

	LOG.trace("Saliendo en DistributionMailDAO.getDistributionRejectedMail()");
	return result;
    }
    
    
    /**
     * Devuelve la lista de mails a los que se enviara el intercambio registral con tipo
     * destino departamento.
     * 
     * @param idDept
     *            id departamento.
     * @return los mails a los que se enviara el intercambio registral.
     */
    @SuppressWarnings("unchecked")
    public List<DistributionMail> getMailsIntercambioRegDepartament(Integer idDept) {
	LOG.trace("Entrando en DistributionMailDAO.getMailsIntercambioRegDepartament()");

	List<DistributionMail> result = null;

	result =
		(List<DistributionMail>) getSqlMapClientTemplate().queryForList(
			"distributionMail.getMailsIntercambioRegDepartament", idDept);

	LOG.trace("Saliendo en DistributionMailDAO.getMailsIntercambioRegDepartament()");
	return result;
    }
    
    /**
     * Devuelve la lista de mails a los que se enviara el intercambio registral con tipo
     * destino departamento.
     * 
     * @param codeTramUnit
     *            codigo de unidad tramitadora.
     * @return los mails a los que se enviara el intercambio registral.
     */
    @SuppressWarnings("unchecked")
    public List<DistributionMail> getMailsIntercambioRegByOrg(String codeTramUnit) {
	LOG.trace("Entrando en DistributionMailDAO.getMailsIntercambioRegByOrg()");

	List<DistributionMail> result = null;

	result =
		(List<DistributionMail>) getSqlMapClientTemplate().queryForList(
			"distributionMail.getMailsIntercambioRegByOrg", codeTramUnit);

	LOG.trace("Saliendo en DistributionMailDAO.getMailsIntercambioRegDepartament()");
	return result;
    }
    
    
    

}
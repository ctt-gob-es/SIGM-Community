/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.ContactUODao;
import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.api.vo.ContactUOVO;

/**
 * DAO de datos básicos de contactos de unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public class ContactUODaoImpl extends SqlMapClientBaseDao implements ContactUODao {

    /**
     * Nombre de la query
     * */
    protected static final String GETCONTACT = "ContactUOVO.getContactUOVO";
    protected static final String LISTCONTACTS = "ContactUOVO.getActiveContactUOVOs";
    protected static final String INSERTCONTACT = "ContactUOVO.addContactUOVO";
    protected static final String UPDATECONTACT = "ContactUOVO.updateContactUOVO";
    protected static final String DELETECONTACT = "ContactUOVO.deleteContactUOVO";

    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(ContactUODaoImpl.class);

    /**
     * Constructor.
     * 
     */
    public ContactUODaoImpl() {
	super();
    }

    /**
     * Método para recuperar un contacto basándonos en su
     * identificador.
     * 
     * @param id
     *            identificador del objeto a recuperar
     * @return el objeto recuperado.
     * @throws SQLException.
     */
    public ContactUOVO get(
	Integer id)
	throws SQLException {
	logger.info("Obteniendo una contacto con el id: " +
	    id);
	ContactUOVO contact;
	contact = (ContactUOVO) getSqlMapClientTemplate().queryForObject(
	    GETCONTACT, id);

	return contact;
    }

    /**
     * Comprueba la existencia de un contacto con el identificador dado.
     * 
     * @param id
     *            identificador del objeto
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario
     * @throws SQLException.
     */
    public boolean exists(
	Integer id)
	throws SQLException {
	logger.info("Obteniendo si existe un contacto con el id: " +
	    id);
	boolean exist = false;
	ContactUOVO contact;
	contact = (ContactUOVO) getSqlMapClientTemplate().queryForObject(
	    GETCONTACT, id);
	if (contact != null) {
	    exist = true;
	}
	return exist;
    }

    /**
     * Método para guardar un contacto. Maneja tanto la inserción como
     * la actualización.
     * 
     * @param entity
     *            contacto a guardar.
     * @throws SQLException.
     */
    public void save(
	ContactUOVO entity)
	throws SQLException {
	logger.info("Guardar un contacto");
	getSqlMapClient().insert(
	    INSERTCONTACT, entity);
    }

    /**
     * Método genérico para actualizar un contacto de la clase dada.
     * 
     * @param entity
     *            objeto a actualizar.
     * @throws SQLException.
     */
    public void update(
	ContactUOVO entity)
	throws SQLException {
	logger.info("Modifico un contacto");
	getSqlMapClient().update(
	    UPDATECONTACT, entity);
    }

    /**
     * Se eliminan los contactos de la unidad que pasa como parámetro.
     * @param id
     * 	código de la unidad.
     * @throws SQLException .
     */
    public void deleteContactsOrg(
	String id)
	throws SQLException {
	logger.info("Borrar los contactos del organismo:" +
	    id);
	getSqlMapClient().update(
	    DELETECONTACT, id);
    }

    /**
     * Se devuelven los contactos de la oficina que pasa como parámetro.
     * @param codigo
     * 	código de la oficina.
     * @throws SQLException .
     */
    @SuppressWarnings("unchecked")
    public List<ContactUOVO> getContacts(
	String codigo)
	throws SQLException {
	logger.info("Obteniendo los contactos de la unidad: " +
	    codigo);
	List<ContactUOVO> list = null;
	list = (List<ContactUOVO>) getSqlMapClientTemplate().queryForList(
	    LISTCONTACTS, codigo);

	return list;
    }

}

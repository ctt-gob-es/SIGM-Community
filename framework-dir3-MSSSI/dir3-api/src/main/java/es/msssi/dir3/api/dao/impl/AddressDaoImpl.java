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

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.AddressDao;
import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.api.vo.AddressVO;

/**
 * DAO de datos básicos de direcciones.
 * 
 * @author cmorenog
 * 
 */
public class AddressDaoImpl extends SqlMapClientBaseDao implements AddressDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Nombre de la query
	 * */
	protected static final String GETADDRESS = "AddressVO.getAddressVO";
	protected static final String INSERTADDRESS = "AddressVO.addAddressVO";
	protected static final String UPDATEADDRESS = "AddressVO.updateAddressVO";
	protected static final String DELETEADDRESS = "AddressVO.deleteAddressVO";
	protected static final String EXISTSADDRESS = "AddressVO.existsAddress";
	protected static final String EXISTSOFFICEADDRESS = "AddressVO.getAddressOffice";
	protected static final String EXISTSUNITADDRESS = "AddressVO.getAddressUnit";
	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(AddressDaoImpl.class);

	/**
	 * Constructor.
	 * 
	 */
	public AddressDaoImpl() {
		super();
	}

	/**
	 * Método genérico para recuperar una direccion basándonos en su
	 * identificador.
	 * 
	 * @param id
	 *            identificador del objeto a recuperar.
	 * @return el objeto recuperado.
	 * @throws SQLException .
	 */
	public AddressVO get(Integer id) throws SQLException {
		logger.info("Obteniendo una dirección con el id: " + id);
		AddressVO address;
		address = (AddressVO) getSqlMapClientTemplate().queryForObject(GETADDRESS, id.toString());

		return address;
	}

	/**
	 * Comprueba la existencia de una dirección con el identificador dado.
	 * 
	 * @param id
	 *            identificador del objeto.
	 * @return <code>true</code> si el objeto existe, <code>false</code> en caso
	 *         contrario.
	 * @throws SQLException .
	 */
	public boolean exists(Integer id) throws SQLException {
		logger.info("Obteniendo si existe una direccion con el id: " + id);
		boolean exist = false;
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject(EXISTSADDRESS, id.toString());
		if (!Integer.valueOf(0).equals(count)) {
			exist = true;
		}
		return exist;
	}

	/**
	 * Método para guardar una direccion.
	 * 
	 * @param address
	 *            dirección a guardar.
	 * @throws SQLException.
	 */
	public void save(AddressVO address) throws SQLException {
		logger.info("Guardar una dirección");
		getSqlMapClient().insert(INSERTADDRESS, address);
	}

	/**
	 * Método para actualizar una dirección.
	 * 
	 * @param address
	 *            dirección a actualizar.
	 * @throws SQLException .
	 */
	public void update(AddressVO address) throws SQLException {
		logger.info("Modifico una direccion: " + address.getId());
		getSqlMapClient().update(UPDATEADDRESS, address);
	}

	/**
	 * Método para guardar una direccion.
	 * 
	 * @param address
	 *            dirección a guardar.
	 * @return id id de la dirección introducida.
	 * @throws SQLException.
	 */
	public Integer saveReturn(AddressVO address) throws SQLException {
		logger.info("Guardar una dirección");
		String id = null;
		id = (String) getSqlMapClient().insert(INSERTADDRESS, address);
		logger.info("Dirección guardada: " + id);
		return Integer.parseInt(id);
	}

	/**
	 * Método borrar una dirección.
	 * 
	 * @param addressId
	 *            dirección a insertar.
	 * @throws SQLException .
	 */
	public void delete(Integer orgId) throws SQLException {
		logger.info("Borro una direccion: " + orgId);
		getSqlMapClient().delete(DELETEADDRESS, "" + orgId);
	}

	/**
	 * Método que devuelve el id de la dirección de la oficina.
	 * 
	 * @param id
	 *            id de la oficina.
	 * @return id de la dirección.
	 * @throws SQLException .
	 */
	public Integer getDirectionOfi(String codigoOrg) throws SQLException {
		logger.info("Compruebo si la oficina tiene dirección");

		Integer addressId = (Integer) getSqlMapClientTemplate().queryForObject(EXISTSOFFICEADDRESS, codigoOrg);
		return addressId;
	}

	/**
	 * Método que devuelve el id de la dirección del organismo.
	 * 
	 * @param id
	 *            id del organismo.
	 * @return id de la dirección.
	 * @throws SQLException
	 */
	public Integer getDirectionOrg(String codigoOrg) throws SQLException {
		logger.info("Compruebo si la unidad orgánica tiene dirección");

		Integer addressId = (Integer) getSqlMapClientTemplate().queryForObject(EXISTSUNITADDRESS, codigoOrg);

		return addressId;
	}
}

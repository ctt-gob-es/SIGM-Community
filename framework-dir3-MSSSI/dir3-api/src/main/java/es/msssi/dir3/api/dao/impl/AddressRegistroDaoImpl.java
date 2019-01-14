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

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.AddressRegistroDao;
import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.core.vo.AddressRegistroVO;

/**
 * DAO de datos básicos de direcciones.
 * 
 * @author cmorenog
 * 
 */
public class AddressRegistroDaoImpl extends SqlMapClientBaseDao implements AddressRegistroDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Nombre de la query
     * */
    protected static final String GETADDRESS = "AddressRegistroVO.getAddressRegistroVO";
    protected static final String INSERTADDRESS = "AddressRegistroVO.addAddressRegistroVO";
    protected static final String UPDATEADDRESS = "AddressRegistroVO.updateAddressRegistroVO";
    protected static final String DELETEADDRESS = "AddressRegistroVO.deleteAddressRegistroVO";
    protected static final String EXISTSADDRESS = "AddressRegistroVO.existsAddress";
    protected static final String IDORGADDRESS = "AddressRegistroVO.IdOrgDirec";
    /**
     * Logger de la clase.
     */
    private static final Logger LOGGER = Logger.getLogger(AddressRegistroDaoImpl.class);

    /**
     * Constructor.
     * 
     */
    public AddressRegistroDaoImpl() {
    	super();
    }

    /**
     * Método genérico para recuperar una direccion basándonos en su
     * identificador.
     * 
     * @param idorg
     *            identificador del objeto a recuperar.
     * @return el objeto recuperado.
     * @throws SQLException .
     */
    public AddressRegistroVO get(String idorg) throws SQLException {
    	LOGGER.info("Obteniendo una dirección con el id: " + idorg);
    	AddressRegistroVO address;
    	address = (AddressRegistroVO) getSqlMapClientTemplate().queryForObject( GETADDRESS, idorg);

    	return address;
    }

    /**
     * Comprueba la existencia de una dirección con el identificador dado.
     * 
     * @param idorg
     *            identificador del objeto.
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario.
     * @throws SQLException .
     */
    public boolean exists(String idorg) throws SQLException {
    	LOGGER.info("Obteniendo si existe una direccion con el id: " + idorg);
    	boolean exist = false;
    	if(StringUtils.isNumeric(idorg)){
    		Integer count = (Integer) getSqlMapClientTemplate().queryForObject( EXISTSADDRESS, Integer.parseInt(idorg));

    		if (!Integer.valueOf(0).equals( count)) {
    			exist = true;
    		}
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
    public void save(AddressRegistroVO address) throws SQLException {
    	LOGGER.info("Guardar una dirección");
    	address.setIdOrganismo((Integer)getSqlMapClient().queryForObject(IDORGADDRESS, address.getId_orgs()));
    	getSqlMapClient().insert( INSERTADDRESS, address);
    }

    /**
     * Método para actualizar una dirección.
     * 
     * @param address
     *            dirección a actualizar.
     * @throws SQLException .
     */
    public void update( AddressRegistroVO address) throws SQLException {
    	LOGGER.info("Modifico una direccion: "+address.getId());
    	getSqlMapClient().update( UPDATEADDRESS, address);
    }


    /**
     * Método borrar una dirección. 
     * 
     * @param orgId
     *         dirección a insertar.
     * @throws SQLException .
     */
    public void delete( String orgId) throws SQLException {
    	LOGGER.info("Borro una direccion: "+orgId);
    	getSqlMapClient().delete( DELETEADDRESS, orgId);
    }
    
    /**
     * Método para obtener el id de una dirección a partir de su code de SCR_ORGS. 
     * 
     * @param address
     *            dirección a guardar.
     * @throws SQLException.
     */
    public Integer getIdOrgs(String codeDir3) throws SQLException {
    	LOGGER.info("Guardar una dirección");
    	Integer idOrgs = (Integer)getSqlMapClient().queryForObject(IDORGADDRESS, codeDir3);
    	
    	return idOrgs;
    }
}

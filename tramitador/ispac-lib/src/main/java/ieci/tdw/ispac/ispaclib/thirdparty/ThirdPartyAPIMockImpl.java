package ieci.tdw.ispac.ispaclib.thirdparty;

import ieci.tdw.ispac.api.BasicThirdPartyAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbQuery;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

/** 
 * @author Iecisa 
 * @version $Revision$ 
 *
 */
public class ThirdPartyAPIMockImpl extends BasicThirdPartyAPI {

	/**INICIO [eCenpri-Felipe #477] Tipos de dirección telemática */
	private static final Logger logger = Logger.getLogger(ThirdPartyAPIMockImpl.class);

	protected String dsName = DEFAULT_DATASOURCE_NAME;

	private static final String DEFAULT_DATASOURCE_NAME = 
			"java:comp/env/jdbc/sicres";
	
	private static final int TYPE_DT_MAIL=2;
	private static final int TYPE_DT_TLFN_FIJO=1;
	private static final int TYPE_DT_TLFN_MOVIL=5;
	private static final String TABLAS_PFIS_PJUR="PERSONS";
	private static final String TABLA_ADDRESS="SCR_ADDRESS";
	
	/**Tipo de dirección electrónica*/
	private static final String TYPE_DIR_ELECTRONICA="1";
	
	private static final String TYPE_DIR_DOMICILIO="0";
	/**FIN [eCenpri-Felipe #477]*/
	
	
	/**
	 * Constructor.
	 * @exception ISPACException si ocurre algún error.
	 */
	public ThirdPartyAPIMockImpl() throws ISPACException {
		super();
	}

	/**
	 * Constructor.
	 * @param dsName Nombre del origen de datos.
	 * @exception ISPACException si ocurre algún error.
	 */
	public ThirdPartyAPIMockImpl(String dsName) throws ISPACException {
		super();
	}

	/**
	 * Obtiene una lista de terceros en función del códigos de identificación. 
	 * @param code Código de identificación del tercero
	 * @param onlyDefaultValues Indica si se cargan solamente las direcciones asociadas por defecto
	 * @return lista de terceros asociados con un código de identificación, 
	 * cargando las direcciones (postal y telemática) por defecto si así se indica. 
	 * @throws ISPACException
	 */
	public IThirdPartyAdapter[] lookup(String code, boolean onlyDefaultValues) throws ISPACException {

		return new IThirdPartyAdapter[] { createThirdPartyAdapter(code, code,
				"Nombre " + code, "Primer Apellido " + code,
				"Segundo Apellido " + code, IThirdPartyAdapter.TIPO_PERSONA_FISICA) };
	}
	
	/**
	 * Obtiene una lista de terceros en función del nombre y dos apellidos.
	 * @param name nombre del tercero
	 * @param surnam1 primer apellido del tercero
	 * @param surname2 segundo apellido del tercero
	 * @param onlyDefaultValues Indica si se cargan solamente las direcciones asociadas por defecto
	 * @return Terceros que cumple la condición.
	 * @exception ISPACException si ocurre algún error.
	 */
	public IThirdPartyAdapter [] lookup(String name, String surname1, String surname2, 
			boolean onlyDefaultValues) throws ISPACException {

		return new IThirdPartyAdapter[] { createThirdPartyAdapter(null, null,
				name, surname1, surname2,
				IThirdPartyAdapter.TIPO_PERSONA_FISICA) };
	}

	/**
	 * Obtiene un tercero a partir de su identificador.
	 * @param id identificador del tercero
	 * @param onlyDefaultValues Indica si se cargan las direcciones asociadas por defecto
	 * @return Información del tercero con sus direcciones por defecto si así se indica 
	 * @throws ISPACException
	 */
	public IThirdPartyAdapter lookupById(String id, boolean onlyDefaultValues) throws ISPACException {
		return createThirdPartyAdapter(id, id, "Nombre " + id,
				"Primer Apellido " + id, "Segundo Apellido " + id,
				IThirdPartyAdapter.TIPO_PERSONA_FISICA);
	}
	
	/**
	 * Obtiene un tercero a partir de su identificador interno y los identificadores de sus direcciones.
	 * @param id Identificador interno del tercero.
	 * @param postalAddressId Identificador de la dirección postal.
	 * @param electronicAddressId Identificador de la dirección electrónica.
	 * @return Información del tercero.
	 * @exception ISPACException si ocurre algún error.
	 */
	public IThirdPartyAdapter lookupById(String id, String postalAddressId, String electronicAddressId) 
			throws ISPACException {
		return createThirdPartyAdapter(id, id, "Nombre " + id,
				"Primer Apellido " + id, "Segundo Apellido " + id,
				IThirdPartyAdapter.TIPO_PERSONA_FISICA);
	}

	
    /**
     * Obtiene una colección de todas las direcciones postales para un tercero
     * @param id identificador de tercero
     * @return lista de direcciones de postales relacionadas con un tercero
     * @throws Exception
     */
    public IPostalAddressAdapter [] lookupPostalAddresses(String id) throws ISPACException {
		return new IPostalAddressAdapter[0];
    }
    
    /**
     * Obtiene la dirección postal por defecto para un tercero
     * @param id identificador de tercero
     * @return dirección postal por defecto
     * @throws ISPACException
     */
    public IPostalAddressAdapter lookupDefaultPostalAddress(String id) throws ISPACException {
		return null;
    }
    
    /**
     * Obtiene una colección de todas las direcciones electrónicas para un tercero
     * @param id identificador de tercero
     * @return colección de direcciones electrónicas
     * @throws ISPACException
     */
    public IElectronicAddressAdapter [] lookupElectronicAddresses(String id) throws ISPACException {
		return new IElectronicAddressAdapter[0];
    }

    /**
     * Obtiene la dirección electrónica por defecto para un tercero
     * @param id identificador de tercero
     * @return dirección electrónica
     * @throws ISPACException
     */
    public IElectronicAddressAdapter lookupDefaultElectronicAddress(String id) throws ISPACException {
    	return null;
    }

	/**
	 * Obtiene una dirección postal según su identificador
	 * @param addressId identificador de dirección postal
	 * @return dirección postal
	 * @throws ISPACException
	 */
	public IPostalAddressAdapter getPostalAddress(String addressId) throws ISPACException {
    	return null;
	}

	/**
	 * Obtiene una dirección electrónica según su identificador
	 * @param addressId identificador de dirección electrónica
	 * @return dirección electrónica
	 * @throws ISPACException
	 */
	public IElectronicAddressAdapter getElectronicAddress(String addressId) throws ISPACException {
		return null;
	}
	
	private static ThirdPartyAdapter createThirdPartyAdapter(String id,
			String identificacion, String nombre, String ape1, String ape2,
			String tipo) throws ISPACException {

		ThirdPartyAdapter tercero = new ThirdPartyAdapter();

		tercero.setIdExt(id);
		tercero.setIdentificacion(identificacion);
		tercero.setNombre(nombre);
		tercero.setPrimerApellido(ape1);
		tercero.setSegundoApellido(ape2);
		tercero.setTipoPersona(tipo);

		return tercero;
	}

	/**
	 * [eCenpri-Felipe #477] Inserta un nuevo tercero en la BBDD
	 * 
	 * @param nif
	 * @param nombre
	 * @param ape1
	 * @param ape2
	 * @param tipo
	 * @param provincia
	 * @param municipio
	 * @param cpostal
	 * @param direccion
	 * @param tfnoFijo
	 * @param tfnoMovil
	 * @param email
	 * @return
	 * @throws ISPACException
	 */
	public boolean insertThirdParty(String nif, int tipoDoc, String nombre,
			String ape1, String ape2, String tipo, String provincia,
			String municipio, String cpostal, String direccion,
			String tfnoFijo, String tfnoMovil, String email)
			throws ISPACException {

		String strQuery = null;
		int idPerson = Integer.MIN_VALUE;
		// boolean continuar = true;

		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;

		try {

			cnt.getConnection();

			// [eCenpri-Felipe] Escapar caracteres SQL
			nif = StringUtils.escapeSql(nif);
			nombre = StringUtils.escapeSql(nombre);
			ape1 = StringUtils.escapeSql(ape1);
			ape2 = StringUtils.escapeSql(ape2);
			provincia = StringUtils.escapeSql(provincia);
			municipio = StringUtils.escapeSql(municipio);
			direccion = StringUtils.escapeSql(direccion);

			// Obtenemos el nuevo id de la persona
			idPerson = getContador(cnt, TABLAS_PFIS_PJUR) + 1;

			if (idPerson != Integer.MIN_VALUE) {
				if (tipo.toUpperCase().startsWith(
						IThirdPartyAdapter.TIPO_PERSONA_FISICA)) {
					strQuery = getInsertPfisSQLQuery(idPerson, tipoDoc, nif,
							nombre, ape1, ape2);
				} else {
					strQuery = getInsertPjurSQLQuery(idPerson, tipoDoc, nif,
							nombre);
				}
				cnt.execute(strQuery);
				updateContador(cnt, TABLAS_PFIS_PJUR, idPerson);

				// Obtenemos el id en scr_address
				int idAddress = getContador(cnt, TABLA_ADDRESS) + 1;
				strQuery = getInsertScrDomSQLQuery(idAddress, direccion,
						municipio, cpostal, "ESPAÑA", 1);
				cnt.execute(strQuery);

				strQuery = getInsertScrAddressSQLQuery(idAddress, idPerson,
						Integer.valueOf(TYPE_DIR_DOMICILIO).intValue());
				cnt.execute(strQuery);

				updateContador(cnt, TABLA_ADDRESS, idAddress);

				// Insertamos las direcciones telemáticas
				int preferencia = 1;

				// Email
				if (StringUtils.isNotEmpty(email)) {
					insertDireccionTelematica(cnt, idPerson, email,
							TYPE_DT_MAIL, preferencia);
					preferencia = 0;
				}
				// Tfono móvil
				if (StringUtils.isNotEmpty(tfnoMovil)) {
					insertDireccionTelematica(cnt, idPerson, tfnoMovil,
							TYPE_DT_TLFN_MOVIL, preferencia);
					preferencia = 0;
				}
				// Tfono fijo
				if (StringUtils.isNotEmpty(tfnoFijo)) {
					insertDireccionTelematica(cnt, idPerson, tfnoFijo,
							TYPE_DT_TLFN_FIJO, preferencia);
				}
			}

		} catch (ISPACException e) {
			logger.error(
					"Error en la búsqueda de la dirección electrónica por defecto",
					e);
			throw e;
		} catch (Exception e) {
			logger.error(
					"Error en la búsqueda de la dirección electrónica por defecto",
					e);
			throw new ISPACException(
					"Error en la búsqueda de la dirección electrónica por defecto",
					e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}

		return true;
	}

	/**
	 * [eCenpri-Felipe #477] Devuelve el contador de la tabla SCR_CONTADOR
	 * 
	 * @param idTabla
	 * @return
	 * @throws ISPACException
	 */
	private static int getContador(DbCnt cnt, String idTabla)
			throws ISPACException {

		StringBuffer sbQuery = new StringBuffer()
				.append("SELECT CONTADOR FROM SCR_CONTADOR WHERE TABLAID = '")
				.append(idTabla).append("'");
		DbQuery dbQuery = cnt.executeDbQuery(sbQuery.toString());
		dbQuery.next();
		return dbQuery.getInt("CONTADOR");
	}

	/**
	 * [eCenpri-Felipe #477] Actualiza el contador de la tabla SCR_CONTADOR
	 * 
	 * @param contador
	 * @return
	 * @throws ISPACException
	 */
	private static boolean updateContador(DbCnt cnt, String idTabla,
			int contador) throws ISPACException {

		StringBuffer sbQuery = new StringBuffer()
				.append("UPDATE SCR_CONTADOR SET CONTADOR=").append(contador)
				.append(" WHERE TABLAID = '").append(idTabla).append("'");
		return cnt.execute(sbQuery.toString());
	}

	/**
	 * [eCenpri-Felipe #477] Obtiene la consulta SQL para crear un registro en
	 * SCR_PFIS
	 * 
	 * @param thirdPartyId
	 *            Identificador del tercero.
	 * @return Consulta SQL.
	 */
	private static String getInsertPfisSQLQuery(int idPerson, int tipoDoc,
			String nif, String nombre, String ape1, String ape2) {

		String sql = new StringBuffer()
				.append("INSERT INTO SCR_PFIS (ID, TYPE_DOC, NIF, FIRST_NAME, SECOND_NAME, SURNAME) VALUES (")
				.append(idPerson).append(",").append(tipoDoc).append(",'")
				.append(nif).append("','").append(ape1).append("','")
				.append(ape2).append("','").append(nombre).append("')")
				.toString();

		return sql;
	}

	/**
	 * [eCenpri-Felipe #477] Obtiene la consulta SQL para crear un registro en
	 * SCR_PJUR
	 * 
	 * @param thirdPartyId
	 *            Identificador del tercero.
	 * @return Consulta SQL.
	 */
	private static String getInsertPjurSQLQuery(int idPerson, int tipoDoc,
			String nif, String nombre) {

		String sql = new StringBuffer()
				.append("INSERT INTO SCR_PJUR(ID, TYPE_DOC, CIF, NAME) VALUES (")
				.append(idPerson).append(",").append(1).append(",'")
				.append(nif).append("','").append(nombre).append("')")
				.toString();

		return sql;
	}

	/**
	 * [eCenpri-Felipe #477] Obtiene la consulta SQL para crear un registro en
	 * SCR_PFIS
	 * 
	 * @param thirdPartyId
	 *            Identificador del tercero.
	 * @return Consulta SQL.
	 */
	private static String getInsertScrDomSQLQuery(int idAddress,
			String direccion, String ciudad, String cpostal, String pais,
			int preferencia) {

		String sql = new StringBuffer()
				.append("INSERT INTO SCR_DOM (ID, ADDRESS, CITY, ZIP, COUNTRY, PREFERENCE) VALUES (")
				.append(idAddress).append(",'").append(direccion).append("','")
				.append(ciudad).append("','").append(cpostal).append("','")
				.append(pais).append("',").append(preferencia).append(")")
				.toString();

		return sql;
	}

	/**
	 * [eCenpri-Felipe #477] Obtiene la consulta SQL para crear un registro en
	 * SCR_ADDRESS
	 * 
	 * @param thirdPartyId
	 *            Identificador del tercero.
	 * @return Consulta SQL.
	 */
	private static String getInsertScrAddressSQLQuery(int idAddress,
			int idPerson, int tipoDireccion) {

		String sql = new StringBuffer()
				.append("INSERT INTO SCR_ADDRESS (ID, ID_PERSON, TYPE) VALUES (")
				.append(idAddress).append(",").append(idPerson).append(",")
				.append(tipoDireccion).append(")").toString();

		return sql;
	}

	/**
	 * [eCenpri-Felipe #477] Inserta una dirección en la BBDD
	 * 
	 * @param thirdPartyId
	 *            Identificador del tercero.
	 * @return Consulta SQL.
	 * @throws ISPACException
	 */
	private static boolean insertDireccionTelematica(DbCnt cnt, int idPerson,
			String address, int tipo, int preferencia) throws ISPACException {

		int idAddress = getContador(cnt, TABLA_ADDRESS) + 1;
		String strQuery = getInsertScrAddrtelSQLQuery(address, tipo,
				preferencia, idAddress);
		cnt.execute(strQuery);

		strQuery = getInsertScrAddressSQLQuery(idAddress, idPerson, Integer
				.valueOf(TYPE_DIR_ELECTRONICA).intValue());
		cnt.execute(strQuery);

		updateContador(cnt, TABLA_ADDRESS, idAddress);

		return true;
	}
	
	/**
	 * [eCenpri-Felipe #477] Devuelve la sentencia SQL de la inserción en la
	 * dirección telemática
	 * 
	 * @param address
	 * @param tipo
	 * @param preferencia
	 * @param idAddress
	 * @return
	 */
	private static String getInsertScrAddrtelSQLQuery(String address, int tipo,
			int preferencia, int idAddress) {

		String strQuery = new StringBuffer()
				.append("INSERT INTO SCR_ADDRTEL(ID, ADDRESS, TYPE, PREFERENCE) VALUES (")
				.append(idAddress).append(",'").append(address).append("',")
				.append(tipo).append(",").append(preferencia).append(")")
				.toString();
		return strQuery;
	}
	
	/**
	 * [dipucr-Felipe #583]
	 * @param idPerson
	 * @param email
	 * @return
	 * @throws ISPACException 
	 */
	public boolean insertDefaultEmail(int idPerson, String email) throws ISPACException{
		
		DbCnt cnt = new DbCnt(dsName);
		try{
			cnt.getConnection();
			return insertDireccionTelematica(cnt, idPerson, email, TYPE_DT_MAIL, 1);
		}
		catch(ISPACException ex){
			logger.error("Error al insertar mail por defecto " + email + " para el tercero " + idPerson);
			throw ex;
		}
		finally{
			if (null != cnt){
				cnt.closeConnection();
			}
		}
	}
	
	/**
	 * [eCenpri-Felipe #592] Actualiza los datos del terceros en la base de datos
	 * @param idPerson
	 * @param nombre
	 * @param ape1
	 * @param ape2
	 * @param provincia
	 * @param municipio
	 * @param cpostal
	 * @param direccion
	 * @return
	 * @throws ISPACException
	 */
	public boolean updateThirdParty(int idPerson, String nombre, String ape1, String ape2, 
			String provincia, String municipio, String cpostal, String direccion) throws ISPACException {

		String strQuery = null;

		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;

		try {

			cnt.getConnection();

			nombre = StringUtils.escapeSql(nombre);
			ape1 = StringUtils.escapeSql(ape1);
			ape2 = StringUtils.escapeSql(ape2);
			provincia = StringUtils.escapeSql(provincia);
			municipio = StringUtils.escapeSql(municipio);
			direccion = StringUtils.escapeSql(direccion);

			// Actualizamos nombre y apellidos
			strQuery = getUpdatePfisSQLQuery(idPerson, nombre, ape1, ape2);
			cnt.execute(strQuery);

			// Obtenemos el id en scr_address
			IPostalAddressAdapter postalAddress = lookupDefaultPostalAddress(String.valueOf(idPerson));
			String idAddress = postalAddress.getId();
			
			// Actualizamos la dirección
			strQuery = getUpdateScrAddressSQLQuery(idAddress, provincia, municipio, cpostal, direccion);
			cnt.execute(strQuery);



		} catch (Exception e) {
			String error = "Error en la actualización del tercero " + idPerson;
			logger.error(error, e);
			throw new ISPACException(error, e);
			
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}

		return true;
	}
	
	/**
	 * [dipucr-Felipe #592]
	 * @param idPerson
	 * @param nombre
	 * @param ape1
	 * @param ape2
	 * @return
	 */
	private static String getUpdatePfisSQLQuery(int idPerson, String nombre, String ape1, String ape2) {

		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append("UPDATE SCR_PFIS SET SURNAME = '");
		sbQuery.append(nombre);
		sbQuery.append("', FIRST_NAME='");
		sbQuery.append(ape1);
		sbQuery.append("'");
		if (!StringUtils.isEmpty(ape2)){
			sbQuery.append(", SECOND_NAME='");
			sbQuery.append(ape2);
			sbQuery.append("'");
		}
		sbQuery.append(" WHERE ID = " + idPerson);

		return sbQuery.toString();
	}
	
	/**
	 * [dipucr-Felipe #592]
	 * @param idAddress
	 * @param provincia
	 * @param municipio
	 * @param cpostal
	 * @param direccion
	 * @return
	 */
	private static String getUpdateScrAddressSQLQuery
			(String idAddress, String provincia, String municipio, String cpostal, String direccion){
		
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append("UPDATE SCR_DOM SET ADDRESS = '");
		sbQuery.append(direccion);
		sbQuery.append("', CITY = '");
		sbQuery.append(municipio);
		sbQuery.append("', ZIP = '");
		sbQuery.append(municipio);
		sbQuery.append("', COUNTRY = '");
		sbQuery.append(provincia);
		sbQuery.append("' WHERE ID = ");
		sbQuery.append(idAddress);
		
		return sbQuery.toString();
		
	}
}

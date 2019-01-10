package ieci.tdw.ispac.ispaclib.invesicres.thirdparty;

import ieci.tdw.ispac.api.BasicThirdPartyAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbQuery;
import ieci.tdw.ispac.ispaclib.thirdparty.ElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.PostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.ThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DIR3IneUtils;

/**
 * Implementación del API de acceso a terceros en SICRES.
 *
 */
public class SicresThirdPartyAPI extends BasicThirdPartyAPI {

	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(SicresThirdPartyAPI.class);
	
	/** Nombre del origen de datos por defecto. */
	private static final String DEFAULT_DATASOURCE_NAME = 
		"java:comp/env/jdbc/sicres";
	
	/** Nombre del origen de datos. */
	protected String dsName = DEFAULT_DATASOURCE_NAME;
	
	/**Código para el teléfono móvil*/
	private static final String TYPE_TLFN_MOVIL="TM";
	/**Código para el teléfono fijo*/
	private static final String TYPE_TLFN_FIJO="TF";
	/**Código para el deu*/
	private static final String TYPE_DEU="DU";
	/**Código para el mail*/
	private static final String TYPE_MAIL="CE";
	/**Tipo de dirección electrónica*/
	private static final String TYPE_DIR_ELECTRONICA="1";
	
	private static final String TYPE_DIR_DOMICILIO="0";
	
	
	/**INICIO [eCenpri-Felipe #477] Tipos de dirección telemática */
	private static final int TYPE_DT_MAIL=2;
	private static final int TYPE_DT_TLFN_FIJO=1;
	private static final int TYPE_DT_TLFN_MOVIL=5;
	private static final String TABLAS_PFIS_PJUR="PERSONS";
	private static final String TABLA_ADDRESS="SCR_ADDRESS";
	/**FIN [eCenpri-Felipe #477]*/
	
	
	
	/**
	 * Constructor.
	 * @exception ISPACException si ocurre algún error.
	 */
	public SicresThirdPartyAPI() throws ISPACException {
		super();
		
		// Nombre del datasource de SICRES
		this.dsName = ISPACConfiguration.getInstance().get("THIRDPARTY_API_DATASOURCE_NAME",DEFAULT_DATASOURCE_NAME);
	}

	/**
	 * Constructor.
	 * @param dsName Nombre del origen de datos.
	 * @exception ISPACException si ocurre algún error.
	 */
	public SicresThirdPartyAPI(String dsName) throws ISPACException {
		super();
		
		// Nombre del datasource de SICRES
		this.dsName = dsName;
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

		List terceros = new ArrayList();
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getThirdPartySQLQuery(null, code, null, null, null));

			while (dbQuery.next()) {
				
				ThirdPartyAdapter tercero = createThirdPartyAdapter(dbQuery);
				
				if (onlyDefaultValues) {
					tercero.setDefaultDireccionElectronica(lookupDefaultElectronicAddress(tercero.getIdExt()));
					tercero.setDefaultDireccionPostal(lookupDefaultPostalAddress(tercero.getIdExt()));
				} else {
					tercero.setDireccionesElectronicas(lookupElectronicAddresses(tercero.getIdExt()));
					tercero.setDireccionesPostales(lookupPostalAddresses(tercero.getIdExt()));
				}
				
				if (ArrayUtils.isEmpty(tercero.getDireccionesPostales()) 
						&& !ArrayUtils.isEmpty(tercero.getDireccionesElectronicas())) {
					tercero.setNotificacionTelematica(true);
				} else {
					tercero.setNotificacionTelematica(false);
				}		

				terceros.add(tercero);
			}

		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de terceros", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de terceros", e);
			throw new ISPACException("Error en la búsqueda de terceros", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return (IThirdPartyAdapter[])terceros.toArray(
				new IThirdPartyAdapter[terceros.size()]);
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

		List terceros = new ArrayList();
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getThirdPartySQLQuery(null, null, name, surname1, surname2));

			while (dbQuery.next()) {
				
				ThirdPartyAdapter tercero = createThirdPartyAdapter(dbQuery);
				
				if (onlyDefaultValues) {
					tercero.setDefaultDireccionElectronica(lookupDefaultElectronicAddress(tercero.getIdExt()));
					tercero.setDefaultDireccionPostal(lookupDefaultPostalAddress(tercero.getIdExt()));
				} else {
					tercero.setDireccionesElectronicas(lookupElectronicAddresses(tercero.getIdExt()));
					tercero.setDireccionesPostales(lookupPostalAddresses(tercero.getIdExt()));
				}
				
				if (ArrayUtils.isEmpty(tercero.getDireccionesPostales()) 
						&& !ArrayUtils.isEmpty(tercero.getDireccionesElectronicas())) {
					tercero.setNotificacionTelematica(true);
				} else {
					tercero.setNotificacionTelematica(false);
				}		

				terceros.add(tercero);
			}

		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de terceros", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de terceros", e);
			throw new ISPACException("Error en la búsqueda de terceros", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return (IThirdPartyAdapter[])terceros.toArray(
				new IThirdPartyAdapter[terceros.size()]);
	}

	/**
	 * Obtiene un tercero a partir de su identificador.
	 * @param id identificador del tercero
	 * @param onlyDefaultValues Indica si se cargan las direcciones asociadas por defecto
	 * @return Información del tercero con sus direcciones por defecto si así se indica 
	 * @throws ISPACException
	 */
	public IThirdPartyAdapter lookupById(String id, boolean onlyDefaultValues) throws ISPACException {

		ThirdPartyAdapter tercero = null;
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getThirdPartySQLQuery(id, null, null, null, null));

			if (dbQuery.next()) {
				
				tercero = createThirdPartyAdapter(dbQuery);
				
				if (onlyDefaultValues) {
					tercero.setDefaultDireccionElectronica(lookupDefaultElectronicAddress(tercero.getIdExt()));
					tercero.setDefaultDireccionPostal(lookupDefaultPostalAddress(tercero.getIdExt()));
				} else {
					tercero.setDireccionesElectronicas(lookupElectronicAddresses(tercero.getIdExt()));
					tercero.setDireccionesPostales(lookupPostalAddresses(tercero.getIdExt()));
				}
				
				if (ArrayUtils.isEmpty(tercero.getDireccionesPostales()) 
						&& !ArrayUtils.isEmpty(tercero.getDireccionesElectronicas())) {
					tercero.setNotificacionTelematica(true);
				} else {
					tercero.setNotificacionTelematica(false);
				}		
			}
			
		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de tercero", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de tercero", e);
			throw new ISPACException("Error en la búsqueda de tercero", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return tercero;
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

		ThirdPartyAdapter tercero = null;
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getThirdPartySQLQuery(id, null, null, null, null));

			if (dbQuery.next()) {
				
				tercero = createThirdPartyAdapter(dbQuery);
				
				tercero.setDefaultDireccionElectronica(getElectronicAddress(electronicAddressId));
				tercero.setDefaultDireccionPostal(getPostalAddress(postalAddressId));
				
				if (ArrayUtils.isEmpty(tercero.getDireccionesPostales()) 
						&& !ArrayUtils.isEmpty(tercero.getDireccionesElectronicas())) {
					tercero.setNotificacionTelematica(true);
				} else {
					tercero.setNotificacionTelematica(false);
				}		
			}
			
		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de tercero", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de tercero", e);
			throw new ISPACException("Error en la búsqueda de tercero", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return tercero;
	}

	
    /**
     * Obtiene una colección de todas las direcciones postales para un tercero
     * @param id identificador de tercero
     * @return lista de direcciones de postales relacionadas con un tercero
     * @throws Exception
     */
    public IPostalAddressAdapter [] lookupPostalAddresses(String id) throws ISPACException {
    	
		List direcciones = new ArrayList();
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getPostalAddressesSQLQuery(id));

			while (dbQuery.next()) {
				direcciones.add(createPostalAddressAdapter(dbQuery));
			}

		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de direcciones postales", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de direcciones postales", e);
			throw new ISPACException("Error en la búsqueda de direcciones postales", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return (IPostalAddressAdapter[])direcciones.toArray(
				new IPostalAddressAdapter[direcciones.size()]);
    }
    
    /**
     * Obtiene la dirección postal por defecto para un tercero
     * @param id identificador de tercero
     * @return dirección postal por defecto
     * @throws ISPACException
     */
    public IPostalAddressAdapter lookupDefaultPostalAddress(String id) throws ISPACException {

    	IPostalAddressAdapter direccion = null;
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getDefaultPostalAddressSQLQuery(id));

			if (dbQuery.next()) {
				direccion = createPostalAddressAdapter(dbQuery);
			}

		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de la dirección postal por defecto", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de la dirección postal por defecto", e);
			throw new ISPACException("Error en la búsqueda de la dirección postal por defecto", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return direccion;
    }
    
    /**
     * Obtiene una colección de todas las direcciones electrónicas para un tercero
     * @param id identificador de tercero
     * @return colección de direcciones electrónicas
     * @throws ISPACException
     */
    public IElectronicAddressAdapter [] lookupElectronicAddresses(String id) throws ISPACException {

		List direcciones = new ArrayList();
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getElectronicAddressesSQLQuery(id));

			while (dbQuery.next()) {
				direcciones.add(createElectronicAddressAdapter(dbQuery));
			}

		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de direcciones electrónicas", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de direcciones electrónicas", e);
			throw new ISPACException("Error en la búsqueda de direcciones electrónicas", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return (IElectronicAddressAdapter[])direcciones.toArray(
				new IElectronicAddressAdapter[direcciones.size()]);
    }

    /**
     * Obtiene la dirección electrónica por defecto para un tercero
     * @param id identificador de tercero
     * @return dirección electrónica
     * @throws ISPACException
     */
    public IElectronicAddressAdapter lookupDefaultElectronicAddress(String id) throws ISPACException {

    	IElectronicAddressAdapter direccion = null;
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getDefaultElectronicAddressSQLQuery(id));

			if (dbQuery.next()) {
				direccion = createElectronicAddressAdapter(dbQuery);
			}

		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de la dirección electrónica por defecto", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de la dirección electrónica por defecto", e);
			throw new ISPACException("Error en la búsqueda de la dirección electrónica por defecto", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return direccion;
    }

	/**
	 * Obtiene una dirección postal según su identificador
	 * @param addressId identificador de dirección postal
	 * @return dirección postal
	 * @throws ISPACException
	 */
	public IPostalAddressAdapter getPostalAddress(String addressId) throws ISPACException {

    	IPostalAddressAdapter direccion = null;
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getPostalAddressSQLQuery(addressId));

			if (dbQuery.next()) {
				direccion = createPostalAddressAdapter(dbQuery);
			}

		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de la dirección postal", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de la dirección postal", e);
			throw new ISPACException("Error en la búsqueda de la dirección postal", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return direccion;
	}

	/**
	 * Obtiene una dirección electrónica según su identificador
	 * @param addressId identificador de dirección electrónica
	 * @return dirección electrónica
	 * @throws ISPACException
	 */
	public IElectronicAddressAdapter getElectronicAddress(String addressId) throws ISPACException {

    	IElectronicAddressAdapter direccion = null;
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			dbQuery = cnt.executeDbQuery(getElectronicAddressSQLQuery(addressId));

			if (dbQuery.next()) {
				direccion = createElectronicAddressAdapter(dbQuery);
			}

		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de la dirección electrónica", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de la dirección electrónica", e);
			throw new ISPACException("Error en la búsqueda de la dirección electrónica", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return direccion;
	}
	
	/**
	 * Compone un tercero con la información de la base de datos.
	 * @param dbQuery Información obtenida de la base de datos.
	 * @return Tercero.
	 * @throws ISPACException si ocurre algún error.
	 */
	private static ThirdPartyAdapter createThirdPartyAdapter(DbQuery dbQuery)
			throws ISPACException {

		ThirdPartyAdapter tercero = new ThirdPartyAdapter();

		tercero.setIdExt(dbQuery.getString("ID"));
		tercero.setTipoPersona(dbQuery.getString("PERSON_TYPE"));
		tercero.setIdentificacion(dbQuery.getString("DOCUMENT"));
		tercero.setNombre(dbQuery.getString("NAME"));
		tercero.setPrimerApellido(dbQuery.getString("FIRST_NAME"));
		tercero.setSegundoApellido(dbQuery.getString("SECOND_NAME"));

		return tercero;
	}

	/**
	 * Compone una dirección postal con la información de la base de datos. 
	 * @param dbQuery Información obtenida de la base de datos.
	 * @return Dirección postal.
	 * @throws ISPACException si ocurre algún error.
	 */
	private IPostalAddressAdapter createPostalAddressAdapter(DbQuery dbQuery) throws ISPACException {

		PostalAddressAdapter direccion = new PostalAddressAdapter();
		
		direccion.setId(dbQuery.getString("ID"));
		direccion.setDireccionPostal(dbQuery.getString("ADDRESS"));
		direccion.setMunicipio(dbQuery.getString("CITY"));
		direccion.setCodigoPostal(dbQuery.getString("ZIP"));
		direccion.setTelefono(dbQuery.getString("PHONE"));
		
		String provinciaPais = dbQuery.getString("COUNTRY");
		if (StringUtils.isNotBlank(provinciaPais)) {
			int ix = provinciaPais.indexOf("/");
			if (ix > 0) {
				direccion.setProvincia(provinciaPais.substring(0, ix));
				direccion.setPais(provinciaPais.substring(ix+1));
			} else {
				direccion.setProvincia(provinciaPais);
			}
		}
		
		//INICIO [dipucr-Felipe 3#333 / 3#468]
		try{
			String codProvincia = getProvCode(direccion.getProvincia());
			direccion.setCodProvincia(codProvincia);
			String codProvinciaDir3 = DIR3IneUtils.getCodDir3Provincia(codProvincia);
			direccion.setCodProvinciaDir3(codProvinciaDir3);
			
			String[] codes = getCityCodes(direccion.getMunicipio(), codProvinciaDir3);
			String codMunicipio = codes[0];
			String codMunicipioDir3 = codes[1];
			codMunicipioDir3 = DIR3IneUtils.getCodDir3Municipio(codMunicipioDir3);
			direccion.setCodMunicipio(codMunicipio);
			direccion.setCodMunicipioDir3(codMunicipioDir3);
		}
		catch(Exception ex){
			logger.info(ex.getMessage());
		}
		//FIN [dipucr-Felipe 3#333]
		
		return direccion;
	}

	/**
	 * Compone una dirección electrónica con la información de la base de datos. 
	 * @param dbQuery Información obtenida de la base de datos.
	 * @return Dirección electrónica.
	 * @throws ISPACException si ocurre algún error.
	 */
	private static IElectronicAddressAdapter createElectronicAddressAdapter(DbQuery dbQuery) throws ISPACException {

		ElectronicAddressAdapter direccion = new ElectronicAddressAdapter();

		direccion.setId(dbQuery.getString("ID"));
		direccion.setDireccion(dbQuery.getString("ADDRESS"));
		
		String code = dbQuery.getString("CODE");
	
		if(StringUtils.equalsIgnoreCase(code, TYPE_TLFN_MOVIL)){
			direccion.setTipo(IElectronicAddressAdapter.MOBILE_PHONE_TYPE);
		}
		else if(StringUtils.equalsIgnoreCase(code, TYPE_DEU)){
			direccion.setTipo(IElectronicAddressAdapter.DEU_TYPE);
		}
		else if(StringUtils.equalsIgnoreCase(code, TYPE_MAIL)){
			direccion.setTipo(IElectronicAddressAdapter.MAIL_TYPE);
		}
		else{
			direccion.setTipo(IElectronicAddressAdapter.PHONE_TYPE);
		}
		
		return direccion;
	}

	/**
	 * Obtiene la consulta SQL para obtener el/los tercero/s.
	 * @param id Identificador del tercero.
	 * @param nifcif NIF/CIF del tercero.
	 * @param name Nombre o razón social del tercero.
	 * @param surname1 Primer apellido del tercero.
	 * @param surname2 Segundo apellido del tercero.
	 * @return Consulta SQL.
	 */
	private static String getThirdPartySQLQuery(String id, String nifcif, String name,
			String surname1, String surname2) {

		StringBuffer pfisCondition = new StringBuffer();
		StringBuffer pjurCondition = new StringBuffer();

		// Filtro por identificador de tercero
		if (StringUtils.isNotBlank(id)) {

			pfisCondition.append(" WHERE").append(" PERSON.ID=").append(id.trim());

			pjurCondition.append(" WHERE").append(" PERSON.ID=").append(id.trim());
		}

		// Filtro por NIF/CIF
		if (StringUtils.isNotBlank(nifcif)) {
			if (pfisCondition.length() > 0) {
				pfisCondition.append(" AND ");
				pjurCondition.append(" AND ");
			} else {
				pfisCondition.append(" WHERE ");
				pjurCondition.append(" WHERE ");
			}

			pfisCondition.append("UPPER(PERSON.NIF)='").append(
					nifcif.toUpperCase().trim()).append("'");

			pjurCondition.append("UPPER(PERSON.CIF)='").append(
					nifcif.toUpperCase().trim()).append("'");
		}

		// Filtro por nombre y apellidos
		if (StringUtils.isNotBlank(name) || StringUtils.isNotBlank(surname1)
				|| StringUtils.isNotBlank(surname2)) {

			StringBuffer pjurName = new StringBuffer();
			
			if (StringUtils.isNotBlank(name)) {
				if (pfisCondition.length() > 0) {
					pfisCondition.append(" AND ");
				} else {
					pfisCondition.append(" WHERE ");
				}
				pfisCondition.append("UPPER(PERSON.SURNAME) LIKE UPPER('%")
							 .append(DBUtil.replaceQuotes(name.toUpperCase().trim()))
							 .append("%')");
				
				pjurName.append("%").append(name.toUpperCase().trim());
			}

			if (StringUtils.isNotBlank(surname1)) {
				if (pfisCondition.length() > 0) {
					pfisCondition.append(" AND ");
				} else {
					pfisCondition.append(" WHERE ");
				}
				pfisCondition.append("UPPER(PERSON.FIRST_NAME) LIKE UPPER('%")
							 .append(DBUtil.replaceQuotes(surname1.toUpperCase().trim()))
							 .append("%')");
				
				pjurName.append("%").append(surname1.toUpperCase().trim());
			}

			if (StringUtils.isNotBlank(surname2)) {
				if (pfisCondition.length() > 0) {
					pfisCondition.append(" AND ");
				} else {
					pfisCondition.append(" WHERE ");
				}
				pfisCondition.append("UPPER(PERSON.SECOND_NAME) LIKE UPPER('%")
							 .append(DBUtil.replaceQuotes(surname2.toUpperCase().trim()))
							 .append("%')");
				
				pjurName.append("%").append(surname2.toUpperCase().trim());
			}
			
			if (pjurCondition.length() > 0) {
				pjurCondition.append(" AND ");
			} else {
				pjurCondition.append(" WHERE ");
			}
			pjurCondition.append("UPPER(PERSON.NAME) LIKE UPPER('")
						 .append(DBUtil.replaceQuotes(pjurName.toString()))
						 .append("%')");
		}

		String sql = new StringBuffer()
			.append("SELECT ")
			.append(" PERSON.ID, 'F' AS PERSON_TYPE, PERSON.NIF AS DOCUMENT, PERSON.SURNAME AS NAME, PERSON.FIRST_NAME, PERSON.SECOND_NAME")
			.append(" FROM SCR_PFIS PERSON")
			.append(pfisCondition)
			.append(" UNION")
			.append(" SELECT")
			.append(" PERSON.ID, 'J' AS PERSON_TYPE, PERSON.CIF AS DOCUMENT, PERSON.NAME, '' AS FIRST_NAME, '' AS SECOND_NAME")
			.append(" FROM SCR_PJUR PERSON")
			.append(pjurCondition)
			.toString();

		return sql;
	}

	/**
	 * Obtiene la consulta SQL común para las direcciones electrónicas.
	 * @return Consulta SQL.
	 */
	private static String getCommonElectronicAddressesSQLQuery() {

		String sql = new StringBuffer()
			.append("SELECT ")
			.append(" BOUND.ID, TYPEADDRESS.CODE,ADDRTEL.ADDRESS")
			.append(" FROM SCR_ADDRESS BOUND,SCR_ADDRTEL ADDRTEL ,SCR_TYPEADDRESS TYPEADDRESS")
			.toString();

		return sql;
	}

	/**
	 * Obtiene la consulta SQL para obtener las direcciones electrónicas.
	 * @param thirdPartyId Identificador del tercero.
	 * @return Consulta SQL.
	 */
	private static String getElectronicAddressesSQLQuery(String thirdPartyId) {

		String sql = new StringBuffer()
			.append(getCommonElectronicAddressesSQLQuery())
			.append(" WHERE BOUND.ID_PERSON=")
			.append(thirdPartyId)
			.append(" AND BOUND.TYPE="+TYPE_DIR_ELECTRONICA)
			.append(" AND BOUND.ID=ADDRTEL.ID")
			.append(" AND ADDRTEL.TYPE=TYPEADDRESS.ID")
			//[dipucr-Felipe 3#166] Incluir mail en direcciones electrónicas
//			.append(" AND (TYPEADDRESS.CODE='"+TYPE_DEU+"' OR TYPEADDRESS.CODE='"+TYPE_TLFN_MOVIL+"')")
			.append(" AND (TYPEADDRESS.CODE='"+TYPE_MAIL+"' OR TYPEADDRESS.CODE='"+TYPE_DEU+"' OR TYPEADDRESS.CODE='"+TYPE_TLFN_MOVIL+"')")
			.append(" ORDER BY ADDRTEL.PREFERENCE DESC, BOUND.ID")
			.toString();

		return sql;
	}

	/**
	 * Obtiene la consulta SQL para obtener la dirección electrónica por defecto.
	 * @param thirdPartyId Identificador del tercero.
	 * @return Consulta SQL.
	 */
	private static String getDefaultElectronicAddressSQLQuery(String thirdPartyId) {

	
		String sql = new StringBuffer()
			.append(getCommonElectronicAddressesSQLQuery())
			.append(" WHERE BOUND.ID_PERSON=")
			.append(thirdPartyId)
			.append(" AND BOUND.TYPE="+TYPE_DIR_ELECTRONICA)
			.append(" AND BOUND.ID=ADDRTEL.ID")
			.append(" AND ADDRTEL.TYPE=TYPEADDRESS.ID")
			//[dipucr-Felipe 3#166] Incluir mail en direcciones electrónicas
//			.append(" AND (TYPEADDRESS.CODE='"+TYPE_DEU+"' OR TYPEADDRESS.CODE='"+TYPE_TLFN_MOVIL+"')")
			.append(" AND (TYPEADDRESS.CODE='"+TYPE_MAIL+"' OR TYPEADDRESS.CODE='"+TYPE_DEU+"' OR TYPEADDRESS.CODE='"+TYPE_TLFN_MOVIL+"')")
		   .append(" AND ADDRTEL.PREFERENCE=1")
			.toString();

		return sql;
	}

	/**
	 * Obtiene la consulta SQL para obtener una dirección electrónica.
	 * @param addressId Identificador de la dirección.
	 * @return Consulta SQL.
	 */
	private static String getElectronicAddressSQLQuery(String addressId) {

		String sql = new StringBuffer()
			.append(getCommonElectronicAddressesSQLQuery())
			.append(" WHERE BOUND.ID=")
			.append(addressId)
			.append(" AND BOUND.ID=ADDRTEL.ID AND ADDRTEL.TYPE=TYPEADDRESS.ID")
			.toString();

		return sql;
	}

	/**
	 * Obtiene la consulta SQL común para las direcciones postales.
	 * @return Consulta SQL.
	 */
	
	
	private static String getCommonPostalAddressesSQLQuery() {	
		String sql = new StringBuffer()
			.append("SELECT ")
			.append(" BOUND.ID,DOM.ADDRESS,DOM.CITY,DOM.ZIP,DOM.COUNTRY,PHONE.ADDRESS AS PHONE")
			.append(" FROM SCR_ADDRESS BOUND")
			.append(" LEFT OUTER JOIN (SELECT PBOUND.ID_PERSON,ADDRTEL.ADDRESS FROM SCR_ADDRESS PBOUND,SCR_ADDRTEL ADDRTEL, SCR_TYPEADDRESS TYPEADDRESS WHERE PBOUND.TYPE="+TYPE_DIR_ELECTRONICA+" AND PBOUND.ID=ADDRTEL.ID AND ADDRTEL.TYPE=TYPEADDRESS.ID AND TYPEADDRESS.CODE='"+TYPE_TLFN_FIJO+"' AND ADDRTEL.PREFERENCE=1) PHONE ON BOUND.ID_PERSON=PHONE.ID_PERSON")
			.append(",SCR_DOM DOM")
			.toString();

		return sql;
	}


	
	/**
	 * Obtiene la consulta SQL para obtener las direcciones postales.
	 * @param thirdPartyId Identificador del tercero.
	 * @return Consulta SQL.
	 */
	private static String getPostalAddressesSQLQuery(String thirdPartyId) {

		String sql = new StringBuffer()
			.append(getCommonPostalAddressesSQLQuery())
			.append(" WHERE BOUND.ID_PERSON=")
			.append(thirdPartyId)
			.append(" AND BOUND.TYPE="+TYPE_DIR_DOMICILIO+"")
			.append(" AND BOUND.ID=DOM.ID")
			.append(" ORDER BY DOM.PREFERENCE DESC, BOUND.ID")
			.toString();

		return sql;
	}

	/**
	 * Obtiene la consulta SQL para obtener la dirección postal por defecto.
	 * @param thirdPartyId Identificador del tercero.
	 * @return Consulta SQL.
	 */
	private static String getDefaultPostalAddressSQLQuery(String thirdPartyId) {

		String sql = new StringBuffer()
			.append(getCommonPostalAddressesSQLQuery())
			.append(" WHERE BOUND.ID_PERSON=")
			.append(thirdPartyId)
			.append(" AND BOUND.TYPE=0")
			.append(" AND BOUND.ID=DOM.ID")
			.append(" AND DOM.PREFERENCE=1")
			.toString();

		return sql;
	}

	/**
	 * Obtiene la consulta SQL para obtener una dirección postal.
	 * @param addressId Identificador de la dirección.
	 * @return Consulta SQL.
	 */
	private static String getPostalAddressSQLQuery(String addressId) {

		String sql = new StringBuffer()
			.append(getCommonPostalAddressesSQLQuery())
			.append(" WHERE BOUND.ID=")
			.append(addressId)
			.append(" AND BOUND.ID=DOM.ID")
		
			.toString();

		return sql;
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
				strQuery = getInsertScrDomSQLQuery(idAddress, direccion, provincia,
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
			String direccion, String provincia, String ciudad, String cpostal, String pais,
			int preferencia) {

		String sql = new StringBuffer()
				.append("INSERT INTO SCR_DOM (ID, ADDRESS, CITY, ZIP, COUNTRY, PAIS, PREFERENCE) VALUES (")
				.append(idAddress).append(",'").append(direccion).append("','")
				.append(ciudad).append("','").append(cpostal).append("','").append(provincia).append("','")
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
	 * [dipucr-Felipe 3#333]
	 * Devuelve el código de la ciudad por su nombre
	 * Devuelve el código DIR3 de la ciudad por su nombre
	 * Ambos resultados se devuelven en un array de dos posiciones
	 * @param cityName
	 * @param provCode [dipucr-Felipe #468]
	 * @return
	 * @throws ISPACException
	 */
    private String[] getCityCodes(String cityName, String provCode) throws ISPACException {

    	String[] codes = new String[2];
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery(getCityByNameSQLQuery(cityName, provCode));
			if (dbQuery.next()) {
				codes[0] = dbQuery.getString("CODE");
				codes[1] = dbQuery.getString("DIR3");
			}

		} catch (ISPACException e) {
			logger.error("Error al buscar el código de la ciudad por nombre: " + cityName, e);
			throw e;
		} catch (Exception e) {
			logger.error("Error al buscar el código de la ciudad por nombre: " + cityName, e);
			throw new ISPACException("Error al buscar el código de la ciudad por nombre: " + cityName, e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return codes;
    }

	private static String getCityByNameSQLQuery(String cityName, String provCode) {
		
		String sql = new StringBuffer()
			.append(" SELECT ID, CODE, NAME, ID_PROV, DIR3 ")
			.append(" FROM SCR_CITIES")
			.append(" WHERE NAME=E'").append(cityName.replace("'", "\\'"))
			//[dipucr-Felipe 3#333] Escapo las comillas simples con E delante. Ejemplo: E'Hospitalet, L\''
			.append("' AND ID_PROV = ")
			.append(provCode) //[dipucr-Felipe #468]
			.toString();

		return sql;
	}
	
	
	/**
	 * [dipucr-Felipe 3#333]
	 * Devuelve el código de la provincia por su nombre
	 * @param provName
	 * @return
	 * @throws ISPACException
	 */
    private String getProvCode(String provName) throws ISPACException {

    	String code = null;
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery(getProvByNameSQLQuery(provName));
			if (dbQuery.next()) {
				code = dbQuery.getString("CODE");
			}

		} catch (ISPACException e) {
			logger.error("Error al buscar el código de la provincia por nombre: " + provName, e);
			throw e;
		} catch (Exception e) {
			logger.error("Error al buscar el código de la provincia por nombre: " + provName, e);
			throw new ISPACException("Error al buscar el código de la provincia por nombre: " + provName, e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return code;
    }

	private static String getProvByNameSQLQuery(String provName) {
		
		String sql = new StringBuffer()
			.append(" SELECT ID, CODE, NAME ")
			.append(" FROM SCR_PROV")
			.append(" WHERE NAME='").append(provName)
			.append("'")
			.toString();

		return sql;
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
			
			if (null != postalAddress){ // Actualizamos la dirección
				String idAddress = postalAddress.getId();
				
				strQuery = getUpdateScrAddressSQLQuery(idAddress, provincia, municipio, cpostal, direccion);
				cnt.execute(strQuery);
			}
			else{ //Inserción de dirección
				int idAddress = getContador(cnt, TABLA_ADDRESS) + 1;
				strQuery = getInsertScrDomSQLQuery(idAddress, direccion, provincia, municipio, cpostal, "ESPAÑA", 1);
				cnt.execute(strQuery);

				strQuery = getInsertScrAddressSQLQuery(idAddress, idPerson, 
						Integer.valueOf(TYPE_DIR_DOMICILIO).intValue());
				cnt.execute(strQuery);

				updateContador(cnt, TABLA_ADDRESS, idAddress);
			}

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
		sbQuery.append(cpostal);
		sbQuery.append("', COUNTRY = '");
		sbQuery.append(provincia);
		sbQuery.append("' WHERE ID = ");
		sbQuery.append(idAddress);
		
		return sbQuery.toString();
		
	}

//	/**
//	 * Obtiene la consulta SQL para obtener el/los tercero/s.
//	 * @param id Identificador del tercero.
//	 * @param nifcif NIF/CIF del tercero.
//	 * @param name Nombre o razón social del tercero.
//	 * @param surname1 Primer apellido del tercero.
//	 * @param surname2 Segundo apellido del tercero.
//	 * @return Consulta SQL.
//	 */
//	private static String getSQLQuery(String id, String nifcif, String name,
//			String surname1, String surname2) {
//
//		StringBuffer pfisCondition = new StringBuffer();
//		StringBuffer pjurCondition = new StringBuffer();
//
//		// Filtro por identificador de tercero
//		if (StringUtils.isNotBlank(id)) {
//
//			pfisCondition.append(" WHERE").append(" PERSON.ID=").append(id.trim());
//
//			pjurCondition.append(" WHERE").append(" PERSON.ID=").append(id.trim());
//		}
//
//		// Filtro por NIF/CIF
//		if (StringUtils.isNotBlank(nifcif)) {
//			if (pfisCondition.length() > 0) {
//				pfisCondition.append(" AND ");
//				pjurCondition.append(" AND ");
//			} else {
//				pfisCondition.append(" WHERE ");
//				pjurCondition.append(" WHERE ");
//			}
//
//			pfisCondition.append("PERSON.NIF='").append(
//					nifcif.toUpperCase().trim()).append("'");
//
//			pjurCondition.append("PERSON.CIF='").append(
//					nifcif.toUpperCase().trim()).append("'");
//		}
//
//		// Filtro por nombre y apellidos
//		if (StringUtils.isNotBlank(name) || StringUtils.isNotBlank(surname1)
//				|| StringUtils.isNotBlank(surname2)) {
//
//			StringBuffer pjurName = new StringBuffer();
//			
//			if (StringUtils.isNotBlank(name)) {
//				if (pfisCondition.length() > 0) {
//					pfisCondition.append(" AND ");
//				} else {
//					pfisCondition.append(" WHERE ");
//				}
//				pfisCondition.append("PERSON.SURNAME LIKE '%")
//							 .append(DBUtil.replaceQuotes(name.toUpperCase().trim()))
//							 .append("%'");
//				
//				pjurName.append("%").append(name.toUpperCase().trim());
//			}
//
//			if (StringUtils.isNotBlank(surname1)) {
//				if (pfisCondition.length() > 0) {
//					pfisCondition.append(" AND ");
//				} else {
//					pfisCondition.append(" WHERE ");
//				}
//				pfisCondition.append("PERSON.FIRST_NAME LIKE '%")
//							 .append(DBUtil.replaceQuotes(surname1.toUpperCase().trim()))
//							 .append("%'");
//				
//				pjurName.append("%").append(surname1.toUpperCase().trim());
//			}
//
//			if (StringUtils.isNotBlank(surname2)) {
//				if (pfisCondition.length() > 0) {
//					pfisCondition.append(" AND ");
//				} else {
//					pfisCondition.append(" WHERE ");
//				}
//				pfisCondition.append("PERSON.SECOND_NAME LIKE '%")
//							 .append(DBUtil.replaceQuotes(surname2.toUpperCase().trim()))
//							 .append("%'");
//				
//				pjurName.append("%").append(surname2.toUpperCase().trim());
//			}
//			
//			if (pjurCondition.length() > 0) {
//				pjurCondition.append(" AND ");
//			} else {
//				pjurCondition.append(" WHERE ");
//			}
//			pjurCondition.append("PERSON.NAME LIKE '")
//						 .append(DBUtil.replaceQuotes(pjurName.toString()))
//						 .append("%'");
//		}
//
//		String sql = new StringBuffer()
//			.append("SELECT ")
//			.append(
//					" PERSON.ID, 'F' AS PERSON_TYPE, PERSON.NIF AS DOCUMENT, PERSON.SURNAME AS NAME, PERSON.FIRST_NAME, PERSON.SECOND_NAME, DOM.ADDRESS AS ADDRESS, DOM.CITY AS CITY, DOM.ZIP AS ZIP, DOM.COUNTRY AS COUNTRY, EMAIL.ADDRESS AS EMAIL, PHONE.ADDRESS AS PHONE, MOBILE.ADDRESS AS MOBILE")
//			.append(" FROM SCR_PFIS PERSON")
//			.append(
//					" LEFT OUTER JOIN (SELECT BOUND.ID_PERSON,DOM.ADDRESS,DOM.CITY,DOM.ZIP,DOM.COUNTRY FROM SCR_ADDRESS BOUND,SCR_DOM DOM WHERE BOUND.TYPE=0 AND BOUND.ID=DOM.ID AND DOM.PREFERENCE=1) DOM ON PERSON.ID=DOM.ID_PERSON")
//			.append(
//					" LEFT OUTER JOIN (SELECT BOUND.ID_PERSON,ADDRTEL.ADDRESS FROM SCR_ADDRESS BOUND,SCR_ADDRTEL ADDRTEL WHERE BOUND.TYPE=1 AND BOUND.ID=ADDRTEL.ID AND ADDRTEL.TYPE=2 AND ADDRTEL.PREFERENCE=1) EMAIL ON PERSON.ID=EMAIL.ID_PERSON")
//			.append(
//					" LEFT OUTER JOIN (SELECT BOUND.ID_PERSON,ADDRTEL.ADDRESS FROM SCR_ADDRESS BOUND,SCR_ADDRTEL ADDRTEL WHERE BOUND.TYPE=1 AND BOUND.ID=ADDRTEL.ID AND ADDRTEL.TYPE=1 AND ADDRTEL.PREFERENCE=1 AND ADDRTEL.ADDRESS LIKE '9%') PHONE ON PERSON.ID=PHONE.ID_PERSON")
//			.append(
//					" LEFT OUTER JOIN (SELECT BOUND.ID_PERSON,ADDRTEL.ADDRESS FROM SCR_ADDRESS BOUND,SCR_ADDRTEL ADDRTEL WHERE BOUND.TYPE=1 AND BOUND.ID=ADDRTEL.ID AND ADDRTEL.TYPE=1 AND ADDRTEL.PREFERENCE=1 AND ADDRTEL.ADDRESS LIKE '6%') MOBILE ON PERSON.ID=MOBILE.ID_PERSON")
//			.append(pfisCondition)
//			.append(" UNION")
//			.append(" SELECT")
//			.append(
//					" PERSON.ID, 'J' AS PERSON_TYPE, PERSON.CIF AS DOCUMENT, PERSON.NAME, '' AS FIRST_NAME, '' AS SECOND_NAME, DOM.ADDRESS AS ADDRESS, DOM.CITY AS CITY, DOM.ZIP AS ZIP, DOM.COUNTRY AS COUNTRY, EMAIL.ADDRESS AS EMAIL, PHONE.ADDRESS AS PHONE, MOBILE.ADDRESS AS MOBILE")
//			.append(" FROM SCR_PJUR PERSON")
//			.append(
//					" LEFT OUTER JOIN (SELECT BOUND.ID_PERSON,DOM.ADDRESS,DOM.CITY,DOM.ZIP,DOM.COUNTRY FROM SCR_ADDRESS BOUND,SCR_DOM DOM WHERE BOUND.TYPE=0 AND BOUND.ID=DOM.ID AND DOM.PREFERENCE=1) DOM ON PERSON.ID=DOM.ID_PERSON")
//			.append(
//					" LEFT OUTER JOIN (SELECT BOUND.ID_PERSON,ADDRTEL.ADDRESS FROM SCR_ADDRESS BOUND,SCR_ADDRTEL ADDRTEL WHERE BOUND.TYPE=1 AND BOUND.ID=ADDRTEL.ID AND ADDRTEL.TYPE=2 AND ADDRTEL.PREFERENCE=1) EMAIL ON PERSON.ID=EMAIL.ID_PERSON")
//			.append(
//					" LEFT OUTER JOIN (SELECT BOUND.ID_PERSON,ADDRTEL.ADDRESS FROM SCR_ADDRESS BOUND,SCR_ADDRTEL ADDRTEL WHERE BOUND.TYPE=1 AND BOUND.ID=ADDRTEL.ID AND ADDRTEL.TYPE=1 AND ADDRTEL.PREFERENCE=1 AND ADDRTEL.ADDRESS LIKE '9%') PHONE ON PERSON.ID=PHONE.ID_PERSON")
//			.append(
//					" LEFT OUTER JOIN (SELECT BOUND.ID_PERSON,ADDRTEL.ADDRESS FROM SCR_ADDRESS BOUND,SCR_ADDRTEL ADDRTEL WHERE BOUND.TYPE=1 AND BOUND.ID=ADDRTEL.ID AND ADDRTEL.TYPE=1 AND ADDRTEL.PREFERENCE=1 AND ADDRTEL.ADDRESS LIKE '6%') MOBILE ON PERSON.ID=MOBILE.ID_PERSON")
//			.append(pjurCondition)
//			.toString();
//
//		return sql;
//	}
}

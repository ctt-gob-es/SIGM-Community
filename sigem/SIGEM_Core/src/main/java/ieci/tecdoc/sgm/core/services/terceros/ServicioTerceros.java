package ieci.tecdoc.sgm.core.services.terceros;

import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionElectronica;
import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionPostal;
import ieci.tecdoc.sgm.core.services.terceros.dto.Tercero;

import java.util.List;


/**
 * Interfaz para el servicio de Terceros.  
 * 
 */
public interface ServicioTerceros {

	/**
	 * Obtiene una lista de terceros en función del código de identificación.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param code Código de identificación del tercero
	 * @return Lista de terceros ({@link Tercero}.
	 * @exception TercerosException si ocurre algún error.
	 */
	public List lookup(String entityId, String code) throws TercerosException;

	/**
	 * Obtiene una lista de terceros en función del códigos de identificación.
	 * @param entityId Identificador de la entidad administrativa. 
	 * @param code Código de identificación del tercero
	 * @param onlyDefaultValues Indica si se cargan solamente las direcciones asociadas por defecto
	 * @return Lista de terceros ({@link Tercero}. 
	 * @exception TercerosException si ocurre algún error.
	 */
	public List lookup(String entityId, String code, boolean defaultValues) throws TercerosException;

	/**
	 * Obtiene una lista de terceros en función del nombre y dos apellidos.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param name Nombre del tercero
	 * @param surnam1 Primer apellido del tercero
	 * @param surname2 Segundo apellido del tercero
	 * @return Lista de terceros ({@link Tercero}.
	 * @exception TercerosException si ocurre algún error.
	 */
	public List lookup(String entityId, String name, String surname1, 
			String surname2) throws TercerosException;

	/**
	 * Obtiene una lista de terceros en función del nombre y dos apellidos.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param name Nombre del tercero
	 * @param surnam1 Primer apellido del tercero
	 * @param surname2 Segundo apellido del tercero
	 * @param onlyDefaultValues Indica si se cargan solamente las direcciones asociadas por defecto
	 * @return Lista de terceros ({@link Tercero}.
	 * @exception TercerosException si ocurre algún error.
	 */
	public List lookup(String entityId, String name, String surname1, 
			String surname2, boolean onlyDefaultValues) throws TercerosException;

	/**
	 * Obtiene un tercero a partir de su identificador interno.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param id Identificador interno del tercero.
	 * @return Información del tercero.
	 * @exception TercerosException si ocurre algún error.
	 */
	public Tercero lookupById(String entityId, String id) throws TercerosException;
	
	/**
	 * Obtiene un tercero a partir de su identificador.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param id identificador del tercero
	 * @param onlyDefaultValues Indica si se cargan solamente las direcciones asociadas por defecto
	 * @return Información del tercero con sus direcciones por defecto si así se indica 
	 * @exception TercerosException si ocurre algún error.
	 */
	public Tercero lookupById(String entityId, String id, boolean onlyDefaultValues) 
			throws TercerosException;

	/**
	 * Obtiene un tercero a partir de su identificador interno.
	 * @param entityId Identificador de la entidad administrativa.
	 * @param postalAddressId Identificador de la dirección postal.
	 * @param electronicAddressId Identificador de la dirección postal.
	 * @return Información del tercero.
	 * @exception TercerosException si ocurre algún error.
	 */
	public Tercero lookupById(String entityId, String id, String postalAddressId, 
			String electronicAddressId) throws TercerosException;

	/**
     * Obtiene una colección de todas las direcciones postales para un tercero
     * @param entityId Identificador de la entidad administrativa.
     * @param id identificador de tercero
     * @return lista de direcciones de postales relacionadas con un tercero
     * @exception TercerosException si ocurre algún error.
     */
	public DireccionPostal[] lookupPostalAddresses(String entityId, String id) throws TercerosException;
	
    /**
     * Obtiene la dirección postal por defecto para un tercero
     * @param entityId Identificador de la entidad administrativa.
     * @param id identificador de tercero
     * @return dirección postal por defecto
     * @exception TercerosException si ocurre algún error.
     */
	public DireccionPostal lookupDefaultPostalAddress(String entityId, String id) 
			throws TercerosException;

    /**
     * Obtiene una colección de todas las direcciones electrónicas para un tercero
     * @param entityId Identificador de la entidad administrativa.
     * @param id identificador de tercero
     * @return colección de direcciones electrónicas
     * @exception TercerosException si ocurre algún error.
     */
	public DireccionElectronica[] lookupElectronicAddresses(String entityId, String id) 
			throws TercerosException;

    /**
     * Obtiene la dirección electrónica por defecto para un tercero
     * @param entityId Identificador de la entidad administrativa.
     * @param id identificador de tercero
     * @return dirección electrónica
     * @exception TercerosException si ocurre algún error.
     */
	public DireccionElectronica lookupDefaultElectronicAddress(String entityId, String id) 
			throws TercerosException;

	/**
	 * Obtiene una dirección postal según su identificador
	 * @param entityId Identificador de la entidad administrativa.
	 * @param id identificador de dirección postal
	 * @return dirección postal
	 * @exception TercerosException si ocurre algún error.
	 */
	public DireccionPostal getPostalAddress(String entityId, String id) throws TercerosException;

	/**
	 * Obtiene una dirección electrónica según su identificador
	 * @param entityId Identificador de la entidad administrativa.
	 * @param id identificador de dirección electrónica
	 * @return dirección electrónica
	 * @exception TercerosException si ocurre algún error.
	 */
	public DireccionElectronica getElectronicAddress(String entityId, String id) throws TercerosException;

	/**
	 * [eCenpri-Felipe #477]
	 * Inserta un nuevo tercero en la BBDD
	 * @param entityId
	 * @param nif
	 * @param tipoDoc
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
	 * @throws TercerosException
	 */
    public boolean insertThirdParty(String entityId, String nif, int tipoDoc, String nombre, String ape1, String ape2,
    		String tipo, String provincia, String municipio, String cpostal, String direccion,
    		String tfnoFijo, String tfnoMovil, String email) throws TercerosException;
    
    /**
	 * [dipucr-Felipe #583]
	 * @param idPerson
	 * @param email
	 * @return
	 * @throws ISPACException 
	 */
	public boolean insertDefaultEmail(String entityId, int idPerson, String email) throws TercerosException;
    
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
	public boolean updateThirdParty(String entityId, int idPerson, String nombre, String ape1, String ape2, 
			String provincia, String municipio, String cpostal, String direccion) throws TercerosException;
    
	/**
	 * Establece el origen de datos.
	 * @param dsName Origen de datos.
	 */
	public void setDsName(String dsName);
}

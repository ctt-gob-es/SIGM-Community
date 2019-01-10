package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbQuery;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.thirdparty.ElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IElectronicAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.PostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.ThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.terceros.ServicioTerceros;
import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionElectronica;
import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionPostal;
import ieci.tecdoc.sgm.core.services.terceros.dto.Tercero;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

public class AccesoBBDDRegistro {
	
	public static final int TIPO_USUARIO = 1;
	public static final int TIPO_DEPARTAMENTO = 2;
	public static final int TIPO_GRUPO = 3;
	
	/** Logger de la clase. */
	private static final Logger LOGGER = Logger.getLogger(AccesoBBDDRegistro.class);
	
	/** Nombre del origen de datos por defecto. */
	private static final String DEFAULT_DATASOURCE_NAME = "java:comp/env/jdbc/registroDS_";
	
	/** Nombre del origen de datos. */
	protected String dsName = DEFAULT_DATASOURCE_NAME;
	
	/**
	 * Constructor.
	 * @exception ISPACException si ocurre algún error.
	 */
	public AccesoBBDDRegistro() throws ISPACException {
		super();
		
		// Nombre del datasource de SICRES
		this.dsName = ISPACConfiguration.getInstance().get("THIRDPARTY_API_DATASOURCE_NAME",DEFAULT_DATASOURCE_NAME);
	}

	/**
	 * Constructor.
	 * @param dsName Nombre del origen de datos.
	 * @exception ISPACException si ocurre algún error.
	 */
	public AccesoBBDDRegistro(String entidad) throws ISPACException {
		super();
		
		// Nombre del datasource de SICRES
		this.dsName = ISPACConfiguration.getInstance().get("THIRDPARTY_API_DATASOURCE_NAME",DEFAULT_DATASOURCE_NAME)+entidad;
	}
	
	/**
	 * Devuelve los datos del tercero por su id
	 * @param idTercero
	 * @return
	 */
	public Tercero getDatosTercero(String idTercero){
		
		ServicioTerceros servicioTerceros;
		Tercero tercero = null;
		String entityId = null;

		try{
			if(StringUtils.isNotBlank(idTercero)){
				servicioTerceros = LocalizadorServicios.getServicioTerceros();
				servicioTerceros.setDsName(dsName);
				
				OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
				if (info != null)
					entityId = info.getOrganizationId();
				
				tercero = servicioTerceros.lookupById(entityId, idTercero);
			}
		}
		catch (SigemException e) {
			LOGGER.error("Error al recuperar los datos del tercero con id: " + idTercero + ". " + e.getMessage(), e);
		}
		return tercero;
	}
	
	/**
	 * Búsqueda de terceros por NIF/CIF
	 * Llamamos al servicio de terceros
	 * @param nif
	 * @return
	 */
	public IThirdPartyAdapter getDatosTerceroByNif(String nif){
		
		ServicioTerceros servicioTerceros;
		List<?> tercero = null;
		String entityId = null;

		try{
			if(StringUtils.isNotBlank(nif)){
				servicioTerceros = LocalizadorServicios.getServicioTerceros();
				servicioTerceros.setDsName(dsName);
				
				OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
				if (info != null){
					entityId = info.getOrganizationId();
				}
				
				tercero = servicioTerceros.lookup(entityId, nif);
			}
		}
		catch (SigemException e) {
			LOGGER.error("Error al recuperar los datos del tercero con id: " + nif + ". " + e.getMessage(), e);
		}
		if(null == tercero || tercero.size() == 0){
			return null;
		}
		return getIThirdPartyAdapter((Tercero) tercero.get(0));
	}
	
	/**
	 * Acceso a la tabla SCR_PJUR que contiene los identificadores de los participantes
	 * @param nameParticipante
	 * @return
	 * @throws ISPACException
	 */
	@Deprecated //[eCenpri-Felipe] Se puede obtener del servicio de terceros
	public String getIdParticipanteJuridico(String nameParticipante) throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String idParticpante = "";
		
		try {
			if(StringUtils.isNotBlank(nameParticipante)){
				cnt.getConnection();
				dbQuery = cnt.executeDbQuery("SELECT ID FROM SCR_PJUR WHERE NAME LIKE '%" + nameParticipante + "%'");
				while (dbQuery.next()) {
					idParticpante = dbQuery.getString("ID");
				}
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda de participantes", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de participantes", e);
			throw new ISPACException("Error en la búsqueda de participantes", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return idParticpante;		
	}
	
	/**
	 * Devuelve el mail de un departamento
	 * @param nombreResp
	 * @return
	 * @throws ISPACException
	 */
	public String getEmailUsuario(String idUsuario) throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String email = "";
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT EMAIL FROM IUSERDATA"
					+ " WHERE ID = '" + idUsuario.trim().split("-")[1] + "'");
			while (dbQuery.next()) {
				email = dbQuery.getString("EMAIL");
			}
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return email;
	}
	
	/**
	 * 
	 * @param tipoTercero
	 * @param id
	 * @return
	 * @throws ISPACException
	 */
	public String getEmailResponsable(int tipoTercero, String id)  throws ISPACException{
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String email = "";
		
		try {
			
			cnt.getConnection();
			
			String tableName = "";
			switch (tipoTercero) {
			case TIPO_USUARIO:
				tableName = "IUSERDATA";
				break;
			case TIPO_DEPARTAMENTO:
				tableName = "IUSERDEPTHDR";
				break;
			case TIPO_GRUPO:
				tableName = "IUSERGROUPHDR";
				break;
			default:
				throw new ISPACException("Tipo de tercero inválido: " + tipoTercero);
			}
			
			dbQuery = cnt.executeDbQuery("SELECT EMAIL FROM " + tableName + " WHERE ID = '" + id + "'");
			if (dbQuery.next()) {
				email = dbQuery.getString("EMAIL");
			}
			
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return email;
	}
	
	/**
	 * 
	 * @param tipoTercero
	 * @param id
	 * @return
	 * @throws ISPACException
	 */
	public String getNombreResponsable(String idCompleto)  throws ISPACException{
		
		String[] arrId = idCompleto.split("-");
		int tipoTercero = Integer.valueOf(arrId[0]);
		String id = arrId[1];
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String nombre = "";
		
		try {
			
			cnt.getConnection();
			
			String tableName = "";
			switch (tipoTercero) {
			case TIPO_USUARIO:
				tableName = "IUSERDATA";
				break;
			case TIPO_DEPARTAMENTO:
				tableName = "IUSERDEPTHDR";
				break;
			case TIPO_GRUPO:
				tableName = "IUSERGROUPHDR";
				break;
			default:
				throw new ISPACException("Tipo de tercero inválido: " + tipoTercero);
			}
			
			dbQuery = cnt.executeDbQuery("SELECT NOMBRE FROM " + tableName + " WHERE ID = '" + id + "'");
			if (dbQuery.next()) {
				nombre = dbQuery.getString("NOMBRE");
			}
			
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return nombre;
	}
	
	/**
	 * Devuelve el mail de un departamento
	 * @param nombreResp
	 * @return
	 * @throws ISPACException
	 */
	public String getEmailGrupo(String nombre) throws ISPACException{
		return getEmailResponsable("IUSERGROUPHDR", nombre);
	}
	
	/**
	 * Devuelve el mail de un departamento
	 * @param nombreResp
	 * @return
	 * @throws ISPACException
	 */
	public String getEmailDepartamento(String nombre) throws ISPACException{
		return getEmailResponsable("IUSERDEPTHDR", nombre);
	}
	
	/**
	 * 
	 * @return
	 * @throws ISPACException 
	 */
	private String getEmailResponsable(String tableName, String nombreResp) 
			throws ISPACException
	{
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String email = "";
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT EMAIL FROM " + tableName
					+ " WHERE NAME = '" + nombreResp + "'");
			while (dbQuery.next()) {
				email = dbQuery.getString("EMAIL");
			}
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return email;
	}
	
	
	/**
	 * Insertar el registro de entrada y el registro de salida en la tabla 'SCR_REGASOC'	
	 * @param identLibroRegisEnt
	 * @param asientoRegisEnt
	 * @param identLibroRegisSal
	 * @param asientoRegisSal
	 * @return
	 * @throws ISPACException
	 */
	public boolean setInsertRegistroAsociado(int identLibroRegisEnt, int asientoRegisEnt, int identLibroRegisSal, int asientoRegisSal) throws ISPACException{
		boolean correcto = false;
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		int id = 0;
		
		try {
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT MAX(ID) AS ID FROM SCR_REGASOC");
			while (dbQuery.next()) {
				id = dbQuery.getInt("ID");
			}
			String sql = "INSERT INTO SCR_REGASOC (ID, ID_ARCHPRIM, ID_FDRPRIM, ID_ARCHSEC, ID_FDRSEC) VALUES ("+(id+1)+", "+identLibroRegisEnt+", "+asientoRegisEnt+", "+identLibroRegisSal+", "+asientoRegisSal+")";
			correcto = cnt.execute(sql);
		}
		catch(ISPACException e){
			correcto=false;
			throw new ISPACException("Insercción en los registros asociados.", e);	
		}
		finally{
			cnt.closeConnection();
		}
		
		return correcto;
	}
	
	/**
	 * La funcion de este metodo es a partir de su id
	 * devuelve todos los grupos a los que pertenece
	 * 
	 * @param user
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector getGruposPerteneceUsuario (int user) throws ISPACException{
		Vector groupsUser = new Vector();
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT GROUPID FROM IUSERGROUPUSER WHERE USERID = " +	user + "");
			while (dbQuery.next()) {
				groupsUser.add(dbQuery.getInt("GROUPID"));
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return groupsUser;
	}
	
	/**
	 * Metodo que se le pasa un vector con todos los id de los grupos a los que
	 * pertenece un usuario y devuelve el nombre que le corresponde
	 * ese id accediendo en la tabla iusergrouphdr
	 * 
	 * @param idGroup
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector getNombreGrupo (Vector idGroup) throws ISPACException{
		Vector nombreGrupo = new Vector ();
		String sConsultaWhere = "";
		
		sConsultaWhere = "ID="+idGroup.get(0)+"";
		for(int i = 1; i< idGroup.size(); i++){
			sConsultaWhere += " OR ID = " + idGroup.get(i) + "";
		}
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT NAME, ID FROM IUSERGROUPHDR WHERE "+sConsultaWhere+"");
			while (dbQuery.next()) {
				nombreGrupo.add(dbQuery.getString("NAME") + "&" + dbQuery.getInt("ID"));
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return nombreGrupo;
	}
	
	//Ticket #232#SIGEM registro distribuido
	/**
	 * Devuelve el numero de expediente de un registro de entrada
	 * pasandole el numero de registro.
	 * **/
	public String getNumExpRegistro(String numRegistro)throws ISPACException{
		String numexpReg = "";
		
		String sConsultaWhere = "FLD1='"+numRegistro+"'";		
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			
			String sQuery = "SELECT FLD19 FROM A1SF WHERE " + sConsultaWhere;
			
			LOGGER.info("sQuery "+sQuery);
			
			dbQuery = cnt.executeDbQuery(sQuery);
			
			while (dbQuery.next()) {
				numexpReg = dbQuery.getString("FLD19");
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la busqueda del registro " + numRegistro + ". " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la busqueda del registro " + numRegistro + ". " + e.getMessage(), e);
			throw new ISPACException("Error en la busqueda del registro " + numRegistro + ". " + e.getMessage(), e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return numexpReg;		
	}
	
	/**
	 * 
	 * @param numRegistro
	 * @return
	 * @throws ISPACException
	 */
	public boolean modificaInteresadoRegistroEntrada(String nreg, String nombreInteresado) throws ISPACException{
		
		DbCnt cnt = new DbCnt(dsName);
		boolean bResult = false;
		
		try {
			cnt.getConnection();
			
			String sQuery = "UPDATE A1SF SET FLD9 = '" + nombreInteresado + "' WHERE FLD1 = '" + nreg + "'";
			LOGGER.info("sQuery "+sQuery);
			bResult = cnt.execute(sQuery);
			
			sQuery = "UPDATE SCR_REGINT SET NAME = '" + nombreInteresado + "' WHERE ID_FDR IN "
					+ "(SELECT FDRID FROM A1SF WHERE FLD1 = '" + nreg + "')";
			LOGGER.info("sQuery "+sQuery);
			bResult = cnt.execute(sQuery);
			
		} catch (ISPACException e) {
			LOGGER.error("Error al modificar el interesado del registro " + nreg + ". " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error al modificar el interesado del registro " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al modificar el interesado del registro " + nreg + ". " + e.getMessage(), e);
		} finally {
			cnt.closeConnection();
		}
		
		return bResult;		
	}
	
	/**
	 * La funcion de este metodo es apartir de su id
	 * devuelve todos los grupos a los que pertenece
	 *
	 */
	public int getDepartamentoPerteneceUsuario (int user) throws ISPACException{

		int resultado = 0;
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT DEPTID FROM IUSERUSERHDR WHERE ID = "+	user +"");
			while (dbQuery.next()) {
				resultado = dbQuery.getInt("DEPTID");
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return resultado;
	}

	/**
	 * 
	 * @param libro
	 * @param numReg
	 * @return
	 * @throws ISPACException
	 */
	public int getIdReg(int libro, String numReg) throws ISPACException {
		
		int resultado = 0;
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String tabla = "";
		
		try {
			
			cnt.getConnection();
			if(libro == 1){
				tabla = "A1SF";
			}
			else{
				tabla = "A2SF";
			}
			dbQuery = cnt.executeDbQuery("SELECT FDRID FROM "+tabla+" WHERE FLD1 = '"+	numReg +"'");
			while (dbQuery.next()) {
				resultado = dbQuery.getInt("FDRID");
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return resultado;
	}
	
	/**
	 * [eCenpri-Felipe #830]
	 * Recupera el campo del usuario de la BBDD
	 * @param nombreUsuario
	 * @return
	 * @throws ISPACException
	 */
	public String getCampoUsuario(String nombreUsuario, String nombreCampo) throws ISPACException{
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String campo = "";
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT " + nombreCampo + 
					" FROM IUSERUSERHDR WHERE NAME = '" + nombreUsuario + "'");
			while (dbQuery.next()) {
				campo = dbQuery.getString(nombreCampo);
			}
		} catch (Exception e) {
			throw new ISPACException
				("Error recuperar el campo " + nombreCampo + " del usuario " + nombreUsuario, e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return campo;
	}
	
	/**
	 * TODO [dipucr-Felipe]
	 * Existen las interfaces provistas por SIGEM UserImpl y UserDataImpl que cargan todos los campos
	 * Esto creo que dio problemas a Agustín a nivel de Maven, por el tema de ciclos entre proyectos
	 */
	/**
	 * [eCenpri-Felipe 3#231]
	 * Recupera el campo del usuario de la BBDD
	 * @param idUsuario
	 * @return
	 * @throws ISPACException
	 */
	public String getCampoUsuarioData(int idUsuario, String nombreCampo) throws ISPACException{
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String campo = "";
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT " + nombreCampo + 
					" FROM IUSERDATA WHERE ID = " + idUsuario);
			while (dbQuery.next()) {
				campo = dbQuery.getString(nombreCampo);
			}
		} catch (Exception e) {
			throw new ISPACException
				("Error recuperar el campo " + nombreCampo + " del usuario " + idUsuario, e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return campo;
	}
	
	
	/**
	 * La funcion de este metodo es apartir de un nombre de departamento recuperar su DPXX asociado de registro
	 *
	 */
	public String getCodeDepByNombre (String nombre) throws ISPACException{

		String resultado = "";
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT CODE FROM SCR_ORGS WHERE UPPER(NAME) = UPPER(" + nombre + ")");
			while (dbQuery.next()) {
				resultado = dbQuery.getString("CODE");
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda de departamentos. No se ha encontrado el departamento: '" + nombre + "'. " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos. No se ha encontrado el departamento: '" + nombre + "'. " + e.getMessage(), e);
			throw new ISPACException("Error en la búsqueda de departamentos. No se ha encontrado el departamento: '" + nombre + "'. " + e.getMessage(), e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return resultado;
	}
	
	/**
	 * La funcion de este metodo es apartir de un id de iserdepthdr
	 *  de departamento recuperar su DPXX asociado de registro (scr_orgs)
	 *
	 */
	public String getCodeDepByIdDep (int id) throws ISPACException{

		String resultado = "";
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT A.CODE FROM SCR_ORGS A, IUSERDEPTHDR B " +
					"WHERE ((UPPER(A.NAME) LIKE '%'||UPPER(B.NAME)||'%') OR (UPPER(B.NAME) LIKE '%'||UPPER(A.NAME)||'%')) " +
					"AND A.CODE LIKE 'DP%' AND B.ID=" + id );
			while (dbQuery.next()) {
				resultado = dbQuery.getString("CODE");
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda de departamentos. No se ha encontrado el departamento con id " + id + ". " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos. No se ha encontrado el departamento con id " + id + ". " + e.getMessage(), e);
			throw new ISPACException("Error en la búsqueda de departamentos. No se ha encontrado el departamento con id " + id + ". " + e.getMessage(), e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return resultado;
	}
	
	protected IThirdPartyAdapter getIThirdPartyAdapter(Tercero adapter) {
		ThirdPartyAdapter tercero = null;
		
		if (adapter != null) {
			tercero = new ThirdPartyAdapter();
			tercero.setIdExt(adapter.getIdExt());
			tercero.setTipoPersona(adapter.getTipoPersona());
			tercero.setIdentificacion(adapter.getIdentificacion());
			tercero.setNombre(adapter.getNombre());
			tercero.setPrimerApellido(adapter.getPrimerApellido());
			tercero.setSegundoApellido(adapter.getSegundoApellido());
			tercero.setNotificacionTelematica(adapter.isNotificacionTelematica());
			tercero.setDireccionesPostales(getDireccionesPostales(adapter.getDireccionesPostales()));
			tercero.setDireccionesElectronicas(getDireccionesElectronicas(adapter.getDireccionesElectronicas()));
		}
		
		return tercero;
	}
	
	protected IPostalAddressAdapter[] getDireccionesPostales(DireccionPostal[] adapters) {
		IPostalAddressAdapter[] dirs = null;
		
		if (adapters != null) {
			dirs = new PostalAddressAdapter[adapters.length];
			for (int i = 0; i < dirs.length; i++) {
				dirs[i] = getDireccionPostal(adapters[i]);
			}
		}
		
		return dirs;
	}
	
	protected IPostalAddressAdapter getDireccionPostal(DireccionPostal adapter) {
		PostalAddressAdapter dir = null;
		
		if (adapter != null) {
			dir = new PostalAddressAdapter();
			dir.setId(adapter.getId());
			dir.setDireccionPostal(adapter.getDireccionPostal());
			dir.setTipoVia(adapter.getTipoVia());
			dir.setVia(adapter.getVia());
			dir.setBloque(adapter.getBloque());
			dir.setPiso(adapter.getPiso());
			dir.setPuerta(adapter.getPuerta());
			dir.setCodigoPostal(adapter.getCodigoPostal());
			dir.setPoblacion(adapter.getPoblacion());
			dir.setMunicipio(adapter.getMunicipio());
			dir.setProvincia(adapter.getProvincia());
			dir.setComunidadAutonoma(adapter.getComunidadAutonoma());
			dir.setPais(adapter.getPais());
			dir.setTelefono(adapter.getTelefono());
		}

		return dir;
	}
	
	protected IElectronicAddressAdapter[] getDireccionesElectronicas(DireccionElectronica[] adapters) {
		IElectronicAddressAdapter[] dirs = null;
		
		if (adapters != null) {
			dirs = new ElectronicAddressAdapter[adapters.length];
			for (int i = 0; i < dirs.length; i++) {
				dirs[i] = getDireccionElectronica(adapters[i]);
			}
		}
		
		return dirs;
	}

	protected IElectronicAddressAdapter getDireccionElectronica(DireccionElectronica adapter) {
		ElectronicAddressAdapter dir = null;
		
		if (adapter != null) {
			dir = new ElectronicAddressAdapter();
			dir.setId(adapter.getId());
			dir.setTipo(adapter.getTipo());
			dir.setDireccion(adapter.getDireccion());
		}
		
		return dir; 
	}
	
	/**
	 * [dipucr-Felipe #791bis-3]
	 * @param nombreResp
	 * @return
	 * @throws ISPACException
	 */
	public String getUserNameByNif(String dni) throws ISPACException{
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String userName = "";
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT * FROM IUSERUSERHDR WHERE ID IN "
					+ "(SELECT ID FROM IUSERDATA WHERE DNI = '" + dni + "') ORDER BY ID DESC");
			if (dbQuery.next()) {
				userName = dbQuery.getString("NAME");
			}
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda de departamentos", e);
			throw new ISPACException("Error en la búsqueda de departamentos", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return userName;
	}
}

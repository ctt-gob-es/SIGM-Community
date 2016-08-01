package ieci.tdw.ispac.ispaclib.dao.cat;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

public class PManualUsuarioDAO extends ObjectDAO {

	private static final long serialVersionUID = 1L;
	
	public static final String TABLENAME = "SPAC_P_MANUALES_USUARIO";
	public static final String IDOBJ = "ID_OBJ";
	public static final String TPOBJ = "TP_OBJ";
	public static final String IDMANUAL = "ID_CT_MANUAL_USUARIO";

	/**
	 * Constructor vacio
	 * @throws ISPACException
	 */
	public PManualUsuarioDAO() throws ISPACException {
		super(null);
	}

	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public PManualUsuarioDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}
	
	/**
	 * Contructor que carga una determinada plantilla
	 * @param cnt conexion
	 * @param id identificador de la plantilla
	 * @throws ISPACException
	 */
	 public PManualUsuarioDAO(DbCnt cnt, int id) throws ISPACException {		
	 	super(cnt,id,null);
	 }	

	/**
	 * Devuelve la query por defecto
	 * @return query
	 * @throws ISPACException 
	 */
	protected String getDefaultSQL(int nActionDAO) throws ISPACException {
		return " WHERE " + IDMANUAL + " = " + getInt(IDMANUAL) 
			+ " AND " + TPOBJ + "=" + getInt(TPOBJ) 
			+ " AND " + IDOBJ + "=" + getInt(IDOBJ);
	}

	/**
	 * Devuelve el nombre de la tabla
	 * @return Nombre de la tabla
	 * @throws ISPACException 
	 */
	public String getTableName() {
		return TABLENAME;
	}

	/**
	 * Devuelve el nombre del campo clave primaria
	 * 
	 * @return nombre de la clave primaria
	 * @throws ISPACException
	 */
	public String getKeyName() throws ISPACException {
		return null;
	}

	/**
	 * Para crear un nuevo registro
	 * 
	 * @param cnt
	 *            conexion
	 * @throws ISPACException
	 */
	public void newObject(DbCnt cnt) throws ISPACException {
	}

	/**
	 * Elimina las relaciones del informe con el objeto indicado.
	 * @param cnt Conexión a base de datos.
	 * @param objectType Tipo de objeto.
	 * @param objectId Identificador del objeto.
	 * @param reportId Identificador del informe. 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static void delete(DbCnt cnt, int objectType, int objectId, int reportId) 
			throws ISPACException {
		String sQuery = "DELETE FROM " + TABLENAME + " WHERE " + IDMANUAL + "="
				+ reportId + " AND " + TPOBJ + "=" + objectType + " AND "
				+ IDOBJ + "=" + objectId;
		cnt.directExec(sQuery);
	}

	/**
	 * Elimina todas las relaciones del informe.
	 * @param cnt Conexión a base de datos.
	 * @param reportId Identificador del informe. 
	 * @throws ISPACException si ocurre algún error.
	 */
	public static void delete(DbCnt cnt, int reportId) throws ISPACException {
		String sQuery = "DELETE FROM " + TABLENAME + " WHERE " + IDMANUAL + "=" + reportId ;
		cnt.directExec(sQuery);
	}
	
	/**
	 * Obtiene la lista de manuales de usuario asociadas a un procedimiento
	 * @param cnt
	 * @param pcdId Identificador del procedimiento
	 * @return
	 * @throws ISPACException
	 */
	public static CollectionDAO getProcedureManualesUsuario(DbCnt cnt,int pcdId)
	throws ISPACException
	{
		String sql = "WHERE " + TPOBJ + " = " + EventsDefines.EVENT_OBJ_PROCEDURE + " AND " + IDOBJ + " = " + pcdId;

		CollectionDAO collection = new CollectionDAO(PManualUsuarioDAO.class);
		collection.query(cnt,sql);
		return collection;
	}
}

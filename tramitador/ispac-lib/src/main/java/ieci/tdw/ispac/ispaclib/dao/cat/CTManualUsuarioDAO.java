package ieci.tdw.ispac.ispaclib.dao.cat;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class CTManualUsuarioDAO extends ObjectDAO
{
	private static final long serialVersionUID = 1L;
	
	public static final String TABLENAME 	= "SPAC_CT_MANUALES_USUARIO";
	static final String IDSEQUENCE 	= "SPAC_SQ_ID_CTMANUALESUSUARIO";
	static final String IDKEY 		= "ID";
	
	public static final int GENERIC_TYPE = 1;
	public static final int SPECIFIC_TYPE = 2;
	public static final int GLOBAL_TYPE=3;
	public static final int SEARCH_TYPE=4;
		
	//private final static int CHUNK_SIZE = 10240;

	/**
	 * Constructor vacio
	 * @throws ISPACException
	 */
	public CTManualUsuarioDAO() throws ISPACException {
		super(null);
	}
	
	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public CTManualUsuarioDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}
  
	/**
	 * Contructor que carga una determinada plantilla
	 * @param cnt conexion
	 * @param id identificador de la plantilla
	 * @throws ISPACException
	 */
	 public CTManualUsuarioDAO(DbCnt cnt, int id) throws ISPACException {		
	 	super(cnt,id,null);
	 }	
	
	/**
	 * Devuelve la query por defecto
	 * @return query
	 * @throws ISPACException 
	 */
	 protected String getDefaultSQL(int nActionDAO) throws ISPACException
	 {
	 	return " WHERE " + IDKEY + " = " + getInt(IDKEY);
	 }
  
	 /**
	  * Devuelve el nombre del campo clave primaria
	  * @return nombre de la clave primaria
	  * @throws ISPACException 
	  */
	 public String getKeyName() throws ISPACException
	 {
	 	return IDKEY;
	 }
  
	 /**
	  * Devuelve el nombre de la tabla
	  * @return Nombre de la tabla
	  * @throws ISPACException 
	  */
	 public String getTableName()
	 {
	 	return TABLENAME;
	 }
  
	 /**
	  * Para crear un nuevo registro con identificador proporcionado
	  * por la secuencia correspondiente
	  * @param cnt conexion
	  * @throws ISPACException
	  */
	 public void newObject(DbCnt cnt) throws ISPACException
	 {
	 	set(IDKEY,IdSequenceMgr.getIdSequence(cnt,IDSEQUENCE));
	 }	
  
	 /**
	  * Devuelve la plantilla de nombre name que esta asociada al tipo de documento
	  * con identificador idTpDoc
	  * @param cnt conexion
	  * @param name nombre de la plantilla
	  * @param idTpDoc identificador del tipo de documento
	  * @param expresion expresión
	  * @return la plantilla
	  * @throws ISPACException
	  */
	 public static CTManualUsuarioDAO getManualUsuario(DbCnt cnt, String name) throws ISPACException{
	 	CTManualUsuarioDAO manualUsuario = null;
	 	
	 	CollectionDAO collection = new CollectionDAO (CTManualUsuarioDAO.class);
	 	collection.query (cnt, "WHERE NOMBRE = '" + DBUtil.replaceQuotes(name) + "'");
	 	if (collection.next())
	 	{
	 		manualUsuario = (CTManualUsuarioDAO) collection.value();
	 	}
	 	
	 	return manualUsuario;
	 }
	 
	 /**
	  * Devuelve la plantilla de nombre name que esta asociada al tipo de documento
	  * con identificador idTpDoc
	  * @param cnt conexion
	  * @param name nombre de la plantilla
	  * @param idTpDoc identificador del tipo de documento
	  * @param expresion expresión
	  * @return la plantilla
	  * @throws ISPACException
	  */
	 public static CTManualUsuarioDAO getManualUsuario(DbCnt cnt, String name, String version) throws ISPACException{
	 	CTManualUsuarioDAO manualUsuario = null;
	 	
	 	CollectionDAO collection = new CollectionDAO (CTManualUsuarioDAO.class);
	 	collection.query (cnt, "WHERE NOMBRE = '" + DBUtil.replaceQuotes(name) + "' AND VERSION = '" + version + "'");
	 	if (collection.next())
	 	{
	 		manualUsuario = (CTManualUsuarioDAO) collection.value();
	 	}
	 	
	 	return manualUsuario;
	 }
 
	 
	 /**
	  * Borra la plantilla y los los enventos asociados a esta
	  */
	 public void delete(DbCnt cnt) throws ISPACException
	 {
	 	String sQuery = "DELETE FROM spac_p_blp_manuales_usuario WHERE id = " + getString ("ID");
 		cnt.directExec(sQuery);
		super.delete(cnt);
	 }
	
	 public static void delete(DbCnt cnt, int id) throws ISPACException
	 {
	 	String sQuery = "DELETE FROM spac_p_blp_manuales_usuario WHERE id = " + id;
 		cnt.directExec(sQuery);
	 	sQuery = "DELETE FROM " + CTManualUsuarioDAO.TABLENAME + " WHERE id = " + id;
 		cnt.directExec(sQuery);
	 }
	 
	/**
	 * Obtiene la colección de plantillas asociadas a un tipo de documento
	 * @param cnt manejador de la conexión
	 * @param documentId tipo de documento 
	 * @return una colección de objetos CTManualUsuarioDAO
	 * @throws ISPACException
	 */
	public static IItemCollection getDocumentManualUsuarios(DbCnt cnt, int id)
	throws ISPACException
	{
		String sql = "WHERE ID = " + id + " ORDER BY NOMBRE ";
			
		CollectionDAO collection = new CollectionDAO(CTManualUsuarioDAO.class);
		collection.query(cnt,sql);
		return collection.disconnect();
	}
	
	/**
	 * Obtiene el contenido de la plantilla
	 * @param cnt conexion con base de datos
	 * @param stream donde se vuelca el archivo
	 * @throws ISPACException
	 */
	public void getManualUsuario(DbCnt cnt, OutputStream stream) 
	throws ISPACException
	{
		ManualUsuarioDataDAO.getBLOB(cnt, this.getKeyInt(), stream);
	}

	/**
	 * Obtiene el contenido de la plantilla
	 * @param cnt conexion con base de datos
	 * @param id identificador de la plantilla
	 * @param stream donde se vuelca el archivo
	 * @throws ISPACException
	 */
	public static void getManualUsuario(DbCnt cnt, int id, OutputStream stream) 
	throws ISPACException
	{
		ManualUsuarioDataDAO.getBLOB(cnt, id, stream);
	}

	/**
	 * Cambia el contenido de la plantilla
	 * @param cnt conexion con base de datos
	 * @param stream origen de datos
	 * @param mimetype mimetype del archivo
	 * @return devuelve el numero de bytes del archivo
	 * @throws ISPACException
	 */
	public void setManualUsuario(DbCnt cnt, InputStream stream, int length, String mimetype)
	throws ISPACException
	{
		ManualUsuarioDataDAO.setBLOB(cnt, this.getKeyInt(), stream, length, mimetype);
		set("FECHA", new Date(System.currentTimeMillis()));
		store( cnt);
	}

	/**
	 * Cambia el contenido de la plantilla
	 * @param cnt conexion con base de datos
	 * @param id identificador de la plantilla
	 * @param stream origen de datos
	 * @param mimetype mimetype del archivo
	 * @return devuelve el numero de bytes del archivo
	 * @throws ISPACException
	 */
	public static void setManualUsuario (DbCnt cnt, int id, InputStream stream, int length, String mimetype)
	throws ISPACException
	{
		CTManualUsuarioDAO ManualUsuario = new CTManualUsuarioDAO(cnt, id);
		ManualUsuario.set("FECHA", new Date(System.currentTimeMillis()));
		ManualUsuario.store(cnt);
		ManualUsuarioDataDAO.setBLOB(cnt, id, stream, length, mimetype);
	}
}

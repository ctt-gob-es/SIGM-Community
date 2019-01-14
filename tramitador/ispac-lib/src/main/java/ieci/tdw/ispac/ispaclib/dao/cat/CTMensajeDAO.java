package ieci.tdw.ispac.ispaclib.dao.cat;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.util.Date;

public class CTMensajeDAO extends ObjectDAO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String TABLENAME = "SPAC_S_SESION_MENSAJE";
	public static final String IDSEQUENCE = "SPAC_SQ_S_SESION_MENSAJE";
	public static final String IDKEY = "ID";

	//Mapeo de las columnas de la tabla	
	public static final String ID_SESION = "ID_SESION";
	public static final String SOLO_CONECTADOS = "SOLO_CONECTADOS";
	public static final String TEXTO = "TEXTO";
	public static final String USUARIO = "USUARIO";
	public static final String ID_USUARIO = "ID_USUARIO";
	public static final String FECHA_MENSAJE = "FECHA_MENSAJE";
	public static final String TIPO = "TIPO";
	public static final String MASIVO = "MASIVO";
	public static final String PASSADM = "PASSADM";

	/**
	 * Constructor vacio
	 * @throws ISPACException
	 */
	public CTMensajeDAO() throws ISPACException {
		super(null);
	}
	
	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public CTMensajeDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}
  
	/**
	 * Contructor que carga una determinada plantilla
	 * @param cnt conexion
	 * @param id identificador de la plantilla
	 * @throws ISPACException
	 */
	 public CTMensajeDAO(DbCnt cnt, int id) throws ISPACException {		
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
	  * Borra todos los mensaje
	  */
	 public void deleteAll(DbCnt cnt) throws ISPACException {
	 	String sQuery = "DELETE FROM " + CTMensajeDAO.TABLENAME;
 		cnt.directExec(sQuery);
		super.delete(cnt);
	 }
	 
	 /**
	  * Borra el mensaje
	  * @param id del mensaje a borrar
	  */	
	 public static void delete(DbCnt cnt, int id) throws ISPACException {
	 	String sQuery = "DELETE FROM " + CTMensajeDAO.TABLENAME + " WHERE " + CTMensajeDAO.IDKEY + " = " + id;
 		cnt.directExec(sQuery);
	 }

	public static void setMensaje(DbCnt cnt, int id, String idSesion, String texto, String usuario, String tipo, Date fechaMensaje) throws ISPACException {
		
		CTMensajeDAO mensaje = new CTMensajeDAO(cnt, id);		
		mensaje.set(CTMensajeDAO.ID_SESION, idSesion);
		mensaje.set(CTMensajeDAO.TEXTO, texto);
		mensaje.set(CTMensajeDAO.USUARIO, usuario);
		mensaje.set(CTMensajeDAO.TIPO, tipo);
		mensaje.set(CTMensajeDAO.FECHA_MENSAJE, fechaMensaje);

		mensaje.store(cnt);
	}
}

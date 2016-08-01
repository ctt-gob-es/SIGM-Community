package ieci.tdw.ispac.ispaclib.dao.session;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author [eCenpri-Felipe #871]
 * @since 24.04.2013
 * Bloqueos para evitar la firma simultánea de decretos
 */
public class BloqueosFirmaDocsDAO extends ObjectDAO
{
	private static final long serialVersionUID = 1L;

	static final String TABLENAME 	= "SPAC_BLOQUEOS_FIRMA_DOCS";
	static final String IDSEQUENCE 	= "SPAC_SQ_ID_BLOQUEOSFIRMADOCS";
	static final String IDKEY = "ID";

	/**
	 * @throws ISPACException
	 */
	public BloqueosFirmaDocsDAO() throws ISPACException	{
		super(null);
	}
	
	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public BloqueosFirmaDocsDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}
	
	/**
	 * @throws ISPACException
	 */
	public BloqueosFirmaDocsDAO(DbCnt cnt, int id) throws ISPACException {
		 super(cnt,id,null);
	}

	public String getTableName()
	{
		return TABLENAME;
	}

	protected String getDefaultSQL(int nActionDAO) throws ISPACException
    {
	    return " WHERE " + IDKEY + " = " + getInt(IDKEY);
    }

	public String getKeyName() throws ISPACException
	{
	    return IDKEY;
	}

	public void newObject(DbCnt cnt) throws ISPACException
	{
	    set(IDKEY,IdSequenceMgr.getIdSequence(cnt,IDSEQUENCE));
	}
	
	/**
	 * Devuelve el bloqueo para un tipo de documento
	 * @param cnt
	 * @param tipoDoc
	 * @return
	 * @throws ISPACException
	 */
	public static CollectionDAO getBloqueoFirmas(DbCnt cnt, String tipoDoc, int estado) throws ISPACException {
		
	    String sql = "WHERE TIPO_DOC = '" + tipoDoc + "' AND ESTADO = " + estado;
		CollectionDAO objlist = new CollectionDAO(BloqueosFirmaDocsDAO.class);
		objlist.query(cnt,sql);
		
		return objlist;
	}
	
	/**
	 * Inserción de un registro en la tabla
	 * @param cnt
	 * @param tipoDoc
	 * @param usuario
	 * @param fecha
	 * @param estado
	 * @throws ISPACException
	 */
	public static void insert (DbCnt cnt, String tipoDoc, String usuario, Date fecha, int estado)
			throws ISPACException
	{
		PreparedStatement ps=null;
		boolean bTX = false;
		boolean creadorTX = false;

		try
		{
			if (cnt.ongoingTX())
				creadorTX = false;
			else{
				creadorTX = true;
			}
			cnt.openTX();

			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("INSERT INTO " + TABLENAME + " (ID, TIPO_DOC, USUARIO, LAST_UPDATE, ESTADO) VALUES(");
			sBuffer.append(IdSequenceMgr.nextVal(cnt, IDSEQUENCE));
			sBuffer.append(", '" + tipoDoc + "'");
			sBuffer.append(", '" + usuario + "'");
			//[Manu] Se modifica esta llamada para desacoplar ispac-lib de DipucrRules
			sBuffer.append(", '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(fecha));
//			sBuffer.append(", '" + FechasUtil.getFormattedDateTimeForQuery(fecha) + "'");
			
			sBuffer.append(", " + estado + ")");

			ps=cnt.prepareStatement(sBuffer.toString());
			ps.execute();

			bTX = true;
		}
		catch (Exception e)
		{
			bTX = false;
			throw new ISPACException(e);
		}
		finally
		{
			try
			{
				if (ps != null)
				    ps.close();
				if (cnt.ongoingTX() && creadorTX==true)
				    cnt.closeTX( bTX);
			}
			catch (Exception e)
			{}
		}
	}
	
	/**
	 * Inserción de un registro en la tabla
	 * @param cnt
	 * @param tipoDoc
	 * @param usuario
	 * @param fecha
	 * @throws ISPACException
	 */
	public static void update (DbCnt cnt, String tipoDoc, String usuario, Date fecha, int estado)
			throws ISPACException
	{
		PreparedStatement ps=null;
		boolean bTX = false;
		boolean creadorTX = false;

		try
		{
			if (cnt.ongoingTX())
				creadorTX = false;
			else{
				creadorTX = true;
			}
			cnt.openTX();

			StringBuffer sBuffer = new StringBuffer();

//			sBuffer.append("UPDATE " + TABLENAME + " SET");
//			sBuffer.append(" USUARIO = '" + usuario + "',");
//			sBuffer.append(" LAST_UPDATE = '" + FechasUtil.getFormattedDateTimeForQuery(fecha) + "',");
//			sBuffer.append(" ESTADO = " + estado);
//			sBuffer.append(" WHERE TIPO_DOC = '" + tipoDoc + "'");
			
			sBuffer.append("UPDATE " + TABLENAME + " SET");
			sBuffer.append(" USUARIO = ?,");
			sBuffer.append(" LAST_UPDATE = ?,");
			sBuffer.append(" ESTADO = ? ");
			sBuffer.append(" WHERE TIPO_DOC = ?");

			ps=cnt.prepareStatement(sBuffer.toString());
			ps.setString(1, usuario);
			ps.setTimestamp(2,  new java.sql.Timestamp(fecha.getTime()));
			ps.setInt(3, estado);
			ps.setString(4, tipoDoc);
			ps.execute();

			bTX = true;
		}
		catch (Exception e)
		{
			bTX = false;
			throw new ISPACException(e);
		}
		finally
		{
			try
			{
				if (ps != null)
				    ps.close();
				if (cnt.ongoingTX() && creadorTX==true)
				    cnt.closeTX( bTX);
			}
			catch (Exception e)
			{}
		}
	}
	
	
	/**
	 * Borrado
	 * @param cnt
	 * @param tipoDoc
	 * @throws ISPACException
	 */
	public static void delete(DbCnt cnt, String tipoDoc) throws ISPACException{
		
		PreparedStatement ps=null;
		boolean bTX = false;
		boolean creadorTX = false;

		try
		{
			if (cnt.ongoingTX())
				creadorTX = false;
			else{
				creadorTX = true;
			}
			cnt.openTX();

			StringBuffer sBuffer = new StringBuffer();

			sBuffer.append("DELETE FROM " + TABLENAME + " WHERE TIPO_DOC = '");
			sBuffer.append(tipoDoc);
			sBuffer.append("'");
			cnt.directExec(sBuffer.toString());

			ps=cnt.prepareStatement(sBuffer.toString());
			ps.execute();

			bTX = true;
		}
		catch (Exception e)
		{
			bTX = false;
			throw new ISPACException(e);
		}
		finally
		{
			try
			{
				if (ps != null)
				    ps.close();
				if (cnt.ongoingTX() && creadorTX==true)
				    cnt.closeTX( bTX);
			}
			catch (Exception e)
			{}
		}
	}
}
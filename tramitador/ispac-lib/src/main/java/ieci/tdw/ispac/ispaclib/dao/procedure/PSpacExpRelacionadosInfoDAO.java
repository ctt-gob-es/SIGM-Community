/*
 * Created on 05-jun-2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ieci.tdw.ispac.ispaclib.dao.procedure;

import java.sql.PreparedStatement;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.dao.ActionDAO;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

/**
 * @author [eCenpri-Teresa #604]
 *
 *         Información sobre los expedientes relacionados de interés
 */
@SuppressWarnings("serial")
public class PSpacExpRelacionadosInfoDAO extends ObjectDAO {
	static final String TABLENAME = "SPAC_EXP_RELACIONADOS_INFO";
	static final String IDSEQUENCE = "SPAC_SQ_EXP_RELACIONADOS_INFO";
	static final String IDKEY = "ID";


	/**
	 * @throws ISPACException
	 */
	public PSpacExpRelacionadosInfoDAO() throws ISPACException {
		super(null);
	}

	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public PSpacExpRelacionadosInfoDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}

	/**
	 * @throws ISPACException
	 */
	public PSpacExpRelacionadosInfoDAO(DbCnt cnt, int id) throws ISPACException {
		super(cnt, id, null);
	}

	public String getTableName() {
		return TABLENAME;
	}

	public void load(DbCnt cnt) throws ISPACException {
		load(cnt, getDefaultSQL(ActionDAO.LOAD));
	}

	protected String getDefaultSQL(int nActionDAO) throws ISPACException {
		return " WHERE " + IDKEY + " = " + getInt(IDKEY);
	}

	public String getKeyName() throws ISPACException {
		return IDKEY;
	}
	
	public int getId() throws ISPACException {
		return getInt(IDKEY);
	}

	public String getNumexpPadre() throws ISPACException {
		return getString("NUMEXP_PADRE");
	}

	public String getNumexpHijo() throws ISPACException {
		return getString("NUMEXP_HIJO");
	}

	public int getIdTramitePadre() throws ISPACException {
		return getInt("ID_TRAMITE_PADRE");
	}

	public int getIdTramiteHijo() throws ISPACException {
		return getInt("ID_TRAMITE_HIJO");
	}

	public String getObservaciones() throws ISPACException {
		return getString("OBSERVACIONES");
	}

	public void newObject(DbCnt cnt) throws ISPACException {
		set(IDKEY, IdSequenceMgr.getIdSequence(cnt, IDSEQUENCE));
	}

	public static PSpacExpRelacionadosInfoDAO getInfoSpacExpRelacionaPadreHijo(DbCnt cnt, String numexp_padre, String numexp_hijo) throws ISPACException {
		String sql = "WHERE NUMEXP_PADRE='"+numexp_padre+"' AND NUMEXP_HIJO='"+numexp_hijo+"'";

		CollectionDAO objlist = new CollectionDAO(PSpacExpRelacionadosInfoDAO.class);
		objlist.query(cnt, sql);
		if (objlist.next())
			return (PSpacExpRelacionadosInfoDAO) objlist.value();
		return null;
	}
	
	public static IItem getInfoSpacExpRelacionaPadreIdTramitePadre(DbCnt cnt, String numexp_padre, int id_tramite_padre) throws ISPACException {
		String sql = "WHERE NUMEXP_PADRE='"+numexp_padre+"' AND ID_TRAMITE_PADRE="+id_tramite_padre+"";

		CollectionDAO objlist = new CollectionDAO(PSpacExpRelacionadosInfoDAO.class);
		objlist.query(cnt, sql);
		if (objlist.next())
			return (IItem) objlist.value();
		return null;
	}
	
	public static IItem getInfoSpacExpRelacionaHijoIdTramiteHijo(DbCnt cnt, String numexp_hijo,int id_tramite_hijo) throws ISPACException {
		String sql = "WHERE NUMEXP_HIJO='"+numexp_hijo+"' AND ID_TRAMITE_HIJO="+id_tramite_hijo+"";

		CollectionDAO objlist = new CollectionDAO(PSpacExpRelacionadosInfoDAO.class);
		objlist.query(cnt, sql);
		if (objlist.next())
			return (IItem) objlist.value();
		return null;
	}
	
	public static CollectionDAO getInfoSpacExpRelacionaPadre(DbCnt cnt, String numexp_padre) throws ISPACException {
		String sql = "WHERE NUMEXP_PADRE='"+numexp_padre+"'";

		CollectionDAO objlist = new CollectionDAO(PSpacExpRelacionadosInfoDAO.class);
		objlist.query(cnt, sql);
		return objlist;
	}
	
	public static CollectionDAO getInfoSpacExpRelacionaHijo(DbCnt cnt, String numexp_hijo) throws ISPACException {
		String sql = "WHERE NUMEXP_HIJO='"+numexp_hijo+"'";

		CollectionDAO objlist = new CollectionDAO(PSpacExpRelacionadosInfoDAO.class);
		objlist.query(cnt, sql);
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
	public static void insert (DbCnt cnt, String numexp_padre, String numexp_hijo, int id_tramite_padre, int id_tramite_hijo, String observaciones)
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
			sBuffer.append("INSERT INTO " + TABLENAME + " (ID, NUMEXP_PADRE, NUMEXP_HIJO, ID_TRAMITE_PADRE, ID_TRAMITE_HIJO, OBSERVACIONES) VALUES(");
			sBuffer.append(IdSequenceMgr.nextVal(cnt, IDSEQUENCE));
			sBuffer.append(", '" + numexp_padre + "'");
			sBuffer.append(", '" + numexp_hijo + "'");
			sBuffer.append(", " + id_tramite_padre + "");
			sBuffer.append(", " + id_tramite_hijo + "");
			sBuffer.append(", '" + observaciones + "')");

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
	 * Elimina la dependencia.
	 * @param cnt Conexión a la base de datos.
	 * @param id Identificador de la dependencia
	 * @throws ISPACException si ocurre algún error.
	 */
	public static void delete(DbCnt cnt, int id) throws ISPACException {
		String sQuery = "DELETE FROM " + TABLENAME + " WHERE id = " + id;
		cnt.directExec(sQuery);
	}

	
}
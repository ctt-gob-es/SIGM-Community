/*
 * Created on 03-may-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ieci.tdw.ispac.ispaclib.dao.historico;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;


/**
 * @author juanin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TXHitoHDAO extends ObjectDAO
{
	static final String TABLENAME 	= "SPAC_HITOS_H";
	static final String IDSEQUENCE 	= "SPAC_SQ_ID_HITOS_H";
	static final String IDKEY 		= "ID";
	
	/**
	 * @throws ISPACException
	 */
	public TXHitoHDAO() throws ISPACException {
		super(null);
	}
	
	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public TXHitoHDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}
	
	/**
	 * @param cnt
	 * @param id
	 * @throws ISPACException
	 */
	public TXHitoHDAO(DbCnt cnt, int id) throws ISPACException {
		super(cnt, id, null);
	}

	public String getTableName()
	{
		return TABLENAME;
	}

	/* (non-Javadoc)
	 * @see ieci.tdw.ispac.ispactx.dao.ObjectDAO#newObject(int)
	 */
	protected void newObject(DbCnt cnt)
		throws ISPACException
	{
		set(IDKEY,IdSequenceMgr.getIdSequence(cnt,IDSEQUENCE));
	}


	/* (non-Javadoc)
	 * @see ieci.tdw.ispac.ispactx.dao.ObjectDAO#getDefaultSQL(int)
	 */
	protected String getDefaultSQL(int nActionDAO) throws ISPACException
	{
		return " WHERE " + IDKEY + " = " + getInt(IDKEY);
	}

	public String getKeyName() throws ISPACException
	{
		return IDKEY;
	}

	public static CollectionDAO getMilestones(DbCnt cnt,int nIdProc)
	throws ISPACException
	{
		String sql="WHERE ID_EXP = "+nIdProc+
					" ORDER BY ID ASC";

		CollectionDAO objlist=new CollectionDAO(TXHitoHDAO.class);
		objlist.query(cnt,sql);
		return objlist;
	}

	public static CollectionDAO getMilestones(DbCnt cnt,String numexp)
	throws ISPACException
	{
		String sql="WHERE ID_EXP IN ( SELECT ID FROM SPAC_PROCESOS WHERE NUMEXP = '" + DBUtil.replaceQuotes(numexp) + "' ) ORDER BY ID ASC";

		CollectionDAO objlist=new CollectionDAO(TXHitoHDAO.class);
		objlist.query(cnt,sql);
		return objlist;
	}

	public static CollectionDAO getMilestones(DbCnt cnt, int nIdProc,String query) throws ISPACException{
		String sql="WHERE ID_EXP = "+nIdProc+" AND "+ query + " ORDER BY ID DESC";

		CollectionDAO objlist=new CollectionDAO(TXHitoHDAO.class);
		objlist.query(cnt,sql);
		return objlist;
	}
}

/*
 * [eCenpri-Felipe #436] 29.09.11
 * Clase que recupera las transacciones de firma de un documento
 */
package ieci.tdw.ispac.ispaclib.dao.sign;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

public class SignTransactionDAO extends ObjectDAO
{
	static final String TABLENAME = "SPAC_ID_TRANSACCION_FIRMA";
	static final String IDSEQUENCE = "SPAC_SQ_ID_TRANSACCIONFIRMA";
	static final String IDKEY = "ID";

	/**
	 * 
	 * @throws ISPACException
	 */
	public SignTransactionDAO() throws ISPACException	{
		super(null);
	}

	/**
	 * 
	 * @param cnt
	 * @throws ISPACException
	 */
	public SignTransactionDAO(DbCnt cnt) throws ISPACException	{
		super(cnt, null);
	}

	/**
	 * 
	 * @param cnt
	 * @param id
	 * @throws ISPACException
	 */
	public SignTransactionDAO(DbCnt cnt, int id) throws ISPACException	{
		super(cnt, id, null);
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
	 * Devuelve las transacciones de firma de un documento
	 * @param cnt
	 * @param documentId
	 * @return
	 * @throws ISPACException
	 */
	public static CollectionDAO getTransactionsByDocument(DbCnt cnt, int documentId) throws ISPACException {
		
	    String sql="WHERE ID_DOCUMENTO = " + documentId;
		CollectionDAO objlist=new CollectionDAO(SignTransactionDAO.class);
		objlist.query(cnt,sql);
		
		return objlist;
	}
}

/*
 * Created on 05-jun-2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ieci.tdw.ispac.ispaclib.dao.procedure;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;


/**
 * @author [eCenpri-Manu #909]
 *
 * Para los circuitos de firma asociados a un procedimiento
 */
public class PTramDatosEspecificosDAO extends ObjectDAO
{
	static final String TABLENAME 	= "SPAC_P_TRAM_DATOSESPECIFICOS";
	static final String IDSEQUENCE 	= "SPAC_SQ_ID_PTRAM_DATOSESPEC";
	static final String IDKEY = "ID";

	/**
	 * @throws ISPACException
	 */
	public PTramDatosEspecificosDAO() throws ISPACException	{
		super(null);
	}
	
	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public PTramDatosEspecificosDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}
	
	/**
	 * @throws ISPACException
	 */
	public PTramDatosEspecificosDAO(DbCnt cnt, int id) throws ISPACException {
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
}
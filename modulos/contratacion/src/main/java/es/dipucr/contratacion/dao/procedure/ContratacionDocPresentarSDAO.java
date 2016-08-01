package es.dipucr.contratacion.dao.procedure;



import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.idsequences.IdSequenceMgr;
import ieci.tdw.ispac.ispaclib.db.DbCnt;


public class ContratacionDocPresentarSDAO extends ObjectDAO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final String TABLENAME 	= "CONTRATACION_DOC_PRESENTAR_S";
	//Para calcular el id de la secuencia Math.abs("CONTRATACION_DOC_PRESENTAR_S".hashCode())
	static final String IDSEQUENCE 	= "SPAC_SQ_742769971";
	static final String IDKEY = "ID";

	/**
	 * @throws ISPACException
	 */
	public ContratacionDocPresentarSDAO() throws ISPACException	{
		super(null);
	}
	
	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public ContratacionDocPresentarSDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}
	
	/**
	 * @throws ISPACException
	 */
	public ContratacionDocPresentarSDAO(DbCnt cnt, int id) throws ISPACException {
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
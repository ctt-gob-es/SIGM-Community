package es.dipucr.bdns.dao;

import java.util.Iterator;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

public class AnuncioBopBdnsDAO extends ObjectDAO {
	private static final long serialVersionUID = 1L;

	static final String TABLENAME = "BDNS_ANUNCIO_BOP";
	static final String IDKEY = "ID_ANUNCIO";

	/**
	 * 
	 * @throws ISPACException
	 */
	public AnuncioBopBdnsDAO() throws ISPACException {
		super(null);
	}

	/**
	 * 
	 * @param cnt
	 * @throws ISPACException
	 */
	public AnuncioBopBdnsDAO(DbCnt cnt) throws ISPACException {
		super(cnt, null);
	}

	/**
	 * 
	 * @param cnt
	 * @param id
	 * @throws ISPACException
	 */
	public AnuncioBopBdnsDAO(DbCnt cnt, int id) throws ISPACException {
		super(cnt, id, null);
	}

	public String getTableName() {
		return TABLENAME;
	}

	protected String getDefaultSQL(int nActionDAO) throws ISPACException {
		return " WHERE " + IDKEY + " = " + getInt(IDKEY);
	}

	public String getKeyName() throws ISPACException {
		return IDKEY;
	}

	public void newObject(DbCnt cnt, int idAnuncio) throws ISPACException {
		set(IDKEY, idAnuncio);
	}
	
	@SuppressWarnings("rawtypes")
	public void createNew(DbCnt cnt, int idAnuncio) throws ISPACException
	{
		Iterator it = iterator();
		while (it.hasNext())
		{
			//getMemberDAO(it).setValue(null);
			getMemberDAO(it).cleanDirty();
		}
		newObject(cnt, idAnuncio);
		mbNewObject = true;
	}

	static public CollectionDAO getAnuncios(DbCnt cnt) throws ISPACException {
		String sql = " ORDER BY " + IDKEY;

		CollectionDAO objlist = new CollectionDAO(AnuncioBopBdnsDAO.class);
		objlist.query(cnt, sql);
		return objlist;
	}

	@Override
	protected void newObject(DbCnt cnt) throws ISPACException {
		// TODO Auto-generated method stub
		
	}

}

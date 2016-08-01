package ieci.tdw.ispac.ispaclib.dao.cat.xml;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.catalog.procedure.ExportProcedureMgr;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.cat.PManualUsuarioDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.XmlTag;

public class XmlPcdManualUsuarioDAO extends PManualUsuarioDAO {
	
	/**
	 * @throws ISPACException
	 */
	public XmlPcdManualUsuarioDAO() throws ISPACException {
		super();
	}
	
	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public XmlPcdManualUsuarioDAO(DbCnt cnt) throws ISPACException {
		super(cnt);
	}
	
	/**
	 * @param cnt
	 * @param id
	 * @throws ISPACException
	 */
	public XmlPcdManualUsuarioDAO(DbCnt cnt, int id) throws ISPACException {
		super(cnt, id);
	}
	
	/**
	 * Obtiene la colección de manuales de usuario asociadas a un procedimiento
	 * 
	 * @param cnt manejador de la conexión
	 * @param procedureId identificador del procedimiento 
	 * @return una colección de objetos PcdTemplateDAO
	 * @throws ISPACException
	 */
	public static CollectionDAO getManualesUsuario(DbCnt cnt, int procedureId)
	throws ISPACException
	{
		String sql = "WHERE " + XmlPcdManualUsuarioDAO.IDOBJ + " = " + procedureId + " AND " + XmlPcdManualUsuarioDAO.TPOBJ + " = 1";
			
		CollectionDAO collection = new CollectionDAO(XmlPcdManualUsuarioDAO.class);
		collection.query(cnt,sql);
		return collection;
	}
	
	public String export() throws ISPACException
	{
		return XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO, null, XmlTag.newAttr(ExportProcedureMgr.ATR_ID, getString(XmlPcdManualUsuarioDAO.IDMANUAL)));
	}
	
}
package ieci.tdw.ispac.ispaclib.dao.cat.xml;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACNullObject;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.catalog.procedure.ExportProcedureMgr;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuarioDAO;
import ieci.tdw.ispac.ispaclib.dao.cat.ManualUsuarioDataDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.XmlTag;

import java.util.List;

public class XmlCTManualUsuarioDAO extends CTManualUsuarioDAO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @throws ISPACException
	 */
	public XmlCTManualUsuarioDAO() throws ISPACException {
		super();
	}
	
	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public XmlCTManualUsuarioDAO(DbCnt cnt) throws ISPACException {
		super(cnt);
	}
	
	/**
	 * @param cnt
	 * @param id
	 * @throws ISPACException
	 */
	public XmlCTManualUsuarioDAO(DbCnt cnt, int id) throws ISPACException {
		super(cnt, id);
	}
	
//	public String export(DbCnt cnt, int procedureid, XmlCTManualUsuarioDAO xmlCTManualUsuarioDAO, List ltManualesUsuario) throws ISPACException
//	{
//		StringBuffer buffer = new StringBuffer();
//		
//		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_NAME, getString("NOMBRE"), true));
//		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_DESCRIPCION, getString("DESCRIPCION"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_FECHA, getString("URL"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_FECHA, getString("FECHA"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_VERSION, getString("VERSION"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_VISIBILIDAD, getString("VISIBILIDAD"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_TIPO, getString("TIPO"), true));
////		
//		// Plantillas asociadas al tipo documental y las específicas al procedimiento
//		StringBuffer manualesUsuario = new StringBuffer();
//		IItemCollection collection = getManualesUsuario(cnt, procedureid).disconnect();
//		while (collection.next()) {
//			
//			XmlManualUsuarioDAO xmlManualUsuarioDAO = (XmlManualUsuarioDAO) collection.value();
//			String xmlManualUsuario = xmlManualUsuarioDAO.export(cnt);
//			if (xmlManualUsuario != null) {
//				manualesUsuario.append(xmlManualUsuario);
//				ltManualesUsuario.add(xmlManualUsuarioDAO);
//			}
//		}
////		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUALES_USUARIO, manualesUsuario.toString()));
//		
////        StringBuffer attributes = new StringBuffer();
////        attributes.append(XmlTag.newAttr(ExportProcedureMgr.ATR_ID, String.valueOf(getKeyInt())))
////        		  .append(XmlTag.newAttr(ExportProcedureMgr.ATR_NAME, ExportProcedureMgr.exportName(getString("NOMBRE"))));
////				
////		return sXml;
////		return buffer.toString();
//		return manualesUsuario.toString();
//	}
	
////	public String export(DbCnt cnt, int procedureid, List ltManualesUsuario) throws ISPACException
//	{
//		String sXml = null;
//		StringBuffer buffer = new StringBuffer();
		
//		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_NAME, getString("NOMBRE"), true));
//		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_DESCRIPCION, getString("DESCRIPCION"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_FECHA, getString("URL"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_FECHA, getString("FECHA"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_VERSION, getString("VERSION"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_VISIBILIDAD, getString("VISIBILIDAD"), true));
//	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_TIPO, getString("TIPO"), true));
//		
		// Plantillas asociadas al tipo documental y las específicas al procedimiento
//		StringBuffer manualesUsuario = new StringBuffer();
//		IItemCollection collection = getManualesUsuario(cnt, procedureid).disconnect();
//		while (collection.next()) {
//			
//			XmlManualUsuarioDAO xmlManualUsuarioDAO = (XmlManualUsuarioDAO) collection.value();
//			String xmlManualUsuario = xmlManualUsuarioDAO.export(cnt);
//			if (xmlManualUsuario != null) {
//				manualesUsuario.append(xmlManualUsuario);
//				ltManualesUsuario.add(xmlManualUsuarioDAO);
//			}
//		}
		
		
//		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUALES_USUARIO, manualesUsuario.toString()));
		
//        StringBuffer attributes = new StringBuffer();
//        attributes.append(XmlTag.newAttr(ExportProcedureMgr.ATR_ID, String.valueOf(getKeyInt())))
//        		  .append(XmlTag.newAttr(ExportProcedureMgr.ATR_NAME, ExportProcedureMgr.exportName(getString("NOMBRE"))));
//				
//		return sXml;
//		return buffer.toString();
//		return manualesUsuario.toString();
//	}
	
	public String export(DbCnt cnt) throws ISPACException
	{
		String sXml = null;
		StringBuffer buffer = new StringBuffer();
		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_NAME, getString("NOMBRE"), true));
		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_DESCRIPCION, getString("DESCRIPCION"), true));
		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_FECHA, getString("URL"), true));
	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_FECHA, getString("FECHA"), true));
	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_VERSION, getString("VERSION"), true));
	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_VISIBILIDAD, getString("VISIBILIDAD"), true));
	    buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO_TIPO, getString("TIPO"), true));

		// Información del fichero de la plantilla
		ManualUsuarioDataDAO manualUsuarioDataDAO = null;
		try {
			manualUsuarioDataDAO = new ManualUsuarioDataDAO(cnt, getKeyInt());
		}
		catch (ISPACNullObject ino) {
			return null;
		}

		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_NBYTES, manualUsuarioDataDAO.getString("NBYTES")));
		buffer.append(XmlTag.newTag(ExportProcedureMgr.TAG_MIMETYPE, manualUsuarioDataDAO.getString("MIMETYPE"), true));

        StringBuffer attributes = new StringBuffer();
        attributes.append(XmlTag.newAttr(ExportProcedureMgr.ATR_ID, String.valueOf(getKeyInt())))
        		  .append(XmlTag.newAttr(ExportProcedureMgr.ATR_NAME, ExportProcedureMgr.exportName(getString("NOMBRE"))));


		sXml = XmlTag.newTag(ExportProcedureMgr.TAG_MANUAL_USUARIO, buffer.toString(), attributes.toString());
		return sXml;
	}
	
    public CollectionDAO getManualesUsuario(DbCnt cnt, int procedureid) throws ISPACException
	{
    	// Plantillas asociadas al tipo documental y las específicas al procedimiento
    	// Una plantilla específica puede estar asociada a varios procedimientos
    	String sql = "WHERE (ID IN ( SELECT ID_CT_MANUAL_USUARIO FROM SPAC_P_MANUALES_USUARIO WHERE " + XmlPcdManualUsuarioDAO.IDOBJ + " = " + procedureid + " AND " + XmlPcdManualUsuarioDAO.TPOBJ + " = 1)) "
    			+ " OR TIPO != 2";   	
    	
		CollectionDAO collection = new CollectionDAO(XmlManualUsuarioDAO.class);
		collection.query(cnt,sql);
		return collection;
	}
	
}
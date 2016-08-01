package ieci.tdw.ispac.ispaclib.dao.cat.xml;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACNullObject;
import ieci.tdw.ispac.ispaclib.catalog.procedure.ExportProcedureMgr;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuarioDAO;
import ieci.tdw.ispac.ispaclib.dao.cat.ManualUsuarioDataDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.XmlTag;

public class XmlManualUsuarioDAO extends CTManualUsuarioDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @throws ISPACException
	 */
	public XmlManualUsuarioDAO() throws ISPACException {
		super();
	}

	/**
	 * @param cnt
	 * @throws ISPACException
	 */
	public XmlManualUsuarioDAO(DbCnt cnt) throws ISPACException {
		super(cnt);
	}

	/**
	 * @param cnt
	 * @param id
	 * @throws ISPACException
	 */
	public XmlManualUsuarioDAO(DbCnt cnt, int id) throws ISPACException {
		super(cnt, id);
	}

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

}
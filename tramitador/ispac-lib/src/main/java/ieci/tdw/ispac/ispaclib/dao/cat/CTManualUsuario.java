package ieci.tdw.ispac.ispaclib.dao.cat;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.item.CompositeItem;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CTManualUsuario extends CompositeItem {

	private static final long serialVersionUID = 4595932643210047186L;

	protected static Logger logger = Logger.getLogger(CTManualUsuario.class);

	public CTManualUsuario() {
		super("ManualUsuario:ID");
	}

	public CTManualUsuario(DbCnt cnt, int ManualUsuarioId) throws ISPACException {
		super("ManualUsuario:ID");
		getManualUsuario(cnt, ManualUsuarioId);
	}

	public CTManualUsuario(DbCnt cnt, CTManualUsuarioDAO ManualUsuario) throws ISPACException {
		super("ManualUsuario:ID");
		addItem(ManualUsuario, "ManualUsuario:");
		ManualUsuarioDataDAO data = new ManualUsuarioDataDAO(cnt, ManualUsuario.getKeyInt());
		addItem(data, "DATA:", false);
	}

	public void newManualUsuario(DbCnt cnt, String name, String descripcion, String version, String visibilidad, String tipo, String url, String documentManualUsuario)
			throws ISPACException {

		if (CTManualUsuarioDAO.getManualUsuario(cnt, name, version) != null) {
			throw new ISPACException("Ya existe una plantilla de nombre '" + name + "' y versión " + version + " asociada al tipo de documento y procedimiento actual");
		}

		clear();
		CTManualUsuarioDAO manualUsuario = new CTManualUsuarioDAO(cnt);
		manualUsuario.createNew(cnt);		
		manualUsuario.set("NOMBRE", name);		
		manualUsuario.set("DESCRIPCION", descripcion);
		manualUsuario.set("FECHA", new Date(System.currentTimeMillis()));
		manualUsuario.set("VERSION", version);
		manualUsuario.set("VISIBILIDAD", visibilidad);
		manualUsuario.set("TIPO", tipo);
		manualUsuario.set("URL", url);
		manualUsuario.store(cnt);
		addItem(manualUsuario, "ManualUsuario:");
		// Datos de la plantilla
		setDefaultManualUsuario(cnt, manualUsuario, documentManualUsuario);
		ManualUsuarioDataDAO data = new ManualUsuarioDataDAO(cnt, manualUsuario.getKeyInt());
		addItem(data, "DATA:", false);
	}

	/**
	 * Para crear la plantilla cuando el documento asociado no es el que se pone
	 * por defecto en blanco sino que se carga el mismo al mismo tiempo que se
	 * crea la plantilla.
	 * 
	 * @param cnt
	 * @param idTpDoc
	 * @param name
	 * @param code
	 * @param idPcd
	 * @param expresion
	 * @param fichero
	 * @throws ISPACException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void newManualUsuario(DbCnt cnt, String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero)
			throws ISPACException, FileNotFoundException, IOException {
		newManualUsuario(cnt, name, descripcion, version, visibilidad, tipo, url, fichero, null);
	}

	public void newManualUsuario(DbCnt cnt, String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero, String mimeType)
			throws ISPACException, FileNotFoundException, IOException {
		
		if (CTManualUsuarioDAO.getManualUsuario(cnt, name, version) != null) {
			throw new ISPACException("Ya existe una plantilla de nombre '" + name + "' y versión " + version + " asociada al tipo de documento y procedimiento actual");
		}

		clear();
		CTManualUsuarioDAO manualUsuario = new CTManualUsuarioDAO(cnt);
		manualUsuario.createNew(cnt);		
		manualUsuario.set("NOMBRE", name);		
		manualUsuario.set("DESCRIPCION", descripcion);
		manualUsuario.set("FECHA", new Date(System.currentTimeMillis()));
		manualUsuario.set("VERSION", version);
		manualUsuario.set("VISIBILIDAD", visibilidad);
		manualUsuario.set("TIPO", tipo);
		manualUsuario.set("URL", url);
		manualUsuario.store(cnt);
		
		manualUsuario.store(cnt);

		addItem(manualUsuario, "ManualUsuario:");

		// Datos de la plantilla
		setDefaultManualUsuario(cnt, manualUsuario, fichero, mimeType);

		ManualUsuarioDataDAO data = new ManualUsuarioDataDAO(cnt, manualUsuario.getKeyInt());
		addItem(data, "DATA:", false);
	}

	public void getManualUsuario(DbCnt cnt, int ManualUsuarioId) throws ISPACException {
		clear();
		CTManualUsuarioDAO ManualUsuario = new CTManualUsuarioDAO(cnt, ManualUsuarioId);
		addItem(ManualUsuario, "ManualUsuario:");
		ManualUsuarioDataDAO data = new ManualUsuarioDataDAO(cnt, ManualUsuario.getKeyInt());
		addItem(data, "DATA:", false);
	}

	public void deleteManualUsuario(DbCnt cnt) throws ISPACException {
		CTManualUsuarioDAO.delete(cnt, getInt("ManualUsuario:ID"));
	}

	public void setManualUsuario(DbCnt cnt, InputStream in, int length,
			String mimetype) throws ISPACException {
		int manualUsuarioId = getInt("ManualUsuario:ID");

		CTManualUsuarioDAO.setManualUsuario(cnt, manualUsuarioId, in, length, mimetype);
		removeItem("DATA:");
		ManualUsuarioDataDAO data = new ManualUsuarioDataDAO(cnt, manualUsuarioId);
		addItem(data, "DATA:", false);
	}

	public void getManualUsuario(DbCnt cnt, OutputStream out) throws ISPACException {
		CTManualUsuarioDAO.getManualUsuario(cnt, getInt("ManualUsuario:ID"), out);
	}

	public static void getManualUsuario(DbCnt cnt, int manualUsuarioId, OutputStream out)
			throws ISPACException {
		CTManualUsuarioDAO.getManualUsuario(cnt, manualUsuarioId, out);
	}

	public int getManualUsuario() throws ISPACException {
		return getInt("ManualUsuario:ID");
	}
	
	public String getName() throws ISPACException {
		return getString("ManualUsuario:NOMBRE");
	}

	public String getDescripcion() throws ISPACException {
		return getString("ManualUsuario:DESCRIPCION");
	}
	
	public String getURL() throws ISPACException {
		return getString("ManualUsuario:URL");
	}

	public Date getDate() throws ISPACException {
		return getDate("ManualUsuario:FECHA");
	}

	public int getVersion() throws ISPACException {
		return getInt("ManualUsuario:VERSION");
	}
	
	public int getVisibilidad() throws ISPACException {
		return getInt("ManualUsuario:VISIBILIDAD");
	}
	
	public int getTipo() throws ISPACException {
		return getInt("ManualUsuario:TIPO");
	}
	public int getSize() throws ISPACException {
		return getInt("DATA:NBYTES");
	}

	public String getMimetype() throws ISPACException {
		return getString("DATA:MIMETYPE");
	}

	protected void setDefaultManualUsuario(DbCnt cnt, CTManualUsuarioDAO manualUsuario, String defaultManualUsuario) throws ISPACException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(defaultManualUsuario);
		setDefaultManualUsuario(cnt, manualUsuario, in);
	}

	/**
	 * Asigna a la plantilla nueva el documento que se acaba de cargar
	 * 
	 * @param cnt
	 * @param manualUsuario
	 * @param in
	 * @throws ISPACException
	 */
	protected void setDefaultManualUsuario(DbCnt cnt, CTManualUsuarioDAO manualUsuario, InputStream in) throws ISPACException {
		setDefaultManualUsuario(cnt, manualUsuario, in, null);
	}

	protected void setDefaultManualUsuario(DbCnt cnt, CTManualUsuarioDAO manualUsuario, InputStream in, String mimeType) throws ISPACException {

		if (StringUtils.isBlank(mimeType)) {
			// [Manu Ticket #124] Modificaciones para que se creen plantillas en blanco como odt

			IClientContext cct = null;
			IInvesflowAPI invesFlowAPI = null;
			IEntitiesAPI entitiesAPI = null;
			String valor = "";
			boolean useOdtTemplantes = false;

			if (cnt != null)
				cct = new ClientContext(cnt);
			else
				cct = new ClientContext();

			if (cct.getAPI() == null) {
				invesFlowAPI = new InvesflowAPI((ClientContext) cct);
			} else {
				invesFlowAPI = cct.getAPI();
			}

			entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IItemCollection collection = entitiesAPI.queryEntities( SpacEntities.SPAC_VARS, "WHERE NOMBRE = '" + DBUtil.replaceQuotes(ConfigurationMgr.USE_ODT_TEMPLATES) + "'");
			Iterator<?> it = collection.iterator();
			if (it.hasNext()) {
				valor = ((IItem) it.next()).getString("VALOR");
			}

			if (StringUtils.equalsIgnoreCase(valor, "si") || StringUtils.equalsIgnoreCase(valor, "yes") || StringUtils.equalsIgnoreCase(valor, "true")){
				useOdtTemplantes = true;
	    	}else if (StringUtils.equalsIgnoreCase(valor, "no") || StringUtils.equalsIgnoreCase(valor, "false")){
	    		useOdtTemplantes = false;
	    	}
			
			if (useOdtTemplantes)
				mimeType = "application/vnd.oasis.opendocument.text";
			else
				mimeType = "application/msword";
			
			// [Manu Ticket #124] Fin Modificaciones para que se creen plantillas en blanco como odt
		}

		try {
			int length = 0;
			if (in.markSupported()) {

				while (in.read() != -1) {
					length++;
				}
				in.close();
				in.reset();

				manualUsuario.setManualUsuario(cnt, in, length, mimeType);

			} else {
				FileTemporaryManager ftm = FileTemporaryManager.getInstance();
				File tmpFile = ftm.newFile();
				OutputStream out = new FileOutputStream(tmpFile);
				FileUtils.copy(in, out);
				out.flush();
				out.close();

				manualUsuario.setManualUsuario(cnt, new FileInputStream(tmpFile), (int) tmpFile.length(), mimeType);
				if (tmpFile != null) {
					tmpFile.delete();
				}
			}

		} catch (Exception e) {
			logger.error(e);
			throw new ISPACException(e);
		}
	}

}

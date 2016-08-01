package ieci.tdw.ispac.api.impl;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IManualUsuarioAPI;
import ieci.tdw.ispac.api.ManualUsuarioMgr;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACNullObject;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuario;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuarioDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.gendoc.parser.DocumentParserConnectorFactory;
import ieci.tdw.ispac.ispaclib.gendoc.parser.IDocumentParserConnector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class ManualUsuarioAPI implements IManualUsuarioAPI {
	
	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(ManualUsuarioAPI.class);
	
	private ManualUsuarioMgr mManualUsuarioMgr;
	private ClientContext context;
	/**
	 * Constructor
	 * @param context Contexto del cliente
	 */
	public ManualUsuarioAPI(ClientContext context)
	throws ISPACException
	{
		mManualUsuarioMgr = new ManualUsuarioMgr(context);
		this.context = context;
	}

	/**
	 * Crea una nueva plantilla.
	 * @param idTpDoc identificador del tipo de documento
	 * @param name nombre de la plantilla
	 * @param expresion expresion de la plantilla
	 * @param idPcd identificador de procedimiento
	 * @param fichero para asociar a la plantilla
	 * @return el objeto plantilla
	 * @throws ISPACException
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public CTManualUsuario newManualUsuario (String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero)
	throws ISPACException, FileNotFoundException, IOException {
		return newManualUsuario(name, descripcion, version, visibilidad, tipo, url, fichero, null);
	}

	/* (non-Javadoc)
	 * @see ieci.tdw.ispac.api.ITemplateAPI#newTemplate(int, java.lang.String, java.lang.String, int, java.lang.String, java.io.InputStream, java.lang.String)
	 */
	public CTManualUsuario newManualUsuario(String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero, String mimeType)
			throws ISPACException, FileNotFoundException, IOException {
		return mManualUsuarioMgr.newManualUsuario(name, descripcion, version, visibilidad, tipo, url, fichero, mimeType);
	}

	/**
	 * Crea una plantilla nueva asociandole un documento
	 * @param idTpDoc
	 * @param name
	 * @param code
	 * @param expresion
	 * @param procId
	 * @param fichero
	 * @return
	 * @throws ISPACException
	 */
	public CTManualUsuario createManualUsuarioProc(int procId, String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero)
			throws ISPACException, FileNotFoundException, IOException {
		return createManualUsuarioProc(procId, name, descripcion, version, visibilidad, tipo, url, fichero, null);
	}

	/**
	 * @see ieci.tdw.ispac.api.ITemplateAPI#createTemplateProc(int, java.lang.String, java.lang.String, java.lang.String, int, java.io.InputStream, java.lang.String)
	 */
	public CTManualUsuario createManualUsuarioProc(int procId, String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero, String mimeType)
			throws ISPACException, FileNotFoundException, IOException {
		
		CTManualUsuario manualUsuario = null;
		DbCnt cnt = null;
		
		try {
			cnt = context.getConnection();
			cnt.openTX();
			if (fichero == null) {
				manualUsuario = mManualUsuarioMgr.newManualUsuario(cnt, name, descripcion, version, visibilidad, tipo, url);
			} else {
				manualUsuario = mManualUsuarioMgr.newManualUsuario(cnt, name, descripcion, version, visibilidad, tipo, url, fichero, mimeType);
			}

			IItem item = context.getAPI().getCatalogAPI().createCTEntity(context.getConnection(),ICatalogAPI.ENTITY_P_MANUALES_USUARIO);
			item.set("ID_OBJ", procId);
			item.set("TP_OBJ", EventsDefines.EVENT_OBJ_PROCEDURE);
			item.set("ID_CT_MANUAL_USUARIO", manualUsuario.getManualUsuario());
			item.store(context);
			cnt.closeTX(true);
		} catch (Exception e) {
			logger.error("Error al crear la plantilla", e);
			cnt.closeTX(false);
			throw new ISPACException(e.getMessage());
		} finally {
			cnt.closeConnection();
		}
		
		return manualUsuario;
	}

	/**
	 * Elimina la plantilla genérica.
	 * @param manualUsuarioId Identificador de la plantilla.
	 * @throws ISPACException si ocurre algún error.
	 */
	public void deleteManualUsuario(int manualUsuarioId) throws ISPACException {
		DbCnt cnt = context.getConnection();
		try{
			cnt.openTX();
			CTManualUsuarioDAO.delete(cnt, manualUsuarioId);
			cnt.closeTX(true);
		}catch (Exception e) {
			cnt.closeTX(false);
			throw new ISPACException(e.getMessage());
		}finally{
			cnt.closeConnection();
		}

	}

	public void deleteManualUsuarioProc(int id_ct_manual_usuario) throws ISPACException {
		int id_pcd = 0;
		try{
			String query = "WHERE ID_CT_MANUAL_USUARIO = " + id_ct_manual_usuario + " AND TP_OBJ = " + EventsDefines.EVENT_OBJ_PROCEDURE;
			IItemCollection items = context.getAPI().getCatalogAPI().queryCTEntities(ICatalogAPI.ENTITY_P_MANUALES_USUARIO, query);
			Iterator<?> itemsIterator = items.iterator();
			id_pcd = ((IItem)itemsIterator.next()).getInt("ID_OBJ");

			deleteManualUsuarioProc(id_pcd, id_ct_manual_usuario);
		}catch (Exception e) {
			throw new ISPACException(e.getMessage());
		}finally{
		}
	}
	
	public void deleteManualUsuarioProc(int procId, int id_ct_manual_usuario) throws ISPACException{
		DbCnt cnt = null;
		CTManualUsuario manualUsuario = null;
		try{
			cnt = context.getConnection();
			cnt.openTX();

				String query = "DELETE FROM SPAC_P_MANUALES_USUARIO WHERE ID_CT_MANUAL_USUARIO = " + id_ct_manual_usuario + " AND ID_OBJ = " + procId + " AND TP_OBJ = " + EventsDefines.EVENT_OBJ_PROCEDURE;
			 	cnt.directExec(query);	

			 	//elimino de la tabla de plantillas
			 	manualUsuario = getManualUsuario(id_ct_manual_usuario);
				
				mManualUsuarioMgr.deleteManualUsuario(manualUsuario);
				
			cnt.closeTX(true);
 				
		}catch (Exception e) {
			cnt.closeTX(false);
			throw new ISPACException(e.getMessage());
		}finally{
			cnt.closeConnection();
		}
	}

	
	/**
	 * Obtiene las plantillas del catálogo.
	 * @return Lista de plantillas.
	 * @throws ISPACException si ocurre algún error.
	 */
	public IItemCollection getManualesUsuario() throws ISPACException {
		return mManualUsuarioMgr.getManualesUsuario();
	}

	/**
	 * Obtiene las plantillas del catálogo.
	 * @param manualUsuarioName Nombre de la plantilla.
	 * @return Lista de plantillas.
	 * @throws ISPACException si ocurre algún error.
	 */
	public IItemCollection getManualesUsuario(String manualUsuarioName) throws ISPACException {
		return mManualUsuarioMgr.getManualesUsuario(manualUsuarioName);
	}

	/**
	 * Obtiene una plantilla.
	 * @param id identificador de la plantilla
	 * @return la plantilla
	 * @throws ISPACException
	 */
	public CTManualUsuario getManualUsuario(int id)
	throws ISPACException
	{
		return mManualUsuarioMgr.getManualUsuario(id);
	}

	/**
	 * Elimina una plantilla
	 * @param manualUsuario la plantilla
	 * @throws ISPACException
	 */
	public void deleteManualUsuario(CTManualUsuario manualUsuario)
	throws ISPACException
	{
		mManualUsuarioMgr.deleteManualUsuario( manualUsuario);
	}
	
	/**
	 * Asigna el contenido a una plantilla.
	 * @param manualUsuario plantilla
	 * @param in fuente de los datos del archivo a anexar
	 * @param mimetype mimetype del archivo
	 * @throws ISPACException
	 */
	public void setManualUsuario(CTManualUsuario manualUsuario, InputStream in, int length, String mimetype)
	throws ISPACException
	{
		mManualUsuarioMgr.setManualUsuario(manualUsuario, in, length, mimetype);
	}

	/**
	 * Obtiene el contenido de una plantilla
	 * @param manualUsuario la plantilla
	 * @param out destino donde se vuelca el archivo
	 * @throws ISPACException, TemplateArchiveException
	 */
	public void getManualUsuario(CTManualUsuario manualUsuario, OutputStream out)
	throws ISPACException
	{
		mManualUsuarioMgr.getManualUsuario(manualUsuario, out);
	}

	public boolean isProcedureManualUsuario(int id_ct_manual_usuario) throws ISPACException {
		try{
			String query = "WHERE ID_CT_MANUAL_USUARIO = " + id_ct_manual_usuario + " AND TP_OBJ = " + EventsDefines.EVENT_OBJ_PROCEDURE;
			IItemCollection items = context.getAPI().getCatalogAPI().queryCTEntities(ICatalogAPI.ENTITY_P_MANUALES_USUARIO, query);
			if (items.next()){
				return true;
			}
			return false;
 				
		}catch (ISPACNullObject e) {
			return false;
		}
		
	}

	public int countManualUsuarioProcedure(int id_ct_manual_usuario) throws ISPACException {
		String query = "WHERE ID_CT_MANUAL_USUARIO = "+id_ct_manual_usuario;
		int numItems = context.getAPI().getCatalogAPI().countCTEntities(ICatalogAPI.ENTITY_P_MANUALES_USUARIO, query);
		return numItems;
	}
	
	/** 
	 * @see ieci.tdw.ispac.api.IManualUsuarioAPI#isMimeTypeSupported(java.lang.String)
	 */
	public boolean isMimeTypeSupported(String mimeType) throws ISPACException {

		boolean isSupported = false;

		try {

			// Obtiene el conector con el gestor de plantillas
			IDocumentParserConnector connector = DocumentParserConnectorFactory
					.getTemplateConnector(context);
			
			// Comprueba si el tipo MIME de la plantilla está soportado
			isSupported = connector.isSupported(mimeType);

		} catch (ISPACException e) {
			logger.error(
					"Error al comprobar si el tipo MIME de la plantilla está soportado",
					e);
			throw e;
		} catch (Exception e) {
			logger.error(
					"Error al comprobar si el tipo MIME de la plantilla está soportado",
					e);
			throw new ISPACException(e);
		}

		return isSupported;
	}
}

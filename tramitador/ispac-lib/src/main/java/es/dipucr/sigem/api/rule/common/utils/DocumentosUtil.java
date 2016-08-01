package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITemplateAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.util.ListCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.TemplateDAO;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemplateManager;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.star.connection.NoConnectException;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XPageCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;

import es.dipucr.sigem.api.rule.common.avisos.AvisosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * Ticket #32 - Aviso fin circuito de firma
 * 
 * @author Felipe-ecenpri
 * @since 03.06.2010
 */
public class DocumentosUtil {
	private static final Logger logger = Logger.getLogger(DocumentosUtil.class);
	
	public static final String BUSQUEDA_EXACTA = "=";
	public final static String BUSQUEDA_LIKE = "LIKE";
	
	public static void nuevaPagina(Document document, boolean imgCabecera, boolean hayFondo, boolean hayPie, Rectangle dimensiones) throws MalformedURLException,
	IOException, DocumentException, DocumentException, ISPACException {
		
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		String entityId = info.getOrganizationId();		
		String dir = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entityId + File.separator + "img_exp_fol"+ File.separator, "/SIGEM_TramitacionWeb");
		String imgFondo = dir + "fondo.png";
		String imgLogoCabecera = dir + "/logoCabecera.gif";
		String imgPie = dir + "pie.jpg";

		
		// Añadimos el logotipo de la diputación
		document.setMargins(document.leftMargin()+20, document.rightMargin(), document.topMargin(), document.bottomMargin());
		document.setPageSize(dimensiones);
		document.newPage();				
		
		Image imagen = null;
		if(imgCabecera){
			try{
				imagen = Image.getInstance(imgLogoCabecera);
				imagen.setAbsolutePosition(50, document.getPageSize().getHeight() - 100);
				imagen.scalePercent(50);
				document.add(imagen);
			}
			catch(Exception e){
				logger.error("ERROR no se ha encontrado la imagen de logo de la entidad: " + imgLogoCabecera + ". " + e.getMessage(), e);
				throw new ISPACRuleException("ERROR no se ha encontrado la imagen de logo de la entidad: " + imgLogoCabecera + ". " + e.getMessage(), e);
			}	
		}
		
		// Añadimos la imagen del fondo
		if(hayFondo){
			try{
				imagen = Image.getInstance(imgFondo);
				imagen.setAbsolutePosition(250, 50);
				imagen.scalePercent(70);
				document.add(imagen);
			}
			catch(Exception e){
				logger.error("ERROR no se ha encontrado la imagen de fondo: " + imgFondo + ". " + e.getMessage(), e);
				throw new ISPACRuleException("ERROR no se ha encontrado la imagen de fondo: " + imgFondo + ". " + e.getMessage(), e);
			}
		}
		
		// Añadimos el pie de página de la diputación
		if(hayPie){
			try{
				imagen = Image.getInstance(imgPie);
				imagen.setAbsolutePosition(document.getPageSize().getWidth()-550, 15);
				imagen.scalePercent(80);
				document.add(imagen);
			}
			catch(Exception e){
				logger.error("ERROR no se ha encontrado la imagen de pie de página: " + imgPie + ". " + e.getMessage(), e);
				throw new ISPACRuleException("ERROR no se ha encontrado la imagen de pie de página: " + imgPie + ". " + e.getMessage(), e);
			}
		}
		document.setMargins(document.leftMargin()-20, document.rightMargin(), document.topMargin(), document.bottomMargin());
		
		
	}
	
	@SuppressWarnings("unchecked")
	public static IItemCollection getDocumentosByTramites (IRuleContext rulectx, String numexp, int tramite)  throws ISPACException {
		IItemCollection resultado = null;
		
		/****************************************************************/
		 IClientContext cct = rulectx.getClientContext();
		 IInvesflowAPI invesFlow = cct.getAPI();
		 IEntitiesAPI entitiesAPI = invesFlow.getEntitiesAPI();
		 /****************************************************************/

		resultado = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS, " WHERE NUMEXP = '"+numexp+"' AND ID_TRAMITE ="+tramite);
		Iterator<IItem> itDoc = resultado.iterator();
		if(!itDoc.hasNext()){
			resultado = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS_H, " WHERE NUMEXP = '"+numexp+"' AND ID_TRAMITE ="+tramite);
			itDoc = resultado.iterator();
			if(!itDoc.hasNext()){
				resultado = null;
			}
			
		}
		
		return resultado;
	}
		
		@SuppressWarnings("unchecked")
		public static IItemCollection getDocumentosByCodTramites (IRuleContext rulectx, String numexp, String cod_tramite)  throws ISPACException {
			 
			IItem tramExcel = TramitesUtil.getTramiteByCode(rulectx, cod_tramite);
				
			 String consulta = "WHERE ID_TRAM_CTL = "+tramExcel.getInt("ID")+" AND NUMEXP='"+rulectx.getNumExp()+"'";
			IItemCollection tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
			Iterator<IItem> iterator = tramspacDtTramite.iterator();
			IItem itTramitedt = iterator.next();
			
			return getDocumentosByTramites(rulectx, numexp, itTramitedt.getInt("ID_TRAM_EXP"));
		}
	

	@SuppressWarnings("rawtypes")
	static private Iterator getListaDocumentos(IRuleContext rulectx, int idTramite) throws ISPACException {

		IClientContext cct = rulectx.getClientContext();
		IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();

		IItemCollection taskTpDocCollection = procedureAPI
				.getTaskTpDoc(idTramite);
		if ((taskTpDocCollection == null)
				|| (taskTpDocCollection.toList().isEmpty())) {
			throw new ISPACInfo(// Messages.getString(
					"error.decretos.acuses.TaskTpDoc"
			// )
			);
		}

		return taskTpDocCollection.iterator();
	}
	
	@SuppressWarnings("unchecked")
	public static boolean eliminarDocumento (IRuleContext rulectx, String sql) throws ISPACException {
		boolean resultado = true;
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
			IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), sql, "FDOC DESC");
	        Iterator<IItem> it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
		}catch(Exception e){			
        	logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return resultado;
	}

	public static IItem getDocumento(IEntitiesAPI entitiesAPI, int idDoc) throws ISPACException {
		
		IItem resultado = null;
		try{
			resultado = entitiesAPI.getDocument(idDoc);
		}
		catch(Exception e){			
			logger.error("No se ha recuperado ningún documento de SPAC_DT_DOCUMENTOS con id: " + idDoc);
			resultado = null;			
		}
		
		if(resultado == null){
			IItemCollection documentos_h = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS_H, " WHERE ID = "+ idDoc);
			for(Object documento : documentos_h.toList()){
				resultado = (IItem)documento;			
			}
		}
		if(resultado == null){
			throw new ISPACException("Error al recuperar el documento. No existe documento con ID: " + idDoc + ".");
		}
		return resultado;
	}

	public static int getIdDocumentByNombre(String numexp, IRuleContext rulectx, String nombrePlantilla) {
		int id = -1;

		try {
			IItem document = DocumentosUtil.getPrimerDocumentByNombre(numexp, rulectx, nombrePlantilla);
			if (document != null)
				id = document.getInt("ID");
		} catch (Exception e) {
			logger.error("Error en getDocumentId al obtener el valor ID del documento: " + nombrePlantilla + ". " + e.getMessage(), e);
		}
		return id;
	}

	public static int getIdDocumentByDescripcion(String numexp,	IRuleContext rulectx, String descripcionPlantilla) {
		int id = -1;
		try {
			IItem document = DocumentosUtil.getPrimerDocumentByDescripcion(numexp, rulectx, descripcionPlantilla);
			if (document != null)
				id = document.getInt("ID");
		} catch (Exception e) {
			logger.error("Error en getDocumentId al obtener el valor ID del documento: " + descripcionPlantilla + ". " + e.getMessage(), e);
		}
		return id;
	}

	/**
	 * 
	 * @param numexp
	 * @param rulectx
	 * @param nombrePlantilla
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static IItem getPrimerDocumentByNombre(String numexp, IRuleContext rulectx, String nombrePlantilla) {
		IItem documento = null;
		try {
			IItemCollection documentosCollection = DocumentosUtil.getDocumentsByNombre(numexp,
					rulectx, nombrePlantilla);
			Iterator documentosIterator = documentosCollection.iterator();
			if (documentosIterator.hasNext())
				documento = (IItem) documentosIterator.next();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return documento;
	}

	/**
	 * 
	 * @param numexp
	 * @param rulectx
	 * @param descripcion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static IItem getPrimerDocumentByDescripcion(String numexp, IRuleContext rulectx, String descripcion) {
		IItem documento = null;
		try {
			IItemCollection documentosCollection = getDocumentsByDescripcion( numexp, rulectx, descripcion);
			Iterator documentosIterator = documentosCollection.iterator();
			if (documentosIterator.hasNext())
				documento = (IItem) documentosIterator.next();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return documento;
	}


	/**
	 * 
	 * @param numexp
	 * @param rulectx
	 * @param nombre
	 * @return
	 */
	public static IItemCollection getDocumentsByNombre(String numexp,IRuleContext rulectx, String nombre) {
		return getDocumentsByNombre(numexp, rulectx, nombre, "FDOC DESC");
	}

	/**
	 * 
	 * @param numexp
	 * @param rulectx
	 * @param nombre
	 * @param orden
	 * @return
	 */
	public static IItemCollection getDocumentsByNombre(String numexp, IRuleContext rulectx, String nombre, String orden) {

		IItemCollection documentsCollection = null;

		try {
			documentsCollection = DocumentosUtil.getDocumentos(rulectx.getClientContext(), numexp, "NOMBRE='" + nombre + "'", orden);

		} catch (Exception e) {
			logger.error("Error en getDocumentsByNombre al obtener el valor ID del documento: "	+ nombre + ". " + e.getMessage(), e);
		}
		return documentsCollection;
	}

	/**
	 * 
	 * @param numexp
	 * @param rulectx
	 * @param descripcion
	 * @return
	 */
	public static IItemCollection getDocumentsByDescripcion(String numexp,	IRuleContext rulectx, String descripcion) {
		return getDocumentsByDescripcion(numexp, rulectx, descripcion,"FDOC DESC");
	}

	/**
	 * 
	 * @param numexp
	 * @param rulectx
	 * @param descripcion
	 * @param orden
	 * @return
	 */
	public static IItemCollection getDocumentsByDescripcion(String numexp,	IRuleContext rulectx, String descripcion, String orden) {
		IItemCollection documentsCollection = null;
		try {
			documentsCollection = DocumentosUtil.getDocumentos(rulectx.getClientContext(),numexp,"DESCRIPCION LIKE '%" + descripcion + "%'", orden);
		} catch (Exception e) {
			logger.error("Error en getDocumentsByDescripcion al obtener el valor ID del documento: "	+ descripcion + ". " + e.getMessage(), e);
		}
		return documentsCollection;
	}

	/**
	 * Devuelve el infopag del primer documento de espediente que coincida con
	 * el parámetro infopag
	 * 
	 * @param numexp
	 * @param rulectx
	 * @param nombre
	 * @return
	 * @throws ISPACException
	 */
	public static String getInfoPagByNombre(String numexp, IRuleContext rulectx, String nombre) throws ISPACException {
		String strInfoPag = "";

		try {
			strInfoPag = DocumentosUtil.getInfoPag(rulectx, DocumentosUtil.getIdDocumentByNombre(numexp, rulectx, nombre));
		} catch (Exception e) {
			throw new ISPACException(e);
		}
		return strInfoPag;
	}


	/**
	 * Devuelve el infopag del primer documento de espediente que coincida con
	 * el parámetro infopag
	 * 
	 * @param numexp
	 * @param rulectx
	 * @param descripcion
	 * @return
	 * @throws ISPACException
	 */

	public static String getInfoPagByDescripcion(String numexp,	IRuleContext rulectx, String descripcion) throws ISPACException {
		String strInfoPag = "";
		try {
			strInfoPag = DocumentosUtil.getInfoPag(rulectx, DocumentosUtil.getIdDocumentByDescripcion(numexp, rulectx, descripcion));
		} catch (Exception e) {
			throw new ISPACException(e);
		}
		return strInfoPag;
	}

	/**
	 * Crea un fichero en el repositorio temporal como copia de un documento del
	 * repositorio documental.
	 * 
	 * @param gendocAPI
	 * @param strInfoPag
	 * @param extension
	 * @return
	 * @throws ISPACException
	 */
	public static File getFile(IClientContext cct, String strInfoPag, String nombreFichero, String extension) throws ISPACException {
		File file = null;
		Object connectorSession = null;
		IGenDocAPI gendocAPI = null;
		String fileName = "";
		OutputStream out = null;

		try {
			gendocAPI = cct.getAPI().getGenDocAPI();
			
			connectorSession = gendocAPI.createConnectorSession();

			if (StringUtils.isEmpty(extension)) {
				String mimetype = gendocAPI.getMimeType(connectorSession, strInfoPag);
				extension = MimetypeMapping.getExtension(mimetype);
			}
			
			StringBuffer sbFilename = new StringBuffer();
			
			sbFilename.append(FileTemporaryManager.getInstance().getFileTemporaryPath());
			sbFilename.append("/");
			
			if(StringUtils.isEmpty(nombreFichero)){
				sbFilename.append(FileTemporaryManager.getInstance().newFileName("." + extension));
			}
			else{					
				sbFilename.append(nombreFichero);
				sbFilename.append(".");
				sbFilename.append(extension);
			}

			fileName = sbFilename.toString();

			out = new FileOutputStream(fileName);
			gendocAPI.getDocument(connectorSession, strInfoPag, out);

			file = new File(fileName);
			if (out != null)
				out.close();
		} catch (Exception e) {
			if(file != null && file.exists()){
				file.delete();
				file = null;
			}
			file = new File (fileName);
			if(file != null && file.exists()){
				file.delete();
				file = null;
			}
			throw new ISPACException(e);
		} finally {
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					throw new ISPACException(e);
				}
			}
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
		}
		return file;
	}


	/**
	 * Elimina el fichero template y el temporary correspondientes
	 * 
	 * @param rulectx
	 * @param infoPag
	 * @param templateId
	 * @return
	 * @throws ISPACException
	 */
	public static boolean deleteFile(String sFileTemplate)
			throws ISPACException {

		FileTemplateManager templateManager = null;
		FileTemporaryManager temporaryManager = null;

		try {
			// Obtiene el manejador de plantillas
			templateManager = (FileTemplateManager) FileTemplateManager
					.getInstance();
			temporaryManager = (FileTemporaryManager) FileTemporaryManager
					.getInstance();

			boolean resultado = true;
			File fTemplate = new File(templateManager.getFileMgrPath() + "/" + sFileTemplate);
			File fTemporary = new File(temporaryManager.getFileTemporaryPath() + "/" + sFileTemplate);

			if (fTemplate != null && fTemplate.exists()) {
				if (!fTemplate.delete()) {
					logger.error("No se pudo eliminar el documento: " + templateManager.getFileMgrPath() + "/" + sFileTemplate);
					resultado = false;
				}
			}
			if (fTemporary != null && fTemporary.exists()) {
				if (!fTemporary.delete()) {
					logger.error("No se pudo eliminar el documento: " + FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"
							+ sFileTemplate);
					resultado = false;
				}
			}
			return resultado;
		} catch (Exception e) {
			logger.error("Error al eliminar el documento " + templateManager.getFileMgrPath() + "/" + sFileTemplate, e);
			throw new ISPACRuleException("Error al eliminar el documento " + templateManager.getFileMgrPath() + "/" + sFileTemplate, e);
		}
	}

	static public void generateAvisoFirma(IEntitiesAPI entitiesAPI, int processId, String numexp, String message, int idDoc, IClientContext ctx) throws ISPACException {

		String mensaje = message + getNombreDocumento(entitiesAPI, idDoc);
		String destino = getAutorUID(entitiesAPI, idDoc);

		AvisosUtil.generarAviso(entitiesAPI, processId, numexp, mensaje, destino, ctx);
	}

	/**
	 * A partir de un identificador de documento, obtiene el autor del mismo.
	 * Para ello debe acceder a la bd en busca de esa entrada.
	 * 
	 * @param rulectx
	 * @param idDoc
	 * @return
	 * @throws ISPACException
	 */
	static public String getAutor(IEntitiesAPI entitiesAPI, int idDoc)
			throws ISPACException {

		String autor = "";
		IItem item = getDocumento(entitiesAPI, idDoc);

		if (item != null) {
			autor = item.getString("AUTOR_INFO");
		}
		return autor;
	}

	static public String getAutorUID(IEntitiesAPI entitiesAPI, int idDoc)
			throws ISPACException {

		String autor = "";
		IItem item = getDocumento(entitiesAPI, idDoc);

		if (item != null) {
			autor = item.getString("AUTOR");
		}
		return autor;
	}

	static public String getNumExp(IEntitiesAPI entitiesAPI, int idDoc)
			throws ISPACException {

		String numexp = "";
		IItem item = DocumentosUtil.getDocumento(entitiesAPI, idDoc);

		if (item != null) {
			numexp = item.getString("NUMEXP");
		}
		return numexp;
	}

	static public String getInfoPag(IRuleContext rulectx, int idDoc)
			throws ISPACException {

		String infoPag = "";
		IItem item = DocumentosUtil.getDocumento(rulectx.getClientContext().getAPI().getEntitiesAPI(), idDoc);
		if (item != null) {
			infoPag = item.getString("INFOPAG");
		}
		return infoPag;
	}

	static public String getInfoPagRDE(IRuleContext rulectx, int idDoc)
			throws ISPACException {

		String infoPagRDE = "";
		IItem item = getDocumento(rulectx.getClientContext().getAPI().getEntitiesAPI(), idDoc);
		if (item != null) {
			infoPagRDE = item.getString("INFOPAG_RDE");
		}
		return infoPagRDE;
	}

	public static String getInfoPagRDEoInfoPag(IRuleContext rulectx, int idDoc)
			throws ISPACException {
		if (StringUtils.isNotEmpty(getInfoPagRDE(rulectx, idDoc)))
			return getInfoPagRDE(rulectx, idDoc);
		else
			return getInfoPag(rulectx, idDoc);
	}

	static public String getNombreDocumento(IEntitiesAPI entitiesAPI, int idDoc)
			throws ISPACException {

		String nombre = "";
		IItem item = getDocumento(entitiesAPI, idDoc);
		if (item != null) {
			nombre = item.getString("NOMBRE");
		}
		return nombre;
	}

	@SuppressWarnings("rawtypes")
	static public String getNombreDocumento(IRuleContext rulectx,
			int idTramite, int tpdoc) throws ISPACException {

		Iterator it = DocumentosUtil.getListaDocumentos(rulectx, idTramite);
		IItem item = null;
		if (it.hasNext()) {
			item = (IItem) it.next();
			if (item.getInt("CT_TPDOC:ID") == tpdoc)
				return item.getString("CT_TPDOC:NOMBRE");
		}
		return "";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public String getNombreListaDocumentos(IRuleContext rulectx,
			int idTramite) throws ISPACException {

		Iterator it = DocumentosUtil.getListaDocumentos(rulectx, idTramite);
		IItem item = null;
		if (it.hasNext()) {
			List nombreDoc = new LinkedList();
			do {
				item = (IItem) it.next();
				nombreDoc.add(item.getString("CT_TPDOC:NOMBRE"));
			} while (it.hasNext());
			return nombreDoc.toString();
		} else
			return "";
	}

	/**
	 * Devuelve el número de páginas de un documento
	 * 
	 * @param xComponent
	 * @return
	 * @throws ISPACException
	 * @author Felipe
	 */
	public static int getNumPages(XComponent xComponent) throws ISPACException {
		int nPages = 0;
		try {
			XTextDocument xTextDocument = (XTextDocument) UnoRuntime
					.queryInterface(XTextDocument.class, xComponent);
			XText xText = xTextDocument.getText();
			XTextCursor xTextCursor = xText.createTextCursor();
			xTextCursor.gotoRange(xText.getEnd(), false);

			XModel xModel = (XModel) UnoRuntime.queryInterface(XModel.class,
					xComponent);
			XController xController = xModel.getCurrentController();
			XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier) UnoRuntime
					.queryInterface(XTextViewCursorSupplier.class, xController);
			XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor();
			xViewCursor.gotoRange(xTextCursor, false);

			XPageCursor xPageCursor = (XPageCursor) UnoRuntime.queryInterface(
					XPageCursor.class, xViewCursor);
			nPages = (int) xPageCursor.getPage();
		} catch (Exception e) {
			throw new ISPACException(e);
		}
		return nPages;
	}

	static public boolean getEsDocumentoAnexo(IRuleContext rulectx,
			int idTramite, int tpdoc) throws ISPACException {
		if (tpdoc == 131)
			return true;
		return false;
	}

	@SuppressWarnings("rawtypes")
	static public boolean isDocTramite(IRuleContext rulectx, int idTramite,
			int tpdoc) throws ISPACException {

		Iterator it = DocumentosUtil.getListaDocumentos(rulectx, idTramite);
		IItem item = null;

		if (it.hasNext()) {
			item = (IItem) it.next();
			int tipo = item.getInt("CT_TPDOC:ID");

			if (tipo == tpdoc)
				return true;
			else
				return false;
		} else {
			return false;
		}

	}

	public static String getPlantillaDefecto(IClientContext cct, int taskProcedureId) {
		String plantilla = "";			
		try {
			IItemCollection plantillasDefectoCollection = cct.getAPI().getEntitiesAPI().queryEntities("SPAC_P_TRAM_DATOSESPECIFICOS", "WHERE ID_TRAM_PCD = " + taskProcedureId);
			Iterator<?> plantillasDefectoIterator = plantillasDefectoCollection.iterator();
			
			if (plantillasDefectoIterator.hasNext()) {
				String plantilla_defecto = ((IItem) plantillasDefectoIterator
						.next()).getString("PLANTILLA_DEFECTO");
				if (StringUtils.isNotEmpty(plantilla_defecto)) {
					plantilla = DocumentosUtil.getNombrePlantillaByCod(cct, plantilla_defecto);
				}
			}
		} catch (Exception e) {
			logger.error("ERROR al recuperar la plantilla por defecto. "	+ e.getMessage(), e);
		}
		return plantilla;
	}
	
	
	public static IItem getPlantillaEspecifica(IRuleContext rulectx, String numexp, String plantilla){
		IItem template = null;
		try{
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			ICatalogAPI catalogAPI = invesflowAPI.getCatalogAPI();
			
			boolean encontrado = false;
			
			IItemCollection planDocIterator = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_TEMPLATE, " WHERE NOMBRE like '%" + DBUtil.replaceQuotes(plantilla) + "%'");
			
			List<?> plantillas = CollectionBean.getBeanList(planDocIterator);
			Iterator<?> iter = plantillas.iterator(); 

			while(iter.hasNext() && !encontrado) {    
				ItemBean item = (ItemBean) iter.next();
	    		String templateNombre = item.getItem().getString("NOMBRE");
	    		
	    		IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
				if (expediente != null) {
					int tpdoc = 0;
					try{
						//logger.debug("Nombre: " + templateNombre);
						String tipoDocDecCabecera = getTipoDocumentoByPlantilla(cct, plantilla);
						//logger.debug("TipoDocCabecera: " + tipoDocDecCabecera);
						tpdoc = getTipoDoc(cct,  tipoDocDecCabecera, DocumentosUtil.BUSQUEDA_EXACTA, false);
						//logger.debug("Nombre tpdoc: " + tpdoc);
						template = TemplateDAO.getTemplate(cct.getConnection(), templateNombre, tpdoc, expediente.getInt("ID_PCD"));
						
						if(template != null){
							encontrado = true;
						}
						//logger.debug((encontrado ? "ENCONTRADO" : "NO ENCONTRADO"));
					}
					catch(ISPACException e){
						logger.debug("Excepción al buscar la plantilla: " + e.getMessage(), e);
					}
				}
			}
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return template;
	}
	
	@SuppressWarnings("rawtypes")
	public static IItem getPlantillaGenerica(IRuleContext rulectx, String numexp, String plantilla){
		IItem template = null;
		try{
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			ICatalogAPI catalogAPI = invesflowAPI.getCatalogAPI();
			ITemplateAPI templateAPI = cct.getAPI().getTemplateAPI();
			
			boolean encontrado = false;
			
			IItemCollection planDocIterator = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_TEMPLATE, " WHERE NOMBRE like '%" + DBUtil.replaceQuotes(plantilla) + "%' AND ID NOT IN (SELECT ID_P_PLANTDOC FROM SPAC_P_PLANTILLAS)");
			
			List plantillas = CollectionBean.getBeanList(planDocIterator);
			Iterator iter = plantillas.iterator(); 

			while(iter.hasNext() && !encontrado) {    
				ItemBean item = (ItemBean) iter.next();
	    		String templateNombre = item.getItem().getString("NOMBRE");
        		int templateId = item.getItem().getInt("ID");

	    		if(!templateAPI.isProcedureTemplate(templateId)){ 
					try{
						template = TemplateDAO.getByName(cct.getConnection(), TemplateDAO.class, templateNombre);							
						if(template != null){
							encontrado = true;
						}
					}
					catch(ISPACException e){}
				}
			}
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return template;
	}

	

	/**
	 * [eCenpri-Felipe] Obtiene el tipo de documento a partir de su nombre
	 * 
	 * @param cct
	 * @param nombre
	 *            Nombre del tipo de documento
	 * @param tipoBusqueda
	 * 			  Indica si búsqueda exacta o like
	 * @return Tipo de documento.
	 * @throws ISPACException
	 *             si ocurre algún error.
	 */
	public static int getTipoDoc(IClientContext cct, String nombre, String tipoBusqueda, boolean busquedaUpper) throws ISPACException {

		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		IItem itemTpDoc = null;
		int tpDoc = Integer.MIN_VALUE;

		StringBuffer strQuery = new StringBuffer("WHERE ");
		
		if(busquedaUpper)
			strQuery.append(" UPPER (");
		strQuery.append("NOMBRE");
		if(busquedaUpper)
			strQuery.append(") ");
		
		strQuery.append(tipoBusqueda);
		
		if(busquedaUpper)
			strQuery.append(" UPPER (");
		
		strQuery.append(" '");
		if(tipoBusqueda.equals(DocumentosUtil.BUSQUEDA_LIKE))
			strQuery.append("%");
		
		strQuery.append(DBUtil.replaceQuotes(nombre));		
		
		if(tipoBusqueda.equals(DocumentosUtil.BUSQUEDA_LIKE))
			strQuery.append("%");
		strQuery.append("'");		
		if(busquedaUpper)
			strQuery.append(") ");

		IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, strQuery.toString());
		if (!collection.next()) {
			logger.info("No se ha encontrado ningun tipo de documento con nombre: "	+ nombre);
		} else {
			itemTpDoc = collection.value();
			tpDoc = itemTpDoc.getInt("ID");
		}

		return tpDoc;
	}
	
	public static IItem getTipoDocByCodigo(IClientContext cct, String codigoTipoDoc) throws ISPACException {

		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		IItem itemTpDoc = null;

		StringBuffer strQuery = new StringBuffer("WHERE COD_TPDOC='"+codigoTipoDoc+"'");

		IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, strQuery.toString());
		if (!collection.next()) {
			logger.error( "ERROR al recuperar el tipo de documento indicada. "+codigoTipoDoc);
			throw new ISPACException("No se encuentra el tipo de documento con el código. "+codigoTipoDoc);
		} else {
			itemTpDoc = collection.value();
		}

		return itemTpDoc;
	}
	
	public static int getIdTipoDocByCodigo(IClientContext cct, String codigoTipoDoc) throws ISPACException {

		int idTpDoc = Integer.MIN_VALUE;
		IItem itemTpDoc = DocumentosUtil.getTipoDocByCodigo(cct, codigoTipoDoc);

		if (itemTpDoc == null) {
			logger.error( "ERROR al recuperar el tipo de documento indicada. " + codigoTipoDoc);
			throw new ISPACException("No se encuentra el tipo de documento con el código. " + codigoTipoDoc);
		} else {
			idTpDoc = itemTpDoc.getInt("ID");
		}
		return idTpDoc;
	}
	
	/**
	 * Devuelve el identificador del tipo de documento indicado
	 * 
	 * @param rulectx El contexto de la regla.
	 * @param nombreTpDoc Nombre del tipo de documento.
	 * @throws ISPACException Debido a errores en la API de SIGEM.
	 */
	public static int getIdTpDocByNombreTpDoc(IRuleContext rulectx, String nombreTpDoc) throws ISPACException{
		int idTpDoc = -1;
		try{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String strQuery = "WHERE NOMBRE='"+nombreTpDoc+"'";
			IItemCollection tpDocs = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
			Iterator<?> it = tpDocs.iterator();
			if (it.hasNext()){
				IItem tpdoc = (IItem)it.next();
				idTpDoc = tpdoc.getInt("ID");
			}
		}
		catch (Exception e){
        	throw new ISPACException(e);
		}
		return idTpDoc;
	}
	
	public static String getTipoDocNombreByCodigo(IClientContext cct, String codigoTipoDoc) throws ISPACException {

		String nombreTpDoc = "";

		IItem itemTpDoc = DocumentosUtil.getTipoDocByCodigo(cct, codigoTipoDoc);

		if (itemTpDoc == null) {
			logger.error( "ERROR al recuperar el tipo de documento indicada. " + codigoTipoDoc);
			throw new ISPACException("No se encuentra el tipo de documento con el código. " + codigoTipoDoc);
		} else {
			nombreTpDoc = itemTpDoc.getString("NOMBRE");
		}

		return nombreTpDoc;
	}

	public static IItem generarDocumento(IRuleContext rulectx, String nombrePlantilla, String descripcion) throws ISPACException {
		IItem entityTemplate = null;
		try {
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();

			String tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, nombrePlantilla);
			int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
			
			if(documentTypeId > 0){
				IItem template = TemplateDAO.getTemplate(cct.getConnection(), nombrePlantilla, documentTypeId);
				
				if(template != null){
					int templateId = template.getInt("ID");

					IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
					
					//Registro telemático. La sesión es null
					String idSession = cct.getTicket();
					boolean bRegistroTelematico = ((StringUtils.isEmpty(idSession) || idSession.equals("null")));
					
					if(processTask != null){
						if (!bRegistroTelematico){
							cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
						}
					}
					
					entityTemplate = generarDocumento(rulectx, documentTypeId, templateId, rulectx.getTaskId(), descripcion, extensionEntidad);
					
					if (!bRegistroTelematico){
						cct.deleteSsVariable("NOMBRE_TRAMITE");
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACException(e.getMessage(), e);
		}
		return entityTemplate;
	}

	/**
	 * [Teresa] este metodo se encarga de generar un documento en un expediente
	 * y en un tramite especifico a partir de una plantilla que se encuentra en
	 * otro expediente
	 * **/
	@SuppressWarnings( "unused" )
	public static void generarDocumento(IRuleContext rulectx, String plantillaDecretoCabecera, String tipoDocDecCabecera, String descripcion, String numexp_ent, int idTramiteNuevo)
			throws ISPACException {
		try {
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = invesflowAPI.getGenDocAPI();
			ITemplateAPI templateAPI = invesflowAPI.getTemplateAPI();
			ICatalogAPI catalogAPI = invesflowAPI.getCatalogAPI();			

			boolean encontrado = false;
			int tpdoc = getTipoDoc(cct,  tipoDocDecCabecera, DocumentosUtil.BUSQUEDA_EXACTA, false);
			int templateId = 0;
			
			IItem template = getPlantillaEspecifica(rulectx, numexp_ent, plantillaDecretoCabecera);
			if(template == null){
				template = getPlantillaGenerica(rulectx, numexp_ent, plantillaDecretoCabecera);
			}
			if(template != null){
				templateId = template.getInt("ID");
			
				IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
				if(processTask != null){
					cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
				}
				logger.debug("Se va a generar el documento de la plantilla " + template.getString("NOMBRE"));
				
				String extensionEntidad = obtenerExtensionDocPorEntidad();
				generarDocumento(rulectx, tpdoc, templateId, idTramiteNuevo, descripcion, extensionEntidad);

				if(processTask != null){
					cct.deleteSsVariable("NOMBRE_TRAMITE");
				}
			}
		} catch (Exception e) {
			throw new ISPACException(e);
		}
	}
	
	/**
	 * [eCenpri-Felipe #1004]
	 * Este método se encarga de generar un documento en un expediente y en un trámite especifico
	 * a partir de una plantilla que se encuentra en otro expediente
	 * TODO: Con el mimetype de la plantilla intentar llamar a "doc" u "odt" en el generar documento.
	 * Si es vacío el mimetype en spac_p_plantdoc, ponemos por defecto extensión "odt"
	 * **/
	@Deprecated
	public static void generarDocumentoDoc(IRuleContext rulectx, String plantillaDecretoCabecera, String tipoDocDecCabecera, String descripcion, String numexp_ent, int idTramiteNuevo)
			throws ISPACException {
		try {
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();		

			int tpdoc = getTipoDoc(cct,  tipoDocDecCabecera, DocumentosUtil.BUSQUEDA_EXACTA, false);
			int templateId = 0;
			
			IItem template = getPlantillaEspecifica(rulectx, numexp_ent, plantillaDecretoCabecera);
			if(template == null){
				template = getPlantillaGenerica(rulectx, numexp_ent, plantillaDecretoCabecera);
			}
			if(template != null){
				templateId = template.getInt("ID");
			
				IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
				if(processTask != null){
					cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
				}
				
				generarDocumento(rulectx, tpdoc, templateId, idTramiteNuevo, descripcion, "doc");

				if(processTask != null){
					cct.deleteSsVariable("NOMBRE_TRAMITE");
				}
			}
		} catch (Exception e) {
			throw new ISPACException(e);
		}
	}

	public static IItem generarDocumento(IRuleContext rulectx, int documentTypeId, int templateId, int taskId, String descripcion, String extension) throws ISPACException{
		
		Object connectorSession = null;
		IClientContext cct = null;
		IGenDocAPI gendocAPI = null;
		IItem entityTemplate = null;
		
		try {
			cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			gendocAPI = invesflowAPI.getGenDocAPI();
			
			connectorSession = gendocAPI.createConnectorSession();

			cct.beginTX();
			
			IItem entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
			int documentId = entityDocument.getKeyInt();

			entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
			entityTemplate.set("EXTENSION", extension);
			
			if (StringUtils.isNotEmpty(descripcion)) {
				String templateDescripcion = entityTemplate.getString("DESCRIPCION");

				templateDescripcion = templateDescripcion	+ " - " + (StringUtils.isNotEmpty(descripcion)?descripcion :"");
				
				entityTemplate.set("DESCRIPCION", templateDescripcion);
			}
			entityTemplate.store(cct);
			cct.endTX(true);
			
		} catch (Exception e) {
			cct.endTX(false);
			logger.error("Error al generar el documento: " + descripcion + ". " + e.getMessage(), e);
			throw new ISPACException("Error al generar el documento: " + descripcion + ". " + e.getMessage(), e);
		} finally {
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
		}		
		return entityTemplate;
	}
	
	/**
	 * [eCenpri-Manu] - Se trae de CommonFunction
	 * Genera un documento a partir de una plantilla.
	 * La funcionalidad es la misma que si se hiciese desde el enlace
	 * de la aplicación adjuntar documento 'Desde plantilla'. 
	 *
	 * @param rulectx El contexto de la regla.
	 * @param nombreTpDoc Nombre del tipo de documento.
	 * @param nombrePlantilla Nombre de la plantilla.
	 * @param descripcion Cadena de texto opcional. Si no es 'null' se añade al campo 'descripcion' del documento generado, separada tras un guión " - ".
	 * @throws ISPACException Debido a errores en la API de SIGEM.
	 */	
	public static void generarDocumento(IRuleContext rulectx, String nombreTpDoc, String nombrePlantilla, String descripcion) throws ISPACException
	{
		try
		{
			//APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();

			//Obtención de los tipos de documento asociados al trámite
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
	    	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	    	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty())
	    	{
	    		throw new ISPACInfo("No hay ningún tipo de documento asociado al trámite");
	    	}

    		//Búsqueda de la plantilla indicada
	    	//  Primero busco el tipo de documento
        	Iterator<?> itTpDocs = taskTpDocCollection.iterator();
        	boolean found = false;
        	while(itTpDocs.hasNext())
        	{
		    	IItem taskTpDoc = (IItem)itTpDocs.next();
	    		int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	        	int tpDocId = getIdTpDocByNombreTpDoc(rulectx, nombreTpDoc);
	    		if (tpDocId != documentTypeId)
	    		{
	    			//Este no es el Tipo de documento solicitado
	    			continue;
	    		}

	    		//Ahora busco la plantilla indicada
	        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
	        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty())
	        	{
	        		//No hay ninguna plantilla asociada al tipo de documento
	        		continue;
	        	}
	        	Iterator<?> itTemplate = tpDocsTemplatesCollection.iterator();
	        	while(itTemplate.hasNext() && !found)
	        	{
		    		IItem tpDocsTemplate = (IItem)itTemplate.next();
		        	int templateId = tpDocsTemplate.getInt("ID");
	
		        	String strTemplateName = tpDocsTemplate.getString("NOMBRE");
		        	if (strTemplateName.compareTo(nombrePlantilla)==0)
		        	{
		        		//Plantilla encontrada. Genero el documento
		        		found = true;
		        		Object connectorSession = null;
		        		try
		        		{
							connectorSession = gendocAPI.createConnectorSession();
							// Abrir transacción para que no se pueda generar un documento sin fichero
					        cct.beginTX();
						
							int taskId = rulectx.getTaskId();
				        	IItem entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
							int documentId = entityDocument.getKeyInt();
			
							// Generar el documento a partir de la plantilla
							IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
							entityTemplate.set("EXTENSION", "doc");
							if ( descripcion != null)
							{
								String templateDescripcion = entityTemplate.getString("DESCRIPCION");
								templateDescripcion = templateDescripcion + " - " + descripcion;
								entityTemplate.set("DESCRIPCION", templateDescripcion);
								entityTemplate.store(cct);
							}
							entityTemplate.store(cct);
						}
						catch (Throwable e)
						{
							// Si se produce algún error se hace rollback de la transacción
							cct.endTX(false);
							
							String message = "exception.documents.generate";
							String extraInfo = null;
							Throwable eCause = e.getCause();
							
							if (eCause instanceof ISPACException)
							{
								if (eCause.getCause() instanceof NoConnectException) 
								{
									extraInfo = "exception.extrainfo.documents.openoffice.off"; 
								}
								else
								{
									extraInfo = eCause.getCause().getMessage();
								}
							}
							else if (eCause instanceof DisposedException)
							{
								extraInfo = "exception.extrainfo.documents.openoffice.stop";
							}
							else
							{
								extraInfo = e.getMessage();
							}			
							throw new ISPACInfo(message, extraInfo);
							
						}
						finally
						{
							if (connectorSession != null)
							{
								gendocAPI.closeConnectorSession(connectorSession);
							}
						}
				    	
			        	// Si todo ha sido correcto se hace commit de la transacción
						cct.endTX(true);
		        	}
	        	}
	    	}
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}
	
	public static String getFiltroOpenOffice(String extension){
		String strFilter = "";
		if(extension.equals(Constants._EXTENSION_ODT)){
			strFilter = "";
		}
		if(extension.equals(Constants._EXTENSION_DOC)){
			strFilter = "MS Word 97";
		}
		if(extension.equals(Constants._EXTENSION_PDF)){
			strFilter = "writer_pdf_Export";
		}
		return strFilter;
	}
	public static String obtenerExtensionDocPorEntidad(){
		String extensionEntidad = Constants._EXTENSION_ODT;
		String valorVariable = DipucrCommonFunctions.getVarGlobal("EXTENSION_DOCUMENTO");
        if(StringUtils.isNotEmpty(valorVariable)) extensionEntidad = valorVariable;
        return extensionEntidad;
	}
	
	public static IItem generarDocumentoDesdePlantilla(IRuleContext rulectx, int taskId, int documentTypeId, int templateId, String descripcion, String extension) throws ISPACException{
		
		Object connectorSession = null;
		IClientContext cct = null;
		IGenDocAPI gendocAPI = null;
		IItem entityDocument = null;
		
		try {
			cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			gendocAPI = invesflowAPI.getGenDocAPI();
			
			connectorSession = gendocAPI.createConnectorSession();

			IItem entityDocumentT = gendocAPI.createTaskDocument( taskId, documentTypeId);
			int documentIdT = entityDocumentT.getKeyInt();

			IItem entityTemplateT = gendocAPI.attachTaskTemplate( connectorSession, taskId, documentIdT, templateId);
			
			String infoPagT = entityTemplateT.getString("INFOPAG");
			entityTemplateT.store(cct);

			entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
			int documentId = entityDocument.getKeyInt();

			String sFileTemplate = DocumentosUtil.getFile(cct, infoPagT, null, null).getName();

			// Generar el documento a partir la plantilla
			IItem entityTemplate = gendocAPI.attachTaskTemplate( connectorSession, taskId, documentId, templateId, sFileTemplate);

			String docref = entityTemplate.getString("INFOPAG");
			String sMimetype = gendocAPI.getMimeType(connectorSession,docref);
			entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
			String templateDescripcion = (StringUtils.isNotEmpty(descripcion)? descripcion + " - " : "") + entityTemplate.getString("DESCRIPCION");
			entityTemplate.set("DESCRIPCION", templateDescripcion);
			entityTemplate.set("DESTINO", cct.getSsVariable("NOMBRE"));

			entityTemplate.store(cct);			

			entityTemplateT.delete(cct);
			entityDocumentT.delete(cct);
			DocumentosUtil.deleteFile(sFileTemplate);
			
		} catch (Throwable e) {
			cct.endTX(false);
			logger.error("Error al generar el documento: " + descripcion + ". " + e.getMessage(), e);
			throw new ISPACException("Error al generar el documento: " + descripcion + ". " + e.getMessage(), e);
		} finally {
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
		}

		return entityDocument;
	}

	/**
	 * [eCenpri-Felipe 05.06.2013] Crea un documento en el trámite actual
	 * 
	 * @param cct
	 * @param nombreTpDoc
	 * @throws ISPACException
	 */
	public static IItem crearDocumentoTramite(IRuleContext rulectx,	String nombreTpDoc) throws ISPACException {

		IItem itemDoc = null;

		try {
			IClientContext cct = rulectx.getClientContext();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();

			int tpdoc = getTipoDoc(cct, nombreTpDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);			

			itemDoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
		} catch (Exception ex) {
			throw new ISPACException("Error al crear el documento de tramite",	ex);
		}
		return itemDoc;
	}
	
	public static String getNombreTipoDocByCod(IRuleContext rulectx, String strCodTpDoc) throws ISPACException{
		String strNombreTramite = "";
		try
		{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String strQuery = "WHERE COD_TPDOC='"+strCodTpDoc+"'";
			IItemCollection tramites = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
			Iterator<?> it = tramites.iterator();
			if (it.hasNext())
			{
				IItem tramite = (IItem)it.next();
				strNombreTramite = tramite.getString("NOMBRE");
			}
		}
		catch (Exception e)
		{
        	throw new ISPACException(e);
		}
		return strNombreTramite;
	}
	
	public static String getNombrePlantillaByCod(IClientContext cct, String strCodPlantillaDoc) throws ISPACException{
		String nombrePlantilla = "";
		try {
			IItemCollection plantillasDelTPDocCollection = cct.getAPI().getEntitiesAPI().queryEntities("SPAC_P_PLANTDOC", "WHERE COD_PLANT='" + strCodPlantillaDoc + "';");
			Iterator<?> plantillasDelTPDocIterator = plantillasDelTPDocCollection.iterator();
			
			if (plantillasDelTPDocIterator.hasNext()) {
				nombrePlantilla = ((IItem) plantillasDelTPDocIterator.next()).getString("NOMBRE");		
			}
			
			else{
				logger.error( "ERROR al recuperar el nombre de la plantilla con el código: " + strCodPlantillaDoc + ". ");
				throw new ISPACException("No se encuentra la plantilla con el código: " + strCodPlantillaDoc + ". ");
			}
			
		} catch (Exception e) {
			logger.error( "ERROR al recuperar el tipo de documento de la plantilla indicada. " + e.getMessage(), e);
			throw new ISPACException(e);
		}
		return nombrePlantilla;
	}
	
	public static String getTipoDocumentoByCodPlantilla(IClientContext cct, String strCodPlantillaDoc) throws ISPACException{
		String tipoDocumento = "";
		try {
			int id_tpDoc = 0;
			IItemCollection plantillasDelTPDocCollection = cct.getAPI().getEntitiesAPI().queryEntities("SPAC_P_PLANTDOC", "WHERE COD_PLANT='" + strCodPlantillaDoc + "';");
			Iterator<?> plantillasDelTPDocIterator = plantillasDelTPDocCollection.iterator();
			
			if (plantillasDelTPDocIterator.hasNext()) {
				id_tpDoc = ((IItem) plantillasDelTPDocIterator.next()).getInt("ID_TPDOC");		
			}			
			else{
				logger.error( "ERROR al recuperar el nombre de la plantilla con el código: " + strCodPlantillaDoc + ". ");
				throw new ISPACException("No se encuentra la plantilla con el código: " + strCodPlantillaDoc + ". ");
			}
			if (id_tpDoc > 0) {
				IItem tipDoc = cct.getAPI().getGenDocAPI().getDocumentType(id_tpDoc);
				if (tipDoc != null) {
					tipoDocumento = tipDoc.getString("NOMBRE");
				}
			}
			
		} catch (Exception e) {
			logger.error( "ERROR al recuperar el tipo de documento de la plantilla indicada. " + e.getMessage(), e);
			throw new ISPACException(e);
		}
		return tipoDocumento;
	}
	
	public static String getTipoDocumentoByPlantilla(IClientContext cct, String plantilla) {
		String tipoDocumento = "";
		try {
			int id_tpDoc = 0;
			String strQueryNombre = "SELECT ID_TPDOC FROM SPAC_P_PLANTDOC WHERE NOMBRE='" + plantilla + "';";
			ResultSet plantillasDelTPDocIterator = cct.getConnection().executeQuery(strQueryNombre).getResultSet();

			while (plantillasDelTPDocIterator.next()) {
				id_tpDoc = plantillasDelTPDocIterator.getInt("ID_TPDOC");
			}
			if (id_tpDoc > 0) {
				IItem tipDoc = cct.getAPI().getGenDocAPI().getDocumentType(id_tpDoc);
				if (tipDoc != null) {
					tipoDocumento = tipDoc.getString("NOMBRE");
				}
			}
		} catch (SQLException e) {
			logger.error( "ERROR al recuperar el tipo de documento de la plantilla indicada. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error( "ERROR al recuperar el tipo de documento de la plantilla indicada. " + e.getMessage(), e);
		}
		return tipoDocumento;
	}
	
	public static IItem getTipoDocumentoByPlantillaIItem(IClientContext cct, String plantilla) {
		IItem tipDoc = null;
		try {
			int id_tpDoc = 0;
			String strQueryNombre = "SELECT ID_TPDOC FROM SPAC_P_PLANTDOC WHERE NOMBRE='" + plantilla + "';";
			ResultSet plantillasDelTPDocIterator = cct.getConnection().executeQuery(strQueryNombre).getResultSet();

			while (plantillasDelTPDocIterator.next()) {
				id_tpDoc = plantillasDelTPDocIterator.getInt("ID_TPDOC");
			}
			if (id_tpDoc > 0) {
				tipDoc = cct.getAPI().getGenDocAPI().getDocumentType(id_tpDoc);
				
			}
		} catch (SQLException e) {
			logger.error( "ERROR al recuperar el tipo de documento de la plantilla indicada. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error( "ERROR al recuperar el tipo de documento de la plantilla indicada. " + e.getMessage(), e);
		}
		return tipDoc;
	}
	
	public static void anadeDocumentoPdf(PdfWriter writer, String rutaOriginal, String nombreDocumento, String descripcionAdjunto) throws ISPACRuleException {

		try{
			
			PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, rutaOriginal, nombreDocumento, null);
			if (pfs != null)
				writer.addFileAttachment(descripcionAdjunto, pfs);
			
		}
		catch(IOException e){
			logger.error("Se produjo una excepciÃ³n "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}

	public static IItem generaYAnexaDocumento(IRuleContext rulectx, int idTipoDoc, String descripcionDocumento,	File documento, String extension) throws ISPACException {
		return generaYAnexaDocumento(rulectx, rulectx.getTaskId(), idTipoDoc, 0, descripcionDocumento, documento, extension);
	}
	
	public static IItem generaYAnexaDocumento(IRuleContext rulectx, int taskId, int idTipoDoc, String descripcionDocumento,	File documento, String extension) throws ISPACException {
		return generaYAnexaDocumento(rulectx, taskId, idTipoDoc, 0, descripcionDocumento, documento, extension);
	}
	
	public static IItem generaYAnexaDocumento(IRuleContext rulectx, int taskId, int idTipoDoc, int templateId, String descripcionDocumento,	File documento, String extension) throws ISPACException {
		IItem entityTemplate = null;
		try {
			// ----------------------------------------------------------------------------------------------
			IClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			// ----------------------------------------------------------------------------------------------

			IItem newdoc = genDocAPI.createTaskDocument(taskId, idTipoDoc);
			int documentId = newdoc.getKeyInt();
			
			//El tamaño del campo descripción en bbdd es 250
			if(descripcionDocumento.length()>=245){
				descripcionDocumento = descripcionDocumento.substring(0, 245);
			}

			if(templateId == 0)
				entityTemplate = DocumentosUtil.anexaDocumento(cct, genDocAPI, taskId, documentId, documento, extension, descripcionDocumento);
			else
				entityTemplate = DocumentosUtil.generaDocumento(cct, genDocAPI, taskId, templateId, documentId, documento, extension, descripcionDocumento);
			
		} catch (ISPACRuleException e) {
			logger.error("ERROR al generar y anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR al generar y anexar un documento. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("ERROR al generar y anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR al generar y anexar un documento. " + e.getMessage(), e);
		}
		return entityTemplate;
	}
	
	public static IItem generaYAnexaDocumento(IClientContext cct, int taskId, int idTipoDoc, String descripcionDocumento, File documento, String extension) throws ISPACException {
		return DocumentosUtil.generaYAnexaDocumento(cct, taskId, idTipoDoc, 0, descripcionDocumento, documento, extension);
	}
	
	public static IItem generaYAnexaDocumento(IClientContext cct, int taskId, int idTipoDoc, int templateId, String descripcionDocumento, File documento, String extension) throws ISPACException {
		IItem entityTemplate = null;
		try {
			// ----------------------------------------------------------------------------------------------
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			// ----------------------------------------------------------------------------------------------
			
			IItem newdoc = genDocAPI.createTaskDocument(taskId, idTipoDoc);
			int documentId = newdoc.getKeyInt();

			if(templateId == 0)
				entityTemplate = DocumentosUtil.anexaDocumento(cct, genDocAPI, taskId, documentId, documento, extension, descripcionDocumento);
			else
				entityTemplate = DocumentosUtil.generaDocumento(cct, genDocAPI, taskId, templateId, documentId, documento, extension, descripcionDocumento);
			
		} catch (ISPACRuleException e) {
			logger.error("ERROR al generar y anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR al generar y anexar un documento. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("ERROR al generar y anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR al generar y anexar un documento. " + e.getMessage(), e);
		}
		return entityTemplate;
	}
	
	public static IItem anexaDocumento(IRuleContext rulectx, int taskId, int documentId, File documento, String extension, String descripcionDocumento) throws ISPACException {
		IItem entityTemplate = null;
		try{
			// ----------------------------------------------------------------------------------------------
			IClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			// ----------------------------------------------------------------------------------------------
			entityTemplate = anexaDocumento(cct, genDocAPI, taskId, documentId, documento, extension, descripcionDocumento);
		}
		catch(ISPACRuleException e){
			logger.error("ERROR al anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR y anexar un documento. " + e.getMessage(), e);
		}
		return entityTemplate;
	}
	
	public static IItem anexaDocumento(IClientContext cct, IGenDocAPI genDocAPI, int taskId, int documentId, File documento, String extension, String descripcionDocumento) throws ISPACException {
		IItem entityTemplate = null;
		try{
			Object connectorSession = genDocAPI.createConnectorSession();
						
			FileInputStream documentoFis = new FileInputStream(documento);
			String sMimetype = MimetypeMapping.getMimeType(extension);
			
			entityTemplate = genDocAPI.attachTaskInputStream(connectorSession, taskId, documentId, documentoFis, (int) documento.length(), sMimetype, descripcionDocumento);
			
			entityTemplate.set("EXTENSION", extension);
			entityTemplate.store(cct);
	
			documentoFis.close();
		}
		catch(ISPACRuleException e){
			logger.error("ERROR al anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR y anexar un documento. " + e.getMessage(), e);
		} 
		catch (FileNotFoundException e) {
			logger.error("ERROR al anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR y anexar un documento. " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("ERROR al anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR y anexar un documento. " + e.getMessage(), e);
		}
		return entityTemplate;
	}
	
	public static IItem generaDocumento(IClientContext cct, IGenDocAPI genDocAPI, int taskId, int templateId, int documentId, File documento, String extension, String descripcionDocumento) throws ISPACException {
		IItem entityTemplate = null;
		try{
			Object connectorSession = genDocAPI.createConnectorSession();
						
			FileInputStream documentoFis = new FileInputStream(documento);
			String sMimetype = MimetypeMapping.getMimeType(extension);
			
			IItem entityDoc1 = genDocAPI.attachTaskInputStream(connectorSession, taskId, documentId, documentoFis, (int) documento.length(), sMimetype, descripcionDocumento);

    		int entityDoc1Id = entityDoc1.getKeyInt();
    		entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, taskId, entityDoc1Id, templateId, documento.getName());
			
			entityTemplate.set("EXTENSION", extension);
			entityTemplate.set("DESCRIPCION", descripcionDocumento);
			entityTemplate.store(cct);
		}
		catch(ISPACRuleException e){
			logger.error("ERROR al anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR y anexar un documento. " + e.getMessage(), e);
		} 
		catch (FileNotFoundException e) {
			logger.error("ERROR al anexar un documento. " + e.getMessage(), e);
			throw new ISPACException("ERROR y anexar un documento. " + e.getMessage(), e);
		}
		return entityTemplate;
	}
	
	public static int getIdUltimaPropuestaFirmada(IClientContext cct, String numexp) throws ISPACException{
		int id_doc = Integer.MIN_VALUE;
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			IItemCollection ids_prop_Collection = entitiesAPI.queryEntities("DPCR_ID_PROPUESTA", "WHERE NUMEXP = '" +numexp+ "' ORDER BY ID DESC");
			Iterator<?> ids_prop_Iterator = ids_prop_Collection.iterator();
			
			if(ids_prop_Iterator.hasNext()){
				IItem id_prop = (IItem) ids_prop_Iterator.next();
				id_doc = id_prop.getInt("ID_DOC");
			}
		} catch(Exception e) {
			logger.error("Error al recuperar el id de la última propuesta firmada del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el id de la última propuesta firmada del expediente: " + numexp + ". " + e.getMessage(), e);	    	
	    }
		return id_doc;
	}
	
	public static IItem getUltimaPropuestaFirmada (IClientContext cct, String numexp) throws ISPACException{
		IItem propuesta = null;
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			int id_doc = DocumentosUtil.getIdUltimaPropuestaFirmada(cct, numexp);
			if(id_doc > Integer.MIN_VALUE)
				propuesta = DocumentosUtil.getDocumento(entitiesAPI, id_doc);
			else{
				logger.error("Error al recuperar la última propuesta firmada del expediente: " + numexp + ". NO EXISTE ID EN LA TABLA DPCR_ID_PROPUESTA.");
				throw new ISPACException("Error al recuperar la última propuesta firmada del expediente: " + numexp + ". NO EXISTE ID EN LA TABLA DPCR_ID_PROPUESTA.");	 
			}
		} catch(Exception e) {
			logger.error("Error al recuperar la última propuesta firmada del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar la última propuesta firmada del expediente: " + numexp + ". " + e.getMessage(), e);	    	
	    }
		return propuesta;
	}
	
	public static void copiaDtDocumentos(IClientContext cct, String tabla, IItem documento_viejo, String numexp) throws ISPACException{
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		IItem documento_nuevo = entitiesAPI.createEntity(tabla, numexp);

		documento_nuevo.set("ID", documento_viejo.getInt("ID"));
		documento_nuevo.set("NUMEXP", documento_viejo.getString("NUMEXP"));
		documento_nuevo.set("FDOC", documento_viejo.getDate("FDOC"));
		documento_nuevo.set("NOMBRE", documento_viejo.getString("NOMBRE"));
		documento_nuevo.set("AUTOR", documento_viejo.getString("AUTOR"));
		documento_nuevo.set("ID_FASE", documento_viejo.getInt("ID_FASE"));
		documento_nuevo.set("ID_TRAMITE", documento_viejo.getInt("ID_TRAMITE"));
		documento_nuevo.set("ID_TPDOC", documento_viejo.getInt("ID_TPDOC"));
		documento_nuevo.set("TP_REG", documento_viejo.getString("TP_REG"));
		documento_nuevo.set("NREG", documento_viejo.getString("NREG"));
		documento_nuevo.set("FREG", documento_viejo.getDate("FREG"));
		documento_nuevo.set("INFOPAG", documento_viejo.getString("INFOPAG"));
		documento_nuevo.set("ID_FASE_PCD", documento_viejo.getInt("ID_FASE_PCD"));
		documento_nuevo.set("ID_TRAMITE_PCD", documento_viejo.getInt("ID_TRAMITE_PCD"));
		documento_nuevo.set("ESTADO", documento_viejo.getString("ESTADO"));
		documento_nuevo.set("ORIGEN", documento_viejo.getString("ORIGEN"));
		documento_nuevo.set("DESCRIPCION", documento_viejo.getString("DESCRIPCION"));
		documento_nuevo.set("ORIGEN_ID", documento_viejo.getString("ORIGEN_ID"));
		documento_nuevo.set("DESTINO", documento_viejo.getString("DESTINO"));
		documento_nuevo.set("AUTOR_INFO", documento_viejo.getString("AUTOR_INFO"));
		documento_nuevo.set("ESTADOFIRMA", documento_viejo.getString("ESTADOFIRMA"));
		documento_nuevo.set("ID_NOTIFICACION", documento_viejo.getString("ID_NOTIFICACION"));
		documento_nuevo.set("ESTADONOTIFICACION", documento_viejo.getString("ESTADONOTIFICACION"));
		documento_nuevo.set("DESTINO_ID", documento_viejo.getString("DESTINO_ID"));
		documento_nuevo.set("FNOTIFICACION", documento_viejo.getDate("FNOTIFICACION"));
		documento_nuevo.set("FAPROBACION", documento_viejo.getDate("FAPROBACION"));
		documento_nuevo.set("ORIGEN_TIPO", documento_viejo.getString("ORIGEN_TIPO"));
		documento_nuevo.set("DESTINO_TIPO",	documento_viejo.getString("DESTINO_TIPO"));
		documento_nuevo.set("ID_PLANTILLA", documento_viejo.getInt("ID_PLANTILLA"));
		documento_nuevo.set("BLOQUEO", documento_viejo.getString("BLOQUEO"));
		documento_nuevo.set("REPOSITORIO", documento_viejo.getString("REPOSITORIO"));
		documento_nuevo.set("EXTENSION", documento_viejo.getString("EXTENSION"));
		documento_nuevo.set("FFIRMA", documento_viejo.getDate("FFIRMA"));
		documento_nuevo.set("INFOPAG_RDE", documento_viejo.getString("INFOPAG_RDE"));
		documento_nuevo.set("ID_ENTIDAD", documento_viejo.getInt("ID_ENTIDAD"));
		documento_nuevo.set("ID_REG_ENTIDAD", documento_viejo.getInt("ID_REG_ENTIDAD"));
		documento_nuevo.set("EXTENSION_RDE", documento_viejo.getString("EXTENSION_RDE"));
		documento_nuevo.set("COD_COTEJO", documento_viejo.getString("COD_COTEJO"));
		documento_nuevo.set("NDOC", documento_viejo.getInt("NDOC"));
		documento_nuevo.set("NUM_ACTO", documento_viejo.getString("NUM_ACTO"));
		documento_nuevo.set("COD_VERIFICACION", documento_viejo.getString("COD_VERIFICACION"));
		documento_nuevo.set("MOTIVO_REPARO", documento_viejo.getString("MOTIVO_REPARO"));
		documento_nuevo.set("MOTIVO_RECHAZO", documento_viejo.getString("MOTIVO_RECHAZO"));		
		documento_nuevo.set("ID_PROCESO_FIRMA", documento_viejo.getString("ID_PROCESO_FIRMA"));
		documento_nuevo.set("ID_CIRCUITO", documento_viejo.getInt("ID_CIRCUITO"));
		documento_nuevo.set("HASH", documento_viejo.getString("HASH"));
		documento_nuevo.set("FUNCION_HASH", documento_viejo.getString("FUNCION_HASH"));

		documento_nuevo.store(cct);
	}
	
	public static IItem copiaDocumentoSinFirmaTramite(IRuleContext rulectx, IItem documento_viejo, String numexp, int tramite, int tpdoc, String sTpdoc) throws ISPACException{
		
		
		//Generamos la copia del documento
		File fileCopiarSinFirma = DocumentosUtil.getFile(rulectx.getClientContext(), documento_viejo.getString("INFOPAG"), documento_viejo.getString("NOMBRE"), documento_viejo.getString("EXTENSION"));
	
		return DocumentosUtil.generaYAnexaDocumento(rulectx.getClientContext(), rulectx.getTaskId(), tpdoc, sTpdoc,fileCopiarSinFirma, documento_viejo.getString("EXTENSION"));

		
	}
	
	public static IItemCollection getDocumentos(IClientContext cct, String numexp)throws ISPACException{
		
		IItemCollection resultado = DocumentosUtil.getDocumentos(cct,  numexp, "");

		return resultado;
	}
	
	public static IItemCollection getDocumentos(IClientContext cct, String numexp, String orden)throws ISPACException{
		IItemCollection resultado;
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		resultado = entitiesAPI.getDocuments(numexp, "", orden);
		
		if(resultado == null || resultado.toList().size() == 0){
			String consulta = "";
			if(StringUtils.isNotEmpty(numexp)){
				consulta += "WHERE NUMEXP = '" + numexp + "' ";
				if(StringUtils.isNotEmpty(orden)) consulta += " ORDER BY " + orden;
				resultado = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS_H, consulta);
			}
		}

		return resultado;
	}
	
	public static IItemCollection getDocumentos(IClientContext cct, String numexp, String sqlQuery, String order)throws ISPACException{
		IItemCollection resultado;
		
		String consulta = "";
		
		if(StringUtils.isNotEmpty(numexp)) consulta += "WHERE NUMEXP = '" + numexp + "' ";

		if(StringUtils.isNotEmpty(sqlQuery)){
			if(StringUtils.isNotEmpty(numexp)) consulta += " AND ";
			else consulta += " WHERE ";
			consulta += "  ( " + sqlQuery + " ) ";
		}
		if(StringUtils.isNotEmpty(order)) consulta += " ORDER BY " + order;
		
		resultado = DocumentosUtil.queryDocumentos(cct, consulta);

		return resultado;
	}
	
	public static IItemCollection queryDocumentos(IClientContext cct, String consulta) throws ISPACException{
		ListCollection resultado = null;
		ArrayList<IItem> part = new ArrayList<IItem>();
		
		try{
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			IItemCollection documentos = (ListCollection) entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS, consulta);
			for(Object documento : documentos.toList()){
				part.add((IItem)documento);
			}
			
			IItemCollection documentos_h = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS_H, consulta);
			for(Object documento : documentos_h.toList()){
				part.add((IItem)documento);
			}
			
			resultado = new ListCollection(part);
		}
		catch(Exception e){
			logger.error("Error al recuperar los documentos. consulta: " + consulta + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar los documentos. " + e.getMessage(), e);
		}
		
		return resultado;
	}
	
	/**
	 * Ticket #184 Tramite generico para preparar un zip con todos los documentos firmados
	 * INICIO
	 * 
	 * **/
	
	/**
	 * Crea un zip con los documentos especificados.
	 * @param session API de sesiones.
	 * @param docs Lista de documentos.
	 * @return Fichero zip.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static File createDocumentsZipFile(IGenDocAPI genDocAPI, List<IItem> docs) throws ISPACException {

		// Manejador de ficheros temporales
		FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
		// Crear un fichero temporal para el zip
		File zipFile = ftMgr.newFile(".zip");

		if (!CollectionUtils.isEmpty(docs)) {
			try {
				// Iniciar el zip
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
				out.setEncoding("CP437");
				
				// Incluir los documentos en el zip
				for (int i = 0; i < docs.size(); i++) {
					createDocumentZipEntry(genDocAPI, out, (IItem) docs.get(i));
				}
	
				// Finalizar el zip
				out.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return zipFile;
	}
	
	private static void createDocumentZipEntry(IGenDocAPI genDocAPI, ZipOutputStream out, IItem doc) throws ISPACException {

		Object connectorSession = null;
		try {
			connectorSession = genDocAPI.createConnectorSession();

			if (doc != null) {
				// Obtener el GUID del documento
				String guid = doc.getString("INFOPAG_RDE");
	
				if (StringUtils.isNotBlank(guid)) {
					// Incluir la entrada del documento en el zip
					out.putNextEntry(new ZipEntry(getDocumentName(doc)));
	
					// Obtener el contenido del fichero
					genDocAPI.getDocument(connectorSession, guid, out);
					// Finalizar entrada de documento
					out.closeEntry();
				}
			}
		} catch (IOException e) {
		}finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}				
	}
	
	private static String getDocumentName(IItem doc) throws ISPACException {
		StringBuffer fullName = new StringBuffer();
		
		// Número de expediente
		String numExp = doc.getString("NUMEXP");
		fullName.append(numExp);
		
		// Nombre del documento
		String name = doc.getString("DESCRIPCION");
		if (StringUtils.isBlank(name)) {
			name = doc.getString("NOMBRE");
			if (StringUtils.isBlank(name)) {
				name = new StringBuffer("unknown_").append(doc.getString("ID"))
						.toString();
			}
		}
		fullName.append("_").append(name);
		
		// Identificador del documento
		fullName.append("_").append(doc.getString("ID")); 

		// Extensión del documento
		String ext = doc.getString("EXTENSION_RDE");
		if (StringUtils.isNotBlank(ext)) {
			fullName.append(".").append(ext);
		}

		return DocumentosUtil.normalize(fullName.toString());
	}
	
	/**
	 * Ticket #184 Tramite generico para preparar un zip con todos los documentos firmados
	 * FIN
	 * 
	 * **/
	
	public static String normalize(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}
}
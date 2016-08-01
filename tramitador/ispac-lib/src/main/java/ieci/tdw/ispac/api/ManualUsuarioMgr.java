package ieci.tdw.ispac.api;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuario;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class ManualUsuarioMgr
{

	private final IClientContext context;

	private final String TEMPLATE = "ieci/tdw/ispac/ispaclib/templates/doc/empty.doc";
	
	//[Manu Ticket #124] Modificaciones para que se creen plantillas en blanco como odt
	private final String TEMPLATEODT = "ieci/tdw/ispac/ispaclib/templates/doc/empty.odt";
	//[Manu Ticket #124] Fin modificaciones
	
	private String msDefaultManualUsuario;

	/**
	 * Constructor
	 * @param context contexto del cliente
	 */
	public ManualUsuarioMgr (IClientContext context)
	throws ISPACException
	{
		this.context = context;
		ISPACConfiguration config = ISPACConfiguration.getInstance();
		msDefaultManualUsuario = config.get(ISPACConfiguration.DEFAULT_TEMPLATE);
		if (msDefaultManualUsuario == null)
		{
			//[Manu Ticket #124] Modificaciones para que se creen plantillas en blanco como odt

			boolean useOdtTemplantes = ConfigurationMgr.getVarGlobalBoolean(context, ConfigurationMgr.USE_ODT_TEMPLATES, true);
			if(useOdtTemplantes)
				msDefaultManualUsuario = TEMPLATEODT;
			else
				msDefaultManualUsuario = TEMPLATE;
			//[Manu Ticket #124] Fin modificaciones
		}
	}

	/**
	 * Crea una nueva plantilla asociandole un tipo de documento, 
	 * sino recibe ningún fichero se crea un documento en blanco por defecto
	 * @param idTpDoc identificador del tipo de documento
	 * @param name nombre de la plantilla
	 * @param expresion expresion de la plantilla
	 * @param fichero que queremos subir como plantilla
	 * @return plantilla
	 * @throws ISPACException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public CTManualUsuario newManualUsuario (String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero)
			throws ISPACException, FileNotFoundException, IOException {
		return newManualUsuario(name, descripcion, version, visibilidad, tipo, url, fichero, null);
	}
	
	public CTManualUsuario newManualUsuario(String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero, String mimetype)
			throws ISPACException, FileNotFoundException, IOException {
		
		DbCnt cnt = null;
		
		try {
			
			cnt = context.getConnection();			
			
			CTManualUsuario manualUsuario = new CTManualUsuario();
			if (fichero == null) {
				manualUsuario.newManualUsuario( cnt, name, descripcion, version, visibilidad, tipo, url, msDefaultManualUsuario);
			} else {
				manualUsuario.newManualUsuario( cnt, name, descripcion, version, visibilidad, tipo, url, fichero, mimetype);
			}
			
			return manualUsuario;
			
		} finally {
			context.releaseConnection(cnt);
		}
	}

	public CTManualUsuario newManualUsuario (DbCnt cnt, String name, String descripcion, String version, String visibilidad, String tipo, String url)
	throws ISPACException
	{
			CTManualUsuario manualUsuario = new CTManualUsuario();
			manualUsuario.newManualUsuario( cnt, name, descripcion, version, visibilidad, tipo, url, msDefaultManualUsuario);
			return manualUsuario;
	}
	
	
	/**
	 * Crea una nueva plantilla asociandole un documento
	 * @param cnt para la conexión
	 * @param idTpDoc identificador del tipo de documento
	 * @param name nombre de la plantilla
	 * @param code código de la plantilla
	 * @param expresion expresion de la plantilla
	 * @param documento documento asociado a la plantilla
	 * @return plantilla
	 * @throws ISPACException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public CTManualUsuario newManualUsuario (DbCnt cnt, String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream documento)
	throws ISPACException, FileNotFoundException, IOException
	{
		CTManualUsuario manualUsuario = new CTManualUsuario();
		manualUsuario.newManualUsuario(cnt, name, descripcion, version, visibilidad, tipo, url, documento);
		return manualUsuario;
	}

	public CTManualUsuario newManualUsuario (DbCnt cnt, String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream documento, String mimeType)
	throws ISPACException, FileNotFoundException, IOException
	{
		CTManualUsuario manualUsuario = new CTManualUsuario();		
		manualUsuario.newManualUsuario(cnt, name, descripcion, version, visibilidad, tipo, url, documento, mimeType);
		return manualUsuario;
	}

	public IItemCollection getManualesUsuario() throws ISPACException {
		return getManualesUsuario(null);
	}
	
	public IItemCollection getManualesUsuario(String manualUsuarioName) throws ISPACException {

		HashMap<String, Integer> tablemap=new HashMap<String, Integer>();
		tablemap.put("CTMANUAL", new Integer(ICatalogAPI.ENTITY_CT_MANUALES_USUARIO));		

		String filter = "";
		if (StringUtils.isNotBlank(manualUsuarioName)) {
			filter += " WHERE CTMANUAL.NOMBRE LIKE '%" + DBUtil.replaceQuotes(manualUsuarioName) + "%'";
		}
		filter += " ORDER BY CTMANUAL.NOMBRE";

		return context.getAPI().getCatalogAPI().queryCTEntities(tablemap, filter);
	}
	
	/**
	 * Carga una plantilla
	 * 
	 * @param id
	 *            identificador de la plantilla
	 * @return plantilla
	 * @throws ISPACException
	 */
	public CTManualUsuario getManualUsuario(int id)
	throws ISPACException
	{
		DbCnt cnt = null;
		try
		{
			cnt = context.getConnection();

			return new CTManualUsuario(cnt, id);
		}
		finally
		{
			context.releaseConnection(cnt);
		}
	}
	
	/**
	 * Borra la plantilla y todas sus versiones
	 * 
	 * @param manualUsuario
	 *            plantilla
	 * @throws ISPACException
	 */
	public void deleteManualUsuario (CTManualUsuario manualUsuario)
	throws ISPACException
	{
		DbCnt cnt = null;
		try
		{
			cnt = context.getConnection();

			manualUsuario.deleteManualUsuario(cnt);
		}
		finally
		{
			context.releaseConnection(cnt);
		}
	}


	/**
	 * Asocia un archivo a la la plantilla. Si existe un archivo
	 * ya asociado, lo reemplaza
	 * @param manualUsuario plantilla
	 * @param in fuente de los datos del archivo a anexar
	 * @param mimetype mimetype del archivo
	 * @throws ISPACException
	 */
	public void setManualUsuario(CTManualUsuario manualUsuario, InputStream in,int length, String mimetype)
	throws ISPACException
	{
		DbCnt cnt = null;
		try
		{
			cnt = context.getConnection();

			//si tiene mimeType el documento con el que trabajamos, usamos su mimeType, si no, cogemos el que nos viene 
			if (StringUtils.isNotBlank(manualUsuario.getMimetype())){
				mimetype = manualUsuario.getMimetype();		
			}

			manualUsuario.setManualUsuario( cnt,in,length,mimetype);
		}
		finally
		{
			context.releaseConnection(cnt);
		}
	}

	/**
	 * Devuelve el archivo anexo a la plantilla. Si el usuario que la pide
	 * es el propietario de un borrador, devuelve el archivo anexo al borrador.
	 * Si no existe borrador o no el usuario no es propietario del borrador,
	 * entonces devuelve el archivo anexo a la version en vigor. Si no existe
	 * versión en vigor, y el borrador no pertenece al usuario, se devuelve
	 * una excepcion de tipo TemplateArchiveException
	 * @param manualUsuario plantilla
	 * @param out donde volcar el contenido del fichero
	 * @throws ISPACException
	 */
	public void getManualUsuario(CTManualUsuario manualUsuario, OutputStream out)
	throws ISPACException
	{
		DbCnt cnt = null;
		try
		{
			cnt = context.getConnection();

			manualUsuario.getManualUsuario( cnt,out);
		}
		finally
		{
			context.releaseConnection(cnt);
		}
	}
}

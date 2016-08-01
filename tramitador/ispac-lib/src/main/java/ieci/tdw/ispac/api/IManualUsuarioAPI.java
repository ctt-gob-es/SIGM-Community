package ieci.tdw.ispac.api;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuario;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuarioDAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IManualUsuarioAPI
{
	/**
	 * Crea una nueva plantilla.
	 * @param idTpDoc identificador del tipo de documento
	 * @param name nombre de la plantilla
	 * @param code código de la plantilla
	 * @param expresion expresion de la plantilla
	 * @param idPcd identificador de procedimiento
	 * @param fichero para asociar a la plantilla
	 * @return el objeto plantilla
	 * @throws ISPACException
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public CTManualUsuario newManualUsuario (String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero)
	throws ISPACException, FileNotFoundException, IOException;

	/**
	 * Crea una nueva plantilla.
	 * 
	 * @param idTpDoc
	 *            identificador del tipo de documento
	 * @param name
	 *            nombre de la plantilla
	 * @param code
	 *            código de la plantilla
	 * @param expresion
	 *            expresion de la plantilla
	 * @param idPcd
	 *            identificador de procedimiento
	 * @param fichero
	 *            para asociar a la plantilla
	 * @param mimeType
	 *            Tipo MIME
	 * @return el objeto plantilla
	 * @throws ISPACException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public CTManualUsuario newManualUsuario(String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero, String mimeType)
			throws ISPACException, FileNotFoundException, IOException;

	/**
	 * Obtiene las plantillas del catálogo.
	 * @return Lista de plantillas.
	 * @throws ISPACException si ocurre algún error.
	 */
	public IItemCollection getManualesUsuario() throws ISPACException;

	/**
	 * Obtiene las plantillas del catálogo.
	 * @param manualUsuarioName Nombre de la plantilla.
	 * @return Lista de plantillas.
	 * @throws ISPACException si ocurre algún error.
	 */
	public IItemCollection getManualesUsuario(String manualUsuarioName) throws ISPACException;
	
	/**
	 * Obtiene una plantilla.
	 * @param id identificador de la plantilla
	 * @return la plantilla
	 * @throws ISPACException
	 */
	public CTManualUsuario getManualUsuario(int id)
	throws ISPACException;

	/**
	 * Elimina una plantilla
	 * @param manualUsuario la plantilla
	 * @throws ISPACException
	 */
	public void deleteManualUsuario(CTManualUsuario manualUsuario)
	throws ISPACException;

	/**
	 * Asigna el contenido a una plantilla.
	 * @param manualUsuario plantilla
	 * @param in fuente de los datos del archivo a anexar
	 * @param mimetype mimetype del archivo
	 * @throws ISPACException
	 */
	
	public void setManualUsuario(CTManualUsuario manualUsuario, InputStream in, int length, String mimetype)
	throws ISPACException;

	/**
	 * Obtiene el contenido de una plantilla
	 * @param manualUsuario la plantilla
	 * @param out destino donde se vuelca el archivo
	 * @throws ISPACException
	 */
	public void getManualUsuario(CTManualUsuario manualUsuario, OutputStream out)
	throws ISPACException;
	
	/**
	 * Crea una plantilla nueva asociandole un documento
	 * @param procId
	 * @param name
	 * @param descripcion
	 * @param version
	 * @param visibilidad
	 * @param tipo
	 * @param fichero
	 * @return
	 * @throws ISPACException
	 */
	public CTManualUsuario createManualUsuarioProc (int procId, String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero)
	throws ISPACException, FileNotFoundException, IOException;

	/**
	 * Crea una plantilla nueva asociandole un documento
	 * 
	 * @param procId
	 * @param name
	 * @param descripcion
	 * @param version
	 * @param visibilidad
	 * @param tipo
	 * @param fichero
	 * @param mimeType
	 * @return
	 * @throws ISPACException
	 */
	public CTManualUsuario createManualUsuarioProc(int procId, String name, String descripcion, String version, String visibilidad, String tipo, String url, InputStream fichero, String mimeType)
			throws ISPACException, FileNotFoundException, IOException;

	/**
	 * Elimina la plantilla genérica.
	 * @param manualUsuarioId Identificador de la plantilla.
	 * @throws ISPACException si ocurre algún error.
	 */
	public void deleteManualUsuario(int manualUsuarioId) throws ISPACException;

	/**
	 * Elimina una plantilla especifica del procedimiento pasado por parametro
	 *  
	 * @param procId
	 * @param id_ct_manual_usuario
	 * @return
	 * @throws ISPACException
	 */
	public void deleteManualUsuarioProc(int procId, int id_ct_manual_usuario)
	throws ISPACException;

	/**
	 * Elimina una plantilla especifica de la lista de plantillas de los procedimientos
	 * 
	 * @param id_ct_manual_usuario
	 * @return
	 * @throws ISPACException
	 */
	public void deleteManualUsuarioProc(int id_ct_manual_usuario)
	throws ISPACException;
	
	/**
	 * Comprueba si el id de plantilla pertenece a una plantilla específica
	 * 
	 * @return True si es una plantilla específica de un procedimiento
	 * 
	 */
	public boolean isProcedureManualUsuario(int id_ct_manual_usuario)
	 throws ISPACException ;
	
	/**
	 * Cuenta los procedimientos a los que esta añadida una plantilla específica
	 * @param id_ct_manual_usuario
	 * @return El numero de procedimiento en los que se encuentra una plantilla específica
	 * @throws ISPACException
	 */
	public int countManualUsuarioProcedure(int id_ct_manual_usuario)
	throws ISPACException ;
	
	/**
	 * Comprueba si el tipo MIME de una plantilla está soportado por el gestor
	 * de plantillas
	 * 
	 * @param mimeType
	 *            Tipo MIME de la plantilla.
	 * @return true si el tipo MIME está soportado, false en caso contrario.
	 * @throws ISPACException
	 *             si ocurre algún error.
	 */
	public boolean isMimeTypeSupported(String mimeType) throws ISPACException;
	
}

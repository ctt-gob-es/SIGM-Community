package ieci.tdw.ispac.api;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.dao.cat.CTMensaje;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public interface IMensajeAPI{
	/**
	 * Crea una nuevo mensaje.
	 * @param idSesion identificador de la sesión
	 * @param texto del mensaje a mostrar
	 * @param usuario usuario al que enviar el mensaje
	 * @param tipo del mensaje
	 * @return el objeto mensaje
	 * @throws ISPACException
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public CTMensaje newMensaje (String idSesion, String texto, String destinatario, String tipo) throws ISPACException;
	
	/**
	 * Crea una nuevo mensaje.
	 * @param idSesion identificador de la sesión
	 * @param texto del mensaje a mostrar
	 * @param usuario usuario al que enviar el mensaje
	 * @param tipo del mensaje
	 * @param fechaMensaje fecha de generación del mensaje
	 * @return el objeto plantilla
	 * @throws ISPACException
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public CTMensaje newMensaje (String idSesion, String texto, String destinatario, String tipo, Date fechaMensaje) throws ISPACException;

	/**
	 * Elimina el mensaje.
	 * @param MensajeId Identificador de la plantilla.
	 * @throws ISPACException si ocurre algún error.
	 */
	public void deleteMensaje(int mensajeId) throws ISPACException ;
	
	/**
	 * Obtiene las plantillas del catálogo.
	 * @return Lista de plantillas.
	 * @throws ISPACException si ocurre algún error.
	 */
	public IItemCollection getMensajes() throws ISPACException;

	/**
	 * Obtiene las plantillas del catálogo.
	 * @param MensajeName Nombre de la plantilla.
	 * @return Lista de plantillas.
	 * @throws ISPACException si ocurre algún error.
	 */
	public IItemCollection getMensajes(String mensajeName) throws ISPACException;

	/**
	 * Obtiene un mensaje.
	 * @param id identificador del mensaje
	 * @return El mensaje
	 * @throws ISPACException
	 */
	public CTMensaje getMensaje(int id)	throws ISPACException;

	/**
	 * Actualiza un mensaje
	 * @param Mensaje
	 * @throws ISPACException
	 */
	public void saveMensaje(CTMensaje mensaje) throws ISPACException;
	
	/**
	 * Elimina un mensaje
	 * @param Mensaje
	 * @throws ISPACException
	 */
	public void deleteMensaje(CTMensaje mensaje) throws ISPACException;

	/**
	 * Actualiza un mensaje
	 * @param keyId id del mensaje
	 * @param idSesion id de la sesión
	 * @param texto del mensaje
	 * @param usuario del mensaje
	 * @param tipo del mensaje
	 * @return el mensaje
	 * @throws ISPACException 
	 */
	public CTMensaje updateMensaje(int keyId, String idSesion, String texto, String usuario, String tipo) throws ISPACException;
	
	/**
	 * Devuelve la pantalla en función del tipo de mensaje 
	 * @param tipo de mensaje.
	 * @return url de la pantalla a mostrar.
	 * @throws ISPACException 
	 */	
	public String getPantallaByTipo(String tipo) throws ISPACException;

	public IItemCollection getMensajesBySesion(String idSesion) throws ISPACException;

	public IItemCollection getMensajesByUsuario(String usuario) throws ISPACException;

	public String getUrlMensaje(SessionAPI sesion);
	
	public int getPrimerMensaje(SessionAPI sesion);
}

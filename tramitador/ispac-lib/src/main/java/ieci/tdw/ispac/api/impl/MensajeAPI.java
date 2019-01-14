package ieci.tdw.ispac.api.impl;

import ieci.tdw.ispac.api.IMensajeAPI;
import ieci.tdw.ispac.api.MensajeMgr;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTMensaje;
import ieci.tdw.ispac.ispaclib.dao.cat.CTMensajeDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class MensajeAPI implements IMensajeAPI {
	
	public static interface TIPO_MENSAJE{//[dipucr-Felipe #712]
		public static final String INFO = "form.mensaje.propertyLabel.info";
		public static final String AVISO = "form.mensaje.propertyLabel.aviso";
		public static final String URGENTE = "form.mensaje.propertyLabel.urgente";
		public static final String CRITICO = "form.mensaje.propertyLabel.critico";
	}
	
	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(MensajeAPI.class);
	
	private MensajeMgr mMensajeMgr;
	private ClientContext context;
	/**
	 * Constructor
	 * @param context Contexto del cliente
	 */
	public MensajeAPI(ClientContext context) throws ISPACException {
		mMensajeMgr = new MensajeMgr(context);
		this.context = context;
	}

	/**
	 * Crea una nuevo mensaje.
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
	public CTMensaje newMensaje (String idSesion, String idMensaje, String destinatario, String tipo) throws ISPACException{		
		return newMensaje(idSesion, idMensaje, destinatario, tipo, null);
	}
	
	/**
	 * Crea una nuevo mensaje.
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
	public CTMensaje newMensaje (String idSesion, String idMensaje, String destinatario, String tipo, Date fechaMensaje) throws ISPACException{		
		return mMensajeMgr.newMensaje(idSesion, idMensaje, destinatario, tipo, fechaMensaje);
	}

	/**
	 * Elimina el mensaje.
	 * @param id Identificador del mensaje.
	 * @throws ISPACException si ocurre algún error.
	 */
	public void deleteMensaje(int id) throws ISPACException {
		DbCnt cnt = context.getConnection();
		try{			
			cnt.openTX();
			CTMensajeDAO.delete(cnt, id);
			cnt.closeTX(true);
		}catch (Exception e) {
			cnt.closeTX(false);
			throw new ISPACException(e.getMessage());
		} finally {
			context.releaseConnection(cnt);
		}
	}
	
	/**
	 * Obtiene los mensajes.
	 * @return Lista de plantillas.
	 * @throws ISPACException si ocurre algún error.
	 */
	public IItemCollection getMensajes() throws ISPACException {
		return mMensajeMgr.getMensajes();
	}

	/**
	 * Obtiene los mensajes.
	 * @param MensajeName Nombre de la plantilla.
	 * @return Lista de plantillas.
	 * @throws ISPACException si ocurre algún error.
	 */
	public IItemCollection getMensajes(String mensajeName) throws ISPACException {
		return mMensajeMgr.getMensajes(mensajeName);
	}

	/**
	 * Obtiene un mensaje.
	 * @param id identificador del mensaje
	 * @return El mensaje
	 * @throws ISPACException
	 */
	public CTMensaje getMensaje(int id)	throws ISPACException {
		return mMensajeMgr.getMensaje(id);
	}
	
	/**
	 * Actualiza un mensaje
	 * @param Mensaje
	 * @throws ISPACException
	 */
	public void saveMensaje(CTMensaje mensaje) throws ISPACException {
		mensaje.store(context);
	}

	/**
	 * Elimina un mensaje
	 * @param Mensaje
	 * @throws ISPACException
	 */
	public void deleteMensaje(CTMensaje mensaje) throws ISPACException{
		mMensajeMgr.deleteMensaje( mensaje);
	}

	public CTMensaje updateMensaje(int keyId, String idSesion, String texto, String usuario, String tipo) throws ISPACException {
		CTMensaje mensaje = getMensaje(keyId);
		
		mensaje.set("Mensaje:" + CTMensajeDAO.ID_SESION, idSesion);
		mensaje.set("Mensaje:" + CTMensajeDAO.TEXTO, texto);
		mensaje.set("Mensaje:" + CTMensajeDAO.USUARIO, usuario);		
		mensaje.set("Mensaje:" + CTMensajeDAO.TIPO, tipo);
		
		saveMensaje(mensaje);
		
		return mensaje;
	}
	
	public String getPantallaByTipo(String tipo) throws ISPACException{
		String mensaje = "";
		
		if("form.mensaje.propertyLabel.info".equals(tipo)){
			mensaje = ISPACConfiguration.getInstance().get(ISPACConfiguration.URL_MENSAJE_INFO); 
		} else if("form.mensaje.propertyLabel.aviso".equals(tipo)){
			mensaje = ISPACConfiguration.getInstance().get(ISPACConfiguration.URL_MENSAJE_AVISO);
		} else if("form.mensaje.propertyLabel.urgente".equals(tipo)){
			mensaje = ISPACConfiguration.getInstance().get(ISPACConfiguration.URL_MENSAJE_URGENTE);
		} else if("form.mensaje.propertyLabel.critico".equals(tipo)){
			mensaje = ISPACConfiguration.getInstance().get(ISPACConfiguration.URL_MENSAJE_CRITICO);
		}
		
		return mensaje;
	}

	public IItemCollection getMensajesBySesion(String idSesion) throws ISPACException {
		return mMensajeMgr.getMensajesByIdSesion(idSesion);
	}

	public IItemCollection getMensajesByUsuario(String usuario) throws ISPACException {
		return mMensajeMgr.getMensajesByUsuario(usuario);
	}
	
	//[Manu Ticket #129] INICIO - SIGEM Avisos Avisos_usuarios Sistema de avisos.
	public String getUrlMensaje(SessionAPI sesion){
		String pantalla = "";
		String tipo = "";
		
		try{
			IClientContext cct = sesion.getClientContext();
			
			String idSesion = sesion.getTicket();			
			String usuario = sesion.getUserName();
			IItemCollection mensajesCollection = getMensajesBySesion(idSesion);
			
			if(null == mensajesCollection || 0 == mensajesCollection.toList().size()){
				IItemCollection mensajesUsuarioCollection = getMensajesByUsuario(usuario);
				mensajesCollection = mensajesUsuarioCollection;
			}
			
			if(null != mensajesCollection && 0 < mensajesCollection.toList().size()){
				Iterator<?> sesionesIterator = mensajesCollection.iterator();
				
				if(sesionesIterator.hasNext()){
					IItem sesiones = (IItem) sesionesIterator.next();
					tipo = sesiones.getString(CTMensajeDAO.TIPO);
					pantalla = getPantallaByTipo(tipo);
					sesiones.delete(cct);
				}
			}
			//[Manu Ticket #133] FIN - SIGEM Avisos modulo avisos modulo para avisos electronicos.
		}
		catch(Exception e){
			LOGGER.error("ERROR al recuperar el mensaje de la sesion: " + sesion.getTicket() + " y usuario: " + sesion.getUserName() + ". " + e.getMessage(), e);
		}
		return pantalla;
	}
	
	//[Manu Ticket #129] INICIO - SIGEM Avisos Avisos_usuarios Sistema de avisos.
	public int getPrimerMensaje(SessionAPI sesion){
		int idMensaje = -1;

		try{
			String idSesion = sesion.getTicket();			
			String usuario = sesion.getUserName();
			IItemCollection mensajesCollection = getMensajesBySesion(idSesion);
			
			if(null == mensajesCollection || 0 == mensajesCollection.toList().size()){
				IItemCollection mensajesUsuarioCollection = getMensajesByUsuario(usuario);
				mensajesCollection = mensajesUsuarioCollection;
			}
			
			if(null != mensajesCollection && 0 < mensajesCollection.toList().size()){
				Iterator<?> sesionesIterator = mensajesCollection.iterator();
				
				if(sesionesIterator.hasNext()){
					IItem sesiones = (IItem) sesionesIterator.next();
					idMensaje = sesiones.getInt(CTMensajeDAO.IDKEY);
				}
			}
			//[Manu Ticket #133] FIN - SIGEM Avisos modulo avisos modulo para avisos electronicos.
		}
		catch(Exception e){
			LOGGER.error("ERROR al recuperar el mensaje de la sesion: " + sesion.getTicket() + " y usuario: " + sesion.getUserName() + ". " + e.getMessage(), e);
		}
		return idMensaje;
	}
	//[Manu Ticket #129] FIN - SIGEM Avisos Avisos_usuarios Sistema de avisos.
}

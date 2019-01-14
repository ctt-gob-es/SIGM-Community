package ieci.tdw.ispac.ispaclib.dao.cat;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.item.CompositeItem;

import java.util.Date;

import org.apache.log4j.Logger;

public class CTMensaje extends CompositeItem {

	private static final long serialVersionUID = 4595932643210047186L;

	protected static Logger LOGGER = Logger.getLogger(CTMensaje.class);

	public CTMensaje() {
		super("Mensaje:" + CTMensajeDAO.IDKEY);
	}

	public CTMensaje(DbCnt cnt, int MensajeId) throws ISPACException {
		super("Mensaje:" + CTMensajeDAO.IDKEY);
		getMensaje(cnt, MensajeId);
	}

	public CTMensaje(DbCnt cnt, CTMensajeDAO Mensaje) throws ISPACException {
		super("Mensaje:" + CTMensajeDAO.IDKEY);
		addItem(Mensaje, "Mensaje:");
	}

	public void newMensaje(DbCnt cnt, String id_sesion, String texto, String tipo, String usuario) throws ISPACException {
		newMensaje(cnt, id_sesion, texto, usuario, tipo, null);
	}
		
	public void newMensaje(DbCnt cnt, String id_sesion, String texto, String usuario, String tipo, Date fechaMensaje) throws ISPACException {

		clear();
		
		CTMensajeDAO mensaje = new CTMensajeDAO(cnt);
		mensaje.createNew(cnt);						
		mensaje.set(CTMensajeDAO.ID_SESION, id_sesion);
		mensaje.set(CTMensajeDAO.TEXTO, texto);
		mensaje.set(CTMensajeDAO.USUARIO, usuario);
		mensaje.set(CTMensajeDAO.TIPO, tipo);
		
		if(null == fechaMensaje){
			fechaMensaje = new Date();
		}
		
		mensaje.set(CTMensajeDAO.FECHA_MENSAJE, fechaMensaje);
		
		mensaje.store(cnt);
		addItem(mensaje, "Mensaje:");
	}

	public void getMensaje(DbCnt cnt, int mensajeId) throws ISPACException {
		clear();
		CTMensajeDAO Mensaje = new CTMensajeDAO(cnt, mensajeId);
		addItem(Mensaje, "Mensaje:");
	}

	public void deleteMensaje(DbCnt cnt) throws ISPACException {
		CTMensajeDAO.delete(cnt, getInt("Mensaje:ID"));
	}
	
	public void setMensaje(DbCnt cnt, String idSesion, String mensajeId, String tipo, String usuario) throws ISPACException {
		setMensaje(cnt, idSesion, mensajeId, usuario, tipo, null);
	}

	public void setMensaje(DbCnt cnt, String idSesion, String mensajeId, String usuario, String tipo, Date fechaMensaje) throws ISPACException {
		CTMensajeDAO.setMensaje(cnt, getId(), idSesion, mensajeId, usuario, tipo, fechaMensaje);
	}

	public int getId() throws ISPACException {
		return getInt("Mensaje:" + CTMensajeDAO.IDKEY);
	}
	
	public String getIdSesion() throws ISPACException {
		return getString("Mensaje:" + CTMensajeDAO.ID_SESION);
	}

	public String getTexto() throws ISPACException {
		return getString("Mensaje:" + CTMensajeDAO.TEXTO);
	}
	
	public String getUsuario() throws ISPACException {
		return getString("Mensaje:" + CTMensajeDAO.USUARIO);
	}
	
	public String getTipo() throws ISPACException {
		return getString("Mensaje:" + CTMensajeDAO.TIPO);
	}
	
	public Date getFechaMensaje() throws ISPACException {
		return getDate("Mensaje:" + CTMensajeDAO.FECHA_MENSAJE);
	}
}

package ieci.tdw.ispac.api;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTMensaje;
import ieci.tdw.ispac.ispaclib.dao.cat.CTMensajeDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Date;

public class MensajeMgr {

	private final IClientContext context;

	/**
	 * Constructor
	 * @param context contexto del cliente
	 */
	public MensajeMgr (IClientContext context) throws ISPACException{
		this.context = context;		
	}

	public CTMensaje newMensaje(String idSesion, String texto, String usuario, String tipo, Date fechaMensaje) throws ISPACException {
		DbCnt cnt = null;
		
		try {
			cnt = context.getConnection();					
			CTMensaje mensaje = newMensaje(cnt, idSesion, texto, usuario, tipo, fechaMensaje); 
			
			return mensaje;
		} finally {
			context.releaseConnection(cnt);
		}
	}

	public CTMensaje newMensaje (DbCnt cnt, String idSesion, String texto, String usuario, String tipo, Date fechaMensaje) throws ISPACException {
		CTMensaje mensaje = new CTMensaje();
		mensaje.newMensaje(cnt, idSesion, texto, usuario, tipo, fechaMensaje);
		return mensaje;
	}

	public IItemCollection getMensajes() throws ISPACException {
		return getMensajes(null);
	}
	
	public IItemCollection getMensajes(String texto) throws ISPACException {
		return getMensajesByCol(CTMensajeDAO.TEXTO, true, texto, CTMensajeDAO.TEXTO, "");
	}
	
	public IItemCollection getMensajesByIdSesion(String idSesion) throws ISPACException {
		return getMensajesByCol(CTMensajeDAO.ID_SESION, false, idSesion, CTMensajeDAO.FECHA_MENSAJE, "ASC");
	}
	
	public IItemCollection getMensajesByUsuario(String usuario) throws ISPACException {
		return getMensajesByCol(CTMensajeDAO.USUARIO, false, usuario, CTMensajeDAO.FECHA_MENSAJE, "ASC");
	}
		
	public IItemCollection getMensajesByCol(String columna, boolean isLike, String valor, String orderCol, String orderAsc) throws ISPACException {

		String filter = "";
		
//		HashMap<String, Integer> tablemap=new HashMap<String, Integer>();
//		tablemap.put("SMENSAJE", new Integer(ICatalogAPI.ENTITY_S_SESION_MENSAJE));
		
		if(StringUtils.isNotEmpty(columna) && StringUtils.isNotEmpty(valor)){			
			if (StringUtils.isNotBlank(valor)) {
				filter += " WHERE " + columna;
				if(isLike){
					filter += " LIKE '%" + DBUtil.replaceQuotes(valor) + "%'";
				} else {
					filter += " = '" + DBUtil.replaceQuotes(valor) + "'";
				}
			}
			
			if(StringUtils.isNotEmpty(orderCol)){
				filter += " ORDER BY " + orderCol;
				
				if(StringUtils.isNotEmpty(orderAsc)){
					filter += " " + orderAsc;
				}				
			}
		} else {
			filter = " WHERE 1 = 2 ";
		}
		
		return context.getAPI().getCatalogAPI().queryCTEntities(new Integer(ICatalogAPI.ENTITY_S_SESION_MENSAJE), filter);
	}
	
	/**
	 * Carga una plantilla
	 * 
	 * @param id
	 *            identificador del mensaje
	 * @return plantilla
	 * @throws ISPACException
	 */
	public CTMensaje getMensaje(int id) throws ISPACException {
		
		DbCnt cnt = null;
		try {
			cnt = context.getConnection();
			return new CTMensaje(cnt, id);
		} finally {
			context.releaseConnection(cnt);
		}
	}
	
	/**
	 * Borra un mensaje
	 * 
	 * @param Mensaje
	 *            
	 * @throws ISPACException
	 */
	public void deleteMensaje (CTMensaje Mensaje) throws ISPACException {
		DbCnt cnt = null;
		try {
			cnt = context.getConnection();

			Mensaje.deleteMensaje(cnt);
		} finally {
			context.releaseConnection(cnt);
		}
	}	
}

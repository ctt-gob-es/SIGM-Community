package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import es.dipucr.sigem.dao.SesionMensajeDAO;

public class MensajesSesionUtil {

	public static String TABLENAME = "SPAC_S_SESION_MENSAJE";
	
	/**
	 * Genera un mensaje de sesion para el usuario en cuestión
	 * @param cct
	 * @param idSession
	 * @param mensaje
	 * @throws ISPACException 
	 */
	public static void crearMensajeUsuario(ClientContext cct, String mensaje, String usuario) throws ISPACException{
		
		DbCnt cnt = cct.getConnection();
		
		SesionMensajeDAO smDAO = new SesionMensajeDAO(cnt);
		smDAO.createNew(cnt);
		smDAO.set("NUMEXP", "");
		smDAO.set("ID_MENSAJE", mensaje);
		smDAO.set("USUARIO", usuario);
		smDAO.store(cnt);
	}
}

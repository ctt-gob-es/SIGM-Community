package es.dipucr.sigem.api.rule.common.utils;

import java.util.StringTokenizer;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;

/**
 * [eCenpri-Felipe #830]
 * Clase con métodos comunes del usuario
 * @author Felipe
 * @since 01.02.13
 */
public class UsuariosUtil {
	
	public static String UID_SEPARATOR = "-";

	/**
	 * TODO [dipucr-Felipe]
	 * Existen las interfaces provistas por SIGEM UserImpl y UserDataImpl que cargan todos los campos
	 * Esto creo que dio problemas a Agustín a nivel de Maven, por el tema de ciclos entre proyectos
	 * 
	 * User user = ObjFactory.createUser(userId, Defs.NULL_ID);
	 * user.load(idUsuario, entidad);
	 */
	
	/**
	 * Devuelve el campo del usuario actual de la tabla IUSERUSERHDR
	 * @param cct
	 * @param nombreCampo
	 * @return
	 * @throws ISPACException
	 */
	public static String getCampoUsuario(ClientContext cct, String nombreCampo) throws ISPACException{
		
		String codEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
		AccesoBBDDRegistro accesoRegistro = new AccesoBBDDRegistro(codEntidad);
		String nombreUsuario = cct.getUser().getName();
		return accesoRegistro.getCampoUsuario(nombreUsuario, nombreCampo);
	}
	
	/**
	 * Devuelve el campo del usuario indicado de la tabla IUSERUSERHDR
	 * @param cct
	 * @param nombreCampo
	 * @return
	 * @throws ISPACException
	 */
	public static String getCampoUsuario(ClientContext cct, String nombreUsuario, String nombreCampo) throws ISPACException{
		
		String codEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
		AccesoBBDDRegistro accesoRegistro = new AccesoBBDDRegistro(codEntidad);
		return accesoRegistro.getCampoUsuario(nombreUsuario, nombreCampo);
	}
	
	/**
	 * Devuelve el campo del usuario actual de la tabla IUSERUSERHDR
	 * @param cct
	 * @param nombreCampo
	 * @return
	 * @throws ISPACException
	 */
	public static String getCampoUsuarioData(ClientContext cct, String nombreCampo) throws ISPACException{
		
		String codEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
		AccesoBBDDRegistro accesoRegistro = new AccesoBBDDRegistro(codEntidad);
		int idUsuario = getIdUsuario(cct);
		return accesoRegistro.getCampoUsuarioData(idUsuario, nombreCampo);
	}
	
	/**
	 * Devuelve el campo del usuario indicado de la tabla IUSERUSERHDR
	 * @param cct
	 * @param nombreCampo
	 * @return
	 * @throws ISPACException
	 */
	public static String getCampoUsuarioData(ClientContext cct, String nombreUsuario, String nombreCampo) throws ISPACException{
		
		String codEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
		AccesoBBDDRegistro accesoRegistro = new AccesoBBDDRegistro(codEntidad);
		int id = Integer.valueOf(getCampoUsuario(cct, nombreUsuario, "ID"));
		return accesoRegistro.getCampoUsuarioData(id, nombreCampo);
	}
	
	/**
	 * [dipucr-Felipe 3#231]
	 * Devuelve únicamente el id de usuario que viene de desglosar el UID en dos tokens
	 * @param cct
	 * @return
	 * @throws ISPACException
	 */
	public static int getIdUsuario(ClientContext cct) throws ISPACException{
		
		int id = Integer.MIN_VALUE;
		String sUID = cct.getUser().getUID();
		
		StringTokenizer tokens = new StringTokenizer( sUID, "-");
		tokens.nextToken();
		if (tokens.hasMoreTokens())
	    {
	        id = Integer.parseInt(tokens.nextToken());
	    }
		else{
			throw new ISPACException("Error al recuperar el id del usuario actual");
		}
		return id;
	}
	
	/**
	 * Devuelve el DNI del usuario actual
	 * @param ctx
	 * @return
	 * @throws ISPACException
	 */
	public static String getDni(ClientContext cct) throws ISPACException{
		
		return getCampoUsuarioData(cct, "DNI");
	}
	
	/**
	 * Devuelve el Nombre de firma del usuario actual
	 * @param ctx
	 * @return
	 * @throws ISPACException
	 */
	public static String getNombreFirma(ClientContext cct) throws ISPACException{
		
		String nombre = getCampoUsuarioData(cct, "NOMBRE");
		String apellidos = getCampoUsuarioData(cct, "APELLIDOS");
		return getNombreCompleto(nombre, apellidos);
	}
	
	/**
	 * Devuelve el Nombre de firma del usuario indicado
	 * @param ctx
	 * @return
	 * @throws ISPACException
	 */
	public static String getNombreFirma(ClientContext cct, String nombreUsuario) throws ISPACException{
		
		String nombre = getCampoUsuarioData(cct, nombreUsuario, "NOMBRE");
		String apellidos = getCampoUsuarioData(cct, nombreUsuario, "APELLIDOS");
		return getNombreCompleto(nombre, apellidos);
	}
	
	/**
	 * @param nombre
	 * @param apellidos
	 * @return
	 */
	public static String getNombreCompleto(String nombre, String apellidos){
		
		StringBuffer sbNombreCompleto = new StringBuffer("");
		if(!StringUtils.isEmpty(nombre)){
			sbNombreCompleto.append(nombre.trim());
		}
		if(!StringUtils.isEmpty(apellidos)){
			sbNombreCompleto.append(" ");
			sbNombreCompleto.append(apellidos.trim());
		}
		return sbNombreCompleto.toString();
	}
	
	/**
	 * Devuelve el Número de serie del certificado del usuario actual
	 * @param ctx
	 * @return
	 * @throws ISPACException
	 */
	public static String getNumeroSerie(ClientContext cct) throws ISPACException{
		
		return getCampoUsuarioData(cct, "ID_CERTIFICADO");
	}
}

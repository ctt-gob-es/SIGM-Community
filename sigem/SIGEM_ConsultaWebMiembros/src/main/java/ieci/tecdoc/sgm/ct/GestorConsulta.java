/**
 * @author Javier Septien Arceredillo
 * 
 * Fecha de Creación: 17-may-2007
 */


package ieci.tecdoc.sgm.ct;

import ieci.tecdoc.sgm.ct.database.ActasDatos;
import ieci.tecdoc.sgm.ct.database.ExpedienteDatos;
import ieci.tecdoc.sgm.ct.database.InteresadoDatos;
import ieci.tecdoc.sgm.ct.database.PropuestaDatos;
import ieci.tecdoc.sgm.ct.database.datatypes.Actas;
import ieci.tecdoc.sgm.ct.database.datatypes.Expedientes;
import ieci.tecdoc.sgm.ct.database.datatypes.Propuestas;
import ieci.tecdoc.sgm.ct.exception.ConsultaCodigosError;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import org.apache.log4j.Logger;

/**
 * Clase que gestiona toda la aplicación de Consulta Web de expedientes.
 * Pone en contacto la capa de presentacion con la capa de acceso a datos y
 * y tambien contiene todos los metodos para trabajar como API sin nescesidad
 * de trabajar directamente con los objetos que enmascaran los datos. 
 */

public class GestorConsulta {

	private static final Logger logger = Logger.getLogger(GestorConsulta.class);
			
	/**
	 * Constructor de clase
	 *
	 */
	public GestorConsulta(){

	}
	
	/**
	 * [Ticket #431 TCG IGEM nuevo proyecto para la visualización de los expedientes de organos colegiados]
	 * Recupera la información de los expedientes de un interesado por el NIF que entra como parámetro
	 * Cumpliendo que pertenezca a los miembros de organo colegiado y que el
	 * expediente este abierto el trámite de notificaciones y el de certificado de dictamenes
	 * @param sessionId Identificador de sesión de la aplicación llamante
	 * @param NIF NIF del interesado
	 * @return  Objeto expedientes con todos los expedientes de un interesado
	 * @throws Exception Si se produce algún error.
	 * 
	 * **/
	public static Expedientes consultarExpedientesMiembros (String sessionId, String NIF, String entidad, String nombreCertificado) throws ConsultaExcepcion {
		Expedientes expedientes = null; 
		try {
			InteresadoDatos interesadoDatos = new InteresadoDatos();
			interesadoDatos.setNIF(NIF);
			interesadoDatos.setNombre(nombreCertificado);
			interesadoDatos.cargarExpedientesPorNIFMiembro(true, entidad);
			expedientes = interesadoDatos.getExpedientes();
		} catch(ConsultaExcepcion ce) {
			logger.error("Error al realizar consulta [consultarExpedientes][ConsultaExcepcion] "+ce.getMessage(), ce);
			throw ce;
		} catch(Exception e) {
			logger.error("Error al realizar consulta [consultarExpedientes][Excepcion] "+e.getMessage(), e);
			throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES, e.getCause());
		}
		return expedientes;
	}
	
	/**
	 * [Ticket #431 TCG IGEM nuevo proyecto para la visualización de los expedientes de organos colegiados]
	 * Recupera la información de los expedientes de un interesado por el NIF que entra como parámetro
	 * Cumpliendo que pertenezca a los miembros de organo colegiado y que el
	 * expediente este abierto el trámite de notificaciones y el de certificado de dictamenes
	 * Metodo para que vuelva
	 * @param sessionId Identificador de sesión de la aplicación llamante
	 * @param NIF NIF del interesado
	 * @return  Objeto expedientes con todos los expedientes de un interesado
	 * @throws Exception Si se produce algún error.
	 * 
	 * **/
	public static Propuestas consultarPropuesta (String NIF, String entidad, String numexp) throws ConsultaExcepcion {
		Propuestas propuestas = null; 
		try {
			PropuestaDatos propuestaDatos = new PropuestaDatos();
			propuestaDatos.setDni(NIF);
			propuestaDatos.cargarPropuestas(entidad, numexp);
			propuestas = propuestaDatos.getDocumentosPropuesta();
		} catch(ConsultaExcepcion ce) {
			logger.error("Error al realizar consulta [consultarExpedientes][ConsultaExcepcion] "+ce.getMessage(), ce);
			throw ce;
		} catch(Exception e) {
			logger.error("Error al realizar consulta [consultarExpedientes][Excepcion] "+e.getMessage(), e);
			throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES, e.getCause());
		}
		return propuestas;
	}
	
	/**
	 * Recupera el listado de actas de pleno con audio que se encuentran firmandas
	 * **/
	
	public static Actas getActas (String NIF, String entidad) throws ConsultaExcepcion {
		ActasDatos actasDatos = new ActasDatos();
		try {
			
			actasDatos.setDni(NIF);
			actasDatos.cargarActasPorNIFMiembro(entidad);
		} catch(ConsultaExcepcion ce) {
			logger.error("Error al realizar consulta [consultarExpedientes][ConsultaExcepcion] "+ce.getMessage(), ce);
			throw ce;
		} catch(Exception e) {
			logger.error("Error al realizar consulta [consultarExpedientes][Excepcion] "+e.getMessage(), e);
			throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES, e.getCause());
		}
		return actasDatos.getActas();
	}
	
	public static Expedientes busquedaExpedientes (String sessionId, String NIF, String fechaDesde, String fechaHasta, String operadorConsulta, String estado, String entidad) throws ConsultaExcepcion {
		try {
			ExpedienteDatos expDatos = new ExpedienteDatos();
			Expedientes expedientes = null;
			boolean bEstado = true, bFecha = true;
			
			if (estado == null || "".equals(estado))
				bEstado = false;
			if (operadorConsulta == null || "".equals(operadorConsulta))
				bFecha = false;
			
			if (bEstado && bFecha)
				expedientes = expDatos.cargarExpedientesPorNIFEstadoYFechas(NIF, fechaDesde, fechaHasta, operadorConsulta, estado, entidad);
			else if (bEstado)
				expedientes = expDatos.cargarExpedientesPorNIFYEstado(NIF, estado, entidad);
			else if (bFecha)
				expedientes = expDatos.cargarExpedientesPorNIFYFecha(NIF, fechaDesde, fechaHasta, operadorConsulta, entidad);
			else if (!bEstado && !bFecha)
				expedientes = expDatos.cargarExpedientesPorNIFYFecha(NIF, fechaDesde, fechaHasta, operadorConsulta, entidad);
			return expedientes;
		} catch(ConsultaExcepcion ce) {
			logger.error("Error al realizar busqueda [busquedaExpedientes][ConsultaExcepcion] "+ce.getMessage(), ce);
			throw ce;
		} catch(Exception e) {
			logger.error("Error al realizar busqueda [busquedaExpedientes][Excepcion] "+e.getMessage(), e);
			throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTES, e.getCause());
		}
	}
	
	/**
	 * Metodo de tipo get que devuelve la URL aportación (subsanación) de expedientes 
	 * y que es configurable desde el fichero de properties ieci.tecdoc.sgm.ct.resources.application.
	 * @return URL donde se puede hacer una subsanación de un expediente
	 * @throws Exception
	 */
	public static String obtenerURLAportacionExpedientes () throws ConsultaExcepcion {
		try {
			return Configuracion.getURLAportacion();
		} catch(Exception e) {
			logger.error("Error al obtener URL de subsanacion [obtenerURLAportacionExpedientes][Excepcion] "+e.getMessage(), e);
			throw new ConsultaExcepcion(ConsultaCodigosError.EC_OBTENER_URL_APORTACION, e.getCause());
		}
	}
	
	/**
	 * Metodo de tipo get que devuelve la URL para consultar notificaciones de expedientes 
	 * y que es configurable desde el fichero de properties ieci.tecdoc.sgm.ct.resources.application.
	 * @return URL para consultar notificaciones de expedientes
	 * @throws Exception
	 */
	public static String obtenerURLNotificacionExpedientes () throws ConsultaExcepcion {
		try {
			return Configuracion.getURLNotificacion();
		} catch(Exception e) {
			logger.error("Error al obtener URL de notificacion  [obtenerURLNotificacionExpedientes][Excepcion] "+e.getMessage(), e);
			throw new ConsultaExcepcion(ConsultaCodigosError.EC_OBTENER_URL_NOTIFICACION, e.getCause());
		}
	}
	
}

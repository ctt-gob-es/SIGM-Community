package ieci.tecdoc.sgm.core.services.tramitacion;

import ieci.tecdoc.sgm.core.services.tramitacion.dto.DatosComunesExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.DocumentoExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.Expediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InfoBExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InfoBProcedimiento;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InfoFichero;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InfoOcupacion;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.Procedimiento;

import java.util.Date;


/**
 * Interfaz para el servicio de Tramitaci�n.  
 * 
 */
public interface ServicioTramitacion {

	/**
	 * Recupera la lista de procedimientos del tipo que se indica en 
	 * tipoProc, con su informaci�n b�sica.
	 * @param tipoProc Tipo de procedimiento.
	 * <p>Valores posibles de tipoProc (@see InfoBProcedimiento):
	 * <li>1 - Todos</li>
	 * <li>2 - Procedimientos  automatizados</li>
	 * <li>3 - Procedimientos no automatizados</li>
	 * </p>
	 * @param nombre Nombre del procedimiento.
	 * @return Lista de informaci�n de procedimientos.
	 */
	public InfoBProcedimiento[] getProcedimientos(String idEntidad, int tipoProc, String nombre) 
		throws TramitacionException;

	/**
	 * Recupera la lista de procedimientos cuyos identificadores se 
	 * incluyen en el par�metro idProcs.
	 * @param idProcs Lista de identificadores de procedimientos.
	 * @return Lista de informaci�n de procedimientos.
	 */
	public InfoBProcedimiento[] getProcedimientos(String idEntidad, String[] idProcs) 
		throws TramitacionException;

	/**
	 * Recupera la informaci�n de un procedimiento cuyo identificador 
	 * �nico es idProc.
	 * @param idProc Identificador de procedimiento.
	 * @return Informaci�n del procedimiento.
	 */
	public Procedimiento getProcedimiento(String idEntidad, String idProc) 
		throws TramitacionException;

    /**
     * Obtiene el contenido del documento.
     * @param guid GUID del documento
     * @return Contenido del documento.
     * @throws TramitacionException si ocurre alg�n error.
     */
    public byte [] getFichero(String idEntidad, String guid) throws TramitacionException;

	/**
	 * Obtiene la informaci�n de origen e integridad del documento.
	 * @param guid GUID del documento.
	 * @return Informaci�n del documento.
	 * @throws TramitacionException si ocurre alg�n error.
	 */
	public InfoFichero getInfoFichero(String idEntidad, String guid) 
		throws TramitacionException;

	/**
	 * Obtiene la informaci�n de ocupaci�n del repositorio.
	 * @return Informaci�n de ocupaci�n.
	 * @throws TramitacionException si ocurre alg�n error.
	 */
	public InfoOcupacion getInfoOcupacion(String idEntidad) throws TramitacionException;

	/**
	 * Elimina los documentos determinados por los GUIDs.
	 * @param guids Lista de GUIDs de los documentos.
	 * @throws TramitacionException si ocurre alg�n error.
	 */
	public void eliminaFicheros(String idEntidad, String[] guids) throws TramitacionException;

	/**
	 * Recupera los identificadores de los expedientes,  del procedimiento identificado 
	 * por idProc, que hayan finalizado en el rango de fechas comprendido entre fechaIni
	 * y fechaFin ordenados por lo que indique el par�metro tipoOrd.
	 * @param idProc Identificador del procedimiento.
	 * @param fechaIni Fecha de inicio.
	 * @param fechaFin Fecha de fin.
	 * @param tipoOrd Tipo de ordenaci�n.
	 * <p>Valores posibles:
	 * <li>1 - N�mero de expediente</li>
	 * <li>2 - Fecha finalizaci�n</li>
	 * </p>
	 * @return Lista de identificadores de expedientes.
	 */
	public String[] getIdsExpedientes(String idEntidad, String idProc, Date fechaIni,
			Date fechaFin, int tipoOrd) throws TramitacionException;

	/**
	 * Recupera la lista de expedientes cuyos identificadores se incluyen en 
	 * el par�metro idExps.
	 * @param idExps Identificadores de expedientes.
	 * @return Lista de expedientes.
	 */
	public InfoBExpediente[] getExpedientes(String idEntidad, String[] idExps) 
		throws TramitacionException;

	/**
	 * Recupera la informaci�n de un expediente cuyo identificador �nico es idExp.
	 * @param idExp Identificador del expediente.
	 * @return Informaci�n de un expediente.
	 */
	public Expediente getExpediente(String idEntidad, String idExp) throws TramitacionException;
		
	/**
	 * Modifica el estado de los expedientes recibidos como par�metro a estado archivado
	 * @param idExps  Array de String con los expedientes que se quieren pasar al estado archivado
	 * @throws ISPACException
	 */
	public void archivarExpedientes(String identidad, String []idExps )throws TramitacionException;

	/**
     * Crear un expediente.
     * @param idEntidad Identificador de la entidad.
     * @param datosComunes Datos comunes para todos los expedientes.
     * @param datosEspecificos XML con los datos espec�ficos del expediente.
     * @param documentos Lista de documentos asociados al expediente.
     * @return Cierto si el expediente se ha creado correctamente.
     * @throws TramitacionException si se produce alg�n error.
     */
    public boolean iniciarExpediente(String idEntidad, DatosComunesExpediente datosComunes, 
    		String datosEspecificos, DocumentoExpediente[] documentos) 
    	throws TramitacionException;

    /**
     * Crear un expediente.
     * @param idEntidad Identificador de la entidad.
     * @param datosComunes Datos comunes para todos los expedientes.
     * @param datosEspecificos XML con los datos espec�ficos del expediente.
     * @param documentos Lista de documentos asociados al expediente.
     * @param initSytem Sistema externo desdel el que se inicia el expediente
     * @return Numero de expediente creado
     * @throws ISPACException si se produce alg�n error.
     */
    public String iniciarExpediente(String idEntidad, DatosComunesExpediente datosComunes, 
    		String datosEspecificos, DocumentoExpediente[] documentos, String initSystem) 
    	throws TramitacionException;        
    
    
    /**
     * A�ade documentos al tr�mite de un expediente.
     * @param idEntidad Identificador de la entidad.
     * @param numReg N�mero de registro de entrada.
     * @param fechaReg Fecha de registro de entrada.
     * @param documentos Lista de documentos asociados al expediente.
     * @return Cierto si los documentos se han creado correctamente.
     * @throws TramitacionException Si se produce alg�n error.
     */
    public boolean anexarDocsExpediente(String idEntidad, String numExp, String numReg, 
    		Date fechaReg, DocumentoExpediente[] documentos) 
    	throws TramitacionException;


    /**
     * Cambia el estado administrativo de un expediente
     * @param idEntidad Identificador de la entidad.
     * @param numExp N�mero de expediente.
     * @param estadoAdm Nuevo estado administrativo
     * @return Cierto si la operaci�n se ha realizado con �xito, falso en caso contrario
     * @throws ISPACException
     */
    public boolean cambiarEstadoAdministrativo(String idEntidad, String numExp, String estadoAdm) 
    	throws TramitacionException;
    
    
    /**
     * Mueve un expedinete a una fase seg�n el identificador de la misma en el cat�logo. 
     * @param idEntidad Identificador de la entidad.
     * @param numExp N�mero de expediente.
     * @param idFaseCatalogo Identificador de la fase en el cat�logo
     * @return Cierto si la operaci�n se ha realizado con �xito, falso en caso contrario
     * @throws ISPACException
     */
    public boolean moverExpedienteAFase(String idEntidad, String numExp, String idFaseCatalogo)
    	throws TramitacionException;

    /**
     * Busqueda avanzada sobre expedientes
     * @param idEntidad Identificador de la entidad.
     * @param groupName Nombre de grupo
     * @param searchFormName Nombre del formulario de busqueda a utilizar
     * @param searchXML XML con los criterios de busqueda
     * @param domain dominio de busqueda (en funcion de la responsabilidad)
     * @return Resultado de la busqueda
     * @throws ISPACException
     */
    public String busquedaAvanzada(String idEntidad, String nombreGrupo, String nombreFrmBusqueda, String xmlBusqueda, int dominio)throws TramitacionException;
    
    /**
     * Inserta o actualiza los datos de un registro de una entidad para un expediente
     * @param idEntidad Identificador de la entidad.
     * @param nombreEntidad Nombre de entidad en BBDD
     * @param numExp N�mero de expediente
     * @param xmlDatosEspecificos Datos especificos para completar los campos del registro a crear
     * @return identificador del registro creado
     * @throws ISPACException
     */
    public int establecerDatosRegistroEntidad(String idEntidad, String nombreEntidad, String numExp, String xmlDatosEspecificos)throws TramitacionException;
    
    /**
     * Obtiene los datos de un registro de una entidad para un expediente
     * @param idEntidad Identificador de la entidad.
     * @param nombreEntidad Nombre de entidad en BBDD
     * @param numExp N�mero de expediente
     * @param idRegistro Identificdaor del registro
     * @return Informaci�n del registro obtenido
     * @throws ISPACException
     */
    public String obtenerRegistroEntidad(String idEntidad, String nombreEntidad, String numExp, int idRegistro)throws TramitacionException;
    
    /**
     * Obtiene los datos de todos los registros de una entidad para un expediente
     * @param idEntidad Identificador de la entidad.
     * @param nombreEntidad Nombre de entidad en BBDD
     * @param numExp N�mero de expediente
     * @param idRegistro Identificdaor del registro
     * @return Informaci�n del registro obtenido
     * @throws ISPACException
     */
    public String obtenerRegistrosEntidad(String idEntidad, String nombreEntidad, String numExp)throws TramitacionException;

    /**
     * Permite recibir un documento firmado
     * @param idEntidad identificador de la entidad
     * @param numExp numero de expediente
     * @param idDocumento identificador del documento
      * @return identificador del nuevo documento generado
     * @throws TramitacionException
     */
    public String recibirDocumentoFirmado(String idEntidad, String numExp, String idDocumento)throws TramitacionException;
    
    /**
	 * [dipucr-Felipe #1246] 
     * Realiza el rechazo de un documento
     * @param idEntidad identificador de la entidad
     * @param numExp numero de expediente
     * @param idDocumento identificador del documento
     * @param motivo Motivo de rechazo
     * @param idUsuario Identificador del usuario
     * @param nombreUsuario Nombre del usuario
     * @return identificador del nuevo documento rechazado
     * @throws TramitacionException
     */
	public String rechazarDocumento(String idEntidad, String numExp, String idDocumento,
			String motivo, String idUsuario, String nombreUsuario) throws TramitacionException;
    
    /**
     * [Dipucr-Agustin #781]
     * Obtiene el contenido del documento temporal.
     * @param guid GUID del documento
     * @param idEntidad codigo de entidad de sigem
     * @return Contenido del documento.
     * @throws TramitacionException si ocurre alg�n error.
     */
    public byte [] getFicheroTemp(String idEntidad, String guid) throws TramitacionException;
    
    /**
     * [Dipucr-Agustin #781]
     * Guarda el contenido de un byte array en un fichero temporal.
     * @param data array de bytes que representa el documento a guardar. 
     * @param guid GUID del documento.
     * @param idEntidad codigo de entidad de sigem.
     * @param data contenido del documento.
     * @return boolean resultado de la operacion.
     * @throws TramitacionException si ocurre alg�n error.
     */
    public boolean setFicheroTemp(String idEntidad, String guid, byte[] data) throws TramitacionException;
    
    /**
     * [Dipucr-Agustin #1297]
     * Obtiene el contenido del justificante de firma que genera sigem a partir de la firma de portafirmas
     * @param guid GUID del documento
     * @param identidad GUID idEntidad
     * @return Contenido del documento.
     */
    public byte [] getJustificanteFirma(String idEntidad, String guid) throws TramitacionException;

    /**
     * [dipucr-Felipe #563]
     * @param idEntidad
     * @param entidad
     * @param query
     * @return
     * @throws TramitacionException
     */
    public String[] queryEntities(String idEntidad, String entidad, String consulta) throws TramitacionException;
    
    /**
     * [dipucr-Felipe #860 WE#153]
     * @param idEntidad
     * @param numexp
     * @return
     * @throws TramitacionException
     */
	public boolean anularLicenciaRRHH(String idEntidad, String numexp) throws TramitacionException;
	
	/**
     * [dipucr-Felipe #1771]
     * @param idEntidad
     * @param numexp
     * @return
     * @throws TramitacionException
     */
	public boolean anularSolicitudPortalEmpleado(String idEntidad, String numexp, String documentoAnular) throws TramitacionException;
    
    /**
     * A�ade documentos al tr�mite de un expediente.
     * @param numExp N�mero de expediente.
     * @param idTramite Identificador del tr�mite al que anexar los documentos.
     * @param numReg N�mero de registro de entrada.
     * @param fechaReg Fecha de registro de entrada.
     * @param documentos Lista de documentos asociados al expediente.
     * @return Cierto si los documentos se han creado correctamente.
     * @throws TramitacionException Si se produce alg�n error.
     */
    public boolean anexarDocsTramite(String idEntidad, String numExp, int idTramite, String numReg, Date fechaReg, DocumentoExpediente[] documentos) throws TramitacionException;

    /***
     * Hace una consulta a la tabla expedientes en BBDD
     * @param string
     * @return
     */
	public String[] queryExpedientes(String idEntidad, String consutla)throws TramitacionException;
	
	/***
	 * Crea Tr�mite en un expediente
	 * @param idEntidad
	 * @param codTramite
	 * @param numExp
	 * @return
	 * @throws TramitacionException
	 */
	public int creaTramiteByCod(String idEntidad, String codTramite, String numExp) throws TramitacionException;
	
	/***
	 * Inserta un apoderamiento en la pesata�a de los apoderamientos del expediente
	 * @param idEntidad
	 * @param numExp
	 * @param representado
	 * @param representante
	 * @return
	 * @throws TramitacionException
	 */
	public boolean insertaApoderamiento(String idEntidad, String numExp, String representado, String representante) throws TramitacionException;
	
	/**
     * [dipucr-Felipe #1745]
     * @param idEntidad
     * @param password
     * @param nombreDocumento
     * @param documento
     * @return
     * @throws TramitacionException
     */
	public byte[] sellarDocumento(String idEntidad, String password, String nombreDocumento, byte[] documento) throws TramitacionException;

}

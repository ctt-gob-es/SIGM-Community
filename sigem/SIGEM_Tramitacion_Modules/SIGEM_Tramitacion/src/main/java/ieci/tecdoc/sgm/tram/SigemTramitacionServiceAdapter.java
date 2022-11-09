package ieci.tecdoc.sgm.tram;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.sign.ISignConnector;
import ieci.tdw.ispac.ispaclib.sign.SignConnectorFactory;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.services.mgr.CatalogoManager;
import ieci.tdw.ispac.services.mgr.CustodiaManager;
import ieci.tdw.ispac.services.mgr.TramitacionManager;
import ieci.tecdoc.sgm.core.services.tramitacion.ServicioTramitacion;
import ieci.tecdoc.sgm.core.services.tramitacion.TramitacionException;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.DatosComunesExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.DocumentoExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.Expediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InfoBExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InfoBProcedimiento;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InfoFichero;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InfoOcupacion;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.InteresadoExpediente;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.Procedimiento;
import ieci.tecdoc.sgm.tram.sign.PortafirmasMinhapSignConnector;
import ieci.tecdoc.sgm.tram.vo.SGMExpedienteVO;
import ieci.tecdoc.sgm.tram.vo.SGMInfoBExpedienteVO;
import ieci.tecdoc.sgm.tram.vo.SGMInfoBProcedimientoVO;
import ieci.tecdoc.sgm.tram.vo.SGMInfoFicheroVO;
import ieci.tecdoc.sgm.tram.vo.SGMInfoOcupacionVO;
import ieci.tecdoc.sgm.tram.vo.SGMProcedimientoVO;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * Implementación del servicio de Tramitación de SIGEM.
 *
 */
public class SigemTramitacionServiceAdapter implements ServicioTramitacion {

	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(SigemTramitacionServiceAdapter.class);

	
	private void setOrganizationUserInfo(String idEntidad){
		OrganizationUserInfo info = new OrganizationUserInfo();
		info.setOrganizationId(idEntidad);
		info.getSpacPoolName();
		OrganizationUser.setOrganizationUserInfo(info);
	}
	
	/**
	 * Recupera la lista de procedimientos del tipo que se indica en 
	 * tipoProc, con su información básica.
	 * @param tipoProc Tipo de procedimiento.
	 * <p>Valores posibles de tipoProc (@see InfoBProcedimiento):
	 * <li>1 - Todos</li>
	 * <li>2 - Procedimientos  automatizados</li>
	 * <li>3 - Procedimientos no automatizados</li>
	 * </p>
	 * @param nombre Nombre del procedimiento.
	 * @return Lista de información de procedimientos.
	 */
	public InfoBProcedimiento[] getProcedimientos(String idEntidad, 
			int tipoProc, String nombre) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return SGMInfoBProcedimientoVO.getInstances(
					CatalogoManager.getInstance().getProcedimientos(tipoProc, nombre));
		} catch (Throwable e){
			logger.error("Error inesperado al obtener los procedimientos [tipoProc="
					+ tipoProc + ", nombre=" + nombre + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * Recupera la lista de procedimientos cuyos identificadores se 
	 * incluyen en el parámetro idProcs.
	 * @param idProcs Lista de identificadores de procedimientos.
	 * @return Lista de información de procedimientos.
	 */
	public InfoBProcedimiento[] getProcedimientos(String idEntidad, String[] idProcs) 
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);	
			return SGMInfoBProcedimientoVO.getInstances(
					CatalogoManager.getInstance().getProcedimientos(idProcs));
		} catch (Throwable e){
			logger.error("Error inesperado al obtener los procedimientos [idProcs="
					+ idProcs + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * Recupera la información de un procedimiento cuyo identificador 
	 * único es idProc.
	 * @param idProc Identificador de procedimiento.
	 * @return Información del procedimiento.
	 */
	public Procedimiento getProcedimiento(String idEntidad, String idProc) 
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return SGMProcedimientoVO.getInstance(CatalogoManager.getInstance().getProcedimiento(idProc));
		} catch (Throwable e){
			logger.error("Error inesperado al obtener el procedimiento [idProc="
					+ idProc + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

    /**
     * Obtiene el contenido del documento.
     * @param guid GUID del documento
     * @return Contenido del documento.
     * @throws TramitacionException si ocurre algún error.
     */
    public byte [] getFichero(String idEntidad, String guid) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return CustodiaManager.getInstance().getFichero(guid);
		} catch (Throwable e){
			logger.error("Error inesperado al obtener el contenido del fichero[guid="
					+ guid + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
    }
    
    
    /**
     * [Dipucr-Agustin #1297]
     * Obtiene el contenido del justificante de firma que genera sigem a partir de la firma de portafirmas
     * @param guid GUID del documento
     * @param identidad GUID idEntidad
     * @return Contenido del documento.
     */
    public byte [] getJustificanteFirma(String idEntidad, String guid) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return CustodiaManager.getInstance().getFicheroJustificanteFirma(guid);
		} catch (Throwable e){
			logger.error("Error inesperado al obtener el contenido del fichero[guid="
					+ guid + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
    }

	/**
	 * Obtiene la información de origen e integridad del documento.
	 * @param guid GUID del documento.
	 * @return Información del documento.
	 * @throws TramitacionException si ocurre algún error.
	 */
	public InfoFichero getInfoFichero(String idEntidad, String guid) 
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return SGMInfoFicheroVO.getInstance(CustodiaManager.getInstance().getInfoFichero(guid));
		} catch (Throwable e){
			logger.error("Error inesperado al obtener la información del fichero[guid="
					+ guid + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * Obtiene la información de ocupación del repositorio.
	 * @return Información de ocupación.
	 * @throws TramitacionException si ocurre algún error.
	 */
	public InfoOcupacion getInfoOcupacion(String idEntidad) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return SGMInfoOcupacionVO.getInstance(CustodiaManager.getInstance().getInfoOcupacion());
		} catch (Throwable e){
			logger.error("Error inesperado al obtener la información de ocupación", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * Elimina los documentos determinados por los GUIDs.
	 * @param guids Lista de GUIDs de los documentos.
	 * @throws TramitacionException si ocurre algún error.
	 */
	public void eliminaFicheros(String idEntidad, String[] guids) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			CustodiaManager.getInstance().eliminaFicheros(guids);
		} catch (Throwable e){
			logger.error("Error inesperado al eliminar los ficheros [guids="
					+ guids + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * Recupera los identificadores de los expedientes,  del procedimiento identificado 
	 * por idProc, que hayan finalizado en el rango de fechas comprendido entre fechaIni
	 * y fechaFin ordenados por lo que indique el parámetro tipoOrd.
	 * @param idProc Identificador del procedimiento.
	 * @param fechaIni Fecha de inicio.
	 * @param fechaFin Fecha de fin.
	 * @param tipoOrd Tipo de ordenación.
	 * <p>Valores posibles:
	 * <li>1 - Número de expediente</li>
	 * <li>2 - Fecha finalización</li>
	 * </p>
	 * @return Lista de identificadores de expedientes.
	 */
	public String[] getIdsExpedientes(String idEntidad, String idProc, Date fechaIni,
			Date fechaFin, int tipoOrd) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().getIdsExpedientes(idProc, 
					fechaIni, fechaFin, tipoOrd);
		} catch (Throwable e){
			logger.error("Error inesperado al obtener los identificadores de expedientes [idProc="
					+ idProc + ", fechaIni=" + fechaIni + ", fechaFin=" 
					+ fechaFin + ", tipoOrd=" + tipoOrd + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * Recupera la lista de expedientes cuyos identificadores se incluyen en 
	 * el parámetro idExps.
	 * @param idExps Identificadores de expedientes.
	 * @return Lista de expedientes.
	 */
	public InfoBExpediente[] getExpedientes(String idEntidad, String[] idExps) 
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return SGMInfoBExpedienteVO.getInstances(TramitacionManager.getInstance().getExpedientes(idExps));
		} catch (Throwable e){
			logger.error("Error inesperado al obtener los expedientes [idExps="
					+ idExps + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * Recupera la información de un expediente cuyo identificador único es idExp.
	 * @param idExp Identificador del expediente.
	 * @return Información de un expediente.
	 */
	public Expediente getExpediente(String idEntidad, String idExp) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return SGMExpedienteVO.getInstance(TramitacionManager.getInstance().getExpediente(idExp));
		} catch (Throwable e){
			logger.error("Error inesperado al obtener el expediente [idExp="
					+ idExp + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * Modifica el estado de los expedientes recibidos como parámetro a estado archivado
	 * @param idExps  Array de String con los expedientes que se quieren pasar al estado archivado
	 */
	public void archivarExpedientes(String idEntidad, String[] idExps) throws TramitacionException {
		
		try {
			setOrganizationUserInfo(idEntidad);
			TramitacionManager.getInstance().archivarExpedientes(idExps);
		} catch (Throwable e){
			logger.error("Error inesperado al archivar los expedientes[idExps="
					+ StringUtils.join(idExps, ",") + "]", e);
			throw new TramitacionException(TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}
		
    /**
     * Iniciar un expediente.
     * 
     * @param datosComunes Datos comunes para todos los expedientes.
     * @param datosEspecificos XML con los datos específicos del expediente.
     * @param documentos Lista de documentos asociados al expediente.
     * @return Cierto si el expediente se ha iniciado correctamente.
     * @throws TramitacionException Si se produce algún error al iniciar el expediente.
     */
    public boolean iniciarExpediente(String idEntidad, DatosComunesExpediente datosComunes, 
    			String datosEspecificos, DocumentoExpediente[] documentos) 
    		throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().iniciarExpediente(
					toInternalForm(datosComunes), datosEspecificos, toInternalForm(documentos));
		} catch (Throwable e){
			logger.error("Error inesperado al iniciar el expediente [datosComunes="
					+ datosComunes + ", datosEspecificos=" + datosEspecificos 
					+ ", documentos=" + documentos + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
    }
    
    
    /**
     * Iniciar un expediente.
     * 
     * @param commonData Datos comunes para todos los expedientes.
     * @param specificDataXML XML con los datos específicos del expediente.
     * @param documents Lista de documentos asociados al expediente.
     * @param initSystem Sistema externo desde el que se inicia el expediente 
     * @return Numero de expediente creado
     * @throws ISPACException Si se produce algún error al iniciar el expediente.
     */
    public String iniciarExpediente(String idEntidad, DatosComunesExpediente datosComunes, 
			String datosEspecificos, DocumentoExpediente[] documentos, String initSystem) 
		throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().iniciarExpediente(
					toInternalForm(datosComunes), datosEspecificos, toInternalForm(documentos),initSystem);
		} catch (Throwable e){
			logger.error("Error inesperado al iniciar el expediente [datosComunes="
					+ datosComunes + ", datosEspecificos=" + datosEspecificos 
					+ ", documentos=" + documentos + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
    }

    
    
    
    /**
     * Añade documentos al trámite de un expediente.
     * @param numExp Número de expediente.
     * @param numReg Número de registro de entrada.
     * @param fechaReg Fecha de registro de entrada.
     * @param documentos Lista de documentos asociados al expediente.
     * @return Cierto si los documentos se han creado correctamente.
     * @throws TramitacionException Si se produce algún error.
     */
    public boolean anexarDocsExpediente(String idEntidad, String numExp, String numReg, 
    			Date fechaReg, DocumentoExpediente[] documentos) 
    		throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().anexarDocsExpediente(
					numExp, numReg, fechaReg, toInternalForm(documentos));
		} catch (Throwable e){
			logger.error("Error inesperado al anexar documentos al expediente [numExp="
					+ numExp + ", numReg=" + numReg + ", fechaReg=" + fechaReg 
					+ ", documentos=" + documentos + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
    	
    }
    
	private static ieci.tdw.ispac.services.dto.DatosComunesExpediente toInternalForm(DatosComunesExpediente obj) {
		ieci.tdw.ispac.services.dto.DatosComunesExpediente res = null;
		
		if (obj != null) {
			res = new ieci.tdw.ispac.services.dto.DatosComunesExpediente();

			res.setIdOrganismo(obj.getIdOrganismo());
			res.setTipoAsunto(obj.getTipoAsunto());
			res.setNumeroRegistro(obj.getNumeroRegistro());
			res.setFechaRegistro(obj.getFechaRegistro());
			res.setInteresados(toInternalForm(obj.getInteresados()));
		}
		
		return res;
	}

    private static ieci.tdw.ispac.services.dto.InteresadoExpediente[] toInternalForm(InteresadoExpediente[] objs) {
    	ieci.tdw.ispac.services.dto.InteresadoExpediente[] res = null;
		
		if (objs != null) {
			res = new ieci.tdw.ispac.services.dto.InteresadoExpediente[objs.length];
			for (int i = 0; i < objs.length; i++) {
				res[i] = toInternalForm(objs[i]);
			}
		}
		
		return res;
    }

	private static ieci.tdw.ispac.services.dto.InteresadoExpediente toInternalForm(InteresadoExpediente obj) {
		ieci.tdw.ispac.services.dto.InteresadoExpediente res = null;
		
		if (obj != null) {
			res = new ieci.tdw.ispac.services.dto.InteresadoExpediente();
			res.setThirdPartyId(obj.getThirdPartyId());
			res.setIndPrincipal(obj.getIndPrincipal());
			res.setNifcif(obj.getNifcif());
			res.setName(obj.getName());
			res.setPostalAddress(obj.getPostalAddress());
			res.setPostalCode(obj.getPostalCode());
			res.setPlaceCity(obj.getPlaceCity());
			res.setRegionCountry(obj.getRegionCountry());
			res.setTelematicAddress(obj.getTelematicAddress());
			res.setNotificationAddressType(obj.getNotificationAddressType());
			res.setPhone(obj.getPhone());
			res.setMobilePhone(obj.getMobilePhone());
		}
		
		return res;
	}

    private static ieci.tdw.ispac.services.dto.DocumentoExpediente[] toInternalForm(DocumentoExpediente[] objs) {
    	ieci.tdw.ispac.services.dto.DocumentoExpediente[] res = null;
		
		if (objs != null) {
			res = new ieci.tdw.ispac.services.dto.DocumentoExpediente[objs.length];
			for (int i = 0; i < objs.length; i++) {
				res[i] = toInternalForm(objs[i]);
			}
		}
		
		return res;
    }
    
	private static ieci.tdw.ispac.services.dto.DocumentoExpediente toInternalForm(DocumentoExpediente obj) {
		ieci.tdw.ispac.services.dto.DocumentoExpediente res = null;
		
		if (obj != null) {
			res = new ieci.tdw.ispac.services.dto.DocumentoExpediente();
			res.setId(obj.getId());
			res.setCode(obj.getCode());
			res.setName(obj.getName());
			res.setExtension(obj.getExtension());
			res.setLenght(obj.getLenght());
			res.setContent(obj.getContent());
		}
		
		return res;
	}

	public boolean cambiarEstadoAdministrativo(String idEntidad, String numExp,
			String estadoAdm) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance()
					.cambiarEstadoAdministrativo(numExp, estadoAdm);
		} catch (Throwable e) {
			logger.error("Error inesperado al cambiar estado administrativo ("
					+ estadoAdm + ") del expediente '" + numExp + "'", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	public boolean moverExpedienteAFase(String idEntidad, String numExp,
			String idFaseCatalogo) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().moverExpedienteAFase(
					numExp, idFaseCatalogo);
		} catch (Throwable e) {
			logger.error("Error inesperado al mover el expediente '" + numExp
					+ "' a la fase cuyo identificador en catalogo es '"
					+ idFaseCatalogo + "'", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	public String busquedaAvanzada(String idEntidad, String nombreGrupo,
			String nombreFrmBusqueda, String xmlBusqueda, int dominio)
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().busquedaAvanzada(nombreGrupo, nombreFrmBusqueda, xmlBusqueda, dominio);
		} catch (Throwable e) {
			logger.error("Error al realizar busqueda avanzada", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	public int establecerDatosRegistroEntidad(String idEntidad,
			String nombreEntidad, String numExp, String xmlDatosEspecificos)
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().establecerDatosRegistroEntidad(nombreEntidad, numExp, xmlDatosEspecificos);
		} catch (Throwable e) {
			logger.error("Error al establecer datos de registro en la entidad '" + nombreEntidad + "'", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}
	
	public String[] queryExpedientes(String idEntidad, String consulta)throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().queryExpedientes(idEntidad, consulta);
		} catch (Throwable e) {
			logger.error("Error al obtener la consulta", e);
			throw new TramitacionException(TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}
	
	public int creaTramiteByCod(String idEntidad, String codTramite, String numExp)throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().creaTramiteByCod(codTramite, numExp);
		} catch (Throwable e) {
			logger.error("Error al obtener la consulta", e);
			throw new TramitacionException(TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	public String obtenerRegistroEntidad(String idEntidad,
			String nombreEntidad, String numExp, int idRegistro)
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().obtenerRegistroEntidad(nombreEntidad, numExp, idRegistro);
		} catch (Throwable e) {
			logger.error("Error al obtener datos de registro de la entidad '" + nombreEntidad + "'", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	public String obtenerRegistrosEntidad(String idEntidad,
			String nombreEntidad, String numExp) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().obtenerRegistrosEntidad(nombreEntidad, numExp);
		} catch (Throwable e) {
			logger.error("Error al obtener registros de la entidad '" + nombreEntidad + "'", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
    * {@inheritDoc}
    * @see ieci.tecdoc.sgm.core.services.tramitacion.ServicioTramitacion#recibirDocumentoFirmado(java.lang.String, java.lang.String, java.lang.String)
    */
	public String recibirDocumentoFirmado(String idEntidad, String numExp,
			String idDocumento) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			ClientContext ctx = TramitacionManager.getInstance().getContext();
			ctx.setLocale(new Locale("es", "ES"));
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			
			//[dipucr-Felipe #1326] Controlamos que el documento no haya sido ya firmado y esté en los históricos
			IItem itemDoc = getDocumento(idDocumento, entitiesAPI);
			
			if ((itemDoc!=null)&&(StringUtils.isEmpty(itemDoc.getString("INFOPAG_RDE")))){
				SignDocument signDocument = new SignDocument(itemDoc);
				ISignConnector signConnector = SignConnectorFactory.getInstance(idEntidad).getSignConnector();
				signConnector.initializate(signDocument, ctx);
				signConnector.sign(true);
				return itemDoc.getString("ID");
			}
			return null;

		} catch (Throwable e) {
			logger.error("Error al recibir un documento firmado '" + idEntidad + "'", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * [dipucr-Felipe #1326] Controlar documento en históricos
	 * @param idDocumento
	 * @param entitiesAPI
	 * @return
	 * @throws ISPACException
	 * @throws Exception
	 */
	private IItem getDocumento(String idDocumento, IEntitiesAPI entitiesAPI) throws ISPACException, Exception {
		
		IItem itemDoc = null;
		try{
			itemDoc = entitiesAPI.getDocument(Integer.parseInt(idDocumento));
		}
		catch(Exception ex){
			itemDoc = DocumentosUtil.getDocumento(entitiesAPI, Integer.parseInt(idDocumento));
			if (null != itemDoc && StringUtils.isNotEmpty(itemDoc.getString("INFOPAG_RDE"))){
				logger.info("El documento " + idDocumento + " ya está firmado en los históricos");
				return null;
			}
			else{
				throw ex;
			}
		}
		return itemDoc;
	}
	
	/**
	* [dipucr-Felipe #1246] 
    * {@inheritDoc}
    * @see ieci.tecdoc.sgm.core.services.tramitacion.ServicioTramitacion#recibirDocumentoFirmado(java.lang.String, java.lang.String, java.lang.String)
    */
	public String rechazarDocumento(String idEntidad, String numExp, String idDocumento,
			String motivo, String idUsuario, String nombreUsuario) throws TramitacionException{
		
		try {
			setOrganizationUserInfo(idEntidad);
			ClientContext ctx = TramitacionManager.getInstance().getContext();
			ctx.setLocale(new Locale("es", "ES"));
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			
			//[dipucr-Felipe #1326] Controlamos que el documento no haya sido ya firmado y esté en los históricos
			IItem itemDoc = getDocumento(idDocumento, entitiesAPI);
			
//			if ((itemDoc!=null)&&(StringUtils.isEmpty(itemDoc.getString("INFOPAG_RDE")))){ //[dipucr-Felipe #1326]
			if ((itemDoc!=null)&&(SignStatesConstants.RECHAZADO != itemDoc.getString("ESTADOFIRMA"))){
				
				SignDocument signDocument = new SignDocument(itemDoc);
//				ISignConnector signConnector = SignConnectorFactory.getSignConnector();
				PortafirmasMinhapSignConnector signConnector = new PortafirmasMinhapSignConnector();
				signConnector.initializate(signDocument, ctx);
				signConnector.rechazarDocumento(true, motivo, idUsuario, nombreUsuario);
				return itemDoc.getString("ID");
			}
			return null;

		} catch (Throwable e) {
			logger.error("Error al recibir un documento firmado '" + idEntidad + "'", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
	 * [DipuCR-Agustin #781]
     * Obtiene el contenido del documento temporal.
     * @param guid GUID del documento
     * @return Contenido del documento.
     * @throws TramitacionException si ocurre algún error.
     */
	public byte[] getFicheroTemp(String idEntidad, String guid)
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return CustodiaManager.getInstance().getFicheroTemp(guid);
		} catch (Throwable e){
			logger.error("Error inesperado al obtener el contenido del fichero[guid="
					+ guid + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}

	}
	
    /**
     * [DipuCR-Agustin #781]
     * Guarda el contenido del documento temporcd al.
     * @param guid GUID del documento
     * @return Path donde se ha guardado el fichero temporal.
     * @throws TramitacionException si ocurre algún error.
     */
	public boolean setFicheroTemp(String idEntidad, String guid, byte[] data)
			throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return CustodiaManager.getInstance().setFicheroTemp(guid, data); 			
		} catch (Throwable e){
			logger.error("Error inesperado al obtener el contenido del fichero[guid="
					+ guid + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}

	}
	
	/**
     * [dipucr-Felipe #563]
     * @param idEntidad
     * @param entidad
     * @param query
     * @return
     * @throws TramitacionException
     */
	@SuppressWarnings("unchecked")
	public String[] queryEntities(String idEntidad, String entidad, String query) throws TramitacionException{
		
		try {
			setOrganizationUserInfo(idEntidad);
			ClientContext ctx = TramitacionManager.getInstance().getContext();
			ctx.setLocale(new Locale("es", "ES"));
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			IItemCollection colItems = entitiesAPI.queryEntities(entidad, query);
			List<IItem> listEntities = colItems.toList();
			String[] arrXmlItems = new String[listEntities.size()];
			int i = 0;
			for (IItem item : listEntities){
//				arrXmlItems[i] = Base64Util.encodeString(item.getXmlValues());
				arrXmlItems[i] = item.getXmlValues();
				i++;
			}
			return arrXmlItems;
					
		} catch (Throwable e) {
			logger.error("Error al realizar la query de entidades. [idEntidad=" + idEntidad + "; "
					+ "entidad=" + entidad + "; query=" + query + "]", e);
			throw new TramitacionException(
					TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}
	
	/**
     * [dipucr-Felipe #860 WE#153]
     * @param idEntidad
     * @param numexp
     * @return
     * @throws TramitacionException
     */
	public boolean anularLicenciaRRHH(String idEntidad, String numexp) throws TramitacionException{
		
		try {
			final String NOMBRE_DOCUMENTO = "RRHH - Solicitud de Licencias";
			
			setOrganizationUserInfo(idEntidad);
			ClientContext ctx = TramitacionManager.getInstance().getContext();
			ctx.setLocale(new Locale("es", "ES"));
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ISignAPI signAPI = invesflowAPI.getSignAPI();
			
			IItem itemExpediente = entitiesAPI.getExpedient(numexp);
			String asunto = itemExpediente.getString("ASUNTO");
			asunto = "[Anulado a solicitud del usuario] " + asunto;
			itemExpediente.set("ASUNTO", asunto);
			itemExpediente.store(ctx);
			
			IItemCollection colDocumentos = DocumentosUtil.getDocumentsByNombre(ctx, numexp, NOMBRE_DOCUMENTO, "");
			if (colDocumentos.toList().size() > 0){
				IItem itemDocumento = colDocumentos.value();
				int documentId = itemDocumento.getKeyInt();
				
				//Borramos los pasos de firma pendientes del documento
				//INICIO [dipucr-Felipe #1503] Borrar/anular los documentos del portafirmas 
				if (entitiesAPI.isDocumentPortafirmas(itemDocumento)){
					if (SignStatesConstants.PENDIENTE_CIRCUITO_FIRMA.equals(itemDocumento.getString("ESTADOFIRMA"))){
						signAPI.deleteCircuitPortafirmas(documentId);
					}
				}
				else{
					signAPI.deleteStepsByDocument(documentId);
				}
				//FIN [dipucr-Felipe #1503]
				
				//Desmarcamos el documento como pendiente de firma
				itemDocumento.set("ESTADOFIRMA", SignStatesConstants.SIN_FIRMA);
				itemDocumento.store(ctx);
				
				//Cerramos trámites y expediente
				ExpedientesUtil.cerrarExpediente(ctx, numexp);
			}
			else{
				throw new Exception("Error al anular la licencia RRHH del expediente " + numexp +
						". No es posible localizar el documento de justificante que se envió a la firma.");
			}
			
			return true;
					
		} catch (Throwable e) {
			logger.error("Error al anular la licencia de RRHH del expediente " + numexp);
			throw new TramitacionException(TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}
	
	/**
     * [dipucr-Felipe #1771]
     * @param idEntidad
     * @param numexp
     * @param documentoAnular
     * @return
     * @throws TramitacionException
     */
	public boolean anularSolicitudPortalEmpleado(String idEntidad, String numexp, String documentoAnular) throws TramitacionException{
		
		try {
			setOrganizationUserInfo(idEntidad);
			ClientContext ctx = TramitacionManager.getInstance().getContext();
			ctx.setLocale(new Locale("es", "ES"));
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ISignAPI signAPI = invesflowAPI.getSignAPI();
			
			if (!org.apache.commons.lang.StringUtils.isEmpty(documentoAnular)){
				
				IItemCollection colDocumentos = DocumentosUtil.getDocumentsByNombre(ctx, numexp, documentoAnular, "");
				if (colDocumentos.toList().size() > 0){
					IItem itemDocumento = colDocumentos.value();
					int documentId = itemDocumento.getKeyInt();
				
					//Borrar/anular los documentos del portafirmas 
					if (entitiesAPI.isDocumentPortafirmas(itemDocumento)){
						if (SignStatesConstants.PENDIENTE_CIRCUITO_FIRMA.equals(itemDocumento.getString("ESTADOFIRMA"))){
							signAPI.deleteCircuitPortafirmas(documentId);
						}
					}
					else{
						signAPI.deleteStepsByDocument(documentId);
					}
				
					//Desmarcamos el documento como pendiente de firma
					itemDocumento.set("ESTADOFIRMA", SignStatesConstants.SIN_FIRMA);
					itemDocumento.store(ctx);
				}
				else{
					logger.warn("No se ha encontrado el documento: " + documentoAnular);
				}
			}
			
			//Modificamos el asunto del expediente
			IItem itemExpediente = entitiesAPI.getExpedient(numexp);
			String asunto = itemExpediente.getString("ASUNTO");
			final String TEXTO_ANULADO = "[Anulado a solicitud del usuario]"; 
			if (!asunto.startsWith(TEXTO_ANULADO)){ 
				asunto = TEXTO_ANULADO + " " + asunto;
				itemExpediente.set("ASUNTO", asunto);
				itemExpediente.store(ctx);
			}
			
			//Eliminamos todas las relaciones
			ExpedientesRelacionadosUtil.eliminarTodosRelacionados(ctx, numexp);
				
			//Cerramos trámites y expediente
			ExpedientesUtil.cerrarExpediente(ctx, numexp);
			
			return true;
					
		} catch (Throwable e) {
			logger.error("Error al anular la solicitud del expediente " + numexp);
			throw new TramitacionException(TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
	}

	/**
     * Añade documentos al trámite de un expediente.
     * @param numExp Número de expediente.
     * @param idTramite Identificador del trámite al que anexar los documentos.
     * @param numReg Número de registro de entrada.
     * @param fechaReg Fecha de registro de entrada.
     * @param documentos Lista de documentos asociados al expediente.
     * @return Cierto si los documentos se han creado correctamente.
     * @throws TramitacionException Si se produce algún error.
     */
    public boolean anexarDocsTramite(String idEntidad, String numExp, int idTramite, String numReg, Date fechaReg, DocumentoExpediente[] documentos) throws TramitacionException {
    	
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().anexarDocsTramite(numExp, idTramite, numReg, fechaReg, toInternalForm(documentos));
			
		} catch (Throwable e){
			logger.error("Error inesperado al anexar documentos al expediente [numExp=" + numExp + ", numReg=" + numReg + ", fechaReg=" + fechaReg + ", documentos=" + documentos + "]", e);
			throw new TramitacionException( TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
    }
    
    /**
     * Añade documentos al trámite de un expediente.
     * @param numExp Número de expediente.
     * @param idTramite Identificador del trámite al que anexar los documentos.
     * @param numReg Número de registro de entrada.
     * @param fechaReg Fecha de registro de entrada.
     * @param documentos Lista de documentos asociados al expediente.
     * @return Cierto si los documentos se han creado correctamente.
     * @throws TramitacionException Si se produce algún error.
     */
	public boolean insertaApoderamiento(String idEntidad, String numExp, String representado, String representante) throws TramitacionException{    	
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().insertaApoderamiento(idEntidad, numExp, representado, representante);
			
		} catch (Throwable e){
			logger.error("Error inesperado al insertar el apoderamiento al expediente [numExp=" + numExp + ", representado=" + representado + ", representante=" + representante + "]", e);
			throw new TramitacionException( TramitacionException.EXC_GENERIC_EXCEPCION, e);
		}
    }
	
	/**
	 * [dipucr-Felipe #1745]
     * Realiza el sellado/firma del documento pasado como parámetro
     * @param idEntidad
     * @param password - Contraseña
     * @param nombreDocumento
     * @param documento - Documento a firmar
     * @return Contenido del documento firmado
     * @throws ISPACException si ocurre algún error.
     */
    public byte [] sellarDocumento(String idEntidad, String password, String nombreDocumento, byte[] documento) throws TramitacionException {
		try {
			setOrganizationUserInfo(idEntidad);
			return TramitacionManager.getInstance().sellarDocumento(password, nombreDocumento, documento);
		} catch (Throwable e){
			String error = "Error inesperado al sellar el documento en la entidad " + idEntidad;
			logger.error(error, e);
			throw new TramitacionException(error, e);
		}
    }
}
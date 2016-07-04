package es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.keys.IDocKeys;

import es.ieci.tecdoc.fwktd.sir.core.types.CanalNotificacionEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.CriterioEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.DocumentacionFisicaEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.EstadoAsientoRegistralEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.OperadorCriterioEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.TipoDocumentoIdentificacionEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.TipoRechazoEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.TipoRegistroEnum;
import es.ieci.tecdoc.fwktd.sir.core.vo.AnexoFirmaVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.AnexoVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.CriterioVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.CriteriosVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.InteresadoVO;
import es.ieci.tecdoc.isicres.api.business.keys.ConstantKeys;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.business.manager.RegistroManager;
import es.ieci.tecdoc.isicres.api.business.vo.BaseRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.CampoAdicionalRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.DocumentoFisicoVO;
import es.ieci.tecdoc.isicres.api.business.vo.DocumentoRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.IdentificadorRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.PaginaDocumentoRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.RegistroEntradaVO;
import es.ieci.tecdoc.isicres.api.business.vo.RegistroOriginalVO;
import es.ieci.tecdoc.isicres.api.business.vo.TipoTransporteVO;
import es.ieci.tecdoc.isicres.api.business.vo.TransporteVO;
import es.ieci.tecdoc.isicres.api.business.vo.UnidadAdministrativaVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.contextholder.IsicresContextHolder;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.ConfiguracionCreateDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoContenidoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoDatosFirmaVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.dao.BandejaEntradaIntercambioRegistralDAO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.exception.IntercambioRegistralException;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.exception.IntercambioRegistralExceptionCodes;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.ConfiguracionIntercambioRegistralManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralEntradaManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralSIRManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.mapper.AsientoRegistralMapper;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.EntidadRegistralVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.EstadoIntercambioRegistralEntradaEnumVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralEntradaVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.TipoRegistroEnumVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadAdministrativaIntercambioRegistralVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.RepresentanteInteresadoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoFisicoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoJuridicoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TerceroValidadoVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.TipoDocumentoIdentificativoTerceroVO;
import es.ieci.tecdoc.isicres.terceros.business.vo.enums.TipoDocumentoIdentificativo;

public class IntercambioRegistralEntradaManagerImpl implements IntercambioRegistralEntradaManager {

    private static final String BLANK = " ";

    private static Logger logger = Logger.getLogger(IntercambioRegistralEntradaManagerImpl.class);
    protected DocumentoElectronicoAnexoManager documentoElectronicoAnexoManager;
    protected IntercambioRegistralSIRManager intercambioRegistralSIRManager;

    /**
     * Manager para leer configuraciones de intercmabios registrales (mapeos de
     * entidades registrales y unidades de tramitacion)
     */
    protected ConfiguracionIntercambioRegistralManager configuracionIntercambioRegistralManager;
    /**
     * Manager para leer y actualizar la bandeja de entrada de intercambio
     * registral
     */
    protected BandejaEntradaIntercambioRegistralDAO bandejaEntradaIntercambioRegistralDAO;
    /**
     * Incrementer para obtener el id del intercambio registral de entrada
     * aceptado
     */
    protected DataFieldMaxValueIncrementer intercambioRegistralEntradaIncrementer;

    public List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistral(Integer estado,
	    Integer idOficina, CriteriosVO criterios) {

	List<BandejaEntradaItemVO> result = null;

	// para los aceptos o rechazados hacemos busqueda local

	if (EstadoIntercambioRegistralEntradaEnumVO.ACEPTADO.getValue() == estado.intValue()) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Obtenemos la bandeja de entrada para el estado ACEPTADOS");
	    }
	    result = getBandejaEntradaIntercambioRegistralAceptados(idOficina, criterios);
	}

	if (EstadoIntercambioRegistralEntradaEnumVO.RECHAZADO.getValue() == estado.intValue()) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Obtenemos la bandeja de entrada para el estado rechazados");
	    }
	    result = getBandejaEntradaIntercambioRegistralRechazados(idOficina, criterios);
	}

	if (EstadoIntercambioRegistralEntradaEnumVO.PENDIENTE.getValue() == estado.intValue()) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Obtenemos la bandeja de entrada para el estado pendiente");
	    }
	    result = getBandejaEntradaIntercambioRegistralPendientes(idOficina, estado, criterios);
	}

	if (EstadoIntercambioRegistralEntradaEnumVO.REENVIADO.getValue() == estado.intValue()) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Obtenemos la bandeja de entrada para el estado reenviado");
	    }
	    result = getBandejaEntradaIntercambioRegistralReenviados(idOficina, estado, criterios);
	}

	return result;
    }

	/**
	 * Obtiene  el número de registros de la bandeja de entrada de Intercambio Registral para el <code>estado</code> e <code>idOficina</code> pasados como parámetros.
	 * @param estado
	 * @param idOficina
	 * @param  CriteriosVO criterios
	 * @return numero de intercambios registrales de la bandeja de entrada que cumplan los criterios
	 */
    public int getCountBandejaEntradaIntercambioRegistral(Integer estado,
	    Integer idOficina, CriteriosVO criterios) {

	int result = 0;
	
	// para los aceptos o rechazados hacemos busqueda local

	if (EstadoIntercambioRegistralEntradaEnumVO.ACEPTADO.getValue() == estado.intValue()) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Obtenemos la bandeja de entrada para el estado ACEPTADOS");
	    }
	    result = getCountBandejaEntradaIntercambioRegistralAceptados(idOficina, criterios);
	}

	if (EstadoIntercambioRegistralEntradaEnumVO.RECHAZADO.getValue() == estado.intValue()) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Obtenemos la bandeja de entrada para el estado rechazados");
	    }
	    result = getCountBandejaEntradaIntercambioRegistralRechazados(idOficina, criterios);
	}

	if (EstadoIntercambioRegistralEntradaEnumVO.PENDIENTE.getValue() == estado.intValue()) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Obtenemos la bandeja de entrada para el estado pendiente");
	    }
	    result = getCountBandejaEntradaIntercambioRegistralPendientes(idOficina, estado, criterios);
	}

	if (EstadoIntercambioRegistralEntradaEnumVO.REENVIADO.getValue() == estado.intValue()) {
	    if (logger.isDebugEnabled()) {
		logger.debug("Obtenemos la bandeja de entrada para el estado reenviado");
	    }
	    result = getCountBandejaEntradaIntercambioRegistralReenviados(idOficina, estado, criterios);
	}

	return result;
    }




    public List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistral(Integer estado,
	    Integer idOficina) {
	return getBandejaEntradaIntercambioRegistral(estado, idOficina, null);

    }
    
    private List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistralPendientes(
	    Integer idOficina, Integer estado, CriteriosVO criterios) {
	estado = convertEstadoToSIREnum(estado);

	if (logger.isDebugEnabled()) {
	    logger.debug("Obtenemos la bandeja de entrada para el estado pendientes :"
		    + " y la oficina: " + idOficina);
	}

	// Para los pendientes realizamos la consulta contra el sir
	EntidadRegistralVO entidadRegistralDestino =
		getConfiguracionIntercambioRegistralManager().getEntidadRegistralVOByIdScrOfic(
			String.valueOf(idOficina));
	List<BandejaEntradaItemVO> bandejaEntradaItems = new ArrayList<BandejaEntradaItemVO>();

	// Si entidadRegistralOrigen == null es que no hay oficina de
	// intercambio registral configurada para la instalaciï¿½n
	if (entidadRegistralDestino != null) {
	    CriteriosVO criteriosVO = null;
	    if (criterios == null){
		criteriosVO = new CriteriosVO();

	    }else {
		criteriosVO = criterios;
	    }
	    
	    CriterioVO criterioEstado;
	    criterioEstado =
		    new CriterioVO(CriterioEnum.ASIENTO_ESTADO, OperadorCriterioEnum.IN,
			    new Integer[] { EstadoAsientoRegistralEnum.RECIBIDO.getValue() });
	    criteriosVO.addCriterioVO(criterioEstado);
	    
	    CriterioVO criterioEntidadDestino =
		    new CriterioVO(CriterioEnum.ASIENTO_CODIGO_ENTIDAD_REGISTRAL_DESTINO,
			    entidadRegistralDestino.getCode());
	    criteriosVO.addCriterioVO(criterioEntidadDestino);
	  
	    List<AsientoRegistralVO> bandejaEntrada =
		    getIntercambioRegistralSIRManager().findAsientosRegistrales(criteriosVO);

	    AsientoRegistralMapper mapper = new AsientoRegistralMapper();
	    for (AsientoRegistralVO asientoRegistralVO : bandejaEntrada) {
		BandejaEntradaItemVO bandejaItem =
			mapper.toBandejaEntradaItemVO(asientoRegistralVO);
		bandejaEntradaItems.add(bandejaItem);
	    }
	}
	else {
	    logger.info("No hay configuracion de intercambio registral para esta oficina: "
		    + idOficina);
	    throw new IntercambioRegistralException(
		    "No se puede enviar porque esta oficina no tiene configurada su Entidad Registral del Directorio Comï¿½n de Organismos.",
		    IntercambioRegistralExceptionCodes.ERROR_CODE_OFICINA_NO_MAPEADA);
	}
	return bandejaEntradaItems;
    }
    
    private int getCountBandejaEntradaIntercambioRegistralPendientes(
	    Integer idOficina, Integer estado, CriteriosVO criterios) {
	estado = convertEstadoToSIREnum(estado);

	if (logger.isDebugEnabled()) {
	    logger.debug("Obtenemos la bandeja de entrada para el estado pendientes :"
		    + " y la oficina: " + idOficina);
	}

	// Para los pendientes realizamos la consulta contra el sir
	EntidadRegistralVO entidadRegistralDestino =
		getConfiguracionIntercambioRegistralManager().getEntidadRegistralVOByIdScrOfic(
			String.valueOf(idOficina));
	int bandejaEntrada = 0;
	// Si entidadRegistralOrigen == null es que no hay oficina de
	// intercambio registral configurada para la instalaciï¿½n
	if (entidadRegistralDestino != null) {
	    CriteriosVO criteriosVO = null;
	    if (criterios == null){
		criteriosVO = new CriteriosVO();

	    }else {
		criteriosVO = criterios;
	    }
	    
	    CriterioVO criterioEstado;
	    criterioEstado =
		    new CriterioVO(CriterioEnum.ASIENTO_ESTADO, OperadorCriterioEnum.IN,
			    new Integer[] { EstadoAsientoRegistralEnum.RECIBIDO.getValue() });
	    criteriosVO.addCriterioVO(criterioEstado);
	    
	    CriterioVO criterioEntidadDestino =
		    new CriterioVO(CriterioEnum.ASIENTO_CODIGO_ENTIDAD_REGISTRAL_DESTINO,
			    entidadRegistralDestino.getCode());
	    criteriosVO.addCriterioVO(criterioEntidadDestino);
	  
	   bandejaEntrada =
		    getIntercambioRegistralSIRManager().countAsientosRegistrales(criteriosVO);

	}
	else {
	    logger.info("No hay configuracion de intercambio registral para esta oficina: "
		    + idOficina);
	    throw new IntercambioRegistralException(
		    "No se puede enviar porque esta oficina no tiene configurada su Entidad Registral del Directorio Comï¿½n de Organismos.",
		    IntercambioRegistralExceptionCodes.ERROR_CODE_OFICINA_NO_MAPEADA);
	}
	return bandejaEntrada;
    }
    
    /**
     * Metodo que hace la busqueda en el modulo intermedio de la plataforma sir
     * 
     * @param idOficina
     * @param estado
     * @return
     */
    private List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistralPendientes(
	    Integer idOficina, Integer estado) {
	return getBandejaEntradaIntercambioRegistralPendientes(
		    idOficina, estado, null); 
    }

    /**
     * Mï¿½todo que hace la busqueda en el modulo intermedio de la plataforma
     * sir los intercambios en estado reenviado.
     * 
     * @param idOficina
     * @param estado
     * @return Listado de intercambios reenviados
     * 
     */
    private List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistralReenviados(
	    Integer idOficina, Integer estado) {

	return getBandejaEntradaIntercambioRegistralDAO().getBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.REENVIADO_VALUE, idOficina);
    }
    
    private List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistralReenviados(
	    Integer idOficina, Integer estado, CriteriosVO criterios) {

	return getBandejaEntradaIntercambioRegistralDAO().getBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.REENVIADO_VALUE, idOficina, criterios);
    }
    
    private Integer convertEstadoToSIREnum(Integer estado) {

	if (estado == EstadoIntercambioRegistralEntradaEnumVO.ACEPTADO_VALUE) {
	    estado = EstadoAsientoRegistralEnum.ACEPTADO.getValue();
	}

	if (estado == EstadoIntercambioRegistralEntradaEnumVO.PENDIENTE_VALUE) {
	    // Lo que para nosotros es PENDIENTE de aceptar, en el CIR es
	    // RECIBIDO por el CIR
	    estado = EstadoAsientoRegistralEnum.RECIBIDO.getValue();
	}

	if (estado == EstadoIntercambioRegistralEntradaEnumVO.RECHAZADO_VALUE) {
	    estado = EstadoAsientoRegistralEnum.RECHAZADO.getValue();
	}

	if (estado == EstadoIntercambioRegistralEntradaEnumVO.REENVIADO_VALUE) {
	    estado = EstadoAsientoRegistralEnum.REENVIADO.getValue();
	}

	return estado;
    }

    private List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistralAceptados(
	    Integer idOficina) {
	return getBandejaEntradaIntercambioRegistralDAO().getBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.ACEPTADO_VALUE, idOficina);
    }
    

    
    private List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistralRechazados(
	    Integer idOficina) {
	return getBandejaEntradaIntercambioRegistralDAO().getBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.RECHAZADO_VALUE, idOficina);
    }
    

    
    private List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistralAceptados(
	    Integer idOficina, CriteriosVO criterios) {
	return getBandejaEntradaIntercambioRegistralDAO().getBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.ACEPTADO_VALUE, idOficina,criterios);
    }

    private List<BandejaEntradaItemVO> getBandejaEntradaIntercambioRegistralRechazados(
	    Integer idOficina, CriteriosVO criterios) {
	return getBandejaEntradaIntercambioRegistralDAO().getBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.RECHAZADO_VALUE, idOficina, criterios);
    }
    
    private int getCountBandejaEntradaIntercambioRegistralAceptados(
	    Integer idOficina, CriteriosVO criterios) {
	return getBandejaEntradaIntercambioRegistralDAO().getCountBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.ACEPTADO_VALUE, idOficina, criterios);
    }
    
    private int getCountBandejaEntradaIntercambioRegistralRechazados(
	    Integer idOficina, CriteriosVO criterios) {
	return getBandejaEntradaIntercambioRegistralDAO().getCountBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.RECHAZADO_VALUE, idOficina, criterios);
    }
    
    private int getCountBandejaEntradaIntercambioRegistralReenviados(
	    Integer idOficina, Integer estado, CriteriosVO criterios) {

	return getBandejaEntradaIntercambioRegistralDAO().getCountBandejaEntradaByEstado(
		EstadoIntercambioRegistralEntradaEnumVO.REENVIADO_VALUE, idOficina, criterios);
    }
    
    public void guardarIntercambioRegistralEntrada(
	    IntercambioRegistralEntradaVO intercambioRegistralEntrada) {

	Long id = getIntercambioRegistralEntradaIncrementer().nextLongValue();
	if (logger.isDebugEnabled()) {
	    logger.debug("Se va a guardar el intercambio registral de entrada con id de intercambio = "
		    + intercambioRegistralEntrada.getIdIntercambioRegistral());
	}
	intercambioRegistralEntrada.setId(id);
	getBandejaEntradaIntercambioRegistralDAO().save(intercambioRegistralEntrada);
    }

    public BandejaEntradaItemVO completarBandejaEntradaItem(
	    BandejaEntradaItemVO bandejaEntradaItemVO) {
	return getBandejaEntradaIntercambioRegistralDAO().completarBandejaEntradaItem(
		bandejaEntradaItemVO);
    }

    public void rechazarIntercambioRegistralEntradaById(String idIntercambioInterno,
	    String tipoRechazo, String observaciones) {

	if (logger.isDebugEnabled()) {
	    logger.debug("Se va a rechazar el intercambio registral de entrada con id de intercambio = "
		    + idIntercambioInterno);
	}

	UsuarioVO usuario = IsicresContextHolder.getContextoAplicacion().getUsuarioActual();
	Integer idOficina =
		Integer.parseInt(usuario.getConfiguracionUsuario().getOficina().getId());

	String idEntidad = usuario.getConfiguracionUsuario().getIdEntidad();

	// obtener datos del intercambio registral
	AsientoRegistralVO asientoRegistral =
		getIntercambioRegistralSIRManager().getAsientoRegistral(idIntercambioInterno);

	IntercambioRegistralEntradaVO intercambioRegistralEntradaVO =
		populateIntercambioRegistralEntrada(usuario, asientoRegistral, idOficina,
			idIntercambioInterno, EstadoIntercambioRegistralEntradaEnumVO.RECHAZADO,
			observaciones);

	// Guardamos en la tabal scr_exregin
	guardarIntercambioRegistralEntrada(intercambioRegistralEntradaVO);

	// y actualizamos estado en el SIR
	getIntercambioRegistralSIRManager().rechazarAsientoRegistral(idIntercambioInterno,
		TipoRechazoEnum.getTipoRechazo(Integer.valueOf(tipoRechazo)), observaciones);

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralEntradaManager#reenviarIntercambioRegistralEntradaById(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO)
     */
    public void reenviarIntercambioRegistralEntradaById(String idIntercambioRegistralEntrada,
	    UnidadTramitacionIntercambioRegistralVO nuevoDestino, String observaciones) {

	UsuarioVO usuario = IsicresContextHolder.getContextoAplicacion().getUsuarioActual();

	Integer idOficina =
		Integer.parseInt(usuario.getConfiguracionUsuario().getOficina().getId());

	String idEntidad = usuario.getConfiguracionUsuario().getIdEntidad();

	// TODO: Almacena el asiento registral en la tabla scr_exregin
	// obtener datos del intercambio registral
	AsientoRegistralVO asientoRegistral =
		getIntercambioRegistralSIRManager().getAsientoRegistral(
			idIntercambioRegistralEntrada);
	// vamos a a realizar actualizacion del estado del intercambio en local,
	// pasa a estado aceptado EstadoIntercambioRegistralEntradaEnumVO

	IntercambioRegistralEntradaVO intercambioRegistralEntradaVO =
		populateIntercambioRegistralEntrada(usuario, asientoRegistral, idOficina,
			idIntercambioRegistralEntrada,
			EstadoIntercambioRegistralEntradaEnumVO.REENVIADO, observaciones);

	// Guardamos en la tabal scr_exregin y actualizamos estado en el SIR
	guardarIntercambioRegistralEntrada(intercambioRegistralEntradaVO);

	// TODO: ï¿½Contacto puede ser fullName? contacto y usuario es opcional,
	// no se envia
	getIntercambioRegistralSIRManager().reenviarAsientoRegistral(idIntercambioRegistralEntrada,
		null, null, observaciones, nuevoDestino);

    }

    public List<IntercambioRegistralEntradaVO> getHistorialIntercambioRegistralEntrada(
	    String idLibro, String idRegistro, String idOficina) {

	IntercambioRegistralEntradaVO intercambioRegistralEntrada =
		new IntercambioRegistralEntradaVO();
	intercambioRegistralEntrada.setIdLibro(Long.parseLong(idLibro));
	intercambioRegistralEntrada.setIdRegistro(Long.parseLong(idRegistro));
	intercambioRegistralEntrada.setIdOfic(Integer.parseInt(idOficina));

	List<IntercambioRegistralEntradaVO> infoEstados =
		getBandejaEntradaIntercambioRegistralDAO().getInfoEstado(
			intercambioRegistralEntrada);

	return infoEstados;
    }

    public void aceptarIntercambioRegistralEntradaById(String idIntercambioInterno, String idLibro,
	    String user, Integer idOficina, String codOficina, boolean llegoDocFisica) {

	// TODO injection en spring
	RegistroManager registroManager = IsicresManagerProvider.getInstance().getRegistroManager();
	UsuarioVO usuario = IsicresContextHolder.getContextoAplicacion().getUsuarioActual();

	// obtener datos del intercambio registral
	AsientoRegistralVO asientoRegistral =
		getIntercambioRegistralSIRManager().getAsientoRegistral(idIntercambioInterno);

	// vamos a crear el resgistro
	BaseRegistroVO registro = completarRegistroVO(asientoRegistral, usuario, idLibro);

	registro = registroManager.createRegistroEntrada(usuario, (RegistroEntradaVO) registro);

	/*registroManager.attachDocuments(registro.getId(), getAnexos(asientoRegistral, registro),
		usuario);*/
	
	//Generamos el documento principal
	List<DocumentoElectronicoAnexoVO> documentosElectronicoAnexo= new ArrayList<DocumentoElectronicoAnexoVO>();
	documentosElectronicoAnexo =populateDocumentoElectronicoAnexoVO(asientoRegistral,registro.getId());
	
	
	ConfiguracionCreateDocumentoElectronicoAnexoVO cfg=new ConfiguracionCreateDocumentoElectronicoAnexoVO();
	
	//seteamos el nombre de la carpeta/clasificador de sicres sobre el que se guardaran
	
	String clasificador = null;
	cfg.setClasificador(clasificador);
	if (documentosElectronicoAnexo != null){
        	for (DocumentoElectronicoAnexoVO documentoElectronicoAnexo:documentosElectronicoAnexo){
        	    documentoElectronicoAnexoManager.create(documentoElectronicoAnexo,cfg);
        	}
	}
	// vamos a a realizar actualizacion del estado del intercambio en local,
	// pasa a estado aceptado EstadoIntercambioRegistralEntradaEnumVO
	IntercambioRegistralEntradaVO intercambioRegistralEntradaVO =
		populateIntercambioRegistralEntrada(usuario, asientoRegistral, idOficina,
			Long.parseLong(idLibro), Long.parseLong(registro.getIdRegistro()),
			idIntercambioInterno, EstadoIntercambioRegistralEntradaEnumVO.ACEPTADO,
			null);

	// Guardamos en la tabal scr_exregin y actualizamos estado en el SIR
	guardarIntercambioRegistralEntrada(intercambioRegistralEntradaVO);

	// hacemos la llamada al sir para aceptarlo, tenemos que poner
	// el numero de registro que hemos generado
	getIntercambioRegistralSIRManager().validarAsientoRegistral(asientoRegistral.getId(),
		registro.getNumeroRegistro(), registro.getFechaRegistro());

    }

    public void aceptarIntercambioRegistralEntradaById(String idIntercambioInterno, String idLibro,
	    UsuarioVO user, Integer idOficina, String codOficina, boolean llegoDocFisica) {

	// TODO injection en spring
	RegistroManager registroManager = IsicresManagerProvider.getInstance().getRegistroManager();

	// obtener datos del intercambio registral
	AsientoRegistralVO asientoRegistral =
		getIntercambioRegistralSIRManager().getAsientoRegistral(idIntercambioInterno);

	// vamos a crear el resgistro
	BaseRegistroVO registro = completarRegistroVO(asientoRegistral, user, idLibro);

	registro = registroManager.createRegistroEntrada(user, (RegistroEntradaVO) registro);

	/*registroManager.attachDocuments(registro.getId(), getAnexos(asientoRegistral, registro),
		usuario);*/
	
	//Generamos el documento principal
	List<DocumentoElectronicoAnexoVO> documentosElectronicoAnexo= new ArrayList<DocumentoElectronicoAnexoVO>();
	documentosElectronicoAnexo =populateDocumentoElectronicoAnexoVO(asientoRegistral,registro.getId());
	
	
	ConfiguracionCreateDocumentoElectronicoAnexoVO cfg=new ConfiguracionCreateDocumentoElectronicoAnexoVO();
	
	//seteamos el nombre de la carpeta/clasificador de sicres sobre el que se guardaran
	
	String clasificador = null;
	cfg.setClasificador(clasificador);
	if (documentosElectronicoAnexo != null){
        	for (DocumentoElectronicoAnexoVO documentoElectronicoAnexo:documentosElectronicoAnexo){
        	    documentoElectronicoAnexoManager.create(documentoElectronicoAnexo,cfg, user);
        	}
	}
	// vamos a a realizar actualizacion del estado del intercambio en local,
	// pasa a estado aceptado EstadoIntercambioRegistralEntradaEnumVO
	IntercambioRegistralEntradaVO intercambioRegistralEntradaVO =
		populateIntercambioRegistralEntrada(user, asientoRegistral, idOficina,
			Long.parseLong(idLibro), Long.parseLong(registro.getIdRegistro()),
			idIntercambioInterno, EstadoIntercambioRegistralEntradaEnumVO.ACEPTADO,
			null);

	// Guardamos en la tabal scr_exregin y actualizamos estado en el SIR
	guardarIntercambioRegistralEntrada(intercambioRegistralEntradaVO);

	// hacemos la llamada al sir para aceptarlo, tenemos que poner
	// el numero de registro que hemos generado
	getIntercambioRegistralSIRManager().validarAsientoRegistral(asientoRegistral.getId(),
		registro.getNumeroRegistro(), registro.getFechaRegistro());

    }
    
    private List<DocumentoElectronicoAnexoVO> populateDocumentoElectronicoAnexoVO
    	(AsientoRegistralVO asientoRegistral, IdentificadorRegistroVO  identificadorRegistroVO ) {

	List<AnexoVO> anexos = asientoRegistral.getAnexos();

	if (logger.isDebugEnabled()) {
	    logger.debug("Se van a recupear los anexos del intercambio registral con identificador = "
		    + asientoRegistral.getIdentificadorIntercambio());
	}

	if (anexos == null || anexos.size() == 0) {
	    if (logger.isDebugEnabled()) {
		logger.debug("El intercambio no tiene anexos");
	    }
	    return null;
	}

	// Verificamos los anexos adjuntados
	List<AnexoVO> anexosVerificados = verificarAnexos(anexos);

	// Generamos el listado de DocumentosRegistroVO a partir de los anexos
	// verificados
	List<DocumentoElectronicoAnexoVO> documentosElectronicoAnexo =
		mapearListAnexoVOaListDocumentoElectronicoVO(anexosVerificados, identificadorRegistroVO);

	// retornamos el array de documentos
	return documentosElectronicoAnexo;
    }
    

    
    private List<DocumentoElectronicoAnexoVO> mapearListAnexoVOaListDocumentoElectronicoVO(
	    List<AnexoVO> anexosVerificados, IdentificadorRegistroVO  identificadorRegistroVO) {

	List<DocumentoElectronicoAnexoVO> result = new ArrayList<DocumentoElectronicoAnexoVO>();
	DocumentoElectronicoAnexoVO documentoElectronicoAnexo = null;
	IdentificadorDocumentoElectronicoAnexoVO id= new IdentificadorDocumentoElectronicoAnexoVO();
	id.setIdLibro(Long.parseLong(identificadorRegistroVO.getIdLibro()));
	id.setIdRegistro(Long.parseLong(identificadorRegistroVO.getIdRegistro()));
	
	List<AnexoFirmaVO> listAnexosVerificados = mapearAnexoFirma (anexosVerificados);
	
	for (AnexoFirmaVO anexoVerificado: listAnexosVerificados){
	    documentoElectronicoAnexo = new DocumentoElectronicoAnexoVO();
	    documentoElectronicoAnexo.setId(id);
	    //TODO codeName como tiene q ser este codigo
	    String codeName=("codeName"+anexoVerificado.getOriginal().getNombreFichero());
	    codeName=StringUtils.abbreviate(codeName, 21);
	    documentoElectronicoAnexo.setCodeName(codeName);
        	
	    //extension
	    String extension=anexoVerificado.getOriginal().getNombreFichero().substring(anexoVerificado.getOriginal().getNombreFichero().lastIndexOf(".") + 1,
        		anexoVerificado.getOriginal().getNombreFichero().length());
	    documentoElectronicoAnexo.setExtension(extension);
        	
	    //contenido
	    DocumentoElectronicoAnexoContenidoVO contenido=new DocumentoElectronicoAnexoContenidoVO();
	    // Contenido del fichero
	    byte[] contenidoAnexo = getIntercambioRegistralSIRManager().getContenidoAnexo(anexoVerificado.getOriginal().getId());
	    contenido.setContent(contenidoAnexo);
        	//contenido.setDocUID(documentoCompulsar.getDocumentoOriginal().getLocation());
	    documentoElectronicoAnexo.setContenido(contenido);
	    
	    documentoElectronicoAnexo.setMimeType(anexoVerificado.getOriginal().getTipoMIME());
	    //datos hash
	    DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumento= new DocumentoElectronicoAnexoDatosFirmaVO();
        
	    if (anexoVerificado.getOriginal().getHash() != null){
		String hashDocumento= Base64.encodeBase64String(anexoVerificado.getOriginal().getHash());
		datosFirmaDocumento.setHash(hashDocumento);
		//String hashAlgDocumento=anexoVerificado.get;
		    //datosFirmaDocumento.setHashAlg(hashAlgDocumento);
		documentoElectronicoAnexo.setDatosFirma(datosFirmaDocumento);
	    }
	    
        	
	    String name = anexoVerificado.getOriginal().getNombreFichero();
	    documentoElectronicoAnexo.setName(name);
	    if (anexoVerificado.getOriginal().getTipoDocumento() != null){
		String tipoDoc = anexoVerificado.getOriginal().getTipoDocumento().getValue();
		documentoElectronicoAnexo.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.getEnum(Integer.valueOf(tipoDoc.substring(1, 2))));
	    }
	    if (anexoVerificado.getOriginal().getValidezDocumento() != null){
		String tipoVal = anexoVerificado.getOriginal().getValidezDocumento().getValue();
		documentoElectronicoAnexo.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.getEnum(Integer.valueOf(tipoVal.substring(1, 2))));
	    }
	    documentoElectronicoAnexo.setComentario(anexoVerificado.getOriginal().getObservaciones());
	    
	    //firmas del documento
	    List<DocumentoElectronicoAnexoVO> firmas=new ArrayList<DocumentoElectronicoAnexoVO>();
	    DocumentoElectronicoAnexoVO firmaDocumento= new DocumentoElectronicoAnexoVO();
	    //////////////////////////////////////////////////////////////////////////////
        
	    if (anexoVerificado.getFirma() != null){
		IdentificadorDocumentoElectronicoAnexoVO idFima= new IdentificadorDocumentoElectronicoAnexoVO();
        	
		idFima.setIdLibro(Long.parseLong(identificadorRegistroVO.getIdLibro()));
		idFima.setIdRegistro(Long.parseLong(identificadorRegistroVO.getIdRegistro()));
		firmaDocumento.setId(idFima);
        	
		firmaDocumento.setComentario(anexoVerificado.getFirma().getObservaciones());
		//TODO codeName como tiene q ser este codigo
		String codeNameFirma="codeName"+anexoVerificado.getFirma().getIdentificadorDocumentoFirmado();
		codeNameFirma=StringUtils.abbreviate(codeNameFirma, 21);
		firmaDocumento.setCodeName(codeNameFirma);
        	
		//contenido
		DocumentoElectronicoAnexoContenidoVO contenidoFirma=new DocumentoElectronicoAnexoContenidoVO();
		byte[] contenidoFirmaAnexo = getIntercambioRegistralSIRManager()
			.getContenidoAnexo(anexoVerificado.getFirma().getId());
	    	contenidoFirma.setContent(contenidoFirmaAnexo);
	    	//contenidoFirma.setDocUID(documentoCompulsar.getFirma().getLocation());
	    	firmaDocumento.setContenido(contenidoFirma);
        	
	    	String extensionFirma=anexoVerificado.getFirma().getIdentificadorFichero().
		    substring(anexoVerificado.getFirma().getIdentificadorFichero().lastIndexOf(".") + 1,
		    anexoVerificado.getFirma().getIdentificadorFichero().length());
	    	firmaDocumento.setExtension(extensionFirma);
        	
	    	//datos hash
	    	DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumentoFirma= new DocumentoElectronicoAnexoDatosFirmaVO();
                if (anexoVerificado.getFirma().getHash() != null){
                    
                    String hashDocumentoFirma=Base64.encodeBase64String(anexoVerificado.getFirma().getHash());
                    datosFirmaDocumentoFirma.setHash(hashDocumentoFirma);
                    //String hashAlgDocumentoFirma=documentoCompulsar.getFirma().getHashAlg();
                    //datosFirmaDocumentoFirma.setHashAlg(hashAlgDocumentoFirma);
                    firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                }	
                	
                String nameFirma=anexoVerificado.getFirma().getNombreFichero();
                firmaDocumento.setName(nameFirma);
        	if (anexoVerificado.getFirma().getTipoDocumento() != null){
        	    String tipoDocFirma = anexoVerificado.getFirma().getTipoDocumento().getValue();
        	    firmaDocumento.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.getEnum(Integer.valueOf(tipoDocFirma.substring(1, 2))));
        	}
        	if (anexoVerificado.getFirma().getValidezDocumento() != null){
        	    String tipoValFirma = anexoVerificado.getFirma().getValidezDocumento().getValue();
     	    	    firmaDocumento.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.getEnum(Integer.valueOf(tipoValFirma.substring(1, 2))));
     	    	}
	
                //String algFirma=datosEspecificosDocumento.getAlgoritmoFirma();
                //datosFirmaDocumentoFirma.setAlgFirma(algFirma);
                if (anexoVerificado.getFirma().getCertificado() != null){
                    String certificado=new String(anexoVerificado.getFirma().getCertificado());
                    datosFirmaDocumentoFirma.setCertificado(certificado);
                }
                if (anexoVerificado.getFirma().getFirma() != null){
                    String firma=new String(anexoVerificado.getFirma().getFirma());
                    datosFirmaDocumentoFirma.setFirma(firma);
                }
               // String formatoFirma=datosEspecificosDocumento.getFormatoFirma();
                //datosFirmaDocumentoFirma.setFormatoFirma(formatoFirma);
                firmaDocumento.setDatosFirma(datosFirmaDocumentoFirma);
                	
                firmas.add(firmaDocumento);
                documentoElectronicoAnexo.setFirmas(firmas);
	    }
	    result.add(documentoElectronicoAnexo);
	}
	return result;
    }

    private List<AnexoFirmaVO> mapearAnexoFirma(List<AnexoVO> anexosVerificados) {
	List<AnexoFirmaVO> result = null;
	AnexoFirmaVO anexoFirmaVO = null;
	if (anexosVerificados != null){
	    result = new  ArrayList<AnexoFirmaVO>();
	    for (AnexoVO anexoOriginal:anexosVerificados){
		if (anexoOriginal.getIdentificadorDocumentoFirmado() == null ||
			(anexoOriginal.getIdentificadorDocumentoFirmado() != null &&
				anexoOriginal.getIdentificadorDocumentoFirmado().equals(anexoOriginal.getIdentificadorFichero()))){
		    anexoFirmaVO = new AnexoFirmaVO();
		    anexoFirmaVO.setOriginal(anexoOriginal);
		    if (!(anexoOriginal.getIdentificadorDocumentoFirmado() != null &&
				anexoOriginal.getIdentificadorDocumentoFirmado().equals(anexoOriginal.getIdentificadorFichero()))){
        		    for (AnexoVO anexoFirma:anexosVerificados){
        			if (anexoFirma.getIdentificadorDocumentoFirmado() != null && 
        				anexoOriginal.getIdentificadorFichero().equals(anexoFirma.getIdentificadorDocumentoFirmado())){
        			    anexoFirmaVO.setFirma(anexoFirma);
        			}
        		    }
		    }
		    result.add(anexoFirmaVO);
		}
	    }
	}
	return result;
    }

    protected IntercambioRegistralEntradaVO populateIntercambioRegistralEntrada(UsuarioVO usuario,
	    AsientoRegistralVO asientoRegistral, Integer idOficina, String idIntercambioInterno,
	    EstadoIntercambioRegistralEntradaEnumVO estado, String observaciones) {

	IntercambioRegistralEntradaVO result = null;

	result =
		populateIntercambioRegistralEntrada(usuario, asientoRegistral, idOficina, null,
			null, idIntercambioInterno, estado, observaciones);

	return result;
    }

    protected IntercambioRegistralEntradaVO populateIntercambioRegistralEntrada(UsuarioVO usuario,
	    AsientoRegistralVO asientoRegistral, Integer idOficina, Long idLibro, Long idRegistro,
	    String idIntercambioInterno, EstadoIntercambioRegistralEntradaEnumVO estado,
	    String observaciones) {

	IntercambioRegistralEntradaVO result = new IntercambioRegistralEntradaVO();

	result.setIdRegistro(idRegistro);
	result.setIdLibro(idLibro);

	result.setIdOfic(idOficina);
	result.setEstado(estado);
	result.setFechaEstado(Calendar.getInstance().getTime());
	result.setIdIntercambioRegistral(asientoRegistral.getIdentificadorIntercambio());
	result.setIdIntercambioInterno(idIntercambioInterno);
	result.setTipoOrigen(Integer.parseInt(asientoRegistral.getTipoRegistro().getValue()));
	result.setFechaIntercambio(result.getFechaEstado());

	result.setUsername(usuario.getLoginName());

	// Entidad de tramitacion inicial del intercambio
	result.setCodeEntity(asientoRegistral.getCodigoEntidadRegistralInicio());

	// validamos si la descripciï¿½n de la entidad es nula y el cï¿½digo de
	// la
	// entidad esta completo, insertamos como descripciï¿½n el cï¿½digo de
	// la
	// entidad
	if (StringUtils.isEmpty(asientoRegistral.getDescripcionEntidadRegistralInicio())
		&& StringUtils.isNotEmpty(asientoRegistral.getCodigoEntidadRegistralInicio())) {
	    // aï¿½adimos el codigo de entidad como descripcion
	    result.setNameEntity(asientoRegistral.getCodigoEntidadRegistralInicio());

	}
	else {
	    // aladimos la descripciï¿½n de la entidad
	    result.setNameEntity(asientoRegistral.getDescripcionEntidadRegistralInicio());
	}

	// Unidad de tramitacion inicial del intercambio
	/*
	 * TODO: ï¿½Cuï¿½l es la unidad de tramitaciï¿½n correspondiente en el
	 * asiento registral? ï¿½Origen o Destino?
	 */
	result.setCodeTramunit(asientoRegistral.getCodigoUnidadTramitacionOrigen());

	// validamos si el nombre de la unidad de tramitaciï¿½n es nula y el
	// cï¿½digo de ï¿½sta estï¿½ completo, insertamos como descripciï¿½n el
	// cï¿½digo
	// de la unidad
	if (StringUtils.isEmpty(asientoRegistral.getDescripcionUnidadTramitacionOrigen())
		&& StringUtils.isNotEmpty(asientoRegistral.getCodigoUnidadTramitacionOrigen())) {
	    // aï¿½adimos el codigo de la unidad como nombre
	    result.setNameTramunit(asientoRegistral.getCodigoUnidadTramitacionOrigen());

	}
	else {
	    // aï¿½adimos la descripciï¿½n de la unidad de tramitaciï¿½n
	    result.setNameTramunit(asientoRegistral.getDescripcionUnidadTramitacionOrigen());
	}

	// numero registro inicial
	result.setNumeroRegistroOrigen(asientoRegistral.getNumeroRegistroInicial());
	// contacto de usuario
	result.setContactoOrigen(asientoRegistral.getContactoUsuario());

	result.setComentarios(observaciones);

	return result;
    }

    private AsientoRegistralVO
	    getAsientoRegistral(String idIntercambioRegistral, Integer idOficina) {
	AsientoRegistralVO asiento = null;
	EntidadRegistralVO entidadRegistralDestino =
		getConfiguracionIntercambioRegistralManager().getEntidadRegistralVOByIdScrOfic(
			String.valueOf(idOficina));
	CriteriosVO criterios = new CriteriosVO();
	CriterioVO criterioIdentificador =
		new CriterioVO(CriterioEnum.ASIENTO_IDENTIFICADOR_INTERCAMBIO,
			idIntercambioRegistral);
	CriterioVO criterioEntidadDestino =
		new CriterioVO(CriterioEnum.ASIENTO_CODIGO_ENTIDAD_REGISTRAL,
			entidadRegistralDestino.getCode());
	criterios.addCriterioVO(criterioIdentificador);
	criterios.addCriterioVO(criterioEntidadDestino);
	List<AsientoRegistralVO> asientoRegistral =
		getIntercambioRegistralSIRManager().findAsientosRegistrales(criterios);
	if (asientoRegistral != null && asientoRegistral.size() == 1) {
	    asiento = asientoRegistral.get(0);
	}
	return asiento;
    }

    private BaseRegistroVO completarRegistroVO(AsientoRegistralVO asientoRegistral,
	    UsuarioVO usuario, String idLibro) {

	if (logger.isDebugEnabled()) {
	    logger.debug("Se va a completar la informaciï¿½n del intercambio para aceptarla");
	}
	RegistroEntradaVO registro = new RegistroEntradaVO();

	registro = mapearCamposAdicionales(registro, asientoRegistral);

	// Mapeamos los terceros
	registro = mapearTerceros(registro, asientoRegistral);

	registro.setComentario(asientoRegistral.getObservacionesApunte());

	// Mapear el resto de campos en comentarios (el tipo de asunto se
	// incluye en el campo comentario)
	//registro = mapearCamposEnComentarios(registro, asientoRegistral);
	// Mapeamos los datos propios del registro
	registro.setUsuarioRegistro(usuario);
	registro.setOficinaRegistro(usuario.getConfiguracionUsuario().getOficina());
	registro.setIdLibro(idLibro);

	registro.setResumen(asientoRegistral.getResumen());
	registro.setReferenciaExpediente(asientoRegistral.getNumeroExpediente());
	if (asientoRegistral.getTipoTransporte() != null) {
	   TransporteVO transporte = new TransporteVO();
	   if (StringUtils.isNotEmpty(asientoRegistral.getNumeroTransporte())) {
	       transporte.setNumeroTransporte(asientoRegistral.getNumeroTransporte());
	   }
	   TipoTransporteVO tipoTransporte = new TipoTransporteVO();
	   tipoTransporte.setDescripcion(asientoRegistral.getTipoTransporte().getValue());
	   tipoTransporte.setId(asientoRegistral.getTipoTransporte().getValue());
	   transporte.setTipoTransporte(tipoTransporte);
	   registro.setTransporte(transporte);
	}

	UnidadAdministrativaVO unidadAdministrativaOrigen = new UnidadAdministrativaVO();
	unidadAdministrativaOrigen.setCodigoUnidad(asientoRegistral
		.getCodigoUnidadTramitacionOrigen());
	UnidadAdministrativaVO unidadAdministrativaDestino = new UnidadAdministrativaVO();
	unidadAdministrativaDestino.setCodigoUnidad(asientoRegistral
		.getCodigoUnidadTramitacionDestino());
	registro.setUnidadAdministrativaOrigen(unidadAdministrativaOrigen);
	registro.setUnidadAdministrativaDestino(unidadAdministrativaDestino);
	RegistroOriginalVO registroOriginal = new RegistroOriginalVO();
	registroOriginal.setNumeroRegistroOriginal(asientoRegistral.getNumeroRegistro());
	registroOriginal.setFechaRegistroOriginal(asientoRegistral.getFechaRegistro());
	registroOriginal.setEntidadRegistral(registro.getUnidadAdministrativaOrigen());
	if (asientoRegistral.getTipoRegistro() == TipoRegistroEnum.ENTRADA) {
	    registroOriginal.setTipoRegistroOriginal(Integer
		    .toString(TipoRegistroEnumVO.ENTRADA_VALUE));
	}
	else {
	    registroOriginal.setTipoRegistroOriginal(Integer
		    .toString(TipoRegistroEnumVO.SALIDA_VALUE));
	}
	registro.setRegistroOriginal(registroOriginal);

	if (logger.isDebugEnabled()) {
	    logger.debug("Se completo la recuperacion de informacion del intercambio con identificador = "
		    + asientoRegistral.getIdentificadorIntercambio());
	}
	return registro;
    }

    private RegistroEntradaVO mapearCamposAdicionales(RegistroEntradaVO registro,
	    AsientoRegistralVO asientoRegistral) {
	RegistroEntradaVO result = registro;
	CampoAdicionalRegistroVO campoExpone = new CampoAdicionalRegistroVO();
	campoExpone.setName(String.valueOf(AxSf.FLD501_FIELD_ID));
	campoExpone.setValue(asientoRegistral.getExpone());
	registro.getCamposAdicionales().add(campoExpone);

	CampoAdicionalRegistroVO campoSolicita = new CampoAdicionalRegistroVO();
	campoSolicita.setName(String.valueOf(AxSf.FLD502_FIELD_ID));
	campoSolicita.setValue(asientoRegistral.getSolicita());
	registro.getCamposAdicionales().add(campoSolicita);

	CampoAdicionalRegistroVO campoObservaciones = new CampoAdicionalRegistroVO();
	campoObservaciones.setName(String.valueOf(AxSf.FLD507_FIELD_ID));
	campoObservaciones.setValue(mapearCamposEnComentarios(asientoRegistral));
	registro.getCamposAdicionales().add(campoObservaciones);
	
	// TODO ï¿½Hay que validar que el libro tenga este campo? Ahora peta si
	// el
	// libro no lo tiene...
	CampoAdicionalRegistroVO campoAdicionalIsIntercambioRegistral =
		new CampoAdicionalRegistroVO();
	campoAdicionalIsIntercambioRegistral.setName(String.valueOf(AxSf.FLD503_FIELD_ID));
	campoAdicionalIsIntercambioRegistral.setValue(ConstantKeys.REG_IS_INTERCAMBIO_REGISTRAL);
	registro.getCamposAdicionales().add(campoAdicionalIsIntercambioRegistral);

	if (DocumentacionFisicaEnum.DOCUMENTACION_FISICA_REQUERIDA == asientoRegistral
		.getDocumentacionFisica()) {
	    CampoAdicionalRegistroVO campoDocFisicaRequerida = new CampoAdicionalRegistroVO();
	    campoDocFisicaRequerida.setName(String.valueOf(AxSf.FLD504_FIELD_ID));
	    campoDocFisicaRequerida.setValue("1");
	    registro.getCamposAdicionales().add(campoDocFisicaRequerida);
	}
	else if (DocumentacionFisicaEnum.DOCUMENTACION_FISICA_COMPLEMENTARIA == asientoRegistral
		.getDocumentacionFisica()) {
	    CampoAdicionalRegistroVO campoDocFisicaCompl = new CampoAdicionalRegistroVO();
	    campoDocFisicaCompl.setName(String.valueOf(AxSf.FLD505_FIELD_ID));
	    campoDocFisicaCompl.setValue("1");
	    registro.getCamposAdicionales().add(campoDocFisicaCompl);
	}
	else {
	    CampoAdicionalRegistroVO noDocFisica = new CampoAdicionalRegistroVO();
	    noDocFisica.setName(String.valueOf(AxSf.FLD506_FIELD_ID));
	    noDocFisica.setValue("1");
	    registro.getCamposAdicionales().add(noDocFisica);
	}
	return result;
    }

    private String mapearCamposEnComentarios(AsientoRegistralVO asientoRegistral) {
	// Inicializamos el buffer con el comentario del registro, a no ser que
	// no tenga ninguno
	StringBuffer comentario = new StringBuffer("");
	if (StringUtils.isNotEmpty(asientoRegistral.getIdentificadorIntercambio())) {
	    comentario.append("Ident. Intercambio: ")
		    .append(asientoRegistral.getIdentificadorIntercambio()).append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getAplicacion())) {
	    comentario.append("Aplicación: ").append(asientoRegistral.getAplicacion())
		    .append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getNumeroRegistroInicial())) {
	    comentario.append("Num. Registro Inicial: ")
		    .append(asientoRegistral.getNumeroRegistroInicial()).append("\n");
	}

	if (StringUtils.isNotEmpty(asientoRegistral.getReferenciaExterna())) {
	    comentario.append("Referencia Externa: ")
		    .append(asientoRegistral.getReferenciaExterna()).append("\n");
	}

	// El tipo de asunto lo incluimos en el comentario, ya que el cod. puede
	// que no exista en el entorno que acepta el intercambio
	if (StringUtils.isNotEmpty(asientoRegistral.getCodigoAsunto())) {
	    comentario.append("Cod. Asunto: ").append(asientoRegistral.getCodigoAsunto())
		    .append("\n");
	}

	if (asientoRegistral.getTipoTransporte() != null) {
	    comentario.append("Tipo de Transporte: ")
		    .append(asientoRegistral.getTipoTransporte().getName()).append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getNumeroTransporte())) {
	    comentario.append("Num. Transporte:").append(asientoRegistral.getNumeroTransporte())
		    .append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getCodigoEntidadRegistralOrigen())) {

	    // Obtnemos la descripcion de la entidad registral, si la
	    // descripcion que recibimos es vacio lo seteamos con el codigo de
	    // la entidad
	    String descripcionEntidad = asientoRegistral.getDescripcionEntidadRegistralOrigen();
	    if (StringUtils.isEmpty(descripcionEntidad)) {
		descripcionEntidad = asientoRegistral.getCodigoEntidadRegistralOrigen();
	    }
	    comentario.append("Entidad Origen del Intercambio: ")
		    .append(asientoRegistral.getCodigoEntidadRegistralOrigen()).append(" - ")
		    .append(descripcionEntidad).append("\n");
	}

	if (StringUtils.isNotEmpty(asientoRegistral.getCodigoUnidadTramitacionOrigen())) {

	    // Obtnemos la descripcion de la unidad de tramitacion, si la
	    // descripcion que recibimos es vacia lo seteamos con el codigo de
	    // la unidad
	    String descripcionUnidad = asientoRegistral.getDescripcionUnidadTramitacionOrigen();
	    if (StringUtils.isEmpty(descripcionUnidad)) {
		descripcionUnidad = asientoRegistral.getCodigoUnidadTramitacionOrigen();
	    }

	    comentario.append("Unidad de Tramitación Origen del Intercambio: ")
		    .append(asientoRegistral.getCodigoUnidadTramitacionOrigen()).append(" - ")
		    .append(descripcionUnidad).append("\n");
	}

	if (StringUtils.isNotEmpty(asientoRegistral.getCodigoEntidadRegistralInicio())) {

	    // Obtnemos la descripcion de la entidad registral inicial, si la
	    // descripcion que recibimos es vacio lo seteamos con el codigo de
	    // la entidad inicial
	    String descripcionEntidadInicial =
		    asientoRegistral.getDescripcionEntidadRegistralInicio();
	    if (StringUtils.isEmpty(descripcionEntidadInicial)) {
		descripcionEntidadInicial = asientoRegistral.getCodigoEntidadRegistralInicio();
	    }

	    comentario.append("Entidad Inicial del Intercambio: ")
		    .append(asientoRegistral.getCodigoEntidadRegistralInicio()).append(" - ")
		    .append(descripcionEntidadInicial).append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getExpone())) {
	    comentario.append("Expone:").append(asientoRegistral.getExpone())
		    .append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getSolicita())) {
	    comentario.append("Solicita:").append(asientoRegistral.getSolicita())
		    .append("\n");
	}
	comentario = mapearRepresentantes(comentario, asientoRegistral);

	if (logger.isDebugEnabled()) {
	    logger.debug("Mapeados campos en el siguiente comentario: " + comentario);
	}
	return comentario.toString();
    }
    
    private RegistroEntradaVO mapearCamposEnComentarios(RegistroEntradaVO registro,
	    AsientoRegistralVO asientoRegistral) {
	// Inicializamos el buffer con el comentario del registro, a no ser que
	// no tenga ninguno
	StringBuffer comentario = new StringBuffer("");
	if (StringUtils.isNotEmpty(registro.getComentario())) {
	    comentario.append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getIdentificadorIntercambio())) {
	    comentario.append("Ident. Intercambio: ")
		    .append(asientoRegistral.getIdentificadorIntercambio()).append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getAplicacion())) {
	    comentario.append("Aplicación: ").append(asientoRegistral.getAplicacion())
		    .append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getNumeroRegistroInicial())) {
	    comentario.append("Num. Registro Inicial: ")
		    .append(asientoRegistral.getNumeroRegistroInicial()).append("\n");
	}

	if (StringUtils.isNotEmpty(asientoRegistral.getReferenciaExterna())) {
	    comentario.append("Referencia Externa: ")
		    .append(asientoRegistral.getReferenciaExterna()).append("\n");
	}

	// El tipo de asunto lo incluimos en el comentario, ya que el cod. puede
	// que no exista en el entorno que acepta el intercambio
	if (StringUtils.isNotEmpty(asientoRegistral.getCodigoAsunto())) {
	    comentario.append("Cod. Asunto: ").append(asientoRegistral.getCodigoAsunto())
		    .append("\n");
	}

	if (asientoRegistral.getTipoTransporte() != null) {
	    comentario.append("Tipo de Transporte: ")
		    .append(asientoRegistral.getTipoTransporte().getName()).append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getNumeroTransporte())) {
	    comentario.append("Num. Transporte:").append(asientoRegistral.getNumeroTransporte())
		    .append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getCodigoEntidadRegistralOrigen())) {

	    // Obtnemos la descripcion de la entidad registral, si la
	    // descripcion que recibimos es vacio lo seteamos con el codigo de
	    // la entidad
	    String descripcionEntidad = asientoRegistral.getDescripcionEntidadRegistralOrigen();
	    if (StringUtils.isEmpty(descripcionEntidad)) {
		descripcionEntidad = asientoRegistral.getCodigoEntidadRegistralOrigen();
	    }
	    comentario.append("Entidad Origen del Intercambio: ")
		    .append(asientoRegistral.getCodigoEntidadRegistralOrigen()).append(" - ")
		    .append(descripcionEntidad).append("\n");
	}

	if (StringUtils.isNotEmpty(asientoRegistral.getCodigoUnidadTramitacionOrigen())) {

	    // Obtnemos la descripcion de la unidad de tramitacion, si la
	    // descripcion que recibimos es vacia lo seteamos con el codigo de
	    // la unidad
	    String descripcionUnidad = asientoRegistral.getDescripcionUnidadTramitacionOrigen();
	    if (StringUtils.isEmpty(descripcionUnidad)) {
		descripcionUnidad = asientoRegistral.getCodigoUnidadTramitacionOrigen();
	    }

	    comentario.append("Unidad de Tramitación Origen del Intercambio: ")
		    .append(asientoRegistral.getCodigoUnidadTramitacionOrigen()).append(" - ")
		    .append(descripcionUnidad).append("\n");
	}

	if (StringUtils.isNotEmpty(asientoRegistral.getCodigoEntidadRegistralInicio())) {

	    // Obtnemos la descripcion de la entidad registral inicial, si la
	    // descripcion que recibimos es vacio lo seteamos con el codigo de
	    // la entidad inicial
	    String descripcionEntidadInicial =
		    asientoRegistral.getDescripcionEntidadRegistralInicio();
	    if (StringUtils.isEmpty(descripcionEntidadInicial)) {
		descripcionEntidadInicial = asientoRegistral.getCodigoEntidadRegistralInicio();
	    }

	    comentario.append("Entidad Inicial del Intercambio: ")
		    .append(asientoRegistral.getCodigoEntidadRegistralInicio()).append(" - ")
		    .append(descripcionEntidadInicial).append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getExpone())) {
	    comentario.append("Expone:").append(asientoRegistral.getExpone())
		    .append("\n");
	}
	if (StringUtils.isNotEmpty(asientoRegistral.getSolicita())) {
	    comentario.append("Solicita:").append(asientoRegistral.getSolicita())
		    .append("\n");
	}
	comentario = mapearRepresentantes(comentario, asientoRegistral);

	String comentarioResult = registro.getComentario();
	if (comentarioResult == null) {
	    comentarioResult = "";
	}
	comentarioResult += comentario.toString();
	registro.setComentario(comentarioResult);

	if (logger.isDebugEnabled()) {
	    logger.debug("Mapeados campos en el siguiente comentario: " + comentario);
	}
	return registro;
    }

    private StringBuffer mapearRepresentantes(StringBuffer comentario,
	    AsientoRegistralVO asientoRegistral) {
	for (InteresadoVO interesado : asientoRegistral.getInteresados()) {
	    if ((StringUtils.isNotEmpty(interesado.getNombreRepresentante()))
		    || (StringUtils.isNotEmpty(interesado.getRazonSocialRepresentante()))) {
		comentario.append("El representante de ").append(getInteresadoString(interesado))
			.append(" es ").append(getRepresentanteString(interesado)).append("\n");
	    }
	}
	return comentario;
    }

    private RegistroEntradaVO mapearTerceros(RegistroEntradaVO registro,
	    AsientoRegistralVO asientoRegistral) {
	List<es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO> listaTerceros =
		new ArrayList<es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO>();

	RegistroEntradaVO result = registro;
	List<InteresadoVO> listaInteresados = asientoRegistral.getInteresados();
	es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO tercero;

	for (InteresadoVO interesado : listaInteresados) {
	    if (!esVacioInteresado (interesado)){
		tercero = mapearInteresado(interesado);
		listaTerceros.add(tercero);
	    }
	    
	}
	result.setInteresados(listaTerceros);
	// inicializamos la variable que indica si el interesado es principal

	/*
	 * boolean interesadoPrincipal = true;
	 * 
	 * String terceroString = null; for (int i = 0; i <
	 * listaInteresados.size(); i++) { InteresadoVO interesado =
	 * listaInteresados.get(i);
	 * 
	 * //Obtenemos la cadena con los datos del interesado terceroString =
	 * getInteresadoString(interesado);
	 * 
	 * // verificamos si esa cadena es mayor al tamaï¿½o del campo if
	 * (terceroString.length() > 95) { // descomponemos los datos del
	 * interesado en partes (nombre y // apellidos, documentacion y/o
	 * direccion) List<String> datosTercero =
	 * InteresadoUtils.getDatosInteresadoArray(interesado); // recorremos
	 * los datos del interesado (nombre/apellidos, // identificacion y
	 * direccion) para tratarlos como partes // independientes for
	 * (Iterator<String> it = datosTercero.iterator(); it .hasNext();) {
	 * String cadenaInteresado = (String) it.next(); // aï¿½adimos la
	 * informaciï¿½n al listado de interesados interesadoPrincipal =
	 * addInteresado(registro, listaTerceros, cadenaInteresado,
	 * interesadoPrincipal); } }else{ // mantenemos toda la informaciï¿½n en
	 * una sola linea, aï¿½adimos la // informaciï¿½n al listado de
	 * interesados interesadoPrincipal = addInteresado(registro,
	 * listaTerceros, terceroString, interesadoPrincipal); } }
	 */

	return result;
    }

    private boolean esVacioInteresado(InteresadoVO interesado) {
	boolean result = false;
	if (interesado == null ){
	    result = true;
	}else {
	    if (StringUtils.isBlank(interesado.getNombreInteresado()) &&
		StringUtils.isBlank(interesado.getPrimerApellidoInteresado()) &&
		StringUtils.isBlank(interesado.getRazonSocialInteresado())){
		    result = true;
		}
	}
	return result;
    }

    private es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO mapearInteresado(
	    InteresadoVO interesado) {
	es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO tercero =
		new es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO();
	TerceroValidadoVO terceroVO = null;
	String nombre = null;
	if ((interesado.getRazonSocialInteresado() != null
		&& !interesado.getRazonSocialInteresado().equals("")) 
		|| (interesado.getTipoDocumentoIdentificacionInteresado() != null && "O".equals(interesado.getTipoDocumentoIdentificacionInteresado().getValue()))) {
	    terceroVO = new TerceroValidadoJuridicoVO();
	    nombre = interesado.getRazonSocialInteresado();
	    ((TerceroValidadoJuridicoVO) terceroVO).setRazonSocial(nombre);
	}
	else if (interesado.getNombreInteresado() != null
		&& !interesado.getNombreInteresado().equals("")) {
	    terceroVO = new TerceroValidadoFisicoVO();
	    nombre =
		    interesado.getNombreInteresado()
			    + (interesado.getPrimerApellidoInteresado() != null ? " "
				    + interesado.getPrimerApellidoInteresado() : "")
			    + (interesado.getSegundoApellidoInteresado() != null ? " "
				    + interesado.getSegundoApellidoInteresado() : "");
	    terceroVO.setNombre(interesado.getNombreInteresado());
	    ((TerceroValidadoFisicoVO) terceroVO).setApellido1((interesado
		    .getPrimerApellidoInteresado() != null ? interesado
		    .getPrimerApellidoInteresado() : null));
	    ((TerceroValidadoFisicoVO) terceroVO).setApellido2((interesado
		    .getSegundoApellidoInteresado() != null ? interesado
		    .getSegundoApellidoInteresado() : null));
	}
	// tercero

	terceroVO.setNumeroDocumento(interesado.getDocumentoIdentificacionInteresado());
	if (interesado.getDocumentoIdentificacionInteresado() != null) {
	    TipoDocumentoIdentificativoTerceroVO tipo = new TipoDocumentoIdentificativoTerceroVO();
	    tipo.setCodigo(interesado.getTipoDocumentoIdentificacionInteresado().getValue());
	    tipo.setDescripcion(interesado.getTipoDocumentoIdentificacionInteresado().getName());
	    tipo.setId(TipoDocumentoIdentificativo.getTipoDocumentoIdentificativo(
		    interesado.getTipoDocumentoIdentificacionInteresado().getValue()).getName());
	    terceroVO.setTipoDocumento(tipo);
	}
	tercero.setTercero(terceroVO);

	tercero.setNombre(nombre);
	// representante
	String nombreRep = null;
	TerceroValidadoVO terceroVORep = null;
	es.ieci.tecdoc.isicres.terceros.business.vo.RepresentanteInteresadoVO representante = null;
	if (interesado.getNombreRepresentante() != null
		&& !interesado.getNombreRepresentante().equals("")) {
	    representante =
		    new es.ieci.tecdoc.isicres.terceros.business.vo.RepresentanteInteresadoVO();
	    if (interesado.getNombreRepresentante() != null) {
		terceroVORep = new TerceroValidadoFisicoVO();
		nombreRep =
			interesado.getNombreRepresentante()
				+ (interesado.getPrimerApellidoRepresentante() != null ? " "
					+ interesado.getPrimerApellidoRepresentante() : "")
				+ (interesado.getSegundoApellidoRepresentante() != null ? " "
					+ interesado.getSegundoApellidoRepresentante() : "");
		terceroVORep.setNombre(interesado.getNombreRepresentante());
		((TerceroValidadoFisicoVO) terceroVORep).setApellido1((interesado
			.getPrimerApellidoRepresentante() != null ? interesado
			.getPrimerApellidoRepresentante() : null));
		((TerceroValidadoFisicoVO) terceroVORep).setApellido2((interesado
			.getSegundoApellidoRepresentante() != null ? interesado
			.getSegundoApellidoRepresentante() : null));
	    }
	    else {
		terceroVORep = new TerceroValidadoJuridicoVO();
		nombreRep = interesado.getRazonSocialRepresentante();
		((TerceroValidadoJuridicoVO) terceroVORep).setRazonSocial(nombreRep);
	    }

	    terceroVORep.setNumeroDocumento(interesado.getDocumentoIdentificacionRepresentante());
	    if (interesado.getTipoDocumentoIdentificacionRepresentante() != null) {
		TipoDocumentoIdentificativoTerceroVO tipoRep =
			new TipoDocumentoIdentificativoTerceroVO();
		tipoRep.setCodigo(interesado.getTipoDocumentoIdentificacionRepresentante()
			.getValue());
		tipoRep.setDescripcion(interesado.getTipoDocumentoIdentificacionRepresentante()
			.getName());
		tipoRep.setId(TipoDocumentoIdentificativo.getTipoDocumentoIdentificativo(
			    interesado.getTipoDocumentoIdentificacionRepresentante().getValue()).getName());
		terceroVORep.setTipoDocumento(tipoRep);
	    }
	    representante.setRepresentante(terceroVORep);
	    representante.setNombre(nombreRep);
	}

	tercero.setRepresentante(representante);
	return tercero;

    }

    private boolean addInteresado(RegistroEntradaVO registro,
	    List<es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO> listaTerceros,
	    String terceroString, boolean interesadoPrincipal) {

	es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO tercero =
		new es.ieci.tecdoc.isicres.terceros.business.vo.InteresadoVO();

	// Comprobamos si la cadena es distinta de vacia
	if (!StringUtils.isEmpty(terceroString)) {
	    // comprobamos si es interesado principal, para ello
	    // consideramos el primer interesado vï¿½lido, como interesado
	    // principal
	    tercero.setPrincipal(interesadoPrincipal);
	    tercero.setNombre(terceroString);
	    if (interesadoPrincipal) {
		// Asignamos el tercero como interesado principal
		registro.setInteresadoPrincipal(tercero);
		// resetamos la variable que indica si el interesado es
		// interesado principal, ya que solo puede ser principal el
		// primer interesado valido
		interesadoPrincipal = false;
	    }
	    else {
		// aï¿½adimos el resto de interesados al listado de terceros
		listaTerceros.add(tercero);
	    }
	}
	return interesadoPrincipal;
    }

    private void mapearUnidadesAdministrativas(RegistroEntradaVO registro,
	    AsientoRegistralVO asientoRegistral) {
	StringBuffer comentario = new StringBuffer("");
	UnidadAdministrativaIntercambioRegistralVO unidadDestino =
		getConfiguracionIntercambioRegistralManager()
			.getUnidadAdministrativaByCodigosComunes(
				asientoRegistral.getCodigoEntidadRegistralDestino(),
				asientoRegistral.getCodigoUnidadTramitacionDestino());
	UnidadAdministrativaIntercambioRegistralVO unidadOrigen =
		getConfiguracionIntercambioRegistralManager()
			.getUnidadAdministrativaByCodigosComunes(
				asientoRegistral.getCodigoEntidadRegistralOrigen(),
				asientoRegistral.getCodigoUnidadTramitacionOrigen());
	UnidadAdministrativaVO unidadAdministrativaDestino = new UnidadAdministrativaVO();
	UnidadAdministrativaVO unidadAdministrativaOrigen = new UnidadAdministrativaVO();
	String codigoDestinoEntidadRegistral = asientoRegistral.getCodigoEntidadRegistralDestino();
	String codigoDestinoUnidadTram = asientoRegistral.getCodigoUnidadTramitacionDestino();
	String codigoOrigenEntidadRegistral = asientoRegistral.getCodigoEntidadRegistralOrigen();
	String codigoOrigenUnidadTram = asientoRegistral.getCodigoUnidadTramitacionOrigen();
	comentario
		.append("Este registro ha sido creado a partir de un intercambio registral con el siguiente origen y destino:");
	comentario.append("\n");
	if (StringUtils.isNotEmpty(codigoOrigenEntidadRegistral)) {
	    comentario.append("Codigo DC Entidad Registral origen:" + codigoOrigenEntidadRegistral);
	    comentario.append("\n");
	}
	if (StringUtils.isNotEmpty(codigoOrigenUnidadTram)) {
	    comentario.append("Codigo DC Unidad Tramitaciï¿½n origen:" + codigoOrigenUnidadTram);
	    comentario.append("\n");
	}
	if (StringUtils.isNotEmpty(codigoDestinoEntidadRegistral)) {
	    comentario.append("Codigo DC Entidad registral destino:"
		    + codigoDestinoEntidadRegistral);
	    comentario.append("\n");
	}
	if (StringUtils.isNotEmpty(codigoDestinoUnidadTram)) {
	    comentario.append("Codigo DC Unidad Tramitaciï¿½n destino:" + codigoDestinoUnidadTram);
	    comentario.append("\n");
	}

	String comentarioResult = registro.getComentario();
	if (comentarioResult == null) {
	    comentarioResult = "";
	}
	comentarioResult += comentario.toString();
	registro.setComentario(comentarioResult);
	unidadAdministrativaDestino.setCodigoUnidad(unidadDestino.getCode());
	unidadAdministrativaOrigen.setCodigoUnidad(unidadOrigen.getCode());
	registro.setUnidadAdministrativaDestino(unidadAdministrativaDestino);
	registro.setUnidadAdministrativaOrigen(unidadAdministrativaOrigen);

    }

    /**
     * Mï¿½tdo que obtiene los anexos de un registro
     * 
     * @param asientoRegistral
     *            - Informaciï¿½n del asiento registral
     * @param registro
     *            - Informaciï¿½n bï¿½sica del registro
     * 
     * @return Listado de objetos {@link DocumentoRegistroVO}
     */
    private List<DocumentoRegistroVO> getAnexos(AsientoRegistralVO asientoRegistral,
	    BaseRegistroVO registro) {
	List<AnexoVO> anexos = asientoRegistral.getAnexos();

	if (logger.isDebugEnabled()) {
	    logger.debug("Se van a recupear los anexos del intercambio registral con identificador = "
		    + asientoRegistral.getIdentificadorIntercambio());
	}

	if (anexos == null || anexos.size() == 0) {
	    if (logger.isDebugEnabled()) {
		logger.debug("El intercambio no tiene anexos");
	    }
	    return null;
	}

	// Verificamos los anexos adjuntados
	List<AnexoVO> anexosVerificados = verificarAnexos(anexos);

	// Generamos el listado de DocumentosRegistroVO a partir de los anexos
	// verificados
	List<DocumentoRegistroVO> documentoRegistro =
		mapearListAnexoVOaListDocumentoRegistroVO(anexosVerificados);

	// retornamos el array de documentos
	return documentoRegistro;
    }

    /**
     * Método que genera un listado de objetos tipo {@link DocumentoRegistroVO}
     * 
     * @param anexos
     *            - {@link AnexoVO}
     * 
     * @return listado de objetos tipo {@link DocumentoRegistroVO}
     */
    private List<DocumentoRegistroVO>
	    mapearListAnexoVOaListDocumentoRegistroVO(List<AnexoVO> anexos) {

	List<DocumentoRegistroVO> result = new ArrayList<DocumentoRegistroVO>();

	// Generamos documento
	DocumentoRegistroVO documento = null;
	new DocumentoRegistroVO();
	AnexoVO anexo = null;
	List<PaginaDocumentoRegistroVO> paginas = null;
	int numPagina = 1;
	for (Iterator it = (Iterator) anexos.iterator(); it.hasNext();) {
	    documento = new DocumentoRegistroVO();
	    anexo = (AnexoVO) it.next();
	    paginas = new ArrayList<PaginaDocumentoRegistroVO>();
	    PaginaDocumentoRegistroVO pagina = createPaginaDocumentoRegistro(anexo, numPagina);
	    // añadimos la pagina al array de paginas
	    paginas.add(pagina);
	    documento.setName(pagina.getName());
	    documento.setPaginas(paginas);
	    result.add(documento);
	}

	return result;
    }

    /**
     * Método que genera un listado de objetos {@link PaginaDocumentoRegistroVO}
     * 
     * @param anexos
     *            - {@link AnexoVO}
     * 
     * @return listado de objetos {@link PaginaDocumentoRegistroVO}
     */
    private List<PaginaDocumentoRegistroVO> generatePaginasDocumentoRegistro(List<AnexoVO> anexos) {

	AnexoVO anexo = null;
	// contador de pagina
	int numPagina = 1;

	List<PaginaDocumentoRegistroVO> paginas = new ArrayList<PaginaDocumentoRegistroVO>();
	for (Iterator it = (Iterator) anexos.iterator(); it.hasNext();) {
	    anexo = (AnexoVO) it.next();

	    // Obtenemos la informaciï¿½n de la pagina
	    PaginaDocumentoRegistroVO pagina = createPaginaDocumentoRegistro(anexo, numPagina);

	    // aï¿½adimos la pagina al array de paginas
	    paginas.add(pagina);

	    // incrementamos el numero de pagina
	    numPagina++;
	}
	return paginas;
    }

    /**
     * Mï¿½todo que valida que los anexos del intercambio son correctos. En la
     * actualidad, se valida que el nombre de los ficheros no se repita, si es
     * asi aï¿½adimos un sufijo
     * 
     * @param anexosVO
     *            - Listado de objetos {@link AnexoVO}
     * 
     * @return Listado de objetos {@link AnexoVO} verificados y adaptados
     */
    private List<AnexoVO> verificarAnexos(List<AnexoVO> anexosVO) {
	// Inicializamos el array de anexos verificados
	List<AnexoVO> result = new ArrayList<AnexoVO>();

	// Generamos una coleccion para verificar el nombre de las paginas
	// dentro de un documento
	Map<String, AnexoVO> nombresDeAnexosVO = new HashMap<String, AnexoVO>();
	// Creamos la variable contador para nombre de anexos con el mismo
	// nombre
	int count_nombreAnexo_duplicado = 0;

	for (Iterator it = (Iterator) anexosVO.iterator(); it.hasNext();) {
	    AnexoVO anexoVO = (AnexoVO) it.next();

	    // obtenemos el nombre de la pagina/fichero
	    String nombreFichero =
		    obtenerNombrePagina(count_nombreAnexo_duplicado, anexoVO, nombresDeAnexosVO);

	    // aï¿½adimos la informaciï¿½n del nombre al hashMap para que no se
	    // repita
	    nombresDeAnexosVO.put(nombreFichero, anexoVO);

	    // asignamos el nombre correcto al anexoVO
	    anexoVO.setNombreFichero(nombreFichero);

	    // aï¿½adimos el anexoVO a los anexos verificados
	    result.add(anexoVO);
	}

	// retornamos el array de anexos verificados
	return result;
    }

    /**
     * Mï¿½todo que crea un objeto {@link PaginaDocumentoRegistroVO} a partir de
     * los datos recibidos como parï¿½metros
     * 
     * @param anexoVO
     *            - {@link AnexoVO}
     * @param numPagina
     *            - Nï¿½mero de la pagina
     * 
     * @return {@link PaginaDocumentoRegistroVO}
     */
    private PaginaDocumentoRegistroVO createPaginaDocumentoRegistro(AnexoVO anexoVO, int numPagina) {

	PaginaDocumentoRegistroVO pagina = new PaginaDocumentoRegistroVO();

	// Contenido del fichero
	byte[] contenido = getIntercambioRegistralSIRManager().getContenidoAnexo(anexoVO.getId());

	// obtenemos el objeto del documento fï¿½sico
	DocumentoFisicoVO documentoFisico =
		createDocumentoFisicoVO(contenido, anexoVO.getNombreFichero());

	// aï¿½adimos a la pagina del documento fisico
	pagina.setDocumentoFisico(documentoFisico);

	// aï¿½adimos el nombre a la pagina
	pagina.setName(anexoVO.getNombreFichero());

	// aï¿½adimos el numero de pagina
	pagina.setNumeroPagina(numPagina);

	return pagina;
    }

    /**
     * Mï¿½todo que genera un {@link DocumentoRegistroVO} a partir de los datos
     * recibidos
     * 
     * @param contenido
     *            - Array de bytes con el contenido del fichero
     * @param nombreFichero
     *            - Nombre del fichero
     * 
     * @return - {@link DocumentoFisicoVO}
     */
    private DocumentoFisicoVO createDocumentoFisicoVO(byte[] contenido, String nombreFichero) {
	DocumentoFisicoVO documentoFisico = new DocumentoFisicoVO();
	// seteamos el valor del nombre de la pagina
	documentoFisico.setName(nombreFichero);

	// asignamos el contenido del fichero
	documentoFisico.setContent(contenido);

	// a partir del nombre del fichero obtenemos la extensiï¿½n
	documentoFisico.setExtension(nombreFichero.substring(nombreFichero.lastIndexOf(".") + 1,
		nombreFichero.length()));

	return documentoFisico;
    }

    /**
     * Mï¿½todo que obtiene el nombre de la pagina/fichero, validando que no se
     * repita en un mismo documento dos paginas con el mismo nombre
     * 
     * @param count_nombreAnexo_duplicado
     *            - Numero de la pagina
     * @param anexoVO
     *            - Informaciï¿½n de la pagina/documento/fichero
     * @param nombresDeAnexosVO
     *            - Colecciï¿½n con los anexos
     * @param documentoFisico
     *            - {@link DocumentoFisicoVO}
     * 
     * @return Nombre de la pagina/fichero
     */
    private String obtenerNombrePagina(int count_nombreAnexo_duplicado, AnexoVO anexoVO,
	    Map<String, AnexoVO> nombresDeAnexosVO) {

	// Obtenemos el nombre original de la pagina
	String result = anexoVO.getNombreFichero();

	// obtenemos el nombre del fichero
	String nombreFichero = result.substring(0, result.lastIndexOf("."));
	// obtenemos la extension del fichero
	String extension = result.substring(result.lastIndexOf("."), result.length());

	// comprobamos que el nombre de la pagina no exista ya en el documento
	if (nombresDeAnexosVO.containsKey(result)) {
	    boolean registrado = false;
	    while (!registrado) {
		// si existe aï¿½adimos un sufijo, para que no existan dos
		// ficheros
		// con el mismo nombre
		count_nombreAnexo_duplicado++;
		String sufijo = "_" + count_nombreAnexo_duplicado;

		// comprobamos que el nombre de la pagina maximo sea de 64
		// caracteres
		if ((result + sufijo).length() > IDocKeys.MAX_LENGTH_NAME_FILE) {
		    // si excede el tamaï¿½o recortamos el nombre del documento
		    result =
			    (nombreFichero.substring(
				    0,
				    IDocKeys.MAX_LENGTH_NAME_FILE
					    - (sufijo.length() + extension.length()))
				    + sufijo + extension);
		    if (!nombresDeAnexosVO.containsKey(result)) {
			// generamos el nombre del documento
			registrado = true;
		    }
		}
		else {
		    result = nombreFichero + sufijo + extension;
		    if (!nombresDeAnexosVO.containsKey(result)) {
			// generamos el nombre del documento
			registrado = true;
		    }
		}
	    }
	}
	else {
	    // No existe el nombre de la pagina en el documento, por tanto
	    // procedemos a comprobar que el nombre tiene una longitud vï¿½lida
	    // (64 caracteres)
	    if (result.length() > IDocKeys.MAX_LENGTH_NAME_FILE) {
		// truncamos el nombre del fichero a 64 caracteres
		result =
			(nombreFichero.substring(0,
				(IDocKeys.MAX_LENGTH_NAME_FILE - extension.length())))
				+ extension;
	    }
	}

	return result;
    }

    private String getInteresadoString(InteresadoVO interesado) {
	StringBuffer nombreTercero = new StringBuffer("");

	if (interesado.getTipoDocumentoIdentificacionInteresado() != TipoDocumentoIdentificacionEnum.CIF) {
	    if (!StringUtils.isEmpty(interesado.getNombreInteresado())) {
		nombreTercero.append(interesado.getNombreInteresado()).append(BLANK);
	    }

	    if (!StringUtils.isEmpty(interesado.getPrimerApellidoInteresado())) {
		nombreTercero.append(interesado.getPrimerApellidoInteresado()).append(BLANK);
	    }

	    if (!StringUtils.isEmpty(interesado.getSegundoApellidoInteresado())) {
		nombreTercero.append(interesado.getSegundoApellidoInteresado());
	    }

	}
	else {
	    if (!StringUtils.isEmpty(interesado.getRazonSocialInteresado())) {
		nombreTercero.append(interesado.getRazonSocialInteresado());
	    }
	}

	if (interesado.getTipoDocumentoIdentificacionInteresado() != null) {
	    nombreTercero.append(" - ").append(
		    interesado.getTipoDocumentoIdentificacionInteresado().getValue());
	}

	if (!StringUtils.isEmpty(interesado.getDocumentoIdentificacionInteresado())) {
	    nombreTercero.append(": ").append(interesado.getDocumentoIdentificacionInteresado());
	}

	if (interesado.getCanalPreferenteComunicacionInteresado() == CanalNotificacionEnum.DIRECCION_POSTAL) {
	    if (!StringUtils.isEmpty(interesado.getDireccionInteresado())) {
		nombreTercero.append(" - ").append(interesado.getDireccionInteresado());
	    }
	}
	else if (interesado.getDireccionElectronicaHabilitadaInteresado() != null) {
	    if (!StringUtils.isEmpty(interesado.getDireccionElectronicaHabilitadaInteresado())) {
		nombreTercero.append(" - ").append(
			interesado.getDireccionElectronicaHabilitadaInteresado());
	    }
	}

	return nombreTercero.toString();
    }

    private String getRepresentanteString(InteresadoVO interesado) {
	StringBuffer nombreTercero = new StringBuffer("");
	if (interesado.getTipoDocumentoIdentificacionRepresentante() != TipoDocumentoIdentificacionEnum.CIF) {
	    if (!StringUtils.isEmpty(interesado.getNombreRepresentante())) {
		nombreTercero.append(interesado.getNombreRepresentante()).append(BLANK);
	    }

	    if (!StringUtils.isEmpty(interesado.getPrimerApellidoRepresentante())) {
		nombreTercero.append(interesado.getPrimerApellidoRepresentante()).append(BLANK);
	    }

	    if (!StringUtils.isEmpty(interesado.getSegundoApellidoRepresentante())) {
		nombreTercero.append(interesado.getSegundoApellidoRepresentante());
	    }

	}
	else {
	    if (!StringUtils.isEmpty(interesado.getRazonSocialRepresentante())) {
		nombreTercero.append(interesado.getRazonSocialRepresentante());
	    }
	}

	if (interesado.getTipoDocumentoIdentificacionRepresentante() != null) {
	    nombreTercero.append(" - ").append(
		    interesado.getTipoDocumentoIdentificacionRepresentante().getValue());
	}

	if (!StringUtils.isEmpty(interesado.getDocumentoIdentificacionRepresentante())) {
	    nombreTercero.append(": ").append(interesado.getDocumentoIdentificacionRepresentante());
	}

	if (interesado.getCanalPreferenteComunicacionRepresentante() == CanalNotificacionEnum.DIRECCION_POSTAL) {
	    if (!StringUtils.isEmpty(interesado.getDireccionRepresentante())) {
		nombreTercero.append(" - ").append(interesado.getDireccionRepresentante());
	    }

	}
	else {
	    if (!StringUtils.isEmpty(interesado.getDireccionElectronicaHabilitadaRepresentante())) {
		nombreTercero.append(" - ").append(
			interesado.getDireccionElectronicaHabilitadaRepresentante());
	    }
	}
	return nombreTercero.toString();
    }

    public ConfiguracionIntercambioRegistralManager getConfiguracionIntercambioRegistralManager() {
	return configuracionIntercambioRegistralManager;
    }

    public void setConfiguracionIntercambioRegistralManager(
	    ConfiguracionIntercambioRegistralManager configuracionIntercambioRegistralManager) {
	this.configuracionIntercambioRegistralManager = configuracionIntercambioRegistralManager;
    }

    public BandejaEntradaIntercambioRegistralDAO getBandejaEntradaIntercambioRegistralDAO() {
	return bandejaEntradaIntercambioRegistralDAO;
    }

    public void setBandejaEntradaIntercambioRegistralDAO(
	    BandejaEntradaIntercambioRegistralDAO bandejaEntradaIntercambioRegistralDAO) {
	this.bandejaEntradaIntercambioRegistralDAO = bandejaEntradaIntercambioRegistralDAO;
    }

    public DataFieldMaxValueIncrementer getIntercambioRegistralEntradaIncrementer() {
	return intercambioRegistralEntradaIncrementer;
    }

    public void setIntercambioRegistralEntradaIncrementer(
	    DataFieldMaxValueIncrementer intercambioRegistralEntradaIncrementer) {
	this.intercambioRegistralEntradaIncrementer = intercambioRegistralEntradaIncrementer;
    }

    public IntercambioRegistralSIRManager getIntercambioRegistralSIRManager() {
	return intercambioRegistralSIRManager;
    }

    public void setIntercambioRegistralSIRManager(
	    IntercambioRegistralSIRManager intercambioRegistralSIRManager) {
	this.intercambioRegistralSIRManager = intercambioRegistralSIRManager;
    }

    public DocumentoElectronicoAnexoManager getDocumentoElectronicoAnexoManager() {
        return documentoElectronicoAnexoManager;
    }

    public void setDocumentoElectronicoAnexoManager(
    	DocumentoElectronicoAnexoManager documentoElectronicoAnexoManager) {
        this.documentoElectronicoAnexoManager = documentoElectronicoAnexoManager;
    }

    public void rechazarIntercambioRegistralEntradaById(String idIntercambioRegistralEntrada,
	    String tipoRechazo, String observaciones, UsuarioVO user, Integer idOficina,
	    String codOficina) {
	if (logger.isDebugEnabled()) {
	    logger.debug("Se va a rechazar el intercambio registral de entrada con id de intercambio = "
		    + idIntercambioRegistralEntrada);
	}


	// obtener datos del intercambio registral
	AsientoRegistralVO asientoRegistral =
		getIntercambioRegistralSIRManager().getAsientoRegistral(idIntercambioRegistralEntrada);

	IntercambioRegistralEntradaVO intercambioRegistralEntradaVO =
		populateIntercambioRegistralEntrada(user, asientoRegistral, idOficina,
			idIntercambioRegistralEntrada, EstadoIntercambioRegistralEntradaEnumVO.RECHAZADO,
			observaciones);

	// Guardamos en la tabal scr_exregin
	guardarIntercambioRegistralEntrada(intercambioRegistralEntradaVO);

	// y actualizamos estado en el SIR
	getIntercambioRegistralSIRManager().rechazarAsientoRegistral(idIntercambioRegistralEntrada,
		user.getFullName(), user.getUserContact(), TipoRechazoEnum.getTipoRechazo(Integer.valueOf(tipoRechazo)), observaciones);

	
    }

    public void reenviarIntercambioRegistralEntradaById(String idIntercambioRegistralEntrada,
	    UnidadTramitacionIntercambioRegistralVO nuevoDestino, String observaciones,
	    UsuarioVO user, Integer idOficina, String codOficina) {

	// TODO: Almacena el asiento registral en la tabla scr_exregin
	// obtener datos del intercambio registral
	AsientoRegistralVO asientoRegistral =
		getIntercambioRegistralSIRManager().getAsientoRegistral(
			idIntercambioRegistralEntrada);
	// vamos a a realizar actualizacion del estado del intercambio en local,
	// pasa a estado aceptado EstadoIntercambioRegistralEntradaEnumVO

	IntercambioRegistralEntradaVO intercambioRegistralEntradaVO =
		populateIntercambioRegistralEntrada(user, asientoRegistral, idOficina,
			idIntercambioRegistralEntrada,
			EstadoIntercambioRegistralEntradaEnumVO.REENVIADO, observaciones);

	// Guardamos en la tabal scr_exregin y actualizamos estado en el SIR
	guardarIntercambioRegistralEntrada(intercambioRegistralEntradaVO);

	// TODO: ï¿½Contacto puede ser fullName? contacto y usuario es opcional,
	// no se envia
	getIntercambioRegistralSIRManager().reenviarAsientoRegistral(idIntercambioRegistralEntrada,
		user.getFullName(), user.getUserContact(), observaciones, nuevoDestino);
	
    }

}

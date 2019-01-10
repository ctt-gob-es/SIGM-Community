package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacpublicador.business.attribute.AttributeContext;
import ieci.tdw.ispac.ispacpublicador.business.context.RuleContext;
import ieci.tdw.ispac.ispacpublicador.business.exceptions.ActionException;
import ieci.tecdoc.sgm.base.guid.Guid;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.ConstantesServicios;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.ConsultaExpedientesException;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.ct.GestorConsulta;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.publicador.service.ConsultaTelematicaService;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;

/**
 * Acción para dar de alta un hito del estado de un expediente 
 * en la Consulta Telemática.
 * 
 */
public class EstablecerHitoCierreExpAction extends SigemBaseAction {

	//========================================================================
	// Constantes de los nombres de los parámetros de la regla
	//========================================================================
	private static final String CODIGO 				= "CODIGO";
	//========================================================================
	
	/** Logger de la clase. */
    private static final Logger logger = Logger.getLogger(EstablecerHitoCierreExpAction.class);

	/** Logger de la clase. */
    private static final Logger CONSULTA_TELEMATICA = Logger.getLogger("CONSULTA_TELEMATICA");

    
    /**
     * Constructor.
     * 
     */
    public EstablecerHitoCierreExpAction() {
    	super();
    }
    
    
    /**
     * Ejecuta la acción.
     * @param rctx Contexto de ejecución de la regla
     * @param attContext Atributos con información extra, utilizados dentro de 
     * la ejecución de la regla.
     * @return true si la ejecución termina correctamente, false en caso 
     * contrario.
     * @throws ActionException si ocurre algún error.
     */
    public boolean execute(RuleContext rctx, AttributeContext attContext) throws ActionException {
    	
        if (logger.isInfoEnabled()) {
            logger.info("Acción [" + this.getClass().getName() + "] en ejecución");
        }

        HitoExpediente hito = new HitoExpediente();
        ExpedienteVO expediente = null;

        try {
        	Expediente existeExp = null;
        	String numExp = rctx.getIdObjeto();
	        ConsultaTelematicaService serviceExp = new ConsultaTelematicaService();
	        expediente = serviceExp.getExpediente(numExp);
    		ServicioConsultaExpedientes consultaExp = LocalizadorServicios.getServicioConsultaExpedientes();

    		try{
    			existeExp = consultaExp.obtenerDetalle(expediente.getCnum(), getEntidad());
    		}
    		catch(Exception e){}
    		if( existeExp != null){
	        	
	        	// Establecer el hito actual
	        	hito = establecerHitoActual(rctx, attContext);
        	
	        	cerrarExpediente(hito.getNumeroExpediente(), getEntidad());
	        }

	        // Log del hito
	        logOk(hito);
        } catch (Exception e) {
        	setInfo("Error al establecer hito actual: " + e.toString());
        	logError(hito, e);
            throw new ActionException(e);
        }
        return true;
    }
    
    private HitoExpediente establecerHitoActual(RuleContext rctx, AttributeContext attContext) throws SigemException, ActionException {
    	
    	// Información del hito
    	HitoExpediente hito = new HitoExpediente();
		hito.setGuid(new Guid().toString());
		hito.setNumeroExpediente(rctx.getIdObjeto());
		hito.setCodigo(StringUtils.nullToEmpty(attContext.getAttribute(CODIGO)));
		hito.setFecha(TypeConverter.toString(rctx.getFecha(), "yyyy-MM-dd"));
		
		ClientContext cct = new ClientContext();
		cct.setAPI(new InvesflowAPI(cct));
		
		IItem exp;
		try {
			exp = ExpedientesUtil.getExpediente(cct, rctx.getIdObjeto());	
		
			hito.setDescripcion("El expediente: " + exp.getString("NUMEXP")  + " - " + exp.getString("ASUNTO") + " ha sido cerrado.");
		} catch (ISPACException e) {
			setInfo("Error al componer el mensaje del hito actual: " + e.toString());
        	logError(hito, e);
            throw new ActionException(e);
		}
		
		hito.setInformacionAuxiliar("");
		
		// Paso a histórico del hito anterior
        boolean pasoAHistorico = true;

        // Establece el hito actual en Consulta Telemática
		ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
		consulta.establecerHitoActual(hito, new FicherosHito(), pasoAHistorico, getEntidad());
		
		// Devolver la información del hito creado
		return consulta.obtenerHitoEstado(rctx.getIdObjeto(), getEntidad());
    }
    
    /**
     * Muestra un log del hito.
     * @param hito Datos del hito.
     */
    private static void logOk(HitoExpediente hito) {
    	
    	if (hito!= null && CONSULTA_TELEMATICA.isInfoEnabled()) {
    		
	        // Información del hito
	        StringBuffer hitoInfo = new StringBuffer().append("- HITO ESTADO: ").append(toString(hito));
	        
	        // Log del resultado de la acción
	        CONSULTA_TELEMATICA.info("Alta de hito actual:\n"  + hitoInfo.toString());
    	}
    }

    /**
     * Muestra un log de error.
     * @param hito Datos del hito.
     */
    private static void logError(HitoExpediente hito, Throwable e) {
    	
        // Información del hito
        StringBuffer hitoInfo = new StringBuffer().append("- HITO ESTADO: ").append(toString(hito));
        
        // Log del error
        CONSULTA_TELEMATICA.error("Error en la acción " + EstablecerHitoCierreExpAction.class.getName() + ":\n" + hitoInfo.toString(), e);
    }
    
    /**
     * Obtiene una cadena con la información del hito.
     * @param hito Información del hito.
     * @return Cadena con la información del hito.
     */
    private static String toString(HitoExpediente hito) {
    	if (hito != null) {
	    	return new StringBuffer()
	    		.append("guid=[").append(hito.getGuid()).append("]")
	    		.append(", numeroExpediente=[").append(hito.getNumeroExpediente())
	    		.append("]")
	    		.append(", codigo=[").append(hito.getCodigo()).append("]")
	    		.append(", fecha=[").append(hito.getFecha()).append("]")
	    		.append(", descripcion=[").append(hito.getDescripcion()).append("]")
	    		.append(", informacionAuxiliar=[")
	    		.append(hito.getInformacionAuxiliar()).append("]")
	    		.toString();
    	} else {
    		return null;
    	}
    }
    
    public void cerrarExpediente(String numeroExpediente, Entidad entidad)
			throws ConsultaExpedientesException {
		try {
			GestorConsulta.actualizarEstado(numeroExpediente, Expediente.COD_ESTADO_EXPEDIENTE_FINALIZADO, entidad.getIdentificador());
		} catch (ConsultaExcepcion e) {
			logger.error("Error cerrando expediente.", e);
			throw getConsultaExpedientesException(e);
		} catch (Exception e) {
			logger.error("Error cerrando expediente.", e);
			throw new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION, e);
		}
	}
    
    private ConsultaExpedientesException getConsultaExpedientesException(
			ConsultaExcepcion poException) {
		if (poException == null) {
			return new ConsultaExpedientesException(ConsultaExpedientesException.EXC_GENERIC_EXCEPCION);
		}
		StringBuffer cCodigo = new StringBuffer(ConstantesServicios.SERVICE_QUERY_EXPS_ERROR_PREFIX);
		cCodigo.append(String.valueOf(poException.getErrorCode()));
		return new ConsultaExpedientesException(Long.valueOf(cCodigo.toString()).longValue(), poException);

	}
}
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
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.publicador.service.ConsultaTelematicaService;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;

/**
 * Acción para dar de alta un hito del estado de un expediente 
 * en la Consulta Telemática.
 * 
 */
public class EstablecerHitoActualNombreAction extends SigemBaseAction {

	//========================================================================
	// Constantes de los nombres de los parámetros de la regla
	//========================================================================
	private static final String CODIGO 				= "CODIGO";
	private static final String DESCRIPCION 		= "DESCRIPCION";
	private static final String PASO_A_HISTORICO 	= "PASO_A_HISTORICO";
	private static final String ENVIAR_DOCUMENTOS 	= "ENVIAR_DOCUMENTOS";
	//========================================================================
	
	/** Logger de la clase. */
    private static final Logger logger = 
    	Logger.getLogger(EstablecerHitoActualNombreAction.class);

	/** Logger de la clase. */
    private static final Logger CONSULTA_TELEMATICA = 
    	Logger.getLogger("CONSULTA_TELEMATICA");

    
    /**
     * Constructor.
     * 
     */
    public EstablecerHitoActualNombreAction() {
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
    public boolean execute(RuleContext rctx, AttributeContext attContext) 
    		throws ActionException {
    	
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
		        	
	        	hito = establecerHitoActual(rctx, attContext);
	
		        // Comprobar si hay que enviar los documentos asociados al hito
		        if ("S".equalsIgnoreCase(attContext.getAttribute(ENVIAR_DOCUMENTOS))) {
		        	anexarFicheros(rctx, hito);
		        }
    		}
		    // Log del hito
		    logOk(hito);
        } catch (Throwable e) {
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
		
			hito.setDescripcion(substituteParams(attContext.getAttribute(DESCRIPCION), getMessageParams(cct, rctx, exp)));
		} catch (ISPACException e) {
			setInfo("Error al componer el mensaje del hito actual: " + e.toString());
        	logError(hito, e);
            throw new ActionException(e);
		}
		
		hito.setInformacionAuxiliar("");
		
		// Paso a histórico del hito anterior
        boolean pasoAHistorico = "S".equalsIgnoreCase(attContext.getAttribute(PASO_A_HISTORICO));

        // Establece el hito actual en Consulta Telemática
		ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
		consulta.establecerHitoActual(hito, new FicherosHito(), pasoAHistorico, getEntidad());
		
		// Devolver la información del hito creado
		return consulta.obtenerHitoEstado(rctx.getIdObjeto(), getEntidad());
    }
    
    private void anexarFicheros(RuleContext rctx, HitoExpediente hito) 
    		throws Exception {
    	
        ConsultaTelematicaService service = new ConsultaTelematicaService();
        FicherosHito ficheros = null;

        // Obtener el identificador del trámite
    	int idTramite = TypeConverter.parseInt((String) rctx.getProperties().get("id_tramite"), -1);
    	int idFase = TypeConverter.parseInt((String) rctx.getProperties().get("id_fase"), -1);

        try {

			// Llamada al API de Consulta Telemática
			ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();

	        // Obtener los ficheros asociados al hito en tramitación
	        if (idTramite > 0) {
		        ficheros = service.getFicherosTramite(hito.getGuid(), rctx.getIdObjeto(), idTramite);
	        } else if (idFase > 0) {
		        ficheros = service.getFicherosFase(hito.getGuid(), rctx.getIdObjeto(), idFase);
	        } else {
	        	logger.warn("No se ha encontrado ningún identificador de fase o trámite");
	        }

	        // Anexar los ficheros al hito actual en CT
	        if ((ficheros != null) && ficheros.count() > 0) {
	        	consulta.anexarFicherosHitoActual(ficheros, getEntidad());
	        }

        } catch (Exception e) {
        	logger.error("Error al anexar ficheros al hito: " + hito.getGuid(), e);
        	
        	try {
	        	// Eliminar los ficheros anexados
	    		service.deleteFicherosHito(ficheros);
        	} catch (Throwable t) {
        		logger.warn("No se han podido eliminar los ficheros en RDE", e);
        	}
        	
            throw e;
        }
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
        CONSULTA_TELEMATICA.error("Error en la acción " + EstablecerHitoActualNombreAction.class.getName() + ":\n" + hitoInfo.toString(), e);
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
    
    protected static Map<String, String> getMessageParams(ClientContext cct, RuleContext rctx, IItem exp) throws ISPACException {
    	
    	Map<String, String> parameters = new HashMap<String, String>();
  
    	if (exp != null) {
    		String numexp = exp.getString("NUMEXP");
    		// Información del EXPEDIENTE
	    	parameters.put("NUMEXP", numexp); // Número de expediente
	    	parameters.put("CODPROCEDIMIENTO", exp.getString("CODPROCEDIMIENTO")); // Código del procedimiento
			parameters.put("NOMBREPROCEDIMIENTO", exp.getString("NOMBREPROCEDIMIENTO")); // Nombre del procedimiento
			parameters.put("NREG", exp.getString("NREG")); // Número de registro del expediente
			parameters.put("FREG", exp.getString("FREG")); // Fecha de registro del expediente
			parameters.put("ASUNTO", exp.getString("ASUNTO")); // Asunto del expediente
			parameters.put("FAPERTURA", exp.getString("FAPERTURA")); // Fecha de apertura del expediente

			// Información del DESTINATARIO
			parameters.put("NDOC_DEST", exp.getString("NIFCIFTITULAR")); // NIF/CIF del destinatario
			parameters.put("NOMBRE_DEST", exp.getString("IDENTIDADTITULAR")); // Identificación del destinatario
			parameters.put("DOMICILIO_DEST", exp.getString("DOMICILIO")); // Domicilio del destinatario
			parameters.put("DIRECCIONTELEMATICA_DEST", exp.getString("DIRECCIONTELEMATICA")); // DEU del destinatario
			
			int idTramite = TypeConverter.parseInt((String) rctx.getProperties().get("idtramite"), -1);
	    	int idFase = TypeConverter.parseInt((String) rctx.getProperties().get("idfase"), -1);
	    	
	    	String nombreFase = "";
	    	if(idFase>0){
	    		nombreFase = FasesUtil.getNombreFase(cct, idFase);
	    	}
	    	else{
	    		nombreFase = FasesUtil.getNombreFaseActiva(cct, numexp);				
	    	}
	    	parameters.put("NOMBREFASE", nombreFase);
	    	
	    	String nombreTramite = "";
	    	if(idTramite > 0){
	    		IItem tramite = TramitesUtil.getPTramiteById(cct, idTramite);
	    		if(tramite != null) nombreTramite = tramite.getString("NOMBRE");
	    		
	    	}
	    	parameters.put("NOMBRETRAMITE", nombreTramite);
    	}
		
		return parameters;
    }
}
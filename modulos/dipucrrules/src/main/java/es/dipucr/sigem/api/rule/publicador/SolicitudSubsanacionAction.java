package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.Properties;
import ieci.tdw.ispac.api.item.Property;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacpublicador.business.attribute.AttributeContext;
import ieci.tdw.ispac.ispacpublicador.business.context.RuleContext;
import ieci.tdw.ispac.ispacpublicador.business.exceptions.ActionException;
import ieci.tecdoc.sgm.base.guid.Guid;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.Pago;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.core.services.consulta.SolicitudAportacionDocumentacion;
import ieci.tecdoc.sgm.core.services.consulta.Subsanacion;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.publicador.vo.TasaVO;

/**
 * <p>
 * Acción para crear una solicitud de subsanación en la Consulta Telemática.
 * </p>
 * 
 * <p>
 * Se deben especificar los siguientes atributos en el XML de configuración de la regla
 * de publicación:
 * <ul>
 * <li>MENSAJE_SUBSANACION: Mensaje de subsanación que aparecerá en la aplicación Consulta de Expedientes.</li>
 * <li>MENSAJE_PAGO: Mensaje de pago que aparecerá en la aplicación Consulta de Expedientes.</li>
 * <li>TASA_[id]: Información de la tasa de pago.</li>
 * </ul>
 * </p>
 * <p>
 * Este es un ejemplo del XML de configuración de la regla de publicación: 
 * <pre>
 * <?xml version='1.0' encoding='ISO-8859-1'?>
 * <attributes>
 * 	<attribute name='TASA_1'>
 * 		<tax>
 * 			<name>Resguardo del pago de tasa</name>
 * 			<labels>
 * 				<label locale='es'>Resguardo del pago de tasa</label>
 * 				<label locale='eu'>Tasaren ordainagiria</label>
 * 				<label locale='ca'>Protegeixo del pagament de taxa</label>
 * 				<label locale='gl'>Resgardo do pago de taxa</label>
 * 			</labels>
 * 			<import>1000</import>
 * 			<sender_entity_id>000000</sender_entity_id>
 * 			<self_settlement_id>100</self_settlement_id>
 * 		</tax>
 * 	</attribute>
 * 	<attribute name='MENSAJE_SUBSANACION'>
 * 		<labels>
 * 			<label locale='es'>Se le ha notificado la necesidad de realizar una subsanación de la documentación aportada al expediente ${NUMEXP} del procedimiento ${NOMBREPROCEDIMIENTO}.</label>
 * 			<label locale='eu'>${NOMBREPROCEDIMIENTO} prozeduraren ${NUMEXP} espedientean aurkeztutako dokumentazioa zuzentzeko beharra dagoela jakinarazi zaizu.</label>
 * 			<label locale='ca'>Se us ha notificat la necessitat d\u0027esmenar la documentacio aportada a l\u0027expedient ${NUMEXP} del procediment ${NOMBREPROCEDIMIENTO}.</label>
 * 			<label locale='gl'>Notificouselle a necesidade de realizar unha reparación da documentación achegada ao expediente ${NUMEXP} do procedemento ${NOMBREPROCEDIMIENTO}.</label>
 * 		</labels>
 * 	</attribute>
 * 	<attribute name='MENSAJE_PAGO'>
 * 		<labels>
 * 			<label locale='es'>Durante la tramitación de su expediente ${NUMEXP} de ${NOMBREPROCEDIMIENTO} se le comunica que es necesario que acredite el pago de la tasa de ${NOMBRE_PAGO} por un valor de ${IMPORTE_PAGO} euros.</label>
 * 			<label locale='eu'>${NOMBREPROCEDIMIENTO}(e)ko ${NUMEXP} espedientea bideratzen ari dela, jakinarazten zaizu ${NOMBRE_PAGO}ren tasa (${IMPORTE_PAGO} eurokoa) ordaindu izana ziurtatu behar duzula.</label>
 * 			<label locale='ca'>Durant la tramitacio del vostre expedient ${NUMEXP} de ${NOMBREPROCEDIMIENTO} se us comunica que acrediteu el pagament de la taxa de ${NOMBRE_PAGO} per un valor de ${IMPORTE_PAGO} euros.</label>
 * 			<label locale='gl'>Durante a tramitación do seu expediente ${NUMEXP} de ${NOMBREPROCEDIMIENTO} comunicaráselle que &eacute; necesario que acredite o pagamento da taxa de ${NOMBRE_PAGO} por un valor de ${IMPORTE_PAGO} euros.</label>
 * 		</labels>
 * 	</attribute>
 * </attributes>
 * </pre>
 * </p>
 * 
 * <p>
 * A la hora de componer los textos, se pueden usar los siguientes parámetros:
 * <ul>
 * <li>NUMEXP: Número de expediente.</li>
 * <li>CODPROCEDIMIENTO: Código del procedimiento.</li>
 * <li>NOMBREPROCEDIMIENTO: Nombre del procedimiento.</li>
 * <li>NREG: Número de registro del expediente.</li>
 * <li>FREG: Fecha de registro del expediente.</li>
 * <li>ASUNTO: Asunto del expediente.</li>
 * <li>FAPERTURA: Fecha de apertura del expediente.</li>
 * <li>NOMBRE_DOC: Nombre del tipo de documento.</li>
 * <li>NDOC_DEST: NIF/CIF del destinatario.</li>
 * <li>NOMBRE_DEST: Identificación del destinatario.</li>
 * <li>DOMICILIO_DEST: Domicilio del destinatario.</li>
 * <li>DIRECCIONTELEMATICA_DEST: Dirección telemática del destinatario.</li>
 * <li>NOMBRE_PAGO: Nombre del pago.</li>
 * <li>IMPORTE_PAGO: Importe del pago.</li>
 * </ul>
 * 
 * </p>
 * 
 */
@SuppressWarnings("unchecked")
public class SolicitudSubsanacionAction extends SigemBaseAction {

	/** Logger de la clase. */
    private static final Logger logger = 
    	Logger.getLogger(SolicitudSubsanacionAction.class);

	/** Logger de la clase. */
    private static final Logger CONSULTA_TELEMATICA = 
    	Logger.getLogger("CONSULTA_TELEMATICA");

    public static final String TIMESTAMPFORMAT_INV = "yyyy-MM-dd HH:mm:ss";
    
    /** Propiedades de la entidad DOCUMENTO. */
    private static final Properties DOC_PROPS = new Properties();
    static {
		int ordinal = 0;
		DOC_PROPS.add( new Property(ordinal++, "ID", Types.VARCHAR));
		DOC_PROPS.add( new Property(ordinal++, "DOCUMENTO", Types.VARCHAR));
		DOC_PROPS.add( new Property(ordinal++, "PENDIENTE", Types.VARCHAR));
    }

    /** Contexto del cliente. */
    private ClientContext clientContext = null;

	/** Lista de solicitudes de subsanación. */
	private List<Subsanacion> solicitudesSubsanacion = new ArrayList<Subsanacion>();

    
    /**
     * Constructor.
     * 
     */
    public SolicitudSubsanacionAction() {
    	super();
    	clientContext = new ClientContext();
    	clientContext.setAPI(new InvesflowAPI(clientContext));
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

        try {
        	// Obtener las solicitudes a partir de los documentos requeridos
	        getSolicitudes(rctx, attContext);
        	
        	// Alta de las solicitudes de subsanación
        	altaSolicitudesSubsanacion();
        	
        } catch (ActionException e) {
        	setInfo("Error en el alta de solicitud de subsanación para el expediente "
        			+ rctx.getIdObjeto() + ": " + e.toString());
            throw e;
        } catch (Throwable e) {
        	setInfo("Error en el alta de solicitud de subsanación para el expediente "
        			+ rctx.getIdObjeto() + ": " + e.toString());
            throw new ActionException(e);
        }
        
        return true;
    }
    
    private IItem getExpediente(String numExp) throws ISPACException {

		// API de entidades
		IEntitiesAPI entitiesAPI = clientContext.getAPI().getEntitiesAPI();

		// Obtener la información del expediente
		IItem exp = entitiesAPI.getExpedient(numExp);
		if (exp == null) {
			throw new ActionException("No se ha encontrado el expediente: "
					+ numExp);
		}
		
		return exp;
    }
    
    private HitoExpediente getHitoExpediente(String numExp) 
    		throws SigemException, ActionException {

    	// API de acceso a Consulta Telemática
		ServicioConsultaExpedientes consulta = 
			LocalizadorServicios.getServicioConsultaExpedientes();

		// Información del hito actual del expediente en CT
        HitoExpediente hito = consulta.obtenerHitoEstado(numExp, getEntidad());
		if (hito == null) {
			throw new ActionException(
					"No existe hito actual en Consulta Telemática para el expediente " 
					+ numExp);
		}
		
		return hito;
    }
    
    private void getSolicitudes(RuleContext rctx, AttributeContext attContext)
    		throws SigemException, ISPACException {
    	
    	// Información del expediente
    	IItem exp = getExpediente(rctx.getIdObjeto());

		// Información del hito actual del expediente en CT
        HitoExpediente hito = getHitoExpediente(rctx.getIdObjeto());

    	// Componer las solicitudes
        List<Subsanacion> docNames = new ArrayList<Subsanacion>();
        String genericDocId = null;

        solicitudesSubsanacion.add(createSubsanacion(rctx, attContext, genericDocId, docNames, hito.getGuid(), getMessageParams(exp, null, null)));
    }

    private Subsanacion createSubsanacion(RuleContext rctx, AttributeContext attContext, String genericDocId, List<Subsanacion> docNames, String idHito, Map<String, String> params) throws ISPACException {
    	
    	Subsanacion subsanacion = new Subsanacion();

    	subsanacion.setIdentificador(new Guid().toString());
    	subsanacion.setIdDocumento(genericDocId);
    	
    	// Nombre del tipo de documento
    	if (!CollectionUtils.isEmpty(docNames)) {
    		params.put("NOMBRE_DOC", ArrayUtils.join(docNames.toArray(new String[docNames.size()]), ", "));
    	} else {
    		params.put("NOMBRE_DOC", "");
    	}
    	
    	// Mensaje para el ciudadano
    	String message = (String) attContext.getProperties().get("MENSAJE_SUBSANACION");
    	if (StringUtils.isNotBlank(message)) {
    		subsanacion.setMensajeParaElCiudadano(substituteParams(message, params));
    	}
    	
    	subsanacion.setFecha(TypeConverter.toString(new Date(), TIMESTAMPFORMAT_INV));
    	subsanacion.setNumeroExpediente(rctx.getIdObjeto());
    	subsanacion.setIdentificadorHito(idHito);

    	return subsanacion;
    }

    private void altaSolicitudesSubsanacion() throws SigemException {
    	
    	if (!CollectionUtils.isEmpty(solicitudesSubsanacion)) {

        	// API de acceso a Consulta Telemática
    		ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();

    		Subsanacion subsanacion;
    		for (int i = 0; i < solicitudesSubsanacion.size(); i++) {
    			subsanacion = solicitudesSubsanacion.get(i);
    			
    	    	try {
    				// Alta de la solicitud de subsanación
    				consulta.altaSolicitudSubsanacion(subsanacion, getEntidad());

    		        // Log de la subsanación
    		        logOk(subsanacion);
    		        
    	        } catch (Exception e) {
    	        	logError(subsanacion, e);
    	        }
    		}
    	}
    }
    
    /**
     * Compone los parámetros para los mensajes de aviso.
     * @param exp Información del expediente.
     * @param doc Información del documento.
     * @param tasa Información de la tasa de pago.
     * @return Parámetros para los mensajes de aviso.
     * @throws ISPACException si ocurre algún error.
     */
    protected static Map<String, String> getMessageParams(IItem exp, IItem doc, TasaVO tasa) throws ISPACException {
    	
    	Map<String, String> parameters = new HashMap<String, String>();
    	
    	String nombrePago = null;
    	String importePago = null;
    	
    	if (tasa != null) {
    		nombrePago = tasa.getEtiquetas();
    		importePago = tasa.getImporte();
    	}

    	if (exp != null) {
    		
    		// Información del EXPEDIENTE
	    	parameters.put("NUMEXP", exp.getString("NUMEXP")); // Número de expediente
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
    	}
				
		// Información del DOCUMENTO
		if (doc != null) {
			parameters.put("NOMBRE_DOC", doc.getString("DOCUMENTO")); // Nombre del tipo de documento
		}

    	// Información del pago
		parameters.put("NOMBRE_PAGO", StringUtils.nullToEmpty(nombrePago)); // Nombre del pago
		parameters.put("IMPORTE_PAGO", StringUtils.nullToEmpty(importePago)); // Importe del pago
		
		return parameters;
    }

    /**
     * Muestra un log terminación correcta.
     * @param solicitud Información de la solicitud.
     */
    private static void logOk(SolicitudAportacionDocumentacion solicitud) {
    	
    	if (CONSULTA_TELEMATICA.isInfoEnabled()) {
    		
    		if (solicitud instanceof Subsanacion) {

    			// Información de la subsanación
		        StringBuffer info = new StringBuffer()
		        	.append("- SUBSANACIÓN: ")
		        	.append(toString(solicitud));
		        
		        // Log del resultado de la acción
		        CONSULTA_TELEMATICA.info("Solicitud de Subsanación realizada:\n" 
		        		+ info.toString());

    		} else if (solicitud instanceof Pago) {

    			// Información del pago
		        StringBuffer info = new StringBuffer()
		        	.append("- PAGO: ")
		        	.append(toString(solicitud));
		        
		        // Log del resultado de la acción
		        CONSULTA_TELEMATICA.info("Solicitud de Pago realizada:\n" 
		        		+ info.toString());

    		}
    	}
    }

    /**
     * Muestra un log de error.
     * @param solicitud Información de la solicitud.
     * @param e Excepción capturada.
     */
    private static void logError(SolicitudAportacionDocumentacion solicitud, 
    		Exception e) {
    	
    	if (solicitud instanceof Subsanacion) {

    		// Información de la subsanación
	        StringBuffer info = new StringBuffer()
	        	.append("- SUBSANACIÓN: ")
	        	.append(toString(solicitud));
	        
	        // Log del error
	        CONSULTA_TELEMATICA.error("Error en la Solicitud de Subsanación:\n" 
	        		+ info.toString(), e);
	        
    	} else if (solicitud instanceof Pago) {

    		// Información del pago
	        StringBuffer info = new StringBuffer()
	        	.append("- PAGO: ")
	        	.append(toString(solicitud));
	        
	        // Log del error
	        CONSULTA_TELEMATICA.error("Error en la Solicitud de Pago:\n" 
	        		+ info.toString(), e);
    	}
    }

    private static String toString(SolicitudAportacionDocumentacion solicitud) {
    	if (solicitud != null) {
    		StringBuffer info = new StringBuffer()
	    		.append("id=[")
	    		.append(solicitud.getIdentificador()).append("]")
	    		.append(", documentoId=[")
	    		.append(solicitud.getIdDocumento()).append("]")
	    		.append(", mensaje=[")
	    		.append(solicitud.getMensajeParaElCiudadano()).append("]")
	    		.append(", hitoId=[")
	    		.append(solicitud.getIdentificadorHito()).append("]")
	    		.append(", fechaSubsanacion=[")
	    		.append(solicitud.getFecha()).append("]")
	    		.append(", expediente=[")
	    		.append(solicitud.getNumeroExpediente()).append("]");
    		
    		if (solicitud instanceof Pago) {
    		    info.append(", entidadEmisoraId=[")
		    		.append(((Pago) solicitud).getEntidadEmisoraId()).append("]")
		    		.append(", autoliquidacionId=[")
		    		.append(((Pago) solicitud).getAutoliquidacionId()).append("]")
		    		.append(", importe=[")
		    		.append(((Pago) solicitud).getImporte()).append("]");
    		}
    		
    		return info.toString();
    	} else {
    		return null;
    	}
    }

}
package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacpublicador.business.attribute.AttributeContext;
import ieci.tdw.ispac.ispacpublicador.business.context.RuleContext;
import ieci.tdw.ispac.ispacpublicador.business.exceptions.ActionException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.ConsultaExpedientesException;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.Interesado;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.publicador.service.ConsultaTelematicaService;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;
import es.dipucr.sigem.api.rule.publicador.vo.InteresadoVO;

/**
 * Acción para dar de alta un expediente en la Consulta Telemá3tica.
 * 
 */
public class DipucrAltaExpedienteInterReprePublicadorAction extends SigemBaseAction {

    private static final Logger logger = Logger.getLogger(DipucrAltaExpedienteInterReprePublicadorAction.class);

    private static final Logger CONSULTA_TELEMATICA = Logger.getLogger("CONSULTA_TELEMATICA");

    public DipucrAltaExpedienteInterReprePublicadorAction() {
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

        ExpedienteVO expediente = null;
        InteresadoVO interesado = null;
        
        try {
	        
        	// Número de expediente
	        String numExp = rctx.getIdObjeto();
        
	        // Servicio de acceso a la información de la Consulta Telemática
	        ConsultaTelematicaService service = new ConsultaTelematicaService();
	        
	        // Información del expediente
	        expediente = service.getExpediente(numExp);
	        if (expediente != null) {
	        	expediente.setCaportacion(attContext.getAttribute("APORTACION"));
	        	expediente.setCcodpres(attContext.getAttribute("CODPRES"));
	        	expediente.setCinfoaux("");
	        } else {
	        	throw new ActionException("No se ha encontrado el expediente: " + numExp);
	        }
	        	
        	// Información del interesado principal del expediente
        	interesado = service.getInteresadoExpediente(numExp);
	        
	        // Creación del expediente en la Consulta Telemática
	        creaExpediente(expediente, interesado);

	        // Log del expediente e interesados
	        logOk(expediente, interesado);
	        
        } catch (ActionException e) {
        	setInfo("Error en el alta de expediente: " + e.toString());
        	logError(expediente, interesado, e);
        	throw e;
        } catch (Throwable e) {
        	setInfo("Error en el alta de expediente: " + e.toString());
        	logError(expediente, interesado, e);
            throw new ActionException(e);
        }
        
        return true;
    }

    /**
     * Muestra un log del expediente e interesado principal.
     * @param expediente Datos del expediente.
     * @param interesado Datos del interesado principal.
     */
    private static void logOk(ExpedienteVO expediente, 
    		InteresadoVO interesado) {
    	
    	if (CONSULTA_TELEMATICA.isInfoEnabled()) {
    		
	        // Información completa del expediente
	        StringBuffer expInfo = new StringBuffer();
	    
	        // Añadir información del expediente
	        expInfo.append("- EXPEDIENTE:\n");
	        expInfo.append("\t").append(expediente).append("\n");
	        
	        // Añadir información de los interesados
	        expInfo.append("- INTERESADO:\n");
        	expInfo.append("\t").append(interesado);
	        
	        // Log del resultado de la acción
	        CONSULTA_TELEMATICA.info("Alta de expediente:\n" 
	        		+ expInfo.toString());
    	}
    }

    /**
     * Muestra un log de error del expediente e interesado principal.
     * @param expediente Datos del expediente.
     * @param interesado Datos del interesado principal.
     * @param e Excepción capturada.
     */
    private static void logError(ExpedienteVO expediente, 
    		InteresadoVO interesado, Throwable e) {
    	
        // Información completa del expediente
        StringBuffer expInfo = new StringBuffer();
    
        // Añadir información del expediente
        expInfo.append("- EXPEDIENTE:\n");
        expInfo.append("\t").append(expediente).append("\n");
        
        // Añadir información de los interesados
        expInfo.append("- INTERESADO:\n");
    	expInfo.append("\t").append(interesado);
        
        // Log del error
        CONSULTA_TELEMATICA.error("Error en la acción " 
        		+ DipucrAltaExpedienteInterReprePublicadorAction.class.getName() + ":\n" 
        		+ expInfo.toString(), e);
    }

    /**
     * Crea un nuevo expediente en la Consulta Telemática.
     * @param expediente Datos del expediente.
     * @param interesado Datos del interesado principal.
     * @throws Exception si ocurre algún error.
     */
    private void creaExpediente(ExpedienteVO expediente, InteresadoVO interesado) throws Exception {

    	Expediente ctexp = getCTExpediente(expediente);
    	Interesado ctinteresado = getCTInteresado(interesado);
		ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
        IItem responsable = null;

        ClientContext cct = new ClientContext();
		cct.setAPI(new InvesflowAPI(cct));
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		String numexp = expediente.getCnum();
		
		//Se eliminan los interesados por si alguna regla del procedimiento ha insertado los interesados antes de que saltara el publicador
		try{
			consulta.eliminarInteresadoExpediente(numexp, EntidadHelper.getEntidad());
		}
		catch(Exception e){}
		Expediente existeExp = null;
		try{
			existeExp = consulta.obtenerDetalle(numexp, getEntidad());
		}
		catch(Exception e){}
		if( existeExp == null)
			consulta.nuevoExpediente(ctexp, ctinteresado, getEntidad());
		//Se vuelven a eliminar para, ahora sí, insertar los interesados que nos interesan
        try{
			consulta.eliminarInteresadoExpediente(numexp, EntidadHelper.getEntidad());
		}
		catch(Exception e){}
		
		IItem expedienteTram = ExpedientesUtil.getExpediente(cct, numexp);
        if ((expedienteTram != null) && StringUtils.isNotBlank(expedienteTram.getString("NIFCIFTITULAR"))) {
        	
			Expediente expedientePublic = null;
			try{
				expedientePublic = consulta.obtenerDetalle(numexp, EntidadHelper.getEntidad());
			}
			catch(ConsultaExpedientesException e){}
			catch(Exception e){}
			
			if(expedientePublic != null){
				consulta.eliminarInteresadoExpediente(numexp, EntidadHelper.getEntidad());
				
				String nifTitular = expedienteTram.getString("NIFCIFTITULAR");
				if(StringUtils.isNotEmpty(nifTitular)){
			        consulta.nuevoInteresado(getCTInteresado(numexp, nifTitular, expedienteTram.getString("IDENTIDADTITULAR"), "S"), EntidadHelper.getEntidad());
							
					IItemCollection responsableCollection = entitiesAPI.getEntities("REC_OBLIGADO", numexp);
					Iterator<?> responsableIterator = responsableCollection.iterator();
					if(responsableIterator.hasNext()){
						responsable = (IItem) responsableIterator.next();
				        if ((responsable != null) && StringUtils.isNotBlank(responsable.getString("REPRES_NIF")) && !nifTitular.toUpperCase().trim().equals(responsable.getString("REPRES_NIF").toUpperCase().trim())) {			        	
					        consulta.nuevoInteresado(getCTInteresado(numexp, responsable.getString("REPRES_NIF"), responsable.getString("REPRES_NOMBRE"), "N"), EntidadHelper.getEntidad());
				        }
					}
				}
	        }
		}
    }
    
    private static Expediente getCTExpediente(ExpedienteVO exp) {
    	Expediente ctexp = null;
    	
    	if (exp != null) {
    		ctexp = new Expediente();
        	ctexp.setNumero(exp.getCnum());
    		ctexp.setProcedimiento(exp.getCproc());
    		ctexp.setFechaInicio(exp.getCfhinicio());
    		ctexp.setNumeroRegistro(exp.getCnumregini());
    		ctexp.setFechaRegistro(exp.getCfhregini());
    		ctexp.setInformacionAuxiliar(
    				StringUtils.nullToEmpty(exp.getCinfoaux()));
    		ctexp.setAportacion(exp.getCaportacion());
    		ctexp.setCodigoPresentacion(StringUtils.defaultString(
    				exp.getCcodpres(), "N"));
    		ctexp.setEstado(Expediente.COD_ESTADO_EXPEDIENTE_INICIADO);
    		//ctexp.setNotificacion();
    	}
    	
    	return ctexp;
    }
    
    private static Interesado getCTInteresado(InteresadoVO interesado) {
    	Interesado ctinteresado = null;
    	
		if (interesado != null) {
			ctinteresado = new Interesado();
			ctinteresado.setNumeroExpediente(interesado.getCnumexp());
			ctinteresado.setNIF(interesado.getCnif());
			ctinteresado.setNombre(interesado.getCnom());
			ctinteresado.setPrincipal("S");
			ctinteresado.setInformacionAuxiliar(
					StringUtils.nullToEmpty(interesado.getCinfoaux()));
			//ctinteresado.setExpedientes();
		}

		return ctinteresado;
    }
    
    private static Interesado getCTInteresado( String numexp, String nif, String nombre, String principal) throws ISPACException {
    	Interesado ctinteresado = null;
    	
		ctinteresado = new Interesado();
		ctinteresado.setNumeroExpediente(numexp);
		ctinteresado.setNIF(nif);
		ctinteresado.setNombre(nombre);
		ctinteresado.setPrincipal(principal);
		ctinteresado.setInformacionAuxiliar(StringUtils.nullToEmpty(""));

		return ctinteresado;
    }
}
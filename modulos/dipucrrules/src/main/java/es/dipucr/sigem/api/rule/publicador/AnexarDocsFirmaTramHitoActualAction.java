package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacpublicador.business.attribute.AttributeContext;
import ieci.tdw.ispac.ispacpublicador.business.context.RuleContext;
import ieci.tdw.ispac.ispacpublicador.business.exceptions.ActionException;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.FicheroHito;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.HitoExpediente;
import ieci.tecdoc.sgm.core.services.consulta.HitosExpediente;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.core.services.repositorio.ServicioRepositorioDocumentosTramitacion;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.publicador.service.ConsultaTelematicaService;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;

/**
 * Acción para anexar los documentos firmados del trámite que se está cerrando al hito actual (Trámite iniciado) de un expediente 
 * en la Consulta Telemática.
 * 
 */
public class AnexarDocsFirmaTramHitoActualAction extends SigemBaseAction {

	ClientContext context = null;
	/** Logger de la clase. */
    private static final Logger logger = Logger.getLogger(AnexarDocsFirmaTramHitoActualAction.class);

	/** Logger de la clase. */
    private static final Logger CONSULTA_TELEMATICA = Logger.getLogger("CONSULTA_TELEMATICA");

    
    /**
     * Constructor.
     * 
     */
    public AnexarDocsFirmaTramHitoActualAction() {
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
    @SuppressWarnings("unchecked")
	public boolean execute(RuleContext rctx, AttributeContext attContext) throws ActionException {
    	
        if (logger.isInfoEnabled()) {
            logger.info("Acción [" + this.getClass().getName() + "] en ejecución");
        }

        String numexp = (String) rctx.getProperties().get("idobjeto");
        int idTramite = TypeConverter.parseInt((String) rctx.getProperties().get("id_tramite"), -1);

        ConsultaTelematicaService service = new ConsultaTelematicaService();
        FicherosHito ficheros = null;
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
    		if( existeExp != null)	{
				ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
	
		        HitoExpediente hito = consulta.obtenerHitoEstado(rctx.getIdObjeto(), getEntidad());
		        if (hito == null) {
		        	throw new ActionException("No se ha encontrado el hito actual del expediente " + rctx.getIdObjeto());
		        }
		        
		        context = new ClientContext();
				context.setAPI(new InvesflowAPI(context));
				
				HitosExpediente hitos = consulta.obtenerHistoricoExpediente(numexp, getEntidad());
				FicherosHito ficherosExistentes  = new FicherosHito();
				
				for(Object oHito: hitos.getHitosExpediente())
					ficherosExistentes.getFicheros().addAll(consulta.obtenerFicherosHito(((HitoExpediente)oHito).getGuid(), getEntidad()).getFicheros());
		    	
				IItemCollection docs = DocumentosUtil.getDocumentos(context, numexp, " (ID_TRAMITE = " + idTramite + " AND ESTADOFIRMA IN ('02','03')) ", " FAPROBACION");
				ficheros =  getFicheros(hito.getGuid(), docs, ficherosExistentes);
				 
		        if ((ficheros != null) && ficheros.count() > 0) {
		        	consulta.anexarFicherosHitoActual(ficheros, getEntidad());
		        }
	        }

	        logOk(ficheros);

        } catch (ActionException e) {
        	setInfo(e.getLocalizedMessage());
        	logError(e);
        	
        	try {
	    		service.deleteFicherosHito(ficheros);
        	} catch (Throwable t) {
        		logger.warn("No se han podido eliminar los ficheros en RDE", e);
        	}
        	
            throw e;
        } catch (Throwable e) {
        	setInfo("Error al anexar ficheros al hito actual: " + e.toString());
        	logError(e);
        	
        	try {
	    		service.deleteFicherosHito(ficheros);
        	} catch (Throwable t) {
        		logger.warn("No se han podido eliminar los ficheros en RDE", e);
        	}
        	
            throw new ActionException(e);
        }
        
        return true;
    }

	/**
     * Muestra un log del resultado.
     * @param ficheros Ficheros anexados.
     */
    private static void logOk(FicherosHito ficheros) {
    	
    	if (CONSULTA_TELEMATICA.isInfoEnabled()) {
	        CONSULTA_TELEMATICA.info("Anexión de ficheros al hito actual:\n" + (ficheros == null? 0 : ficheros.count()));
    	}
    }

    /**
     * Muestra un log de error.
     * @param e Excepción provocada por el error.
     */
    private static void logError(Throwable e) {
        CONSULTA_TELEMATICA.error("Error en la acción " + AnexarDocsFirmaTramHitoActualAction.class.getName(), e);
    }
    
    protected FicherosHito getFicheros(String guidHito, IItemCollection docs, FicherosHito ficherosExistentes) throws ISPACException, SigemException {
    	
    	// Ficheros asociados al hito
        FicherosHito ficheros = new FicherosHito();
        
        // Documentos
        if ((docs != null) && docs.next()) {
        	
			// Llamada al API de RDE
			ServicioRepositorioDocumentosTramitacion rde = LocalizadorServicios.getServicioRepositorioDocumentosTramitacion();

			IItem doc;
			FicheroHito fichero;
			String guid;
			String guidRDE;
			
			do {
				doc = docs.value();
				String titulo = doc.getInt("ID") + " - " + doc.getString("NOMBRE") + " - " +doc.getString("DESCRIPCION");
				if(titulo.length()>128) titulo = titulo.substring(0,127);
				
				boolean existe = false;
				
				for(int i = 0; i< ficherosExistentes.getFicheros().size() && !existe; i++ ){
		    		FicheroHito ficheroExistente = (FicheroHito)ficherosExistentes.getFicheros().get(i);
		    		
		    		if(ficheroExistente.getTitulo().equals(titulo)){
		    			existe = true;
		    		}
				}
				if(!existe){
	    			// GUID del fichero en el tramitador
					guid = doc.getString("INFOPAG_RDE");
					if (StringUtils.isNotBlank(guid)) {
						
						// Almacenar el fichero en RDE
						guidRDE = rde.storeDocumentInfoPag(null, EntidadHelper.getEntidad(), guid, doc.getString("EXTENSION_RDE"));
		
						// Información del fichero
						fichero = new FicheroHito();
						fichero.setGuid(guidRDE);
						fichero.setGuidHito(guidHito);
						fichero.setTitulo(titulo);
						
						// Añadir el fichero a la lista
						ficheros.add(fichero);
					}
				}
			} while (docs.next());
        }
        
        return ficheros;
    }
}
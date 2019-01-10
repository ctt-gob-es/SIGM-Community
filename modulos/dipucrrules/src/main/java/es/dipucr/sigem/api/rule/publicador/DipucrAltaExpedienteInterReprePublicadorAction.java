package es.dipucr.sigem.api.rule.publicador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.ConsultaExpedientesException;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.Interesado;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;
import es.dipucr.sigem.api.rule.publicador.vo.InteresadoVO;

/**
 * Acción para dar de alta un expediente en la Consulta Telemá3tica.
 * 
 */
public class DipucrAltaExpedienteInterReprePublicadorAction extends DipucrAltaExpedientePublicadorAction {

    private static final Logger LOGGER = Logger.getLogger(DipucrAltaExpedienteInterReprePublicadorAction.class);

    public DipucrAltaExpedienteInterReprePublicadorAction() {
    	super();
    }
        

    /**
     * Crea un nuevo expediente en la Consulta Telemática.
     * @param expediente Datos del expediente.
     * @param interesado Datos del interesado principal.
     * @throws Exception si ocurre algún error.
     */
    public void creaExpediente(ExpedienteVO expediente, InteresadoVO interesado) throws Exception {

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
		catch(Exception e){
			LOGGER.info("No hace nada, solo captura la excepcion. " + e.getMessage(), e);
		}
		Expediente existeExp = null;
		try{
			existeExp = consulta.obtenerDetalle(numexp, getEntidad());
		}
		catch(Exception e){
			LOGGER.info("No hace nada, solo captura la excepcion. " + e.getMessage(), e);
		}
		if( existeExp == null)
			consulta.nuevoExpediente(ctexp, ctinteresado, getEntidad());
		//Se vuelven a eliminar para, ahora sí, insertar los interesados que nos interesan
        try{
			consulta.eliminarInteresadoExpediente(numexp, EntidadHelper.getEntidad());
		}
		catch(Exception e){
			LOGGER.info("No hace nada, solo captura la excepcion. " + e.getMessage(), e);
		}
		
		IItem expedienteTram = ExpedientesUtil.getExpediente(cct, numexp);
        if ((expedienteTram != null) && StringUtils.isNotBlank(expedienteTram.getString("NIFCIFTITULAR"))) {
        	
			Expediente expedientePublic = null;
			try{
				expedientePublic = consulta.obtenerDetalle(numexp, EntidadHelper.getEntidad());
			}
			catch(ConsultaExpedientesException e){
				LOGGER.info("No hace nada, solo captura la excepcion. " + e.getMessage(), e);
			}
			catch(Exception e){
				LOGGER.info("No hace nada, solo captura la excepcion. " + e.getMessage(), e);
			}
			
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
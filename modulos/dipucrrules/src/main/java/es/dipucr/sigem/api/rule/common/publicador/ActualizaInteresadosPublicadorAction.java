package es.dipucr.sigem.api.rule.common.publicador;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
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

/**
 * Acción para dar de alta un hito del estado de un expediente 
 * en la Consulta Telemática.
 * 
 */
public class ActualizaInteresadosPublicadorAction implements IRule {
	
	/** Logger de la clase. */
    private static final Logger LOGGER = Logger.getLogger(ActualizaInteresadosPublicadorAction.class);


	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
    	
        LOGGER.info("INICIO - " + this.getClass().getName());

        IItem responsable = null;
        String numexp = rulectx.getNumExp();
        try {	        
        
        	IClientContext cct = rulectx.getClientContext();
        	IInvesflowAPI invesflowAPI = cct.getAPI();
        	IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
        	
			IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
	        if ((expediente != null) && StringUtils.isNotBlank(expediente.getString("NIFCIFTITULAR"))) {
	        	
				ServicioConsultaExpedientes consulta = LocalizadorServicios.getServicioConsultaExpedientes();
				Expediente expedientePublic = null;
				try{
					expedientePublic = consulta.obtenerDetalle(numexp, EntidadHelper.getEntidad());
				} catch(ConsultaExpedientesException e){
					LOGGER.info("No se ha podido obtener el expediente", e);
				} catch(Exception e){
					LOGGER.info("No se ha podido obtener el expediente", e);
				}
				
				if(expedientePublic != null){
					consulta.eliminarInteresadoExpediente(numexp, EntidadHelper.getEntidad());
	
					String nifTitular = expediente.getString("NIFCIFTITULAR");
					if(StringUtils.isNotEmpty(nifTitular)){
				        consulta.nuevoInteresado(getCTInteresado(numexp, nifTitular, expediente.getString("IDENTIDADTITULAR"), "S"), EntidadHelper.getEntidad());
								
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
        } catch (ISPACException e) {
        	LOGGER.error("Error en el al insertar el responsable como interesado en el publicador del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Error en el al insertar el responsable como interesado en el publicador del expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
        	LOGGER.error("Error en el al insertar el responsable como interesado en el publicador del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Error en el al insertar el responsable como interesado en el publicador del expediente: " + numexp + ". " + e.getMessage(), e);
        }
        
        LOGGER.info("FIN - " + this.getClass().getName());
        return true;
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

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// empty
	}
}
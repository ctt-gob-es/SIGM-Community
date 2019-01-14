package ieci.tdw.ispac.ispacmgr.scheduler;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.session.ISPACScheduler;
import ieci.tecdoc.sgm.core.admin.web.AdministracionHelper;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.extra.ExtraConfiguration;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.ieci.tecdoc.isicres.admin.estructura.dao.Volume;
import es.ieci.tecdoc.isicres.admin.estructura.dao.VolumeList;
import es.ieci.tecdoc.isicres.admin.estructura.dao.VolumeLists;
import es.ieci.tecdoc.isicres.admin.estructura.dao.impl.VolumesImpl;

public class AvisoLlenadoRepositoriosTask extends ISPACScheduler {
	
	public final static String LIST_TRAMSEGURA_INITIAL_NAME = "ListaTramitadorSegura";
	
	/** 
	 * Logger de la clase. 
	 */
    private static Logger LOGGER = Logger.getLogger(AvisoLlenadoRepositoriosTask.class);

    
    /**
     * Ejecuta la tarea del scheduler.
     */
	public void run() {

		ClientContext cct = new ClientContext();
		IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
		cct.setAPI(invesFlowAPI);
		
		try{
			LOGGER.warn("INICIO Ejecución AvisoLlenadoRepositoriosTask");
			LOGGER.warn("*********************************************");
	        
	        // Obtener la lista de entidades
	        @SuppressWarnings("rawtypes")
			List listEntidades = AdministracionHelper.obtenerListaEntidades();
	        LOGGER.info("Se han encontrado " + (listEntidades != null ? listEntidades.size() : 0) + " entidades");
	
	        if (!CollectionUtils.isEmpty(listEntidades)) {
	        	for (int i = 0; i < listEntidades.size(); i++) {
	        		
	        		// Información de la entidad
	        		Entidad entidad = (Entidad) listEntidades.get(i);
	        		if (entidad != null) {
	
	        			LOGGER.warn("---------------------------------------------");
	                    LOGGER.warn(entidad.getIdentificador() + " - " + entidad.getNombre());
	            		LOGGER.warn("---------------------------------------------");
	
	        			// Establecer la entidad en el thread local
	    				EntidadHelper.setEntidad(entidad);
	        			
	    				// Comprobar el estado de notificación
	    				try{
	    					execute(entidad);
	    				}
	    				catch(Exception ex){
	    					LOGGER.error("Error al ejecutar la entidad " + entidad.getIdentificador() + " - " + entidad.getNombre());
	    		    		try{
	    		    			enviarCorreoError(cct, ex);
	    		    		}
	    		    		catch(Exception ex2){
		    		    		LOGGER.error("NO SE HA PODIDO ENVIAR EL CORREO DE ERROR");
	    		    		}
	    		    	}
	        		}
	        	}
	        }
	        
			LOGGER.warn("Fin Ejecución AvisoLlenadoRepositoriosTask");
			LOGGER.warn("*********************************************");

		}
    	catch(Exception ex){
    		LOGGER.error("Error el recorrer las entidades y comprobar el llenado repositorios", ex);
    		enviarCorreoError(cct, ex);
    	}
	}

	public void execute(Entidad entidad) throws ISPACRuleException {
		
		ClientContext cct = new ClientContext();
		IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
		cct.setAPI(invesFlowAPI);
		
		try{
			String idEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
			
			//Porcentaje máximo de los volúmenes
			double MAX_PERCENT = 0.0;
			ExtraConfiguration extraConf = ExtraConfiguration.getInstance(cct);
			try{
				String sPercent = extraConf.get(ExtraConfiguration.ALERTAS_VOLUMENES.OCCUPANCY_PERCENT);
				MAX_PERCENT = Double.valueOf(sPercent);
			}
			catch(Exception ex){
				throw new ISPACException("La variable '" + ExtraConfiguration.ALERTAS_VOLUMENES.OCCUPANCY_PERCENT
						+ "' no existe en el fichero extra.properties de la entidad o no tiene un formato númerico");
			}
			
			//Recuperamos las listas de volúmenes
			//* ListaRegistro01, ListaTramitador01, ListaArchivo01, ListaTramitadorSegura01
			VolumeLists volumneListsAPI = new VolumeLists();
			volumneListsAPI.load(idEntidad);
			
			for (int i = 0; i < volumneListsAPI.count(); i++){
				VolumeList volList = volumneListsAPI.getVolumeList(i);
				
				if (!volList.getName().contains(LIST_TRAMSEGURA_INITIAL_NAME)){
					// Obtener los volúmenes de la lista de volúmenes
					VolumesImpl volumesAPI = new VolumesImpl();
					volumesAPI.loadByVolumeList(volList.getId(), "");
					
					//Recuperamos únicamente el último volumen de cada lista, que es el volumen activo
					Volume volume = volumesAPI.getVolume(volumesAPI.count() - 1);

					double dActSize = Double.valueOf(volume.getActSize());
					double dMaxSize = Double.valueOf(volume.getMaxSize());
					double dPercent = dActSize/dMaxSize * 100; 
					if (dPercent > MAX_PERCENT){
						enviarCorreoVolumenLleno(cct, entidad, volume, dPercent);
					}
					
					DecimalFormat df = new DecimalFormat("0.00");
					LOGGER.warn("Nombre volumen: " + volume.getName());
					LOGGER.warn("Tamaño actual: " + volume.getActSize());
					LOGGER.warn("Tamaño máximo: " + volume.getMaxSize());
					LOGGER.warn("Porcentaje ocupación: " + df.format(dPercent) + "%");
				}
			}
		}
		catch (Exception e) {
			String error = "Error alertas llenado repositorios entidad " + entidad.getIdentificador() + ": " + e.getMessage();
			LOGGER.error(error, e);
			throw new ISPACRuleException(error, e);
		}
	}
	
	/**
	 * 
	 * @param cct
	 * @param entidad
	 * @param volume
	 * @param dPercet
	 */
	private void enviarCorreoVolumenLleno(IClientContext cct, Entidad entidad, Volume volume, double dPercent) {

		try{
			ExtraConfiguration extraConf = ExtraConfiguration.getInstance(cct);
			
			String strTo = extraConf.get(ExtraConfiguration.ALERTAS_VOLUMENES.MAIL.TO);
			String strAsunto = extraConf.get(ExtraConfiguration.ALERTAS_VOLUMENES.MAIL.SUBJECT);

			String strContenido = extraConf.get(ExtraConfiguration.ALERTAS_VOLUMENES.MAIL.CONTENT);
			HashMap<String, String> variables = new HashMap<String, String>();
			variables.put("ENTIDAD", entidad.getIdentificador() + " - " + entidad.getNombre());
			variables.put("VOLUMEN", volume.getName());
			DecimalFormat df = new DecimalFormat("0.00");
			variables.put("PORCENTAJE", df.format(dPercent));
			strContenido = StringUtils.replaceVariables(strContenido, variables);

			MailUtil.enviarCorreoInfoTask(cct, strTo, strAsunto, strContenido);
		}
		catch(Exception ex){
			LOGGER.error("AvisoLlenadoRepositorios: Se produjo una excepción al enviar el correo de aviso de llenado", ex);
		}
	}
	
	/**
	 * Enviar un correo de error al administrador
	 * @param ctx
	 * @param ex
	 * @throws ISPACException 
	 */
	private void enviarCorreoError(IClientContext cct, Exception ex) {

		try{
			ExtraConfiguration extraConf = ExtraConfiguration.getInstance(cct);
			
			String strTo = extraConf.get(ExtraConfiguration.ALERTAS_VOLUMENES.MAIL_ERROR.TO);
			String strAsunto = extraConf.get(ExtraConfiguration.ALERTAS_VOLUMENES.MAIL_ERROR.SUBJECT);
			
			String strContenido = extraConf.get(ExtraConfiguration.ALERTAS_VOLUMENES.MAIL_ERROR.CONTENT);
			HashMap<String, String> variables = new HashMap<String, String>();
			variables.put("EXCEPCION", ex.getMessage());
			strContenido = StringUtils.replaceVariables(strContenido, variables);

			MailUtil.enviarCorreoInfoTask(cct, strTo, strAsunto, strContenido);
		}
		catch(Exception ex1){
			LOGGER.error("AvisoLlenadoRepositorios: Se produjo una excepción al enviar el correo de error", ex1);
		}
	}
}
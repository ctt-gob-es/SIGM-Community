package es.dipucr.licitacionelectronica.ws;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.dipucr.contratacion.objeto.sw.EspacioVirtualLicitacionBean;
import es.dipucr.contratacion.utils.EspacioVirtualLicitacionUtilsSW;

public class LicitacionElectronicaWS {

	private static final Logger LOGGER = LoggerFactory.getLogger(LicitacionElectronicaWS.class);
	
	public EspacioVirtualLicitacionBean getEspacioVirtualLicitacionBean(String entidadId, String numexp){
	
		EspacioVirtualLicitacionBean anuncioLicitacion= null;
		try{
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("LicitacionElectronicaWS - getEspacioVirtualLicitacionBean(entidadId: [" + entidadId + "], numexp:[" + numexp + "]) - Inicio");
			}
			
			// Establecer la entidad para la multientidad
			setOrganizationUserInfo(entidadId);
		
			// Crear el contexto de tramitación para la consulta
			IClientContext cct = createClientContext();
		
			anuncioLicitacion = EspacioVirtualLicitacionUtilsSW.getEspacioVirtualLicitacionBeanSW(cct, numexp, null);
		
		} catch (ISPACException e){
			LOGGER.error("ERROR al recuperar el espacio virtual de licitación para el expediente: " + numexp + ", de la entidad: " + entidadId + ". " + e.getMessage(), e);
		
		} catch (Exception e) {
			LOGGER.error("ERROR al recuperar el espacio virtual de licitación para el expediente: " + numexp + ", de la entidad: " + entidadId + ". " + e.getMessage(), e);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("LicitacionElectronicaWS - getEspacioVirtualLicitacionBean(entidadId: [" + entidadId + "], numexp:[" + numexp + "]) - Fin");
		}

		return anuncioLicitacion;
	}
	
	/**
	 * Establecer la entidad para la multientidad.
	 *
	 * @param idEntidad Identificador de la entidad
	 */
	protected void setOrganizationUserInfo(String idEntidad) {

		OrganizationUserInfo info = new OrganizationUserInfo();
		info.setOrganizationId(idEntidad);
		info.getSpacPoolName();

		OrganizationUser.setOrganizationUserInfo(info);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Establecida la entidad en la sesion de tramitacion: " + idEntidad);
		}
	}
	
	/**
	 * Crear el contexto de tramitación.
	 *
	 * @return Contexto de tramitación.
	 * @throws Exception
	 */
	protected IClientContext createClientContext() throws Exception {

		IClientContext context = new ClientContext();
		InvesflowAPI invesflow = new InvesflowAPI((ClientContext) context);
		((ClientContext) context).setAPI(invesflow);
		context.setLocale(new Locale("es", "ES"));

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creado el contexto de tramitacion.");
		}

		return context;
	}
}

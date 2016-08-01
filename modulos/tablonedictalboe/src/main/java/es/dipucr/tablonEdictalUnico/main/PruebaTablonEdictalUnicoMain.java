package es.dipucr.tablonEdictalUnico.main;

import ieci.tdw.ispac.api.errors.ISPACRuleException;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import es.boe.www.ServicioNotificaciones.ListaAnuncios;
import es.boe.www.ServicioNotificaciones.ListaAvisos;
import es.boe.www.ServicioNotificaciones.ListaErrores;
import es.boe.www.ServicioNotificaciones.Respuesta;
import es.boe.www.ServicioNotificaciones.ServicioNotificacionesProxy;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.tablonEdictalUnico.commons.FuncionesComunes;
import es.dipucr.tablonEdictalUnico.commons.ServiciosWebTEUFunciones;


public class PruebaTablonEdictalUnicoMain {

	public static final Logger logger = Logger.getLogger(PruebaTablonEdictalUnicoMain.class);
	

	public static void main(String[] args) throws ISPACRuleException {
		
		try {
			
			String tablonEdictal_address = ServiciosWebTEUFunciones.getDireccionSW();
			ServicioNotificacionesProxy notificacion = new ServicioNotificacionesProxy(tablonEdictal_address);
		
			//https://unbeagleyyo.wordpress.com/2010/02/12/firmar-webservices-axis-con-wss4j-y-certificados-x509/
			String envio = FuncionesComunes.creacionMensaje(null);
						
			Respuesta respuesta = notificacion.envioAnuncios(envio);

			logger.warn("RESPUESTA");
			logger.warn("Id. envío. "+respuesta.getIdEnvio());
			logger.warn("Fecha. "+respuesta.getFecha());
			ListaAnuncios listaAnuncios = respuesta.getAnuncios();
			es.boe.www.ServicioNotificaciones.Anuncio[]  anuncios = listaAnuncios.getAnuncio();
			for(int i = 0; i<anuncios.length; i++){
				es.boe.www.ServicioNotificaciones.Anuncio anuncio = anuncios[i];
				logger.warn("-"+i+". "+anuncio.getCve());
				logger.warn("-"+i+". "+anuncio.getEstadoBoe());
				logger.warn("-"+i+". "+anuncio.getFechaPub());
				logger.warn("-"+i+". "+anuncio.getId());
				logger.warn("-"+i+". "+anuncio.getIdBoe());
				logger.warn("-"+i+". "+anuncio.getNbo());
				logger.warn("-"+i+". "+anuncio.getUrl());
				ListaAvisos[] listaAviso = anuncio.getAvisos();
				ListaErrores[] vlistaError = anuncio.getErrores();
				for (int j = 0; j < vlistaError.length; j++) {
					ListaErrores listaError = vlistaError[i];
					es.boe.www.ServicioNotificaciones.Error[] verror = listaError.getError();
					for (int k = 0; k < verror.length; k++) {
						es.boe.www.ServicioNotificaciones.Error error = verror[k];
						logger.warn("ERROR"+k+": "+error.getCodigo()+" - "+error.getDescripcion());
					}
				}
			}
			
			logger.warn("Resultado. "+respuesta.getResultado().getCodigo() +" - "+ respuesta.getResultado().getDescripcion());
			
		} catch (RemoteException e) {
			logger.error("Error. "+e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACRuleException e) {
			logger.error("Error. "+e.getMessage(), e);			
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}

	}
		
}

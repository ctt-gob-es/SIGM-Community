package es.dipucr.tablonEdictalUnico.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import es.boe.www.ServicioNotificaciones.Respuesta;
import es.boe.www.ServicioNotificaciones.ServicioNotificacionesProxy;
import es.dipucr.tablonEdictalUnico.commons.FuncionesComunes;
import es.dipucr.tablonEdictalUnico.commons.ServiciosWebTEUFunciones;
import es.dipucr.tablonEdictalUnico.objetos.RecaudacionFichero;
import es.dipucr.tablonEdictalUnico.xml.Envio;

public class EnvioAnuncioRecaudacionTablonEdictalBOERule implements IRule {

	public static final Logger logger = Logger
			.getLogger(EnvioAnuncioRecaudacionTablonEdictalBOERule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			
			OpenOfficeHelper ooHelper = OpenOfficeHelper.getInstance();

			String tablonEdictal_address = ServiciosWebTEUFunciones.getDireccionSW();
			ServicioNotificacionesProxy notificacion = new ServicioNotificacionesProxy(tablonEdictal_address);

			
			RecaudacionFichero recaudacion = new RecaudacionFichero();

			FuncionesComunes.obtenerRutaFicheroRecaudacion(rulectx, recaudacion);
			if (!recaudacion.getRuta().equals("")) {

				Envio envio = FuncionesComunes.construccionEnvio(rulectx, recaudacion);
				String sEnvio = FuncionesComunes.creacionMensaje(envio);

				Respuesta respuesta = notificacion.envioAnuncios(sEnvio);
				
				FuncionesComunes.anadirInformacionRespuesta(rulectx, respuesta);

				FuncionesComunes.crearDocInformacionEnvioTEU(ooHelper, rulectx, respuesta);

			}

		} catch (RemoteException e) {
			logger.error("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
		} catch (ISPACRuleException e) {
			logger.error("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar el anuncio en el numexp "+rulectx.getNumExp()+ "." + e.getMessage(), e);
		}

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

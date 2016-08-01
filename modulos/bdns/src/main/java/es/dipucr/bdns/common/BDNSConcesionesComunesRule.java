package es.dipucr.bdns.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Atributos;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmisionDatosGenericos;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitudes;
import es.dipucr.bdns.api.impl.BDNSAPI;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class BDNSConcesionesComunesRule {
	
	private static final Logger logger = Logger.getLogger(BDNSDipucrFuncionesComunes.class);

	public static Atributos obtenerArtibutos(IRuleContext rulectx) throws ISPACRuleException {
		Atributos atributos = null;
		
		
		String idPeticion = "";
		try {
			idPeticion = BDNSAPI.getIdPeticion((ClientContext) rulectx.getClientContext(), rulectx.getClientContext().getAPI().getEntitiesAPI());
		} catch (ISPACException e) {
			logger.error("Obtener el id de la petición. "+rulectx.getNumExp()+ " en el método BDNSAPI.getIdPeticion - "+e.getMessage(), e);
			throw new ISPACRuleException("Obtener el id de la petición. "+rulectx.getNumExp()+ " en el método BDNSAPI.getIdPeticion - "+e.getMessage(), e);
		}
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
		String timeStamp = sdf.format(cal.getTime());
		
		
		if(idPeticion!=null || timeStamp!=null){
			atributos = new Atributos();
		}
		if(idPeticion!=null){
			atributos.setIdPeticion(idPeticion);
		}
		
		atributos.setNumElementos(1);
		
		atributos.setCodigoCertificado(ServiciosWebConfiguration.BDNS_APP_CONCESPAGOPROY);

		if(timeStamp!=null){
			atributos.setTimeStamp(timeStamp);
		}
		return atributos;
	}

	public static Solicitudes obtenerSolicitudes() {
		Solicitudes solicitudes = null;		
		
		//solicitudes.setId(id);
		solicitudes.setSolicitudTransmision(obtenerSolicitudTransmision());
		
		return solicitudes;
	}

	private static SolicitudTransmision[] obtenerSolicitudTransmision() {
		SolicitudTransmision[] solicitud = null;
		
		SolicitudTransmision solTransmision = null;
		
		DatosEspecificos datosEspecificos = obtenerDatosEspecificos();
		SolicitudTransmisionDatosGenericos datosGenericos = obtenerDatosGenericos();
		if(datosEspecificos!=null || datosGenericos!=null){
			solTransmision = new SolicitudTransmision();
			solicitud = new SolicitudTransmision[1];
			
			solTransmision.setDatosEspecificos(datosEspecificos);		
			solTransmision.setDatosGenericos(datosGenericos);
			
			solicitud[0] = solTransmision;
		}
		
		
		return solicitud;
	}

	private static SolicitudTransmisionDatosGenericos obtenerDatosGenericos() {
		// TODO Auto-generated method stub
		return null;
	}

	private static DatosEspecificos obtenerDatosEspecificos() {
		// TODO Auto-generated method stub
		return null;
	}


}

























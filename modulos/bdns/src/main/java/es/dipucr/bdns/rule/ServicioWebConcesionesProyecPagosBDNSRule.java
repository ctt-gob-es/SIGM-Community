package es.dipucr.bdns.rule;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Atributos;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitudes;
import es.dipucr.bdns.common.BDNSConcesionesComunesRule;
import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyectoProxy;

public class ServicioWebConcesionesProyecPagosBDNSRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(ServicioWebConcesionesProyecPagosBDNSRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		String url = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_CONCESPAGOPROY);
		ConcesionPagoProyectoProxy concesion = new ConcesionPagoProyectoProxy(url);
		
		Peticion peticion=null;
		Atributos atributos=BDNSConcesionesComunesRule.obtenerArtibutos(rulectx);
		Solicitudes solicitudes=BDNSConcesionesComunesRule.obtenerSolicitudes();
		
		
		if(atributos!=null || solicitudes!=null){
			peticion = new Peticion();
		}
		if(atributos!=null){
			peticion.setAtributos(atributos);
		}
		if(solicitudes!=null){
			peticion.setSolicitudes(solicitudes);
		}		
		
		try {			
			concesion.peticion(peticion);
		} catch (RemoteException e) {
			LOGGER.error("Error al enviar la concesion. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar la concesion. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
		}
		
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}

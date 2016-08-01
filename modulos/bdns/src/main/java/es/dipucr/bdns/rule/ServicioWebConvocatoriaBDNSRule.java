package es.dipucr.bdns.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import es.dipucr.bdns.common.DatosEspecificos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuesta;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Atributos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitudes;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos;
import conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Transmisiones;
import es.dipucr.bdns.common.DatosEspecificosDatosEspecificosRespuestaAbstracta;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.objetos.Convocatoria;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.minhap.igae.ws.convocatoria.ConvocatoriaProxy;

public class ServicioWebConvocatoriaBDNSRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(ServicioWebConvocatoriaBDNSRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		String url = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_CONVOCATORIAS);
		ConvocatoriaProxy convocatoria = new ConvocatoriaProxy(url);
		
		/**
		 * TipoMovimiento
		 * Valores:
		 * A: para realizar alta de convocatorias
		 * B: para realizar la baja de convocatorias
		 * M: para realizar la modificación de convocatorias
		 * **/ 
		Atributos atributos = BDNSDipucrFuncionesComunes.obtenerAtributos(rulectx);
		
		String tipoMovimiento = TramitesUtil.getDatosEspecificosOtrosDatos(rulectx.getClientContext(), rulectx.getTaskProcedureId());
		TipoMovimiento tipoMov = null;
		if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_ALTA)){
			tipoMov = TipoMovimiento.A;
		}
		if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_MODIFICACION)){
			tipoMov = TipoMovimiento.M;
		}
		if(tipoMovimiento.equals(Constantes.TIPOMOVIMIENTO_BAJA)){
			tipoMov = TipoMovimiento.B;
		}
		
		Convocatoria convocaObj = BDNSDipucrFuncionesComunes.obtenerConvocatoria(rulectx, tipoMov);
		SolicitudTransmision[] solicitudTransmision = BDNSDipucrFuncionesComunes.obtenerSolicitudes(rulectx, atributos.getIdPeticion(), tipoMov, convocaObj);
		
		try {
			Peticion peticionSimple = new Peticion();
			Solicitudes solicitudes = new Solicitudes(solicitudTransmision, "identificador");
			peticionSimple.setSolicitudes(solicitudes);
			peticionSimple.setAtributos(atributos);
			Respuesta respuesta = convocatoria.peticion(peticionSimple);
			
			Transmisiones trasmisiones = respuesta.getTransmisiones();
			TransmisionDatos[] vTrasmisionesDatos = trasmisiones.getTransmisionDatos();
			for (int i = 0; i < vTrasmisionesDatos.length; i++) {
				TransmisionDatos TrasmisionesDatos = vTrasmisionesDatos[i];
				DatosEspecificos datosEspec = TrasmisionesDatos.getDatosEspecificos();
				DatosEspecificosDatosEspecificosRespuestaAbstracta datosRespuestaAbstracta = datosEspec.getDatosEspecificosRespuesta();
				
				DatosEspecificosDatosEspecificosRespuesta datosRespuesta = (DatosEspecificosDatosEspecificosRespuesta)datosRespuestaAbstracta;
				
				if(datosRespuesta.getIdConvocatoria()!=null){
					IItem bdIGAEConv = BDNSDipucrFuncionesComunes.obtenerItemBDNS(rulectx);
					
					bdIGAEConv.set("IDCONVOCATORIA", datosRespuesta.getIdConvocatoria());
					
					bdIGAEConv.store(rulectx.getClientContext());
				}
				else{
					logger.error("Error al enviar la convocatoria. "+rulectx.getNumExp()+ ": Codigo error: "+datosRespuesta.getCodigoEstadoSo()+" - Descripcion: "+datosRespuesta.getLiteralErrorSo());
					throw new ISPACRuleException("Error al enviar la convocatoria. "+rulectx.getNumExp()+ ": Codigo error: "+datosRespuesta.getCodigoEstadoSo()+" - Descripcion: "+datosRespuesta.getLiteralErrorSo());
				}				
			}	
		} catch (RemoteException e) {
			logger.error("Error al enviar la convocatoria. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar la convocatoria. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error al enviar la convocatoria. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al enviar la convocatoria. "+rulectx.getNumExp()+ " en los servicios web - "+e.getMessage(), e);
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

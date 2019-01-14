package es.dipucr.bdns.concesiones.client;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidades;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvio;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioConcesion;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPago;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPagoRetencion;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesionIdBeneficiario;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Atributos;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Emisor;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitante;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmisionDatosGenericos;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitudes;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Transmision;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Estado;
import concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
import es.dipucr.bdns.api.impl.BDNSAPI;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.common.DatosEspecificos;
import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.datospersonales.client.BDNSDatosPersonalesClient;
import es.dipucr.bdns.exception.BDNSException;
import es.dipucr.bdns.objetos.Concesion;
import es.dipucr.bdns.objetos.Pago;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyectoProxy;

public class BDNSConcesionesPagosClient {
	
	private static final Logger logger = Logger.getLogger(BDNSDipucrFuncionesComunes.class);

	public static List<Respuesta> altaModifConcesiones(IRuleContext rulectx, List<Concesion> listConcesiones) throws ISPACException{
		
		ArrayList<Respuesta> listRespuestas = new ArrayList<Respuesta>();
		for (Concesion concesion : listConcesiones){
			Respuesta respuesta = concesiones(rulectx, TipoMovimiento.A, concesion);
			String codEstado = respuesta.getAtributos().getEstado().getCodigoEstadoSecundario();
			if(BDNSConcesionesErrorConstants.COD_ERROR_EXISTE_CONCESION.equals(codEstado)){
				respuesta = concesiones(rulectx, TipoMovimiento.M, concesion);
			}
			listRespuestas.add(respuesta);
		}
		return listRespuestas;
	}
	
	public static List<Respuesta> bajaConcesiones(IRuleContext rulectx, List<Concesion> listConcesiones) throws ISPACException{
		
		ArrayList<Respuesta> listRespuestas = new ArrayList<Respuesta>();
		for (Concesion concesion : listConcesiones){
			Respuesta respuesta = concesiones(rulectx, TipoMovimiento.B, concesion);
			listRespuestas.add(respuesta);
		}
		return listRespuestas;
	}
	
	public static List<Respuesta> altaModifPagos(IClientContext cct, List<Pago> listPagos) throws ISPACException{
		
		ArrayList<Respuesta> listRespuestas = new ArrayList<Respuesta>();
		for (Pago pago : listPagos){
			Respuesta respuesta = pagos(cct, TipoMovimiento.A, pago);
			String codEstado = respuesta.getAtributos().getEstado().getCodigoEstadoSecundario();
			if(BDNSPagosErrorConstants.COD_ERROR_EXISTE_PAGO.equals(codEstado)){
				respuesta = pagos(cct, TipoMovimiento.M, pago);
			}
			listRespuestas.add(respuesta);
		}
		return listRespuestas;
	}
	
	public static List<Respuesta> bajaPagos(IClientContext cct, List<Pago> listPagos) throws ISPACException{
		
		ArrayList<Respuesta> listRespuestas = new ArrayList<Respuesta>();
		for (Pago pago : listPagos){
			Respuesta respuesta = pagos(cct, TipoMovimiento.B, pago);
			listRespuestas.add(respuesta);
		}
		return listRespuestas;
	}
	
	private static Respuesta concesiones(IRuleContext rulectx, TipoMovimiento tipoMovimiento, Concesion concesion) throws ISPACException{
		
		String idPeticion = null;
		Respuesta respuesta = null;
		
		try{
			IClientContext ctx = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			
			String url = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_CONCESPAGOPROY);
			
			//Damos de alta previamente el tercero al que hemos concedido la subvención
			BDNSDatosPersonalesClient.altaModifDatosPersonales(rulectx, concesion.getTercero());
			
			Peticion peticion = new Peticion();
			
			idPeticion = BDNSAPI.getIdPeticion((ClientContext) ctx, entitiesAPI);
			Atributos atributos = obtenerAtributos(idPeticion);
			peticion.setAtributos(atributos);
			
			Solicitudes solicitud = obtenerSolicitudConcesion(rulectx, idPeticion, tipoMovimiento, concesion);
			peticion.setSolicitudes(solicitud);
		
			/** Llamada al web service **/
			ConcesionPagoProyectoProxy WSConcesiones = new ConcesionPagoProyectoProxy(url);
			respuesta = WSConcesiones.peticion(peticion);
			
			if (null != respuesta){
				Estado estado = respuesta.getAtributos().getEstado();
				String codEstado = estado.getCodigoEstadoSecundario();
				if (!BDNSConcesionesErrorConstants.COD_EXITO.equals(codEstado)
						&& !BDNSConcesionesErrorConstants.COD_ERROR_EXISTE_CONCESION.equals(codEstado)
						&& !BDNSConcesionesErrorConstants.COD_ERROR_NO_EXISTE_CONCESION.equals(codEstado)){
					throw new BDNSException(BDNSException.COD_ERROR_CONCE, estado.getCodigoEstado(), 
							estado.getCodigoEstadoSecundario(), estado.getLiteralError());
				}
			}
		}
		catch(Exception e){
			manageException(tipoMovimiento, idPeticion, e);
		}
		return respuesta;
	}

	
	private static Respuesta pagos(IClientContext ctx, TipoMovimiento tipoMovimiento, Pago pago) throws ISPACException{
		
		String idPeticion = null;
		Respuesta respuesta = null;
		
		try{
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			
			String url = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_CONCESPAGOPROY);
			
			Peticion peticion = new Peticion();
			
			idPeticion = BDNSAPI.getIdPeticion((ClientContext) ctx, entitiesAPI);
			Atributos atributos = obtenerAtributos(idPeticion);
			peticion.setAtributos(atributos);
			
			Solicitudes solicitud = obtenerSolicitudPago(idPeticion, tipoMovimiento, pago);
			peticion.setSolicitudes(solicitud);
		
			/** Llamada al web service **/
			ConcesionPagoProyectoProxy WSConcesiones = new ConcesionPagoProyectoProxy(url);
			respuesta = WSConcesiones.peticion(peticion);
			
			if (null != respuesta){
				Estado estado = respuesta.getAtributos().getEstado();
				String codEstado = estado.getCodigoEstadoSecundario();
				if (!BDNSPagosErrorConstants.COD_EXITO.equals(codEstado)
						&& !BDNSPagosErrorConstants.COD_ERROR_EXISTE_PAGO.equals(codEstado)){
					throw new BDNSException(BDNSException.COD_ERROR_PAGOS, estado.getCodigoEstado(), 
							estado.getCodigoEstadoSecundario(), estado.getLiteralError());
				}
			}
		}
		catch(Exception e){
			manageException(tipoMovimiento, idPeticion, e);
		}
		return respuesta;
	}
	
	
	private static Atributos obtenerAtributos(String idPeticion) throws ISPACException {
		
		try{ 
			
			/** Objeto Atributos **/
			Atributos atributos = new Atributos();
			atributos.setIdPeticion(idPeticion);
			atributos.setNumElementos(1); //Siempre 1 en petición síncrona
			String timeStamp = FechasUtil.getFormattedDate(new Date(), "dd/MM/yyyy HH:mm:ss");
			atributos.setTimeStamp(timeStamp);
			atributos.setEstado(new concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Estado()); //Vacío. Sólo tiene sentido en la respuesta
			atributos.setCodigoCertificado(Constantes.COD_APP_CONCESIONES);
			
			return atributos;
		}
		catch(Exception e){
			String error = "Error al obtener los atributos de la concesión/pago";
			logger.error(error, e);
			throw new ISPACException(error, e);
		}
	}

	
	private static Solicitudes obtenerSolicitudConcesion(IRuleContext rulectx, String idPeticion, TipoMovimiento tipoMovimiento, 
			Concesion concesion) throws ISPACException {

		String numexp = null;
		try{ 
			numexp = rulectx.getNumExp();
			
			/** Objeto Solicitudes **/
			Solicitudes solicitudes = new Solicitudes();
			SolicitudTransmision solicitudTransmision = new SolicitudTransmision();
			solicitudTransmision.setDatosGenericos(obtenerDatosGenericos(idPeticion));
			solicitudTransmision.setDatosEspecificos(obtenerDatosEspecificosConcesion(tipoMovimiento, concesion));
			
			SolicitudTransmision [] arrSolicitudTransmision = new SolicitudTransmision[1];
			arrSolicitudTransmision[0] = solicitudTransmision;
			solicitudes.setSolicitudTransmision(arrSolicitudTransmision);
			
			return solicitudes;
		}
		catch(Exception e){
			String error = "Error al obtener las solicitudes de la concesión en el expediente " + numexp;
			logger.error(error, e);
			throw new ISPACException(error, e);
		}
	}
	
	
	private static Solicitudes obtenerSolicitudPago(String idPeticion, TipoMovimiento tipoMovimiento, Pago pago) throws ISPACException {

		try{ 
			/** Objeto Solicitudes **/
			Solicitudes solicitudes = new Solicitudes();
			SolicitudTransmision solicitudTransmision = new SolicitudTransmision();
			solicitudTransmision.setDatosGenericos(obtenerDatosGenericos(idPeticion));
			solicitudTransmision.setDatosEspecificos(obtenerDatosEspecificosPago(tipoMovimiento, pago));
			
			SolicitudTransmision [] arrSolicitudTransmision = new SolicitudTransmision[1];
			arrSolicitudTransmision[0] = solicitudTransmision;
			solicitudes.setSolicitudTransmision(arrSolicitudTransmision);
			
			return solicitudes;
		}
		catch(Exception e){
			String error = "Error al obtener las solicitudes de la concesión";
			logger.error(error, e);
			throw new ISPACException(error, e);
		}
	}

	
	private static SolicitudTransmisionDatosGenericos obtenerDatosGenericos(String idPeticion) {

		//TODO: Intentar fusionar con SolicitudTransmisionDatosGenericos de BDNSDipucrFuncionesComunes, aunque el objeto es distinto
		
		/** Emisor **/
		Emisor emisor = new Emisor();
		emisor.setNifEmisor(Constantes.NIF_IGAE);
		emisor.setNombreEmisor(Constantes.NOMBRE_IGAE);

		/** Solicitante **/
		Solicitante solicitante = new Solicitante();
		solicitante.setIdentificadorSolicitante(Constantes.DIR3_DIPUCR);
		solicitante.setNombreSolicitante(Constantes.NOMBRE_DIPUCR);
		//String finalidad = null;
		//String consentimiento = null;
		
		/** Titular (vacío) **/
		//Titular titular = null;
		
		/** Transmision **/
		Transmision transmision = new Transmision();
		transmision.setCodigoCertificado(Constantes.COD_APP_CONCESIONES);
		transmision.setIdSolicitud(idPeticion);
		//String idTransmision = null;
		//String fechaGeneracion = null;
		
		/** Objeto DatosGenericos **/
		SolicitudTransmisionDatosGenericos datosGenericos = new SolicitudTransmisionDatosGenericos();
		datosGenericos.setEmisor(emisor);
		datosGenericos.setSolicitante(solicitante);
		datosGenericos.setTransmision(transmision);
		
		return datosGenericos;
	}

	
	private static DatosEspecificos obtenerDatosEspecificosConcesion(TipoMovimiento tipoMovimiento, Concesion objConcesion) {
		
		/** Datos Generales **/
		DatosEspecificosDatosEspecificosPeticionDatosGenerales datosGenerales = new DatosEspecificosDatosEspecificosPeticionDatosGenerales();
		datosGenerales.setOrganoGestor(Constantes.DIR3_DIPUCR);
		datosGenerales.setTipoMovimiento(tipoMovimiento);
		
		/** Envio = Concesion + Pago **/
		DatosEspecificosDatosEspecificosPeticionEnvio envio = new DatosEspecificosDatosEspecificosPeticionEnvio();
		/** Concesion **/
		DatosEspecificosDatosEspecificosPeticionEnvioConcesion envioConcesion = new DatosEspecificosDatosEspecificosPeticionEnvioConcesion();
		IdConcesion idConcesion = new IdConcesion();
		idConcesion.setIdConvocatoria(objConcesion.getIdConvocatoria());
		IdConcesionIdBeneficiario idBeneficiario = new IdConcesionIdBeneficiario();
		idBeneficiario.setPaisBen(Constantes.DIR3_ESPAÑA);
		idBeneficiario.setIdPersonaBen(objConcesion.getTercero().getIdentificacion());
		idConcesion.setIdBeneficiario(idBeneficiario);
		idConcesion.setDiscriminadorConcesion(objConcesion.getReferenciaConcesion());
		envioConcesion.setIdConcesion(idConcesion);
		envioConcesion.setInstrumentoAyuda(objConcesion.getInstrumentoAyuda());
		if (null != objConcesion.getAyudaConcesion()){
			envioConcesion.setAyudaConcesion(BigDecimal.valueOf(objConcesion.getAyudaConcesion()));
		}
		if (null != objConcesion.getAyudaEquivalenteConcesion()){
			envioConcesion.setAyudaEquivalenteConcesion(BigDecimal.valueOf(objConcesion.getAyudaEquivalenteConcesion()));
		}
		if (null != objConcesion.getCosteConcesion()){
			envioConcesion.setCosteConcesion(BigDecimal.valueOf(objConcesion.getCosteConcesion()));
		}
		if (null != objConcesion.getSubvencionConcesion()){
			envioConcesion.setSubvencionConcesion(BigDecimal.valueOf(objConcesion.getSubvencionConcesion()));
		}
		envioConcesion.setFechaConcesion(objConcesion.getFechaConcesion());
		envioConcesion.setObjetivoConcesion(objConcesion.getObjetivoConcesion());
		envioConcesion.setRegionConcesion(Constantes.NUTS_PROV_CIUDADREAL);
		/** Datos de anualidades **/
		DatosAnualidades datosAnualidades = new DatosAnualidades(objConcesion.getArrAnualidades());
		envioConcesion.setDatosAnualidades(datosAnualidades);
		
		envio.setConcesion(envioConcesion);
		
		/** Datos específicos: Objeto devuelto **/
		DatosEspecificosDatosEspecificosPeticion peticion = new DatosEspecificosDatosEspecificosPeticion();
		peticion.setDatosGenerales(datosGenerales);
		peticion.setEnvio(envio);
		DatosEspecificos datosEspecificos = new DatosEspecificos();
		datosEspecificos.setDatosEspecificosPeticion(peticion);

		return datosEspecificos;
	}
	
	
	private static DatosEspecificos obtenerDatosEspecificosPago(TipoMovimiento tipoMovimiento, Pago objPago) {
		
		/** Datos Generales **/
		DatosEspecificosDatosEspecificosPeticionDatosGenerales datosGenerales = new DatosEspecificosDatosEspecificosPeticionDatosGenerales();
		datosGenerales.setOrganoGestor(Constantes.DIR3_DIPUCR);
		datosGenerales.setTipoMovimiento(tipoMovimiento);
		
		/** Envio = Concesion + Pago **/
		DatosEspecificosDatosEspecificosPeticionEnvio envio = new DatosEspecificosDatosEspecificosPeticionEnvio();
		/** Pago **/
		DatosEspecificosDatosEspecificosPeticionEnvioPago envioPago = new DatosEspecificosDatosEspecificosPeticionEnvioPago();
		IdPago idPago = new IdPago();
		idPago.setDiscriminadorPago(objPago.getReferenciaPago());
		IdConcesion idConcesion = new IdConcesion();
		idConcesion.setIdConvocatoria(objPago.getIdConvocatoria());
		IdConcesionIdBeneficiario idBeneficiario = new IdConcesionIdBeneficiario();
		idBeneficiario.setPaisBen(Constantes.DIR3_ESPAÑA);
		idBeneficiario.setIdPersonaBen(objPago.getCifBeneficiario());
		idConcesion.setIdBeneficiario(idBeneficiario);
		idConcesion.setDiscriminadorConcesion(objPago.getReferenciaConcesion());
		idPago.setIdConcesion(idConcesion);
		envioPago.setIdPago(idPago);
		
		//TODO: Estos son realmente los únicos dos campos distintos de la concesión
		envioPago.setFechaPago(objPago.getFechaPago());
		if (null != objPago.getImportePagado()){
			envioPago.setImportePagado(BigDecimal.valueOf(objPago.getImportePagado()));
		}
		if(objPago.isbRetencion()){
			envioPago.setRetencion(DatosEspecificosDatosEspecificosPeticionEnvioPagoRetencion.value2);//1: Con retención
		}
		else{
			envioPago.setRetencion(DatosEspecificosDatosEspecificosPeticionEnvioPagoRetencion.value1);//0: Sin retención
		}
		envio.setPago(envioPago);
		
		
		/** Datos específicos: Objeto devuelto **/
		DatosEspecificosDatosEspecificosPeticion peticion = new DatosEspecificosDatosEspecificosPeticion();
		peticion.setDatosGenerales(datosGenerales);
		peticion.setEnvio(envio);
		DatosEspecificos datosEspecificos = new DatosEspecificos();
		datosEspecificos.setDatosEspecificosPeticion(peticion);

		return datosEspecificos;
	}

	
	private static void manageException(TipoMovimiento tipoMovimiento, String idPeticion, Exception e)
			throws ISPACException {
		
		String error = "Error al realizar la petición de concesiones con tipo movimiento: " 
				+ tipoMovimiento.getValue() + ". IdPeticion: " + idPeticion;
		if (e instanceof BDNSException){
			error += " " + e.getMessage();
		}
		logger.error(error, e);
		throw new ISPACException(error, e);
	}

}



package es.dipucr.bdns.devoluciones.client;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificos;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvio;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioDevolucion;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesionIdBeneficiario;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Atributos;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Emisor;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitante;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmisionDatosGenericos;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitudes;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Transmision;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Estado;
import devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
import es.dipucr.bdns.api.impl.BDNSAPI;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.exception.BDNSException;
import es.dipucr.bdns.objetos.Devolucion;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.minhap.igae.ws.devolucionreintegro.DevolucionReintegroProxy;

public class BDNSDevolucionRentegrosClient {
	
	private static final Logger logger = Logger.getLogger(BDNSDipucrFuncionesComunes.class);

	
	public static List<Respuesta> altaModifDevoluciones(IClientContext cct, List<Devolucion> listDevoluciones) throws ISPACException{
		
		ArrayList<Respuesta> listRespuestas = new ArrayList<Respuesta>();
		for (Devolucion devolucion : listDevoluciones){
			Respuesta respuesta = devoluciones(cct, TipoMovimiento.A, devolucion);
			String codEstado = respuesta.getAtributos().getEstado().getCodigoEstadoSecundario();
			if(BDNSDevolucionReintegrosErrorConstants.COD_ERROR_EXISTE.equals(codEstado)){
				respuesta = devoluciones(cct, TipoMovimiento.M, devolucion);
			}
			listRespuestas.add(respuesta);
		}
		return listRespuestas;
	}
	
	
	private static Respuesta devoluciones(IClientContext cct, TipoMovimiento tipoMovimiento, Devolucion devolucion) throws ISPACException{
		
		String numexp = null;
		String idPeticion = null;
		Respuesta respuesta = null;
		
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			String url = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_DEVOLUCIONREINTEGRO);
			
			Peticion peticion = new Peticion();
			
			idPeticion = BDNSAPI.getIdPeticion((ClientContext) cct, entitiesAPI);
			Atributos atributos = obtenerAtributos(idPeticion);
			peticion.setAtributos(atributos);
			
			SolicitudTransmision[] arrSolicitudes = obtenerSolicitud(idPeticion, tipoMovimiento, devolucion);
			Solicitudes solicitudes = new Solicitudes();
			solicitudes.setSolicitudTransmision(arrSolicitudes);
			peticion.setSolicitudes(solicitudes);
		
			/** Llamada al web service **/
			DevolucionReintegroProxy WSDevoluciones = new DevolucionReintegroProxy(url);
			respuesta = WSDevoluciones.peticion(peticion);
			
			if (null != respuesta){
				Estado estado = respuesta.getAtributos().getEstado();
				String codEstado = estado.getCodigoEstadoSecundario();
				if (!BDNSDevolucionReintegrosErrorConstants.COD_EXITO.equals(codEstado)
						&& !BDNSDevolucionReintegrosErrorConstants.COD_ERROR_EXISTE.equals(codEstado)){
					throw new BDNSException(BDNSException.COD_ERROR_DEVOL, estado.getCodigoEstado(), 
							estado.getCodigoEstadoSecundario(), estado.getLiteralError());
				}
			}
		}
		catch(Exception e){
			String error = "Error al realizar el envío de devoluciones/reintegros con tipo movimiento: " 
					+ tipoMovimiento.getValue() + ". Expediente: " + numexp + ". IdPeticion: " + idPeticion;
			if (e instanceof BDNSException){
				error += " " + e.getMessage();
			}
			logger.error(error, e);
			throw new ISPACException(error, e);
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
			atributos.setEstado(new devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Estado()); //Vacío. Sólo tiene sentido en la respuesta
			atributos.setCodigoCertificado(Constantes.COD_APP_DEVOLUCIONES);
			
			return atributos;
		}
		catch(Exception e){
			String error = "Error al obtener los atributos de los datos personales";
			logger.error(error, e);
			throw new ISPACException(error, e);
		}
	}

	
	private static SolicitudTransmision[] obtenerSolicitud(String idPeticion, TipoMovimiento tipoMovimiento, Devolucion devolucion) throws ISPACException {

		try{
			SolicitudTransmision solicitudTransmision = new SolicitudTransmision();
			solicitudTransmision.setDatosGenericos(obtenerDatosGenericos(idPeticion));
			solicitudTransmision.setDatosEspecificos(obtenerDatosEspecificos(tipoMovimiento, devolucion));
			
			SolicitudTransmision [] arrSolicitudTransmision = new SolicitudTransmision[1];
			arrSolicitudTransmision[0] = solicitudTransmision;
			
			return arrSolicitudTransmision;
		}
		catch(Exception e){
			String error = "Error al obtener las solicitudes de la devolucion/reintegro";
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
		transmision.setCodigoCertificado(Constantes.COD_APP_DEVOLUCIONES);
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

	
	private static DatosEspecificos obtenerDatosEspecificos(TipoMovimiento tipoMovimiento, Devolucion objDevolucion) throws ISPACException {
		
		/** Datos Generales **/
		DatosEspecificosDatosEspecificosPeticionDatosGenerales datosGenerales = new DatosEspecificosDatosEspecificosPeticionDatosGenerales();
		datosGenerales.setOrganoGestor(Constantes.DIR3_DIPUCR);
		datosGenerales.setTipoMovimiento(tipoMovimiento);
		
		/** Datos personales **/
		DatosEspecificosDatosEspecificosPeticionEnvio envio = new DatosEspecificosDatosEspecificosPeticionEnvio();
		
		DatosEspecificosDatosEspecificosPeticionEnvioDevolucion devolucion = new DatosEspecificosDatosEspecificosPeticionEnvioDevolucion();
		
		/** IdDevolucion = DiscriminadorDev + IdConcesion **/
		IdDevolucion idDevolucion = new IdDevolucion();
		idDevolucion.setDiscriminadorDev(objDevolucion.getReferenciaDevolucion());
		/** IdConcesion = idConvocatoria + DiscriminadorConcesion + IdConcesionIdBeneficiario **/
		IdConcesion idConcesion = new IdConcesion();
		idConcesion.setIdConvocatoria(objDevolucion.getIdConvocatoria());
		idConcesion.setDiscriminadorConcesion(objDevolucion.getReferenciaConcesion());
		IdConcesionIdBeneficiario idBeneficiario = new IdConcesionIdBeneficiario();
		idBeneficiario.setPaisBen(Constantes.DIR3_ESPAÑA);
		idBeneficiario.setIdPersonaBen(objDevolucion.getCifBeneficiario());
		idConcesion.setIdBeneficiario(idBeneficiario);
		idDevolucion.setIdConcesion(idConcesion);
		devolucion.setIdDevolucion(idDevolucion);
		
		devolucion.setFechaDev(objDevolucion.getFechaDevolucion());
		if (null != objDevolucion.getImporteDevolucion()){
			devolucion.setImportePrincipalDev(BigDecimal.valueOf(objDevolucion.getImporteDevolucion()));
		}
		else{
			devolucion.setImportePrincipalDev(BigDecimal.valueOf(0.0));
		}
		if (null != objDevolucion.getImporteIntereses()){
			devolucion.setImporteInteresesDev(BigDecimal.valueOf(objDevolucion.getImporteIntereses()));
		}
		else{
			devolucion.setImporteInteresesDev(BigDecimal.valueOf(0.0));
		}
		envio.setDevolucion(devolucion);
		
		/** Datos Específicos **/
		DatosEspecificosDatosEspecificosPeticion peticion = new DatosEspecificosDatosEspecificosPeticion();
		peticion.setDatosGenerales(datosGenerales);
		peticion.setEnvio(envio);
		
		DatosEspecificos datosEspecificos = new DatosEspecificos();
		datosEspecificos.setDatosEspecificosPeticion(peticion);
		
		return datosEspecificos;
	}


}





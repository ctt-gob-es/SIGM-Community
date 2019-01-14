package es.dipucr.bdns.datospersonales.client;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IPostalAddressAdapter;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;

import java.util.Date;

import org.apache.log4j.Logger;

import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.ActividadEconomica;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificos;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonales;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosIdentificacion;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaFisica;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaJuridica;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Atributos;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Emisor;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitante;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmisionDatosGenericos;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Solicitudes;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Transmision;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Estado;
import datosper.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta;
import es.dipucr.bdns.api.impl.BDNSAPI;
import es.dipucr.bdns.common.BDNSDipucrFuncionesComunes;
import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.bdns.constantes.Constantes;
import es.dipucr.bdns.exception.BDNSException;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.minhap.igae.ws.datosPersonales.DatosPersonalesProxy;

public class BDNSDatosPersonalesClient {
	
	private static final Logger logger = Logger.getLogger(BDNSDipucrFuncionesComunes.class);

	
	public static void altaModifDatosPersonales(IRuleContext rulectx, IThirdPartyAdapter beneficiario) throws ISPACException{
		String codEstado = datosPersonales(rulectx, TipoMovimiento.M, beneficiario);
		if(BDNSDatosPersonalesErrorConstants.COD_ERROR_NO_EXISTE.equals(codEstado)){
			datosPersonales(rulectx, TipoMovimiento.A, beneficiario);
		}
	}
	
	
	private static String datosPersonales(IRuleContext rulectx, TipoMovimiento tipoMovimiento, IThirdPartyAdapter beneficiario) throws ISPACException{
		
		String numexp = null;
		String idPeticion = null;
		String codEstado = null;
		
		try{
			IClientContext ctx = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			numexp = rulectx.getNumExp();
			
			String url = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_DATOSPERSONALES);
			
			Peticion peticion = new Peticion();
			
			idPeticion = BDNSAPI.getIdPeticion((ClientContext) ctx, entitiesAPI);
			Atributos atributos = obtenerAtributos(rulectx, idPeticion);
			peticion.setAtributos(atributos);
			
			SolicitudTransmision[] arrSolicitudes = obtenerSolicitud(rulectx, idPeticion, tipoMovimiento, beneficiario);
			Solicitudes solicitudes = new Solicitudes();
			solicitudes.setSolicitudTransmision(arrSolicitudes);
			peticion.setSolicitudes(solicitudes);
		
			/** Llamada al web service **/
			DatosPersonalesProxy WSDatosPersonales = new DatosPersonalesProxy(url);
			Respuesta respuesta = WSDatosPersonales.peticion(peticion);
//			WSDatosPersonales.getDatosPersonales();//?
			
			if (null != respuesta){
				Estado estado = respuesta.getAtributos().getEstado();
				codEstado = estado.getCodigoEstadoSecundario();
				if (!BDNSDatosPersonalesErrorConstants.COD_EXITO.equals(codEstado)
						&& !BDNSDatosPersonalesErrorConstants.COD_ERROR_NO_EXISTE.equals(codEstado)){
					throw new BDNSException(BDNSException.COD_ERROR_PERSO, estado.getCodigoEstado(), 
							estado.getCodigoEstadoSecundario(), estado.getLiteralError());
				}
			}
		}
		catch(Exception e){
			String error = "Error al realizar la petición de datos personales con tipo movimiento: " 
					+ tipoMovimiento.getValue() + ". Expediente: " + numexp + ". IdPeticion: " + idPeticion;
			if (e instanceof BDNSException){
				error += " " + e.getMessage();
			}
			logger.error(error, e);
			throw new ISPACException(error, e);
		}
		return codEstado;
	}
	
	
	private static Atributos obtenerAtributos(IRuleContext rulectx, String idPeticion) throws ISPACException {
		
		try{ 
			
			/** Objeto Atributos **/
			Atributos atributos = new Atributos();
			atributos.setIdPeticion(idPeticion);
			atributos.setNumElementos(1); //Siempre 1 en petición síncrona
			String timeStamp = FechasUtil.getFormattedDate(new Date(), "dd/MM/yyyy HH:mm:ss");
			atributos.setTimeStamp(timeStamp);
			atributos.setEstado(new datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Estado()); //Vacío. Sólo tiene sentido en la respuesta
			atributos.setCodigoCertificado(Constantes.COD_APP_DATOSPERSONALES);
			
			return atributos;
		}
		catch(Exception e){
			String error = "Error al obtener los atributos de los datos personales";
			logger.error(error, e);
			throw new ISPACException(error, e);
		}
	}

	
	private static SolicitudTransmision[] obtenerSolicitud(IRuleContext rulectx, String idPeticion, TipoMovimiento tipoMovimiento, IThirdPartyAdapter beneficiario) throws ISPACException {

		String numexp = null;
		try{ 
			numexp = rulectx.getNumExp();
			
			SolicitudTransmision solicitudTransmision = new SolicitudTransmision();
			solicitudTransmision.setDatosGenericos(obtenerDatosGenericos(idPeticion));
			solicitudTransmision.setDatosEspecificos(obtenerDatosEspecificos(tipoMovimiento, beneficiario));
			
			SolicitudTransmision [] arrSolicitudTransmision = new SolicitudTransmision[1];
			arrSolicitudTransmision[0] = solicitudTransmision;
			
			return arrSolicitudTransmision;
		}
		catch(Exception e){
			String error = "Error al obtener las solicitudes de los datos personales en el expediente " + numexp;
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
		transmision.setCodigoCertificado(Constantes.COD_APP_DATOSPERSONALES);
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

	
	private static DatosEspecificos obtenerDatosEspecificos(TipoMovimiento tipoMovimiento, IThirdPartyAdapter beneficiario) throws ISPACException {
		
		/** Datos Generales **/
		DatosEspecificosDatosEspecificosPeticionDatosGenerales datosGenerales = new DatosEspecificosDatosEspecificosPeticionDatosGenerales();
		datosGenerales.setOrganoGestor(Constantes.DIR3_DIPUCR);
		datosGenerales.setTipoMovimiento(tipoMovimiento);
		
		/** Datos personales **/
		DatosEspecificosDatosEspecificosPeticionDatosPersonales datosPersonales = new DatosEspecificosDatosEspecificosPeticionDatosPersonales();
		/** Datos personales : Datos Identificación **/
		DatosIdentificacion datosIdentificacion = new DatosIdentificacion();
		datosIdentificacion.setPais(Constantes.DIR3_ESPAÑA);
		datosIdentificacion.setIdentificador(beneficiario.getIdentificacion());
		datosPersonales.setDatosIdentificacion(datosIdentificacion);
		
		/** Datos personales : Datos Denominación **/
		DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion datosDenominacion = 
				new DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion();
		if(IThirdPartyAdapter.TIPO_PERSONA_FISICA.equals(beneficiario.getTipoPersona())){
			PersonaFisica personaFisica = new PersonaFisica();
			personaFisica.setNombre(beneficiario.getNombre());
			personaFisica.setPrimerApellido(beneficiario.getPrimerApellido());
			personaFisica.setSegundoApellido(beneficiario.getSegundoApellido());
			datosDenominacion.setPersonaFisica(personaFisica);
		}
		else{
			PersonaJuridica personaJuridica = new PersonaJuridica();
			personaJuridica.setRazonSocial(beneficiario.getNombreCompleto());
			datosDenominacion.setPersonaJuridica(personaJuridica);
		}
		datosPersonales.setDatosDenominacion(datosDenominacion);
		
		/** Datos personales : Datos Domicilio **/
		DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio datosDomicilio = 
				new DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio();
		datosDomicilio.setPaisDom(Constantes.DIR3_ESPAÑA);
		IPostalAddressAdapter address = beneficiario.getDefaultDireccionPostal();
		datosDomicilio.setDomicilio(address.getDireccionPostal());
		datosDomicilio.setMunicipio(address.getMunicipio());
		datosDomicilio.setCodigoPostal(address.getCodigoPostal());
		datosDomicilio.setCodProvincia(address.getCodProvinciaDir3());
		datosDomicilio.setCodMunicipio(address.getCodMunicipioDir3());
		datosPersonales.setDatosDomicilio(datosDomicilio);
		
		/** Datos personales : Actividad económica **/
		ActividadEconomica actividadEconomica = new ActividadEconomica();
		actividadEconomica.setRegion(Constantes.NUTS_PROV_CIUDADREAL);//TODO
		//TODO: De momento metemos por defecto Personas Jurídicas sin actividad económica
		actividadEconomica.setTipoBeneficiario(Constantes.TIPO_TERCERO.JURIDICAS_NO_ECO);
//		actividadEconomica.setSectorEconomico(sectorEconomico); //OPCIONAL
		datosPersonales.setActividadEconomica(actividadEconomica);
		
		
		/** Datos Específicos **/
		DatosEspecificosDatosEspecificosPeticion peticion = new DatosEspecificosDatosEspecificosPeticion();
		peticion.setDatosGenerales(datosGenerales);
		peticion.setDatosPersonales(datosPersonales);
		
		DatosEspecificos datosEspecificos = new DatosEspecificos();
		datosEspecificos.setDatosEspecificosPeticion(peticion);
		
		return datosEspecificos;
	}


}

























package es.dipucr.bdns.boletinoficial.client;

import java.util.Date;

import org.apache.log4j.Logger;

import BDNS.confirmacionAnuncio.ConfirmacionAnuncio;
import BDNS.peticionAnuncio.PeticionAnuncio;
import BDNS.publicacionAnuncio.PublicacionAnuncio;
import BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncio;
import BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncioEstadoPublicacion;
import BDNS.respuestaAnuncio.RespuestaAnuncio;
import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.minhap.igae.ws.boletinoficial.BoletinOficialProxy;

/**
 * Clase cliente de Face que llamará a sus servicios web 
 * @author Felipe
 */
public class BDNSBopClient {
	
	/** Constantes **/
	public static final String REQUEST_DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
	public static final String APP_CODE_NAME = ServiciosWebConfiguration.BDNS_APP_BOP;
	
	/**
	 * Logger de la clase
	 */
	protected static final Logger logger = Logger.getLogger(BDNSBopClient.class);
		
	/**
	 * Objeto proxy que hará de web service para realizar las llamadas
	 */
	private BoletinOficialProxy ws = null;
	
	/**
	 * Constructor
	 * @throws Exception 
	 */
	public BDNSBopClient() throws Exception{
		iniciarConfiguracion();
	}
	
	/**
	 * Método que inicia la configuración de la aplicación
	 * @throws Exception 
	 */
	private void iniciarConfiguracion() throws Exception{
		
		String url = WSBDNSProperties.getURL(APP_CODE_NAME);
		ws = new BoletinOficialProxy(url);
	}
	
	
	public RespuestaAnuncio solicitarAnuncios(String idPeticion) throws Exception{
		
		iniciarConfiguracion();
		RespuestaAnuncio respuesta = null;
		
		try{
			//Formamos la petición de anuncio
			PeticionAnuncio peticionAnuncio = new PeticionAnuncio();
			peticionAnuncio.setIdPeticion(idPeticion);
			peticionAnuncio.setTimeStamp(FechasUtil.getFormattedDate(new Date(), REQUEST_DATE_PATTERN));
			
			//Petición
			respuesta = ws.peticionAnuncio(peticionAnuncio);
			
			if (BDNSBopErrorConstants.COD_NO_ANUNCIOS.equals(respuesta.getCodigoEstado())){
				logger.info("No hay ningún anuncio a publicar");
			}
			else if (!BDNSBopErrorConstants.COD_EXITO.equals(respuesta.getCodigoEstado())){
				throw new Exception("Error en la solicitud de anuncios. Error: " 
						+ respuesta.getCodigoEstado() + " :: " + respuesta.getLiteralError());
			}
		} catch (Exception e) {
			logger.error("Error al solicitar los anuncios", e);
			throw e;
		}
		return respuesta;
	}
	
	
	public RespuestaAnuncio solicitarAnuncios(String idPeticion, int idAnuncio) throws Exception{
		
		iniciarConfiguracion();
		RespuestaAnuncio respuesta = null;
		
		try{
			//Formamos la petición de anuncio
			PeticionAnuncio peticionAnuncio = new PeticionAnuncio();
			peticionAnuncio.setIdPeticion(idPeticion);
			peticionAnuncio.setTimeStamp(FechasUtil.getFormattedDate(new Date(), REQUEST_DATE_PATTERN));
			peticionAnuncio.setIdAnuncio(idAnuncio);
			
			//Petición
			respuesta = ws.peticionAnuncio(peticionAnuncio);
			
			if (!BDNSBopErrorConstants.COD_EXITO.equals(respuesta.getCodigoEstado())){
				throw new Exception("Error en la solicitud de anuncios. Error: " 
						+ respuesta.getCodigoEstado() + " :: " + respuesta.getLiteralError());
			}
		} catch (Exception e) {
			logger.error("Error al solicitar el anuncio " + idAnuncio, e);
			throw e;
		}
		return respuesta;
	}
	
	
	public ConfirmacionAnuncio publicarAnuncio(String idPeticion, String idAnuncio, Date fechaPublicacion,
			String cve, String idAnuncioBOP, String URL, String numexp) throws Exception{
		
		iniciarConfiguracion();
		ConfirmacionAnuncio confirmacion = null;
		try{
			PublicacionAnuncio publicacionAnuncio = new PublicacionAnuncio();
			publicacionAnuncio.setIdPeticion(idPeticion);
			publicacionAnuncio.setTimeStamp(FechasUtil.getFormattedDate(new Date(), REQUEST_DATE_PATTERN));
			
			PublicacionAnuncioAnunciosAnuncio[] arrAnuncios = new PublicacionAnuncioAnunciosAnuncio[1];
			PublicacionAnuncioAnunciosAnuncio anuncio = new PublicacionAnuncioAnunciosAnuncio();
			anuncio.setCVE(cve);
			anuncio.setEstadoPublicacion(PublicacionAnuncioAnunciosAnuncioEstadoPublicacion.P);
			anuncio.setFechaPublicacion(fechaPublicacion);
			anuncio.setIdAnuncio(Integer.valueOf(idAnuncio));
			anuncio.setIdAnuncioDiarioOficial(idAnuncioBOP);
			anuncio.setObservaciones("Anuncio del expediente " + numexp);
			anuncio.setURL(URL);
			arrAnuncios[0] = anuncio;
			
			publicacionAnuncio.setAnuncios(arrAnuncios);
			confirmacion = ws.publicacionAnuncio(publicacionAnuncio);
			
			if (!BDNSBopErrorConstants.COD_EXITO.equals(confirmacion.getCodigoEstado())){
				throw new Exception("Error en la solicitud de anuncios. Error: " 
						+ confirmacion.getCodigoEstado() + " :: " + confirmacion.getLiteralError());
			}
		}
		catch(Exception e){
			logger.error("Error al confirmar la publicación de los anuncios", e);
			throw e;
		}
		return confirmacion;
	}
	
}

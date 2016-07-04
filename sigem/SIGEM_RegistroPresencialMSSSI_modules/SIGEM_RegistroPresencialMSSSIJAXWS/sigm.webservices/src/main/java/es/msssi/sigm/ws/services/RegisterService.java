/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.ws.services;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import sigm.dao.dataaccess.domain.IUserUserHdrWS;
import sigm.dao.dataaccess.service.SIGMServiceManager;
import sigm.dao.exception.DaoException;
import core.tools.hasher.Hasher;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.msssi.sgm.registropresencial.beans.ContentTypeEnum;
import es.msssi.sigm.core.connector.PfeConnector;
import es.msssi.sigm.core.connector.SigmConnector;
import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.core.util.Constants;
import es.msssi.sigm.core.util.SigmUtil;
import es.msssi.sigm.core.util.XmlUtil;
import es.msssi.sigm.ws.beans.Fichero;
import es.msssi.sigm.ws.beans.Ficheros;
import es.msssi.sigm.ws.beans.FileType;
import es.msssi.sigm.ws.beans.FullInfoRegister;
import es.msssi.sigm.ws.beans.PeticionRegistro;
import es.msssi.sigm.ws.beans.RespuestaRegistro;
import es.msssi.sigm.ws.beans.SigmRequest;
import es.msssi.sigm.ws.beans.SigmResponse;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoDatos;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoFichero;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoSolicitud;

public class RegisterService extends SigmServiceBase implements SigmServiceI {
	
	private static final int MIMETYPE_MAX_LENGTH = 20;
	private Logger log = Logger.getLogger(RegisterService.class.getName());
	private ElementoSolicitud solicitud;
	private PeticionRegistro peticion;
	
	private Map<String, Fichero> adjuntos = null;
	private byte[] firmaFile;
	private byte[] solicitudFile;
	private IUserUserHdrWS iUserUserHdrWS;
	private String peticionValidada;
	
	public RegisterService(SigmRequest request) {
		super(request);
	}
	 	
	@Override
	public void printRequest(SigmRequest request) {
		log.error("Petición errónea: \n "+peticionValidada);
	}

	@Override
	public void validate() throws SigmWSException  {
	
		log.debug("RegisterService... validando");
		peticion = (PeticionRegistro)request; 
		peticionValidada = XmlUtil.validarPeticion(peticion);
         
		leerConfiguracionBBDD();
		
		extraerFicheros();
		
		boolean bVerificarFirma = true;
		
		bVerificarFirma = iUserUserHdrWS.isSignRequired();
		
		
		// si no hay fichero y hay es obligatorio firmar, se lanza excepcion
		if(bVerificarFirma && firmaFile == null) {
				throw new SigmWSException("err.validacion.sign.noFile");
		}
		
		if(firmaFile != null)
			verificarFirmaSolicitud();
		
		solicitud = transformarSolicitud();

		if(adjuntos != null && solicitud.getFicheros() != null){
			if(adjuntos.size() != solicitud.getFicheros().getFichero().size()){
				throw new SigmWSException("err.validacion.files.size");
			}
		} else if(adjuntos == null && solicitud.getFicheros() != null){
			if(solicitud.getFicheros().getFichero().size() != 0){
				throw new SigmWSException("err.validacion.files.size");
			}
		} else if(adjuntos != null && solicitud.getFicheros() == null){
				throw new SigmWSException("err.validacion.files.size");
		} else if(adjuntos == null && solicitud.getFicheros() != null){
			throw new SigmWSException("err.validacion.files.size");
		}
		
		if(adjuntos != null)
			verificarFicheros();
		
		validarParametrosRequeridosSigm();
		
		if(solicitud.getFicheros() != null)
			validarDocumentosElectronicos(solicitud.getFicheros().getFichero());

	}


	@Override
	public SigmResponse internalExecute() throws SigmWSException {

		log.debug("Peticion registrar...");
		
		FullInfoRegister fullInfoRegister = null; 

		SigmConnector connector = new SigmConnector();
		List<ElementoFichero> listElementoFichero = null;
		if(solicitud.getFicheros() != null)
			listElementoFichero = solicitud.getFicheros().getFichero();
		
		fullInfoRegister = connector.registrarSolicitud(user, pass, idBook, solicitud.getDatos(), adjuntos, listElementoFichero, solicitudFile, firmaFile);

		// GENERAR Respuesta
		log.debug("Procesando GENERANDO RESPUESTA...");
		
		RespuestaRegistro result = new RespuestaRegistro();
		result.setEstadoRespuesta(Constants.RESPONSE_OK);
		if(fullInfoRegister!=null){
			result.setEstadoRegistro(fullInfoRegister.getEstado());
			result.setFechaRegistro(SigmUtil.formatTimeStampInString(fullInfoRegister.getFechaRegistro()));
			result.setNumeroRegistro(fullInfoRegister.getNumRegistro());

            boolean genContentAcuse = false;
			if(peticion.getContenidoAcuse()!=null){
                genContentAcuse = peticion.getContenidoAcuse().equalsIgnoreCase(Constants.SI);
            }
			
			if(!genContentAcuse){
				fullInfoRegister.getAcuse().setContenido(null);
				fullInfoRegister.getAcuse().setNombre(null);			
			}
						
			result.setAcuse(fullInfoRegister.getAcuse());
			
		}
		
		return result;		
		
	}
	
	
	
	private void leerConfiguracionBBDD() throws SigmWSException {
		//consultar en BD la configuracion de la app
	 try {
		iUserUserHdrWS = SIGMServiceManager.getiUserUserHdrWSService().getByName(user);
		
		if(iUserUserHdrWS == null)
			throw new SigmWSException("err.validacion.noUser");
		
		} catch (DaoException e) {
			throw new SigmWSException("err.service.sigm", e);
		}
	 
		
	}
	
	
	
	private ElementoSolicitud transformarSolicitud() throws SigmWSException {

		ElementoSolicitud elementoSolicitud = null;		
		elementoSolicitud = XmlUtil.getJavafromXml(solicitudFile);
		return elementoSolicitud;
	}

	
	private void extraerFicheros() throws SigmWSException {

		PeticionRegistro peticion = (PeticionRegistro) request;		
		
		if(peticion.getFicheros() != null) {
			log.debug("Num ficheros: "+peticion.getFicheros().getFichero().size());
		
			Ficheros ficheros = peticion.getFicheros();
			for(Fichero fichero : ficheros.getFichero()){
				log.debug("Tipo de fichero: "+fichero.getTipo());
				if(fichero.getTipo().equals(FileType.ADJUNTO)){
					if(adjuntos == null)
						adjuntos = new HashMap<String, Fichero>();
					else 
					{					
						if(adjuntos.get(fichero.getNombre()) != null)
							throw new SigmWSException("err.validacion.files.duplicateName", new String[]{fichero.getNombre()});					
					}
					
					SigmUtil.validateNombreFichero(fichero.getNombre());
					
					adjuntos.put(fichero.getNombre(),fichero);
					
				} else if(fichero.getTipo().equals(FileType.FIRMA)){
					firmaFile = fichero.getContenido();
				} else if(fichero.getTipo().equals(FileType.SOLICITUD)){
					solicitudFile = fichero.getContenido();
				}
				
			}

		}
				
	}
	

	private boolean verificarFirmaSolicitud() throws SigmWSException {
		
		
		// VERIFICAMOS LA FIRMA ES CORRECTA Y SE CORRESPONDE CON SOLICITUD.XML						
		boolean validate = true;
		validate = PfeConnector.validate(solicitudFile, firmaFile);
		
		if(!validate){
			throw new SigmWSException("sign.noValid");
		}
		return validate;		
		 
		
	}
	
	private void verificarFicheros() throws SigmWSException {
		
		// SE VERIFICA QUE EL HASH DE CADA FICHERO SE CORRESPONDE CON SOLICITUD.XML			
		List<ElementoFichero> listFicheros = solicitud.getFicheros().getFichero();
		for(int i=0; i<listFicheros.size(); i++){
			ElementoFichero elementoFichero = listFicheros.get(i);
			
			// Informacion del fichero			
			String hashValue = elementoFichero.getHashValue();
			
			// Datos del fichero. Cargar desde disco:
			String nombreFichero = elementoFichero.getNombre();
			
			Fichero fichero = adjuntos.get(nombreFichero);
			if(fichero == null){
				throw new SigmWSException("err.validacion.files.noValidName", new String[]{nombreFichero});				
			}
			
			byte[] contenidoFichero = fichero.getContenido();
			
			
			byte[] generateHash;
			try {
				generateHash = Hasher.generateHash(contenidoFichero, elementoFichero.getHashAlgorithm().value());
			} catch (NoSuchAlgorithmException e) {
				throw new SigmWSException("excepcion.algoritmo", e);
			}
			
			String playHash = core.tools.Conversor.bytesToHex(generateHash);
			
			log.debug("Comprobando Hash de fichero "+nombreFichero);
			
			if(!playHash.equalsIgnoreCase(hashValue)) { // BinaryTools.byteArrayEquals(hashValue.getBytes(),generateHash)
				throw new SigmWSException("err.validacion.hash.noValid");				
			}
			
			log.debug("Comprobando tipo de contenido de fichero.");

			if(!fichero.getFormato().equals(ContentTypeEnum.valueOf(FilenameUtils.getExtension(nombreFichero).toUpperCase()).getContentType())){
				throw new SigmWSException("err.validacion.files.mime.noValid", new String[]{FilenameUtils.getExtension(nombreFichero), fichero.getFormato()});
			}	// Norma SICRES 3 limita el tamaño de tipo mime a 20 chars.
			else if(fichero.getFormato().length() > MIMETYPE_MAX_LENGTH){
				fichero.setFormato("");
			}

		}
				
	}
	
	

	private void validarParametrosRequeridosSigm() throws SigmWSException {
		
		ElementoDatos datos = solicitud.getDatos();

		tipoRegistro = datos.getTipoRegistro();
		if (tipoRegistro.equals(Constants.REGISTRO_ENTRADA))
			idBook = Constants.REGISTRO_ELECTRONICO_ENTRADA; // 6
		else if (tipoRegistro.equals(Constants.REGISTRO_SALIDA))
			idBook = Constants.REGISTRO_ELECTRONICO_SALIDA; // 7
				
		if(tipoRegistro.equals(Constants.REGISTRO_ENTRADA)) {
			if(datos.getDestino() == null){
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Destino es nulo"});
			}
			if(datos.getInteresados() != null && datos.getOrigen() != null){
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Interesado y Origen son no nulos"});
			}
			if(datos.getInteresados() == null && datos.getOrigen() == null){
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Interesado y Origen son nulos"});				
			}
		}else if(tipoRegistro.equals(Constants.REGISTRO_SALIDA)) {
			if(datos.getOrigen() == null){
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Origen es nulo"});
			}
			if(datos.getInteresados() != null && datos.getDestino() != null){
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Interesado y Destino son no nulos"});
			}
			if(datos.getInteresados() == null && datos.getDestino() == null){
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"Interesado y Destino son nulos"});				
			}
		}
	}

	private void validarDocumentosElectronicos(List<ElementoFichero> listElementoFichero) throws SigmWSException {
		
		for(ElementoFichero elementoFichero: listElementoFichero){
			
			
			log.debug("Comprobando tipo de documento.");

			
			if(elementoFichero.getTipoDocumento() == null) {
				elementoFichero.setTipoDocumento(String.valueOf(TipoDocumentoAnexoEnumVO.DOCUMENTO_ADJUNTO_FORMULARIO_VALUE));
			}
			
			
		}
	}
	
}

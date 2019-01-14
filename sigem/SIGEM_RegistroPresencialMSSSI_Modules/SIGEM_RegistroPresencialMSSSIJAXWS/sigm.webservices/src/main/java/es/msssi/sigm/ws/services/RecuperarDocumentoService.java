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

import org.apache.log4j.Logger;

import es.msssi.sigm.core.connector.SigmConnector;
import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.core.util.Constants;
import es.msssi.sigm.core.util.XmlUtil;
import es.msssi.sigm.ws.beans.Documento;
import es.msssi.sigm.ws.beans.PeticionRecuperarDocumento;
import es.msssi.sigm.ws.beans.RespuestaRecuperarDocumento;
import es.msssi.sigm.ws.beans.SigmRequest;
import es.msssi.sigm.ws.beans.SigmResponse;

public class RecuperarDocumentoService extends SigmServiceBase implements SigmServiceI {
	
	private Logger log = Logger.getLogger(RecuperarDocumentoService.class.getName());
	private String peticionValidada;
	
	public RecuperarDocumentoService(SigmRequest request) {
		super(request);
	}
	
	@Override
	public void printRequest(SigmRequest request) {
		log.error("Petición errónea: \n "+peticionValidada);
	}
	
	@Override
	public void validate() throws SigmWSException {
		log.debug("RecuperarDocumentoService... validando");
		PeticionRecuperarDocumento peticion = (PeticionRecuperarDocumento) request;
		
		peticionValidada = XmlUtil.validarPeticion(peticion);
		

		tipoRegistro = peticion.getTipoRegistro();
		if (tipoRegistro.equals(Constants.REGISTRO_ENTRADA))
			idBook = Constants.REGISTRO_ELECTRONICO_ENTRADA; 
		else if (tipoRegistro.equals(Constants.REGISTRO_SALIDA))
			idBook = Constants.REGISTRO_ELECTRONICO_SALIDA;
		
		
	}

	@Override
	public SigmResponse internalExecute() throws SigmWSException {
		
		PeticionRecuperarDocumento peticion = (PeticionRecuperarDocumento) request;
		byte[] contentFile = null;

		SigmConnector connector = new SigmConnector();
		contentFile = connector.recuperarDocumento(user, pass, idBook, peticion.getNumeroRegistro(), peticion.getNombreDocumento());
		
		// GENERAR Respuesta
		log.debug("Procesando GENERANDO RESPUESTA...");
		
		RespuestaRecuperarDocumento result = new RespuestaRecuperarDocumento();
		result.setEstadoRespuesta(Constants.RESPONSE_OK);
		if(contentFile != null){
			Documento documento = new Documento();
			documento.setNombre(peticion.getNombreDocumento());
			documento.setContenido(contentFile);
			result.setDocumento(documento);
		}
		
		return result;
		
	}


}

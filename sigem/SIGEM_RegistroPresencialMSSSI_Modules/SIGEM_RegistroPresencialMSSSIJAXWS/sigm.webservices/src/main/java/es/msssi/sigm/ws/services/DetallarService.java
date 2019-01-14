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

import java.util.List;

import org.apache.log4j.Logger;

import es.msssi.sigm.core.connector.SigmConnector;
import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.core.util.Constants;
import es.msssi.sigm.core.util.XmlUtil;
import es.msssi.sigm.ws.beans.PeticionDetalle;
import es.msssi.sigm.ws.beans.Registro;
import es.msssi.sigm.ws.beans.RespuestaDetalle;
import es.msssi.sigm.ws.beans.SigmRequest;
import es.msssi.sigm.ws.beans.SigmResponse;

public class DetallarService extends SigmServiceBase implements SigmServiceI {
	
	private Logger log = Logger.getLogger(DetallarService.class.getName());
	private String peticionValidada;
	
	public DetallarService(SigmRequest request) {
		super(request);
	}

	@Override
	public void printRequest(SigmRequest request) {
		log.error("Petición errónea: \n "+peticionValidada);
	}
	
	@Override
	public void validate() throws SigmWSException {

		log.debug("DetallarService... validando");
		
		PeticionDetalle peticion = (PeticionDetalle) request;
		peticionValidada = XmlUtil.validarPeticion(peticion);
		
		tipoRegistro = peticion.getTipoRegistro();
		if (tipoRegistro.equals(Constants.REGISTRO_ENTRADA))
			idBook = Constants.REGISTRO_ELECTRONICO_ENTRADA;
		else if (tipoRegistro.equals(Constants.REGISTRO_SALIDA))
			idBook = Constants.REGISTRO_ELECTRONICO_SALIDA;


		
		
	}

	
	@Override
	public SigmResponse internalExecute() throws SigmWSException {
		
		PeticionDetalle peticion = (PeticionDetalle) request;
		List<Registro> listRegistro = null;		
		 
		SigmConnector connector = new SigmConnector();
		listRegistro = connector.detallar(user, pass, tipoRegistro, idBook, peticion);
		
		
		// GENERAR Respuesta
		log.debug("Procesando GENERANDO RESPUESTA...");
		
		RespuestaDetalle result = new RespuestaDetalle();
		result.setEstadoRespuesta(Constants.RESPONSE_OK);
		
		if(listRegistro != null && listRegistro.size() != 0) {
			result.setRegistro(listRegistro.get(0));				
		}
		
		return result;
	}
	

}

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
import es.msssi.sigm.ws.beans.Acuse;
import es.msssi.sigm.ws.beans.PeticionGenerarAcuse;
import es.msssi.sigm.ws.beans.RespuestaGenerarAcuse;
import es.msssi.sigm.ws.beans.SigmRequest;
import es.msssi.sigm.ws.beans.SigmResponse;

public class GenerarAcuseService extends SigmServiceBase implements SigmServiceI {

	private Logger log = Logger.getLogger(GenerarAcuseService.class.getName());
	private String peticionValidada;
	
	public GenerarAcuseService(SigmRequest request) {
		super(request);
	}

	@Override
	public void printRequest(SigmRequest request) {
		log.error("Petición errónea: \n "+peticionValidada);
	}

	@Override
	public void validate() throws SigmWSException {
		
		log.debug("GenerarAcuseService... validando");
		PeticionGenerarAcuse peticion = (PeticionGenerarAcuse) request;
		peticionValidada = XmlUtil.validarPeticion(peticion);
		

		tipoRegistro = peticion.getTipoRegistro();
		if (tipoRegistro.equals(Constants.REGISTRO_ENTRADA))
			idBook = Constants.REGISTRO_ELECTRONICO_ENTRADA; 
		else if (tipoRegistro.equals(Constants.REGISTRO_SALIDA))
			idBook = Constants.REGISTRO_ELECTRONICO_SALIDA;
		
	}

	@Override
	public SigmResponse internalExecute() throws SigmWSException {
		PeticionGenerarAcuse peticion = (PeticionGenerarAcuse) request;
		
		SigmConnector connector = new SigmConnector();
		Acuse acuse = connector.acuse(user, pass, tipoRegistro, idBook, peticion);
		
		log.debug("Procesando GENERANDO RESPUESTA...");
		
		RespuestaGenerarAcuse result = new RespuestaGenerarAcuse();
		result.setEstadoRespuesta(Constants.RESPONSE_OK);
		if(acuse != null){
			result.setAcuse(acuse);
		}
		
		return result;
		
	}

}

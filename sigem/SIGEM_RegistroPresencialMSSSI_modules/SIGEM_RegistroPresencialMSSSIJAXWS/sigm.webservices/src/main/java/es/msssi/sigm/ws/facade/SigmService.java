/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.ws.facade;

import javax.jws.HandlerChain;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import es.msssi.sigm.ws.beans.PeticionAdjuntarDocumento;
import es.msssi.sigm.ws.beans.PeticionBusqueda;
import es.msssi.sigm.ws.beans.PeticionDetalle;
import es.msssi.sigm.ws.beans.PeticionGenerarAcuse;
import es.msssi.sigm.ws.beans.PeticionRecuperarDocumento;
import es.msssi.sigm.ws.beans.PeticionRegistro;
import es.msssi.sigm.ws.beans.RespuestaAdjuntarDocumento;
import es.msssi.sigm.ws.beans.RespuestaBusqueda;
import es.msssi.sigm.ws.beans.RespuestaDetalle;
import es.msssi.sigm.ws.beans.RespuestaGenerarAcuse;
import es.msssi.sigm.ws.beans.RespuestaRecuperarDocumento;
import es.msssi.sigm.ws.beans.RespuestaRegistro;
import es.msssi.sigm.ws.beans.SigmResponse;
import es.msssi.sigm.ws.services.AdjuntarDocumentoService;
import es.msssi.sigm.ws.services.BuscarService;
import es.msssi.sigm.ws.services.DetallarService;
import es.msssi.sigm.ws.services.GenerarAcuseService;
import es.msssi.sigm.ws.services.RecuperarDocumentoService;
import es.msssi.sigm.ws.services.RegisterService;

@WebService
@javax.xml.ws.soap.MTOM
@HandlerChain(file = "wssec-handler-chain.xml")
public class SigmService extends BaseService{

	private Logger log = Logger.getLogger(SigmService.class.getName());
	
	/**
	 * Realiza el registro de datos y documentos en SIGM 
	 * @param request PeticionRegistro
	 * @return RespuestaRegistro
	 */
	public RespuestaRegistro registrar(@WebParam(name = "request") PeticionRegistro request){

		log.debug("Registrando...");
		saveSOAPCredentials();
		
		RegisterService service = new RegisterService(request);
		service.setUser(getUser());
		service.setPass(getPass());
		SigmResponse response = service.execute();
		
		
		return SigmResponse.parse(response, RespuestaRegistro.class);
	}	

	
	/**
	 * Obtiene informacion en detalle de un registro
	 * @param request PeticionDetalle
	 * @return RespuestaDetalle
	 */
    public RespuestaDetalle detallar(@WebParam(name = "request") PeticionDetalle request) {
		
		log.debug("Detallar...");
		saveSOAPCredentials();
		
		DetallarService service = new DetallarService(request);
		service.setUser(getUser());
		service.setPass(getPass());
		SigmResponse response = service.execute();
		
		
		return SigmResponse.parse(response, RespuestaDetalle.class);
	}

	/**
	 * Obtiene el documento de acuse en pdf
	 * @param request PeticionAcuse
	 * @return RespuestaAcuse
	 */
    public RespuestaGenerarAcuse generarAcuse(@WebParam(name = "request") PeticionGenerarAcuse request) {
		
		log.debug("Acuse...");
		saveSOAPCredentials();
		
		GenerarAcuseService service = new GenerarAcuseService(request);	
		service.setUser(getUser());
		service.setPass(getPass());
		SigmResponse response = service.execute();
		
		return SigmResponse.parse(response, RespuestaGenerarAcuse.class);
	}
  
    /**
     * Recupera un documento de un registro
     * @param request PeticionRecuperarDocumento
     * @return RespuestaRecuperarDocumento
     */
    public RespuestaRecuperarDocumento recuperarDocumento(@WebParam(name = "request") PeticionRecuperarDocumento request) {
		
		log.debug("Recuperar documento de registro...");
		saveSOAPCredentials();
		
		RecuperarDocumentoService service = new RecuperarDocumentoService(request);	
		service.setUser(getUser());
		service.setPass(getPass());
		SigmResponse response = service.execute();
		
		return SigmResponse.parse(response, RespuestaRecuperarDocumento.class);
	}
	
    /**
     * Adjunta un documento a un registro
     * @param request PeticionAdjunto
     * @return RespuestaAdjunto
     */
    public RespuestaAdjuntarDocumento adjuntarDocumento(@WebParam(name = "request") PeticionAdjuntarDocumento request) {
    	
		log.debug("Peticion adjuntar...");
		saveSOAPCredentials();
		
		AdjuntarDocumentoService service = new AdjuntarDocumentoService(request);	
		service.setUser(getUser());
		service.setPass(getPass());
		SigmResponse response = service.execute();
		
		return SigmResponse.parse(response, RespuestaAdjuntarDocumento.class);
	}
	
	/**
	 * Busca registro a partir de unos criterios
	 * @param request PeticionBusqueda
	 * @return RespuestaBusqueda
	 */
    public RespuestaBusqueda buscar(@WebParam(name = "request") PeticionBusqueda request) {
		log.debug("Peticion busqueda...");
		saveSOAPCredentials();
		
		BuscarService service = new BuscarService(request);	
		service.setUser(getUser());
		service.setPass(getPass());
		
		SigmResponse response = service.execute();
		
		return SigmResponse.parse(response, RespuestaBusqueda.class);
	}
	
}

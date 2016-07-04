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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.core.util.Constants;
import es.msssi.sigm.core.util.SigmUtil;
import es.msssi.sigm.ws.beans.ErrorResponse;
import es.msssi.sigm.ws.beans.SigmRequest;
import es.msssi.sigm.ws.beans.SigmResponse;

public abstract class SigmServiceBase {
	
	private Logger log = Logger.getLogger(SigmServiceBase.class.getName());
	protected SigmRequest request;
	protected String user;
	protected String pass;
	protected int idBook = 6; 
	protected String tipoRegistro = "E";
	
	public SigmServiceBase(SigmRequest request) {
		this.request = request;
	}
	
	public SigmResponse execute(){
		SigmResponse sigmResponse = null;
		try{
			log.info("Peticion["+this+"]...");
			validateUser();
			validate();
			sigmResponse = internalExecute();
			
		}catch(SigmWSException se){
			printRequest(request);
			sigmResponse = new SigmResponse();
			ErrorResponse  errorResponse = new ErrorResponse(); 
			errorResponse.setCodigo(SigmWSException.getErrorCode(se.getMsgID()));
			errorResponse.setDescripcion(SigmWSException.getErrorDescription(se.getMessage()));
			sigmResponse.setEstadoRespuesta(Constants.RESPONSE_ERROR);
			sigmResponse.setErrorResponse(errorResponse);
		}catch(Throwable se){ 
			
			printRequest(request);
			sigmResponse = new SigmResponse();
			ErrorResponse  errorResponse = new ErrorResponse();
			log.error(ExceptionUtils.getStackTrace(se)); 
			errorResponse.setCodigo(SigmWSException.getErrorCode("err.general"));
			errorResponse.setDescripcion(SigmWSException.getErrorDescription("Excepcion general"));
			sigmResponse.setEstadoRespuesta(Constants.RESPONSE_ERROR);
			sigmResponse.setErrorResponse(errorResponse);
		}finally{
			return sigmResponse;
		}
		
	}
	

	public abstract void printRequest(SigmRequest request);
	
	public abstract void validate() throws SigmWSException;
	
	public abstract SigmResponse internalExecute() throws SigmWSException;
	
	protected void validateUser() throws SigmWSException{
 
		SigmUtil.validarUsuario(getUser(),getPass(), Constants.SIGEM_ENTIDAD_MSSSI);		
		
	}

	
	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	

}

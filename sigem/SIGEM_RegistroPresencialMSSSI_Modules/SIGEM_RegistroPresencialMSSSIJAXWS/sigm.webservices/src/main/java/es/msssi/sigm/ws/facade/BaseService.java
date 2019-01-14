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

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import core.wssec.utils.Constants;

/**
 * Clase abstracta de la que extenderan los servicios web. Sirve
 * funcionalidad comun a todos ellos.
 * 
 * @author jviota
 * 
 */

public abstract class BaseService {

    private String user;
    private String pass;

    @Resource
    WebServiceContext wsContext;

    protected void saveSOAPCredentials(){
    	MessageContext messageContext = (MessageContext) wsContext.getMessageContext();
    	
    	String user = (String) messageContext.get(Constants.NODE_USERNAME);
    	String pass = (String) messageContext.get(Constants.NODE_PASSWORD);
    	
    	setUser(user);
    	setPass(pass);
    	
    }

    public void setPass(String pass) {
		this.pass = pass;
	}
    public void setUser(String user) {
		this.user = user;
	}
    public String getPass() {
		return pass;
	}
    public String getUser() {
		return user;
	}
}
/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.services;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.Service;

/**
 * Servicio con la funcionalidad base de todos los servicios que implementen los
 * WS de dir3.
 * 
 * @author cmorenog
 */

public class ServiceBaseImpl {

    /**
     * PORTAFIRMASSERVICE.
     */
    protected static final String DIR3SERVICE = "DIR3SERVICE";

    /**
     * CTXSERVICEBASEPATH.
     */
    private static final String CTXSERVICEBASEPATH = "java:comp/env/service/";

    /**
     * Recupera el servicio a partir del nombre.
     * 
     * @param name
     *            Nombre dle servicio.
     * 
     * @return Service.
     */
    protected static Service getCtxService(
	String name) {
	try {
	    Context ctx = new InitialContext();
	    return (Service) ctx.lookup(CTXSERVICEBASEPATH +
		name);
	}
	catch (NamingException e) {
	    throw new RuntimeException(
		e);
	}
    }

}
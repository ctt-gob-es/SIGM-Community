/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.init;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import core.log.SecurityLogger;

/**
 * This servlet initialize the swart.core library.
 *
 * @author jviota
 * @version version, 07/01/2015 13:17
 */
public class SigmInitServlet extends HttpServlet {

    private boolean alreadyInitied = false;
 
    /**
     * Initialize the swart.core library.
     *
     * @throws ServletException
     *             if an error has been found.
     */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
		    if (alreadyInitied) {
			SecurityLogger.info("sigm.webservices library already initialized");
			return;
		    }
//		    SecurityLogger.info("Initializing swart.core library");
		    alreadyInitied = true;
		    SecurityLogger.info("sigm.webservices library  initialized");
		}
		catch (Throwable t) {
		    SecurityLogger.error("Error initializing sigm.webservices library", t);
		}
	}
	
}

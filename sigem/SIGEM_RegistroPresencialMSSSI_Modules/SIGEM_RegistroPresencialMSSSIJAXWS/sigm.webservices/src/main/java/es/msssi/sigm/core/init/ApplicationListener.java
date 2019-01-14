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

import java.util.Enumeration;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import core.security.BouncyCastle;
import es.msssi.sgm.registropresencial.beans.WebParameter;

/**
 * Clase inicial que carga las propiedades para los servicios web.
 * 
 * @author jortizs
 */
public class ApplicationListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(ApplicationListener.class.getName());
    private static String propertiesPath = null;

    /**
     * Carga las propiedades en el contexto de la aplicación.
     * 
     * @param arg0
     *            Contexto de la aplicación.
     */
    public void contextInitialized(ServletContextEvent arg0) {
	LOG.trace("Inicializando contexto y cargando los archivos de propiedades");
	ServletContext sc = arg0.getServletContext();
	initParameters(sc);
//	propertiesPath =
//		(String) WebParameter.getEntryParameter("PATH_REPO")
//			+ (String) WebParameter
//				.getEntryParameter("MSSSIPATH_regtel.properties.path");
//	if (PropertiesLoader.getInstance().getPropertiesDir() == null) {
//	    PropertiesLoader.getInstance().setPropertiesDir(propertiesPath);
//	}
	
//	new ClassPathXmlApplicationContext("classpath*:spring-config.xml");
	
	LOG.info("Carga el proveedor de Bouncy Castle");
	BouncyCastle.removeAndInsert();
    }

    /**
     * Método no utilizado, válido en el momento de la destrucción del contexto
     * de la aplicación.
     * 
     * @param arg0
     *            Contexto de la aplicación.
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    /**
     * Inicia con el contexto de aplicación.
     * 
     * @param context
     *            ServletContext principal de la aplicación web.
     * 
     * @throws NamingException.
     */
    @SuppressWarnings("unchecked")
    private void initParameters(ServletContext context) {
	LOG.trace("Entrando en Utils.initParameters()"
		+ " para iniciar el contexto de la aplicación");
	Enumeration<String> e = context.getInitParameterNames();
	HashMap<String, String> initParameters = new HashMap<String, String>();
	String key = null;
	while (e.hasMoreElements()) {
	    key = e.nextElement();
	    initParameters.put(key, context.getInitParameter(key));
	}
	WebParameter.setInitParameters(initParameters);
	try {
	    Context inicial = new InitialContext();
	    Context miCtx = (Context) inicial.lookup("java:comp/env");
	    HashMap<String, Object> entryParameters = new HashMap<String, Object>();
	    Enumeration<NameClassPair> namingE = miCtx.list("");
	    while (namingE.hasMoreElements()) {
		key = ((NameClassPair) namingE.nextElement()).getName();
		entryParameters.put(key, miCtx.lookup(key));
	    }
	    WebParameter.setEntryParameters(entryParameters);
	}
	catch (NamingException namingException) {
	    LOG.error("Error al crear el contexto principal de la aplicación", namingException);
	}
    }
}
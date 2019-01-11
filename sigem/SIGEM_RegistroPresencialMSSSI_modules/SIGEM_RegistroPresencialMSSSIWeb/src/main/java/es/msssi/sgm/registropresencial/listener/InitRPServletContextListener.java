package es.msssi.sgm.registropresencial.listener;

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

import es.msssi.sgm.registropresencial.beans.WebParameter;

/*
 * Listener que introduce en las variables de sistema las rutas provenientes de los env-entry
 * en relación a las rutas y endpoints del sistema
 * @author cmorenog
 * */
public class InitRPServletContextListener implements ServletContextListener {

    private static final long serialVersionUID = 417839469745899329L;

    private static final Logger logger = Logger.getLogger(InitRPServletContextListener.class);

    public void contextDestroyed(ServletContextEvent arg0) {

    }

    public void contextInitialized(ServletContextEvent event) {
	Context env;
	try {
	    ServletContext context = event.getServletContext();
	    Enumeration<String> e = context.getInitParameterNames();
	    HashMap<String, String> initParameters = new HashMap<String, String>();
	    String key = null;
	    while (e.hasMoreElements()) {
		key = e.nextElement();
		initParameters.put(key, context.getInitParameter(key));
	    }
	    WebParameter.setInitParameters(initParameters);

	    env = (Context) new InitialContext().lookup("java:comp/env");

	    HashMap<String, Object> entryParameters = new HashMap<String, Object>();
	    Enumeration<NameClassPair> namingE = env.list("");
	    while (namingE.hasMoreElements()) {
		key = ((NameClassPair) namingE.nextElement()).getName();
		entryParameters.put(key, env.lookup(key));
	    }
	    WebParameter.setEntryParameters(entryParameters);

	    logger.info("Paths cargados");
	}
	catch (NamingException e) {
	    logger.error(
		    "No se ha podido inicializar los Path de la aplicación", e);
	}

    }

}

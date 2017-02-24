package es.msssi.tecdoc.sgm.registropresencial.servlet;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

/*
 * Servlet que introduce en las variables de sistema las rutas provenientes de los env-entry
 * en relación a las rutas y endpoints del sistema
 * @author cmorenog
 * */
public class InitPathServlet extends HttpServlet {

	

	private static final long serialVersionUID = 417839469745899329L;
	private static final String PATHREPO ="PATH_REPO";
	private static final String PATHNASREPO ="PATH_NAS_REPO";
	private static final String PATHSMBREPO ="PATH_SMB_REPO";
	private static final String URLWS ="URL_WS";
	
	@Override
    public void init() throws ServletException {
		NamingEnumeration<NameClassPair> listNaming;
		NameClassPair enventry;
		String name;
		Context env;
		try {
			listNaming = new InitialContext().list("java:comp/env");
			env = (Context) new InitialContext().lookup("java:comp/env");
			 while(listNaming.hasMoreElements()){  
				 enventry = (NameClassPair) listNaming.next();
				
				if (enventry.getName().startsWith(PATHNASREPO))
				{
					name = enventry.getName();
					System.setProperty(name, (String)env.lookup(enventry.getName()));    
				}
				if (enventry.getName().startsWith(PATHSMBREPO))
				{
					name = enventry.getName();
					System.setProperty(name, (String)env.lookup(enventry.getName()));    
				}
				if (enventry.getName().startsWith("MSSSI_"))
				{
					name = enventry.getName().substring(6);
					System.setProperty(name, (String)env.lookup(enventry.getName()));    
				}
				if (enventry.getName().startsWith("MSSSIPATH_"))
				{
					name = enventry.getName().substring(10);
					System.setProperty(name, (String)env.lookup(PATHREPO)+(String)env.lookup(enventry.getName()));    
				}
				if (enventry.getName().startsWith("MSSSINASPATH_"))
				{
					name = enventry.getName().substring(13);
					System.setProperty(name, (String)env.lookup(PATHNASREPO)+(String)env.lookup(enventry.getName()));  
				}
				if (enventry.getName().startsWith("MSSSIURL_"))
				{
					name = enventry.getName().substring(9);
					System.setProperty(name, (String)env.lookup(URLWS)+(String)env.lookup(enventry.getName()));    
				}
		    }
			 
		    Logger.getLogger(InitPathServlet.class).info("Paths cargados");
		} catch (NamingException e) {
			Logger.getLogger(InitPathServlet.class).error("No se ha podido inicializar los Path de la aplicación", e);
		}
	}
}

package es.scsp.client.test.datosespecificos;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
public class NameSpaceContextV3 implements NamespaceContext{
	public String getNamespaceURI( String prefix) {
	      if ( prefix.equals( "esp")) {
	        return "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos";
	      }else{
	    	 return "http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta";
	      }
	     
	    }
	    public String getPrefix( String namespaceURI) {
	      if ( namespaceURI.equals( "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos")) {
	        return "esp";
	      }  else 			    
	    	  return "resp";
	    }
	  
	    public Iterator getPrefixes( String namespaceURI) {
	      ArrayList list = new ArrayList();
	    
	      if ( namespaceURI.equals( "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos")) {
	        list.add( "esp");
	      } else
	    	  list.add("resp");
	    
	      return list.iterator();
	    }
}

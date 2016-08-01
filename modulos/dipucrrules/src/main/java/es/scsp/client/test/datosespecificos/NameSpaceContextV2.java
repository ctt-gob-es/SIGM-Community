package es.scsp.client.test.datosespecificos;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
public class NameSpaceContextV2 implements NamespaceContext{
	public String getNamespaceURI( String prefix) {
	      if ( prefix.equals( "esp")) {
	        return "http://www.map.es/scsp/esquemas/datosespecificos";
	      }else{
	    	 return "http://www.map.es/scsp/esquemas/V2/respuesta";
	      }
	     
	    }
	    public String getPrefix( String namespaceURI) {
	      if ( namespaceURI.equals( "http://www.map.es/scsp/esquemas/datosespecificos")) {
	        return "esp";
	      }  else 			    
	    	  return "resp";
	    }
	  
	    public Iterator getPrefixes( String namespaceURI) {
	      ArrayList list = new ArrayList();
	    
	      if ( namespaceURI.equals( "http://www.map.es/scsp/esquemas/datosespecificos")) {
	        list.add( "esp");
	      } else
	    	  list.add("resp");
	    
	      return list.iterator();
	    }
}

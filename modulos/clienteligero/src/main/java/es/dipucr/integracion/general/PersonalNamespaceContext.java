package es.dipucr.integracion.general;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

/**
 * Clase para asignar espacios de nombres.
 * Tuve problemas para evaluar expresiones Xpath así que buscando soluciones
 * encontré esta clase y solucionó el problema. Normalmente, la llamada 
 * a esta clase será:
 * 	XPathFactory factory = XPathFactory.newInstance();
 *	XPath xPath = factory.newXPath();
 *	xPath.setNamespaceContext(new PersonalNamespaceContext("env","http://schemas.xmlsoap.org/soap/envelope/"));
 *
 */
public class PersonalNamespaceContext implements NamespaceContext {

	public String uri;

	 public String prefix;

	 public PersonalNamespaceContext(){}

	 public PersonalNamespaceContext(String prefix, String uri){

	     this.uri=uri;

	     this.prefix=prefix;

	    }

	 public String getNamespaceURI(String prefix){

	   return uri;

	 }

	 public void setNamespaceURI(String uri){

	   this.uri=uri;

	 }

	 public String getPrefix(String uri){

	   return prefix;

	 }

	 public void setPrefix(String prefix){

	   this.prefix=prefix;

	 }

	 public Iterator<?> getPrefixes(String uri){return null;}
}
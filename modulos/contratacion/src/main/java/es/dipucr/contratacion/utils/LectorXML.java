package es.dipucr.contratacion.utils;

import ieci.tdw.ispac.api.errors.ISPACRuleException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import es.dipucr.contratacion.objeto.ValorXML;

public class LectorXML extends DefaultHandler {
	private static final Logger logger = Logger.getLogger(LectorXML.class);

	private XMLReader xr = null;
	private StringBuffer contenido = null;
	private ValorXML valorActual = new ValorXML();
	private ArrayList<ValorXML> nodos = null;
	private String atributo = "";

	public LectorXML(ArrayList<ValorXML> nodos) throws ISPACRuleException{

		try {
			xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(this);
			xr.setErrorHandler(this);
			this.nodos = nodos;
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}

	}

	public void leer(final String archivoXML) throws ISPACRuleException {
		try{
			URL oracle = new URL(archivoXML);
			URLConnection yc = oracle.openConnection();
	        InputStream in = yc.getInputStream();	
			xr.parse(new InputSource(in));
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException(e);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException(e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException(e);
		} 

	}

	public void startDocument() {

		//logger.warn("Comienzo del Documento XML");

	}

	public void endDocument() {
		//logger.warn("Final del Documento XML");

	}

	public void startElement(String uri, String name, String qName, Attributes atts) {
		if ("Row".equals(qName)) {
			valorActual = new ValorXML();
        }
		//logger.warn("tElemento: " + name);
		//Inicializo el contenido a vacío
		contenido = new StringBuffer("");
		for (int i = 0; i < atts.getLength(); i++) {
			this.atributo = atts.getValue(i);
			//logger.warn("ttAtributo: " + atts.getLocalName(i) + " = " + atts.getValue(i));

		}

	}
	
	public void characters(char ch[], int start, int length) throws SAXException {
		contenido.append(new String(ch, start, length));
	}
 


	public void endElement(String uri, String name, String qName) {
		if(qName.equals("SimpleValue")){
			if(this.atributo.equals("code")){
				this.valorActual.setCodeKey(contenido.toString());
			}
			if(this.atributo.equals("nombre")){
				this.valorActual.setNombre(contenido.toString());
			}
			if(this.atributo.equals("name")){
				this.valorActual.setName(contenido.toString());
			}
		}
		if("Row".equals(qName)) {
	    	 this.nodos.add(valorActual);    	    
	    }

		//logger.warn("tFin Elemento: " + name + ", su contenido es "+contenido.toString());

	}

}

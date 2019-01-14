/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import es.msssi.sgm.registropresencial.beans.WebParameter;
import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoSolicitud;

public class XmlUtil {
	private static Logger log = Logger.getLogger(XmlUtil.class.getName());
	
	// Unmarshall
	public static ElementoSolicitud getJavafromXml(byte[] contenido) throws SigmWSException {
		ElementoSolicitud result = null;

	    String SCHEMA_PATH = (String) WebParameter.getEntryParameter(Constants.PATH_SCHEMA);
	    
	    String schemaFile = Constants.PATH_REPO + SCHEMA_PATH+Constants.SCHEMA_VALIDATION_FILENAME;
	    log.debug("Recuperando el esquema: "+schemaFile);
		String solicitud = "";
		
		try {
			File file = new File(schemaFile);
			 
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
		    
			Schema schema = sf.newSchema(file); 			
			ByteArrayInputStream bis = new ByteArrayInputStream(contenido);
			JAXBContext jaxbContext = JAXBContext.newInstance(ElementoSolicitud.class);

//			/** IMPRIME UN XML
		 
	        
//             Marshaller m = jaxbContext.createMarshaller();
//             m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//             m.marshal(result, System.out);			
//			**/
			solicitud = new String(contenido);
			log.debug("Solicitud a convertir en objetos: \n"+solicitud);
						
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			jaxbUnmarshaller.setSchema(schema);
			result = (ElementoSolicitud) jaxbUnmarshaller.unmarshal(new InputStreamReader(bis,
					Constants.UTF_8_ENCODING));
		} catch (UnsupportedEncodingException e) {
			log.error("Solicitud a convertir: " +solicitud);
			throw new SigmWSException("err.jaxb.encoding",e);
		} catch (JAXBException e) {
			log.error("Solicitud a convertir: " +solicitud);
			throw new SigmWSException("err.jaxb.conversion",e);
		} catch (Exception e) {
			log.error("Solicitud a convertir: " +solicitud);
			throw new SigmWSException("err.jaxb.general",e);
		}

		return result;
	}

	public static String validarPeticion(Object objeto) throws SigmWSException {
	
		    String SCHEMA_PATH = (String) WebParameter.getEntryParameter(Constants.PATH_SCHEMA);
		    
		    String schemaFile = Constants.PATH_REPO + SCHEMA_PATH+Constants.SCHEMA_VALIDATION_FILENAME;
		    log.debug("Recuperando el esquema: "+schemaFile);
		    
			JAXBContext jc;
			String peticionValidar = "";
		    
			try {
				File file = new File(schemaFile);
				jc = JAXBContext.newInstance(objeto.getClass());
	
				JAXBSource source = new JAXBSource(jc, objeto);
	
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Schema schema = sf.newSchema(file);
	
				 
		        
					
				StringWriter writer = new StringWriter();
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				m.marshal(objeto, writer);			
				peticionValidar = writer.toString();
				log.debug("Peticion a validar: \n"+peticionValidar);
				
				Validator validator = schema.newValidator();
				validator.validate(source);
	
			} catch (JAXBException e) {
				log.error("Peticion a validar: "+peticionValidar);
				throw new SigmWSException("err.jaxb.general", e);
			} catch (SAXException e) {
				log.error("Peticion a validar: "+peticionValidar);
				throw new SigmWSException("err.validacion.entrada.noValid", new String[]{"No valida el esquema"}, e);
			} catch (IOException e) {
				log.error("Peticion a validar: "+peticionValidar);
				throw new SigmWSException("err.fichero", new String[]{schemaFile}, e);
			}
			
			return peticionValidar;	
		}

	public static String printJAXB(Object objeto) throws JAXBException {
		 
		JAXBContext jc = JAXBContext.newInstance(objeto.getClass());
	 
		StringWriter writer = new StringWriter();
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		 JAXBElement jaxbElement = new JAXBElement(new QName(Constants.QNAME_ROOT), objeto.getClass(), objeto);
         m.marshal(jaxbElement, writer);
		return writer.toString();

	}	
}

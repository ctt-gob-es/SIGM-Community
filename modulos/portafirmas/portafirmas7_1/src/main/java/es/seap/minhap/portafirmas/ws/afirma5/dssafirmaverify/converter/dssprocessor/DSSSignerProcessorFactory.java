/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import es.seap.minhap.portafirmas.utils.XMLUtil;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.impl.BinaryDSSSignerProcessor;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.impl.XMLDSSSignerProcessor;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.impl.XMLEnvelopingDSSSignerProcessor;



public class DSSSignerProcessorFactory {
	
	protected final static Log logger = LogFactory.getLog(DSSSignerProcessorFactory.class);
	
	
	public DSSSignerProcessor getDSSSignerProcessor(byte[] sign) throws DSSSignerProcessorException {
		DSSSignerProcessor processor = null;
		boolean errorParsing = false;
		Document doc = null;
		
		// Comprobamos si es un documento XML
		try {
			doc = XMLUtil.getDOMDocument(sign, false);
		} catch (ParserConfigurationException pe) {
			errorParsing = true;
		} catch (SAXException se) {
			errorParsing = true;
		} catch (IOException ioe) {
			errorParsing = true;
		}
		
		// Si el parseo ha dado error entonces no es un XML
		if (errorParsing) {		
			logger.debug("El documento no es XML por lo que se devuelve un BinaryDSSSignerProcessor");
			processor = new BinaryDSSSignerProcessor ();			
		// Si no ha dado error entonces comprobamos si es XMLEnveloping o si es Detached o Enveloped
		} else {
			
			try {
				javax.xml.xpath.XPath xpath = javax.xml.xpath.XPathFactory.newInstance("http://java.sun.com/jaxp/xpath/dom").newXPath();
			
				if (XMLUtil.isXMLDSigSignature (doc, xpath)) {					
					if (XMLUtil.isXMLDSigEnveloping (doc, xpath)) {
						logger.debug("El documento es XMLDSigEnveloping por lo que se devuelve un XMLEnvelopingDSSSignerProcessor");
						processor = new XMLEnvelopingDSSSignerProcessor ();
					} else {
						processor = new XMLDSSSignerProcessor ();
					} 
				} else {
					logger.info("El documento es un XML, pero no es una Firma XML.");
				}
			} catch (XPathFactoryConfigurationException e) {
				logger.error("Ha ocurrido un error al intentar saber si es Enveloping, Enveloped o Detached ", e);	
				throw new DSSSignerProcessorException ("Ha ocurrido un error al intentar saber si es Enveloping, Enveloped o Detached", e);
			} catch (XPathExpressionException e) {
				logger.error("Ha ocurrido un error al intentar saber si es Enveloping, Enveloped o Detached ", e);
				throw new DSSSignerProcessorException ("Ha ocurrido un error al intentar saber si es Enveloping, Enveloped o Detached", e);
			}
		}
		
		
		return processor;
	}
	
}

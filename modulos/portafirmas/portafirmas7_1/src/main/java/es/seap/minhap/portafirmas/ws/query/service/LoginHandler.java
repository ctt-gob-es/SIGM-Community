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

package es.seap.minhap.portafirmas.ws.query.service;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class LoginHandler implements SOAPHandler<SOAPMessageContext> {
	/**
	 * M&eacute;todo vac&iacute;o, no hace nada
	 */
	public void close(MessageContext messageContext) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @return siempre true
	 */
	public boolean handleFault(SOAPMessageContext messageContext) {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * @return siempre true
	 */
	public boolean handleMessage(SOAPMessageContext messageContext) {
//		Iterator<Node> iteradorNodos;
//		String usuario = null;		
//		String password = null;
//		try {
//			iteradorNodos = (Iterator<Node>) messageContext.getMessage().getSOAPHeader().getChildElements();
//			while (iteradorNodos.hasNext()) {
//				Node nodo = iteradorNodos.next();
//				String nodeName = nodo.getLocalName();
//				if ("userName".equals(nodeName)) {
//					usuario = nodo.getFirstChild().getNodeValue();
//				}
//				if ("password".equals(nodeName)) {
//					password = nodo.getFirstChild().getNodeValue();
//				}				
//			}
//		} catch (SOAPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}

		return true;
	}
	/**
	 * @return un conjunto vac&iacute;o de cabeceras
	 */
	public Set<QName> getHeaders() {
		Set<QName> cabeceras = new HashSet<QName>();
		//cabeceras.add(new QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "userName"));
		//cabeceras.add(new QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", "password"));
		return cabeceras;
	}




}

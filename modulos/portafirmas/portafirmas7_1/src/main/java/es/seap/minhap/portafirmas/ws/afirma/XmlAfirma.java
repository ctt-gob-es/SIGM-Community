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

package es.seap.minhap.portafirmas.ws.afirma;

//import org.apache.axis.utils.XMLUtils;

public class XmlAfirma {

	/*public String generateRequest() {
		String peticion = new String();

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElementNS("urn:oasis:names:tc:dss:1.0:core:schema", "VerifyRequest");
            rootElement.setPrefix("dss");
            document.appendChild(rootElement);
            rootElement.setAttribute("Profile","urn:afirma:dss:1.0:profile:XSS");
            rootElement.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttribute("xsi:SchemaLocation","urn:oasis:names:tc:dss:1.0:core:schema");
            Element nodoPeticion = document.createElementNS("dss","InputDocuments");
            nodoPeticion.appendChild(document.createTextNode("ValidarFirma"));
            Element nodoVersionMsg = document.createElement("versionMsg");
            nodoVersionMsg.appendChild(document.createTextNode("1.0"));
            Element nodoParametros = document.createElement("parametros");
            Element firmaElectronica = document.createElement("firmaElectronica");
            firmaElectronica.appendChild(document.createCDATASection("FIRMA"));
            Element formatoFirma = document.createElement("formatoFirma");
            formatoFirma.appendChild(document.createTextNode("TIPO FIRMA"));
            Element nodoIdAplicacion = document.createElement("idAplicacion");
            nodoIdAplicacion.appendChild(document.createTextNode("ID APLICACION"));
            nodoParametros.appendChild(nodoIdAplicacion);
            nodoParametros.appendChild(firmaElectronica);
            nodoParametros.appendChild(formatoFirma);
            rootElement.appendChild(nodoPeticion);
            rootElement.appendChild(nodoVersionMsg);
            rootElement.appendChild(nodoParametros);
            peticion = XMLUtils.DocumentToString(document); 
            System.out.println("PETICION = " + peticion);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return peticion;
	}*/
}

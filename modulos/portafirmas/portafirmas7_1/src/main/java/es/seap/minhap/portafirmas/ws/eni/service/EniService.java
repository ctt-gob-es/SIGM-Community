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

package es.seap.minhap.portafirmas.ws.eni.service;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.CsvJustificante;
import es.seap.minhap.portafirmas.ws.bean.Signature;
import es.seap.minhap.portafirmas.ws.eni.request.MetadataEni;
import es.seap.minhap.portafirmas.ws.eni.request.TipoMetadatosAdicionales;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

/**
 * Java class for services with ENI.
 * 
 * @author jorge
 */
@WebService(targetNamespace = "urn:juntadeandalucia:cice:pfirma:eni:v2.0", name = "EniService")
public interface EniService {

	/**
	 * Obtains binary content from a sign.
	 * 
	 * @param documentId
	 *            Document id.
	 * @return {@link DataHandler} object with sign binary content.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "downloadSignEniResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:e:request:v2.0", className = "es.seap.minhap.portafirmas.ws.eni.request.DownloadSignEniResponse")
	@RequestWrapper(localName = "downloadSignEni", targetNamespace = "urn:juntadeandalucia:cice:pfirma:eni:request:v2.0", className = "es.seap.minhap.portafirmas.ws.eni.request.DownloadSignEni")
	@WebResult(name = "signatureEni", targetNamespace = "")
	@WebMethod
	public Signature downloadSignEni(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "documentId", targetNamespace = "") String documentId,
			@WebParam(name = "metadatosEni", targetNamespace = "") MetadataEni metadatosEni,
			@WebParam(name = "metadatosAdicionales", targetNamespace = "") TipoMetadatosAdicionales metadatosAdicionales) throws PfirmaException;
	/**
	 * Obtiene el justificante de firma de un documento a partir de su CSV
	 * @param authentication Autenticación del usuario.
	 * @param documentId Identificador del documento.
	 * @return reportDocument Justificante de la firma
	 * @throws PfirmaException Error del Portafirmas.
	 */
	@ResponseWrapper(localName = "justificanteCsvEniResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:eni:request:v2.0", className = "es.seap.minhap.portafirmas.ws.eni.request.JustificanteCsvEniResponse")
	@RequestWrapper(localName = "justificanteCsvEni", targetNamespace = "urn:juntadeandalucia:cice:pfirma:eni:request:v2.0", className = "es.seap.minhap.portafirmas.ws.eni.request.JustificanteCsvEni")
	@WebResult(name = "csvJustificante", targetNamespace = "")
	@WebMethod
	public CsvJustificante justificanteCsvEni(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "documentId", targetNamespace = "") String documentId,
			@WebParam(name = "metadatosEni", targetNamespace = "") MetadataEni metadatosEni,
			@WebParam(name = "metadatosAdicionales", targetNamespace = "") TipoMetadatosAdicionales metadatosAdicionales)throws PfirmaException;
}

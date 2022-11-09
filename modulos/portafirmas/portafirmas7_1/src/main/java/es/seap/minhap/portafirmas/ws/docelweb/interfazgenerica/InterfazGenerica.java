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

package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.AnularSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.AnularSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ComenzarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ComenzarSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarDocumentoElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarDocumentoResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarEstadoSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarEstadoSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ObjectFactory;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarDocumentoElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarDocumentoResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarSolicitudResponseElement;

/**
 * @author MINHAP
 * InterfazGenerica web service definition
 */
@WebService(targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/", name = "InterfazGenerica")
@XmlSeeAlso({ ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface InterfazGenerica {

	@WebResult(name = "comenzarSolicitudResponseElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/", partName = "parameters")
	@WebMethod(action = "http://ip2.docelweb.wsInterfazGenerica//comenzarSolicitud")
	public ComenzarSolicitudResponseElement comenzarSolicitud(@WebParam(partName = "parameters", name = "comenzarSolicitudElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/") ComenzarSolicitudElement parameters);

	@WebResult(name = "registrarSolicitudResponseElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/", partName = "parameters")
	@WebMethod(action = "http://ip2.docelweb.wsInterfazGenerica//registrarSolicitud")
	public RegistrarSolicitudResponseElement registrarSolicitud(@WebParam(partName = "parameters", name = "registrarSolicitudElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/") RegistrarSolicitudElement parameters);

	@WebResult(name = "consultarEstadoSolicitudResponseElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/", partName = "parameters")
	@WebMethod(action = "http://ip2.docelweb.wsInterfazGenerica//consultarEstadoSolicitud")
	public ConsultarEstadoSolicitudResponseElement consultarEstadoSolicitud(@WebParam(partName = "parameters", name = "consultarEstadoSolicitudElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/") ConsultarEstadoSolicitudElement parameters);

	@WebResult(name = "registrarDocumentoResponseElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/", partName = "parameters")
	@WebMethod(action = "http://ip2.docelweb.wsInterfazGenerica//registrarDocumento")
	public RegistrarDocumentoResponseElement registrarDocumento(@WebParam(partName = "parameters", name = "registrarDocumentoElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/") RegistrarDocumentoElement parameters);

	@WebResult(name = "anularSolicitudResponseElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/", partName = "parameters")
	@WebMethod(action = "http://ip2.docelweb.wsInterfazGenerica//anularSolicitud")
	public AnularSolicitudResponseElement anularSolicitud(@WebParam(partName = "parameters", name = "anularSolicitudElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/") AnularSolicitudElement parameters);

	@WebResult(name = "consultarDocumentoResponseElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/", partName = "parameters")
	@WebMethod(action = "http://ip2.docelweb.wsInterfazGenerica//consultarDocumento")
	public ConsultarDocumentoResponseElement consultarDocumento(@WebParam(partName = "parameters", name = "consultarDocumentoElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/") ConsultarDocumentoElement parameters);

	@WebResult(name = "consultarSolicitudResponseElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/", partName = "parameters")
	@WebMethod(action = "http://ip2.docelweb.wsInterfazGenerica//consultarSolicitud")
	public ConsultarSolicitudResponseElement consultarSolicitud(@WebParam(partName = "parameters", name = "consultarSolicitudElement", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/types/") ConsultarSolicitudElement parameters);
}

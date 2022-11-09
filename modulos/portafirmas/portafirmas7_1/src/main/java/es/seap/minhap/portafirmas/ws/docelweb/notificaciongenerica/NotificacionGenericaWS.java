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

package es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

/**
 * @author MINHAP
 * NotificacionGenericaWS web service definition
 */
@WebService(targetNamespace = "http://ip2/docelweb/proceso/notificacionGenerica/NotificacionGenericaWSImpl.wsdl", name = "NotificacionGenericaWS")
@SOAPBinding(style = Style.RPC, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
public interface NotificacionGenericaWS {
	
	@WebResult(name = "return")
	@WebMethod(action = "http://ip2/docelweb/proceso/notificacionGenerica/NotificacionGenericaWSImpl.wsdl/notificarDevolucionSolicitud")
	public String notificarDevolucionSolicitud(
			@WebParam(name = "aplicacion") String aplicacion, 
			@WebParam(name = "idSolicitud") Long idSolicitud, 
			@WebParam(name = "estSolicitud") String estSolicitud);

}

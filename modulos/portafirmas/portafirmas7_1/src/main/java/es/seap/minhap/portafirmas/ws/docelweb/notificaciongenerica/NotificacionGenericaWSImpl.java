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

import javax.jws.WebService;

import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.interceptor.Fault;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ComenzarSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business.NotificacionGenericaServerBO;

/**
 * @author MINHAP
 * NotificacionGenericaWS web service implementation for SOAP 1.1
 */
@Service("NotificacionGenericaWSImpl")
@WebService(name = "NotificacionGenericaWS", serviceName = "NotificacionGenericaWS", portName = "NotificacionGenericaWSImplBindingPort", targetNamespace = "http://ip2/docelweb/proceso/notificacionGenerica/NotificacionGenericaWSImpl.wsdl", wsdlLocation = "WSDL/NotificacionGenericaWS.wsdl", endpointInterface = "es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.NotificacionGenericaWS")
public class NotificacionGenericaWSImpl implements NotificacionGenericaWS {

	private Logger LOG = Logger.getLogger(NotificacionGenericaWSImpl.class);
	
	@Autowired
	private NotificacionGenericaServerBO notificacionGenericaServerBO;

	public String notificarDevolucionSolicitud(String aplicacion, Long idSolicitud, String estSolicitud) throws SoapFault {
		try {
			return notificacionGenericaServerBO.notificarDevolucionSolicitud(aplicacion, idSolicitud, estSolicitud);
		} catch (Exception ex) {
			LOG.error("Se ha producido un error notificando la devolución de una solicitud", ex);
			if (ex instanceof SoapFault) {
				throw (SoapFault) ex;
			}
			throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + ex.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, ex);
		}
	}

}

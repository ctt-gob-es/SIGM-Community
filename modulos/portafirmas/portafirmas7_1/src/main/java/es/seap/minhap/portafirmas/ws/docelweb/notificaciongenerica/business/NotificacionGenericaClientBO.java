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

/**
 * Business object that implements the InterfazGenerica web service client logic methods. 
 * where the client side portafirmas system.
 */
package es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business;

import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSpfirmaDTO;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.NotificacionGenericaWS;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business.exception.NotificacionGenericaClientException;
import es.seap.minhap.portafirmas.ws.docelweb.wss.DocelwebClientManager;

/**
 * @author MINHAP
 * Business object that implements the InterfazGenerica web service client logic methods. 
 * where the client side portafirmas system.
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class NotificacionGenericaClientBO {


	@Resource(name="interfazGenericaProperties")
	private Properties interfazGenericaProperties;

	
	/**
	 * The logger object.
	 */
	private Logger log = Logger.getLogger(NotificacionGenericaServerBO.class);

	/**
	 * The DOCEL client manager.
	 */
	@Autowired
	private DocelwebClientManager clientManager;

	/**
	 * Call the NotificacionGEnerica.notificarDevolucionSolicitud web service method with given params config.
	 * @param pfDocelRequestToNotify The portafirmas system docel request to return notify
	 * @return 
	 * @throws NotificacionGenericaClientException
	 */
	@Transactional(readOnly=false)
	public String notificarDevolucionSolicitud(PfDocelwebRequestSpfirmaDTO pfDocelRequestToNotify, String textReject) throws NotificacionGenericaClientException {	
		if (pfDocelRequestToNotify != null) {			
			try {				
				String estado = pfDocelRequestToNotify.getdState();		
				if (textReject != null && !textReject.isEmpty()) {
					estado = pfDocelRequestToNotify.getdState() + DocelwebConstants.STATE_DELIMITER + textReject;
				}
				
				NotificacionGenericaWS notificacionGenericaProxy = clientManager.getNotificacionGenericaWSClient(pfDocelRequestToNotify.getPortafirmas());
				return notificacionGenericaProxy.notificarDevolucionSolicitud(
						interfazGenericaProperties.getProperty(DocelwebConstants.MI_PORTAFIRMAS), pfDocelRequestToNotify.getPrimaryKey(), estado);
				
			} catch (Exception e) {
				log.error("Invoking NotificacionGenerica.notificarDevolucionSolicitud fail", e);
				throw new NotificacionGenericaClientException("Se ha producido un error invocando el WS NotificacionGenerica.notificarDevolucionSolicitud", e);
			}
		} else {
			log.error("Invoking NotificacionGenerica.notificarDevolucionSolicitud fail. La solicitud InterfazGenerica a notificar es nula");
			throw new NotificacionGenericaClientException("Se ha producido un error invocando el WS NotificacionGenerica.notificarDevolucionSolicitud. La solicitud InterfazGenerica a notificar es nula");
		}
	}

}

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

package es.seap.minhap.portafirmas.business.ws;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.utils.notice.service.NoticeService;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class MailToAdminBO {

	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private NoticeBO noticeBO;
	
	@Autowired
	private NoticeService noticeService;

	Logger log = Logger.getLogger(EEUtilMiscBO.class);
	
	//EEUtilUtilFirmaBO
	public void sendMailToAdmin (String evento, String csv, Throwable e) throws EeutilException {
		sendMailToAdminGeneric (null, null, evento, null, csv, e);
	}
	
	//EEUtilMiscBO, EEUtilOperFirmaBO, EEUtilVisBO
	public void sendMailToAdmin (String evento, Throwable e) throws EeutilException{
		sendMailToAdminGeneric (null, null, evento, null, "", e);
	}
	
	public void sendMailToAdmin (String tipoNotice, String tipoExcepcion, String evento, String entorno, String csv, Throwable e) throws EeutilException {
		sendMailToAdminGeneric (tipoNotice, tipoExcepcion, evento, entorno, csv, e);
	}
	
	private void sendMailToAdminGeneric (String tipoNotice, String tipoExcepcion, String evento, String entorno, String csv, Throwable e) throws EeutilException {
		String entornoGeneric  = (entorno!=null && !"".equals(entorno))?entorno:applicationBO.getEnvironment().getTvalue();
		String tipoNoticeGeneric = (tipoNotice!=null && !"".equals(tipoNotice))?tipoNotice:Constants.EMAIL_NOTICE;
		String tipoExcepcionGeneric = (tipoExcepcion!=null && !"".equals(tipoExcepcion))?tipoExcepcion:Constants.NOTICE_EEUTIL_EXCEPTION;
		
		//Tenemos que filtrar las excepciones y enviar solo correo cuando existan problemas de conectividad
		if (e  instanceof javax.xml.ws.WebServiceException || 
			e.getCause() instanceof javax.xml.ws.WebServiceException ||
			e  instanceof java.util.concurrent.TimeoutException ||
			e.getCause() instanceof java.util.concurrent.TimeoutException ||
			e  instanceof java.rmi.RemoteException ||
			e.getCause() instanceof java.rmi.RemoteException ){
			if (noticeBO.isAdminNoticeEnabled()) {
				try {
					noticeService.doNoticeEeutilException(tipoNoticeGeneric, tipoExcepcionGeneric, evento, entornoGeneric, csv);
				} catch (NoticeException t) {
					log.error("No se puede enviar la notificación a los administradores: " + t.getMessage() + t);
					throw new EeutilException ("No se puede enviar la notificación a los administradores: ", t);
				}
			}
		}
	}

}



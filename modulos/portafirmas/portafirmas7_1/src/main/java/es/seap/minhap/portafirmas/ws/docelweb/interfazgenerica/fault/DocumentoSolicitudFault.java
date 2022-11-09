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

package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.ws.WebFault;

import org.apache.cxf.binding.soap.SoapFault;

@WebFault(name = "DocumentoSolicitudFault")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "erorrCode" })
@XmlRootElement(name = "documentoSolicitudFault")
public class DocumentoSolicitudFault extends SoapFault {

	/**
	 * The serial version OID.
	 */
	private static final long serialVersionUID = -5186540452087990867L;

	@XmlElement(required = true)
	private String erorrCode;

	public DocumentoSolicitudFault(String message, QName errorSide, String errorCode) {
		super(message, errorSide);
		this.erorrCode = errorCode;
	}

	public DocumentoSolicitudFault(String message, QName errorSide, String errorCode, Throwable cause) {
		super(message, cause, errorSide);
		this.erorrCode = errorCode;
	}

	public void setErorrCode(String erorrCode) {
		this.erorrCode = erorrCode;
	}

	public String getErrorCode() {
		return erorrCode;
	}

}

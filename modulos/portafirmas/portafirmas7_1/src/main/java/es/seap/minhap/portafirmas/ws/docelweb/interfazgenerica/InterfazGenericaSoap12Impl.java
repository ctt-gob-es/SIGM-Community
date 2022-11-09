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

import javax.jws.WebService;

import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.interceptor.Fault;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.InterfazGenericaServerBO;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.AnularSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ComenzarSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ConsultarDocumentoFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ConsultarEstadoSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.ConsultarSolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.RegistrarDocumentoFault;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.RegistrarSolicitudFault;
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
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarDocumentoElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarDocumentoResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarSolicitudResponseElement;

/**
 * @author MINHAP
 * IntrfazGenerica web service implementation for SOAP 1.2
 */
@Service("InterfazGenericaSoap12Impl")
@WebService(name = "InterfazGenerica", serviceName = "InterfazGenerica", portName = "InterfazGenericaSoap12HttpPort", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/", wsdlLocation = "WSDL/InterfazGenerica.wsdl", endpointInterface = "es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica")
public class InterfazGenericaSoap12Impl implements InterfazGenerica {

	private Logger LOG = Logger.getLogger(InterfazGenericaSoap12Impl.class);
	
	@Autowired
	private InterfazGenericaServerBO interfazGenericaServerBO;

	/* (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica#comenzarSolicitud(ComenzarSolicitudElement  parameters )*
	 */
	public ComenzarSolicitudResponseElement comenzarSolicitud(ComenzarSolicitudElement parameters) throws SoapFault {
		LOG.info("Executing operation comenzarSolicitud");
		try {
			ComenzarSolicitudResponseElement response = interfazGenericaServerBO.comenzarSolicitud(parameters);
			return response;
		} catch (Exception ex) {
			LOG.error("Se ha producido un error comenzando una nueva solicitud", ex);
			if (ex instanceof SoapFault) {
				throw (SoapFault) ex;
			}
			throw new ComenzarSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + ex.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica#registres.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.ement  parameters )*
	 */
	public RegistrarSolicitudResponseElement registrarSolicitud(RegistrarSolicitudElement parameters) throws SoapFault {
		LOG.info("Executing operation registrarSolicitud");
		try {
			RegistrarSolicitudResponseElement response = interfazGenericaServerBO.registrarSolicitud(parameters);
			return response;
		} catch (Exception ex) {
			LOG.error("Se ha producido un error registrando una solicitud", ex);
			if (ex instanceof SoapFault) {
				throw (SoapFault) ex;
			}
			throw new RegistrarSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + ex.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica#consultarEstadoSolicitud(ConsultarEstadoSolicitudElement  parameters )*
	 */
	public ConsultarEstadoSolicitudResponseElement consultarEstadoSolicitud(ConsultarEstadoSolicitudElement parameters) throws SoapFault {
		LOG.info("Executing operation consultarEstadoSolicitud");
		try {
			ConsultarEstadoSolicitudResponseElement response = interfazGenericaServerBO.consultarEstadoSolicitud(parameters);
			return response;
		} catch (Exception ex) {
			LOG.error("Se ha producido un error consultando el estado de una solicitud", ex);
			if (ex instanceof SoapFault) {
				throw (SoapFault) ex;
			}
			throw new ConsultarEstadoSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + ex.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica#registrarDocumento(RegistrarDocumentoElement  parameters )*
	 */
	public RegistrarDocumentoResponseElement registrarDocumento(RegistrarDocumentoElement parameters) throws SoapFault {
		LOG.info("Executing operation registrarDocumento");
		try {
			RegistrarDocumentoResponseElement response = interfazGenericaServerBO.registrarDocumento(parameters);
			return response;
		} catch (Exception ex) {
			LOG.error("Se ha producido un error registrando un documento", ex);
			if (ex instanceof SoapFault) {
				throw (SoapFault) ex;
			}
			throw new RegistrarDocumentoFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + ex.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica#anularSolicitud(AnularSolicitudElement  parameters )*
	 */
	public AnularSolicitudResponseElement anularSolicitud(AnularSolicitudElement parameters) throws SoapFault {
		LOG.info("Executing operation anularSolicitud");
		try {
			AnularSolicitudResponseElement response = interfazGenericaServerBO.anularSolicitud(parameters);
			return response;
		} catch (Exception ex) {
			LOG.error("Se ha producido un error anulando una solicitud", ex);
			if (ex instanceof SoapFault) {
				throw (SoapFault) ex;
			}
			throw new AnularSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + ex.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica#consultarDocumento(ConsultarDocumentoElement  parameters )*
	 */
	public ConsultarDocumentoResponseElement consultarDocumento(ConsultarDocumentoElement parameters) throws SoapFault {
		LOG.info("Executing operation consultarDocumento");
		try {
			ConsultarDocumentoResponseElement response = interfazGenericaServerBO.consultarDocumento(parameters);
			return response;
		} catch (Exception ex) {
			LOG.error("Se ha producido un error consultando un documento", ex);
			if (ex instanceof SoapFault) {
				throw (SoapFault) ex;
			}
			throw new ConsultarDocumentoFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + ex.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, ex);
		}
	}

	/* (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica#consultarSolicitud(ConsultarSolicitudElement  parameters )*
	 */
	public ConsultarSolicitudResponseElement consultarSolicitud(ConsultarSolicitudElement parameters) throws SoapFault {
		LOG.info("Executing operation consultarSolicitud");
		try {
			ConsultarSolicitudResponseElement response = interfazGenericaServerBO.consultarSolicitud(parameters);
			return response;
		} catch (Exception ex) {
			LOG.error("Se ha producido un error consultando una solicitud", ex);
			if (ex instanceof SoapFault) {
				throw (SoapFault) ex;
			}
			throw new ConsultarSolicitudFault(DocelwebConstants.CODE_E00_UNKNOWN + DocelwebConstants.FAULT_CODE_SEPARATOR + ex.getMessage(), Fault.FAULT_CODE_SERVER, DocelwebConstants.CODE_E00_UNKNOWN, ex);
		}
	}

}

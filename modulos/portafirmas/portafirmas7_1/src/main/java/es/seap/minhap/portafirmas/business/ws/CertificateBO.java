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

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.beans.ValidateCertificateResponse;
import es.seap.minhap.portafirmas.exceptions.CertificateException;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.clientmanager.ConfigManagerBO;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;
import es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.RespuestaValidarCertificado;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.InfoCertificado;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.InfoDetalladaCertificado;

/**
 * @author domingo
 * 
 * El objetivo principal de esta clase es obtener obtener información del paquete
 * eeutil-oper-firma y devolverla adaptada a los tipos de eeutil-util-firma
 *
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class CertificateBO {

	@Autowired
	private Afirma5BO afirma5BO;
	
	@Autowired
	private ConfigManagerBO configManagerBO;

	@Autowired
	private EEUtilOperFirmaBO eeUtilOperFirmaBO;
	
	@Autowired
	DateComponent dateComponent;
	
	@Autowired
	UtilComponent util;
	
	Logger log = Logger.getLogger(SignatoryBO.class);

	/**
	 * Valida el certificado del usuario, bien contra Afirma o bien contra EEutil.
	 * 
	 * @param certificado
	 * @return
	 * @throws CertificateException
	 * @throws SocketTimeoutException 
	 */
	public ValidateCertificateResponse validarCertificado (String certificado) throws CertificateException {
		ValidateCertificateResponse retorno = null;
		try {
			if (afirma5BO.estaActivoValidarCertificado()) {
				RespuestaValidarCertificado respuestaAfirma = afirma5BO.queryDetailsFromCertificateAfirma5(certificado, 
							configManagerBO.getDefaultConfigurationId ());
					// Se construye la respuesta a partir del resultado obtenido de @Firma
					retorno = new ValidateCertificateResponse();
					retorno.setValido(respuestaAfirma.isValido());
					retorno.setError(respuestaAfirma.isError());
					if (respuestaAfirma.isValido()) {
						retorno.setNifCif(respuestaAfirma.getNifCif());
						retorno.setNumeroSerie(respuestaAfirma.getNumeroSerie());
						retorno.setSeudonimo(respuestaAfirma.isSeudonimo());
						
						//fecha de validez del certificado
						retorno.setDateValidSign(dateComponent.stringToDate(respuestaAfirma.getFechaValidez(), Afirma5Constantes.AFIRMA_CAMPO_FECHA_VALIDEZ_CERTIFICADO_FORMATO));
					} else {
						retorno.setMensaje(respuestaAfirma.getMensaje());
					}
								
			} else  if (eeUtilOperFirmaBO.checkValidarCertificado()) {

				InfoCertificado resultadoEEUtil = eeUtilOperFirmaBO.getInfoCertificado(certificado);
				
				// Se construye la respuesta a partir del resultado obtenido de EEUtil
				retorno = new ValidateCertificateResponse();
				retorno.setValido(resultadoEEUtil.isValidado());
				retorno.setError(false);
				if (resultadoEEUtil.isValidado()) {
					retorno.setNifCif(resultadoEEUtil.getIdUsuario());
					retorno.setNumeroSerie(resultadoEEUtil.getNumeroSerie());
					retorno.setSeudonimo(esSeudonimo(resultadoEEUtil));
					
					//fecha de validez del certificado
					retorno.setDateValidSign(getValidDateFromCertificateDetails(resultadoEEUtil.getInformacionDetallada()));
				} else {
					retorno.setMensaje(resultadoEEUtil.getDetalleValidacion());
				}
				
			}
		} catch (EeutilException eu) {
			log.error("Error al validar certificado por EEUtil: ", eu);
			throw new CertificateException(eu.getMessage(), CertificateException.COD_003);
		} catch (Afirma5Exception af5) {
			log.error("Error al validar certificado por @Firma: ", af5);
			throw new CertificateException(af5.getMessage() + af5, CertificateException.COD_004);
		} catch(SocketTimeoutException e){
			if(e.getMessage().startsWith("El servicio ValidarCertificado no está disponible")){
				log.error(CertificateException.MESSAGE_001 + ", ", e);
				throw new CertificateException(e.getMessage() + e, CertificateException.COD_001);
			}else if(e.getMessage().startsWith("La operación validarCertificado del servicio ValidarCertificado")){
				log.error(CertificateException.MESSAGE_002 + ", ", e);
				throw new CertificateException(e.getMessage() + e, CertificateException.COD_002);
			}else{
				log.error("Se ha producido un timeout en la validación del certificado por @Firma, ", e);
				throw new CertificateException(e.getMessage() + e, CertificateException.COD_000);
			}
		}
		return retorno;
	}
	
	private boolean esSeudonimo(InfoCertificado resultadoEEUtil) {
		boolean esSeudonimo = false;
		List<InfoDetalladaCertificado> atributos = resultadoEEUtil.getInformacionDetallada();
		for (Iterator<InfoDetalladaCertificado> it = atributos.iterator(); it.hasNext();) {
			InfoDetalladaCertificado infoDetalladaCertificado = (InfoDetalladaCertificado) it.next();
			if(Afirma5Constantes.AFIRMA_CAMPO_SEUDONIMO.equals(infoDetalladaCertificado.getClass())) {
				esSeudonimo = true;
			}
		}
		return esSeudonimo;
	}

	public Date getFechaValidezCertificado(String certificado) throws CertificateException {
		ValidateCertificateResponse respuestaValidarCertificado = this.validarCertificado(certificado);
		return respuestaValidarCertificado.getDateValidSign();
	}

	private Date getValidDateFromCertificateDetails(List<InfoDetalladaCertificado> data) {
		Date retorno = null;
		if (util.isNotEmpty(data)) {
			for (InfoDetalladaCertificado detalle : data) {
				if (detalle.getClave().equalsIgnoreCase(Afirma5Constantes.AFIRMA_CAMPO_FECHA_VALIDEZ_CERTIFICADO)) {
					retorno = dateComponent.stringToDate(detalle.getValor(), Afirma5Constantes.AFIRMA_CAMPO_FECHA_VALIDEZ_CERTIFICADO_FORMATO);
				}
			}
		}
		return retorno;
	}

}

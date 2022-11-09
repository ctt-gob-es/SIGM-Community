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

package es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.seap.minhap.portafirmas.business.Base64;
import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;
import es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.RespuestaValidarCertificado;
import es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida.Respuesta.ResultadoProcesamiento;

public class Afirma5ValidarCertificadoConverter {

	private static es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.ObjectFactory objFactValidarCertificado = new es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.ObjectFactory ();
	
	public static es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeEntrada crearPeticionValidarCertificado (String certificado, Map<String, String> mapConfig) throws Afirma5Exception{
		
		es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeEntrada mensajeEntrada = objFactValidarCertificado.createMensajeEntrada();
		mensajeEntrada.setVersionMsg(Afirma5Constantes.AFIRMA_VERSION_MENSAJE);
		mensajeEntrada.setPeticion(Afirma5Constantes.AFIRMA_VALIDAR_CERTIFICADO_OPERACION);
		
		es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeEntrada.Parametros parametros = objFactValidarCertificado.createMensajeEntradaParametros();
		try {
			parametros.setCertificado(Base64.decode(certificado));
		} catch (IOException e) {
			throw new Afirma5Exception ("No se puede decodificar el certificado" , e);
		}
		
		parametros.setIdAplicacion(mapConfig.get(Afirma5Constantes.FIRMA_APLICACION));		
		parametros.setModoValidacion(Afirma5Constantes.AFIRMA_NIVEL_VALIDACION);
		parametros.setObtenerInfo(true);
		
		mensajeEntrada.setParametros(parametros);
		
		return mensajeEntrada;
		
	}
	
	public static RespuestaValidarCertificado mensajeSalidaToRespuestaValidarCertificado (es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida mensajeSalida) throws Afirma5Exception {
		
		RespuestaValidarCertificado respuesta = new RespuestaValidarCertificado ();
		
		// La validación no se ha realizado.
		if (mensajeSalida.getRespuesta().getExcepcion() != null) {
			respuesta.setError(true);
			respuesta.setMensaje("Ha ocurrido un error en la peticion al servicio validarCertificado");
			respuesta.setMensajeAmpliado(construyeDetalleRespuesta (mensajeSalida.getRespuesta().getExcepcion()));
		
		// La validación se ha realizado.
		} else {
			respuesta.setError(false);
			// Certificado válido
			if ("0".equalsIgnoreCase(mensajeSalida.getRespuesta().getResultadoProcesamiento().getResultadoValidacion().getResultado())) {
				respuesta.setValido(true);
				respuesta.setMensaje(mensajeSalida.getRespuesta().getResultadoProcesamiento().getResultadoValidacion().getDescripcion());
				respuesta.setMensajeAmpliado(construyeDetalleRespuesta (mensajeSalida.getRespuesta().getResultadoProcesamiento().getResultadoValidacion().getDescripcion()));
			// Certificado no válido.
			} else {
				respuesta.setValido(false);
				respuesta.setMensaje(construyeDetalleRespuesta(mensajeSalida.getRespuesta().getResultadoProcesamiento()));
				respuesta.setMensajeAmpliado(construyeDetalleRespuesta(mensajeSalida.getRespuesta().getResultadoProcesamiento()));
			}
		}
		
		if (!respuesta.isError()) {
			Map<String, String> infoMap = infoCertificadoToMap (mensajeSalida.getRespuesta().getResultadoProcesamiento().getInfoCertificado());
			if (infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_NIF_RESPONSABLE) != null) {
				respuesta.setNifCif(infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_NIF_RESPONSABLE));
			} else if (infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_NIF_CIF) != null) {
				respuesta.setNifCif(infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_NIF_CIF));
			} else if (infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_SEUDONIMO) != null) {
				respuesta.setSeudonimo(true);
			} else {
				throw new Afirma5Exception ("No se ha podido extraer el NIF de la respuesta del servicio ValidarCertificado");
			}
			
			if (infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_NUMERO_SERIE) != null) {
				respuesta.setNumeroSerie(infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_NUMERO_SERIE));
			} else {
				throw new Afirma5Exception ("No se ha podido extraer el número de serie del certificado de la respuesta del servicio ValidarCertificado");
			}
			
			if (infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_FECHA_VALIDEZ_CERTIFICADO) != null) {
				respuesta.setFechaValidez(infoMap.get(Afirma5Constantes.AFIRMA_CAMPO_FECHA_VALIDEZ_CERTIFICADO));
			}
		}
		
		return respuesta;
	}
	
	
	private static Map<String, String> infoCertificadoToMap (es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.InfoCertificadoInfo infoCertificado) {
		Map<String, String> mapa = new HashMap<String, String> ();
		List<es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.InfoCertificadoInfo.Campo> campos = infoCertificado.getCampo();
		for (es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.InfoCertificadoInfo.Campo campo : campos) {
			mapa.put(campo.getIdCampo(), campo.getValorCampo());
		}
		return mapa;
	}
	
	private static String construyeDetalleRespuesta (Object obj) {
		final String ln = System.getProperty("line.separator");
		
		StringBuffer mensaje = new StringBuffer("");
		if (obj instanceof es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida.Respuesta.Excepcion) {			
			es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida.Respuesta.Excepcion e = (es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida.Respuesta.Excepcion) obj;
			mensaje.append("CodigoError: " + e.getCodigoError() + ln);
			mensaje.append("Descripcion: " + e.getDescripcion() + ln);
			mensaje.append("ExcepcionAsociada: " + e.getExcepcionAsociada() + ln);
		/*} else if (obj instanceof afirmaws.services.nativos.model.validarfirma.Excepcion) {
			afirmaws.services.nativos.model.validarfirma.Excepcion e = (afirmaws.services.nativos.model.validarfirma.Excepcion) obj;
			mensaje.append("CodigoError: " + e.getCodigoError() + ln);
			mensaje.append("Descripcion: " + e.getDescripcion() + ln);
			mensaje.append("ExcepcionAsociada: " + e.getExcepcionAsociada() + ln);
		} else if (obj instanceof ValidacionFirmaElectronica) {
			ValidacionFirmaElectronica val = (ValidacionFirmaElectronica) obj;
			mensaje.append("Proceso: " + val.getProceso() + ln);
			mensaje.append("Detalle: " + val.getDetalle() + ln);
			mensaje.append("Conclusion: " + val.getConclusion() + ln);
			*/
		} else if (obj instanceof es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida.Respuesta.ResultadoProcesamiento) {
			es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida.Respuesta.ResultadoProcesamiento res = (ResultadoProcesamiento) obj;
			mensaje.append("Resultado: " + res.getResultadoValidacion().getResultado()+ ln);
			mensaje.append("Descripcion: " + res.getResultadoValidacion().getDescripcion()+ "." + ln);
			if (res.getResultadoValidacion().getValidacionSimple() != null) {
				mensaje.append(res.getResultadoValidacion().getValidacionSimple().getDescResultado());
			}			
		} else if (obj instanceof String) {
			String s = (String) obj;
			mensaje.append(s + ln);		
		}
		
		return mensaje.toString();
	}
	
}

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

package es.seap.minhap.portafirmas.ws.afirma5.validarfirma.converter;

import java.util.List;
import java.util.Map;


import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.RespuestaValidarFirma;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Excepcion;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.MensajeEntrada;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.MensajeSalida;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.ValidacionFirmaElectronica;

public class Afirma5ValidarFirmaConverter {
	
	private static es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.ObjectFactory objFactValidarFirma = new es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.ObjectFactory ();
	
	public static MensajeEntrada crearPeticionValidarFirma (byte[] sign, 
															byte[] document, 
															byte[] hash, 
															String algoritmoHash, 
															String tipoFirma, 
															Map<String, String> mapConfig) throws Afirma5Exception {
		
		es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.MensajeEntrada mensajeEntrada = objFactValidarFirma.createMensajeEntrada();
		mensajeEntrada.setVersionMsg(Afirma5Constantes.AFIRMA_VERSION_MENSAJE);
		mensajeEntrada.setPeticion(Afirma5Constantes.AFIRMA_VALIDAR_FIRMA_OPERACION);
		
		
		es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Parametros parametros = objFactValidarFirma.createParametros();
		
		parametros.setIdAplicacion(mapConfig.get(Afirma5Constantes.FIRMA_APLICACION));
		parametros.setFirmaElectronica (sign);
		parametros.setFormatoFirma(tipoFirma);
		parametros.setHash(hash);
		parametros.setAlgoritmoHash(algoritmoHash);
		parametros.setDatos(document);
		
		mensajeEntrada.setParametros(parametros);
		
		return mensajeEntrada;
		
	}
	
	
	public static RespuestaValidarFirma mensajeSalidaToRespuestaValidarFirma (MensajeSalida mensajeSalida) throws Afirma5Exception {
		
		RespuestaValidarFirma respuesta = new RespuestaValidarFirma ();
		// Si la respuesta no es nula, se ha podido validar la firma y comprobamos el resultado
		if (mensajeSalida.getRespuesta() != null && mensajeSalida.getRespuesta().getRespuesta() != null) {
			
			respuesta.setError(false);
			respuesta.setValido(mensajeSalida.getRespuesta().getRespuesta().isEstado());
			
			// Firma válida
			if (respuesta.isValido()) {
				
				respuesta.setMensaje("FIRMA VALIDA");
				respuesta.setMensajeAmpliado("FIRMA VALIDA");
		
			// Firma no válida, obtenemos motivo .
			} else {
			
				List<Object> listaRespuestas = mensajeSalida.getRespuesta().getRespuesta().getDescripcion().getContent();
				String mensaje = "";
				for (Object obj : listaRespuestas) {
					ValidacionFirmaElectronica validacion = (ValidacionFirmaElectronica) obj;
					mensaje += construyeDetalleRespuesta (validacion);
				
				}
				
				respuesta.setMensaje("FIRMA NO VALIDA");
				respuesta.setMensajeAmpliado (mensaje);
				
			}
			
		// La firma no se ha podido validar.
		} else {
			respuesta.setError(true);
			String mensaje = construyeDetalleRespuesta(mensajeSalida.getRespuesta().getExcepcion());
			respuesta.setMensaje(mensaje);
			respuesta.setMensajeAmpliado(mensaje);
		}	
		
		return respuesta;
	}
	
	public static String construyeDetalleRespuesta (Object obj) {
		StringBuffer mensaje = new StringBuffer("");
		String ln = System.getProperty("line.separator");
	
		if (obj instanceof Excepcion) {			
			Excepcion e = (Excepcion) obj;
			mensaje.append("CodigoError: " + e.getCodigoError() + ln);
			mensaje.append("Descripcion: " + e.getDescripcion() + ln);
			mensaje.append("ExcepcionAsociada: " + e.getExcepcionAsociada() + ln);
		} else if (obj instanceof ValidacionFirmaElectronica) {
				ValidacionFirmaElectronica val = (ValidacionFirmaElectronica) obj;
				mensaje.append("Proceso: " + val.getProceso() + ln);
				mensaje.append("Detalle: " + val.getDetalle() + ln);
				mensaje.append("Conclusion: " + val.getConclusion() + ln);
		}
		return mensaje.toString();
	}
}

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

package es.seap.minhap.portafirmas.ws.pfserviciows;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import es.seap.minhap.portafirmas.ws.legacy.bean.DocumentoWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.EntregaWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.PeticionWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.UsuarioWS;
import es.seap.minhap.portafirmas.ws.legacy.service.Servicio;

/**
 * Clase que implementa los servicios de integraci&oacute;n de aplicaciones externas.
 * @author hugo
 * @see es.seap.minhap.portafirmas.ws.legacy.service.Servicio
 */
@SuppressWarnings("deprecation")
@WebService(endpointInterface="es.seap.minhap.portafirmas.ws.pfserviciows.PortafirmaService",
		serviceName="PfServicioWS", 
		targetNamespace="http://DefaultNamespace",
		name="http://DefaultNamespace")
@BindingType(SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class PortafirmaServiceImpl implements PortafirmaService {

	public long actualizarPeticion(String chash, String aplicacion,
			boolean firmaCascada, boolean firmaOrdenada, boolean notificaEmail,
			Calendar fInicio, Calendar fCaducidad, String remitenteNombre,
			String remitenteEmail, String remitenteMovil, String dReferencia,
			String dAsunto, BigDecimal nPrioridad, String usuario,
			String petChash, String tTexto) {

		Servicio servicio = new Servicio();
		return servicio.actualizarPeticion(chash, aplicacion, firmaCascada,
										   firmaOrdenada, notificaEmail, false, false,
										   fInicio, fCaducidad, remitenteNombre,
										   remitenteEmail, remitenteMovil, dReferencia,
										   dAsunto, nPrioridad, usuario, petChash, tTexto);
	}

	public PeticionWS consultarPeticion(String peticion) {
		Servicio servicio = new Servicio();
		PeticionWS pet = servicio.consultarPeticion(peticion);
		pet.setLNOTIFICAAVISO(null);
		pet.setLNOTIFICAMOVIL(null);
		return pet;
	}

	public EntregaWS[] consultarEntregasPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.consultarEntregasPeticion(peticion);
	}

	public String consultarObservacionesEntrega(String hashDoc, String dni) {
		Servicio servicio = new Servicio();
		return servicio.consultarObservacionesEntrega(hashDoc, dni);
	}

	public long entregarPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.entregarPeticion(peticion);
	}

	public long entregarPeticion(String peticion, String parametrosDeEntradaComunes, String parametrosLogica) {
		Servicio servicio = new Servicio();
		return servicio.entregarPeticion(peticion);
	}

	public String[] consultarPreLogicasPeticion(String peticion) {
		// TODO Por implementar (preguntar en MPR)
		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
	}

	public String[] consultarPostLogicasPeticion(String peticion) {
		// TODO Por implementar (preguntar en MPR)
		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
	}

	public String[] consultarPreLogicasAplicacion(String aplicacion) {
		// TODO Por implementar (preguntar en MPR)
		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
	}

	public String[] consultarPostLogicasAplicacion(String aplicacion) {
		// TODO Por implementar (preguntar en MPR)
		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
	}

	public boolean entregadaPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.entregadaPeticion(peticion);
	}

	public long eliminarPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.eliminarPeticion(peticion);
	}

	public long insertarDetalleFirmaPeticion(String formatoFirma, String politicaFirma, String peticion) {
		// TODO Por implementar
		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
	}

	public String consultarFormatoFirmaPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.consultarFormatoFirmaPeticion(peticion);
	}

	public String consultarPoliticaFirmaPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.consultarFormatoFirmaPeticion(peticion);
	}

	public long eliminarDetalleFirmaPeticion(String peticion) {
		// TODO Por implementar
//		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
		return -2;
	}

	public String[] consultarFormatosFirma() {
		// TODO Por implementar
//		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
		String[] res = {"Funci&oacute;n no implementada"};
		return res;
	}

	public String[] consultarPoliticasFirma() {
		// TODO Por implementar
//		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
		String[] res = {"Funci&oacute;n no implementada"};
		return res;
	}

	public long insertarDestinatarioPeticion(String dni, String peticion) {
		Servicio servicio = new Servicio();
		return servicio.insertarDestinatarioPeticion(dni, peticion);
	}

	public UsuarioWS[] consultarDestinatariosPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.consultarDestinatariosPeticion(peticion);
	}

	public long cambiarDestinatario(String hashPeticion, String dniOrigen, String dniDestino) {
		Servicio servicio = new Servicio();
		return servicio.cambiarDestinatario(hashPeticion, dniOrigen, dniDestino, "", "", "", "N");
	}

	public long eliminarDestinatarioPeticion(String dni, String peticion) {
		Servicio servicio = new Servicio();
		return servicio.eliminarDestinatarioPeticion(dni, peticion);
	}

	public String insertarDocumentoPeticion(String peticion, String tipoDoc, String nombreDoc, String tipoMime, byte[] documento) {
		Servicio servicio = new Servicio();
		return servicio.insertarDocumentoPeticion(peticion, tipoDoc, nombreDoc, tipoMime, documento);
	}

	public DocumentoWS[] consultarDocumentosPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.consultarDocumentosPeticion(peticion);
	}

	public long eliminarDocumentoPeticion(String documento) {
		Servicio servicio = new Servicio();
		return servicio.eliminarDocumentoPeticion(documento);
	}

	public long insertarNotificacionPeticion(String peticion, String estado) {
		Servicio servicio = new Servicio();
		return servicio.insertarNotificacionPeticion(peticion, estado);
	}

	public String[] consultarNotificacionesPeticion(String peticion) {
		Servicio servicio = new Servicio();
		return servicio.consultarNotificacionesPeticion(peticion);
	}

	public long eliminarNotificacionPeticion(String peticion, String estado) {
		Servicio servicio = new Servicio();
		return servicio.eliminarNotificacionPeticion(peticion, estado);
	}

	public String descargarDocumento(String documento) {
		Servicio servicio = new Servicio();
		return servicio.descargarDocumento(documento);
	}

	public String descargarFirma(String documento, String idTransaccion) {
		Servicio servicio = new Servicio();
		return servicio.descargarPKCS7(documento, idTransaccion);
	}

	public long documentoAccion(String hashDoc, String estado, String accion, String tipo) {
		Servicio servicio = new Servicio();
		return servicio.documentoAccion(hashDoc, estado, accion, tipo);
	}

	public String[] valoresEstados() {
		Servicio servicio = new Servicio();
		return servicio.valoresEstados();
	}

	public String[] valoresTiposDocumento() {
		Servicio servicio = new Servicio();
		return servicio.valoresTiposDocumento();
	}

	public EntregaWS[] consultarEntregasTransaccion(String idTransaccion) {
		// TODO Por implementar (preguntar en MPR)
		throw new UnsupportedOperationException("Operaci&oacute;n no implementada");
	}

	public String insertarPeticion(String usuario) {
		Servicio servicio = new Servicio();
		return servicio.insertarPeticion(usuario);
	}

}

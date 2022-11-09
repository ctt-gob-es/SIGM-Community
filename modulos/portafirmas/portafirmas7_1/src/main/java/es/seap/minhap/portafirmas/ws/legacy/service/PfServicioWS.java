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

package es.seap.minhap.portafirmas.ws.legacy.service;

import java.math.BigDecimal;
import java.util.Calendar;

import es.seap.minhap.portafirmas.ws.legacy.bean.DocumentoWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.EntregaWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.PeticionWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.UsuarioWS;

/**
 * Java class for pfirma v1 services definition.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@SuppressWarnings("deprecation")
public class PfServicioWS {

	/**
	 * Sole constructor.
	 */
	public PfServicioWS() {
	}

	/**
	 * Tests if request is sent.
	 * 
	 * @param peticion
	 *            Request id.
	 * @return true if request was sent; false otherwise.
	 */
	public boolean entregadaPeticion(String peticion) {

		Servicio servicio = new Servicio();
		return servicio.entregadaPeticion(peticion);
	}

	/**
	 * Sends a request.
	 * 
	 * @param peticion
	 *            Request id.
	 * @return 0 if request was sent correctly; another value otherwise.
	 */
	public long entregarPeticion(String peticion) {

		Servicio servicio = new Servicio();
		return servicio.entregarPeticion(peticion);
	}

	/**
	 * Inserts a new request.
	 * 
	 * @param usuario
	 *            Request remmitter.
	 * @return Request id if inserction was correct; null otherwise
	 */
	public String insertarPeticion(String usuario) {

		Servicio servicio = new Servicio();
		return servicio.insertarPeticion(usuario);
	}

	/**
	 * Updates request fields.
	 * 
	 * @param chash
	 *            Request id.
	 * @param aplicacion
	 *            Application which request will be associated.
	 * @param firmaCascada
	 *            Boolean for cascade sign (true) or paralell sign (false).
	 * @param firmaOrdenada
	 *            Boolean to define if document sign is limited in a concrete
	 *            order (true); false otherwise.
	 * @param notificaEmail
	 *            Boolean to activate remitter email notification (true); false
	 *            to deactivate.
	 * @param fInicio
	 *            Start date since request documents can be signed.
	 * @param fCaducidad
	 *            Limit date to sign request (only for additional info).
	 * @param remitenteNombre
	 *            Remmitter name.
	 * @param remitenteEmail
	 *            Remmitter email.
	 * @param remitenteMovil
	 *            Remmitter mobile number.
	 * @param dReferencia
	 *            Request reference. Generally, expedient associated to request
	 *            in the client application to add information.
	 * @param dAsunto
	 *            Request subject to describe it.
	 * @param nPrioridad
	 *            Request priority.
	 * @param usuario
	 *            Portafirmas user to link request to a portafirmas user.
	 * @param petChash
	 *            Request id for request nesting.
	 * @param tTexto
	 *            Text as attached note for additional info.
	 * @return 0 if everything is correct, another value if an error occurs.
	 */
	public long actualizarPeticion(String chash, String aplicacion,
			boolean firmaCascada, boolean firmaOrdenada, boolean notificaEmail,
			Calendar fInicio, Calendar fCaducidad, String remitenteNombre, String remitenteEmail,
			String remitenteMovil, String dReferencia, String dAsunto,
			BigDecimal nPrioridad, String usuario, String petChash,
			String tTexto) {

		Servicio servicio = new Servicio();
		// ESTA IMPLEMENTACIÓN COPIA LA DE LA VERSIÓN DE MPR
//		return servicio.actualizarPeticion(chash, aplicacion, firmaCascada,
//				firmaOrdenada, notificaEmail, notificaMovil, notificaAviso,
//				fInicio, fCaducidad, remitenteNombre, remitenteEmail,
//				remitenteMovil, dReferencia, dAsunto, nPrioridad, usuario,
//				petChash, tTexto);
		return servicio.actualizarPeticion(chash, aplicacion, firmaCascada,
				firmaOrdenada, notificaEmail, false, false,
				fInicio, fCaducidad, remitenteNombre, remitenteEmail,
				remitenteMovil, dReferencia, dAsunto, nPrioridad, usuario,
				petChash, tTexto);

	}

	/**
	 * Queries request data.
	 * 
	 * @param peticion
	 *            Request id.
	 * @return PeticionWS object with request data.
	 */

	public PeticionWS consultarPeticion(String peticion) {

		Servicio servicio = new Servicio();
		return servicio.consultarPeticion(peticion);
	}

	/**
	 * Obtains every request receivers.
	 * 
	 * @param peticion
	 *            Request id.
	 * @return UsuariosWS object array with receivers data; null if an error
	 *         occurs.
	 */
	public UsuarioWS[] consultarDestinatariosPeticion(String peticion) {

		Servicio servicio = new Servicio();
		return servicio.consultarDestinatariosPeticion(peticion);
	}

	/**
	 * Inserts a receiver into request. If receiver does not exist, it is not
	 * created; an error code is returned.
	 * 
	 * @param dni
	 *            Receiver id.
	 * @param peticion
	 *            Request id.
	 * @return 0 if everything is correct; another value if an error occurs.
	 */
	public long insertarDestinatarioPeticion(String dni, String peticion) {

		Servicio servicio = new Servicio();
		return servicio.insertarDestinatarioPeticion(dni, peticion);
	}

	/**
	 * Deletes a receiver from a request (only if request is not sent or if is
	 * sent, if receiver has not signed yet. If every receivers are deleted from
	 * a request, it is opened again and it can be deleted.
	 * 
	 * @param dni
	 *            User id.
	 * @param peticion
	 *            Request id.
	 * @return 0 if everything is correct; another value if an error occurs.
	 */
	public long eliminarDestinatarioPeticion(String dni, String peticion) {

		Servicio servicio = new Servicio();
		return servicio.eliminarDestinatarioPeticion(dni, peticion);
	}

	/**
	 * Queries user data from Portafirmas.
	 * 
	 * @param dni
	 *            User id.
	 * @return UsuarioWS object with user data; null if user was not found.
	 */
	public UsuarioWS consultarUsuario(String dni) {

		Servicio servicio = new Servicio();
		return servicio.consultarUsuario(dni);
	}

	/**
	 * Obtains every document contained in a request.
	 * 
	 * @param peticion
	 *            Request id.
	 * @return DocumentosWS object array, empty if an error occurs or request
	 *         has not documents.
	 */
	public DocumentoWS[] consultarDocumentosPeticion(String peticion) {

		Servicio servicio = new Servicio();
		return servicio.consultarDocumentosPeticion(peticion);
	}

	/**
	 * Obtains document binary content from a request.
	 * 
	 * @param documento
	 *            Document id.
	 * @return byte array with document binary content, empty if an error occurs
	 *         or request has not content.
	 */
	public byte[] descargarDocumento(String documento) {

//		Servicio servicio = new Servicio();
//		return servicio.descargarDocumento(documento);
		// TODO Implementar correctamente
		return null;
	}

	/**
	 * Deletes a document from a request. Only if request was not sent yet.
	 * 
	 * @param documento
	 *            Document id.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long eliminarDocumentoPeticion(String documento) {

		Servicio servicio = new Servicio();
		return servicio.eliminarDocumentoPeticion(documento);
	}

	/**
	 * Attachs a document to a request.
	 * 
	 * @param peticion
	 *            Request id.
	 * @param tipoDoc
	 *            Document type.
	 * @param nombreDoc
	 *            File name which will be attached.
	 * @param tipoMime
	 *            Myme type of attached document.
	 * @param documento
	 *            Binary document of attached document.
	 * @return Document id if everything was correct; null if an error occurs.
	 */
	public String insertarDocumentoPeticion(String peticion, String tipoDoc,
			String nombreDoc, String tipoMime, byte[] documento) {

		Servicio servicio = new Servicio();
		return servicio.insertarDocumentoPeticion(peticion, tipoDoc, nombreDoc,
				tipoMime, documento);
	}

	/**
	 * Obtains list of the document types existing in the system.
	 * 
	 * @return String array with valid document types; empty array if an error
	 *         occurs.
	 */
	public String[] valoresTiposDocumento() {

		Servicio servicio = new Servicio();
		return servicio.valoresTiposDocumento();
	}

	/**
	 * Obtains notification status for a request.
	 * 
	 * @param peticion
	 *            Request id.
	 * @return String array with states where receiver will be notified in case
	 *         of user asked for it.
	 */
	public String[] consultarNotificacionesPeticion(String peticion) {

		Servicio servicio = new Servicio();
		return servicio.consultarNotificacionesPeticion(peticion);
	}

	/**
	 * Adds a new notice status for request.
	 * 
	 * @param peticion
	 *            Request id.
	 * @param estado
	 *            State where remitter will be notified.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long insertarNotificacionPeticion(String peticion, String estado) {

		Servicio servicio = new Servicio();
		return servicio.insertarNotificacionPeticion(peticion, estado);
	}

	/**
	 * Deletes request notices state.
	 * 
	 * @param peticion
	 *            Request id.
	 * @param estado
	 *            Notice state to delete.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long eliminarNotificacionPeticion(String peticion, String estado) {

		Servicio servicio = new Servicio();
		return servicio.eliminarNotificacionPeticion(peticion, estado);
	}

	/**
	 * Obtains delivers of a request with it data.
	 * 
	 * @param peticion
	 *            Request id.
	 * @return EntregaWS object array with data from request delivers; empty if
	 *         an error occurs.
	 */
	public EntregaWS[] consultarEntregasPeticion(String peticion) {

		Servicio servicio = new Servicio();
		return servicio.consultarEntregasPeticion(peticion);
	}

	/**
	 * Obtains all possible states of a request.
	 * 
	 * @return String array with possible states of a request.
	 */
	public String[] valoresEstados() {

		Servicio servicio = new Servicio();
		return servicio.valoresEstados();
	}

	/**
	 * Obtains PKCS7 from a document sign.
	 * 
	 * @param documento
	 *            Document id.
	 * @param idTransaccion
	 *            Sign transaction id at "@firma".
	 * @return byte array with sign receipt.
	 */
	public byte[] descargarPKCS7(String documento, String idTransaccion) {

//		Servicio servicio = new Servicio();
//		return servicio.descargarPKCS7(documento, idTransaccion);
		// TODO Implementar correctamente
		return null;
	}

	/**
	 * Adds an action to a document state change.
	 * 
	 * @param hashDoc
	 *            Document id.
	 * @param estado
	 *            State where action is invoked.
	 * @param accion
	 *            PLSQ procedure or URL invoked as action. If an error occurs,
	 *            error code "ORA-XX" or http code as an event associated to the
	 *            document.
	 * @param tipo
	 *            Value which identifies the action type. PLSQL for procedure
	 *            calls or WEB for urk invokations.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long documentoAccion(String hashDoc, String estado, String accion,
			String tipo) {

		Servicio servicio = new Servicio();
		return servicio.documentoAccion(hashDoc, estado, accion, tipo);
	}

	/**
	 * Change a request receiver from another one.
	 * 
	 * @param hashPeticion
	 *            Request id.
	 * @param dniOrigen
	 *            Source receiver id.
	 * @param dniDestino
	 *            Destination receiver id.
	 * @param nombre
	 *            Destinataion receiver name.
	 * @param apellido1
	 *            Destinataion receiver first surname.
	 * @param apellido2
	 *            Destinataion receiver secon surname.
	 * @param nuevo
	 *            String who represents if destination receiver is new. 'S' for
	 *            yes, 'N' for not.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long cambiarDestinatario(String hashPeticion, String dniOrigen,
			String dniDestino) {

		Servicio servicio = new Servicio();
		// ESTA IMPLEMENTACIÓN COPIA LA DE LA VERSIÓN DE MPR
//		return servicio.cambiarDestinatario(hashPeticion, dniOrigen,
//				dniDestino, nombre, apellido1, apellido2, nuevo);
		return servicio.cambiarDestinatario(hashPeticion, dniOrigen,
				dniDestino, "", "", "", "N");
	}

	/**
	 * Obtains deliver comments associated to document and user received in
	 * parameters.
	 * 
	 * @param hashDoc
	 *            Document id.
	 * @param dni
	 *            Receiver id.
	 * @return Deliver comments.
	 */
	public String consultarObservacionesEntrega(String hashDoc, String dni) {

		Servicio servicio = new Servicio();
		return servicio.consultarObservacionesEntrega(hashDoc, dni);
	}

	// Nuevos m�todos de la versi�n 1.2

	/**
	 * Inserts a receiver into a request. If receiver does not exists, it is
	 * created with received data.
	 * 
	 * @param usuario
	 *            User to insert as receiver.
	 * @param peticion
	 *            Request id.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long insertarDestinatarioPet(UsuarioWS usuario, String peticion) {

		Servicio servicio = new Servicio();
		return servicio.insertarDestinatarioPeticion(usuario, peticion);
	}

	/**
	 * Deletes a receiver from a request (only if request is not sent or if is
	 * sent, if receiver has not signed yet. If every receivers are deleted from
	 * a request, it is opened again and it can be deleted.
	 * 
	 * @param usuario
	 *            User to delete as receiver.
	 * @param peticion
	 *            Request id.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long eliminarDestinatarioPet(UsuarioWS usuario, String peticion) {

		Servicio servicio = new Servicio();
		return servicio.eliminarDestinatarioPeticion(usuario, peticion);
	}

	/**
	 * Changes a receiver in a request.
	 * 
	 * @param hashPeticion
	 *            Request id.
	 * @param usuarioOrigen
	 *            Receiver who has to be replaced.
	 * @param usuarioDestino
	 *            Receiver which will replace the source receiver.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long cambiarDestinatarioPet(String hashPeticion,
			UsuarioWS usuarioOrigen, UsuarioWS usuarioDestino, String nuevo) {

		Servicio servicio = new Servicio();
		return servicio.cambiarDestinatario(hashPeticion, usuarioOrigen,
				usuarioDestino, nuevo);
	}

	/**
	 * Deletes a request. It only can be done if request is not sent.
	 * 
	 * @param hashPeticion
	 *            Request id.
	 * @return 0 if everything is correct; another if an error occurs.
	 */
	public long eliminarPeticion(String hashPeticion) {

		Servicio servicio = new Servicio();
		return servicio.eliminarPeticion(hashPeticion);
	}

	/**
	 * Obtains all users.
	 * 
	 * @return UsuarioWS object array containing all users.
	 */
	public UsuarioWS[] obtenerListaUsuarios() {

		Servicio servicio = new Servicio();
		return servicio.obtenerListaUsuarios();
	}

	/**
	 * Returns sign transaction id from server when block sign is done.
	 * 
	 * @param hashDocumento
	 *            Document id.
	 * @return Transaction id.
	 */
	public double obtenerIdTransaccion(String hashDocumento) {

		Servicio servicio = new Servicio();
		return servicio.obtenerIdTransaccion(hashDocumento);
	}

	/**
	 * M&eacute;todo que inserta el detalle de firma de la petici&oacute;n (Formato y Pol&iacute;tica de firma)
	 * @param formatoFirma Descripci&oacute;n del Formato de Firma de la petici&oacute;n
	 * @param politicaFirma Descripci&oacute;n de la Pol&iacute;tica de Firma de la petici&oacute;n
	 * @param peticion Identificador de petici&oacute;n
	 * @return devuelve 0 si todo va bien, cualquier otro valor en caso contrario
	 */
	public long insertarDetalleFirmaPeticion(String formatoFirma, String politicaFirma, String peticion) {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
	 * M&eacute;todo que obtiene la descripci&oacute;n del Formato de Firma de la petici&oacute;n
	 * @param peticion Identificador de petici&oacute;n
	 * @return devuelve el Formato de la Peticion si todo se realiza correctamente
	 */
	public String consultarFormatoFirmaPeticion(String peticion) {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
	 * M&eacute;todo que obtiene la descripci&oacute;n de la Pol&iacute;tica de Firma de la petici&oacute;n
	 * @param peticion Identificador de petici&oacute;n
	 * @return devuelve la Pol&iacute;tica de la Peticion si todo se realiza correctamente
	 */
	public String consultarPoliticaFirmaPeticion(String peticion) {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
	 * M&eacute;todo que elimina el detalle de Firma de la petici&oacute;n
	 * @param peticion Identificador de petici&oacute;n
	 * @return devuelve 0 si todo va bien, cualquier otro valor en caso contrario
	 */
	public long eliminarDetalleFirmaPeticion(String peticion) {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
	 * M&eacute;todo que obtiene la descripci&oacute;n de los Formatos de Firma disponibles
	 * @return devuelve un Array de String con los Formatos de Firma
	 */
	public String[] consultarFormatosFirma() {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
	 * M&eacute;todo que obtiene la descripci&oacute;n de las Politicas de Firma disponibles
	 * @return devuelve un Array de String con las Politicas de Firma 
	 */
	public String[] consultarPoliticasFirma() {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
     * M&eacute;todo para consultar los nombres de las prel&oacute;gicas
     * que se aplican a la petici&oacute;n
     * @param peticion Identificador de la petici&oacute;n
     * @return Devuelve los nombres de las l&oacute;gicas
     */
	public String[] consultarPreLogicasPeticion(String peticion) {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
     * M&eacute;todo para consultar los nombres de las postl&oacute;gicas
     * que se aplican a la petici&oacute;n
     * @param peticion Identificador de la petici&oacute;n
     * @return Devuelve los nombres de las l&oacute;gicas
     */
	public String[] consultarPostLogicasPeticion(String peticion) {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
     * M&eacute;todo para consultar los nombres de las prel&oacute;gicas
     * que se aplican a las peticiones de la aplicaci&oacute;n indicada
     * @param aplicacion Identificador de la aplicaci&oacute;n
     * @return Devuelve los nombres de las l&oacute;gicas
     */
	public String[] consultarPreLogicasAplicacion(String aplicacion) {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}

	/**
     * M&eacute;todo para consultar los nombres de las postl&oacute;gicas
     * que se aplican a las peticiones de la aplicaci&oacute;n indicada
     * @param aplicacion Identificador de la aplicaci&oacute;n
     * @return Devuelve los nombres de las l&oacute;gicas
     */
	public String[] consultarPostLogicasAplicacion(String aplicacion) {
		throw new UnsupportedOperationException("Est&eacute; m&eacute;todo no ha sido implementado");
	}
}

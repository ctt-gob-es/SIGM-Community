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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import es.seap.minhap.portafirmas.ws.legacy.bean.DocumentoWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.EntregaWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.PeticionWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.UsuarioWS;


@WebService(name="PfServicioWS", targetNamespace="http://DefaultNamespace")
@SOAPBinding(style=Style.RPC, use=Use.LITERAL, parameterStyle=ParameterStyle.WRAPPED)
public interface PortafirmaService {

	@WebResult(name="insertarPeticionReturn")
	public String insertarPeticion(@WebParam(name="usuario") String usuario);

	@WebResult(name="actualizarPeticionReturn")
//	@RequestWrapper(localName = "actualizarPeticion", targetNamespace = "http://DefaultNamespace", className = "es.seap.minhap.portafirmas.ws.pfserviciows.ActualizarPeticionRequest")
	public long actualizarPeticion(@WebParam(name="chash") String chash,
								   @WebParam(name="aplicacion") String aplicacion,
								   @WebParam(name="firmaCascada") boolean firmaCascada,
								   @WebParam(name="firmaOrdenada") boolean firmaOrdenada,
								   @WebParam(name="notificaEmail") boolean notificaEmail,
								   @WebParam(name="fInicio") Calendar fInicio,
								   @WebParam(name="fCaducidad") Calendar fCaducidad,
								   @WebParam(name="remitenteNombre") String remitenteNombre,
								   @WebParam(name="remitenteEmail") String remitenteEmail,
								   @WebParam(name="remitenteMovil") String remitenteMovil,
								   @WebParam(name="dReferencia") String dReferencia,
								   @WebParam(name="dAsunto") String dAsunto,
								   @WebParam(name="nPrioridad") BigDecimal nPrioridad,
								   @WebParam(name="usuario") String usuario,
								   @WebParam(name="petChash") String petChash,
								   @WebParam(name="tTexto") String tTexto);

	@WebResult(name="consultarPeticionReturn")
	public PeticionWS consultarPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="consultarEntregasPeticionReturn")
	public EntregaWS[] consultarEntregasPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="consultarObservacionesEntregaReturn")
	public String consultarObservacionesEntrega(@WebParam(name="hasDoc") String hashDoc,
            									@WebParam(name="dni") String dni);

	@WebResult(name="entregarPeticionReturn")
	@WebMethod(operationName="entregarPeticion")
	public long entregarPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="entregarPeticionReturn")
	@WebMethod(operationName="entregarPeticionParametros")
	public long entregarPeticion(@WebParam(name="peticion") String peticion,
								 @WebParam(name="parametrosDeEntradaComunes") String parametrosDeEntradaComunes,
								 @WebParam(name="parametrosLogica") String parametrosLogica);

	@WebResult(name="consultarPreLogicasPeticionReturn")
	public String[] consultarPreLogicasPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="consultarPostLogicasPeticionReturn")
	public String[] consultarPostLogicasPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="consultarPreLogicasAplicacionReturn")
	public String[] consultarPreLogicasAplicacion(@WebParam(name="aplicacion") String aplicacion);

	@WebResult(name="consultarPostLogicasAplicacionReturn")
	public String[] consultarPostLogicasAplicacion(@WebParam(name="aplicacion") String aplicacion);

	@WebResult(name="entregadaPeticionReturn")
	public boolean entregadaPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="eliminarPeticionReturn")
	public long eliminarPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="insertarDetalleFirmaPeticionReturn")
	public long insertarDetalleFirmaPeticion(@WebParam(name="formatoFirma") String formatoFirma,
								             @WebParam(name="politicaFirma")  String politicaFirma,
								             @WebParam(name="peticion") String peticion);

	@WebResult(name="consultarFormatoFirmaPeticionReturn")
	public String consultarFormatoFirmaPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="consultarPoliticaFirmaPeticionReturn")
	public String consultarPoliticaFirmaPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="eliminarDetalleFirmaPeticionReturn")
	public long eliminarDetalleFirmaPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="consultarFormatosFirmaReturn")
	public String[] consultarFormatosFirma();

	@WebResult(name="consultarPoliticasFirmaReturn")
	public String[] consultarPoliticasFirma();

	@WebResult(name="insertarDestinatarioPeticionReturn")
	public long insertarDestinatarioPeticion(@WebParam(name="dni") String dni,
											 @WebParam(name="peticion") String peticion);

	@WebResult(name="consultarDestinatariosPeticionReturn")
	public UsuarioWS[] consultarDestinatariosPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="cambiarDestinatarioReturn")
	public long cambiarDestinatario(@WebParam(name="hashPeticion") String hashPeticion,
									@WebParam(name="dniOrigen") String dniOrigen,
									@WebParam(name="dniDestino") String dniDestino);

	@WebResult(name="eliminarDestinatarioPeticionReturn")
	public long eliminarDestinatarioPeticion(@WebParam(name="dni") String dni,
											 @WebParam(name="peticion") String peticion);

	@WebResult(name="insertarDocumentoPeticionReturn")
	public String insertarDocumentoPeticion(@WebParam(name="peticion")  String peticion, 
											@WebParam(name="tipoDoc")   String tipoDoc,
											@WebParam(name="nombreDoc") String nombreDoc,
											@WebParam(name="tipoMime")  String tipoMime,
											@WebParam(name="documento") byte[] documento);

	@WebResult(name="consultarDocumentosPeticionReturn")
	public DocumentoWS[] consultarDocumentosPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="eliminarDocumentoPeticionReturn")
	public long eliminarDocumentoPeticion(@WebParam(name="documento") String documento);

	@WebResult(name="insertarNotificacionPeticionReturn")
	public long insertarNotificacionPeticion(@WebParam(name="peticion") String peticion,
											 @WebParam(name="estado") String estado);

	@WebResult(name="consultarNotificacionesPeticionReturn")
	public String[] consultarNotificacionesPeticion(@WebParam(name="peticion") String peticion);

	@WebResult(name="eliminarNotificacionPeticionReturn")
	public long eliminarNotificacionPeticion(@WebParam(name="peticion") String peticion,
											 @WebParam(name="estado") String estado);

	@WebResult(name="descargarDocumentoReturn")
	public String descargarDocumento(@WebParam(name="documento") String documento);

	@WebResult(name="descargarFirmaReturn")
	public String descargarFirma(@WebParam(name="documento") String documento,
								 @WebParam(name="idTransaccion") String idTransaccion);

	@WebResult(name="documentoAccionReturn")
	public long documentoAccion(@WebParam(name="hashDoc") String hashDoc,
								@WebParam(name="estado") String estado,
								@WebParam(name="accion") String accion,
								@WebParam(name="tipo") String tipo);

	@WebResult(name="valoresEstadosReturn")
	public String[] valoresEstados();

	@WebResult(name="valoresTiposDocumentoReturn")
	public String[] valoresTiposDocumento();

	@WebResult(name="consultarEntregasTransaccionReturn")
	public EntregaWS[] consultarEntregasTransaccion(@WebParam(name="idTransaccion") String idTransaccion);
}

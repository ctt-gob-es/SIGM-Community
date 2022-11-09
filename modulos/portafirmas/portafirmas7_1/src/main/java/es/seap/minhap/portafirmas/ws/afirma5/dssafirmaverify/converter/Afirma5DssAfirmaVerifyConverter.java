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

package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter;

import java.util.List;
import java.util.Map;

import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.RespuestaAmpliarFirma;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ResponseBaseType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.VerifyRequest;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.DSSSignerProcessor;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.DSSSignerProcessorException;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.DSSSignerProcessorFactory;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.DSSUtil;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.constantes.DSSResultConstantes;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;

public class Afirma5DssAfirmaVerifyConverter {
	
	private DSSSignerProcessor processor;
	
	public Afirma5DssAfirmaVerifyConverter () {
		
	}
	
	public void inicializar (byte[] firma) throws Afirma5Exception {
		// Si la firma es PADES y se quiere a�adir sello de tiempo devolvemos un error //
		/*if (esPADES(firma) && AfirmaConstantes.UPGRADE_TIMESTAMP.equals(configuracion.getFormatoAmpliacion())) {
			EstadoInfo estadoInfo = new EstadoInfo("ERROR", CodigosError.COD_0008, CodigosError.MSJ_0008);
			throw new InSideException (estadoInfo.getDescripcion(), estadoInfo);
		}*/
		DSSSignerProcessorFactory factory = new DSSSignerProcessorFactory ();
		
		// Obtenemos el objeto que construir� la petici�n a enviar a los WS de Afirma.
		try {
			processor = factory.getDSSSignerProcessor(firma);
		} catch (DSSSignerProcessorException e) {
			throw new Afirma5Exception ("No se ha podido recuperar el DSSSignerProcessor adecuado para la firma", e);
		}
		
		if (processor == null) {
			throw new Afirma5Exception ("No se ha recuperado el DSSSignerProcessor adecuado para la firma");			
		}
		
	}
	
		
	public VerifyRequest crearPeticionAmpliarFirma (byte[] firma,														    
														   List<byte[]>certificados, 
														   boolean ignorarPeriodoGracia, 
														   String formatoAmpliacion, 
														   Map<String, String> params) throws Afirma5Exception {
		
		
		
		if (processor == null) {
			throw new Afirma5Exception ("No se ha inicializado y DSSSignerProcessor es nulo");			
		}
		
		
		VerifyRequest vrequest = processor.buildVerifyRequestToUpgrade(params.get(Afirma5Constantes.FIRMA_APLICACION), 
																	   firma, 
																	   certificados, 
																	   ignorarPeriodoGracia, 
																	   formatoAmpliacion);
		
		return vrequest;
	}
	
	
	public RespuestaAmpliarFirma responseTypeToResultadoAmpliarFirma (ResponseBaseType verifyResponse) {
		
		RespuestaAmpliarFirma respuesta = new RespuestaAmpliarFirma ();
		
		// Comprobamos si se ha realizado la operaci�n correctamente
		if (verifyResponse.getResult().getResultMajor().contentEquals(DSSResultConstantes.DSS_MAJOR_REQUESTERERROR) &&
			verifyResponse.getResult().getResultMinor().contentEquals(DSSResultConstantes.DSS_MINOR_INCOMPLETEUPGRADEOP)) {
			respuesta.setError(true);
			respuesta.setMensaje("El servicio DSS de Afirma no ha podido realizar operacion de ampliacion de firma requerida");
			respuesta.setMensajeAmpliado("Los servicios de Afirma responden IncompleteUpgradeOperation, El servicio DSS de Afirma no ha podido realizar operacion de ampliacion de firma requerida");			

		// Comprobamos si el tipo de amplicaci�n introducido es correcto
		} else if (verifyResponse.getResult().getResultMajor().contentEquals(DSSResultConstantes.DSS_MAJOR_REQUESTERERROR) &&
			verifyResponse.getResult().getResultMinor().contentEquals(DSSResultConstantes.DSS_MINOR_INCORRECTRETURNUPDATEDSIGNTYPE)) {
			respuesta.setError(true);
			respuesta.setMensaje("El servicio DSS de Afirma no reconoce el formato de firma al que se desea ampliar");
			respuesta.setMensajeAmpliado("Los servicios de Afirma responden IncompleteReturnUpdatedSignatureType. El servicio DSS de Afirma no reconoce el formato de firma al que se desea ampliar");
		
		// Comprobamos si el tipo de amplicaci�n ha sido introducido
		} else if (verifyResponse.getResult().getResultMajor().contentEquals(DSSResultConstantes.DSS_MAJOR_REQUESTERERROR) &&
			verifyResponse.getResult().getResultMinor().contentEquals(DSSResultConstantes.DSS_MINOR_UPDATEDSIGNTYPENOTPROVIDED)) {
			respuesta.setError(true);
			respuesta.setMensaje("El servicio DSS de Afirma no encuentra el formato de firma al que se desea ampliar");
			respuesta.setMensajeAmpliado("Los servicios de Afirma responden UpdatedSignatureTypeNotProvided. El servicio DSS de Afirma no encuentra el formato de firma al que se desea ampliar");
			
		// Admitimos la respuesta "�xito", "Pendiente de periodo de gracia" y "warning". Para el resto lanzamos una excepci�n"
		} else if (!verifyResponse.getResult().getResultMajor().contentEquals(DSSResultConstantes.DSS_MAJOR_SUCCESS) &&
				   !verifyResponse.getResult().getResultMajor().contentEquals(DSSResultConstantes.DSS_MAJOR_PENDING) &&
				   !verifyResponse.getResult().getResultMajor().contentEquals(DSSResultConstantes.DSS_MAJOR_WARNING)) {
			
			respuesta.setError(true);
			respuesta.setMensaje("Codigo de respuesta de Afirma inesperado");
			respuesta.setMensajeAmpliado(DSSUtil.getInfoResult(verifyResponse.getResult()));
			
		} else {
			respuesta.setError(false);
			respuesta.setFirmaAmpliada(processor.getUpgradedSignature(verifyResponse));
			respuesta.setMensaje("Firma amplicada correctamente");
			respuesta.setMensajeAmpliado("Firma amplicada correctamente");
		}
		
		return respuesta;
		
		
	}
	

}

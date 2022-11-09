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

//package es.seap.minhap.portafirmas.ws.legacy.service;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
//import es.seap.minhap.portafirmas.actions.ApplicationVO;
//import es.seap.minhap.portafirmas.business.ws.legacy.remotesign.RemoteSignWSLegacyBO;
//import es.seap.minhap.portafirmas.ws.legacy.exception.FirmaRemotaException;
//
///**
// * Java class for remote sign v1 services definition.
// * 
// * @author Guadaltel
// * @version 2.0.0
// * @since 2.0.0
// */
//public class FirmaRemotaWS implements ApplicationContextAware {
//
//	@Autowired
//	private ApplicationVO applicationVO;
//
//	private ApplicationContext applicationContext;
//
//	public void setApplicationContext(ApplicationContext applicationContext)
//			throws BeansException {
//		this.applicationContext = applicationContext;
//	}
//
//	/**
//	 * Sole constructor.
//	 */
//	public FirmaRemotaWS() {
//	}
//
//	/**
//	 * Method to start sign process.
//	 * 
//	 * @param hashDoc
//	 *            Hash of the document which will be signed.
//	 * @param idUsuario
//	 *            Id of the user which will sign the document.
//	 * @return Data which will be signed.
//	 * @throws FirmaRemotaException
//	 *             If an error occurs during sign start process.
//	 */
//	public String iniciarFirma(String hashDoc, String idUsuario)
//			throws FirmaRemotaException {
//		RemoteSignWSLegacyBO remoteSignWSLegacyBO = applicationContext.getBean(RemoteSignWSLegacyBO.class);
//		return remoteSignWSLegacyBO.initSign(hashDoc, idUsuario);
//	}
//
//	////////////ESTE MÉTODO SE COMENTA POR QUE YA NO SE UTILIZA Y ES INCOMPATIBLE CON LA MODIFICACIÓN /////////
//	//////////// NECESARIA PARA CORREGIR EL PROBLEMA DE LAS LÍNEAS DE FIRMA CIRCULARES                 /////////
////	/**
////	 * Method which finalizes sign process.
////	 * 
////	 * @param hashDoc
////	 *            Hash of the document which will be signed.
////	 * @param datosFirmados
////	 *            Hash obtained from start sign process.
////	 * @param idUsuario
////	 *            Id of the user which will sign the document.
////	 * @return Transaction id of signed document.
////	 * @throws FirmaRemotaException
////	 *             If an error occurs during sign end process.
////	 */
////	public double finalizarFirma(String hashDoc, String datosFirmados,
////			String idUsuario) throws FirmaRemotaException {
////		RemoteSignWSLegacyBO remoteSignWSLegacyBO = (RemoteSignWSLegacyBO) Component
////				.getInstance("remoteSignWSLegacyBO");
////		return remoteSignWSLegacyBO.endSign(hashDoc, datosFirmados, idUsuario, stateTags);
////	}
//}
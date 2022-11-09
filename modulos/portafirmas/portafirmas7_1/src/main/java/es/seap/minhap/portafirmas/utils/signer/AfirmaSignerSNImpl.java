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

package es.seap.minhap.portafirmas.utils.signer;

//import org.apache.axis.utils.XMLUtils;

public class AfirmaSignerSNImpl { 

/*implements PfSign {
	private static final Log log = LogFactory.getLog(AfirmaSignerSNImpl.class);
	private Configuration config;
	private String trustedStorePass;
	private String httpUser;
	private String httpPass;

	public AfirmaSignerSNImpl(Configuration config, String trustedStorePass,
			String httpUser, String httpPass) {
		this.config = config;
		this.trustedStorePass = trustedStorePass;
		this.httpUser = httpUser;
		this.httpPass = httpPass;
	}

	public String registerDocument(SignDocument signDocument)
			throws SignerException {
		String idTransaccion = null;
		Properties systemProps = System.getProperties();
		try {
			log.info("Preparando la petición al servicio Web AlmacenarDocumento...");
			Document custodyDocumentRequest;
			try {
				custodyDocumentRequest = UtilsWebService
						.prepareCustodyDocumentRequest(
								this.config
										.getString("signer.afirma5.application"),
								signDocument.getName(),
								signDocument.getMimeType(),
								new String(
										new Base64Coder()
												.encodeBase64(signDocument
														.getContent())));
			} catch (Exception e) {
				log.error("Error al convertir el contenido del documento en Base64: "
						+ e.getMessage());

				throw new SignerException(
						"Error al convertir el contenido del documento en Base64: "
								+ e.getMessage());
			}

			log.info("Lanzando la petición...");
			log.debug("Peticion :"
					+ XMLUtils.DocumentToString(custodyDocumentRequest));

			String response = UtilsWebService.launchRequest(
					"AlmacenarDocumento", custodyDocumentRequest, this.config,
					this.trustedStorePass, this.httpUser, this.httpPass);

			log.debug("Respuesta :" + response);

			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de Almacenamiento del documento "
						+ signDocument.getName() + " no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de Almacenamiento del documento "
								+ signDocument.getName()
								+ " no ha sido satisfactoria.");
			}

			idTransaccion = UtilsWebService.getInfoFromDocumentNode(response,
					"idDocumento");

			log.info("Identificador del documento correctamente extraído de la respuesta"
					+ idTransaccion);
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}

		return idTransaccion;
	}

	public void initSignBlockThreePhases(SignTransaction signTransaction,
			List<String> listIdsTransactionDocs,
			HashMap<Object, Object> blockMultisign) throws SignerException {
		Properties systemProps = System.getProperties();
		try {
			String[] listaIds = new String[listIdsTransactionDocs.size()];
			for (int i = 0; i < listIdsTransactionDocs.size(); ++i) {
				listaIds[i] = ((String) listIdsTransactionDocs.get(i));
			}

			log.info("Preparando la petición al servicio Web FirmaUsuarioBloquesF1...");

			Document blockUserSignatureF1Request = UtilsWebService
					.prepareBlockUserSignatureF1Request(
							this.config.getString("signer.afirma5.application"),
							listaIds,
							null,
							this.config
									.getString("signer.afirma5.servercert.alias"),
							blockMultisign, this.config
									.getString("signer.afirma5.hash.algorithm"));

			log.info("Lanzando la petición...");
			log.debug("Peticion : "
					+ XMLUtils.DocumentToString(blockUserSignatureF1Request));

			String response = UtilsWebService.launchRequest(
					"FirmaUsuarioBloquesF1", blockUserSignatureF1Request,
					this.config, this.trustedStorePass, this.httpUser,
					this.httpPass);

			log.debug("Respuesta : " + response);

			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de Firma Usuario por Bloques F1 no ha sido satisfactoria");

				throw new SignerException(
						"La petición de Firma Usuario por Bloques F1 no ha sido satisfactoria");
			}

			log.info("Extrayendo el identificador de transaccion de la respuesta...");

			String transactionId = UtilsWebService.getInfoFromDocumentNode(
					response, "idTransaccion");

			log.info("Id de transaccion: " + transactionId);

			log.info("Extrayendo el hash del bloque de firma  de la respuesta...");

			String hashBlockSignature = UtilsWebService
					.getInfoFromDocumentNode(response, "hash");

			log.info("Hash del bloque de firma: " + hashBlockSignature);

			signTransaction.setId(transactionId);
			signTransaction.setHash(hashBlockSignature);
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
	}

	public void completeSignBlockThreePhases(SignTransaction transaccion,
			String datosFirmados, String anagrama) throws SignerException {
		Properties systemProps = System.getProperties();
		try {
			log.info("Vamos a finalizar la firma en bloque...");

			log.info("Preparando la petición al servicio Web FirmaUsuarioBloquesF3...]");

			Document blockUserSignatureF3Request = UtilsWebService
					.prepareBlockUserSignatureF3Request(
							this.config.getString("signer.afirma5.application"),
							transaccion.getId(),
							datosFirmados,
							anagrama,
							this.config
									.getString("signer.afirma5.signature.format"));

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(blockUserSignatureF3Request));

			String response = UtilsWebService.launchRequest(
					"FirmaUsuarioBloquesF3", blockUserSignatureF3Request,
					this.config, this.trustedStorePass, this.httpUser,
					this.httpPass);

			log.debug("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de Firma Usuario Bloques F3 no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de Firma Usuario Bloques F3 no ha sido satisfactoria");
			}

			log.info("Petición correctamente realizada");
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
	}

	public void getInformationSignaturesBlockRequest(SignTransaction transaction)
			throws SignerException {
		Properties systemProps = System.getProperties();
		try {
			log.info("Vamos a obtener información de bloques...");

			log.info("Preparando la petición al servicio Web ObtenerInformacionBloqueFirmas...]");

			Document getDocIdSignaturesRequest = UtilsWebService
					.prepareGetInformationSignaturesBlockRequest(
							this.config.getString("signer.afirma5.application"),
							transaction.getId());

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(getDocIdSignaturesRequest));

			String response = UtilsWebService.launchRequest(
					"ObtenerInformacionBloqueFirmas",
					getDocIdSignaturesRequest, this.config,
					this.trustedStorePass, this.httpUser, this.httpPass);

			log.info("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de Información de Bloques no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de Información de Bloques no ha sido satisfactoria");
			}

			log.info("Petición correctamente realizada");
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
	}

	public Map<String, SignerBlock> getCompleteInformationSignaturesBlockRequest(
			SignTransaction transaction) throws SignerException {
		Properties systemProps = System.getProperties();
		try {
			log.info("Vamos a obtener información de bloques...");

			log.info("Preparando la petición al servicio Web ObtenerInfoCompletaBloqueFirmas...]");

			Document getDocIdSignaturesRequest = UtilsWebService
					.prepareGetCompleteInfoSignaturesBlockRequest(
							this.config.getString("signer.afirma5.application"),
							transaction.getId());

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(getDocIdSignaturesRequest));

			String response = UtilsWebService.launchRequest(
					"ObtenerInfoCompletaBloqueFirmas",
					getDocIdSignaturesRequest, this.config,
					this.trustedStorePass, this.httpUser, this.httpPass);

			log.info("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de Información de Bloques no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de Información de Bloques no ha sido satisfactoria");
			}

			log.info("Extrayendo información de bloques de la respuesta...");
			Map signList = UtilsWebService.getListBlockFromDocumentNode(
					response, "documentoBloque");

			log.info("Se han extraido: " + signList.size() + " valores.");

			log.info("Petición correctamente realizada");

			Map localMap1 = signList;

			return localMap1;
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
	}

	public String getSignature(SignTransaction transaction)
			throws SignerException {
		String firma = null;
		Properties systemProps = System.getProperties();
		try {
			log.info("Vamos a obtener binario de firma...");

			log.info("Preparando la petición al servicio Web ObtenerFirmaTransaccion...]");

			Document getSignatureRequest = UtilsWebService
					.prepareGetESignatureRequest(
							this.config.getString("signer.afirma5.application"),
							transaction.getId());

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(getSignatureRequest));

			String response = UtilsWebService.launchRequest(
					"ObtenerFirmaTransaccion", getSignatureRequest,
					this.config, this.trustedStorePass, this.httpUser,
					this.httpPass);

			log.debug("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de binario de firma no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de binario de firma no ha sido satisfactoria");
			}

			log.info("Extrayendo binario de la respuesta...");
			firma = UtilsWebService.getInfoFromDocumentNode(response,
					"firmaElectronica");

			log.info("Petición correctamente realizada");

			String str1 = firma;

			return str1;
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
	}

	public String validateSignature(String signData) throws SignerException {
		String certificado = null;
		Properties systemProps = System.getProperties();
		try {
			log.info("Vamos a validar firma...");

			log.info("Preparando la petición al servicio Web ValidarFirma...]");

			Document getSignatureRequest = UtilsWebService
					.prepareValidateSignatureRequest(
							this.config.getString("signer.afirma5.application"),
							signData, null, null, null, null);

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(getSignatureRequest));

			String response = UtilsWebService.launchRequest("ValidarFirma",
					getSignatureRequest, this.config, this.trustedStorePass,
					this.httpUser, this.httpPass);

			log.debug("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de validacion de firma no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de validacion de firma no ha sido satisfactoria");
			}

			log.info("Extrayendo info de la respuesta...");
			certificado = UtilsWebService.getInfoFromDocumentNode(response,
					"certificado");

			log.info("Petición correctamente realizada");

			String str1 = certificado;

			return str1;
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
	}

	public Map<String, String> validateCertificate(String certificate)
			throws SignerException {
		Map dataSigner = new HashMap();
		String dni = null;
		String nombre = null;
		String apellidos = null;
		String numeroSerie = null;

		Properties systemProps = System.getProperties();
		try {
			log.info("Vamos a validar certificado...");

			log.info("Preparando la petición al servicio Web ObtenerInfoCertificado...]");

			Document getCertificateRequest = UtilsWebService
					.prepareGetCertificateInfoRequest(
							this.config.getString("signer.afirma5.application"),
							certificate);

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(getCertificateRequest));

			String response = UtilsWebService.launchRequest(
					"ObtenerInfoCertificado", getCertificateRequest,
					this.config, this.trustedStorePass, this.httpUser,
					this.httpPass);

			log.info("Respuesta : " + response);

			log.info("Extrayendo dni de la respuesta...");

			String campoDni = UtilsWebService.getXMLChildsFromDocumentNodeTag(
					response, "InfoCertificado", "NIF-CIF");

			String campoNombre = UtilsWebService
					.getXMLChildsFromDocumentNodeTag(response,
							"InfoCertificado", "nombreResponsable");

			String campoApellidos = UtilsWebService
					.getXMLChildsFromDocumentNodeTag(response,
							"InfoCertificado", "ApellidosResponsable");

			String campoNumeroSerie = UtilsWebService
					.getXMLChildsFromDocumentNodeTag(response,
							"InfoCertificado", "numeroSerie");

			if (campoDni == null) {
				campoDni = UtilsWebService.getXMLChildsFromDocumentNodeTag(
						response, "InfoCertificado", "NIFResponsable");
			}

			if (campoDni == null) {
				log.error("No se encuentra el nif/cif en el certificado");
				throw new SignerException(
						"No se encuentra el nif/cif en el certificado");
			}

			dni = UtilsWebService.getInfoFromDocumentNode(campoDni, "valorCampo");

			log.info("Dni: " + dni);
			dataSigner.put("dni", dni);

			nombre = UtilsWebService.getInfoFromDocumentNode(campoNombre, "valorCampo");

			log.info("Nombre: " + nombre);
			dataSigner.put("nombre", nombre);

			apellidos = UtilsWebService.getInfoFromDocumentNode(campoApellidos,	"valorCampo");

			log.info("Apellidos: " + apellidos);
			dataSigner.put("apellidos", apellidos);

			numeroSerie = UtilsWebService.getInfoFromDocumentNode(campoNumeroSerie, "valorCampo");

			log.info("Número de serie: " + numeroSerie);
			dataSigner.put("numeroSerie", numeroSerie);

			log.info("Petición correctamente realizada");

			Map localMap1 = dataSigner;

			return localMap1;
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
	}

	public String getIdDocument(SignTransaction transaction)
			throws SignerException {
		String idDocumento = null;
		Properties systemProps = System.getProperties();
		try {
			log.info("Vamos a obtener el id de documento ...");

			log.info("Preparando la petición al servicio Web ObtenerIdDocumento...]");

			Document getDocumentIdRequest = UtilsWebService
					.prepareGetDocumentIdRequest(
							this.config.getString("signer.afirma5.application"),
							transaction.getId());

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(getDocumentIdRequest));

			String response = UtilsWebService.launchRequest(
					"ObtenerIdDocumento", getDocumentIdRequest, this.config,
					this.trustedStorePass, this.httpUser, this.httpPass);

			log.info("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de id Documento no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de id Documento no ha sido satisfactoria");
			}

			log.info("Extrayendo id de la respuesta...");
			idDocumento = UtilsWebService.getInfoFromDocumentNode(response,
					"idDocumento");

			log.info("Petición correctamente realizada");

			String str1 = idDocumento;

			return str1;
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
	}

	public List<String> validateSign(String eSignature, byte[] hashData,
			byte[] data) throws SignerException {
		Properties systemProps = System.getProperties();
		List res = null;
		try {
			log.info("Vamos a validar la firma...");

			log.info("Preparando la petición al servicio Web ValidarFirma...]");

			Document signatureValidationRequest = UtilsWebService
					.prepareValidateSignatureRequest(
							this.config.getString("signer.afirma5.application"),
							eSignature,
							this.config
									.getString("signer.afirma5.signature.format"),
							hashData,
							this.config
									.getString("signer.afirma5.hash.algorithm"),
							data);

			log.info("Lanzando la petición ...");

			String response = UtilsWebService.launchRequest("ValidarFirma",
					signatureValidationRequest, this.config,
					this.trustedStorePass, this.httpUser, this.httpPass);

			log.info("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de Validación de Firma no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de Validación de Firma no ha sido satisfactoria");
			}

			res = UtilsWebService.getListFromDocumentNode(response,
					"certificado");

			log.info("Petición correctamente realizada");
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
		return res;
	}

	public String validateSignBlockDocument(String eSignature, String document,
			String idDocument) throws SignerException {
		Properties systemProps = System.getProperties();
		String res = null;
		try {
			log.info("Vamos a validar la firma en bloque...");

			log.info("Preparando la petición al servicio Web ValidarFirmaBloquesDocumento...]");

			Document signatureValidationRequest = UtilsWebService
					.prepareBlockSignatureDocumentValidationRequest(
							this.config.getString("signer.afirma5.application"),
							eSignature,
							document,
							idDocument,
							this.config
									.getString("signer.afirma5.signature.format"));

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(signatureValidationRequest));

			String response = UtilsWebService.launchRequest(
					"ValidarFirmaBloquesDocumento", signatureValidationRequest,
					this.config, this.trustedStorePass, this.httpUser,
					this.httpPass);

			log.info("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de Validación de Firma de Bloques Documento no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de Validación de Firma de Bloques Documento no ha sido satisfactoria");
			}

			res = UtilsWebService.getInfoFromDocumentNode(response,
					"certificado");

			log.info("Petición correctamente realizada");
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
		return res;
	}

	public String completeSignTwoPhases(String eSignature, String certificate,
			String contentDoc, String nameDoc, String mimeDoc)
			throws SignerException {
		Properties systemProps = System.getProperties();
		String res = null;
		try {
			log.info("Vamos a finalizar la firma en 2 fases...");

			log.info("Preparando la petición al servicio Web FirmaUsuario2FasesF2...]");

			Document userSignatureF2Request = UtilsWebService
					.prepareTwoPhasesUserSignatureF2Request(
							this.config.getString("signer.afirma5.application"),
							eSignature,
							certificate,
							this.config
									.getString("signer.afirma5.signature.format"),
							contentDoc, mimeDoc, nameDoc, this.config
									.getString("signer.afirma5.hash.algorithm"));

			log.info("Lanzando la petición ...");
			log.debug("Petición : "
					+ XMLUtils.DocumentToString(userSignatureF2Request));

			String response = UtilsWebService.launchRequest(
					"FirmaUsuario2FasesF2", userSignatureF2Request,
					this.config, this.trustedStorePass, this.httpUser,
					this.httpPass);

			log.debug("Respuesta : " + response);
			if (!(UtilsWebService.isCorrect(response))) {
				log.error("La petición de Firma en 2 Fases no ha sido satisfactoria.");

				throw new SignerException(
						"La petición de Firma en 2 Fases no ha sido satisfactoria");
			}

			res = UtilsWebService.getInfoFromDocumentNode(response,
					"idTransaccion");

			log.info("Petición correctamente realizada");
		} catch (Exception ex) {
			throw new SignerException(ex.getMessage());
		} finally {
			System.setProperties(systemProps);
		}
		return res;
	}*/
}
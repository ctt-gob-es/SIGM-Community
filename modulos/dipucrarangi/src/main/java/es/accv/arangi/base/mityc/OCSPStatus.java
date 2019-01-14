/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.mityc;
import java.security.cert.X509Certificate;
import java.util.Date;

import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.util.validation.ValidationResult;
import es.mityc.javasign.certificate.AbstractCertStatus;
import es.mityc.javasign.certificate.IOCSPCertStatus;
import es.mityc.javasign.certificate.RevokedInfo;

/**
 * Estado de una respuesta OCSP
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class OCSPStatus extends AbstractCertStatus implements IOCSPCertStatus {

	/** Contenido binario de la respuesta OCSP. */
	private byte[] respOCSP = null;
	/** Fecha de la respuesta OCSP. */
	private Date dateResponse = null;
//	private String tiempoRespuesta = null;
	/** Identificador del OCSP responder. */
	private String responderID = null;
	/** Tipo del identificador del OCSP responder. */
	private TYPE_RESPONDER responderType;
	
	/**
	 * <p>Constructor.</p>
	 * 
	 * @param resp Respuesta OCSP del estado del certificado
	 * @param cert Certificado sobre el que se realiza la consulta de estado
	 */
	public OCSPStatus(OCSPResponse resp, X509Certificate cert) {
		super();
		setRespOCSP(resp.toDER());
		setRespondeDate(resp.getBasicOCSPResponse().getProducedAt());
		
		//-- el responder ID lo probamos primero por X509Name
		String responderId = resp.getResponderIdName();
		if (responderId != null) {
			setResponder(responderId, IOCSPCertStatus.TYPE_RESPONDER.BY_NAME);
		} else {
			//-- el responder ID es un keyhash
			setResponder(resp.getResponderIdKeyHash(), IOCSPCertStatus.TYPE_RESPONDER.BY_KEY);
		}
		
		if (resp.getStatus() == ValidationResult.RESULT_VALID) {
			setCertStatus(CERT_STATUS.valid);
		} else if (resp.getStatus() == ValidationResult.RESULT_CERTIFICATE_REVOKED) {
			setCertStatus(CERT_STATUS.revoked);
			setRevokedInfo(new RevokedInfo(resp.getSingleResponses()[0].getRevocationReason(), resp.getSingleResponses()[0].getRevocationTime()));
		} else { 
			setCertStatus(CERT_STATUS.unknown);
		}
		setCertificate(cert);
	}
	
	/**
	 * <p>Establece el contenido binario de la respuesta OCSP.</p>
	 * @param binary byte[] con el contenido binario de la respuesta OCSP
	 */
	private void setRespOCSP(final byte[] binary) {
		respOCSP = (binary != null) ? (byte[]) binary.clone() : null;
	}
	
	/**
	 * <p>Establece los datos de identificación del OCSP responder. </p>
	 * @param id cadena identificativa
	 * @param tipoResponder tipo de identificador del OCSP responder
	 */
	private void setResponder(final String id, final IOCSPCertStatus.TYPE_RESPONDER tipoResponder) {
		this.responderID = new String(id);
		responderType = tipoResponder;
//		switch (tipoResponder) {
//			case BY_KEY:
//				responderType = TYPE_RESPONDER.BY_KEY;
//				break;
//			default:
//				responderType = TYPE_RESPONDER.BY_NAME;
//				break;
//		}
	}
	
	/**
	 * <p>Establece la fecha de la respuesta de la consulta de estado.</p>
	 * @param date fecha de la respuesta
	 */
	private void setRespondeDate(final Date date) {
		dateResponse = (Date) date.clone();
	}

	
	/**
	 * <p>Devuelve la cadena identificadora del OCSP Responder que generó esta respuesta OCSP.</p>
	 * @return cadena identificadora
	 * @see es.mityc.javasign.certificate.IOCSPCertStatus#getResponderID()
	 */
	public String getResponderID() {
		return (responderID != null) ? new String(responderID) : null;
	}

	/**
	 * <p>Devuelve el tipo de identificador del OCSP Responder.</p>
	 * @return tipo de identificador según {@link #responderType}
	 * @see es.mityc.javasign.certificate.IOCSPCertStatus#getResponderType()
	 */
	public TYPE_RESPONDER getResponderType() {
		return responderType;
	}

	/**
	 * <p>Devuelve la fecha de emisión de la respuesta OCSP.</p>
	 * @return fecha de la respuesta OCSP 
	 * @see es.mityc.javasign.certificate.IOCSPCertStatus#getResponseDate()
	 */
	public Date getResponseDate() {
		return (dateResponse != null) ? (Date) dateResponse.clone() : null;
	}

	/**
	 * <p>Devuelve la respuesta OCSP en formato binario.</p>
	 * @return byte[] con la respuesta según la RFC 2560
	 * @see es.mityc.javasign.certificate.ICertStatus#getEncoded()
	 */
	public byte[] getEncoded() {
		return respOCSP;
	}

}


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
package es.accv.arangi.base.certificate.validation.service;

import java.util.Date;
import java.util.Map;

import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Resultado de la validación a través del servicio de validación de 
 * certificados.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 */
public class CertificateValidationServiceResult {

	private int result;
	
	private Map<String, Object> fields;
	
	private Date revocationDate;
	
	private int revocationReason;
	
	private OCSPResponse ocspResponse; 

	public CertificateValidationServiceResult() {
		super();
	}
	
	public CertificateValidationServiceResult(int result,
			Map<String, Object> fields) {
		super();
		this.result = result;
		this.fields = fields;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}

	public Date getRevocationDate() {
		return revocationDate;
	}

	public void setRevocationDate(Date revocationDate) {
		this.revocationDate = revocationDate;
	}

	public int getRevocationReason() {
		return revocationReason;
	}

	public void setRevocationReason(int revocationReason) {
		this.revocationReason = revocationReason;
	}

	public OCSPResponse getOcspResponse() {
		return ocspResponse;
	}

	public void setOcspResponse(OCSPResponse ocspResponse) {
		this.ocspResponse = ocspResponse;
	}

	public boolean isValid() {
		return getResult() == ValidationResult.RESULT_VALID;
	}
}

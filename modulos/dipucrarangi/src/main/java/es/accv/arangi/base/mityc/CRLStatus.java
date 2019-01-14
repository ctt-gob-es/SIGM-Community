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

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import es.accv.arangi.base.certificate.validation.CRL;
import es.mityc.javasign.certificate.AbstractCertStatus;
import es.mityc.javasign.certificate.IX509CRLCertStatus;

/**
 * Estado de una CRL
 * 
 * @author <a href="mailto:lusalas16@gmail.com">Luis Carlos Salas Villalobos</a>
 */

public class CRLStatus extends AbstractCertStatus implements IX509CRLCertStatus {

	private CRL crl;

	public CRLStatus(CRL crl, X509Certificate cert) {
		super();
		this.crl = crl;
		setCertStatus(CERT_STATUS.valid);
		setCertificate(cert);
	}

	public X509CRL getX509CRL() {
		return crl.getX509CRL();
	}

	@Override
	public byte[] getEncoded() {
		return crl.toDER();
	}
}

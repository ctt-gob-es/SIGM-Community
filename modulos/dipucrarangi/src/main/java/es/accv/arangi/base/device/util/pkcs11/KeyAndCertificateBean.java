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
package es.accv.arangi.base.device.util.pkcs11;


import iaik.pkcs.pkcs11.objects.Key;
import iaik.pkcs.pkcs11.objects.X509PublicKeyCertificate;



/**
 * Clase de datos, contiene un certificado y su clave asociada.
 * 
 * @author faparicio
 * 
 */
public class KeyAndCertificateBean {


  /**
   * The key.
   */
  protected Key key_;

  /**
   * This optional certificate.
   */
  protected X509PublicKeyCertificate certificate_;


  /**
   * Creates a new object that holds the given key and certificate.
   * 
   * @param key
   *          The key.
   * @param certificate
   *          The certificate.
   */
  public KeyAndCertificateBean(Key key, X509PublicKeyCertificate certificate) {
    key_ = key;
    certificate_ = certificate;
  }


  /**
   * Returns the certificate.
   * 
   * @return The certificate.
   */
  public X509PublicKeyCertificate getCertificate() {
    return certificate_;
  }


  /**
   * Returns the key.
   * 
   * @return The key.
   */
  public Key getKey() {
    return key_;
  }


  /**
   * Sets the certificate.
   * 
   * @param certificate
   *          The certificate.
   */
  public void setCertificate(X509PublicKeyCertificate certificate) {
    certificate_ = certificate;
  }


  /**
   * Sets the key.
   * 
   * @param key
   *          The key.
   */
  public void setKey(Key key) {
    key_ = key;
  }

}

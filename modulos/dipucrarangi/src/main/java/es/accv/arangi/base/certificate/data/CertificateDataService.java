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
package es.accv.arangi.base.certificate.data;

import java.util.Map;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.exception.certificate.validation.ServiceException;
import es.accv.arangi.base.exception.certificate.validation.ServiceNotFoundException;

/**
 * Interfaz que han de cumplir todas las clases que implementen un 
 * servicio de obtención de datos de certificados. Lo más común serán 
 * servicios web para validación de certificados.
 *  
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 */
public interface CertificateDataService {

	/**
	 * Obtiene los datos de un certificado mediante una llamada a un servicio externo.
	 * 
	 * @param certificate Certificado 
	 * @param extraParams Parámetros extra por si fueran necesarios para 
	 * 	realizar la obtención
	 * @return Map con los valores obtenidos del certificado
	 * @throws ServiceNotFoundException El servicio no se encuentra disponible
	 * @throws ServiceException La llamada al servicio devuelve un error
	 */
	public Map<String,String> getData (Certificate certificate, 
			Map<String,Object> extraParams) throws ServiceNotFoundException, ServiceException;
	
}

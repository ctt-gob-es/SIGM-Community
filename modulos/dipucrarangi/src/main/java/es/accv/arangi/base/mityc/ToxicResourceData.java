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

import java.util.Arrays;

import org.apache.log4j.Logger;

import es.mityc.javasign.xml.resolvers.IResourceData;
import es.mityc.javasign.xml.resolvers.ResourceDataException;

/**
 * Devolverá un texto fijo para que la validación siempre de como resultado
 * que el documento no coincide con la firma. Se utiliza para el caso de que
 * la firma sea attached y se pase un documento que no coincide con el que
 * se encuentra dentro de la firma.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 *
 */
public class ToxicResourceData implements IResourceData {

	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(ToxicResourceData.class);
	
	/* (non-Javadoc)
	 * @see es.mityc.javasign.xml.resolvers.IResourceData#canAccess(java.lang.String, java.lang.String)
	 */
	public boolean canAccess(String name, String baseURI) {
		if (name != null && !name.startsWith("#Signature") && !name.startsWith("#Certificate")) {
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see es.mityc.javasign.xml.resolvers.IResourceData#getAccess(java.lang.String, java.lang.String)
	 */
	public Object getAccess(String name, String baseURI) throws ResourceDataException {
		
		logger.debug("[ToxicResourceData.getAccess]::Entrada::" + Arrays.asList(new Object[] { name, baseURI }));
		
		//-- Devolver texto fijo y raro
		return "$(&%!??¿**#@@@".getBytes();
	}


}

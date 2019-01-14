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
package es.accv.arangi.base.device.model;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Los distintos modelos de dispositivos registran en esta clase su módulo asociado.
 * Accediendo al método getInstance se obtienen estos módulos con los que se pueden
 * abrir los dispositivos.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class CardModuleFactory {

	/**
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(CardModuleFactory.class);
	
	static HashMap hmModules;
	
	/**
	 * Registra un nuevo módulo para tratar un tipo de dispositivo
	 * 
	 * @param deviceType Nombre del tipo de dispositivo
	 * @param cardModule Módulo 
	 */
	public static void register (String deviceType, Pkcs11Manufacturer cardModule) {
		getModules().put (deviceType, cardModule);
	}
	
	/**
	 * Obtiene un módulo para tratar con el tipo de dispositivo pasado como
	 * parámetro.
	 * 
	 * @param deviceType Tipo de dispositivo
	 * @return Modulo o null si no existe ningún módulo registrado para el
	 * tipo de dispositivo
	 */
	public static Pkcs11Manufacturer getInstance (String deviceType) {
		return (Pkcs11Manufacturer) getModules().get(deviceType);
	}

	/*
	 * Obtiene el mapa de módulos
	 */
	private static HashMap getModules() {
		if (hmModules == null) {
			hmModules = new HashMap ();
		}
		return hmModules;
	}
}

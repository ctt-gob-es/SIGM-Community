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

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.util.Util;
import es.mityc.javasign.xml.resolvers.IResourceData;
import es.mityc.javasign.xml.resolvers.ResourceDataException;

/**
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 *
 */
public class UnknownFileResourceData implements IResourceData {

	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(UnknownFileResourceData.class);
	
	/**
	 * Fichero que devolverá la clase
	 */
	IDocument document;
	
	/**
	 * Constructor: el fichero externo es el que se devolverá si se encuentra
	 * una referencia diferente a #Signature... (Firma de los datos firmables) o
	 * #Certificate... (Firma del certificado)
	 * 
	 * @param document Fichero que devolverá la clase
	 */
	public UnknownFileResourceData (IDocument document) {
		this.document = document;
	}
	
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
		
		logger.debug("[UnknownFileResourceData.getAccess]::Entrada::" + Arrays.asList(new Object[] { name, baseURI }));
		
		//-- Si el fichero existe devolver un FileInputStream
		try {
			return Util.readStream(document.getInputStream());
		} catch (IOException e) {
			logger.info("[UnknownFileResourceData.getAccess]::No se ha podido leer el documento");
			return null;
		}
	}


}

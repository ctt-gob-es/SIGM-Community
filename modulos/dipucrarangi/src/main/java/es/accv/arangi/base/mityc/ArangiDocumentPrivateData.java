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

import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.exception.document.HashingException;
import es.mityc.javasign.xml.resolvers.IPrivateData;
import es.mityc.javasign.xml.resolvers.ResourceDataException;

/**
 * Permite hacer firmas XAdES detached sobre ficheros
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class ArangiDocumentPrivateData implements IPrivateData {

	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(ArangiDocumentPrivateData.class);
	
	/**
	 * Fichero
	 */
	IDocument document;
	
	/**
	 * Constructor
	 * @param document Documento
	 */
	public ArangiDocumentPrivateData(IDocument document) {
		this.document = document;
	}

	/* (non-Javadoc)
	 * @see es.mityc.javasign.xml.resolvers.IPrivateData#canDigest(java.lang.String, java.lang.String)
	 */
	public boolean canDigest(String name, String baseURI) {
		
		if (name == null || name.startsWith("#Signature") || name.startsWith("#Certificate")) {
			return false;
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see es.mityc.javasign.xml.resolvers.IPrivateData#getDigest(java.lang.String, java.lang.String, java.lang.String)
	 */
	public byte[] getDigest(String name, String baseURI, String algName)
			throws ResourceDataException {
		
		logger.debug("[ArangiDocumentPrivateData.getDigest]::Entrada::" + Arrays.asList(new Object[] { name, baseURI }));
		
		//-- Obtener un objeto document y de él obtener el hash
		try {
			return document.getHash();
		} catch (HashingException e) {
			logger.debug("[ArangiDocumentPrivateData.getDigest]::No es posible obtener el digest del documento: " + document, e);
			throw new ResourceDataException("No es posible obtener el digest del documento: " + document, e);
		}
		
	}


}

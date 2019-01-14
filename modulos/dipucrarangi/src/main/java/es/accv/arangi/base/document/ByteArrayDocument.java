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
package es.accv.arangi.base.document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;

import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.exception.document.HashingException;

/**
 * Documento en el que su hash será un resumen de todo su contenido.<br><br> 
 *  
 * Se inicializa con un array de bytes, por lo que hay que tener cuidado con
 * el tamaño que puede llegar a ocupar en memoria. Si el documento es muy 
 * grande es mejor tratarlo con la clase {@link InputStreamDocument}.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class ByteArrayDocument extends Document{

	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(ByteArrayDocument.class);
	
	/*
	 * Array de bytes con el contenido del documento
	 */
	protected byte[] document;
	
	/**
	 * Inicializa el objeto pasándole un array de bytes
	 * 
	 * @param document Array de bytes con el contenido del documento
	 */
	public ByteArrayDocument (byte[] document) {
		logger.debug("[ArrayBytesDocument]::Entrada::" + document);
		this.document = document;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getHash()
	 */
	public byte[] getHash() throws HashingException {
		return getHash(HashingAlgorithm.getDefault());
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getHash(java.lang.String)
	 */
	public byte[] getHash(String hashingAlgorithm) throws HashingException {
		
		logger.debug("[ArrayBytesDocument.getHash]::Entrada::" + hashingAlgorithm);
		
		//-- Obtener un input stream
		ByteArrayInputStream bais = new ByteArrayInputStream (document);
		
		//-- Crear un objeto InputStreamDocument
		InputStreamDocument isd = new InputStreamDocument (bais);
		
		//-- Obtener el hash y devolver
		byte[] hash = isd.getHash(hashingAlgorithm);
		logger.debug ("[ArrayBytesDocument.getHash]::FIN::Devolviendo: " + hash);
		
		return hash ;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getInputStream()
	 */
	public InputStream getInputStream() {
		
		logger.debug("[ArrayBytesDocument.getInputStream]::Entrada");
		
		//-- Obtener un input stream y devolver
		return new ByteArrayInputStream (document);
	}
	
}

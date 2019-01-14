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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;

import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.document.InitDocumentException;

/**
 * Clase que representa a documentos ubicados en URLs. Si hay que configurar
 * algún tipo de proxy o establecer keystores para comunicación SSL deberá
 * hacerse antes de llamar a esta clase. 
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class URLDocument extends Document {

	/**
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(URLDocument.class);
	
	/**
	 * URL con el contenido del documento
	 */
	private URL url;
	
	/**
	 * Inicialización del objeto mediante una URL
	 * 
	 * @param url URL con el contenido del documento
	 * @throws InitDocumentException El fichero es nulo o no existe
	 */
	public URLDocument (URL url) throws InitDocumentException {
		logger.debug("[URLDocument]::Entrada::" + url);
		if (url == null) {
			logger.info("[URLDocument]::La URL es nula");
			throw new InitDocumentException ("La URL es nula");
		}
		
		try {
			url.openConnection();
		} catch (IOException e) {
			logger.info("[URLDocument]::No es posible conectarse a la URL indicada: " + url, e);
			throw new InitDocumentException ("No es posible conectarse a la URL indicada: " + url, e);
		}
		
		this.url = url;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getHash()
	 */
	public byte[] getHash() throws HashingException {
		return getHash (HashingAlgorithm.getDefault());
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getHash(java.lang.String)
	 */
	public byte[] getHash(String hashingAlgorithm) throws HashingException {
		
		logger.debug("[URLDocument.getHash]::Entrada::" + hashingAlgorithm);
		
		//-- Hacer un FileInputStream y utilizar un InputStreamDocument para obtener el hash
		InputStream is = null;
		byte[] hash = null;
		try {
			is = url.openStream();
			InputStreamDocument isd = new InputStreamDocument (is);
			hash = isd.getHash(hashingAlgorithm);
			logger.debug ("[URLDocument.getHash]::FIN::Devolviendo: " + hash);
			
			return hash;
			
		} catch (IOException e) {
			logger.info("[URLDocument.getHash]::No ha sido posible leer la URL: " + url, e);
			throw new HashingException ("No ha sido posible leer la URL:" + url, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.info("[URLDocument.getHash]::El fichero no existe", e);
					return hash;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.document.IDocument#getInputStream()
	 */
	public InputStream getInputStream() {
		try {
			return url.openStream();
		} catch (IOException e) {
			logger.info("[URLDocument.getInputStream]::No ha sido posible leer la URL: " + url, e);
			return null;
		}
	}

}

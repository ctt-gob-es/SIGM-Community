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
package es.accv.arangi.base.signature.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.exception.signature.XMLDocumentException;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.firmaJava.libreria.xades.elementos.xades.DocumentationReference;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SPURI;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SPUserNotice;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SigPolicyQualifier;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SignaturePolicyIdentifier;
import es.mityc.firmaJava.libreria.xades.errores.InvalidInfoNodeException;

/**
 * Contiene la información de la política que se añade a la firma XAdES-BES-EPES
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 */
public class ArangiXAdESPolicyId extends ArangiObject implements ArangiXAdESPolicyIdentifier {

	Logger logger = Logger.getLogger(ArangiXAdESPolicyId.class);
	
	private SignaturePolicyIdentifier spi;
	
	public ArangiXAdESPolicyId (String xml) throws XMLDocumentException {
		
		ByteArrayInputStream bais;
		try {
			bais = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.info("La codificación UTF-8 no está soportada", e);
			throw new XMLDocumentException("La codificación UTF-8 no está soportada", e);
		}
		
		Document doc;
		try {
			doc = getDocumentBuilder().parse(bais);
		} catch (Exception e) {
			logger.info("No se puede parsear el XML: \n" + xml, e);
			throw new XMLDocumentException("No se puede parsear el XML", e);
		}
		
		spi = new SignaturePolicyIdentifier(XAdESSchemas.XAdES_132);
		try {
			spi.load(doc.getDocumentElement());
		} catch (InvalidInfoNodeException e) {
			logger.info("El XML no parece un SignaturePolicyIdentifier vï¿½lido: \n" + xml, e);
			throw new XMLDocumentException("El XML no parece un SignaturePolicyIdentifier vï¿½lido", e);
		}
		
	}
	
	public SignaturePolicyIdentifier getSignaturePolicyIdentifier() {
		return spi;
	}
	
	public String toString () {
		String a = "Implied: " + spi.isImplied() + "\n";
		if (spi.getSignaturePolicyId() != null) {
				a += "Identifier: " + spi.getSignaturePolicyId().getSigPolicyId().getIdentifier().getUri() + "\n" +
				"Description: " + spi.getSignaturePolicyId().getSigPolicyId().getDescription().getValue() + "\n";
		
				if (spi.getSignaturePolicyId().getSigPolicyId().getReferences() != null) {
					for (DocumentationReference dr : spi.getSignaturePolicyId().getSigPolicyId().getReferences().getList()) {
						a += "DocumentationReference: " + dr.getValue() + "\n";
					}
				}
				
				if (spi.getSignaturePolicyId().getSigPolicyHash() != null) {
					a += "Hash algorithm: " + spi.getSignaturePolicyId().getSigPolicyHash().getDigestMethod().getAlgorithm() + "\n" +
							"Hash value: " + spi.getSignaturePolicyId().getSigPolicyHash().getDigestValue().getValue() + "\n";
				}
				
				if (spi.getSignaturePolicyId().getSigPolicyQualifiers() != null) {
					for (SigPolicyQualifier spq : spi.getSignaturePolicyId().getSigPolicyQualifiers().getList()) {
						if (spq.getQualifier() instanceof SPURI) {
							a += "SigPolicyQualifier(URI): " + ((SPURI)spq.getQualifier()).getValue() + "\n";
						} else {
							a += "SigPolicyQualifier(user notice): " + ((SPUserNotice)spq.getQualifier()).getNoticeRef() + " | " + ((SPUserNotice)spq.getQualifier()).getExplicitText() + "\n";	
						}
					}
				}
		}
		
		return a;
	}
	
}

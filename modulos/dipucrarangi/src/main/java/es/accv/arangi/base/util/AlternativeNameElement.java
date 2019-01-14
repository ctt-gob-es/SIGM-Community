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
package es.accv.arangi.base.util;

import java.util.HashMap;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x500.X500Name;

/**
 * Representa cada elemento de las extensiones SubjectAlternativeName e IssuerAlternativeName
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class AlternativeNameElement {

	int type;
	Object value;
	public AlternativeNameElement(int type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}
	public AlternativeNameElement() {
		super();
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		
		//-- Por defecto
		this.value = value;
		
		//-- Si el valor es un X500Name se obtiene un Map y se guarda
		if (value instanceof X500Name) {
			
			HashMap<String,String> result = new HashMap<String,String> ();
			X500Name nameValue = (X500Name) value;
			for (int i=0;i<nameValue.getRDNs().length;i++) {
				ASN1ObjectIdentifier oid = nameValue.getRDNs()[i].getFirst().getType();
				if (nameValue.getRDNs()[i].getFirst().getValue() instanceof DERUTF8String) {
					result.put(oid.getId(), ((DERUTF8String)nameValue.getRDNs()[i].getFirst().getValue()).getString());
				} else {
					result.put(oid.getId(), ((DERPrintableString)nameValue.getRDNs()[i].getFirst().getValue()).getString());
				}
			}
			
			this.value = result;
		}

	}
	
}

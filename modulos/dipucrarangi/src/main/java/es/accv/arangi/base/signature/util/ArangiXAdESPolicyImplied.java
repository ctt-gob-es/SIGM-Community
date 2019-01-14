/**
 * Copyright 2012 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.signature.util;

import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SignaturePolicyIdentifier;

/**
 * Pólitica implícita
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 */
public class ArangiXAdESPolicyImplied {

	private SignaturePolicyIdentifier spi;

	public ArangiXAdESPolicyImplied() {
		super();
		spi = new SignaturePolicyIdentifier(XAdESSchemas.XAdES_132);
		spi.setSignaturePolicyImplied();
	}


	
}

/**
 * Copyright 2012 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.signature.util;

import java.net.URI;

import es.accv.arangi.base.signature.XAdESSignature;

/**
 * Clase que representa un objeto Object identifier según el punto 7.1.2 del estándar.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 *
 */
public class ObjectIdentifier extends es.mityc.firmaJava.libreria.xades.elementos.xades.ObjectIdentifier {
	
	public ObjectIdentifier(String description, URI uri) {
		super(XAdESSignature.DEFAULT_XADES_SCHEMA, uri, description);
	}
	
}

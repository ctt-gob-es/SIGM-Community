/**
 * Copyright 2012 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.signature.util;

/**
 * Al método attached se le indicará cuál es el nodo a firmar
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 */
public class XAdESAttachedNodeToSignObject implements XAdESAttachedNodeToSign {
	
	String idToSign;
	
	public XAdESAttachedNodeToSignObject(String idToSign) {
		super();
		this.idToSign = idToSign;
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.util.XAdESAttachedNodeToSign#getIdToSign()
	 */
	public String getIdToSign() {
		return idToSign;
	}

	public void setIdToSign(String idToSign) {
		this.idToSign = idToSign;
	}

}

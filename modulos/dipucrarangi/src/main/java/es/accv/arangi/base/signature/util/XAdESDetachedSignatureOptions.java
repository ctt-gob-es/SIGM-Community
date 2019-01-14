/**
 * Copyright 2012 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.signature.util;

import java.util.Arrays;

/**
 * A partir de la versión 1.4.7 se agrupan todas las opciones que se pueden pasar
 * a una firma XAdES detached en este objeto.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 */
public class XAdESDetachedSignatureOptions {
	
	private String digitalSignatureAlgorithm;
	private String tsaHashingAlgorithm;
	private XAdESDataObjectFormat dof;
	private String[] claimedRoles;
	private ArangiXAdESPolicyIdentifier policyIdentifier;
	private ArangiXAdESProductionPlace productionPlace;
	
	public XAdESDetachedSignatureOptions() {
		super();
	}
	
	public XAdESDetachedSignatureOptions(String digitalSignatureAlgorithm,
			String tsaHashingAlgorithm,
			XAdESDataObjectFormat dof,
			String[] claimedRoles,
			ArangiXAdESPolicyIdentifier policyIdentifier,
			ArangiXAdESProductionPlace productionPlace) {
		super();
		this.dof = dof;
		this.claimedRoles = claimedRoles;
		this.policyIdentifier = policyIdentifier;
		this.digitalSignatureAlgorithm = digitalSignatureAlgorithm;
		this.tsaHashingAlgorithm = tsaHashingAlgorithm;
		this.productionPlace = productionPlace;
	}

	public XAdESDataObjectFormat getDof() {
		return dof;
	}
	public void setDof(XAdESDataObjectFormat dof) {
		this.dof = dof;
	}
	public String[] getClaimedRoles() {
		return claimedRoles;
	}
	public void setClaimedRoles(String[] claimedRoles) {
		this.claimedRoles = claimedRoles;
	}
	public ArangiXAdESPolicyIdentifier getPolicyIdentifier() {
		return policyIdentifier;
	}
	public void setPolicyIdentifier(ArangiXAdESPolicyIdentifier policyIdentifier) {
		this.policyIdentifier = policyIdentifier;
	}

	public String getDigitalSignatureAlgorithm() {
		return digitalSignatureAlgorithm;
	}

	public void setDigitalSignatureAlgorithm(String digitalSignatureAlgorithm) {
		this.digitalSignatureAlgorithm = digitalSignatureAlgorithm;
	}

	public String getTsaHashingAlgorithm() {
		return tsaHashingAlgorithm;
	}

	public void setTsaHashingAlgorithm(String tsaHashingAlgorithm) {
		this.tsaHashingAlgorithm = tsaHashingAlgorithm;
	}

	public ArangiXAdESProductionPlace getProductionPlace() {
		return productionPlace;
	}

	public void setProductionPlace(ArangiXAdESProductionPlace productionPlace) {
		this.productionPlace = productionPlace;
	}
	
	@Override
	public String toString() {
		String a = "digitalSignatureAlgorithm=" + digitalSignatureAlgorithm +
				"XAdESDataObjectFormat=" + dof +
				"ArangiSignaturePolicyIdentifier=" +  policyIdentifier +
				"ProductionPlace=" + productionPlace;
		if (claimedRoles != null) {
			a += "claimedRoles=" + Arrays.asList(claimedRoles);
		}
		return a;
	}

}

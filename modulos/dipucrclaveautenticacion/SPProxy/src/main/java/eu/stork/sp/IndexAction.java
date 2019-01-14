package eu.stork.sp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.STORKAuthnRequest;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;

/**
 * 
 * @author iinigo This Action Generates a SAML Request with the data given by
 *         the user, then sends it to the selected peps
 * 
 */

public class IndexAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	private static final long serialVersionUID = 3660074009157921579L;

	static final Logger logger = LoggerFactory.getLogger(IndexAction.class
			.getName());

	private HttpServletRequest request;
	private String SAMLRequest;
	private String samlRequestXML;

	private static Properties configs;
	private static ArrayList<PersonalAttribute> attributeList;
	
	private List<String> proxyUrls;
	private String selectUrl;
	
	private static String spId;
	private static String providerName;
	private static String spSector;
	private static String spApplication;

	/* Requested parameters */
	private String pepsUrl;

	private String qaa;

	private String returnUrl;

	// Checkboxes para excluir mecanismos de autenticación
	private boolean checkAFirma;
	private boolean checkStork;
	private boolean checkSS;
	private boolean checkAEAT;
	private String excludedIdPList;
	private String forcedIdP = "none";
	
	// Checkboxes para permitir certificados de persona juridica
	private boolean allowLegalPerson = false;
	
	// Checkboxes para Single Sign On
	private boolean forceAuth = true;

	// Lista de IdPs para forzar
	private List<String> opcionesIdP;
	private String selectIdP;

	/**
	 * Fill the data in the JSP that is shown to the user in order to fill the
	 * requested data to generate a saml request
	 */
	public String populate() {

		try {
			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"Could not load configuration file", e.getMessage());
		}

		returnUrl = configs.getProperty(Constants.SP_RETURN);
		qaa = configs.getProperty(Constants.SP_QAALEVEL);
		spId = configs.getProperty(Constants.PROVIDER_NAME);
		providerName = configs.getProperty(Constants.PROVIDER_NAME);
		spSector = configs.getProperty(Constants.SP_SECTOR);
		spApplication = configs.getProperty(Constants.SP_APLICATION);
		pepsUrl = selectUrl;

		int nAttr = Integer.parseInt(configs
				.getProperty(Constants.ATTRIBUTE_NUMBER));

		attributeList = new ArrayList<PersonalAttribute>();

		for (int i = 1; i <= nAttr; i++) {
			PersonalAttribute pa = new PersonalAttribute();
			pa.setName(configs.getProperty("attribute" + i + ".name"));

			String value = configs.getProperty("attribute" + i + ".value");
			if (value != null) {
				if (pa.getName().contains("signedDoc"))
					value = String.format(value, UUID.randomUUID().toString());
				ArrayList<String> aux = new ArrayList<String>();
				aux.add(value);
				pa.setValue(aux);
			}
			pa.setType("personal");

			attributeList.add(pa);
		}

		nAttr = Integer.parseInt(configs
				.getProperty(Constants.BUSINESS_ATTRIBUTE_NUMBER));

		for (int i = 1; i <= nAttr; i++) {
			PersonalAttribute pa = new PersonalAttribute();
			pa.setName(configs.getProperty("businessAttribute" + i + ".name"));

			String value = configs.getProperty("businessAttribute" + i
					+ ".value");
			if (value != null) {
				ArrayList<String> aux = new ArrayList<String>();
				aux.add(value);
				pa.setValue(aux);
			}
			pa.setType("business");

			attributeList.add(pa);
		}

		nAttr = Integer.parseInt(configs
				.getProperty(Constants.LEGAL_ATTRIBUTE_NUMBER));

		for (int i = 1; i <= nAttr; i++) {
			PersonalAttribute pa = new PersonalAttribute();
			pa.setName(configs.getProperty("legalAttribute" + i + ".name"));

			String value = configs.getProperty("legalAttribute" + i + ".value");
			if (value != null) {
				ArrayList<String> aux = new ArrayList<String>();
				aux.add(value);
				pa.setValue(aux);
			}
			pa.setType("legal");

			attributeList.add(pa);
		}

		// Rellenar la lista para forzar un IdP
		opcionesIdP = new ArrayList<String>();
		opcionesIdP.add("@Firma");
		opcionesIdP.add("Stork");
		opcionesIdP.add("Seguridad Social");
		opcionesIdP.add("Agencia Tributaria");

		return "populate";
	}

	/**
	 * Generates the Saml Request with the data given by the user
	 */
	public String execute() {
		
		IPersonalAttributeList pAttList = new PersonalAttributeList();

		// Iterate through the request parameters looking for SAML Engine
		// attributes
		for (Enumeration enu = request.getParameterNames(); enu
				.hasMoreElements();) {
			String parameterName = (String) enu.nextElement();
			if (configs.containsValue(parameterName)) {
				// Iterate through the request parameters looking for SAML
				// Engine attribute types
				for (Enumeration en = request.getParameterNames(); en
						.hasMoreElements();) {
					// Create a personal attribute with the attribute and
					// its type
					String parameterType = (String) en.nextElement();
					if (parameterType.equals(parameterName + "Type")) {
						if (!"none".equals(request.getParameter(parameterType))) {
							PersonalAttribute att = new PersonalAttribute();
							att.setName(request.getParameter(parameterName));
							if ("true".equals(request
									.getParameter(parameterType)))
								att.setIsRequired(true);
							else
								att.setIsRequired(false);
							// Iterate through the request parameters
							// looking for SAML Engine attribute types
							for (Enumeration e = request.getParameterNames(); e
									.hasMoreElements();) {
								// Create a personal attribute with the
								// attribute and its type
								String parameterValue = (String) e
										.nextElement();
								if (parameterValue.equals(parameterName
										+ "Value")) {
									ArrayList<String> aux = new ArrayList<String>();
									aux.add(request
											.getParameter(parameterValue));
									att.setValue(aux);
								}
							}
							pAttList.add(att);
						}
					}
				}
			}
		}

		byte[] token = null;

		STORKAuthnRequest authnRequest = new STORKAuthnRequest();

		authnRequest.setDestination(pepsUrl);
		authnRequest.setProviderName(providerName);
		authnRequest.setQaa(Integer.parseInt(qaa));
		authnRequest.setPersonalAttributeList(pAttList);
		authnRequest.setAssertionConsumerServiceURL(returnUrl);

		// new parameters
		authnRequest.setSpSector(spSector);
		authnRequest.setSpApplication(spApplication);

		// V-IDP parameters
		authnRequest.setSPID(spId);
		authnRequest.setForceAuthN(isForceAuth());

		try {
			STORKSAMLEngine engine = STORKSAMLEngine
					.getInstance(Constants.SP_CONF);
			authnRequest = engine.generateSTORKAuthnRequest(authnRequest);
		} catch (STORKSAMLEngineException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"Could not generate token for Saml Request",
					e.getErrorMessage());
		}

		token = authnRequest.getTokenSaml();

		SAMLRequest = PEPSUtil.encodeSAMLToken(token);
		samlRequestXML = new String(token);
		
		generateExcludedIdPList();

		return Action.SUCCESS;
	}

	/**
	 * Generates the default Saml Request with eIdentifier, givenName and
	 * surname
	 */
	public String defaultRequest() {
		excludedIdPList = "none";
		forcedIdP = "none";

		try {
			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"Could not load configuration file", e.getMessage());
		}

		returnUrl = configs.getProperty(Constants.SP_RETURN);
		qaa = configs.getProperty(Constants.SP_QAALEVEL);
		spId = configs.getProperty(Constants.PROVIDER_NAME);
		providerName = configs.getProperty(Constants.PROVIDER_NAME);
		spSector = configs.getProperty(Constants.SP_SECTOR);
		spApplication = configs.getProperty(Constants.SP_APLICATION);
		
		if(selectUrl==null)
			selectUrl = configs.getProperty(Constants.SP_URL1);
		
		pepsUrl = selectUrl;

		IPersonalAttributeList pAttList = new PersonalAttributeList();

		/* eIdentifier */
		PersonalAttribute att = new PersonalAttribute();
		att.setName(configs.getProperty("attribute1.name"));
		att.setIsRequired(true);
		pAttList.add(att);

		/* givenName */
		att = new PersonalAttribute();
		att.setName(configs.getProperty("attribute2.name"));
		att.setIsRequired(true);
		pAttList.add(att);

		/* surname */
		att = new PersonalAttribute();
		att.setName(configs.getProperty("attribute3.name"));
		att.setIsRequired(true);
		pAttList.add(att);

		byte[] token = null;

		STORKAuthnRequest authnRequest = new STORKAuthnRequest();

		authnRequest.setDestination(pepsUrl);
		authnRequest.setProviderName(providerName);
		authnRequest.setQaa(Integer.parseInt(qaa));
		authnRequest.setPersonalAttributeList(pAttList);
		authnRequest.setAssertionConsumerServiceURL(returnUrl);

		// new parameters
		authnRequest.setSpSector(spSector);
		authnRequest.setSpApplication(spApplication);

		// V-IDP parameters
		authnRequest.setSPID(spId);

		try {
			STORKSAMLEngine engine = STORKSAMLEngine
					.getInstance(Constants.SP_CONF);
			authnRequest = engine.generateSTORKAuthnRequest(authnRequest);
		} catch (STORKSAMLEngineException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"Could not generate token for Saml Request",
					e.getErrorMessage());
		}

		token = authnRequest.getTokenSaml();

		SAMLRequest = PEPSUtil.encodeSAMLToken(token);
		samlRequestXML = new String(token);

		return "default";
	}
		
	private void generateExcludedIdPList() {
		excludedIdPList = new String();
		if (selectIdP != null) {
			if (selectIdP.equals("none")) {
				if (checkAFirma)
					excludedIdPList = excludedIdPList.concat("aFirma;");
				if (checkStork)
					excludedIdPList = excludedIdPList.concat("Stork;");	
				if (checkSS)
					excludedIdPList = excludedIdPList.concat("SS;");
				if (checkAEAT)
					excludedIdPList = excludedIdPList.concat("AEAT;");
				if (excludedIdPList.isEmpty())
					excludedIdPList = "none";
			} else {
				if (selectIdP.equals("@Firma"))
					forcedIdP = "aFirma";
				if (selectIdP.equals("Stork"))
					forcedIdP = "Stork";
				if (selectIdP.equals("Agencia Tributaria"))
					forcedIdP = "AEAT";
				if (selectIdP.equals("Seguridad Social"))
					forcedIdP = "SS";
				excludedIdPList = "none";
			}
		}
	}

	public String loadProxyUrls() {
		try {
			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"Could not load configuration file", e.getMessage());
		}
		
		int proxyNumber = Integer.parseInt(configs.getProperty(Constants.COUNTRY_URL_NUMBER));
		proxyUrls = new ArrayList<String>();
		
		for (int i=0; i<proxyNumber; i++) {
			proxyUrls.add(configs.getProperty(Constants.COUNTRY_URL + (i+1)));
		}
		
		return Action.SUCCESS;	
	}
	
	public void setAttrList(ArrayList<PersonalAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	public ArrayList<PersonalAttribute> getAttributeList() {
		return attributeList;
	}

	public void setSAMLRequest(String samlToken) {
		this.SAMLRequest = samlToken;
	}

	public String getSAMLRequest() {
		return SAMLRequest;
	}

	public String getQaa() {
		return qaa;
	}

	public void setQaa(String qaa) {
		this.qaa = qaa;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getSelectUrl() {
		return selectUrl;
	}

	public void setSelectUrl(String selectUrl) {
		this.selectUrl = selectUrl;
	}

	public List<String> getProxyUrls() {
		return proxyUrls;
	}

	public void setProxyUrls(List<String> proxyUrls) {
		this.proxyUrls = proxyUrls;
	}

	public String getSamlRequestXML() {
		return samlRequestXML;
	}

	public void setSamlRequestXML(String samlRequestXML) {
		this.samlRequestXML = samlRequestXML;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getPepsUrl() {
		return pepsUrl;
	}

	public void setPepsUrl(String pepsUrl) {
		this.pepsUrl = pepsUrl;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
	}

	public boolean isCheckAFirma() {
		return checkAFirma;
	}

	public void setCheckAFirma(boolean checkAFirma) {
		this.checkAFirma = checkAFirma;
	}

	public boolean isCheckStork() {
		return checkStork;
	}

	public void setCheckStork(boolean checkStork) {
		this.checkStork = checkStork;
	}

	public boolean isCheckSS() {
		return checkSS;
	}

	public String getExcludedIdPList() {
		return excludedIdPList;
	}

	public void setExcludedIdPList(String excludedIdPList) {
		this.excludedIdPList = excludedIdPList;
	}

	public String getForcedIdP() {
		return forcedIdP;
	}

	public void setForcedIdP(String forcedIdP) {
		this.forcedIdP = forcedIdP;
	}

	public boolean isAllowLegalPerson() {
		return allowLegalPerson;
	}

	public void setAllowLegalPerson(boolean allowLegalPerson) {
		this.allowLegalPerson = allowLegalPerson;
	}

	public boolean isForceAuth() {
		return forceAuth;
	}

	public void setForceAuth(boolean forceAuth) {
		this.forceAuth = forceAuth;
	}

	public void setCheckSS(boolean checkSS) {
		this.checkSS = checkSS;
	}

	public boolean isCheckAEAT() {
		return checkAEAT;
	}

	public void setCheckAEAT(boolean checkAEAT) {
		this.checkAEAT = checkAEAT;
	}

	public List<String> getOpcionesIdP() {
		return opcionesIdP;
	}

	public void setOpcionesIdP(List<String> opcionesIdP) {
		this.opcionesIdP = opcionesIdP;
	}

	public String getSelectIdP() {
		return selectIdP;
	}

	public void setSelectIdP(String selectIdP) {
		this.selectIdP = selectIdP;
	}
}

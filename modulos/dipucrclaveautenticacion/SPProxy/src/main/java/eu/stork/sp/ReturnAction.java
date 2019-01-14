package eu.stork.sp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.commons.STORKStatusCode;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;

/**
 * This Action recives a SAML Response, shows it to the user and then validates
 * it getting the attributes values
 * 
 * @author iinigo
 * 
 */
/**
 * @author agustin
 *
 */
public class ReturnAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	private static final long serialVersionUID = 3660074009157921579L;

	static final Logger logger = LoggerFactory.getLogger(IndexAction.class
			.getName());

	private String SAMLResponse;
	private String samlResponseXML;

	private ArrayList<PersonalAttribute> attrList;

	private ArrayList<PersonalAttribute> spanishAttrList;
	
	private String attrListString;

	private HttpServletResponse response;	

	private HttpServletRequest request;	
	
	private static Properties configs;
	private static Properties spanishProperties;

	private static String providerName;
	private static String homepage = null;
	
	private String isLegalPerson = "none";
	private String oid;
	private String inheritedFamilyName= "";
	private String surname="";
	private String scope;

	/**
	 * Translates the SAMLResponse to XML format in order to be shown in the JSP
	 * 
	 * @return
	 */
	public String execute() {

		try {
			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"No se ha podido cargar el fichero de configuraciones", e.getMessage());
		}

		providerName = configs.getProperty(Constants.PROVIDER_NAME);

		byte[] decSamlToken = PEPSUtil.decodeSAMLToken(SAMLResponse);
		samlResponseXML = new String(decSamlToken);
		
		

		return Action.SUCCESS;
	}

	/**
	 * Validates the response and displays the value of the requested attributes
	 * 
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public String populate() throws ServletException, IOException {
		
		try {
			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"No se ha podido cargar el fichero de configuraciones", e.getMessage());
		}
		

		
		homepage = configs.getProperty("sp.url");

		STORKAuthnResponse authnResponse = null;
		IPersonalAttributeList personalAttributeList = null;

		// Decodes SAML Response
		byte[] decSamlToken = PEPSUtil.decodeSAMLToken(SAMLResponse);

		// Get SAMLEngine instance
		STORKSAMLEngine engine = STORKSAMLEngine.getInstance(Constants.SP_CONF);

		try {
			// validate SAML Token
			authnResponse = engine.validateSTORKAuthnResponse(decSamlToken,	(String) request.getRemoteHost());
		} catch (STORKSAMLEngineException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException("No se ha podido validar el token de la SAML Response", e.getErrorMessage());
		}			
		
		if (authnResponse.isFail()) {
			throw new ApplicationSpecificServiceException("La SAML Response ha dado un fallo", authnResponse.getMessage());			
		} else {			
			//Get attributes
			personalAttributeList = authnResponse.getPersonalAttributeList();
			
// Workaround para pruebas en local			
//			PersonalAttribute pre_inherited = new PersonalAttribute();
//			pre_inherited.setName("inheritedFamilyName");
//			pre_inherited.setIsRequired(false);
//			List<String> pre_values = new ArrayList<String>();
//			pre_values.add("FELIZ");
//			pre_inherited.setValue(pre_values);
//			pre_inherited.setStatus(STORKStatusCode.STATUS_AVAILABLE.toString());
//			personalAttributeList.add(pre_inherited);			
			
			if (personalAttributeList.containsKey(Constants.CLAVE_AUTHN_RESPONSE_INHERITED_FAMILY_NAME) && personalAttributeList.containsKey(Constants.CLAVE_AUTHN_RESPONSE_SURNAME)) {
				PersonalAttribute inherited = personalAttributeList.get(Constants.CLAVE_AUTHN_RESPONSE_INHERITED_FAMILY_NAME);
				PersonalAttribute surname = personalAttributeList.get(Constants.CLAVE_AUTHN_RESPONSE_SURNAME);
				if (surname != null && !surname.getValue().equals(inherited.getValue())) {
					PersonalAttribute apellido2 = new PersonalAttribute();
					String inheritedValue = inherited.getValue().toString().substring(1, inherited.getValue().toString().length()-1);
					String surnameValue = surname.getValue().toString().substring(1, surname.getValue().toString().length()-1);
					apellido2.setName(Constants.CLAVE_AUTHN_RESPONSE_APELLIDO2);
					apellido2.setIsRequired(inherited.isRequired());
					List<String> values = new ArrayList<String>();
					values.add(surnameValue.substring(inheritedValue.length()+1));
					apellido2.setValue(values);
					apellido2.setStatus(STORKStatusCode.STATUS_AVAILABLE.toString());
					personalAttributeList.add(apellido2);
					
					//Paso a Sigem parametros
					ServletActionContext.getServletContext().setAttribute(Constants.CLAVE_AUTHN_RESPONSE_INHERITED_FAMILY_NAME, inheritedValue);
					ServletActionContext.getServletContext().setAttribute(Constants.CLAVE_AUTHN_RESPONSE_SURNAME, surnameValue);
					
				}
			}
			
			setAttrList(new ArrayList<PersonalAttribute>(personalAttributeList.values()));	
			spanishAttrList = new ArrayList<PersonalAttribute>(attrList);
			ServletActionContext.getServletContext().setAttribute("attrList", attrList.toString());
			
			ServletActionContext.getServletContext().setAttribute(Constants.CLAVE_ATTRLIST_NAME,  personalAttributeList.get(Constants.CLAVE_ATTRLIST_NAME).toString().split(Constants.CLAVE_SEPARADOR)[2].replace(Constants.CLAVE_SEPARADOR_1,"").replace(Constants.CLAVE_SEPARADOR_2,""));
			ServletActionContext.getServletContext().setAttribute(Constants.CLAVE_ATTRLIST_SURNAME,  personalAttributeList.get(Constants.CLAVE_ATTRLIST_SURNAME).toString().split(Constants.CLAVE_SEPARADOR)[2].replace(Constants.CLAVE_SEPARADOR_1,"").replace(Constants.CLAVE_SEPARADOR_2,""));
			ServletActionContext.getServletContext().setAttribute(Constants.CLAVE_ATTRLIST_EIDENTIFIER,  personalAttributeList.get(Constants.CLAVE_ATTRLIST_EIDENTIFIER).toString().split(Constants.CLAVE_SEPARADOR)[2].replace(Constants.CLAVE_SEPARADOR_1,"").replace(Constants.CLAVE_SEPARADOR_2,"").split(Constants.CLAVE_SEPARADOR_3)[2]);
			ServletActionContext.getServletContext().setAttribute(Constants.CLAVE_ATTRLIST_NIVEL_CITIZENQAALEVEL,  personalAttributeList.get(Constants.CLAVE_ATTRLIST_NIVEL_CITIZENQAALEVEL).toString().split(Constants.CLAVE_SEPARADOR)[2].replace(Constants.CLAVE_SEPARADOR_1,"").replace(Constants.CLAVE_SEPARADOR_2,""));
						
			try {
				spanishProperties = SPUtil.loadConfigs(Constants.SPANISH_SP_PROPERTIES);
			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new ApplicationSpecificServiceException("No se ha podido cargar el fichero de configuración en espa&nacute;ol", e.getMessage());
			}
			
			for (PersonalAttribute pAt : spanishAttrList){
				pAt.setName(spanishProperties.getProperty(pAt.getName()));								
			}
			
			for (Assertion ass : authnResponse.getAssertions()){
				if (ass.getIssuer() != null){
					scope = ass.getIssuer().getValue();
					
					switch(scope.substring(0, 6)) {
						case Constants.SCOPE_AFIRMA:
							scope = "DNIe / Certificado electrónico";
						break;
						case Constants.SCOPE_AEAT:
							scope = "Cl@ve PIN";
						break;
						case Constants.SCOPE_SS:
							scope = "Cl@ve permanente";
						break;
						case Constants.SCOPE_STORK + "-":
							scope = "Ciudadanos UE - " + scope.substring(6);
					}
					
					break;
				}
			}
			
			ServletActionContext.getServletContext().setAttribute(Constants.CLAVE_SCOPE, scope);

			//request.setAttribute("request_setAttribute", "request_setAttributes");
			//request.getSession().setAttribute("request_getSession_setAttribute", "request_getSession_setAttribute");
			//request.getSession(false).setAttribute("request_getSessionfalse_setAttribute", "request_getSessionfalse_setAttribute");
			//request.getSession(true).setAttribute("request_getSessiontrue_setAttribute", "request_getSessiontrue_setAttribute");
			
			String redirect = "https://"+request.getServerName()+":4443/SIGEM_AutenticacionWeb/validacionClave.do";
			//request.getRequestDispatcher(redirect);
			//request.getRequestDispatcher(redirect).forward(request, response);
			//request.getRequestDispatcher("MyFirstJSP.jsp").forward(request, response);
			response.sendRedirect(redirect);
			
			//ServletActionContext.getServletContext().getContext("/SIGEM_AutenticacionWeb").getRequestDispatcher("/validacionClave.do").forward(request, response);
			//ServletActionContext.getServletContext().getContext("/SIGEM_AutenticacionWeb").getRequestDispatcher("/validacionClave.do").forward(request, response);
			
			return "SALIR";
			//return "populate";
		}
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setAttrList(ArrayList<PersonalAttribute> attrList) {
		this.attrList = attrList;
	}

	public ArrayList<PersonalAttribute> getAttrList() {
		return attrList;
	}

	public ArrayList<PersonalAttribute> getSpanishAttrList() {
		return spanishAttrList;
	}

	public void setSpanishAttrList(ArrayList<PersonalAttribute> spanishAttrList) {
		this.spanishAttrList = spanishAttrList;
	}

	public String getAttrListString() {
		return attrListString;
	}

	public void setAttrListString(String attrListString) {
		this.attrListString = attrListString;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public String getInheritedFamilyName() {
		return inheritedFamilyName;
	}

	public void setInheritedFamilyName(String inheritedFamilyName) {
		this.inheritedFamilyName = inheritedFamilyName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the isLegalPerson
	 */
	public String getIsLegalPerson() {
		return isLegalPerson;
	}

	/**
	 * @param isLegalPerson the isLegalPerson to set
	 */
	public void setIsLegalPerson(String isLegalPerson) {
		this.isLegalPerson = isLegalPerson;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		ReturnAction.homepage = homepage;
	}

	public String getSAMLResponse() {
		return SAMLResponse;
	}

	public void setSAMLResponse(String samlResponse) {
		this.SAMLResponse = samlResponse;
	}

	public String getSamlResponseXML() {
		return samlResponseXML;
	}

	public void setSamlResponseXML(String samlResponseXML) {
		this.samlResponseXML = samlResponseXML;
	}

}
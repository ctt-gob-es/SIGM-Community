/*
 * This work is Open Source and licensed by the European Commission under the
 * conditions of the European Public License v1.1 
 *  
 * (http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1); 
 * 
 * any use of this file implies acceptance of the conditions of this license. 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */
package eu.stork.peps.auth.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;

/**
 * This class is a bean used to store the information relative to the
 * STORKAuthnResponse.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.15 $, $Date: 2010-11-17 05:15:28 $
 */
public final class STORKAuthnResponse implements Serializable, Cloneable {

    /**
     * Response Id.
     */
    private String samlId;

    /**
     * Authentication failed?
     */
    private boolean fail;

    /**
     * Status code.
     */
    private String statusCode;

    /**
     * Secondary status code.
     */
    private String subStatusCode;

    /**
     * Audience restriction.
     */
    private transient String audienceRest;

    /**
     * Error message.
     */
    private String message;

    /**
     * Id of the request that originated this response.
     */
    private String inResponseTo;

    /**
     * Expiration date.
     */
    private DateTime notOnOrAfter;

    /**
     * Creation date.
     */
    private DateTime notBefore;

    /**
     * The SAML token.
     */
    private byte[] tokenSaml = new byte[0];

    /**
     * Country.
     */
    private String country;

    /**
     * Citizen's personal attribute list.
     */
    private transient IPersonalAttributeList attributeList = new PersonalAttributeList();

    /**
     * List of all assertions in response *
     */
    private List<Assertion> assertions;

    /**
     * The complete list from all assertions *
     */
    private transient IPersonalAttributeList totalAttributeList = new PersonalAttributeList();

    /**
     * All personal attribute lists *
     */
    private List<IPersonalAttributeList> attributeLists;

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(STORKAuthnResponse.class.getName());

    /**
     * Getter for the subStatusCode.
     *
     * @return The subStatusCode value.
     */
    public String getSubStatusCode() {
        return subStatusCode;
    }

    /**
     * Setter for the subStatusCode.
     *
     * @param samlSubStatusCode the new subStatusCode value.
     */
    public void setSubStatusCode(final String samlSubStatusCode) {
        this.subStatusCode = samlSubStatusCode;
    }

    /**
     * Getter for audienceRest.
     *
     * @return The audienceRest value.
     */
    public String getAudienceRestriction() {
        return audienceRest;
    }

    /**
     * Setter for audienceRest.
     *
     * @param audRest the new audienceRest value.
     */
    public void setAudienceRestriction(final String audRest) {
        this.audienceRest = audRest;
    }

    /**
     * Getter for the samlToken.
     *
     * @return The samlToken value.
     */
    public byte[] getTokenSaml() {
        return tokenSaml.clone();
    }

    /**
     * Setter for samlToken.
     *
     * @param samlToken the new tokenSaml value.
     */
    public void setTokenSaml(final byte[] samlToken) {
        if (samlToken != null) {
            this.tokenSaml = samlToken.clone();
        }
    }

    /**
     * Getter for the country name.
     *
     * @return The country name value.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter for the country name.
     *
     * @param cCountry the new country name value.
     */
    public void setCountry(final String cCountry) {
        this.country = cCountry;
    }

    /**
     * Getter for pal value.
     *
     * @return The pal value.
     *
     * @see PersonalAttributeList
     */
    public IPersonalAttributeList getPersonalAttributeList() {
        return (IPersonalAttributeList) attributeList.clone();
    }

    /**
     * Setter for the Personal Attribute List value.
     *
     * @param attrList the new value.
     *
     * @see PersonalAttributeList
     */
    public void setPersonalAttributeList(final IPersonalAttributeList attrList) {
        if (attrList != null) {
            this.attributeList = attrList;
        }
    }

    /**
     * Getter for the inResponseTo value.
     *
     * @return The inResponseTo value.
     */
    public String getInResponseTo() {
        return inResponseTo;
    }

    /**
     * Setter for the inResponseTo value.
     *
     * @param samlInResponseTo the new inResponseTo value.
     */
    public void setInResponseTo(final String samlInResponseTo) {
        this.inResponseTo = samlInResponseTo;
    }

    /**
     * Getter for the fail value.
     *
     * @return The fail value.
     */
    public boolean isFail() {
        return fail;
    }

    /**
     * Setter for the fail value.
     *
     * @param failVal the new fail value.
     */
    public void setFail(final boolean failVal) {
        this.fail = failVal;
    }

    /**
     * Getter for the message value.
     *
     * @return The message value.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for the message value.
     *
     * @param msg the new message value.
     */
    public void setMessage(final String msg) {
        this.message = msg;
    }

    /**
     * Getter for the statusCode value.
     *
     * @return The statusCode value.
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Setter for the statusCode value.
     *
     * @param status the new statusCode value.
     */
    public void setStatusCode(final String status) {
        this.statusCode = status;
    }

    /**
     * Getter for the samlId value.
     *
     * @return The samlId value.
     */
    public String getSamlId() {
        return samlId;
    }

    /**
     * Setter for the samlId value.
     *
     * @param nSamlId the new samlId value.
     */
    public void setSamlId(final String nSamlId) {
        this.samlId = nSamlId;
    }

    /**
     * Getter for the notOnOrAfter value.
     *
     * @return The notOnOrAfter value.
     *
     * @see DateTime
     */
    public DateTime getNotOnOrAfter() {
        return this.notOnOrAfter;
    }

    /**
     * Setter for the notOnOrAfter value.
     *
     * @param nOnOrAfter the new notOnOrAfter value.
     *
     * @see DateTime
     */
    public void setNotOnOrAfter(final DateTime nOnOrAfter) {
        this.notOnOrAfter = nOnOrAfter;
    }

    /**
     * Getter for the notBefore value.
     *
     * @return The notBefore value.
     *
     * @see DateTime
     */
    public DateTime getNotBefore() {
        return notBefore;
    }

    /**
     * Setter for the notBefore value.
     *
     * @param nBefore the new notBefore value.
     *
     * @see DateTime
     */
    public void setNotBefore(final DateTime nBefore) {
        this.notBefore = nBefore;
    }

    public void setAssertions(List<Assertion> newAssert) {
        this.assertions = newAssert;
    }

    public List<Assertion> getAssertions() {
        return assertions;
    }

    /**
     * Getter for the toal pal value.
     *
     * @return The total pal value.
     *
     * @see PersonalAttributeList
     */
    public IPersonalAttributeList getTotalPersonalAttributeList() {
        return (IPersonalAttributeList) totalAttributeList.clone();
    }

    public List<PersonalAttribute> getNormalizedPersonalAttributeList() {
        List<PersonalAttribute> returnAttrList = new ArrayList<PersonalAttribute>();

        if (this.totalAttributeList.isEmpty()) {
            this.totalAttributeList = this.attributeList;
        }

        for (PersonalAttribute pa : this.totalAttributeList) {
            //Get the shortname of the attribute by removing
            //the attached assertionId, if there is one and
            //put the shortname as the attribute name
            pa.setName(pa.getName().split("_")[0]);
            //We add it to the return list.
            returnAttrList.add(pa);
        }
        return returnAttrList;
    }

    /**
     * Setter for the total Personal Attribute List value.
     *
     * @param attrList the new value.
     *
     * @see PersonalAttributeList
     */
    public void setTotalPersonalAttributeList(final IPersonalAttributeList attrList) {
        if (attrList != null) {
            this.totalAttributeList = attrList;
        }
    }

    /**
     * Getter for personal attribute lists
     *
     * @return The lists
     *
     * @see PersonalAttributeList
     */
    public List<IPersonalAttributeList> getPersonalAttributeLists() {
        return attributeLists;
    }

    /**
     * Setter for the Personal Attribute List value.
     *
     * @param attrList the new value.
     *
     * @see PersonalAttributeList
     */
    public void setPersonalAttributeLists(final List<IPersonalAttributeList> attrLists) {
        if (attrLists != null) {
            this.attributeLists = attrLists;
        }
    }

}

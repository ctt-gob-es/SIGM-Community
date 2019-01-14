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
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import eu.stork.peps.auth.commons.exceptions.InternalErrorPEPSException;

/**
 * This class is a bean used to store the information relative to the
 * PersonalAttribute.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.22 $, $Date: 2010-11-17 05:15:28 $
 */
public final class PersonalAttribute implements Serializable, Cloneable {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = 2612951678412632174L;

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(PersonalAttribute.class
            .getName());

    /**
     * Name of the personal attribute.
     */
    private String name;

    /**
     * Values of the personal attribute.
     */
    private List<String> value = new ArrayList<String>();

    /**
     * Type of the personal attribute.
     */
    private String type;

    /**
     * Complex values of the personal attribute.
     */
    private List<HashMap<String, String>> complexValue = new Vector<HashMap<String, String>>();

    /**
     * Is the personal attribute mandatory?
     */
    private transient boolean required;

    /**
     * Returned status of the attribute from the IdP.
     */
    private String status;

    /**
     * Name of the personal attribute.
     */
    private String friendlyName;

    /**
     * Empty Constructor.
     */
    public PersonalAttribute() {
        super();
    }

    /**
     * PersonalAttribute Constructor for complex values.
     *
     * @param attrName The attribute name.
     * @param attrIsRequired The attribute type value.
     * @param attrComplexValue The attribute's value.
     * @param attrStatus The attribute's status value.
     */
    public PersonalAttribute(final String attrName, final boolean attrIsRequired,
            final List<String> attrComplexValue, final String attrStatus) {
        this.setName(attrName);
        this.setIsRequired(attrIsRequired);
        this.setValue(attrComplexValue);
        this.setStatus(attrStatus);
    }

    /**
     * PersonalAttribute Constructor for complex values.
     *
     * @param attrName The attribute name.
     * @param attrIsRequired The attribute type value.
     * @param attrComplexValue The attribute's complex value.
     * @param attrStatus The attribute's status value.
     */
    public PersonalAttribute(final String attrName, final boolean attrIsRequired,
            final HashMap<String, String> attrComplexValue, final String attrStatus) {
        this.setName(attrName);
        this.setIsRequired(attrIsRequired);
        this.setComplexValue(attrComplexValue);
        this.setStatus(attrStatus);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object clone() {

        try {
            final PersonalAttribute personalAttr = (PersonalAttribute) super.clone();
            personalAttr.setIsRequired(this.isRequired());
            personalAttr.setName(this.getName());
            personalAttr.setStatus(this.getStatus());
            if (!isEmptyValue()) {
                final List<String> val
                        = (List<String>) ((ArrayList<String>) this.getValue()).clone();
                personalAttr.setValue(val);
            }
            if (!isEmptyComplexValue()) {
                personalAttr.addComplexValues(this.getComplexValues());
            }
            return personalAttr;
        } catch (final CloneNotSupportedException e) {
            // assert false;
            LOG.trace("Nothing to do.");
            throw new InternalErrorPEPSException(
                    PEPSUtil.getConfig(PEPSErrors.INTERNAL_ERROR.errorCode()),
                    PEPSUtil.getConfig(PEPSErrors.INTERNAL_ERROR.errorMessage()), e);
        }
    }

    /**
     * Getter for the required value.
     *
     * @return The required value.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Setter for the required value.
     *
     * @param attrIsRequired this attribute?
     */
    public void setIsRequired(final boolean attrIsRequired) {
        this.required = attrIsRequired;
    }

    /**
     * Getter for the name value.
     *
     * @return The name value.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name value.
     *
     * @param attrName The personal attribute name.
     */
    public void setName(final String attrName) {
        this.name = attrName;
    }

    /**
     * Getter for the value.
     *
     * @return The list of values.
     */
    public List<String> getValue() {
        return value;
    }

    /**
     * Setter for the list of values.
     *
     * @param attrValue The personal attribute value.
     */
    public void setValue(final List<String> attrValue) {
        if (attrValue != null) {
            this.value = attrValue;
        }
    }

    /**
     * Add new value to list of values.
     *
     * @param attrValue The personal attribute value.
     */
    public void addValue(final String attrValue) {
        if (attrValue != null) {
            this.value.add(attrValue);
        }
    }

    /**
     * Getter for the type value.
     *
     * @return The name value.
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the type value.
     *
     * @param attrName The personal attribute type.
     */
    public void setType(final String attrType) {
        this.type = attrType;
    }

    /**
     * Getter for the status.
     *
     * @return The status value.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter for the status value.
     *
     * @param attrStatus The personal attribute status.
     */
    public void setStatus(final String attrStatus) {
        this.status = attrStatus;
    }

    /**
     * Getter for the complex value.
     *
     * @return The complex value.
     */
    public HashMap<String, String> getComplexValue() {
        if (complexValue.size() > 0) {
            return complexValue.get(0);
        } else {
            return new HashMap<String, String>();
        }
    }

    /**
     * Getter for the complex values.
     *
     * @return The complex value.
     */
    public List<HashMap<String, String>> getComplexValues() {
        return complexValue;
    }

    /**
     * Setter for the complex value.
     *
     * @param complexVal The personal attribute Complex value.
     */
    public void setComplexValue(final HashMap<String, String> complexVal) {
        if (complexVal != null) {
            this.complexValue.add(complexVal);
        }
    }

    /**
     * Setter for the complex values.
     *
     * @param complexVal The personal attribute Complex values.
     */
    public void addComplexValues(final List<HashMap<String, String>> complexVals) {
        this.complexValue.addAll(complexVals);
    }

    /**
     * Getter for the personal's friendly name.
     *
     * @return The personal's friendly name value.
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Setter for the personal's friendly name.
     *
     * @param fName The personal's friendly name.
     */
    public void setFriendlyName(final String fName) {
        this.friendlyName = fName;
    }

    /**
     * Return true the value is empty.
     *
     * @return True if the value is empty "[]";
     */
    public boolean isEmptyValue() {
        return value.isEmpty() || (value.size() == 1 && value.get(0).length() == 0);
    }

    /**
     * Returns true if the Complex Value is empty.
     *
     * @return True if the Complex Value is empty;
     */
    public boolean isEmptyComplexValue() {
        return complexValue.isEmpty() || complexValue.get(0).isEmpty();
    }

    /**
     * Returns true if the Status is empty.
     *
     * @return True if the Status is empty;
     */
    public boolean isEmptyStatus() {
        return (status == null || status.length() == 0);
    }

    /**
     * Prints the PersonalAttribute in the following format.
     * name:required:[v,a,l,u,e,s]|[v=a,l=u,e=s]:status;
     *
     * @return The PersonalAttribute as a string.
     */
    public String toString() {
        final StringBuilder strBuild = new StringBuilder();

        AttributeUtil.appendIfNotNull(strBuild, getName());
        strBuild.append(PEPSValues.ATTRIBUTE_TUPLE_SEP.toString());
        AttributeUtil.appendIfNotNull(strBuild, String.valueOf(isRequired()));
        strBuild.append(PEPSValues.ATTRIBUTE_TUPLE_SEP.toString());
        strBuild.append('[');

        if (isEmptyValue()) {
            if (!isEmptyComplexValue()) {
                AttributeUtil.appendIfNotNull(strBuild, AttributeUtil.mapToString(
                        getComplexValue(), PEPSValues.ATTRIBUTE_VALUE_SEP.toString()));
            }
        } else {
            AttributeUtil.appendIfNotNull(
                    strBuild,
                    AttributeUtil.listToString(getValue(),
                            PEPSValues.ATTRIBUTE_VALUE_SEP.toString()));
        }

        strBuild.append(']');
        strBuild.append(PEPSValues.ATTRIBUTE_TUPLE_SEP.toString());
        AttributeUtil.appendIfNotNull(strBuild, getStatus());
        strBuild.append(PEPSValues.ATTRIBUTE_SEP.toString());

        return strBuild.toString();
    }

    /**
     * Empties the Value or ComplexValue field of a PersonalAttribute
     */
    public void setEmptyValue() {
        if (this.isEmptyValue()) {
            this.complexValue = new Vector<HashMap<String, String>>();
        } else {
            this.value = new ArrayList<String>();
        }
    }

}

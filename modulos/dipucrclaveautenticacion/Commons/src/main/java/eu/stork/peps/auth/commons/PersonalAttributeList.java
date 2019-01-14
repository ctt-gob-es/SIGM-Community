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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class is a bean used to store the information relative to the
 * PersonalAttributeList.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.27 $, $Date: 2010-11-18 22:54:56 $
 *
 * @see PersonalAttribute
 */
@SuppressWarnings("PMD")
public final class PersonalAttributeList extends ConcurrentHashMap<String, PersonalAttribute> implements
        IPersonalAttributeList {

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(PersonalAttributeList.class.getName());

    /**
     * Serial id.
     */
    private static final long serialVersionUID = 7375127363889975062L;

    /**
     * Hash with the latest fetched attribute name alias.
     */
    private final transient Map<String, Integer> latestAttrAlias = new HashMap<String, Integer>();

    /**
     * Hash with mapping number of alias or the attribute name.
     */
    private final transient Map<String, List<String>> attrAliasNumber = new HashMap<String, List<String>>();

    /**
     * Default constructor.
     */
    public PersonalAttributeList() {
        // The best practices recommend to call the super constructor.
        super();
    }

    /**
     * Constructor with initial capacity for the PersonalAttributeList size.
     *
     * @param capacity The initial capacity for the PersonalAttributeList.
     */
    public PersonalAttributeList(final int capacity) {
        super(capacity);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<PersonalAttribute> iterator() {
        return this.values().iterator();
    }

    /**
     * {@inheritDoc}
     */
    public PersonalAttribute get(final Object key) {
        String attrName = (String) key;

        if (this.latestAttrAlias.containsKey(key)) {
            int index = this.latestAttrAlias.get(key);
            if ((index + 1) > this.attrAliasNumber.get(key).size()) {
                index = 0;
            }

            attrName = this.attrAliasNumber.get(key).get(index);
            this.latestAttrAlias.put((String) key, Integer.valueOf(++index));
        } else {
            if (this.attrAliasNumber.containsKey(key)) {
                this.latestAttrAlias.put((String) key, Integer.valueOf(0));
                attrName = this.attrAliasNumber.get(key).get(0);
            }
        }
        return super.get(attrName);
    }

    /**
     * {@inheritDoc}
     */
    public void add(final PersonalAttribute value) {
        if (value != null) {
            this.put(value.getName(), value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public PersonalAttribute replace(final String key, final PersonalAttribute val) {
        return super.put(key, val);
    }

    /**
     * {@inheritDoc}
     */
    public PersonalAttribute put(final String key, final PersonalAttribute val) {
        PersonalAttribute attr = val;
        if (this.containsKey(key)) {
            attr = this.get(key);

            if (!attr.isEmptyValue()) {
                if (!attr.getValue().containsAll(val.getValue())) {
                    attr.getValue().addAll(val.getValue());
                }
            } else {

                if (!attr.isEmptyComplexValue()) {
                    for (HashMap<String, String> valTemp : val.getComplexValues()) {
                        if (!attr.getComplexValues().contains(valTemp)) {
                            attr.setComplexValue(valTemp);
                        }
                    }
                } else {
                    if (STORKStatusCode.STATUS_NOT_AVAILABLE.toString().equals(attr.getStatus())) {
                        attr = val;
                    }
                }
            }
        }
        return super.put(key, attr);
    }

    /**
     * Escape method for attributes with double comma
     *
     * @return escaped attribute list
     *
     */
    private String attrListEncoder(String attrList) {
        StringBuilder finalAttr = new StringBuilder();
        String boolAttr = PEPSValues.TRUE.toString();
        String reqRegex
                = PEPSValues.ATTRIBUTE_TUPLE_SEP.toString() + PEPSValues.TRUE.toString()
                + PEPSValues.ATTRIBUTE_TUPLE_SEP.toString();

        String reqRegexSeparator
                = PEPSValues.ATTRIBUTE_TUPLE_SEP.toString() + PEPSValues.TRUE.toString()
                + PEPSValues.ATTRIBUTE_TUPLE_SEP.toString() + "|" + PEPSValues.ATTRIBUTE_TUPLE_SEP.toString()
                + PEPSValues.FALSE.toString() + PEPSValues.ATTRIBUTE_TUPLE_SEP.toString();

        for (String s : attrList.split(PEPSValues.ATTRIBUTE_SEP.toString())) {
            StringBuilder tempBuilder = new StringBuilder(s);
            if (s.split(PEPSValues.ATTRIBUTE_TUPLE_SEP.toString()).length > 4) {
                LOG.info("Found attributes with special characters, escaping special characters");

                if (s.split(reqRegex) == null) {
                    boolAttr = PEPSValues.FALSE.toString();
                }

                tempBuilder.setLength(0);
                tempBuilder.append(AttributeUtil.escape(s.split(reqRegexSeparator)[0]));
                tempBuilder.append(PEPSValues.ATTRIBUTE_TUPLE_SEP.toString());
                tempBuilder.append(boolAttr);
                tempBuilder.append(PEPSValues.ATTRIBUTE_TUPLE_SEP.toString());
                tempBuilder.append(s.split(reqRegexSeparator)[1]);

            }

            finalAttr.append(tempBuilder.toString());
            finalAttr.append(PEPSValues.ATTRIBUTE_SEP.toString());
        }
        return finalAttr.toString();
    }

    /**
     * Unescape a string
     *
     * @see PersonalAttributeList#attrListEncoder
     *
     */
    private String attrListDecoder(String string) {
        return AttributeUtil.unescape(string);
    }

    public void populate(final String attrList) {

        final StringTokenizer strToken
                = new StringTokenizer(attrListEncoder(attrList), PEPSValues.ATTRIBUTE_SEP.toString());

        while (strToken.hasMoreTokens()) {
            final PersonalAttribute persAttr = new PersonalAttribute();
            String[] tuples
                    = strToken.nextToken().split(PEPSValues.ATTRIBUTE_TUPLE_SEP.toString(),
                            AttributeConstants.NUMBER_TUPLES.intValue());

            // Convert to the new format if needed!
            tuples = convertFormat(tuples);

            if (AttributeUtil.hasValidTuples(tuples)) {
                final int attrValueIndex = AttributeConstants.ATTR_VALUE_INDEX.intValue();
                final String tmpAttrValue = tuples[attrValueIndex].substring(1, tuples[attrValueIndex].length() - 1);
                final String[] vals = tmpAttrValue.split(PEPSValues.ATTRIBUTE_VALUE_SEP.toString());

                persAttr.setName(tuples[AttributeConstants.ATTR_NAME_INDEX.intValue()]);
                persAttr.setIsRequired(Boolean.valueOf(tuples[AttributeConstants.ATTR_TYPE_INDEX.intValue()]));

                // check if it is a complex value
                if (isComplexValue(vals)) {
                    persAttr.setComplexValue(createComplexValue(vals));
                } else {
                    persAttr.setValue(createValues(vals));
                }

                if (tuples.length == AttributeConstants.NUMBER_TUPLES.intValue()) {
                    tuples[0] = attrListDecoder(tuples[0]);
                    persAttr.setName(attrListDecoder(persAttr.getName()));

                    persAttr.setStatus(tuples[AttributeConstants.ATTR_STATUS_INDEX.intValue()]);
                }
                this.put(tuples[AttributeConstants.ATTR_NAME_INDEX.intValue()], persAttr);

            } else {
                LOG.warn("Invalid personal attribute list tuples");
            }

        }
    }

    /**
     * Returns a copy of this <tt>IPersonalAttributeList</tt> instance.
     *
     * @return The copy of this IPersonalAttributeList.
     */
    public Object clone() {
        //This implementation may have an bug!
        try {
            return (PersonalAttributeList) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Creates a string in the following format.
     *
     * attrName:attrType:[attrValue1,attrValue2=attrComplexValue]:attrStatus;
     *
     * @return {@inheritDoc}
     */
    @Override
    public String toString() {
        final Iterator<Entry<String, PersonalAttribute>> itAttrs = this.entrySet().iterator();
        final StringBuilder strBuilder = new StringBuilder();

        while (itAttrs.hasNext()) {
            final Entry<String, PersonalAttribute> attrEntry = itAttrs.next();
            final PersonalAttribute attr = attrEntry.getValue();
            // strBuilder.append(attr.toString());
            strBuilder.insert(0, attr.toString());
        }

        return strBuilder.toString();
    }

    /**
     * Validates and creates the attribute's complex values.
     *
     * @param values The complex values.
     *
     * @return The {@link Map} with the complex values.
     *
     * @see Map
     */
    private HashMap<String, String> createComplexValue(final String[] values) {
        final HashMap<String, String> complexValue = new HashMap<String, String>();
        for (final String val : values) {
            final String[] tVal = val.split("=");
            if (StringUtils.isNotEmpty(val) && tVal.length == 2) {
                complexValue.put(tVal[0], AttributeUtil.unescape(tVal[1]));
            }
        }
        return complexValue;
    }

    /**
     * Checks if value is complex or not
     *
     * @param values The values to check
     * @return True if succesful
     */
    private boolean isComplexValue(final String[] values) {
        boolean isComplex = false;
        if (values.length > 0) {
            final String[] tVal = values[0].split("=");
            if (StringUtils.isNotEmpty(values[0]) && tVal.length == 2) {
                isComplex = true;
            }
        }
        return isComplex;
    }

    /**
     * Validates and creates the attribute values.
     *
     * @param vals The attribute values.
     *
     * @return The {@link List} with the attribute values.
     *
     * @see List
     */
    private List<String> createValues(final String[] vals) {
        final List<String> values = new ArrayList<String>();
        for (final String val : vals) {
            if (StringUtils.isNotEmpty(val)) {
                values.add(AttributeUtil.unescape(val));
            }
        }
        return values;
    }

    /**
     * Converts the attribute tuple (attrName:attrType...) to the new format.
     *
     * @param tuples The attribute tuples to convert.
     *
     * @return The attribute tuples in the new format.
     */
    private String[] convertFormat(final String[] tuples) {
        final String[] newFormatTuples = new String[AttributeConstants.NUMBER_TUPLES.intValue()];
        if (tuples != null) {
            System.arraycopy(tuples, 0, newFormatTuples, 0, tuples.length);

            for (int i = tuples.length; i < newFormatTuples.length; i++) {
                if (i == AttributeConstants.ATTR_VALUE_INDEX.intValue()) {
                    newFormatTuples[i] = "[]";
                } else {
                    newFormatTuples[i] = "";
                }
            }
        }
        return newFormatTuples;
    }

    /**
     * Returns a IPersonalAttributeList of the complex attributes in this map.
     *
     * @return an IPersonalAttributeList of the complex attributes contained in
     * this map.
     */
    public IPersonalAttributeList getComplexAttributes() {
        LOG.info("get complex attributes");
        IPersonalAttributeList attrList = new PersonalAttributeList();
        for (PersonalAttribute attr : this) {
            if (!attr.getComplexValue().isEmpty()) {
                attrList.put(attr.getName(), attr);
                LOG.info("adding complex attribute:" + attr.getName());
            }
        }
        return attrList;
    }

    /**
     * {@inheritDoc}
     */
    public IPersonalAttributeList merge(IPersonalAttributeList attrList1) {

        for (PersonalAttribute attr : attrList1) {
            this.add(attr);
        }
        return this;
    }

    /**
     * Returns a IPersonalAttributeList of the mandatory attributes in this map.
     *
     * @return an IPersonalAttributeList of the mandatory attributes contained
     * in this map.
     */
    public IPersonalAttributeList getSimpleValueAttributes() {
        LOG.info("get simple attributes");
        IPersonalAttributeList attrList = new PersonalAttributeList();
        for (PersonalAttribute attr : this) {
            if (!attr.getValue().isEmpty()) {
                attrList.put(attr.getName(), attr);
                LOG.info("adding simple attribute:" + attr.getName());
            }
        }
        return attrList;
    }

    /**
     * Returns a IPersonalAttributeList of the mandatory attributes in this map.
     *
     * @return an IPersonalAttributeList of the mandatory attributes contained
     * in this map.
     */
    public IPersonalAttributeList getMandatoryAttributes() {
        return getAttributesByParam(true);
    }

    /**
     * Returns a IPersonalAttributeList of the attributes in this map by
     * parameter value.
     *
     * @param compareValue The boolean to get mandatory (true) or optional
     * (false) attributes.
     *
     * @return an IPersonalAttributeList of the mandatory attributes contained
     * in this map if compareValue is true or optional otherwise.
     */
    private IPersonalAttributeList getAttributesByParam(final boolean compareValue) {
        LOG.info("get attributes by param :" + compareValue);
        IPersonalAttributeList attrList = new PersonalAttributeList();
        for (PersonalAttribute attr : this) {
            if (attr.isRequired() == compareValue) {
                attrList.put(attr.getName(), attr);
                LOG.info("adding attribute:" + attr.getName());
            }
        }
        return attrList;
    }

    /**
     * Returns a IPersonalAttributeList of the optional attributes in this map.
     *
     * @return an IPersonalAttributeList of the optional attributes contained in
     * this map.
     */
    public IPersonalAttributeList getOptionalAttributes() {
        return getAttributesByParam(false);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasMissingValues() {
        for (PersonalAttribute attr : this) {
            if (attr.isEmptyValue() && attr.isEmptyComplexValue()) {
                return true;
            }
        }
        return false;
    }
}

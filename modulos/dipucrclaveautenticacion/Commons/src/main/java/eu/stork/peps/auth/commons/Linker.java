package eu.stork.peps.auth.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.opensaml.saml2.core.Assertion;

/**
 * This class is a bean used to store the information of Attribute Providers,
 * the Attribute List to be requested, the Assertions returned by the Attribute
 * Providers and the values that each Attribute has. This information along with
 * the current status of the Linker (the attribute providers that were queried
 * and the remaining providers) is used by the PEPS actions in order to complete
 * the Attribute gathering.
 *
 * @author Stelios Lelis (stelios.lelis@aegean.gr), Elias Pastos
 * (ilias@aegean.gr)
 *
 * @version $Revision: 1.50 $, $Date: 2013-11-28 $
 */
public final class Linker implements Serializable {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = -3268006381745987237L;

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(Linker.class.getName());

    /**
     * Attributes Providers map.
     */
    private IAttributeProvidersMap attributeProvidersMap;

    /**
     * Assertion map.
     */
    private LinkedHashMap<AttributeSource, List<STORKAttrQueryResponse>> assertions;

    /**
     * The current index of local (domestic) Attribute Providers.
     */
    private int localIndex;

    /**
     * The current index of remote (foreign) Attribute Providers - countries.
     */
    private int remoteIndex;

    /**
     * Constructs an empty Linker object.
     */
    public Linker() {
        localIndex = 0;
        remoteIndex = 0;

        assertions = new LinkedHashMap<AttributeSource, List<STORKAttrQueryResponse>>();
    }

    /**
     * Based on the internal state of the Linker it returns the next local
     * Attribute Source
     *
     * @return The next Attribute Source or null if not found
     *
     * @see AttributeSource
     */
    public AttributeSource getNextLocalProvider() {
        Iterator<AttributeSource> iterator;
        AttributeSource source, found;
        int curIndex = 0;

        found = null;

        if (attributeProvidersMap != null && !attributeProvidersMap.isEmpty()) {
            iterator = attributeProvidersMap.keyIterator();
            while (iterator.hasNext()) {
                source = iterator.next();

                if (source.getSourceType() == AttributeSource.SOURCE_LOCAL_APROVIDER) {
                    if (curIndex >= localIndex) {
                        found = source;

                        break;
                    }

                    curIndex++;
                }
            }
        }

        return found;
    }

    /**
     * Based on the internal state of the Linker it returns the next remote
     * Attribute Source
     *
     * @return The next Attribute Source or null if not found
     *
     * @see AttributeSource
     */
    public AttributeSource getNextRemoteProvider() {
        Iterator<AttributeSource> iterator;
        AttributeSource source, found;
        int curIndex = 0;

        found = null;

        if (attributeProvidersMap != null && !attributeProvidersMap.isEmpty()) {
            iterator = attributeProvidersMap.keyIterator();
            while (iterator.hasNext()) {
                source = iterator.next();

                if (source.getSourceType() == AttributeSource.SOURCE_REMOTE_COUNTRY) {
                    if (curIndex >= remoteIndex) {
                        found = source;

                        break;
                    }

                    curIndex++;
                }
            }
        }

        return found;
    }

    /**
     * It updates the Linker with the values returned by the Attribute Source.
     * It also advances to the next index in order to mark this attribute source
     * as completed.
     *
     * @param source The Attribute Source that was queried for attribute values.
     * @param attrResponse The attrResponse returned by the Attribute Source
     * that contains the attribute values.
     *
     * @see AttributeSource, STORKAttrQueryResponse
     */
    public void setProviderReponse(AttributeSource source, STORKAttrQueryResponse attrResponse) {
        if (source.getSourceType() == AttributeSource.SOURCE_REMOTE_COUNTRY) {
            remoteIndex++;
        } else {
            localIndex++;
        }

        //Assertion storage
        if (this.assertions.containsKey(source)) {
            this.assertions.get(source).add(attrResponse);
        } else {
            ArrayList<STORKAttrQueryResponse> temp = new ArrayList<STORKAttrQueryResponse>();
            temp.add(attrResponse);
            this.assertions.put(source, temp);
        }

        if (source.getSourceType() == AttributeSource.SOURCE_REMOTE_COUNTRY) {
            this.attributeProvidersMap.put(source, attrResponse.getTotalPersonalAttributeList());
        } else {
            this.attributeProvidersMap.put(source, attrResponse.getPersonalAttributeList());
        }
    }

    /**
     * Reset the internal state of the local Attribute Source in order to start
     * over.
     */
    public void resetLocalIndex() {
        localIndex = 0;
    }

    /**
     * Reset the internal state of the remote Attribute Source in order to start
     * over.
     */
    public void resetRemoteIndex() {
        remoteIndex = 0;
    }

    /**
     * Setter for attributeProvidersMap.
     *
     * @param attributeProvidersMap The attributeProvidersMap to set.
     */
    public void setAttributeProvidersMap(IAttributeProvidersMap attributeProvidersMap) {
        this.attributeProvidersMap = attributeProvidersMap;
    }

    /**
     * Getter for attributeProvidersMap.
     *
     * @return attributeProvidersMap
     */
    public IAttributeProvidersMap getAttributeProvidersMap() {
        return attributeProvidersMap;
    }

    /**
     * Returns the Personal Attribute list of the provided Attribute Source.
     *
     * @param source The attributeSource in reference
     *
     * @return The IPersonalAttributeList assosiated with this source or null if
     * empty
     *
     * @see IPersonalAttributeList
     */
    public IPersonalAttributeList getProviderAttributes(AttributeSource source) {
        if (attributeProvidersMap.containsKey(source)) {
            return attributeProvidersMap.get(source);
        } else {
            return null;
        }
    }

    /**
     * Returns the merged Personal Attribute list from all the Attribute
     * Sources.
     *
     * @return The IPersonalAttributeList merged Personal Attribute list or null
     * if empty
     *
     * @see IPersonalAttributeList
     */
    public IPersonalAttributeList getAllAttributes() {
        Iterator<AttributeSource> iterator;
        AttributeSource source;
        IPersonalAttributeList list, merged;

        merged = null;

        if (attributeProvidersMap != null && !attributeProvidersMap.isEmpty()) {
            iterator = attributeProvidersMap.keyIterator();

            merged = new PersonalAttributeList();
            while (iterator.hasNext()) {
                source = iterator.next();
                list = this.getProviderAttributes(source);

                for (final PersonalAttribute pa : list) {
                    merged.add(pa);
                }
            }
        }

        return merged;
    }

    /**
     * Returns a List with all the assertions gathered by the AAS-PEPS module
     * returned both by local APs or remote A-PEPS.
     *
     * @return The assertions returned from the APs and A-PEPS
     */
    public List<STORKAttrQueryResponse> getAttrQueryResponseList() {
        List<STORKAttrQueryResponse> originalAssertions;

        originalAssertions = new ArrayList<STORKAttrQueryResponse>();

        //Gather all assertions
        for (List<STORKAttrQueryResponse> element : this.assertions.values()) {
            originalAssertions.addAll(element);
        }
        return originalAssertions;
    }

    /**
     * Checks the internal state of the Linker and if all Attribute Sources
     * where visited returns true, otherwise it returns false. So if you go
     * directly from AtPLinkerAction to MoreAttributesAction the call will have,
     * since the method setProviderReponse was not executed from every Attribute
     * Source.
     *
     * @return true if everything is OK, false otherwise
     */
    public boolean isComplete() {
        boolean outcome = false;

        LOG.debug("Check if linkder is complete: R[" + remoteIndex + "], L[" + localIndex + "], S[" + attributeProvidersMap.size() + "]");
        if (attributeProvidersMap != null && !attributeProvidersMap.isEmpty()) {
            if ((remoteIndex + localIndex) == attributeProvidersMap.size()) {
                outcome = true;
            }
        } else {
            outcome = true;
        }

        return outcome;
    }

    /**
     * Merge the two Linker objects.
     *
     * @param previous The other Linker object to merge with this one.
     */
    public void mergeWith(Linker previous) {
        //BEFORE
        if (LOG.isDebugEnabled()) {
            LOG.debug("The attributeProvidersMap from the current object.");
            ((AttributeProvidersMap) this.attributeProvidersMap).trace();
            LOG.debug("The attributeProvidersMap from the provided object.");
            ((AttributeProvidersMap) previous.getAttributeProvidersMap()).trace();
        }

        IAttributeProvidersMap map = previous.getAttributeProvidersMap();
        Iterator<AttributeSource> items = map.keyIterator();
        while (items.hasNext()) {
            AttributeSource item = items.next();
            IPersonalAttributeList pal = map.get(item);

            if (this.attributeProvidersMap.containsKey(item)) {
                IPersonalAttributeList new_pal = this.attributeProvidersMap.get(item);

                for (PersonalAttribute pa : pal) {
                    new_pal.add(pa);
                }
            } else {
                if (item.getSourceType() == AttributeSource.SOURCE_REMOTE_COUNTRY) {
                    remoteIndex++;
                } else {
                    localIndex++;
                }

                this.attributeProvidersMap.put(item, pal);
            }
        }

        //AFTER
        if (LOG.isDebugEnabled()) {
            LOG.debug("The attributeProvidersMap after the merge.");
            ((AttributeProvidersMap) this.attributeProvidersMap).trace();
        }

        for (AttributeSource as : previous.assertions.keySet()) {
            if (!assertions.containsKey(as)) {
                assertions.put(as, previous.assertions.get(as));
            } else {
                assertions.get(as).addAll(previous.assertions.get(as));
            }
        }
    }
}

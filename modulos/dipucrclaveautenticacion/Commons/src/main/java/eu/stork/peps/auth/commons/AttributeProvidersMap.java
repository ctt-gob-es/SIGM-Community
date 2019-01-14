package eu.stork.peps.auth.commons;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

/**
 * Implementation of the AttributeProviderMap using a LinkedHashMap.
 *
 * @author Stelios Lelis (stelios.lelis@aegean.gr), Elias Pastos
 * (ilias@aegean.gr)
 *
 * @version $Revision: 1.01 $, $Date: 2013-09-20 $
 *
 * @see LinkedHashMap
 */
public class AttributeProvidersMap extends LinkedHashMap<AttributeSource, IPersonalAttributeList>
        implements IAttributeProvidersMap {

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(AttributeProvidersMap.class.getName());

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = 8949081185106296122L;

    /**
     * {@inheritDoc}
     */
    @Override
    public IPersonalAttributeList get(final AttributeSource key) {
        return this.get((Object) key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPersonalAttributeList remove(final AttributeSource key) {
        return this.remove((Object) key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(final AttributeSource key) {
        return this.containsKey((Object) key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<AttributeSource> keyIterator() {
        return this.keySet().iterator();
    }

    public void trace() {
        Iterator<AttributeSource> iterator;
        Iterator<PersonalAttribute> iterator2;
        AttributeSource source;
        IPersonalAttributeList pal;
        PersonalAttribute pa;

        iterator = this.keyIterator();
        LOG.trace("Start dumping of AttributeProvidersMap\n=======================");
        while (iterator.hasNext()) {
            source = iterator.next();

            LOG.trace("Source details: type [" + source.getSourceType() + "], URL [" + source.getProviderURL() + "]");

            if (source.getSourceType() == AttributeSource.SOURCE_LOCAL_APROVIDER) {
                LOG.trace("-> Attribute Provider: ID [" + source.getProvider().getProviderId() + "], name [" + source.getProvider().getProviderName() + "]");
            } else {
                LOG.trace("-> Country: ID [" + source.getCountry().getCountryId() + "], name [" + source.getCountry().getCountryName() + "]");
            }

            pal = this.get(source);
            LOG.trace("++++++++=>");
            iterator2 = pal.iterator();
            while (iterator2.hasNext()) {
                pa = iterator2.next();

                LOG.trace("-> Citizen Attribute: name [" + pa.getName() + "], required [" + pa.isRequired() + "]");
            }
            LOG.trace("<=++++++++");

            LOG.trace("-----------------------");
        }
        LOG.trace("END\n=======================");
    }

    public void mergeWith(IAttributeProvidersMap aPMap) {
        Iterator<AttributeSource> maKeys = aPMap.keyIterator();
        while (maKeys.hasNext()) {
            AttributeSource key = maKeys.next();
            IPersonalAttributeList l2 = aPMap.get(key);
            if (containsKey(key)) {
                IPersonalAttributeList l1 = get(key);
                for (PersonalAttribute pa : l2) {
                    if (!l1.containsKey(pa.getName())) {
                        l1.add(pa);
                    }
                }
            } else {
                put(key, l2);
            }
        }
    }
}

package eu.stork.peps.sso.commons;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;

/**
 * This class is used to store information regarding SSO
 */
public class UserSession implements HttpSessionListener {

    /**
     * Indicates if SSO is enabled
     */
    private static boolean caching;

    /**
     * Cache indexed by SessionId:SPId
     */
    private static final Map<String, CacheEntry> sessions = new ConcurrentHashMap<String, CacheEntry>();

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(UserSession.class.getName());

    /**
     * Number of days in which the entries are stored before being cleaned
     */
    private static int CACHED_DAYS = 2;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.put(session.getId(), new CacheEntry());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession().getId());
    }

    /**
     * Returns the attribute list associated with the key composed by sessionId
     * and SPId if its qaa level is higher or equal to the qaa requested
     *
     * @param key The key formatted as sessionId:SPId
     * @param qaa The QAA level of the current request
     *
     * @return the personal attribute list associated with the key composed by
     * sessionId and SPId or null, if the list has a QAA level higher or equal
     * to the qaa requested
     */
    public static IPersonalAttributeList find(String key, int qaa) {
        if (sessions.get(key).getQaa() >= qaa) {
            return sessions.get(key).getCachedList();
        }
        return null;
    }

    /**
     * Checks if exists an entry associated with the key composed by sessionId
     * and SPId, with a QAA level higher or equal to the qaa requested
     *
     * @param key formatted as sessionId:SPId
     * @param qaa The QAA level of the current request
     *
     * @return true if an entry associated with the key exists
     */
    public static boolean has(String key, int qaa) {
        return sessions.containsKey(key) && sessions.get(key).getCachedList().size() > 0
                && sessions.get(key).getQaa() >= qaa;
    }

    /**
     * Checks if exists an entry associated with the key composed by sessionId
     * and SPId, if all the attributes in attrList exists in that entry and if
     * it has a QAA level higher or equal to the qaa requested
     *
     * @param key formatted as sessionId:SPId
     * @param attrList contains the attributes that were requested by the user
     * @param qaa The QAA level of the current request
     *
     * @return true if an entry associated with the key exists and it contains
     * all the requested attributes
     */
    public static boolean has(String key, IPersonalAttributeList attrList, int qaa) {
        boolean result = false;

        if (caching) {
            if (sessions.containsKey(key) && sessions.get(key).getCachedList().size() > 0
                    && sessions.get(key).getQaa() >= qaa) {

                IPersonalAttributeList sessionAttr = sessions.get(key).getCachedList();
                if (sessionAttr.size() >= attrList.size()) {
                    Iterator<PersonalAttribute> paListIt = attrList.iterator();

                    while (paListIt.hasNext()) {
                        PersonalAttribute pa = (PersonalAttribute) paListIt.next();
                        if (!sessionAttr.containsKey(pa.getName())) {
                            sessions.remove(key);
                            result = false;
                            break;
                        }
                        result = true;
                    }
                }
            }

            if (result) {
                LOG.debug("Found valid session in local cache");
            }
        }
        return result;
    }

    /**
     * Inserts into cache a new list of attributes
     *
     * @param key formatted as sessionId:SPId which will be associated with the
     * new attributes
     * @param qaa The QAA level of the current request
     * @param paList represents the new list of attributes to be inserted in
     * cache
     */
    public static void populate(String key, IPersonalAttributeList paList, int qaa) {
        CacheEntry entry;
        final IPersonalAttributeList paListResult = new PersonalAttributeList();
        IPersonalAttributeList paListTemp = new PersonalAttributeList();

        if (sessions.containsKey(key) && sessions.get(key).getQaa() >= qaa) {
            paListTemp = sessions.get(key).getCachedList();
        } else {
            paListTemp.populate(paList.toString());
        }

        for (PersonalAttribute attr : paListTemp) {

            if (!attr.isEmptyValue() || !attr.isEmptyComplexValue()) {
                String temp = attr.isEmptyValue() ? attr.getComplexValue().toString() : attr.getValue().toString();
                LOG.debug("Adding attribute " + attr.getName() + " to local cache with value: " + temp);
                paListResult.add(attr);

            } else {
                LOG.warn("Empty attribute " + attr.getName() + " not adding to user session");
            }
        }

        if (caching) {
            if (!sessions.containsKey(key) || (sessions.containsKey(key) && sessions.get(key).getQaa() <= qaa)) {
                entry = new CacheEntry(paListResult, qaa);
                sessions.put(key, entry);
            }

        }
    }

    /**
     * Removes the value mapped for the key formatted as sessionId:spId
     *
     * @param sessionId the Id of the current session
     */
    public static void removeSessionBySpId(String key) {
        sessions.remove(key);
    }

    /**
     * Removes entries with more than CACHED_DAYS days in cache
     */
    public static void removeOldSessions() {
        Iterator<Entry<String, CacheEntry>> it = sessions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry< String, CacheEntry> entry = (Entry<String, CacheEntry>) it.next();

            if (entry.getValue().isOlderThanXTime(CACHED_DAYS)) {
                sessions.remove(entry.getKey());
            }
        }
    }

    /**
     * Getter for the caching
     *
     * @return the caching value.
     */
    public static boolean getSessionConfigs() {
        return caching;
    }

    /**
     * Setter for caching
     *
     * @param caching The new caching value.
     */
    public static void setCaching(boolean caching) {
        UserSession.caching = caching;
    }

}

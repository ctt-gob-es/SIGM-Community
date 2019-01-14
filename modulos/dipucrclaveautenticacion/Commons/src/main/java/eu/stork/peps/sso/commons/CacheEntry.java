package eu.stork.peps.sso.commons;

import java.util.Date;
import eu.stork.peps.auth.commons.IPersonalAttributeList;

/**
 * This class is a bean used to store the information relative to an entry in
 * SSO cache
 */
public class CacheEntry {

    /**
     * The attributeList to be saved in cache for a session ID and Service
     * Provider ID
     */
    private IPersonalAttributeList cachedList;
    /**
     * The QAA level of the request
     */
    private int qaa;

    /**
     * The date on which the entry was created
     */
    private Date creationDate;

    /**
     * One day in milliseconds
     */
    private long DAY_IN_MILLIS = 86400000;

    /**
     * Default constructor.
     */
    public CacheEntry() {
    }

    /**
     * Constructor to create an entry to be inserted in SSO cache
     *
     * @param attrList The list to be saved
     * @param qaa The QAA level of the request
     */
    public CacheEntry(IPersonalAttributeList attrList, int qaa) {
        cachedList = attrList;
        this.qaa = qaa;
        this.creationDate = new Date();
    }

    /**
     * Getter for cachedList
     *
     * @return The cachedList value
     */
    public IPersonalAttributeList getCachedList() {
        return cachedList;
    }

    /**
     * Setter for cachedList.
     *
     * @param ncachedList The new cachedList value.
     */
    public void setCachedList(IPersonalAttributeList ncachedList) {
        cachedList = ncachedList;
    }

    /**
     * Getter for qaa
     *
     * @return The qaa value
     */
    public int getQaa() {
        return qaa;
    }

    /**
     * Setter for qaa.
     *
     * @param nqaa The new qaa value.
     */
    public void setQaa(int nqaa) {
        qaa = nqaa;
    }

    /**
     * Getter for creationDate
     *
     * @return The creationDate value
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Setter for creationDate.
     *
     * @param ncreationDate The new creationDate value.
     */
    public void setCreationDate(Date ncreationDate) {
        creationDate = ncreationDate;
    }

    /**
     * Checks if the creation date of an entry is older than nDays days
     *
     * @return true if older than nDays
     */
    public boolean isOlderThanXTime(int nDays) {
        Date current = new Date();
        if (current.getTime() - this.getCreationDate().getTime() >= (nDays * DAY_IN_MILLIS)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "Personal AttributeList: " + this.cachedList.toString() + "\n" + "QAA Level: " + this.qaa + ";" + "CreationDate: " + this.creationDate + ";" + "Day in Miliseconds: " + this.DAY_IN_MILLIS;
    }
}

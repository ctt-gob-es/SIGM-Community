package es.dipucr.ownCloud;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.xml.Namespace;
import org.apache.log4j.Logger;

public class WebdavEntry {
	
	private static final Logger logger = Logger.getLogger(WebdavEntry.class); 

	public static final String NAMESPACE_OC = "http://owncloud.org/ns";
	public static final String EXTENDED_PROPERTY_NAME_PERMISSIONS = "permissions";
	public static final String EXTENDED_PROPERTY_NAME_REMOTE_ID = "id";
    public static final String EXTENDED_PROPERTY_NAME_SIZE = "size";

    public static final String PROPERTY_QUOTA_USED_BYTES = "quota-used-bytes";
    public static final String PROPERTY_QUOTA_AVAILABLE_BYTES = "quota-available-bytes";

    private static final int CODE_PROP_NOT_FOUND = 404;

	private String mName, mPath, mUri, mContentType, mEtag, mPermissions, mRemoteId;
	private long mContentLength, mCreateTimestamp, mModifiedTimestamp, mSize;
    private BigDecimal mQuotaUsedBytes, mQuotaAvailableBytes;

	public WebdavEntry(MultiStatusResponse ms, String splitElement) {
        resetData();
        if (ms.getStatus().length != 0) {
            mUri = ms.getHref();

            mPath = mUri;

            int status = ms.getStatus()[0].getStatusCode();
            if ( status == CODE_PROP_NOT_FOUND ){
                status = ms.getStatus()[1].getStatusCode();
            }
            DavPropertySet propSet = ms.getProperties(status);
            DavProperty<?> prop = propSet.get(DavPropertyName.DISPLAYNAME);
            if (prop != null) {
                mName = (String) prop.getName().toString();
                mName = mName.substring(1, mName.length()-1);
            }
            else {
                String[] tmp = mPath.split("/");
                if (tmp.length > 0)
                    mName = tmp[tmp.length - 1];
            }

            mContentType = "application/octet-stream";
            prop = propSet.get(DavPropertyName.GETCONTENTTYPE);
            if (prop != null) {
                mContentType = (String) prop.getValue();
                if (mContentType.indexOf(";") >= 0) {
                    mContentType = mContentType.substring(0, mContentType.indexOf(";"));
                }
            }

            prop = propSet.get(DavPropertyName.RESOURCETYPE);
            if (prop!= null) {
                Object value = prop.getValue();
                if (value != null) {
                    mContentType = "DIR";
                }
            }

            prop = propSet.get(DavPropertyName.GETCONTENTLENGTH);
            if (prop != null)
                mContentLength = Long.parseLong((String) prop.getValue());

            prop = propSet.get(DavPropertyName.GETLASTMODIFIED);
            if (prop != null) {
                Date d = WebdavUtils
                        .parseResponseDate((String) prop.getValue());
                mModifiedTimestamp = (d != null) ? d.getTime() : 0;
            }

            prop = propSet.get(DavPropertyName.CREATIONDATE);
            if (prop != null) {
                Date d = WebdavUtils
                        .parseResponseDate((String) prop.getValue());
                mCreateTimestamp = (d != null) ? d.getTime() : 0;
            }

            prop = propSet.get(DavPropertyName.GETETAG);
            if (prop != null) {
                mEtag = (String) prop.getValue();
                mEtag = mEtag.substring(1, mEtag.length()-1);
            }

            prop = propSet.get(DavPropertyName.create(PROPERTY_QUOTA_USED_BYTES));
            if (prop != null) {
                String quotaUsedBytesSt = (String) prop.getValue();
                try {
                    mQuotaUsedBytes = new BigDecimal(quotaUsedBytesSt);
                } catch (NumberFormatException e) {
                    logger.error("No value for QuotaUsedBytes - NumberFormatException", e);
                } catch (NullPointerException e ){
                    logger.error("No value for QuotaUsedBytes - NullPointerException", e);
                }
                logger.debug( "QUOTA_USED_BYTES " + quotaUsedBytesSt );
            }

            prop = propSet.get(DavPropertyName.create(PROPERTY_QUOTA_AVAILABLE_BYTES));
            if (prop != null) {
                String quotaAvailableBytesSt = (String) prop.getValue();
                try {
                    mQuotaAvailableBytes = new BigDecimal(quotaAvailableBytesSt);
                } catch (NumberFormatException e) {
                    logger.error("No value for QuotaAvailableBytes - NumberFormatException", e);
                } catch (NullPointerException e ){
                    logger.error("No value for QuotaAvailableBytes", e);
                }
                logger.debug( "QUOTA_AVAILABLE_BYTES " + quotaAvailableBytesSt );
            }

            prop = propSet.get(EXTENDED_PROPERTY_NAME_PERMISSIONS, Namespace.getNamespace(NAMESPACE_OC));
            if (prop != null) {
                mPermissions = prop.getValue().toString();
            }

            prop = propSet.get(EXTENDED_PROPERTY_NAME_REMOTE_ID, Namespace.getNamespace(NAMESPACE_OC));
            if (prop != null) {
                mRemoteId = prop.getValue().toString();
            }

            prop = propSet.get(EXTENDED_PROPERTY_NAME_SIZE, Namespace.getNamespace(NAMESPACE_OC));
            if (prop != null) {
                mSize = Long.parseLong((String) prop.getValue());
            }

        } else {
            logger.error("WebdavEntry" + "General fuckup, no status for webdav response");
        }
    }

    public String path() {
        return mPath;
    }

    public String name() {
        return mName;
    }

    public boolean isDirectory() {
        return "DIR".equals(mContentType);
    }

    public String contentType() {
        return mContentType;
    }

    public String uri() {
        return mUri;
    }

    public long contentLength() {
        return mContentLength;
    }

    public long createTimestamp() {
        return mCreateTimestamp;
    }

    public long modifiedTimestamp() {
        return mModifiedTimestamp;
    }
    
    public String etag() {
        return mEtag;
    }

    public String permissions() {
        return mPermissions;
    }

    public String remoteId() {
        return mRemoteId;
    }

    public long size(){
        return mSize;
    }

    public BigDecimal quotaUsedBytes() {
        return mQuotaUsedBytes;
    }

    public BigDecimal quotaAvailableBytes() {
        return mQuotaAvailableBytes;
    }

    private void resetData() {
        mName = mUri = mContentType = mPermissions = null; mRemoteId = null;
        mContentLength = mCreateTimestamp = mModifiedTimestamp = 0;
        mSize = 0;
        mQuotaUsedBytes = null;
        mQuotaAvailableBytes = null;
    }

	public String decodedPath() { 
		return mPath;
	}
}

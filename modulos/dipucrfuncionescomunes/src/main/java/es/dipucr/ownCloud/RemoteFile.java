package es.dipucr.ownCloud;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;


public class RemoteFile implements Serializable {

    /** Generated - should be refreshed every time the class changes!! */
    private static final long serialVersionUID = 3130865437811248451L;

	private String mRemotePath;
	private String mMimeType;
	private long mLength;
	private long mCreationTimestamp;
	private long mModifiedTimestamp;
	private String mEtag;
	private String mPermissions;
	private String mRemoteId;
    private long mSize;
    private BigDecimal mQuotaUsedBytes;
    private BigDecimal mQuotaAvailableBytes;

	/** 
	 * Getters and Setters
	 */
	
    public String getRemotePath() {
		return mRemotePath;
	}

	public void setRemotePath(String remotePath) {
		this.mRemotePath = remotePath;
	}

	public String getMimeType() {
		return mMimeType;
	}

	public void setMimeType(String mimeType) {
		this.mMimeType = mimeType;
	}

	public long getLength() {
		return mLength;
	}

	public void setLength(long length) {
		this.mLength = length;
	}

	public long getCreationTimestamp() {
		return mCreationTimestamp;
	}

	public void setCreationTimestamp(long creationTimestamp) {
		this.mCreationTimestamp = creationTimestamp;
	}

	public long getModifiedTimestamp() {
		return mModifiedTimestamp;
	}

	public void setModifiedTimestamp(long modifiedTimestamp) {
		this.mModifiedTimestamp = modifiedTimestamp;
	}

	public String getEtag() {
		return mEtag;
	}

	public void setEtag(String etag) {
		this.mEtag = etag;
	}
	
	public String getPermissions() {
		return mPermissions;
	}

	public void setPermissions(String permissions) {
		this.mPermissions = permissions;
	}

	public String getRemoteId() {
		return mRemoteId;
	}

	public void setRemoteId(String remoteId) {
		this.mRemoteId = remoteId;
	}

    public long getSize() {
        return mSize;
    }

    public void setSize (long size){
        mSize = size;
    }

    public BigDecimal getQuotaUsedBytes () {
        return mQuotaUsedBytes;
    }
    
    public void setQuotaUsedBytes (BigDecimal quotaUsedBytes) {
        mQuotaUsedBytes = quotaUsedBytes;
    }

    public BigDecimal getQuotaAvailableBytes () {
        return mQuotaAvailableBytes;
    }

    public void setQuotaAvailableBytes (BigDecimal quotaAvailableBytes) {
        mQuotaAvailableBytes = quotaAvailableBytes;
    }

	public RemoteFile() {
		resetData();
	}

	/**
     * Create new {@link RemoteFile} with given path.
     * 
     * The path received must be URL-decoded. Path separator must be OCFile.PATH_SEPARATOR, and it must be the first character in 'path'.
     * 
     * @param path The remote path of the file.
     */
	public RemoteFile(String path) {
		resetData();
        if (path == null || path.length() <= 0 || !path.startsWith(File.separator)) {
            throw new IllegalArgumentException("Trying to create a OCFile with a non valid remote path: " + path);
        }
        mRemotePath = path;
	}
	
	public RemoteFile(WebdavEntry we) {
        this(we.decodedPath());
        this.setCreationTimestamp(we.createTimestamp());
        this.setLength(we.contentLength());
        this.setMimeType(we.contentType());
        this.setModifiedTimestamp(we.modifiedTimestamp());
        this.setEtag(we.etag());
        this.setPermissions(we.permissions());
        this.setRemoteId(we.remoteId());
        this.setSize(we.size());
        this.setQuotaUsedBytes(we.quotaUsedBytes());
        this.setQuotaAvailableBytes(we.quotaAvailableBytes());
	}

	/**
     * Used internally. Reset all file properties
     */
    private void resetData() {
        mRemotePath = null;
        mMimeType = null;
        mLength = 0;
        mCreationTimestamp = 0;
        mModifiedTimestamp = 0;
        mEtag = null;
        mPermissions = null;
        mRemoteId = null;
        mSize = 0;
        mQuotaUsedBytes = null;
        mQuotaAvailableBytes = null;
    }
}
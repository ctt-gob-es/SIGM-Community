package es.dipucr.ownCloud;
/* ownCloud Android client application
 *   Copyright (C) 2011  Bartek Przybylski
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.log4j.Logger;

public class WebdavClient extends HttpClient {
	
	private static final Logger logger = Logger.getLogger(WebdavClient.class); 
	
    private String url;
    private Credentials mCredentials;
    final private static String TAG = "WebdavClient";
    private static final String USER_AGENT = "Dipucr-ownCloud";
    
    private OnDatatransferProgressListener mDataTransferListener;
    static private byte[] sExhaustBuffer = new byte[1024];
    
    private ArrayList<Object> mFolderAndFiles;
    
    /**
     * Constructor
     */
    public WebdavClient(HttpConnectionManager connectionMgr) {
        super(connectionMgr);
        logger.debug(TAG + "Creating WebdavClient");
        getParams().setParameter(HttpMethodParams.USER_AGENT, USER_AGENT);
        getParams().setParameter(HttpMethodParams.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    }

    public void setCredentials(String username, String password) {
        getParams().setAuthenticationPreemptive(true);
        getState().setCredentials(AuthScope.ANY, getCredentials(username, password));
    }

    private Credentials getCredentials(String username, String password) {
        if (mCredentials == null)
            mCredentials = new UsernamePasswordCredentials(username, password);
        return mCredentials;
    }
    
    /**
     * Downloads a file in remoteFilepath to the local targetPath.
     * 
     * @param remoteFilepath    Path to the file in the remote server, URL DECODED. 
     * @param targetFile        Local path to save the downloaded file.
     * @return                  'True' when the file is successfully downloaded.
     */
    
    public ArrayList<Object> getContenidoCarpeta(String remoteFilePath){
    	String ruta = url.toString() + WebdavUtils.encodePath(remoteFilePath);
    	PropFindMethod query = null;
    	RemoteOperationResult result = null;
    	try {
			query = new PropFindMethod(ruta, WebdavClient.getAllPropSet(), DavConstants.DEPTH_1);
			
			int status = executeMethod(query);
			boolean isSuccess = (status == HttpStatus.SC_MULTI_STATUS || status == HttpStatus.SC_OK);
			if (isSuccess) {
				// get data from remote folder
				MultiStatus dataInServer = query.getResponseBodyAsMultiStatus();
				readData(dataInServer);
				
				// Result of the operation
				result = new RemoteOperationResult(true, status, query.getResponseHeaders());
				
				// Add data to the result
				if (result.isSuccess()) {
					result.setData(mFolderAndFiles);
				}
			} else {
				// synchronization failed
				exhaustResponse(query.getResponseBodyAsStream());
				result = new RemoteOperationResult(false, status, query.getResponseHeaders());
            }
		} catch (IOException e) {
			logger.error("Error al recuperar los documentos de la ruta: " + ruta + ". " + e.getMessage(), e);
	    } catch (Exception e) {
			logger.error("Error al recuperar los documentos de la ruta: " + ruta + ". " + e.getMessage(), e);
	        result = new RemoteOperationResult(e);       
	    } finally {
	        if (query != null)
	            query.releaseConnection();  // let the connection available for other methods
	        if (result.isSuccess()) {
	        	logger.info("Synchronized "  + url + ": " + result.getLogMessage());
	        } else {
            	logger.error("Synchronized " + url  + ": " + result.getLogMessage(), result.getException());
	        }
	    }
    	
    	return mFolderAndFiles;
    }
    
    /**
     *  Read the data retrieved from the server about the contents of the target folder 
     *  
     * 
     *  @param remoteData     	Full response got from the server with the data of the target 
     *                          folder and its direct children.
     *  @param client           Client instance to the remote server where the data were 
     *                          retrieved.  
     *  @return                
     */
    private void readData(MultiStatus remoteData) {   	
        mFolderAndFiles = new ArrayList<Object>();
        
        // parse data from remote folder 
        WebdavEntry we = new WebdavEntry(remoteData.getResponses()[0],url.toString());
        mFolderAndFiles.add(fillOCFile(we));
        
        // loop to update every child
        RemoteFile remoteFile = null;
        for (int i = 1; i < remoteData.getResponses().length; ++i) {
            /// new OCFile instance with the data from the server
            we = new WebdavEntry(remoteData.getResponses()[i], url.toString());                        
            remoteFile = fillOCFile(we);
            mFolderAndFiles.add(remoteFile);
        }
        
    }
    
    /**
     * Creates and populates a new {@link RemoteFile} object with the data read from the server.
     * 
     * @param we        WebDAV entry read from the server for a WebDAV resource (remote file or folder).
     * @return          New OCFile instance representing the remote resource described by we.
     */
    private RemoteFile fillOCFile(WebdavEntry we) {
        RemoteFile file = new RemoteFile(we.decodedPath());
        file.setCreationTimestamp(we.createTimestamp());
        file.setLength(we.contentLength());
        file.setMimeType(we.contentType());
        file.setModifiedTimestamp(we.modifiedTimestamp());
        file.setEtag(we.etag());
        file.setPermissions(we.permissions());
        file.setRemoteId(we.remoteId());
        file.setSize(we.size());
        file.setQuotaUsedBytes(we.quotaUsedBytes());
        file.setQuotaAvailableBytes(we.quotaAvailableBytes());
        return file;
    }

   public boolean downloadFile(String remoteFilePath, File targetFile) {
        boolean ret = false;
        String ruta = url.toString() + WebdavUtils.encodePath(remoteFilePath);
        GetMethod get = new GetMethod(ruta);

        try {
            int status = executeMethod(get);
            if (status == HttpStatus.SC_OK) {
                targetFile.createNewFile();
                BufferedInputStream bis = new BufferedInputStream(
                        get.getResponseBodyAsStream());
                FileOutputStream fos = new FileOutputStream(targetFile);

                byte[] bytes = new byte[4096];
                int readResult;
                while ((readResult = bis.read(bytes)) != -1) {
                    if (mDataTransferListener != null)
                        mDataTransferListener.transferProgress(readResult);
                    fos.write(bytes, 0, readResult);
                }
                ret = true;
                fos.close();
               
            } else {
                exhaustResponse(get.getResponseBodyAsStream());
            }
            logger.error(TAG + "Download of " + remoteFilePath + " to " + targetFile + " finished with HTTP status " + status + (!ret?"(FAIL)":""));
            
        } catch (Exception e) {
            logException(e, "dowloading " + remoteFilePath);
            
        } finally {
            if (!ret && targetFile.exists()) {
                targetFile.delete();
            }
            get.releaseConnection();    // let the connection available for other methods
        }
        return ret;
    }
    
    /**
     * Deletes a remote file via webdav
     * @param remoteFilePath       Remote file path of the file to delete, in URL DECODED format.
     * @return
     */
    public boolean deleteFile(String remoteFilePath) {
        boolean ret = false;
        DavMethod delete = new DeleteMethod(url.toString() + WebdavUtils.encodePath(remoteFilePath));
        try {
            int status = executeMethod(delete);
            ret = (status == HttpStatus.SC_OK || status == HttpStatus.SC_ACCEPTED || status == HttpStatus.SC_NO_CONTENT);
            exhaustResponse(delete.getResponseBodyAsStream());
            
            logger.error(TAG + "DELETE of " + remoteFilePath + " finished with HTTP status " + status +  (!ret?"(FAIL)":""));
            
        } catch (Exception e) {
            logException(e, "deleting " + remoteFilePath);
            
        } finally {
            delete.releaseConnection();    // let the connection available for other methods
        }
        return ret;
    }

    
    public void setDataTransferProgressListener(OnDatatransferProgressListener listener) {
        mDataTransferListener = listener;
    }
    
    /**
     * Creates or update a file in the remote server with the contents of a local file.
     * 
     * 
     * @param localFile         Path to the local file to upload.
     * @param remoteTarget      Remote path to the file to create or update, URL DECODED
     * @param contentType       MIME type of the file.
     * @return                  'True' then the upload was successfully completed
     */
    public boolean putFile(String localFile, String remoteTarget, String contentType) {
        boolean result = false;
        int status = -1;
        PutMethod put = new PutMethod(url.toString() + WebdavUtils.encodePath(remoteTarget));
        
        try {
            File f = new File(localFile);
            FileRequestEntity entity = new FileRequestEntity(f, contentType);
            entity.setOnDatatransferProgressListener(mDataTransferListener);
            put.setRequestEntity(entity);
            status = executeMethod(put);
            
            result = (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED || status == HttpStatus.SC_NO_CONTENT);
            
            logger.debug(TAG+ "PUT to " + remoteTarget + " finished with HTTP status " + status + (!result?"(FAIL)":""));

            exhaustResponse(put.getResponseBodyAsStream());
            
        } catch (Exception e) {
            logException(e, "uploading " + localFile + " to " + remoteTarget);
            
        } finally {
            put.releaseConnection();    // let the connection available for other methods
        }
        return result;
    }

    /**
     * Tries to log in to the current URI, with the current credentials
     * 
     * @return A {@link HttpStatus}-Code of the result. SC_OK is good.
     */
    public int tryToLogin() {
        int status = 0;
        HeadMethod head = new HeadMethod(url.toString());
        try {
            status = executeMethod(head);
            boolean result = status == HttpStatus.SC_OK;
            logger.debug(TAG + "HEAD for " + url + " finished with HTTP status " + status + (!result?"(FAIL)":""));
            exhaustResponse(head.getResponseBodyAsStream());
            
        } catch (Exception e) {
            logException(e, "trying to login at " + url.toString());
            
        } finally {
            head.releaseConnection();
        }
        return status;
    }

    /**
     * Shares a remote directory or file with the received path.
     * 
     * @param path      Path of the directory to create, URL DECODED
     * @return          'True' when the directory is successfully created
     */
    public String compartir(String path, String objCompartir) {
        String resultado = "";
        int status = -1;
        PostMethod post = new PostMethod(url + WebdavUtils.encodePath(path));
        try {
            logger.debug(TAG + " Sharing directory " + path);
            
//			String postParameters = "path=" + path + "&shareType=3&publicUpload=true&permissions=31";
            NameValuePair[] parametros = {
            		new NameValuePair("path", WebdavUtils.encodePath(objCompartir)),
            		new NameValuePair("shareType", "3"),
            		new NameValuePair("publicUpload", "true"),
            		new NameValuePair("permissions", "31"),
            };
            post.setRequestBody(parametros);
            
            status = executeMethod(post);
            logger.debug(TAG + "Status returned: " + status);
                        
            if (status == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream()));

				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				resultado = response.toString();
			}
            
        } catch (Exception e) {
            logException(e, " share directory " + path);
            
        } finally {
            post.releaseConnection();    // let the connection available for other methods
        }
        return resultado;
    }
    
    /**
     * Creates a remote directory with the received path.
     * 
     * @param path      Path of the directory to create, URL DECODED
     * @return          'True' when the directory is successfully created
     */
    public boolean createDirectory(String path) {
        boolean result = false;
        int status = -1;
        MkColMethod mkcol = new MkColMethod(url + WebdavUtils.encodePath(path));
        try {
            logger.debug(TAG + " Creating directory " + path);
            status = executeMethod(mkcol);
            logger.debug(TAG + " Status returned: " + status);
            result = mkcol.succeeded();
            
            logger.debug(TAG + " MKCOL to " + path + " finished with HTTP status " + status + (!result?"(FAIL)":""));
            exhaustResponse(mkcol.getResponseBodyAsStream());
            
        } catch (Exception e) {
            logException(e, "creating directory " + path);
            
        } finally {
            mkcol.releaseConnection();    // let the connection available for other methods
        }
        return result;
    }
    
    
    /**
     * Check if a file exists in the OC server
     * 
     * @return      'Boolean.TRUE' if the file exists; 'Boolean.FALSE' it doesn't exist; NULL if couldn't be checked
     */
    public Boolean existsFile(String path) {
        HeadMethod head = new HeadMethod(url + WebdavUtils.encodePath(path));
        try {
            int status = executeMethod(head);
            logger.debug(TAG + " HEAD to " + path + " finished with HTTP status " + status + ((status != HttpStatus.SC_OK)?"(FAIL)":""));
            exhaustResponse(head.getResponseBodyAsStream());
            return (status == HttpStatus.SC_OK);
            
        } catch (Exception e) {
            logException(e, "checking existence of " + path);
            return null;
            
        } finally {
            head.releaseConnection();    // let the connection available for other methods
        }
    }


    /**
     * Requests the received method with the received timeout (milliseconds).
     * 
     * Executes the method through the inherited HttpClient.executedMethod(method).
     * 
     * Sets the socket and connection timeouts only for the method received.
     * 
     * The timeouts are both in milliseconds; 0 means 'infinite'; < 0 means 'do not change the default'
     * 
     * @param method            HTTP method request.
     * @param readTimeout       Timeout to set for data reception
     * @param conntionTimout    Timeout to set for connection establishment
     */
    public int executeMethod(HttpMethodBase method, int readTimeout, int connectionTimeout) throws HttpException, IOException {
        int oldSoTimeout = getParams().getSoTimeout();
        int oldConnectionTimeout = getHttpConnectionManager().getParams().getConnectionTimeout();
        try {
            if (readTimeout >= 0) { 
                method.getParams().setSoTimeout(readTimeout);   // this should be enough...
                getParams().setSoTimeout(readTimeout);          // ... but this looks like necessary for HTTPS
            }
            if (connectionTimeout >= 0) {
                getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
            }
            return executeMethod(method);
        } finally {
            getParams().setSoTimeout(oldSoTimeout);
            getHttpConnectionManager().getParams().setConnectionTimeout(oldConnectionTimeout);
        }
    }

    /**
     * Exhausts a not interesting HTTP response. Encouraged by HttpClient documentation.
     * 
     * @param responseBodyAsStream      InputStream with the HTTP response to exhaust.
     */
    private static void exhaustResponse(InputStream responseBodyAsStream) {
        if (responseBodyAsStream != null) {
            try {
                while (responseBodyAsStream.read(sExhaustBuffer) >= 0);
                responseBodyAsStream.close();
            
            } catch (IOException io) {
                logger.error(TAG + "Unexpected exception while exhausting not interesting HTTP response; will be IGNORED", io);
            }
        }
    }


    /**
     * Logs an exception triggered in a HTTP request. 
     * 
     * @param e         Caught exception.
     * @param doing     Suffix to add at the end of the logged message.
     */
    private static void logException(Exception e, String doing) {
        if (e instanceof HttpException) {
            logger.error(TAG + "HTTP violation while " + doing, e);

        } else if (e instanceof IOException) {
            logger.error(TAG + "Unrecovered transport exception while " + doing, e);

        } else {
            logger.error(TAG +  "Unexpected exception while " + doing, e);
        }
    }

    
    /**
     * Sets the connection and wait-for-data timeouts to be applied by default to the methods performed by this client.
     */
    public void setDefaultTimeouts(int defaultDataTimeout, int defaultConnectionTimeout) {
            getParams().setSoTimeout(defaultDataTimeout);
            getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
    }

    /**
     * Sets the base URI for the helper methods that receive paths as parameters, instead of full URLs
     * @param uri
     */
    public void setBaseUrl(String url) {
        this.url = url;
    }
    
    /**
     * Builds a DavPropertyNameSet with all prop
     * For using instead of DavConstants.PROPFIND_ALL_PROP
     * @return
     */
    public static DavPropertyNameSet getAllPropSet(){
        DavPropertyNameSet propSet = new DavPropertyNameSet();
        propSet.add(DavPropertyName.DISPLAYNAME);
        propSet.add(DavPropertyName.GETCONTENTTYPE);
        propSet.add(DavPropertyName.RESOURCETYPE);
        propSet.add(DavPropertyName.GETCONTENTLENGTH);
        propSet.add(DavPropertyName.GETLASTMODIFIED);
        propSet.add(DavPropertyName.CREATIONDATE);
        propSet.add(DavPropertyName.GETETAG);
    
        return propSet;
    }
    
}

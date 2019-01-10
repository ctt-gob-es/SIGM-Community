package es.dipucr.ownCloud;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.apache.log4j.Logger;



public class WebDAVFile {
	
	public static final Logger LOGGER =  Logger.getLogger(WebDAVFile.class);

	/* 
	 * The predefined buffer size when retrieving repository items
	 */
	private static final int BUFFER_SIZE = 1024;
	
	private byte[] file;
	private InputStream inputStream;

	/**
	 * Constructor
	 */
	public WebDAVFile() {		
	}

	/**
	 * Constructor requires input stream
	 * 
	 * @param inputStream
	 * @param mimetype
	 */
	public WebDAVFile(InputStream inputStream) {
		setInputStream(inputStream);
	}	

	/**
	 * @return
	 */
	public InputStream getInputStream() {
		
		if (this.inputStream == null)
			return new ByteArrayInputStream(file);

		return this.inputStream;
	}

	/**
	 * @param inputStream
	 * @param mimetype
	 */
	public void setInputStream(InputStream inputStream) {
		
		this.inputStream = inputStream;
		this.file = getByteArrayFromInputStream(inputStream);
		
	}

    /**
     * @param is
     * @return
     */
    public static byte[] getByteArrayFromInputStream(InputStream is) {
    	
    	ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

    	byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        
        try {
        	while ((len = is.read(buffer, 0, buffer.length)) != -1) {
        		baos.write(buffer, 0, len);
        	}
        	//is.close();

        } catch (IOException e) {
        	LOGGER.error(e.getMessage(), e);
        }
        
        return baos.toByteArray();
        //return content;
    
    }

    /**
     * @param bytes
     * @param mimetype
     * @return
     */
    public static ByteArrayDataSource createByteArrayDataSource(byte[] bytes, String mimetype) {
    	return new ByteArrayDataSource(bytes, mimetype);
    }
  
}

class ByteArrayDataSource implements DataSource {
   
	byte bytes[];
	String contentType;
   
	public ByteArrayDataSource(byte bytes[], String contentType) {
	   
		this.bytes = bytes;
		this.contentType = contentType;
       
	}
   
	public String getContentType() {
		return contentType;
	}
   
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(bytes);
	}
   
	public String getName() {
		// unknown
		throw new UnsupportedOperationException("ByteArrayDataSource.getName()");
	}
   
	public OutputStream getOutputStream() throws java.io.IOException {
		// not required, do not expose
		throw new UnsupportedOperationException("ByteArrayDataSource.getOutputStream()");
	}

}


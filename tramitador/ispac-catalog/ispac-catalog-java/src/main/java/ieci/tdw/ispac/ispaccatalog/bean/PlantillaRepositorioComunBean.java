package ieci.tdw.ispac.ispaccatalog.bean;
import java.util.Date;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.bean.ObjectBean;

public class PlantillaRepositorioComunBean extends ObjectBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static String NOMBRE = "NOMBRE";
	public final static String PATH = "PATH";
	public final static String LASTMODIFIED = "LASTMODIFIED";
	public final static String LENGTH = "LENGTH";
	
	private String nombre;
	private String path;
	private Date lastModified;
	private long length;
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public Object get(String sProperty) throws ISPACException {
		if(NOMBRE.equalsIgnoreCase(sProperty)){
			return getNombre();
		}else if (PATH.equalsIgnoreCase(sProperty)){
			return getPath();			
		}else if (LASTMODIFIED.equalsIgnoreCase(sProperty)){
			return getLastModified();
		}else if (LENGTH.equalsIgnoreCase(sProperty)){
			return getLength();
		}
		return null;
	}

	public long getLong(String sProperty) throws ISPACException {
		if (LENGTH.equalsIgnoreCase(sProperty)){
			return getLength();
		}
		return 0;
	}
	
	public Date getDate(String sProperty) throws ISPACException {
		if (LASTMODIFIED.equalsIgnoreCase(sProperty)){
			return getLastModified();
		}
		return null;
	}

	public String getString(String sProperty) throws ISPACException {
		if(NOMBRE.equalsIgnoreCase(sProperty)){
			return getNombre();
		}else if (PATH.equalsIgnoreCase(sProperty)){
			return getPath();			
		}
		return null;
	}

	public void set(String sProperty, Object value) throws ISPACException {
		if(NOMBRE.equalsIgnoreCase(sProperty)){
			setNombre((String)value);
		}else if (PATH.equalsIgnoreCase(sProperty)){
			setPath((String)value);			
		}else if (LASTMODIFIED.equalsIgnoreCase(sProperty)){
			setLastModified((Date)value);
		}else if (LENGTH.equalsIgnoreCase(sProperty)){
			setLength((long)((Long)value).longValue());
		}
	}

	public void set(String sProperty, long value) throws ISPACException {
		if (LENGTH.equalsIgnoreCase(sProperty)){
			setLength((long)((Long)value).longValue());
		}
	}
	
	public void set(String sProperty, Date value) throws ISPACException {
		if (LASTMODIFIED.equalsIgnoreCase(sProperty)){
			setLastModified((Date)value);
		}
	}

	public void set(String sProperty, String value) throws ISPACException {
		if(NOMBRE.equalsIgnoreCase(sProperty)){
			setNombre((String)value);
		}else if (PATH.equalsIgnoreCase(sProperty)){
			setPath((String)value);			
		}		
	}

	public Object getProperty(String name) {
		if(NOMBRE.equalsIgnoreCase(name)){
			return getNombre();
		}else if (PATH.equalsIgnoreCase(name)){
			return getPath();			
		}else if (LASTMODIFIED.equalsIgnoreCase(name)){
			return getLastModified();
		}else if (LENGTH.equalsIgnoreCase(name)){
			return getLength();
		}
		return null;
	}

	public void setProperty(String key, Object value) throws ISPACException {
		if(NOMBRE.equalsIgnoreCase(key)){
			setNombre((String)value);
		}else if (PATH.equalsIgnoreCase(key)){
			setPath((String)value);			
		}else if (LASTMODIFIED.equalsIgnoreCase(key)){
			setLastModified((Date)value);
		}else if (LENGTH.equalsIgnoreCase(key)){
			setLength((long)((Long)value).longValue());
		}		
	}
}
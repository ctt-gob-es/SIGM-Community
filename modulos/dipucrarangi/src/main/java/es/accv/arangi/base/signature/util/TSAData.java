/**
 * Copyright 2012 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.signature.util;

import java.net.URL;

/**
 * Información para hacer una llamada a una TSA
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 */
public class TSAData {

	private URL url;
	private String user;
	private String password;
	
	public TSAData(URL url) {
		super();
		this.url = url;
	}
	
	public TSAData(URL url, String user, String password) {
		super();
		this.url = url;
		if ((user == null || password == null) && (user != null || password != null)) {
			if (user == null) user = "";
			if (password == null) password = "";
		}
		this.user = user;
		this.password = password;
	}

	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return url + " | " + user + " | " + password;
	}
}

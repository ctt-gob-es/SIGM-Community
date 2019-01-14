package es.sigem.dipcoruna.framework.service.util;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class ParametersWrapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParametersWrapper.class);
	
	private String protocolo;	
	private Map<String, List<String>> parametros = new HashMap<String, List<String>>();
	
	public ParametersWrapper (String argumento) {
		if (!StringUtils.hasLength(argumento)) {
			return;
		}
		validarFormatoArgumento(argumento);
		protocolo = extraerProtocolo(argumento);
		parametros = parsearParametros(argumento);
	}
		
	private void validarFormatoArgumento(String argumento) {
		if (!StringUtils.hasText(argumento)) {
			LOGGER.error("La cadena de argumentos no puede ser vacía");
			throw new IllegalArgumentException("La cadena de argumentos no puede ser vacía");
		}
		
		if (argumento.indexOf(":") == -1) {
			LOGGER.error("La cadena {} no contiene ':' para indicar el protocolo", argumento);
			throw new IllegalArgumentException("La cadena no contiene ':' para indicar el protocolo");
		}				
	}


	private String extraerProtocolo(String argumento) {		
		return argumento.substring(0, argumento.indexOf(":"));
	}
	
	private String extraerParametros(String argumento) {
		return argumento.substring(argumento.indexOf(":") + 1);
	}
	
	
	private Map<String, List<String>> parsearParametros(String argumento) {
		Map<String, List<String>> parametros = new LinkedHashMap<String, List<String>>();
		
		final String[] pairs = extraerParametros(argumento).split("&");		
		for (String pair : pairs) {			
			final String key = obtenerKey(pair);
			final String value = botenerValue(pair);
					
			if (!parametros.containsKey(key)) {
				parametros.put(key, new LinkedList<String>());
			}									
			parametros.get(key).add(value);
		}	
		return  parametros;
	}
	

	private String obtenerKey(String pair) {
		int posicionIgual = pair.indexOf("=");
		if (posicionIgual == -1) {
			return pair;			
		}
		
		try {
			return URLDecoder.decode(pair.substring(0, posicionIgual), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Error al decodificar el valor de la clave {}", pair, e);
			throw new RuntimeException("Error al decodificar el valor de la clave " + pair, e);
		}		
	}
	
	
	private String botenerValue(String pair) {
		int posicionIgual = pair.indexOf("=");
		if (posicionIgual == -1 || pair.length() <= posicionIgual + 1) {
			return null;			
		}
				
		try {
			return URLDecoder.decode(pair.substring(posicionIgual + 1), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Error al decodificar el valor de la clave {}", pair, e);
			throw new RuntimeException("Error al decodificar el valor de la clave " + pair, e);
		}		
	}
	
	
	

	public String getProtocolo() {
		return protocolo;
	}
	
	
	public boolean existeParametro (String key) {
		return parametros.containsKey(key);
	}
	
	public List<String> getValue(String key) {
		return parametros.get(key);	
	}
	
	public String getSimpleValue(String key) {
		List<String> values = getValue(key);
		if (CollectionUtils.isEmpty(values)) {
			return null;
		}
		return values.get(0);		
	}

	@Override
	public String toString() {
		return "ParametersWrapper [protocolo=" + protocolo + ", parametros=" + parametros + "]";
	}	
}

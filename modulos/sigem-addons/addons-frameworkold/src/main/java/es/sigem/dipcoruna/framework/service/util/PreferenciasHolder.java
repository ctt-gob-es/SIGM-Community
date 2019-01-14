package es.sigem.dipcoruna.framework.service.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferenciasHolder {
	private final Preferences preferences;

	
	PreferenciasHolder(String claveAplicacion) {	
		preferences = Preferences.userRoot().node(claveAplicacion);
	}

	
	public String getProperty(final String key) {
		return preferences.get(key, null);
	}

	
	public void putProperty(final String key, final Object value) {
		if (value instanceof String) {
			preferences.put(key, (String)value);			
		}
		else if (value instanceof Boolean) {
			preferences.putBoolean(key, (Boolean)value);
		}
		else if (value instanceof Long) {
			preferences.putLong(key, (Long)value);
		}
		else {
			throw new IllegalArgumentException("Tipo de propiedad no contemplada");
		}
	}
	
	
	public List<String> getAllPropertyKeys() {		
		try {
			return Arrays.asList(preferences.keys());
		} catch (BackingStoreException e) {
			throw new RuntimeException("Error al obtener todas las claves", e);
		}
	}

	public Map<String, String> getPropertiesAsMap() {	
		Map<String, String> preferncesMap = new HashMap<String, String>();
		for(String key : getAllPropertyKeys()) {
			preferncesMap.put(key, getProperty(key));
		}
		return preferncesMap;	
	}
	
	public void clearAllKeys() {
		try {
			preferences.clear();
		} catch (BackingStoreException e) {
			throw new RuntimeException("Error al borrar todas las claves", e);
		}
	}
	
	public void removeProperty(final String key) {
        preferences.remove(key);
    }
}

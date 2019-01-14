package es.dipucr.sigemaytos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import es.dipucr.sigemaytos.config.EntidadesProperties;
import es.dipucr.sigemaytos.domain.beans.Entidad;

public class EntidadManager {

	private List<Entidad> listEntidades;
	private HashMap<String, Entidad> hashEntidades;
	
	public EntidadManager(){
		Properties properties = EntidadesProperties.getInstance().getProperties();
		
		listEntidades = new ArrayList<Entidad>();
		hashEntidades = new HashMap<String, Entidad>();
		
		Set<String> keys = properties.stringPropertyNames();
		for (String key : keys) {
			Entidad entidad = new Entidad(properties.getProperty(key));
			listEntidades.add(entidad);
			hashEntidades.put(key, entidad);
	    }
		Collections.sort(listEntidades);
	}

	public List<Entidad> getListEntidades() {
		return listEntidades;
	}

	public void setListEntidades(List<Entidad> listEntidades) {
		this.listEntidades = listEntidades;
	}

	public HashMap<String, Entidad> getHashEntidades() {
		return hashEntidades;
	}

	public void setHashEntidades(HashMap<String, Entidad> hashEntidades) {
		this.hashEntidades = hashEntidades;
	}
	
}

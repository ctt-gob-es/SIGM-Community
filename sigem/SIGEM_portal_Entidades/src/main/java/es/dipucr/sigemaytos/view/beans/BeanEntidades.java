package es.dipucr.sigemaytos.view.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;

import es.dipucr.sigemaytos.domain.EntidadManager;
import es.dipucr.sigemaytos.domain.beans.Entidad;

public class BeanEntidades {

	private List<Entidad> listEntidades;
	private HashMap<String, Entidad> hashEntidades;
	private List<SelectItem> listComboEntidades;
	private String idEntidad;
	private Entidad entidadActual;
	
	public BeanEntidades(){
		EntidadManager manager = new EntidadManager();
		listEntidades = manager.getListEntidades();
		hashEntidades = manager.getHashEntidades();
		listComboEntidades = new ArrayList<SelectItem>();
		for (Entidad entidad : listEntidades){
			listComboEntidades.add(new SelectItem(entidad.getId(), entidad.getNombre()));
		}
		entidadActual = listEntidades.get(0);
	}

	public List<Entidad> getListEntidades() {
		return listEntidades;
	}

	public void setListEntidades(List<Entidad> listEntidades) {
		this.listEntidades = listEntidades;
	}

	public String getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(String idEntidad) {
		this.idEntidad = idEntidad;
	}

	public Entidad getEntidadActual() {
		return entidadActual;
	}

	public void setEntidadActual(Entidad entidadActual) {
		this.entidadActual = entidadActual;
	}

	public List<SelectItem> getListComboEntidades() {
		return listComboEntidades;
	}

	public void setListComboEntidades(List<SelectItem> listComboEntidades) {
		this.listComboEntidades = listComboEntidades;
	}

	public void cargarDatosEntidad(){
		
		entidadActual = hashEntidades.get(idEntidad);
	}
}

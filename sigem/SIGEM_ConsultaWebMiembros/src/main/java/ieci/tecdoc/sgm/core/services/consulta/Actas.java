package ieci.tecdoc.sgm.core.services.consulta;

import java.util.ArrayList;
import java.util.List;

public class Actas {
	private List actas;
	
	public Actas(){
		actas = new ArrayList();
	}

	public List getActas() {
		return actas;
	}

	public void setActas(List actas) {
		this.actas = actas;
	}
	
	public int count() {
		return actas.size();
	}

	public Acta get(int index) {
		return (Acta)actas.get(index);
	}

	/**
	 * Añade un nuevo expediente a la colección.
	 * @param expediente Nuevo expediente a añadir.
	 */
	public void add(Acta propuesta) {
		actas.add(propuesta);
	}


}

package ieci.tecdoc.sgm.core.services.consulta;

import java.util.ArrayList;
import java.util.List;

public class Propuestas {

	private List propuestas;
	
	public Propuestas(){
		propuestas = new ArrayList();
	}
	
	public int count() {
		return propuestas.size();
	}

	/**
	 * Devuelve el expediente de la posición indicada dentro de la colección
	 * @param index Posición del expediente a recuperar.
	 * @return Expediente asociado a registro.
	 */
	public Propuesta get(int index) {
		return (Propuesta)propuestas.get(index);
	}

	/**
	 * Añade un nuevo expediente a la colección.
	 * @param expediente Nuevo expediente a añadir.
	 */
	public void add(Propuesta propuesta) {
		propuestas.add(propuesta);
	}

	public List getPropuestas() {
		return propuestas;
	}

	public void setPropuestas(List propuestas) {
		this.propuestas = propuestas;
	}

}

package ieci.tecdoc.sgm.ct.database.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class Actas extends ArrayList implements Serializable {
	
	 /**
    * Constructor de clase
    */

	public Actas() {
	}

	/**
	 * Devuelve el número de expedientes de la colección.
	 * @return int Número de expedientes de la colección.
	 */
	public int count() {
		return size();
	}

	/**
	 * Devuelve el expediente de la posición indicada dentro de la colección
	 * @param index Posición del expediente a recuperar.
	 * @return Expediente asociado a registro.
	 */
	public Object get(int index) {
		return (Acta)super.get(index);
	}
}


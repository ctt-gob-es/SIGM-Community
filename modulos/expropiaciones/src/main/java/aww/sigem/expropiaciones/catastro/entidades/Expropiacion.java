package aww.sigem.expropiaciones.catastro.entidades;

import java.util.ArrayList;

public class Expropiacion {
	/**
	 * Datos generales de la expropiacion
	 */
   //TODO: Ver la relacion de las entidades.
   //TODO: Posibilidad 1: Dentro de la entidad Expropiado, crear un listado de fincas expropiadas.
   //TODO: Posibilidad 2: Dentro de la entidad Finca, crear un listado de expropiados.
	
	
	/**
	 * Listados Expropiados y Fincas expropiadas. 
	 */
	private ArrayList expropiados = new ArrayList();
	private ArrayList fincas = new ArrayList();
	public ArrayList getExpropiados() {
		return expropiados;
	}
	
	public void addExpropiado(Expropiado expropiado){
		this.expropiados.add(expropiado);
	}
	public void addFinca(Finca finca){
		this.fincas.add(finca);
	}
	
	public void setExpropiados(ArrayList expropiados) {
		this.expropiados = expropiados;
	}
	public ArrayList getFincas() {
		return fincas;
	}
	public void setFincas(ArrayList fincas) {
		this.fincas = fincas;
	}

}

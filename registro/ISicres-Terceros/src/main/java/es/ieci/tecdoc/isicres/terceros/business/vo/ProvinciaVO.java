package es.ieci.tecdoc.isicres.terceros.business.vo;

import java.util.List;

import es.ieci.tecdoc.fwktd.core.model.Entity;

/**
 *
 * @author IECISA
 *
 */
public class ProvinciaVO extends Entity {

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public List<CiudadVO> getCiudades() {
		return ciudades;
	}

	public void setCiudades(List<CiudadVO> ciudades) {
		this.ciudades = ciudades;
	}

	// Members
	protected String nombre;

	protected String codigo;
	
	private List<CiudadVO> ciudades = null;

	private static final long serialVersionUID = 4243038324427058924L;
	
	public String toString() {
		return nombre;
	}
	
	public boolean equals(Object o) {
		if (null == o || !(o instanceof ProvinciaVO)){
			return false;
		}
		
		ProvinciaVO comparador = (ProvinciaVO) o;
		return comparador.getNombre().equals(nombre);
	}
}

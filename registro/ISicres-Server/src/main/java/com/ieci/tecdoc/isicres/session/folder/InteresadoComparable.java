package com.ieci.tecdoc.isicres.session.folder;

import org.apache.commons.lang.StringUtils;

public class InteresadoComparable implements Comparable<InteresadoComparable>{
		
	private InteresadoComparable representante;

	private String docIdentidad;
	private String interName;
	private int idTercero;
	
	public InteresadoComparable(){
		super();
	}
	
	public InteresadoComparable getRepresentante() {
		return representante;
	}

	public void setRepresentante(InteresadoComparable representante) {
		this.representante = representante;
	}

	public String getDocIdentidad() {
		return docIdentidad;
	}

	public void setDocIdentidad(String docIdentidad) {
		this.docIdentidad = docIdentidad;
	}

	public int getIdTercero() {
		return idTercero;
	}

	public void setIdTercero(int idTercero) {
		this.idTercero = idTercero;
	}

	public String getInterName() {
		return interName;
	}

	public void setInterName(String interName) {
		this.interName = interName;
	}

	public int compareTo(InteresadoComparable interesadoComparar) {
		
		int resultado = 0;
		
		if(null != interesadoComparar && null != docIdentidad){
			resultado = docIdentidad.compareTo(interesadoComparar.getDocIdentidad());
		}
		
		return resultado;
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((docIdentidad == null) ? 0 : docIdentidad.hashCode());
		result = prime * result + idTercero;
		result = prime * result + ((interName == null) ? 0 : interName.hashCode());
		result = prime * result + ((representante == null) ? 0 : representante.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		
		InteresadoComparable other = (InteresadoComparable) obj;
		if (StringUtils.isEmpty(docIdentidad)) {
			if (StringUtils.isNotEmpty(other.getDocIdentidad())){
				return false;
			}
		} else if (!docIdentidad.equals(other.getDocIdentidad())){
			return false;
		}		
		if (StringUtils.isEmpty(interName)) {
			if (StringUtils.isNotEmpty(other.interName)){
				return false;
			}
		} else if (!interName.equals(other.interName)){
			return false;
		}
		if (representante == null) {
			if (other.representante != null){
				return false;
			}
		} else if (!representante.equals(other.representante)){
			return false;
		}
		return true;
	}
	
	public String toString(){
		String resultado = "";
		
		if(StringUtils.isNotEmpty(getInterName())){
			resultado += getInterName();
		}
		
		if(StringUtils.isNotEmpty(getDocIdentidad()) && !resultado.contains(getDocIdentidad().trim())){
			resultado = getDocIdentidad() + " " + resultado;
		}
		
		if(null != getRepresentante() && StringUtils.isNotEmpty(getRepresentante().toString())){
			resultado += " - Representante: " + getRepresentante().toString();
		}
		
		return resultado;
	}
}

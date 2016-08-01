package ieci.tecdoc.sgm.ct.database.datatypes;

import java.util.Vector;

public class ActasImpl {

	private String numeroExpediente;
	
	private String dni;
	
	private Actas actas;

	/**
	 * Constructor de clase
	 */
	public ActasImpl(){
		
		numeroExpediente = "";
		setActas(new Actas());

	}
	public String getDni(){
		return this.dni;
	}
	
	public void setDni(String dni){
		this.dni = dni;
	}
	
	/**
	 * Devuelve el numero del expediente.
	 * @return String Numero del expediente.
	 */
	public String getNumeroExpediente(){
		return this.numeroExpediente;
	}




	/**
	 * Establece un numero de expediente.
	 * @param numeroExpediente Numero de expediente. 
	 */	
	public void setNumeroExpediente(String numeroExpediente){
		this.numeroExpediente = numeroExpediente;
	}
	
	
	public Actas getActas() {
		return actas;
	}
	public void setActas(Actas actas) {
		this.actas = actas;
	}


}


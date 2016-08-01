package ieci.tecdoc.sgm.ct.database.datatypes;


public class PropuestaImpl{

	protected String numeroExpediente;
	
	protected String dni;
	
	protected Propuestas propuestas;

	/**
	 * Constructor de clase
	 */
	public PropuestaImpl(){
		
		numeroExpediente = "";
		propuestas = null;

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
	 * Devuelve los DocumentosPropuesta de la propuesta.
	 * @return List propuesta.
	 */   
	public Propuestas getDocumentosPropuesta(){
		return this.propuestas;
	}


	/**
	 * Establece un numero de expediente.
	 * @param numeroExpediente Numero de expediente. 
	 */	
	public void setNumeroExpediente(String numeroExpediente){
		this.numeroExpediente = numeroExpediente;
	}
	
	
	/**
	 * Establece el NIF del interesado.
	 * @param NIF NIF del interesado.
	 */   
	public void setDocumentosPropuesta(Propuestas documentosPropuesta){
		this.propuestas = documentosPropuesta;
	}


	/**
	 * Recoge los valores de la instancia en una cadena xml
	 * @param header Si se incluye la cabecera
	 * @return los datos en formato xml
	 */
	public String toXML(boolean header) {
		/*XmlTextBuilder bdr;
		String tagName = "Document";

		bdr = new XmlTextBuilder();
		if (header)
			bdr.setStandardHeader();

		bdr.addOpeningTag(tagName);

		bdr.addSimpleElement("Guid", guid);
		bdr.addSimpleElement("Content", content.toString());
		bdr.addSimpleElement("Hash", hash);
		bdr.addSimpleElement("Extension", extension);
		bdr.addSimpleElement("Timestamp", timestamp.toString());

		bdr.addClosingTag(tagName);

		return bdr.getText();*/
		return null;
	}

	/**
	 * Devuelve los valores de la instancia en una cadena de caracteres.
	 */
	public String toString() {
		return toXML(false);
	}



}

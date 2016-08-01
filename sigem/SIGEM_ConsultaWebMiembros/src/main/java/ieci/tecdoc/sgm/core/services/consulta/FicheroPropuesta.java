package ieci.tecdoc.sgm.core.services.consulta;

import java.util.Date;

public class FicheroPropuesta {
	
	protected String guid;

	protected String titulo;
	
	protected Date fechaFirma;
	
	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Constructor de clase
	 */
	public FicheroPropuesta(){
		
		fechaFirma = null;
		guid = null;
		titulo = null;
	}
	
	/**
	 * Devuelve el Guid del Hito.
	 * @return String Guid del Hito.
	 */
	public Date getFechaFirma(){
		
		return fechaFirma;
	}


	/**
	 * Devuelve el Guid del Fichero.
	 * @return String Guid del Fichero.
	 */   
	public String getGuid(){
		return guid;
	}

	/**
	 * Devuelve el titulo. Para presentacion.
	 * @return String Presentacion.
	 */   
	public String getTitulo(){
		return titulo;
	}
	

	/**
	 * Establece un Guid de un Hito para el Fichero.
	 * @param guidHito Guid de un Hito. 
	 */	
	public void setFechaFirma(Date fechaFirma){
		this.fechaFirma = fechaFirma;
	}

	
	/**
	 * Establece el guid de un Fichero.
	 * @param guid Guid de un Fichero.
	 */   
	public void setGuid(String guid){
		this.guid = guid;
	}

		
	/**
	 * Establece el titulo de un fichero para presentacion.
	 * @param titulo Titulo de un fichero.
	 */   
	public void setTitulo(String titulo){
		this.titulo = titulo;
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

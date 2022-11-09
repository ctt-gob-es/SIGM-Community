package es.gob.afirma.signfolder.server.proxy;

import java.io.Serializable;

/**
 * Enumerado que define los tipos de autorización disponibles.
 */
public enum AuthorizedType implements Serializable {
    DELEGATE("Delegado"),
    SUBSTITUTE("Sustituto");

    /**
     * Atributo que representa el valor del enumerado.
     */
    private String value;

    /**
     * Constructor por defecto.
     * @param value Nuevo valor del enumerado.
     */
    AuthorizedType(String value){
        this.value = value;
    }

    /**
     * Método que recupera el valor del enumerado a partir de un string.
     * @param value Cadena que representa el valor a recuperar.
     * @return un objeto de tipo AuthorizedType o null si el parámetro recibido no tiene un valor válido.
     */
    public static AuthorizedType getAuthorizedType(String value){
        if(value == null){
            return null;
        }
        String val = value.toUpperCase();
        switch (val){
            case "Delegado":
                return AuthorizedType.DELEGATE;
            case "Sustituto":
                return AuthorizedType.SUBSTITUTE;
            default:
                return null;
        }
    }

	/**
	 * Get method for the <i>value</i> attribute.
	 * @return the attribute value.
	 */
	public String getValue() {
		return value;
	}
    
}

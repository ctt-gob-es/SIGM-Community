
package es.dipucr.contratacion.objeto.sw;

public class PersonaComite{
    private java.lang.String cargo;

    private java.lang.String email;

    private java.lang.String nombre;

    public PersonaComite() {
    }

    public PersonaComite(
           java.lang.String cargo,
           java.lang.String email,
           java.lang.String nombre) {
           this.cargo = cargo;
           this.email = email;
           this.nombre = nombre;
    }


    /**
     * Gets the cargo value for this PersonaComite.
     * 
     * @return cargo
     */
    public java.lang.String getCargo() {
        return cargo;
    }


    /**
     * Sets the cargo value for this PersonaComite.
     * 
     * @param cargo
     */
    public void setCargo(java.lang.String cargo) {
        this.cargo = cargo;
    }


    /**
     * Gets the email value for this PersonaComite.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this PersonaComite.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the nombre value for this PersonaComite.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this PersonaComite.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }
}

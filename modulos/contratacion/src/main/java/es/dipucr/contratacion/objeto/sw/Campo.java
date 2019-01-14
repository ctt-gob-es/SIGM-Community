
package es.dipucr.contratacion.objeto.sw;

public class Campo{
    private java.lang.String id;

    private java.lang.String valor;

    public Campo() {
    }

    public Campo(
           java.lang.String id,
           java.lang.String valor) {
           this.id = id;
           this.valor = valor;
    }


    /**
     * Gets the id value for this Campo.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Campo.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the valor value for this Campo.
     * 
     * @return valor
     */
    public java.lang.String getValor() {
        return valor;
    }


    /**
     * Sets the valor value for this Campo.
     * 
     * @param valor
     */
    public void setValor(java.lang.String valor) {
        this.valor = valor;
    }
}

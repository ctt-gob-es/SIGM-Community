package es.dipucr.contratacion.objeto.sw;

public class AplicacionPresupuestaria{
    private String anualidad;

    private String aplicPres;

    private String importe;

    public AplicacionPresupuestaria() {
    }

    public AplicacionPresupuestaria(
           String anualidad,
           String aplicPres,
           String importe) {
           this.anualidad = anualidad;
           this.aplicPres = aplicPres;
           this.importe = importe;
    }


    /**
     * Gets the anualidad value for this AplicacionPresupuestaria.
     * 
     * @return anualidad
     */
    public java.lang.String getAnualidad() {
        return anualidad;
    }


    /**
     * Sets the anualidad value for this AplicacionPresupuestaria.
     * 
     * @param anualidad
     */
    public void setAnualidad(java.lang.String anualidad) {
        this.anualidad = anualidad;
    }


    /**
     * Gets the aplicPres value for this AplicacionPresupuestaria.
     * 
     * @return aplicPres
     */
    public java.lang.String getAplicPres() {
        return aplicPres;
    }


    /**
     * Sets the aplicPres value for this AplicacionPresupuestaria.
     * 
     * @param aplicPres
     */
    public void setAplicPres(java.lang.String aplicPres) {
        this.aplicPres = aplicPres;
    }


    /**
     * Gets the importe value for this AplicacionPresupuestaria.
     * 
     * @return importe
     */
    public java.lang.String getImporte() {
        return importe;
    }


    /**
     * Sets the importe value for this AplicacionPresupuestaria.
     * 
     * @param importe
     */
    public void setImporte(java.lang.String importe) {
        this.importe = importe;
    }
 
}

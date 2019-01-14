
package es.dipucr.contratacion.objeto.sw;

public class Periodo{
    private java.lang.String[] description;

    private Campo[] descriptionCode;

    private java.lang.String duracion;

    private Campo durationMeasure;

    private java.util.Calendar endCalendar;

    private java.util.Calendar startCalendar;

    private Campo tipoDuracion;

    public Periodo() {
    }

    public Periodo(
           java.lang.String[] description,
           Campo[] descriptionCode,
           java.lang.String duracion,
           Campo durationMeasure,
           java.util.Calendar endCalendar,
           java.util.Calendar startCalendar,
           Campo tipoDuracion) {
           this.description = description;
           this.descriptionCode = descriptionCode;
           this.duracion = duracion;
           this.durationMeasure = durationMeasure;
           this.endCalendar = endCalendar;
           this.startCalendar = startCalendar;
           this.tipoDuracion = tipoDuracion;
    }


    /**
     * Gets the description value for this Periodo.
     * 
     * @return description
     */
    public java.lang.String[] getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Periodo.
     * 
     * @param description
     */
    public void setDescription(java.lang.String[] description) {
        this.description = description;
    }


    /**
     * Gets the descriptionCode value for this Periodo.
     * 
     * @return descriptionCode
     */
    public Campo[] getDescriptionCode() {
        return descriptionCode;
    }


    /**
     * Sets the descriptionCode value for this Periodo.
     * 
     * @param descriptionCode
     */
    public void setDescriptionCode(Campo[] descriptionCode) {
        this.descriptionCode = descriptionCode;
    }


    /**
     * Gets the duracion value for this Periodo.
     * 
     * @return duracion
     */
    public java.lang.String getDuracion() {
        return duracion;
    }


    /**
     * Sets the duracion value for this Periodo.
     * 
     * @param duracion
     */
    public void setDuracion(java.lang.String duracion) {
        this.duracion = duracion;
    }


    /**
     * Gets the durationMeasure value for this Periodo.
     * 
     * @return durationMeasure
     */
    public Campo getDurationMeasure() {
        return durationMeasure;
    }


    /**
     * Sets the durationMeasure value for this Periodo.
     * 
     * @param durationMeasure
     */
    public void setDurationMeasure(Campo durationMeasure) {
        this.durationMeasure = durationMeasure;
    }


    /**
     * Gets the endCalendar value for this Periodo.
     * 
     * @return endCalendar
     */
    public java.util.Calendar getEndCalendar() {
        return endCalendar;
    }


    /**
     * Sets the endCalendar value for this Periodo.
     * 
     * @param endCalendar
     */
    public void setEndCalendar(java.util.Calendar endCalendar) {
        this.endCalendar = endCalendar;
    }


    /**
     * Gets the startCalendar value for this Periodo.
     * 
     * @return startCalendar
     */
    public java.util.Calendar getStartCalendar() {
        return startCalendar;
    }


    /**
     * Sets the startCalendar value for this Periodo.
     * 
     * @param startCalendar
     */
    public void setStartCalendar(java.util.Calendar startCalendar) {
        this.startCalendar = startCalendar;
    }


    /**
     * Gets the tipoDuracion value for this Periodo.
     * 
     * @return tipoDuracion
     */
    public Campo getTipoDuracion() {
        return tipoDuracion;
    }


    /**
     * Sets the tipoDuracion value for this Periodo.
     * 
     * @param tipoDuracion
     */
    public void setTipoDuracion(Campo tipoDuracion) {
        this.tipoDuracion = tipoDuracion;
    }
}

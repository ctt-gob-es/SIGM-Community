
package es.dipucr.contratacion.objeto.sw;

import java.util.Calendar;

public class Periodo{
    private String[] description;
    private Campo[] descriptionCode;
    private String duracion;
    private Campo durationMeasure;
    private Calendar endCalendar;
    private Calendar startCalendar;
    private Campo tipoDuracion;

    public Periodo() {
    }

    public Periodo(
           String[] description,
           Campo[] descriptionCode,
           String duracion,
           Campo durationMeasure,
           Calendar endCalendar,
           Calendar startCalendar,
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
    public String[] getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Periodo.
     * 
     * @param description
     */
    public void setDescription(String[] description) {
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
    public String getDuracion() {
        return duracion;
    }


    /**
     * Sets the duracion value for this Periodo.
     * 
     * @param duracion
     */
    public void setDuracion(String duracion) {
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
    public Calendar getEndCalendar() {
        return endCalendar;
    }


    /**
     * Sets the endCalendar value for this Periodo.
     * 
     * @param endCalendar
     */
    public void setEndCalendar(Calendar endCalendar) {
        this.endCalendar = endCalendar;
    }


    /**
     * Gets the startCalendar value for this Periodo.
     * 
     * @return startCalendar
     */
    public Calendar getStartCalendar() {
        return startCalendar;
    }


    /**
     * Sets the startCalendar value for this Periodo.
     * 
     * @param startCalendar
     */
    public void setStartCalendar(Calendar startCalendar) {
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

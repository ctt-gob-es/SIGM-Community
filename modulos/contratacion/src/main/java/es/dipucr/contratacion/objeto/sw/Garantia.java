
package es.dipucr.contratacion.objeto.sw;

public class Garantia{
    private java.lang.String amountRate;

    private java.util.Calendar constitutionPeriod;

    private java.lang.String[] descripcion;

    private java.lang.String importeGarantia;

    private java.lang.String periodoGarantia;

    private Campo tipoGarantia;

    private Campo tipoPeriodo;

    public Garantia() {
    }

    public Garantia(
           java.lang.String amountRate,
           java.util.Calendar constitutionPeriod,
           java.lang.String[] descripcion,
           java.lang.String importeGarantia,
           java.lang.String periodoGarantia,
           Campo tipoGarantia,
           Campo tipoPeriodo) {
           this.amountRate = amountRate;
           this.constitutionPeriod = constitutionPeriod;
           this.descripcion = descripcion;
           this.importeGarantia = importeGarantia;
           this.periodoGarantia = periodoGarantia;
           this.tipoGarantia = tipoGarantia;
           this.tipoPeriodo = tipoPeriodo;
    }


    /**
     * Gets the amountRate value for this Garantia.
     * 
     * @return amountRate
     */
    public java.lang.String getAmountRate() {
        return amountRate;
    }


    /**
     * Sets the amountRate value for this Garantia.
     * 
     * @param amountRate
     */
    public void setAmountRate(java.lang.String amountRate) {
        this.amountRate = amountRate;
    }


    /**
     * Gets the constitutionPeriod value for this Garantia.
     * 
     * @return constitutionPeriod
     */
    public java.util.Calendar getConstitutionPeriod() {
        return constitutionPeriod;
    }


    /**
     * Sets the constitutionPeriod value for this Garantia.
     * 
     * @param constitutionPeriod
     */
    public void setConstitutionPeriod(java.util.Calendar constitutionPeriod) {
        this.constitutionPeriod = constitutionPeriod;
    }


    /**
     * Gets the descripcion value for this Garantia.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this Garantia.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the importeGarantia value for this Garantia.
     * 
     * @return importeGarantia
     */
    public java.lang.String getImporteGarantia() {
        return importeGarantia;
    }


    /**
     * Sets the importeGarantia value for this Garantia.
     * 
     * @param importeGarantia
     */
    public void setImporteGarantia(java.lang.String importeGarantia) {
        this.importeGarantia = importeGarantia;
    }


    /**
     * Gets the periodoGarantia value for this Garantia.
     * 
     * @return periodoGarantia
     */
    public java.lang.String getPeriodoGarantia() {
        return periodoGarantia;
    }


    /**
     * Sets the periodoGarantia value for this Garantia.
     * 
     * @param periodoGarantia
     */
    public void setPeriodoGarantia(java.lang.String periodoGarantia) {
        this.periodoGarantia = periodoGarantia;
    }


    /**
     * Gets the tipoGarantia value for this Garantia.
     * 
     * @return tipoGarantia
     */
    public Campo getTipoGarantia() {
        return tipoGarantia;
    }


    /**
     * Sets the tipoGarantia value for this Garantia.
     * 
     * @param tipoGarantia
     */
    public void setTipoGarantia(Campo tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }


    /**
     * Gets the tipoPeriodo value for this Garantia.
     * 
     * @return tipoPeriodo
     */
    public Campo getTipoPeriodo() {
        return tipoPeriodo;
    }


    /**
     * Sets the tipoPeriodo value for this Garantia.
     * 
     * @param tipoPeriodo
     */
    public void setTipoPeriodo(Campo tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

}

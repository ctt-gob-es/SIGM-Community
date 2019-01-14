
package es.dipucr.contratacion.objeto.sw;

public class Lote {
    private Campo[] cpv;

    private java.lang.String descripcion;

    private java.lang.String idLote;

    private Campo lugarEjecucionContratoPais;

    private Campo lugarEjecucionContratoPaisNUTS;

    private Campo lugarEjecucionContratoProvincia;

    private java.lang.Double presupuestoConIva;

    private java.lang.Double presupuestoSinIva;

    public Lote() {
    }

    public Lote(
           Campo[] cpv,
           java.lang.String descripcion,
           java.lang.String idLote,
           Campo lugarEjecucionContratoPais,
           Campo lugarEjecucionContratoPaisNUTS,
           Campo lugarEjecucionContratoProvincia,
           java.lang.Double presupuestoConIva,
           java.lang.Double presupuestoSinIva) {
           this.cpv = cpv;
           this.descripcion = descripcion;
           this.idLote = idLote;
           this.lugarEjecucionContratoPais = lugarEjecucionContratoPais;
           this.lugarEjecucionContratoPaisNUTS = lugarEjecucionContratoPaisNUTS;
           this.lugarEjecucionContratoProvincia = lugarEjecucionContratoProvincia;
           this.presupuestoConIva = presupuestoConIva;
           this.presupuestoSinIva = presupuestoSinIva;
    }


    /**
     * Gets the cpv value for this Lote.
     * 
     * @return cpv
     */
    public Campo[] getCpv() {
        return cpv;
    }


    /**
     * Sets the cpv value for this Lote.
     * 
     * @param cpv
     */
    public void setCpv(Campo[] cpv) {
        this.cpv = cpv;
    }


    /**
     * Gets the descripcion value for this Lote.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this Lote.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the idLote value for this Lote.
     * 
     * @return idLote
     */
    public java.lang.String getIdLote() {
        return idLote;
    }


    /**
     * Sets the idLote value for this Lote.
     * 
     * @param idLote
     */
    public void setIdLote(java.lang.String idLote) {
        this.idLote = idLote;
    }


    /**
     * Gets the lugarEjecucionContratoPais value for this Lote.
     * 
     * @return lugarEjecucionContratoPais
     */
    public Campo getLugarEjecucionContratoPais() {
        return lugarEjecucionContratoPais;
    }


    /**
     * Sets the lugarEjecucionContratoPais value for this Lote.
     * 
     * @param lugarEjecucionContratoPais
     */
    public void setLugarEjecucionContratoPais(Campo lugarEjecucionContratoPais) {
        this.lugarEjecucionContratoPais = lugarEjecucionContratoPais;
    }


    /**
     * Gets the lugarEjecucionContratoPaisNUTS value for this Lote.
     * 
     * @return lugarEjecucionContratoPaisNUTS
     */
    public Campo getLugarEjecucionContratoPaisNUTS() {
        return lugarEjecucionContratoPaisNUTS;
    }


    /**
     * Sets the lugarEjecucionContratoPaisNUTS value for this Lote.
     * 
     * @param lugarEjecucionContratoPaisNUTS
     */
    public void setLugarEjecucionContratoPaisNUTS(Campo lugarEjecucionContratoPaisNUTS) {
        this.lugarEjecucionContratoPaisNUTS = lugarEjecucionContratoPaisNUTS;
    }


    /**
     * Gets the lugarEjecucionContratoProvincia value for this Lote.
     * 
     * @return lugarEjecucionContratoProvincia
     */
    public Campo getLugarEjecucionContratoProvincia() {
        return lugarEjecucionContratoProvincia;
    }


    /**
     * Sets the lugarEjecucionContratoProvincia value for this Lote.
     * 
     * @param lugarEjecucionContratoProvincia
     */
    public void setLugarEjecucionContratoProvincia(Campo lugarEjecucionContratoProvincia) {
        this.lugarEjecucionContratoProvincia = lugarEjecucionContratoProvincia;
    }


    /**
     * Gets the presupuestoConIva value for this Lote.
     * 
     * @return presupuestoConIva
     */
    public java.lang.Double getPresupuestoConIva() {
        return presupuestoConIva;
    }


    /**
     * Sets the presupuestoConIva value for this Lote.
     * 
     * @param presupuestoConIva
     */
    public void setPresupuestoConIva(java.lang.Double presupuestoConIva) {
        this.presupuestoConIva = presupuestoConIva;
    }


    /**
     * Gets the presupuestoSinIva value for this Lote.
     * 
     * @return presupuestoSinIva
     */
    public java.lang.Double getPresupuestoSinIva() {
        return presupuestoSinIva;
    }


    /**
     * Sets the presupuestoSinIva value for this Lote.
     * 
     * @param presupuestoSinIva
     */
    public void setPresupuestoSinIva(java.lang.Double presupuestoSinIva) {
        this.presupuestoSinIva = presupuestoSinIva;
    }
}

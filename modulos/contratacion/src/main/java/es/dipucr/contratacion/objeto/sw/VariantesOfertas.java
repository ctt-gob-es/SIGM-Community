
package es.dipucr.contratacion.objeto.sw;

public class VariantesOfertas{
    private java.lang.String[] descVariantes;

    private java.lang.String numMaxVar;

    private boolean varianteOferta;

    public VariantesOfertas() {
    }

    public VariantesOfertas(
           java.lang.String[] descVariantes,
           java.lang.String numMaxVar,
           boolean varianteOferta) {
           this.descVariantes = descVariantes;
           this.numMaxVar = numMaxVar;
           this.varianteOferta = varianteOferta;
    }


    /**
     * Gets the descVariantes value for this VariantesOfertas.
     * 
     * @return descVariantes
     */
    public java.lang.String[] getDescVariantes() {
        return descVariantes;
    }


    /**
     * Sets the descVariantes value for this VariantesOfertas.
     * 
     * @param descVariantes
     */
    public void setDescVariantes(java.lang.String[] descVariantes) {
        this.descVariantes = descVariantes;
    }


    /**
     * Gets the numMaxVar value for this VariantesOfertas.
     * 
     * @return numMaxVar
     */
    public java.lang.String getNumMaxVar() {
        return numMaxVar;
    }


    /**
     * Sets the numMaxVar value for this VariantesOfertas.
     * 
     * @param numMaxVar
     */
    public void setNumMaxVar(java.lang.String numMaxVar) {
        this.numMaxVar = numMaxVar;
    }


    /**
     * Gets the varianteOferta value for this VariantesOfertas.
     * 
     * @return varianteOferta
     */
    public boolean isVarianteOferta() {
        return varianteOferta;
    }


    /**
     * Sets the varianteOferta value for this VariantesOfertas.
     * 
     * @param varianteOferta
     */
    public void setVarianteOferta(boolean varianteOferta) {
        this.varianteOferta = varianteOferta;
    }
}

package es.dipucr.contratacion.objeto.sw;

public class FundacionPrograma{
    private java.lang.String programa;

    private Campo programasFinanciacionCode;

    public FundacionPrograma() {
    }

    public FundacionPrograma(
           java.lang.String programa,
           Campo programasFinanciacionCode) {
           this.programa = programa;
           this.programasFinanciacionCode = programasFinanciacionCode;
    }


    /**
     * Gets the programa value for this FundacionPrograma.
     * 
     * @return programa
     */
    public java.lang.String getPrograma() {
        return programa;
    }


    /**
     * Sets the programa value for this FundacionPrograma.
     * 
     * @param programa
     */
    public void setPrograma(java.lang.String programa) {
        this.programa = programa;
    }


    /**
     * Gets the programasFinanciacionCode value for this FundacionPrograma.
     * 
     * @return programasFinanciacionCode
     */
    public Campo getProgramasFinanciacionCode() {
        return programasFinanciacionCode;
    }


    /**
     * Sets the programasFinanciacionCode value for this FundacionPrograma.
     * 
     * @param programasFinanciacionCode
     */
    public void setProgramasFinanciacionCode(Campo programasFinanciacionCode) {
        this.programasFinanciacionCode = programasFinanciacionCode;
    }
}

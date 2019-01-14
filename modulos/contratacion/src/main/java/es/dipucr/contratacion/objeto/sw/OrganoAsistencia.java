package es.dipucr.contratacion.objeto.sw;

public class OrganoAsistencia{
    private java.lang.String identificacion;

    private PersonalContacto informacionOC;

    private java.lang.String nombreOrgAsist;

    public OrganoAsistencia() {
    }

    public OrganoAsistencia(
           java.lang.String identificacion,
           PersonalContacto informacionOC,
           java.lang.String nombreOrgAsist) {
           this.identificacion = identificacion;
           this.informacionOC = informacionOC;
           this.nombreOrgAsist = nombreOrgAsist;
    }


    /**
     * Gets the identificacion value for this OrganoAsistencia.
     * 
     * @return identificacion
     */
    public java.lang.String getIdentificacion() {
        return identificacion;
    }


    /**
     * Sets the identificacion value for this OrganoAsistencia.
     * 
     * @param identificacion
     */
    public void setIdentificacion(java.lang.String identificacion) {
        this.identificacion = identificacion;
    }


    /**
     * Gets the informacionOC value for this OrganoAsistencia.
     * 
     * @return informacionOC
     */
    public PersonalContacto getInformacionOC() {
        return informacionOC;
    }


    /**
     * Sets the informacionOC value for this OrganoAsistencia.
     * 
     * @param informacionOC
     */
    public void setInformacionOC(PersonalContacto informacionOC) {
        this.informacionOC = informacionOC;
    }


    /**
     * Gets the nombreOrgAsist value for this OrganoAsistencia.
     * 
     * @return nombreOrgAsist
     */
    public java.lang.String getNombreOrgAsist() {
        return nombreOrgAsist;
    }


    /**
     * Sets the nombreOrgAsist value for this OrganoAsistencia.
     * 
     * @param nombreOrgAsist
     */
    public void setNombreOrgAsist(java.lang.String nombreOrgAsist) {
        this.nombreOrgAsist = nombreOrgAsist;
    }
}

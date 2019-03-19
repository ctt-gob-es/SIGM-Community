package es.dipucr.contratacion.objeto.sw;

public class OrganoAsistencia{
    private String identificacion = null;
    private Departamento informacionOC = null;
    private String nombreOrgAsist = null;

    public OrganoAsistencia() {
    }

    public OrganoAsistencia(
           String identificacion,
           Departamento informacionOC,
           String nombreOrgAsist) {
           this.identificacion = identificacion;
           this.informacionOC = informacionOC;
           this.nombreOrgAsist = nombreOrgAsist;
    }


    /**
     * Gets the identificacion value for this OrganoAsistencia.
     * 
     * @return identificacion
     */
    public String getIdentificacion() {
        return identificacion;
    }


    /**
     * Sets the identificacion value for this OrganoAsistencia.
     * 
     * @param identificacion
     */
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }


    /**
     * Gets the informacionOC value for this OrganoAsistencia.
     * 
     * @return informacionOC
     */
    public Departamento getInformacionOC() {
        return informacionOC;
    }


    /**
     * Sets the informacionOC value for this OrganoAsistencia.
     * 
     * @param informacionOC
     */
    public void setInformacionOC(Departamento informacionOC) {
        this.informacionOC = informacionOC;
    }


    /**
     * Gets the nombreOrgAsist value for this OrganoAsistencia.
     * 
     * @return nombreOrgAsist
     */
    public String getNombreOrgAsist() {
        return nombreOrgAsist;
    }


    /**
     * Sets the nombreOrgAsist value for this OrganoAsistencia.
     * 
     * @param nombreOrgAsist
     */
    public void setNombreOrgAsist(String nombreOrgAsist) {
        this.nombreOrgAsist = nombreOrgAsist;
    }
}

package es.dipucr.sigem.api.rule.procedures;

public class ParticipantesSubvencionesUtil {
    
    protected String nifParticipante;
    protected String nombreParticipante;
    protected String rol;
    protected String tipoPersona;
    protected String tipoDireccion;
    protected String recurso;
    
    public ParticipantesSubvencionesUtil(String nifParticipante, String nombreParticipante){
        this.nifParticipante = nifParticipante;
        this.nombreParticipante = nombreParticipante;
    }

    public String getNifParticipante() {
        return nifParticipante;
    }

    public void setNifParticipante(String nifParticipante) {
        this.nifParticipante = nifParticipante;
    }

    public String getNombreParticipante() {
        return nombreParticipante;
    }

    public void setNombreParticipante(String nombreParticipante) {
        this.nombreParticipante = nombreParticipante;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(String tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }
}

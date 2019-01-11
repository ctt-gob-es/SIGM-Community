package ieci.tecdoc.sgm.core.services.telematico;

/**
 * Bean con la información de un documento adjunto a la solicitud de registro.
 *
 * @author IECISA
 *
 */
public class Solicitante
{
	private String tipoIdentificacion;
	private String tipoPresentacion;
	private String tipoSolicitante;


	private String documentoIdentidadRepresentante;
	private String nombreApellidosRepresentante;
	private String nombreRepresentante;
	private String apellido1Representante;
	private String apellido2Representante;
	private String cifRepresentante;
	private String razonSocialRepresentante;
	private String documentoIdentidadRepresentanteLegalRepresentante;
	private String nombreApellidosRepresentanteLegalRepresentante;
	private String domicilioRepresentante;
	private String localidadRepresentante;
	private String provinciaRepresentante;
	private String descripcionProvinciaRepresentante;
	private String codigoPostalRepresentante;
	private String telefonoRepresentante;
	private String telefonoFijoRepresentante;
	private String correoElectronicoRepresentante;

	private String documentoIdentidadSolicitante;
	private String nombreApellidosSolicitante;
	private String nombreSolicitante;
	private String apellido1Solicitante;
	private String apellido2Solicitante;
	private String cifSolicitante;
	private String razonSocialSolicitante;
	private String documentoIdentidadRepresentanteLegalSolicitante;
	private String nombreApellidosRepresentanteLegalSolicitante;
	private String domicilioSolicitante;
	private String localidadSolicitante;
	private String provinciaSolicitante;
	private String descripcionProvinciaSolicitante;
	private String codigoPostalSolicitante;
	private String telefonoSolicitante;
	private String telefonoFijoSolicitante;
	private String correoElectronicoSolicitante;

	private String tipoComunicacionNotificacion;
	private String paisNotificacion;
	private String provinciaNotificacion;
	private String descripcionProvinciaNotificacion;
	private String localidadNotificacion;
	private String domicilioNotificacion;
	private String codigoPostalNotificacion;
	private String telefonoNotificacion;
	private String telefonoFijoNotificacion;
	private String faxNotificacion;
	private String correoElectronicoNotificacion;
	private String institucion;
	private String cargo;
	private String faxSolicitante;
	private String faxRepresentante;

	public Solicitante()
	{
		tipoIdentificacion=null;
		tipoPresentacion=null;
		tipoSolicitante=null;

		documentoIdentidadRepresentante=null;
		nombreApellidosRepresentante=null;
		nombreRepresentante=null;
		apellido1Representante=null;
		apellido2Representante=null;
		cifRepresentante=null;
		razonSocialRepresentante=null;
		documentoIdentidadRepresentanteLegalRepresentante=null;
		nombreApellidosRepresentanteLegalRepresentante=null;
		domicilioRepresentante=null;
		localidadRepresentante=null;
		provinciaRepresentante=null;
		descripcionProvinciaRepresentante=null;
		codigoPostalRepresentante=null;
		telefonoRepresentante=null;
		telefonoFijoRepresentante=null;
		correoElectronicoRepresentante=null;

		documentoIdentidadSolicitante=null;
		nombreApellidosSolicitante=null;
		nombreSolicitante=null;
		apellido1Solicitante=null;
		apellido2Solicitante=null;
		cifSolicitante=null;
		razonSocialSolicitante=null;
		documentoIdentidadRepresentanteLegalSolicitante=null;
		nombreApellidosRepresentanteLegalSolicitante=null;
		domicilioSolicitante=null;
		localidadSolicitante=null;
		provinciaSolicitante=null;
		descripcionProvinciaSolicitante=null;
		codigoPostalSolicitante=null;
		telefonoSolicitante=null;
		telefonoFijoSolicitante=null;
		correoElectronicoSolicitante=null;

		tipoComunicacionNotificacion=null;
		paisNotificacion=null;
		provinciaNotificacion=null;
		descripcionProvinciaNotificacion=null;
		localidadNotificacion=null;
		domicilioNotificacion=null;
		codigoPostalNotificacion=null;
		telefonoNotificacion=null;
		telefonoFijoNotificacion=null;
		faxNotificacion=null;
		correoElectronicoNotificacion=null;
		institucion=null;
		cargo=null;
		faxSolicitante=null;
		faxRepresentante=null;
	}

	public String getTipoSolicitante() {
		return tipoSolicitante;
	}
	
	public void setTipoSolicitante(String tipoSolicitante) {
		this.tipoSolicitante = tipoSolicitante;
	}
	
	public String getDocumentoIdentidadSolicitante() {
		return documentoIdentidadSolicitante;
	}
	
	public void setDocumentoIdentidadSolicitante(
			String documentoIdentidadSolicitante) {
		this.documentoIdentidadSolicitante = documentoIdentidadSolicitante;
	}
	
	public String getNombreSolicitante() {
		return nombreSolicitante;
	}
	
	public void setNombreSolicitante(String nombreSolicitante) {
		this.nombreSolicitante = nombreSolicitante;
	}
	
	public String getApellido1Solicitante() {
		return apellido1Solicitante;
	}
	
	public void setApellido1Solicitante(String apellido1Solicitante) {
		this.apellido1Solicitante = apellido1Solicitante;
	}
	
	public String getApellido2Solicitante() {
		return apellido2Solicitante;
	}
	
	public void setApellido2Solicitante(String apellido2Solicitante) {
		this.apellido2Solicitante = apellido2Solicitante;
	}
	
	public String getCifSolicitante() {
		return cifSolicitante;
	}
	
	public void setCifSolicitante(String cifSolicitante) {
		this.cifSolicitante = cifSolicitante;
	}
	
	public String getRazonSocialSolicitante() {
		return razonSocialSolicitante;
	}
	
	public void setRazonSocialSolicitante(String razonSocialSolicitante) {
		this.razonSocialSolicitante = razonSocialSolicitante;
	}
	
	public String getDocumentoIdentidadRepresentanteLegalSolicitante() {
		return documentoIdentidadRepresentanteLegalSolicitante;
	}
	
	public void setDocumentoIdentidadRepresentanteLegalSolicitante(
			String documentoIdentidadRepresentanteLegalSolicitante) {
		this.documentoIdentidadRepresentanteLegalSolicitante = documentoIdentidadRepresentanteLegalSolicitante;
	}
	
	public String getNombreApellidosRepresentanteLegalSolicitante() {
		return nombreApellidosRepresentanteLegalSolicitante;
	}
	
	public void setNombreApellidosRepresentanteLegalSolicitante(
			String nombreApellidosRepresentanteLegalSolicitante) {
		this.nombreApellidosRepresentanteLegalSolicitante = nombreApellidosRepresentanteLegalSolicitante;
	}
	
	public String getDomicilioSolicitante() {
		return domicilioSolicitante;
	}
	
	public void setDomicilioSolicitante(String domicilioSolicitante) {
		this.domicilioSolicitante = domicilioSolicitante;
	}
	
	public String getLocalidadSolicitante() {
		return localidadSolicitante;
	}
	
	public void setLocalidadSolicitante(String localidadSolicitante) {
		this.localidadSolicitante = localidadSolicitante;
	}
	
	public String getProvinciaSolicitante() {
		return provinciaSolicitante;
	}
	
	public void setProvinciaSolicitante(String provinciaSolicitante) {
		this.provinciaSolicitante = provinciaSolicitante;
	}
	
	public String getDescripcionProvinciaSolicitante() {
		return descripcionProvinciaSolicitante;
	}
	
	public void setDescripcionProvinciaSolicitante(
			String descripcionProvinciaSolicitante) {
		this.descripcionProvinciaSolicitante = descripcionProvinciaSolicitante;
	}
	
	public String getCodigoPostalSolicitante() {
		return codigoPostalSolicitante;
	}
	
	public void setCodigoPostalSolicitante(String codigoPostalSolicitante) {
		this.codigoPostalSolicitante = codigoPostalSolicitante;
	}
	
	public String getTelefonoSolicitante() {
		return telefonoSolicitante;
	}
	
	public void setTelefonoSolicitante(String telefonoSolicitante) {
		this.telefonoSolicitante = telefonoSolicitante;
	}
	
	public String getTelefonoFijoSolicitante() {
		return telefonoFijoSolicitante;
	}
	
	public void setTelefonoFijoSolicitante(String telefonoFijoSolicitante) {
		this.telefonoFijoSolicitante = telefonoFijoSolicitante;
	}
	
	public String getCorreoElectronicoSolicitante() {
		return correoElectronicoSolicitante;
	}
	
	public void setCorreoElectronicoSolicitante(String correoElectronicoSolicitante) {
		this.correoElectronicoSolicitante = correoElectronicoSolicitante;
	}
	

    public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getTipoPresentacion() {
		return tipoPresentacion;
	}

	public void setTipoPresentacion(String tipoPresentacion) {
		this.tipoPresentacion = tipoPresentacion;
	}

	public String getDocumentoIdentidadRepresentante() {
		return documentoIdentidadRepresentante;
	}

	public void setDocumentoIdentidadRepresentante(
			String documentoIdentidadRepresentante) {
		this.documentoIdentidadRepresentante = documentoIdentidadRepresentante;
	}

	public String getNombreApellidosRepresentante() {
		return nombreApellidosRepresentante;
	}

	public void setNombreApellidosRepresentante(String nombreApellidosRepresentante) {
		this.nombreApellidosRepresentante = nombreApellidosRepresentante;
	}

	public String getNombreRepresentante() {
		return nombreRepresentante;
	}

	public void setNombreRepresentante(String nombreRepresentante) {
		this.nombreRepresentante = nombreRepresentante;
	}

	public String getApellido1Representante() {
		return apellido1Representante;
	}

	public void setApellido1Representante(String apellido1Representante) {
		this.apellido1Representante = apellido1Representante;
	}

	public String getApellido2Representante() {
		return apellido2Representante;
	}

	public void setApellido2Representante(String apellido2Representante) {
		this.apellido2Representante = apellido2Representante;
	}

	public String getCifRepresentante() {
		return cifRepresentante;
	}

	public void setCifRepresentante(String cifRepresentante) {
		this.cifRepresentante = cifRepresentante;
	}

	public String getRazonSocialRepresentante() {
		return razonSocialRepresentante;
	}

	public void setRazonSocialRepresentante(String razonSocialRepresentante) {
		this.razonSocialRepresentante = razonSocialRepresentante;
	}

	public String getDocumentoIdentidadRepresentanteLegalRepresentante() {
		return documentoIdentidadRepresentanteLegalRepresentante;
	}

	public void setDocumentoIdentidadRepresentanteLegalRepresentante(
			String documentoIdentidadRepresentanteLegalRepresentante) {
		this.documentoIdentidadRepresentanteLegalRepresentante = documentoIdentidadRepresentanteLegalRepresentante;
	}

	public String getNombreApellidosRepresentanteLegalRepresentante() {
		return nombreApellidosRepresentanteLegalRepresentante;
	}

	public void setNombreApellidosRepresentanteLegalRepresentante(
			String nombreApellidosRepresentanteLegalRepresentante) {
		this.nombreApellidosRepresentanteLegalRepresentante = nombreApellidosRepresentanteLegalRepresentante;
	}

	public String getDomicilioRepresentante() {
		return domicilioRepresentante;
	}

	public void setDomicilioRepresentante(String domicilioRepresentante) {
		this.domicilioRepresentante = domicilioRepresentante;
	}

	public String getLocalidadRepresentante() {
		return localidadRepresentante;
	}

	public void setLocalidadRepresentante(String localidadRepresentante) {
		this.localidadRepresentante = localidadRepresentante;
	}

	public String getProvinciaRepresentante() {
		return provinciaRepresentante;
	}

	public void setProvinciaRepresentante(String provinciaRepresentante) {
		this.provinciaRepresentante = provinciaRepresentante;
	}

	public String getDescripcionProvinciaRepresentante() {
		return descripcionProvinciaRepresentante;
	}

	public void setDescripcionProvinciaRepresentante(
			String descripcionProvinciaRepresentante) {
		this.descripcionProvinciaRepresentante = descripcionProvinciaRepresentante;
	}

	public String getCodigoPostalRepresentante() {
		return codigoPostalRepresentante;
	}

	public void setCodigoPostalRepresentante(String codigoPostalRepresentante) {
		this.codigoPostalRepresentante = codigoPostalRepresentante;
	}

	public String getTelefonoRepresentante() {
		return telefonoRepresentante;
	}

	public void setTelefonoRepresentante(String telefonoRepresentante) {
		this.telefonoRepresentante = telefonoRepresentante;
	}
	
	public String getTelefonoFijoRepresentante() {
		return telefonoFijoRepresentante;
	}

	public void setTelefonoFijoRepresentante(String telefonoFijoRepresentante) {
		this.telefonoFijoRepresentante = telefonoFijoRepresentante;
	}

	public String getCorreoElectronicoRepresentante() {
		return correoElectronicoRepresentante;
	}

	public void setCorreoElectronicoRepresentante(
			String correoElectronicoRepresentante) {
		this.correoElectronicoRepresentante = correoElectronicoRepresentante;
	}

	public String getNombreApellidosSolicitante() {
		return nombreApellidosSolicitante;
	}

	public void setNombreApellidosSolicitante(String nombreApellidosSolicitante) {
		this.nombreApellidosSolicitante = nombreApellidosSolicitante;
	}

	public String getTipoComunicacionNotificacion() {
		return tipoComunicacionNotificacion;
	}

	public void setTipoComunicacionNotificacion(String tipoComunicacionNotificacion) {
		this.tipoComunicacionNotificacion = tipoComunicacionNotificacion;
	}

	public String getPaisNotificacion() {
		return paisNotificacion;
	}

	public void setPaisNotificacion(String paisNotificacion) {
		this.paisNotificacion = paisNotificacion;
	}

	public String getProvinciaNotificacion() {
		return provinciaNotificacion;
	}

	public void setProvinciaNotificacion(String provinciaNotificacion) {
		this.provinciaNotificacion = provinciaNotificacion;
	}

	public String getDescripcionProvinciaNotificacion() {
		return descripcionProvinciaNotificacion;
	}

	public void setDescripcionProvinciaNotificacion(
			String descripcionProvinciaNotificacion) {
		this.descripcionProvinciaNotificacion = descripcionProvinciaNotificacion;
	}

	public String getLocalidadNotificacion() {
		return localidadNotificacion;
	}

	public void setLocalidadNotificacion(String localidadNotificacion) {
		this.localidadNotificacion = localidadNotificacion;
	}
	
	public String getDomicilioNotificacion() {
		return domicilioNotificacion;
	}

	public void setDomicilioNotificacion(String domicilioNotificacion) {
		this.domicilioNotificacion = domicilioNotificacion;
	}

	public String getCodigoPostalNotificacion() {
		return codigoPostalNotificacion;
	}

	public void setCodigoPostalNotificacion(String codigoPostalNotificacion) {
		this.codigoPostalNotificacion = codigoPostalNotificacion;
	}

	public String getTelefonoNotificacion() {
		return telefonoNotificacion;
	}

	public void setTelefonoNotificacion(String telefonoNotificacion) {
		this.telefonoNotificacion = telefonoNotificacion;
	}
	
	public String getTelefonoFijoNotificacion() {
		return telefonoFijoNotificacion;
	}

	public void setTelefonoFijoNotificacion(String telefonoFijoNotificacion) {
		this.telefonoFijoNotificacion = telefonoFijoNotificacion;
	}

	public String getFaxNotificacion() {
		return faxNotificacion;
	}

	public void setFaxNotificacion(String faxNotificacion) {
		this.faxNotificacion = faxNotificacion;
	}

	public String getCorreoElectronicoNotificacion() {
		return correoElectronicoNotificacion;
	}

	public void setCorreoElectronicoNotificacion(
			String correoElectronicoNotificacion) {
		this.correoElectronicoNotificacion = correoElectronicoNotificacion;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getFaxSolicitante() {
		return faxSolicitante;
	}

	public void setFaxSolicitante(String faxSolicitante) {
		this.faxSolicitante = faxSolicitante;
	}
	
	public String getFaxRepresentante() {
		return faxRepresentante;
	}

	public void setFaxRepresentante(String faxRepresentante) {
		this.faxRepresentante = faxRepresentante;
	}
}

/**
 * AyudaSocial.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.domain;

public class AyudaSocial  implements java.io.Serializable {
    private java.lang.String anoAyuda;

    private java.lang.String anoSolicitud;

    private java.lang.String beneficiario;

    private java.lang.String concedida;

    private java.lang.String descConcepto;

    private java.lang.String descGrupo;

    private java.lang.String email;

    private int estado;

    private java.util.Calendar fechaBeneficiario;

    private java.util.Calendar fechaDecreto;

    private java.util.Calendar fechaRegSalida;

    private java.util.Calendar fechaRegistro;

    private int idAyuda;

    private int idConcepto;

    private int idConvocatoria;

    private int idGrupo;

    private int idPropuesta;

    private double importeConcedido;

    private double importeSolicitado;

    private java.lang.String motivo;

    private java.lang.String nif;

    private java.lang.String nombre;

    private java.lang.String numRegistro;

    private java.lang.String numexp;

    private java.lang.String observaciones;

    private java.lang.String parentesco;

    private java.lang.String procedenciaWeb;

    private int puntos;

    private java.lang.String telefono;

    private java.lang.String tipoEmpleado;

    public AyudaSocial() {
    }

    public AyudaSocial(
           java.lang.String anoAyuda,
           java.lang.String anoSolicitud,
           java.lang.String beneficiario,
           java.lang.String concedida,
           java.lang.String descConcepto,
           java.lang.String descGrupo,
           java.lang.String email,
           int estado,
           java.util.Calendar fechaBeneficiario,
           java.util.Calendar fechaDecreto,
           java.util.Calendar fechaRegSalida,
           java.util.Calendar fechaRegistro,
           int idAyuda,
           int idConcepto,
           int idConvocatoria,
           int idGrupo,
           int idPropuesta,
           double importeConcedido,
           double importeSolicitado,
           java.lang.String motivo,
           java.lang.String nif,
           java.lang.String nombre,
           java.lang.String numRegistro,
           java.lang.String numexp,
           java.lang.String observaciones,
           java.lang.String parentesco,
           java.lang.String procedenciaWeb,
           int puntos,
           java.lang.String telefono,
           java.lang.String tipoEmpleado) {
           this.anoAyuda = anoAyuda;
           this.anoSolicitud = anoSolicitud;
           this.beneficiario = beneficiario;
           this.concedida = concedida;
           this.descConcepto = descConcepto;
           this.descGrupo = descGrupo;
           this.email = email;
           this.estado = estado;
           this.fechaBeneficiario = fechaBeneficiario;
           this.fechaDecreto = fechaDecreto;
           this.fechaRegSalida = fechaRegSalida;
           this.fechaRegistro = fechaRegistro;
           this.idAyuda = idAyuda;
           this.idConcepto = idConcepto;
           this.idConvocatoria = idConvocatoria;
           this.idGrupo = idGrupo;
           this.idPropuesta = idPropuesta;
           this.importeConcedido = importeConcedido;
           this.importeSolicitado = importeSolicitado;
           this.motivo = motivo;
           this.nif = nif;
           this.nombre = nombre;
           this.numRegistro = numRegistro;
           this.numexp = numexp;
           this.observaciones = observaciones;
           this.parentesco = parentesco;
           this.procedenciaWeb = procedenciaWeb;
           this.puntos = puntos;
           this.telefono = telefono;
           this.tipoEmpleado = tipoEmpleado;
    }


    /**
     * Gets the anoAyuda value for this AyudaSocial.
     * 
     * @return anoAyuda
     */
    public java.lang.String getAnoAyuda() {
        return anoAyuda;
    }


    /**
     * Sets the anoAyuda value for this AyudaSocial.
     * 
     * @param anoAyuda
     */
    public void setAnoAyuda(java.lang.String anoAyuda) {
        this.anoAyuda = anoAyuda;
    }


    /**
     * Gets the anoSolicitud value for this AyudaSocial.
     * 
     * @return anoSolicitud
     */
    public java.lang.String getAnoSolicitud() {
        return anoSolicitud;
    }


    /**
     * Sets the anoSolicitud value for this AyudaSocial.
     * 
     * @param anoSolicitud
     */
    public void setAnoSolicitud(java.lang.String anoSolicitud) {
        this.anoSolicitud = anoSolicitud;
    }


    /**
     * Gets the beneficiario value for this AyudaSocial.
     * 
     * @return beneficiario
     */
    public java.lang.String getBeneficiario() {
        return beneficiario;
    }


    /**
     * Sets the beneficiario value for this AyudaSocial.
     * 
     * @param beneficiario
     */
    public void setBeneficiario(java.lang.String beneficiario) {
        this.beneficiario = beneficiario;
    }


    /**
     * Gets the concedida value for this AyudaSocial.
     * 
     * @return concedida
     */
    public java.lang.String getConcedida() {
        return concedida;
    }


    /**
     * Sets the concedida value for this AyudaSocial.
     * 
     * @param concedida
     */
    public void setConcedida(java.lang.String concedida) {
        this.concedida = concedida;
    }


    /**
     * Gets the descConcepto value for this AyudaSocial.
     * 
     * @return descConcepto
     */
    public java.lang.String getDescConcepto() {
        return descConcepto;
    }


    /**
     * Sets the descConcepto value for this AyudaSocial.
     * 
     * @param descConcepto
     */
    public void setDescConcepto(java.lang.String descConcepto) {
        this.descConcepto = descConcepto;
    }


    /**
     * Gets the descGrupo value for this AyudaSocial.
     * 
     * @return descGrupo
     */
    public java.lang.String getDescGrupo() {
        return descGrupo;
    }


    /**
     * Sets the descGrupo value for this AyudaSocial.
     * 
     * @param descGrupo
     */
    public void setDescGrupo(java.lang.String descGrupo) {
        this.descGrupo = descGrupo;
    }


    /**
     * Gets the email value for this AyudaSocial.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this AyudaSocial.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the estado value for this AyudaSocial.
     * 
     * @return estado
     */
    public int getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this AyudaSocial.
     * 
     * @param estado
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }


    /**
     * Gets the fechaBeneficiario value for this AyudaSocial.
     * 
     * @return fechaBeneficiario
     */
    public java.util.Calendar getFechaBeneficiario() {
        return fechaBeneficiario;
    }


    /**
     * Sets the fechaBeneficiario value for this AyudaSocial.
     * 
     * @param fechaBeneficiario
     */
    public void setFechaBeneficiario(java.util.Calendar fechaBeneficiario) {
        this.fechaBeneficiario = fechaBeneficiario;
    }


    /**
     * Gets the fechaDecreto value for this AyudaSocial.
     * 
     * @return fechaDecreto
     */
    public java.util.Calendar getFechaDecreto() {
        return fechaDecreto;
    }


    /**
     * Sets the fechaDecreto value for this AyudaSocial.
     * 
     * @param fechaDecreto
     */
    public void setFechaDecreto(java.util.Calendar fechaDecreto) {
        this.fechaDecreto = fechaDecreto;
    }


    /**
     * Gets the fechaRegSalida value for this AyudaSocial.
     * 
     * @return fechaRegSalida
     */
    public java.util.Calendar getFechaRegSalida() {
        return fechaRegSalida;
    }


    /**
     * Sets the fechaRegSalida value for this AyudaSocial.
     * 
     * @param fechaRegSalida
     */
    public void setFechaRegSalida(java.util.Calendar fechaRegSalida) {
        this.fechaRegSalida = fechaRegSalida;
    }


    /**
     * Gets the fechaRegistro value for this AyudaSocial.
     * 
     * @return fechaRegistro
     */
    public java.util.Calendar getFechaRegistro() {
        return fechaRegistro;
    }


    /**
     * Sets the fechaRegistro value for this AyudaSocial.
     * 
     * @param fechaRegistro
     */
    public void setFechaRegistro(java.util.Calendar fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    /**
     * Gets the idAyuda value for this AyudaSocial.
     * 
     * @return idAyuda
     */
    public int getIdAyuda() {
        return idAyuda;
    }


    /**
     * Sets the idAyuda value for this AyudaSocial.
     * 
     * @param idAyuda
     */
    public void setIdAyuda(int idAyuda) {
        this.idAyuda = idAyuda;
    }


    /**
     * Gets the idConcepto value for this AyudaSocial.
     * 
     * @return idConcepto
     */
    public int getIdConcepto() {
        return idConcepto;
    }


    /**
     * Sets the idConcepto value for this AyudaSocial.
     * 
     * @param idConcepto
     */
    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }


    /**
     * Gets the idConvocatoria value for this AyudaSocial.
     * 
     * @return idConvocatoria
     */
    public int getIdConvocatoria() {
        return idConvocatoria;
    }


    /**
     * Sets the idConvocatoria value for this AyudaSocial.
     * 
     * @param idConvocatoria
     */
    public void setIdConvocatoria(int idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }


    /**
     * Gets the idGrupo value for this AyudaSocial.
     * 
     * @return idGrupo
     */
    public int getIdGrupo() {
        return idGrupo;
    }


    /**
     * Sets the idGrupo value for this AyudaSocial.
     * 
     * @param idGrupo
     */
    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }


    /**
     * Gets the idPropuesta value for this AyudaSocial.
     * 
     * @return idPropuesta
     */
    public int getIdPropuesta() {
        return idPropuesta;
    }


    /**
     * Sets the idPropuesta value for this AyudaSocial.
     * 
     * @param idPropuesta
     */
    public void setIdPropuesta(int idPropuesta) {
        this.idPropuesta = idPropuesta;
    }


    /**
     * Gets the importeConcedido value for this AyudaSocial.
     * 
     * @return importeConcedido
     */
    public double getImporteConcedido() {
        return importeConcedido;
    }


    /**
     * Sets the importeConcedido value for this AyudaSocial.
     * 
     * @param importeConcedido
     */
    public void setImporteConcedido(double importeConcedido) {
        this.importeConcedido = importeConcedido;
    }


    /**
     * Gets the importeSolicitado value for this AyudaSocial.
     * 
     * @return importeSolicitado
     */
    public double getImporteSolicitado() {
        return importeSolicitado;
    }


    /**
     * Sets the importeSolicitado value for this AyudaSocial.
     * 
     * @param importeSolicitado
     */
    public void setImporteSolicitado(double importeSolicitado) {
        this.importeSolicitado = importeSolicitado;
    }


    /**
     * Gets the motivo value for this AyudaSocial.
     * 
     * @return motivo
     */
    public java.lang.String getMotivo() {
        return motivo;
    }


    /**
     * Sets the motivo value for this AyudaSocial.
     * 
     * @param motivo
     */
    public void setMotivo(java.lang.String motivo) {
        this.motivo = motivo;
    }


    /**
     * Gets the nif value for this AyudaSocial.
     * 
     * @return nif
     */
    public java.lang.String getNif() {
        return nif;
    }


    /**
     * Sets the nif value for this AyudaSocial.
     * 
     * @param nif
     */
    public void setNif(java.lang.String nif) {
        this.nif = nif;
    }


    /**
     * Gets the nombre value for this AyudaSocial.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this AyudaSocial.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the numRegistro value for this AyudaSocial.
     * 
     * @return numRegistro
     */
    public java.lang.String getNumRegistro() {
        return numRegistro;
    }


    /**
     * Sets the numRegistro value for this AyudaSocial.
     * 
     * @param numRegistro
     */
    public void setNumRegistro(java.lang.String numRegistro) {
        this.numRegistro = numRegistro;
    }


    /**
     * Gets the numexp value for this AyudaSocial.
     * 
     * @return numexp
     */
    public java.lang.String getNumexp() {
        return numexp;
    }


    /**
     * Sets the numexp value for this AyudaSocial.
     * 
     * @param numexp
     */
    public void setNumexp(java.lang.String numexp) {
        this.numexp = numexp;
    }


    /**
     * Gets the observaciones value for this AyudaSocial.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this AyudaSocial.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }


    /**
     * Gets the parentesco value for this AyudaSocial.
     * 
     * @return parentesco
     */
    public java.lang.String getParentesco() {
        return parentesco;
    }


    /**
     * Sets the parentesco value for this AyudaSocial.
     * 
     * @param parentesco
     */
    public void setParentesco(java.lang.String parentesco) {
        this.parentesco = parentesco;
    }


    /**
     * Gets the procedenciaWeb value for this AyudaSocial.
     * 
     * @return procedenciaWeb
     */
    public java.lang.String getProcedenciaWeb() {
        return procedenciaWeb;
    }


    /**
     * Sets the procedenciaWeb value for this AyudaSocial.
     * 
     * @param procedenciaWeb
     */
    public void setProcedenciaWeb(java.lang.String procedenciaWeb) {
        this.procedenciaWeb = procedenciaWeb;
    }


    /**
     * Gets the puntos value for this AyudaSocial.
     * 
     * @return puntos
     */
    public int getPuntos() {
        return puntos;
    }


    /**
     * Sets the puntos value for this AyudaSocial.
     * 
     * @param puntos
     */
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


    /**
     * Gets the telefono value for this AyudaSocial.
     * 
     * @return telefono
     */
    public java.lang.String getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this AyudaSocial.
     * 
     * @param telefono
     */
    public void setTelefono(java.lang.String telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the tipoEmpleado value for this AyudaSocial.
     * 
     * @return tipoEmpleado
     */
    public java.lang.String getTipoEmpleado() {
        return tipoEmpleado;
    }


    /**
     * Sets the tipoEmpleado value for this AyudaSocial.
     * 
     * @param tipoEmpleado
     */
    public void setTipoEmpleado(java.lang.String tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AyudaSocial)) return false;
        AyudaSocial other = (AyudaSocial) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anoAyuda==null && other.getAnoAyuda()==null) || 
             (this.anoAyuda!=null &&
              this.anoAyuda.equals(other.getAnoAyuda()))) &&
            ((this.anoSolicitud==null && other.getAnoSolicitud()==null) || 
             (this.anoSolicitud!=null &&
              this.anoSolicitud.equals(other.getAnoSolicitud()))) &&
            ((this.beneficiario==null && other.getBeneficiario()==null) || 
             (this.beneficiario!=null &&
              this.beneficiario.equals(other.getBeneficiario()))) &&
            ((this.concedida==null && other.getConcedida()==null) || 
             (this.concedida!=null &&
              this.concedida.equals(other.getConcedida()))) &&
            ((this.descConcepto==null && other.getDescConcepto()==null) || 
             (this.descConcepto!=null &&
              this.descConcepto.equals(other.getDescConcepto()))) &&
            ((this.descGrupo==null && other.getDescGrupo()==null) || 
             (this.descGrupo!=null &&
              this.descGrupo.equals(other.getDescGrupo()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            this.estado == other.getEstado() &&
            ((this.fechaBeneficiario==null && other.getFechaBeneficiario()==null) || 
             (this.fechaBeneficiario!=null &&
              this.fechaBeneficiario.equals(other.getFechaBeneficiario()))) &&
            ((this.fechaDecreto==null && other.getFechaDecreto()==null) || 
             (this.fechaDecreto!=null &&
              this.fechaDecreto.equals(other.getFechaDecreto()))) &&
            ((this.fechaRegSalida==null && other.getFechaRegSalida()==null) || 
             (this.fechaRegSalida!=null &&
              this.fechaRegSalida.equals(other.getFechaRegSalida()))) &&
            ((this.fechaRegistro==null && other.getFechaRegistro()==null) || 
             (this.fechaRegistro!=null &&
              this.fechaRegistro.equals(other.getFechaRegistro()))) &&
            this.idAyuda == other.getIdAyuda() &&
            this.idConcepto == other.getIdConcepto() &&
            this.idConvocatoria == other.getIdConvocatoria() &&
            this.idGrupo == other.getIdGrupo() &&
            this.idPropuesta == other.getIdPropuesta() &&
            this.importeConcedido == other.getImporteConcedido() &&
            this.importeSolicitado == other.getImporteSolicitado() &&
            ((this.motivo==null && other.getMotivo()==null) || 
             (this.motivo!=null &&
              this.motivo.equals(other.getMotivo()))) &&
            ((this.nif==null && other.getNif()==null) || 
             (this.nif!=null &&
              this.nif.equals(other.getNif()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.numRegistro==null && other.getNumRegistro()==null) || 
             (this.numRegistro!=null &&
              this.numRegistro.equals(other.getNumRegistro()))) &&
            ((this.numexp==null && other.getNumexp()==null) || 
             (this.numexp!=null &&
              this.numexp.equals(other.getNumexp()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            ((this.parentesco==null && other.getParentesco()==null) || 
             (this.parentesco!=null &&
              this.parentesco.equals(other.getParentesco()))) &&
            ((this.procedenciaWeb==null && other.getProcedenciaWeb()==null) || 
             (this.procedenciaWeb!=null &&
              this.procedenciaWeb.equals(other.getProcedenciaWeb()))) &&
            this.puntos == other.getPuntos() &&
            ((this.telefono==null && other.getTelefono()==null) || 
             (this.telefono!=null &&
              this.telefono.equals(other.getTelefono()))) &&
            ((this.tipoEmpleado==null && other.getTipoEmpleado()==null) || 
             (this.tipoEmpleado!=null &&
              this.tipoEmpleado.equals(other.getTipoEmpleado())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAnoAyuda() != null) {
            _hashCode += getAnoAyuda().hashCode();
        }
        if (getAnoSolicitud() != null) {
            _hashCode += getAnoSolicitud().hashCode();
        }
        if (getBeneficiario() != null) {
            _hashCode += getBeneficiario().hashCode();
        }
        if (getConcedida() != null) {
            _hashCode += getConcedida().hashCode();
        }
        if (getDescConcepto() != null) {
            _hashCode += getDescConcepto().hashCode();
        }
        if (getDescGrupo() != null) {
            _hashCode += getDescGrupo().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        _hashCode += getEstado();
        if (getFechaBeneficiario() != null) {
            _hashCode += getFechaBeneficiario().hashCode();
        }
        if (getFechaDecreto() != null) {
            _hashCode += getFechaDecreto().hashCode();
        }
        if (getFechaRegSalida() != null) {
            _hashCode += getFechaRegSalida().hashCode();
        }
        if (getFechaRegistro() != null) {
            _hashCode += getFechaRegistro().hashCode();
        }
        _hashCode += getIdAyuda();
        _hashCode += getIdConcepto();
        _hashCode += getIdConvocatoria();
        _hashCode += getIdGrupo();
        _hashCode += getIdPropuesta();
        _hashCode += new Double(getImporteConcedido()).hashCode();
        _hashCode += new Double(getImporteSolicitado()).hashCode();
        if (getMotivo() != null) {
            _hashCode += getMotivo().hashCode();
        }
        if (getNif() != null) {
            _hashCode += getNif().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getNumRegistro() != null) {
            _hashCode += getNumRegistro().hashCode();
        }
        if (getNumexp() != null) {
            _hashCode += getNumexp().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        if (getParentesco() != null) {
            _hashCode += getParentesco().hashCode();
        }
        if (getProcedenciaWeb() != null) {
            _hashCode += getProcedenciaWeb().hashCode();
        }
        _hashCode += getPuntos();
        if (getTelefono() != null) {
            _hashCode += getTelefono().hashCode();
        }
        if (getTipoEmpleado() != null) {
            _hashCode += getTipoEmpleado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AyudaSocial.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "AyudaSocial"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anoAyuda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "anoAyuda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anoSolicitud");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "anoSolicitud"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beneficiario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "beneficiario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("concedida");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "concedida"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descConcepto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "descConcepto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descGrupo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "descGrupo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaBeneficiario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "fechaBeneficiario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaDecreto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "fechaDecreto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaRegSalida");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "fechaRegSalida"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "fechaRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAyuda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "idAyuda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConcepto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "idConcepto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "idConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idGrupo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "idGrupo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPropuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "idPropuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeConcedido");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "importeConcedido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeSolicitado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "importeSolicitado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "motivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nif");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "nif"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "numRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numexp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "numexp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "observaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parentesco");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "parentesco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedenciaWeb");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "procedenciaWeb"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("puntos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "puntos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "telefono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoEmpleado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://domain.webempleado.dipucr.es", "tipoEmpleado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

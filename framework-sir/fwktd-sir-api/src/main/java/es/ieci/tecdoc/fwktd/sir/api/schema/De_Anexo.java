/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema;

/**
 * Class De_Anexo.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class De_Anexo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _nombre_Fichero_Anexado.
     */
    private java.lang.String _nombre_Fichero_Anexado;

    /**
     * Field _identificador_Fichero.
     */
    private java.lang.String _identificador_Fichero;

    /**
     * Field _validez_Documento.
     */
    private java.lang.String _validez_Documento;

    /**
     * Field _tipo_Documento.
     */
    private java.lang.String _tipo_Documento;

    /**
     * Field _certificado.
     */
    private byte[] _certificado;

    /**
     * Field _firma_Documento.
     */
    private byte[] _firma_Documento;

    /**
     * Field _timeStamp.
     */
    private byte[] _timeStamp;

    /**
     * Field _validacion_OCSP_Certificado.
     */
    private byte[] _validacion_OCSP_Certificado;

    /**
     * Field _hash.
     */
    private byte[] _hash;

    /**
     * Field _tipo_MIME.
     */
    private java.lang.String _tipo_MIME;

    /**
     * Field _anexo.
     */
    private byte[] _anexo;

    /**
     * Field _identificador_Documento_Firmado.
     */
    private java.lang.String _identificador_Documento_Firmado;

    /**
     * Field _observaciones.
     */
    private java.lang.String _observaciones;


      //----------------/
     //- Constructors -/
    //----------------/

    public De_Anexo() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'anexo'.
     * 
     * @return the value of field 'Anexo'.
     */
    public byte[] getAnexo(
    ) {
        return this._anexo;
    }

    /**
     * Returns the value of field 'certificado'.
     * 
     * @return the value of field 'Certificado'.
     */
    public byte[] getCertificado(
    ) {
        return this._certificado;
    }

    /**
     * Returns the value of field 'firma_Documento'.
     * 
     * @return the value of field 'Firma_Documento'.
     */
    public byte[] getFirma_Documento(
    ) {
        return this._firma_Documento;
    }

    /**
     * Returns the value of field 'hash'.
     * 
     * @return the value of field 'Hash'.
     */
    public byte[] getHash(
    ) {
        return this._hash;
    }

    /**
     * Returns the value of field
     * 'identificador_Documento_Firmado'.
     * 
     * @return the value of field 'Identificador_Documento_Firmado'.
     */
    public java.lang.String getIdentificador_Documento_Firmado(
    ) {
        return this._identificador_Documento_Firmado;
    }

    /**
     * Returns the value of field 'identificador_Fichero'.
     * 
     * @return the value of field 'Identificador_Fichero'.
     */
    public java.lang.String getIdentificador_Fichero(
    ) {
        return this._identificador_Fichero;
    }

    /**
     * Returns the value of field 'nombre_Fichero_Anexado'.
     * 
     * @return the value of field 'Nombre_Fichero_Anexado'.
     */
    public java.lang.String getNombre_Fichero_Anexado(
    ) {
        return this._nombre_Fichero_Anexado;
    }

    /**
     * Returns the value of field 'observaciones'.
     * 
     * @return the value of field 'Observaciones'.
     */
    public java.lang.String getObservaciones(
    ) {
        return this._observaciones;
    }

    /**
     * Returns the value of field 'timeStamp'.
     * 
     * @return the value of field 'TimeStamp'.
     */
    public byte[] getTimeStamp(
    ) {
        return this._timeStamp;
    }

    /**
     * Returns the value of field 'tipo_Documento'.
     * 
     * @return the value of field 'Tipo_Documento'.
     */
    public java.lang.String getTipo_Documento(
    ) {
        return this._tipo_Documento;
    }

    /**
     * Returns the value of field 'tipo_MIME'.
     * 
     * @return the value of field 'Tipo_MIME'.
     */
    public java.lang.String getTipo_MIME(
    ) {
        return this._tipo_MIME;
    }

    /**
     * Returns the value of field 'validacion_OCSP_Certificado'.
     * 
     * @return the value of field 'Validacion_OCSP_Certificado'.
     */
    public byte[] getValidacion_OCSP_Certificado(
    ) {
        return this._validacion_OCSP_Certificado;
    }

    /**
     * Returns the value of field 'validez_Documento'.
     * 
     * @return the value of field 'Validez_Documento'.
     */
    public java.lang.String getValidez_Documento(
    ) {
        return this._validez_Documento;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'anexo'.
     * 
     * @param anexo the value of field 'anexo'.
     */
    public void setAnexo(
            final byte[] anexo) {
        this._anexo = anexo;
    }

    /**
     * Sets the value of field 'certificado'.
     * 
     * @param certificado the value of field 'certificado'.
     */
    public void setCertificado(
            final byte[] certificado) {
        this._certificado = certificado;
    }

    /**
     * Sets the value of field 'firma_Documento'.
     * 
     * @param firma_Documento the value of field 'firma_Documento'.
     */
    public void setFirma_Documento(
            final byte[] firma_Documento) {
        this._firma_Documento = firma_Documento;
    }

    /**
     * Sets the value of field 'hash'.
     * 
     * @param hash the value of field 'hash'.
     */
    public void setHash(
            final byte[] hash) {
        this._hash = hash;
    }

    /**
     * Sets the value of field 'identificador_Documento_Firmado'.
     * 
     * @param identificador_Documento_Firmado the value of field
     * 'identificador_Documento_Firmado'.
     */
    public void setIdentificador_Documento_Firmado(
            final java.lang.String identificador_Documento_Firmado) {
        this._identificador_Documento_Firmado = identificador_Documento_Firmado;
    }

    /**
     * Sets the value of field 'identificador_Fichero'.
     * 
     * @param identificador_Fichero the value of field
     * 'identificador_Fichero'.
     */
    public void setIdentificador_Fichero(
            final java.lang.String identificador_Fichero) {
        this._identificador_Fichero = identificador_Fichero;
    }

    /**
     * Sets the value of field 'nombre_Fichero_Anexado'.
     * 
     * @param nombre_Fichero_Anexado the value of field
     * 'nombre_Fichero_Anexado'.
     */
    public void setNombre_Fichero_Anexado(
            final java.lang.String nombre_Fichero_Anexado) {
        this._nombre_Fichero_Anexado = nombre_Fichero_Anexado;
    }

    /**
     * Sets the value of field 'observaciones'.
     * 
     * @param observaciones the value of field 'observaciones'.
     */
    public void setObservaciones(
            final java.lang.String observaciones) {
        this._observaciones = observaciones;
    }

    /**
     * Sets the value of field 'timeStamp'.
     * 
     * @param timeStamp the value of field 'timeStamp'.
     */
    public void setTimeStamp(
            final byte[] timeStamp) {
        this._timeStamp = timeStamp;
    }

    /**
     * Sets the value of field 'tipo_Documento'.
     * 
     * @param tipo_Documento the value of field 'tipo_Documento'.
     */
    public void setTipo_Documento(
            final java.lang.String tipo_Documento) {
        this._tipo_Documento = tipo_Documento;
    }

    /**
     * Sets the value of field 'tipo_MIME'.
     * 
     * @param tipo_MIME the value of field 'tipo_MIME'.
     */
    public void setTipo_MIME(
            final java.lang.String tipo_MIME) {
        this._tipo_MIME = tipo_MIME;
    }

    /**
     * Sets the value of field 'validacion_OCSP_Certificado'.
     * 
     * @param validacion_OCSP_Certificado the value of field
     * 'validacion_OCSP_Certificado'.
     */
    public void setValidacion_OCSP_Certificado(
            final byte[] validacion_OCSP_Certificado) {
        this._validacion_OCSP_Certificado = validacion_OCSP_Certificado;
    }

    /**
     * Sets the value of field 'validez_Documento'.
     * 
     * @param validez_Documento the value of field
     * 'validez_Documento'.
     */
    public void setValidez_Documento(
            final java.lang.String validez_Documento) {
        this._validez_Documento = validez_Documento;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo
     */
    public static es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo) org.exolab.castor.xml.Unmarshaller.unmarshal(es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}

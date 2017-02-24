/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema;

/**
 * Class De_Origen_o_Remitente.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class De_Origen_o_Remitente implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _codigo_Entidad_Registral_Origen.
     */
    private java.lang.String _codigo_Entidad_Registral_Origen;

    /**
     * Field _decodificacion_Entidad_Registral_Origen.
     */
    private java.lang.String _decodificacion_Entidad_Registral_Origen;

    /**
     * Field _numero_Registro_Entrada.
     */
    private java.lang.String _numero_Registro_Entrada;

    /**
     * Field _fecha_Hora_Entrada.
     */
    private java.lang.String _fecha_Hora_Entrada;

    /**
     * Field _timestamp_Entrada.
     */
    private byte[] _timestamp_Entrada;

    /**
     * Field _codigo_Unidad_Tramitacion_Origen.
     */
    private java.lang.String _codigo_Unidad_Tramitacion_Origen;

    /**
     * Field _decodificacion_Unidad_Tramitacion_Origen.
     */
    private java.lang.String _decodificacion_Unidad_Tramitacion_Origen;


      //----------------/
     //- Constructors -/
    //----------------/

    public De_Origen_o_Remitente() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field
     * 'codigo_Entidad_Registral_Origen'.
     * 
     * @return the value of field 'Codigo_Entidad_Registral_Origen'.
     */
    public java.lang.String getCodigo_Entidad_Registral_Origen(
    ) {
        return this._codigo_Entidad_Registral_Origen;
    }

    /**
     * Returns the value of field
     * 'codigo_Unidad_Tramitacion_Origen'.
     * 
     * @return the value of field 'Codigo_Unidad_Tramitacion_Origen'
     */
    public java.lang.String getCodigo_Unidad_Tramitacion_Origen(
    ) {
        return this._codigo_Unidad_Tramitacion_Origen;
    }

    /**
     * Returns the value of field
     * 'decodificacion_Entidad_Registral_Origen'.
     * 
     * @return the value of field
     * 'Decodificacion_Entidad_Registral_Origen'.
     */
    public java.lang.String getDecodificacion_Entidad_Registral_Origen(
    ) {
        return this._decodificacion_Entidad_Registral_Origen;
    }

    /**
     * Returns the value of field
     * 'decodificacion_Unidad_Tramitacion_Origen'.
     * 
     * @return the value of field
     * 'Decodificacion_Unidad_Tramitacion_Origen'.
     */
    public java.lang.String getDecodificacion_Unidad_Tramitacion_Origen(
    ) {
        return this._decodificacion_Unidad_Tramitacion_Origen;
    }

    /**
     * Returns the value of field 'fecha_Hora_Entrada'.
     * 
     * @return the value of field 'Fecha_Hora_Entrada'.
     */
    public java.lang.String getFecha_Hora_Entrada(
    ) {
        return this._fecha_Hora_Entrada;
    }

    /**
     * Returns the value of field 'numero_Registro_Entrada'.
     * 
     * @return the value of field 'Numero_Registro_Entrada'.
     */
    public java.lang.String getNumero_Registro_Entrada(
    ) {
        return this._numero_Registro_Entrada;
    }

    /**
     * Returns the value of field 'timestamp_Entrada'.
     * 
     * @return the value of field 'Timestamp_Entrada'.
     */
    public byte[] getTimestamp_Entrada(
    ) {
        return this._timestamp_Entrada;
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
     * Sets the value of field 'codigo_Entidad_Registral_Origen'.
     * 
     * @param codigo_Entidad_Registral_Origen the value of field
     * 'codigo_Entidad_Registral_Origen'.
     */
    public void setCodigo_Entidad_Registral_Origen(
            final java.lang.String codigo_Entidad_Registral_Origen) {
        this._codigo_Entidad_Registral_Origen = codigo_Entidad_Registral_Origen;
    }

    /**
     * Sets the value of field 'codigo_Unidad_Tramitacion_Origen'.
     * 
     * @param codigo_Unidad_Tramitacion_Origen the value of field
     * 'codigo_Unidad_Tramitacion_Origen'.
     */
    public void setCodigo_Unidad_Tramitacion_Origen(
            final java.lang.String codigo_Unidad_Tramitacion_Origen) {
        this._codigo_Unidad_Tramitacion_Origen = codigo_Unidad_Tramitacion_Origen;
    }

    /**
     * Sets the value of field
     * 'decodificacion_Entidad_Registral_Origen'.
     * 
     * @param decodificacion_Entidad_Registral_Origen the value of
     * field 'decodificacion_Entidad_Registral_Origen'.
     */
    public void setDecodificacion_Entidad_Registral_Origen(
            final java.lang.String decodificacion_Entidad_Registral_Origen) {
        this._decodificacion_Entidad_Registral_Origen = decodificacion_Entidad_Registral_Origen;
    }

    /**
     * Sets the value of field
     * 'decodificacion_Unidad_Tramitacion_Origen'.
     * 
     * @param decodificacion_Unidad_Tramitacion_Origen the value of
     * field 'decodificacion_Unidad_Tramitacion_Origen'.
     */
    public void setDecodificacion_Unidad_Tramitacion_Origen(
            final java.lang.String decodificacion_Unidad_Tramitacion_Origen) {
        this._decodificacion_Unidad_Tramitacion_Origen = decodificacion_Unidad_Tramitacion_Origen;
    }

    /**
     * Sets the value of field 'fecha_Hora_Entrada'.
     * 
     * @param fecha_Hora_Entrada the value of field
     * 'fecha_Hora_Entrada'.
     */
    public void setFecha_Hora_Entrada(
            final java.lang.String fecha_Hora_Entrada) {
        this._fecha_Hora_Entrada = fecha_Hora_Entrada;
    }

    /**
     * Sets the value of field 'numero_Registro_Entrada'.
     * 
     * @param numero_Registro_Entrada the value of field
     * 'numero_Registro_Entrada'.
     */
    public void setNumero_Registro_Entrada(
            final java.lang.String numero_Registro_Entrada) {
        this._numero_Registro_Entrada = numero_Registro_Entrada;
    }

    /**
     * Sets the value of field 'timestamp_Entrada'.
     * 
     * @param timestamp_Entrada the value of field
     * 'timestamp_Entrada'.
     */
    public void setTimestamp_Entrada(
            final byte[] timestamp_Entrada) {
        this._timestamp_Entrada = timestamp_Entrada;
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
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente
     */
    public static es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente) org.exolab.castor.xml.Unmarshaller.unmarshal(es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente.class, reader);
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

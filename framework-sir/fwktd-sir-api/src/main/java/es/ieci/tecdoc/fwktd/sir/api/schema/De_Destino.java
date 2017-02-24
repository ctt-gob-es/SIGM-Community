/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema;

/**
 * Class De_Destino.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class De_Destino implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _codigo_Entidad_Registral_Destino.
     */
    private java.lang.String _codigo_Entidad_Registral_Destino;

    /**
     * Field _decodificacion_Entidad_Registral_Destino.
     */
    private java.lang.String _decodificacion_Entidad_Registral_Destino;

    /**
     * Field _codigo_Unidad_Tramitacion_Destino.
     */
    private java.lang.String _codigo_Unidad_Tramitacion_Destino;

    /**
     * Field _decodificacion_Unidad_Tramitacion_Destino.
     */
    private java.lang.String _decodificacion_Unidad_Tramitacion_Destino;


      //----------------/
     //- Constructors -/
    //----------------/

    public De_Destino() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field
     * 'codigo_Entidad_Registral_Destino'.
     * 
     * @return the value of field 'Codigo_Entidad_Registral_Destino'
     */
    public java.lang.String getCodigo_Entidad_Registral_Destino(
    ) {
        return this._codigo_Entidad_Registral_Destino;
    }

    /**
     * Returns the value of field
     * 'codigo_Unidad_Tramitacion_Destino'.
     * 
     * @return the value of field
     * 'Codigo_Unidad_Tramitacion_Destino'.
     */
    public java.lang.String getCodigo_Unidad_Tramitacion_Destino(
    ) {
        return this._codigo_Unidad_Tramitacion_Destino;
    }

    /**
     * Returns the value of field
     * 'decodificacion_Entidad_Registral_Destino'.
     * 
     * @return the value of field
     * 'Decodificacion_Entidad_Registral_Destino'.
     */
    public java.lang.String getDecodificacion_Entidad_Registral_Destino(
    ) {
        return this._decodificacion_Entidad_Registral_Destino;
    }

    /**
     * Returns the value of field
     * 'decodificacion_Unidad_Tramitacion_Destino'.
     * 
     * @return the value of field
     * 'Decodificacion_Unidad_Tramitacion_Destino'.
     */
    public java.lang.String getDecodificacion_Unidad_Tramitacion_Destino(
    ) {
        return this._decodificacion_Unidad_Tramitacion_Destino;
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
     * Sets the value of field 'codigo_Entidad_Registral_Destino'.
     * 
     * @param codigo_Entidad_Registral_Destino the value of field
     * 'codigo_Entidad_Registral_Destino'.
     */
    public void setCodigo_Entidad_Registral_Destino(
            final java.lang.String codigo_Entidad_Registral_Destino) {
        this._codigo_Entidad_Registral_Destino = codigo_Entidad_Registral_Destino;
    }

    /**
     * Sets the value of field 'codigo_Unidad_Tramitacion_Destino'.
     * 
     * @param codigo_Unidad_Tramitacion_Destino the value of field
     * 'codigo_Unidad_Tramitacion_Destino'.
     */
    public void setCodigo_Unidad_Tramitacion_Destino(
            final java.lang.String codigo_Unidad_Tramitacion_Destino) {
        this._codigo_Unidad_Tramitacion_Destino = codigo_Unidad_Tramitacion_Destino;
    }

    /**
     * Sets the value of field
     * 'decodificacion_Entidad_Registral_Destino'.
     * 
     * @param decodificacion_Entidad_Registral_Destino the value of
     * field 'decodificacion_Entidad_Registral_Destino'.
     */
    public void setDecodificacion_Entidad_Registral_Destino(
            final java.lang.String decodificacion_Entidad_Registral_Destino) {
        this._decodificacion_Entidad_Registral_Destino = decodificacion_Entidad_Registral_Destino;
    }

    /**
     * Sets the value of field
     * 'decodificacion_Unidad_Tramitacion_Destino'.
     * 
     * @param decodificacion_Unidad_Tramitacion_Destino the value
     * of field 'decodificacion_Unidad_Tramitacion_Destino'.
     */
    public void setDecodificacion_Unidad_Tramitacion_Destino(
            final java.lang.String decodificacion_Unidad_Tramitacion_Destino) {
        this._decodificacion_Unidad_Tramitacion_Destino = decodificacion_Unidad_Tramitacion_Destino;
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
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino
     */
    public static es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino) org.exolab.castor.xml.Unmarshaller.unmarshal(es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino.class, reader);
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

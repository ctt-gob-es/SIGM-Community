/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema;

/**
 * Class De_Asunto.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class De_Asunto implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resumen.
     */
    private java.lang.String _resumen;

    /**
     * Field _codigo_Asunto_Segun_Destino.
     */
    private java.lang.String _codigo_Asunto_Segun_Destino;

    /**
     * Field _referencia_Externa.
     */
    private java.lang.String _referencia_Externa;

    /**
     * Field _numero_Expediente.
     */
    private java.lang.String _numero_Expediente;


      //----------------/
     //- Constructors -/
    //----------------/

    public De_Asunto() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'codigo_Asunto_Segun_Destino'.
     * 
     * @return the value of field 'Codigo_Asunto_Segun_Destino'.
     */
    public java.lang.String getCodigo_Asunto_Segun_Destino(
    ) {
        return this._codigo_Asunto_Segun_Destino;
    }

    /**
     * Returns the value of field 'numero_Expediente'.
     * 
     * @return the value of field 'Numero_Expediente'.
     */
    public java.lang.String getNumero_Expediente(
    ) {
        return this._numero_Expediente;
    }

    /**
     * Returns the value of field 'referencia_Externa'.
     * 
     * @return the value of field 'Referencia_Externa'.
     */
    public java.lang.String getReferencia_Externa(
    ) {
        return this._referencia_Externa;
    }

    /**
     * Returns the value of field 'resumen'.
     * 
     * @return the value of field 'Resumen'.
     */
    public java.lang.String getResumen(
    ) {
        return this._resumen;
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
     * Sets the value of field 'codigo_Asunto_Segun_Destino'.
     * 
     * @param codigo_Asunto_Segun_Destino the value of field
     * 'codigo_Asunto_Segun_Destino'.
     */
    public void setCodigo_Asunto_Segun_Destino(
            final java.lang.String codigo_Asunto_Segun_Destino) {
        this._codigo_Asunto_Segun_Destino = codigo_Asunto_Segun_Destino;
    }

    /**
     * Sets the value of field 'numero_Expediente'.
     * 
     * @param numero_Expediente the value of field
     * 'numero_Expediente'.
     */
    public void setNumero_Expediente(
            final java.lang.String numero_Expediente) {
        this._numero_Expediente = numero_Expediente;
    }

    /**
     * Sets the value of field 'referencia_Externa'.
     * 
     * @param referencia_Externa the value of field
     * 'referencia_Externa'.
     */
    public void setReferencia_Externa(
            final java.lang.String referencia_Externa) {
        this._referencia_Externa = referencia_Externa;
    }

    /**
     * Sets the value of field 'resumen'.
     * 
     * @param resumen the value of field 'resumen'.
     */
    public void setResumen(
            final java.lang.String resumen) {
        this._resumen = resumen;
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
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto
     */
    public static es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto) org.exolab.castor.xml.Unmarshaller.unmarshal(es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto.class, reader);
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

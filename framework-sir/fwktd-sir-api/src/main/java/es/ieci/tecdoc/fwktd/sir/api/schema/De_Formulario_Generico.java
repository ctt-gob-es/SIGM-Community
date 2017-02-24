/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema;

/**
 * Class De_Formulario_Generico.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class De_Formulario_Generico implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _expone.
     */
    private java.lang.String _expone;

    /**
     * Field _solicita.
     */
    private java.lang.String _solicita;


      //----------------/
     //- Constructors -/
    //----------------/

    public De_Formulario_Generico() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'expone'.
     * 
     * @return the value of field 'Expone'.
     */
    public java.lang.String getExpone(
    ) {
        return this._expone;
    }

    /**
     * Returns the value of field 'solicita'.
     * 
     * @return the value of field 'Solicita'.
     */
    public java.lang.String getSolicita(
    ) {
        return this._solicita;
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
     * Sets the value of field 'expone'.
     * 
     * @param expone the value of field 'expone'.
     */
    public void setExpone(
            final java.lang.String expone) {
        this._expone = expone;
    }

    /**
     * Sets the value of field 'solicita'.
     * 
     * @param solicita the value of field 'solicita'.
     */
    public void setSolicita(
            final java.lang.String solicita) {
        this._solicita = solicita;
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
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico
     */
    public static es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico) org.exolab.castor.xml.Unmarshaller.unmarshal(es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico.class, reader);
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

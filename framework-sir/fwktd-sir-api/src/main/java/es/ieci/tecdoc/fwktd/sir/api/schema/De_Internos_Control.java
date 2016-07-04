/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema;

/**
 * Class De_Internos_Control.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class De_Internos_Control implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tipo_Transporte_Entrada.
     */
    private java.lang.String _tipo_Transporte_Entrada;

    /**
     * Field _numero_Transporte_Entrada.
     */
    private java.lang.String _numero_Transporte_Entrada;

    /**
     * Field _nombre_Usuario.
     */
    private java.lang.String _nombre_Usuario;

    /**
     * Field _contacto_Usuario.
     */
    private java.lang.String _contacto_Usuario;

    /**
     * Field _identificador_Intercambio.
     */
    private java.lang.String _identificador_Intercambio;

    /**
     * Field _aplicacion_Version_Emisora.
     */
    private java.lang.String _aplicacion_Version_Emisora;

    /**
     * Field _tipo_Anotacion.
     */
    private java.lang.String _tipo_Anotacion;

    /**
     * Field _descripcion_Tipo_Anotacion.
     */
    private java.lang.String _descripcion_Tipo_Anotacion;

    /**
     * Field _tipo_Registro.
     */
    private java.lang.String _tipo_Registro;

    /**
     * Field _documentacion_Fisica.
     */
    private java.lang.String _documentacion_Fisica;

    /**
     * Field _observaciones_Apunte.
     */
    private java.lang.String _observaciones_Apunte;

    /**
     * Field _indicador_Prueba.
     */
    private java.lang.String _indicador_Prueba;

    /**
     * Field _codigo_Entidad_Registral_Inicio.
     */
    private java.lang.String _codigo_Entidad_Registral_Inicio;

    /**
     * Field _decodificacion_Entidad_Registral_Inicio.
     */
    private java.lang.String _decodificacion_Entidad_Registral_Inicio;


      //----------------/
     //- Constructors -/
    //----------------/

    public De_Internos_Control() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'aplicacion_Version_Emisora'.
     * 
     * @return the value of field 'Aplicacion_Version_Emisora'.
     */
    public java.lang.String getAplicacion_Version_Emisora(
    ) {
        return this._aplicacion_Version_Emisora;
    }

    /**
     * Returns the value of field
     * 'codigo_Entidad_Registral_Inicio'.
     * 
     * @return the value of field 'Codigo_Entidad_Registral_Inicio'.
     */
    public java.lang.String getCodigo_Entidad_Registral_Inicio(
    ) {
        return this._codigo_Entidad_Registral_Inicio;
    }

    /**
     * Returns the value of field 'contacto_Usuario'.
     * 
     * @return the value of field 'Contacto_Usuario'.
     */
    public java.lang.String getContacto_Usuario(
    ) {
        return this._contacto_Usuario;
    }

    /**
     * Returns the value of field
     * 'decodificacion_Entidad_Registral_Inicio'.
     * 
     * @return the value of field
     * 'Decodificacion_Entidad_Registral_Inicio'.
     */
    public java.lang.String getDecodificacion_Entidad_Registral_Inicio(
    ) {
        return this._decodificacion_Entidad_Registral_Inicio;
    }

    /**
     * Returns the value of field 'descripcion_Tipo_Anotacion'.
     * 
     * @return the value of field 'Descripcion_Tipo_Anotacion'.
     */
    public java.lang.String getDescripcion_Tipo_Anotacion(
    ) {
        return this._descripcion_Tipo_Anotacion;
    }

    /**
     * Returns the value of field 'documentacion_Fisica'.
     * 
     * @return the value of field 'Documentacion_Fisica'.
     */
    public java.lang.String getDocumentacion_Fisica(
    ) {
        return this._documentacion_Fisica;
    }

    /**
     * Returns the value of field 'identificador_Intercambio'.
     * 
     * @return the value of field 'Identificador_Intercambio'.
     */
    public java.lang.String getIdentificador_Intercambio(
    ) {
        return this._identificador_Intercambio;
    }

    /**
     * Returns the value of field 'indicador_Prueba'.
     * 
     * @return the value of field 'Indicador_Prueba'.
     */
    public java.lang.String getIndicador_Prueba(
    ) {
        return this._indicador_Prueba;
    }

    /**
     * Returns the value of field 'nombre_Usuario'.
     * 
     * @return the value of field 'Nombre_Usuario'.
     */
    public java.lang.String getNombre_Usuario(
    ) {
        return this._nombre_Usuario;
    }

    /**
     * Returns the value of field 'numero_Transporte_Entrada'.
     * 
     * @return the value of field 'Numero_Transporte_Entrada'.
     */
    public java.lang.String getNumero_Transporte_Entrada(
    ) {
        return this._numero_Transporte_Entrada;
    }

    /**
     * Returns the value of field 'observaciones_Apunte'.
     * 
     * @return the value of field 'Observaciones_Apunte'.
     */
    public java.lang.String getObservaciones_Apunte(
    ) {
        return this._observaciones_Apunte;
    }

    /**
     * Returns the value of field 'tipo_Anotacion'.
     * 
     * @return the value of field 'Tipo_Anotacion'.
     */
    public java.lang.String getTipo_Anotacion(
    ) {
        return this._tipo_Anotacion;
    }

    /**
     * Returns the value of field 'tipo_Registro'.
     * 
     * @return the value of field 'Tipo_Registro'.
     */
    public java.lang.String getTipo_Registro(
    ) {
        return this._tipo_Registro;
    }

    /**
     * Returns the value of field 'tipo_Transporte_Entrada'.
     * 
     * @return the value of field 'Tipo_Transporte_Entrada'.
     */
    public java.lang.String getTipo_Transporte_Entrada(
    ) {
        return this._tipo_Transporte_Entrada;
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
     * Sets the value of field 'aplicacion_Version_Emisora'.
     * 
     * @param aplicacion_Version_Emisora the value of field
     * 'aplicacion_Version_Emisora'.
     */
    public void setAplicacion_Version_Emisora(
            final java.lang.String aplicacion_Version_Emisora) {
        this._aplicacion_Version_Emisora = aplicacion_Version_Emisora;
    }

    /**
     * Sets the value of field 'codigo_Entidad_Registral_Inicio'.
     * 
     * @param codigo_Entidad_Registral_Inicio the value of field
     * 'codigo_Entidad_Registral_Inicio'.
     */
    public void setCodigo_Entidad_Registral_Inicio(
            final java.lang.String codigo_Entidad_Registral_Inicio) {
        this._codigo_Entidad_Registral_Inicio = codigo_Entidad_Registral_Inicio;
    }

    /**
     * Sets the value of field 'contacto_Usuario'.
     * 
     * @param contacto_Usuario the value of field 'contacto_Usuario'
     */
    public void setContacto_Usuario(
            final java.lang.String contacto_Usuario) {
        this._contacto_Usuario = contacto_Usuario;
    }

    /**
     * Sets the value of field
     * 'decodificacion_Entidad_Registral_Inicio'.
     * 
     * @param decodificacion_Entidad_Registral_Inicio the value of
     * field 'decodificacion_Entidad_Registral_Inicio'.
     */
    public void setDecodificacion_Entidad_Registral_Inicio(
            final java.lang.String decodificacion_Entidad_Registral_Inicio) {
        this._decodificacion_Entidad_Registral_Inicio = decodificacion_Entidad_Registral_Inicio;
    }

    /**
     * Sets the value of field 'descripcion_Tipo_Anotacion'.
     * 
     * @param descripcion_Tipo_Anotacion the value of field
     * 'descripcion_Tipo_Anotacion'.
     */
    public void setDescripcion_Tipo_Anotacion(
            final java.lang.String descripcion_Tipo_Anotacion) {
        this._descripcion_Tipo_Anotacion = descripcion_Tipo_Anotacion;
    }

    /**
     * Sets the value of field 'documentacion_Fisica'.
     * 
     * @param documentacion_Fisica the value of field
     * 'documentacion_Fisica'.
     */
    public void setDocumentacion_Fisica(
            final java.lang.String documentacion_Fisica) {
        this._documentacion_Fisica = documentacion_Fisica;
    }

    /**
     * Sets the value of field 'identificador_Intercambio'.
     * 
     * @param identificador_Intercambio the value of field
     * 'identificador_Intercambio'.
     */
    public void setIdentificador_Intercambio(
            final java.lang.String identificador_Intercambio) {
        this._identificador_Intercambio = identificador_Intercambio;
    }

    /**
     * Sets the value of field 'indicador_Prueba'.
     * 
     * @param indicador_Prueba the value of field 'indicador_Prueba'
     */
    public void setIndicador_Prueba(
            final java.lang.String indicador_Prueba) {
        this._indicador_Prueba = indicador_Prueba;
    }

    /**
     * Sets the value of field 'nombre_Usuario'.
     * 
     * @param nombre_Usuario the value of field 'nombre_Usuario'.
     */
    public void setNombre_Usuario(
            final java.lang.String nombre_Usuario) {
        this._nombre_Usuario = nombre_Usuario;
    }

    /**
     * Sets the value of field 'numero_Transporte_Entrada'.
     * 
     * @param numero_Transporte_Entrada the value of field
     * 'numero_Transporte_Entrada'.
     */
    public void setNumero_Transporte_Entrada(
            final java.lang.String numero_Transporte_Entrada) {
        this._numero_Transporte_Entrada = numero_Transporte_Entrada;
    }

    /**
     * Sets the value of field 'observaciones_Apunte'.
     * 
     * @param observaciones_Apunte the value of field
     * 'observaciones_Apunte'.
     */
    public void setObservaciones_Apunte(
            final java.lang.String observaciones_Apunte) {
        this._observaciones_Apunte = observaciones_Apunte;
    }

    /**
     * Sets the value of field 'tipo_Anotacion'.
     * 
     * @param tipo_Anotacion the value of field 'tipo_Anotacion'.
     */
    public void setTipo_Anotacion(
            final java.lang.String tipo_Anotacion) {
        this._tipo_Anotacion = tipo_Anotacion;
    }

    /**
     * Sets the value of field 'tipo_Registro'.
     * 
     * @param tipo_Registro the value of field 'tipo_Registro'.
     */
    public void setTipo_Registro(
            final java.lang.String tipo_Registro) {
        this._tipo_Registro = tipo_Registro;
    }

    /**
     * Sets the value of field 'tipo_Transporte_Entrada'.
     * 
     * @param tipo_Transporte_Entrada the value of field
     * 'tipo_Transporte_Entrada'.
     */
    public void setTipo_Transporte_Entrada(
            final java.lang.String tipo_Transporte_Entrada) {
        this._tipo_Transporte_Entrada = tipo_Transporte_Entrada;
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
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control
     */
    public static es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control) org.exolab.castor.xml.Unmarshaller.unmarshal(es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control.class, reader);
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

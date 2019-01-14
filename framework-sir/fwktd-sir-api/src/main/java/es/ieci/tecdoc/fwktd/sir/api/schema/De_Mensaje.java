/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema;

/**
 * Class De_Mensaje.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class De_Mensaje implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _codigo_Entidad_Registral_Origen.
     */
    private java.lang.String _codigo_Entidad_Registral_Origen;

    /**
     * Field _codigo_Entidad_Registral_Destino.
     */
    private java.lang.String _codigo_Entidad_Registral_Destino;

    /**
     * Field _identificador_Intercambio.
     */
    private java.lang.String _identificador_Intercambio;

    /**
     * Field _tipo_Mensaje.
     */
    private java.lang.String _tipo_Mensaje;

    /**
     * Field _descripcion_Mensaje.
     */
    private java.lang.String _descripcion_Mensaje;

    /**
     * Field _numero_Registro_Entrada_Destino.
     */
    private java.lang.String _numero_Registro_Entrada_Destino;

    /**
     * Field _fecha_Hora_Entrada_Destino.
     */
    private java.lang.String _fecha_Hora_Entrada_Destino;

    /**
     * Field _indicador_Prueba.
     */
    private es.ieci.tecdoc.fwktd.sir.api.schema.types.Indicador_PruebaType _indicador_Prueba;

    /**
     * Field _identificador_FicheroList.
     */
    private java.util.List<java.lang.String> _identificador_FicheroList;

    /**
     * Field _codigo_Error.
     */
    private java.lang.String _codigo_Error;


      //----------------/
     //- Constructors -/
    //----------------/

    public De_Mensaje() {
        super();
        this._identificador_FicheroList = new java.util.ArrayList<java.lang.String>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vIdentificador_Fichero
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdentificador_Fichero(
            final java.lang.String vIdentificador_Fichero)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._identificador_FicheroList.size() >= 10) {
            throw new IndexOutOfBoundsException("addIdentificador_Fichero has a maximum of 10");
        }

        this._identificador_FicheroList.add(vIdentificador_Fichero);
    }

    /**
     * 
     * 
     * @param index
     * @param vIdentificador_Fichero
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdentificador_Fichero(
            final int index,
            final java.lang.String vIdentificador_Fichero)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._identificador_FicheroList.size() >= 10) {
            throw new IndexOutOfBoundsException("addIdentificador_Fichero has a maximum of 10");
        }

        this._identificador_FicheroList.add(index, vIdentificador_Fichero);
    }

    /**
     * Method enumerateIdentificador_Fichero.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends java.lang.String> enumerateIdentificador_Fichero(
    ) {
        return java.util.Collections.enumeration(this._identificador_FicheroList);
    }

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
     * 'codigo_Entidad_Registral_Origen'.
     * 
     * @return the value of field 'Codigo_Entidad_Registral_Origen'.
     */
    public java.lang.String getCodigo_Entidad_Registral_Origen(
    ) {
        return this._codigo_Entidad_Registral_Origen;
    }

    /**
     * Returns the value of field 'codigo_Error'.
     * 
     * @return the value of field 'Codigo_Error'.
     */
    public java.lang.String getCodigo_Error(
    ) {
        return this._codigo_Error;
    }

    /**
     * Returns the value of field 'descripcion_Mensaje'.
     * 
     * @return the value of field 'Descripcion_Mensaje'.
     */
    public java.lang.String getDescripcion_Mensaje(
    ) {
        return this._descripcion_Mensaje;
    }

    /**
     * Returns the value of field 'fecha_Hora_Entrada_Destino'.
     * 
     * @return the value of field 'Fecha_Hora_Entrada_Destino'.
     */
    public java.lang.String getFecha_Hora_Entrada_Destino(
    ) {
        return this._fecha_Hora_Entrada_Destino;
    }

    /**
     * Method getIdentificador_Fichero.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getIdentificador_Fichero(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._identificador_FicheroList.size()) {
            throw new IndexOutOfBoundsException("getIdentificador_Fichero: Index value '" + index + "' not in range [0.." + (this._identificador_FicheroList.size() - 1) + "]");
        }

        return (java.lang.String) _identificador_FicheroList.get(index);
    }

    /**
     * Method getIdentificador_Fichero.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getIdentificador_Fichero(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._identificador_FicheroList.toArray(array);
    }

    /**
     * Method getIdentificador_FicheroCount.
     * 
     * @return the size of this collection
     */
    public int getIdentificador_FicheroCount(
    ) {
        return this._identificador_FicheroList.size();
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
    public es.ieci.tecdoc.fwktd.sir.api.schema.types.Indicador_PruebaType getIndicador_Prueba(
    ) {
        return this._indicador_Prueba;
    }

    /**
     * Returns the value of field
     * 'numero_Registro_Entrada_Destino'.
     * 
     * @return the value of field 'Numero_Registro_Entrada_Destino'.
     */
    public java.lang.String getNumero_Registro_Entrada_Destino(
    ) {
        return this._numero_Registro_Entrada_Destino;
    }

    /**
     * Returns the value of field 'tipo_Mensaje'.
     * 
     * @return the value of field 'Tipo_Mensaje'.
     */
    public java.lang.String getTipo_Mensaje(
    ) {
        return this._tipo_Mensaje;
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
     * Method iterateIdentificador_Fichero.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends java.lang.String> iterateIdentificador_Fichero(
    ) {
        return this._identificador_FicheroList.iterator();
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
     */
    public void removeAllIdentificador_Fichero(
    ) {
        this._identificador_FicheroList.clear();
    }

    /**
     * Method removeIdentificador_Fichero.
     * 
     * @param vIdentificador_Fichero
     * @return true if the object was removed from the collection.
     */
    public boolean removeIdentificador_Fichero(
            final java.lang.String vIdentificador_Fichero) {
        boolean removed = _identificador_FicheroList.remove(vIdentificador_Fichero);
        return removed;
    }

    /**
     * Method removeIdentificador_FicheroAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeIdentificador_FicheroAt(
            final int index) {
        java.lang.Object obj = this._identificador_FicheroList.remove(index);
        return (java.lang.String) obj;
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
     * Sets the value of field 'codigo_Error'.
     * 
     * @param codigo_Error the value of field 'codigo_Error'.
     */
    public void setCodigo_Error(
            final java.lang.String codigo_Error) {
        this._codigo_Error = codigo_Error;
    }

    /**
     * Sets the value of field 'descripcion_Mensaje'.
     * 
     * @param descripcion_Mensaje the value of field
     * 'descripcion_Mensaje'.
     */
    public void setDescripcion_Mensaje(
            final java.lang.String descripcion_Mensaje) {
        this._descripcion_Mensaje = descripcion_Mensaje;
    }

    /**
     * Sets the value of field 'fecha_Hora_Entrada_Destino'.
     * 
     * @param fecha_Hora_Entrada_Destino the value of field
     * 'fecha_Hora_Entrada_Destino'.
     */
    public void setFecha_Hora_Entrada_Destino(
            final java.lang.String fecha_Hora_Entrada_Destino) {
        this._fecha_Hora_Entrada_Destino = fecha_Hora_Entrada_Destino;
    }

    /**
     * 
     * 
     * @param index
     * @param vIdentificador_Fichero
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setIdentificador_Fichero(
            final int index,
            final java.lang.String vIdentificador_Fichero)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._identificador_FicheroList.size()) {
            throw new IndexOutOfBoundsException("setIdentificador_Fichero: Index value '" + index + "' not in range [0.." + (this._identificador_FicheroList.size() - 1) + "]");
        }

        this._identificador_FicheroList.set(index, vIdentificador_Fichero);
    }

    /**
     * 
     * 
     * @param vIdentificador_FicheroArray
     */
    public void setIdentificador_Fichero(
            final java.lang.String[] vIdentificador_FicheroArray) {
        //-- copy array
        _identificador_FicheroList.clear();

        for (int i = 0; i < vIdentificador_FicheroArray.length; i++) {
                this._identificador_FicheroList.add(vIdentificador_FicheroArray[i]);
        }
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
            final es.ieci.tecdoc.fwktd.sir.api.schema.types.Indicador_PruebaType indicador_Prueba) {
        this._indicador_Prueba = indicador_Prueba;
    }

    /**
     * Sets the value of field 'numero_Registro_Entrada_Destino'.
     * 
     * @param numero_Registro_Entrada_Destino the value of field
     * 'numero_Registro_Entrada_Destino'.
     */
    public void setNumero_Registro_Entrada_Destino(
            final java.lang.String numero_Registro_Entrada_Destino) {
        this._numero_Registro_Entrada_Destino = numero_Registro_Entrada_Destino;
    }

    /**
     * Sets the value of field 'tipo_Mensaje'.
     * 
     * @param tipo_Mensaje the value of field 'tipo_Mensaje'.
     */
    public void setTipo_Mensaje(
            final java.lang.String tipo_Mensaje) {
        this._tipo_Mensaje = tipo_Mensaje;
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
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Mensaje
     */
    public static es.ieci.tecdoc.fwktd.sir.api.schema.De_Mensaje unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Mensaje) org.exolab.castor.xml.Unmarshaller.unmarshal(es.ieci.tecdoc.fwktd.sir.api.schema.De_Mensaje.class, reader);
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

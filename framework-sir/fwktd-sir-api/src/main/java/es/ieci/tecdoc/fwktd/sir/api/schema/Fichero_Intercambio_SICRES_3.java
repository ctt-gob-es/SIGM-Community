/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema;

/**
 * Class Fichero_Intercambio_SICRES_3.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Fichero_Intercambio_SICRES_3 implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _de_Origen_o_Remitente.
     */
    private es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente _de_Origen_o_Remitente;

    /**
     * Field _de_Destino.
     */
    private es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino _de_Destino;

    /**
     * Field _de_InteresadoList.
     */
    private java.util.List<es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado> _de_InteresadoList;

    /**
     * Field _de_Asunto.
     */
    private es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto _de_Asunto;

    /**
     * Field _de_AnexoList.
     */
    private java.util.List<es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo> _de_AnexoList;

    /**
     * Field _de_Internos_Control.
     */
    private es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control _de_Internos_Control;

    /**
     * Field _de_Formulario_Generico.
     */
    private es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico _de_Formulario_Generico;


      //----------------/
     //- Constructors -/
    //----------------/

    public Fichero_Intercambio_SICRES_3() {
        super();
        this._de_InteresadoList = new java.util.ArrayList<es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado>();
        this._de_AnexoList = new java.util.ArrayList<es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo>();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDe_Anexo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDe_Anexo(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo vDe_Anexo)
    throws java.lang.IndexOutOfBoundsException {
        this._de_AnexoList.add(vDe_Anexo);
    }

    /**
     * 
     * 
     * @param index
     * @param vDe_Anexo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDe_Anexo(
            final int index,
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo vDe_Anexo)
    throws java.lang.IndexOutOfBoundsException {
        this._de_AnexoList.add(index, vDe_Anexo);
    }

    /**
     * 
     * 
     * @param vDe_Interesado
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDe_Interesado(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado vDe_Interesado)
    throws java.lang.IndexOutOfBoundsException {
        this._de_InteresadoList.add(vDe_Interesado);
    }

    /**
     * 
     * 
     * @param index
     * @param vDe_Interesado
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDe_Interesado(
            final int index,
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado vDe_Interesado)
    throws java.lang.IndexOutOfBoundsException {
        this._de_InteresadoList.add(index, vDe_Interesado);
    }

    /**
     * Method enumerateDe_Anexo.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo> enumerateDe_Anexo(
    ) {
        return java.util.Collections.enumeration(this._de_AnexoList);
    }

    /**
     * Method enumerateDe_Interesado.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado> enumerateDe_Interesado(
    ) {
        return java.util.Collections.enumeration(this._de_InteresadoList);
    }

    /**
     * Method getDe_Anexo.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo at the given
     * index
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo getDe_Anexo(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._de_AnexoList.size()) {
            throw new IndexOutOfBoundsException("getDe_Anexo: Index value '" + index + "' not in range [0.." + (this._de_AnexoList.size() - 1) + "]");
        }

        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo) _de_AnexoList.get(index);
    }

    /**
     * Method getDe_Anexo.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo[] getDe_Anexo(
    ) {
        es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo[] array = new es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo[0];
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo[]) this._de_AnexoList.toArray(array);
    }

    /**
     * Method getDe_AnexoCount.
     * 
     * @return the size of this collection
     */
    public int getDe_AnexoCount(
    ) {
        return this._de_AnexoList.size();
    }

    /**
     * Returns the value of field 'de_Asunto'.
     * 
     * @return the value of field 'De_Asunto'.
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto getDe_Asunto(
    ) {
        return this._de_Asunto;
    }

    /**
     * Returns the value of field 'de_Destino'.
     * 
     * @return the value of field 'De_Destino'.
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino getDe_Destino(
    ) {
        return this._de_Destino;
    }

    /**
     * Returns the value of field 'de_Formulario_Generico'.
     * 
     * @return the value of field 'De_Formulario_Generico'.
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico getDe_Formulario_Generico(
    ) {
        return this._de_Formulario_Generico;
    }

    /**
     * Method getDe_Interesado.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado at the
     * given index
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado getDe_Interesado(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._de_InteresadoList.size()) {
            throw new IndexOutOfBoundsException("getDe_Interesado: Index value '" + index + "' not in range [0.." + (this._de_InteresadoList.size() - 1) + "]");
        }

        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado) _de_InteresadoList.get(index);
    }

    /**
     * Method getDe_Interesado.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado[] getDe_Interesado(
    ) {
        es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado[] array = new es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado[0];
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado[]) this._de_InteresadoList.toArray(array);
    }

    /**
     * Method getDe_InteresadoCount.
     * 
     * @return the size of this collection
     */
    public int getDe_InteresadoCount(
    ) {
        return this._de_InteresadoList.size();
    }

    /**
     * Returns the value of field 'de_Internos_Control'.
     * 
     * @return the value of field 'De_Internos_Control'.
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control getDe_Internos_Control(
    ) {
        return this._de_Internos_Control;
    }

    /**
     * Returns the value of field 'de_Origen_o_Remitente'.
     * 
     * @return the value of field 'De_Origen_o_Remitente'.
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente getDe_Origen_o_Remitente(
    ) {
        return this._de_Origen_o_Remitente;
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
     * Method iterateDe_Anexo.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo> iterateDe_Anexo(
    ) {
        return this._de_AnexoList.iterator();
    }

    /**
     * Method iterateDe_Interesado.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado> iterateDe_Interesado(
    ) {
        return this._de_InteresadoList.iterator();
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
    public void removeAllDe_Anexo(
    ) {
        this._de_AnexoList.clear();
    }

    /**
     */
    public void removeAllDe_Interesado(
    ) {
        this._de_InteresadoList.clear();
    }

    /**
     * Method removeDe_Anexo.
     * 
     * @param vDe_Anexo
     * @return true if the object was removed from the collection.
     */
    public boolean removeDe_Anexo(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo vDe_Anexo) {
        boolean removed = _de_AnexoList.remove(vDe_Anexo);
        return removed;
    }

    /**
     * Method removeDe_AnexoAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo removeDe_AnexoAt(
            final int index) {
        java.lang.Object obj = this._de_AnexoList.remove(index);
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo) obj;
    }

    /**
     * Method removeDe_Interesado.
     * 
     * @param vDe_Interesado
     * @return true if the object was removed from the collection.
     */
    public boolean removeDe_Interesado(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado vDe_Interesado) {
        boolean removed = _de_InteresadoList.remove(vDe_Interesado);
        return removed;
    }

    /**
     * Method removeDe_InteresadoAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado removeDe_InteresadoAt(
            final int index) {
        java.lang.Object obj = this._de_InteresadoList.remove(index);
        return (es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDe_Anexo
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDe_Anexo(
            final int index,
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo vDe_Anexo)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._de_AnexoList.size()) {
            throw new IndexOutOfBoundsException("setDe_Anexo: Index value '" + index + "' not in range [0.." + (this._de_AnexoList.size() - 1) + "]");
        }

        this._de_AnexoList.set(index, vDe_Anexo);
    }

    /**
     * 
     * 
     * @param vDe_AnexoArray
     */
    public void setDe_Anexo(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo[] vDe_AnexoArray) {
        //-- copy array
        _de_AnexoList.clear();

        for (int i = 0; i < vDe_AnexoArray.length; i++) {
                this._de_AnexoList.add(vDe_AnexoArray[i]);
        }
    }

    /**
     * Sets the value of field 'de_Asunto'.
     * 
     * @param de_Asunto the value of field 'de_Asunto'.
     */
    public void setDe_Asunto(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto de_Asunto) {
        this._de_Asunto = de_Asunto;
    }

    /**
     * Sets the value of field 'de_Destino'.
     * 
     * @param de_Destino the value of field 'de_Destino'.
     */
    public void setDe_Destino(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino de_Destino) {
        this._de_Destino = de_Destino;
    }

    /**
     * Sets the value of field 'de_Formulario_Generico'.
     * 
     * @param de_Formulario_Generico the value of field
     * 'de_Formulario_Generico'.
     */
    public void setDe_Formulario_Generico(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico de_Formulario_Generico) {
        this._de_Formulario_Generico = de_Formulario_Generico;
    }

    /**
     * 
     * 
     * @param index
     * @param vDe_Interesado
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDe_Interesado(
            final int index,
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado vDe_Interesado)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._de_InteresadoList.size()) {
            throw new IndexOutOfBoundsException("setDe_Interesado: Index value '" + index + "' not in range [0.." + (this._de_InteresadoList.size() - 1) + "]");
        }

        this._de_InteresadoList.set(index, vDe_Interesado);
    }

    /**
     * 
     * 
     * @param vDe_InteresadoArray
     */
    public void setDe_Interesado(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado[] vDe_InteresadoArray) {
        //-- copy array
        _de_InteresadoList.clear();

        for (int i = 0; i < vDe_InteresadoArray.length; i++) {
                this._de_InteresadoList.add(vDe_InteresadoArray[i]);
        }
    }

    /**
     * Sets the value of field 'de_Internos_Control'.
     * 
     * @param de_Internos_Control the value of field
     * 'de_Internos_Control'.
     */
    public void setDe_Internos_Control(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control de_Internos_Control) {
        this._de_Internos_Control = de_Internos_Control;
    }

    /**
     * Sets the value of field 'de_Origen_o_Remitente'.
     * 
     * @param de_Origen_o_Remitente the value of field
     * 'de_Origen_o_Remitente'.
     */
    public void setDe_Origen_o_Remitente(
            final es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente de_Origen_o_Remitente) {
        this._de_Origen_o_Remitente = de_Origen_o_Remitente;
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
     * es.ieci.tecdoc.fwktd.sir.api.schema.Fichero_Intercambio_SICRES_3
     */
    public static es.ieci.tecdoc.fwktd.sir.api.schema.Fichero_Intercambio_SICRES_3 unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (es.ieci.tecdoc.fwktd.sir.api.schema.Fichero_Intercambio_SICRES_3) org.exolab.castor.xml.Unmarshaller.unmarshal(es.ieci.tecdoc.fwktd.sir.api.schema.Fichero_Intercambio_SICRES_3.class, reader);
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

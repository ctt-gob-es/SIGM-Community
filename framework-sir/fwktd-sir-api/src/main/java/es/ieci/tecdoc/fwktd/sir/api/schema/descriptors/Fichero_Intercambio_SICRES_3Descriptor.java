/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package es.ieci.tecdoc.fwktd.sir.api.schema.descriptors;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import es.ieci.tecdoc.fwktd.sir.api.schema.Fichero_Intercambio_SICRES_3;

/**
 * Class Fichero_Intercambio_SICRES_3Descriptor.
 * 
 * @version $Revision$ $Date$
 */
public class Fichero_Intercambio_SICRES_3Descriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field _xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public Fichero_Intercambio_SICRES_3Descriptor() {
        super();
        _xmlName = "Fichero_Intercambio_SICRES_3";
        _elementDefinition = true;

        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors

        //-- initialize element descriptors

        //-- _de_Origen_o_Remitente
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente.class, "_de_Origen_o_Remitente", "De_Origen_o_Remitente", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                return target.getDe_Origen_o_Remitente();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.setDe_Origen_o_Remitente( (es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente();
            }
        };
        desc.setSchemaType("es.ieci.tecdoc.fwktd.sir.api.schema.De_Origen_o_Remitente");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _de_Origen_o_Remitente
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _de_Destino
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino.class, "_de_Destino", "De_Destino", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                return target.getDe_Destino();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.setDe_Destino( (es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino();
            }
        };
        desc.setSchemaType("es.ieci.tecdoc.fwktd.sir.api.schema.De_Destino");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _de_Destino
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _de_InteresadoList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado.class, "_de_InteresadoList", "De_Interesado", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                return target.getDe_Interesado();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.addDe_Interesado( (es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.removeAllDe_Interesado();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado();
            }
        };
        desc.setSchemaType("list");
        desc.setComponentType("es.ieci.tecdoc.fwktd.sir.api.schema.De_Interesado");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _de_InteresadoList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _de_Asunto
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto.class, "_de_Asunto", "De_Asunto", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                return target.getDe_Asunto();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.setDe_Asunto( (es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto();
            }
        };
        desc.setSchemaType("es.ieci.tecdoc.fwktd.sir.api.schema.De_Asunto");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _de_Asunto
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _de_AnexoList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo.class, "_de_AnexoList", "De_Anexo", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                return target.getDe_Anexo();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.addDe_Anexo( (es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.removeAllDe_Anexo();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo();
            }
        };
        desc.setSchemaType("list");
        desc.setComponentType("es.ieci.tecdoc.fwktd.sir.api.schema.De_Anexo");
        desc.setHandler(handler);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _de_AnexoList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _de_Internos_Control
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control.class, "_de_Internos_Control", "De_Internos_Control", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                return target.getDe_Internos_Control();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.setDe_Internos_Control( (es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control();
            }
        };
        desc.setSchemaType("es.ieci.tecdoc.fwktd.sir.api.schema.De_Internos_Control");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _de_Internos_Control
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _de_Formulario_Generico
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico.class, "_de_Formulario_Generico", "De_Formulario_Generico", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                return target.getDe_Formulario_Generico();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Fichero_Intercambio_SICRES_3 target = (Fichero_Intercambio_SICRES_3) object;
                    target.setDe_Formulario_Generico( (es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico();
            }
        };
        desc.setSchemaType("es.ieci.tecdoc.fwktd.sir.api.schema.De_Formulario_Generico");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: _de_Formulario_Generico
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    @Override()
    public org.exolab.castor.mapping.AccessMode getAccessMode(
    ) {
        return null;
    }

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    @Override()
    public org.exolab.castor.mapping.FieldDescriptor getIdentity(
    ) {
        return _identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    @Override()
    public java.lang.Class getJavaClass(
    ) {
        return es.ieci.tecdoc.fwktd.sir.api.schema.Fichero_Intercambio_SICRES_3.class;
    }

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpacePrefix(
    ) {
        return _nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshaling and
     * unmarshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpaceURI(
    ) {
        return _nsURI;
    }

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    @Override()
    public org.exolab.castor.xml.TypeValidator getValidator(
    ) {
        return this;
    }

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    @Override()
    public java.lang.String getXMLName(
    ) {
        return _xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition(
    ) {
        return _elementDefinition;
    }

}

/**
 * PadronSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class PadronSoapStub extends org.apache.axis.client.Stub implements es.atm2.PadronSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[8];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ObtenerPersonaPorNombre");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "codInstitucion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "esFisica"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "nombre"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "apellido1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "apellido2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "numItems"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "desdePosicion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerPersonaPorNombreResponse>ObtenerPersonaPorNombreResult"));
        oper.setReturnClass(es.atm2.ObtenerPersonaPorNombreResponseObtenerPersonaPorNombreResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerPersonaPorNombreResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ObtenerPersonaPorNIF");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sCodInstitucion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sPersonaFisica_o_Juridica"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sDocumentoIdentidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerPersonaPorNIFResponse>ObtenerPersonaPorNIFResult"));
        oper.setReturnClass(es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerPersonaPorNIFResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ObtenerDatosPadronalesPersona");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sCodInstitucion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sTipoDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sDocumentoIdentidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerDatosPadronalesPersonaResponse>ObtenerDatosPadronalesPersonaResult"));
        oper.setReturnClass(es.atm2.ObtenerDatosPadronalesPersonaResponseObtenerDatosPadronalesPersonaResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerDatosPadronalesPersonaResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ObtenerDatosConvivenciaPersona");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sCodInstitucion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sTipoDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "p_sDocumentoIdentidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerDatosConvivenciaPersonaResponse>ObtenerDatosConvivenciaPersonaResult"));
        oper.setReturnClass(es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerDatosConvivenciaPersonaResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ObtenerCertificadoConvivencia");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "codInstitucion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "tipoDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "documentoIdentidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerCertificadoConvivenciaResponse>ObtenerCertificadoConvivenciaResult"));
        oper.setReturnClass(es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerCertificadoConvivenciaResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ObtenerVolanteConvivencia");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "codInstitucion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "tipoDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "documentoIdentidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerVolanteConvivenciaResponse>ObtenerVolanteConvivenciaResult"));
        oper.setReturnClass(es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerVolanteConvivenciaResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ObtenerCertificadoEmpadronamiento");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "codInstitucion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "tipoDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "documentoIdentidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerCertificadoEmpadronamientoResponse>ObtenerCertificadoEmpadronamientoResult"));
        oper.setReturnClass(es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerCertificadoEmpadronamientoResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ObtenerVolanteEmpadronamiento");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "codInstitucion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "tipoDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://atm2.es/", "documentoIdentidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerVolanteEmpadronamientoResponse>ObtenerVolanteEmpadronamientoResult"));
        oper.setReturnClass(es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerVolanteEmpadronamientoResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

    }

    public PadronSoapStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PadronSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PadronSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerCertificadoConvivenciaResponse>ObtenerCertificadoConvivenciaResult");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerCertificadoEmpadronamientoResponse>ObtenerCertificadoEmpadronamientoResult");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerDatosConvivenciaPersonaResponse>ObtenerDatosConvivenciaPersonaResult");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerDatosPadronalesPersonaResponse>ObtenerDatosPadronalesPersonaResult");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerDatosPadronalesPersonaResponseObtenerDatosPadronalesPersonaResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerPersonaPorNIFResponse>ObtenerPersonaPorNIFResult");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerPersonaPorNombreResponse>ObtenerPersonaPorNombreResult");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerPersonaPorNombreResponseObtenerPersonaPorNombreResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerVolanteConvivenciaResponse>ObtenerVolanteConvivenciaResult");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerVolanteEmpadronamientoResponse>ObtenerVolanteEmpadronamientoResult");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerCertificadoConvivencia");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerCertificadoConvivencia.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerCertificadoConvivenciaResponse");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerCertificadoConvivenciaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerCertificadoEmpadronamiento");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerCertificadoEmpadronamiento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerCertificadoEmpadronamientoResponse");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerCertificadoEmpadronamientoResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerDatosConvivenciaPersona");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerDatosConvivenciaPersona.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerDatosConvivenciaPersonaResponse");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerDatosConvivenciaPersonaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerDatosPadronalesPersona");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerDatosPadronalesPersona.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerDatosPadronalesPersonaResponse");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerDatosPadronalesPersonaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerPersonaPorNIF");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerPersonaPorNIF.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerPersonaPorNIFResponse");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerPersonaPorNIFResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerVolanteConvivencia");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerVolanteConvivencia.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerVolanteConvivenciaResponse");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerVolanteConvivenciaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerVolanteEmpadronamiento");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerVolanteEmpadronamiento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerVolanteEmpadronamientoResponse");
            cachedSerQNames.add(qName);
            cls = es.atm2.ObtenerVolanteEmpadronamientoResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public es.atm2.ObtenerPersonaPorNombreResponseObtenerPersonaPorNombreResult obtenerPersonaPorNombre(java.lang.String codInstitucion, java.lang.String esFisica, java.lang.String nombre, java.lang.String apellido1, java.lang.String apellido2, int numItems, java.lang.String desdePosicion) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://atm2.es/ObtenerPersonaPorNombre");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerPersonaPorNombre"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codInstitucion, esFisica, nombre, apellido1, apellido2, new java.lang.Integer(numItems), desdePosicion});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.atm2.ObtenerPersonaPorNombreResponseObtenerPersonaPorNombreResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.atm2.ObtenerPersonaPorNombreResponseObtenerPersonaPorNombreResult) org.apache.axis.utils.JavaUtils.convert(_resp, es.atm2.ObtenerPersonaPorNombreResponseObtenerPersonaPorNombreResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult obtenerPersonaPorNIF(java.lang.String p_sCodInstitucion, java.lang.String p_sPersonaFisica_o_Juridica, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://atm2.es/ObtenerPersonaPorNIF");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerPersonaPorNIF"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {p_sCodInstitucion, p_sPersonaFisica_o_Juridica, p_sDocumentoIdentidad});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult) org.apache.axis.utils.JavaUtils.convert(_resp, es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.atm2.ObtenerDatosPadronalesPersonaResponseObtenerDatosPadronalesPersonaResult obtenerDatosPadronalesPersona(java.lang.String p_sCodInstitucion, java.lang.String p_sTipoDocumento, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://atm2.es/ObtenerDatosPadronalesPersona");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerDatosPadronalesPersona"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {p_sCodInstitucion, p_sTipoDocumento, p_sDocumentoIdentidad});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.atm2.ObtenerDatosPadronalesPersonaResponseObtenerDatosPadronalesPersonaResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.atm2.ObtenerDatosPadronalesPersonaResponseObtenerDatosPadronalesPersonaResult) org.apache.axis.utils.JavaUtils.convert(_resp, es.atm2.ObtenerDatosPadronalesPersonaResponseObtenerDatosPadronalesPersonaResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult obtenerDatosConvivenciaPersona(java.lang.String p_sCodInstitucion, java.lang.String p_sTipoDocumento, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://atm2.es/ObtenerDatosConvivenciaPersona");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerDatosConvivenciaPersona"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {p_sCodInstitucion, p_sTipoDocumento, p_sDocumentoIdentidad});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult) org.apache.axis.utils.JavaUtils.convert(_resp, es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult obtenerCertificadoConvivencia(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://atm2.es/ObtenerCertificadoConvivencia");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerCertificadoConvivencia"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codInstitucion, tipoDocumento, documentoIdentidad});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult) org.apache.axis.utils.JavaUtils.convert(_resp, es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult obtenerVolanteConvivencia(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://atm2.es/ObtenerVolanteConvivencia");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerVolanteConvivencia"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codInstitucion, tipoDocumento, documentoIdentidad});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult) org.apache.axis.utils.JavaUtils.convert(_resp, es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult obtenerCertificadoEmpadronamiento(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://atm2.es/ObtenerCertificadoEmpadronamiento");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerCertificadoEmpadronamiento"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codInstitucion, tipoDocumento, documentoIdentidad});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult) org.apache.axis.utils.JavaUtils.convert(_resp, es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult obtenerVolanteEmpadronamiento(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://atm2.es/ObtenerVolanteEmpadronamiento");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerVolanteEmpadronamiento"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codInstitucion, tipoDocumento, documentoIdentidad});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult) org.apache.axis.utils.JavaUtils.convert(_resp, es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}

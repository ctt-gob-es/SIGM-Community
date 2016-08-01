/**
 * PlataformaContratacionSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.services;

public class PlataformaContratacionSoapBindingStub extends org.apache.axis.client.Stub implements es.dipucr.contratacion.services.PlataformaContratacion {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[7];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("anuncioAdjudicacion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "entidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "anuncioAdjudicacion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AnuncioAdjudicacionBean"), es.dipucr.contratacion.client.beans.AnuncioAdjudicacionBean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "publishedByUser"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado"));
        oper.setReturnClass(es.dipucr.contratacion.resultadoBeans.Resultado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "anuncioAdjudicacionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("consultarDatosAlta");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "entidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "numexp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "publishedByUser"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado"));
        oper.setReturnClass(es.dipucr.contratacion.resultadoBeans.Resultado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "consultarDatosAltaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("envioOtrosDocumentosLicitacion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "entidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "documentoAdicional"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Documento"), es.dipucr.contratacion.client.beans.Documento.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "publishedByUser"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado"));
        oper.setReturnClass(es.dipucr.contratacion.resultadoBeans.Resultado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "envioOtrosDocumentosLicitacionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("envioAnalisisPrevio");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "entidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "analisisPrevio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AnuncioPrevioBean"), es.dipucr.contratacion.client.beans.AnuncioPrevioBean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "publishedByUser"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado"));
        oper.setReturnClass(es.dipucr.contratacion.resultadoBeans.Resultado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "envioAnalisisPrevioReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("envioPliegos");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "entidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "pliego"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PliegoBean"), es.dipucr.contratacion.client.beans.PliegoBean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "publishedByUser"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado"));
        oper.setReturnClass(es.dipucr.contratacion.resultadoBeans.Resultado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "envioPliegosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("estadoExpediente");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "entidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "numexp"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "publishedByUser"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado"));
        oper.setReturnClass(es.dipucr.contratacion.resultadoBeans.Resultado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "estadoExpedienteReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("envioPublicacionAnuncioLicitacion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "entidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "anuncioLicitacion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AnuncioLicitacionBean"), es.dipucr.contratacion.client.beans.AnuncioLicitacionBean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "publishedByUser"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado"));
        oper.setReturnClass(es.dipucr.contratacion.resultadoBeans.Resultado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "envioPublicacionAnuncioLicitacionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

    }

    public PlataformaContratacionSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PlataformaContratacionSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PlataformaContratacionSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AnuncioAdjudicacionBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.AnuncioAdjudicacionBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AnuncioBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.AnuncioBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AnuncioLicitacionBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.AnuncioLicitacionBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AnuncioPrevioBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.AnuncioPrevioBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AplicacionPresupuestaria");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.AplicacionPresupuestaria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.Campo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CondicionesLicitadores");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.CondicionesLicitadores.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CriterioAdjudicacionMultCrit");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.CriterioAdjudicacionMultCrit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CriteriosAdjudicacion");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.CriteriosAdjudicacion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "DatoDocumento");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.DatoDocumento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Documento");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.Documento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "DuracionContratoBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.DuracionContratoBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "EventoAperturaBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.EventoAperturaBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "FormalizacionBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.FormalizacionBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "FundacionPrograma");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.FundacionPrograma.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Garantia");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.Garantia.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "LicitadorBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.LicitadorBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "OfertasRecibidas");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.OfertasRecibidas.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Periodo");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.Periodo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PersonaComite");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.PersonaComite.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PersonalContacto");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.PersonalContacto.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PliegoBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.PliegoBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PublicacionesOficialesBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.PublicacionesOficialesBean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "RequisitfiDeclaraciones");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SobreElectronico");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.SobreElectronico.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SolvenciaEconomica");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.SolvenciaEconomica.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SolvenciaTecnica");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.SolvenciaTecnica.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "VariantesOfertas");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.VariantesOfertas.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://cbclib.common.codice.dgpe.org", "EmbeddedDocumentBinaryObjectType");
            cachedSerQNames.add(qName);
            cls = org.dgpe.codice.common.cbclib.EmbeddedDocumentBinaryObjectType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Anuncio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.Anuncio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "ExpedientStateData");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.ExpedientStateData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "GeneralErrorDetails");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Mensaje");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.Mensaje.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "OfficialPublicationResult");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.OfficialPublicationResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "OfficialPublicationResults");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "PlaceAskResult");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.PlaceAskResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "PublicationResult");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.PublicationResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "PublishErrorDetails");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.PublishErrorDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.Resultado.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "VisualizationResult");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.VisualizationResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_AplicacionPresupuestaria");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.AplicacionPresupuestaria[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AplicacionPresupuestaria");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_Campo");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.Campo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_CriterioAdjudicacionMultCrit");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.CriterioAdjudicacionMultCrit[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CriterioAdjudicacionMultCrit");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_DatoDocumento");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.DatoDocumento[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "DatoDocumento");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_Garantia");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.Garantia[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Garantia");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_LicitadorBean");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.LicitadorBean[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "LicitadorBean");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_PersonaComite");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.PersonaComite[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PersonaComite");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_RequisitfiDeclaraciones");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.RequisitfiDeclaraciones[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "RequisitfiDeclaraciones");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_SobreElectronico");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.SobreElectronico[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SobreElectronico");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_SolvenciaEconomica");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.SolvenciaEconomica[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SolvenciaEconomica");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns1_SolvenciaTecnica");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.client.beans.SolvenciaTecnica[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SolvenciaTecnica");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns2_Anuncio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.Anuncio[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Anuncio");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns2_Mensaje");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.Mensaje[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Mensaje");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_tns2_OfficialPublicationResult");
            cachedSerQNames.add(qName);
            cls = es.dipucr.contratacion.resultadoBeans.OfficialPublicationResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "OfficialPublicationResult");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "ArrayOf_xsd_string");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "BinaryObjectType");
            cachedSerQNames.add(qName);
            cls = org.oasis.ubl.common.udt.BinaryObjectType.class;
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

    public es.dipucr.contratacion.resultadoBeans.Resultado anuncioAdjudicacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioAdjudicacionBean anuncioAdjudicacion, java.lang.String publishedByUser) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "anuncioAdjudicacion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidad, anuncioAdjudicacion, publishedByUser});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.contratacion.resultadoBeans.Resultado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.contratacion.resultadoBeans.Resultado consultarDatosAlta(java.lang.String entidad, java.lang.String numexp, java.lang.String publishedByUser) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "consultarDatosAlta"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidad, numexp, publishedByUser});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.contratacion.resultadoBeans.Resultado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.contratacion.resultadoBeans.Resultado envioOtrosDocumentosLicitacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.Documento documentoAdicional, java.lang.String publishedByUser) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "envioOtrosDocumentosLicitacion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidad, documentoAdicional, publishedByUser});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.contratacion.resultadoBeans.Resultado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.contratacion.resultadoBeans.Resultado envioAnalisisPrevio(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioPrevioBean analisisPrevio, java.lang.String publishedByUser) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "envioAnalisisPrevio"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidad, analisisPrevio, publishedByUser});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.contratacion.resultadoBeans.Resultado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.contratacion.resultadoBeans.Resultado envioPliegos(java.lang.String entidad, es.dipucr.contratacion.client.beans.PliegoBean pliego, java.lang.String publishedByUser) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "envioPliegos"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidad, pliego, publishedByUser});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.contratacion.resultadoBeans.Resultado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.contratacion.resultadoBeans.Resultado estadoExpediente(java.lang.String entidad, java.lang.String numexp, java.lang.String publishedByUser) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "estadoExpediente"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidad, numexp, publishedByUser});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.contratacion.resultadoBeans.Resultado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.contratacion.resultadoBeans.Resultado envioPublicacionAnuncioLicitacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioLicitacionBean anuncioLicitacion, java.lang.String publishedByUser) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "envioPublicacionAnuncioLicitacion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidad, anuncioLicitacion, publishedByUser});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.contratacion.resultadoBeans.Resultado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.contratacion.resultadoBeans.Resultado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}

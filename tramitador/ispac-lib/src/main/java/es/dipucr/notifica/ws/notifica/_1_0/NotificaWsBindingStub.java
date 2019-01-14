/**
 * NotificaWsBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

import java.rmi.RemoteException;

import javax.xml.soap.SOAPException;

import es.dipucr.notifica.commons.FuncionesComunesNotifica;

public class NotificaWsBindingStub extends org.apache.axis.client.Stub implements es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortType {
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
        oper.setName("altaEnvio");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "envio_type"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_envio"), es.dipucr.notifica.ws.notifica._1_0.Tipo_envio.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_alta"));
        oper.setReturnClass(es.dipucr.notifica.ws.notifica._1_0.Resultado_alta.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("consultaDatadoEnvio");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identificador_envio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_datado"));
        oper.setReturnClass(es.dipucr.notifica.ws.notifica._1_0.Resultado_datado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("consultaCertificacionEnvio");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identificador_envio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_certificacion"));
        oper.setReturnClass(es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("consultaEstado");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "identificador_envio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_estado"));
        oper.setReturnClass(es.dipucr.notifica.ws.notifica._1_0.Resultado_estado.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("infoEnvio");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "info_envio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "info_envio"), es.dipucr.notifica.ws.notifica._1_0.Info_envio.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultadoInfoEnvio"));
        oper.setReturnClass(es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("consultaCies");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "consulta_cies"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "consulta_cies"), es.dipucr.notifica.ws.notifica._1_0.Consulta_cies.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultadoGetCies"));
        oper.setReturnClass(es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("consultaOrganismosActivos");
        oper.setReturnType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_organismos_activos"));
        oper.setReturnClass(es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

    }

	public NotificaWsBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public NotificaWsBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public NotificaWsBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfCie");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.ArrayOfCie.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfIdentificador_envio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.ArrayOfIdentificador_envio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfTipo_destinatario");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipo_destinatario.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfTipoIntento");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipoIntento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfTipoOrganismoEmisor");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipoOrganismoEmisor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "certificacion_envio_respuesta");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Certificacion_envio_respuesta.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "cie");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Cie.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "consulta_cies");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Consulta_cies.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "datado_envio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Datado_envio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "direccion_electronica_habilitada");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Direccion_electronica_habilitada.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "documento");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Documento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "estado_respuesta");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Estado_respuesta.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "identificador_envio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Identificador_envio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "info_envio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Info_envio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "opciones_emision");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Opciones_emision.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_alta");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Resultado_alta.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_certificacion");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_datado");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Resultado_datado.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_estado");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Resultado_estado.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_organismos_activos");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultadoGetCies");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultadoInfoEnvio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_destinatario");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Tipo_destinatario.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_domicilio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Tipo_domicilio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_envio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Tipo_envio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_municipio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Tipo_municipio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_pais");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Tipo_pais.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_persona_destinatario");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_procedimiento");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Tipo_procedimiento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_provincia");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.Tipo_provincia.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoIntento");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.TipoIntento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoEmisor");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoEmisor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoPagadorCIE");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCIE.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoPagadorCorreos");
            cachedSerQNames.add(qName);
            cls = es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCorreos.class;
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

    public es.dipucr.notifica.ws.notifica._1_0.Resultado_alta altaEnvio(es.dipucr.notifica.ws.notifica._1_0.Tipo_envio envio_type, String apiKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/altaEnvio");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "altaEnvio"));
        

        setRequestHeaders(_call);
        setAttachments(_call);
 try {   
	 //FIRMA DE PETICION 
	 //FuncionesComunesNotifica.firmarPeticion(_call);
	 try {
		_call.addHeader(FuncionesComunesNotifica.dameApiKeySoapHeaderPorParametro(apiKey));
	} catch (SOAPException e) {
		throw new RemoteException(e.getCause()+e.getMessage());
	}
	 java.lang.Object _resp = _call.invoke(new java.lang.Object[] {envio_type});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_alta) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_alta) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.notifica.ws.notifica._1_0.Resultado_alta.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
	  FuncionesComunesNotifica.imprimirErrorEnvio(_call);
	  throw axisFaultException;
  
}
    }

    public es.dipucr.notifica.ws.notifica._1_0.Resultado_datado consultaDatadoEnvio(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/consultaDatadoEnvio");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "consultaDatadoEnvio"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        
	 //FIRMA DE PETICION
	 //FuncionesComunesNotifica.firmarPeticion(_call);
	 try {
		_call.addHeader(FuncionesComunesNotifica.dameApiKeySoapHeaderPorParametro(apiKey));
	 } catch (SOAPException e) {
			throw new RemoteException(e.getCause()+e.getMessage());
	}
	 java.lang.Object _resp = _call.invoke(new java.lang.Object[] {identificador_envio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_datado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_datado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.notifica.ws.notifica._1_0.Resultado_datado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion consultaCertificacionEnvio(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/consultaCertificacionEnvio");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "consultaCertificacionEnvio"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        
	 //FIRMA DE PETICION
	 //FuncionesComunesNotifica.firmarPeticion(_call);
	 try {
		_call.addHeader(FuncionesComunesNotifica.dameApiKeySoapHeaderPorParametro(apiKey));
	 } catch (SOAPException e) {
			throw new RemoteException(e.getCause()+e.getMessage());
	 }
	 java.lang.Object _resp = _call.invoke(new java.lang.Object[] {identificador_envio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.notifica.ws.notifica._1_0.Resultado_estado consultaEstado(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/consultaEstado");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "consultaEstado"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        
	 	//FIRMA DE PETICION
		//FuncionesComunesNotifica.firmarPeticion(_call);
	    try {
			_call.addHeader(FuncionesComunesNotifica.dameApiKeySoapHeaderPorParametro(apiKey));
	    } catch (SOAPException e) {
			throw new RemoteException(e.getCause()+e.getMessage());
		}
	    java.lang.Object _resp = _call.invoke(new java.lang.Object[] {identificador_envio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_estado) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_estado) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.notifica.ws.notifica._1_0.Resultado_estado.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio infoEnvio(es.dipucr.notifica.ws.notifica._1_0.Info_envio info_envio, String apiKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/infoEnvio");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "infoEnvio"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        
	 
	 	//FIRMA DE PETICION
		//FuncionesComunesNotifica.firmarPeticion(_call);
	    try {
			_call.addHeader(FuncionesComunesNotifica.dameApiKeySoapHeaderPorParametro(apiKey));
	    } catch (SOAPException e) {
			throw new RemoteException(e.getCause()+e.getMessage());
		}
	    java.lang.Object _resp = _call.invoke(new java.lang.Object[] {info_envio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies consultaCies(es.dipucr.notifica.ws.notifica._1_0.Consulta_cies consulta_cies, String apiKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/consultaCies");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "consultaCies"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        
	 	
	 	//FIRMA DE PETICION
		//FuncionesComunesNotifica.firmarPeticion(_call);
	    try {
			_call.addHeader(FuncionesComunesNotifica.dameApiKeySoapHeader(apiKey));
	    } catch (SOAPException e) {
			throw new RemoteException(e.getCause()+e.getMessage());
		}
	    java.lang.Object _resp = _call.invoke(new java.lang.Object[] {consulta_cies});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos consultaOrganismosActivos(String apiKey) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/consultaOrganismosActivos");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "consultaOrganismosActivos"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        
	 
	 //FIRMA DE PETICION
	 //FuncionesComunesNotifica.firmarPeticion(_call);
	 try {
		_call.addHeader(FuncionesComunesNotifica.dameApiKeySoapHeaderPorParametro(apiKey));
	 } catch (SOAPException e) {
			throw new RemoteException(e.getCause()+e.getMessage());
		}
	 java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }
    
    
    

}

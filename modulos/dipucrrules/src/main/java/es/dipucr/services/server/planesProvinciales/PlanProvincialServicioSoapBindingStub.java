/**
 * PlanProvincialServicioSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.services.server.planesProvinciales;

public class PlanProvincialServicioSoapBindingStub extends org.apache.axis.client.Stub implements es.dipucr.services.server.planesProvinciales.PlanProvincialServicio {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[13];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPlanProvincial");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "nombreMunicipio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "PlanProvincial"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.PlanProvincial[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getPlanProvincialReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPlanesProvincialesByAnio");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "PlanProvincial"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.PlanProvincial[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getPlanesProvincialesByAnioReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAllMunicipios");
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "MunicipioPlanesProvinciales"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getAllMunicipiosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getResumenPlanCooperacion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumePlanProvincial"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.ResumePlanProvincial[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getResumenPlanCooperacionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTotalResumenPlanCooperacion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumePlanProvincial"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.ResumePlanProvincial[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalResumenPlanCooperacionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getResumenPlanComplementario");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumePlanProvincial"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.ResumePlanProvincial[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getResumenPlanComplementarioReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTotalResumenPlanComplementario");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumePlanProvincial"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.ResumePlanProvincial[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalResumenPlanComplementarioReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getResumenPlanPorAyuntamiento");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "ianio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumenPlanPorAyuntamiento"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getResumenPlanPorAyuntamientoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTotalResumenPlanPorAyuntamiento");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "ianio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumenPlanPorAyuntamiento"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalResumenPlanPorAyuntamientoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getCuadroA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "DatosCuadroMinisterio"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getCuadroAReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTotalesCuadroA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "DatosCuadroMinisterio"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalesCuadroAReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getCuadroC");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "DatosCuadroMinisterio"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getCuadroCReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTotalesCuadroC");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "anio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "DatosCuadroMinisterio"));
        oper.setReturnClass(es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalesCuadroCReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

    }

    public PlanProvincialServicioSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PlanProvincialServicioSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PlanProvincialServicioSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "DatosCuadroMinisterio");
            cachedSerQNames.add(qName);
            cls = es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "MunicipioPlanesProvinciales");
            cachedSerQNames.add(qName);
            cls = es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "PlanProvincial");
            cachedSerQNames.add(qName);
            cls = es.dipucr.domain.planesProvinciales.PlanProvincial.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumenPlanPorAyuntamiento");
            cachedSerQNames.add(qName);
            cls = es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumePlanProvincial");
            cachedSerQNames.add(qName);
            cls = es.dipucr.domain.planesProvinciales.ResumePlanProvincial.class;
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

    public es.dipucr.domain.planesProvinciales.PlanProvincial[] getPlanProvincial(java.lang.String nombreMunicipio, java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getPlanProvincial"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nombreMunicipio, anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.PlanProvincial[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.PlanProvincial[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.PlanProvincial[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.PlanProvincial[] getPlanesProvincialesByAnio(java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getPlanesProvincialesByAnio"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.PlanProvincial[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.PlanProvincial[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.PlanProvincial[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales[] getAllMunicipios() throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getAllMunicipios"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getResumenPlanCooperacion(java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getResumenPlanCooperacion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.ResumePlanProvincial[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.ResumePlanProvincial[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.ResumePlanProvincial[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getTotalResumenPlanCooperacion(java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalResumenPlanCooperacion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.ResumePlanProvincial[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.ResumePlanProvincial[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.ResumePlanProvincial[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getResumenPlanComplementario(java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getResumenPlanComplementario"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.ResumePlanProvincial[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.ResumePlanProvincial[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.ResumePlanProvincial[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getTotalResumenPlanComplementario(java.lang.String anio) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalResumenPlanComplementario"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {anio});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.ResumePlanProvincial[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.ResumePlanProvincial[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.ResumePlanProvincial[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[] getResumenPlanPorAyuntamiento(int ianio) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getResumenPlanPorAyuntamiento"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(ianio)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[] getTotalResumenPlanPorAyuntamiento(int ianio) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalResumenPlanPorAyuntamiento"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(ianio)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getCuadroA(int anio) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getCuadroA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(anio)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getTotalesCuadroA(int anio) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalesCuadroA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(anio)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getCuadroC(int anio) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getCuadroC"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(anio)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getTotalesCuadroC(int anio) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getTotalesCuadroC"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(anio)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}

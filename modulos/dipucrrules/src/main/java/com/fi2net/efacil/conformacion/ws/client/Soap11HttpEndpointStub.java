/*
 * Plan Avanza Local Soluciones – e-fácil – Copyright © 2011 – Ministerio de Industria, Turismo y 
 * Comercio.
 *
 * Este programa es software libre, por ello está permitido redistribuirlo y/o modificarlo bajo los 
 * términos de la GNU General Public License, en su versión 3, publicada por la Free Software 
 * Foundation.
 *
 * Junto con este programa debe haber recibido una copia de la GNU General Public License, en 
 * caso contrario puede consultarla en <http://www.gnu.org/licenses/>.
 *
 * El presente programa posee las siguientes cláusulas particulares de licencia GPL v3:
 *
 *  - Queda restringido su uso a Administraciones Públicas, en el ámbito de sus 
 * competencias legalmente establecidas y con la finalidad de utilidad pública e interés 
 * social;
 *  - La distribución y el uso del software tienen carácter gratuito;
 *  - Ni el software ni ninguno de los Módulos que lo componen serán usados ni 
 * sublicenciados, bajo ninguna circunstancia, con fines o intereses comerciales, bien sea 
 * directa o indirectamente, por parte de la Administración Pública, sus funcionarios, 
 * empleados o cualquier otra persona u organización dentro o fuera de la 
 * administración.
 *
 */

/**
 * Soap11HttpEndpointStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.fi2net.efacil.conformacion.ws.client;

public class Soap11HttpEndpointStub extends org.apache.axis.client.Stub implements com.fi2net.efacil.conformacion.ws.client.IEFacilConformacionFacturasService {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ConformarFactura");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "conformacionFacturaRequestData"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "ConformarFacturaRequestData"), com.fi2net.efacil.conformacion.ws.client.ConformarFacturaRequestData.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "ConformarFacturaResponseData"));
        oper.setReturnClass(com.fi2net.efacil.conformacion.ws.client.ConformarFacturaResponseData.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "conformacionFacturaResponseData"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

    }

    public Soap11HttpEndpointStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public Soap11HttpEndpointStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public Soap11HttpEndpointStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "CodUsuario");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "ConformarFacturaRequestData");
            cachedSerQNames.add(qName);
            cls = com.fi2net.efacil.conformacion.ws.client.ConformarFacturaRequestData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "ConformarFacturaResponseData");
            cachedSerQNames.add(qName);
            cls = com.fi2net.efacil.conformacion.ws.client.ConformarFacturaResponseData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "InvoiceIdentificator");
            cachedSerQNames.add(qName);
            cls = com.fi2net.efacil.conformacion.ws.client.InvoiceIdentificator.class;
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

    public com.fi2net.efacil.conformacion.ws.client.ConformarFacturaResponseData conformarFactura(com.fi2net.efacil.conformacion.ws.client.ConformarFacturaRequestData conformacionFacturaRequestData) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://mityc.es/eFacil/ConformacionFactura/1.0/IEFacilConformacionFacturasService/ConformarFactura");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "ConformarFactura"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {conformacionFacturaRequestData});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.fi2net.efacil.conformacion.ws.client.ConformarFacturaResponseData) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.fi2net.efacil.conformacion.ws.client.ConformarFacturaResponseData) org.apache.axis.utils.JavaUtils.convert(_resp, com.fi2net.efacil.conformacion.ws.client.ConformarFacturaResponseData.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}

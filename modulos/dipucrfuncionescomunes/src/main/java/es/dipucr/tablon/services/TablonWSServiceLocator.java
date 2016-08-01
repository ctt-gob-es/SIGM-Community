/**
 * TablonWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.tablon.services;

public class TablonWSServiceLocator extends org.apache.axis.client.Service implements es.dipucr.tablon.services.TablonWSService {

    public TablonWSServiceLocator() {
    }


    public TablonWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TablonWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TablonWS
    private java.lang.String TablonWS_address = WSTablonProperties.getURL() + "TablonWS";

    public java.lang.String getTablonWSAddress() {
        return TablonWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TablonWSWSDDServiceName = "TablonWS";

    public java.lang.String getTablonWSWSDDServiceName() {
        return TablonWSWSDDServiceName;
    }

    public void setTablonWSWSDDServiceName(java.lang.String name) {
        TablonWSWSDDServiceName = name;
    }

    public es.dipucr.tablon.services.TablonWS getTablonWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TablonWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTablonWS(endpoint);
    }

    public es.dipucr.tablon.services.TablonWS getTablonWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.tablon.services.TablonWSSoapBindingStub _stub = new es.dipucr.tablon.services.TablonWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getTablonWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTablonWSEndpointAddress(java.lang.String address) {
        TablonWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.tablon.services.TablonWS.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.tablon.services.TablonWSSoapBindingStub _stub = new es.dipucr.tablon.services.TablonWSSoapBindingStub(new java.net.URL(TablonWS_address), this);
                _stub.setPortName(getTablonWSWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TablonWS".equals(inputPortName)) {
            return getTablonWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://services.tablon.dipucr.es", "TablonWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://services.tablon.dipucr.es", "TablonWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TablonWS".equals(portName)) {
            setTablonWSEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

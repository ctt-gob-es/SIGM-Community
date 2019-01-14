/**
 * ScspServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.verifdatos.services;

import es.dipucr.svd.services.ServiciosWebSVDFunciones;

public class ScspServiceLocator extends org.apache.axis.client.Service implements es.dipucr.verifdatos.services.ScspService {

    public ScspServiceLocator() {
    }


    public ScspServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ScspServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Scsp
    private java.lang.String Scsp_address  = ServiciosWebSVDFunciones.getDireccionClienteLigeroSW();

    public java.lang.String getScspAddress() {
        return Scsp_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ScspWSDDServiceName = "Scsp";

    public java.lang.String getScspWSDDServiceName() {
        return ScspWSDDServiceName;
    }

    public void setScspWSDDServiceName(java.lang.String name) {
        ScspWSDDServiceName = name;
    }

    public es.dipucr.verifdatos.services.Scsp getScsp() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Scsp_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getScsp(endpoint);
    }

    public es.dipucr.verifdatos.services.Scsp getScsp(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.verifdatos.services.ScspSoapBindingStub _stub = new es.dipucr.verifdatos.services.ScspSoapBindingStub(portAddress, this);
            _stub.setPortName(getScspWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setScspEndpointAddress(java.lang.String address) {
        Scsp_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.verifdatos.services.Scsp.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.verifdatos.services.ScspSoapBindingStub _stub = new es.dipucr.verifdatos.services.ScspSoapBindingStub(new java.net.URL(Scsp_address), this);
                _stub.setPortName(getScspWSDDServiceName());
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
        if ("Scsp".equals(inputPortName)) {
            return getScsp();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://services.verifdatos.dipucr.es", "ScspService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://services.verifdatos.dipucr.es", "Scsp"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Scsp".equals(portName)) {
            setScspEndpointAddress(address);
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

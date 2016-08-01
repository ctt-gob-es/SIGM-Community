/**
 * FacturaWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.factura.services.factura;

public class FacturaWSServiceLocator extends org.apache.axis.client.Service implements es.dipucr.factura.services.factura.FacturaWSService {

    public FacturaWSServiceLocator() {
    }


    public FacturaWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FacturaWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FacturaWS
    private java.lang.String FacturaWS_address = WSFacturaProperties.getURL() + "FacturaWS";

    public java.lang.String getFacturaWSAddress() {
        return FacturaWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FacturaWSWSDDServiceName = "FacturaWS";

    public java.lang.String getFacturaWSWSDDServiceName() {
        return FacturaWSWSDDServiceName;
    }

    public void setFacturaWSWSDDServiceName(java.lang.String name) {
        FacturaWSWSDDServiceName = name;
    }

    public es.dipucr.factura.services.factura.FacturaWS getFacturaWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FacturaWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFacturaWS(endpoint);
    }

    public es.dipucr.factura.services.factura.FacturaWS getFacturaWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.factura.services.factura.FacturaWSSoapBindingStub _stub = new es.dipucr.factura.services.factura.FacturaWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getFacturaWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFacturaWSEndpointAddress(java.lang.String address) {
        FacturaWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.factura.services.factura.FacturaWS.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.factura.services.factura.FacturaWSSoapBindingStub _stub = new es.dipucr.factura.services.factura.FacturaWSSoapBindingStub(new java.net.URL(FacturaWS_address), this);
                _stub.setPortName(getFacturaWSWSDDServiceName());
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
        if ("FacturaWS".equals(inputPortName)) {
            return getFacturaWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://factura.services.factura.dipucr.es", "FacturaWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://factura.services.factura.dipucr.es", "FacturaWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FacturaWS".equals(portName)) {
            setFacturaWSEndpointAddress(address);
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

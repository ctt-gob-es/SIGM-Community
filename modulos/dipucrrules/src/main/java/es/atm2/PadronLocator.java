/**
 * PadronLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class PadronLocator extends org.apache.axis.client.Service implements es.atm2.Padron {

    public PadronLocator() {
    }


    public PadronLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PadronLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PadronSoap
    private java.lang.String PadronSoap_address = "https://padron.dipucr.es/conectores/Padron.asmx";

    public java.lang.String getPadronSoapAddress() {
        return PadronSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PadronSoapWSDDServiceName = "PadronSoap";

    public java.lang.String getPadronSoapWSDDServiceName() {
        return PadronSoapWSDDServiceName;
    }

    public void setPadronSoapWSDDServiceName(java.lang.String name) {
        PadronSoapWSDDServiceName = name;
    }

    public es.atm2.PadronSoap getPadronSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PadronSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPadronSoap(endpoint);
    }

    public es.atm2.PadronSoap getPadronSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.atm2.PadronSoapStub _stub = new es.atm2.PadronSoapStub(portAddress, this);
            _stub.setPortName(getPadronSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPadronSoapEndpointAddress(java.lang.String address) {
        PadronSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.atm2.PadronSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                es.atm2.PadronSoapStub _stub = new es.atm2.PadronSoapStub(new java.net.URL(PadronSoap_address), this);
                _stub.setPortName(getPadronSoapWSDDServiceName());
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
        if ("PadronSoap".equals(inputPortName)) {
            return getPadronSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://atm2.es/", "Padron");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://atm2.es/", "PadronSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PadronSoap".equals(portName)) {
            setPadronSoapEndpointAddress(address);
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

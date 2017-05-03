/**
 * TramitacionWebServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class TramitacionWebServiceServiceLocator extends org.apache.axis.client.Service implements ieci.tecdoc.sgm.tram.ws.server.TramitacionWebServiceService {

    public TramitacionWebServiceServiceLocator() {
    }


    public TramitacionWebServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TramitacionWebServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TramitacionWebService
    private java.lang.String TramitacionWebService_address = "";

    public java.lang.String getTramitacionWebServiceAddress() {
        return TramitacionWebService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TramitacionWebServiceWSDDServiceName = "TramitacionWebService";

    public java.lang.String getTramitacionWebServiceWSDDServiceName() {
        return TramitacionWebServiceWSDDServiceName;
    }

    public void setTramitacionWebServiceWSDDServiceName(java.lang.String name) {
        TramitacionWebServiceWSDDServiceName = name;
    }

    public ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService getTramitacionWebService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TramitacionWebService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTramitacionWebService(endpoint);
    }

    public ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService getTramitacionWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ieci.tecdoc.sgm.tram.ws.server.TramitacionWebServiceSoapBindingStub _stub = new ieci.tecdoc.sgm.tram.ws.server.TramitacionWebServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTramitacionWebServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTramitacionWebServiceEndpointAddress(java.lang.String address) {
        TramitacionWebService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                ieci.tecdoc.sgm.tram.ws.server.TramitacionWebServiceSoapBindingStub _stub = new ieci.tecdoc.sgm.tram.ws.server.TramitacionWebServiceSoapBindingStub(new java.net.URL(TramitacionWebService_address), this);
                _stub.setPortName(getTramitacionWebServiceWSDDServiceName());
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
        if ("TramitacionWebService".equals(inputPortName)) {
            return getTramitacionWebService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "TramitacionWebServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "TramitacionWebService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TramitacionWebService".equals(portName)) {
            setTramitacionWebServiceEndpointAddress(address);
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

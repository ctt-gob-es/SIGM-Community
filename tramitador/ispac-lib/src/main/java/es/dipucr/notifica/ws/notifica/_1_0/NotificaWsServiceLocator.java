/**
 * NotificaWsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class NotificaWsServiceLocator extends org.apache.axis.client.Service implements es.dipucr.notifica.ws.notifica._1_0.NotificaWsService {

    public NotificaWsServiceLocator() {
    }


    public NotificaWsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NotificaWsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NotificaWsPort
    private java.lang.String NotificaWsPort_address = "https://se-notificaws.redsara.es/ws/soap/NotificaWs";

    public java.lang.String getNotificaWsPortAddress() {
        return NotificaWsPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NotificaWsPortWSDDServiceName = "NotificaWsPort";

    public java.lang.String getNotificaWsPortWSDDServiceName() {
        return NotificaWsPortWSDDServiceName;
    }

    public void setNotificaWsPortWSDDServiceName(java.lang.String name) {
        NotificaWsPortWSDDServiceName = name;
    }

    public es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortType getNotificaWsPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NotificaWsPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNotificaWsPort(endpoint);
    }

    public es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortType getNotificaWsPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.notifica.ws.notifica._1_0.NotificaWsBindingStub _stub = new es.dipucr.notifica.ws.notifica._1_0.NotificaWsBindingStub(portAddress, this);
            _stub.setPortName(getNotificaWsPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNotificaWsPortEndpointAddress(java.lang.String address) {
        NotificaWsPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.notifica.ws.notifica._1_0.NotificaWsBindingStub _stub = new es.dipucr.notifica.ws.notifica._1_0.NotificaWsBindingStub(new java.net.URL(NotificaWsPort_address), this);
                _stub.setPortName(getNotificaWsPortWSDDServiceName());
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
        if ("NotificaWsPort".equals(inputPortName)) {
            return getNotificaWsPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "NotificaWsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "NotificaWsPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NotificaWsPort".equals(portName)) {
            setNotificaWsPortEndpointAddress(address);
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

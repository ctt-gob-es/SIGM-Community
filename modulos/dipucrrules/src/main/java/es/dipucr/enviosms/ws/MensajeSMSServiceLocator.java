/**
 * MensajeSMSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.enviosms.ws;

public class MensajeSMSServiceLocator extends org.apache.axis.client.Service implements es.dipucr.enviosms.ws.MensajeSMSService {

    public MensajeSMSServiceLocator() {
    }


    public MensajeSMSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MensajeSMSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MensajeSMS
    private java.lang.String MensajeSMS_address = "http://10.12.200.35:8090/ServidorSmsWS/services/MensajeSMS";

    public java.lang.String getMensajeSMSAddress() {
        return MensajeSMS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MensajeSMSWSDDServiceName = "MensajeSMS";

    public java.lang.String getMensajeSMSWSDDServiceName() {
        return MensajeSMSWSDDServiceName;
    }

    public void setMensajeSMSWSDDServiceName(java.lang.String name) {
        MensajeSMSWSDDServiceName = name;
    }

    public es.dipucr.enviosms.ws.MensajeSMS getMensajeSMS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MensajeSMS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMensajeSMS(endpoint);
    }

    public es.dipucr.enviosms.ws.MensajeSMS getMensajeSMS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.enviosms.ws.MensajeSMSSoapBindingStub _stub = new es.dipucr.enviosms.ws.MensajeSMSSoapBindingStub(portAddress, this);
            _stub.setPortName(getMensajeSMSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMensajeSMSEndpointAddress(java.lang.String address) {
        MensajeSMS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.enviosms.ws.MensajeSMS.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.enviosms.ws.MensajeSMSSoapBindingStub _stub = new es.dipucr.enviosms.ws.MensajeSMSSoapBindingStub(new java.net.URL(MensajeSMS_address), this);
                _stub.setPortName(getMensajeSMSWSDDServiceName());
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
        if ("MensajeSMS".equals(inputPortName)) {
            return getMensajeSMS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.enviosms.dipucr.es", "MensajeSMSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.enviosms.dipucr.es", "MensajeSMS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MensajeSMS".equals(portName)) {
            setMensajeSMSEndpointAddress(address);
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

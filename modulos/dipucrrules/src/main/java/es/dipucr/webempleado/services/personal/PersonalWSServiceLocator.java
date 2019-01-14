/**
 * PersonalWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.personal;

import es.dipucr.webempleado.services.WSEmpleadoProperties;

public class PersonalWSServiceLocator extends org.apache.axis.client.Service implements es.dipucr.webempleado.services.personal.PersonalWSService {

    public PersonalWSServiceLocator() {
    }


    public PersonalWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PersonalWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PersonalWS
    private java.lang.String PersonalWS_address = WSEmpleadoProperties.getURL() + "PersonalWS";

    public java.lang.String getPersonalWSAddress() {
        return PersonalWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PersonalWSWSDDServiceName = "PersonalWS";

    public java.lang.String getPersonalWSWSDDServiceName() {
        return PersonalWSWSDDServiceName;
    }

    public void setPersonalWSWSDDServiceName(java.lang.String name) {
        PersonalWSWSDDServiceName = name;
    }

    public es.dipucr.webempleado.services.personal.PersonalWS getPersonalWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PersonalWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPersonalWS(endpoint);
    }

    public es.dipucr.webempleado.services.personal.PersonalWS getPersonalWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.webempleado.services.personal.PersonalWSSoapBindingStub _stub = new es.dipucr.webempleado.services.personal.PersonalWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getPersonalWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPersonalWSEndpointAddress(java.lang.String address) {
        PersonalWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.webempleado.services.personal.PersonalWS.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.webempleado.services.personal.PersonalWSSoapBindingStub _stub = new es.dipucr.webempleado.services.personal.PersonalWSSoapBindingStub(new java.net.URL(PersonalWS_address), this);
                _stub.setPortName(getPersonalWSWSDDServiceName());
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
        if ("PersonalWS".equals(inputPortName)) {
            return getPersonalWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "PersonalWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://personal.services.webempleado.dipucr.es", "PersonalWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PersonalWS".equals(portName)) {
            setPersonalWSEndpointAddress(address);
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

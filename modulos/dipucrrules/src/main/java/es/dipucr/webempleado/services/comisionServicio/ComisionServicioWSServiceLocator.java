/**
 * ComisionServicioWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.comisionServicio;

import es.dipucr.webempleado.services.WSEmpleadoProperties;

public class ComisionServicioWSServiceLocator extends org.apache.axis.client.Service implements es.dipucr.webempleado.services.comisionServicio.ComisionServicioWSService {

    public ComisionServicioWSServiceLocator() {
    }


    public ComisionServicioWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ComisionServicioWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ComisionServicioWS
    private java.lang.String ComisionServicioWS_address = WSEmpleadoProperties.getURL() + "ComisionServicioWS";

    public java.lang.String getComisionServicioWSAddress() {
        return ComisionServicioWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ComisionServicioWSWSDDServiceName = "ComisionServicioWS";

    public java.lang.String getComisionServicioWSWSDDServiceName() {
        return ComisionServicioWSWSDDServiceName;
    }

    public void setComisionServicioWSWSDDServiceName(java.lang.String name) {
        ComisionServicioWSWSDDServiceName = name;
    }

    public es.dipucr.webempleado.services.comisionServicio.ComisionServicioWS getComisionServicioWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ComisionServicioWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getComisionServicioWS(endpoint);
    }

    public es.dipucr.webempleado.services.comisionServicio.ComisionServicioWS getComisionServicioWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.webempleado.services.comisionServicio.ComisionServicioWSSoapBindingStub _stub = new es.dipucr.webempleado.services.comisionServicio.ComisionServicioWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getComisionServicioWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setComisionServicioWSEndpointAddress(java.lang.String address) {
        ComisionServicioWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.webempleado.services.comisionServicio.ComisionServicioWS.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.webempleado.services.comisionServicio.ComisionServicioWSSoapBindingStub _stub = new es.dipucr.webempleado.services.comisionServicio.ComisionServicioWSSoapBindingStub(new java.net.URL(ComisionServicioWS_address), this);
                _stub.setPortName(getComisionServicioWSWSDDServiceName());
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
        if ("ComisionServicioWS".equals(inputPortName)) {
            return getComisionServicioWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://comisionServicio.services.webempleado.dipucr.es", "ComisionServicioWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://comisionServicio.services.webempleado.dipucr.es", "ComisionServicioWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ComisionServicioWS".equals(portName)) {
            setComisionServicioWSEndpointAddress(address);
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
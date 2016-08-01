/**
 * PlataformaContratacionServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.services;

import es.dipucr.contratacion.common.ServiciosWebContratacionFunciones;

public class PlataformaContratacionServiceLocator extends org.apache.axis.client.Service implements es.dipucr.contratacion.services.PlataformaContratacionService {

    public PlataformaContratacionServiceLocator() {
    }


    public PlataformaContratacionServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PlataformaContratacionServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PlataformaContratacion
    private java.lang.String PlataformaContratacion_address = ServiciosWebContratacionFunciones.getDireccionSW();

    public java.lang.String getPlataformaContratacionAddress() {
        return PlataformaContratacion_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PlataformaContratacionWSDDServiceName = "PlataformaContratacion";

    public java.lang.String getPlataformaContratacionWSDDServiceName() {
        return PlataformaContratacionWSDDServiceName;
    }

    public void setPlataformaContratacionWSDDServiceName(java.lang.String name) {
        PlataformaContratacionWSDDServiceName = name;
    }

    public es.dipucr.contratacion.services.PlataformaContratacion getPlataformaContratacion() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PlataformaContratacion_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPlataformaContratacion(endpoint);
    }

    public es.dipucr.contratacion.services.PlataformaContratacion getPlataformaContratacion(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.contratacion.services.PlataformaContratacionSoapBindingStub _stub = new es.dipucr.contratacion.services.PlataformaContratacionSoapBindingStub(portAddress, this);
            _stub.setPortName(getPlataformaContratacionWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPlataformaContratacionEndpointAddress(java.lang.String address) {
        PlataformaContratacion_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.contratacion.services.PlataformaContratacion.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.contratacion.services.PlataformaContratacionSoapBindingStub _stub = new es.dipucr.contratacion.services.PlataformaContratacionSoapBindingStub(new java.net.URL(PlataformaContratacion_address), this);
                _stub.setPortName(getPlataformaContratacionWSDDServiceName());
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
        if ("PlataformaContratacion".equals(inputPortName)) {
            return getPlataformaContratacion();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "PlataformaContratacionService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "PlataformaContratacion"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PlataformaContratacion".equals(portName)) {
            setPlataformaContratacionEndpointAddress(address);
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

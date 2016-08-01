/**
 * CumplimientoTrieniosWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.CumplimientoTrienios;

import es.dipucr.webempleado.services.WSEmpleadoProperties;

public class CumplimientoTrieniosWSServiceLocator extends org.apache.axis.client.Service implements CumplimientoTrieniosWSService {

    public CumplimientoTrieniosWSServiceLocator() {
    }


    public CumplimientoTrieniosWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CumplimientoTrieniosWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CumplimientoTrieniosService
    private java.lang.String CumplimientoTrieniosService_address = WSEmpleadoProperties.getURL() + "CumplimientoTrieniosService";

    public java.lang.String getCumplimientoTrieniosServiceAddress() {
        return CumplimientoTrieniosService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CumplimientoTrieniosServiceWSDDServiceName = "CumplimientoTrieniosService";

    public java.lang.String getCumplimientoTrieniosServiceWSDDServiceName() {
        return CumplimientoTrieniosServiceWSDDServiceName;
    }

    public void setCumplimientoTrieniosServiceWSDDServiceName(java.lang.String name) {
        CumplimientoTrieniosServiceWSDDServiceName = name;
    }

    public CumplimientoTrieniosService getCumplimientoTrieniosService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CumplimientoTrieniosService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCumplimientoTrieniosService(endpoint);
    }

    public CumplimientoTrieniosService getCumplimientoTrieniosService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            CumplimientoTrieniosServiceSoapBindingStub _stub = new CumplimientoTrieniosServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCumplimientoTrieniosServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCumplimientoTrieniosServiceEndpointAddress(java.lang.String address) {
        CumplimientoTrieniosService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (CumplimientoTrieniosService.class.isAssignableFrom(serviceEndpointInterface)) {
                CumplimientoTrieniosServiceSoapBindingStub _stub = new CumplimientoTrieniosServiceSoapBindingStub(new java.net.URL(CumplimientoTrieniosService_address), this);
                _stub.setPortName(getCumplimientoTrieniosServiceWSDDServiceName());
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
        if ("CumplimientoTrieniosService".equals(inputPortName)) {
            return getCumplimientoTrieniosService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://trienios.services.webempleado.dipucr.es", "CumplimientoTrieniosWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://trienios.services.webempleado.dipucr.es", "CumplimientoTrieniosService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CumplimientoTrieniosService".equals(portName)) {
            setCumplimientoTrieniosServiceEndpointAddress(address);
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

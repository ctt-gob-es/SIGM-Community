/**
 * LicenciasAytosWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.pempleado.services.licencias;

import es.dipucr.pempleado.services.WSPEmpleadoAytosProperties;

public class LicenciasAytosWSServiceLocator extends org.apache.axis.client.Service implements es.dipucr.pempleado.services.licencias.LicenciasAytosWSService {

    public LicenciasAytosWSServiceLocator() {
    }


    public LicenciasAytosWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LicenciasAytosWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LicenciasAytosWS
    private java.lang.String LicenciasAytosWS_address = WSPEmpleadoAytosProperties.getURL() + "LicenciasAytosWS";

    public java.lang.String getLicenciasAytosWSAddress() {
        return LicenciasAytosWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LicenciasAytosWSWSDDServiceName = "LicenciasAytosWS";

    public java.lang.String getLicenciasAytosWSWSDDServiceName() {
        return LicenciasAytosWSWSDDServiceName;
    }

    public void setLicenciasAytosWSWSDDServiceName(java.lang.String name) {
        LicenciasAytosWSWSDDServiceName = name;
    }

    public es.dipucr.pempleado.services.licencias.LicenciasAytosWS getLicenciasAytosWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LicenciasAytosWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLicenciasAytosWS(endpoint);
    }

    public es.dipucr.pempleado.services.licencias.LicenciasAytosWS getLicenciasAytosWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.pempleado.services.licencias.LicenciasAytosWSSoapBindingStub _stub = new es.dipucr.pempleado.services.licencias.LicenciasAytosWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getLicenciasAytosWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLicenciasAytosWSEndpointAddress(java.lang.String address) {
        LicenciasAytosWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.pempleado.services.licencias.LicenciasAytosWS.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.pempleado.services.licencias.LicenciasAytosWSSoapBindingStub _stub = new es.dipucr.pempleado.services.licencias.LicenciasAytosWSSoapBindingStub(new java.net.URL(LicenciasAytosWS_address), this);
                _stub.setPortName(getLicenciasAytosWSWSDDServiceName());
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
        if ("LicenciasAytosWS".equals(inputPortName)) {
            return getLicenciasAytosWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://licencias.services.pempleado.dipucr.es", "LicenciasAytosWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://licencias.services.pempleado.dipucr.es", "LicenciasAytosWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LicenciasAytosWS".equals(portName)) {
            setLicenciasAytosWSEndpointAddress(address);
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

/**
 * CertificadosPersonalWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.certificadosPersonal;

import es.dipucr.webempleado.services.WSEmpleadoProperties;

public class CertificadosPersonalWSServiceLocator extends org.apache.axis.client.Service implements es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWSService {

    public CertificadosPersonalWSServiceLocator() {
    }


    public CertificadosPersonalWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CertificadosPersonalWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CertificadosPersonalWS
    private java.lang.String CertificadosPersonalWS_address = WSEmpleadoProperties.getURL() + "CertificadosPersonalWS";

    public java.lang.String getCertificadosPersonalWSAddress() {
        return CertificadosPersonalWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CertificadosPersonalWSWSDDServiceName = "CertificadosPersonalWS";

    public java.lang.String getCertificadosPersonalWSWSDDServiceName() {
        return CertificadosPersonalWSWSDDServiceName;
    }

    public void setCertificadosPersonalWSWSDDServiceName(java.lang.String name) {
        CertificadosPersonalWSWSDDServiceName = name;
    }

    public es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWS getCertificadosPersonalWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CertificadosPersonalWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCertificadosPersonalWS(endpoint);
    }

    public es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWS getCertificadosPersonalWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWSSoapBindingStub _stub = new es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getCertificadosPersonalWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCertificadosPersonalWSEndpointAddress(java.lang.String address) {
        CertificadosPersonalWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWS.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWSSoapBindingStub _stub = new es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWSSoapBindingStub(new java.net.URL(CertificadosPersonalWS_address), this);
                _stub.setPortName(getCertificadosPersonalWSWSDDServiceName());
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
        if ("CertificadosPersonalWS".equals(inputPortName)) {
            return getCertificadosPersonalWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://certificadosPersonal.services.webempleado.dipucr.es", "CertificadosPersonalWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://certificadosPersonal.services.webempleado.dipucr.es", "CertificadosPersonalWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CertificadosPersonalWS".equals(portName)) {
            setCertificadosPersonalWSEndpointAddress(address);
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

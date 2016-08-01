/**
 * ConcesionPagoProyectoServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.minhap.igae.ws.concesionPagoProyecto;

import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class ConcesionPagoProyectoServiceLocator extends org.apache.axis.client.Service implements es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyectoService {

    public ConcesionPagoProyectoServiceLocator() {
    }


    public ConcesionPagoProyectoServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ConcesionPagoProyectoServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ConcesionPagoProyectoSoap11
    private java.lang.String ConcesionPagoProyectoSoap11_address = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_CONCESPAGOPROY);

    public java.lang.String getConcesionPagoProyectoSoap11Address() {
        return ConcesionPagoProyectoSoap11_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ConcesionPagoProyectoSoap11WSDDServiceName = "ConcesionPagoProyectoSoap11";

    public java.lang.String getConcesionPagoProyectoSoap11WSDDServiceName() {
        return ConcesionPagoProyectoSoap11WSDDServiceName;
    }

    public void setConcesionPagoProyectoSoap11WSDDServiceName(java.lang.String name) {
        ConcesionPagoProyectoSoap11WSDDServiceName = name;
    }

    public es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyecto getConcesionPagoProyectoSoap11() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ConcesionPagoProyectoSoap11_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getConcesionPagoProyectoSoap11(endpoint);
    }

    public es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyecto getConcesionPagoProyectoSoap11(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyectoSoap11Stub _stub = new es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyectoSoap11Stub(portAddress, this);
            _stub.setPortName(getConcesionPagoProyectoSoap11WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setConcesionPagoProyectoSoap11EndpointAddress(java.lang.String address) {
        ConcesionPagoProyectoSoap11_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyecto.class.isAssignableFrom(serviceEndpointInterface)) {
                es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyectoSoap11Stub _stub = new es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyectoSoap11Stub(new java.net.URL(ConcesionPagoProyectoSoap11_address), this);
                _stub.setPortName(getConcesionPagoProyectoSoap11WSDDServiceName());
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
        if ("ConcesionPagoProyectoSoap11".equals(inputPortName)) {
            return getConcesionPagoProyectoSoap11();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://concesionPagoProyecto.ws.igae.minhap.es", "ConcesionPagoProyectoService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://concesionPagoProyecto.ws.igae.minhap.es", "ConcesionPagoProyectoSoap11"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ConcesionPagoProyectoSoap11".equals(portName)) {
            setConcesionPagoProyectoSoap11EndpointAddress(address);
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

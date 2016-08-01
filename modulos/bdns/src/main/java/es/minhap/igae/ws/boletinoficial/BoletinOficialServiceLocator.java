/**
 * BoletinOficialServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.minhap.igae.ws.boletinoficial;

import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class BoletinOficialServiceLocator extends org.apache.axis.client.Service implements es.minhap.igae.ws.boletinoficial.BoletinOficialService {

    public BoletinOficialServiceLocator() {
    }


    public BoletinOficialServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BoletinOficialServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BoletinOficialSoap11
    private java.lang.String BoletinOficialSoap11_address = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_BOP);

    public java.lang.String getBoletinOficialSoap11Address() {
        return BoletinOficialSoap11_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BoletinOficialSoap11WSDDServiceName = "BoletinOficialSoap11";

    public java.lang.String getBoletinOficialSoap11WSDDServiceName() {
        return BoletinOficialSoap11WSDDServiceName;
    }

    public void setBoletinOficialSoap11WSDDServiceName(java.lang.String name) {
        BoletinOficialSoap11WSDDServiceName = name;
    }

    public es.minhap.igae.ws.boletinoficial.BoletinOficial getBoletinOficialSoap11() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BoletinOficialSoap11_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBoletinOficialSoap11(endpoint);
    }

    public es.minhap.igae.ws.boletinoficial.BoletinOficial getBoletinOficialSoap11(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.minhap.igae.ws.boletinoficial.BoletinOficialSoap11Stub _stub = new es.minhap.igae.ws.boletinoficial.BoletinOficialSoap11Stub(portAddress, this);
            _stub.setPortName(getBoletinOficialSoap11WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBoletinOficialSoap11EndpointAddress(java.lang.String address) {
        BoletinOficialSoap11_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.minhap.igae.ws.boletinoficial.BoletinOficial.class.isAssignableFrom(serviceEndpointInterface)) {
                es.minhap.igae.ws.boletinoficial.BoletinOficialSoap11Stub _stub = new es.minhap.igae.ws.boletinoficial.BoletinOficialSoap11Stub(new java.net.URL(BoletinOficialSoap11_address), this);
                _stub.setPortName(getBoletinOficialSoap11WSDDServiceName());
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
        if ("BoletinOficialSoap11".equals(inputPortName)) {
            return getBoletinOficialSoap11();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://boletinoficial.ws.igae.minhap.es", "BoletinOficialService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://boletinoficial.ws.igae.minhap.es", "BoletinOficialSoap11"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BoletinOficialSoap11".equals(portName)) {
            setBoletinOficialSoap11EndpointAddress(address);
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

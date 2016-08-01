/**
 * ConvocatoriaServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.minhap.igae.ws.convocatoria;

import es.dipucr.bdns.common.WSBDNSProperties;
import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class ConvocatoriaServiceLocator extends org.apache.axis.client.Service implements es.minhap.igae.ws.convocatoria.ConvocatoriaService {

    public ConvocatoriaServiceLocator() {
    }


    public ConvocatoriaServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ConvocatoriaServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ConvocatoriaSoap11
    private java.lang.String ConvocatoriaSoap11_address = WSBDNSProperties.getURL(ServiciosWebConfiguration.BDNS_APP_CONVOCATORIAS);

    public java.lang.String getConvocatoriaSoap11Address() {
        return ConvocatoriaSoap11_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ConvocatoriaSoap11WSDDServiceName = "ConvocatoriaSoap11";

    public java.lang.String getConvocatoriaSoap11WSDDServiceName() {
        return ConvocatoriaSoap11WSDDServiceName;
    }

    public void setConvocatoriaSoap11WSDDServiceName(java.lang.String name) {
        ConvocatoriaSoap11WSDDServiceName = name;
    }

    public es.minhap.igae.ws.convocatoria.Convocatoria getConvocatoriaSoap11() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ConvocatoriaSoap11_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getConvocatoriaSoap11(endpoint);
    }

    public es.minhap.igae.ws.convocatoria.Convocatoria getConvocatoriaSoap11(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.minhap.igae.ws.convocatoria.ConvocatoriaSoap11Stub _stub = new es.minhap.igae.ws.convocatoria.ConvocatoriaSoap11Stub(portAddress, this);
            _stub.setPortName(getConvocatoriaSoap11WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setConvocatoriaSoap11EndpointAddress(java.lang.String address) {
        ConvocatoriaSoap11_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.minhap.igae.ws.convocatoria.Convocatoria.class.isAssignableFrom(serviceEndpointInterface)) {
                es.minhap.igae.ws.convocatoria.ConvocatoriaSoap11Stub _stub = new es.minhap.igae.ws.convocatoria.ConvocatoriaSoap11Stub(new java.net.URL(ConvocatoriaSoap11_address), this);
                _stub.setPortName(getConvocatoriaSoap11WSDDServiceName());
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
        if ("ConvocatoriaSoap11".equals(inputPortName)) {
            return getConvocatoriaSoap11();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://convocatoria.ws.igae.minhap.es", "ConvocatoriaService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://convocatoria.ws.igae.minhap.es", "ConvocatoriaSoap11"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ConvocatoriaSoap11".equals(portName)) {
            setConvocatoriaSoap11EndpointAddress(address);
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

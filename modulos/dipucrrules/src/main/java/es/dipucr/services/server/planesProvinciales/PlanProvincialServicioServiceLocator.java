/**
 * PlanProvincialServicioServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.services.server.planesProvinciales;

public class PlanProvincialServicioServiceLocator extends org.apache.axis.client.Service implements es.dipucr.services.server.planesProvinciales.PlanProvincialServicioService {

    public PlanProvincialServicioServiceLocator() {
    }


    public PlanProvincialServicioServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PlanProvincialServicioServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PlanProvincialServicio
    private java.lang.String PlanProvincialServicio_address = "http://10.12.200.151:8070/DipuCrServices/services/PlanProvincialServicio";

    public java.lang.String getPlanProvincialServicioAddress() {
        return PlanProvincialServicio_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PlanProvincialServicioWSDDServiceName = "PlanProvincialServicio";

    public java.lang.String getPlanProvincialServicioWSDDServiceName() {
        return PlanProvincialServicioWSDDServiceName;
    }

    public void setPlanProvincialServicioWSDDServiceName(java.lang.String name) {
        PlanProvincialServicioWSDDServiceName = name;
    }

    public es.dipucr.services.server.planesProvinciales.PlanProvincialServicio getPlanProvincialServicio() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PlanProvincialServicio_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPlanProvincialServicio(endpoint);
    }

    public es.dipucr.services.server.planesProvinciales.PlanProvincialServicio getPlanProvincialServicio(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.dipucr.services.server.planesProvinciales.PlanProvincialServicioSoapBindingStub _stub = new es.dipucr.services.server.planesProvinciales.PlanProvincialServicioSoapBindingStub(portAddress, this);
            _stub.setPortName(getPlanProvincialServicioWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPlanProvincialServicioEndpointAddress(java.lang.String address) {
        PlanProvincialServicio_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.dipucr.services.server.planesProvinciales.PlanProvincialServicio.class.isAssignableFrom(serviceEndpointInterface)) {
                es.dipucr.services.server.planesProvinciales.PlanProvincialServicioSoapBindingStub _stub = new es.dipucr.services.server.planesProvinciales.PlanProvincialServicioSoapBindingStub(new java.net.URL(PlanProvincialServicio_address), this);
                _stub.setPortName(getPlanProvincialServicioWSDDServiceName());
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
        if ("PlanProvincialServicio".equals(inputPortName)) {
            return getPlanProvincialServicio();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "PlanProvincialServicioService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "PlanProvincialServicio"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PlanProvincialServicio".equals(portName)) {
            setPlanProvincialServicioEndpointAddress(address);
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

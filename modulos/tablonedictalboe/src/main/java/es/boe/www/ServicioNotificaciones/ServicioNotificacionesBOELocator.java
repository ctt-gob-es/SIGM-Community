/**
 * ServicioNotificacionesBOELocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.boe.www.ServicioNotificaciones;

public class ServicioNotificacionesBOELocator extends org.apache.axis.client.Service implements es.boe.www.ServicioNotificaciones.ServicioNotificacionesBOE {

    public ServicioNotificacionesBOELocator() {
    }


    public ServicioNotificacionesBOELocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ServicioNotificacionesBOELocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ServicioNotificacionesPort
    private java.lang.String ServicioNotificacionesPort_address = "https://extranet.boe.es/notificaciones/ws/index.php";

    public java.lang.String getServicioNotificacionesPortAddress() {
        return ServicioNotificacionesPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ServicioNotificacionesPortWSDDServiceName = "ServicioNotificacionesPort";

    public java.lang.String getServicioNotificacionesPortWSDDServiceName() {
        return ServicioNotificacionesPortWSDDServiceName;
    }

    public void setServicioNotificacionesPortWSDDServiceName(java.lang.String name) {
        ServicioNotificacionesPortWSDDServiceName = name;
    }

    public es.boe.www.ServicioNotificaciones.ServicioNotificaciones getServicioNotificacionesPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ServicioNotificacionesPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServicioNotificacionesPort(endpoint);
    }

    public es.boe.www.ServicioNotificaciones.ServicioNotificaciones getServicioNotificacionesPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.boe.www.ServicioNotificaciones.ServicioNotificacionesSOAPStub _stub = new es.boe.www.ServicioNotificaciones.ServicioNotificacionesSOAPStub(portAddress, this);
            _stub.setPortName(getServicioNotificacionesPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServicioNotificacionesPortEndpointAddress(java.lang.String address) {
        ServicioNotificacionesPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.boe.www.ServicioNotificaciones.ServicioNotificaciones.class.isAssignableFrom(serviceEndpointInterface)) {
                es.boe.www.ServicioNotificaciones.ServicioNotificacionesSOAPStub _stub = new es.boe.www.ServicioNotificaciones.ServicioNotificacionesSOAPStub(new java.net.URL(ServicioNotificacionesPort_address), this);
                _stub.setPortName(getServicioNotificacionesPortWSDDServiceName());
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
        if ("ServicioNotificacionesPort".equals(inputPortName)) {
            return getServicioNotificacionesPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "ServicioNotificacionesBOE");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "ServicioNotificacionesPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ServicioNotificacionesPort".equals(portName)) {
            setServicioNotificacionesPortEndpointAddress(address);
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

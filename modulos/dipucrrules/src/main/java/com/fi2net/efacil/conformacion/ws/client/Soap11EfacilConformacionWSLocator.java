/*
 * Plan Avanza Local Soluciones – e-fácil – Copyright © 2011 – Ministerio de Industria, Turismo y 
 * Comercio.
 *
 * Este programa es software libre, por ello está permitido redistribuirlo y/o modificarlo bajo los 
 * términos de la GNU General Public License, en su versión 3, publicada por la Free Software 
 * Foundation.
 *
 * Junto con este programa debe haber recibido una copia de la GNU General Public License, en 
 * caso contrario puede consultarla en <http://www.gnu.org/licenses/>.
 *
 * El presente programa posee las siguientes cláusulas particulares de licencia GPL v3:
 *
 *  - Queda restringido su uso a Administraciones Públicas, en el ámbito de sus 
 * competencias legalmente establecidas y con la finalidad de utilidad pública e interés 
 * social;
 *  - La distribución y el uso del software tienen carácter gratuito;
 *  - Ni el software ni ninguno de los Módulos que lo componen serán usados ni 
 * sublicenciados, bajo ninguna circunstancia, con fines o intereses comerciales, bien sea 
 * directa o indirectamente, por parte de la Administración Pública, sus funcionarios, 
 * empleados o cualquier otra persona u organización dentro o fuera de la 
 * administración.
 *
 */

/**
 * Soap11EfacilConformacionWSLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.fi2net.efacil.conformacion.ws.client;

public class Soap11EfacilConformacionWSLocator extends org.apache.axis.client.Service implements com.fi2net.efacil.conformacion.ws.client.Soap11EfacilConformacionWS {

    public Soap11EfacilConformacionWSLocator() {
    }


    public Soap11EfacilConformacionWSLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Soap11EfacilConformacionWSLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Soap11HttpEndpoint
    private java.lang.String Soap11HttpEndpoint_address = "http://localhost:8080/EFacilConformacionWS/Soap11EfacilConformacionWS";

    public java.lang.String getSoap11HttpEndpointAddress() {
        return Soap11HttpEndpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String Soap11HttpEndpointWSDDServiceName = "Soap11HttpEndpoint";

    public java.lang.String getSoap11HttpEndpointWSDDServiceName() {
        return Soap11HttpEndpointWSDDServiceName;
    }

    public void setSoap11HttpEndpointWSDDServiceName(java.lang.String name) {
        Soap11HttpEndpointWSDDServiceName = name;
    }

    public com.fi2net.efacil.conformacion.ws.client.IEFacilConformacionFacturasService getSoap11HttpEndpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Soap11HttpEndpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSoap11HttpEndpoint(endpoint);
    }

    public com.fi2net.efacil.conformacion.ws.client.IEFacilConformacionFacturasService getSoap11HttpEndpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.fi2net.efacil.conformacion.ws.client.Soap11HttpEndpointStub _stub = new com.fi2net.efacil.conformacion.ws.client.Soap11HttpEndpointStub(portAddress, this);
            _stub.setPortName(getSoap11HttpEndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSoap11HttpEndpointEndpointAddress(java.lang.String address) {
        Soap11HttpEndpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.fi2net.efacil.conformacion.ws.client.IEFacilConformacionFacturasService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.fi2net.efacil.conformacion.ws.client.Soap11HttpEndpointStub _stub = new com.fi2net.efacil.conformacion.ws.client.Soap11HttpEndpointStub(new java.net.URL(Soap11HttpEndpoint_address), this);
                _stub.setPortName(getSoap11HttpEndpointWSDDServiceName());
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
        if ("Soap11HttpEndpoint".equals(inputPortName)) {
            return getSoap11HttpEndpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "Soap11EfacilConformacionWS");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "Soap11HttpEndpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Soap11HttpEndpoint".equals(portName)) {
            setSoap11HttpEndpointEndpointAddress(address);
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

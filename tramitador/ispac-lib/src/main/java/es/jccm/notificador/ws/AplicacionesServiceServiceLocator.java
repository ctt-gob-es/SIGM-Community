/**
 * AplicacionesServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.jccm.notificador.ws;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class AplicacionesServiceServiceLocator extends org.apache.axis.client.Service implements es.jccm.notificador.ws.AplicacionesServiceService {

	private static final Logger logger = Logger.getLogger(AplicacionesServiceServiceLocator.class);
    public AplicacionesServiceServiceLocator() {
    }

    public AplicacionesServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AplicacionesServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AplicacionesService

    public String getDireccionSW(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_COMPARECE_SW);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web de la Plataforma de Contratación. " + e.getMessage(), e);
		}
        return url;
    }

    // Use to get a proxy class for PlataformaContratacion    
    private java.lang.String AplicacionesService_address = getDireccionSW();
    

    public java.lang.String getAplicacionesServiceAddress() {
        return AplicacionesService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AplicacionesServiceWSDDServiceName = "AplicacionesService";

    public java.lang.String getAplicacionesServiceWSDDServiceName() {
        return AplicacionesServiceWSDDServiceName;
    }

    public void setAplicacionesServiceWSDDServiceName(java.lang.String name) {
        AplicacionesServiceWSDDServiceName = name;
    }

    public es.jccm.notificador.ws.AplicacionesService getAplicacionesService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AplicacionesService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAplicacionesService(endpoint);
    }

    public es.jccm.notificador.ws.AplicacionesService getAplicacionesService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.jccm.notificador.ws.AplicacionesServiceSoapBindingStub _stub = new es.jccm.notificador.ws.AplicacionesServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getAplicacionesServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAplicacionesServiceEndpointAddress(java.lang.String address) {
        AplicacionesService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.jccm.notificador.ws.AplicacionesService.class.isAssignableFrom(serviceEndpointInterface)) {
                es.jccm.notificador.ws.AplicacionesServiceSoapBindingStub _stub = new es.jccm.notificador.ws.AplicacionesServiceSoapBindingStub(new java.net.URL(AplicacionesService_address), this);
                _stub.setPortName(getAplicacionesServiceWSDDServiceName());
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
        if ("AplicacionesService".equals(inputPortName)) {
            return getAplicacionesService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "AplicacionesServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "AplicacionesService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AplicacionesService".equals(portName)) {
            setAplicacionesServiceEndpointAddress(address);
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

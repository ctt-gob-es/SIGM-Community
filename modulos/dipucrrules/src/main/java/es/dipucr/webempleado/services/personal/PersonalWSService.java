/**
 * PersonalWSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.personal;

public interface PersonalWSService extends javax.xml.rpc.Service {
    public java.lang.String getPersonalWSAddress();

    public es.dipucr.webempleado.services.personal.PersonalWS getPersonalWS() throws javax.xml.rpc.ServiceException;

    public es.dipucr.webempleado.services.personal.PersonalWS getPersonalWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

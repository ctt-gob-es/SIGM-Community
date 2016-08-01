/**
 * MensajeSMS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.enviosms.ws;

public interface MensajeSMS extends java.rmi.Remote {
    public es.dipucr.enviosms.objetos.Respuesta envioSMS(java.lang.String entidad, java.lang.String aplicacion, java.lang.String[] movil, java.lang.String mensaje) throws java.rmi.RemoteException;
    public es.dipucr.enviosms.objetos.Respuesta envioSMSCertificado(java.lang.String entidad, java.lang.String aplicacion, java.lang.String[] movil, java.lang.String mensaje, java.lang.String certificadoMailAcuse) throws java.rmi.RemoteException;
}

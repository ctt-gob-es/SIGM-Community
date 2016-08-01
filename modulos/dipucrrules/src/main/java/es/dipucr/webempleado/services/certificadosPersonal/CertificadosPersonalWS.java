/**
 * CertificadosPersonalWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.certificadosPersonal;

public interface CertificadosPersonalWS extends java.rmi.Remote {
    public es.dipucr.webempleado.services.certificadosPersonal.ObjetoCertificadoDataHandler generarCertificado(java.lang.String nif, java.lang.String nombre, java.lang.String donde, int tipoCert) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.services.certificadosPersonal.ItemLista[] getTiposCertificado() throws java.rmi.RemoteException;
}

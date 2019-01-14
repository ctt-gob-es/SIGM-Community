/**
 * Scsp.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.verifdatos.services;

public interface Scsp extends java.rmi.Remote {
    public java.lang.String getEmisorCertificadoByNombre(java.lang.String nombreEntidad, java.lang.String emisorServicio) throws java.rmi.RemoteException;
    public java.lang.String[] getCoreServByCodCertificadoDescriVersion(java.lang.String nombreEntidad, java.lang.String codCertificado) throws java.rmi.RemoteException;
}

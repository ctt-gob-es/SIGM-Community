/**
 * CertificadoDeRetencionesService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.certRetencion;

public interface CertificadoDeRetencionesService extends java.rmi.Remote {
    public es.dipucr.webempleado.services.certRetencion.ObjetoCertificadoDataHandler generarCertificadosRetenciones(java.lang.String nif) throws java.rmi.RemoteException;
    public void enviarCertificadosRetenciones(es.dipucr.webempleado.services.certRetencion.ObjetoCertificadoDataHandler dataHandlerFirmados) throws java.rmi.RemoteException;
    public java.lang.String[] dameNIF() throws java.rmi.RemoteException;
}

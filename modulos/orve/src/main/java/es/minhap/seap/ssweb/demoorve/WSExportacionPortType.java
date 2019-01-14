/**
 * WSExportacionPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.minhap.seap.ssweb.demoorve;

public interface WSExportacionPortType extends java.rmi.Remote {
    public es.minhap.seap.ssweb.demoorve.ObtenerIdentificaoresRespuestaWS obtenerIdentificadores(es.minhap.seap.ssweb.demoorve.holders.SecurityHolder security, es.minhap.seap.ssweb.demoorve.FiltrosIdentificadores filtros) throws java.rmi.RemoteException;
    public es.minhap.seap.ssweb.demoorve.ObtenerRegistroRespuestaWS obtenerRegistro(es.minhap.seap.ssweb.demoorve.holders.SecurityHolder security, int identificador) throws java.rmi.RemoteException;
}

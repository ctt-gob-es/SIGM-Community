/**
 * TablonWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.tablon.services;

public interface TablonWS extends java.rmi.Remote {
    public es.dipucr.tablon.services.ItemLista[] getServicios(java.lang.String codEntidad) throws java.rmi.RemoteException;
    public es.dipucr.tablon.services.ItemLista[] getCategorias(java.lang.String codEntidad) throws java.rmi.RemoteException;
    public boolean insertarPublicacion(java.lang.String codEntidad, java.lang.String titulo, java.lang.String descripcion, java.util.Calendar fechaFirma, java.lang.String codServicio, java.lang.String codCategoria, java.util.Calendar fechaIniVigencia, java.util.Calendar fechaFinVigencia, java.lang.String cve, java.lang.String hash, java.lang.String idTransaccion, java.lang.String numexp, java.lang.String servicioOtros, java.lang.String categoriaOtros, javax.activation.DataHandler dhPublicacion) throws java.rmi.RemoteException;
    public es.dipucr.tablon.services.ItemLista getServicioByCodigo(java.lang.String codEntidad, java.lang.String codServicio) throws java.rmi.RemoteException;
    public es.dipucr.tablon.services.ItemLista getCategoriaByCodigo(java.lang.String codEntidad, java.lang.String codCategoria) throws java.rmi.RemoteException;
}

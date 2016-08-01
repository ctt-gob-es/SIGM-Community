/**
 * ServicioNotificaciones.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.boe.www.ServicioNotificaciones;

public interface ServicioNotificaciones extends java.rmi.Remote {
    public es.boe.www.ServicioNotificaciones.Respuesta envioAnuncios(java.lang.String envio) throws java.rmi.RemoteException;
    public es.boe.www.ServicioNotificaciones.Respuesta consultaEnvio(java.lang.String idEnvio) throws java.rmi.RemoteException;
    public es.boe.www.ServicioNotificaciones.Respuesta consultaAnuncio(java.lang.String idAnuncio) throws java.rmi.RemoteException;
}

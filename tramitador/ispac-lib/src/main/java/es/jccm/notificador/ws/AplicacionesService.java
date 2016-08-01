/**
 * AplicacionesService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.jccm.notificador.ws;

public interface AplicacionesService extends java.rmi.Remote {
    public es.dipucr.notificador.model.EnvioWS[] notificar(java.lang.String id, java.lang.String[] dnis, java.lang.String procedimiento, java.lang.String expediente, java.lang.String descripcion, int diaCaducidad, int mesCaducidad, int anyoCaducidad, javax.activation.DataHandler notificacion, java.lang.String nombre, java.lang.String extension, java.lang.String idHashNotificacion, java.lang.String idCve) throws java.rmi.RemoteException;
    public es.dipucr.notificador.model.NotificacionWS recuperarEstadosNotificaciones(java.lang.String id, int idNotificacion) throws java.rmi.RemoteException;
    public javax.activation.DataHandler recuperarAcuseFirmado(java.lang.String id, int idNotificacion) throws java.rmi.RemoteException;
    public boolean modificarTercero(java.lang.String id, java.lang.String dni, java.lang.String email, java.lang.String telefono) throws java.rmi.RemoteException;
    public es.dipucr.notificador.model.TerceroWS consultarDNI(java.lang.String id, java.lang.String dni) throws java.rmi.RemoteException;
    public boolean altaTercero(java.lang.String id, java.lang.String dni, java.lang.String email, java.lang.String telefono) throws java.rmi.RemoteException;
    public boolean borrarTercero(java.lang.String id, java.lang.String dni) throws java.rmi.RemoteException;
}

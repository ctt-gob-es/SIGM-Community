/**
 * ConcesionPagoProyecto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.minhap.igae.ws.concesionPagoProyecto;

public interface ConcesionPagoProyecto extends java.rmi.Remote {
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta peticion(concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion peticion) throws java.rmi.RemoteException;
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.confirmacionPeticion.ConfirmacionPeticion peticionAsincrona(concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.PeticionAsincrona peticion) throws java.rmi.RemoteException;
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta solicitudRespuesta(concesion.es.redsara.intermediacion.scsp.esquemas.V3.solicitudRespuesta.SolicitudRespuesta solicitudRespuesta) throws java.rmi.RemoteException;
}

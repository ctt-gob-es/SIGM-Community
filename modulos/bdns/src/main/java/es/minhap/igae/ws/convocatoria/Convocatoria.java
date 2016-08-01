/**
 * Convocatoria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.minhap.igae.ws.convocatoria;

public interface Convocatoria extends java.rmi.Remote {
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta peticion(conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion peticion) throws java.rmi.RemoteException;
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.confirmacionPeticion.ConfirmacionPeticion peticionAsincrona(conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.PeticionAsincrona peticion) throws java.rmi.RemoteException;
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta solicitudRespuesta(conv.es.redsara.intermediacion.scsp.esquemas.V3.solicitudRespuesta.SolicitudRespuesta solicitudRespuesta) throws java.rmi.RemoteException;
}

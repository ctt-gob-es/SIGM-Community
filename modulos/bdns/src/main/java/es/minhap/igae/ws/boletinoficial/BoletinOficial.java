/**
 * BoletinOficial.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.minhap.igae.ws.boletinoficial;

public interface BoletinOficial extends java.rmi.Remote {
    public BDNS.respuestaAnuncio.RespuestaAnuncio peticionAnuncio(BDNS.peticionAnuncio.PeticionAnuncio peticionAnuncio) throws java.rmi.RemoteException;
    public BDNS.confirmacionAnuncio.ConfirmacionAnuncio publicacionAnuncio(BDNS.publicacionAnuncio.PublicacionAnuncio publicacionAnuncio) throws java.rmi.RemoteException;
}

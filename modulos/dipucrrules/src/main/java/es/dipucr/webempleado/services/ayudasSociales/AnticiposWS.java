/**
 * AnticiposWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.ayudasSociales;

public interface AnticiposWS extends java.rmi.Remote {
    public boolean comprobarAnticipoPendiente(java.lang.String nif) throws java.rmi.RemoteException;
    public boolean crearAnticipoPendiente(java.lang.String nif, double importeTotal, double importeMes, double importeUltimoMes, int meses, java.lang.String observaciones, java.lang.String sigemNreg, java.util.Calendar sigemFreg, java.lang.String sigemNumexp) throws java.rmi.RemoteException;
    public boolean ponerAnticipoValidado(java.lang.String nif, boolean bFirmada, java.lang.String motivo) throws java.rmi.RemoteException;
}

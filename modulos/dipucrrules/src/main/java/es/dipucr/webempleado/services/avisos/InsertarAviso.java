/**
 * InsertarAviso.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.avisos;

public interface InsertarAviso extends java.rmi.Remote {
    public int nuevoAvisoUsuario(java.lang.String asunto, java.lang.String cuerpo, java.lang.String nif) throws java.rmi.RemoteException;
    public int nuevoAvisoGeneral(java.lang.String asunto, java.lang.String cuerpo) throws java.rmi.RemoteException;
    public boolean comprobarUsuario(java.lang.String nif) throws java.rmi.RemoteException;
}

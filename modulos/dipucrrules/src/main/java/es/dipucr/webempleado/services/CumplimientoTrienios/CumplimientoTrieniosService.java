/**
 * CumplimientoTrieniosService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.CumplimientoTrienios;

public interface CumplimientoTrieniosService extends java.rmi.Remote {
    public java.lang.Object[] listaCumplimientoTrienios(int tipo) throws java.rmi.RemoteException;
    public boolean insertarVidaAdministrativa(java.lang.String dni, java.lang.String fecha_decreto, java.lang.String num_trienio, java.lang.String fecha_trienio, java.lang.String no_expediente, java.lang.String grupo) throws java.rmi.RemoteException;
    public boolean insertarApunteTrienio(java.lang.String dni, java.lang.String num_trienio, java.lang.String fecha_trienio, java.lang.String fecha_decreto, java.lang.String grupo, java.lang.String numExp) throws java.rmi.RemoteException;
}

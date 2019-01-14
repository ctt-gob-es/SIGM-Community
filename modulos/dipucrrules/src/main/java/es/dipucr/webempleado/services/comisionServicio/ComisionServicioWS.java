/**
 * ComisionServicioWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.comisionServicio;

public interface ComisionServicioWS extends java.rmi.Remote {
    public boolean ponerComisionAnulada(int idComision) throws java.rmi.RemoteException;
    public boolean existenComisionesCoincidentes(java.lang.String nif, java.util.Calendar dFechaInicio, java.util.Calendar dFechaFin) throws java.rmi.RemoteException;
    public int crearComisionPendiente(java.lang.String nif, java.lang.String motivo, java.util.Calendar fechaInicio, java.lang.String horaInicio, java.util.Calendar fechaFin, java.lang.String horaFin, java.lang.String observaciones, java.lang.String nreg, java.lang.String numexp) throws java.rmi.RemoteException;
    public boolean cambiarEstadoComision(int idComision, boolean bFirmada, java.lang.String motivo, java.lang.String firmantes) throws java.rmi.RemoteException;
}

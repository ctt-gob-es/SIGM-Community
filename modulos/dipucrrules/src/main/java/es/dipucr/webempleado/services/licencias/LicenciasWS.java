/**
 * LicenciasWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.licencias;

public interface LicenciasWS extends java.rmi.Remote {
    public boolean ponerLicenciaAnulada(java.lang.String nif, int ano, java.lang.String nlic, java.lang.String observaciones) throws java.rmi.RemoteException;
    public boolean ponerLicenciaValidada(java.lang.String nif, int ano, java.lang.String nlic, boolean bFirmada, java.lang.String motivo, java.lang.String firmantes) throws java.rmi.RemoteException;
    public java.lang.String crearLicenciaPendiente(java.lang.String nif, java.lang.String tlic, int ano, java.util.Calendar finicio, java.util.Calendar ffinal, java.lang.String dias, java.lang.String observaciones, java.lang.String sigemNumexp, java.lang.String sigemNreg, java.util.Calendar sigemFreg) throws java.rmi.RemoteException;
    public boolean existenLicenciasCoincidentes(java.lang.String nif, int ano, java.util.Calendar finicio, java.util.Calendar ffinal) throws java.rmi.RemoteException;
}

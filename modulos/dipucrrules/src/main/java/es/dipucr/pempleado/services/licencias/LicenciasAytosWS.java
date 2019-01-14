/**
 * LicenciasAytosWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.pempleado.services.licencias;

public interface LicenciasAytosWS extends java.rmi.Remote {
    public boolean ponerLicenciaAnulada(java.lang.String idEntidad, int idLicencia, java.lang.String observaciones) throws java.rmi.RemoteException;
    public boolean existenLicenciasCoincidentes(java.lang.String idEntidad, java.lang.String nif, java.lang.String anio, java.util.Calendar fechaInicio, java.util.Calendar fechaFin) throws java.rmi.RemoteException;
    public int crearLicenciaPendiente(java.lang.String idEntidad, java.lang.String nif, java.lang.String tlic, java.lang.String anio, java.util.Calendar fechaInicio, java.util.Calendar fechaFin, int numDias, java.lang.String detalleDias, java.lang.String observaciones, java.lang.String sigemNumexp, java.lang.String sigemNreg, java.util.Calendar sigemFreg) throws java.rmi.RemoteException;
    public boolean ponerLicenciaValidada(java.lang.String idEntidad, int id, boolean bFirmada, java.lang.String motivo, java.lang.String firmantes) throws java.rmi.RemoteException;
}

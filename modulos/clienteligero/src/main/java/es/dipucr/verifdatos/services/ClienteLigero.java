/**
 * ClienteLigero.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.verifdatos.services;

public interface ClienteLigero extends java.rmi.Remote {
    public java.lang.String getRegistroCivil(java.lang.String provincia, java.lang.String municipio) throws java.rmi.RemoteException;
    public java.lang.String[] getComunidad() throws java.rmi.RemoteException;
    public int[] selectAllYear(java.lang.String nombreEntidad) throws java.rmi.RemoteException;
    public java.lang.String[] getProvincia(java.lang.String comunidad) throws java.rmi.RemoteException;
    public java.lang.String[] getMunicipio(java.lang.String provincia) throws java.rmi.RemoteException;
    public java.lang.String getXmlDatosEspecificos(java.lang.String nombreEntidad, java.lang.String procSelec) throws java.rmi.RemoteException;
    public java.lang.String[] getCertAutByCodCert(java.lang.String nombreEntidad, java.lang.String codigoCertificado) throws java.rmi.RemoteException;
    public java.lang.String[] consultaProcedimientoByNIF(java.lang.String nombreEntidad, java.lang.String nifFuncionario, java.lang.String codigoProcedimiento) throws java.rmi.RemoteException;
    public java.lang.String[] getUniversidadesCRUE() throws java.rmi.RemoteException;
    public java.lang.String[] getPostprocessorDatosEspecificos(java.lang.String nombreEntidad, java.lang.String certificado) throws java.rmi.RemoteException;
    public java.lang.String[] getPreprocessorDatosEspecificos(java.lang.String nombreEntidad, java.lang.String certificado) throws java.rmi.RemoteException;
    public es.dipucr.verifdatos.model.dao.DatosGeograficosManager getDatosGeograficosManager(java.lang.String nombreEntidad) throws java.rmi.RemoteException;
}

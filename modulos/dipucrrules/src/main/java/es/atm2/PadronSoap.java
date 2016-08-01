/**
 * PadronSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public interface PadronSoap extends java.rmi.Remote {
    public es.atm2.ObtenerPersonaPorNombreResponseObtenerPersonaPorNombreResult obtenerPersonaPorNombre(java.lang.String codInstitucion, java.lang.String esFisica, java.lang.String nombre, java.lang.String apellido1, java.lang.String apellido2, int numItems, java.lang.String desdePosicion) throws java.rmi.RemoteException;
    public es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult obtenerPersonaPorNIF(java.lang.String p_sCodInstitucion, java.lang.String p_sPersonaFisica_o_Juridica, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException;
    public es.atm2.ObtenerDatosPadronalesPersonaResponseObtenerDatosPadronalesPersonaResult obtenerDatosPadronalesPersona(java.lang.String p_sCodInstitucion, java.lang.String p_sTipoDocumento, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException;
    public es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult obtenerDatosConvivenciaPersona(java.lang.String p_sCodInstitucion, java.lang.String p_sTipoDocumento, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException;
    public es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult obtenerCertificadoConvivencia(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException;
    public es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult obtenerVolanteConvivencia(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException;
    public es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult obtenerCertificadoEmpadronamiento(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException;
    public es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult obtenerVolanteEmpadronamiento(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException;
}

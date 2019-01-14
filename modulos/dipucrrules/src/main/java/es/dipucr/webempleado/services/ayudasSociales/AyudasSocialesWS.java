/**
 * AyudasSocialesWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.ayudasSociales;

public interface AyudasSocialesWS extends java.rmi.Remote {
    public int crearAyuda(java.lang.String anoAyuda, int idConcepto, int idGrupo, java.lang.String nif, java.lang.String parentesco, java.lang.String numFactura, java.lang.String fechaFactura, double importe, double importeConcedido, java.lang.String fechaRegistro, java.lang.String beneficiario, java.lang.String fechaNacBeneficiario, java.lang.String email, java.lang.String telefono, java.lang.String observaciones, java.lang.String numRegistro, java.lang.String numexp, es.dipucr.webempleado.domain.beans.DocumentoAyudas[] arrDocumentos, java.lang.String tipoContrato, java.lang.String idConvocatoria) throws java.rmi.RemoteException;
    public boolean anularAyuda(java.lang.String idAyuda) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Propuesta getPropuesta(int idPropuesta) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.domain.AyudaSocial[] getAyudasPropuesta(int idPropuesta) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Convocatoria calcularPuntosTotales(int idConvocatoria) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Convocatoria getConvocatoriaByAnio(java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.domain.AyudaSocial[] getAyudasEstudiosConvocatoria(int idConvocatoria) throws java.rmi.RemoteException;
    public boolean crearBeneficiario(java.lang.String nif, java.lang.String parentesco, java.lang.String nombre, java.lang.String fechaNacimiento, java.lang.String numexp) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.domain.AyudaSocial[] getAyudasEstudiosAnio(java.lang.String anio) throws java.rmi.RemoteException;
}

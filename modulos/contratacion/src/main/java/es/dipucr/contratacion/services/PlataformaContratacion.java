/**
 * PlataformaContratacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.services;

public interface PlataformaContratacion extends java.rmi.Remote {
    public es.dipucr.contratacion.resultadoBeans.Resultado anuncioAdjudicacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioAdjudicacionBean anuncioAdjudicacion, java.lang.String publishedByUser) throws java.rmi.RemoteException;
    public es.dipucr.contratacion.resultadoBeans.Resultado consultarDatosAlta(java.lang.String entidad, java.lang.String numexp, java.lang.String publishedByUser) throws java.rmi.RemoteException;
    public es.dipucr.contratacion.resultadoBeans.Resultado envioOtrosDocumentosLicitacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.Documento documentoAdicional, java.lang.String publishedByUser) throws java.rmi.RemoteException;
    public es.dipucr.contratacion.resultadoBeans.Resultado envioAnalisisPrevio(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioPrevioBean analisisPrevio, java.lang.String publishedByUser) throws java.rmi.RemoteException;
    public es.dipucr.contratacion.resultadoBeans.Resultado envioPliegos(java.lang.String entidad, es.dipucr.contratacion.client.beans.PliegoBean pliego, java.lang.String publishedByUser) throws java.rmi.RemoteException;
    public es.dipucr.contratacion.resultadoBeans.Resultado estadoExpediente(java.lang.String entidad, java.lang.String numexp, java.lang.String publishedByUser) throws java.rmi.RemoteException;
    public es.dipucr.contratacion.resultadoBeans.Resultado envioPublicacionAnuncioLicitacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioLicitacionBean anuncioLicitacion, java.lang.String publishedByUser) throws java.rmi.RemoteException;
}

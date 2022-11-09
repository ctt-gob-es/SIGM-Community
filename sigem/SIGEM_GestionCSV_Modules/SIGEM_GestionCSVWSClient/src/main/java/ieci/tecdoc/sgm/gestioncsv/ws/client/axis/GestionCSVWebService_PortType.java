/**
 * GestionCSVWebService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.gestioncsv.ws.client.axis;

public interface GestionCSVWebService_PortType extends java.rmi.Remote {
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.StringB64 getContenidoDocumentoOriginal(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String id) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.BooleanRetorno existeContenidoDocumentoOriginal(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String id) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.InfoDocumentoCSVRetorno generarCSV(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, ieci.tecdoc.sgm.gestioncsv.ws.client.axis.InfoDocumentoCSVFormDTO infoDocumentoForm) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.InfoDocumentoCSVRetorno getInfoDocumentoByCSV(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String csv) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.DocumentoCSVRetorno getDocumentoByCSV(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String csv) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.RetornoServicio deleteInfoDocumento(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String id) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.BooleanRetorno existeContenidoDocumento(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String id) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.RetornoServicio writeDocumento(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String id, java.lang.String stringB64Encoded) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.RetornoServicio writeDocumentoOriginal(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String id, java.lang.String stringB64Encoded) throws java.rmi.RemoteException;
    public ieci.tecdoc.sgm.gestioncsv.ws.client.axis.StringB64 getContenidoDocumento(ieci.tecdoc.sgm.gestioncsv.ws.client.axis.Entidad entidad, java.lang.String id) throws java.rmi.RemoteException;
}

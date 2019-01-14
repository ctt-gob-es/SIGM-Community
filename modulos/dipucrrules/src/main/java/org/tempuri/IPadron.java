/**
 * IPadron.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface IPadron extends java.rmi.Remote {
    public org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ClsDocumentoResponse getDocumento(java.lang.String p_sCodInstitucion, java.lang.String p_sIdentificador, org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ETipoDocumento p_eTipoDocumento, java.lang.String p_sObservaciones, java.lang.String p_sEfecto, java.lang.Boolean p_bHojaCompleta) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ClsHabitanteResponse getNombrePersona(java.lang.String p_sCodInstitucion, java.lang.String p_sIdentificador) throws java.rmi.RemoteException;
}

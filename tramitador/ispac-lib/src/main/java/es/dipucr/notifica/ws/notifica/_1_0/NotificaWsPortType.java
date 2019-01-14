/**
 * NotificaWsPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public interface NotificaWsPortType extends java.rmi.Remote {
    public es.dipucr.notifica.ws.notifica._1_0.Resultado_alta altaEnvio(es.dipucr.notifica.ws.notifica._1_0.Tipo_envio envio_type, String apiKey) throws java.rmi.RemoteException;
    public es.dipucr.notifica.ws.notifica._1_0.Resultado_datado consultaDatadoEnvio(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException;
    public es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion consultaCertificacionEnvio(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException;
    public es.dipucr.notifica.ws.notifica._1_0.Resultado_estado consultaEstado(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException;
    public es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio infoEnvio(es.dipucr.notifica.ws.notifica._1_0.Info_envio info_envio, String apiKey) throws java.rmi.RemoteException;
    public es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies consultaCies(es.dipucr.notifica.ws.notifica._1_0.Consulta_cies consulta_cies, String apiKey) throws java.rmi.RemoteException;
    public es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos consultaOrganismosActivos(String apiKey) throws java.rmi.RemoteException;
}

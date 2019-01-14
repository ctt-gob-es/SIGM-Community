package es.dipucr.notifica.ws.notifica._1_0;

public class NotificaWsPortTypeProxy implements es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortType {
  private String _endpoint = null;
  private es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortType notificaWsPortType = null;
  
  public NotificaWsPortTypeProxy() {
    _initNotificaWsPortTypeProxy();
  }
  
  public NotificaWsPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initNotificaWsPortTypeProxy();
  }
    
  
  public String get_endpoint() {
	return _endpoint;
}

public void set_endpoint(String _endpoint) {
	this._endpoint = _endpoint;
}

private void _initNotificaWsPortTypeProxy() {
    try {
      notificaWsPortType = (new es.dipucr.notifica.ws.notifica._1_0.NotificaWsServiceLocator()).getNotificaWsPort();
      if (notificaWsPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)notificaWsPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)notificaWsPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (notificaWsPortType != null)
      ((javax.xml.rpc.Stub)notificaWsPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.notifica.ws.notifica._1_0.NotificaWsPortType getNotificaWsPortType() {
    if (notificaWsPortType == null)
      _initNotificaWsPortTypeProxy();
    return notificaWsPortType;
  }
  
  public es.dipucr.notifica.ws.notifica._1_0.Resultado_alta altaEnvio(es.dipucr.notifica.ws.notifica._1_0.Tipo_envio envio_type, String apiKey) throws java.rmi.RemoteException{
    if (notificaWsPortType == null)
      _initNotificaWsPortTypeProxy();
    return notificaWsPortType.altaEnvio(envio_type,apiKey);
  }
  
  public es.dipucr.notifica.ws.notifica._1_0.Resultado_datado consultaDatadoEnvio(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException{
    if (notificaWsPortType == null)
      _initNotificaWsPortTypeProxy();
    return notificaWsPortType.consultaDatadoEnvio(identificador_envio,apiKey);
  }
  
  public es.dipucr.notifica.ws.notifica._1_0.Resultado_certificacion consultaCertificacionEnvio(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException{
    if (notificaWsPortType == null)
      _initNotificaWsPortTypeProxy();
    return notificaWsPortType.consultaCertificacionEnvio(identificador_envio,apiKey);
  }
  
  public es.dipucr.notifica.ws.notifica._1_0.Resultado_estado consultaEstado(java.lang.String identificador_envio, String apiKey) throws java.rmi.RemoteException{
    if (notificaWsPortType == null)
      _initNotificaWsPortTypeProxy();
    return notificaWsPortType.consultaEstado(identificador_envio,apiKey);
  }
  
  public es.dipucr.notifica.ws.notifica._1_0.ResultadoInfoEnvio infoEnvio(es.dipucr.notifica.ws.notifica._1_0.Info_envio info_envio, String apiKey) throws java.rmi.RemoteException{
    if (notificaWsPortType == null)
      _initNotificaWsPortTypeProxy();
    return notificaWsPortType.infoEnvio(info_envio,apiKey);
  }
  
  public es.dipucr.notifica.ws.notifica._1_0.ResultadoGetCies consultaCies(es.dipucr.notifica.ws.notifica._1_0.Consulta_cies consulta_cies, String apiKey) throws java.rmi.RemoteException{
    if (notificaWsPortType == null)
      _initNotificaWsPortTypeProxy();
    return notificaWsPortType.consultaCies(consulta_cies,apiKey);
  }
  
  public es.dipucr.notifica.ws.notifica._1_0.Resultado_organismos_activos consultaOrganismosActivos(String apiKey) throws java.rmi.RemoteException{
    if (notificaWsPortType == null)
      _initNotificaWsPortTypeProxy();
    return notificaWsPortType.consultaOrganismosActivos(apiKey);
  }
  
  
}
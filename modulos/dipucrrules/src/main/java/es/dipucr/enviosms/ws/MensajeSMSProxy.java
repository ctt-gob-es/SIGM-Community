package es.dipucr.enviosms.ws;

public class MensajeSMSProxy implements es.dipucr.enviosms.ws.MensajeSMS {
  private String _endpoint = null;
  private es.dipucr.enviosms.ws.MensajeSMS mensajeSMS = null;
  
  public MensajeSMSProxy() {
    _initMensajeSMSProxy();
  }
  
  public MensajeSMSProxy(String endpoint) {
    _endpoint = endpoint;
    _initMensajeSMSProxy();
  }
  
  private void _initMensajeSMSProxy() {
    try {
      mensajeSMS = (new es.dipucr.enviosms.ws.MensajeSMSServiceLocator()).getMensajeSMS();
      if (mensajeSMS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mensajeSMS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mensajeSMS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mensajeSMS != null)
      ((javax.xml.rpc.Stub)mensajeSMS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.enviosms.ws.MensajeSMS getMensajeSMS() {
    if (mensajeSMS == null)
      _initMensajeSMSProxy();
    return mensajeSMS;
  }
  
  public es.dipucr.enviosms.objetos.Respuesta envioSMS(java.lang.String entidad, java.lang.String aplicacion, java.lang.String[] movil, java.lang.String mensaje) throws java.rmi.RemoteException{
    if (mensajeSMS == null)
      _initMensajeSMSProxy();
    return mensajeSMS.envioSMS(entidad, aplicacion, movil, mensaje);
  }
  
  public es.dipucr.enviosms.objetos.Respuesta envioSMSCertificado(java.lang.String entidad, java.lang.String aplicacion, java.lang.String[] movil, java.lang.String mensaje, java.lang.String certificadoMailAcuse) throws java.rmi.RemoteException{
    if (mensajeSMS == null)
      _initMensajeSMSProxy();
    return mensajeSMS.envioSMSCertificado(entidad, aplicacion, movil, mensaje, certificadoMailAcuse);
  }
  
  
}
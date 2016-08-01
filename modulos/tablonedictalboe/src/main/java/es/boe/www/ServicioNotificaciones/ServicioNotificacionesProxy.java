package es.boe.www.ServicioNotificaciones;

public class ServicioNotificacionesProxy implements es.boe.www.ServicioNotificaciones.ServicioNotificaciones {
  private String _endpoint = null;
  private es.boe.www.ServicioNotificaciones.ServicioNotificaciones servicioNotificaciones = null;
  
  public ServicioNotificacionesProxy() {
    _initServicioNotificacionesProxy();
  }
  
  public ServicioNotificacionesProxy(String endpoint) {
    _endpoint = endpoint;
    _initServicioNotificacionesProxy();
  }
  
  private void _initServicioNotificacionesProxy() {
    try {
      servicioNotificaciones = (new es.boe.www.ServicioNotificaciones.ServicioNotificacionesBOELocator()).getServicioNotificacionesPort();
      if (servicioNotificaciones != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)servicioNotificaciones)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)servicioNotificaciones)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (servicioNotificaciones != null)
      ((javax.xml.rpc.Stub)servicioNotificaciones)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.boe.www.ServicioNotificaciones.ServicioNotificaciones getServicioNotificaciones() {
    if (servicioNotificaciones == null)
      _initServicioNotificacionesProxy();
    return servicioNotificaciones;
  }
  
  public es.boe.www.ServicioNotificaciones.Respuesta envioAnuncios(java.lang.String envio) throws java.rmi.RemoteException{
    if (servicioNotificaciones == null)
      _initServicioNotificacionesProxy();
    return servicioNotificaciones.envioAnuncios(envio);
  }
  
  public es.boe.www.ServicioNotificaciones.Respuesta consultaEnvio(java.lang.String idEnvio) throws java.rmi.RemoteException{
    if (servicioNotificaciones == null)
      _initServicioNotificacionesProxy();
    return servicioNotificaciones.consultaEnvio(idEnvio);
  }
  
  public es.boe.www.ServicioNotificaciones.Respuesta consultaAnuncio(java.lang.String idAnuncio) throws java.rmi.RemoteException{
    if (servicioNotificaciones == null)
      _initServicioNotificacionesProxy();
    return servicioNotificaciones.consultaAnuncio(idAnuncio);
  }
  
  
}
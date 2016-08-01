package es.jccm.notificador.ws;

public class AplicacionesServiceProxy implements es.jccm.notificador.ws.AplicacionesService {
  private String _endpoint = null;
  private es.jccm.notificador.ws.AplicacionesService aplicacionesService = null;
  
  public AplicacionesServiceProxy() {
    _initAplicacionesServiceProxy();
  }
  
  public AplicacionesServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initAplicacionesServiceProxy();
  }
  
  private void _initAplicacionesServiceProxy() {
    try {
      aplicacionesService = (new es.jccm.notificador.ws.AplicacionesServiceServiceLocator()).getAplicacionesService();
      if (aplicacionesService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)aplicacionesService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)aplicacionesService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (aplicacionesService != null)
      ((javax.xml.rpc.Stub)aplicacionesService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.jccm.notificador.ws.AplicacionesService getAplicacionesService() {
    if (aplicacionesService == null)
      _initAplicacionesServiceProxy();
    return aplicacionesService;
  }
  
  public es.dipucr.notificador.model.EnvioWS[] notificar(java.lang.String id, java.lang.String[] dnis, java.lang.String procedimiento, java.lang.String expediente, java.lang.String descripcion, int diaCaducidad, int mesCaducidad, int anyoCaducidad, javax.activation.DataHandler notificacion, java.lang.String nombre, java.lang.String extension, java.lang.String idHashNotificacion, java.lang.String idCve) throws java.rmi.RemoteException{
    if (aplicacionesService == null)
      _initAplicacionesServiceProxy();
    return aplicacionesService.notificar(id, dnis, procedimiento, expediente, descripcion, diaCaducidad, mesCaducidad, anyoCaducidad, notificacion, nombre, extension, idHashNotificacion, idCve);
  }
  
  public es.dipucr.notificador.model.NotificacionWS recuperarEstadosNotificaciones(java.lang.String id, int idNotificacion) throws java.rmi.RemoteException{
    if (aplicacionesService == null)
      _initAplicacionesServiceProxy();
    return aplicacionesService.recuperarEstadosNotificaciones(id, idNotificacion);
  }
  
  public javax.activation.DataHandler recuperarAcuseFirmado(java.lang.String id, int idNotificacion) throws java.rmi.RemoteException{
    if (aplicacionesService == null)
      _initAplicacionesServiceProxy();
    return aplicacionesService.recuperarAcuseFirmado(id, idNotificacion);
  }
  
  public boolean modificarTercero(java.lang.String id, java.lang.String dni, java.lang.String email, java.lang.String telefono) throws java.rmi.RemoteException{
    if (aplicacionesService == null)
      _initAplicacionesServiceProxy();
    return aplicacionesService.modificarTercero(id, dni, email, telefono);
  }
  
  public es.dipucr.notificador.model.TerceroWS consultarDNI(java.lang.String id, java.lang.String dni) throws java.rmi.RemoteException{
    if (aplicacionesService == null)
      _initAplicacionesServiceProxy();
    return aplicacionesService.consultarDNI(id, dni);
  }
  
  public boolean altaTercero(java.lang.String id, java.lang.String dni, java.lang.String email, java.lang.String telefono) throws java.rmi.RemoteException{
    if (aplicacionesService == null)
      _initAplicacionesServiceProxy();
    return aplicacionesService.altaTercero(id, dni, email, telefono);
  }
  
  public boolean borrarTercero(java.lang.String id, java.lang.String dni) throws java.rmi.RemoteException{
    if (aplicacionesService == null)
      _initAplicacionesServiceProxy();
    return aplicacionesService.borrarTercero(id, dni);
  }
  
  
}
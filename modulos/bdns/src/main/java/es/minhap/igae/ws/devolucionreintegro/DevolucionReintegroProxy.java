package es.minhap.igae.ws.devolucionreintegro;

public class DevolucionReintegroProxy implements es.minhap.igae.ws.devolucionreintegro.DevolucionReintegro {
  private String _endpoint = null;
  private es.minhap.igae.ws.devolucionreintegro.DevolucionReintegro devolucionReintegro = null;
  
  public DevolucionReintegroProxy() {
    _initDevolucionReintegroProxy();
  }
  
  public DevolucionReintegroProxy(String endpoint) {
    _endpoint = endpoint;
    _initDevolucionReintegroProxy();
  }
  
  private void _initDevolucionReintegroProxy() {
    try {
      devolucionReintegro = (new es.minhap.igae.ws.devolucionreintegro.DevolucionReintegroServiceLocator()).getDevolucionReintegroSoap11();
      if (devolucionReintegro != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)devolucionReintegro)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)devolucionReintegro)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (devolucionReintegro != null)
      ((javax.xml.rpc.Stub)devolucionReintegro)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.minhap.igae.ws.devolucionreintegro.DevolucionReintegro getDevolucionReintegro() {
    if (devolucionReintegro == null)
      _initDevolucionReintegroProxy();
    return devolucionReintegro;
  }
  
  public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta peticion(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion peticion) throws java.rmi.RemoteException{
    if (devolucionReintegro == null)
      _initDevolucionReintegroProxy();
    return devolucionReintegro.peticion(peticion);
  }
  
  public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.confirmacionPeticion.ConfirmacionPeticion peticionAsincrona(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.PeticionAsincrona peticion) throws java.rmi.RemoteException{
    if (devolucionReintegro == null)
      _initDevolucionReintegroProxy();
    return devolucionReintegro.peticionAsincrona(peticion);
  }
  
  public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta solicitudRespuesta(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.solicitudRespuesta.SolicitudRespuesta solicitudRespuesta) throws java.rmi.RemoteException{
    if (devolucionReintegro == null)
      _initDevolucionReintegroProxy();
    return devolucionReintegro.solicitudRespuesta(solicitudRespuesta);
  }
  
  
}
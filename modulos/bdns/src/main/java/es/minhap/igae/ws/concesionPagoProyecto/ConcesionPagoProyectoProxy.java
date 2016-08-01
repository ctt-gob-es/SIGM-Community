package es.minhap.igae.ws.concesionPagoProyecto;

public class ConcesionPagoProyectoProxy implements es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyecto {
  private String _endpoint = null;
  private es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyecto concesionPagoProyecto = null;
  
  public ConcesionPagoProyectoProxy() {
    _initConcesionPagoProyectoProxy();
  }
  
  public ConcesionPagoProyectoProxy(String endpoint) {
    _endpoint = endpoint;
    _initConcesionPagoProyectoProxy();
  }
  
  private void _initConcesionPagoProyectoProxy() {
    try {
      concesionPagoProyecto = (new es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyectoServiceLocator()).getConcesionPagoProyectoSoap11();
      if (concesionPagoProyecto != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)concesionPagoProyecto)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)concesionPagoProyecto)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (concesionPagoProyecto != null)
      ((javax.xml.rpc.Stub)concesionPagoProyecto)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.minhap.igae.ws.concesionPagoProyecto.ConcesionPagoProyecto getConcesionPagoProyecto() {
    if (concesionPagoProyecto == null)
      _initConcesionPagoProyectoProxy();
    return concesionPagoProyecto;
  }
  
  public concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta peticion(concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion peticion) throws java.rmi.RemoteException{
    if (concesionPagoProyecto == null)
      _initConcesionPagoProyectoProxy();
    return concesionPagoProyecto.peticion(peticion);
  }
  
  public concesion.es.redsara.intermediacion.scsp.esquemas.V3.confirmacionPeticion.ConfirmacionPeticion peticionAsincrona(concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.PeticionAsincrona peticion) throws java.rmi.RemoteException{
    if (concesionPagoProyecto == null)
      _initConcesionPagoProyectoProxy();
    return concesionPagoProyecto.peticionAsincrona(peticion);
  }
  
  public concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta solicitudRespuesta(concesion.es.redsara.intermediacion.scsp.esquemas.V3.solicitudRespuesta.SolicitudRespuesta solicitudRespuesta) throws java.rmi.RemoteException{
    if (concesionPagoProyecto == null)
      _initConcesionPagoProyectoProxy();
    return concesionPagoProyecto.solicitudRespuesta(solicitudRespuesta);
  }
  
  
}
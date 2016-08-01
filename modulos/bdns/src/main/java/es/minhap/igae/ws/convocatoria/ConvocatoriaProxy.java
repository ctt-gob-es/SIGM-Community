package es.minhap.igae.ws.convocatoria;

public class ConvocatoriaProxy implements es.minhap.igae.ws.convocatoria.Convocatoria {
  private String _endpoint = null;
  private es.minhap.igae.ws.convocatoria.Convocatoria convocatoria = null;
  
  public ConvocatoriaProxy() {
    _initConvocatoriaProxy();
  }
  
  public ConvocatoriaProxy(String endpoint) {
    _endpoint = endpoint;
    _initConvocatoriaProxy();
  }
  
  private void _initConvocatoriaProxy() {
    try {
      convocatoria = (new es.minhap.igae.ws.convocatoria.ConvocatoriaServiceLocator()).getConvocatoriaSoap11();
      if (convocatoria != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)convocatoria)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)convocatoria)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (convocatoria != null)
      ((javax.xml.rpc.Stub)convocatoria)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.minhap.igae.ws.convocatoria.Convocatoria getConvocatoria() {
    if (convocatoria == null)
      _initConvocatoriaProxy();
    return convocatoria;
  }
  
  public conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta peticion(conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion peticion) throws java.rmi.RemoteException{
    if (convocatoria == null)
      _initConvocatoriaProxy();
    return convocatoria.peticion(peticion);
  }
  
  public conv.es.redsara.intermediacion.scsp.esquemas.V3.confirmacionPeticion.ConfirmacionPeticion peticionAsincrona(conv.es.redsara.intermediacion.scsp.esquemas.V3.peticion.PeticionAsincrona peticion) throws java.rmi.RemoteException{
    if (convocatoria == null)
      _initConvocatoriaProxy();
    return convocatoria.peticionAsincrona(peticion);
  }
  
  public conv.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta solicitudRespuesta(conv.es.redsara.intermediacion.scsp.esquemas.V3.solicitudRespuesta.SolicitudRespuesta solicitudRespuesta) throws java.rmi.RemoteException{
    if (convocatoria == null)
      _initConvocatoriaProxy();
    return convocatoria.solicitudRespuesta(solicitudRespuesta);
  }
  
  
}
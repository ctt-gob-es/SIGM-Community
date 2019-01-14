package es.minhap.igae.ws.datosPersonales;

public class DatosPersonalesProxy implements es.minhap.igae.ws.datosPersonales.DatosPersonales {
  private String _endpoint = null;
  private es.minhap.igae.ws.datosPersonales.DatosPersonales datosPersonales = null;
  
  public DatosPersonalesProxy() {
    _initDatosPersonalesProxy();
  }
  
  public DatosPersonalesProxy(String endpoint) {
    _endpoint = endpoint;
    _initDatosPersonalesProxy();
  }
  
  private void _initDatosPersonalesProxy() {
    try {
      datosPersonales = (new es.minhap.igae.ws.datosPersonales.DatosPersonalesServiceLocator()).getDatosPersonalesSoap11();
      if (datosPersonales != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)datosPersonales)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)datosPersonales)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (datosPersonales != null)
      ((javax.xml.rpc.Stub)datosPersonales)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.minhap.igae.ws.datosPersonales.DatosPersonales getDatosPersonales() {
    if (datosPersonales == null)
      _initDatosPersonalesProxy();
    return datosPersonales;
  }
  
  public datosper.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta peticion(datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.Peticion peticion) throws java.rmi.RemoteException{
    if (datosPersonales == null)
      _initDatosPersonalesProxy();
    return datosPersonales.peticion(peticion);
  }
  
  public datosper.es.redsara.intermediacion.scsp.esquemas.V3.confirmacionPeticion.ConfirmacionPeticion peticionAsincrona(datosper.es.redsara.intermediacion.scsp.esquemas.V3.peticion.PeticionAsincrona peticion) throws java.rmi.RemoteException{
    if (datosPersonales == null)
      _initDatosPersonalesProxy();
    return datosPersonales.peticionAsincrona(peticion);
  }
  
  public datosper.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Respuesta solicitudRespuesta(datosper.es.redsara.intermediacion.scsp.esquemas.V3.solicitudRespuesta.SolicitudRespuesta solicitudRespuesta) throws java.rmi.RemoteException{
    if (datosPersonales == null)
      _initDatosPersonalesProxy();
    return datosPersonales.solicitudRespuesta(solicitudRespuesta);
  }
  
  
}
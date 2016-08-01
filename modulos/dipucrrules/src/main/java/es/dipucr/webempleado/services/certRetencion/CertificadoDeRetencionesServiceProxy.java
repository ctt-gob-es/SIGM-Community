package es.dipucr.webempleado.services.certRetencion;

public class CertificadoDeRetencionesServiceProxy implements es.dipucr.webempleado.services.certRetencion.CertificadoDeRetencionesService {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.certRetencion.CertificadoDeRetencionesService certificadoDeRetencionesService = null;
  
  public CertificadoDeRetencionesServiceProxy() {
    _initCertificadoDeRetencionesServiceProxy();
  }
  
  public CertificadoDeRetencionesServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCertificadoDeRetencionesServiceProxy();
  }
  
  private void _initCertificadoDeRetencionesServiceProxy() {
    try {
      certificadoDeRetencionesService = (new es.dipucr.webempleado.services.certRetencion.CertificadoDeRetencionesServiceServiceLocator()).getCertificadoDeRetencionesService();
      if (certificadoDeRetencionesService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)certificadoDeRetencionesService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)certificadoDeRetencionesService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (certificadoDeRetencionesService != null)
      ((javax.xml.rpc.Stub)certificadoDeRetencionesService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.certRetencion.CertificadoDeRetencionesService getCertificadoDeRetencionesService() {
    if (certificadoDeRetencionesService == null)
      _initCertificadoDeRetencionesServiceProxy();
    return certificadoDeRetencionesService;
  }
  
  public es.dipucr.webempleado.services.certRetencion.ObjetoCertificadoDataHandler generarCertificadosRetenciones(java.lang.String nif) throws java.rmi.RemoteException{
    if (certificadoDeRetencionesService == null)
      _initCertificadoDeRetencionesServiceProxy();
    return certificadoDeRetencionesService.generarCertificadosRetenciones(nif);
  }
  
  public void enviarCertificadosRetenciones(es.dipucr.webempleado.services.certRetencion.ObjetoCertificadoDataHandler dataHandlerFirmados) throws java.rmi.RemoteException{
    if (certificadoDeRetencionesService == null)
      _initCertificadoDeRetencionesServiceProxy();
    certificadoDeRetencionesService.enviarCertificadosRetenciones(dataHandlerFirmados);
  }
  
  public java.lang.String[] dameNIF() throws java.rmi.RemoteException{
    if (certificadoDeRetencionesService == null)
      _initCertificadoDeRetencionesServiceProxy();
    return certificadoDeRetencionesService.dameNIF();
  }
  
  
}
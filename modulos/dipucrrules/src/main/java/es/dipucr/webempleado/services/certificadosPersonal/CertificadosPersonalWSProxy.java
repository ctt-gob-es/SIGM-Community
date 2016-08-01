package es.dipucr.webempleado.services.certificadosPersonal;

public class CertificadosPersonalWSProxy implements es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWS {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWS certificadosPersonalWS = null;
  
  public CertificadosPersonalWSProxy() {
    _initCertificadosPersonalWSProxy();
  }
  
  public CertificadosPersonalWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initCertificadosPersonalWSProxy();
  }
  
  private void _initCertificadosPersonalWSProxy() {
    try {
      certificadosPersonalWS = (new es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWSServiceLocator()).getCertificadosPersonalWS();
      if (certificadosPersonalWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)certificadosPersonalWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)certificadosPersonalWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (certificadosPersonalWS != null)
      ((javax.xml.rpc.Stub)certificadosPersonalWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWS getCertificadosPersonalWS() {
    if (certificadosPersonalWS == null)
      _initCertificadosPersonalWSProxy();
    return certificadosPersonalWS;
  }
  
  public es.dipucr.webempleado.services.certificadosPersonal.ObjetoCertificadoDataHandler generarCertificado(java.lang.String nif, java.lang.String nombre, java.lang.String donde, int tipoCert) throws java.rmi.RemoteException{
    if (certificadosPersonalWS == null)
      _initCertificadosPersonalWSProxy();
    return certificadosPersonalWS.generarCertificado(nif, nombre, donde, tipoCert);
  }
  
  public es.dipucr.webempleado.services.certificadosPersonal.ItemLista[] getTiposCertificado() throws java.rmi.RemoteException{
    if (certificadosPersonalWS == null)
      _initCertificadosPersonalWSProxy();
    return certificadosPersonalWS.getTiposCertificado();
  }
  
  
}
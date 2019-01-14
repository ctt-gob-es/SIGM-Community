package es.dipucr.verifdatos.services;

public class ScspProxy implements es.dipucr.verifdatos.services.Scsp {
  private String _endpoint = null;
  private es.dipucr.verifdatos.services.Scsp scsp = null;
  
  public ScspProxy() {
    _initScspProxy();
  }
  
  public ScspProxy(String endpoint) {
    _endpoint = endpoint;
    _initScspProxy();
  }
  
  private void _initScspProxy() {
    try {
      scsp = (new es.dipucr.verifdatos.services.ScspServiceLocator()).getScsp();
      if (scsp != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)scsp)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)scsp)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (scsp != null)
      ((javax.xml.rpc.Stub)scsp)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.verifdatos.services.Scsp getScsp() {
    if (scsp == null)
      _initScspProxy();
    return scsp;
  }
  
  public java.lang.String getEmisorCertificadoByNombre(java.lang.String nombreEntidad, java.lang.String emisorServicio) throws java.rmi.RemoteException{
    if (scsp == null)
      _initScspProxy();
    return scsp.getEmisorCertificadoByNombre(nombreEntidad, emisorServicio);
  }
  
  public java.lang.String[] getCoreServByCodCertificadoDescriVersion(java.lang.String nombreEntidad, java.lang.String codCertificado) throws java.rmi.RemoteException{
    if (scsp == null)
      _initScspProxy();
    return scsp.getCoreServByCodCertificadoDescriVersion(nombreEntidad, codCertificado);
  }
  
  
}
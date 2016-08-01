package es.dipucr.sede.services.bop.enviarBOPSedeWS;

public class EnviarBOPSedeWSProxy implements es.dipucr.sede.services.bop.enviarBOPSedeWS.EnviarBOPSedeWS {
  private String _endpoint = null;
  private es.dipucr.sede.services.bop.enviarBOPSedeWS.EnviarBOPSedeWS enviarBOPSedeWS = null;
  
  public EnviarBOPSedeWSProxy() {
    _initEnviarBOPSedeWSProxy();
  }
  
  public EnviarBOPSedeWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initEnviarBOPSedeWSProxy();
  }
  
  private void _initEnviarBOPSedeWSProxy() {
    try {
      enviarBOPSedeWS = (new es.dipucr.sede.services.bop.enviarBOPSedeWS.EnviarBOPSedeWSServiceLocator()).getenviarBOPSedeWS();
      if (enviarBOPSedeWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)enviarBOPSedeWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)enviarBOPSedeWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (enviarBOPSedeWS != null)
      ((javax.xml.rpc.Stub)enviarBOPSedeWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.sede.services.bop.enviarBOPSedeWS.EnviarBOPSedeWS getEnviarBOPSedeWS() {
    if (enviarBOPSedeWS == null)
      _initEnviarBOPSedeWSProxy();
    return enviarBOPSedeWS;
  }
  
  public int publicarHeaderBOPSede(es.dipucr.sede.services.bop.enviarBOPSedeWS.HeaderBOP cabeceraBOP) throws java.rmi.RemoteException{
    if (enviarBOPSedeWS == null)
      _initEnviarBOPSedeWSProxy();
    return enviarBOPSedeWS.publicarHeaderBOPSede(cabeceraBOP);
  }
  
  public int publicarAnunciosBOPSede(es.dipucr.sede.services.bop.enviarBOPSedeWS.AnuncioBOP[] anunciosBOP) throws java.rmi.RemoteException{
    if (enviarBOPSedeWS == null)
      _initEnviarBOPSedeWSProxy();
    return enviarBOPSedeWS.publicarAnunciosBOPSede(anunciosBOP);
  }
  
  
}
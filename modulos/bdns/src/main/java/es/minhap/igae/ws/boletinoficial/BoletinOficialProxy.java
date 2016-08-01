package es.minhap.igae.ws.boletinoficial;

public class BoletinOficialProxy implements es.minhap.igae.ws.boletinoficial.BoletinOficial {
  private String _endpoint = null;
  private es.minhap.igae.ws.boletinoficial.BoletinOficial boletinOficial = null;
  
  public BoletinOficialProxy() {
    _initBoletinOficialProxy();
  }
  
  public BoletinOficialProxy(String endpoint) {
    _endpoint = endpoint;
    _initBoletinOficialProxy();
  }
  
  private void _initBoletinOficialProxy() {
    try {
      boletinOficial = (new es.minhap.igae.ws.boletinoficial.BoletinOficialServiceLocator()).getBoletinOficialSoap11();
      if (boletinOficial != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)boletinOficial)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)boletinOficial)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (boletinOficial != null)
      ((javax.xml.rpc.Stub)boletinOficial)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.minhap.igae.ws.boletinoficial.BoletinOficial getBoletinOficial() {
    if (boletinOficial == null)
      _initBoletinOficialProxy();
    return boletinOficial;
  }
  
  public BDNS.respuestaAnuncio.RespuestaAnuncio peticionAnuncio(BDNS.peticionAnuncio.PeticionAnuncio peticionAnuncio) throws java.rmi.RemoteException{
    if (boletinOficial == null)
      _initBoletinOficialProxy();
    return boletinOficial.peticionAnuncio(peticionAnuncio);
  }
  
  public BDNS.confirmacionAnuncio.ConfirmacionAnuncio publicacionAnuncio(BDNS.publicacionAnuncio.PublicacionAnuncio publicacionAnuncio) throws java.rmi.RemoteException{
    if (boletinOficial == null)
      _initBoletinOficialProxy();
    return boletinOficial.publicacionAnuncio(publicacionAnuncio);
  }
  
  
}
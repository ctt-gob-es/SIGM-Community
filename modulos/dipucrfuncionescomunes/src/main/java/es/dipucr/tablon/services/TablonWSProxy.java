package es.dipucr.tablon.services;

public class TablonWSProxy implements es.dipucr.tablon.services.TablonWS {
  private String _endpoint = null;
  private es.dipucr.tablon.services.TablonWS tablonWS = null;
  
  public TablonWSProxy() {
    _initTablonWSProxy();
  }
  
  public TablonWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initTablonWSProxy();
  }
  
  private void _initTablonWSProxy() {
    try {
      tablonWS = (new es.dipucr.tablon.services.TablonWSServiceLocator()).getTablonWS();
      if (tablonWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)tablonWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)tablonWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (tablonWS != null)
      ((javax.xml.rpc.Stub)tablonWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.tablon.services.TablonWS getTablonWS() {
    if (tablonWS == null)
      _initTablonWSProxy();
    return tablonWS;
  }
  
  public int insertarPublicacion(java.lang.String codEntidad, java.lang.String titulo, java.lang.String descripcion, java.util.Calendar fechaFirma, java.lang.String codServicio, java.lang.String codCategoria, java.util.Calendar fechaIniVigencia, java.util.Calendar fechaFinVigencia, java.lang.String cve, java.lang.String hash, java.lang.String idTransaccion, java.lang.String numexp, java.lang.String servicioOtros, java.lang.String categoriaOtros, javax.activation.DataHandler dhPublicacion) throws java.rmi.RemoteException{
    if (tablonWS == null)
      _initTablonWSProxy();
    return tablonWS.insertarPublicacion(codEntidad, titulo, descripcion, fechaFirma, codServicio, codCategoria, fechaIniVigencia, fechaFinVigencia, cve, hash, idTransaccion, numexp, servicioOtros, categoriaOtros, dhPublicacion);
  }
  
  public boolean eliminarPublicacion(int idPublicacion) throws java.rmi.RemoteException{
    if (tablonWS == null)
      _initTablonWSProxy();
    return tablonWS.eliminarPublicacion(idPublicacion);
  }
  
  public es.dipucr.tablon.services.ItemLista getServicioByCodigo(java.lang.String codEntidad, java.lang.String codServicio) throws java.rmi.RemoteException{
    if (tablonWS == null)
      _initTablonWSProxy();
    return tablonWS.getServicioByCodigo(codEntidad, codServicio);
  }
  
  public es.dipucr.tablon.services.ItemLista getCategoriaByCodigo(java.lang.String codEntidad, java.lang.String codCategoria) throws java.rmi.RemoteException{
    if (tablonWS == null)
      _initTablonWSProxy();
    return tablonWS.getCategoriaByCodigo(codEntidad, codCategoria);
  }
  
  public boolean eliminarPublicaciones(java.lang.String codEntidad, java.lang.String numexp) throws java.rmi.RemoteException{
    if (tablonWS == null)
      _initTablonWSProxy();
    return tablonWS.eliminarPublicaciones(codEntidad, numexp);
  }
  
  public es.dipucr.tablon.services.ItemLista[] getServicios(java.lang.String codEntidad) throws java.rmi.RemoteException{
    if (tablonWS == null)
      _initTablonWSProxy();
    return tablonWS.getServicios(codEntidad);
  }
  
  public es.dipucr.tablon.services.ItemLista[] getCategorias(java.lang.String codEntidad) throws java.rmi.RemoteException{
    if (tablonWS == null)
      _initTablonWSProxy();
    return tablonWS.getCategorias(codEntidad);
  }
  
  
}
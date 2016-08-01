package es.dipucr.contratacion.services;

public class PlataformaContratacionProxy implements es.dipucr.contratacion.services.PlataformaContratacion {
  private String _endpoint = null;
  private es.dipucr.contratacion.services.PlataformaContratacion plataformaContratacion = null;
  
  public PlataformaContratacionProxy() {
    _initPlataformaContratacionProxy();
  }
  
  public PlataformaContratacionProxy(String endpoint) {
    _endpoint = endpoint;
    _initPlataformaContratacionProxy();
  }
  
  private void _initPlataformaContratacionProxy() {
    try {
      plataformaContratacion = (new es.dipucr.contratacion.services.PlataformaContratacionServiceLocator()).getPlataformaContratacion();
      if (plataformaContratacion != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)plataformaContratacion)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)plataformaContratacion)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (plataformaContratacion != null)
      ((javax.xml.rpc.Stub)plataformaContratacion)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.contratacion.services.PlataformaContratacion getPlataformaContratacion() {
    if (plataformaContratacion == null)
      _initPlataformaContratacionProxy();
    return plataformaContratacion;
  }
  
  public es.dipucr.contratacion.resultadoBeans.Resultado anuncioAdjudicacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioAdjudicacionBean anuncioAdjudicacion, java.lang.String publishedByUser) throws java.rmi.RemoteException{
    if (plataformaContratacion == null)
      _initPlataformaContratacionProxy();
    return plataformaContratacion.anuncioAdjudicacion(entidad, anuncioAdjudicacion, publishedByUser);
  }
  
  public es.dipucr.contratacion.resultadoBeans.Resultado consultarDatosAlta(java.lang.String entidad, java.lang.String numexp, java.lang.String publishedByUser) throws java.rmi.RemoteException{
    if (plataformaContratacion == null)
      _initPlataformaContratacionProxy();
    return plataformaContratacion.consultarDatosAlta(entidad, numexp, publishedByUser);
  }
  
  public es.dipucr.contratacion.resultadoBeans.Resultado envioOtrosDocumentosLicitacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.Documento documentoAdicional, java.lang.String publishedByUser) throws java.rmi.RemoteException{
    if (plataformaContratacion == null)
      _initPlataformaContratacionProxy();
    return plataformaContratacion.envioOtrosDocumentosLicitacion(entidad, documentoAdicional, publishedByUser);
  }
  
  public es.dipucr.contratacion.resultadoBeans.Resultado envioAnalisisPrevio(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioPrevioBean analisisPrevio, java.lang.String publishedByUser) throws java.rmi.RemoteException{
    if (plataformaContratacion == null)
      _initPlataformaContratacionProxy();
    return plataformaContratacion.envioAnalisisPrevio(entidad, analisisPrevio, publishedByUser);
  }
  
  public es.dipucr.contratacion.resultadoBeans.Resultado envioPliegos(java.lang.String entidad, es.dipucr.contratacion.client.beans.PliegoBean pliego, java.lang.String publishedByUser) throws java.rmi.RemoteException{
    if (plataformaContratacion == null)
      _initPlataformaContratacionProxy();
    return plataformaContratacion.envioPliegos(entidad, pliego, publishedByUser);
  }
  
  public es.dipucr.contratacion.resultadoBeans.Resultado estadoExpediente(java.lang.String entidad, java.lang.String numexp, java.lang.String publishedByUser) throws java.rmi.RemoteException{
    if (plataformaContratacion == null)
      _initPlataformaContratacionProxy();
    return plataformaContratacion.estadoExpediente(entidad, numexp, publishedByUser);
  }
  
  public es.dipucr.contratacion.resultadoBeans.Resultado envioPublicacionAnuncioLicitacion(java.lang.String entidad, es.dipucr.contratacion.client.beans.AnuncioLicitacionBean anuncioLicitacion, java.lang.String publishedByUser) throws java.rmi.RemoteException{
    if (plataformaContratacion == null)
      _initPlataformaContratacionProxy();
    return plataformaContratacion.envioPublicacionAnuncioLicitacion(entidad, anuncioLicitacion, publishedByUser);
  }
  
  
}
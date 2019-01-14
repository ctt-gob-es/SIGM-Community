package es.dipucr.webempleado.services.avisos;

public class InsertarAvisoProxy implements es.dipucr.webempleado.services.avisos.InsertarAviso {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.avisos.InsertarAviso insertarAviso = null;
  
  public InsertarAvisoProxy() {
    _initInsertarAvisoProxy();
  }
  
  public InsertarAvisoProxy(String endpoint) {
    _endpoint = endpoint;
    _initInsertarAvisoProxy();
  }
  
  private void _initInsertarAvisoProxy() {
    try {
      insertarAviso = (new es.dipucr.webempleado.services.avisos.InsertarAvisoServiceLocator()).getInsertarAviso();
      if (insertarAviso != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)insertarAviso)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)insertarAviso)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (insertarAviso != null)
      ((javax.xml.rpc.Stub)insertarAviso)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.avisos.InsertarAviso getInsertarAviso() {
    if (insertarAviso == null)
      _initInsertarAvisoProxy();
    return insertarAviso;
  }
  
  public int nuevoAvisoUsuario(java.lang.String asunto, java.lang.String cuerpo, java.lang.String nif) throws java.rmi.RemoteException{
    if (insertarAviso == null)
      _initInsertarAvisoProxy();
    return insertarAviso.nuevoAvisoUsuario(asunto, cuerpo, nif);
  }
  
  public int nuevoAvisoGeneral(java.lang.String asunto, java.lang.String cuerpo) throws java.rmi.RemoteException{
    if (insertarAviso == null)
      _initInsertarAvisoProxy();
    return insertarAviso.nuevoAvisoGeneral(asunto, cuerpo);
  }
  
  public boolean comprobarUsuario(java.lang.String nif) throws java.rmi.RemoteException{
    if (insertarAviso == null)
      _initInsertarAvisoProxy();
    return insertarAviso.comprobarUsuario(nif);
  }
  
  
}
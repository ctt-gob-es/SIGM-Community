package es.dipucr.webempleado.services.trienios;

public class CumplimientoTrieniosServiceProxy implements es.dipucr.webempleado.services.trienios.CumplimientoTrieniosService {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.trienios.CumplimientoTrieniosService cumplimientoTrieniosService = null;
  
  public CumplimientoTrieniosServiceProxy() {
    _initCumplimientoTrieniosServiceProxy();
  }
  
  public CumplimientoTrieniosServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCumplimientoTrieniosServiceProxy();
  }
  
  private void _initCumplimientoTrieniosServiceProxy() {
    try {
      cumplimientoTrieniosService = (new es.dipucr.webempleado.services.trienios.CumplimientoTrieniosServiceServiceLocator()).getCumplimientoTrieniosService();
      if (cumplimientoTrieniosService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cumplimientoTrieniosService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cumplimientoTrieniosService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cumplimientoTrieniosService != null)
      ((javax.xml.rpc.Stub)cumplimientoTrieniosService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.trienios.CumplimientoTrieniosService getCumplimientoTrieniosService() {
    if (cumplimientoTrieniosService == null)
      _initCumplimientoTrieniosServiceProxy();
    return cumplimientoTrieniosService;
  }
  
  public boolean insertarVidaAdministrativa(java.lang.String dni, java.lang.String fecha_decreto, java.lang.String num_trienio, java.lang.String fecha_trienio, java.lang.String no_expediente, java.lang.String grupo) throws java.rmi.RemoteException{
    if (cumplimientoTrieniosService == null)
      _initCumplimientoTrieniosServiceProxy();
    return cumplimientoTrieniosService.insertarVidaAdministrativa(dni, fecha_decreto, num_trienio, fecha_trienio, no_expediente, grupo);
  }
  
  public es.dipucr.webempleado.model.mapping.InfoTrienios[] listaCumplimientoTrienios(int tipoEmpleado, java.lang.String fecha) throws java.rmi.RemoteException{
    if (cumplimientoTrieniosService == null)
      _initCumplimientoTrieniosServiceProxy();
    return cumplimientoTrieniosService.listaCumplimientoTrienios(tipoEmpleado, fecha);
  }
  
  public boolean insertarApunteTrienio(java.lang.String dni, java.lang.String num_trienio, java.lang.String fecha_trienio, java.lang.String fecha_decreto, java.lang.String grupo, java.lang.String numExp) throws java.rmi.RemoteException{
    if (cumplimientoTrieniosService == null)
      _initCumplimientoTrieniosServiceProxy();
    return cumplimientoTrieniosService.insertarApunteTrienio(dni, num_trienio, fecha_trienio, fecha_decreto, grupo, numExp);
  }
  
  
}
package es.dipucr.webempleado.services.licencias;

public class LicenciasWSProxy implements es.dipucr.webempleado.services.licencias.LicenciasWS {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.licencias.LicenciasWS licenciasWS = null;
  
  public LicenciasWSProxy() {
    _initLicenciasWSProxy();
  }
  
  public LicenciasWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initLicenciasWSProxy();
  }
  
  private void _initLicenciasWSProxy() {
    try {
      licenciasWS = (new es.dipucr.webempleado.services.licencias.LicenciasWSServiceLocator()).getLicenciasWS();
      if (licenciasWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)licenciasWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)licenciasWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (licenciasWS != null)
      ((javax.xml.rpc.Stub)licenciasWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.licencias.LicenciasWS getLicenciasWS() {
    if (licenciasWS == null)
      _initLicenciasWSProxy();
    return licenciasWS;
  }
  
  public boolean ponerLicenciaAnulada(java.lang.String nif, int ano, java.lang.String nlic, java.lang.String observaciones) throws java.rmi.RemoteException{
    if (licenciasWS == null)
      _initLicenciasWSProxy();
    return licenciasWS.ponerLicenciaAnulada(nif, ano, nlic, observaciones);
  }
  
  public boolean ponerLicenciaValidada(java.lang.String nif, int ano, java.lang.String nlic, boolean bFirmada, java.lang.String motivo, java.lang.String firmantes) throws java.rmi.RemoteException{
    if (licenciasWS == null)
      _initLicenciasWSProxy();
    return licenciasWS.ponerLicenciaValidada(nif, ano, nlic, bFirmada, motivo, firmantes);
  }
  
  public java.lang.String crearLicenciaPendiente(java.lang.String nif, java.lang.String tlic, int ano, java.util.Calendar finicio, java.util.Calendar ffinal, java.lang.String dias, java.lang.String observaciones, java.lang.String sigemNumexp, java.lang.String sigemNreg, java.util.Calendar sigemFreg) throws java.rmi.RemoteException{
    if (licenciasWS == null)
      _initLicenciasWSProxy();
    return licenciasWS.crearLicenciaPendiente(nif, tlic, ano, finicio, ffinal, dias, observaciones, sigemNumexp, sigemNreg, sigemFreg);
  }
  
  public boolean existenLicenciasCoincidentes(java.lang.String nif, int ano, java.util.Calendar finicio, java.util.Calendar ffinal) throws java.rmi.RemoteException{
    if (licenciasWS == null)
      _initLicenciasWSProxy();
    return licenciasWS.existenLicenciasCoincidentes(nif, ano, finicio, ffinal);
  }
  
  
}
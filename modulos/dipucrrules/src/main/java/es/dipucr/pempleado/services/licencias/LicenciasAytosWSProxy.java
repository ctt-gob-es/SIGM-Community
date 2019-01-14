package es.dipucr.pempleado.services.licencias;

public class LicenciasAytosWSProxy implements es.dipucr.pempleado.services.licencias.LicenciasAytosWS {
  private String _endpoint = null;
  private es.dipucr.pempleado.services.licencias.LicenciasAytosWS licenciasAytosWS = null;
  
  public LicenciasAytosWSProxy() {
    _initLicenciasAytosWSProxy();
  }
  
  public LicenciasAytosWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initLicenciasAytosWSProxy();
  }
  
  private void _initLicenciasAytosWSProxy() {
    try {
      licenciasAytosWS = (new es.dipucr.pempleado.services.licencias.LicenciasAytosWSServiceLocator()).getLicenciasAytosWS();
      if (licenciasAytosWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)licenciasAytosWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)licenciasAytosWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (licenciasAytosWS != null)
      ((javax.xml.rpc.Stub)licenciasAytosWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.pempleado.services.licencias.LicenciasAytosWS getLicenciasAytosWS() {
    if (licenciasAytosWS == null)
      _initLicenciasAytosWSProxy();
    return licenciasAytosWS;
  }
  
  public boolean ponerLicenciaAnulada(java.lang.String idEntidad, int idLicencia, java.lang.String observaciones) throws java.rmi.RemoteException{
    if (licenciasAytosWS == null)
      _initLicenciasAytosWSProxy();
    return licenciasAytosWS.ponerLicenciaAnulada(idEntidad, idLicencia, observaciones);
  }
  
  public boolean existenLicenciasCoincidentes(java.lang.String idEntidad, java.lang.String nif, java.lang.String anio, java.util.Calendar fechaInicio, java.util.Calendar fechaFin) throws java.rmi.RemoteException{
    if (licenciasAytosWS == null)
      _initLicenciasAytosWSProxy();
    return licenciasAytosWS.existenLicenciasCoincidentes(idEntidad, nif, anio, fechaInicio, fechaFin);
  }
  
  public int crearLicenciaPendiente(java.lang.String idEntidad, java.lang.String nif, java.lang.String tlic, java.lang.String anio, java.util.Calendar fechaInicio, java.util.Calendar fechaFin, int numDias, java.lang.String detalleDias, java.lang.String observaciones, java.lang.String sigemNumexp, java.lang.String sigemNreg, java.util.Calendar sigemFreg) throws java.rmi.RemoteException{
    if (licenciasAytosWS == null)
      _initLicenciasAytosWSProxy();
    return licenciasAytosWS.crearLicenciaPendiente(idEntidad, nif, tlic, anio, fechaInicio, fechaFin, numDias, detalleDias, observaciones, sigemNumexp, sigemNreg, sigemFreg);
  }
  
  public boolean ponerLicenciaValidada(java.lang.String idEntidad, int id, boolean bFirmada, java.lang.String motivo, java.lang.String firmantes) throws java.rmi.RemoteException{
    if (licenciasAytosWS == null)
      _initLicenciasAytosWSProxy();
    return licenciasAytosWS.ponerLicenciaValidada(idEntidad, id, bFirmada, motivo, firmantes);
  }
  
  
}
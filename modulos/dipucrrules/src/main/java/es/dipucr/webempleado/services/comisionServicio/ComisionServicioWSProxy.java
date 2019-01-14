package es.dipucr.webempleado.services.comisionServicio;

public class ComisionServicioWSProxy implements es.dipucr.webempleado.services.comisionServicio.ComisionServicioWS {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.comisionServicio.ComisionServicioWS comisionServicioWS = null;
  
  public ComisionServicioWSProxy() {
    _initComisionServicioWSProxy();
  }
  
  public ComisionServicioWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initComisionServicioWSProxy();
  }
  
  private void _initComisionServicioWSProxy() {
    try {
      comisionServicioWS = (new es.dipucr.webempleado.services.comisionServicio.ComisionServicioWSServiceLocator()).getComisionServicioWS();
      if (comisionServicioWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)comisionServicioWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)comisionServicioWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (comisionServicioWS != null)
      ((javax.xml.rpc.Stub)comisionServicioWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.comisionServicio.ComisionServicioWS getComisionServicioWS() {
    if (comisionServicioWS == null)
      _initComisionServicioWSProxy();
    return comisionServicioWS;
  }
  
  public boolean ponerComisionAnulada(int idComision) throws java.rmi.RemoteException{
    if (comisionServicioWS == null)
      _initComisionServicioWSProxy();
    return comisionServicioWS.ponerComisionAnulada(idComision);
  }
  
  public boolean existenComisionesCoincidentes(java.lang.String nif, java.util.Calendar dFechaInicio, java.util.Calendar dFechaFin) throws java.rmi.RemoteException{
    if (comisionServicioWS == null)
      _initComisionServicioWSProxy();
    return comisionServicioWS.existenComisionesCoincidentes(nif, dFechaInicio, dFechaFin);
  }
  
  public int crearComisionPendiente(java.lang.String nif, java.lang.String motivo, java.util.Calendar fechaInicio, java.lang.String horaInicio, java.util.Calendar fechaFin, java.lang.String horaFin, java.lang.String observaciones, java.lang.String nreg, java.lang.String numexp) throws java.rmi.RemoteException{
    if (comisionServicioWS == null)
      _initComisionServicioWSProxy();
    return comisionServicioWS.crearComisionPendiente(nif, motivo, fechaInicio, horaInicio, fechaFin, horaFin, observaciones, nreg, numexp);
  }
  
  public boolean cambiarEstadoComision(int idComision, boolean bFirmada, java.lang.String motivo, java.lang.String firmantes) throws java.rmi.RemoteException{
    if (comisionServicioWS == null)
      _initComisionServicioWSProxy();
    return comisionServicioWS.cambiarEstadoComision(idComision, bFirmada, motivo, firmantes);
  }
  
  
}
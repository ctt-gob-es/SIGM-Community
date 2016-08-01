package es.dipucr.factura.services.factura;

public class FacturaWSProxy implements es.dipucr.factura.services.factura.FacturaWS {
  private String _endpoint = null;
  private es.dipucr.factura.services.factura.FacturaWS facturaWS = null;
  
  public FacturaWSProxy() {
    _initFacturaWSProxy();
  }
  
  public FacturaWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initFacturaWSProxy();
  }
  
  private void _initFacturaWSProxy() {
    try {
      facturaWS = (new es.dipucr.factura.services.factura.FacturaWSServiceLocator()).getFacturaWS();
      if (facturaWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)facturaWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)facturaWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (facturaWS != null)
      ((javax.xml.rpc.Stub)facturaWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.factura.services.factura.FacturaWS getFacturaWS() {
    if (facturaWS == null)
      _initFacturaWSProxy();
    return facturaWS;
  }
  
  public void cambiarEstadoFactura(java.lang.String idEntidad, java.lang.String nreg, java.lang.String codEstado, int subtipo, java.lang.String motivo) throws java.rmi.RemoteException{
    if (facturaWS == null)
      _initFacturaWSProxy();
    facturaWS.cambiarEstadoFactura(idEntidad, nreg, codEstado, subtipo, motivo);
  }
  
  public void cambiarEstadoAnulacionFactura(java.lang.String idEntidad, java.lang.String nreg, java.lang.String codEstado, java.lang.String motivo) throws java.rmi.RemoteException{
    if (facturaWS == null)
      _initFacturaWSProxy();
    facturaWS.cambiarEstadoAnulacionFactura(idEntidad, nreg, codEstado, motivo);
  }
  
  public void enviarFacturaFirmada(java.lang.String idEntidad, java.lang.String nreg, byte[] data) throws java.rmi.RemoteException{
    if (facturaWS == null)
      _initFacturaWSProxy();
    facturaWS.enviarFacturaFirmada(idEntidad, nreg, data);
  }
  
  public es.dipucr.factura.domain.bean.ContratoMenorBean[] recuperarContratosMenores(java.lang.String idEntidad, java.lang.String ejercicio, java.util.Calendar dFechaInicio, java.util.Calendar dFechaFin, java.lang.String sImporteDesde, java.lang.String sImporteHasta, java.lang.String[] arrPartidasExluidas) throws java.rmi.RemoteException{
    if (facturaWS == null)
      _initFacturaWSProxy();
    return facturaWS.recuperarContratosMenores(idEntidad, ejercicio, dFechaInicio, dFechaFin, sImporteDesde, sImporteHasta, arrPartidasExluidas);
  }
  
  
}
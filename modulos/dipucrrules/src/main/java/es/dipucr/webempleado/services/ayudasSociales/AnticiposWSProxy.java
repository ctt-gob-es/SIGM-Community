package es.dipucr.webempleado.services.ayudasSociales;

public class AnticiposWSProxy implements es.dipucr.webempleado.services.ayudasSociales.AnticiposWS {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.ayudasSociales.AnticiposWS anticiposWS = null;
  
  public AnticiposWSProxy() {
    _initAnticiposWSProxy();
  }
  
  public AnticiposWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initAnticiposWSProxy();
  }
  
  private void _initAnticiposWSProxy() {
    try {
      anticiposWS = (new es.dipucr.webempleado.services.ayudasSociales.AnticiposWSServiceLocator()).getAnticiposWS();
      if (anticiposWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)anticiposWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)anticiposWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (anticiposWS != null)
      ((javax.xml.rpc.Stub)anticiposWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.ayudasSociales.AnticiposWS getAnticiposWS() {
    if (anticiposWS == null)
      _initAnticiposWSProxy();
    return anticiposWS;
  }
  
  public boolean comprobarAnticipoPendiente(java.lang.String nif) throws java.rmi.RemoteException{
    if (anticiposWS == null)
      _initAnticiposWSProxy();
    return anticiposWS.comprobarAnticipoPendiente(nif);
  }
  
  public boolean crearAnticipoPendiente(java.lang.String nif, double importeTotal, double importeMes, double importeUltimoMes, int meses, java.lang.String observaciones, java.lang.String sigemNreg, java.util.Calendar sigemFreg, java.lang.String sigemNumexp) throws java.rmi.RemoteException{
    if (anticiposWS == null)
      _initAnticiposWSProxy();
    return anticiposWS.crearAnticipoPendiente(nif, importeTotal, importeMes, importeUltimoMes, meses, observaciones, sigemNreg, sigemFreg, sigemNumexp);
  }
  
  public boolean ponerAnticipoValidado(java.lang.String nif, boolean bFirmada, java.lang.String motivo) throws java.rmi.RemoteException{
    if (anticiposWS == null)
      _initAnticiposWSProxy();
    return anticiposWS.ponerAnticipoValidado(nif, bFirmada, motivo);
  }
  
  
}
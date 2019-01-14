package org.tempuri;

public class IPadronProxy implements org.tempuri.IPadron {
  private String _endpoint = null;
  private org.tempuri.IPadron iPadron = null;
  
  public IPadronProxy() {
    _initIPadronProxy();
  }
  
  public IPadronProxy(String endpoint) {
    _endpoint = endpoint;
    _initIPadronProxy();
  }
  
  private void _initIPadronProxy() {
    try {
      iPadron = (new org.tempuri.PadronLocator()).getBasicHttpBinding_IPadron();
      if (iPadron != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iPadron)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iPadron)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iPadron != null)
      ((javax.xml.rpc.Stub)iPadron)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.IPadron getIPadron() {
    if (iPadron == null)
      _initIPadronProxy();
    return iPadron;
  }
  
  public org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ClsDocumentoResponse getDocumento(java.lang.String p_sCodInstitucion, java.lang.String p_sIdentificador, org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ETipoDocumento p_eTipoDocumento, java.lang.String p_sObservaciones, java.lang.String p_sEfecto, java.lang.Boolean p_bHojaCompleta) throws java.rmi.RemoteException{
    if (iPadron == null)
      _initIPadronProxy();
    return iPadron.getDocumento(p_sCodInstitucion, p_sIdentificador, p_eTipoDocumento, p_sObservaciones, p_sEfecto, p_bHojaCompleta);
  }
  
  public org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ClsHabitanteResponse getNombrePersona(java.lang.String p_sCodInstitucion, java.lang.String p_sIdentificador) throws java.rmi.RemoteException{
    if (iPadron == null)
      _initIPadronProxy();
    return iPadron.getNombrePersona(p_sCodInstitucion, p_sIdentificador);
  }
  
  
}
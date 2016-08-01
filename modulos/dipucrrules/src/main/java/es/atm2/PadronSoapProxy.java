package es.atm2;

public class PadronSoapProxy implements es.atm2.PadronSoap {
  private String _endpoint = null;
  private es.atm2.PadronSoap padronSoap = null;
  
  public PadronSoapProxy() {
    _initPadronSoapProxy();
  }
  
  public PadronSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initPadronSoapProxy();
  }
  
  private void _initPadronSoapProxy() {
    try {
      padronSoap = (new es.atm2.PadronLocator()).getPadronSoap();
      if (padronSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)padronSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)padronSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (padronSoap != null)
      ((javax.xml.rpc.Stub)padronSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.atm2.PadronSoap getPadronSoap() {
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap;
  }
  
  public es.atm2.ObtenerPersonaPorNombreResponseObtenerPersonaPorNombreResult obtenerPersonaPorNombre(java.lang.String codInstitucion, java.lang.String esFisica, java.lang.String nombre, java.lang.String apellido1, java.lang.String apellido2, int numItems, java.lang.String desdePosicion) throws java.rmi.RemoteException{
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap.obtenerPersonaPorNombre(codInstitucion, esFisica, nombre, apellido1, apellido2, numItems, desdePosicion);
  }
  
  public es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult obtenerPersonaPorNIF(java.lang.String p_sCodInstitucion, java.lang.String p_sPersonaFisica_o_Juridica, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException{
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap.obtenerPersonaPorNIF(p_sCodInstitucion, p_sPersonaFisica_o_Juridica, p_sDocumentoIdentidad);
  }
  
  public es.atm2.ObtenerDatosPadronalesPersonaResponseObtenerDatosPadronalesPersonaResult obtenerDatosPadronalesPersona(java.lang.String p_sCodInstitucion, java.lang.String p_sTipoDocumento, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException{
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap.obtenerDatosPadronalesPersona(p_sCodInstitucion, p_sTipoDocumento, p_sDocumentoIdentidad);
  }
  
  public es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult obtenerDatosConvivenciaPersona(java.lang.String p_sCodInstitucion, java.lang.String p_sTipoDocumento, java.lang.String p_sDocumentoIdentidad) throws java.rmi.RemoteException{
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap.obtenerDatosConvivenciaPersona(p_sCodInstitucion, p_sTipoDocumento, p_sDocumentoIdentidad);
  }
  
  public es.atm2.ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult obtenerCertificadoConvivencia(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException{
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap.obtenerCertificadoConvivencia(codInstitucion, tipoDocumento, documentoIdentidad);
  }
  
  public es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult obtenerVolanteConvivencia(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException{
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap.obtenerVolanteConvivencia(codInstitucion, tipoDocumento, documentoIdentidad);
  }
  
  public es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult obtenerCertificadoEmpadronamiento(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException{
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap.obtenerCertificadoEmpadronamiento(codInstitucion, tipoDocumento, documentoIdentidad);
  }
  
  public es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult obtenerVolanteEmpadronamiento(java.lang.String codInstitucion, java.lang.String tipoDocumento, java.lang.String documentoIdentidad) throws java.rmi.RemoteException{
    if (padronSoap == null)
      _initPadronSoapProxy();
    return padronSoap.obtenerVolanteEmpadronamiento(codInstitucion, tipoDocumento, documentoIdentidad);
  }
  
  
}
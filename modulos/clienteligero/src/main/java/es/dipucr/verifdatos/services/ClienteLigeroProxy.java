package es.dipucr.verifdatos.services;

public class ClienteLigeroProxy implements es.dipucr.verifdatos.services.ClienteLigero {
  private String _endpoint = null;
  private es.dipucr.verifdatos.services.ClienteLigero clienteLigero = null;
  
  public ClienteLigeroProxy() {
    _initClienteLigeroProxy();
  }
  
  public ClienteLigeroProxy(String endpoint) {
    _endpoint = endpoint;
    _initClienteLigeroProxy();
  }
  
  private void _initClienteLigeroProxy() {
    try {
      clienteLigero = (new es.dipucr.verifdatos.services.ClienteLigeroServiceLocator()).getClienteLigero();
      if (clienteLigero != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)clienteLigero)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)clienteLigero)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (clienteLigero != null)
      ((javax.xml.rpc.Stub)clienteLigero)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.verifdatos.services.ClienteLigero getClienteLigero() {
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero;
  }
  
  public java.lang.String[] getComunidad() throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getComunidad();
  }
  
  public int[] selectAllYear(java.lang.String nombreEntidad) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.selectAllYear(nombreEntidad);
  }
  
  public java.lang.String[] getProvincia(java.lang.String comunidad) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getProvincia(comunidad);
  }
  
  public java.lang.String[] getMunicipio(java.lang.String provincia) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getMunicipio(provincia);
  }
  
  public java.lang.String getXmlDatosEspecificos(java.lang.String nombreEntidad, java.lang.String procSelec) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getXmlDatosEspecificos(nombreEntidad, procSelec);
  }
  
  public java.lang.String[] getCertAutByCodCert(java.lang.String nombreEntidad, java.lang.String codigoCertificado) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getCertAutByCodCert(nombreEntidad, codigoCertificado);
  }
  
  public java.lang.String[] consultaProcedimientoByNIF(java.lang.String nombreEntidad, java.lang.String nifFuncionario, java.lang.String codigoProcedimiento) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.consultaProcedimientoByNIF(nombreEntidad, nifFuncionario, codigoProcedimiento);
  }
  
  public java.lang.String[] getUniversidadesCRUE() throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getUniversidadesCRUE();
  }
  
  public java.lang.String[] getPostprocessorDatosEspecificos(java.lang.String nombreEntidad, java.lang.String certificado) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getPostprocessorDatosEspecificos(nombreEntidad, certificado);
  }
  
  public java.lang.String[] getPreprocessorDatosEspecificos(java.lang.String nombreEntidad, java.lang.String certificado) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getPreprocessorDatosEspecificos(nombreEntidad, certificado);
  }
  
  public es.dipucr.verifdatos.model.dao.DatosGeograficosManager getDatosGeograficosManager(java.lang.String nombreEntidad) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getDatosGeograficosManager(nombreEntidad);
  }
  
  public java.lang.String getRegistroCivil(java.lang.String provincia, java.lang.String municipio) throws java.rmi.RemoteException{
    if (clienteLigero == null)
      _initClienteLigeroProxy();
    return clienteLigero.getRegistroCivil(provincia, municipio);
  }
  
  
}
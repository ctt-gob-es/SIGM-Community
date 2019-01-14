package es.dipucr.webempleado.services.personal;

public class PersonalWSProxy implements es.dipucr.webempleado.services.personal.PersonalWS {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.personal.PersonalWS personalWS = null;
  
  public PersonalWSProxy() {
    _initPersonalWSProxy();
  }
  
  public PersonalWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initPersonalWSProxy();
  }
  
  private void _initPersonalWSProxy() {
    try {
      personalWS = (new es.dipucr.webempleado.services.personal.PersonalWSServiceLocator()).getPersonalWS();
      if (personalWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)personalWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)personalWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (personalWS != null)
      ((javax.xml.rpc.Stub)personalWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.personal.PersonalWS getPersonalWS() {
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS;
  }
  
  public java.lang.String[] getGrupos() throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getGrupos();
  }
  
  public es.dipucr.webempleado.model.mapping.Provision[] getProvisiones() throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getProvisiones();
  }
  
  public es.dipucr.webempleado.model.mapping.Expediente getExpediente(java.lang.String nif) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getExpediente(nif);
  }
  
  public es.dipucr.webempleado.domain.beans.PeriodoLaboral[] getVidaLaboral(java.lang.String nif) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getVidaLaboral(nif);
  }
  
  public es.dipucr.webempleado.model.mapping.Puestos[] getPuestos(java.lang.String codServicio) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getPuestos(codServicio);
  }
  
  public es.dipucr.webempleado.model.mapping.HorariosId[] getHorarios(java.lang.String anio) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getHorarios(anio);
  }
  
  public es.dipucr.webempleado.model.mapping.Servicios[] getServiciosByCodigo() throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getServiciosByCodigo();
  }
  
  public es.dipucr.webempleado.model.mapping.Servicios[] getServiciosByNombre() throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getServiciosByNombre();
  }
  
  public es.dipucr.webempleado.domain.beans.PuestoComplementos[] getPuestosComplementosVacantes(java.lang.String codServicio, java.lang.String anio) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getPuestosComplementosVacantes(codServicio, anio);
  }
  
  public es.dipucr.webempleado.domain.beans.PuestoComplementos[] getPuestosComplementos(java.lang.String codServicio, java.lang.String anio) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getPuestosComplementos(codServicio, anio);
  }
  
  public es.dipucr.webempleado.domain.beans.PuestoComplementos getPuestoComplemento(java.lang.String codServicio, java.lang.String numPuesto, java.lang.String anio) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getPuestoComplemento(codServicio, numPuesto, anio);
  }
  
  public es.dipucr.webempleado.model.mapping.Provision getProvision(java.lang.String codigo) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getProvision(codigo);
  }
  
  public es.dipucr.webempleado.model.mapping.Escalas getEscala(java.lang.String codigo) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getEscala(codigo);
  }
  
  public es.dipucr.webempleado.model.mapping.Subescalas getSubescala(java.lang.String codigo) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getSubescala(codigo);
  }
  
  public es.dipucr.webempleado.model.mapping.Clases getClase(java.lang.String codigo) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getClase(codigo);
  }
  
  public es.dipucr.webempleado.model.mapping.Puestos getPuesto(java.lang.String codigo) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getPuesto(codigo);
  }
  
  public es.dipucr.webempleado.model.mapping.Categorias getCategoria(java.lang.String codigo) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getCategoria(codigo);
  }
  
  public es.dipucr.webempleado.model.mapping.HorariosId getTipoHorario(java.lang.String codigo, java.lang.String anio) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getTipoHorario(codigo, anio);
  }
  
  public es.dipucr.webempleado.model.mapping.TipoP getTipoPuesto(java.lang.String codigo) throws java.rmi.RemoteException{
    if (personalWS == null)
      _initPersonalWSProxy();
    return personalWS.getTipoPuesto(codigo);
  }
  
  
}
package ieci.tecdoc.sgm.tram.ws.server;

public class TramitacionWebServiceProxy implements ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService {
  private String _endpoint = null;
  private ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService tramitacionWebService = null;
  
  public TramitacionWebServiceProxy() {
    _initTramitacionWebServiceProxy();
  }
  
  public TramitacionWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initTramitacionWebServiceProxy();
  }
  
  private void _initTramitacionWebServiceProxy() {
    try {
      TramitacionWebServiceServiceLocator locator = new ieci.tecdoc.sgm.tram.ws.server.TramitacionWebServiceServiceLocator();
      locator.setTramitacionWebServiceEndpointAddress(this._endpoint);
      tramitacionWebService = locator.getTramitacionWebService();
      if (tramitacionWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)tramitacionWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)tramitacionWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (tramitacionWebService != null)
      ((javax.xml.rpc.Stub)tramitacionWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService getTramitacionWebService() {
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService;
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos getProcedimientosPorTipo(java.lang.String idEntidad, int tipoProc, java.lang.String nombre) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getProcedimientosPorTipo(idEntidad, tipoProc, nombre);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBProcedimientos getProcedimientos(java.lang.String idEntidad, java.lang.String[] idProcs) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getProcedimientos(idEntidad, idProcs);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Procedimiento getProcedimiento(java.lang.String idEntidad, java.lang.String idProc) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getProcedimiento(idEntidad, idProc);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Binario getFichero(java.lang.String idEntidad, java.lang.String guid) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getFichero(idEntidad, guid);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Binario getFicheroTemp(java.lang.String idEntidad, java.lang.String guid) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getFicheroTemp(idEntidad, guid);
  }
  
  public boolean setFicheroTemp(java.lang.String idEntidad, java.lang.String guid, byte[] data) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.setFicheroTemp(idEntidad, guid, data);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.InfoFichero getInfoFichero(java.lang.String idEntidad, java.lang.String guid) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getInfoFichero(idEntidad, guid);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.InfoOcupacion getInfoOcupacion(java.lang.String idEntidad) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getInfoOcupacion(idEntidad);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.RetornoServicio eliminaFicheros(java.lang.String idEntidad, java.lang.String[] guids) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.eliminaFicheros(idEntidad, guids);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.ListaIdentificadores getIdsExpedientes(java.lang.String idEntidad, java.lang.String idProc, java.util.Calendar fechaIni, java.util.Calendar fechaFin, int tipoOrd) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getIdsExpedientes(idEntidad, idProc, fechaIni, fechaFin, tipoOrd);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.ListaInfoBExpedientes getExpedientes(java.lang.String idEntidad, java.lang.String[] idExps) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getExpedientes(idEntidad, idExps);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Expediente getExpediente(java.lang.String idEntidad, java.lang.String idExp) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.getExpediente(idEntidad, idExp);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.RetornoServicio archivarExpedientes(java.lang.String idEntidad, java.lang.String[] idExps) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.archivarExpedientes(idEntidad, idExps);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Booleano iniciarExpediente(java.lang.String idEntidad, ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente datosComunes, java.lang.String datosEspecificos, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.iniciarExpediente(idEntidad, datosComunes, datosEspecificos, documentos);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Booleano anexarDocsExpediente(java.lang.String idEntidad, java.lang.String numExp, java.lang.String numReg, java.util.Calendar fechaReg, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.anexarDocsExpediente(idEntidad, numExp, numReg, fechaReg, documentos);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Cadena crearExpediente(java.lang.String idEntidad, ieci.tecdoc.sgm.tram.ws.server.DatosComunesExpediente datosComunes, java.lang.String datosEspecificos, ieci.tecdoc.sgm.tram.ws.server.DocumentoExpediente[] documentos, java.lang.String initSystem) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.crearExpediente(idEntidad, datosComunes, datosEspecificos, documentos, initSystem);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Booleano cambiarEstadoAdministrativo(java.lang.String idEntidad, java.lang.String numExp, java.lang.String estadoAdm) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.cambiarEstadoAdministrativo(idEntidad, numExp, estadoAdm);
  }
  
  public ieci.tecdoc.sgm.tram.ws.server.Booleano moverExpedienteAFase(java.lang.String idEntidad, java.lang.String numExp, java.lang.String idFaseCatalogo) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.moverExpedienteAFase(idEntidad, numExp, idFaseCatalogo);
  }
  
  public ieci.tdw.ispac.services.ws.server.Cadena busquedaAvanzada(java.lang.String idEntidad, java.lang.String groupName, java.lang.String searchFormName, java.lang.String searchXML, int domain) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.busquedaAvanzada(idEntidad, groupName, searchFormName, searchXML, domain);
  }
  
  public ieci.tdw.ispac.services.ws.server.Entero establecerDatosRegistroEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp, java.lang.String xmlDatosEspecificos) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.establecerDatosRegistroEntidad(idEntidad, nombreEntidad, numExp, xmlDatosEspecificos);
  }
  
  public ieci.tdw.ispac.services.ws.server.Cadena obtenerRegistroEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp, int idRegistro) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.obtenerRegistroEntidad(idEntidad, nombreEntidad, numExp, idRegistro);
  }
  
  public ieci.tdw.ispac.services.ws.server.Cadena obtenerRegistrosEntidad(java.lang.String idEntidad, java.lang.String nombreEntidad, java.lang.String numExp) throws java.rmi.RemoteException{
    if (tramitacionWebService == null)
      _initTramitacionWebServiceProxy();
    return tramitacionWebService.obtenerRegistrosEntidad(idEntidad, nombreEntidad, numExp);
  }
  
  
}
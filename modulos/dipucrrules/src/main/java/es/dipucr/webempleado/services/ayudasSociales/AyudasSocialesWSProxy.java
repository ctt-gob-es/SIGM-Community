package es.dipucr.webempleado.services.ayudasSociales;

public class AyudasSocialesWSProxy implements es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWS {
  private String _endpoint = null;
  private es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWS ayudasSocialesWS = null;
  
  public AyudasSocialesWSProxy() {
    _initAyudasSocialesWSProxy();
  }
  
  public AyudasSocialesWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initAyudasSocialesWSProxy();
  }
  
  private void _initAyudasSocialesWSProxy() {
    try {
      ayudasSocialesWS = (new es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWSServiceLocator()).getAyudasSocialesWS();
      if (ayudasSocialesWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)ayudasSocialesWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)ayudasSocialesWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (ayudasSocialesWS != null)
      ((javax.xml.rpc.Stub)ayudasSocialesWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWS getAyudasSocialesWS() {
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS;
  }
  
  public int crearAyuda(java.lang.String anoAyuda, int idConcepto, int idGrupo, java.lang.String nif, java.lang.String parentesco, java.lang.String numFactura, java.lang.String fechaFactura, double importe, double importeConcedido, java.lang.String fechaRegistro, java.lang.String beneficiario, java.lang.String fechaNacBeneficiario, java.lang.String email, java.lang.String telefono, java.lang.String observaciones, java.lang.String numRegistro, java.lang.String numexp, es.dipucr.webempleado.domain.beans.DocumentoAyudas[] arrDocumentos, java.lang.String tipoContrato, java.lang.String idConvocatoria) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.crearAyuda(anoAyuda, idConcepto, idGrupo, nif, parentesco, numFactura, fechaFactura, importe, importeConcedido, fechaRegistro, beneficiario, fechaNacBeneficiario, email, telefono, observaciones, numRegistro, numexp, arrDocumentos, tipoContrato, idConvocatoria);
  }
  
  public boolean anularAyuda(java.lang.String idAyuda) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.anularAyuda(idAyuda);
  }
  
  public es.dipucr.webempleado.model.mapping.Propuesta getPropuesta(int idPropuesta) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.getPropuesta(idPropuesta);
  }
  
  public es.dipucr.webempleado.domain.AyudaSocial[] getAyudasPropuesta(int idPropuesta) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.getAyudasPropuesta(idPropuesta);
  }
  
  public es.dipucr.webempleado.model.mapping.Convocatoria calcularPuntosTotales(int idConvocatoria) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.calcularPuntosTotales(idConvocatoria);
  }
  
  public es.dipucr.webempleado.model.mapping.Convocatoria getConvocatoriaByAnio(java.lang.String anio) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.getConvocatoriaByAnio(anio);
  }
  
  public es.dipucr.webempleado.domain.AyudaSocial[] getAyudasEstudiosConvocatoria(int idConvocatoria) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.getAyudasEstudiosConvocatoria(idConvocatoria);
  }
  
  public boolean crearBeneficiario(java.lang.String nif, java.lang.String parentesco, java.lang.String nombre, java.lang.String fechaNacimiento, java.lang.String numexp) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.crearBeneficiario(nif, parentesco, nombre, fechaNacimiento, numexp);
  }
  
  public es.dipucr.webempleado.domain.AyudaSocial[] getAyudasEstudiosAnio(java.lang.String anio) throws java.rmi.RemoteException{
    if (ayudasSocialesWS == null)
      _initAyudasSocialesWSProxy();
    return ayudasSocialesWS.getAyudasEstudiosAnio(anio);
  }
  
  
}
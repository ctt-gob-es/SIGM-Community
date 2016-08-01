package es.dipucr.services.server.planesProvinciales;

public class PlanProvincialServicioProxy implements es.dipucr.services.server.planesProvinciales.PlanProvincialServicio {
  private String _endpoint = null;
  private es.dipucr.services.server.planesProvinciales.PlanProvincialServicio planProvincialServicio = null;
  
  public PlanProvincialServicioProxy() {
    _initPlanProvincialServicioProxy();
  }
  
  public PlanProvincialServicioProxy(String endpoint) {
    _endpoint = endpoint;
    _initPlanProvincialServicioProxy();
  }
  
  private void _initPlanProvincialServicioProxy() {
    try {
      planProvincialServicio = (new es.dipucr.services.server.planesProvinciales.PlanProvincialServicioServiceLocator()).getPlanProvincialServicio();
      if (planProvincialServicio != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)planProvincialServicio)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)planProvincialServicio)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (planProvincialServicio != null)
      ((javax.xml.rpc.Stub)planProvincialServicio)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public es.dipucr.services.server.planesProvinciales.PlanProvincialServicio getPlanProvincialServicio() {
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio;
  }
  
  public es.dipucr.domain.planesProvinciales.PlanProvincial[] getPlanProvincial(java.lang.String nombreMunicipio, java.lang.String anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getPlanProvincial(nombreMunicipio, anio);
  }
  
  public es.dipucr.domain.planesProvinciales.PlanProvincial[] getPlanesProvincialesByAnio(java.lang.String anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getPlanesProvincialesByAnio(anio);
  }
  
  public es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales[] getAllMunicipios() throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getAllMunicipios();
  }
  
  public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getResumenPlanCooperacion(java.lang.String anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getResumenPlanCooperacion(anio);
  }
  
  public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getTotalResumenPlanCooperacion(java.lang.String anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getTotalResumenPlanCooperacion(anio);
  }
  
  public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getResumenPlanComplementario(java.lang.String anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getResumenPlanComplementario(anio);
  }
  
  public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getTotalResumenPlanComplementario(java.lang.String anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getTotalResumenPlanComplementario(anio);
  }
  
  public es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[] getResumenPlanPorAyuntamiento(int ianio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getResumenPlanPorAyuntamiento(ianio);
  }
  
  public es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[] getTotalResumenPlanPorAyuntamiento(int ianio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getTotalResumenPlanPorAyuntamiento(ianio);
  }
  
  public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getCuadroA(int anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getCuadroA(anio);
  }
  
  public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getTotalesCuadroA(int anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getTotalesCuadroA(anio);
  }
  
  public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getCuadroC(int anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getCuadroC(anio);
  }
  
  public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getTotalesCuadroC(int anio) throws java.rmi.RemoteException{
    if (planProvincialServicio == null)
      _initPlanProvincialServicioProxy();
    return planProvincialServicio.getTotalesCuadroC(anio);
  }
  
  
}
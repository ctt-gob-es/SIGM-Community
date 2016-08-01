/**
 * PlanProvincialServicio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.services.server.planesProvinciales;

public interface PlanProvincialServicio extends java.rmi.Remote {
    public es.dipucr.domain.planesProvinciales.PlanProvincial[] getPlanProvincial(java.lang.String nombreMunicipio, java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.PlanProvincial[] getPlanesProvincialesByAnio(java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales[] getAllMunicipios() throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getResumenPlanCooperacion(java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getTotalResumenPlanCooperacion(java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getResumenPlanComplementario(java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.ResumePlanProvincial[] getTotalResumenPlanComplementario(java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[] getResumenPlanPorAyuntamiento(int ianio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento[] getTotalResumenPlanPorAyuntamiento(int ianio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getCuadroA(int anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getTotalesCuadroA(int anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getCuadroC(int anio) throws java.rmi.RemoteException;
    public es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio[] getTotalesCuadroC(int anio) throws java.rmi.RemoteException;
}

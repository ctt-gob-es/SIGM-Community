/**
 * PersonalWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.personal;

public interface PersonalWS extends java.rmi.Remote {
    public es.dipucr.webempleado.model.mapping.Provision getProvision(java.lang.String codigo) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Escalas getEscala(java.lang.String codigo) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Subescalas getSubescala(java.lang.String codigo) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Clases getClase(java.lang.String codigo) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.HorariosId getTipoHorario(java.lang.String codigo, java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Servicios[] getServiciosByCodigo() throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Servicios[] getServiciosByNombre() throws java.rmi.RemoteException;
    public es.dipucr.webempleado.domain.beans.PuestoComplementos getPuestoComplemento(java.lang.String codServicio, java.lang.String numPuesto, java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.domain.beans.PuestoComplementos[] getPuestosComplementosVacantes(java.lang.String codServicio, java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.domain.beans.PuestoComplementos[] getPuestosComplementos(java.lang.String codServicio, java.lang.String anio) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.HorariosId[] getHorarios(java.lang.String anio) throws java.rmi.RemoteException;
    public java.lang.String[] getGrupos() throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Provision[] getProvisiones() throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Expediente getExpediente(java.lang.String nif) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.domain.beans.PeriodoLaboral[] getVidaLaboral(java.lang.String nif) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Puestos[] getPuestos(java.lang.String codServicio) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Puestos getPuesto(java.lang.String codigo) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.Categorias getCategoria(java.lang.String codigo) throws java.rmi.RemoteException;
    public es.dipucr.webempleado.model.mapping.TipoP getTipoPuesto(java.lang.String codigo) throws java.rmi.RemoteException;
}

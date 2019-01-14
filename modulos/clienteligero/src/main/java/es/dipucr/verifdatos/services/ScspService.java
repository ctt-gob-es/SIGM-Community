/**
 * ScspService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.verifdatos.services;

public interface ScspService extends javax.xml.rpc.Service {
    public java.lang.String getScspAddress();

    public es.dipucr.verifdatos.services.Scsp getScsp() throws javax.xml.rpc.ServiceException;

    public es.dipucr.verifdatos.services.Scsp getScsp(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

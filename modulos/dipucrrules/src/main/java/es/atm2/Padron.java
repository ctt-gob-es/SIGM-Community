/**
 * Padron.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public interface Padron extends javax.xml.rpc.Service {
    public java.lang.String getPadronSoapAddress();

    public es.atm2.PadronSoap getPadronSoap() throws javax.xml.rpc.ServiceException;

    public es.atm2.PadronSoap getPadronSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

/**
 * TramitacionWebServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public interface TramitacionWebServiceService extends javax.xml.rpc.Service {
    public java.lang.String getTramitacionWebServiceAddress();

    public ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService getTramitacionWebService() throws javax.xml.rpc.ServiceException;

    public ieci.tecdoc.sgm.tram.ws.server.TramitacionWebService getTramitacionWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

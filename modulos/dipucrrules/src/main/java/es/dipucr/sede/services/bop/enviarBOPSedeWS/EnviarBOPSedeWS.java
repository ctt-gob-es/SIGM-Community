/**
 * EnviarBOPSedeWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.sede.services.bop.enviarBOPSedeWS;

public interface EnviarBOPSedeWS extends java.rmi.Remote {
    public int publicarHeaderBOPSede(es.dipucr.sede.services.bop.enviarBOPSedeWS.HeaderBOP cabeceraBOP) throws java.rmi.RemoteException;
    public int publicarAnunciosBOPSede(es.dipucr.sede.services.bop.enviarBOPSedeWS.AnuncioBOP[] anunciosBOP) throws java.rmi.RemoteException;
}

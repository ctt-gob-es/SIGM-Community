
/**
 * EeUtilServiceImplServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package es.mpt.dsic.inside.ws.service.impl;

    /**
     *  EeUtilServiceImplServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class EeUtilServiceImplServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public EeUtilServiceImplServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public EeUtilServiceImplServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for generarCSV method
            * override this method for handling normal response from generarCSV operation
            */
           public void receiveResultgenerarCSV(
                    es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCSVResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from generarCSV operation
           */
            public void receiveErrorgenerarCSV(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for generarCopiaFirma method
            * override this method for handling normal response from generarCopiaFirma operation
            */
           public void receiveResultgenerarCopiaFirma(
                    es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCopiaFirmaResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from generarCopiaFirma operation
           */
            public void receiveErrorgenerarCopiaFirma(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for validacionFirma method
            * override this method for handling normal response from validacionFirma operation
            */
           public void receiveResultvalidacionFirma(
                    es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ValidacionFirmaResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from validacionFirma operation
           */
            public void receiveErrorvalidacionFirma(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ampliarFirma method
            * override this method for handling normal response from ampliarFirma operation
            */
           public void receiveResultampliarFirma(
                    es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.AmpliarFirmaResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ampliarFirma operation
           */
            public void receiveErrorampliarFirma(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for obtenerFirmantes method
            * override this method for handling normal response from obtenerFirmantes operation
            */
           public void receiveResultobtenerFirmantes(
                    es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ObtenerFirmantesResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from obtenerFirmantes operation
           */
            public void receiveErrorobtenerFirmantes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for validarCertificado method
            * override this method for handling normal response from validarCertificado operation
            */
           public void receiveResultvalidarCertificado(
                    es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ValidarCertificadoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from validarCertificado operation
           */
            public void receiveErrorvalidarCertificado(java.lang.Exception e) {
            }
                


    }
    
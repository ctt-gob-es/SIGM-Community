
/**
 * ModifyServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package _0.v2.modify.pfirma.cice.juntadeandalucia;

    /**
     *  ModifyServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ModifyServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ModifyServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ModifyServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for insertDocument method
            * override this method for handling normal response from insertDocument operation
            */
           public void receiveResultinsertDocument(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.InsertDocumentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertDocument operation
           */
            public void receiveErrorinsertDocument(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteDocument method
            * override this method for handling normal response from deleteDocument operation
            */
           public void receiveResultdeleteDocument(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.DeleteDocumentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteDocument operation
           */
            public void receiveErrordeleteDocument(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateRequest method
            * override this method for handling normal response from updateRequest operation
            */
           public void receiveResultupdateRequest(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.UpdateRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateRequest operation
           */
            public void receiveErrorupdateRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createRequest method
            * override this method for handling normal response from createRequest operation
            */
           public void receiveResultcreateRequest(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.CreateRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createRequest operation
           */
            public void receiveErrorcreateRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendRequest method
            * override this method for handling normal response from sendRequest operation
            */
           public void receiveResultsendRequest(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.SendRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendRequest operation
           */
            public void receiveErrorsendRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteRequest method
            * override this method for handling normal response from deleteRequest operation
            */
           public void receiveResultdeleteRequest(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.DeleteRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteRequest operation
           */
            public void receiveErrordeleteRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteSigners method
            * override this method for handling normal response from deleteSigners operation
            */
           public void receiveResultdeleteSigners(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.DeleteSignersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteSigners operation
           */
            public void receiveErrordeleteSigners(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertSigners method
            * override this method for handling normal response from insertSigners operation
            */
           public void receiveResultinsertSigners(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.InsertSignersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertSigners operation
           */
            public void receiveErrorinsertSigners(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for removeRequest method
            * override this method for handling normal response from removeRequest operation
            */
           public void receiveResultremoveRequest(
                    _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.RemoveRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removeRequest operation
           */
            public void receiveErrorremoveRequest(java.lang.Exception e) {
            }
                


    }
    
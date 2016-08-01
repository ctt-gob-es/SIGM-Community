
/**
 * AdminServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package _0.v2.admin.pfirma.cice.juntadeandalucia;

    /**
     *  AdminServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class AdminServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public AdminServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public AdminServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for deleteDocumentsType method
            * override this method for handling normal response from deleteDocumentsType operation
            */
           public void receiveResultdeleteDocumentsType(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteDocumentsTypeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteDocumentsType operation
           */
            public void receiveErrordeleteDocumentsType(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for assignJobToUser method
            * override this method for handling normal response from assignJobToUser operation
            */
           public void receiveResultassignJobToUser(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.AssignJobToUserResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from assignJobToUser operation
           */
            public void receiveErrorassignJobToUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateEnhancedUsers method
            * override this method for handling normal response from updateEnhancedUsers operation
            */
           public void receiveResultupdateEnhancedUsers(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedUsersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateEnhancedUsers operation
           */
            public void receiveErrorupdateEnhancedUsers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteJobs method
            * override this method for handling normal response from deleteJobs operation
            */
           public void receiveResultdeleteJobs(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteJobsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteJobs operation
           */
            public void receiveErrordeleteJobs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateDocumentsType method
            * override this method for handling normal response from updateDocumentsType operation
            */
           public void receiveResultupdateDocumentsType(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateDocumentsTypeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateDocumentsType operation
           */
            public void receiveErrorupdateDocumentsType(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertDocumentsType method
            * override this method for handling normal response from insertDocumentsType operation
            */
           public void receiveResultinsertDocumentsType(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertDocumentsTypeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertDocumentsType operation
           */
            public void receiveErrorinsertDocumentsType(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertEnhancedUsers method
            * override this method for handling normal response from insertEnhancedUsers operation
            */
           public void receiveResultinsertEnhancedUsers(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedUsersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertEnhancedUsers operation
           */
            public void receiveErrorinsertEnhancedUsers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for separateJobToUser method
            * override this method for handling normal response from separateJobToUser operation
            */
           public void receiveResultseparateJobToUser(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.SeparateJobToUserResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from separateJobToUser operation
           */
            public void receiveErrorseparateJobToUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateEnhancedJobs method
            * override this method for handling normal response from updateEnhancedJobs operation
            */
           public void receiveResultupdateEnhancedJobs(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedJobsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateEnhancedJobs operation
           */
            public void receiveErrorupdateEnhancedJobs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for insertEnhancedJobs method
            * override this method for handling normal response from insertEnhancedJobs operation
            */
           public void receiveResultinsertEnhancedJobs(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedJobsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from insertEnhancedJobs operation
           */
            public void receiveErrorinsertEnhancedJobs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteUsers method
            * override this method for handling normal response from deleteUsers operation
            */
           public void receiveResultdeleteUsers(
                    _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.DeleteUsersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteUsers operation
           */
            public void receiveErrordeleteUsers(java.lang.Exception e) {
            }
                


    }
    
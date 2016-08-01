
/**
 * QueryServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package _0.v2.query.pfirma.cice.juntadeandalucia;

    /**
     *  QueryServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class QueryServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public QueryServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public QueryServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for querySeats method
            * override this method for handling normal response from querySeats operation
            */
           public void receiveResultquerySeats(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QuerySeatsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from querySeats operation
           */
            public void receiveErrorquerySeats(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCVS method
            * override this method for handling normal response from getCVS operation
            */
           public void receiveResultgetCVS(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.GetCVSResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCVS operation
           */
            public void receiveErrorgetCVS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for downloadSign method
            * override this method for handling normal response from downloadSign operation
            */
           public void receiveResultdownloadSign(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadSignResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from downloadSign operation
           */
            public void receiveErrordownloadSign(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for downloadDocument method
            * override this method for handling normal response from downloadDocument operation
            */
           public void receiveResultdownloadDocument(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadDocumentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from downloadDocument operation
           */
            public void receiveErrordownloadDocument(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryStates method
            * override this method for handling normal response from queryStates operation
            */
           public void receiveResultqueryStates(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryStatesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryStates operation
           */
            public void receiveErrorqueryStates(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryRequest method
            * override this method for handling normal response from queryRequest operation
            */
           public void receiveResultqueryRequest(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryRequest operation
           */
            public void receiveErrorqueryRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryEnhancedUsers method
            * override this method for handling normal response from queryEnhancedUsers operation
            */
           public void receiveResultqueryEnhancedUsers(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryEnhancedUsersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryEnhancedUsers operation
           */
            public void receiveErrorqueryEnhancedUsers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryEnhancedUserJobAssociatedToJob method
            * override this method for handling normal response from queryEnhancedUserJobAssociatedToJob operation
            */
           public void receiveResultqueryEnhancedUserJobAssociatedToJob(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryEnhancedUserJobAssociatedToJobResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryEnhancedUserJobAssociatedToJob operation
           */
            public void receiveErrorqueryEnhancedUserJobAssociatedToJob(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryUsers method
            * override this method for handling normal response from queryUsers operation
            */
           public void receiveResultqueryUsers(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryUsersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryUsers operation
           */
            public void receiveErrorqueryUsers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryImportanceLevels method
            * override this method for handling normal response from queryImportanceLevels operation
            */
           public void receiveResultqueryImportanceLevels(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryImportanceLevelsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryImportanceLevels operation
           */
            public void receiveErrorqueryImportanceLevels(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryEnhancedUserJobAssociatedToUser method
            * override this method for handling normal response from queryEnhancedUserJobAssociatedToUser operation
            */
           public void receiveResultqueryEnhancedUserJobAssociatedToUser(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryEnhancedUserJobAssociatedToUserResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryEnhancedUserJobAssociatedToUser operation
           */
            public void receiveErrorqueryEnhancedUserJobAssociatedToUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryJobs method
            * override this method for handling normal response from queryJobs operation
            */
           public void receiveResultqueryJobs(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryJobsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryJobs operation
           */
            public void receiveErrorqueryJobs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryDocumentTypes method
            * override this method for handling normal response from queryDocumentTypes operation
            */
           public void receiveResultqueryDocumentTypes(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryDocumentTypesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryDocumentTypes operation
           */
            public void receiveErrorqueryDocumentTypes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryEnhancedJobs method
            * override this method for handling normal response from queryEnhancedJobs operation
            */
           public void receiveResultqueryEnhancedJobs(
                    _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryEnhancedJobsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryEnhancedJobs operation
           */
            public void receiveErrorqueryEnhancedJobs(java.lang.Exception e) {
            }
                


    }
    
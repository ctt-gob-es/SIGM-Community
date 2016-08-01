
/**
 * InSideException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package es.mpt.dsic.inside.ws.service.impl;

public class InSideException extends java.lang.Exception{

    private static final long serialVersionUID = 1404369212067L;
    
    private es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ErrorTest faultMessage;

    
        public InSideException() {
            super("InSideException");
        }

        public InSideException(java.lang.String s) {
           super(s);
        }

        public InSideException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public InSideException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ErrorTest msg){
       faultMessage = msg;
    }
    
    public es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ErrorTest getFaultMessage(){
       return faultMessage;
    }
}
    
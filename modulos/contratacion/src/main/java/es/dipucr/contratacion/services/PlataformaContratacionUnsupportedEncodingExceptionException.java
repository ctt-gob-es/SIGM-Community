
/**
 * PlataformaContratacionUnsupportedEncodingExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package es.dipucr.contratacion.services;

public class PlataformaContratacionUnsupportedEncodingExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1552041273800L;
    
    private es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionUnsupportedEncodingException faultMessage;

    
        public PlataformaContratacionUnsupportedEncodingExceptionException() {
            super("PlataformaContratacionUnsupportedEncodingExceptionException");
        }

        public PlataformaContratacionUnsupportedEncodingExceptionException(java.lang.String s) {
           super(s);
        }

        public PlataformaContratacionUnsupportedEncodingExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public PlataformaContratacionUnsupportedEncodingExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionUnsupportedEncodingException msg){
       faultMessage = msg;
    }
    
    public es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionUnsupportedEncodingException getFaultMessage(){
       return faultMessage;
    }
}
    

/**
 * PlataformaContratacionMalformedURLExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package es.dipucr.contratacion.services;

public class PlataformaContratacionMalformedURLExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1552041273824L;
    
    private es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionMalformedURLException faultMessage;

    
        public PlataformaContratacionMalformedURLExceptionException() {
            super("PlataformaContratacionMalformedURLExceptionException");
        }

        public PlataformaContratacionMalformedURLExceptionException(java.lang.String s) {
           super(s);
        }

        public PlataformaContratacionMalformedURLExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public PlataformaContratacionMalformedURLExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionMalformedURLException msg){
       faultMessage = msg;
    }
    
    public es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionMalformedURLException getFaultMessage(){
       return faultMessage;
    }
}
    
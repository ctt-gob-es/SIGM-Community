
/**
 * PlataformaContratacionJAXBExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package es.dipucr.contratacion.services;

public class PlataformaContratacionJAXBExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1551260858575L;
    
    private es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionJAXBException faultMessage;

    
        public PlataformaContratacionJAXBExceptionException() {
            super("PlataformaContratacionJAXBExceptionException");
        }

        public PlataformaContratacionJAXBExceptionException(java.lang.String s) {
           super(s);
        }

        public PlataformaContratacionJAXBExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public PlataformaContratacionJAXBExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionJAXBException msg){
       faultMessage = msg;
    }
    
    public es.dipucr.contratacion.services.PlataformaContratacionStub.PlataformaContratacionJAXBException getFaultMessage(){
       return faultMessage;
    }
}
    
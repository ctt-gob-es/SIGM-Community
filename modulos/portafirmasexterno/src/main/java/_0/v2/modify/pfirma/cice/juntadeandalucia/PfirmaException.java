
/**
 * PfirmaException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package _0.v2.modify.pfirma.cice.juntadeandalucia;

public class PfirmaException extends java.lang.Exception{

    private static final long serialVersionUID = 1402490804859L;
    
    private _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.ExceptionInfo faultMessage;

    
        public PfirmaException() {
            super("PfirmaException");
        }

        public PfirmaException(java.lang.String s) {
           super(s);
        }

        public PfirmaException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public PfirmaException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(_0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.ExceptionInfo msg){
       faultMessage = msg;
    }
    
    public _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.ExceptionInfo getFaultMessage(){
       return faultMessage;
    }
}
    
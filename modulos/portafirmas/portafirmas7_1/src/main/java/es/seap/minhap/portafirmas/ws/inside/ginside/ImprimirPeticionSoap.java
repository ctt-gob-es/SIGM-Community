/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.ws.inside.ginside;



import java.io.PrintStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/*
* This simple SOAPHandler will output the contents of incoming
* and outgoing messages.
*/
public class ImprimirPeticionSoap implements SOAPHandler<SOAPMessageContext> {

   // change this to redirect output if desired
   private static PrintStream out = System.out;

   public Set<QName> getHeaders() {
       return null;
   }

   public boolean handleMessage(SOAPMessageContext smc) {
       logToSystemOut(smc);
       return true;
   }

   public boolean handleFault(SOAPMessageContext smc) {
       logToSystemOut(smc);
       return true;
   }

   // nothing to clean up
   public void close(MessageContext messageContext) {
   }

   /*
    * Check the MESSAGE_OUTBOUND_PROPERTY in the context
    * to see if this is an outgoing or incoming message.
    * Write a brief message to the print stream and
    * output the message. The writeTo() method can throw
    * SOAPException or IOException
    */
   private void logToSystemOut(SOAPMessageContext smc) {
       Boolean outboundProperty = (Boolean)smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

       if (outboundProperty.booleanValue()) {
           out.println("\nOutbound message:");
       } else {
           out.println("\nInbound message:");
       }

      
       SOAPMessage message = smc.getMessage();
       try {
           message.writeTo(out);
           out.println("");   // just to add a newline
       } catch (Exception e) {
           out.println("Exception in handler: " + e);
       }
   }
}
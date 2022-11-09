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


package es.seap.minhap.portafirmas.ws.sim.respuesta;

import javax.xml.bind.annotation.XmlRegistry;

import es.seap.minhap.portafirmas.ws.sim.peticion.Adjunto;
import es.seap.minhap.portafirmas.ws.sim.peticion.Adjuntos;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatarioMail;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatarioPush;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatarioSMS;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatarioWebPush;
import es.seap.minhap.portafirmas.ws.sim.peticion.Destinatarios;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatariosMail;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatariosPush;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatariosSMS;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatariosWebPush;
import es.seap.minhap.portafirmas.ws.sim.peticion.MensajeEmail;
import es.seap.minhap.portafirmas.ws.sim.peticion.MensajePush;
import es.seap.minhap.portafirmas.ws.sim.peticion.MensajeSMS;
import es.seap.minhap.portafirmas.ws.sim.peticion.MensajeWebPush;
import es.seap.minhap.portafirmas.ws.sim.peticion.Mensajes2;
import es.seap.minhap.portafirmas.ws.sim.peticion.Peticion;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.cliente.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.cliente.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Peticion }
     * 
     */
    public Peticion createPeticion() {
        return new Peticion();
    }

    /**
     * Create an instance of {@link Mensajes }
     * 
     */
    public Mensajes createMensajes() {
        return new Mensajes();
    }

    /**
     * Create an instance of {@link DestinatarioPush }
     * 
     */
    public DestinatarioPush createDestinatarioPush() {
        return new DestinatarioPush();
    }

    /**
     * Create an instance of {@link DestinatariosWebPush }
     * 
     */
    public DestinatariosWebPush createDestinatariosWebPush() {
        return new DestinatariosWebPush();
    }

    /**
     * Create an instance of {@link DestinatariosSMS }
     * 
     */
    public DestinatariosSMS createDestinatariosSMS() {
        return new DestinatariosSMS();
    }

    /**
     * Create an instance of {@link MensajePush }
     * 
     */
    public MensajePush createMensajePush() {
        return new MensajePush();
    }

    /**
     * Create an instance of {@link Destinatarios }
     * 
     */
    public Destinatarios createDestinatarios() {
        return new Destinatarios();
    }

    /**
     * Create an instance of {@link MensajeSMS }
     * 
     */
    public MensajeSMS createMensajeSMS() {
        return new MensajeSMS();
    }

    /**
     * Create an instance of {@link Adjunto }
     * 
     */
    public Adjunto createAdjunto() {
        return new Adjunto();
    }

    /**
     * Create an instance of {@link DestinatarioWebPush }
     * 
     */
    public DestinatarioWebPush createDestinatarioWebPush() {
        return new DestinatarioWebPush();
    }

    /**
     * Create an instance of {@link DestinatarioSMS }
     * 
     */
    public DestinatarioSMS createDestinatarioSMS() {
        return new DestinatarioSMS();
    }

    /**
     * Create an instance of {@link Adjuntos }
     * 
     */
    public Adjuntos createAdjuntos() {
        return new Adjuntos();
    }

    /**
     * Create an instance of {@link MensajeWebPush }
     * 
     */
    public MensajeWebPush createMensajeWebPush() {
        return new MensajeWebPush();
    }

    /**
     * Create an instance of {@link DestinatarioMail }
     * 
     */
    public DestinatarioMail createDestinatarioMail() {
        return new DestinatarioMail();
    }

    /**
     * Create an instance of {@link DestinatariosMail }
     * 
     */
    public DestinatariosMail createDestinatariosMail() {
        return new DestinatariosMail();
    }

    /**
     * Create an instance of {@link DestinatariosPush }
     * 
     */
    public DestinatariosPush createDestinatariosPush() {
        return new DestinatariosPush();
    }

    /**
     * Create an instance of {@link MensajeEmail }
     * 
     */
    public MensajeEmail createMensajeEmail() {
        return new MensajeEmail();
    }

    /**
     * Create an instance of {@link Respuesta }
     * 
     */
    public Respuesta createRespuesta() {
        return new Respuesta();
    }

    /**
     * Create an instance of {@link ResponseStatusType }
     * 
     */
    public ResponseStatusType createResponseStatusType() {
        return new ResponseStatusType();
    }

    /**
     * Create an instance of {@link Lote }
     * 
     */
    public Lote createLote() {
        return new Lote();
    }

    /**
     * Create an instance of {@link Mensajes2 }
     * 
     */
    public Mensajes2 createMensajes2() {
        return new Mensajes2();
    }

    /**
     * Create an instance of {@link Mensaje }
     * 
     */
    public Mensaje createMensaje() {
        return new Mensaje();
    }

}

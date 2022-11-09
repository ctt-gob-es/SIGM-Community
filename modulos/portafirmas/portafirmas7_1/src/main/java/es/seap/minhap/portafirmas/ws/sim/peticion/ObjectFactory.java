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


package es.seap.minhap.portafirmas.ws.sim.peticion;

import javax.xml.bind.annotation.XmlRegistry;

import es.seap.minhap.portafirmas.ws.sim.respuesta.Mensajes;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.seap.minhap.portafirmas.ws.sim.peticion package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.seap.minhap.portafirmas.ws.sim.peticion
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

}

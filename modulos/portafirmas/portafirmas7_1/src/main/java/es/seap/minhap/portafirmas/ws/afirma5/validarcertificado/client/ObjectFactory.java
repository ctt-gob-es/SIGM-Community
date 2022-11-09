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


package es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.seap.minhap.portafirmas.ws.afirma5.client.validarcertificado package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.seap.minhap.portafirmas.ws.afirma5.client.validarcertificado
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MensajeSalida }
     * 
     */
    public MensajeSalida createMensajeSalida() {
        return new MensajeSalida();
    }

    /**
     * Create an instance of {@link MensajeEntrada }
     * 
     */
    public MensajeEntrada createMensajeEntrada() {
        return new MensajeEntrada();
    }

    /**
     * Create an instance of {@link ValidacionEstadoInfo }
     * 
     */
    public ValidacionEstadoInfo createValidacionEstadoInfo() {
        return new ValidacionEstadoInfo();
    }

    /**
     * Create an instance of {@link ValidacionEstadoInfo.InfoMetodoVerificacion }
     * 
     */
    public ValidacionEstadoInfo.InfoMetodoVerificacion createValidacionEstadoInfoInfoMetodoVerificacion() {
        return new ValidacionEstadoInfo.InfoMetodoVerificacion();
    }

    /**
     * Create an instance of {@link ValidacionCadenaInfo }
     * 
     */
    public ValidacionCadenaInfo createValidacionCadenaInfo() {
        return new ValidacionCadenaInfo();
    }

    /**
     * Create an instance of {@link InfoCertificadoInfo }
     * 
     */
    public InfoCertificadoInfo createInfoCertificadoInfo() {
        return new InfoCertificadoInfo();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta }
     * 
     */
    public MensajeSalida.Respuesta createMensajeSalidaRespuesta() {
        return new MensajeSalida.Respuesta();
    }

    /**
     * Create an instance of {@link MensajeEntrada.Parametros }
     * 
     */
    public MensajeEntrada.Parametros createMensajeEntradaParametros() {
        return new MensajeEntrada.Parametros();
    }

    /**
     * Create an instance of {@link ResultadoValidacionInfo }
     * 
     */
    public ResultadoValidacionInfo createResultadoValidacionInfo() {
        return new ResultadoValidacionInfo();
    }

    /**
     * Create an instance of {@link ValidacionSimpleInfo }
     * 
     */
    public ValidacionSimpleInfo createValidacionSimpleInfo() {
        return new ValidacionSimpleInfo();
    }

    /**
     * Create an instance of {@link ValidacionEstadoInfo.InfoMetodoVerificacion.Metodo }
     * 
     */
    public ValidacionEstadoInfo.InfoMetodoVerificacion.Metodo createValidacionEstadoInfoInfoMetodoVerificacionMetodo() {
        return new ValidacionEstadoInfo.InfoMetodoVerificacion.Metodo();
    }

    /**
     * Create an instance of {@link ValidacionCadenaInfo.ErrorCertificado }
     * 
     */
    public ValidacionCadenaInfo.ErrorCertificado createValidacionCadenaInfoErrorCertificado() {
        return new ValidacionCadenaInfo.ErrorCertificado();
    }

    /**
     * Create an instance of {@link InfoCertificadoInfo.Campo }
     * 
     */
    public InfoCertificadoInfo.Campo createInfoCertificadoInfoCampo() {
        return new InfoCertificadoInfo.Campo();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta.ResultadoProcesamiento }
     * 
     */
    public MensajeSalida.Respuesta.ResultadoProcesamiento createMensajeSalidaRespuestaResultadoProcesamiento() {
        return new MensajeSalida.Respuesta.ResultadoProcesamiento();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta.Excepcion }
     * 
     */
    public MensajeSalida.Respuesta.Excepcion createMensajeSalidaRespuestaExcepcion() {
        return new MensajeSalida.Respuesta.Excepcion();
    }

}

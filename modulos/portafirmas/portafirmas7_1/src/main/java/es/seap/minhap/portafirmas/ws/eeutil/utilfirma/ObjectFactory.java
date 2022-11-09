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


package es.seap.minhap.portafirmas.ws.eeutil.utilfirma;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.seap.minhap.portafirmas.ws.eeutil.utilfirma package. 
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

    private final static QName _GenerarCopiaFirmaResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarCopiaFirmaResponse");
    private final static QName _GenerarCopiaResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarCopiaResponse");
    private final static QName _GenerarCopiaFirmaNormalizada_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarCopiaFirmaNormalizada");
    private final static QName _GenerarCSVResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarCSVResponse");
    private final static QName _GenerarCopia_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarCopia");
    private final static QName _GenerarCopiaFirmaNormalizadaResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarCopiaFirmaNormalizadaResponse");
    private final static QName _GenerarJustificanteFirmaResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarJustificanteFirmaResponse");
    private final static QName _GenerarCSV_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarCSV");
    private final static QName _ErrorTest_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "ErrorTest");
    private final static QName _GenerarJustificanteFirma_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarJustificanteFirma");
    private final static QName _GenerarCopiaFirma_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "generarCopiaFirma");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.seap.minhap.portafirmas.ws.eeutil.utilfirma
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListaFirmaInfo }
     * 
     */
    public ListaFirmaInfo createListaFirmaInfo() {
        return new ListaFirmaInfo();
    }

    /**
     * Create an instance of {@link GenerarJustificanteFirma }
     * 
     */
    public GenerarJustificanteFirma createGenerarJustificanteFirma() {
        return new GenerarJustificanteFirma();
    }

    /**
     * Create an instance of {@link GenerarCopiaFirma }
     * 
     */
    public GenerarCopiaFirma createGenerarCopiaFirma() {
        return new GenerarCopiaFirma();
    }

    /**
     * Create an instance of {@link GenerarJustificanteFirmaResponse }
     * 
     */
    public GenerarJustificanteFirmaResponse createGenerarJustificanteFirmaResponse() {
        return new GenerarJustificanteFirmaResponse();
    }

    /**
     * Create an instance of {@link EstadoInfo }
     * 
     */
    public EstadoInfo createEstadoInfo() {
        return new EstadoInfo();
    }

    /**
     * Create an instance of {@link GenerarCSV }
     * 
     */
    public GenerarCSV createGenerarCSV() {
        return new GenerarCSV();
    }

    /**
     * Create an instance of {@link GenerarCopiaResponse }
     * 
     */
    public GenerarCopiaResponse createGenerarCopiaResponse() {
        return new GenerarCopiaResponse();
    }

    /**
     * Create an instance of {@link GenerarCopiaFirmaNormalizada }
     * 
     */
    public GenerarCopiaFirmaNormalizada createGenerarCopiaFirmaNormalizada() {
        return new GenerarCopiaFirmaNormalizada();
    }

    /**
     * Create an instance of {@link GenerarCopiaFirmaNormalizadaResponse }
     * 
     */
    public GenerarCopiaFirmaNormalizadaResponse createGenerarCopiaFirmaNormalizadaResponse() {
        return new GenerarCopiaFirmaNormalizadaResponse();
    }

    /**
     * Create an instance of {@link GenerarCopia }
     * 
     */
    public GenerarCopia createGenerarCopia() {
        return new GenerarCopia();
    }

    /**
     * Create an instance of {@link GenerarCSVResponse }
     * 
     */
    public GenerarCSVResponse createGenerarCSVResponse() {
        return new GenerarCSVResponse();
    }

    /**
     * Create an instance of {@link GenerarCopiaFirmaResponse }
     * 
     */
    public GenerarCopiaFirmaResponse createGenerarCopiaFirmaResponse() {
        return new GenerarCopiaFirmaResponse();
    }

    /**
     * Create an instance of {@link ApplicationLogin }
     * 
     */
    public ApplicationLogin createApplicationLogin() {
        return new ApplicationLogin();
    }

    /**
     * Create an instance of {@link CopiaInfoFirma }
     * 
     */
    public CopiaInfoFirma createCopiaInfoFirma() {
        return new CopiaInfoFirma();
    }

    /**
     * Create an instance of {@link CopiaInfoFirmaSalida }
     * 
     */
    public CopiaInfoFirmaSalida createCopiaInfoFirmaSalida() {
        return new CopiaInfoFirmaSalida();
    }

    /**
     * Create an instance of {@link FirmaInfo }
     * 
     */
    public FirmaInfo createFirmaInfo() {
        return new FirmaInfo();
    }

    /**
     * Create an instance of {@link CSVInfo }
     * 
     */
    public CSVInfo createCSVInfo() {
        return new CSVInfo();
    }

    /**
     * Create an instance of {@link CopiaInfo }
     * 
     */
    public CopiaInfo createCopiaInfo() {
        return new CopiaInfo();
    }

    /**
     * Create an instance of {@link ContenidoInfo }
     * 
     */
    public ContenidoInfo createContenidoInfo() {
        return new ContenidoInfo();
    }

    /**
     * Create an instance of {@link OpcionesPagina }
     * 
     */
    public OpcionesPagina createOpcionesPagina() {
        return new OpcionesPagina();
    }

    /**
     * Create an instance of {@link ListaFirmaInfo.InformacionFirmas }
     * 
     */
    public ListaFirmaInfo.InformacionFirmas createListaFirmaInfoInformacionFirmas() {
        return new ListaFirmaInfo.InformacionFirmas();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarCopiaFirmaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarCopiaFirmaResponse")
    public JAXBElement<GenerarCopiaFirmaResponse> createGenerarCopiaFirmaResponse(GenerarCopiaFirmaResponse value) {
        return new JAXBElement<GenerarCopiaFirmaResponse>(_GenerarCopiaFirmaResponse_QNAME, GenerarCopiaFirmaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarCopiaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarCopiaResponse")
    public JAXBElement<GenerarCopiaResponse> createGenerarCopiaResponse(GenerarCopiaResponse value) {
        return new JAXBElement<GenerarCopiaResponse>(_GenerarCopiaResponse_QNAME, GenerarCopiaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarCopiaFirmaNormalizada }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarCopiaFirmaNormalizada")
    public JAXBElement<GenerarCopiaFirmaNormalizada> createGenerarCopiaFirmaNormalizada(GenerarCopiaFirmaNormalizada value) {
        return new JAXBElement<GenerarCopiaFirmaNormalizada>(_GenerarCopiaFirmaNormalizada_QNAME, GenerarCopiaFirmaNormalizada.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarCSVResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarCSVResponse")
    public JAXBElement<GenerarCSVResponse> createGenerarCSVResponse(GenerarCSVResponse value) {
        return new JAXBElement<GenerarCSVResponse>(_GenerarCSVResponse_QNAME, GenerarCSVResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarCopia }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarCopia")
    public JAXBElement<GenerarCopia> createGenerarCopia(GenerarCopia value) {
        return new JAXBElement<GenerarCopia>(_GenerarCopia_QNAME, GenerarCopia.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarCopiaFirmaNormalizadaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarCopiaFirmaNormalizadaResponse")
    public JAXBElement<GenerarCopiaFirmaNormalizadaResponse> createGenerarCopiaFirmaNormalizadaResponse(GenerarCopiaFirmaNormalizadaResponse value) {
        return new JAXBElement<GenerarCopiaFirmaNormalizadaResponse>(_GenerarCopiaFirmaNormalizadaResponse_QNAME, GenerarCopiaFirmaNormalizadaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarJustificanteFirmaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarJustificanteFirmaResponse")
    public JAXBElement<GenerarJustificanteFirmaResponse> createGenerarJustificanteFirmaResponse(GenerarJustificanteFirmaResponse value) {
        return new JAXBElement<GenerarJustificanteFirmaResponse>(_GenerarJustificanteFirmaResponse_QNAME, GenerarJustificanteFirmaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarCSV }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarCSV")
    public JAXBElement<GenerarCSV> createGenerarCSV(GenerarCSV value) {
        return new JAXBElement<GenerarCSV>(_GenerarCSV_QNAME, GenerarCSV.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EstadoInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "ErrorTest")
    public JAXBElement<EstadoInfo> createErrorTest(EstadoInfo value) {
        return new JAXBElement<EstadoInfo>(_ErrorTest_QNAME, EstadoInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarJustificanteFirma }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarJustificanteFirma")
    public JAXBElement<GenerarJustificanteFirma> createGenerarJustificanteFirma(GenerarJustificanteFirma value) {
        return new JAXBElement<GenerarJustificanteFirma>(_GenerarJustificanteFirma_QNAME, GenerarJustificanteFirma.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarCopiaFirma }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "generarCopiaFirma")
    public JAXBElement<GenerarCopiaFirma> createGenerarCopiaFirma(GenerarCopiaFirma value) {
        return new JAXBElement<GenerarCopiaFirma>(_GenerarCopiaFirma_QNAME, GenerarCopiaFirma.class, null, value);
    }

}

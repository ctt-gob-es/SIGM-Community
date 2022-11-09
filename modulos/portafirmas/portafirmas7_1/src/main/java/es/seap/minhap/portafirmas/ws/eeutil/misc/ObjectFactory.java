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


package es.seap.minhap.portafirmas.ws.eeutil.misc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.seap.minhap.portafirmas.ws.eeutil.misc package. 
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

    private final static QName _ConvertirTCNAPdfResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "convertirTCNAPdfResponse");
    private final static QName _VisualizarFacturaeResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "visualizarFacturaeResponse");
    private final static QName _ConvertirTCNAPdf_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "convertirTCNAPdf");
    private final static QName _VisualizarFacturae_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "visualizarFacturae");
    private final static QName _ComprobarPDFAResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "comprobarPDFAResponse");
    private final static QName _ComprobarPDFA_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "comprobarPDFA");
    private final static QName _PostProcesarFirma_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "postProcesarFirma");
    private final static QName _ConvertirPDFAResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "convertirPDFAResponse");
    private final static QName _PostProcesarFirmaResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "postProcesarFirmaResponse");
    private final static QName _ConvertirPDFA_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "convertirPDFA");
    private final static QName _ErrorTest_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "ErrorTest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.seap.minhap.portafirmas.ws.eeutil.misc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PostProcesarFirma }
     * 
     */
    public PostProcesarFirma createPostProcesarFirma() {
        return new PostProcesarFirma();
    }

    /**
     * Create an instance of {@link PostProcesarFirmaResponse }
     * 
     */
    public PostProcesarFirmaResponse createPostProcesarFirmaResponse() {
        return new PostProcesarFirmaResponse();
    }

    /**
     * Create an instance of {@link ConvertirPDFAResponse }
     * 
     */
    public ConvertirPDFAResponse createConvertirPDFAResponse() {
        return new ConvertirPDFAResponse();
    }

    /**
     * Create an instance of {@link EstadoInfo }
     * 
     */
    public EstadoInfo createEstadoInfo() {
        return new EstadoInfo();
    }

    /**
     * Create an instance of {@link ConvertirPDFA }
     * 
     */
    public ConvertirPDFA createConvertirPDFA() {
        return new ConvertirPDFA();
    }

    /**
     * Create an instance of {@link VisualizarFacturae }
     * 
     */
    public VisualizarFacturae createVisualizarFacturae() {
        return new VisualizarFacturae();
    }

    /**
     * Create an instance of {@link ConvertirTCNAPdf }
     * 
     */
    public ConvertirTCNAPdf createConvertirTCNAPdf() {
        return new ConvertirTCNAPdf();
    }

    /**
     * Create an instance of {@link ComprobarPDFAResponse }
     * 
     */
    public ComprobarPDFAResponse createComprobarPDFAResponse() {
        return new ComprobarPDFAResponse();
    }

    /**
     * Create an instance of {@link ComprobarPDFA }
     * 
     */
    public ComprobarPDFA createComprobarPDFA() {
        return new ComprobarPDFA();
    }

    /**
     * Create an instance of {@link ConvertirTCNAPdfResponse }
     * 
     */
    public ConvertirTCNAPdfResponse createConvertirTCNAPdfResponse() {
        return new ConvertirTCNAPdfResponse();
    }

    /**
     * Create an instance of {@link VisualizarFacturaeResponse }
     * 
     */
    public VisualizarFacturaeResponse createVisualizarFacturaeResponse() {
        return new VisualizarFacturaeResponse();
    }

    /**
     * Create an instance of {@link ApplicationLogin }
     * 
     */
    public ApplicationLogin createApplicationLogin() {
        return new ApplicationLogin();
    }

    /**
     * Create an instance of {@link PdfSalida }
     * 
     */
    public PdfSalida createPdfSalida() {
        return new PdfSalida();
    }

    /**
     * Create an instance of {@link DocumentoEntrada }
     * 
     */
    public DocumentoEntrada createDocumentoEntrada() {
        return new DocumentoEntrada();
    }

    /**
     * Create an instance of {@link DocumentoContenido }
     * 
     */
    public DocumentoContenido createDocumentoContenido() {
        return new DocumentoContenido();
    }

    /**
     * Create an instance of {@link ContenidoInfo }
     * 
     */
    public ContenidoInfo createContenidoInfo() {
        return new ContenidoInfo();
    }

    /**
     * Create an instance of {@link SalidaVisualizacion }
     * 
     */
    public SalidaVisualizacion createSalidaVisualizacion() {
        return new SalidaVisualizacion();
    }

    /**
     * Create an instance of {@link TCNInfo }
     * 
     */
    public TCNInfo createTCNInfo() {
        return new TCNInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirTCNAPdfResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "convertirTCNAPdfResponse")
    public JAXBElement<ConvertirTCNAPdfResponse> createConvertirTCNAPdfResponse(ConvertirTCNAPdfResponse value) {
        return new JAXBElement<ConvertirTCNAPdfResponse>(_ConvertirTCNAPdfResponse_QNAME, ConvertirTCNAPdfResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarFacturaeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "visualizarFacturaeResponse")
    public JAXBElement<VisualizarFacturaeResponse> createVisualizarFacturaeResponse(VisualizarFacturaeResponse value) {
        return new JAXBElement<VisualizarFacturaeResponse>(_VisualizarFacturaeResponse_QNAME, VisualizarFacturaeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirTCNAPdf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "convertirTCNAPdf")
    public JAXBElement<ConvertirTCNAPdf> createConvertirTCNAPdf(ConvertirTCNAPdf value) {
        return new JAXBElement<ConvertirTCNAPdf>(_ConvertirTCNAPdf_QNAME, ConvertirTCNAPdf.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarFacturae }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "visualizarFacturae")
    public JAXBElement<VisualizarFacturae> createVisualizarFacturae(VisualizarFacturae value) {
        return new JAXBElement<VisualizarFacturae>(_VisualizarFacturae_QNAME, VisualizarFacturae.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComprobarPDFAResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "comprobarPDFAResponse")
    public JAXBElement<ComprobarPDFAResponse> createComprobarPDFAResponse(ComprobarPDFAResponse value) {
        return new JAXBElement<ComprobarPDFAResponse>(_ComprobarPDFAResponse_QNAME, ComprobarPDFAResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComprobarPDFA }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "comprobarPDFA")
    public JAXBElement<ComprobarPDFA> createComprobarPDFA(ComprobarPDFA value) {
        return new JAXBElement<ComprobarPDFA>(_ComprobarPDFA_QNAME, ComprobarPDFA.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostProcesarFirma }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "postProcesarFirma")
    public JAXBElement<PostProcesarFirma> createPostProcesarFirma(PostProcesarFirma value) {
        return new JAXBElement<PostProcesarFirma>(_PostProcesarFirma_QNAME, PostProcesarFirma.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirPDFAResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "convertirPDFAResponse")
    public JAXBElement<ConvertirPDFAResponse> createConvertirPDFAResponse(ConvertirPDFAResponse value) {
        return new JAXBElement<ConvertirPDFAResponse>(_ConvertirPDFAResponse_QNAME, ConvertirPDFAResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostProcesarFirmaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "postProcesarFirmaResponse")
    public JAXBElement<PostProcesarFirmaResponse> createPostProcesarFirmaResponse(PostProcesarFirmaResponse value) {
        return new JAXBElement<PostProcesarFirmaResponse>(_PostProcesarFirmaResponse_QNAME, PostProcesarFirmaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirPDFA }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "convertirPDFA")
    public JAXBElement<ConvertirPDFA> createConvertirPDFA(ConvertirPDFA value) {
        return new JAXBElement<ConvertirPDFA>(_ConvertirPDFA_QNAME, ConvertirPDFA.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EstadoInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "ErrorTest")
    public JAXBElement<EstadoInfo> createErrorTest(EstadoInfo value) {
        return new JAXBElement<EstadoInfo>(_ErrorTest_QNAME, EstadoInfo.class, null, value);
    }

}

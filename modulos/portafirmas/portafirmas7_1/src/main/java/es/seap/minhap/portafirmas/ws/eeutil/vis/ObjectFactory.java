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


package es.seap.minhap.portafirmas.ws.eeutil.vis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.seap.minhap.portafirmas.ws.eeutil.vis package. 
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

    private final static QName _Firmas_QNAME = new QName("https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma", "firmas");
    private final static QName _VisualizarContenidoOriginalResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "visualizarContenidoOriginalResponse");
    private final static QName _EliminarPlantilla_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "eliminarPlantilla");
    private final static QName _VisualizarDocumentoConPlantillaResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "visualizarDocumentoConPlantillaResponse");
    private final static QName _ErrorTest_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "ErrorTest");
    private final static QName _AsociarPlantillaResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "asociarPlantillaResponse");
    private final static QName _VisualizarDocumentoConPlantilla_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "visualizarDocumentoConPlantilla");
    private final static QName _Documento_QNAME = new QName("https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento", "documento");
    private final static QName _Contenido_QNAME = new QName("https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/contenido", "contenido");
    private final static QName _AsociarPlantilla_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "asociarPlantilla");
    private final static QName _EliminarPlantillaResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "eliminarPlantillaResponse");
    private final static QName _ObtenerPlantillas_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "obtenerPlantillas");
    private final static QName _VisualizarContenidoOriginal_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "visualizarContenidoOriginal");
    private final static QName _Visualizar_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "visualizar");
    private final static QName _ObtenerPlantillasResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "obtenerPlantillasResponse");
    private final static QName _VisualizarResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "visualizarResponse");
    private final static QName _Metadatos_QNAME = new QName("https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", "metadatos");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.seap.minhap.portafirmas.ws.eeutil.vis
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListaCadenas }
     * 
     */
    public ListaCadenas createListaCadenas() {
        return new ListaCadenas();
    }

    /**
     * Create an instance of {@link ListaItem }
     * 
     */
    public ListaItem createListaItem() {
        return new ListaItem();
    }

    /**
     * Create an instance of {@link ListaPropiedades }
     * 
     */
    public ListaPropiedades createListaPropiedades() {
        return new ListaPropiedades();
    }

    /**
     * Create an instance of {@link TipoFirmasElectronicas }
     * 
     */
    public TipoFirmasElectronicas createTipoFirmasElectronicas() {
        return new TipoFirmasElectronicas();
    }

    /**
     * Create an instance of {@link TipoFirmasElectronicas.ContenidoFirma }
     * 
     */
    public TipoFirmasElectronicas.ContenidoFirma createTipoFirmasElectronicasContenidoFirma() {
        return new TipoFirmasElectronicas.ContenidoFirma();
    }

    /**
     * Create an instance of {@link Firmas }
     * 
     */
    public Firmas createFirmas() {
        return new Firmas();
    }

    /**
     * Create an instance of {@link TipoMetadatosAdicionales }
     * 
     */
    public TipoMetadatosAdicionales createTipoMetadatosAdicionales() {
        return new TipoMetadatosAdicionales();
    }

    /**
     * Create an instance of {@link MetadatoAdicional }
     * 
     */
    public MetadatoAdicional createMetadatoAdicional() {
        return new MetadatoAdicional();
    }

    /**
     * Create an instance of {@link DocumentoEniConMAdicionales }
     * 
     */
    public DocumentoEniConMAdicionales createDocumentoEniConMAdicionales() {
        return new DocumentoEniConMAdicionales();
    }

    /**
     * Create an instance of {@link TipoMetadatos }
     * 
     */
    public TipoMetadatos createTipoMetadatos() {
        return new TipoMetadatos();
    }

    /**
     * Create an instance of {@link TipoEstadoElaboracion }
     * 
     */
    public TipoEstadoElaboracion createTipoEstadoElaboracion() {
        return new TipoEstadoElaboracion();
    }

    /**
     * Create an instance of {@link TipoContenido }
     * 
     */
    public TipoContenido createTipoContenido() {
        return new TipoContenido();
    }

    /**
     * Create an instance of {@link TipoDocumento }
     * 
     */
    public TipoDocumento createTipoDocumento() {
        return new TipoDocumento();
    }

    /**
     * Create an instance of {@link VisualizarResponse }
     * 
     */
    public VisualizarResponse createVisualizarResponse() {
        return new VisualizarResponse();
    }

    /**
     * Create an instance of {@link AsociarPlantillaResponse }
     * 
     */
    public AsociarPlantillaResponse createAsociarPlantillaResponse() {
        return new AsociarPlantillaResponse();
    }

    /**
     * Create an instance of {@link VisualizarDocumentoConPlantillaResponse }
     * 
     */
    public VisualizarDocumentoConPlantillaResponse createVisualizarDocumentoConPlantillaResponse() {
        return new VisualizarDocumentoConPlantillaResponse();
    }

    /**
     * Create an instance of {@link Visualizar }
     * 
     */
    public Visualizar createVisualizar() {
        return new Visualizar();
    }

    /**
     * Create an instance of {@link ObtenerPlantillasResponse }
     * 
     */
    public ObtenerPlantillasResponse createObtenerPlantillasResponse() {
        return new ObtenerPlantillasResponse();
    }

    /**
     * Create an instance of {@link EstadoInfo }
     * 
     */
    public EstadoInfo createEstadoInfo() {
        return new EstadoInfo();
    }

    /**
     * Create an instance of {@link AsociarPlantilla }
     * 
     */
    public AsociarPlantilla createAsociarPlantilla() {
        return new AsociarPlantilla();
    }

    /**
     * Create an instance of {@link VisualizarContenidoOriginal }
     * 
     */
    public VisualizarContenidoOriginal createVisualizarContenidoOriginal() {
        return new VisualizarContenidoOriginal();
    }

    /**
     * Create an instance of {@link ObtenerPlantillas }
     * 
     */
    public ObtenerPlantillas createObtenerPlantillas() {
        return new ObtenerPlantillas();
    }

    /**
     * Create an instance of {@link EliminarPlantillaResponse }
     * 
     */
    public EliminarPlantillaResponse createEliminarPlantillaResponse() {
        return new EliminarPlantillaResponse();
    }

    /**
     * Create an instance of {@link EliminarPlantilla }
     * 
     */
    public EliminarPlantilla createEliminarPlantilla() {
        return new EliminarPlantilla();
    }

    /**
     * Create an instance of {@link VisualizarDocumentoConPlantilla }
     * 
     */
    public VisualizarDocumentoConPlantilla createVisualizarDocumentoConPlantilla() {
        return new VisualizarDocumentoConPlantilla();
    }

    /**
     * Create an instance of {@link VisualizarContenidoOriginalResponse }
     * 
     */
    public VisualizarContenidoOriginalResponse createVisualizarContenidoOriginalResponse() {
        return new VisualizarContenidoOriginalResponse();
    }

    /**
     * Create an instance of {@link Propiedad }
     * 
     */
    public Propiedad createPropiedad() {
        return new Propiedad();
    }

    /**
     * Create an instance of {@link Plantilla }
     * 
     */
    public Plantilla createPlantilla() {
        return new Plantilla();
    }

    /**
     * Create an instance of {@link ApplicationLogin }
     * 
     */
    public ApplicationLogin createApplicationLogin() {
        return new ApplicationLogin();
    }

    /**
     * Create an instance of {@link OpcionesLogo }
     * 
     */
    public OpcionesLogo createOpcionesLogo() {
        return new OpcionesLogo();
    }

    /**
     * Create an instance of {@link OpcionesVisualizacion }
     * 
     */
    public OpcionesVisualizacion createOpcionesVisualizacion() {
        return new OpcionesVisualizacion();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link DocumentoContenido }
     * 
     */
    public DocumentoContenido createDocumentoContenido() {
        return new DocumentoContenido();
    }

    /**
     * Create an instance of {@link SalidaVisualizacion }
     * 
     */
    public SalidaVisualizacion createSalidaVisualizacion() {
        return new SalidaVisualizacion();
    }

    /**
     * Create an instance of {@link ListaCadenas.Cadenas }
     * 
     */
    public ListaCadenas.Cadenas createListaCadenasCadenas() {
        return new ListaCadenas.Cadenas();
    }

    /**
     * Create an instance of {@link ListaItem.Items }
     * 
     */
    public ListaItem.Items createListaItemItems() {
        return new ListaItem.Items();
    }

    /**
     * Create an instance of {@link ListaPropiedades.Propiedades }
     * 
     */
    public ListaPropiedades.Propiedades createListaPropiedadesPropiedades() {
        return new ListaPropiedades.Propiedades();
    }

    /**
     * Create an instance of {@link TipoFirmasElectronicas.ContenidoFirma.CSV }
     * 
     */
    public TipoFirmasElectronicas.ContenidoFirma.CSV createTipoFirmasElectronicasContenidoFirmaCSV() {
        return new TipoFirmasElectronicas.ContenidoFirma.CSV();
    }

    /**
     * Create an instance of {@link TipoFirmasElectronicas.ContenidoFirma.FirmaConCertificado }
     * 
     */
    public TipoFirmasElectronicas.ContenidoFirma.FirmaConCertificado createTipoFirmasElectronicasContenidoFirmaFirmaConCertificado() {
        return new TipoFirmasElectronicas.ContenidoFirma.FirmaConCertificado();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Firmas }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma", name = "firmas")
    public JAXBElement<Firmas> createFirmas(Firmas value) {
        return new JAXBElement<Firmas>(_Firmas_QNAME, Firmas.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarContenidoOriginalResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "visualizarContenidoOriginalResponse")
    public JAXBElement<VisualizarContenidoOriginalResponse> createVisualizarContenidoOriginalResponse(VisualizarContenidoOriginalResponse value) {
        return new JAXBElement<VisualizarContenidoOriginalResponse>(_VisualizarContenidoOriginalResponse_QNAME, VisualizarContenidoOriginalResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarPlantilla }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "eliminarPlantilla")
    public JAXBElement<EliminarPlantilla> createEliminarPlantilla(EliminarPlantilla value) {
        return new JAXBElement<EliminarPlantilla>(_EliminarPlantilla_QNAME, EliminarPlantilla.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarDocumentoConPlantillaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "visualizarDocumentoConPlantillaResponse")
    public JAXBElement<VisualizarDocumentoConPlantillaResponse> createVisualizarDocumentoConPlantillaResponse(VisualizarDocumentoConPlantillaResponse value) {
        return new JAXBElement<VisualizarDocumentoConPlantillaResponse>(_VisualizarDocumentoConPlantillaResponse_QNAME, VisualizarDocumentoConPlantillaResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link AsociarPlantillaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "asociarPlantillaResponse")
    public JAXBElement<AsociarPlantillaResponse> createAsociarPlantillaResponse(AsociarPlantillaResponse value) {
        return new JAXBElement<AsociarPlantillaResponse>(_AsociarPlantillaResponse_QNAME, AsociarPlantillaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarDocumentoConPlantilla }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "visualizarDocumentoConPlantilla")
    public JAXBElement<VisualizarDocumentoConPlantilla> createVisualizarDocumentoConPlantilla(VisualizarDocumentoConPlantilla value) {
        return new JAXBElement<VisualizarDocumentoConPlantilla>(_VisualizarDocumentoConPlantilla_QNAME, VisualizarDocumentoConPlantilla.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento", name = "documento")
    public JAXBElement<TipoDocumento> createDocumento(TipoDocumento value) {
        return new JAXBElement<TipoDocumento>(_Documento_QNAME, TipoDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoContenido }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/contenido", name = "contenido")
    public JAXBElement<TipoContenido> createContenido(TipoContenido value) {
        return new JAXBElement<TipoContenido>(_Contenido_QNAME, TipoContenido.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsociarPlantilla }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "asociarPlantilla")
    public JAXBElement<AsociarPlantilla> createAsociarPlantilla(AsociarPlantilla value) {
        return new JAXBElement<AsociarPlantilla>(_AsociarPlantilla_QNAME, AsociarPlantilla.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarPlantillaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "eliminarPlantillaResponse")
    public JAXBElement<EliminarPlantillaResponse> createEliminarPlantillaResponse(EliminarPlantillaResponse value) {
        return new JAXBElement<EliminarPlantillaResponse>(_EliminarPlantillaResponse_QNAME, EliminarPlantillaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerPlantillas }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "obtenerPlantillas")
    public JAXBElement<ObtenerPlantillas> createObtenerPlantillas(ObtenerPlantillas value) {
        return new JAXBElement<ObtenerPlantillas>(_ObtenerPlantillas_QNAME, ObtenerPlantillas.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarContenidoOriginal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "visualizarContenidoOriginal")
    public JAXBElement<VisualizarContenidoOriginal> createVisualizarContenidoOriginal(VisualizarContenidoOriginal value) {
        return new JAXBElement<VisualizarContenidoOriginal>(_VisualizarContenidoOriginal_QNAME, VisualizarContenidoOriginal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Visualizar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "visualizar")
    public JAXBElement<Visualizar> createVisualizar(Visualizar value) {
        return new JAXBElement<Visualizar>(_Visualizar_QNAME, Visualizar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerPlantillasResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "obtenerPlantillasResponse")
    public JAXBElement<ObtenerPlantillasResponse> createObtenerPlantillasResponse(ObtenerPlantillasResponse value) {
        return new JAXBElement<ObtenerPlantillasResponse>(_ObtenerPlantillasResponse_QNAME, ObtenerPlantillasResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "visualizarResponse")
    public JAXBElement<VisualizarResponse> createVisualizarResponse(VisualizarResponse value) {
        return new JAXBElement<VisualizarResponse>(_VisualizarResponse_QNAME, VisualizarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoMetadatos }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", name = "metadatos")
    public JAXBElement<TipoMetadatos> createMetadatos(TipoMetadatos value) {
        return new JAXBElement<TipoMetadatos>(_Metadatos_QNAME, TipoMetadatos.class, null, value);
    }

}

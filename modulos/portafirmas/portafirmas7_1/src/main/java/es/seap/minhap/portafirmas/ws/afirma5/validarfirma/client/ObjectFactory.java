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


package es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client package. 
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

    private final static QName _JustificanteFirmaElectronica_QNAME = new QName("http://afirmaws/ws/firma", "justificanteFirmaElectronica");
    private final static QName _TipoDocumento_QNAME = new QName("http://afirmaws/ws/firma", "tipoDocumento");
    private final static QName _Datos_QNAME = new QName("http://afirmaws/ws/firma", "datos");
    private final static QName _FirmaElectronicaServidor_QNAME = new QName("http://afirmaws/ws/firma", "firmaElectronicaServidor");
    private final static QName _IdAplicacion_QNAME = new QName("http://afirmaws/ws/firma", "idAplicacion");
    private final static QName _CustodiarDocumento_QNAME = new QName("http://afirmaws/ws/firma", "custodiarDocumento");
    private final static QName _Estado_QNAME = new QName("http://afirmaws/ws/firma", "estado");
    private final static QName _AlgoritmoHash_QNAME = new QName("http://afirmaws/ws/firma", "algoritmoHash");
    private final static QName _IdTransaccionBloque_QNAME = new QName("http://afirmaws/ws/firma", "idTransaccionBloque");
    private final static QName _IdReferencia_QNAME = new QName("http://afirmaws/ws/firma", "idReferencia");
    private final static QName _CertificadoFirmante_QNAME = new QName("http://afirmaws/ws/firma", "certificadoFirmante");
    private final static QName _Documento_QNAME = new QName("http://afirmaws/ws/firma", "documento");
    private final static QName _FormatoFirma_QNAME = new QName("http://afirmaws/ws/firma", "formatoFirma");
    private final static QName _Firmante_QNAME = new QName("http://afirmaws/ws/firma", "firmante");
    private final static QName _IdDocumento_QNAME = new QName("http://afirmaws/ws/firma", "idDocumento");
    private final static QName _BloqueFirmas_QNAME = new QName("http://afirmaws/ws/firma", "bloqueFirmas");
    private final static QName _NombreDocumento_QNAME = new QName("http://afirmaws/ws/firma", "nombreDocumento");
    private final static QName _Hash_QNAME = new QName("http://afirmaws/ws/firma", "hash");
    private final static QName _FirmaElectronica_QNAME = new QName("http://afirmaws/ws/firma", "firmaElectronica");
    private final static QName _FirmanteObjetivo_QNAME = new QName("http://afirmaws/ws/firma", "firmanteObjetivo");
    private final static QName _IdTransaccion_QNAME = new QName("http://afirmaws/ws/firma", "idTransaccion");
    private final static QName _TipoValidacionFirmaElectronicaValidacionFirmaElectronicaConclusion_QNAME = new QName("http://afirmaws/ws/firma", "conclusion");
    private final static QName _TipoValidacionFirmaElectronicaValidacionFirmaElectronicaDetalle_QNAME = new QName("http://afirmaws/ws/firma", "detalle");
    private final static QName _TipoValidacionFirmaElectronicaValidacionFirmaElectronicaInformacionAdicional_QNAME = new QName("http://afirmaws/ws/firma", "informacionAdicional");
    private final static QName _TipoValidacionFirmaElectronicaValidacionFirmaElectronicaProceso_QNAME = new QName("http://afirmaws/ws/firma", "proceso");
    private final static QName _MensajeSalidaRespuestaRespuestaBloqueIdBloque_QNAME = new QName("http://afirmaws/ws/firma", "idBloque");
    private final static QName _MensajeSalidaRespuestaRespuesta_QNAME = new QName("http://afirmaws/ws/firma", "Respuesta");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.InformacionAdicional }
     * 
     */
    public es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.InformacionAdicional createInformacionAdicional() {
        return new es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.InformacionAdicional();
    }

    /**
     * Create an instance of {@link MensajeSalida }
     * 
     */
    public MensajeSalida createMensajeSalida() {
        return new MensajeSalida();
    }

    /**
     * Create an instance of {@link TipoValidacionFirmaElectronica }
     * 
     */
    public TipoValidacionFirmaElectronica createTipoValidacionFirmaElectronica() {
        return new TipoValidacionFirmaElectronica();
    }

    /**
     * Create an instance of {@link TipoValidacionFirmaElectronica.ValidacionFirmaElectronica }
     * 
     */
    public TipoValidacionFirmaElectronica.ValidacionFirmaElectronica createTipoValidacionFirmaElectronicaValidacionFirmaElectronica() {
        return new TipoValidacionFirmaElectronica.ValidacionFirmaElectronica();
    }

    /**
     * Create an instance of {@link TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional }
     * 
     */
    public TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional createTipoValidacionFirmaElectronicaValidacionFirmaElectronicaInformacionAdicional() {
        return new TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta }
     * 
     */
    public MensajeSalida.RespuestaX createMensajeSalidaRespuesta() {
        return new MensajeSalida.RespuestaX();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta.Respuesta }
     * 
     */
    public MensajeSalida.RespuestaX.Respuesta createMensajeSalidaRespuestaRespuesta() {
        return new MensajeSalida.RespuestaX.Respuesta();
    }

    /**
     * Create an instance of {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.InformacionAdicional.Firmante }
     * 
     */
    public es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.InformacionAdicional.Firmante createInformacionAdicionalFirmante() {
        return new es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.InformacionAdicional.Firmante();
    }

    /**
     * Create an instance of {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.ValidacionFirmaElectronica }
     * 
     */
    public es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.ValidacionFirmaElectronica createValidacionFirmaElectronica() {
        return new es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.ValidacionFirmaElectronica();
    }

    /**
     * Create an instance of {@link MensajeEntrada }
     * 
     */
    public MensajeEntrada createMensajeEntrada() {
        return new MensajeEntrada();
    }

    /**
     * Create an instance of {@link Parametros }
     * 
     */
    public Parametros createParametros() {
        return new Parametros();
    }

    /**
     * Create an instance of {@link BloqueOrigen }
     * 
     */
    public BloqueOrigen createBloqueOrigen() {
        return new BloqueOrigen();
    }

    /**
     * Create an instance of {@link DocumentosBloque }
     * 
     */
    public DocumentosBloque createDocumentosBloque() {
        return new DocumentosBloque();
    }

    /**
     * Create an instance of {@link DocumentoBloque }
     * 
     */
    public DocumentoBloque createDocumentoBloque() {
        return new DocumentoBloque();
    }

    /**
     * Create an instance of {@link Bloques }
     * 
     */
    public Bloques createBloques() {
        return new Bloques();
    }

    /**
     * Create an instance of {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Bloque }
     * 
     */
    public es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Bloque createBloque() {
        return new es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Bloque();
    }

    /**
     * Create an instance of {@link DocumentoSeleccionado }
     * 
     */
    public DocumentoSeleccionado createDocumentoSeleccionado() {
        return new DocumentoSeleccionado();
    }

    /**
     * Create an instance of {@link DocumentosSeleccionados }
     * 
     */
    public DocumentosSeleccionados createDocumentosSeleccionados() {
        return new DocumentosSeleccionados();
    }

    /**
     * Create an instance of {@link DocumentosMultifirma }
     * 
     */
    public DocumentosMultifirma createDocumentosMultifirma() {
        return new DocumentosMultifirma();
    }

    /**
     * Create an instance of {@link Bloque2 }
     * 
     */
    public Bloque2 createBloque2() {
        return new Bloque2();
    }

    /**
     * Create an instance of {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentos }
     * 
     */
    public es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentos createIdDocumentos() {
        return new es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentos();
    }

    /**
     * Create an instance of {@link Descripcion }
     * 
     */
    public Descripcion createDescripcion() {
        return new Descripcion();
    }

    /**
     * Create an instance of {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentosBloque }
     * 
     */
    public es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentosBloque createIdDocumentosBloque() {
        return new es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentosBloque();
    }

    /**
     * Create an instance of {@link IdDocumentosMultifirmados }
     * 
     */
    public IdDocumentosMultifirmados createIdDocumentosMultifirmados() {
        return new IdDocumentosMultifirmados();
    }

    /**
     * Create an instance of {@link BloqueSeleccionado }
     * 
     */
    public BloqueSeleccionado createBloqueSeleccionado() {
        return new BloqueSeleccionado();
    }

    /**
     * Create an instance of {@link IdTransacciones }
     * 
     */
    public IdTransacciones createIdTransacciones() {
        return new IdTransacciones();
    }

    /**
     * Create an instance of {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Respuesta }
     * 
     */
    public es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Respuesta2 createRespuesta() {
        return new es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Respuesta2();
    }

    /**
     * Create an instance of {@link Excepcion }
     * 
     */
    public Excepcion createExcepcion() {
        return new Excepcion();
    }

    /**
     * Create an instance of {@link TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional.Firmante }
     * 
     */
    public TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional.Firmante createTipoValidacionFirmaElectronicaValidacionFirmaElectronicaInformacionAdicionalFirmante() {
        return new TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional.Firmante();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta.Respuesta.IdDocumentos }
     * 
     */
    public MensajeSalida.RespuestaX.Respuesta.IdDocumentos createMensajeSalidaRespuestaRespuestaIdDocumentos() {
        return new MensajeSalida.RespuestaX.Respuesta.IdDocumentos();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta.Respuesta.IdDocumentosBloque }
     * 
     */
    public MensajeSalida.RespuestaX.Respuesta.IdDocumentosBloque createMensajeSalidaRespuestaRespuestaIdDocumentosBloque() {
        return new MensajeSalida.RespuestaX.Respuesta.IdDocumentosBloque();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta.Respuesta.InfoBloque }
     * 
     */
    public MensajeSalida.RespuestaX.Respuesta.InfoBloque createMensajeSalidaRespuestaRespuestaInfoBloque() {
        return new MensajeSalida.RespuestaX.Respuesta.InfoBloque();
    }

    /**
     * Create an instance of {@link MensajeSalida.Respuesta.Respuesta.Bloque }
     * 
     */
    public MensajeSalida.RespuestaX.Respuesta.Bloque createMensajeSalidaRespuestaRespuestaBloque() {
        return new MensajeSalida.RespuestaX.Respuesta.Bloque();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "justificanteFirmaElectronica")
    public JAXBElement<byte[]> createJustificanteFirmaElectronica(byte[] value) {
        return new JAXBElement<byte[]>(_JustificanteFirmaElectronica_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "tipoDocumento")
    public JAXBElement<String> createTipoDocumento(String value) {
        return new JAXBElement<String>(_TipoDocumento_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "datos")
    public JAXBElement<byte[]> createDatos(byte[] value) {
        return new JAXBElement<byte[]>(_Datos_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "firmaElectronicaServidor")
    public JAXBElement<String> createFirmaElectronicaServidor(String value) {
        return new JAXBElement<String>(_FirmaElectronicaServidor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "idAplicacion")
    public JAXBElement<String> createIdAplicacion(String value) {
        return new JAXBElement<String>(_IdAplicacion_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "custodiarDocumento", defaultValue = "false")
    public JAXBElement<Boolean> createCustodiarDocumento(Boolean value) {
        return new JAXBElement<Boolean>(_CustodiarDocumento_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "estado")
    public JAXBElement<Boolean> createEstado(Boolean value) {
        return new JAXBElement<Boolean>(_Estado_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "algoritmoHash")
    public JAXBElement<String> createAlgoritmoHash(String value) {
        return new JAXBElement<String>(_AlgoritmoHash_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "idTransaccionBloque")
    public JAXBElement<String> createIdTransaccionBloque(String value) {
        return new JAXBElement<String>(_IdTransaccionBloque_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "idReferencia")
    public JAXBElement<String> createIdReferencia(String value) {
        return new JAXBElement<String>(_IdReferencia_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "certificadoFirmante")
    public JAXBElement<byte[]> createCertificadoFirmante(byte[] value) {
        return new JAXBElement<byte[]>(_CertificadoFirmante_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "documento")
    public JAXBElement<byte[]> createDocumento(byte[] value) {
        return new JAXBElement<byte[]>(_Documento_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "formatoFirma")
    public JAXBElement<String> createFormatoFirma(String value) {
        return new JAXBElement<String>(_FormatoFirma_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "firmante")
    public JAXBElement<String> createFirmante(String value) {
        return new JAXBElement<String>(_Firmante_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "idDocumento")
    public JAXBElement<String> createIdDocumento(String value) {
        return new JAXBElement<String>(_IdDocumento_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "bloqueFirmas")
    public JAXBElement<byte[]> createBloqueFirmas(byte[] value) {
        return new JAXBElement<byte[]>(_BloqueFirmas_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "nombreDocumento")
    public JAXBElement<String> createNombreDocumento(String value) {
        return new JAXBElement<String>(_NombreDocumento_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "hash")
    public JAXBElement<byte[]> createHash(byte[] value) {
        return new JAXBElement<byte[]>(_Hash_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "firmaElectronica")
    public JAXBElement<byte[]> createFirmaElectronica(byte[] value) {
        return new JAXBElement<byte[]>(_FirmaElectronica_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "firmanteObjetivo")
    public JAXBElement<byte[]> createFirmanteObjetivo(byte[] value) {
        return new JAXBElement<byte[]>(_FirmanteObjetivo_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "idTransaccion")
    public JAXBElement<String> createIdTransaccion(String value) {
        return new JAXBElement<String>(_IdTransaccion_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "conclusion", scope = TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.class)
    public JAXBElement<Object> createTipoValidacionFirmaElectronicaValidacionFirmaElectronicaConclusion(Object value) {
        return new JAXBElement<Object>(_TipoValidacionFirmaElectronicaValidacionFirmaElectronicaConclusion_QNAME, Object.class, TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "detalle", scope = TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.class)
    public JAXBElement<Object> createTipoValidacionFirmaElectronicaValidacionFirmaElectronicaDetalle(Object value) {
        return new JAXBElement<Object>(_TipoValidacionFirmaElectronicaValidacionFirmaElectronicaDetalle_QNAME, Object.class, TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "informacionAdicional", scope = TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.class)
    public JAXBElement<TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional> createTipoValidacionFirmaElectronicaValidacionFirmaElectronicaInformacionAdicional(TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional value) {
        return new JAXBElement<TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional>(_TipoValidacionFirmaElectronicaValidacionFirmaElectronicaInformacionAdicional_QNAME, TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional.class, TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "proceso", scope = TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.class)
    public JAXBElement<Object> createTipoValidacionFirmaElectronicaValidacionFirmaElectronicaProceso(Object value) {
        return new JAXBElement<Object>(_TipoValidacionFirmaElectronicaValidacionFirmaElectronicaProceso_QNAME, Object.class, TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "idBloque", scope = MensajeSalida.RespuestaX.Respuesta.Bloque.class)
    public JAXBElement<String> createMensajeSalidaRespuestaRespuestaBloqueIdBloque(String value) {
        return new JAXBElement<String>(_MensajeSalidaRespuestaRespuestaBloqueIdBloque_QNAME, String.class, MensajeSalida.RespuestaX.Respuesta.Bloque.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MensajeSalida.Respuesta.Respuesta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://afirmaws/ws/firma", name = "Respuesta", scope = MensajeSalida.RespuestaX.class)
    public JAXBElement<MensajeSalida.RespuestaX.Respuesta> createMensajeSalidaRespuestaRespuesta(MensajeSalida.RespuestaX.Respuesta value) {
        return new JAXBElement<MensajeSalida.RespuestaX.Respuesta>(_MensajeSalidaRespuestaRespuesta_QNAME, MensajeSalida.RespuestaX.Respuesta.class, MensajeSalida.RespuestaX.class, value);
    }

}

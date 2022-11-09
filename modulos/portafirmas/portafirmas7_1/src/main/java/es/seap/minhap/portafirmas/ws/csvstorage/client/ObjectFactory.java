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


package es.seap.minhap.portafirmas.ws.csvstorage.client;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.seap.minhap.portafirmas.ws.csvstorage.client package. 
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

    private final static QName _ModificarDocumentoEniResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "modificarDocumentoEniResponse");
    private final static QName _PGPData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPData");
    private final static QName _EliminarDocumento_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "eliminarDocumento");
    private final static QName _ObtenerDocumentoENI_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "obtenerDocumentoENI");
    private final static QName _SPKIData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SPKIData");
    private final static QName _ConsultarDocumento_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "consultarDocumento");
    private final static QName _RetrievalMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
    private final static QName _CanonicalizationMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod");
    private final static QName _ConsultarDocumentoResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "consultarDocumentoResponse");
    private final static QName _Metadatos_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "metadatos");
    private final static QName _ConvertirDocumentoAEni_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "convertirDocumentoAEni");
    private final static QName _SignatureProperty_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperty");
    private final static QName _GuardarDocumentoENI_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "guardarDocumentoENI");
    private final static QName _OtorgarPermisoConsulta_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "otorgarPermisoConsulta");
    private final static QName _Transforms_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Transforms");
    private final static QName _Manifest_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Manifest");
    private final static QName _ObtenerDocumentoEniResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "obtenerDocumentoEniResponse");
    private final static QName _SignatureMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod");
    private final static QName _KeyInfo_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo");
    private final static QName _GuardarDocumentoResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "guardarDocumentoResponse");
    private final static QName _DigestMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
    private final static QName _MgmtData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "MgmtData");
    private final static QName _OtorgarPermisoConsultaResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "otorgarPermisoConsultaResponse");
    private final static QName _ConsultarAplicaciones_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "consultarAplicaciones");
    private final static QName _Reference_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Reference");
    private final static QName _ConsultarPermisosDocumento_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "consultarPermisosDocumento");
    private final static QName _RevocarPermisoConsultaResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "revocarPermisoConsultaResponse");
    private final static QName _RSAKeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValue");
    private final static QName _GuardarDocumentoEniResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "guardarDocumentoEniResponse");
    private final static QName _Signature_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Signature");
    private final static QName _DSAKeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue");
    private final static QName _EliminarDocumentoResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "eliminarDocumentoResponse");
    private final static QName _ModificarDocumentoResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "modificarDocumentoResponse");
    private final static QName _ConsultarPermisosDocumentoResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "consultarPermisosDocumentoResponse");
    private final static QName _ObtenerDocumentoResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "obtenerDocumentoResponse");
    private final static QName _SignedInfo_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo");
    private final static QName _ObtenerDocumento_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "obtenerDocumento");
    private final static QName _RevocarPermisoConsulta_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "revocarPermisoConsulta");
    private final static QName _Object_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Object");
    private final static QName _ObtenerTamanioDocumentoResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "obtenerTamanioDocumentoResponse");
    private final static QName _ErrorInfo_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "errorInfo");
    private final static QName _ConvertirDocumentoEniResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "convertirDocumentoEniResponse");
    private final static QName _Firmas_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/firma", "firmas");
    private final static QName _SignatureValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
    private final static QName _Transform_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Transform");
    private final static QName _X509Data_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data");
    private final static QName _ModificarDocumentoENI_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "modificarDocumentoENI");
    private final static QName _CSVStorageException_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "CSVStorageException");
    private final static QName _ConsultarAplicacionesResponse_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "consultarAplicacionesResponse");
    private final static QName _GuardarDocumento_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "guardarDocumento");
    private final static QName _Contenido_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/contenido", "contenido");
    private final static QName _ObtenerTamanioDocumento_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "obtenerTamanioDocumento");
    private final static QName _ModificarDocumento_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:v1.0", "modificarDocumento");
    private final static QName _DigestValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue");
    private final static QName _SignatureProperties_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperties");
    private final static QName _KeyName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyName");
    private final static QName _KeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyValue");
    private final static QName _Documento_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e", "documento");
    private final static QName _Aplicacion_QNAME = new QName("urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", "Aplicacion");
    private final static QName _SignatureMethodTypeHMACOutputLength_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "HMACOutputLength");
    private final static QName _TransformTypeXPath_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "XPath");
    private final static QName _X509DataTypeX509IssuerSerial_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
    private final static QName _X509DataTypeX509Certificate_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
    private final static QName _X509DataTypeX509SKI_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SKI");
    private final static QName _X509DataTypeX509SubjectName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName");
    private final static QName _X509DataTypeX509CRL_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509CRL");
    private final static QName _SPKIDataTypeSPKISexp_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SPKISexp");
    private final static QName _PGPDataTypePGPKeyID_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyID");
    private final static QName _PGPDataTypePGPKeyPacket_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyPacket");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.seap.minhap.portafirmas.ws.csvstorage.client
     * 
     */
    public ObjectFactory() {
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
     * Create an instance of {@link ConvertirDocumentoEniRequest }
     * 
     */
    public ConvertirDocumentoEniRequest createConvertirDocumentoEniRequest() {
        return new ConvertirDocumentoEniRequest();
    }

    /**
     * Create an instance of {@link ModificarDocumentoENI }
     * 
     */
    public ModificarDocumentoENI createModificarDocumentoENI() {
        return new ModificarDocumentoENI();
    }

    /**
     * Create an instance of {@link EliminarDocumento }
     * 
     */
    public EliminarDocumento createEliminarDocumento() {
        return new EliminarDocumento();
    }

    /**
     * Create an instance of {@link ConsultarAplicacionesResponse }
     * 
     */
    public ConsultarAplicacionesResponse createConsultarAplicacionesResponse() {
        return new ConsultarAplicacionesResponse();
    }

    /**
     * Create an instance of {@link GuardarDocumento }
     * 
     */
    public GuardarDocumento createGuardarDocumento() {
        return new GuardarDocumento();
    }

    /**
     * Create an instance of {@link ObtenerDocumentoENI }
     * 
     */
    public ObtenerDocumentoENI createObtenerDocumentoENI() {
        return new ObtenerDocumentoENI();
    }

    /**
     * Create an instance of {@link ObtenerDocumento }
     * 
     */
    public ObtenerDocumento createObtenerDocumento() {
        return new ObtenerDocumento();
    }

    /**
     * Create an instance of {@link RevocarPermisoConsulta }
     * 
     */
    public RevocarPermisoConsulta createRevocarPermisoConsulta() {
        return new RevocarPermisoConsulta();
    }

    /**
     * Create an instance of {@link ObtenerTamanioDocumento }
     * 
     */
    public ObtenerTamanioDocumento createObtenerTamanioDocumento() {
        return new ObtenerTamanioDocumento();
    }

    /**
     * Create an instance of {@link ConsultarDocumento }
     * 
     */
    public ConsultarDocumento createConsultarDocumento() {
        return new ConsultarDocumento();
    }

    /**
     * Create an instance of {@link ModificarDocumento }
     * 
     */
    public ModificarDocumento createModificarDocumento() {
        return new ModificarDocumento();
    }

    /**
     * Create an instance of {@link ConsultarPermisosDocumento }
     * 
     */
    public ConsultarPermisosDocumento createConsultarPermisosDocumento() {
        return new ConsultarPermisosDocumento();
    }

    /**
     * Create an instance of {@link ConsultarAplicaciones }
     * 
     */
    public ConsultarAplicaciones createConsultarAplicaciones() {
        return new ConsultarAplicaciones();
    }

    /**
     * Create an instance of {@link GuardarDocumentoENI }
     * 
     */
    public GuardarDocumentoENI createGuardarDocumentoENI() {
        return new GuardarDocumentoENI();
    }

    /**
     * Create an instance of {@link OtorgarPermisoConsulta }
     * 
     */
    public OtorgarPermisoConsulta createOtorgarPermisoConsulta() {
        return new OtorgarPermisoConsulta();
    }

    /**
     * Create an instance of {@link ConvertirDocumentoAEni }
     * 
     */
    public ConvertirDocumentoAEni createConvertirDocumentoAEni() {
        return new ConvertirDocumentoAEni();
    }

    /**
     * Create an instance of {@link KeyInfoType }
     * 
     */
    public KeyInfoType createKeyInfoType() {
        return new KeyInfoType();
    }

    /**
     * Create an instance of {@link SignedInfoType }
     * 
     */
    public SignedInfoType createSignedInfoType() {
        return new SignedInfoType();
    }

    /**
     * Create an instance of {@link RetrievalMethodType }
     * 
     */
    public RetrievalMethodType createRetrievalMethodType() {
        return new RetrievalMethodType();
    }

    /**
     * Create an instance of {@link DigestMethodType }
     * 
     */
    public DigestMethodType createDigestMethodType() {
        return new DigestMethodType();
    }

    /**
     * Create an instance of {@link SignatureMethodType }
     * 
     */
    public SignatureMethodType createSignatureMethodType() {
        return new SignatureMethodType();
    }

    /**
     * Create an instance of {@link SPKIDataType }
     * 
     */
    public SPKIDataType createSPKIDataType() {
        return new SPKIDataType();
    }

    /**
     * Create an instance of {@link X509DataType }
     * 
     */
    public X509DataType createX509DataType() {
        return new X509DataType();
    }

    /**
     * Create an instance of {@link SignatureType }
     * 
     */
    public SignatureType createSignatureType() {
        return new SignatureType();
    }

    /**
     * Create an instance of {@link PGPDataType }
     * 
     */
    public PGPDataType createPGPDataType() {
        return new PGPDataType();
    }

    /**
     * Create an instance of {@link DSAKeyValueType }
     * 
     */
    public DSAKeyValueType createDSAKeyValueType() {
        return new DSAKeyValueType();
    }

    /**
     * Create an instance of {@link TransformsType }
     * 
     */
    public TransformsType createTransformsType() {
        return new TransformsType();
    }

    /**
     * Create an instance of {@link SignatureValueType }
     * 
     */
    public SignatureValueType createSignatureValueType() {
        return new SignatureValueType();
    }

    /**
     * Create an instance of {@link ManifestType }
     * 
     */
    public ManifestType createManifestType() {
        return new ManifestType();
    }

    /**
     * Create an instance of {@link RSAKeyValueType }
     * 
     */
    public RSAKeyValueType createRSAKeyValueType() {
        return new RSAKeyValueType();
    }

    /**
     * Create an instance of {@link TransformType }
     * 
     */
    public TransformType createTransformType() {
        return new TransformType();
    }

    /**
     * Create an instance of {@link SignaturePropertyType }
     * 
     */
    public SignaturePropertyType createSignaturePropertyType() {
        return new SignaturePropertyType();
    }

    /**
     * Create an instance of {@link KeyValueType }
     * 
     */
    public KeyValueType createKeyValueType() {
        return new KeyValueType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link CanonicalizationMethodType }
     * 
     */
    public CanonicalizationMethodType createCanonicalizationMethodType() {
        return new CanonicalizationMethodType();
    }

    /**
     * Create an instance of {@link SignaturePropertiesType }
     * 
     */
    public SignaturePropertiesType createSignaturePropertiesType() {
        return new SignaturePropertiesType();
    }

    /**
     * Create an instance of {@link ObjectType }
     * 
     */
    public ObjectType createObjectType() {
        return new ObjectType();
    }

    /**
     * Create an instance of {@link X509IssuerSerialType }
     * 
     */
    public X509IssuerSerialType createX509IssuerSerialType() {
        return new X509IssuerSerialType();
    }

    /**
     * Create an instance of {@link GuardarDocumentoResponse }
     * 
     */
    public GuardarDocumentoResponse createGuardarDocumentoResponse() {
        return new GuardarDocumentoResponse();
    }

    /**
     * Create an instance of {@link ConvertirDocumentoEniResponse }
     * 
     */
    public ConvertirDocumentoEniResponse createConvertirDocumentoEniResponse() {
        return new ConvertirDocumentoEniResponse();
    }

    /**
     * Create an instance of {@link ObtenerTamanioDocumentoResponse }
     * 
     */
    public ObtenerTamanioDocumentoResponse createObtenerTamanioDocumentoResponse() {
        return new ObtenerTamanioDocumentoResponse();
    }

    /**
     * Create an instance of {@link ConsultarDocumentoResponse }
     * 
     */
    public ConsultarDocumentoResponse createConsultarDocumentoResponse() {
        return new ConsultarDocumentoResponse();
    }

    /**
     * Create an instance of {@link Aplicacion }
     * 
     */
    public Aplicacion createAplicacion() {
        return new Aplicacion();
    }

    /**
     * Create an instance of {@link CSVStorageException }
     * 
     */
    public CSVStorageException createCSVStorageException() {
        return new CSVStorageException();
    }

    /**
     * Create an instance of {@link EliminarDocumentoResponse }
     * 
     */
    public EliminarDocumentoResponse createEliminarDocumentoResponse() {
        return new EliminarDocumentoResponse();
    }

    /**
     * Create an instance of {@link GuardarDocumentoEniResponse }
     * 
     */
    public GuardarDocumentoEniResponse createGuardarDocumentoEniResponse() {
        return new GuardarDocumentoEniResponse();
    }

    /**
     * Create an instance of {@link ObtenerDocumentoEniResponse }
     * 
     */
    public ObtenerDocumentoEniResponse createObtenerDocumentoEniResponse() {
        return new ObtenerDocumentoEniResponse();
    }

    /**
     * Create an instance of {@link ObtenerDocumentoResponse }
     * 
     */
    public ObtenerDocumentoResponse createObtenerDocumentoResponse() {
        return new ObtenerDocumentoResponse();
    }

    /**
     * Create an instance of {@link ConsultarPermisosDocumentoResponse }
     * 
     */
    public ConsultarPermisosDocumentoResponse createConsultarPermisosDocumentoResponse() {
        return new ConsultarPermisosDocumentoResponse();
    }

    /**
     * Create an instance of {@link ConsultarDocumentoRequest }
     * 
     */
    public ConsultarDocumentoRequest createConsultarDocumentoRequest() {
        return new ConsultarDocumentoRequest();
    }

    /**
     * Create an instance of {@link Restricciones }
     * 
     */
    public Restricciones createRestricciones() {
        return new Restricciones();
    }

    /**
     * Create an instance of {@link GuardarDocumentoRequest }
     * 
     */
    public GuardarDocumentoRequest createGuardarDocumentoRequest() {
        return new GuardarDocumentoRequest();
    }

    /**
     * Create an instance of {@link GuardarDocumentoEniRequest }
     * 
     */
    public GuardarDocumentoEniRequest createGuardarDocumentoEniRequest() {
        return new GuardarDocumentoEniRequest();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link RestringidoAplicaciones }
     * 
     */
    public RestringidoAplicaciones createRestringidoAplicaciones() {
        return new RestringidoAplicaciones();
    }

    /**
     * Create an instance of {@link DocumentoEniResponse }
     * 
     */
    public DocumentoEniResponse createDocumentoEniResponse() {
        return new DocumentoEniResponse();
    }

    /**
     * Create an instance of {@link ContenidoInfo }
     * 
     */
    public ContenidoInfo createContenidoInfo() {
        return new ContenidoInfo();
    }

    /**
     * Create an instance of {@link DocumentoPermisoResponse }
     * 
     */
    public DocumentoPermisoResponse createDocumentoPermisoResponse() {
        return new DocumentoPermisoResponse();
    }

    /**
     * Create an instance of {@link DocumentoRevocarPermisoRequest }
     * 
     */
    public DocumentoRevocarPermisoRequest createDocumentoRevocarPermisoRequest() {
        return new DocumentoRevocarPermisoRequest();
    }

    /**
     * Create an instance of {@link TamanioDocumentoResponse }
     * 
     */
    public TamanioDocumentoResponse createTamanioDocumentoResponse() {
        return new TamanioDocumentoResponse();
    }

    /**
     * Create an instance of {@link RestringidoPorIdentificacion }
     * 
     */
    public RestringidoPorIdentificacion createRestringidoPorIdentificacion() {
        return new RestringidoPorIdentificacion();
    }

    /**
     * Create an instance of {@link DocumentoRequest }
     * 
     */
    public DocumentoRequest createDocumentoRequest() {
        return new DocumentoRequest();
    }

    /**
     * Create an instance of {@link DocumentoPermisoRequest }
     * 
     */
    public DocumentoPermisoRequest createDocumentoPermisoRequest() {
        return new DocumentoPermisoRequest();
    }

    /**
     * Create an instance of {@link RestringidoNif }
     * 
     */
    public RestringidoNif createRestringidoNif() {
        return new RestringidoNif();
    }

    /**
     * Create an instance of {@link ObtenerDocumentoRequest }
     * 
     */
    public ObtenerDocumentoRequest createObtenerDocumentoRequest() {
        return new ObtenerDocumentoRequest();
    }

    /**
     * Create an instance of {@link DocumentoResponse }
     * 
     */
    public DocumentoResponse createDocumentoResponse() {
        return new DocumentoResponse();
    }

    /**
     * Create an instance of {@link EliminarDocumentoRequest }
     * 
     */
    public EliminarDocumentoRequest createEliminarDocumentoRequest() {
        return new EliminarDocumentoRequest();
    }

    /**
     * Create an instance of {@link ObtenerDocumentoEniRequest }
     * 
     */
    public ObtenerDocumentoEniRequest createObtenerDocumentoEniRequest() {
        return new ObtenerDocumentoEniRequest();
    }

    /**
     * Create an instance of {@link ConsultarPermisosDocumentoRequest }
     * 
     */
    public ConsultarPermisosDocumentoRequest createConsultarPermisosDocumentoRequest() {
        return new ConsultarPermisosDocumentoRequest();
    }

    /**
     * Create an instance of {@link Firmas }
     * 
     */
    public Firmas createFirmas() {
        return new Firmas();
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
     * Create an instance of {@link ConvertirDocumentoEniRequest.MetadatosEni }
     * 
     */
    public ConvertirDocumentoEniRequest.MetadatosEni createConvertirDocumentoEniRequestMetadatosEni() {
        return new ConvertirDocumentoEniRequest.MetadatosEni();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuardarDocumentoEniResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "modificarDocumentoEniResponse")
    public JAXBElement<GuardarDocumentoEniResponse> createModificarDocumentoEniResponse(GuardarDocumentoEniResponse value) {
        return new JAXBElement<GuardarDocumentoEniResponse>(_ModificarDocumentoEniResponse_QNAME, GuardarDocumentoEniResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PGPDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPData")
    public JAXBElement<PGPDataType> createPGPData(PGPDataType value) {
        return new JAXBElement<PGPDataType>(_PGPData_QNAME, PGPDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "eliminarDocumento")
    public JAXBElement<EliminarDocumento> createEliminarDocumento(EliminarDocumento value) {
        return new JAXBElement<EliminarDocumento>(_EliminarDocumento_QNAME, EliminarDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerDocumentoENI }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "obtenerDocumentoENI")
    public JAXBElement<ObtenerDocumentoENI> createObtenerDocumentoENI(ObtenerDocumentoENI value) {
        return new JAXBElement<ObtenerDocumentoENI>(_ObtenerDocumentoENI_QNAME, ObtenerDocumentoENI.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SPKIDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SPKIData")
    public JAXBElement<SPKIDataType> createSPKIData(SPKIDataType value) {
        return new JAXBElement<SPKIDataType>(_SPKIData_QNAME, SPKIDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "consultarDocumento")
    public JAXBElement<ConsultarDocumento> createConsultarDocumento(ConsultarDocumento value) {
        return new JAXBElement<ConsultarDocumento>(_ConsultarDocumento_QNAME, ConsultarDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrievalMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "RetrievalMethod")
    public JAXBElement<RetrievalMethodType> createRetrievalMethod(RetrievalMethodType value) {
        return new JAXBElement<RetrievalMethodType>(_RetrievalMethod_QNAME, RetrievalMethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CanonicalizationMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "CanonicalizationMethod")
    public JAXBElement<CanonicalizationMethodType> createCanonicalizationMethod(CanonicalizationMethodType value) {
        return new JAXBElement<CanonicalizationMethodType>(_CanonicalizationMethod_QNAME, CanonicalizationMethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "consultarDocumentoResponse")
    public JAXBElement<ConsultarDocumentoResponse> createConsultarDocumentoResponse(ConsultarDocumentoResponse value) {
        return new JAXBElement<ConsultarDocumentoResponse>(_ConsultarDocumentoResponse_QNAME, ConsultarDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoMetadatos }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", name = "metadatos")
    public JAXBElement<TipoMetadatos> createMetadatos(TipoMetadatos value) {
        return new JAXBElement<TipoMetadatos>(_Metadatos_QNAME, TipoMetadatos.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirDocumentoAEni }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "convertirDocumentoAEni")
    public JAXBElement<ConvertirDocumentoAEni> createConvertirDocumentoAEni(ConvertirDocumentoAEni value) {
        return new JAXBElement<ConvertirDocumentoAEni>(_ConvertirDocumentoAEni_QNAME, ConvertirDocumentoAEni.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignaturePropertyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureProperty")
    public JAXBElement<SignaturePropertyType> createSignatureProperty(SignaturePropertyType value) {
        return new JAXBElement<SignaturePropertyType>(_SignatureProperty_QNAME, SignaturePropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuardarDocumentoENI }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "guardarDocumentoENI")
    public JAXBElement<GuardarDocumentoENI> createGuardarDocumentoENI(GuardarDocumentoENI value) {
        return new JAXBElement<GuardarDocumentoENI>(_GuardarDocumentoENI_QNAME, GuardarDocumentoENI.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OtorgarPermisoConsulta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "otorgarPermisoConsulta")
    public JAXBElement<OtorgarPermisoConsulta> createOtorgarPermisoConsulta(OtorgarPermisoConsulta value) {
        return new JAXBElement<OtorgarPermisoConsulta>(_OtorgarPermisoConsulta_QNAME, OtorgarPermisoConsulta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransformsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Transforms")
    public JAXBElement<TransformsType> createTransforms(TransformsType value) {
        return new JAXBElement<TransformsType>(_Transforms_QNAME, TransformsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManifestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Manifest")
    public JAXBElement<ManifestType> createManifest(ManifestType value) {
        return new JAXBElement<ManifestType>(_Manifest_QNAME, ManifestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerDocumentoEniResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "obtenerDocumentoEniResponse")
    public JAXBElement<ObtenerDocumentoEniResponse> createObtenerDocumentoEniResponse(ObtenerDocumentoEniResponse value) {
        return new JAXBElement<ObtenerDocumentoEniResponse>(_ObtenerDocumentoEniResponse_QNAME, ObtenerDocumentoEniResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureMethod")
    public JAXBElement<SignatureMethodType> createSignatureMethod(SignatureMethodType value) {
        return new JAXBElement<SignatureMethodType>(_SignatureMethod_QNAME, SignatureMethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyInfo")
    public JAXBElement<KeyInfoType> createKeyInfo(KeyInfoType value) {
        return new JAXBElement<KeyInfoType>(_KeyInfo_QNAME, KeyInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuardarDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "guardarDocumentoResponse")
    public JAXBElement<GuardarDocumentoResponse> createGuardarDocumentoResponse(GuardarDocumentoResponse value) {
        return new JAXBElement<GuardarDocumentoResponse>(_GuardarDocumentoResponse_QNAME, GuardarDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DigestMethodType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DigestMethod")
    public JAXBElement<DigestMethodType> createDigestMethod(DigestMethodType value) {
        return new JAXBElement<DigestMethodType>(_DigestMethod_QNAME, DigestMethodType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "MgmtData")
    public JAXBElement<String> createMgmtData(String value) {
        return new JAXBElement<String>(_MgmtData_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuardarDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "otorgarPermisoConsultaResponse")
    public JAXBElement<GuardarDocumentoResponse> createOtorgarPermisoConsultaResponse(GuardarDocumentoResponse value) {
        return new JAXBElement<GuardarDocumentoResponse>(_OtorgarPermisoConsultaResponse_QNAME, GuardarDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarAplicaciones }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "consultarAplicaciones")
    public JAXBElement<ConsultarAplicaciones> createConsultarAplicaciones(ConsultarAplicaciones value) {
        return new JAXBElement<ConsultarAplicaciones>(_ConsultarAplicaciones_QNAME, ConsultarAplicaciones.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Reference")
    public JAXBElement<ReferenceType> createReference(ReferenceType value) {
        return new JAXBElement<ReferenceType>(_Reference_QNAME, ReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarPermisosDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "consultarPermisosDocumento")
    public JAXBElement<ConsultarPermisosDocumento> createConsultarPermisosDocumento(ConsultarPermisosDocumento value) {
        return new JAXBElement<ConsultarPermisosDocumento>(_ConsultarPermisosDocumento_QNAME, ConsultarPermisosDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuardarDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "revocarPermisoConsultaResponse")
    public JAXBElement<GuardarDocumentoResponse> createRevocarPermisoConsultaResponse(GuardarDocumentoResponse value) {
        return new JAXBElement<GuardarDocumentoResponse>(_RevocarPermisoConsultaResponse_QNAME, GuardarDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RSAKeyValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "RSAKeyValue")
    public JAXBElement<RSAKeyValueType> createRSAKeyValue(RSAKeyValueType value) {
        return new JAXBElement<RSAKeyValueType>(_RSAKeyValue_QNAME, RSAKeyValueType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuardarDocumentoEniResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "guardarDocumentoEniResponse")
    public JAXBElement<GuardarDocumentoEniResponse> createGuardarDocumentoEniResponse(GuardarDocumentoEniResponse value) {
        return new JAXBElement<GuardarDocumentoEniResponse>(_GuardarDocumentoEniResponse_QNAME, GuardarDocumentoEniResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Signature")
    public JAXBElement<SignatureType> createSignature(SignatureType value) {
        return new JAXBElement<SignatureType>(_Signature_QNAME, SignatureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DSAKeyValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DSAKeyValue")
    public JAXBElement<DSAKeyValueType> createDSAKeyValue(DSAKeyValueType value) {
        return new JAXBElement<DSAKeyValueType>(_DSAKeyValue_QNAME, DSAKeyValueType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "eliminarDocumentoResponse")
    public JAXBElement<EliminarDocumentoResponse> createEliminarDocumentoResponse(EliminarDocumentoResponse value) {
        return new JAXBElement<EliminarDocumentoResponse>(_EliminarDocumentoResponse_QNAME, EliminarDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuardarDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "modificarDocumentoResponse")
    public JAXBElement<GuardarDocumentoResponse> createModificarDocumentoResponse(GuardarDocumentoResponse value) {
        return new JAXBElement<GuardarDocumentoResponse>(_ModificarDocumentoResponse_QNAME, GuardarDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarPermisosDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "consultarPermisosDocumentoResponse")
    public JAXBElement<ConsultarPermisosDocumentoResponse> createConsultarPermisosDocumentoResponse(ConsultarPermisosDocumentoResponse value) {
        return new JAXBElement<ConsultarPermisosDocumentoResponse>(_ConsultarPermisosDocumentoResponse_QNAME, ConsultarPermisosDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "obtenerDocumentoResponse")
    public JAXBElement<ObtenerDocumentoResponse> createObtenerDocumentoResponse(ObtenerDocumentoResponse value) {
        return new JAXBElement<ObtenerDocumentoResponse>(_ObtenerDocumentoResponse_QNAME, ObtenerDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignedInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignedInfo")
    public JAXBElement<SignedInfoType> createSignedInfo(SignedInfoType value) {
        return new JAXBElement<SignedInfoType>(_SignedInfo_QNAME, SignedInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "obtenerDocumento")
    public JAXBElement<ObtenerDocumento> createObtenerDocumento(ObtenerDocumento value) {
        return new JAXBElement<ObtenerDocumento>(_ObtenerDocumento_QNAME, ObtenerDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RevocarPermisoConsulta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "revocarPermisoConsulta")
    public JAXBElement<RevocarPermisoConsulta> createRevocarPermisoConsulta(RevocarPermisoConsulta value) {
        return new JAXBElement<RevocarPermisoConsulta>(_RevocarPermisoConsulta_QNAME, RevocarPermisoConsulta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Object")
    public JAXBElement<ObjectType> createObject(ObjectType value) {
        return new JAXBElement<ObjectType>(_Object_QNAME, ObjectType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerTamanioDocumentoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "obtenerTamanioDocumentoResponse")
    public JAXBElement<ObtenerTamanioDocumentoResponse> createObtenerTamanioDocumentoResponse(ObtenerTamanioDocumentoResponse value) {
        return new JAXBElement<ObtenerTamanioDocumentoResponse>(_ObtenerTamanioDocumentoResponse_QNAME, ObtenerTamanioDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "errorInfo")
    public JAXBElement<Object> createErrorInfo(Object value) {
        return new JAXBElement<Object>(_ErrorInfo_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirDocumentoEniResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "convertirDocumentoEniResponse")
    public JAXBElement<ConvertirDocumentoEniResponse> createConvertirDocumentoEniResponse(ConvertirDocumentoEniResponse value) {
        return new JAXBElement<ConvertirDocumentoEniResponse>(_ConvertirDocumentoEniResponse_QNAME, ConvertirDocumentoEniResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Firmas }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/firma", name = "firmas")
    public JAXBElement<Firmas> createFirmas(Firmas value) {
        return new JAXBElement<Firmas>(_Firmas_QNAME, Firmas.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureValue")
    public JAXBElement<SignatureValueType> createSignatureValue(SignatureValueType value) {
        return new JAXBElement<SignatureValueType>(_SignatureValue_QNAME, SignatureValueType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransformType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "Transform")
    public JAXBElement<TransformType> createTransform(TransformType value) {
        return new JAXBElement<TransformType>(_Transform_QNAME, TransformType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link X509DataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509Data")
    public JAXBElement<X509DataType> createX509Data(X509DataType value) {
        return new JAXBElement<X509DataType>(_X509Data_QNAME, X509DataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModificarDocumentoENI }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "modificarDocumentoENI")
    public JAXBElement<ModificarDocumentoENI> createModificarDocumentoENI(ModificarDocumentoENI value) {
        return new JAXBElement<ModificarDocumentoENI>(_ModificarDocumentoENI_QNAME, ModificarDocumentoENI.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CSVStorageException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "CSVStorageException")
    public JAXBElement<CSVStorageException> createCSVStorageException(CSVStorageException value) {
        return new JAXBElement<CSVStorageException>(_CSVStorageException_QNAME, CSVStorageException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarAplicacionesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "consultarAplicacionesResponse")
    public JAXBElement<ConsultarAplicacionesResponse> createConsultarAplicacionesResponse(ConsultarAplicacionesResponse value) {
        return new JAXBElement<ConsultarAplicacionesResponse>(_ConsultarAplicacionesResponse_QNAME, ConsultarAplicacionesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GuardarDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "guardarDocumento")
    public JAXBElement<GuardarDocumento> createGuardarDocumento(GuardarDocumento value) {
        return new JAXBElement<GuardarDocumento>(_GuardarDocumento_QNAME, GuardarDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoContenido }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/contenido", name = "contenido")
    public JAXBElement<TipoContenido> createContenido(TipoContenido value) {
        return new JAXBElement<TipoContenido>(_Contenido_QNAME, TipoContenido.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerTamanioDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "obtenerTamanioDocumento")
    public JAXBElement<ObtenerTamanioDocumento> createObtenerTamanioDocumento(ObtenerTamanioDocumento value) {
        return new JAXBElement<ObtenerTamanioDocumento>(_ObtenerTamanioDocumento_QNAME, ObtenerTamanioDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModificarDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:v1.0", name = "modificarDocumento")
    public JAXBElement<ModificarDocumento> createModificarDocumento(ModificarDocumento value) {
        return new JAXBElement<ModificarDocumento>(_ModificarDocumento_QNAME, ModificarDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DigestValue")
    public JAXBElement<byte[]> createDigestValue(byte[] value) {
        return new JAXBElement<byte[]>(_DigestValue_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignaturePropertiesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureProperties")
    public JAXBElement<SignaturePropertiesType> createSignatureProperties(SignaturePropertiesType value) {
        return new JAXBElement<SignaturePropertiesType>(_SignatureProperties_QNAME, SignaturePropertiesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyName")
    public JAXBElement<String> createKeyName(String value) {
        return new JAXBElement<String>(_KeyName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyValue")
    public JAXBElement<KeyValueType> createKeyValue(KeyValueType value) {
        return new JAXBElement<KeyValueType>(_KeyValue_QNAME, KeyValueType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoDocumento }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e", name = "documento")
    public JAXBElement<TipoDocumento> createDocumento(TipoDocumento value) {
        return new JAXBElement<TipoDocumento>(_Documento_QNAME, TipoDocumento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Aplicacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0", name = "Aplicacion")
    public JAXBElement<Aplicacion> createAplicacion(Aplicacion value) {
        return new JAXBElement<Aplicacion>(_Aplicacion_QNAME, Aplicacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "HMACOutputLength", scope = SignatureMethodType.class)
    public JAXBElement<BigInteger> createSignatureMethodTypeHMACOutputLength(BigInteger value) {
        return new JAXBElement<BigInteger>(_SignatureMethodTypeHMACOutputLength_QNAME, BigInteger.class, SignatureMethodType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "XPath", scope = TransformType.class)
    public JAXBElement<String> createTransformTypeXPath(String value) {
        return new JAXBElement<String>(_TransformTypeXPath_QNAME, String.class, TransformType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link X509IssuerSerialType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509IssuerSerial", scope = X509DataType.class)
    public JAXBElement<X509IssuerSerialType> createX509DataTypeX509IssuerSerial(X509IssuerSerialType value) {
        return new JAXBElement<X509IssuerSerialType>(_X509DataTypeX509IssuerSerial_QNAME, X509IssuerSerialType.class, X509DataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509Certificate", scope = X509DataType.class)
    public JAXBElement<byte[]> createX509DataTypeX509Certificate(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeX509Certificate_QNAME, byte[].class, X509DataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509SKI", scope = X509DataType.class)
    public JAXBElement<byte[]> createX509DataTypeX509SKI(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeX509SKI_QNAME, byte[].class, X509DataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509SubjectName", scope = X509DataType.class)
    public JAXBElement<String> createX509DataTypeX509SubjectName(String value) {
        return new JAXBElement<String>(_X509DataTypeX509SubjectName_QNAME, String.class, X509DataType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509CRL", scope = X509DataType.class)
    public JAXBElement<byte[]> createX509DataTypeX509CRL(byte[] value) {
        return new JAXBElement<byte[]>(_X509DataTypeX509CRL_QNAME, byte[].class, X509DataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SPKISexp", scope = SPKIDataType.class)
    public JAXBElement<byte[]> createSPKIDataTypeSPKISexp(byte[] value) {
        return new JAXBElement<byte[]>(_SPKIDataTypeSPKISexp_QNAME, byte[].class, SPKIDataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPKeyID", scope = PGPDataType.class)
    public JAXBElement<byte[]> createPGPDataTypePGPKeyID(byte[] value) {
        return new JAXBElement<byte[]>(_PGPDataTypePGPKeyID_QNAME, byte[].class, PGPDataType.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "PGPKeyPacket", scope = PGPDataType.class)
    public JAXBElement<byte[]> createPGPDataTypePGPKeyPacket(byte[] value) {
        return new JAXBElement<byte[]>(_PGPDataTypePGPKeyPacket_QNAME, byte[].class, PGPDataType.class, ((byte[]) value));
    }

}

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


package es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles package. 
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

    private final static QName _PGPData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPData");
    private final static QName _ValidarExpedienteEniFileResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "validarExpedienteEniFileResponse");
    private final static QName _VisualizarDocumentoEni_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "visualizarDocumentoEni");
    private final static QName _SPKIData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SPKIData");
    private final static QName _ErrorInside_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebService/types/error", "errorInside");
    private final static QName _RetrievalMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
    private final static QName _ConvertirExpedienteAEniConMAdicionalesAutocontenido_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirExpedienteAEniConMAdicionalesAutocontenido");
    private final static QName _ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirExpedienteAEniConMAdicionalesAutocontenidoResponse");
    private final static QName _CanonicalizationMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod");
    private final static QName _Metadatos_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", "metadatos");
    private final static QName _ConvertirExpedienteAEniResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirExpedienteAEniResponse");
    private final static QName _Expediente_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e", "expediente");
    private final static QName _ConvertirExpedienteAEniAutocontenidoResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirExpedienteAEniAutocontenidoResponse");
    private final static QName _ExpedienteValidacion_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/expediente-e", "expedienteValidacion");
    private final static QName _SignatureProperty_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperty");
    private final static QName _ConvertirExpedienteAEniConMAdicionales_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirExpedienteAEniConMAdicionales");
    private final static QName _Transforms_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Transforms");
    private final static QName _Manifest_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Manifest");
    private final static QName _ConvertirDocumentoAEni_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirDocumentoAEni");
    private final static QName _VisualizarDocumentoEniResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "visualizarDocumentoEniResponse");
    private final static QName _SignatureMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod");
    private final static QName _TipoResultadoValidacionDocumentoInside_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/documento-e/resultados", "TipoResultadoValidacionDocumentoInside");
    private final static QName _KeyInfo_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo");
    private final static QName _ExpedienteConversionwsMtom_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion", "expedienteConversionwsMtom");
    private final static QName _DigestMethod_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
    private final static QName _ConvertirDocumentoAEniConMAdicionalesResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirDocumentoAEniConMAdicionalesResponse");
    private final static QName _MgmtData_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "MgmtData");
    private final static QName _TipoResultadoValidacionExpedienteInside_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/expediente-e/resultados", "TipoResultadoValidacionExpedienteInside");
    private final static QName _Reference_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Reference");
    private final static QName _ExpedienteConversionws_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion", "expedienteConversionws");
    private final static QName _ConvertirExpedienteAEniConMAdicionalesResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirExpedienteAEniConMAdicionalesResponse");
    private final static QName _ConvertirDocumentoAEniResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirDocumentoAEniResponse");
    private final static QName _RSAKeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValue");
    private final static QName _IndiceContenido_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/indice-e/contenido", "IndiceContenido");
    private final static QName _DocumentoEniFile_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/documento-e/documentoEniFile", "documentoEniFile");
    private final static QName _DocumentoConversion_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/documento-e/conversion", "documentoConversion");
    private final static QName _Signature_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Signature");
    private final static QName _DSAKeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue");
    private final static QName _SignedInfo_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo");
    private final static QName _ValidarDocumentoEniFile_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "validarDocumentoEniFile");
    private final static QName _DocumentoValidacion_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/documento-e", "documentoValidacion");
    private final static QName _Object_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Object");
    private final static QName _MetadatosExp_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/metadatos", "metadatosExp");
    private final static QName _ExpedienteConversion_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion", "expedienteConversion");
    private final static QName _Credential_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebService/types/credential", "credential");
    private final static QName _ValidarExpedienteEniFile_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "validarExpedienteEniFile");
    private final static QName _Firmas_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/firma", "firmas");
    private final static QName _DocumentoVisualizacion_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/visualizacion/documento-e", "documentoVisualizacion");
    private final static QName _SignatureValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
    private final static QName _ConvertirExpedienteAEniAutocontenido_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirExpedienteAEniAutocontenido");
    private final static QName _Transform_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "Transform");
    private final static QName _ValidarDocumentoEniFileResponse_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "validarDocumentoEniFileResponse");
    private final static QName _X509Data_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data");
    private final static QName _ConvertirExpedienteAEni_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirExpedienteAEni");
    private final static QName _ExpedienteEniFile_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/expedienteEniFile", "expedienteEniFile");
    private final static QName _Indice_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/indice-e", "indice");
    private final static QName _Contenido_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/contenido", "contenido");
    private final static QName _ConvertirDocumentoAEniConMAdicionales_QNAME = new QName("https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", "convertirDocumentoAEniConMAdicionales");
    private final static QName _DigestValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue");
    private final static QName _SignatureProperties_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperties");
    private final static QName _KeyName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyName");
    private final static QName _KeyValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyValue");
    private final static QName _Documento_QNAME = new QName("http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e", "documento");
    private final static QName _SPKIDataTypeSPKISexp_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SPKISexp");
    private final static QName _PGPDataTypePGPKeyID_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyID");
    private final static QName _PGPDataTypePGPKeyPacket_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyPacket");
    private final static QName _TransformTypeXPath_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "XPath");
    private final static QName _X509DataTypeX509IssuerSerial_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial");
    private final static QName _X509DataTypeX509Certificate_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
    private final static QName _X509DataTypeX509SKI_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SKI");
    private final static QName _X509DataTypeX509SubjectName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName");
    private final static QName _X509DataTypeX509CRL_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509CRL");
    private final static QName _SignatureMethodTypeHMACOutputLength_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "HMACOutputLength");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TipoOpcionesVisualizacionIndiceWS }
     * 
     */
    public TipoOpcionesVisualizacionIndiceWS createTipoOpcionesVisualizacionIndiceWS() {
        return new TipoOpcionesVisualizacionIndiceWS();
    }

    /**
     * Create an instance of {@link TipoOpcionesVisualizacionIndice }
     * 
     */
    public TipoOpcionesVisualizacionIndice createTipoOpcionesVisualizacionIndice() {
        return new TipoOpcionesVisualizacionIndice();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInsideWSMtom }
     * 
     */
    public TipoExpedienteConversionInsideWSMtom createTipoExpedienteConversionInsideWSMtom() {
        return new TipoExpedienteConversionInsideWSMtom();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInsideWSMtom.MetadatosEni }
     * 
     */
    public TipoExpedienteConversionInsideWSMtom.MetadatosEni createTipoExpedienteConversionInsideWSMtomMetadatosEni() {
        return new TipoExpedienteConversionInsideWSMtom.MetadatosEni();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInsideWS }
     * 
     */
    public TipoExpedienteConversionInsideWS createTipoExpedienteConversionInsideWS() {
        return new TipoExpedienteConversionInsideWS();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInsideWS.MetadatosEni }
     * 
     */
    public TipoExpedienteConversionInsideWS.MetadatosEni createTipoExpedienteConversionInsideWSMetadatosEni() {
        return new TipoExpedienteConversionInsideWS.MetadatosEni();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInside }
     * 
     */
    public TipoExpedienteConversionInside createTipoExpedienteConversionInside() {
        return new TipoExpedienteConversionInside();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInside.MetadatosEni }
     * 
     */
    public TipoExpedienteConversionInside.MetadatosEni createTipoExpedienteConversionInsideMetadatosEni() {
        return new TipoExpedienteConversionInside.MetadatosEni();
    }

    /**
     * Create an instance of {@link TipoOpcionesVisualizacionDocumento }
     * 
     */
    public TipoOpcionesVisualizacionDocumento createTipoOpcionesVisualizacionDocumento() {
        return new TipoOpcionesVisualizacionDocumento();
    }

    /**
     * Create an instance of {@link TipoDocumentoConversionInside }
     * 
     */
    public TipoDocumentoConversionInside createTipoDocumentoConversionInside() {
        return new TipoDocumentoConversionInside();
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
     * Create an instance of {@link TipoMetadatos }
     * 
     */
    public TipoMetadatos createTipoMetadatos() {
        return new TipoMetadatos();
    }

    /**
     * Create an instance of {@link TipoMetadatos2 }
     * 
     */
    public TipoMetadatos2 createTipoMetadatos2() {
        return new TipoMetadatos2();
    }

    /**
     * Create an instance of {@link TipoEstadoElaboracion }
     * 
     */
    public TipoEstadoElaboracion createTipoEstadoElaboracion() {
        return new TipoEstadoElaboracion();
    }

    /**
     * Create an instance of {@link TipoIndice }
     * 
     */
    public TipoIndice createTipoIndice() {
        return new TipoIndice();
    }

    /**
     * Create an instance of {@link TipoIndiceContenido }
     * 
     */
    public TipoIndiceContenido createTipoIndiceContenido() {
        return new TipoIndiceContenido();
    }

    /**
     * Create an instance of {@link TipoDocumentoIndizado }
     * 
     */
    public TipoDocumentoIndizado createTipoDocumentoIndizado() {
        return new TipoDocumentoIndizado();
    }

    /**
     * Create an instance of {@link TipoCarpetaIndizada }
     * 
     */
    public TipoCarpetaIndizada createTipoCarpetaIndizada() {
        return new TipoCarpetaIndizada();
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
     * Create an instance of {@link PGPDataType }
     * 
     */
    public PGPDataType createPGPDataType() {
        return new PGPDataType();
    }

    /**
     * Create an instance of {@link SignatureType }
     * 
     */
    public SignatureType createSignatureType() {
        return new SignatureType();
    }

    /**
     * Create an instance of {@link DSAKeyValueType }
     * 
     */
    public DSAKeyValueType createDSAKeyValueType() {
        return new DSAKeyValueType();
    }

    /**
     * Create an instance of {@link ManifestType }
     * 
     */
    public ManifestType createManifestType() {
        return new ManifestType();
    }

    /**
     * Create an instance of {@link SignatureValueType }
     * 
     */
    public SignatureValueType createSignatureValueType() {
        return new SignatureValueType();
    }

    /**
     * Create an instance of {@link TransformsType }
     * 
     */
    public TransformsType createTransformsType() {
        return new TransformsType();
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
     * Create an instance of {@link Firmas }
     * 
     */
    public Firmas createFirmas() {
        return new Firmas();
    }

    /**
     * Create an instance of {@link TipoExpedienteInsideConMAdicionales }
     * 
     */
    public TipoExpedienteInsideConMAdicionales createTipoExpedienteInsideConMAdicionales() {
        return new TipoExpedienteInsideConMAdicionales();
    }

    /**
     * Create an instance of {@link TipoDocumentoValidacionInside }
     * 
     */
    public TipoDocumentoValidacionInside createTipoDocumentoValidacionInside() {
        return new TipoDocumentoValidacionInside();
    }

    /**
     * Create an instance of {@link TipoOpcionesValidacionDocumentoInside }
     * 
     */
    public TipoOpcionesValidacionDocumentoInside createTipoOpcionesValidacionDocumentoInside() {
        return new TipoOpcionesValidacionDocumentoInside();
    }

    /**
     * Create an instance of {@link TipoDocumentoEniFileInside }
     * 
     */
    public TipoDocumentoEniFileInside createTipoDocumentoEniFileInside() {
        return new TipoDocumentoEniFileInside();
    }

    /**
     * Create an instance of {@link TipoDocumentoInsideConMAdicionales }
     * 
     */
    public TipoDocumentoInsideConMAdicionales createTipoDocumentoInsideConMAdicionales() {
        return new TipoDocumentoInsideConMAdicionales();
    }

    /**
     * Create an instance of {@link TipoDocumentoVisualizacionInside }
     * 
     */
    public TipoDocumentoVisualizacionInside createTipoDocumentoVisualizacionInside() {
        return new TipoDocumentoVisualizacionInside();
    }

    /**
     * Create an instance of {@link TipoDocumentoEniBinarioOTipo }
     * 
     */
    public TipoDocumentoEniBinarioOTipo createTipoDocumentoEniBinarioOTipo() {
        return new TipoDocumentoEniBinarioOTipo();
    }

    /**
     * Create an instance of {@link TipoResultadoVisualizacionDocumentoInside }
     * 
     */
    public TipoResultadoVisualizacionDocumentoInside createTipoResultadoVisualizacionDocumentoInside() {
        return new TipoResultadoVisualizacionDocumentoInside();
    }

    /**
     * Create an instance of {@link TipoResultadoValidacionExpedienteInside }
     * 
     */
    public TipoResultadoValidacionExpedienteInside createTipoResultadoValidacionExpedienteInside() {
        return new TipoResultadoValidacionExpedienteInside();
    }

    /**
     * Create an instance of {@link TipoResultadoValidacionDetalleExpedienteInside }
     * 
     */
    public TipoResultadoValidacionDetalleExpedienteInside createTipoResultadoValidacionDetalleExpedienteInside() {
        return new TipoResultadoValidacionDetalleExpedienteInside();
    }

    /**
     * Create an instance of {@link TipoResultadoValidacionDocumentoInside }
     * 
     */
    public TipoResultadoValidacionDocumentoInside createTipoResultadoValidacionDocumentoInside() {
        return new TipoResultadoValidacionDocumentoInside();
    }

    /**
     * Create an instance of {@link TipoResultadoValidacionDetalleDocumentoInside }
     * 
     */
    public TipoResultadoValidacionDetalleDocumentoInside createTipoResultadoValidacionDetalleDocumentoInside() {
        return new TipoResultadoValidacionDetalleDocumentoInside();
    }

    /**
     * Create an instance of {@link DocumentoEniFileInsideConMAdicionales }
     * 
     */
    public DocumentoEniFileInsideConMAdicionales createDocumentoEniFileInsideConMAdicionales() {
        return new DocumentoEniFileInsideConMAdicionales();
    }

    /**
     * Create an instance of {@link DocumentoEniFileInside }
     * 
     */
    public DocumentoEniFileInside createDocumentoEniFileInside() {
        return new DocumentoEniFileInside();
    }

    /**
     * Create an instance of {@link WSCredentialInside }
     * 
     */
    public WSCredentialInside createWSCredentialInside() {
        return new WSCredentialInside();
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
     * Create an instance of {@link ConvertirExpedienteAEniConMAdicionales }
     * 
     */
    public ConvertirExpedienteAEniConMAdicionales createConvertirExpedienteAEniConMAdicionales() {
        return new ConvertirExpedienteAEniConMAdicionales();
    }

    /**
     * Create an instance of {@link ConvertirDocumentoAEniResponse }
     * 
     */
    public ConvertirDocumentoAEniResponse createConvertirDocumentoAEniResponse() {
        return new ConvertirDocumentoAEniResponse();
    }

    /**
     * Create an instance of {@link ConvertirExpedienteAEniConMAdicionalesResponse }
     * 
     */
    public ConvertirExpedienteAEniConMAdicionalesResponse createConvertirExpedienteAEniConMAdicionalesResponse() {
        return new ConvertirExpedienteAEniConMAdicionalesResponse();
    }

    /**
     * Create an instance of {@link ConvertirExpedienteAEniAutocontenido }
     * 
     */
    public ConvertirExpedienteAEniAutocontenido createConvertirExpedienteAEniAutocontenido() {
        return new ConvertirExpedienteAEniAutocontenido();
    }

    /**
     * Create an instance of {@link ValidarDocumentoEniFileResponse }
     * 
     */
    public ValidarDocumentoEniFileResponse createValidarDocumentoEniFileResponse() {
        return new ValidarDocumentoEniFileResponse();
    }

    /**
     * Create an instance of {@link ConvertirExpedienteAEniConMAdicionalesAutocontenido }
     * 
     */
    public ConvertirExpedienteAEniConMAdicionalesAutocontenido createConvertirExpedienteAEniConMAdicionalesAutocontenido() {
        return new ConvertirExpedienteAEniConMAdicionalesAutocontenido();
    }

    /**
     * Create an instance of {@link ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse }
     * 
     */
    public ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse createConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse() {
        return new ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse();
    }

    /**
     * Create an instance of {@link ConvertirExpedienteAEniResponse }
     * 
     */
    public ConvertirExpedienteAEniResponse createConvertirExpedienteAEniResponse() {
        return new ConvertirExpedienteAEniResponse();
    }

    /**
     * Create an instance of {@link ValidarExpedienteEniFile }
     * 
     */
    public ValidarExpedienteEniFile createValidarExpedienteEniFile() {
        return new ValidarExpedienteEniFile();
    }

    /**
     * Create an instance of {@link ConvertirExpedienteAEniAutocontenidoResponse }
     * 
     */
    public ConvertirExpedienteAEniAutocontenidoResponse createConvertirExpedienteAEniAutocontenidoResponse() {
        return new ConvertirExpedienteAEniAutocontenidoResponse();
    }

    /**
     * Create an instance of {@link VisualizarDocumentoEniResponse }
     * 
     */
    public VisualizarDocumentoEniResponse createVisualizarDocumentoEniResponse() {
        return new VisualizarDocumentoEniResponse();
    }

    /**
     * Create an instance of {@link ConvertirDocumentoAEniConMAdicionales }
     * 
     */
    public ConvertirDocumentoAEniConMAdicionales createConvertirDocumentoAEniConMAdicionales() {
        return new ConvertirDocumentoAEniConMAdicionales();
    }

    /**
     * Create an instance of {@link ConvertirDocumentoAEniConMAdicionalesResponse }
     * 
     */
    public ConvertirDocumentoAEniConMAdicionalesResponse createConvertirDocumentoAEniConMAdicionalesResponse() {
        return new ConvertirDocumentoAEniConMAdicionalesResponse();
    }

    /**
     * Create an instance of {@link ValidarDocumentoEniFile }
     * 
     */
    public ValidarDocumentoEniFile createValidarDocumentoEniFile() {
        return new ValidarDocumentoEniFile();
    }

    /**
     * Create an instance of {@link ValidarExpedienteEniFileResponse }
     * 
     */
    public ValidarExpedienteEniFileResponse createValidarExpedienteEniFileResponse() {
        return new ValidarExpedienteEniFileResponse();
    }

    /**
     * Create an instance of {@link ConvertirDocumentoAEni }
     * 
     */
    public ConvertirDocumentoAEni createConvertirDocumentoAEni() {
        return new ConvertirDocumentoAEni();
    }

    /**
     * Create an instance of {@link ConvertirExpedienteAEni }
     * 
     */
    public ConvertirExpedienteAEni createConvertirExpedienteAEni() {
        return new ConvertirExpedienteAEni();
    }

    /**
     * Create an instance of {@link VisualizarDocumentoEni }
     * 
     */
    public VisualizarDocumentoEni createVisualizarDocumentoEni() {
        return new VisualizarDocumentoEni();
    }

    /**
     * Create an instance of {@link ExpedienteEniFileInsideConMAdicionales }
     * 
     */
    public ExpedienteEniFileInsideConMAdicionales createExpedienteEniFileInsideConMAdicionales() {
        return new ExpedienteEniFileInsideConMAdicionales();
    }

    /**
     * Create an instance of {@link ExpedienteEniFileInside }
     * 
     */
    public ExpedienteEniFileInside createExpedienteEniFileInside() {
        return new ExpedienteEniFileInside();
    }

    /**
     * Create an instance of {@link TipoDocumento }
     * 
     */
    public TipoDocumento createTipoDocumento() {
        return new TipoDocumento();
    }

    /**
     * Create an instance of {@link TipoExpediente }
     * 
     */
    public TipoExpediente createTipoExpediente() {
        return new TipoExpediente();
    }

    /**
     * Create an instance of {@link WSErrorInside }
     * 
     */
    public WSErrorInside createWSErrorInside() {
        return new WSErrorInside();
    }

    /**
     * Create an instance of {@link TipoContenido }
     * 
     */
    public TipoContenido createTipoContenido() {
        return new TipoContenido();
    }

    /**
     * Create an instance of {@link TipoExpedienteEniFileInside }
     * 
     */
    public TipoExpedienteEniFileInside createTipoExpedienteEniFileInside() {
        return new TipoExpedienteEniFileInside();
    }

    /**
     * Create an instance of {@link TipoExpedienteValidacionInside }
     * 
     */
    public TipoExpedienteValidacionInside createTipoExpedienteValidacionInside() {
        return new TipoExpedienteValidacionInside();
    }

    /**
     * Create an instance of {@link TipoOpcionesValidacionExpedienteInside }
     * 
     */
    public TipoOpcionesValidacionExpedienteInside createTipoOpcionesValidacionExpedienteInside() {
        return new TipoOpcionesValidacionExpedienteInside();
    }

    /**
     * Create an instance of {@link TipoDocumentoIndizadoConversionWS }
     * 
     */
    public TipoDocumentoIndizadoConversionWS createTipoDocumentoIndizadoConversionWS() {
        return new TipoDocumentoIndizadoConversionWS();
    }

    /**
     * Create an instance of {@link TipoCarpetaIndizadaConversionWSMtom }
     * 
     */
    public TipoCarpetaIndizadaConversionWSMtom createTipoCarpetaIndizadaConversionWSMtom() {
        return new TipoCarpetaIndizadaConversionWSMtom();
    }

    /**
     * Create an instance of {@link TipoDocumentoIndizadoConversion }
     * 
     */
    public TipoDocumentoIndizadoConversion createTipoDocumentoIndizadoConversion() {
        return new TipoDocumentoIndizadoConversion();
    }

    /**
     * Create an instance of {@link TipoCarpetaIndizadaConversionWS }
     * 
     */
    public TipoCarpetaIndizadaConversionWS createTipoCarpetaIndizadaConversionWS() {
        return new TipoCarpetaIndizadaConversionWS();
    }

    /**
     * Create an instance of {@link TipoCarpetaIndizadaConversion }
     * 
     */
    public TipoCarpetaIndizadaConversion createTipoCarpetaIndizadaConversion() {
        return new TipoCarpetaIndizadaConversion();
    }

    /**
     * Create an instance of {@link TipoDocumentoIndizadoConversionWSMtom }
     * 
     */
    public TipoDocumentoIndizadoConversionWSMtom createTipoDocumentoIndizadoConversionWSMtom() {
        return new TipoDocumentoIndizadoConversionWSMtom();
    }

    /**
     * Create an instance of {@link TipoIndiceConversion }
     * 
     */
    public TipoIndiceConversion createTipoIndiceConversion() {
        return new TipoIndiceConversion();
    }

    /**
     * Create an instance of {@link TipoIndiceConversionWS }
     * 
     */
    public TipoIndiceConversionWS createTipoIndiceConversionWS() {
        return new TipoIndiceConversionWS();
    }

    /**
     * Create an instance of {@link TipoIndiceConversionWSMtom }
     * 
     */
    public TipoIndiceConversionWSMtom createTipoIndiceConversionWSMtom() {
        return new TipoIndiceConversionWSMtom();
    }

    /**
     * Create an instance of {@link TipoOpcionesVisualizacionIndiceWS.FilasNombreOrganismo }
     * 
     */
    public TipoOpcionesVisualizacionIndiceWS.FilasNombreOrganismo createTipoOpcionesVisualizacionIndiceWSFilasNombreOrganismo() {
        return new TipoOpcionesVisualizacionIndiceWS.FilasNombreOrganismo();
    }

    /**
     * Create an instance of {@link TipoOpcionesVisualizacionIndice.FilasNombreOrganismo }
     * 
     */
    public TipoOpcionesVisualizacionIndice.FilasNombreOrganismo createTipoOpcionesVisualizacionIndiceFilasNombreOrganismo() {
        return new TipoOpcionesVisualizacionIndice.FilasNombreOrganismo();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInsideWSMtom.MetadatosEni.Estado }
     * 
     */
    public TipoExpedienteConversionInsideWSMtom.MetadatosEni.Estado createTipoExpedienteConversionInsideWSMtomMetadatosEniEstado() {
        return new TipoExpedienteConversionInsideWSMtom.MetadatosEni.Estado();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInsideWS.MetadatosEni.Estado }
     * 
     */
    public TipoExpedienteConversionInsideWS.MetadatosEni.Estado createTipoExpedienteConversionInsideWSMetadatosEniEstado() {
        return new TipoExpedienteConversionInsideWS.MetadatosEni.Estado();
    }

    /**
     * Create an instance of {@link TipoExpedienteConversionInside.MetadatosEni.Estado }
     * 
     */
    public TipoExpedienteConversionInside.MetadatosEni.Estado createTipoExpedienteConversionInsideMetadatosEniEstado() {
        return new TipoExpedienteConversionInside.MetadatosEni.Estado();
    }

    /**
     * Create an instance of {@link TipoOpcionesVisualizacionDocumento.FilasNombreOrganismo }
     * 
     */
    public TipoOpcionesVisualizacionDocumento.FilasNombreOrganismo createTipoOpcionesVisualizacionDocumentoFilasNombreOrganismo() {
        return new TipoOpcionesVisualizacionDocumento.FilasNombreOrganismo();
    }

    /**
     * Create an instance of {@link TipoDocumentoConversionInside.MetadatosEni }
     * 
     */
    public TipoDocumentoConversionInside.MetadatosEni createTipoDocumentoConversionInsideMetadatosEni() {
        return new TipoDocumentoConversionInside.MetadatosEni();
    }

    /**
     * Create an instance of {@link TipoDocumentoConversionInside.Csv }
     * 
     */
    public TipoDocumentoConversionInside.Csv createTipoDocumentoConversionInsideCsv() {
        return new TipoDocumentoConversionInside.Csv();
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
     * Create an instance of {@link TipoMetadatos.Estado }
     * 
     */
    public TipoMetadatos.Estado createTipoMetadatosEstado() {
        return new TipoMetadatos.Estado();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarExpedienteEniFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "validarExpedienteEniFileResponse")
    public JAXBElement<ValidarExpedienteEniFileResponse> createValidarExpedienteEniFileResponse(ValidarExpedienteEniFileResponse value) {
        return new JAXBElement<ValidarExpedienteEniFileResponse>(_ValidarExpedienteEniFileResponse_QNAME, ValidarExpedienteEniFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarDocumentoEni }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "visualizarDocumentoEni")
    public JAXBElement<VisualizarDocumentoEni> createVisualizarDocumentoEni(VisualizarDocumentoEni value) {
        return new JAXBElement<VisualizarDocumentoEni>(_VisualizarDocumentoEni_QNAME, VisualizarDocumentoEni.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link WSErrorInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebService/types/error", name = "errorInside")
    public JAXBElement<WSErrorInside> createErrorInside(WSErrorInside value) {
        return new JAXBElement<WSErrorInside>(_ErrorInside_QNAME, WSErrorInside.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirExpedienteAEniConMAdicionalesAutocontenido }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirExpedienteAEniConMAdicionalesAutocontenido")
    public JAXBElement<ConvertirExpedienteAEniConMAdicionalesAutocontenido> createConvertirExpedienteAEniConMAdicionalesAutocontenido(ConvertirExpedienteAEniConMAdicionalesAutocontenido value) {
        return new JAXBElement<ConvertirExpedienteAEniConMAdicionalesAutocontenido>(_ConvertirExpedienteAEniConMAdicionalesAutocontenido_QNAME, ConvertirExpedienteAEniConMAdicionalesAutocontenido.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirExpedienteAEniConMAdicionalesAutocontenidoResponse")
    public JAXBElement<ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse> createConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse(ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse value) {
        return new JAXBElement<ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse>(_ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse_QNAME, ConvertirExpedienteAEniConMAdicionalesAutocontenidoResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoMetadatos2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos", name = "metadatos")
    public JAXBElement<TipoMetadatos2> createMetadatos(TipoMetadatos2 value) {
        return new JAXBElement<TipoMetadatos2>(_Metadatos_QNAME, TipoMetadatos2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirExpedienteAEniResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirExpedienteAEniResponse")
    public JAXBElement<ConvertirExpedienteAEniResponse> createConvertirExpedienteAEniResponse(ConvertirExpedienteAEniResponse value) {
        return new JAXBElement<ConvertirExpedienteAEniResponse>(_ConvertirExpedienteAEniResponse_QNAME, ConvertirExpedienteAEniResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoExpediente }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e", name = "expediente")
    public JAXBElement<TipoExpediente> createExpediente(TipoExpediente value) {
        return new JAXBElement<TipoExpediente>(_Expediente_QNAME, TipoExpediente.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirExpedienteAEniAutocontenidoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirExpedienteAEniAutocontenidoResponse")
    public JAXBElement<ConvertirExpedienteAEniAutocontenidoResponse> createConvertirExpedienteAEniAutocontenidoResponse(ConvertirExpedienteAEniAutocontenidoResponse value) {
        return new JAXBElement<ConvertirExpedienteAEniAutocontenidoResponse>(_ConvertirExpedienteAEniAutocontenidoResponse_QNAME, ConvertirExpedienteAEniAutocontenidoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoExpedienteValidacionInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/expediente-e", name = "expedienteValidacion")
    public JAXBElement<TipoExpedienteValidacionInside> createExpedienteValidacion(TipoExpedienteValidacionInside value) {
        return new JAXBElement<TipoExpedienteValidacionInside>(_ExpedienteValidacion_QNAME, TipoExpedienteValidacionInside.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirExpedienteAEniConMAdicionales }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirExpedienteAEniConMAdicionales")
    public JAXBElement<ConvertirExpedienteAEniConMAdicionales> createConvertirExpedienteAEniConMAdicionales(ConvertirExpedienteAEniConMAdicionales value) {
        return new JAXBElement<ConvertirExpedienteAEniConMAdicionales>(_ConvertirExpedienteAEniConMAdicionales_QNAME, ConvertirExpedienteAEniConMAdicionales.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirDocumentoAEni }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirDocumentoAEni")
    public JAXBElement<ConvertirDocumentoAEni> createConvertirDocumentoAEni(ConvertirDocumentoAEni value) {
        return new JAXBElement<ConvertirDocumentoAEni>(_ConvertirDocumentoAEni_QNAME, ConvertirDocumentoAEni.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VisualizarDocumentoEniResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "visualizarDocumentoEniResponse")
    public JAXBElement<VisualizarDocumentoEniResponse> createVisualizarDocumentoEniResponse(VisualizarDocumentoEniResponse value) {
        return new JAXBElement<VisualizarDocumentoEniResponse>(_VisualizarDocumentoEniResponse_QNAME, VisualizarDocumentoEniResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoResultadoValidacionDocumentoInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/documento-e/resultados", name = "TipoResultadoValidacionDocumentoInside")
    public JAXBElement<TipoResultadoValidacionDocumentoInside> createTipoResultadoValidacionDocumentoInside(TipoResultadoValidacionDocumentoInside value) {
        return new JAXBElement<TipoResultadoValidacionDocumentoInside>(_TipoResultadoValidacionDocumentoInside_QNAME, TipoResultadoValidacionDocumentoInside.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoExpedienteConversionInsideWSMtom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion", name = "expedienteConversionwsMtom")
    public JAXBElement<TipoExpedienteConversionInsideWSMtom> createExpedienteConversionwsMtom(TipoExpedienteConversionInsideWSMtom value) {
        return new JAXBElement<TipoExpedienteConversionInsideWSMtom>(_ExpedienteConversionwsMtom_QNAME, TipoExpedienteConversionInsideWSMtom.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirDocumentoAEniConMAdicionalesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirDocumentoAEniConMAdicionalesResponse")
    public JAXBElement<ConvertirDocumentoAEniConMAdicionalesResponse> createConvertirDocumentoAEniConMAdicionalesResponse(ConvertirDocumentoAEniConMAdicionalesResponse value) {
        return new JAXBElement<ConvertirDocumentoAEniConMAdicionalesResponse>(_ConvertirDocumentoAEniConMAdicionalesResponse_QNAME, ConvertirDocumentoAEniConMAdicionalesResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoResultadoValidacionExpedienteInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/expediente-e/resultados", name = "TipoResultadoValidacionExpedienteInside")
    public JAXBElement<TipoResultadoValidacionExpedienteInside> createTipoResultadoValidacionExpedienteInside(TipoResultadoValidacionExpedienteInside value) {
        return new JAXBElement<TipoResultadoValidacionExpedienteInside>(_TipoResultadoValidacionExpedienteInside_QNAME, TipoResultadoValidacionExpedienteInside.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoExpedienteConversionInsideWS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion", name = "expedienteConversionws")
    public JAXBElement<TipoExpedienteConversionInsideWS> createExpedienteConversionws(TipoExpedienteConversionInsideWS value) {
        return new JAXBElement<TipoExpedienteConversionInsideWS>(_ExpedienteConversionws_QNAME, TipoExpedienteConversionInsideWS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirExpedienteAEniConMAdicionalesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirExpedienteAEniConMAdicionalesResponse")
    public JAXBElement<ConvertirExpedienteAEniConMAdicionalesResponse> createConvertirExpedienteAEniConMAdicionalesResponse(ConvertirExpedienteAEniConMAdicionalesResponse value) {
        return new JAXBElement<ConvertirExpedienteAEniConMAdicionalesResponse>(_ConvertirExpedienteAEniConMAdicionalesResponse_QNAME, ConvertirExpedienteAEniConMAdicionalesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirDocumentoAEniResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirDocumentoAEniResponse")
    public JAXBElement<ConvertirDocumentoAEniResponse> createConvertirDocumentoAEniResponse(ConvertirDocumentoAEniResponse value) {
        return new JAXBElement<ConvertirDocumentoAEniResponse>(_ConvertirDocumentoAEniResponse_QNAME, ConvertirDocumentoAEniResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoIndiceContenido }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/indice-e/contenido", name = "IndiceContenido")
    public JAXBElement<TipoIndiceContenido> createIndiceContenido(TipoIndiceContenido value) {
        return new JAXBElement<TipoIndiceContenido>(_IndiceContenido_QNAME, TipoIndiceContenido.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoDocumentoEniFileInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/documento-e/documentoEniFile", name = "documentoEniFile")
    public JAXBElement<TipoDocumentoEniFileInside> createDocumentoEniFile(TipoDocumentoEniFileInside value) {
        return new JAXBElement<TipoDocumentoEniFileInside>(_DocumentoEniFile_QNAME, TipoDocumentoEniFileInside.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoDocumentoConversionInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/documento-e/conversion", name = "documentoConversion")
    public JAXBElement<TipoDocumentoConversionInside> createDocumentoConversion(TipoDocumentoConversionInside value) {
        return new JAXBElement<TipoDocumentoConversionInside>(_DocumentoConversion_QNAME, TipoDocumentoConversionInside.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link SignedInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignedInfo")
    public JAXBElement<SignedInfoType> createSignedInfo(SignedInfoType value) {
        return new JAXBElement<SignedInfoType>(_SignedInfo_QNAME, SignedInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarDocumentoEniFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "validarDocumentoEniFile")
    public JAXBElement<ValidarDocumentoEniFile> createValidarDocumentoEniFile(ValidarDocumentoEniFile value) {
        return new JAXBElement<ValidarDocumentoEniFile>(_ValidarDocumentoEniFile_QNAME, ValidarDocumentoEniFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoDocumentoValidacionInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/documento-e", name = "documentoValidacion")
    public JAXBElement<TipoDocumentoValidacionInside> createDocumentoValidacion(TipoDocumentoValidacionInside value) {
        return new JAXBElement<TipoDocumentoValidacionInside>(_DocumentoValidacion_QNAME, TipoDocumentoValidacionInside.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoMetadatos }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/metadatos", name = "metadatosExp")
    public JAXBElement<TipoMetadatos> createMetadatosExp(TipoMetadatos value) {
        return new JAXBElement<TipoMetadatos>(_MetadatosExp_QNAME, TipoMetadatos.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoExpedienteConversionInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion", name = "expedienteConversion")
    public JAXBElement<TipoExpedienteConversionInside> createExpedienteConversion(TipoExpedienteConversionInside value) {
        return new JAXBElement<TipoExpedienteConversionInside>(_ExpedienteConversion_QNAME, TipoExpedienteConversionInside.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WSCredentialInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebService/types/credential", name = "credential")
    public JAXBElement<WSCredentialInside> createCredential(WSCredentialInside value) {
        return new JAXBElement<WSCredentialInside>(_Credential_QNAME, WSCredentialInside.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarExpedienteEniFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "validarExpedienteEniFile")
    public JAXBElement<ValidarExpedienteEniFile> createValidarExpedienteEniFile(ValidarExpedienteEniFile value) {
        return new JAXBElement<ValidarExpedienteEniFile>(_ValidarExpedienteEniFile_QNAME, ValidarExpedienteEniFile.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoDocumentoVisualizacionInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/visualizacion/documento-e", name = "documentoVisualizacion")
    public JAXBElement<TipoDocumentoVisualizacionInside> createDocumentoVisualizacion(TipoDocumentoVisualizacionInside value) {
        return new JAXBElement<TipoDocumentoVisualizacionInside>(_DocumentoVisualizacion_QNAME, TipoDocumentoVisualizacionInside.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirExpedienteAEniAutocontenido }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirExpedienteAEniAutocontenido")
    public JAXBElement<ConvertirExpedienteAEniAutocontenido> createConvertirExpedienteAEniAutocontenido(ConvertirExpedienteAEniAutocontenido value) {
        return new JAXBElement<ConvertirExpedienteAEniAutocontenido>(_ConvertirExpedienteAEniAutocontenido_QNAME, ConvertirExpedienteAEniAutocontenido.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarDocumentoEniFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "validarDocumentoEniFileResponse")
    public JAXBElement<ValidarDocumentoEniFileResponse> createValidarDocumentoEniFileResponse(ValidarDocumentoEniFileResponse value) {
        return new JAXBElement<ValidarDocumentoEniFileResponse>(_ValidarDocumentoEniFileResponse_QNAME, ValidarDocumentoEniFileResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirExpedienteAEni }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirExpedienteAEni")
    public JAXBElement<ConvertirExpedienteAEni> createConvertirExpedienteAEni(ConvertirExpedienteAEni value) {
        return new JAXBElement<ConvertirExpedienteAEni>(_ConvertirExpedienteAEni_QNAME, ConvertirExpedienteAEni.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoExpedienteEniFileInside }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/expedienteEniFile", name = "expedienteEniFile")
    public JAXBElement<TipoExpedienteEniFileInside> createExpedienteEniFile(TipoExpedienteEniFileInside value) {
        return new JAXBElement<TipoExpedienteEniFileInside>(_ExpedienteEniFile_QNAME, TipoExpedienteEniFileInside.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoIndice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/indice-e", name = "indice")
    public JAXBElement<TipoIndice> createIndice(TipoIndice value) {
        return new JAXBElement<TipoIndice>(_Indice_QNAME, TipoIndice.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ConvertirDocumentoAEniConMAdicionales }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", name = "convertirDocumentoAEniConMAdicionales")
    public JAXBElement<ConvertirDocumentoAEniConMAdicionales> createConvertirDocumentoAEniConMAdicionales(ConvertirDocumentoAEniConMAdicionales value) {
        return new JAXBElement<ConvertirDocumentoAEniConMAdicionales>(_ConvertirDocumentoAEniConMAdicionales_QNAME, ConvertirDocumentoAEniConMAdicionales.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "HMACOutputLength", scope = SignatureMethodType.class)
    public JAXBElement<BigInteger> createSignatureMethodTypeHMACOutputLength(BigInteger value) {
        return new JAXBElement<BigInteger>(_SignatureMethodTypeHMACOutputLength_QNAME, BigInteger.class, SignatureMethodType.class, value);
    }

}

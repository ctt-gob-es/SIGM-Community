
package es.msssi.tecdoc.fwktd.dir.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.msssi.tecdoc.fwktd.dir.ws package. 
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

    private final static QName _ConsultarUnidadesOrganicasResponse_QNAME = new QName("http://ws.dir.fwktd.tecdoc.msssi.es/", "consultarUnidadesOrganicasResponse");
    private final static QName _Dir3WSException_QNAME = new QName("http://ws.dir.fwktd.tecdoc.msssi.es/", "Dir3WSException");
    private final static QName _ConsultarUnidadesOrganicas_QNAME = new QName("http://ws.dir.fwktd.tecdoc.msssi.es/", "consultarUnidadesOrganicas");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.msssi.tecdoc.fwktd.dir.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CriteriosUO }
     * 
     */
    public CriteriosUO createCriteriosUO() {
        return new CriteriosUO();
    }

    /**
     * Create an instance of {@link DatosUnidadOrganica }
     * 
     */
    public DatosUnidadOrganica createDatosUnidadOrganica() {
        return new DatosUnidadOrganica();
    }

    /**
     * Create an instance of {@link PageInfoUO }
     * 
     */
    public PageInfoUO createPageInfoUO() {
        return new PageInfoUO();
    }

    /**
     * Create an instance of {@link CommonResponse }
     * 
     */
    public CommonResponse createCommonResponse() {
        return new CommonResponse();
    }

    /**
     * Create an instance of {@link CriterioUO }
     * 
     */
    public CriterioUO createCriterioUO() {
        return new CriterioUO();
    }

    /**
     * Create an instance of {@link ConsultarUnidadesOrganicasResponse }
     * 
     */
    public ConsultarUnidadesOrganicasResponse createConsultarUnidadesOrganicasResponse() {
        return new ConsultarUnidadesOrganicasResponse();
    }

    /**
     * Create an instance of {@link Dir3WSException }
     * 
     */
    public Dir3WSException createDir3WSException() {
        return new Dir3WSException();
    }

    /**
     * Create an instance of {@link CommonRequest }
     * 
     */
    public CommonRequest createCommonRequest() {
        return new CommonRequest();
    }

    /**
     * Create an instance of {@link ConsultarUnidadesOrganicas }
     * 
     */
    public ConsultarUnidadesOrganicas createConsultarUnidadesOrganicas() {
        return new ConsultarUnidadesOrganicas();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarUnidadesOrganicasResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir.fwktd.tecdoc.msssi.es/", name = "consultarUnidadesOrganicasResponse")
    public JAXBElement<ConsultarUnidadesOrganicasResponse> createConsultarUnidadesOrganicasResponse(ConsultarUnidadesOrganicasResponse value) {
        return new JAXBElement<ConsultarUnidadesOrganicasResponse>(_ConsultarUnidadesOrganicasResponse_QNAME, ConsultarUnidadesOrganicasResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Dir3WSException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir.fwktd.tecdoc.msssi.es/", name = "Dir3WSException")
    public JAXBElement<Dir3WSException> createDir3WSException(Dir3WSException value) {
        return new JAXBElement<Dir3WSException>(_Dir3WSException_QNAME, Dir3WSException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarUnidadesOrganicas }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir.fwktd.tecdoc.msssi.es/", name = "consultarUnidadesOrganicas")
    public JAXBElement<ConsultarUnidadesOrganicas> createConsultarUnidadesOrganicas(ConsultarUnidadesOrganicas value) {
        return new JAXBElement<ConsultarUnidadesOrganicas>(_ConsultarUnidadesOrganicas_QNAME, ConsultarUnidadesOrganicas.class, null, value);
    }

}

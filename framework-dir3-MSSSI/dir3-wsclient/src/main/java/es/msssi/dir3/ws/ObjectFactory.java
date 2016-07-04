/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the es.msssi.dir3.ws package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FindBasicDataOffices_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "findBasicDataOffices");
    private final static QName _GetOfficeResponse_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "getOfficeResponse");
    private final static QName _FindBasicDataUnitsResponse_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "findBasicDataUnitsResponse");
    private final static QName _CountOfficesResponse_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "countOfficesResponse");
    private final static QName _FindBasicDataOfficesResponse_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "findBasicDataOfficesResponse");
    private final static QName _FindOfficesResponse_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "findOfficesResponse");
    private final static QName _FindUnits_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "findUnits");
    private final static QName _Dir3WSException_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "Dir3WSException");
    private final static QName _CountUnitsResponse_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "countUnitsResponse");
    private final static QName _GetUnit_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "getUnit");
    private final static QName _CountUnits_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "countUnits");
    private final static QName _FindOffices_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "findOffices");
    private final static QName _CountOffices_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "countOffices");
    private final static QName _GetUnitResponse_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "getUnitResponse");
    private final static QName _FindUnitsResponse_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "findUnitsResponse");
    private final static QName _GetOffice_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "getOffice");
    private final static QName _FindBasicDataUnits_QNAME = new QName(
	"http://ws.dir3.msssi.es/", "findBasicDataUnits");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: es.msssi.dir3.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Dir3WSException }
     * 
     */
    public Dir3WSException createDir3WSException() {
	return new Dir3WSException();
    }

    /**
     * Create an instance of {@link FindOfficesResponse }
     * 
     */
    public FindOfficesResponse createFindOfficesResponse() {
	return new FindOfficesResponse();
    }

    /**
     * Create an instance of {@link FindUnits }
     * 
     */
    public FindUnits createFindUnits() {
	return new FindUnits();
    }

    /**
     * Create an instance of {@link FindUnitsResponse }
     * 
     */
    public FindUnitsResponse createFindUnitsResponse() {
	return new FindUnitsResponse();
    }

    /**
     * Create an instance of {@link Unit }
     * 
     */
    public Unit createUnit() {
	return new Unit();
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
	return new Address();
    }

    /**
     * Create an instance of {@link GetUnit }
     * 
     */
    public GetUnit createGetUnit() {
	return new GetUnit();
    }

    /**
     * Create an instance of {@link FindBasicDataOffices }
     * 
     */
    public FindBasicDataOffices createFindBasicDataOffices() {
	return new FindBasicDataOffices();
    }

    /**
     * Create an instance of {@link CommonResponse }
     * 
     */
    public CommonResponse createCommonResponse() {
	return new CommonResponse();
    }

    /**
     * Create an instance of {@link Entity }
     * 
     */
    public Entity createEntity() {
	return new Entity();
    }

    /**
     * Create an instance of {@link CriterionOF }
     * 
     */
    public CriterionOF createCriterionOF() {
	return new CriterionOF();
    }

    /**
     * Create an instance of {@link BasicDataUnit }
     * 
     */
    public BasicDataUnit createBasicDataUnit() {
	return new BasicDataUnit();
    }

    /**
     * Create an instance of {@link CountOfficesResponse }
     * 
     */
    public CountOfficesResponse createCountOfficesResponse() {
	return new CountOfficesResponse();
    }

    /**
     * Create an instance of {@link CountOffices }
     * 
     */
    public CountOffices createCountOffices() {
	return new CountOffices();
    }

    /**
     * Create an instance of {@link FindBasicDataOfficesResponse }
     * 
     */
    public FindBasicDataOfficesResponse createFindBasicDataOfficesResponse() {
	return new FindBasicDataOfficesResponse();
    }

    /**
     * Create an instance of {@link FindBasicDataUnits }
     * 
     */
    public FindBasicDataUnits createFindBasicDataUnits() {
	return new FindBasicDataUnits();
    }

    /**
     * Create an instance of {@link GetUnitResponse }
     * 
     */
    public GetUnitResponse createGetUnitResponse() {
	return new GetUnitResponse();
    }

    /**
     * Create an instance of {@link FindBasicDataUnitsResponse }
     * 
     */
    public FindBasicDataUnitsResponse createFindBasicDataUnitsResponse() {
	return new FindBasicDataUnitsResponse();
    }

    /**
     * Create an instance of {@link CriterionUO }
     * 
     */
    public CriterionUO createCriterionUO() {
	return new CriterionUO();
    }

    /**
     * Create an instance of {@link CountUnitsResponse }
     * 
     */
    public CountUnitsResponse createCountUnitsResponse() {
	return new CountUnitsResponse();
    }

    /**
     * Create an instance of {@link CommonRequest }
     * 
     */
    public CommonRequest createCommonRequest() {
	return new CommonRequest();
    }

    /**
     * Create an instance of {@link BasicDataOffice }
     * 
     */
    public BasicDataOffice createBasicDataOffice() {
	return new BasicDataOffice();
    }

    /**
     * Create an instance of {@link GetOfficeResponse }
     * 
     */
    public GetOfficeResponse createGetOfficeResponse() {
	return new GetOfficeResponse();
    }

    /**
     * Create an instance of {@link FindOffices }
     * 
     */
    public FindOffices createFindOffices() {
	return new FindOffices();
    }

    /**
     * Create an instance of {@link GetOffice }
     * 
     */
    public GetOffice createGetOffice() {
	return new GetOffice();
    }

    /**
     * Create an instance of {@link Contact }
     * 
     */
    public Contact createContact() {
	return new Contact();
    }

    /**
     * Create an instance of {@link Service }
     * 
     */
    public Service createService() {
	return new Service();
    }

    /**
     * Create an instance of {@link Office }
     * 
     */
    public Office createOffice() {
	return new Office();
    }

    /**
     * Create an instance of {@link CountUnits }
     * 
     */
    public CountUnits createCountUnits() {
	return new CountUnits();
    }

    /**
     * Create an instance of {@link PageInfo }
     * 
     */
    public PageInfo createPageInfo() {
	return new PageInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link FindBasicDataOffices }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "findBasicDataOffices")
    public JAXBElement<FindBasicDataOffices> createFindBasicDataOffices(
	FindBasicDataOffices value) {
	return new JAXBElement<FindBasicDataOffices>(
	    _FindBasicDataOffices_QNAME, FindBasicDataOffices.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link GetOfficeResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "getOfficeResponse")
    public JAXBElement<GetOfficeResponse> createGetOfficeResponse(
	GetOfficeResponse value) {
	return new JAXBElement<GetOfficeResponse>(
	    _GetOfficeResponse_QNAME, GetOfficeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link FindBasicDataUnitsResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "findBasicDataUnitsResponse")
    public JAXBElement<FindBasicDataUnitsResponse> createFindBasicDataUnitsResponse(
	FindBasicDataUnitsResponse value) {
	return new JAXBElement<FindBasicDataUnitsResponse>(
	    _FindBasicDataUnitsResponse_QNAME, FindBasicDataUnitsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link CountOfficesResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "countOfficesResponse")
    public JAXBElement<CountOfficesResponse> createCountOfficesResponse(
	CountOfficesResponse value) {
	return new JAXBElement<CountOfficesResponse>(
	    _CountOfficesResponse_QNAME, CountOfficesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link FindBasicDataOfficesResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "findBasicDataOfficesResponse")
    public JAXBElement<FindBasicDataOfficesResponse> createFindBasicDataOfficesResponse(
	FindBasicDataOfficesResponse value) {
	return new JAXBElement<FindBasicDataOfficesResponse>(
	    _FindBasicDataOfficesResponse_QNAME, FindBasicDataOfficesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link FindOfficesResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "findOfficesResponse")
    public JAXBElement<FindOfficesResponse> createFindOfficesResponse(
	FindOfficesResponse value) {
	return new JAXBElement<FindOfficesResponse>(
	    _FindOfficesResponse_QNAME, FindOfficesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindUnits }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "findUnits")
    public JAXBElement<FindUnits> createFindUnits(
	FindUnits value) {
	return new JAXBElement<FindUnits>(
	    _FindUnits_QNAME, FindUnits.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Dir3WSException }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "Dir3WSException")
    public JAXBElement<Dir3WSException> createDir3WSException(
	Dir3WSException value) {
	return new JAXBElement<Dir3WSException>(
	    _Dir3WSException_QNAME, Dir3WSException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link CountUnitsResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "countUnitsResponse")
    public JAXBElement<CountUnitsResponse> createCountUnitsResponse(
	CountUnitsResponse value) {
	return new JAXBElement<CountUnitsResponse>(
	    _CountUnitsResponse_QNAME, CountUnitsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUnit }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "getUnit")
    public JAXBElement<GetUnit> createGetUnit(
	GetUnit value) {
	return new JAXBElement<GetUnit>(
	    _GetUnit_QNAME, GetUnit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountUnits }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "countUnits")
    public JAXBElement<CountUnits> createCountUnits(
	CountUnits value) {
	return new JAXBElement<CountUnits>(
	    _CountUnits_QNAME, CountUnits.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindOffices }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "findOffices")
    public JAXBElement<FindOffices> createFindOffices(
	FindOffices value) {
	return new JAXBElement<FindOffices>(
	    _FindOffices_QNAME, FindOffices.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountOffices }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "countOffices")
    public JAXBElement<CountOffices> createCountOffices(
	CountOffices value) {
	return new JAXBElement<CountOffices>(
	    _CountOffices_QNAME, CountOffices.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUnitResponse }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "getUnitResponse")
    public JAXBElement<GetUnitResponse> createGetUnitResponse(
	GetUnitResponse value) {
	return new JAXBElement<GetUnitResponse>(
	    _GetUnitResponse_QNAME, GetUnitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link FindUnitsResponse }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "findUnitsResponse")
    public JAXBElement<FindUnitsResponse> createFindUnitsResponse(
	FindUnitsResponse value) {
	return new JAXBElement<FindUnitsResponse>(
	    _FindUnitsResponse_QNAME, FindUnitsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOffice }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "getOffice")
    public JAXBElement<GetOffice> createGetOffice(
	GetOffice value) {
	return new JAXBElement<GetOffice>(
	    _GetOffice_QNAME, GetOffice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link FindBasicDataUnits }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dir3.msssi.es/", name = "findBasicDataUnits")
    public JAXBElement<FindBasicDataUnits> createFindBasicDataUnits(
	FindBasicDataUnits value) {
	return new JAXBElement<FindBasicDataUnits>(
	    _FindBasicDataUnits_QNAME, FindBasicDataUnits.class, null, value);
    }

}

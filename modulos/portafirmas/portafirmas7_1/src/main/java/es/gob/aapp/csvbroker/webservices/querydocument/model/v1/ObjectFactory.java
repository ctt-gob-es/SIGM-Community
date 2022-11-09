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


package es.gob.aapp.csvbroker.webservices.querydocument.model.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.gob.aapp.csvbroker.webservices.querydocument.model.v1 package. 
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

    private final static QName _CSVQueryDocumentResponse_QNAME = new QName("urn:es:gob:aapp:csvbroker:webservices:querydocument:model:v1.0", "CSVQueryDocumentResponse");
    private final static QName _ErrorInfo_QNAME = new QName("urn:es:gob:aapp:csvbroker:webservices:querydocument:model:v1.0", "errorInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.gob.aapp.csvbroker.webservices.querydocument.model.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CSVQueryDocumentResponse }
     * 
     */
    public CSVQueryDocumentResponse createCSVQueryDocumentResponse() {
        return new CSVQueryDocumentResponse();
    }

    /**
     * Create an instance of {@link CsvQueryDocumentException }
     * 
     */
    public CsvQueryDocumentException createCsvQueryDocumentException() {
        return new CsvQueryDocumentException();
    }

    /**
     * Create an instance of {@link DocumentResponse }
     * 
     */
    public DocumentResponse createDocumentResponse() {
        return new DocumentResponse();
    }

    /**
     * Create an instance of {@link WaitResponse }
     * 
     */
    public WaitResponse createWaitResponse() {
        return new WaitResponse();
    }

    /**
     * Create an instance of {@link CSVQueryDocumentRequest }
     * 
     */
    public CSVQueryDocumentRequest createCSVQueryDocumentRequest() {
        return new CSVQueryDocumentRequest();
    }

    /**
     * Create an instance of {@link WSCredential }
     * 
     */
    public WSCredential createWSCredential() {
        return new WSCredential();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CSVQueryDocumentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvbroker:webservices:querydocument:model:v1.0", name = "CSVQueryDocumentResponse")
    public JAXBElement<CSVQueryDocumentResponse> createCSVQueryDocumentResponse(CSVQueryDocumentResponse value) {
        return new JAXBElement<CSVQueryDocumentResponse>(_CSVQueryDocumentResponse_QNAME, CSVQueryDocumentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CsvQueryDocumentException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:es:gob:aapp:csvbroker:webservices:querydocument:model:v1.0", name = "errorInfo")
    public JAXBElement<CsvQueryDocumentException> createErrorInfo(CsvQueryDocumentException value) {
        return new JAXBElement<CsvQueryDocumentException>(_ErrorInfo_QNAME, CsvQueryDocumentException.class, null, value);
    }

}

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


package es.mpt.dsic.inside.ws.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.mpt.dsic.inside.ws.service package. 
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

    private final static QName _FirmaFicheroWithPropertieResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "firmaFicheroWithPropertieResponse");
    private final static QName _FirmaFicheroResponse_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "firmaFicheroResponse");
    private final static QName _FirmaFicheroWithPropertie_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "firmaFicheroWithPropertie");
    private final static QName _FirmaFichero_QNAME = new QName("http://service.ws.inside.dsic.mpt.es/", "firmaFichero");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.mpt.dsic.inside.ws.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FirmaFicheroResponse }
     * 
     */
    public FirmaFicheroResponse createFirmaFicheroResponse() {
        return new FirmaFicheroResponse();
    }

    /**
     * Create an instance of {@link FirmaFicheroWithPropertie }
     * 
     */
    public FirmaFicheroWithPropertie createFirmaFicheroWithPropertie() {
        return new FirmaFicheroWithPropertie();
    }

    /**
     * Create an instance of {@link FirmaFichero }
     * 
     */
    public FirmaFichero createFirmaFichero() {
        return new FirmaFichero();
    }

    /**
     * Create an instance of {@link FirmaFicheroWithPropertieResponse }
     * 
     */
    public FirmaFicheroWithPropertieResponse createFirmaFicheroWithPropertieResponse() {
        return new FirmaFicheroWithPropertieResponse();
    }

    /**
     * Create an instance of {@link DatosFirmante }
     * 
     */
    public DatosFirmante createDatosFirmante() {
        return new DatosFirmante();
    }

    /**
     * Create an instance of {@link ApplicationLogin }
     * 
     */
    public ApplicationLogin createApplicationLogin() {
        return new ApplicationLogin();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link DatosSalida }
     * 
     */
    public DatosSalida createDatosSalida() {
        return new DatosSalida();
    }

    /**
     * Create an instance of {@link ResultadoFirmaFichero }
     * 
     */
    public ResultadoFirmaFichero createResultadoFirmaFichero() {
        return new ResultadoFirmaFichero();
    }

    /**
     * Create an instance of {@link ResultadoFirma }
     * 
     */
    public ResultadoFirma createResultadoFirma() {
        return new ResultadoFirma();
    }

    /**
     * Create an instance of {@link ResultadoFirmaFicheroMtom }
     * 
     */
    public ResultadoFirmaFicheroMtom createResultadoFirmaFicheroMtom() {
        return new ResultadoFirmaFicheroMtom();
    }

    /**
     * Create an instance of {@link DatosEntradaFichero }
     * 
     */
    public DatosEntradaFichero createDatosEntradaFichero() {
        return new DatosEntradaFichero();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmaFicheroWithPropertieResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "firmaFicheroWithPropertieResponse")
    public JAXBElement<FirmaFicheroWithPropertieResponse> createFirmaFicheroWithPropertieResponse(FirmaFicheroWithPropertieResponse value) {
        return new JAXBElement<FirmaFicheroWithPropertieResponse>(_FirmaFicheroWithPropertieResponse_QNAME, FirmaFicheroWithPropertieResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmaFicheroResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "firmaFicheroResponse")
    public JAXBElement<FirmaFicheroResponse> createFirmaFicheroResponse(FirmaFicheroResponse value) {
        return new JAXBElement<FirmaFicheroResponse>(_FirmaFicheroResponse_QNAME, FirmaFicheroResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmaFicheroWithPropertie }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "firmaFicheroWithPropertie")
    public JAXBElement<FirmaFicheroWithPropertie> createFirmaFicheroWithPropertie(FirmaFicheroWithPropertie value) {
        return new JAXBElement<FirmaFicheroWithPropertie>(_FirmaFicheroWithPropertie_QNAME, FirmaFicheroWithPropertie.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmaFichero }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "firmaFichero")
    public JAXBElement<FirmaFichero> createFirmaFichero(FirmaFichero value) {
        return new JAXBElement<FirmaFichero>(_FirmaFichero_QNAME, FirmaFichero.class, null, value);
    }

}

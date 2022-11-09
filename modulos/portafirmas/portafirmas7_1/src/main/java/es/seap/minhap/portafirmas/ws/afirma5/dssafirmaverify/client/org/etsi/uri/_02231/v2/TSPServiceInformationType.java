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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._02231.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TSPServiceInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TSPServiceInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceTypeIdentifier"/>
 *         &lt;element name="ServiceName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceDigitalIdentity"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceStatus"/>
 *         &lt;element name="StatusStartingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="SchemeServiceDefinitionURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceSupplyPoints" minOccurs="0"/>
 *         &lt;element name="TSPServiceDefinitionURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType" minOccurs="0"/>
 *         &lt;element name="ServiceInformationExtensions" type="{http://uri.etsi.org/02231/v2#}ExtensionsListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSPServiceInformationType", propOrder = {
    "serviceTypeIdentifier",
    "serviceName",
    "serviceDigitalIdentity",
    "serviceStatus",
    "statusStartingTime",
    "schemeServiceDefinitionURI",
    "serviceSupplyPoints",
    "tspServiceDefinitionURI",
    "serviceInformationExtensions"
})
public class TSPServiceInformationType {

    @XmlElement(name = "ServiceTypeIdentifier", required = true)
    protected String serviceTypeIdentifier;
    @XmlElement(name = "ServiceName", required = true)
    protected InternationalNamesType serviceName;
    @XmlElement(name = "ServiceDigitalIdentity", required = true)
    protected DigitalIdentityListType serviceDigitalIdentity;
    @XmlElement(name = "ServiceStatus", required = true)
    protected String serviceStatus;
    @XmlElement(name = "StatusStartingTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar statusStartingTime;
    @XmlElement(name = "SchemeServiceDefinitionURI")
    protected NonEmptyMultiLangURIListType schemeServiceDefinitionURI;
    @XmlElement(name = "ServiceSupplyPoints")
    protected ServiceSupplyPointsType serviceSupplyPoints;
    @XmlElement(name = "TSPServiceDefinitionURI")
    protected NonEmptyMultiLangURIListType tspServiceDefinitionURI;
    @XmlElement(name = "ServiceInformationExtensions")
    protected ExtensionsListType serviceInformationExtensions;

    /**
     * Gets the value of the serviceTypeIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceTypeIdentifier() {
        return serviceTypeIdentifier;
    }

    /**
     * Sets the value of the serviceTypeIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceTypeIdentifier(String value) {
        this.serviceTypeIdentifier = value;
    }

    /**
     * Gets the value of the serviceName property.
     * 
     * @return
     *     possible object is
     *     {@link InternationalNamesType }
     *     
     */
    public InternationalNamesType getServiceName() {
        return serviceName;
    }

    /**
     * Sets the value of the serviceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalNamesType }
     *     
     */
    public void setServiceName(InternationalNamesType value) {
        this.serviceName = value;
    }

    /**
     * Gets the value of the serviceDigitalIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link DigitalIdentityListType }
     *     
     */
    public DigitalIdentityListType getServiceDigitalIdentity() {
        return serviceDigitalIdentity;
    }

    /**
     * Sets the value of the serviceDigitalIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link DigitalIdentityListType }
     *     
     */
    public void setServiceDigitalIdentity(DigitalIdentityListType value) {
        this.serviceDigitalIdentity = value;
    }

    /**
     * Gets the value of the serviceStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceStatus() {
        return serviceStatus;
    }

    /**
     * Sets the value of the serviceStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceStatus(String value) {
        this.serviceStatus = value;
    }

    /**
     * Gets the value of the statusStartingTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStatusStartingTime() {
        return statusStartingTime;
    }

    /**
     * Sets the value of the statusStartingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStatusStartingTime(XMLGregorianCalendar value) {
        this.statusStartingTime = value;
    }

    /**
     * Gets the value of the schemeServiceDefinitionURI property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public NonEmptyMultiLangURIListType getSchemeServiceDefinitionURI() {
        return schemeServiceDefinitionURI;
    }

    /**
     * Sets the value of the schemeServiceDefinitionURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public void setSchemeServiceDefinitionURI(NonEmptyMultiLangURIListType value) {
        this.schemeServiceDefinitionURI = value;
    }

    /**
     * Gets the value of the serviceSupplyPoints property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceSupplyPointsType }
     *     
     */
    public ServiceSupplyPointsType getServiceSupplyPoints() {
        return serviceSupplyPoints;
    }

    /**
     * Sets the value of the serviceSupplyPoints property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceSupplyPointsType }
     *     
     */
    public void setServiceSupplyPoints(ServiceSupplyPointsType value) {
        this.serviceSupplyPoints = value;
    }

    /**
     * Gets the value of the tspServiceDefinitionURI property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public NonEmptyMultiLangURIListType getTSPServiceDefinitionURI() {
        return tspServiceDefinitionURI;
    }

    /**
     * Sets the value of the tspServiceDefinitionURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public void setTSPServiceDefinitionURI(NonEmptyMultiLangURIListType value) {
        this.tspServiceDefinitionURI = value;
    }

    /**
     * Gets the value of the serviceInformationExtensions property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsListType }
     *     
     */
    public ExtensionsListType getServiceInformationExtensions() {
        return serviceInformationExtensions;
    }

    /**
     * Sets the value of the serviceInformationExtensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsListType }
     *     
     */
    public void setServiceInformationExtensions(ExtensionsListType value) {
        this.serviceInformationExtensions = value;
    }

}

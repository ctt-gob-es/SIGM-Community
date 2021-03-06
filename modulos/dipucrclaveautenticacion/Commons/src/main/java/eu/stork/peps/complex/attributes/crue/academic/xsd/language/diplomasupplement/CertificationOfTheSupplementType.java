//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.12 at 12:22:50 PM GMT 
//
package eu.stork.peps.complex.attributes.crue.academic.xsd.language.diplomasupplement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Certification of the Supplement
 *
 * <p>
 * Java class for CertificationOfTheSupplementType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="CertificationOfTheSupplementType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CertificationDate" type="{urn:crue:academic:xsd:language:diplomasupplement}DateType"/>
 *         &lt;element name="OfficialsCertifying" type="{urn:crue:academic:xsd:language:diplomasupplement}OfficialsCertifyingType"/>
 *         &lt;element name="OfficialStamp" type="{urn:crue:academic:xsd:language:diplomasupplement}OfficialStampType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CertificationOfTheSupplementType", propOrder = {
    "certificationDate",
    "officialsCertifying",
    "officialStamp"
})
public class CertificationOfTheSupplementType {

    @XmlElement(name = "CertificationDate", required = true)
    protected XMLGregorianCalendar certificationDate;
    @XmlElement(name = "OfficialsCertifying", required = true)
    protected OfficialsCertifyingType officialsCertifying;
    @XmlElement(name = "OfficialStamp")
    protected OfficialStampType officialStamp;

    /**
     * Gets the value of the certificationDate property.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getCertificationDate() {
        return certificationDate;
    }

    /**
     * Sets the value of the certificationDate property.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     *
     */
    public void setCertificationDate(XMLGregorianCalendar value) {
        this.certificationDate = value;
    }

    /**
     * Gets the value of the officialsCertifying property.
     *
     * @return possible object is {@link OfficialsCertifyingType }
     *
     */
    public OfficialsCertifyingType getOfficialsCertifying() {
        return officialsCertifying;
    }

    /**
     * Sets the value of the officialsCertifying property.
     *
     * @param value allowed object is {@link OfficialsCertifyingType }
     *
     */
    public void setOfficialsCertifying(OfficialsCertifyingType value) {
        this.officialsCertifying = value;
    }

    /**
     * Gets the value of the officialStamp property.
     *
     * @return possible object is {@link OfficialStampType }
     *
     */
    public OfficialStampType getOfficialStamp() {
        return officialStamp;
    }

    /**
     * Sets the value of the officialStamp property.
     *
     * @param value allowed object is {@link OfficialStampType }
     *
     */
    public void setOfficialStamp(OfficialStampType value) {
        this.officialStamp = value;
    }

}

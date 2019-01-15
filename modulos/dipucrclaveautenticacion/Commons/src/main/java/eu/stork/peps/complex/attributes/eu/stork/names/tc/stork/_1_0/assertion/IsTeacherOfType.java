//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.12 at 12:22:50 PM GMT 
//
package eu.stork.peps.complex.attributes.eu.stork.names.tc.stork._1_0.assertion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for isTeacherOfType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="isTeacherOfType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nameOfInstitution" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="course" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="role" type="{urn:eu:stork:names:tc:STORK:1.0:assertion}teacherRoleType"/>
 *         &lt;element name="AQAA" type="{urn:eu:stork:names:tc:STORK:1.0:assertion}QualityAuthenticationAssuranceLevelType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isTeacherOfType", propOrder = {
    "nameOfInstitution",
    "course",
    "role",
    "aqaa"
})
public class IsTeacherOfType {

    @XmlElement(required = true)
    protected String nameOfInstitution;
    @XmlElement(required = true)
    protected String course;
    @XmlElement(required = true)
    protected String role;
    @XmlElement(name = "AQAA")
    protected int aqaa;

    /**
     * Gets the value of the nameOfInstitution property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getNameOfInstitution() {
        return nameOfInstitution;
    }

    /**
     * Sets the value of the nameOfInstitution property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setNameOfInstitution(String value) {
        this.nameOfInstitution = value;
    }

    /**
     * Gets the value of the course property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCourse() {
        return course;
    }

    /**
     * Sets the value of the course property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCourse(String value) {
        this.course = value;
    }

    /**
     * Gets the value of the role property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the aqaa property.
     *
     */
    public int getAQAA() {
        return aqaa;
    }

    /**
     * Sets the value of the aqaa property.
     *
     */
    public void setAQAA(int value) {
        this.aqaa = value;
    }

}
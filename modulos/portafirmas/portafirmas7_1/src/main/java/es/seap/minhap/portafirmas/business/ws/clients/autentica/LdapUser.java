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


package es.seap.minhap.portafirmas.business.ws.clients.autentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para LdapUser complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="LdapUser"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="appId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="appUrl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4Cn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4DirCenCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4DocumentId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4DocumentType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4Email" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4EmployeeType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4GivenName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4JobCentreCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4LastName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4Observations" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4OrganizationCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4Sn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4TelephoneNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4Title" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4UserCCAACode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4UserCompany" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4UserCountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4UserDateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4UserLocalityCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4UserLocalityEntity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4UserProvinceCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LdapUser", namespace = "http://obj.dir4.ws.sag.es", propOrder = {
    "appId",
    "appUrl",
    "dir4Cn",
    "dir4DirCenCode",
    "dir4DocumentId",
    "dir4DocumentType",
    "dir4Email",
    "dir4EmployeeType",
    "dir4GivenName",
    "dir4JobCentreCode",
    "dir4LastName",
    "dir4Observations",
    "dir4OrganizationCode",
    "dir4Sn",
    "dir4TelephoneNumber",
    "dir4Title",
    "dir4UserCCAACode",
    "dir4UserCompany",
    "dir4UserCountryCode",
    "dir4UserDateOfBirth",
    "dir4UserLocalityCode",
    "dir4UserLocalityEntity",
    "dir4UserProvinceCode"
})
public class LdapUser {

    @XmlElement(required = true, nillable = true)
    protected String appId;
    @XmlElement(required = true, nillable = true)
    protected String appUrl;
    @XmlElement(required = true, nillable = true)
    protected String dir4Cn;
    @XmlElement(required = true, nillable = true)
    protected String dir4DirCenCode;
    @XmlElement(required = true, nillable = true)
    protected String dir4DocumentId;
    @XmlElement(required = true, nillable = true)
    protected String dir4DocumentType;
    @XmlElement(required = true, nillable = true)
    protected String dir4Email;
    @XmlElement(required = true, nillable = true)
    protected String dir4EmployeeType;
    @XmlElement(required = true, nillable = true)
    protected String dir4GivenName;
    @XmlElement(required = true, nillable = true)
    protected String dir4JobCentreCode;
    @XmlElement(required = true, nillable = true)
    protected String dir4LastName;
    @XmlElement(required = true, nillable = true)
    protected String dir4Observations;
    @XmlElement(required = true, nillable = true)
    protected String dir4OrganizationCode;
    @XmlElement(required = true, nillable = true)
    protected String dir4Sn;
    @XmlElement(required = true, nillable = true)
    protected String dir4TelephoneNumber;
    @XmlElement(required = true, nillable = true)
    protected String dir4Title;
    @XmlElement(required = true, nillable = true)
    protected String dir4UserCCAACode;
    @XmlElement(required = true, nillable = true)
    protected String dir4UserCompany;
    @XmlElement(required = true, nillable = true)
    protected String dir4UserCountryCode;
    @XmlElement(required = true, nillable = true)
    protected String dir4UserDateOfBirth;
    @XmlElement(required = true, nillable = true)
    protected String dir4UserLocalityCode;
    @XmlElement(required = true, nillable = true)
    protected String dir4UserLocalityEntity;
    @XmlElement(required = true, nillable = true)
    protected String dir4UserProvinceCode;

    /**
     * Obtiene el valor de la propiedad appId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Define el valor de la propiedad appId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppId(String value) {
        this.appId = value;
    }

    /**
     * Obtiene el valor de la propiedad appUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppUrl() {
        return appUrl;
    }

    /**
     * Define el valor de la propiedad appUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppUrl(String value) {
        this.appUrl = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4Cn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4Cn() {
        return dir4Cn;
    }

    /**
     * Define el valor de la propiedad dir4Cn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4Cn(String value) {
        this.dir4Cn = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4DirCenCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4DirCenCode() {
        return dir4DirCenCode;
    }

    /**
     * Define el valor de la propiedad dir4DirCenCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4DirCenCode(String value) {
        this.dir4DirCenCode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4DocumentId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4DocumentId() {
        return dir4DocumentId;
    }

    /**
     * Define el valor de la propiedad dir4DocumentId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4DocumentId(String value) {
        this.dir4DocumentId = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4DocumentType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4DocumentType() {
        return dir4DocumentType;
    }

    /**
     * Define el valor de la propiedad dir4DocumentType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4DocumentType(String value) {
        this.dir4DocumentType = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4Email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4Email() {
        return dir4Email;
    }

    /**
     * Define el valor de la propiedad dir4Email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4Email(String value) {
        this.dir4Email = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4EmployeeType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4EmployeeType() {
        return dir4EmployeeType;
    }

    /**
     * Define el valor de la propiedad dir4EmployeeType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4EmployeeType(String value) {
        this.dir4EmployeeType = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4GivenName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4GivenName() {
        return dir4GivenName;
    }

    /**
     * Define el valor de la propiedad dir4GivenName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4GivenName(String value) {
        this.dir4GivenName = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4JobCentreCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4JobCentreCode() {
        return dir4JobCentreCode;
    }

    /**
     * Define el valor de la propiedad dir4JobCentreCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4JobCentreCode(String value) {
        this.dir4JobCentreCode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4LastName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4LastName() {
        return dir4LastName;
    }

    /**
     * Define el valor de la propiedad dir4LastName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4LastName(String value) {
        this.dir4LastName = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4Observations.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4Observations() {
        return dir4Observations;
    }

    /**
     * Define el valor de la propiedad dir4Observations.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4Observations(String value) {
        this.dir4Observations = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4OrganizationCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4OrganizationCode() {
        return dir4OrganizationCode;
    }

    /**
     * Define el valor de la propiedad dir4OrganizationCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4OrganizationCode(String value) {
        this.dir4OrganizationCode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4Sn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4Sn() {
        return dir4Sn;
    }

    /**
     * Define el valor de la propiedad dir4Sn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4Sn(String value) {
        this.dir4Sn = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4TelephoneNumber.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4TelephoneNumber() {
        return dir4TelephoneNumber;
    }

    /**
     * Define el valor de la propiedad dir4TelephoneNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4TelephoneNumber(String value) {
        this.dir4TelephoneNumber = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4Title.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4Title() {
        return dir4Title;
    }

    /**
     * Define el valor de la propiedad dir4Title.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4Title(String value) {
        this.dir4Title = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4UserCCAACode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4UserCCAACode() {
        return dir4UserCCAACode;
    }

    /**
     * Define el valor de la propiedad dir4UserCCAACode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4UserCCAACode(String value) {
        this.dir4UserCCAACode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4UserCompany.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4UserCompany() {
        return dir4UserCompany;
    }

    /**
     * Define el valor de la propiedad dir4UserCompany.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4UserCompany(String value) {
        this.dir4UserCompany = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4UserCountryCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4UserCountryCode() {
        return dir4UserCountryCode;
    }

    /**
     * Define el valor de la propiedad dir4UserCountryCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4UserCountryCode(String value) {
        this.dir4UserCountryCode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4UserDateOfBirth.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4UserDateOfBirth() {
        return dir4UserDateOfBirth;
    }

    /**
     * Define el valor de la propiedad dir4UserDateOfBirth.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4UserDateOfBirth(String value) {
        this.dir4UserDateOfBirth = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4UserLocalityCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4UserLocalityCode() {
        return dir4UserLocalityCode;
    }

    /**
     * Define el valor de la propiedad dir4UserLocalityCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4UserLocalityCode(String value) {
        this.dir4UserLocalityCode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4UserLocalityEntity.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4UserLocalityEntity() {
        return dir4UserLocalityEntity;
    }

    /**
     * Define el valor de la propiedad dir4UserLocalityEntity.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4UserLocalityEntity(String value) {
        this.dir4UserLocalityEntity = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4UserProvinceCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4UserProvinceCode() {
        return dir4UserProvinceCode;
    }

    /**
     * Define el valor de la propiedad dir4UserProvinceCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4UserProvinceCode(String value) {
        this.dir4UserProvinceCode = value;
    }

}

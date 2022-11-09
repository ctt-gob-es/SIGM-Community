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

package es.seap.minhap.portafirmas.business.ws.clients.autentica.respuestas.getuser;
//
//Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.5-2 
//Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
//Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
//Generado el: PM.11.22 a las 12:48:15 PM CET 
//

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
* <p>Clase Java para anonymous complex type.
* 
* <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
* 
* <pre>
* &lt;complexType>
*   &lt;complexContent>
*     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
*       &lt;sequence>
*         &lt;element name="resultado" type="{http://www.w3.org/2001/XMLSchema}string"/>
*         &lt;element name="usuario" maxOccurs="unbounded" minOccurs="0">
*           &lt;complexType>
*             &lt;complexContent>
*               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
*                 &lt;sequence>
*                   &lt;element name="isCitizen" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4DocumentID" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4DocumentType" type="{http://www.w3.org/2001/XMLSchema}byte"/>
*                   &lt;element name="cn" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="givenName" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="sn" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4LastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4UserDateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4UserLocalityCode" type="{http://www.w3.org/2001/XMLSchema}short"/>
*                   &lt;element name="dir4UserLocalityEntity" type="{http://www.w3.org/2001/XMLSchema}byte"/>
*                   &lt;element name="dir4UserLocality" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4UserProvinceCode" type="{http://www.w3.org/2001/XMLSchema}byte"/>
*                   &lt;element name="dir4UserProvince" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4UserCCAACode" type="{http://www.w3.org/2001/XMLSchema}byte"/>
*                   &lt;element name="dir4UserCCAA" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4AdministrationLevel" type="{http://www.w3.org/2001/XMLSchema}byte"/>
*                   &lt;element name="dir4OrganizationCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4OrganizationDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4DirCenCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4DirCenDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4JobCentreCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4JobCentreDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4OrganicalUnitCodeDir3" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="st" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="l" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="employeeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="uid" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4UserName" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4LdapBranch" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4OriginSource" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                   &lt;element name="dir4SystemRegisterDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
*                 &lt;/sequence>
*               &lt;/restriction>
*             &lt;/complexContent>
*           &lt;/complexType>
*         &lt;/element>
*       &lt;/sequence>
*     &lt;/restriction>
*   &lt;/complexContent>
* &lt;/complexType>
* </pre>
* 
* 
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
 "resultado",
 "usuario"
})
@XmlRootElement(name = "respuesta")
public class Respuesta {

 @XmlElement(required = true)
 protected String resultado;
 protected List<Respuesta.Usuario> usuario;

 /**
  * Obtiene el valor de la propiedad resultado.
  * 
  * @return
  *     possible object is
  *     {@link String }
  *     
  */
 public String getResultado() {
     return resultado;
 }

 /**
  * Define el valor de la propiedad resultado.
  * 
  * @param value
  *     allowed object is
  *     {@link String }
  *     
  */
 public void setResultado(String value) {
     this.resultado = value;
 }

 /**
  * Gets the value of the usuario property.
  * 
  * <p>
  * This accessor method returns a reference to the live list,
  * not a snapshot. Therefore any modification you make to the
  * returned list will be present inside the JAXB object.
  * This is why there is not a <CODE>set</CODE> method for the usuario property.
  * 
  * <p>
  * For example, to add a new item, do as follows:
  * <pre>
  *    getUsuario().add(newItem);
  * </pre>
  * 
  * 
  * <p>
  * Objects of the following type(s) are allowed in the list
  * {@link Respuesta.Usuario }
  * 
  * 
  */
 public List<Respuesta.Usuario> getUsuario() {
     if (usuario == null) {
         usuario = new ArrayList<Respuesta.Usuario>();
     }
     return this.usuario;
 }


 /**
  * <p>Clase Java para anonymous complex type.
  * 
  * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
  * 
  * <pre>
  * &lt;complexType>
  *   &lt;complexContent>
  *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
  *       &lt;sequence>
  *         &lt;element name="isCitizen" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4DocumentID" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4DocumentType" type="{http://www.w3.org/2001/XMLSchema}byte"/>
  *         &lt;element name="cn" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="givenName" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="sn" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4LastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4UserDateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4UserLocalityCode" type="{http://www.w3.org/2001/XMLSchema}short"/>
  *         &lt;element name="dir4UserLocalityEntity" type="{http://www.w3.org/2001/XMLSchema}byte"/>
  *         &lt;element name="dir4UserLocality" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4UserProvinceCode" type="{http://www.w3.org/2001/XMLSchema}byte"/>
  *         &lt;element name="dir4UserProvince" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4UserCCAACode" type="{http://www.w3.org/2001/XMLSchema}byte"/>
  *         &lt;element name="dir4UserCCAA" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4AdministrationLevel" type="{http://www.w3.org/2001/XMLSchema}byte"/>
  *         &lt;element name="dir4OrganizationCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4OrganizationDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4DirCenCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4DirCenDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4JobCentreCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4JobCentreDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4OrganicalUnitCodeDir3" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="st" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="l" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="employeeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="uid" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4UserName" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4LdapBranch" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4OriginSource" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *         &lt;element name="dir4SystemRegisterDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
  *       &lt;/sequence>
  *     &lt;/restriction>
  *   &lt;/complexContent>
  * &lt;/complexType>
  * </pre>
  * 
  * 
  */
 @XmlAccessorType(XmlAccessType.FIELD)
 @XmlType(name = "", propOrder = {
     "isCitizen",
     "dir4DocumentID",
     "dir4DocumentType",
     "cn",
     "givenName",
     "sn",
     "dir4LastName",
     "dir4UserDateOfBirth",
     "dir4UserLocalityCode",
     "dir4UserLocalityEntity",
     "dir4UserLocality",
     "dir4UserProvinceCode",
     "dir4UserProvince",
     "dir4UserCCAACode",
     "dir4UserCCAA",
     "dir4AdministrationLevel",
     "dir4OrganizationCode",
     "dir4OrganizationDesc",
     "dir4DirCenCode",
     "dir4DirCenDesc",
     "dir4JobCentreCode",
     "dir4JobCentreDesc",
     "dir4OrganicalUnitCodeDir3",
     "st",
     "l",
     "employeeType",
     "title",
     "uid",
     "dir4UserName",
     "dir4LdapBranch",
     "dir4OriginSource",
     "dir4SystemRegisterDate"
 })
 public static class Usuario {

     @XmlElement(required = true)
     protected String isCitizen;
     @XmlElement(required = true)
     protected String dir4DocumentID;
     protected byte dir4DocumentType;
     @XmlElement(required = true)
     protected String cn;
     @XmlElement(required = true)
     protected String givenName;
     @XmlElement(required = true)
     protected String sn;
     @XmlElement(required = true)
     protected String dir4LastName;
     @XmlElement(required = true)
     protected String dir4UserDateOfBirth;
     protected short dir4UserLocalityCode;
     protected byte dir4UserLocalityEntity;
     @XmlElement(required = true)
     protected String dir4UserLocality;
     protected byte dir4UserProvinceCode;
     @XmlElement(required = true)
     protected String dir4UserProvince;
     protected byte dir4UserCCAACode;
     @XmlElement(required = true)
     protected String dir4UserCCAA;
     protected byte dir4AdministrationLevel;
     @XmlElement(required = true)
     protected String dir4OrganizationCode;
     @XmlElement(required = true)
     protected String dir4OrganizationDesc;
     @XmlElement(required = true)
     protected String dir4DirCenCode;
     @XmlElement(required = true)
     protected String dir4DirCenDesc;
     @XmlElement(required = true)
     protected String dir4JobCentreCode;
     @XmlElement(required = true)
     protected String dir4JobCentreDesc;
     @XmlElement(required = true)
     protected String dir4OrganicalUnitCodeDir3;
     @XmlElement(required = true)
     protected String st;
     @XmlElement(required = true)
     protected String l;
     @XmlElement(required = true)
     protected String employeeType;
     @XmlElement(required = true)
     protected String title;
     @XmlElement(required = true)
     protected String uid;
     @XmlElement(required = true)
     protected String dir4UserName;
     @XmlElement(required = true)
     protected String dir4LdapBranch;
     @XmlElement(required = true)
     protected String dir4OriginSource;
     @XmlElement(required = true)
     protected String dir4SystemRegisterDate;

     /**
      * Obtiene el valor de la propiedad isCitizen.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getIsCitizen() {
         return isCitizen;
     }

     /**
      * Define el valor de la propiedad isCitizen.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setIsCitizen(String value) {
         this.isCitizen = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4DocumentID.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4DocumentID() {
         return dir4DocumentID;
     }

     /**
      * Define el valor de la propiedad dir4DocumentID.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4DocumentID(String value) {
         this.dir4DocumentID = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4DocumentType.
      * 
      */
     public byte getDir4DocumentType() {
         return dir4DocumentType;
     }

     /**
      * Define el valor de la propiedad dir4DocumentType.
      * 
      */
     public void setDir4DocumentType(byte value) {
         this.dir4DocumentType = value;
     }

     /**
      * Obtiene el valor de la propiedad cn.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getCn() {
         return cn;
     }

     /**
      * Define el valor de la propiedad cn.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setCn(String value) {
         this.cn = value;
     }

     /**
      * Obtiene el valor de la propiedad givenName.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getGivenName() {
         return givenName;
     }

     /**
      * Define el valor de la propiedad givenName.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setGivenName(String value) {
         this.givenName = value;
     }

     /**
      * Obtiene el valor de la propiedad sn.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getSn() {
         return sn;
     }

     /**
      * Define el valor de la propiedad sn.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setSn(String value) {
         this.sn = value;
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
      */
     public short getDir4UserLocalityCode() {
         return dir4UserLocalityCode;
     }

     /**
      * Define el valor de la propiedad dir4UserLocalityCode.
      * 
      */
     public void setDir4UserLocalityCode(short value) {
         this.dir4UserLocalityCode = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4UserLocalityEntity.
      * 
      */
     public byte getDir4UserLocalityEntity() {
         return dir4UserLocalityEntity;
     }

     /**
      * Define el valor de la propiedad dir4UserLocalityEntity.
      * 
      */
     public void setDir4UserLocalityEntity(byte value) {
         this.dir4UserLocalityEntity = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4UserLocality.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4UserLocality() {
         return dir4UserLocality;
     }

     /**
      * Define el valor de la propiedad dir4UserLocality.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4UserLocality(String value) {
         this.dir4UserLocality = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4UserProvinceCode.
      * 
      */
     public byte getDir4UserProvinceCode() {
         return dir4UserProvinceCode;
     }

     /**
      * Define el valor de la propiedad dir4UserProvinceCode.
      * 
      */
     public void setDir4UserProvinceCode(byte value) {
         this.dir4UserProvinceCode = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4UserProvince.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4UserProvince() {
         return dir4UserProvince;
     }

     /**
      * Define el valor de la propiedad dir4UserProvince.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4UserProvince(String value) {
         this.dir4UserProvince = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4UserCCAACode.
      * 
      */
     public byte getDir4UserCCAACode() {
         return dir4UserCCAACode;
     }

     /**
      * Define el valor de la propiedad dir4UserCCAACode.
      * 
      */
     public void setDir4UserCCAACode(byte value) {
         this.dir4UserCCAACode = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4UserCCAA.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4UserCCAA() {
         return dir4UserCCAA;
     }

     /**
      * Define el valor de la propiedad dir4UserCCAA.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4UserCCAA(String value) {
         this.dir4UserCCAA = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4AdministrationLevel.
      * 
      */
     public byte getDir4AdministrationLevel() {
         return dir4AdministrationLevel;
     }

     /**
      * Define el valor de la propiedad dir4AdministrationLevel.
      * 
      */
     public void setDir4AdministrationLevel(byte value) {
         this.dir4AdministrationLevel = value;
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
      * Obtiene el valor de la propiedad dir4OrganizationDesc.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4OrganizationDesc() {
         return dir4OrganizationDesc;
     }

     /**
      * Define el valor de la propiedad dir4OrganizationDesc.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4OrganizationDesc(String value) {
         this.dir4OrganizationDesc = value;
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
      * Obtiene el valor de la propiedad dir4DirCenDesc.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4DirCenDesc() {
         return dir4DirCenDesc;
     }

     /**
      * Define el valor de la propiedad dir4DirCenDesc.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4DirCenDesc(String value) {
         this.dir4DirCenDesc = value;
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
      * Obtiene el valor de la propiedad dir4JobCentreDesc.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4JobCentreDesc() {
         return dir4JobCentreDesc;
     }

     /**
      * Define el valor de la propiedad dir4JobCentreDesc.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4JobCentreDesc(String value) {
         this.dir4JobCentreDesc = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4OrganicalUnitCodeDir3.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4OrganicalUnitCodeDir3() {
         return dir4OrganicalUnitCodeDir3;
     }

     /**
      * Define el valor de la propiedad dir4OrganicalUnitCodeDir3.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4OrganicalUnitCodeDir3(String value) {
         this.dir4OrganicalUnitCodeDir3 = value;
     }

     /**
      * Obtiene el valor de la propiedad st.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getSt() {
         return st;
     }

     /**
      * Define el valor de la propiedad st.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setSt(String value) {
         this.st = value;
     }

     /**
      * Obtiene el valor de la propiedad l.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getL() {
         return l;
     }

     /**
      * Define el valor de la propiedad l.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setL(String value) {
         this.l = value;
     }

     /**
      * Obtiene el valor de la propiedad employeeType.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getEmployeeType() {
         return employeeType;
     }

     /**
      * Define el valor de la propiedad employeeType.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setEmployeeType(String value) {
         this.employeeType = value;
     }

     /**
      * Obtiene el valor de la propiedad title.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getTitle() {
         return title;
     }

     /**
      * Define el valor de la propiedad title.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setTitle(String value) {
         this.title = value;
     }

     /**
      * Obtiene el valor de la propiedad uid.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getUid() {
         return uid;
     }

     /**
      * Define el valor de la propiedad uid.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setUid(String value) {
         this.uid = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4UserName.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4UserName() {
         return dir4UserName;
     }

     /**
      * Define el valor de la propiedad dir4UserName.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4UserName(String value) {
         this.dir4UserName = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4LdapBranch.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4LdapBranch() {
         return dir4LdapBranch;
     }

     /**
      * Define el valor de la propiedad dir4LdapBranch.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4LdapBranch(String value) {
         this.dir4LdapBranch = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4OriginSource.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4OriginSource() {
         return dir4OriginSource;
     }

     /**
      * Define el valor de la propiedad dir4OriginSource.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4OriginSource(String value) {
         this.dir4OriginSource = value;
     }

     /**
      * Obtiene el valor de la propiedad dir4SystemRegisterDate.
      * 
      * @return
      *     possible object is
      *     {@link String }
      *     
      */
     public String getDir4SystemRegisterDate() {
         return dir4SystemRegisterDate;
     }

     /**
      * Define el valor de la propiedad dir4SystemRegisterDate.
      * 
      * @param value
      *     allowed object is
      *     {@link String }
      *     
      */
     public void setDir4SystemRegisterDate(String value) {
         this.dir4SystemRegisterDate = value;
     }

 }

}

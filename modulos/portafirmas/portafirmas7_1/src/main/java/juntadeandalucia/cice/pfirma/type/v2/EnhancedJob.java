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


package juntadeandalucia.cice.pfirma.type.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enhancedJob complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="enhancedJob">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="job" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}job" minOccurs="0"/>
 *         &lt;element name="enhancedUserJobInfo" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}enhancedUserJobInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enhancedJob", propOrder = {
    "job",
    "enhancedUserJobInfo"
})
public class EnhancedJob {

    @XmlElementRef(name = "job", type = JAXBElement.class, required = false)
    protected JAXBElement<Job> job;
    @XmlElementRef(name = "enhancedUserJobInfo", type = JAXBElement.class, required = false)
    protected JAXBElement<EnhancedUserJobInfo> enhancedUserJobInfo;

    /**
     * Gets the value of the job property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Job }{@code >}
     *     
     */
    public JAXBElement<Job> getJob() {
        return job;
    }

    /**
     * Sets the value of the job property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Job }{@code >}
     *     
     */
    public void setJob(JAXBElement<Job> value) {
        this.job = ((JAXBElement<Job> ) value);
    }

    /**
     * Gets the value of the enhancedUserJobInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EnhancedUserJobInfo }{@code >}
     *     
     */
    public JAXBElement<EnhancedUserJobInfo> getEnhancedUserJobInfo() {
        return enhancedUserJobInfo;
    }

    /**
     * Sets the value of the enhancedUserJobInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EnhancedUserJobInfo }{@code >}
     *     
     */
    public void setEnhancedUserJobInfo(JAXBElement<EnhancedUserJobInfo> value) {
        this.enhancedUserJobInfo = ((JAXBElement<EnhancedUserJobInfo> ) value);
    }

}

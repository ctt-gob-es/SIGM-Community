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


package es.seap.minhap.portafirmas.ws.eeutil.utilfirma;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListaFirmaInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListaFirmaInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="informacionFirmas">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="informacionFirmas" type="{http://service.ws.inside.dsic.mpt.es/}FirmaInfo" maxOccurs="unbounded"/>
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
@XmlType(name = "ListaFirmaInfo", propOrder = {
    "informacionFirmas"
})
public class ListaFirmaInfo {

    @XmlElement(required = true)
    protected ListaFirmaInfo.InformacionFirmas informacionFirmas;

    /**
     * Gets the value of the informacionFirmas property.
     * 
     * @return
     *     possible object is
     *     {@link ListaFirmaInfo.InformacionFirmas }
     *     
     */
    public ListaFirmaInfo.InformacionFirmas getInformacionFirmas() {
        return informacionFirmas;
    }

    /**
     * Sets the value of the informacionFirmas property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaFirmaInfo.InformacionFirmas }
     *     
     */
    public void setInformacionFirmas(ListaFirmaInfo.InformacionFirmas value) {
        this.informacionFirmas = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="informacionFirmas" type="{http://service.ws.inside.dsic.mpt.es/}FirmaInfo" maxOccurs="unbounded"/>
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
        "informacionFirmas"
    })
    public static class InformacionFirmas {

        @XmlElement(required = true)
        protected List<FirmaInfo> informacionFirmas;

        /**
         * Gets the value of the informacionFirmas property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the informacionFirmas property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInformacionFirmas().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FirmaInfo }
         * 
         * 
         */
        public List<FirmaInfo> getInformacionFirmas() {
            if (informacionFirmas == null) {
                informacionFirmas = new ArrayList<FirmaInfo>();
            }
            return this.informacionFirmas;
        }

    }

}

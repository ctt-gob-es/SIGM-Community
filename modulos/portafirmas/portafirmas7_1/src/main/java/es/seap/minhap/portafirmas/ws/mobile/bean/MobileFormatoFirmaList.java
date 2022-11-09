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


package es.seap.minhap.portafirmas.ws.mobile.bean;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mobileFormatoFirmaList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mobileFormatoFirmaList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mobileFormatoFirma" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileFormatoFirma" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileFormatoFirmaList", propOrder = {
    "mobileFormatoFirma"
})
public class MobileFormatoFirmaList {

    protected List<MobileFormatoFirma> mobileFormatoFirma;

    /**
     * Gets the value of the mobileFormatoFirma property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mobileFormatoFirma property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMobileFormatoFirma().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MobileFormatoFirma }
     * 
     * 
     */
    public List<MobileFormatoFirma> getMobileFormatoFirma() {
        if (mobileFormatoFirma == null) {
            mobileFormatoFirma = new ArrayList<MobileFormatoFirma>();
        }
        return this.mobileFormatoFirma;
    }

}

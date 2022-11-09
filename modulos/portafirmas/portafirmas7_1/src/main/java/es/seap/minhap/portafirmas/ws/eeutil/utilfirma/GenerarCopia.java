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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generarCopia complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generarCopia">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aplicacionInfo" type="{http://service.ws.inside.dsic.mpt.es/}applicationLogin"/>
 *         &lt;element name="copiaInfo" type="{http://service.ws.inside.dsic.mpt.es/}CopiaInfo"/>
 *         &lt;element name="simpleFormat" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generarCopia", propOrder = {
    "aplicacionInfo",
    "copiaInfo",
    "simpleFormat"
})
public class GenerarCopia {

    @XmlElement(required = true)
    protected ApplicationLogin aplicacionInfo;
    @XmlElement(required = true)
    protected CopiaInfo copiaInfo;
    protected boolean simpleFormat;

    /**
     * Gets the value of the aplicacionInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationLogin }
     *     
     */
    public ApplicationLogin getAplicacionInfo() {
        return aplicacionInfo;
    }

    /**
     * Sets the value of the aplicacionInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationLogin }
     *     
     */
    public void setAplicacionInfo(ApplicationLogin value) {
        this.aplicacionInfo = value;
    }

    /**
     * Gets the value of the copiaInfo property.
     * 
     * @return
     *     possible object is
     *     {@link CopiaInfo }
     *     
     */
    public CopiaInfo getCopiaInfo() {
        return copiaInfo;
    }

    /**
     * Sets the value of the copiaInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CopiaInfo }
     *     
     */
    public void setCopiaInfo(CopiaInfo value) {
        this.copiaInfo = value;
    }

    /**
     * Gets the value of the simpleFormat property.
     * 
     */
    public boolean isSimpleFormat() {
        return simpleFormat;
    }

    /**
     * Sets the value of the simpleFormat property.
     * 
     */
    public void setSimpleFormat(boolean value) {
        this.simpleFormat = value;
    }

}

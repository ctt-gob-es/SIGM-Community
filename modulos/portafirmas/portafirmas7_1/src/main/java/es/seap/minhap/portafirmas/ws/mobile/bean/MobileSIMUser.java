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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mobileSIMUser complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mobileSIMUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Servicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Plataforma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdRegistro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdDispositivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileSIMUser", propOrder = {
    "idRegistro",
    "plataforma",
    "idDispositivo"
})
public class MobileSIMUser {

    @XmlElement(name = "IdRegistro")
    protected String idRegistro;
    @XmlElement(name = "Plataforma")
    protected String plataforma;
    @XmlElement(name = "IdDispositivo")
    protected String idDispositivo;


    public String getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(String idRegistro) {
		this.idRegistro = idRegistro;
	}

	/**
     * Gets the value of the plataforma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlataforma() {
        return plataforma;
    }

    /**
     * Sets the value of the plataforma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlataforma(String value) {
        this.plataforma = value;
    }

    /**
     * Gets the value of the idDispositivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDispositivo() {
        return idDispositivo;
    }

    /**
     * Sets the value of the idDispositivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDispositivo(String value) {
        this.idDispositivo = value;
    }

}

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


package es.seap.minhap.portafirmas.ws.eeutil.vis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Plantilla complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Plantilla">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identicador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bytesPlantilla" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Plantilla", propOrder = {
    "identicador",
    "bytesPlantilla"
})
public class Plantilla {

    @XmlElement(required = true)
    protected String identicador;
    @XmlElement(required = true)
    protected byte[] bytesPlantilla;

    /**
     * Obtiene el valor de la propiedad identicador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdenticador() {
        return identicador;
    }

    /**
     * Define el valor de la propiedad identicador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdenticador(String value) {
        this.identicador = value;
    }

    /**
     * Obtiene el valor de la propiedad bytesPlantilla.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBytesPlantilla() {
        return bytesPlantilla;
    }

    /**
     * Define el valor de la propiedad bytesPlantilla.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBytesPlantilla(byte[] value) {
        this.bytesPlantilla = value;
    }

}

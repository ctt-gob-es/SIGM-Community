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


package es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoResultadoValidacionDetalleExpedienteInside complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoResultadoValidacionDetalleExpedienteInside">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoValidacion" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/expediente-e}TipoOpcionValidacionExpediente"/>
 *         &lt;element name="resultadoValidacion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="detalleValidacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoResultadoValidacionDetalleExpedienteInside", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/expediente-e/resultados", propOrder = {
    "tipoValidacion",
    "resultadoValidacion",
    "detalleValidacion"
})
public class TipoResultadoValidacionDetalleExpedienteInside {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TipoOpcionValidacionExpediente tipoValidacion;
    protected boolean resultadoValidacion;
    @XmlElement(required = true)
    protected String detalleValidacion;

    /**
     * Obtiene el valor de la propiedad tipoValidacion.
     * 
     * @return
     *     possible object is
     *     {@link TipoOpcionValidacionExpediente }
     *     
     */
    public TipoOpcionValidacionExpediente getTipoValidacion() {
        return tipoValidacion;
    }

    /**
     * Define el valor de la propiedad tipoValidacion.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoOpcionValidacionExpediente }
     *     
     */
    public void setTipoValidacion(TipoOpcionValidacionExpediente value) {
        this.tipoValidacion = value;
    }

    /**
     * Obtiene el valor de la propiedad resultadoValidacion.
     * 
     */
    public boolean isResultadoValidacion() {
        return resultadoValidacion;
    }

    /**
     * Define el valor de la propiedad resultadoValidacion.
     * 
     */
    public void setResultadoValidacion(boolean value) {
        this.resultadoValidacion = value;
    }

    /**
     * Obtiene el valor de la propiedad detalleValidacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetalleValidacion() {
        return detalleValidacion;
    }

    /**
     * Define el valor de la propiedad detalleValidacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetalleValidacion(String value) {
        this.detalleValidacion = value;
    }

}

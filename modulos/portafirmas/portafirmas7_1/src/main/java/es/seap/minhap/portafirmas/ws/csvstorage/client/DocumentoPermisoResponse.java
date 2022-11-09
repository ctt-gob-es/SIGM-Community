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


package es.seap.minhap.portafirmas.ws.csvstorage.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para documentoPermisoResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="documentoPermisoResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}response">
 *       &lt;sequence>
 *         &lt;element name="valorCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoPermiso" type="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}tipoPermiso" minOccurs="0"/>
 *         &lt;element name="restricciones" type="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}restricciones" minOccurs="0"/>
 *         &lt;element name="restringidoPorIdentificacion" type="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}restringidoPorIdentificacion" minOccurs="0"/>
 *         &lt;element name="restringidoNif" type="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}restringidoNif" minOccurs="0"/>
 *         &lt;element name="restringidoAplicaciones" type="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}restringidoAplicaciones" minOccurs="0"/>
 *         &lt;element name="direccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="algoritmo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentoPermisoResponse", propOrder = {
    "valorCSV",
    "tipoPermiso",
    "restricciones",
    "restringidoPorIdentificacion",
    "restringidoNif",
    "restringidoAplicaciones",
    "direccion",
    "hash",
    "algoritmo"
})
public class DocumentoPermisoResponse
    extends Response
{

    protected String valorCSV;
    @XmlSchemaType(name = "string")
    protected TipoPermiso tipoPermiso;
    protected Restricciones restricciones;
    protected RestringidoPorIdentificacion restringidoPorIdentificacion;
    protected RestringidoNif restringidoNif;
    protected RestringidoAplicaciones restringidoAplicaciones;
    protected String direccion;
    protected String hash;
    protected String algoritmo;

    /**
     * Obtiene el valor de la propiedad valorCSV.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorCSV() {
        return valorCSV;
    }

    /**
     * Define el valor de la propiedad valorCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorCSV(String value) {
        this.valorCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoPermiso.
     * 
     * @return
     *     possible object is
     *     {@link TipoPermiso }
     *     
     */
    public TipoPermiso getTipoPermiso() {
        return tipoPermiso;
    }

    /**
     * Define el valor de la propiedad tipoPermiso.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoPermiso }
     *     
     */
    public void setTipoPermiso(TipoPermiso value) {
        this.tipoPermiso = value;
    }

    /**
     * Obtiene el valor de la propiedad restricciones.
     * 
     * @return
     *     possible object is
     *     {@link Restricciones }
     *     
     */
    public Restricciones getRestricciones() {
        return restricciones;
    }

    /**
     * Define el valor de la propiedad restricciones.
     * 
     * @param value
     *     allowed object is
     *     {@link Restricciones }
     *     
     */
    public void setRestricciones(Restricciones value) {
        this.restricciones = value;
    }

    /**
     * Obtiene el valor de la propiedad restringidoPorIdentificacion.
     * 
     * @return
     *     possible object is
     *     {@link RestringidoPorIdentificacion }
     *     
     */
    public RestringidoPorIdentificacion getRestringidoPorIdentificacion() {
        return restringidoPorIdentificacion;
    }

    /**
     * Define el valor de la propiedad restringidoPorIdentificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link RestringidoPorIdentificacion }
     *     
     */
    public void setRestringidoPorIdentificacion(RestringidoPorIdentificacion value) {
        this.restringidoPorIdentificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad restringidoNif.
     * 
     * @return
     *     possible object is
     *     {@link RestringidoNif }
     *     
     */
    public RestringidoNif getRestringidoNif() {
        return restringidoNif;
    }

    /**
     * Define el valor de la propiedad restringidoNif.
     * 
     * @param value
     *     allowed object is
     *     {@link RestringidoNif }
     *     
     */
    public void setRestringidoNif(RestringidoNif value) {
        this.restringidoNif = value;
    }

    /**
     * Obtiene el valor de la propiedad restringidoAplicaciones.
     * 
     * @return
     *     possible object is
     *     {@link RestringidoAplicaciones }
     *     
     */
    public RestringidoAplicaciones getRestringidoAplicaciones() {
        return restringidoAplicaciones;
    }

    /**
     * Define el valor de la propiedad restringidoAplicaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link RestringidoAplicaciones }
     *     
     */
    public void setRestringidoAplicaciones(RestringidoAplicaciones value) {
        this.restringidoAplicaciones = value;
    }

    /**
     * Obtiene el valor de la propiedad direccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Define el valor de la propiedad direccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccion(String value) {
        this.direccion = value;
    }

    /**
     * Obtiene el valor de la propiedad hash.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Define el valor de la propiedad hash.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    /**
     * Obtiene el valor de la propiedad algoritmo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgoritmo() {
        return algoritmo;
    }

    /**
     * Define el valor de la propiedad algoritmo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgoritmo(String value) {
        this.algoritmo = value;
    }

}

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
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Clase Java para item complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="item">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificador" type="{http://www.w3.org/2001/XMLSchema}ID"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="padre" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/>
 *         &lt;element name="hijos" type="{http://service.ws.inside.dsic.mpt.es/}ListaItem" minOccurs="0"/>
 *         &lt;element name="documentoContenido" type="{http://service.ws.inside.dsic.mpt.es/}documentoContenido" minOccurs="0"/>
 *         &lt;element name="propiedadesItem" type="{http://service.ws.inside.dsic.mpt.es/}ListaPropiedades" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = {
    "identificador",
    "nombre",
    "padre",
    "hijos",
    "documentoContenido",
    "propiedadesItem"
})
public class Item {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String identificador;
    @XmlElement(required = true)
    protected String nombre;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object padre;
    protected ListaItem hijos;
    protected DocumentoContenido documentoContenido;
    protected ListaPropiedades propiedadesItem;

    /**
     * Obtiene el valor de la propiedad identificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Define el valor de la propiedad identificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificador(String value) {
        this.identificador = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad padre.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPadre() {
        return padre;
    }

    /**
     * Define el valor de la propiedad padre.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPadre(Object value) {
        this.padre = value;
    }

    /**
     * Obtiene el valor de la propiedad hijos.
     * 
     * @return
     *     possible object is
     *     {@link ListaItem }
     *     
     */
    public ListaItem getHijos() {
        return hijos;
    }

    /**
     * Define el valor de la propiedad hijos.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaItem }
     *     
     */
    public void setHijos(ListaItem value) {
        this.hijos = value;
    }

    /**
     * Obtiene el valor de la propiedad documentoContenido.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoContenido }
     *     
     */
    public DocumentoContenido getDocumentoContenido() {
        return documentoContenido;
    }

    /**
     * Define el valor de la propiedad documentoContenido.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoContenido }
     *     
     */
    public void setDocumentoContenido(DocumentoContenido value) {
        this.documentoContenido = value;
    }

    /**
     * Obtiene el valor de la propiedad propiedadesItem.
     * 
     * @return
     *     possible object is
     *     {@link ListaPropiedades }
     *     
     */
    public ListaPropiedades getPropiedadesItem() {
        return propiedadesItem;
    }

    /**
     * Define el valor de la propiedad propiedadesItem.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaPropiedades }
     *     
     */
    public void setPropiedadesItem(ListaPropiedades value) {
        this.propiedadesItem = value;
    }

}

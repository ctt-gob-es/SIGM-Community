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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Para el intercambio de un expediente electronico, se envia  en primer lugar, el indice del expediente. Posteriormente, se enviarian los documentos que lo componen , uno a uno,  y siguiendo la distribucion reflejada en el contenido del indice.
 * 
 * <p>Clase Java para TipoExpediente complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoExpediente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/indice-e}indice"/>
 *         &lt;element ref="{http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/metadatos}metadatosExp"/>
 *         &lt;element name="VisualizacionIndice" type="{http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/contenido}TipoContenido" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoExpediente", namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e", propOrder = {
    "indice",
    "metadatosExp",
    "visualizacionIndice"
})
public class TipoExpediente {

    @XmlElement(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/indice-e", required = true)
    protected TipoIndice indice;
    @XmlElement(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/metadatos", required = true)
    protected TipoMetadatos metadatosExp;
    @XmlElement(name = "VisualizacionIndice")
    protected TipoContenido visualizacionIndice;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Obtiene el valor de la propiedad indice.
     * 
     * @return
     *     possible object is
     *     {@link TipoIndice }
     *     
     */
    public TipoIndice getIndice() {
        return indice;
    }

    /**
     * Define el valor de la propiedad indice.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoIndice }
     *     
     */
    public void setIndice(TipoIndice value) {
        this.indice = value;
    }

    /**
     * Obtiene el valor de la propiedad metadatosExp.
     * 
     * @return
     *     possible object is
     *     {@link TipoMetadatos }
     *     
     */
    public TipoMetadatos getMetadatosExp() {
        return metadatosExp;
    }

    /**
     * Define el valor de la propiedad metadatosExp.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoMetadatos }
     *     
     */
    public void setMetadatosExp(TipoMetadatos value) {
        this.metadatosExp = value;
    }

    /**
     * Obtiene el valor de la propiedad visualizacionIndice.
     * 
     * @return
     *     possible object is
     *     {@link TipoContenido }
     *     
     */
    public TipoContenido getVisualizacionIndice() {
        return visualizacionIndice;
    }

    /**
     * Define el valor de la propiedad visualizacionIndice.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoContenido }
     *     
     */
    public void setVisualizacionIndice(TipoContenido value) {
        this.visualizacionIndice = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}

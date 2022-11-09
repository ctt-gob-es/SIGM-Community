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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoDocumentoEniBinarioOTipo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoDocumentoEniBinarioOTipo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="documentoEniBinario" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="documentoEniTipo" type="{http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e}TipoDocumento"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoDocumentoEniBinarioOTipo", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/visualizacion/documento-e", propOrder = {
    "documentoEniBinario",
    "documentoEniTipo"
})
public class TipoDocumentoEniBinarioOTipo {

    protected byte[] documentoEniBinario;
    protected TipoDocumento documentoEniTipo;

    /**
     * Obtiene el valor de la propiedad documentoEniBinario.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDocumentoEniBinario() {
        return documentoEniBinario;
    }

    /**
     * Define el valor de la propiedad documentoEniBinario.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDocumentoEniBinario(byte[] value) {
        this.documentoEniBinario = value;
    }

    /**
     * Obtiene el valor de la propiedad documentoEniTipo.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumento }
     *     
     */
    public TipoDocumento getDocumentoEniTipo() {
        return documentoEniTipo;
    }

    /**
     * Define el valor de la propiedad documentoEniTipo.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumento }
     *     
     */
    public void setDocumentoEniTipo(TipoDocumento value) {
        this.documentoEniTipo = value;
    }

}

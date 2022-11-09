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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para enumeracionEstadoElaboracion.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="enumeracionEstadoElaboracion">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EE01"/>
 *     &lt;enumeration value="EE02"/>
 *     &lt;enumeration value="EE03"/>
 *     &lt;enumeration value="EE04"/>
 *     &lt;enumeration value="EE99"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumeracionEstadoElaboracion", namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/metadatos")
@XmlEnum
public enum EnumeracionEstadoElaboracion {

    @XmlEnumValue("EE01")
    EE_01("EE01"),
    @XmlEnumValue("EE02")
    EE_02("EE02"),
    @XmlEnumValue("EE03")
    EE_03("EE03"),
    @XmlEnumValue("EE04")
    EE_04("EE04"),
    @XmlEnumValue("EE99")
    EE_99("EE99");
    private final String value;

    EnumeracionEstadoElaboracion(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumeracionEstadoElaboracion fromValue(String v) {
        for (EnumeracionEstadoElaboracion c: EnumeracionEstadoElaboracion.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

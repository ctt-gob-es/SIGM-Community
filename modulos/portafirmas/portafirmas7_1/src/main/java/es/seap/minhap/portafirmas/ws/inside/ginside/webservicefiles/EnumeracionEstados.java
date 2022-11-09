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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para enumeracionEstados.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="enumeracionEstados">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="E01"/>
 *     &lt;enumeration value="E02"/>
 *     &lt;enumeration value="E03"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumeracionEstados", namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/expediente-e/metadatos")
@XmlEnum
public enum EnumeracionEstados {

    @XmlEnumValue("E01")
    E_01("E01"),
    @XmlEnumValue("E02")
    E_02("E02"),
    @XmlEnumValue("E03")
    E_03("E03");
    private final String value;

    EnumeracionEstados(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumeracionEstados fromValue(String v) {
        for (EnumeracionEstados c: EnumeracionEstados.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

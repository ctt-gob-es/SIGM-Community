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
 * <p>Clase Java para TipoOpcionValidacionExpediente.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoOpcionValidacionExpediente">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TOVE01"/>
 *     &lt;enumeration value="TOVE02"/>
 *     &lt;enumeration value="TOVE03"/>
 *     &lt;enumeration value="TOVE04"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TipoOpcionValidacionExpediente", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/validacion/expediente-e")
@XmlEnum
public enum TipoOpcionValidacionExpediente {

    @XmlEnumValue("TOVE01")
    TOVE_01("TOVE01"),
    @XmlEnumValue("TOVE02")
    TOVE_02("TOVE02"),
    @XmlEnumValue("TOVE03")
    TOVE_03("TOVE03"),
    @XmlEnumValue("TOVE04")
    TOVE_04("TOVE04");
    private final String value;

    TipoOpcionValidacionExpediente(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoOpcionValidacionExpediente fromValue(String v) {
        for (TipoOpcionValidacionExpediente c: TipoOpcionValidacionExpediente.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

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


package juntadeandalucia.cice.pfirma.type.v2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for signFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="signFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PKCS7"/>
 *     &lt;enumeration value="CMS"/>
 *     &lt;enumeration value="CADES"/>
 *     &lt;enumeration value="XADES"/>
 *     &lt;enumeration value="XADES IMPLICITO"/>
 *     &lt;enumeration value="XADES EXPLICITO"/>
 *     &lt;enumeration value="XADES ENVELOPING"/>
 *     &lt;enumeration value="PDF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "signFormat")
@XmlEnum
public enum SignFormat {

    @XmlEnumValue("PKCS7")
    PKCS_7("PKCS7"),
    CMS("CMS"),
    CADES("CADES"),
    XADES("XADES"),
    @XmlEnumValue("XADES IMPLICITO")
    XADES_IMPLICITO("XADES IMPLICITO"),
    @XmlEnumValue("XADES EXPLICITO")
    XADES_EXPLICITO("XADES EXPLICITO"),
    @XmlEnumValue("XADES ENVELOPING")
    XADES_ENVELOPING("XADES ENVELOPING"),
    PDF("PDF");
    private final String value;

    SignFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SignFormat fromValue(String v) {
        for (SignFormat c: SignFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoDocumental.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoDocumental">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TD01"/>
 *     &lt;enumeration value="TD02"/>
 *     &lt;enumeration value="TD03"/>
 *     &lt;enumeration value="TD04"/>
 *     &lt;enumeration value="TD05"/>
 *     &lt;enumeration value="TD06"/>
 *     &lt;enumeration value="TD07"/>
 *     &lt;enumeration value="TD08"/>
 *     &lt;enumeration value="TD09"/>
 *     &lt;enumeration value="TD10"/>
 *     &lt;enumeration value="TD11"/>
 *     &lt;enumeration value="TD12"/>
 *     &lt;enumeration value="TD13"/>
 *     &lt;enumeration value="TD14"/>
 *     &lt;enumeration value="TD15"/>
 *     &lt;enumeration value="TD16"/>
 *     &lt;enumeration value="TD17"/>
 *     &lt;enumeration value="TD18"/>
 *     &lt;enumeration value="TD19"/>
 *     &lt;enumeration value="TD20"/>
 *     &lt;enumeration value="TD51"/>
 *     &lt;enumeration value="TD52"/>
 *     &lt;enumeration value="TD53"/>
 *     &lt;enumeration value="TD54"/>
 *     &lt;enumeration value="TD55"/>
 *     &lt;enumeration value="TD56"/>
 *     &lt;enumeration value="TD57"/>
 *     &lt;enumeration value="TD58"/>
 *     &lt;enumeration value="TD59"/>
 *     &lt;enumeration value="TD60"/>
 *     &lt;enumeration value="TD61"/>
 *     &lt;enumeration value="TD62"/>
 *     &lt;enumeration value="TD63"/>
 *     &lt;enumeration value="TD64"/>
 *     &lt;enumeration value="TD65"/>
 *     &lt;enumeration value="TD66"/>
 *     &lt;enumeration value="TD67"/>
 *     &lt;enumeration value="TD68"/>
 *     &lt;enumeration value="TD69"/>
 *     &lt;enumeration value="TD99"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoDocumental", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos")
@XmlEnum
public enum TipoDocumental {

    @XmlEnumValue("TD01")
    TD_01("TD01"),
    @XmlEnumValue("TD02")
    TD_02("TD02"),
    @XmlEnumValue("TD03")
    TD_03("TD03"),
    @XmlEnumValue("TD04")
    TD_04("TD04"),
    @XmlEnumValue("TD05")
    TD_05("TD05"),
    @XmlEnumValue("TD06")
    TD_06("TD06"),
    @XmlEnumValue("TD07")
    TD_07("TD07"),
    @XmlEnumValue("TD08")
    TD_08("TD08"),
    @XmlEnumValue("TD09")
    TD_09("TD09"),
    @XmlEnumValue("TD10")
    TD_10("TD10"),
    @XmlEnumValue("TD11")
    TD_11("TD11"),
    @XmlEnumValue("TD12")
    TD_12("TD12"),
    @XmlEnumValue("TD13")
    TD_13("TD13"),
    @XmlEnumValue("TD14")
    TD_14("TD14"),
    @XmlEnumValue("TD15")
    TD_15("TD15"),
    @XmlEnumValue("TD16")
    TD_16("TD16"),
    @XmlEnumValue("TD17")
    TD_17("TD17"),
    @XmlEnumValue("TD18")
    TD_18("TD18"),
    @XmlEnumValue("TD19")
    TD_19("TD19"),
    @XmlEnumValue("TD20")
    TD_20("TD20"),
    @XmlEnumValue("TD51")
    TD_51("TD51"),
    @XmlEnumValue("TD52")
    TD_52("TD52"),
    @XmlEnumValue("TD53")
    TD_53("TD53"),
    @XmlEnumValue("TD54")
    TD_54("TD54"),
    @XmlEnumValue("TD55")
    TD_55("TD55"),
    @XmlEnumValue("TD56")
    TD_56("TD56"),
    @XmlEnumValue("TD57")
    TD_57("TD57"),
    @XmlEnumValue("TD58")
    TD_58("TD58"),
    @XmlEnumValue("TD59")
    TD_59("TD59"),
    @XmlEnumValue("TD60")
    TD_60("TD60"),
    @XmlEnumValue("TD61")
    TD_61("TD61"),
    @XmlEnumValue("TD62")
    TD_62("TD62"),
    @XmlEnumValue("TD63")
    TD_63("TD63"),
    @XmlEnumValue("TD64")
    TD_64("TD64"),
    @XmlEnumValue("TD65")
    TD_65("TD65"),
    @XmlEnumValue("TD66")
    TD_66("TD66"),
    @XmlEnumValue("TD67")
    TD_67("TD67"),
    @XmlEnumValue("TD68")
    TD_68("TD68"),
    @XmlEnumValue("TD69")
    TD_69("TD69"),
    @XmlEnumValue("TD99")
    TD_99("TD99");
    private final String value;

    TipoDocumental(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoDocumental fromValue(String v) {
        for (TipoDocumental c: TipoDocumental.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

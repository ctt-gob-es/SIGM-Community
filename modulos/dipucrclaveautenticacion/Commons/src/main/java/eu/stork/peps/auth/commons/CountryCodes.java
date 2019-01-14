/*
 * This work is Open Source and licensed by the European Commission under the
 * conditions of the European Public License v1.1 
 *  
 * (http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1); 
 * 
 * any use of this file implies acceptance of the conditions of this license. 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */
package eu.stork.peps.auth.commons;

import java.util.Arrays;
import java.util.List;

/**
 * This class contains all the ISO 3166-1 Alpha 3 Country Codes.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.2 $, $Date: 2011-04-14 00:24:56 $
 */
public final class CountryCodes {

    /**
     * Private Constructor.
     */
    private CountryCodes() {

    }

    /**
     * ISO 3166-1 Alpha 3 Country Codes.
     */
    private static List<String> countrysAlpha3 = Arrays.asList("ABW", "AFG",
            "AGO", "AIA", "ALA", "ALB", "AND", "ANT", "ARE", "ARG", "ARM", "ASM",
            "ATA", "ATF", "ATG", "AUS", "AUT", "AZE", "BDI", "BEL", "BEN", "BES",
            "BFA", "BGD", "BGR", "BHR", "BHS", "BIH", "BLM", "BLR", "BLZ", "BMU",
            "BOL", "BRA", "BRB", "BRN", "BTN", "BUR", "BVT", "BWA", "BYS", "CAF",
            "CAN", "CCK", "CHE", "CHL", "CHN", "CIV", "CMR", "COD", "COG", "COK",
            "COL", "COM", "CPV", "CRI", "CSK", "CUB", "CUW", "CXR", "CYM", "CYP",
            "CZE", "DEU", "DJI", "DMA", "DNK", "DOM", "DZA", "ECU", "EGY", "ERI",
            "ESH", "ESP", "EST", "ETH", "FIN", "FJI", "FLK", "FRA", "FRO", "FSM",
            "GAB", "GBR", "GEO", "GGY", "GHA", "GIB", "GIN", "GLP", "GMB", "GNB",
            "GNQ", "GRC", "GRD", "GRL", "GTM", "GUF", "GUM", "GUY", "HKG", "HMD",
            "HND", "HRV", "HTI", "HUN", "IDN", "IMN", "IND", "IOT", "IRL", "IRN",
            "IRQ", "ISL", "ISR", "ITA", "JAM", "JEY", "JOR", "JPN", "KAZ", "KEN",
            "KGZ", "KHM", "KIR", "KNA", "KOR", "KWT", "LAO", "LBN", "LBR", "LBY",
            "LCA", "LIE", "LKA", "LSO", "LTU", "LUX", "LVA", "MAC", "MAF", "MAR",
            "MCO", "MDA", "MDG", "MDV", "MEX", "MHL", "MKD", "MLI", "MLT", "MMR",
            "MNE", "MNG", "MNP", "MOZ", "MRT", "MSR", "MTQ", "MUS", "MWI", "MYS",
            "MYT", "NAM", "NCL", "NER", "NFK", "NGA", "NIC", "NIU", "NLD", "NOR",
            "NPL", "NRU", "NZL", "OMN", "PAK", "PAN", "PCN", "PER", "PHL", "PLW",
            "PNG", "POL", "PRI", "PRK", "PRT", "PRY", "PSE", "PYF", "QAT", "REU",
            "ROM", "ROU", "RUS", "RWA", "SAU", "SCG", "SDN", "SEN", "SGP", "SGS",
            "SHN", "SJM", "SLB", "SLE", "SLV", "SMR", "SOM", "SPM", "SRB", "STP",
            "SUR", "SVK", "SVN", "SXW", "SWE", "SWZ", "SYC", "SYR", "TCA", "TCD",
            "TGO", "THA", "TJK", "TKL", "TKM", "TLS", "TMP", "TON", "TTO", "TUN",
            "TUR", "TUV", "TWN", "TZA", "UGA", "UKR", "UMI", "URY", "USA", "UZB",
            "VAT", "VCT", "VEN", "VGB", "VIR", "VNM", "VUT", "WLF", "WSM", "YEM",
            "YUG", "ZAF", "ZAR", "ZMB", "ZWE");

    /**
     * Searches the CountryCode (3166-1 alpha3 format) an return true if it
     * exists.
     *
     * @param countryCode The Country code to search.
     *
     * @return true if the CountryCode exists, false otherwise.
     */
    public static boolean hasCountryCodeAlpha3(final String countryCode) {

        return CountryCodes.countrysAlpha3.contains(countryCode);
    }
}

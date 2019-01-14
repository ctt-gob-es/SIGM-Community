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

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import eu.stork.peps.auth.commons.exceptions.SecurityPEPSException;

/**
 * This class holds static helper methods for Date Operations.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.4 $, $Date: 2010-11-17 05:15:28 $
 */
public final class DateUtil {

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(DateUtil.class.getName());

    /**
     * yyyy Date format size.
     */
    private static final int YEAR_DATE_SIZE = 4;

    /**
     * yyyyMM Date format size.
     */
    private static final int MONTH_DATE_SIZE = 6;

    /**
     * Private constructor. Prevents the class from being instantiated.
     */
    private DateUtil() {
        // empty constructor
    }

    /**
     * Fulfils dateValue with a valid date. The following roles are applied: a)
     * If the dateValue only contains the year then fulfils with last year's
     * day. e.g. this method returns 19951231 to the 1995 dateValue. b) If the
     * dateValue contains the year and the month then fulfils with last month's
     * day. e.g. this method returns 19950630 to the 199505 dateValue.
     *
     * @param dateValue The date to be fulfilled.
     *
     * @return The dateValue fulfilled.
     */
    private static String fulfilDate(final String dateValue) {

        final StringBuffer strBuf = new StringBuffer();
        strBuf.append(dateValue);
        // if the IdP just provides the year then we must fullfil the date.
        if (dateValue.length() == YEAR_DATE_SIZE) {
            strBuf.append(PEPSValues.LAST_MONTH.toString());
        }
        // if the IdP provides the year and the month then we must fullfil the
        // date.
        if (dateValue.length() == MONTH_DATE_SIZE
                || strBuf.length() == MONTH_DATE_SIZE) {
            // IdP doesn't provide the day, so we will use DateTime to
            // calculate it.
            final String noDayCons = PEPSValues.NO_DAY_DATE_FORMAT.toString();
            final DateTimeFormatter fmt = DateTimeFormat.forPattern(noDayCons);
            final DateTime dateTime = fmt.parseDateTime(strBuf.toString());
            // Append the last month's day.
            strBuf.append(dateTime.dayOfMonth().withMaximumValue().getDayOfMonth());
        }

        return strBuf.toString();
    }

    /**
     * Validates the dateValue format: a) if has a valid size; b) if has a
     * numeric value; Note: dateValue must have the format yyyyMMdd.
     *
     * @param dateValueTmp The date to be validated.
     * @param pattern The accepted date format.
     *
     * @return true if the date has a valid format.
     */
    public static boolean isValidFormatDate(final String dateValueTmp,
            final String pattern) {

        boolean retVal = true;
        try {
            final String dateValue = DateUtil.fulfilDate(dateValueTmp);

            final DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
            fmt.parseDateTime(dateValue);
        } catch (final Exception e) {
            // We catch Exception because we only have to return false
            // value!
            retVal = false;
        }
        return retVal;
    }

    /**
     * Calculates the age for a given date string.
     *
     * @param dateVal The date to be validated.
     * @param now The current date.
     * @param pattern The date pattern.
     *
     * @return The age value.
     */
    public static int calculateAge(final String dateVal, final DateTime now,
            final String pattern) {

        if (DateUtil.isValidFormatDate(dateVal, pattern)) {
            try {
                final String dateValueTemp = DateUtil.fulfilDate(dateVal);
                final DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
                final DateTime dateTime = fmt.parseDateTime(dateValueTemp);
                // Calculating age
                final Years age = Years.yearsBetween(dateTime, now);

                return age.getYears();
            } catch (final IllegalArgumentException e) {
                LOG.warn("Invalid date format (" + pattern
                        + ") or an invalid dateValue.");
                throw new SecurityPEPSException(
                        PEPSUtil.getConfig(PEPSErrors.INVALID_ATTRIBUTE_VALUE.errorCode()),
                        PEPSUtil.getConfig(PEPSErrors.INVALID_ATTRIBUTE_VALUE.errorMessage()),
                        e);
            }
        } else {
            LOG.warn("Couldn't calculate Age, invalid date!");
            throw new SecurityPEPSException(
                    PEPSUtil.getConfig(PEPSErrors.INVALID_ATTRIBUTE_VALUE.errorCode()),
                    PEPSUtil.getConfig(PEPSErrors.INVALID_ATTRIBUTE_VALUE.errorMessage()));
        }

    }

    /**
     * Generates the current timestamp.
     *
     * @return timestamp The current timestamp
     */
    public static Timestamp currentTimeStamp() {
        final GregorianCalendar cal = new GregorianCalendar();
        final long millis = cal.getTimeInMillis();
        return new Timestamp(millis);
    }

}

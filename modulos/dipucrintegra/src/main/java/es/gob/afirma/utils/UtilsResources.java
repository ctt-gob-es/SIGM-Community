// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/**
 * <b>File:</b><p>es.gob.afirma.utils.UtilsResources.java.</p>
 * <b>Description:</b><p>Class that provides functionality to control the close of resources.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>13/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 13/01/2014.
 */
package es.gob.afirma.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * <p>Class that provides functionality to control the close of resources.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 13/01/2014.
 */
public final class UtilsResources {

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(UtilsResources.class);

    /**
     * Constructor method for the class UtilsResources.java.
     */
    private UtilsResources() {
    }

    /**
     * Method that handles the closing of a {@link InputStream} resource.
     * @param is Parameter that represents a {@link InputStream} resource.
     */
    public static void safeCloseInputStream(InputStream is) {
	if (is != null) {
	    try {
		is.close();
	    } catch (IOException e) {
		LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.UR_LOG001, new Object[ ] { is.getClass().getName() }), e);
	    }
	}
    }

    /**
     * Method that handles the closing of a {@link OutputStream} resource.
     * @param os Parameter that represents a {@link OutputStream} resource.
     */
    public static void safeCloseOutputStream(OutputStream os) {
	if (os != null) {
	    try {
		os.close();
	    } catch (IOException e) {
		LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.UR_LOG001, new Object[ ] { os.getClass().getName() }), e);
	    }
	}
    }

    /**
     * Method that handles the closing of a {@link Socket} resource.
     * @param socket Parameter that represents a {@link Socket} resource.
     */
    public static void safeCloseSocket(Socket socket) {
	if (socket != null) {
	    try {
		socket.close();
	    } catch (IOException e) {
		LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.UR_LOG001, new Object[ ] { socket.getClass().getName() }), e);
	    }
	}
    }

}

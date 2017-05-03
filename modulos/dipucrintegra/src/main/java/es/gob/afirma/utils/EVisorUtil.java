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

/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.utils.EVisorUtil.java.</p>
 * <b>Description:</b><p>Utilities for Evisor webservice.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>15/12/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 15/12/2011.
 */
package es.gob.afirma.utils;

import java.util.HashMap;
import java.util.Map;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.utils.EVisorConstants.EVisorTagsRequest;

/**
 * <p>Utilities for Evisor webservice.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 15/12/2011.
 */
public final class EVisorUtil {

    /**
     * Constructor method for the class EVisorUtil.java.
     */
    private EVisorUtil() {
    }

    /**
     * Creates a new instance of a {@link Map} with all values of a node <code>srsm:Barcode</code>.
     * @param message barcode message.
     * @param type type of barcode.
     * @param configParams barcode configuration parameters.
     * @return a new instance of a {@link Map} with all values of a node <code>srsm:Barcode</code>.
     */
    public static Map<String, Object> newBarcodeMap(String message, String type, Map<String, String> configParams) {
	if (GenericUtils.assertStringValue(message) && GenericUtils.assertStringValue(type)) {
	    final int NUMBER3 = 3;
	    // creamos el mapa que contiene los valores del objeto Barcode
	    Map<String, Object> barcode = new HashMap<String, Object>(NUMBER3);
	    // atributo mensaje
	    barcode.put(EVisorTagsRequest.BARCODE_MESSAGE, message);
	    // atributo tipo código de barras
	    barcode.put(EVisorTagsRequest.BARCODE_TYPE, type);

	    // creamos los parámetros de configuración del código de barra (si
	    // se indican).
	    Map<?, ?>[ ] barcodeParams = newParameterMap(configParams);
	    if (barcodeParams != null) {
		barcode.put(EVisorTagsRequest.BARCODE_CONFIGURATION_PARAM, barcodeParams);
	    }
	    return barcode;
	} else {
	    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.EVU_LOG001));
	}
    }

    /**
     * Creates a array with {@link Map} objects. Each {@link Map} contains two records:
     * <ul><li>One, with value of <code>srsm:ParameterId</code> parameter.</li>
     * <li>Other, with value of <code>srsm:ParameterValue</code> parameter.</li></ul>
     * @param configParams {@link Map} with parameters. Parameter name as key, and parameter value as value.
     * @return a array with {@link Map} objects.
     */
    public static Map<?, ?>[ ] newParameterMap(Map<String, String> configParams) {
	Map<?, ?>[ ] result = null;
	if (configParams != null && !configParams.isEmpty()) {
	    result = new HashMap<?, ?>[configParams.size()];
	    int i = 0;
	    for (String key: configParams.keySet()) {
		Map<String, String> mapTmp = new HashMap<String, String>(2);
		mapTmp.put(EVisorTagsRequest.PARAMETER_ID, key);
		mapTmp.put(EVisorTagsRequest.PARAMETER_VALUE, configParams.get(key));
		result[i] = mapTmp;
		i++;
	    }
	}
	return result;
    }
}

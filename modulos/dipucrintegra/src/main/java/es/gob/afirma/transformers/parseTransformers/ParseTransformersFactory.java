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

package es.gob.afirma.transformers.parseTransformers;

import java.util.Properties;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.transformers.TransformersProperties;
import es.gob.afirma.utils.GenericUtils;

/**
 * Esta clase implementa una f&aacute;brica de parseadores de respuestas retornadas por
 * los servicios publicados por la plataforma @Firma.
 * 
 * @author SEPAOT
 *
 */
public final class ParseTransformersFactory {

    /**
     * Constructor method for the class ParseTransformersFactory.java.
     */
    private ParseTransformersFactory() {
    }

    /**
     * Attribute that represents .
     */
    private static Logger logger = Logger.getLogger(ParseTransformersFactory.class);

    /**
     * Obtiene la clase encargada de parsear el par&aacute;metro de salida de un servicio de la plataforma @Firma.
     * @param serviceReq servicio solicitado.
     * @param method nombre del método del servicio solicitado
     * @param version versi&oacute; del servicio.
     * @return un objeto que implementa la interfaz IResponseTransformer encargado de parsear el par&aacute;metro de
     * salida retornado por un servicio de la plataforma @Firma.
     * @throws TransformersException si ocurre alg&uacute;n error obteniendo la clase que parsea un par&aacute;metro XML generado por
     * los servicios de la plataforma.
     */
    public static Class<Object> getParseTransformer(String serviceReq, String method, String version) throws TransformersException {
	boolean found;
	Class res, c;
	Class<Object>[ ] interfaces;
	int i;
	String transformerClass;

	res = null;

	try {
	    if (!GenericUtils.assertStringValue(serviceReq) || !GenericUtils.assertStringValue(method)) {
		throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.PTF_LOG001, new Object[ ] { serviceReq, version }));
	    }
	    transformerClass = getTransformerClassName(serviceReq, method, version);
	    if (transformerClass == null) {
		throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.PTF_LOG002, new Object[ ] { serviceReq, method, version }));
	    }
	    res = Class.forName(transformerClass);

	    interfaces = res.getInterfaces();
	    i = 0;
	    found = false;
	    while (i < interfaces.length && !found) {
		c = interfaces[i];
		if (c.getName().equals(IParseTransformer.class.getName())) {
		    found = true;
		}
		i++;
	    }

	    if (!found) {
		res = null;
		throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.PTF_LOG003, new Object[ ] { transformerClass, IParseTransformer.class.getName() }));
	    }
	    logger.debug(Language.getFormatResIntegra(ILogConstantKeys.PTF_LOG004, new Object[ ] { transformerClass }));
	} catch (ClassNotFoundException e) {
	    logger.error(e);
	    throw new TransformersException(e.getMessage(), e);
	}

	return res;
    }

    /**
     * getTransformerClassName.
     * @param serviceReq serviceReq
     * @param method method name
     * @param version version
     * @return String.
     */
    private static String getTransformerClassName(String serviceReq, String method, String version) {
	Properties properties;
	String res;

	properties = TransformersProperties.getMethodParseTransformersProperties(serviceReq, method, version);
	res = properties.getProperty(serviceReq + "." + method + "." + version + "." + TransformersConstants.PARSER_CTE + "." + TransformersConstants.TRANSFORMER_CLASS_CTE);

	return res;
    }
}

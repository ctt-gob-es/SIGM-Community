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

package es.gob.afirma.transformers.xmlTransformers;

import java.util.Properties;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.transformers.TransformersProperties;

/**
 * Esta clase forma implementa una f&aacute;brica de generadores de par&aacute;metros de entrada y salida
 * requeridos y generados, respectivamente, por los servicios publicados por la plataforma @Firma.
 * 
 * @author SEPAOT
 *
 */
public final class XmlTransformersFactory {

    /**
     * Constructor method for the class XmlTransformersFactory.java.
     */
    private XmlTransformersFactory() {
    }

    /**
     * Attribute that represents .
     */
    private static Logger logger = Logger.getLogger(XmlTransformersFactory.class);

    /**
     * Obtiene la clase encargada de formar el par&aacute;metro de entrada o salida para un servicio publicado por la plataforma @Firma.
     * @param serviceReq servicio solicitado.
     * @param method método del servicio solicitado.
     * @param type indica el tipo de par&aacute;metro a generar, de entrada (request) o de salida (response).
     * @param version versi&oacute; del servicio.
     * @return clase que implementa la interfaz IXmlTransformer encargado de formar el par&aacute;metro de
     * entrada o salida para un servicio publicado por la plataforma @Firma.
     * @throws TransformersException si ocurre alg&uacute;n error obteniendo la clase transformadora.
     */
    public static Class<?> getXmlTransformer(String serviceReq, String method, String type, String version) throws TransformersException {
	boolean found;
	Class<?> res, c;
	Class<?>[ ] interfaces;
	int i;
	String transformerClass;

	res = null;

	try {
	    if (serviceReq == null || method == null || version == null) {
		throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.XTF_LOG001, new Object[ ] { serviceReq, method, version }));
	    }
	    transformerClass = getTransformerClassName(serviceReq, method, type, version);
	    if (transformerClass == null) {
		throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.XTF_LOG002, new Object[ ] { serviceReq, method, version }));
	    }
	    res = (Class) Class.forName(transformerClass);

	    interfaces = res.getInterfaces();
	    i = 0;
	    found = false;
	    while (i < interfaces.length && !found) {
		c = interfaces[i];
		if (c.getName().equals(IXmlTransformer.class.getName())) {
		    found = true;
		}
		i++;
	    }

	    if (!found) {
		res = null;
		throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.XTF_LOG003, new Object[ ] { transformerClass, IXmlTransformer.class.getName() }));
	    }

	    logger.debug(Language.getFormatResIntegra(ILogConstantKeys.XTF_LOG004, new Object[ ] { transformerClass }));
	} catch (ClassNotFoundException e) {
	    logger.error(e);
	    throw new TransformersException(e.getMessage(), e);
	}

	return res;
    }

    /**
     * 
     * @param serviceReq serviceReq
     * @param type type
     * @param method method
     * @param version version
     * @return String
     */
    private static String getTransformerClassName(String serviceReq, String method, String type, String version) {
	Properties properties;
	String res;

	properties = new Properties();

	if (type.equals(TransformersConstants.REQUEST_CTE)) {
	    properties = TransformersProperties.getMethodRequestTransformersProperties(serviceReq, method, version);
	} else if (type.equals(TransformersConstants.RESPONSE_CTE)) {
	    properties = TransformersProperties.getMethodResponseTransformersProperties(serviceReq, method, version);
	}
	StringBuffer transfClassName = new StringBuffer(serviceReq).append(".");
	transfClassName.append(method).append(".");
	transfClassName.append(version).append(".");
	transfClassName.append(type).append(".");
	transfClassName.append(TransformersConstants.TRANSFORMER_CLASS_CTE);

	res = properties.getProperty(transfClassName.toString());

	return res;
    }
}

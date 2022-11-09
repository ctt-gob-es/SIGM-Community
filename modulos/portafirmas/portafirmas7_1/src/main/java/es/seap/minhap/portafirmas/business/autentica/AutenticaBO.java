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

package es.seap.minhap.portafirmas.business.autentica;

import java.util.Hashtable;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.xml.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import es.sag.autentica.xmlreader.XMLReader;
import es.seap.minhap.portafirmas.business.ws.Afirma5BO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.XMLUtil;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AutenticaBO {

	protected final Log log = LogFactory.getLog(getClass());

	@Resource(name="autenticaProperties")
	private Properties autenticaProperties;

	@Autowired
	private Afirma5BO afirma5BO;
	
	public String leerDniAutentica(String samlResponse) throws Exception {

		byte[] autenticaUserXml = leerAutenticaUserXml(samlResponse);
		String autenticaUserXmlString = new String(autenticaUserXml, Constants.CHARSET_ISO_8859_1);
		String idApp = autenticaProperties.getProperty("autentica.aplicacion.id");
		String rutaCer = Constants.PATH_CERTIFICADOS + autenticaProperties.getProperty("autentica.path.cert");
		// Por si cambian los datos del certificado usamos el fichero de propiedades 
		// con el valor de CN, esto permitiría cambiarlo sin modificar código
		String CN_valor = autenticaProperties.getProperty("autentica.cn.firma");
		String [] CN = new String[1];
		CN[0] = CN_valor;
		// XMLReader recibe como parámetro el xml de respuesta
		// y un array con 1 o 2 CNs
		XMLReader xmlReader = new XMLReader(autenticaUserXmlString, CN, idApp, rutaCer);
		Hashtable hashtable = xmlReader.processResponseTags();

		String dni = (String) hashtable.get(Constants.AUTENTICA_ID);
		log.debug("Acceso con Autentica de dni: " + dni);
		return dni;
	}

	public void validarRespuestaAutentica(String samlResponse) throws Exception {
		if (StringUtils.isBlank(samlResponse)) {
			throw new BadCredentialsException( "No se obtuvo respuesta correcta de Autentica." );
		}
		
		byte[] firma = leerAutenticaUserXml(samlResponse);
		if (!afirma5BO.validarFirma(firma)) {
			throw new BadCredentialsException("No se pudo validar la respuesta de Autentica.");
		}
		
		String resultadoResponse = XMLUtil.getNodeTextContent(firma, Constants.AUTENTICA_SAML_RESULTADO);
		if(!"OK".equals(resultadoResponse)) {
			throw new BadCredentialsException("Autentica indica que no encuentra el usuario.");
		}
	}

	private byte[] leerAutenticaUserXml(String samlResponseBase64) throws Exception {

		byte[] samlResponseDecoded = Base64.decode(samlResponseBase64);

		// Se obtiene el contenido del nodo que nos interesa. Llega codificado en base 64
		String userXmlBase64 = XMLUtil.getNodeTextContent(samlResponseDecoded, Constants.AUTENTICA_USER_ATTRIBUTE_VALUE);

		// La respuesta SAML está en UTF-8 mientrás que AUTENTICA_USER_XML está en ISO-8859-1
		byte[] userXmlDecoded = new String(Base64.decode(userXmlBase64), Constants.CHARSET_UTF_8).getBytes(Constants.CHARSET_ISO_8859_1);
		
		return userXmlDecoded;
	}
	
}

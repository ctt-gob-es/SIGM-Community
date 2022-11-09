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

package es.seap.minhap.portafirmas.utils.facturae;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.XMLUtil;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class FacturaeBO {
	protected final Log log = LogFactory.getLog(getClass());
	
	public byte[] generateFacturae (byte[] doc) throws FileNotFoundException, TransformerException {
		
		InputStream document = new ByteArrayInputStream(doc);
		
		TransformerFactory factory = TransformerFactory.newInstance();
		
		String version = XMLUtil.checkVersionFacturae(doc);
		// Get file path
		String pathTemplate = FacturaeBO.class.getClassLoader().getResource("visorFace3.2.1.xsl").toString();
		if (version != null){
			try {
				pathTemplate = FacturaeBO.class.getClassLoader().getResource("visorFace"+ version +".xsl").toString();
			} catch (Exception e) {
				log.warn("Se ha producido un error al recuperar la versión de la facturae");
			}
		}
		
		if (pathTemplate != null && !pathTemplate.equals("")) {
			// delete "file:/" string get from getResource method
			pathTemplate = pathTemplate.replaceAll("file:/", "");
			// replace %20 with space
			pathTemplate = pathTemplate.replaceAll("%20", " ");
			// add / at first to be recognized at linux
			pathTemplate = "/" + pathTemplate;
		}
		
		Source xslt = new StreamSource(new FileInputStream (pathTemplate));
		Transformer transformer = factory.newTransformer(xslt);
		    
		Source text = new StreamSource (document);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		transformer.transform(text, new StreamResult(output));

	    //Source text = new StreamSource(new ByteArrayInputStream (osDocument.toByteArray()));
		return output.toByteArray();
		
	}

}

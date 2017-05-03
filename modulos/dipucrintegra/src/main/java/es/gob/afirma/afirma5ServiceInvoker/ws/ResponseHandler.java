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
 * <b>File:</b><p>es.gob.afirma.afirma5ServiceInvoker.ws.ResponseHandler.java.</p>
 * <b>Description:</b><p>Class that represents handler used to verify the signature response.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>03/10/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 23/03/2011.
 */
package es.gob.afirma.afirma5ServiceInvoker.ws;

import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.log4j.Logger;
import org.apache.ws.security.components.crypto.CryptoType;
import org.apache.ws.security.components.crypto.CryptoType.TYPE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.signature.xades.IdRegister;
import es.gob.afirma.signature.xades.XadesSigner;
import es.gob.afirma.utils.UtilsXML;

/**
 * <p>Class that represents handler used to verify the signature response.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 28/09/2011.
 */
public class ResponseHandler extends AbstractCommonHandler {

    /**
     * Attribute that represents serial version identificator.
     */
    private static final long serialVersionUID = 3921688388842794471L;
    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(ResponseHandler.class);

    /**
     * Constructor method for the class CopyOfClientHandler.java.
     */
    public ResponseHandler() {
    }

    /**
     * Constructor method for the class ResponseHandler.java.
     * @param keystorePath keystore path.
     * @param keystorePass keystore password.
     * @param keystoreType keystore type.
     * @param autUser alias of certificate stored in keystore.
     * @param autPassword password of certificate (private key).
     */
    public ResponseHandler(String keystorePath, String keystorePass, String keystoreType, String autUser, String autPassword) {
	setUserKeystore(keystorePath);
	setUserKeystorePass(keystorePass);
	setUserKeystoreType(keystoreType);
	setUserAlias(autUser);
	setPassword(autPassword);
    }

    /**
     * {@inheritDoc}
     * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
     */
    @Override
    public final void invoke(MessageContext msgContext) throws AxisFault {
	Message msg;
	Document doc = null;
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.RH_LOG001));
	try {
	    org.apache.xml.security.Init.init();
	    // Obtención del documento XML que representa la petición SOAP
	    msg = msgContext.getCurrentMessage();

	    // Obtenemos los bytes correspondientes al mensaje SOAP recibido
	    ByteArrayInputStream bais = new ByteArrayInputStream(msg.getSOAPPartAsBytes());
	    // convertimos en un objeto Document (DOM)
	    doc = UtilsXML.getDocument(bais);
	    // Obtenemos el objeto signature
	    Element sigElement = null;
	    NodeList nl = doc.getElementsByTagNameNS(XadesSigner.DSIGNNS, XadesSigner.SIGNATURE_NODE_NAME);
	    if (nl.getLength() > 0) {
		sigElement = (Element) nl.item(0);
	    } else {
		throw new AxisFault(Language.getResIntegra(ILogConstantKeys.RH_LOG002));
	    }
	    // creamos un manejador de la firma (para validarlo) a partir del
	    // xml de la firma
	    org.apache.xml.security.signature.XMLSignature signature = new org.apache.xml.security.signature.XMLSignature(sigElement, "");
	    IdRegister.registerElements(doc.getDocumentElement());
	    // Obtenemos la clave pública usada en el servidor para las
	    // respuestas a partir del almacén de certificados.
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.RH_LOG003, new Object[ ] { getUserAlias() }));
	    CryptoType aliasCertificate = new CryptoType(TYPE.ALIAS);
	    aliasCertificate.setAlias(getUserAlias());
	    X509Certificate[ ] certificates = getCryptoInstance().getX509Certificates(aliasCertificate);
	    if (certificates != null && certificates.length > 0) {
		X509Certificate certificate = certificates[0];
		if (signature.checkSignatureValue(certificate)) {
		    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.RH_LOG004));
		} else {
		    throw new AxisFault(Language.getFormatResIntegra(ILogConstantKeys.RH_LOG005, new Object[ ] { certificate.getSubjectDN(), certificate.getSerialNumber() }));
		}
	    } else {
		throw new AxisFault(Language.getResIntegra(ILogConstantKeys.RH_LOG006));
	    }
	} catch (Exception e) {
	    throw AxisFault.makeFault(e);
	}
    }

}

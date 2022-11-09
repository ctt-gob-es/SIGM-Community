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

package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.impl;


import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;


import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.AnyType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DocumentType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.InputDocuments;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ResponseBaseType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ReturnUpdatedSignature;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.SignatureObject;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.SignaturePtr;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.UpdatedSignatureType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.VerifyRequest;
//import es.mpt.dsic.inside.afirma.ws.client.AfirmaConstantes;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.DSSSignerProcessor;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.DSSUtil;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.constantes.DSSTiposFirmaConstantes;

/**
 * Procesa las peticiones a los servicios DSS de afirma para firmas XMLDSig Enveloped y Detached
 * @author rus
 *
 */
public class XMLDSSSignerProcessor extends DSSSignerProcessor{

	
	@Override
	public void fillSignatureObjectAndInputDocuments(VerifyRequest vf,
			byte[] sign) {
		
		
		vf.setInputDocuments(buildInputDocuments(sign));
		vf.setSignatureObject(buildSignatureObject());
		
		vf.getSignatureObject().getSignaturePtr().setWhichDocument(vf.getInputDocuments().getDocumentOrTransformedDataOrDocumentHash().get(0));
		
	}

	@Override
	public void fillReturnUpdatedSignature (AnyType at, final String upgradeFormat) {

		ReturnUpdatedSignature returnUpdatedSignature = of_oasis_core.createReturnUpdatedSignature();

		// Se genera el nodo en base a la ampliaci�n solicitada
		if (Afirma5Constantes.UPGRADE_TIMESTAMP.equals(upgradeFormat)) {
			returnUpdatedSignature.setType(DSSTiposFirmaConstantes.DSS_SIGNATURE_MODE_T);
		} else {
			returnUpdatedSignature.setType(upgradeFormat);
		}

		at.getAny().add(returnUpdatedSignature);
	}

	@Override
	public byte[] getUpgradedSignature(ResponseBaseType verifyResponse) {

		// Se obtiene el nodo DocumentWithSignature //
		UpdatedSignatureType updatedSignatureType = (UpdatedSignatureType) 
					DSSUtil.getObjectByClass(UpdatedSignatureType.class, verifyResponse.getOptionalOutputs().getAny());

		// En las firmas XADES DETACHED y ENVELOPED la firma actualizada se almacena en DocumentWithSignature
		// y se accede mediante un puntero que se define dentro del nodo UpdatedSignatureType
		SignaturePtr signaturePtr = updatedSignatureType.getSignatureObject().getSignaturePtr();
		DocumentType document = (DocumentType) signaturePtr.getWhichDocument();

		return document.getBase64XML();
	}

	/*@Override
	public ContenidoFirmado getSignedData(ResponseBaseType verifyResponse, final byte[] sign) throws ContentNotExtractedException {
		
		ContenidoFirmado contenidoFirmado = null;
		
		SignedDataInfo signedDataInfo = (SignedDataInfo) DSSUtil.getObjectByClass(SignedDataInfo.class, verifyResponse.getOptionalOutputs().getAny());
		
		// Obtengo los objetos SignedDataRefType para buscar información sobre el contenido firmado.
		if (signedDataInfo != null) {
			contenidoFirmado = new ContenidoFirmado ();
			
			
			List<SignedDataRefType> signedDataRefList = DSSUtil.getSignedDataRefListFromSignedDataInfo(signedDataInfo);
			
			
			int i = 0;
			SignedDataRefType signedDataRefConEncoding = null;
			
			// Nos quedamos con el que tenga un elemento "Encoding" dentro
			while (i < signedDataRefList.size() && signedDataRefConEncoding == null) {				
				if (signedDataRefList.get(i).getEncoding() != null) {
					signedDataRefConEncoding = signedDataRefList.get(i);					
				}
				i++;
			}
			
			if (signedDataRefConEncoding == null) {
				throw new ContentNotExtractedException ("No se ha encontrado ningun nodo SignedDataRef que contenga el elemento Encoding");
			}
			
			// Obtenemos la expresión xpath del nodo que tiene el contenido firmado.
			String xpathExpr = DSSUtil.expresionXpathValida(signedDataRefConEncoding.getXPath());
			

			
			//Node nodoContenido = null;
			try {
				// Obtenemos la estructura DOM del XML de la firma
				Document doc = XMLUtil.getDOMDocument(sign, false);
				
				contenidoFirmado = new ContenidoFirmado(); 
				// Si la expresión xpath es "//*" se trata de una firma XAdES Enveloped
				if (xpathExpr.contentEquals("//*")) {
					contenidoFirmado = getContenidoFirmadoEnveloped(doc);					
				// Si no, se trata de XAdES detached
				} else {
					contenidoFirmado = getContenidoFirmadoDetached(doc, xpathExpr, signedDataRefConEncoding.getEncoding());
				}
			
			} catch (Exception e) {
				throw new ContentNotExtractedException ("No se puede obtener el nodo que contiene el contenido firmado" + e.getMessage(), e);
			}
					}

		if (contenidoFirmado == null) {
			throw new ContentNotExtractedException ("No existe el nodo SignedDataInfo");
		}
		
		return contenidoFirmado;
		
	}*/
	
	/**
	 * Para obtener el contenido firmado elimina los nodos "Signature"
	 * @param doc
	 * @return
	 * @throws XPathFactoryConfigurationException
	 * @throws XPathExpressionException
	 * @throws UnsupportedEncodingException
	 * @throws TransformerException
	 */
	/*protected ContenidoFirmado getContenidoFirmadoEnveloped (Document doc) throws XPathFactoryConfigurationException, XPathExpressionException, UnsupportedEncodingException, TransformerException {
		// Buscamos los nodos SignatureValue
		String xpathExpression = "//*//*Signature";
			
		
		// Los eliminamos
		XMLUtil.removeNodesFromDocument(xpathExpression, doc);
				
		// Convertimos el documento a array de bytes.
		byte[] bytesDocumento = XMLUtil.getBytesFromNode(doc, doc.getInputEncoding()).toByteArray();
		ContenidoFirmado contenidoFirmado = new ContenidoFirmado ();
		contenidoFirmado.setBytesDocumento(bytesDocumento);		
		contenidoFirmado.setMimeDocumento("application/xml");
		 
		return contenidoFirmado;	
	}*/
	
	/**
	 * Para obtener el contenido firmado obtenemos el contenido del nodo que cumpla la expresión XPATH.
	 * Si es XML, formamos un nuevo XML (con la cabecera), y si no, obtenemos los bytes y su mime.
	 * @param doc
	 * @param xpathExpression
	 * @param encodingSignedData
	 * @return
	 * @throws XPathFactoryConfigurationException
	 * @throws XPathExpressionException
	 * @throws CharacterCodingException
	 * @throws TransformerException 
	 * @throws UnsupportedEncodingException 
	 */
	/*protected ContenidoFirmado getContenidoFirmadoDetached (Document doc, String xpathExpression, String encodingSignedData) throws XPathFactoryConfigurationException, XPathExpressionException, CharacterCodingException, TransformerException, UnsupportedEncodingException {
		// En este caso obtenemos el nodoContenido para obtener el contenido del interior.
		Node nodoContenido = XMLUtil.getNodeByXpathExpression(doc, xpathExpression);
		
		ContenidoFirmado contenidoFirmado = new ContenidoFirmado();
		
		// Obtenemos el contenido de ese nodo.
		// Si es hash
		if (XMLUtil.isHashMimeType(nodoContenido)) {
			contenidoFirmado.setHash(MiscUtil.decodeByEncoding(nodoContenido.getTextContent(), encodingSignedData));
			contenidoFirmado.setAlgoritmoHash(XMLUtil.getHashAlgorithm(nodoContenido));
		// Si no es hash
		} else {
			// Si lo que se ha firmado es un XML
			if (XMLUtil.isXMLMimeType(nodoContenido)) {
				//String xml = XMLUtil.getStringFromNode(nodoContenido.getFirstChild(), encodingSignedData);
				byte[] bytesDocumento = XMLUtil.getBytesFromNode(nodoContenido.getFirstChild(), encodingSignedData).toByteArray();
				contenidoFirmado.setBytesDocumento(bytesDocumento);
				contenidoFirmado.setMimeDocumento("application/xml");
			// Si no lo es
			} else {
				byte[] bytesDocumento = MiscUtil.decodeByEncoding(nodoContenido.getTextContent(), encodingSignedData);
				contenidoFirmado.setBytesDocumento(bytesDocumento);
				contenidoFirmado.setMimeDocumento(MimeUtil.getMimeNotNull(bytesDocumento));
			}
		}
		
		return contenidoFirmado;
	}*/
	
	protected static InputDocuments buildInputDocuments (byte[] sign) {
		InputDocuments id = of_oasis_core.createInputDocuments();
		
		DocumentType dt = of_oasis_core.createDocumentType();
		dt.setID("ID_DOCUMENTO");
		
		dt.setBase64XML(sign);
		id.getDocumentOrTransformedDataOrDocumentHash().add(dt);
		return id;
		
	}
	protected static SignatureObject buildSignatureObject () {
		SignatureObject so = of_oasis_core.createSignatureObject();
		so.setSignaturePtr(buildSignaturePtr());
		return so;
	}
	protected static SignaturePtr buildSignaturePtr () {
		SignaturePtr sptr = of_oasis_core.createSignaturePtr();		
		return sptr;
	}
	
	
	

}

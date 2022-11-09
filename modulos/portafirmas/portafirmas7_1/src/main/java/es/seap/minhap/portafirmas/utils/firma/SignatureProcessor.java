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

package es.seap.minhap.portafirmas.utils.firma;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.FileCopyUtils;

import es.gob.afirma.core.AOException;
import es.gob.afirma.core.AOInvalidFormatException;
import es.gob.afirma.core.AOUnsupportedSignFormatException;
import es.gob.afirma.core.misc.MimeHelper;
import es.gob.afirma.core.signers.AOSignInfo;
import es.gob.afirma.core.signers.AOSigner;
import es.gob.afirma.core.signers.AOSignerFactory;
import es.gob.afirma.core.signers.AOSimpleSignInfo;
import es.gob.afirma.core.util.tree.AOTreeModel;
import es.gob.afirma.core.util.tree.AOTreeNode;


/**
 * Clase copiada del proyecto eeutil
 * @author rus
 *
 */
public class SignatureProcessor {

    /** Firma que deseamos visualizar. */
    private byte[] signatureData;
    
    /** Manejador de firma compatible con el objeto de firma analizado. */
    private AOSigner signer;
    
    /** Informaci&oacute;n general obtenida de la firma. */
    private AOSignInfo signInfo;
    
    /** Informaci&oacute;n de los datos contenidos en la firma. */
    private AOSignedDataInfo dataInfo;
    
    /** &Aacute;rbol de firmas. */
    private AOTreeModel signTree;
    
    /**
     * Procesa un objeto de firma electr&oacute;nica en uno de los formatos de firma soportados
     * para extraer sus caracter&iacute;sticas y los datos firmados.
     * @param signature Firma que se desea analizar.
     * @throws AOUnsupportedSignFormatException Cuando el formato de firma no est&aacute; soportado.
     * @throws AOException Cuando ocurre un error al analizar el objeto de firma.
     * @throws IOException 
     */
	public void processSign(byte[] signature)  throws AOUnsupportedSignFormatException, AOException, IOException {
        
    	// Almacenamos la propia firma
        this.signatureData = signature;
    	
        // Comprobaciones de seguridad
        if(signature == null || signature.length == 0)
            throw new NullPointerException("No se han introducido datos de firma");
        
        // Obtenemos un manejador de firma compatible
        
        //TODO: Comprobar esto
        this.signer = AOSignerFactory.getSigner(signature);
        
        if(this.signer == null) {
        	throw new AOUnsupportedSignFormatException("La firma proporcionada no se corresponde con ning\u00FAn formato reconocido");
        }
        
        // Obtenemos los datos generales de la firma y los datos que se firmaron
        byte[] signedData = null;
        this.signInfo = this.signer.getSignInfo(signature);
        signedData = this.signer.getData(signature);

        // Analizamos los datos firmados
        if(signedData != null) {
        	this.dataInfo = new AOSignedDataInfo ();
        	//TODO: Comprobar que el Mime no hace falta
        	MimeHelper mimeHelper = new MimeHelper(signature);
        	this.dataInfo.setDataMimeType(mimeHelper.getMimeType());
        	this.dataInfo.setData(signedData);
        }
    }
    
    /**
     * Genera un &aacute;rbol con la misma estructura que el &aacute;rbol de firmas
     * de la firma procesada. Cada nodo distinto del ra&iacute;z contendr&aacute; la
     * informaci&oacute;n de una firma simple de la firma electr&oacute;nica manteniendo
     * la misma estructura conceptual que esta.
     * @return &Aacute;rbol de firmas.
     * @throws IOException 
     * @throws AOInvalidFormatException 
     */
    public AOTreeModel generateCertificatesTree() throws AOInvalidFormatException, IOException {

    	// Si todavia no hemos obtenido el arbol de firmas, lo obtenemos ahora
    	// y lo guardamos
    	if(this.signTree == null) {
				this.signTree = this.signer.getSignersStructure(this.signatureData, true);
    	}

    	return this.signTree;
    }
  
    /**
     * Recupera un array formado por el XOR de los PKCS1 de las distinas firmas simples de
     * la firma electr&oacute;nica.
     * @return Array resultante. 
     * @throws IOException 
     * @throws AOInvalidFormatException 
     */
    public byte[] signatureValue() throws AOInvalidFormatException, IOException {
    	
    	if(this.signTree == null) {
    		generateCertificatesTree();
    	}
    	
    	Vector<byte[]> result = new Vector<byte[]>();
    	getPKCS1Values(result, (AOTreeNode)this.signTree.getRoot());
    	
    	if(result.size() == 0) return null;
    	else if(result.size() == 1) return result.elementAt(0);
    	else return MathUtils.xorArrays(result);
    }
    
    /**
     * Recupera un listado con los PKCS#1 de las firmas simples de una firma electr&oacute;nica.
     */
    private void getPKCS1Values(Vector<byte[]> values, AOTreeNode node) {
    	
  		//Object object = ((DefaultMutableTreeNode)node).getUserObject();
  		Object object = ((AOTreeNode)node).getUserObject();
  		if(object instanceof AOSimpleSignInfo) {
  			
  			// Comprobamos si ya tenemos este certificado
  			byte[] pkcs1 = ((AOSimpleSignInfo)object).getPkcs1();
  			if(pkcs1 != null)
  				values.add(pkcs1);
  		}
  		
  		// Procesamos los nodos hijos
  		for(int i=0; i<node.getChildCount(); i++) {
  			getPKCS1Values(values, (AOTreeNode)node.getChildAt(i));
  		}
  	}
    
    /**
     * Recupera la informacion de los datos firmados. Si no se ha realizado el an&aacute;lisis de
     * la firma o esta no contenida datos, se devolvera <code>null</code>.
     * @return Informaci&oacute;n de los datos firmados.
     */
    public AOSignedDataInfo getDataInfo() {
        return this.dataInfo;
    }
    
    /**
     * Recupera la informaci&oacute;n de firma. Si no se ha realizado el an&aacute;lisis de
     * la firma, se devolvera <code>null</code>.
     * @return Informaci&oacute;n de la firma.
     */
    public AOSignInfo getSignInfo() {
        return this.signInfo;
    }
    
    /**
     * Recupera la firma que se est&aacute; procesando.
     * @return Firma electr&oacute;nica.
     */
    public byte[] getSign() {
    	return this.signatureData;
    }    
    
    /*public SignatureProcessorResponseDocument transformXML(){
    	SignatureProcessorResponseDocument response = null;
    	try{
		    response = SignatureProcessorResponseDocument.Factory.newInstance();
		    final SignatureProcessorResponseType signatureProcessorResponseType = response.addNewSignatureProcessorResponse();
		    signatureProcessorResponseType.setError(00);
		    signatureProcessorResponseType.setErrorMessage("");
		    final ResultType result = signatureProcessorResponseType.addNewResult();
		    final SignedDataInfoType signedDataInfoXML = result.addNewSignedDataInfo();
		    transform(signedDataInfoXML, dataInfo);
		    final SignInfoType signInfoXML = result.addNewSignInfo();
		    transform(signInfoXML, signInfo);		    
		    final SignTreeType signTreeXML = result.addNewSignTree();
		    final Object root = generateCertificatesTree().getRoot();
		    transform(signTreeXML, (TreeNode) root);
    	}catch (Exception ex) {
    		ex.printStackTrace();
			if (response != null)
			{	response.getSignatureProcessorResponse().setError(99);
				response.getSignatureProcessorResponse().setErrorMessage(ex.getMessage());				
			}
		}    	
    	
    	return response;
    }
*/
    /*
	private void transform(SignTreeType signTreeXML, TreeNode signTreeIn) {
		final TreeSigners treeSigners = signTreeXML.addNewTreeSigners();		
		for(int i=0; i<signTreeIn.getChildCount(); i++){
			final TreeSignerInfoType newSigner = treeSigners.addNewSigner();
			transform((TreeNode) signTreeIn.getChildAt(i),newSigner);
		}
	}*/

    /*
	private void transform(TreeNode node, TreeSignerInfoType newSigner) {
		//if( ((DefaultMutableTreeNode)node).getUserObject() instanceof AOSimpleSignInfo) {
		if( ((TreeNode)node).getUserObject() instanceof AOSimpleSignInfo) {
			//final AOSimpleSignInfo aoSimpleSignInfo = (AOSimpleSignInfo) ((DefaultMutableTreeNode)node).getUserObject();
			final AOSimpleSignInfo aoSimpleSignInfo = (AOSimpleSignInfo) ((TreeNode)node).getUserObject();
			newSigner.setSignAlgorithm(aoSimpleSignInfo.getSignAlgorithm());
			newSigner.setSignFormat(aoSimpleSignInfo.getSignFormat());			
			final Calendar calendarSigningTime=Calendar.getInstance();
			calendarSigningTime.setTime(aoSimpleSignInfo.getSigningTime());
			newSigner.setSigningTime(calendarSigningTime);
			final Date[] calendar = aoSimpleSignInfo.getTimestampingTime();			
			final TimestampingTime newTimestampingTime = newSigner.addNewTimestampingTime();
			if (calendar != null)
			{
				for (int j = 0; j < calendar.length; j++) {
					final XmlDate newTime = newTimestampingTime.addNewTime();
					newTime.setDateValue(calendar[j]);
				}		
			}
			newSigner.setPkcs1(aoSimpleSignInfo.getPkcs1());
			final Certs certs = newSigner.addNewCerts();			
			transform(certs, aoSimpleSignInfo.getCerts());
			final TreeSignerInfo newTreeSignerInfo = newSigner.addNewTreeSignerInfo();		
			for(int j=0; j<node.getChildCount(); j++) {
				final TreeSignerInfoType newSignTree = newTreeSignerInfo.addNewSignTree();
				transform((TreeNode) node.getChildAt(j), newSignTree);
			}			
			//newSigner.setPolicyInfo(aoSimpleSignInfo.);
		}
	}

	private void transform(Certs certs, X509Certificate[] certsIn) {
		if (certsIn != null){
			for (int i = 0; i < certsIn.length; i++) {
				final X509Certificate certificate = certsIn[i];
				final X509CertificateType newCertificate = certs.addNewCertificate();
				newCertificate.setVersion(certificate.getVersion());
				if (certificate.getIssuerDN()!= null)
					newCertificate.setIssuerDN(certificate.getIssuerDN().getName());
				transform(newCertificate.addNewIssuerX500Principal(), certificate.getIssuerX500Principal());
				if (certificate.getSubjectDN()!= null)
				newCertificate.setSubjectDN(certificate.getSubjectDN().getName());
				transform(newCertificate.addNewSubjectX500OPrincipal(), certificate.getSubjectX500Principal());
				if (certificate.getNotBefore()!= null){			
					final Calendar calendarNotBefore=Calendar.getInstance();
					calendarNotBefore.setTime(certificate.getNotBefore());
					newCertificate.setNotBefore(calendarNotBefore);
				}
				if (certificate.getNotAfter()!= null){			
					final Calendar calendarNotAfter=Calendar.getInstance();
					calendarNotAfter.setTime(certificate.getNotAfter());
					newCertificate.setNotAfter(calendarNotAfter);
				}
				newCertificate.setSignature(certificate.getSignature());
				newCertificate.setSigAlgName(certificate.getSigAlgName());
				newCertificate.setSigAlgOID(certificate.getSigAlgOID());
				newCertificate.setSigAlgParams(certificate.getSigAlgParams());
			}
		}		
	}

	private void transform(X500PrincipalType newIssuerX500Principal, X500Principal issuerX500Principal) {
		newIssuerX500Principal.setName(issuerX500Principal.getName());
		newIssuerX500Principal.setEncoded(issuerX500Principal.getEncoded());
	}

	private void transform(SignedDataInfoType signedDataInfoXML, AOSignedDataInfo dataInfoIn) {
		signedDataInfoXML.setDataDescription(dataInfoIn.getDataDescription());
		signedDataInfoXML.setDataExtension(dataInfoIn.getDataExtension());
		signedDataInfoXML.setDataMimeType(dataInfoIn.getDataMimeType());
		signedDataInfoXML.setData(dataInfoIn.getData());
	}

	private void transform(SignInfoType signInfoXML, AOSignInfo signInfoIn) {
		signInfoXML.setFormat(signInfoIn.getFormat());
		signInfoXML.setB64VerificationCode(signInfoIn.getB64VerificationCode());
		signInfoXML.setVariant(signInfoIn.getVariant());
	}*/
		
	public byte[] generateB64VerificationCode() throws AOInvalidFormatException, IOException{
		final Vector<byte[]> vector = new Vector<byte[]>();
		final AOTreeNode root = (AOTreeNode)generateCertificatesTree().getRoot();
		generateB64VerificationCode(root, vector);
		return MathUtils.xorArrays(vector);
	}

	private void generateB64VerificationCode(AOTreeNode root, Vector<byte[]> vector) {
  		for(int i=0; i<root.getChildCount(); i++) {
  			getPKCS1Values(vector, (AOTreeNode)root.getChildAt(i));
  		}
	}
	
	/*public static byte[] getBytes(InputStream is) throws IOException {
		is.reset();
		return IOUtils.toByteArray(is);
	  }*/
	
	public static InputStream getInputStream(byte[] ba) throws IOException {
	
		return  new ByteArrayInputStream(ba);
	}
	
public byte[] fromBase64StringToByteArray(String s) {
		
		return Base64.decodeBase64(s);
	}
	
	public String fromByteArrayToBase64(byte[] s) {
		
		return Base64.encodeBase64String(s);
	}
	
	public byte[] fromInputStreamToByteArray(InputStream is) {
		
		byte[] copyToByteArray=null;
		try {
			if (is.markSupported()==true)
				is.reset();
			copyToByteArray = FileCopyUtils.copyToByteArray(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return copyToByteArray;
	}
	
	
	
	public String fromInputStreamToBase64String(InputStream is) {
		byte[] fromInputStreamToByteArray = fromInputStreamToByteArray(is);
		return fromByteArrayToBase64(fromInputStreamToByteArray);
	}

}

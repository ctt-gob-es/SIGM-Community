package es.atm2;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.axis.message.MessageElement;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.api.rule.procedures.certificadosPadron.PadronUtils;

public class TestPadron {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		//Esto sólo es necesario en mi máquina, al estar en una 172
//		System.getProperties().setProperty("http.proxySet", "true");
//		System.getProperties().setProperty("http.proxyHost", "proxy.dipucr.es");
//		System.getProperties().setProperty("http.proxyPort", "8080");
		
		/**FERNAN CABALLERO**/
//		String codInstitucion = "130408"; //Cod.INE de F.Caballero
//		String documentoIdentidad = "05671215J";
//		String documentoIdentidad = "05677492B";
//		String documentoIdentidad = "05636486Z";
//		String documentoIdentidad = "05694855D";
//		String documentoIdentidad = "PTE13437869";
//		String documentoIdentidad = "X2183209A";
		
		/**LA SOLANA**/
//		String codInstitucion = "130795";
//		String documentoIdentidad = "71218679F";
//		String documentoIdentidad = "71215866T";
//		String documentoIdentidad = "70718644S";
//		String documentoIdentidad = "71219475K";
		
		/**BALLESTEROS**/
//		String codInstitucion = "130224";
//		String documentoIdentidad = "03928239T";
		
		/**ALCOLEA**/
		String codInstitucion = "130071";
		String documentoIdentidad = "05680278Z";
//		String documentoIdentidad = "05507048C";
		
		/**MIGUELTURRA**/
//		String codInstitucion = "130564";
//		String documentoIdentidad = "05630485Q";
		
		getNombrePersona(codInstitucion, documentoIdentidad);
		generarDocumentoPadron(codInstitucion, documentoIdentidad);
		
	}

	/**
	 * Generar documento del padrón
	 * @throws RemoteException
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void generarDocumentoPadron(String codInstitucion, String documentoIdentidad)
			throws RemoteException,	Exception, IOException, FileNotFoundException {
		
		PadronSoapProxy wsPadron = new PadronSoapProxy();
		MessageElement[] arrResponse = null;
		MessageElement msgElement = null;
		
		int tipoDocumento = PadronUtils.getTipoDocumento(documentoIdentidad);
		if (tipoDocumento == Constants.CERTPADRON._TIPO_DOC_NIE){
			documentoIdentidad = PadronUtils.retocarNIE(documentoIdentidad);
		}
		String strTipoDocumento = String.valueOf(tipoDocumento);
		
//		ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult documento = 
//				wsPadron.obtenerVolanteEmpadronamiento(codInstitucion, strTipoDocumento, documentoIdentidad);
//		ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult documento = 
//				wsPadron.obtenerCertificadoEmpadronamiento(codInstitucion, strTipoDocumento, documentoIdentidad);
		ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult documento = 
				wsPadron.obtenerVolanteConvivencia(codInstitucion, strTipoDocumento, documentoIdentidad);
//		ObtenerCertificadoConvivenciaResponseObtenerCertificadoConvivenciaResult documento =
//				wsPadron.obtenerCertificadoConvivencia(codInstitucion, strTipoDocumento, documentoIdentidad);
		
		arrResponse = documento.get_any();
		msgElement = arrResponse[0];
		System.out.println(msgElement);
		
		//Obtenemos el nodo documento
		Node nodoDocumento = null;
		NodeList listNodosDoc = msgElement.getElementsByTagName("Documento");
		if (listNodosDoc.getLength() == 0){
			//Se ha producido un error
			Node nodoError = msgElement.getElementsByTagName("MensajeError").item(0);
			String textoError = nodoError.getFirstChild().getFirstChild().getNodeValue();
			String codigoError = nodoError.getLastChild().getFirstChild().getNodeValue();
			throw new Exception(codigoError + ": " + textoError);
		}
		
		nodoDocumento = listNodosDoc.item(0);
		String strB64File = nodoDocumento.getFirstChild().getNodeValue();
		System.out.println(strB64File);
		
//		BASE64Decoder b64Decoder = new BASE64Decoder();
//		byte[] arrBytes = b64Decoder.decodeBuffer(strB64File);
		byte[] arrBytes = Base64.decodeBase64(strB64File);
		
		String strRuta = "C:\\home\\cert_padron.pdf";
		File fileCert = new File(strRuta); 
		FileOutputStream fileOuputStream = new FileOutputStream(strRuta); 
	    fileOuputStream.write(arrBytes);
	    fileOuputStream.close();

	    Desktop.getDesktop().open(fileCert);
	}
	
	
	/**
	 * Devuelve la información del usuario por su NIF, NIE o PTE
	 * @throws RemoteException
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void getNombrePersona(String codInstitucion, String documentoIdentidad)
			throws RemoteException,	Exception, IOException, FileNotFoundException {
		
		PadronSoapProxy wsPadron = new PadronSoapProxy();
		MessageElement[] arrResponse = null;
		MessageElement msgElement = null;
		
		int tipoDocumento = PadronUtils.getTipoDocumento(documentoIdentidad);
		if (tipoDocumento == Constants.CERTPADRON._TIPO_DOC_NIE){
			documentoIdentidad = PadronUtils.retocarNIE(documentoIdentidad);
		}

		String tipoFisica = Constants.CERTPADRON._TIPO_PERSONA_FISICA;
		ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult datosPersona =
				wsPadron.obtenerPersonaPorNIF(codInstitucion, tipoFisica, documentoIdentidad);
		
		arrResponse = datosPersona.get_any();
		msgElement = arrResponse[0];
		System.out.println(msgElement);
		
		//Obtenemos el nodo documento
		Node nodoPersona = null;
		NodeList listNodosDoc = msgElement.getElementsByTagName("Persona");
		if (listNodosDoc.getLength() == 0){
			//Se ha producido un error
			Node nodoError = msgElement.getElementsByTagName("MensajeError").item(0);
			String textoError = nodoError.getFirstChild().getFirstChild().getNodeValue();
			String codigoError = nodoError.getLastChild().getFirstChild().getNodeValue();
			throw new Exception(codigoError + ": " + textoError);
		}
		nodoPersona = listNodosDoc.item(0);
		
		Node nodo = nodoPersona.getFirstChild();
		String nombre, ape1, ape2;
		nombre = ape1 = ape2 = null;
		while ((nodo = nodo.getNextSibling()) != null){
			if (nodo.getNodeName() == "Nombre"){
				nombre = nodo.getFirstChild().getNodeValue();
			}
			else if (nodo.getNodeName() == "Apellido1"){
				ape1 = nodo.getFirstChild().getNodeValue();
			}
			else if (nodo.getNodeName() == "Apellido2"){
				if (nodo.getChildNodes().getLength() > 0){
					ape2 = nodo.getFirstChild().getNodeValue();
				}
			}
		}
		
		StringBuffer sbNombreCompleto = new StringBuffer();
		sbNombreCompleto.append(nombre);
		sbNombreCompleto.append(" ");
		sbNombreCompleto.append(ape1);
		if (StringUtils.isNotEmpty(ape2)){
			sbNombreCompleto.append(" ");
			sbNombreCompleto.append(ape2);
		}
		
		System.out.println(sbNombreCompleto.toString());
	}
	
}
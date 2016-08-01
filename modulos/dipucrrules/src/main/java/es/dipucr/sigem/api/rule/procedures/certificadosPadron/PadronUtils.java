package es.dipucr.sigem.api.rule.procedures.certificadosPadron;

import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.MessageElement;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult;
import es.atm2.PadronSoapProxy;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class PadronUtils {
	
	private static final Logger logger = Logger.getLogger(PadronUtils.class);

	/**
	 * Devuelve el tipo de documento en función del documento recibido
	 * @param numDocumento
	 * @return
	 */
	public static int getTipoDocumento(String numDocumento) {
		
		String clavePasaporte = Constants.CERTPADRON._INICIO_DOC_PASAPORTE;
		if (numDocumento.startsWith(clavePasaporte)){
			return Constants.CERTPADRON._TIPO_DOC_PASAPORTE;
		}
		else{
			char caracterInicial = numDocumento.charAt(0);
			if (Character.isDigit(caracterInicial)){
				return Constants.CERTPADRON._TIPO_DOC_NIF;
			}
			else{
				return Constants.CERTPADRON._TIPO_DOC_NIE;
			}
		}
	}
	
	/**
	 * Devuelve el hash de variables de la solicitud ce certificados del Padrón
	 * @param itemPadron
	 * @param itemExpediente
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getVariablesPadron(IItem itemPadron, IItem itemExpediente, String error) throws Exception {
		
		Map<String,String> variables = new HashMap<String,String>();
		variables.put("INTERESADO_NIF", itemExpediente.getString("NIFCIFTITULAR"));
		variables.put("INTERESADO_NOMBRE", itemExpediente.getString("IDENTIDADTITULAR"));
		variables.put("DESC_CERTIFICADO", itemPadron.getString("DESC_CERTIFICADO"));
		if (StringUtils.isNotEmpty(error)){
			variables.put("ERROR", error);
		}
		return variables;	
	}
	
	/**
	 * Devuelve la información del usuario por su NIF, NIE o PTE
	 * @throws RemoteException
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String getNombrePersona(String codInstitucion, String documentoIdentidad)
			throws Exception {
		
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
		logger.warn(msgElement);
		
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
				//Muchos extranjeros no tiene segundo apellido
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
		
		return sbNombreCompleto.toString();
	}
	
	
	/**
	 * Añade un 0 a la izquierda del NIE para seguir el formato del padrón de ATM
	 * X2183209A -> X02183209A 
	 * @param NIE
	 * @return NIE modificado
	 */
	public static String retocarNIE(String nie){
		
		String result = null;
		if (nie.length() == 9){
			result = nie.charAt(0) + "0" + nie.substring(1);
		}
		return result;
	}
}

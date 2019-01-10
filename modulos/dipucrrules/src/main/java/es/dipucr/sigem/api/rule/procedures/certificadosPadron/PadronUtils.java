package es.dipucr.sigem.api.rule.procedures.certificadosPadron;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ClsHabitanteResponse;
import org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ETipoDocumento;
import org.tempuri.IPadronProxy;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class PadronUtils {
	
//	private static final Logger LOGGER = Logger.getLogger(PadronUtils.class);
	public static final String COD_PROVINCIA_CR = "13";
	
	public static final int TIPO_CERTIFICADO = 1;
	public static final int TIPO_VOLANTE = 2;
	public static final int TIPO_CERTIFICADO_EMPADRONAMIENTO_HISTORICO = 3;
	public static final int TIPO_CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS = 4;
	public static final int TIPO_CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS_CON_CAMBIO_DOMICILIO = 5;
	public static final int TIPO_CERTIFICADO_CONVIVENCIA = 6;
	

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
	 * @throws  
	 * @throws RemoteException
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 * [dipucr-Felipe #515] Actualización servicios web padrón ATM
	 * [dipucr-Alberto #798]
	 */	
	public static String getNombrePersona(String codInstitucion, String documentoIdentidad) throws Exception {
		return getPersona(codInstitucion, documentoIdentidad).getNombre();
	}
	
	public static PersonaPadron getPersona(String codInstitucion, String documentoIdentidad) throws Exception {
		
		codInstitucion = retocarCodInstitucion(codInstitucion);
		
		int tipoDocumento = PadronUtils.getTipoDocumento(documentoIdentidad);
		if (tipoDocumento == Constants.CERTPADRON._TIPO_DOC_NIE){
			return getPersonaNie(codInstitucion, documentoIdentidad);
		}
		
		return getPersonaATM(codInstitucion, documentoIdentidad);
	}
	
	private static PersonaPadron getPersonaNie(String codInstitucion, String documentoIdentidad) throws Exception {

		String documentoIdentidadRetocado = PadronUtils.retocarNIE(documentoIdentidad);
		
		// Probamos con el NIE retocado (añadido el 0) y si no lo encuentra probamos con el NIE sin retocar
		try {
			return getPersonaATM(codInstitucion, documentoIdentidadRetocado);
		} catch (Exception e) {
			return getPersonaATM(codInstitucion, documentoIdentidad);
		}
	}

	private static PersonaPadron getPersonaATM(String codInstitucion, String documentoIdentidad) throws RemoteException, ISPACException {
		
		String nombre;
		IPadronProxy wsPadron = new IPadronProxy();
		
		ClsHabitanteResponse response = wsPadron.getNombrePersona(codInstitucion, documentoIdentidad);
		
		if (response.getCORRECTO()){			
			nombre = response.getHABITANTE();
			return new PersonaPadron(nombre, documentoIdentidad);
		}
		else{
			throw new ISPACException(PadronExceptions.getDescripcion(response.getCODIGO_RESULTADO()));
		}
	}
	
	
	/**
	 * [dipucr-Felipe #515]
	 * Cada elemento de la enumeración tiene asignado un ID en la tabla VLDTBL_CERT_PADRON
	 * @param tipoCert
	 * @return
	 * @throws ISPACException
	 */
	public static ETipoDocumento getEnumTipoCert(int tipoCert) throws ISPACException{
		
		ETipoDocumento enumTipoCert = null;
		switch (tipoCert) {
		case TIPO_CERTIFICADO:
			enumTipoCert = ETipoDocumento.CERTIFICADO;
			break;
		case TIPO_VOLANTE:
			enumTipoCert = ETipoDocumento.VOLANTE;
			break;
		case TIPO_CERTIFICADO_EMPADRONAMIENTO_HISTORICO:
			enumTipoCert = ETipoDocumento.CERTIFICADO_EMPADRONAMIENTO_HISTORICO;
			break;
		case TIPO_CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS:
			enumTipoCert = ETipoDocumento.CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS;
			break;
		case TIPO_CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS_CON_CAMBIO_DOMICILIO:
			enumTipoCert = ETipoDocumento.CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS_CON_CAMBIO_DOMICILIO;
			break;
		case TIPO_CERTIFICADO_CONVIVENCIA:
			enumTipoCert = ETipoDocumento.CERTIFICADO_CONVIVENCIA;
			break;
		default:
			throw new ISPACException("Tipo de certificado no reconocido");
		}
		return enumTipoCert;
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

	/**
	 * Adaptar código de institución INE a los webservices de ATM
	 * @param codInstitucion
	 * @return
	 */
	public static String retocarCodInstitucion(String codInstitucion) {
		
		return codInstitucion.substring(0,5);
	}
	
	/**
	 * Devuelve si el documento es de tipo certificado y se debe firmar
	 * @return
	 */
	public static boolean esTipoCertificado(int tipoCert){
		return (tipoCert != PadronUtils.TIPO_VOLANTE);
	}
}

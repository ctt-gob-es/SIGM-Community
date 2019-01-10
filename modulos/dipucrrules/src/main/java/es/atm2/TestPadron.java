package es.atm2;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ClsDocumentoResponse;
import org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ClsHabitanteResponse;
import org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS.ETipoDocumento;
import org.tempuri.IPadronProxy;

import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.api.rule.procedures.certificadosPadron.PadronUtils;

public class TestPadron {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		/**FERNAN CABALLERO**/
//		String codInstitucion = "130408"; //Cod.INE de F.Caballero
//		String documentoIdentidad = "05671215J";
//		String documentoIdentidad = "05677492B";
//		String documentoIdentidad = "05636486Z";
//		String documentoIdentidad = "05694855D";
//		String documentoIdentidad = "PTE13437869";
//		String documentoIdentidad = "X2183209A";
		
		/**LA SOLANA**/
		String codInstitucion = "130795";
		String documentoIdentidad = "71218679F";
//		String documentoIdentidad = "71215866T";
//		String documentoIdentidad = "70718644S";
//		String documentoIdentidad = "71219475K";
		
		/**BALLESTEROS**/
//		String codInstitucion = "130224";
//		String documentoIdentidad = "03928239T";
		
		/**ALCOLEA**/
//		String codInstitucion = "130071";
//		String documentoIdentidad = "05680278Z";
//		String documentoIdentidad = "05507048C";
		
		/**SOCUÉLLAMOS**/
//		String codInstitucion = "130782";
//		String documentoIdentidad = "70729684S";
		
		/**MIGUELTURRA**/
//		String codInstitucion = "130564";
//		String documentoIdentidad = "05630485Q";
		
		/**SANTA CRUZ DE MUDELA**/
//		String codInstitucion = "130776";
//		String documentoIdentidad = "05630485Q";
		
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
		
		IPadronProxy wsPadron = new IPadronProxy();
		
		int tipoDocumento = PadronUtils.getTipoDocumento(documentoIdentidad);
		if (tipoDocumento == Constants.CERTPADRON._TIPO_DOC_NIE){
			documentoIdentidad = PadronUtils.retocarNIE(documentoIdentidad);
		}
		
		ClsHabitanteResponse responseNombre = wsPadron.getNombrePersona(codInstitucion, documentoIdentidad);
		System.out.println(responseNombre.getHABITANTE());
		ClsDocumentoResponse responseDoc = wsPadron.getDocumento(codInstitucion, documentoIdentidad, 
				ETipoDocumento.CERTIFICADO_CONVIVENCIA, "", "", false);
		
		if (responseDoc.getCORRECTO()){
			
			String strRuta = "C:\\home\\cert_padron.pdf";
			File fileCert = new File(strRuta); 
			FileOutputStream fileOuputStream = new FileOutputStream(strRuta); 
		    fileOuputStream.write(responseDoc.getDOCUMENTO());
		    fileOuputStream.close();

		    Desktop.getDesktop().open(fileCert);
		}
		else{
			
			System.out.println(responseDoc.getCODIGO_RESULTADO());
		}
	}
	
	
}
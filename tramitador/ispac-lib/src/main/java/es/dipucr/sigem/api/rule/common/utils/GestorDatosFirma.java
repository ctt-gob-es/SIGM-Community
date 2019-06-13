package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.sign.ASN1Parser;
import ieci.tdw.ispac.ispaclib.sign.CamerfirmaCertificateParser;
import ieci.tdw.ispac.ispaclib.sign.DatosCompletosFirma;
import ieci.tdw.ispac.ispaclib.sign.FMNTCertificateParser;
import ieci.tdw.ispac.ispaclib.sign.FMNTEmpleadoPublicoCertificateParser;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.ServicioEstructuraOrganizativa;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;

import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;

public class GestorDatosFirma {

	public static final Logger LOGGER = Logger.getLogger(PdfFirmaUtils.class);
	protected static final int ENTITY_ID = 20;
	protected static final String ENTITY_NAME = "SPAC_DATOS_FIRMA";

	
	public static void storeDatosFirma(IClientContext cct, SignDocument signDocument, IItem itemStep) throws ISPACException {
		
		String usuario = cct.getResponsible().getName();
		
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			String idEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
			
			//Recuperamos el firmante del pdf
			InfoFirmante infofirmante = getUltimaFirma(cct, signDocument.getItemDoc(), idEntidad).getFirmante();
			
			IItem itemDatosFirma = entitiesAPI.createEntity(ENTITY_ID);
			itemDatosFirma.set("ID_DOCUMENTO", signDocument.getDocumentId());
			itemDatosFirma.set("USUARIO", usuario);
			itemDatosFirma.set("NIF", infofirmante.getDocIdentidadCertificado());
			itemDatosFirma.set("NOMBRE", infofirmante.getNombreFirmante());
			itemDatosFirma.set("CARGO", infofirmante.getCargo());
			itemDatosFirma.set("FECHA_FIRMA", new Date());
			if (null != itemStep){
				itemDatosFirma.set("ID_CIRCUITO", itemStep.getString("ID_CIRCUITO"));
				itemDatosFirma.set("ID_PASO", itemStep.getString("ID_PASO"));
			}
			itemDatosFirma.store(cct);
		}
		catch(Exception ex){
			String error = "Error al guardar los datos del firma del documento " + signDocument.getDocumentId() 
					+ " por el usuario " + usuario;
			LOGGER.error(error);
			throw new ISPACException(error, ex);
		}
	}
	
	
	public static void deleteDatosFirma(IClientContext cct, IItem itemDocument) throws ISPACException{
		
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			int idDocumento = itemDocument.getKeyInt();
			
			String query = "WHERE ID_DOCUMENTO = " + idDocumento;
			IItemCollection collection = entitiesAPI.queryEntities(ENTITY_NAME, query);
			@SuppressWarnings({ "unchecked" })
			List<IItem> listDatosFirma = collection.toList();
			
			for (IItem itemDatosFirma : listDatosFirma){
				itemDatosFirma.delete(cct);
			}
			
		}
		catch(Exception ex){
			String error = "Error al borrar los datos del firma del documento " + itemDocument.getKeyInt();
			LOGGER.error(error);
			throw new ISPACException(error, ex);
		}
	}

	public static void deleteDatosFirma(IClientContext cct, IItem itemDocument, int idPaso) throws ISPACException {

		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			int idDocumento = itemDocument.getKeyInt();
			
			String query = "WHERE ID_DOCUMENTO = " + idDocumento + " AND ID_PASO = " + idPaso;
			IItemCollection collection = entitiesAPI.queryEntities(ENTITY_NAME, query);
			@SuppressWarnings({ "unchecked" })
			List<IItem> listDatosFirma = collection.toList();
			
			for (IItem itemDatosFirma : listDatosFirma){
				itemDatosFirma.delete(cct);
			}
			
		}
		catch(Exception ex){
			String error = "Error al borrar los datos del firma del documento "
					+ itemDocument.getKeyInt() + " y paso " + idPaso;
			LOGGER.error(error);
			throw new ISPACException(error, ex);
		}
		
	}
	
	/**
	 * Devuelve el 
	 * @param cct
	 * @param document
	 * @param idEntidad
	 * @return
	 * @throws Exception
	 */
	public static List<DatosCompletosFirma> getFirmasDocumento(IClientContext cct, IItem document, String idEntidad) throws Exception {
		
		String infopagRde = document.getString("INFOPAG_RDE");
		File file = DocumentosUtil.getFile(cct, infopagRde);
		
		PdfReader reader = new PdfReader(file.getAbsolutePath());
		
		AcroFields fields = reader.getAcroFields();
        @SuppressWarnings("unchecked")
		List<String> listNames = fields.getSignatureNames();
        Collections.sort(listNames);
        
        ArrayList<DatosCompletosFirma> listFirmas = new ArrayList<DatosCompletosFirma>();
		
        for (String name : listNames){
		
        	DatosCompletosFirma datosFirma = getDatosFirmaByName(cct, idEntidad, fields, name, false);
	        listFirmas.add(datosFirma);
        }
    	
        return listFirmas;
	}


	private static DatosCompletosFirma getDatosFirmaByName(IClientContext cct, String idEntidad, AcroFields fields, String name, boolean usuarioActual) throws Exception{
		
		DatosCompletosFirma datosFirma = null;
		String dni ="";
		String nombre ="";
		
		try {
			InfoFirmante infofirmante = new InfoFirmante();
			PdfPKCS7 pkcs7 = fields.verifySignature(name);
			
			//[DipuCR-Agustin] #819 error al leer los campos del dni y nombre en certificados representante 
			//y otros certificados que no sean los normales de FNMT
			ASN1Parser aux = new ASN1Parser();
			@SuppressWarnings("rawtypes")
			Map aux1=aux.readPropertiesOid(pkcs7.getSigningCertificate());
			
			//Caso de certificado FNMT
			if(null!=aux1.get(FMNTCertificateParser.DNI_OID)) {
			
				String dni_oid =(String) aux1.get(FMNTCertificateParser.DNI_OID);
				nombre = getNombreCompletoCertificadoFNMT(aux1);
				    	
				String[] arrNombreCompleto = dni_oid.split("-");
				
				if(arrNombreCompleto.length>1){    	
					dni = arrNombreCompleto[1].trim();
				}else{
				    dni = dni_oid;    	
				}
				
			// Caso de certificado FMNT 2.16.724.1.3.5.7.2.4
			} else if (null!=aux1.get(FMNTEmpleadoPublicoCertificateParser.DNI_OID)) {

				String dni_oid =(String) aux1.get(FMNTEmpleadoPublicoCertificateParser.DNI_OID);
				nombre = getNombreCompletoEmpleadoPublicoCertificadoFNMT(aux1);
				    	
				String[] arrNombreCompleto = dni_oid.split("-");
				
				if(arrNombreCompleto.length>1){    	
					dni = arrNombreCompleto[1].trim();
				}else{
				    dni = dni_oid;    	
				}
				
			//Caso de certificado Camerfirma
			} else if(null!=aux1.get(CamerfirmaCertificateParser.POLITICA_CAMERFIRMA_OID) && aux1.get(CamerfirmaCertificateParser.POLITICA_CAMERFIRMA_OID).toString().trim().equals(CamerfirmaCertificateParser.POLITICA_CAMERFIRMA_URL_OID) )
			{
				dni = pkcs7.getSignName().split(" ")[0];	
				nombre = getNombreCompletoCertificadoCamerfirma(aux1);
			}	
			
			else{
				
				String mensaje="EL CERTIFICADO FIRMANTE NO ES CAMERFIRMA FNMT " + pkcs7.getSignName(); 
				LOGGER.error(mensaje);
				throw new Exception(mensaje);
				
			}
			
			//[DipuCR-Agustin] #819 FIN
			
			infofirmante.setDocIdentidadCertificado(dni);
			
			String nombreUsuario = null;
			if (usuarioActual){
				nombreUsuario = cct.getResponsible().getName();
			}
			else{
				AccesoBBDDRegistro accesoRegistro = new AccesoBBDDRegistro(idEntidad);
				nombreUsuario = accesoRegistro.getUserNameByNif(dni);
			}

			ServicioEstructuraOrganizativa servEstOrg = LocalizadorServicios.getServicioEstructuraOrganizativa();
			Usuario user = servEstOrg.getUsuario(nombreUsuario, idEntidad);
			String cargo = user.get_description();
			if(!usuarioActual) nombre = UsuariosUtil.getNombreFirma(idEntidad, user.get_id());
			
			infofirmante.setNombreFirmante(nombre);
			infofirmante.setCargo(cargo);
			
			datosFirma = new DatosCompletosFirma(infofirmante, pkcs7.getSignDate().getTime());
		} catch (Exception e) {
			LOGGER.error("ERROR AL RECUPERAR LA INFORMACIÓN DEL FIRMANTE CON DNI "+dni , e);
		}
		
		return datosFirma;
	}


	@SuppressWarnings("rawtypes")
	private static String getNombreCompletoCertificadoFNMT(Map certProperties) {
		
		StringBuffer sbNombreCompleto = new StringBuffer();
		
		if (null != certProperties.get(FMNTCertificateParser.FIRST_NAME_OID)){
			sbNombreCompleto.append(certProperties.get(FMNTCertificateParser.FIRST_NAME_OID));
		}
		if (null != certProperties.get(FMNTCertificateParser.SURNAME_OID)){
			sbNombreCompleto.append(" ");
			sbNombreCompleto.append(certProperties.get(FMNTCertificateParser.SURNAME_OID));
		}
		if (null != certProperties.get(FMNTCertificateParser.SECOND_SURNAME_OID)){
			sbNombreCompleto.append(" ");
			sbNombreCompleto.append(certProperties.get(FMNTCertificateParser.SECOND_SURNAME_OID));
		}
		return sbNombreCompleto.toString().trim();
	}
	
	
	@SuppressWarnings("rawtypes")
	private static String getNombreCompletoEmpleadoPublicoCertificadoFNMT(Map certProperties) {
		
		StringBuffer sbNombreCompleto = new StringBuffer();
		
		if (null != certProperties.get(FMNTEmpleadoPublicoCertificateParser.FIRST_NAME_OID)){
			sbNombreCompleto.append(certProperties.get(FMNTEmpleadoPublicoCertificateParser.FIRST_NAME_OID));
		}
		if (null != certProperties.get(FMNTEmpleadoPublicoCertificateParser.SURNAME_OID)){
			sbNombreCompleto.append(" ");
			sbNombreCompleto.append(certProperties.get(FMNTEmpleadoPublicoCertificateParser.SURNAME_OID));
		}
		if (null != certProperties.get(FMNTEmpleadoPublicoCertificateParser.SECOND_SURNAME_OID)){
			sbNombreCompleto.append(" ");
			sbNombreCompleto.append(certProperties.get(FMNTEmpleadoPublicoCertificateParser.SECOND_SURNAME_OID));
		}
		return sbNombreCompleto.toString().trim();
	}
	
	
	@SuppressWarnings("rawtypes")
	private static String getNombreCompletoCertificadoCamerfirma(Map certProperties) {

		StringBuffer sbNombreCompleto = new StringBuffer();
		
		if (null != certProperties.get(CamerfirmaCertificateParser.FIRST_NAME_OID)){
			sbNombreCompleto.append(certProperties.get(CamerfirmaCertificateParser.FIRST_NAME_OID));
		}
		if (null != certProperties.get(CamerfirmaCertificateParser.SURNAME_OID)){
			sbNombreCompleto.append(" ");
			sbNombreCompleto.append(certProperties.get(CamerfirmaCertificateParser.SURNAME_OID));
		}
		if (null != certProperties.get(CamerfirmaCertificateParser.SECOND_SURNAME_OID)){
			sbNombreCompleto.append(" ");
			sbNombreCompleto.append(certProperties.get(CamerfirmaCertificateParser.SECOND_SURNAME_OID));
		}
		return sbNombreCompleto.toString().trim();
	}
	
	/**
	 * Devuelve el nombre de la última firma
	 * @param reader
	 * @return
	 * @throws Exception 
	 */
	public static DatosCompletosFirma getUltimaFirma(IClientContext cct, IItem document, String idEntidad) throws Exception{
		
		File file = DocumentosUtil.getFile(cct, document.getString("INFOPAG_RDE"));
		PdfReader reader = new PdfReader(file.getAbsolutePath());
		
		AcroFields fields = reader.getAcroFields();
        @SuppressWarnings("unchecked")
		List<String> listNames = fields.getSignatureNames();
        Collections.sort(listNames);
        
        String nameUltimo = listNames.get(listNames.size() - 1);
		return getDatosFirmaByName(cct, idEntidad, fields, nameUltimo, true);
	}

}


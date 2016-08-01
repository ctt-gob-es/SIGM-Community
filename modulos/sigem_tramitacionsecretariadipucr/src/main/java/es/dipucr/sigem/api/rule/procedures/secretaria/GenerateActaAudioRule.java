package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.ownCloud.OwnCloudConfiguration;
import es.dipucr.ownCloud.RemoteFile;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.OwnCloudUtils;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GenerateActaAudioRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(GenerateActaAudioRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			/*********************************************************/
			numexp = rulectx.getNumExp(); 
			
			int id_tram_pcd = 0;
			String rutaFileName = "";
			FileInputStream fisFileAnexo = null;
			PdfWriter writer = null;
			
			// Obtengo el id del tramite
			StringBuffer query = new StringBuffer("WHERE NOMBRE LIKE 'Carga audio%' AND NUMEXP='" + numexp + "'");

			IItemCollection itemcol = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_TRAMITES, query.toString());
			Iterator<?> itTramites = itemcol.iterator();
			
			while (itTramites.hasNext()) {
				IItem itemTramites = (IItem) itTramites.next();
				id_tram_pcd = itemTramites.getInt("ID_TRAM_PCD");
			}
			
			String sqlQuery = "NUMEXP='" + numexp
					+ "' AND (EXTENSION='odt' OR EXTENSION='doc') AND NOMBRE LIKE '%Acta de Pleno%' and ID_TRAMITE_PCD=" + id_tram_pcd
					+ "";
			IItemCollection documentos = entitiesAPI.getDocuments(numexp, sqlQuery, "FDOC ASC");
			Iterator<?> iDoc = documentos.iterator();
			
			boolean existeUsuarioOwncloud = true;
			boolean anexadoAudio = false;

			if (iDoc.hasNext()) {

				IItem docActa = (IItem) iDoc.next();
			
				OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
				
				String entidad = EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext());
				
				String username = ownCloudConfig.getProperty("USR_"+entidad);
				String password = ownCloudConfig.getProperty("USR_PASSWORD_"+entidad);
				
				File resultado = null;
				
				if(username!=null && password!=null){
					String directorio = numexp.replace("/", "_");
					
					Document document = null;
					// Creamos un reader para el documento
					PdfReader reader = null;
						
					// Convertir el documento a pdf
					String docFilePath= DocumentConverter.convert2PDF(rulectx.getClientContext().getAPI(), docActa.getString("INFOPAG"), docActa.getString("EXTENSION"));
					
					// Obtener la informaci�n del fichero convertido
					File sFileTemplate = new File( docFilePath);				
					

					rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"
							+ FileTemporaryManager.getInstance().newFileName() + ".pdf";
					resultado = new File(rutaFileName);
					FileOutputStream resultadoFO = new FileOutputStream(resultado.getPath());

					fisFileAnexo = new FileInputStream(sFileTemplate);
					reader = new PdfReader((InputStream) fisFileAnexo);
					reader.consolidateNamedDestinations();
					int n = reader.getNumberOfPages();

					document = new Document(reader.getPageSizeWithRotation(1));

					document.setMargins(0, 0, 0, 0);

					writer = PdfWriter.getInstance(document, resultadoFO);
					writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);

					document.open();
					for (int i = 1; i <= n; i++) {
						document.newPage();
						Image imagen = Image.getInstance(writer.getImportedPage(reader, i));
						imagen.scalePercent(100);
						document.add(imagen);
					}
					
					ArrayList<?> documentosAnexar = OwnCloudUtils.obtenerDocsCarpeta(username, password, directorio);
					for (int i = 0; documentosAnexar != null && i < documentosAnexar.size(); i++) {
						
						RemoteFile fileAnexar = (RemoteFile)documentosAnexar.get(i);

						String nombreDoc = OwnCloudUtils.getNombreDoc(fileAnexar);
						
						String sfileAnexar = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + nombreDoc;
						File fileAnexarContenido = new File(sfileAnexar);
						logger.warn("fileAnexarContenido "+fileAnexarContenido);
						if(!fileAnexarContenido.exists()){
							try{
								fileAnexarContenido.createNewFile();
							}
							catch(IOException e){
								logger.warn("No se puede crear el documento especificado "+fileAnexarContenido);
								if(!anexadoAudio){
									existeUsuarioOwncloud = false;
								}									
							}
						}						
						OwnCloudUtils.descargarDoc(username, password, fileAnexar.getRemotePath(), fileAnexarContenido);
						
						DocumentosUtil.anadeDocumentoPdf(writer, fileAnexarContenido.getAbsolutePath(), fileAnexarContenido.getName(), normalizar(fileAnexarContenido.getName()));
						anexadoAudio = true;
						existeUsuarioOwncloud = true;
						if(!anexadoAudio){
							existeUsuarioOwncloud = false;
						}
					
						if(fileAnexarContenido!= null && fileAnexarContenido.exists()) fileAnexarContenido.delete();
					}
					
					document.close();
					resultadoFO.close();
					reader.close();
				}
				else{
					//No existe usuario de Owncloud
					existeUsuarioOwncloud = false;					
				}

				if(!existeUsuarioOwncloud && !anexadoAudio){
					resultado = DocumentosUtil.getFile(cct, docActa.getString("INFOPAG"), docActa.getString("NOMBRE"), docActa.getString("EXTENSION"));
				}
				// Guarda el resultado en gestor documental
				int tpdoc = DocumentosUtil.getTipoDoc(cct, "Acta de pleno con audio", DocumentosUtil.BUSQUEDA_EXACTA, false);
				
				//[Ticket #1301#INICIO]SIGEM envío de correo del audio acta con debates para los integantes de la sesión
				String extensionDoc = docActa.getString("EXTENSION");
				if(existeUsuarioOwncloud){
					extensionDoc = Constants._EXTENSION_PDF;
				}
				IItem docAudioActa = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, "Acta de pleno con audio", resultado, extensionDoc);
				docAudioActa.set("ESTADO", "PښBLICO");
				docAudioActa.store(cct);
				//[Ticket #1301#FIN]
				
				if(existeUsuarioOwncloud){
					resultado.delete();
					resultado = null;
					DocumentosUtil.deleteFile(rutaFileName);
		
					fisFileAnexo.close();
					fisFileAnexo = null;
					writer.close();
					writer = null;
					System.gc();
				}				
				
				// Borra los documentos intermedios del gestor documental
				IItemCollection collectionBorrar = entitiesAPI.getDocuments(numexp, "ID_TRAMITE='" + rulectx.getTaskId()
						+ "' AND EXTENSION='odt' AND DESCRIPCION = 'Prueba Acta de pleno con audio'", "");
				Iterator<?> itBorrar = collectionBorrar.iterator();
				while (itBorrar.hasNext()) {
					IItem docBorrar = (IItem) itBorrar.next();
					entitiesAPI.deleteDocument(docBorrar);
				}	
			}
		} catch (ISPACException e) {
			logger.error("Error al generar los documentos del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			logger.error("Error al generar los documentos del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Error al generar los documentos del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (DocumentException e) {
			logger.error("Error al generar los documentos del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		return new Boolean (true);
	}

	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}

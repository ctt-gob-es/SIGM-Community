package es.dipucr.contratacion.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class CrearDocumentoconAnexosRule implements IRule{
	
	protected static final Logger logger = Logger.getLogger(CrearDocumentoconAnexosRule.class);
	private IItemCollection docAnexos = null;
	private IItem plantillaInicial = null;
	private String codPlantillaGenerar = "SOLINFTECNDOCTE";
	private String codTramiteGenerar = "SOLINFTECNDOCTE";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();

			/*********************************************************/
			//File con el contenido en pdf
			File file = null;
			
			Object connectorSession = null;
			String nombreDocumento = "";
			
			Document document = null;
			// Creamos un reader para el documento
			PdfReader reader = null;
			
			connectorSession = gendocAPI.createConnectorSession();
			
			 int taskId = TramitesUtil.crearTramite((ClientContext) cct, this.codTramiteGenerar, rulectx.getNumExp());
			
			
			//Compruebo que tenga anexos este expediente y si tiene que se ejecite la regla y si no tiene que
			//no haga nada
			Iterator<IItem> iDocAnexar = docAnexos.iterator();
			
			if(iDocAnexar.hasNext()){
					
				String  infoPag = "";
				String extension = "";
				if(plantillaInicial.getString("INFOPAG_RDE")!=null){
					infoPag = plantillaInicial.getString("INFOPAG_RDE");
					extension = plantillaInicial.getString("EXTENSION_RDE");
				}
				else{
					infoPag = plantillaInicial.getString("INFOPAG");
					extension = plantillaInicial.getString("EXTENSION");
				}
				
				//plantilla final
				IItem tipoDocFinal = null;
				int templateIdFinal = 0;
				String plantillaFinal = DocumentosUtil.getNombrePlantillaByCod(cct, codPlantillaGenerar);		
				if(StringUtils.isNotEmpty(plantillaFinal)){
					tipoDocFinal = DocumentosUtil.getTipoDocumentoByPlantillaIItem(cct, plantillaFinal);			
				}
				if(tipoDocFinal!=null){
					templateIdFinal = tipoDocFinal.getInt("ID");
				}
				
				//Plantilla inicial
				
	        	//Plantilla de Notificaciones
				int idPlantilla = DocumentosUtil.getIdDocumentByNombre(rulectx.getNumExp(), rulectx, plantillaInicial.getString("NOMBRE"));
				logger.warn("idPlantilla = " + idPlantilla);
	        	
				File resultado1 = DocumentosUtil.getFile(cct, infoPag, null, null);
	        	
	        	
	        	IItem newdoc1 = gendocAPI.createTaskDocument(taskId, templateIdFinal);
	    		FileInputStream in1 = new FileInputStream(resultado1);
	    		int docId1 = newdoc1.getInt("ID");
	    		
	    		
	    		IItem entityDoc1 = gendocAPI.attachTaskInputStream(connectorSession, taskId, docId1, in1, (int)resultado1.length(), "application/vnd.oasis.opendocument.text", plantillaFinal);
	    		int documentId = entityDoc1.getKeyInt();
	    		IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateIdFinal, resultado1.getName());
	    		entityTemplate.set("EXTENSION", Constants._EXTENSION_ODT);
	    		entityTemplate.set("DESCRIPCION", "Prueba Carta Digital Plantilla");
	    		entityTemplate.store(cct);
	        	
	        	
	        	String docInfoPag = entityTemplate.getString("INFOPAG");
				// Convertir el documento original a PDF
	    		String docFilePath= DocumentConverter.convert2PDF(cct.getAPI(), docInfoPag,extension);
	    		
	    		// Obtener la información del fichero convertido
	    		file = new File(docFilePath);
	    		if (!file.exists())
	    			throw new ISPACException("No se ha podido convertir el documento a PDF");

				String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+FileTemporaryManager.getInstance().newFileName()+".pdf";
				File resultado = new File(rutaFileName);
				FileOutputStream resultadoFO = new FileOutputStream(resultado.getPath());
				
				FileInputStream fisFileAnexo = new FileInputStream(file);
				reader = new PdfReader((InputStream) fisFileAnexo);
				reader.consolidateNamedDestinations();
				int n = reader.getNumberOfPages();
				
				document = new Document(reader.getPageSizeWithRotation(1));
		
				document.setMargins(0, 0, 0, 0);
				
				PdfWriter writer = PdfWriter.getInstance(document, resultadoFO);				
				writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);

				document.open();
				for(int i = 1; i <= n; i++){
					document.newPage();
					Image imagen = Image.getInstance(writer.getImportedPage(reader, i));
					imagen.scalePercent(100);
					document.add(imagen);
					
				}

				while(iDocAnexar.hasNext()){
					IItem docAnexar = (IItem) iDocAnexar.next();
					
					String  infoPagAnexar = docAnexar.getString("INFOPAG");
					String ext = docAnexar.getString("EXTENSION");
					String descripcion = docAnexar.getString("DESCRIPCION");
					
					File fileAnexo = DocumentosUtil.getFile(cct, infoPagAnexar, null, null);
					

					
					nombreDocumento = descripcion + "." + ext;
					
					DocumentosUtil.anadeDocumentoPdf(writer, fileAnexo.getAbsolutePath(), nombreDocumento, normalizar(descripcion));
	
					fileAnexo.delete();
					fileAnexo = null;
					
					
				}
				
				document.close();								
				resultadoFO.close();								
				reader.close();
				
				//Guarda el resultado en gestor documental
	    		IItem newdoc = gendocAPI.createTaskDocument(taskId, templateIdFinal);
	    		FileInputStream in = new FileInputStream(resultado);
	    		int docId = newdoc.getInt("ID");
	    		
	    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, taskId, docId, in, (int)resultado.length(), "application/pdf", plantillaFinal);
	    		entityDoc.set("EXTENSION", Constants._EXTENSION_PDF);
	    		String templateDescripcion = entityDoc.getString("DESCRIPCION");
				entityDoc.set("DESCRIPCION", templateDescripcion);
	    		
	    		entityDoc.store(cct);
	    		file.delete();
	    		file = null;
	    		resultado.delete();
	    		resultado = null;
	    		DocumentosUtil.deleteFile(rutaFileName);
	    		
	    		//Borra los documentos intermedios del gestor documental
				String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION = 'Prueba Carta Digital Plantilla'";
		        IItemCollection collectionBorrar = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
		        Iterator<IItem> itBorrar = collectionBorrar.iterator();
		        while (itBorrar.hasNext())
		        {
		        	IItem docBorrar = (IItem)itBorrar.next();
		        	entitiesAPI.deleteDocument(docBorrar);
		        }
		        
		        DocumentosUtil.deleteFile(resultado1.getName());
		        resultado1.delete();
		        resultado1 = null;
		        in1.close();
		        fisFileAnexo.close();
		        fisFileAnexo = null;
		        writer.close();
		        writer = null;
		        in.close();
		        in = null;
		        DocumentosUtil.deleteFile(docFilePath);
				System.gc();
			}
			
			
			
		} catch (ISPACException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (FileNotFoundException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (IOException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (DocumentException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return new Boolean (true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}

	public IItemCollection getDocAnexos() {
		return docAnexos;
	}

	public void setDocAnexos(IItemCollection docAnexos) {
		this.docAnexos = docAnexos;
	}

	public IItem getPlantillaInicial() {
		return plantillaInicial;
	}

	public void setPlantillaInicial(IItem plantillaInicial) {
		this.plantillaInicial = plantillaInicial;
	}

	public String getCodPlantillaGenerar() {
		return codPlantillaGenerar;
	}

	public void setCodPlantillaGenerar(String codPlantillaGenerar) {
		this.codPlantillaGenerar = codPlantillaGenerar;
	}

	public String getCodTramiteGenerar() {
		return codTramiteGenerar;
	}

	public void setCodTramiteGenerar(String codTramiteGenerar) {
		this.codTramiteGenerar = codTramiteGenerar;
	}

}

package es.dipucr.sigem.api.rule.procedures.accTelExp;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.lowagie.text.DocumentException;

import es.dipucr.sigem.api.rule.common.DipucrGenerarExpedienteFoliadoConIndiceRule;
import es.dipucr.sigem.api.rule.common.expFol.DocIncluir;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class RecuperaDocumentos extends DipucrGenerarExpedienteFoliadoConIndiceRule{

	private static final Logger logger = Logger.getLogger(RecuperaDocumentos.class);
		
	@SuppressWarnings("rawtypes")
	public String getNumExpFoliar(IRuleContext rulectx, IEntitiesAPI entitiesAPI){
		String resultado = "";
		try {
			IItemCollection expedienteDestino = entitiesAPI.getEntities("DPCR_ACC_TEL_EXP", rulectx.getNumExp());
			Iterator itExpedienteDestino = expedienteDestino.iterator();
	
			if (itExpedienteDestino.hasNext()){	
				IItem expDest = (IItem) itExpedienteDestino.next();
				String numexp = expDest.getString("NUMEXP_ACC_TEL_EXP");
				if(StringUtils.isEmpty(numexp)) throw new ISPACRuleException("Debe indicar el expediente cuyos documentos desea recuperar.");
				else resultado = numexp.trim();
			}
			else throw new ISPACRuleException("Debe indicar el expediente cuyos documentos desea recuperar.");
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar el expediente a foliar.", e);
		} catch (ISPACException e) {
			logger.error("Error al recuperar el expediente a foliar.", e);
		}
		
		return resultado;
	}
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			// ----------------------------------------------------------------------------------------------

			logger.info("Inicio " + this.getClass().getName());

			numexp = rulectx.getNumExp();
			int taskId = rulectx.getTaskId();
			
			numExpPadre = getNumExpFoliar(rulectx, entitiesAPI);
			if(StringUtils.isNotEmpty(numExpPadre)){
				try{
					ArrayList<DocIncluir> docsDelExp = new ArrayList<DocIncluir>();
		
					ArrayList<String> expedientes = ExpedientesRelacionadosUtil.getProcedimientosRelacionadosHijos(numExpPadre, entitiesAPI);		
		
					if (expedientes != null && expedientes.size() > 0) {
						FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();				
						errores = ftMgr.newFile(".txt");
						erroresFW = new FileWriter(errores);
						
						erroresFW.write("\t\tDocumentos el expediente '" + numExpPadre + "' que no se han incluido en el expediente foliado:\n\n");
						
						IItem expedienteOriginal = ExpedientesUtil.getExpediente(cct, numExpPadre);
		
						String consultaDocumentos = getConsultaDocumentos(expedientes, rulectx, expedienteOriginal);
										
						IItemCollection documentsCollection = DocumentosUtil.queryDocumentos(cct, consultaDocumentos);
						docsDelExp = getDocsDelExp(documentsCollection);				
						
						recuperarDocs(docsDelExp, taskId, genDocAPI, entitiesAPI, cct, rulectx);
		
						erroresFW.flush();
						erroresFW.close();					
										
						//Añadimos el documento de errores sólo si hay algún error
						if(hayErrores){
							String sName = "Documentos no incluidos";
							
							int idTypeDocument = DocumentosUtil.getTipoDoc(cct, "EXPEDIENTE FOLIADO CON ÍNDICE", DocumentosUtil.BUSQUEDA_LIKE, true);
							if (idTypeDocument == Integer.MIN_VALUE) {
								throw new ISPACInfo("Error al obtener el tipo de documento EXPEDIENTE FOLIADO CON ÍNDICE del expediente: " + numexp + ".");
							}
							
							DocumentosUtil.generaYAnexaDocumento(rulectx, idTypeDocument, sName, errores, "txt");
						}
						if(errores != null && errores.exists()) errores.delete();
					}
				}catch (ISPACRuleException e) {
					logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
					throw new ISPACRuleException("ERROR Recuperando los documentos del expediente indicado: " + e.getMessage(), e);
				}catch (ISPACException e) {
					logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
					throw new ISPACRuleException("ERROR Recuperando los documentos del expediente indicado: " + e.getMessage(), e);
				}catch (FileNotFoundException e) {
					logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
					throw new ISPACRuleException("ERROR Recuperando los documentos del expediente indicado: " + e.getMessage(), e);
				}catch (IOException e) {
					logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
					throw new ISPACRuleException("ERROR Recuperando los documentos del expediente indicado: " + e.getMessage(), e);
				}
			}
			else{
			throw new ISPACRuleException("Debe indicar el expediente cuyos documentos desea recuperar.");
			}
		}catch (ISPACException e) {
			logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR Recuperando los documentos del expediente indicado: " + e.getMessage(), e);
		}
		logger.info("FIN " + this.getClass().getName());
					
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<DocIncluir> getDocsDelExp(IItemCollection documentsCollection) throws ISPACException, IOException {

		ArrayList<DocIncluir> resultado = new ArrayList<DocIncluir>();
		
		if (documentsCollection != null && documentsCollection.next()) {
			// Creamos el nuevo documento
			Iterator documentos = documentsCollection.iterator();

			while (documentos.hasNext()) {
				IItem documento = (IItem) documentos.next();
				String infoPagRDE = documento.getString("INFOPAG_RDE");
				String extension = documento.getString("EXTENSION_RDE");
				if (infoPagRDE == null || infoPagRDE.equals("")) {
					infoPagRDE = documento.getString("INFOPAG");
					extension = documento.getString("EXTENSION");
				}
				String numExp = documento.getString("NUMEXP");
				String nombreDocumento = documento.getString("NOMBRE");
				String descripcionDocumento = documento.getString("DESCRIPCION");
				Date fechaDocumento = documento.getDate("FFIRMA");
				Date fechaAprobacion = documento.getDate("FAPROBACION");
				String tipoRegistro = documento.getString("TP_REG");
				String nreg = documento.getString("NREG");
				String freg = documento.getString("FREG");
				String idPlantilla = documento.getString("ID_PLANTILLA");
				int idTipDoc = documento.getInt("ID_TPDOC");
				if (tipoRegistro == null
						|| tipoRegistro.toUpperCase().equals("NINGUNO"))
					tipoRegistro = "";
				if (nreg == null) {
					nreg = "";
					freg = "";
				}
				if(infoPagRDE != null){
					DocIncluir doc = new DocIncluir(infoPagRDE, numExp, tipoRegistro, nreg, freg, normalizar(nombreDocumento), extension, descripcionDocumento, fechaDocumento, fechaAprobacion, idPlantilla, idTipDoc);
					resultado.add(doc);
				}
				else{
					logger.error("Error no existe el documento '"+nombreDocumento+"' del expediente: '"+numExp+"'");
					erroresFW.write("\t - Error no existe el documento '"+nombreDocumento+"'\n\t\t'" + descripcionDocumento + "." +extension.trim() + "'\n\t\tExpediente: '"+numExp+"'\n\n");
					hayErrores = true;
				}
			}
		}
		return resultado;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void recuperarDocs(ArrayList<DocIncluir> docsDelExp, int taskId, IGenDocAPI genDocAPI, IEntitiesAPI entitiesAPI, ClientContext cct, IRuleContext rulectx) {
		
		FileOutputStream resultadoDocFO = null;
				
		String infoPag = "";
		String numexp = "";
		String nombreDoc = "";
		String extension = "";
		String descripcion = "";
		Date fechaDoc;
		Date fechaAprobacion;
		String idPlantilla = "";
		int idTipDoc = 0;
		File archivoOriginal = null;
		ArrayList<DocIncluir> docsResultado = null;			
		
		try {		
			
			if (docsDelExp.size() != 0) {

				Iterator<DocIncluir> docsDelExpIterator = docsDelExp.iterator();
				while (docsDelExpIterator.hasNext()) {
					
					DocIncluir documento1 = docsDelExpIterator.next();

					infoPag = documento1.infoPagRDE;
					numexp = documento1.numExp;
					
					nombreDoc = normalizar(documento1.nombreDoc);					
					
					extension = normalizar(documento1.extension);					
					
					descripcion = normalizar(documento1.descripcion);
					
					fechaDoc = documento1.fechaDoc;
					fechaAprobacion = documento1.fechaAprobacion;
					
					idPlantilla = documento1.idPlantilla;
					idTipDoc = documento1.idTipDoc;

					docsResultado = new ArrayList<DocIncluir>();
					
					try{
						archivoOriginal = DocumentosUtil.getFile(cct, infoPag, null, extension.trim());
					}
					catch(ISPACException e){
						logger.error("Error al recuperar el documento '"+nombreDoc+"' del expediente: '"+numexp+"'",e);
						erroresFW.write("\t - Error al recuperar el documento '"+nombreDoc+"'\n\t\t'" + descripcion + "." +extension.trim() + "'\n\t\tExpediente: '"+numexp+"'\n\n");
						hayErrores = true;
					}
					catch(Exception e){
						logger.error("Error al recuperar el documento '"+nombreDoc+"' del expediente: '"+numexp+"'",e);
						erroresFW.write("\t - Error al recuperar el documento '"+nombreDoc+"'\n\t\t'" + descripcion + "." +extension.trim() + "'\n\t\tExpediente: '"+numexp+"'\n\n");
						hayErrores = true;
					}
					
					try{
						if(archivoOriginal != null && archivoOriginal.exists())
							extraeDocs(archivoOriginal, extension, nombreDoc, descripcion, fechaDoc, fechaAprobacion, idPlantilla, idTipDoc, docsResultado, cct, infoPag);
					}
					catch(ISPACException e){
						logger.error("Error al extraer el documento '"+nombreDoc+"' del expediente: '"+numexp+"'",e);
						erroresFW.write("\t - Error al extraer el documento '"+nombreDoc+"'\n\t\t'" + descripcion + "." +extension.trim() + "'\n\t\tExpediente: '"+numexp+"'\n\n");
						hayErrores = true;
					}
					catch(Exception e){
						logger.error("Error al extraer el documento '"+nombreDoc+"' del expediente: '"+numexp+"'",e);
						erroresFW.write("\t - Error al extraer el documento '"+nombreDoc+"'\n\t\t'" + descripcion + "." +extension.trim() + "'\n\t\tExpediente: '"+numexp+"'\n\n");
						hayErrores = true;
					}
					
					if(docsResultado != null && !docsResultado.isEmpty()){
						Iterator docsResultadoIterator = docsResultado.iterator();					
						while(docsResultadoIterator.hasNext()){																				
							DocIncluir documento = (DocIncluir) docsResultadoIterator.next();
							
							try{
								añadeDocumento(taskId, documento, entitiesAPI, genDocAPI, rulectx);
							}
							catch(ISPACException e){
								logger.error("Error al guardar el documento '"+nombreDoc+"' del expediente: '"+numexp+"'",e);
								erroresFW.write("\t - Error al guardar el documento '"+nombreDoc+"'\n\t\t'" + descripcion + "." +extension.trim() + "'\n\t\tExpediente: '"+numexp+"'\n\n");
								hayErrores = true;
							}
							catch(Exception e){
								logger.error("Error al guardar el documento '"+nombreDoc+"' del expediente: '"+numexp+"'",e);
								erroresFW.write("\t - Error al guardar el documento '"+nombreDoc+"'\n\t\t'" + descripcion + "." +extension.trim() + "'\n\t\tExpediente: '"+numexp+"'\n\n");
								hayErrores = true;
							}
							finally{
								documento.borrar();
							}
						}//while docsResultadoIterator
					}
					if(archivoOriginal != null && archivoOriginal.exists())
						archivoOriginal.delete();
				}// while
			} 
		} catch (Exception e) {
			logger.error("Error al concatenar los PDF's: " + e.getMessage(),e);
		}
		finally{				
			if(resultadoDocFO != null)
				try {
					resultadoDocFO.close();
				} catch (IOException e) {
					logger.error("ERROR al concatenar los PDF's: "+e.getMessage(), e);
				}
		}
	}

	public void añadeDocumento(int taskId, DocIncluir documento, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, IRuleContext rulectx)
			throws ISPACException, DocumentException, MalformedURLException, IOException {

		String nombreDoc = "";
		String descripcion = "";
		String idPlantilla = "";
		int idTipDoc = 0;
		
		nombreDoc = normalizar(documento.nombreDoc);						
		descripcion = normalizar(documento.descripcion);
			
		idPlantilla = documento.idPlantilla;
		idTipDoc = documento.idTipDoc;
			
		File documentoFile = documento.docPdf; 
		IItem entityDocument = DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, descripcion, documentoFile, documento.extension);
			
		entityDocument.set("EXTENSION", "pdf");
		entityDocument.set("ID_PLANTILLA", idPlantilla);
		entityDocument.set("NOMBRE", nombreDoc);
		entityDocument.set("FFIRMA", documento.fechaDoc);
		entityDocument.set("FAPROBACION", documento.fechaAprobacion);
		
		entityDocument.store(rulectx.getClientContext());
	}
}
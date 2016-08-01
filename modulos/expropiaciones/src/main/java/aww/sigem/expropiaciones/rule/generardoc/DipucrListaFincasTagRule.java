package aww.sigem.expropiaciones.rule.generardoc;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.rule.tags.ListaFincasExpropiadoTagRule;
import aww.sigem.expropiaciones.util.PropietariosUtil;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrTablasUtil;

public class DipucrListaFincasTagRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ListaFincasExpropiadoTagRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			logger.warn("Ejecutando regla DipucrListaFincasTagRule");
			
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
	        //----------------------------------------------------------------------------------------------
			
			Object connectorSession = null;
		    OpenOfficeHelper ooHelper = null;
			
			//Documentos a visualizar
			String tipoDocumentoInforme = "EXPR-001 - Informe Iniciación";
			String tipoDocumentoPropuesta = "EXPR-002 - Propuesta aprobación provisional";
			
			String plantillaInforme = "EXPR-001 - Informe Iniciación";
			String plantillaPropuesta = "EXPR-002 - Propuesta aprobación provisional";
			
			//Obtiene el expediente
	        String numexp = rulectx.getNumExp();
	        IItem entityDocument = null;
	        IItem entityDocumentPropuesta = null;
			int documentTypeId = 0;
			int documentTypeIdPropuesta = 0;
			int documentId  = 0;
			int documentIdPropuesta  = 0;
			int templateId = 0;
			int templateIdPropuesta = 0;
			int taskId = rulectx.getTaskId();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			
			IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);     		
    		Iterator it = taskTpDocCollection.iterator();
			while (it.hasNext()){
    			IItem taskTpDoc = (IItem)it.next();
    			if ((((String)taskTpDoc.get("CT_TPDOC:NOMBRE")).trim().toUpperCase()).equals((tipoDocumentoInforme).trim().toUpperCase())){    				
    				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
    			}
    		}
			   		
    		it = taskTpDocCollection.iterator();
			while (it.hasNext()){
    			IItem taskTpDoc = (IItem)it.next();
    			if ((((String)taskTpDoc.get("CT_TPDOC:NOMBRE")).trim().toUpperCase()).equals((tipoDocumentoPropuesta).trim().toUpperCase())){    				
    				documentTypeIdPropuesta = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
    			}
    		}
			
			cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
			
			IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
			Iterator docs = tpDocsTemplatesCollection.iterator();
			while (docs.hasNext()){
				IItem tpDocsTemplate = (IItem)docs.next();
				if(((String)tpDocsTemplate.get("NOMBRE")).trim().toUpperCase().equals(plantillaInforme.trim().toUpperCase())){
					templateId = tpDocsTemplate.getInt("ID");
				}
			}
			
			IItemCollection tpDocsTemplatesCollectionPropuesta = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeIdPropuesta);
			Iterator docsPropuesta = tpDocsTemplatesCollectionPropuesta.iterator();
			while (docsPropuesta.hasNext()){
				IItem tpDocsTemplate = (IItem)docsPropuesta.next();
				if(((String)tpDocsTemplate.get("NOMBRE")).trim().toUpperCase().equals(plantillaPropuesta.trim().toUpperCase())){
					templateIdPropuesta = tpDocsTemplate.getInt("ID");
				}
			}
			
			//Informe
    		entityDocument  = genDocAPI.createTaskDocument(taskId, documentTypeId);
    		documentId = entityDocument.getKeyInt();
    		
    		//Propuesta
    		entityDocumentPropuesta  = genDocAPI.createTaskDocument(taskId, documentTypeIdPropuesta);
    		documentIdPropuesta = entityDocumentPropuesta.getKeyInt();
												
			// Generar el documento a partir la plantilla Informe 
    		IItem entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
									
			String docref = entityTemplate.getString("INFOPAG");
			String sMimetype = genDocAPI.getMimeType(connectorSession, docref);
			entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
			String mime = "application/vnd.oasis.opendocument.text";
			String templateDescripcion = entityTemplate.getString("DESCRIPCION");
			entityTemplate.set("DESCRIPCION", templateDescripcion);

			entityTemplate.store(cct);
			
			// Generar el documento a partir la plantilla Propuesta
    		IItem entityTemplatePropuesta = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentIdPropuesta, templateIdPropuesta);
									
			String docrefPropuesta = entityTemplatePropuesta.getString("INFOPAG");
			String sMimetypePropuesta = genDocAPI.getMimeType(connectorSession, docrefPropuesta);
			entityTemplatePropuesta.set("EXTENSION", MimetypeMapping.getExtension(sMimetypePropuesta));
			String templateDescripcionPrpuesta = entityTemplatePropuesta.getString("DESCRIPCION");
			entityTemplatePropuesta.set("DESCRIPCION", templateDescripcionPrpuesta);

			entityTemplatePropuesta.store(cct);
			
			//Abre el documento Informe
			String extension = "odt";
			String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			OutputStream out = new FileOutputStream(fileName);
    		connectorSession = genDocAPI.createConnectorSession();
    		genDocAPI.getDocument(connectorSession, docref, out);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
    		
    		//Abre el documento Propuesta
			String fileNamePropuesta = FileTemporaryManager.getInstance().newFileName("."+extension);
			fileNamePropuesta = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNamePropuesta;
			OutputStream outPropuesta = new FileOutputStream(fileNamePropuesta);
    		genDocAPI.getDocument(connectorSession, docrefPropuesta, outPropuesta);
    		XComponent xComponentPropuesta = ooHelper.loadDocument("file://" + fileNamePropuesta);
    		
    		
			
			List contenido = generaListFincas(rulectx);
			int numColumnas = 0;
			//Saco el número de columnas
			if(contenido != null){
				numColumnas = ((List)contenido.get(0)).size();
			}
			DipucrTablasUtil.insertaTabla1(xComponent, contenido, numColumnas-1, false);
			
			DipucrTablasUtil.insertaTabla1(xComponentPropuesta, contenido, numColumnas-1, false);
			
			 //Guarda el documento Informe
			String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
			fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
    		File fileOut = new File(fileNameOut);
    		InputStream in = new FileInputStream(fileOut);			    		
    		genDocAPI.setDocument(connectorSession, documentId, docref, in, (int)(fileOut.length()), mime);
    		
    		
    		 //Guarda el documento Propuesta
			String fileNameOutPropuesta = FileTemporaryManager.getInstance().newFileName(".odt");
			fileNameOutPropuesta = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOutPropuesta;
    		OpenOfficeHelper.saveDocument(xComponentPropuesta,"file://" + fileNameOutPropuesta,"");
    		File fileOutPropuesta = new File(fileNameOutPropuesta);
    		InputStream inPropuesta = new FileInputStream(fileOutPropuesta);			    		
    		genDocAPI.setDocument(connectorSession, documentIdPropuesta, docrefPropuesta, inPropuesta, (int)(fileOutPropuesta.length()), mime);
			
			
			logger.warn("FIN DipucrListaFincasTagRule");	
			
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
		return new Boolean (true);
	}
	
	public List generaListFincas(IRuleContext rulectx){
		
		//Genera los datos de las filas de datos			
		//Lista de listas de filas
		List contenido = new ArrayList();
		
		try{
			
			//Genera los encabezados de la tabla
			List titulos = new ArrayList();
			
			titulos.add("Finca Num.");
			titulos.add("Propietario");
			titulos.add("Polígono");
			titulos.add("Parcela");
			titulos.add("Municipio de Parcela");
			titulos.add("Superficie de Parcela (m2)");
			titulos.add("Superficie a ocupar (m2)");
			titulos.add("Calificación");			
			
			
			//Obtener las fincas relacionadas con la Expropiacion
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			//Obtiene los números de expediente de las fincas
			String listado = "";
			String strQuery = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator it = collection.iterator();
			IItem item = null;
			List expFincas = new ArrayList();
			
			logger.warn("Expediente de Expropiacion: " + rulectx.getNumExp());
			
			while (it.hasNext()) {
			   item = (IItem)it.next();
			   expFincas.add(item.getString("NUMEXP_HIJO"));
			   logger.warn("Expediente de Finca: " + item.getString("NUMEXP_HIJO"));			
			}
			
			//Si la lista de fincas está vacía no dibujar la tabla
			if(expFincas.isEmpty()){
				return contenido;
			}
			
			//Obtiene los datos de las fincas
			Iterator itExpFincas = expFincas.iterator();
			strQuery="WHERE NUMEXP = '" + itExpFincas.next() + "'";
			while (itExpFincas.hasNext()) {
				strQuery+=" OR NUMEXP = '" + itExpFincas.next() + "'";				 		
			}
				
			
			logger.warn("Fincas a buscar: " + strQuery);
			
			/**
			 * #[Teresa - Ticket 200] INICIO SIGEM expropiaciones Modificar los listados para que aparezcan los metros a ocupar por propietario 
			 * **/
			
			
			String clausulas = "ORDER BY MUNICIPIO, CASE WHEN NUM_POLIGONO < 'A' THEN LPAD(NUM_POLIGONO, 255, '0') ELSE NUM_POLIGONO END, " +
					"CASE WHEN NUM_PARCELA < 'A' THEN LPAD(NUM_PARCELA, 255, '0') ELSE NUM_PARCELA END";
			
			// 	IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + "ORDER BY MUNICIPIO, NUM_POLIGONO ASC, NUM_PARCELA ASC");
			
			IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + clausulas);
			
			/**
			 * #[Teresa - Ticket 200] FIN SIGEM expropiaciones Modificar los listados para que aparezcan los metros a ocupar por propietario 
			 * **/
		
			//Si la lista de fincas está vacía no dibujar la tabla
			if(collectionFincas.toList().isEmpty()){
				return contenido;
			}
			
			Iterator itFincas = collectionFincas.iterator();

			//Cada fila individual
			List fila = null;
			//Añado la cabecera
			contenido.add(titulos);
			
			while (itFincas.hasNext()) {
				fila = new ArrayList();
				item = (IItem)itFincas.next();
				fila.add(item.getString("NUMEXP"));
				//Código que extrae una lista de propietarios
				fila.add(PropietariosUtil.propietariosFinca(entitiesAPI,item.getString("NUMEXP")));				
				fila.add(item.getString("NUM_POLIGONO"));
				fila.add(item.getString("NUM_PARCELA"));
				fila.add(item.getString("MUNICIPIO"));
				fila.add(item.getString("SUP_PARCELA"));
				fila.add(item.getString("SUP_EXPROPIADA"));
				fila.add(item.getString("APROVECHAMIENTO"));
				
				//Si hay algun nulo reemplazarlo por desconocido

				for (int i=0;i<fila.size();i++){
					if (fila.get(i) == null) {
						fila.set(i, "Desconocido");
					}
				}			
				
				contenido.add(fila);				
				logger.warn("Detalle de Expediente de Finca: " + item.getString("NUMEXP") + " Num Finca: " + item.getString("NUM_FINCA"));
			}
		}catch (ISPACRuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ISPACException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return contenido;
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

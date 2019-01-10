package es.dipucr.sigem.api.rule.procedures.planesProvinciales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.text.ParagraphVertAlign;
import com.sun.star.text.TableColumnSeparator;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextTableCursor;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.domain.planesProvinciales.DatosCuadroMinisterio;
import es.dipucr.domain.planesProvinciales.ResumePlanProvincial;
import es.dipucr.domain.planesProvinciales.ResumenPlanPorAyuntamiento;
import es.dipucr.services.server.planesProvinciales.PlanProvincialServicioProxy;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrGeneraDocPropuesta2PlanesProvRule implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrGeneraDocPropuesta2PlanesProvRule.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {		
		OpenOfficeHelper ooHelper = null;
		
		try{
			logger.info("INICIO - DipucrGeneraDocPropuesta2PlanesProvRule");
			
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			cct.endTX(true);
			
			String anio = "";
			
			String consultaAnio = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			IItemCollection planesProvinciales = entitiesAPI.queryEntities("DIPUCRPLANESPROVINCIALES", consultaAnio);
			Iterator it1 = (Iterator) planesProvinciales.iterator();
			if(it1.hasNext()){
				anio = ((IItem)it1.next()).getString("ANIO");
			}
			if(anio == null){
				anio = "";
			}	
			
			// Variables
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			
			String numExp = rulectx.getNumExp();
	    	int documentId = 0;
	    	Object connectorSession = null;	    	
	    	
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, " (ROL != 'TRAS' OR ROL IS NULL) ", "ID");
			
			// 2. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes!=null && participantes.toList().size()>=1) {
				// 3. Obtener plantilla "Comunicación Administrativa Ayuntamientos Planes"
				// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
	        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);     		
        		Iterator it = taskTpDocCollection.iterator();
        		while (it.hasNext()){
        			IItem taskTpDoc = (IItem)it.next();
        			if ((((String)taskTpDoc.get("CT_TPDOC:NOMBRE")).trim().toUpperCase()).equals(("Contenido de la propuesta").trim().toUpperCase())){
        				
        				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
        			}
        		}
        		//Asignamos el nombre del trátime ya que si no no lo muestra
        		setSsVariables(cct, rulectx);
        		cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
        		
        		//Comprobamos que haya encontrado el Tipo de documento
        		if (documentTypeId != 0){
	        		// Comprobar que el tipo de documento tiene asociado una plantilla
		        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
		        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
		        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
		        	}else{
		        		Iterator docs = tpDocsTemplatesCollection.iterator();
		        		boolean encontrado= false;
		        		while (docs.hasNext() && !encontrado){
		        			IItem tpDocsTemplate = (IItem)docs.next();
		        			if(((String)tpDocsTemplate.get("NOMBRE")).trim().toUpperCase().equals("Propuesta 2 Planes Provinciales".trim().toUpperCase())){
		        				templateId = tpDocsTemplate.getInt("ID");
		        				encontrado= true;
		        			}
		        		}
		        		IItem entityDocumentT  = gendocAPI.createTaskDocument(taskId, documentTypeId);
						int documentIdT = entityDocumentT.getKeyInt();						

						IItem entityTemplateT = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentIdT, templateId);
						String infoPagT = entityTemplateT.getString("INFOPAG");
						entityTemplateT.store(cct);
		        		
		        		entityDocument  = gendocAPI.createTaskDocument(taskId, documentTypeId);
						documentId = entityDocument.getKeyInt();

						String sFileTemplate = DocumentosUtil.getFile(cct, infoPagT, null, null).getName();
															
						// Generar el documento a partir la plantilla 
						IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);
												
						String docref = entityTemplate.getString("INFOPAG");
						String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
						entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
						String templateDescripcion = entityTemplate.getString("DESCRIPCION");
						entityTemplate.set("DESCRIPCION", templateDescripcion);
						
						entityTemplate.store(cct);
						
						deleteSsVariables(cct);
						cct.deleteSsVariable("NOMBRE_TRAMITE");
						
						//Abre el documento
						String extension = "odt";
						String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
						fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
						OutputStream out = new FileOutputStream(fileName);
			    		connectorSession = gendocAPI.createConnectorSession();
						gendocAPI.getDocument(connectorSession, docref, out);
						File file = new File(fileName);
			    		ooHelper = OpenOfficeHelper.getInstance();
			    		XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
			    		
			    		//Realiza los cambios
			    		insertaTabla1(rulectx, xComponent, anio);
			    		insertaTabla2(rulectx, xComponent, anio);
			    		insertaTabla3(rulectx, xComponent, anio);
			    		
			    		//Tenemos dos veces las mismas tablas
			    		insertaTabla1(rulectx, xComponent, anio);
			    		insertaTabla2(rulectx, xComponent, anio);
			    		insertaTabla3(rulectx, xComponent, anio);


			    		insertaTabla4(rulectx, xComponent, anio);
			    		insertaTabla5(rulectx, xComponent, anio);
					    
					    //Guarda el documento
						String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
						fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
						String mime = "application/vnd.oasis.opendocument.text";
			    		OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
			    		File fileOut = new File(fileNameOut);
			    		InputStream in = new FileInputStream(fileOut);
			    		gendocAPI.setDocument(connectorSession, documentId, docref, in, (int)(fileOut.length()), mime);
			    		
			    		//Borra archivos temporales
			    		file.delete();
			    		fileOut.delete();

			    		entityTemplateT.delete(cct);
						entityDocumentT.delete(cct);
		        	}
        		}
			}		
			cct.endTX(true);
			logger.info("FIN - DipucrGeneraDocPropuesta2PlanesProvRule");
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	private void insertaTabla1(IRuleContext rulectx , XComponent xComponent, String anio) throws ISPACException, Exception
	{
        PlanProvincialServicioProxy planService;
        planService = new PlanProvincialServicioProxy();
        ResumePlanProvincial[] resumenPlanCooperacion = planService.getResumenPlanCooperacion(anio);
        ResumePlanProvincial[] totalPlanCooperacion = planService.getTotalResumenPlanCooperacion(anio);        
        
		if (resumenPlanCooperacion.length>0){
		    //Busca la posición de la tabla y coloca el cursor ahí
			//Usaremos el localizador %TABLA1%
			XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
	        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
	        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
	        xSearchDescriptor.setSearchString("%TABLA1%");
	        XInterface xSearchInterface = null;
	        XTextRange xSearchTextRange = null;
	        xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
	        if (xSearchInterface != null) 
	        {
	        	//Cadena encontrada, la borro antes de insertar la tabla
		        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
		        xSearchTextRange.setString("");
		        
				//Inserta una tabla de 9 columnas y tantas filas
			    //como nuevas liquidaciones haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				
				//Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
				xTable.initialize(resumenPlanCooperacion.length+3, 7);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
				//Rellena la cabecera de la tabla				
				setHeaderCellText(xTable, "A1", "PLAN PROVINCIAL DE COOPERACIÓN "+anio);	
				XTextTableCursor xtc = xTable.createCursorByCellName("A1");
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				
				setHeaderCellText(xTable, "A2", "Tipo de obra");				
				setHeaderCellText(xTable, "B2", "Finalidad");				
				setHeaderCellText(xTable, "C2", "Nº Obras");				
				setHeaderCellText(xTable, "D2", "Estado");				
				setHeaderCellText(xTable, "E2", "Diputación");				
				setHeaderCellText(xTable, "F2", "Ayuntamiento");				
				setHeaderCellText(xTable, "G2", "Total");	

				//Rellena la tabla con los datos de las liquidaciones
				NumberFormat df = NumberFormat.getCurrencyInstance();
				
				int i;
				for(i =0; i<resumenPlanCooperacion.length;i++){
					ResumePlanProvincial resumen = resumenPlanCooperacion[i];					
					
					setCellText(xTable, "A"+String.valueOf(i+3), ""+resumen.getTipoObra());
					setCellText(xTable, "B"+String.valueOf(i+3), resumen.getFinalidad());
					setCellText(xTable, "C"+String.valueOf(i+3), ""+new Double(resumen.getObras()).intValue());
					setCellText(xTable, "D"+String.valueOf(i+3), ""+df.format(resumen.getEstado()));
					setCellText(xTable, "E"+String.valueOf(i+3), ""+df.format(resumen.getDiputacion()));
					setCellText(xTable, "F"+String.valueOf(i+3), ""+df.format(resumen.getAyuntamiento()));
					setCellText(xTable, "G"+String.valueOf(i+3), ""+df.format(resumen.getTotal()));
				}
				
				//Añadimos la celda final
				ResumePlanProvincial total = totalPlanCooperacion[0];

				setCellTextLasRow(xTable, "A"+String.valueOf(i+3), "");
				setCellTextLasRow(xTable, "B"+String.valueOf(i+3), total.getFinalidad());
				setCellTextLasRow(xTable, "C"+String.valueOf(i+3), ""+new Double(total.getObras()).intValue());
				setCellTextLasRow(xTable, "D"+String.valueOf(i+3), ""+df.format(total.getEstado()));
				setCellTextLasRow(xTable, "E"+String.valueOf(i+3), ""+df.format(total.getDiputacion()));
				setCellTextLasRow(xTable, "F"+String.valueOf(i+3), ""+df.format(total.getAyuntamiento()));
				setCellTextLasRow(xTable, "G"+String.valueOf(i+3), ""+df.format(total.getTotal()));
	        }
		}
	}
	
	private void insertaTabla2(IRuleContext rulectx , XComponent xComponent, String anio) throws ISPACException, Exception
	{
        PlanProvincialServicioProxy planService;
        planService = new PlanProvincialServicioProxy();
        ResumePlanProvincial[] resumenPlanComplemetario = planService.getResumenPlanComplementario(anio);
        ResumePlanProvincial[] totalPlanComplemetario = planService.getTotalResumenPlanComplementario(anio);        
        
		if (resumenPlanComplemetario.length>0){
		    //Busca la posición de la tabla y coloca el cursor ahí
			//Usaremos el localizador %TABLA2%
			XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
	        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
	        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
	        xSearchDescriptor.setSearchString("%TABLA2%");
	        XInterface xSearchInterface = null;
	        XTextRange xSearchTextRange = null;
	        xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
	        if (xSearchInterface != null) 
	        {
	        	//Cadena encontrada, la borro antes de insertar la tabla
		        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
		        xSearchTextRange.setString("");
		        
				//Inserta una tabla de 9 columnas y tantas filas
			    //como nuevas liquidaciones haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				
				//Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
				xTable.initialize(resumenPlanComplemetario.length+3, 7);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
				//Rellena la cabecera de la tabla				
				setHeaderCellText(xTable, "A1", "PLAN COMPLEMENTARIO "+anio);	
				XTextTableCursor xtc = xTable.createCursorByCellName("A1");
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				
				setHeaderCellText(xTable, "A2", "Tipo de obra");				
				setHeaderCellText(xTable, "B2", "Finalidad");				
				setHeaderCellText(xTable, "C2", "Nº Obras");				
				setHeaderCellText(xTable, "D2", "Estado");				
				setHeaderCellText(xTable, "E2", "Diputación");				
				setHeaderCellText(xTable, "F2", "Ayuntamiento");				
				setHeaderCellText(xTable, "G2", "Total");	

				//Rellena la tabla con los datos de las liquidaciones
				NumberFormat df = NumberFormat.getCurrencyInstance();
				
				int i;
				for(i =0; i<resumenPlanComplemetario.length;i++){
					ResumePlanProvincial resumen = resumenPlanComplemetario[i];					
					
					setCellText(xTable, "A"+String.valueOf(i+3), ""+resumen.getTipoObra());
					setCellText(xTable, "B"+String.valueOf(i+3), resumen.getFinalidad());
					setCellText(xTable, "C"+String.valueOf(i+3), ""+new Double(resumen.getObras()).intValue());
					setCellText(xTable, "D"+String.valueOf(i+3), ""+df.format(resumen.getEstado()));
					setCellText(xTable, "E"+String.valueOf(i+3), ""+df.format(resumen.getDiputacion()));
					setCellText(xTable, "F"+String.valueOf(i+3), ""+df.format(resumen.getAyuntamiento()));
					setCellText(xTable, "G"+String.valueOf(i+3), ""+df.format(resumen.getTotal()));
				}
				
				//Añadimos la celda final
				ResumePlanProvincial total = totalPlanComplemetario[0];

				setCellTextLasRow(xTable, "A"+String.valueOf(i+3), "");
				setCellTextLasRow(xTable, "B"+String.valueOf(i+3), total.getFinalidad());
				setCellTextLasRow(xTable, "C"+String.valueOf(i+3), ""+new Double(total.getObras()).intValue());
				setCellTextLasRow(xTable, "D"+String.valueOf(i+3), ""+df.format(total.getEstado()));
				setCellTextLasRow(xTable, "E"+String.valueOf(i+3), ""+df.format(total.getDiputacion()));
				setCellTextLasRow(xTable, "F"+String.valueOf(i+3), ""+df.format(total.getAyuntamiento()));
				setCellTextLasRow(xTable, "G"+String.valueOf(i+3), ""+df.format(total.getTotal()));
	        }
		}
	}
	
	private void insertaTabla3(IRuleContext rulectx , XComponent xComponent, String anio) throws ISPACException, Exception
	{
        PlanProvincialServicioProxy planService;
        planService = new PlanProvincialServicioProxy();
        ResumenPlanPorAyuntamiento[] resumenPlanPorAyuntamiento = planService.getResumenPlanPorAyuntamiento(Integer.parseInt(anio));
        ResumenPlanPorAyuntamiento[] totalPlanPorAyuntamiento = planService.getTotalResumenPlanPorAyuntamiento(Integer.parseInt(anio));        
        
		if (resumenPlanPorAyuntamiento.length>0){
		    //Busca la posición de la tabla y coloca el cursor ahí
			//Usaremos el localizador %TABLA3%
			XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
	        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
	        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
	        xSearchDescriptor.setSearchString("%TABLA3%");
	        XInterface xSearchInterface = null;
	        XTextRange xSearchTextRange = null;
	        xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
	        if (xSearchInterface != null) 
	        {
	        	//Cadena encontrada, la borro antes de insertar la tabla
		        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
		        xSearchTextRange.setString("");
		        
				//Inserta una tabla de 9 columnas y tantas filas
			    //como nuevas liquidaciones haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				
				//Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
				xTable.initialize(resumenPlanPorAyuntamiento.length+3, 6);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
				//Rellena la cabecera de la tabla				
				setHeaderCellText(xTable, "A1", "Ayuntamiento");				
				setHeaderCellText(xTable, "B1", "Habitantes\n01-01-2010");
				setHeaderCellText(xTable, "C1", "Aporación Diputación/MPT\n(95%)");
				setHeaderCellText(xTable, "E1", "Aportación\nAytos. 5%");
				setHeaderCellText(xTable, "F1", "TOTAL\nPlan "+anio);
				
				setHeaderCellText(xTable, "C2", "Importe\nPlan "+(Integer.parseInt(anio)-1));				
				setHeaderCellText(xTable, "D2", "Importe\nPlan "+anio);	

				XTextTableCursor xtc = xTable.createCursorByCellName("A1");
				xtc.goRight((short) 6, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("B1");
				xtc.goRight((short) 6, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("E1");
				xtc.goRight((short) 6, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("F1");
				xtc.goRight((short) 6, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("C1");
				xtc.goRight((short) 1, true);
				xtc.mergeRange();				
				
				//Rellena la tabla con los datos de las liquidaciones
				NumberFormat df = NumberFormat.getCurrencyInstance();
				
				int i;
				for(i =0; i<resumenPlanPorAyuntamiento.length;i++){
					ResumenPlanPorAyuntamiento resumen = resumenPlanPorAyuntamiento[i];					
					String nombre = resumen.getNombreayuntamiento();
					if(nombre.toUpperCase().contains("¥")){						        		
		        		nombre = nombre.replace("¥", "Ñ");						        		
		        	}
					
					setCellText(xTable, "A"+String.valueOf(i+3), ""+nombre);
					setCellText(xTable, "B"+String.valueOf(i+3), resumen.getHabitantes());
					setCellText(xTable, "C"+String.valueOf(i+3), ""+df.format(resumen.getDipumptant()));
					setCellText(xTable, "D"+String.valueOf(i+3), ""+df.format(resumen.getDipumptact()));					
					setCellText(xTable, "E"+String.valueOf(i+3), ""+df.format(resumen.getAportayuntamiento()));
					setCellText(xTable, "F"+String.valueOf(i+3), ""+df.format(resumen.getTotal()));
				}
				
				//Añadimos la celda final
				ResumenPlanPorAyuntamiento total = totalPlanPorAyuntamiento[0];

				setCellTextLasRow(xTable, "A"+String.valueOf(i+3), "");
				setCellTextLasRow(xTable, "B"+String.valueOf(i+3), total.getHabitantes());
				setCellTextLasRow(xTable, "C"+String.valueOf(i+3), ""+df.format(total.getDipumptant()));
				setCellTextLasRow(xTable, "D"+String.valueOf(i+3), ""+df.format(total.getDipumptact()));					
				setCellTextLasRow(xTable, "E"+String.valueOf(i+3), ""+df.format(total.getAportayuntamiento()));
				setCellTextLasRow(xTable, "F"+String.valueOf(i+3), ""+df.format(total.getTotal()));
	        }
		}
	}
	
	private void insertaTabla4(IRuleContext rulectx , XComponent xComponent, String anio) throws ISPACException, Exception
	{
        PlanProvincialServicioProxy planService;
        planService = new PlanProvincialServicioProxy();
        DatosCuadroMinisterio[] datosCuadroMinisterio = planService.getCuadroA(Integer.parseInt(anio));
        DatosCuadroMinisterio[] totalDatosCuadroMinisterio = planService.getTotalesCuadroA(Integer.parseInt(anio));        
        
		if (datosCuadroMinisterio.length>0){
		    //Busca la posición de la tabla y coloca el cursor ahí
			//Usaremos el localizador %TABLA4%
			XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
	        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
	        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
	        xSearchDescriptor.setSearchString("%TABLA4%");
	        XInterface xSearchInterface = null;
	        XTextRange xSearchTextRange = null;
//	        XTextTableCursor xtc = null;
	        xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
	        if (xSearchInterface != null) 
	        {
	        	//Cadena encontrada, la borro antes de insertar la tabla
		        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
		        xSearchTextRange.setString("");
		        
				//Inserta una tabla de 9 columnas y tantas filas
			    //como nuevas liquidaciones haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				
				
				//Calculamos las filas, son 3 seguras de la cabecera, más a partir de 8*2 3 más por cada 14*2 más una de los totales
				//Añadimos 4 filas más para las 3 de la cabecera de la tabla y uno para la celda final
				int filas = datosCuadroMinisterio.length*2;
				int filasTotal = filas;
				//primera cabecera
				filasTotal += 3;
				//Si hay más de 8 municipios otra cabecera
				if(filas>=16){
					filasTotal += 3;
					filas -= 16;
				}
				//Vemos cuantos grupos de 14 hay, una cabecera por cada uno
				int grupos = filas / 28;
				filasTotal += grupos * 3;
				
				//Añadimos los totales
				filasTotal++;
				
				xTable.initialize(filasTotal, 11);
				
//				xTable.initialize((datosCuadroMinisterio.length*2)+4, 11);
				
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
				colocaColumnas(xTable);
				
				Float tamanioFuente = new Float(7.0);
				
//				xtc = aniadirCabecera(xTable, tamanioFuente, indexFila, xtc);
								
				setHeaderCellText(xTable, "A1", "OBRA", tamanioFuente, ParagraphAdjust.CENTER);				
				setHeaderCellText(xTable, "E1", "PROGRAMA DE FINANCIACIÓN", tamanioFuente, ParagraphAdjust.CENTER);
				
				XTextTableCursor xtc = xTable.createCursorByCellName("A1");
				xtc.goRight((short) 3, true);
				xtc.mergeRange();				
				xtc = xTable.createCursorByCellName("B1");
				xtc.goRight((short) 6, true);
				xtc.mergeRange();

				setHeaderCellText(xTable, "A2", "Núm.", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "B2", "a) Código y denominación de la obra", tamanioFuente, ParagraphAdjust.LEFT);
				setHeaderCellText(xTable, "C2", "Puntos\nkilométricos", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "E2", "Presupuesto", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "F2", "PARTÍCIPES", tamanioFuente, ParagraphAdjust.CENTER);		

				setHeaderCellText(xTable, "B3", "b) Código y denominación del municipio", tamanioFuente, ParagraphAdjust.LEFT);				
				setHeaderCellText(xTable, "C3", "Inicial", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "D3", "Final", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "F3", "Administración\nGeneral del\nEstado (MPT)", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "G3", "Comunidad\nAutónoma", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "H3", "Diputación\nProvincial", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "I3", "Ayuntamiento", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "J3", "Unión Europea", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "K3", "Otros", tamanioFuente, ParagraphAdjust.CENTER);
		
				xtc = xTable.createCursorByCellName("A2");
				xtc.goRight((short) 11, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("E2");
				xtc.goRight((short) 11, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("C2");
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("E2");
				xtc.goRight((short) 5, true);
				xtc.mergeRange();
				
				//Rellena la tabla con los datos de las liquidaciones
				NumberFormat df = NumberFormat.getCurrencyInstance();
				
				int i=4;
				for(int contadorMunicipio = 0; contadorMunicipio<datosCuadroMinisterio.length;contadorMunicipio++){	
					DatosCuadroMinisterio cuadro = datosCuadroMinisterio[contadorMunicipio];						
					String nombre = cuadro.getMunicipio();
					if(nombre.toUpperCase().contains("¥")){						        		
		        		nombre = nombre.replace("¥", "Ñ");						        		
		        	}
					
					setCellText(xTable, "A"+String.valueOf(i), "" +(contadorMunicipio+1), tamanioFuente, ParagraphAdjust.CENTER);
					setCellText(xTable, "B"+String.valueOf(i), "a) " + cuadro.getDenoobra(), tamanioFuente, ParagraphAdjust.LEFT);
					xtc = xTable.createCursorByCellName("A"+i);
					xtc.goRight((short) 11, true);
					xtc.mergeRange();
					xtc = xTable.createCursorByCellName("B"+String.valueOf(i));
					xtc.goRight((short) 9, true);
					xtc.mergeRange();
					i++;
					setCellText(xTable, "B"+String.valueOf(i), "b) " + nombre, tamanioFuente, ParagraphAdjust.LEFT);
					setCellText(xTable, "E"+String.valueOf(i), "" + df.format(cuadro.getPresupuesto()), tamanioFuente, ParagraphAdjust.RIGHT);
					setCellText(xTable, "F"+String.valueOf(i), "" + df.format(cuadro.getEstado()), tamanioFuente, ParagraphAdjust.RIGHT);					
					setCellText(xTable, "H"+String.valueOf(i), "" + df.format(cuadro.getDiputacion()), tamanioFuente, ParagraphAdjust.RIGHT);
					setCellText(xTable, "I"+String.valueOf(i), "" + df.format(cuadro.getAyuntamiento()), tamanioFuente, ParagraphAdjust.RIGHT);
					i++;	
					if(contadorMunicipio+1 == 8 && datosCuadroMinisterio.length>contadorMunicipio+1){
						//xtc = aniadirCabecera(xTable, tamanioFuente, i, xtc);
//						i +=3;
						setHeaderCellText(xTable, "A"+i, "OBRA", tamanioFuente, ParagraphAdjust.CENTER);				
						setHeaderCellText(xTable, "E"+i, "PROGRAMA DE FINANCIACIÓN", tamanioFuente, ParagraphAdjust.CENTER);
						
						xtc = xTable.createCursorByCellName("A"+i);
						xtc.goRight((short) 3, true);
						xtc.mergeRange();				
						xtc = xTable.createCursorByCellName("B"+i);
						xtc.goRight((short) 6, true);
						xtc.mergeRange();
						
						i++;
						
						setHeaderCellText(xTable, "A"+i, "Núm.", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "B"+i, "a) Código y denominación de la obra", tamanioFuente, ParagraphAdjust.LEFT);
						setHeaderCellText(xTable, "C"+i, "Puntos\nkilométricos", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "E"+i, "Presupuesto", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "F"+i, "PARTÍCIPES", tamanioFuente, ParagraphAdjust.CENTER);		
				
						i++;
						
						setHeaderCellText(xTable, "B"+i, "b) Código y denominación del municipio", tamanioFuente, ParagraphAdjust.LEFT);				
						setHeaderCellText(xTable, "C"+i, "Inicial", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "D"+i, "Final", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "F"+i, "Administración\nGeneral del\nEstado (MPT)", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "G"+i, "Comunidad\nAutónoma", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "H"+i, "Diputación\nProvincial", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "I"+i, "Ayuntamiento", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "J"+i, "Unión Europea", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "K"+i, "Otros", tamanioFuente, ParagraphAdjust.CENTER);
				
						xtc = xTable.createCursorByCellName("A"+(i-1));
						xtc.goRight((short) 11, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("E"+(i-1));
						xtc.goRight((short) 11, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("C"+(i-1));
						xtc.goRight((short) 1, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("E"+(i-1));
						xtc.goRight((short) 5, true);
						xtc.mergeRange();
						i++;
					}
					else if(contadorMunicipio+1-8>=14 && (contadorMunicipio+1-8!=7 && (contadorMunicipio+1-8)%14 == 0)){
						//xtc = aniadirCabecera(xTable, tamanioFuente, i, xtc);
//						i +=3;
						setHeaderCellText(xTable, "A"+i, "OBRA", tamanioFuente, ParagraphAdjust.CENTER);				
						setHeaderCellText(xTable, "E"+i, "PROGRAMA DE FINANCIACIÓN", tamanioFuente, ParagraphAdjust.CENTER);
						
						xtc = xTable.createCursorByCellName("A"+i);
						xtc.goRight((short) 3, true);
						xtc.mergeRange();				
						xtc = xTable.createCursorByCellName("B"+i);
						xtc.goRight((short) 6, true);
						xtc.mergeRange();
						
						i++;
						
						setHeaderCellText(xTable, "A"+i, "Núm.", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "B"+i, "a) Código y denominación de la obra", tamanioFuente, ParagraphAdjust.LEFT);
						setHeaderCellText(xTable, "C"+i, "Puntos\nkilométricos", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "E"+i, "Presupuesto", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "F"+i, "PARTÍCIPES", tamanioFuente, ParagraphAdjust.CENTER);		
				
						i++;
						
						setHeaderCellText(xTable, "B"+i, "b) Código y denominación del municipio", tamanioFuente, ParagraphAdjust.LEFT);				
						setHeaderCellText(xTable, "C"+i, "Inicial", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "D"+i, "Final", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "F"+i, "Administración\nGeneral del\nEstado (MPT)", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "G"+i, "Comunidad\nAutónoma", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "H"+i, "Diputación\nProvincial", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "I"+i, "Ayuntamiento", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "J"+i, "Unión Europea", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "K"+i, "Otros", tamanioFuente, ParagraphAdjust.CENTER);
				
						xtc = xTable.createCursorByCellName("A"+(i-1));
						xtc.goRight((short) 11, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("E"+(i-1));
						xtc.goRight((short) 11, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("C"+(i-1));
						xtc.goRight((short) 1, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("E"+(i-1));
						xtc.goRight((short) 5, true);
						xtc.mergeRange();
						i++;
					}
				}
				
				//Añadimos la celda final
				DatosCuadroMinisterio total = totalDatosCuadroMinisterio[0];

				setCellTextLasRow(xTable, "A"+String.valueOf(i), total.getMunicipio(), tamanioFuente, ParagraphAdjust.RIGHT);				
				setCellTextLasRow(xTable, "E"+String.valueOf(i), ""+df.format(total.getPresupuesto()), tamanioFuente, ParagraphAdjust.RIGHT);
				setCellTextLasRow(xTable, "F"+String.valueOf(i), ""+df.format(total.getEstado()), tamanioFuente, ParagraphAdjust.RIGHT);
				setCellTextLasRow(xTable, "H"+String.valueOf(i), ""+df.format(total.getDiputacion()), tamanioFuente, ParagraphAdjust.RIGHT);
				setCellTextLasRow(xTable, "I"+String.valueOf(i), ""+df.format(total.getAyuntamiento()), tamanioFuente, ParagraphAdjust.RIGHT);
				
				xtc = xTable.createCursorByCellName("A"+String.valueOf(i));
				xtc.goRight((short) 3, true);
				xtc.mergeRange();				
	        }
		}
	}
	
	private void insertaTabla5(IRuleContext rulectx , XComponent xComponent, String anio) throws ISPACException, Exception
	{
        PlanProvincialServicioProxy planService;
        planService = new PlanProvincialServicioProxy();
        DatosCuadroMinisterio[] datosCuadroMinisterio = planService.getCuadroC(Integer.parseInt(anio));
        DatosCuadroMinisterio[] totalDatosCuadroMinisterio = planService.getTotalesCuadroC(Integer.parseInt(anio));        
        
		if (datosCuadroMinisterio.length>0){
		    //Busca la posición de la tabla y coloca el cursor ahí
			//Usaremos el localizador %TABLA5%
			XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
	        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
	        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
	        xSearchDescriptor.setSearchString("%TABLA5%");
	        XInterface xSearchInterface = null;
	        XTextRange xSearchTextRange = null;	 
//	        XTextTableCursor xtc = null;
	        xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
	        if (xSearchInterface != null) 
	        {
	        	//Cadena encontrada, la borro antes de insertar la tabla
		        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
		        xSearchTextRange.setString("");
		        
				//Inserta una tabla de 9 columnas y tantas filas
			    //como nuevas liquidaciones haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				
				
				//Calculamos las filas, son 3 seguras de la cabecera, más a partir de 8*2 3 más por cada 14*2 más una de los totales
				//Añadimos 4 filas más para las 3 de la cabecera de la tabla y uno para la celda final
				int filas = datosCuadroMinisterio.length*2;
				int filasTotal = filas;
				//primera cabecera
				filasTotal += 3;
				//Si hay más de 8 municipios otra cabecera
				if(filas>=16){
					filasTotal += 3;
					filas -= 16;
				}
				//Vemos cuantos grupos de 14 hay, una cabecera por cada uno
				int grupos = filas / 28;
				filasTotal += grupos * 3;
				
				//Añadimos los totales
				filasTotal++;
				
				xTable.initialize(filasTotal, 11);
				
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
				colocaColumnas(xTable);
				
				Float tamanioFuente = new Float(7.0);
				
//				xtc = aniadirCabecera(xTable, tamanioFuente, indexFila, xtc);
								
				setHeaderCellText(xTable, "A1", "OBRA", tamanioFuente, ParagraphAdjust.CENTER);				
				setHeaderCellText(xTable, "E1", "PROGRAMA DE FINANCIACIÓN", tamanioFuente, ParagraphAdjust.CENTER);
				
				XTextTableCursor xtc = xTable.createCursorByCellName("A1");
				xtc.goRight((short) 3, true);
				xtc.mergeRange();				
				xtc = xTable.createCursorByCellName("B1");
				xtc.goRight((short) 6, true);
				xtc.mergeRange();
				
				setHeaderCellText(xTable, "A2", "Núm.", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "B2", "a) Código y denominación de la obra", tamanioFuente, ParagraphAdjust.LEFT);
				setHeaderCellText(xTable, "C2", "Puntos\nkilométricos", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "E2", "Presupuesto", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "F2", "PARTÍCIPES", tamanioFuente, ParagraphAdjust.CENTER);		
				
				setHeaderCellText(xTable, "B3", "b) Código y denominación del municipio", tamanioFuente, ParagraphAdjust.LEFT);				
				setHeaderCellText(xTable, "C3", "Inicial", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "D3", "Final", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "F3", "Administración\nGeneral del\nEstado (MPT)", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "G3", "Comunidad\nAutónoma", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "H3", "Diputación\nProvincial", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "I3", "Ayuntamiento", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "J3", "Unión Europea", tamanioFuente, ParagraphAdjust.CENTER);
				setHeaderCellText(xTable, "K3", "Otros", tamanioFuente, ParagraphAdjust.CENTER);
		
				xtc = xTable.createCursorByCellName("A2");
				xtc.goRight((short) 11, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("E2");
				xtc.goRight((short) 11, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("C2");
				xtc.goRight((short) 1, true);
				xtc.mergeRange();
				xtc = xTable.createCursorByCellName("E2");
				xtc.goRight((short) 5, true);
				xtc.mergeRange();
				
				//Rellena la tabla con los datos de las liquidaciones
				NumberFormat df = NumberFormat.getCurrencyInstance();
				
				int i=4;
				for(int contadorMunicipio = 0; contadorMunicipio<datosCuadroMinisterio.length;contadorMunicipio++){		
					DatosCuadroMinisterio cuadro = datosCuadroMinisterio[contadorMunicipio];						
					String nombre = cuadro.getMunicipio();
					if(nombre.toUpperCase().contains("¥")){						        		
		        		nombre = nombre.replace("¥", "Ñ");						        		
		        	}
					
					setCellText(xTable, "A"+String.valueOf(i), "" +(contadorMunicipio+1), tamanioFuente, ParagraphAdjust.CENTER);
					setCellText(xTable, "B"+String.valueOf(i), "a) " + cuadro.getDenoobra(), tamanioFuente, ParagraphAdjust.LEFT);
					xtc = xTable.createCursorByCellName("A"+i);
					xtc.goRight((short) 11, true);
					xtc.mergeRange();
					xtc = xTable.createCursorByCellName("B"+String.valueOf(i));
					xtc.goRight((short) 9, true);
					xtc.mergeRange();
					i++;
					setCellText(xTable, "B"+String.valueOf(i), "b) " + nombre, tamanioFuente, ParagraphAdjust.LEFT);
					setCellText(xTable, "E"+String.valueOf(i), "" + df.format(cuadro.getPresupuesto()), tamanioFuente, ParagraphAdjust.RIGHT);
					setCellText(xTable, "F"+String.valueOf(i), "" + df.format(cuadro.getEstado()), tamanioFuente, ParagraphAdjust.RIGHT);					
					setCellText(xTable, "H"+String.valueOf(i), "" + df.format(cuadro.getDiputacion()), tamanioFuente, ParagraphAdjust.RIGHT);
					setCellText(xTable, "I"+String.valueOf(i), "" + df.format(cuadro.getAyuntamiento()), tamanioFuente, ParagraphAdjust.RIGHT);
					i++;	
					if(contadorMunicipio+1 == 8 && datosCuadroMinisterio.length>contadorMunicipio+1){
						//xtc = aniadirCabecera(xTable, tamanioFuente, i, xtc);
//						i +=3;
						setHeaderCellText(xTable, "A"+i, "OBRA", tamanioFuente, ParagraphAdjust.CENTER);				
						setHeaderCellText(xTable, "E"+i, "PROGRAMA DE FINANCIACIÓN", tamanioFuente, ParagraphAdjust.CENTER);
						
						xtc = xTable.createCursorByCellName("A"+i);
						xtc.goRight((short) 3, true);
						xtc.mergeRange();				
						xtc = xTable.createCursorByCellName("B"+i);
						xtc.goRight((short) 6, true);
						xtc.mergeRange();
						
						i++;
						
						setHeaderCellText(xTable, "A"+i, "Núm.", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "B"+i, "a) Código y denominación de la obra", tamanioFuente, ParagraphAdjust.LEFT);
						setHeaderCellText(xTable, "C"+i, "Puntos\nkilométricos", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "E"+i, "Presupuesto", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "F"+i, "PARTÍCIPES", tamanioFuente, ParagraphAdjust.CENTER);		
				
						i++;
						
						setHeaderCellText(xTable, "B"+i, "b) Código y denominación del municipio", tamanioFuente, ParagraphAdjust.LEFT);				
						setHeaderCellText(xTable, "C"+i, "Inicial", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "D"+i, "Final", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "F"+i, "Administración\nGeneral del\nEstado (MPT)", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "G"+i, "Comunidad\nAutónoma", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "H"+i, "Diputación\nProvincial", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "I"+i, "Ayuntamiento", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "J"+i, "Unión Europea", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "K"+i, "Otros", tamanioFuente, ParagraphAdjust.CENTER);
				
						xtc = xTable.createCursorByCellName("A"+(i-1));
						xtc.goRight((short) 11, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("E"+(i-1));
						xtc.goRight((short) 11, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("C"+(i-1));
						xtc.goRight((short) 1, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("E"+(i-1));
						xtc.goRight((short) 5, true);
						xtc.mergeRange();
						i++;
					}
					else if(contadorMunicipio+1-8>=14 && (contadorMunicipio+1-8!=7 && (contadorMunicipio+1-8)%14 == 0)){
						//xtc = aniadirCabecera(xTable, tamanioFuente, i, xtc);
//						i +=3;
						setHeaderCellText(xTable, "A"+i, "OBRA", tamanioFuente, ParagraphAdjust.CENTER);				
						setHeaderCellText(xTable, "E"+i, "PROGRAMA DE FINANCIACIÓN", tamanioFuente, ParagraphAdjust.CENTER);
						
						xtc = xTable.createCursorByCellName("A"+i);
						xtc.goRight((short) 3, true);
						xtc.mergeRange();				
						xtc = xTable.createCursorByCellName("B"+i);
						xtc.goRight((short) 6, true);
						xtc.mergeRange();
						
						i++;
						
						setHeaderCellText(xTable, "A"+i, "Núm.", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "B"+i, "a) Código y denominación de la obra", tamanioFuente, ParagraphAdjust.LEFT);
						setHeaderCellText(xTable, "C"+i, "Puntos\nkilométricos", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "E"+i, "Presupuesto", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "F"+i, "PARTÍCIPES", tamanioFuente, ParagraphAdjust.CENTER);		
				
						i++;
						
						setHeaderCellText(xTable, "B"+i, "b) Código y denominación del municipio", tamanioFuente, ParagraphAdjust.LEFT);				
						setHeaderCellText(xTable, "C"+i, "Inicial", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "D"+i, "Final", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "F"+i, "Administración\nGeneral del\nEstado (MPT)", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "G"+i, "Comunidad\nAutónoma", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "H"+i, "Diputación\nProvincial", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "I"+i, "Ayuntamiento", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "J"+i, "Unión Europea", tamanioFuente, ParagraphAdjust.CENTER);
						setHeaderCellText(xTable, "K"+i, "Otros", tamanioFuente, ParagraphAdjust.CENTER);
				
						xtc = xTable.createCursorByCellName("A"+(i-1));
						xtc.goRight((short) 11, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("E"+(i-1));
						xtc.goRight((short) 11, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("C"+(i-1));
						xtc.goRight((short) 1, true);
						xtc.mergeRange();
						xtc = xTable.createCursorByCellName("E"+(i-1));
						xtc.goRight((short) 5, true);
						xtc.mergeRange();
						i++;
					}
				}
				
				//Añadimos la celda final
				DatosCuadroMinisterio total = totalDatosCuadroMinisterio[0];

				setCellTextLasRow(xTable, "A"+String.valueOf(i), total.getMunicipio(), tamanioFuente, ParagraphAdjust.RIGHT);				
				setCellTextLasRow(xTable, "E"+String.valueOf(i), ""+df.format(total.getPresupuesto()), tamanioFuente, ParagraphAdjust.RIGHT);
				setCellTextLasRow(xTable, "F"+String.valueOf(i), ""+df.format(total.getEstado()), tamanioFuente, ParagraphAdjust.RIGHT);
				setCellTextLasRow(xTable, "H"+String.valueOf(i), ""+df.format(total.getDiputacion()), tamanioFuente, ParagraphAdjust.RIGHT);
				setCellTextLasRow(xTable, "I"+String.valueOf(i), ""+df.format(total.getAyuntamiento()), tamanioFuente, ParagraphAdjust.RIGHT);
				
				xtc = xTable.createCursorByCellName("A"+String.valueOf(i));
				xtc.goRight((short) 3, true);
				xtc.mergeRange();				
	        }
		}
	}
	
	private void setHeaderCellText(XTextTable xTextTable, String CellName, String strText) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(CellName));

		//Propiedades		
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharHeight", new Float(8.0));	
		xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
		xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
		xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)60));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)60));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
		xCPS.setPropertyValue("BackColor", new Integer(0xC0C0C0));
		
		//Texto de la celda
		xCellText.setString(strText);
	}	

    private void setCellText(XTextTable xTextTable, String CellName, String strText) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);    	
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharHeight", new Float(8.0));	
		xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
		xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

		//Texto de la celda
		xCellText.setString(strText);
	}
    
    private void setCellTextLasRow(XTextTable xTextTable, String CellName, String strText) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharHeight", new Float(8.0));
		xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
		xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
		xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

		//Texto de la celda
		xCellText.setString(strText);
	}
    
    
    private void setHeaderCellText(XTextTable xTextTable, String CellName, String strText, Float tamanioFuente, ParagraphAdjust alineacion) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(CellName));

		//Propiedades		
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharHeight", tamanioFuente);	
		xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
		xTPS.setPropertyValue("ParaAdjust", alineacion);
//		xTPS.setPropertyValue("ParaVertAlignment", alineacionVertical);
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)60));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)60));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
		xCPS.setPropertyValue("BackColor", new Integer(0xC0C0C0));
		
		//Texto de la celda
		xCellText.setString(strText);
	}	

    private void setCellText(XTextTable xTextTable, String CellName, String strText, Float tamanioFuente, ParagraphAdjust alineacion) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);    	
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharHeight", tamanioFuente);	
		xTPS.setPropertyValue("ParaAdjust", alineacion);
//		xTPS.setPropertyValue("ParaVertAlignment", alineacionVertical);
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

		//Texto de la celda
		xCellText.setString(strText);
	}
    
    private void setCellTextLasRow(XTextTable xTextTable, String CellName, String strText, Float tamanioFuente, ParagraphAdjust alineacion) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharHeight", tamanioFuente);
		xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
		xTPS.setPropertyValue("ParaAdjust", alineacion);
//		xTPS.setPropertyValue("ParaVertAlignment", alineacionVertical);
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

		//Texto de la celda
		xCellText.setString(strText);
	}
    private void colocaColumnas(XTextTable xTextTable){
    	
		XPropertySet xPS = ( XPropertySet ) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);
		 
		// Get table Width and TableColumnRelativeSum properties values
		int iWidth;
		try {
			iWidth = ( Integer ) xPS.getPropertyValue( "Width" );
			
    		short sTableColumnRelativeSum = ( Short ) xPS.getPropertyValue( "TableColumnRelativeSum" );
    		 
    		// Get table column separators
    		Object xObj = xPS.getPropertyValue( "TableColumnSeparators" );
    		 
    		TableColumnSeparator[] xSeparators = ( TableColumnSeparator[] )UnoRuntime.queryInterface(
    		    TableColumnSeparator[].class, xObj );

    		
    		//Calculamos el tamaño que le queremos dar a la celda
    		//Se empieza colocando de la última a la primera, por eso las primeras 7 son iguales 
    		double dRatio = ( double ) sTableColumnRelativeSum / ( double ) iWidth;
    		double dRelativeWidth = ( double ) 9000 * dRatio;
    		
    		// Last table column separator position
    		double dPosition = sTableColumnRelativeSum - dRelativeWidth;
    		 
    		// Set set new position for all column separators        
    		//Número de separadores
    		int i = xSeparators.length - 1;
    		//Las 8 úlitmas columnas miden los mismo
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		dPosition -= dRelativeWidth;
    		i--;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		dPosition -= dRelativeWidth;
    		i--;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		dPosition -= dRelativeWidth;
    		i--;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		dPosition -= dRelativeWidth;
    		i--;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		dPosition -= dRelativeWidth;
    		i--;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		dPosition -= dRelativeWidth;
    		i--;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		i--;
    		
    		//Las columnas de los puntos kilométricosvan a medir menos 7 a cada una
    		dRelativeWidth = ( double ) 7000 * dRatio;
    		dPosition -= dRelativeWidth;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		dPosition -= dRelativeWidth;
    		i--;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		i--;
    		
//	    		el Nombre del municipio = 32
    		dRelativeWidth = ( double ) 32000 * dRatio;
    		dPosition -= dRelativeWidth;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );	    		
    		i--;
    		
    		// Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
    		xPS.setPropertyValue( "TableColumnSeparators", xSeparators );	
		} catch (UnknownPropertyException e) {
			logger.error(e.getMessage(), e);
		} catch (WrappedTargetException e) {
			logger.error(e.getMessage(), e);
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		}
	}
    
    @SuppressWarnings("rawtypes")
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			String anio = "", anioAnterior = "", fechaPrimerPleno = "", fechaPrimerBop = "";
			String fechaPrimeraNotificacion = ""; 
			StringBuffer nregistros = new StringBuffer();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			String sqlQueryPart = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			IItemCollection participantes = entitiesAPI.queryEntities("DIPUCRPLANESPROVINCIALES", sqlQueryPart);
			Iterator it = (Iterator) participantes.iterator();
			if(it.hasNext()){
				IItem item = (IItem)it.next();
				anio = item.getString("ANIO");
				fechaPrimerPleno = item.getString("SEGUNDOPLENO");
				fechaPrimerBop = item.getString("SEGUNDOBOP");
			}
			if(anio == null){
				anio = "";
			}
			else{
				try{
					anioAnterior = ""+(Integer.parseInt(anio)-1);
				}
				catch(Exception e){anioAnterior = "";}
			}
			if(fechaPrimerPleno == null) fechaPrimerPleno = "";
			if(fechaPrimerBop == null) fechaPrimerBop = "";
			
			IItemCollection docs = entitiesAPI.getDocuments(rulectx.getNumExp(), "UPPER(NOMBRE) LIKE '%NOTIFICACIÓN AYUNTAMIENTOS APROBACIÓN OBRA%' AND COD_COTEJO IS NOT NULL AND NREG != NULL", "");
			Iterator it2 = (Iterator) docs.iterator();
			while(it2.hasNext()){
				IItem item = (IItem)it2.next();
				fechaPrimeraNotificacion = item.getString("FREG");
				nregistros.append(item.getString("NREG"));
				nregistros.append(",");
			}
			if(nregistros!= null && !nregistros.equals(""))
				nregistros.substring(0, nregistros.length());
			
			if(fechaPrimeraNotificacion == null) fechaPrimeraNotificacion = "";
			
			cct.setSsVariable("ANIO", anio);			
			cct.setSsVariable("FECHASEGUNDOPLENO", fechaPrimerPleno);
			cct.setSsVariable("FECHASEGUNDOBOP", fechaPrimerBop);
			cct.setSsVariable("ANIOANTERIOR", anioAnterior);
			cct.setSsVariable("FECHASEGUNDANOTIFICACION", fechaPrimeraNotificacion);
			cct.setSsVariable("NREGISTROSALIDASEGUNDANOTIF", nregistros.toString());			
		} catch (ISPACException e) {				
			logger.error(e.getMessage(), e);
		}
	}

	public void deleteSsVariables(IClientContext cct) {	
		try {
			cct.deleteSsVariable("ANIO");
			cct.deleteSsVariable("FECHASEGUNDOPLENO");
			cct.deleteSsVariable("FECHASEGUNDOBOP");
			cct.deleteSsVariable("ANIOANTERIOR");
			cct.deleteSsVariable("FECHASEGUNDANOTIFICACION");
			cct.deleteSsVariable("NREGISTROSALIDASEGUNDANOTIF");			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}	
}

package es.dipucr.sigem.api.rule.procedures.intervencion.obligacionesReconocidas;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExcelUtils;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrGeneraDocsObligacionesReconocidas implements IRule {
	
	private static Logger logger = Logger.getLogger(DipucrGeneraDocsObligacionesReconocidas.class);

	protected String refTablas ;
	
	protected String plantilla = "";
	protected String tipoDocumento = "";
	public IRuleContext ruleContext;
	
	File documentoExcel;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try{
			IClientContext cct = rulectx.getClientContext();
			
			plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
			
			if(StringUtils.isNotEmpty(plantilla)){
				tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
			}
			ruleContext = rulectx;
			refTablas = "%TABLA1%";
		}
		catch(ISPACException e){
			logger.error("Error al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al recuperar la plantilla específica del expediente. ");
		}
		logger.info("FIN - " + this.getClass().getName());
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		String numexp = rulectx.getNumExp();
		
		try{
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesflowAPI.getGenDocAPI();			
			Object connectorSession = genDocAPI.createConnectorSession();
			
			IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());

			String nombreAyto = "";
			String id_ext = "";
			
			int documentId = 0;
			int taskId = rulectx.getTaskId();
			int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, true);
			IItem template = DocumentosUtil.getPlantillaEspecifica(rulectx, numexp, plantilla);
			int templateId = template.getInt("ID");
			
			String strInfoPag = DocumentosUtil.getInfoPagByNombre(numexp, rulectx, "Hoja de Cálculo de Obligaciones Reconocidas");			
			documentoExcel = DocumentosUtil.getFile(rulectx.getClientContext(), strInfoPag, "", "");
			
			List<?>[] datos = recuperaTerceros(rulectx);
			List<?> listaNifs = (List<?>) datos[0];
			List<?> listaFilas = (List<?>) datos[1];
			
			for(int i=0; i< listaNifs.size(); i++){
				String nifAyto = (String) listaNifs.get(i);
				List<?> filasAyto = (List<?>) listaFilas.get(i);
				
				IItemCollection partAytoCollection = ParticipantesUtil.getParticipantes(cct, numexp, "NDOC = '" + nifAyto + "'", "");
				IItem partAyto = null;
				if(partAytoCollection.iterator().hasNext()) partAyto = (IItem) partAytoCollection.iterator().next();
				if(partAyto != null){
					nombreAyto = partAyto.getString("NOMBRE");
					id_ext = partAyto.getString("ID_EXT");
	
					if(StringUtils.isEmpty(nombreAyto)) nombreAyto = "";
					if(StringUtils.isEmpty(id_ext)) id_ext = "";
					
					cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
					cct.setSsVariable("NOMBREAYTO", nombreAyto);
		        	cct.setSsVariable("ANIO", "" + Calendar.getInstance().get(Calendar.YEAR));
		        	cct.setSsVariable("ANIOANT", "" + (Calendar.getInstance().get(Calendar.YEAR)-1));
		        	
		        	IItem entityDocumentT = genDocAPI.createTaskDocument(taskId, documentTypeId);
					int documentIdT = entityDocumentT.getKeyInt();
			
					IItem entityTemplateT = genDocAPI.attachTaskTemplate( connectorSession, taskId, documentIdT, templateId);
					
					String infoPagT = entityTemplateT.getString("INFOPAG");
					entityTemplateT.store(cct);
					
		        	IItem entityDocument = genDocAPI.createTaskDocument(taskId, documentTypeId);
					documentId = entityDocument.getKeyInt();
			
					String sFileTemplate = DocumentosUtil.getFile(cct,	infoPagT, null, null).getName();
			
					IItem entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);

					
					String docref = entityTemplate.getString("INFOPAG");
					String sMimetype = genDocAPI.getMimeType(connectorSession, docref);
					entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
					String templateDescripcion = entityTemplate.getString("DESCRIPCION");
					templateDescripcion = templateDescripcion + " - " + numexp + " - " + nifAyto + " - " + nombreAyto;
					entityTemplate.set("DESCRIPCION", templateDescripcion);
					entityTemplate.set("DESTINO", nombreAyto);
					entityTemplate.set("DESTINO_ID", id_ext);
			
					entityTemplate.store(cct);
											
					if(refTablas != null && !refTablas.equals("")){						
						insertaTablas(genDocAPI, docref, rulectx, documentId, refTablas, entitiesAPI, numexp, filasAyto);
					}
					
					cct.deleteSsVariable("NOMBRE_TRAMITE");
					cct.deleteSsVariable("NOMBREAYTO");
		        	cct.deleteSsVariable("ANIO");
		        	cct.deleteSsVariable("ANIOANT");
		        	
					entityTemplateT.delete(cct);
					entityDocumentT.delete(cct);
		
					DocumentosUtil.deleteFile(sFileTemplate);
				}
			}
    		documentoExcel.delete();
		}
		catch (ISPACException e) {
			logger.error("ERROR al generar los documentos de obligaciones reconocidas. Numexp: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR No se han podido generar los documentos de obligaciones reconocidas. ");
		}
		catch (Exception e) {
			logger.error("ERROR al generar los documentos de obligaciones reconocidas. Numexp: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR No se han podido generar los documentos de obligaciones reconocidas. ");
		}
		return null;
	}
	
	public void insertaTablas(IGenDocAPI gendocAPI, String docref, IRuleContext rulectx, int documentId, String string, IEntitiesAPI entitiesAPI, String numexp, List<?> filasAyto) {
		
		Object connectorSession = null;
    	OpenOfficeHelper ooHelper = null;
    	try {
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
			
			String[] refTabla = refTablas.split(",");			
			for(int i = 0; i<refTabla.length; i++){						
				insertaTabla(rulectx, xComponent, refTabla[i], entitiesAPI, numexp, filasAyto);
			}
		    
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
			DocumentosUtil.deleteFile(fileName);
			if(in!=null) in.close();
			if(out != null) out.close();
			
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
			
    	} catch (ISPACException e) {
    		logger.error(e.getMessage(),e);
		} catch (FileNotFoundException e) {
    		logger.error(e.getMessage(),e);
		} catch (Exception e) {
    		logger.error(e.getMessage(),e);
		} catch (java.lang.Exception e) {
    		logger.error(e.getMessage(),e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
	}
	
	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp, List<?> filasAyto)
	{
		String numOperacion, importe, concepto = "";
		
		int numFilas = 1;
		
        try{      	
		    //Busca la posición de la tabla y coloca el cursor ahí
			//Usaremos el localizador %TABLA1%
			XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, component);
		    XText xText = xTextDocument.getText();
	        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, component);
	        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
	        xSearchDescriptor.setSearchString("%TABLA1%");
	        XInterface xSearchInterface = null;
	        XTextRange xSearchTextRange = null;
	        xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
	        XTextTable xTable = null;
	        	       
	        if (xSearchInterface != null) 
	        {
	        	//Cadena encontrada, la borro antes de insertar la tabla
		        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
		        xSearchTextRange.setString("");
		        
				//Inserta una tabla de 4 columnas y tantas filas
			    //como nuevas liquidaciones haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				
				//Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final

				numFilas = filasAyto.size();
						
				xTable.initialize(numFilas + 1, 3);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
				
				colocaColumnas(xTable);

				//Rellena la cabecera de la tabla				
				setHeaderCellText(xTable, "A1", "Nº de operación");	
				setHeaderCellText(xTable, "B1", "Concepto");				
				setHeaderCellText(xTable, "C1", "Importe");								
								
				int cont = 2;
				
				for(Object dato: filasAyto){
					int numFila = (Integer)dato;
					List<String> fila = ExcelUtils.getRow(documentoExcel, 0, numFila);
					
					numOperacion = fila.get(0);				
					importe = fila.get(1);
					concepto = fila.get(4);
					
					Double dNumOperacion = new Double(numOperacion);
			    		
			    	setCellText(xTable, "A"+String.valueOf(cont), "" + dNumOperacion.longValue());
			    	setCellText(xTable, "B"+String.valueOf(cont), concepto);
			    	try{
			    		double dImporte = Double.parseDouble(importe);
			    		setCellText(xTable, "C"+String.valueOf(cont), new DecimalFormat("#,##0.00").format(dImporte));
			    	}
			    	catch(Exception e){
			    		setCellText(xTable, "C"+String.valueOf(cont), importe);
			    	}
			    	cont++;
				}
	        }
		} catch (IllegalArgumentException e) {
    		logger.error(e.getMessage(),e);
		} catch (Exception e) {
    		logger.error(e.getMessage(),e);
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
    		//Se empieza colocando de la última a la primera
    		double dRatio = ( double ) sTableColumnRelativeSum / ( double ) iWidth;
    		double dRelativeWidth = ( double ) 15000 * dRatio;
    		
    		// Last table column separator position
    		double dPosition = sTableColumnRelativeSum - dRelativeWidth;
    		 
    		// Set set new position for all column separators        
    		//Número de separadores
    		int i = xSeparators.length - 1;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		i--;    		
    		dRelativeWidth = ( double ) 80000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		    		
    		
    		// Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
    		xPS.setPropertyValue( "TableColumnSeparators", xSeparators );	
		} catch (UnknownPropertyException e) {
			logger.error(e.getMessage(),e);
		} catch (WrappedTargetException e) {
    		logger.error(e.getMessage(),e);
		} catch (PropertyVetoException e) {
    		logger.error(e.getMessage(),e);
		} catch (IllegalArgumentException e) {
    		logger.error(e.getMessage(),e);
		}
	}
    
	public List<?>[] recuperaTerceros(IRuleContext rulectx) throws ISPACRuleException, Exception{
    	List<?>[] resultado = new ArrayList[2];
    	List<List<Integer>> listaFilas= new ArrayList<List<Integer>>();
    	List<String> listaNifs = new ArrayList<String>();
    	
    	List<String> columna3 = ExcelUtils.getColumn(documentoExcel, 0, 2);
		int fila = 0;
		
		for(String nif : columna3){
			if(StringUtils.isNotEmpty(nif) && !nif.toUpperCase().trim().equals("TERCERO")){
				if(!listaNifs.contains(nif)){
					listaNifs.add(nif);
					List<Integer> datosFilas= new ArrayList<Integer>();
					datosFilas.add(fila);
					listaFilas.add(datosFilas);
				}
				else{
					int indice = listaNifs.indexOf(nif);
					List<Integer> dato = listaFilas.get(indice);    					
					dato.add(fila);
				}
			}
			fila++;
		}
    	resultado[0] = listaNifs;
    	resultado[1] = listaFilas;
    	return resultado;
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}

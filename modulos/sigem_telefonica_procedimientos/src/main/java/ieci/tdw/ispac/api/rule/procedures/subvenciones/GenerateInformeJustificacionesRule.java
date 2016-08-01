package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import com.sun.star.awt.FontWeight;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

public class GenerateInformeJustificacionesRule implements IRule {

	private OpenOfficeHelper ooHelper = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	String numexp_hijo = "";
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
	        //----------------------------------------------------------------------------------------------

			//Obtención de las solicitudes asociadas a la convocatoria
			int nSolicitudes = 0;
			String strQuery = "WHERE NUMEXP_PADRE='" + rulectx.getNumExp() + "'";
			IItemCollection relaciones = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator it = relaciones.iterator();
			while (it.hasNext())
			{
				IItem relacion = (IItem)it.next();
				
				numexp_hijo = relacion.getString("NUMEXP_HIJO");
				IItemCollection solicitudes = entitiesAPI.getEntities("SUBV_SOLICITUD", numexp_hijo);
				Iterator it2 = solicitudes.iterator();
				if(it2.hasNext())
				{
					IItem solicitud = (IItem)it2.next();
					String strJustificada = solicitud.getString("JUSTIFICADA");
					if (strJustificada!=null && strJustificada.compareTo("SI")==0)
					{	
						nSolicitudes++;
					}
				}
			}

			//Creación de variables de sesión para las plantillas
			int taskId = rulectx.getTaskId();
			ITask iTask = invesFlowAPI.getTask(taskId);
			String strTaskName = iTask.getString("NOMBRE");
			cct.setSsVariable("NOMBRE_TRAMITE", strTaskName);
			
	        //Generación del informe a partir de la plantilla
			String strTpDocName = "Informe del servicio";
			String strTemplateName = "Informe de justificaciones";
	        CommonFunctions.generarDocumento(rulectx, strTpDocName, strTemplateName, "previo");
			
	        //Abro el documento con OppenOffice para añadir la tabla
        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, strTemplateName+" - previo");
        	File file1 = CommonFunctions.getFile(rulectx, strInfoPag);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
			
    		//Busca la posición de la tabla y coloca el cursor ahí
    		//Usaremos el localizador %TABLA1%
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
		    
				//Inserta una tabla de 4 columnas y tantas filas
			    //como solicitudes haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				xTable.initialize(nSolicitudes + 1, 4);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
				//adjustColumnsWidth(xTable);
				
				//Rellena la cabecera de la tabla
				setHeaderCellText(xTable, "A1", "AYUNTAMIENTO");				
				setHeaderCellText(xTable, "B1", "C.I.F.");				
				setHeaderCellText(xTable, "C1", "ACTIVIDAD");				
				setHeaderCellText(xTable, "D1", "IMPORTE");				
				
				//Rellena la tabla con los datos de las solicitudes
				strQuery = "WHERE NUMEXP_PADRE='" + rulectx.getNumExp() + "'";
				relaciones = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
				it = relaciones.iterator();
				int nSolicitud = 0;
				while (it.hasNext())
				{
					IItem relacion = (IItem)it.next();
					
					numexp_hijo = relacion.getString("NUMEXP_HIJO");
					IItemCollection solicitudes = entitiesAPI.getEntities("SUBV_SOLICITUD", numexp_hijo);
					Iterator it2 = solicitudes.iterator();
					if(it2.hasNext())
					{
						IItem solicitud = (IItem)it2.next();
						IItemCollection entidades = entitiesAPI.getEntities("SUBV_ENTIDAD", numexp_hijo);
						IItem entidad = (IItem)entidades.iterator().next();

						String strJustificada = solicitud.getString("JUSTIFICADA");
						if (strJustificada!=null && strJustificada.compareTo("SI")==0)
						{	
							nSolicitud++;
	
							setCellText(xTable, "A"+String.valueOf(nSolicitud+1), entidad.getString("NOMBRE"));
							setCellText(xTable, "B"+String.valueOf(nSolicitud+1), entidad.getString("CIF"));
							setCellText(xTable, "C"+String.valueOf(nSolicitud+1), solicitud.getString("PROYECTO"));
							setCellText(xTable, "D"+String.valueOf(nSolicitud+1), solicitud.getString("CANTIDAD_RECIBIDA"));
						}
					}
				}
	        }	
    		//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			File file = new File(fileName);
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
    		file1.delete();
    		
    		//Guarda el resultado en gestor documental
			strQuery = "WHERE NOMBRE = '"+strTpDocName+"'";
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
	        it = collection.iterator();
	        int tpdoc = 0;
	        if (it.hasNext())
	        {
	        	IItem tpd = (IItem)it.next();
	        	tpdoc = tpd.getInt("ID");
	        }
    		IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
    		FileInputStream in = new FileInputStream(file);
    		int docId = newdoc.getInt("ID");
    		Object connectorSession = gendocAPI.createConnectorSession();
    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strTemplateName);
    		entityDoc.set("EXTENSION", "doc");
    		entityDoc.store(cct);
    		file.delete();
			
    		//Borra el documento previo del gestor documental
			strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" +
				" AND DESCRIPCION='" + strTemplateName + " - previo'" ;
	        collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
	        
	        //Borra las variables de sesion utilizadas
			cct.deleteSsVariable("NOMBRE_TRAMITE");

        	return new Boolean(true);
        }
    	catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido generar el Informe del servicio.",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

//	private void adjustColumnsWidth(XTextTable xTable) throws ISPACRuleException
//	{
//		try
//		{
//			// Los anchos se ajustan moviendo los separadores de columnas de la tabla
//			XPropertySet xPS = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, xTable);
//			Object xObj = xPS.getPropertyValue( "TableColumnSeparators" );
//			TableColumnSeparator[] xSeparators = (TableColumnSeparator[])UnoRuntime.queryInterface(TableColumnSeparator[].class, xObj);
//			short sTableColumnRelativeSum = ((Short)xPS.getPropertyValue("TableColumnRelativeSum")).shortValue();
//			double dPosition = (double)0.17 * sTableColumnRelativeSum;
//			xSeparators[0].Position = (short) Math.ceil( dPosition );
//		    dPosition = (double)0.65 * sTableColumnRelativeSum;
//			xSeparators[1].Position = (short) Math.ceil( dPosition );
//			xPS.setPropertyValue("TableColumnSeparators", xSeparators);
//		}
//    	catch(Exception e) 
//        {
//        	if (e instanceof ISPACRuleException)
//        	{
//			    throw new ISPACRuleException(e);
//        	}
//        	throw new ISPACRuleException("Fallo al editar la tabla",e);
//        }
//	}
	
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
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)60));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)60));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
		

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
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

		//Texto de la celda
		xCellText.setString(strText);
	}	

}

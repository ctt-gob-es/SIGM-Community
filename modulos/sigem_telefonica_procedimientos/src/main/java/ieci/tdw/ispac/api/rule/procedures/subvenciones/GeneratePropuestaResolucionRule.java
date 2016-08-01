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

public class GeneratePropuestaResolucionRule implements IRule {

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

			//Obtención de las solicitudes admitidas asociadas a la convocatoria
			int nSolicitudesAdmitidas = 0;
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
					nSolicitudesAdmitidas++;
				}
			}

			//Obtención de las solicitudes rechazadas asociadas a la convocatoria
			int nSolicitudesRechazadas = 0;
			strQuery = "WHERE NUMEXP_PADRE='" + rulectx.getNumExp() + "'";
			relaciones = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			it = relaciones.iterator();
			while (it.hasNext())
			{
				IItem relacion = (IItem)it.next();
				
				numexp_hijo = relacion.getString("NUMEXP_HIJO");
				IItemCollection solicitudes = entitiesAPI.getEntities("SUBV_SOLICITUD", numexp_hijo);
				Iterator it2 = solicitudes.iterator();
				if(it2.hasNext())
				{
					IItem solicitud = (IItem)it2.next();
					String strMotivo = solicitud.getString("MOTIVO_DENEGACION");
					if (strMotivo!=null && strMotivo.length()>0)
					{	
						nSolicitudesRechazadas++;
					}
				}
			}

			//Creación de variables de sesión para las plantillas
			int taskId = rulectx.getTaskId();
			ITask iTask = invesFlowAPI.getTask(taskId);
			String strTaskName = iTask.getString("NOMBRE");
			cct.setSsVariable("NOMBRE_TRAMITE", strTaskName);
			
	        //Generación del informe a partir de la plantilla
			String strTpDocName = "Propuesta de resolución";
			String strTemplateName = "Propuesta de resolución convocatoria";
	        CommonFunctions.generarDocumento(rulectx, strTpDocName, strTemplateName, "previo");
			
	        //Abro el documento con OppenOffice para añadir las tablas
        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, strTemplateName+" - previo");
        	File file1 = CommonFunctions.getFile(rulectx, strInfoPag);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
			
    		//Inserto la tabla 1 en el lugar indicado por el localizador %TABLA1%
	        XSearchable xSearchable1 = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
	        XSearchDescriptor xSearchDescriptor1 = xSearchable1.createSearchDescriptor();
	        xSearchDescriptor1.setSearchString("%TABLA1%");
	        XInterface xSearchInterface1 = null;
	        XTextRange xSearchTextRange1 = null;
	        xSearchInterface1 = (XInterface)xSearchable1.findFirst(xSearchDescriptor1);
	        if (xSearchInterface1 != null) 
	        {
	        	//Cadena encontrada, la borro antes de insertar la tabla
		        xSearchTextRange1 = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface1);
		        xSearchTextRange1.setString("");
		    
				//Inserta una tabla de 8 columnas y tantas filas
			    //como solicitudes haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				xTable.initialize(nSolicitudesAdmitidas + 1, 8);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange1, xTextContent, false);
	
				//adjustColumnsWidth(xTable);
				
				//Rellena la cabecera de la tabla
				setHeaderCellText(xTable, "A1", "Solicitante");				
				setHeaderCellText(xTable, "B1", "Criterio 1");				
				setHeaderCellText(xTable, "C1", "Criterio 2");				
				setHeaderCellText(xTable, "D1", "Criterio 3");				
				setHeaderCellText(xTable, "E1", "Criterio 4");				
				setHeaderCellText(xTable, "F1", "Criterio 5");				
				setHeaderCellText(xTable, "G1", "Total puntos");				
				setHeaderCellText(xTable, "H1", "Total subvención");				
				
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
						nSolicitud++;

						IItem solicitud = (IItem)it2.next();
						IItemCollection entidades = entitiesAPI.getEntities("SUBV_ENTIDAD", numexp_hijo);
						IItem entidad = (IItem)entidades.iterator().next();
	
						setCellText(xTable, "A"+String.valueOf(nSolicitud+1), entidad.getString("NOMBRE"));
						setCellText(xTable, "B"+String.valueOf(nSolicitud+1), solicitud.getString("CRITERIO1"));
						setCellText(xTable, "C"+String.valueOf(nSolicitud+1), solicitud.getString("CRITERIO2"));
						setCellText(xTable, "D"+String.valueOf(nSolicitud+1), solicitud.getString("CRITERIO3"));
						setCellText(xTable, "E"+String.valueOf(nSolicitud+1), solicitud.getString("CRITERIO4"));
						setCellText(xTable, "F"+String.valueOf(nSolicitud+1), solicitud.getString("CRITERIO5"));
						setCellText(xTable, "G"+String.valueOf(nSolicitud+1), solicitud.getString("TOTAL_PUNTOS"));
						setCellText(xTable, "H"+String.valueOf(nSolicitud+1), solicitud.getString("CANTIDAD_RECIBIDA"));
					}
				}
	        }	

    		//Inserto la tabla 2 en el lugar indicado por el localizador %TABLA2%
	        XSearchable xSearchable2 = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, xComponent);
	        XSearchDescriptor xSearchDescriptor2 = xSearchable2.createSearchDescriptor();
	        xSearchDescriptor2.setSearchString("%TABLA2%");
	        XInterface xSearchInterface2 = null;
	        XTextRange xSearchTextRange2 = null;
	        xSearchInterface2 = (XInterface)xSearchable2.findFirst(xSearchDescriptor2);
	        if (xSearchInterface2 != null) 
	        {
	        	//Cadena encontrada, la borro antes de insertar la tabla
		        xSearchTextRange2 = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface2);
		        xSearchTextRange2.setString("");
		    
				//Inserta una tabla de 2 columnas y tantas filas
			    //como solicitudes haya mas una de cabecera
				XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				xTable.initialize(nSolicitudesRechazadas + 1, 2);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange2, xTextContent, false);
	
				//adjustColumnsWidth(xTable);
				
				//Rellena la cabecera de la tabla
				setHeaderCellText(xTable, "A1", "ENTIDAD LOCAL");				
				setHeaderCellText(xTable, "B1", "MOTIVO DE LA DENEGACIÓN");				
				
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
						
						String strMotivo = solicitud.getString("MOTIVO_DENEGACION");
						if (strMotivo!=null && strMotivo.length()>0)
						{	
							nSolicitud++;
							setCellText(xTable, "A"+String.valueOf(nSolicitud+1), entidad.getString("NOMBRE"));
							setCellText(xTable, "B"+String.valueOf(nSolicitud+1), strMotivo);
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

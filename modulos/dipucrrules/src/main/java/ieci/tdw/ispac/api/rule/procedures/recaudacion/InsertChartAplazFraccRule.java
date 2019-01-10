package ieci.tdw.ispac.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.docs.tags.HidePwdTagRule;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

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

/**
 * Regla que añade la tabla de detalle en la propuesta de fraccionamiento estimatorio
 *
 */
public class InsertChartAplazFraccRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(HidePwdTagRule.class);
	private OpenOfficeHelper ooHelper = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
        try
        {
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        IGenDocAPI gendocAPI = invesFlowAPI.getGenDocAPI();
	        
	        //Busca el infopag del último documento del trámite actual
	        IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE = '" + rulectx.getTaskId() + "'", "FDOC DESC");
	        Iterator it = collection.iterator();
	        if (!it.hasNext())
	        {
	        	logger.error("No se encuentran documentos del expediente " + rulectx.getNumExp() + " y trámite " + rulectx.getTaskId() + ".");
	        	return new Boolean(false);
	        }
        	IItem doc = (IItem)it.next();
        	int docId = doc.getInt("ID");
        	String strInfoPag = doc.getString("INFOPAG");
        	
        	//Solo seguimos si se ha usado la plantilla de fraccionamiento estimatorio
        	String strTemplateName = doc.getString("DESCRIPCION").toLowerCase();
        	if (!(strTemplateName.indexOf("fracc")>0) || !(strTemplateName.indexOf(" estim")>0) )
        	{
	        	logger.error("No es la plantilla adecuada");
	        	return new Boolean(false);
        	}
        	
        	//Abre el documento
			String extension = "odt";
			String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			OutputStream out = new FileOutputStream(fileName);
    		Object connectorSession = gendocAPI.createConnectorSession();
			gendocAPI.getDocument(connectorSession, strInfoPag, out);
			File file = new File(fileName);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
    		
    		//Realiza los cambios
    		insertaTabla(rulectx, xComponent);
		    
		    //Guarda el documento
			String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
			fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
    		File fileOut = new File(fileNameOut);
    		InputStream in = new FileInputStream(fileOut);
    		String mime = "application/vnd.oasis.opendocument.text";
    		gendocAPI.setDocument(connectorSession, docId, strInfoPag, in, (int)(fileOut.length()), mime);
    		
    		//Borra archivos temporales
    		if (null != file) {
    			file.delete();
    		}
    		if(null != fileOut){
    			fileOut.delete();
    		}
    		
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error en InsertChartAplazFraccRule.", e);
	    } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
        
        return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	@SuppressWarnings("rawtypes")
	private void insertaTabla(IRuleContext rulectx , XComponent xComponent) throws ISPACException, Exception
	{
		//Obtención de las liquidaciones
        ClientContext cct = (ClientContext) rulectx.getClientContext();
        IInvesflowAPI invesFlowAPI = cct.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		int nLiquidaciones = 0;
		String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' ORDER BY FRACCION ASC";
		IItemCollection liquidaciones = entitiesAPI.queryEntities("REC_DAT_FRACC", strQuery);
		Iterator it = liquidaciones.iterator();
		if (it.hasNext())
		{
			nLiquidaciones = liquidaciones.toList().size();
				
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
				xTable.initialize(nLiquidaciones + 1, 9);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
				//Rellena la cabecera de la tabla
				setHeaderCellText(xTable, "A1", "Fracción");				
				setHeaderCellText(xTable, "B1", "Cuota");				
				setHeaderCellText(xTable, "C1", "Intereses cuota");				
				setHeaderCellText(xTable, "D1", "Recargo");				
				setHeaderCellText(xTable, "E1", "Cuota Municipal");				
				setHeaderCellText(xTable, "F1", "Intereses Cuota Municipal");				
				setHeaderCellText(xTable, "G1", "Recargo Provincial");				
				setHeaderCellText(xTable, "H1", "Intereses Recargo Provincial");				
				setHeaderCellText(xTable, "I1", "Fecha fin de pago");				

				//Rellena la tabla con los datos de las liquidaciones
				int nLiquidacion = 0; 
				while (it.hasNext())
				{
					IItem liquidacion = (IItem)it.next();
					
					nLiquidacion++;

					setCellText(xTable, "A"+String.valueOf(nLiquidacion+1), liquidacion.getString("FRACCION"));
					setCellText(xTable, "B"+String.valueOf(nLiquidacion+1), liquidacion.getString("CUOTA"));
					setCellText(xTable, "C"+String.valueOf(nLiquidacion+1), liquidacion.getString("INTERESES_CUOTA"));
					setCellText(xTable, "D"+String.valueOf(nLiquidacion+1), liquidacion.getString("RECARGO"));
					setCellText(xTable, "E"+String.valueOf(nLiquidacion+1), liquidacion.getString("CUOTA_MUNICIPAL"));
					setCellText(xTable, "F"+String.valueOf(nLiquidacion+1), liquidacion.getString("INTERESES_CUOTA_MUNICIPAL"));
					setCellText(xTable, "G"+String.valueOf(nLiquidacion+1), liquidacion.getString("RECARGO_PROVINCIAL"));
					setCellText(xTable, "H"+String.valueOf(nLiquidacion+1), liquidacion.getString("INTERESES_RECARGO_PROVINCIAL"));
					Date date = liquidacion.getDate("FECHA_FIN_PAGO");
					String strDate = "";
					if (date != null)
					{
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						strDate = df.format(date);
					}
					setCellText(xTable, "I"+String.valueOf(nLiquidacion+1), strDate);
				}
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
		xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
		xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
		XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
		xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));

		//Texto de la celda
		xCellText.setString(strText);
	}	
	
}

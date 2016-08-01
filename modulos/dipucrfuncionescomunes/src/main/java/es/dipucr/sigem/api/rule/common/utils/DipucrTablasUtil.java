package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.awt.FontWeight;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.text.ParagraphVertAlign;
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

public class DipucrTablasUtil {

	private static final Logger logger = Logger.getLogger(DipucrTablasUtil.class);
	
	public static void insertaTabla1(XComponent xComponent, List<?> valores, int numeroColumnas, boolean pieTabla) throws ISPACException, Exception{
		logger.info("Inicio insertaTabla1");
		if (valores.size() > 0){
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
		        
		        XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
				Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
				XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
				
				//Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
				xTable.initialize(valores.size(), numeroColumnas+1);
				XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
				xText.insertTextContent(xSearchTextRange, xTextContent, false);
				
				ArrayList<?> fila1 = (ArrayList<?>) valores.get(0);
				for(int i = 0; i < fila1.size(); i++){
					String valorCelda = (String) (fila1.get(i) == null?"":fila1.get(i));
					DipucrTablasUtil.setHeaderCellText(xTable, DipucrTablasUtil.getNombreColumna(i)+"1", valorCelda, "Arial");
				}			
				
				//Resto de filas de la hoja de cálculo
				int numFilas = valores.size();
				if(pieTabla){
					numFilas = numFilas-1;
				}
				
				for(int i = 1; i < numFilas ;i++){
					List<?> valor = (List<?>) valores.get(i);	
					for(int j =0; j < valor.size(); j++){	
						String valorCelda = (String) (valor.get(j) == null?"":valor.get(j));
						DipucrTablasUtil.setCellText(xTable, DipucrTablasUtil.getNombreColumna(j)+""+(i+1), ""+valorCelda, "Arial");
					}
				}
				
				if(pieTabla){
					ArrayList<?> filaFin = (ArrayList<?>) valores.get(valores.size()-1);
					for(int i = 0; i < filaFin.size(); i++){
						String valorCelda = (String) (filaFin.get(i) == null?"":filaFin.get(i));
						DipucrTablasUtil.setCellTextLastRow(xTable, DipucrTablasUtil.getNombreColumna(i)+""+valores.size(), valorCelda, "Arial");
					}
				}
		        
	        }
		}
		logger.info("Fin insertaTabla1");
	}
	
	public static void pintaTabla(XComponent xComponent, List<?> valores, int numeroColumnas, boolean pieTabla) throws ISPACException, Exception
	{               
		if (valores.size() > 0){
			
			//Coloco el cursor al final del documento			
			XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    
			XTextCursor xTextCursor = xText.createTextCursor();
			xTextCursor.gotoRange(xText.getEnd(),false);

			XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
			Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
			XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
			
			//Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
			xTable.initialize(valores.size(), numeroColumnas+1);
			XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
			xText.insertTextContent(xTextCursor, xTextContent, false);
			
			ArrayList<?> fila1 = (ArrayList<?>) valores.get(0);
			for(int i = 0; i < fila1.size(); i++){
				String valorCelda = (String) (fila1.get(i) == null?"":fila1.get(i));
				DipucrTablasUtil.setHeaderCellText(xTable, DipucrTablasUtil.getNombreColumna(i)+"1", valorCelda, "Arial");
			}			
			
			//Resto de filas de la hoja de cálculo
			int numFilas = valores.size();
			if(pieTabla){
				numFilas = numFilas-1;
			}
			
			for(int i = 1; i < numFilas ;i++){
				List<?> valor = (List<?>) valores.get(i);	
				for(int j =0; j < valor.size(); j++){	
					String valorCelda = (String) (valor.get(j) == null?"":valor.get(j));
					DipucrTablasUtil.setCellText(xTable, DipucrTablasUtil.getNombreColumna(j)+""+(i+1), ""+valorCelda, "Arial");
				}
			}
			
			if(pieTabla){
				ArrayList<?> filaFin = (ArrayList<?>) valores.get(valores.size()-1);
				for(int i = 0; i < filaFin.size(); i++){
					String valorCelda = (String) (filaFin.get(i) == null?"":filaFin.get(i));
					DipucrTablasUtil.setCellTextLastRow(xTable, DipucrTablasUtil.getNombreColumna(i)+""+valores.size(), valorCelda, "Arial");
				}
			}
        }
	}
	
	public static void setHeaderCellText(XTextTable xTextTable, String CellName, String strText, String fuente) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(CellName));

		//Propiedades		
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
//		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharFontName", fuente);
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
	
	public static void setHeaderCellText(XTextTable xTextTable, String CellName, String strText, Float tamanioFuente, ParagraphAdjust alineacion, String fuente) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(CellName));

		//Propiedades		
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
//		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharFontName", fuente);
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
    
	public static void setCellTextLastRow(XTextTable xTextTable, String CellName, String strText, String fuente) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
//		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharFontName", fuente);
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

	public static void setCellTextLastRow(XTextTable xTextTable, String CellName, String strText, Float tamanioFuente, ParagraphAdjust alineacion, String fuente) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
//		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharFontName", fuente);
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

	public static void setCellText(XTextTable xTextTable, String CellName, String strText, String fuente) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);    	
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
//		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharFontName", fuente);
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
	
	public static void setCellText(XTextTable xTextTable, String CellName, String strText, Float tamanioFuente, ParagraphAdjust alineacion, String fuente) throws Exception 
    {
    	XCell xCell = xTextTable.getCellByName(CellName);    	
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);

		//Propiedades
		XTextCursor xTC = xCellText.createTextCursor();
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		xTPS.setPropertyValue("CharFontName", new String("Arial"));
		xTPS.setPropertyValue("CharFontName", fuente);
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

	public static String getNombreColumna(int i) {
		String resultado = "A";
		switch (i) {
			case 0:
				resultado = "A";
				break;
			case 1:
				resultado = "B";
				break;
			case 2:
				resultado = "C";
				break;
			case 3:
				resultado = "D";
				break;
			case 4:
				resultado = "E";
				break;
			case 5:
				resultado = "F";
				break;
			case 6:
				resultado = "G";
				break;
			case 7:
				resultado = "H";
				break;
			case 8:
				resultado = "I";
				break;
			case 9:
				resultado = "J";
				break;
			case 10:
				resultado = "K";
				break;
			case 11:
				resultado = "L";
				break;
			case 12:
				resultado = "M";
				break;
			case 13:
				resultado = "N";
				break;
			case 14:
				resultado = "0";
				break;
			case 15:
				resultado = "P";
				break;
			case 16:
				resultado = "Q";
				break;
			case 17:
				resultado = "R";
				break;
			case 18:
				resultado = "S";
				break;
			case 19:
				resultado = "T";
				break;
			case 20:
				resultado = "U";
				break;
			case 21:
				resultado = "V";
				break;
			case 22:
				resultado = "W";
				break;
			case 23:
				resultado = "X";
				break;
			case 24:
				resultado = "Y";
				break;
			case 25:
				resultado = "Z";
				break;
			case 26:
				resultado = "AA";
				break;
			default:
				break;
		}
		return resultado;
	}
}

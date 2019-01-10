package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.container.XIndexAccess;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XCellRangeAddressable;
import com.sun.star.sheet.XSheetCellCursor;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.sheet.XUsedAreaCursor;
import com.sun.star.table.CellContentType;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;

public class ExcelUtils {
	/**
	 * [Ticket #476 TCG] SIGEM Utilidades de Excel
	 * **/
	protected static final Logger LOGGER = Logger.getLogger(ExcelUtils.class);
	
	public static XSpreadsheets getHojas(File documentoExcel){
		XSpreadsheets xSheets = null;
		OpenOfficeHelper ooHelper = null;
	
		try{
			ooHelper = OpenOfficeHelper.getInstance();
			XComponent xComp = ooHelper.loadDocument("file://"+ documentoExcel.getPath());
			XSpreadsheetDocument xls = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class, xComp);
			xSheets = xls.getSheets();
			
		} catch (Exception e) {
			LOGGER.error("Error al recuperar el libro: " + documentoExcel.getName() + ". " + e.getMessage(), e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
		
		return xSheets;
	}
	
	
	
	/**
	* Función que devuelve el numero de hojas
	* @param input identificador del documento
	* @return numero de hojas.
	*/
	public static int getNumSheet(File documentoExcel){
		int numHojas = 1;
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			String[] names = xSheets.getElementNames();
			
			numHojas = names.length;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return numHojas;
	}
	
	
	
	/**
	* Función que devuelve el numero de columnas a partir de la hoja
	* @param File identificador del documento
	* @param input Numero de la hoja
	* @return numero de hojas.
	*/
	public static int getNumColumn(File documentoExcel, int hoja){
		int numColumn = 0;
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			// Obtenemos la hoja actual
			XSpreadsheet xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(hoja));
			
			XSheetCellCursor cursor1 = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor1); 
		    
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
		    
		    numColumn = endColumn;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return numColumn;
	}
	
	
	/**
	* Función que devuelve el numero de columnas a partir de la hoja
	* @param File identificador del documento
	* @param input Numero de la hoja
	* @return numero de hojas.
	*/
	public static int getNumRow(File documentoExcel, int hoja){
		int numFilas = 0;
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			// Obtenemos la hoja actual
			XSpreadsheet xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(hoja));
			
			XSheetCellCursor cursor1 = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor1);
		    
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
			
		    numFilas = endRow;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return numFilas;
	}
	
	//int(numHoja) findSheet(idDoc, valorBusqueda);
	public static int findSheet(File documentoExcel, String valorBusqueda){
		int numHoja = 0;
		boolean encontrado = false;
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			for (int i = 0; i < xIndexAccess.getCount() && !encontrado; i++) {
				try {
					// Obtenemos la hoja actual
					xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(i));
				} catch (IndexOutOfBoundsException e1) {
					LOGGER.error(e1.getMessage(), e1);
				} catch (WrappedTargetException e1) {
					LOGGER.error(e1.getMessage(), e1);
				}
			
				XSheetCellCursor cursor = xSheet.createCursor(); 
			    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
			    
			    xUsedAreaCursor.gotoStartOfUsedArea(false); 
			    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
		            xUsedAreaCursor); 
			    int startColumn = xCellRangeAddressable.getRangeAddress().StartColumn; 
			    int startRow = xCellRangeAddressable.getRangeAddress().StartRow;
		          
			    xUsedAreaCursor.gotoEndOfUsedArea(false); 
			    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
			    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
			    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
			    
			    XSheetCellCursor cursorGeneral = xSheet.createCursor(); 
			    
			 // Recorremos las celdas existentes por sus coordenadas
				for (int x = startColumn; x <= endColumn && !encontrado; x++) {
					for (int y = startRow; y <= endRow && !encontrado; y++) {
						// Obtemos la celda actual
						XCell cell = cursorGeneral.getCellByPosition(x, y);

						// Miramos el contenido de la celda
						if (cell.getType() == CellContentType.EMPTY) {
						} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
							// Celda con valor constante o formula: obtenemos su
							// valor
							String valorCelda = cell.getValue()+"";
							if(valorCelda.equals(valorBusqueda)){
								encontrado = true;
								numHoja = i;
							}
						} else if (cell.getType() == CellContentType.TEXT) {
							// Celda con texto: obtenemos su contenido
							XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
							if(cellText.getString().equals(valorBusqueda)){
								encontrado = true;
								numHoja = i;
							}
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return numHoja;
	}
	
	public static int findRow(File documentoExcel, int numHoja, String valorBusqueda){
		int numFila = 0;
		boolean encontrado = false;
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
	            xUsedAreaCursor); 
		    int startColumn = xCellRangeAddressable.getRangeAddress().StartColumn; 
		    int startRow = xCellRangeAddressable.getRangeAddress().StartRow;
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
		    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor();
		    
		 // Recorremos las celdas existentes por sus coordenadas
			for (int x = startColumn; x <= endColumn && !encontrado; x++) {
				for (int y = startRow; y <= endRow && !encontrado; y++) {
					// Obtemos la celda actual
					XCell cell = cursorGeneral.getCellByPosition(x, y);

					// Miramos el contenido de la celda
					if (cell.getType() == CellContentType.EMPTY) {
					} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
						// Celda con valor constante o formula: obtenemos su
						// valor
						String valorCelda = cell.getValue()+"";
						if(valorCelda.equals(valorBusqueda)){
							encontrado = true;
							numFila = y;
						}
					} else if (cell.getType() == CellContentType.TEXT) {
						// Celda con texto: obtenemos su contenido
						XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
						if(cellText.getString().equals(valorBusqueda)){
							encontrado = true;
							numFila = y;
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return numFila;
	}
	
	
	public static int findColumn(File documentoExcel, int numHoja, String valorBusqueda){
		int numColumn = 0;
		boolean encontrado = false;
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
	            xUsedAreaCursor); 
		    int startColumn = xCellRangeAddressable.getRangeAddress().StartColumn; 
		    int startRow = xCellRangeAddressable.getRangeAddress().StartRow;
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
		    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor(); 
		    
		 // Recorremos las celdas existentes por sus coordenadas
			for (int x = startColumn; x <= endColumn && !encontrado; x++) {
				for (int y = startRow; y <= endRow && !encontrado; y++) {
					// Obtemos la celda actual
					XCell cell = cursorGeneral.getCellByPosition(x, y);

					// Miramos el contenido de la celda
					if (cell.getType() == CellContentType.EMPTY) {
						// Celda vacia: no hacemos nada
					} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
						// Celda con valor constante o formula: obtenemos su
						// valor
						String valorCelda = cell.getValue()+"";
						if(valorCelda.equals(valorBusqueda)){
							encontrado = true;
							numColumn = x;
						}
					} else if (cell.getType() == CellContentType.TEXT) {
						// Celda con texto: obtenemos su contenido
						XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
						if(cellText.getString().equals(valorBusqueda)){
							encontrado = true;
							numColumn = x;
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return numColumn;
	}
	
	public static int findRowbyColumn(File documentoExcel, int numHoja, int numColumna, String valorBusqueda){
		int numFila = 0;
		boolean encontrado = false;
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
	            xUsedAreaCursor); 
		    int startRow = xCellRangeAddressable.getRangeAddress().StartRow;
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor();
		    
		 // Recorremos las celdas existentes por sus coordenadas
			for (int y = startRow; y <= endRow && !encontrado; y++) {
				// Obtemos la celda actual
				XCell cell = cursorGeneral.getCellByPosition(numColumna, y);

				// Miramos el contenido de la celda
				if (cell.getType() == CellContentType.EMPTY) {
					// Celda vacia: no hacemos nada
				} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
					// Celda con valor constante o formula: obtenemos su
					// valor
					String valorCelda = cell.getValue()+"";
					if(valorCelda.equals(valorBusqueda)){
						encontrado = true;
						numFila = y;
					}
				} else if (cell.getType() == CellContentType.TEXT) {
					// Celda con texto: obtenemos su contenido
					XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
					if(cellText.getString().equals(valorBusqueda)){
						encontrado = true;
						numFila = y;
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return numFila;
	}
	
	public static int findRowbyColumnVector(File documentoExcel, int numHoja, int []numColumna, String []valorBusqueda){
		int numFila = 0;
		boolean encontrado = false;
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
	            xUsedAreaCursor); 
		    int startRow = xCellRangeAddressable.getRangeAddress().StartRow;
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor();
		 // Recorremos las celdas existentes por sus coordenadas
		    
		    boolean [] vEncontrado = new boolean [numColumna.length]; 
			for (int y = startRow; y <= endRow && !encontrado; y++) {
				// Obtemos la celda actual
				
				for(int colum=0; colum<numColumna.length; colum++){
					XCell cell = cursorGeneral.getCellByPosition(numColumna[colum], y);
	
					// Miramos el contenido de la celda
					if (cell.getType() == CellContentType.EMPTY) {
						// Celda vacia: no hacemos nada
					} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
						// Celda con valor constante o formula: obtenemos su
						// valor
						String valorCelda = cell.getValue()+"";
						if(valorCelda.equals(valorBusqueda[colum])){
							vEncontrado[colum] = true;
							//encontrado = true;
							numFila = y;
						}
					} else if (cell.getType() == CellContentType.TEXT) {
						// Celda con texto: obtenemos su contenido
						XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
						String valor = cellText.getString();
						if(valor.equals(valorBusqueda[colum])){
							vEncontrado[colum] = true;
							//encontrado = true;
							numFila = y;
						}
					}
				}
				//Compruebo si estan todos a true y si es asi hemos encontrado la superficie
				boolean encoRefCatastral = false;
				//Compruebo primero si ha encontrado la referencia catastral que es en el campo 0
				if(vEncontrado[0]){
					encoRefCatastral = true;
					numFila = y;
				}
				encontrado = true;
				//Compruebo si es poligono, municipio, ... campos 1, 2, 3, 4
				for(int i = 1; i< vEncontrado.length && !encoRefCatastral; i++){
					encontrado = encontrado && vEncontrado[i];
				}
				if(encontrado){
					numFila = y;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return numFila;
	}
	
	public static String findValueBySheetRowColumn(File documentoExcel, int numHoja, int numColumna, int numFila){
		
		String value = "";

		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor();
		    // Recorremos las celdas existentes por sus coordenadas

			// Obtemos la celda actual
				
			XCell cell = cursorGeneral.getCellByPosition(numColumna, numFila);

			// Miramos el contenido de la celda
			if (cell.getType() == CellContentType.EMPTY) {
				// Celda vacia: no hacemos nada
			} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
				// Celda con valor constante o formula: obtenemos su
				// valor
				value = cell.getValue()+"";
				
			} else if (cell.getType() == CellContentType.TEXT) {
				// Celda con texto: obtenemos su contenido
				XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
				value=cellText.getString();
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return value;
	}
	
	public static int findColumnByRow(File documentoExcel, int numHoja, int numFila, String valorBusqueda){
		int numColumn = 0;
		boolean encontrado = false;
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
	            xUsedAreaCursor); 
		    int startColumn = xCellRangeAddressable.getRangeAddress().StartColumn; 
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor(); 
		    
		 // Recorremos las celdas existentes por sus coordenadas
			for (int x = startColumn; x <= endColumn && !encontrado; x++) {
			
				// Obtemos la celda actual
				XCell cell = cursorGeneral.getCellByPosition(x, numFila);

				// Miramos el contenido de la celda
				if (cell.getType() == CellContentType.EMPTY) {
					// Celda vacia: no hacemos nada
				} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
					// Celda con valor constante o formula: obtenemos su
					// valor
					String valorCelda = cell.getValue()+"";
					if(valorCelda.equals(valorBusqueda)){
						encontrado = true;
						numColumn = x;
					}
				} else if (cell.getType() == CellContentType.TEXT) {
					// Celda con texto: obtenemos su contenido
					XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
					if(cellText.getString().equals(valorBusqueda)){
						encontrado = true;
						numColumn = x;
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return numColumn;
	}
	
	public static List<String> getRow(File documentoExcel, int numHoja, int numFila){
		List<String> lRow = new ArrayList<String>();
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
	            xUsedAreaCursor); 
		    int startColumn = xCellRangeAddressable.getRangeAddress().StartColumn; 
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor();
		    
		 // Recorremos las celdas existentes por sus coordenadas
			for (int x = startColumn; x <= endColumn; x++) {
				// Obtemos la celda actual
				XCell cell = cursorGeneral.getCellByPosition(x, numFila);

				// Miramos el contenido de la celda
				if (cell.getType() == CellContentType.EMPTY) {
					lRow.add("");
				} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
					// Celda con valor constante o formula: obtenemos su
					// valor
					String valorCelda = cell.getValue()+"";
					lRow.add(valorCelda);
				} else if (cell.getType() == CellContentType.TEXT) {
					// Celda con texto: obtenemos su contenido
					XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
					lRow.add(cellText.getString());
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return lRow;
	}
	
	
	public static List<String> getColumn(File documentoExcel, int numHoja, int numColumn){
		List<String> lColumn = new ArrayList<String>();
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
	            xUsedAreaCursor); 
		    int startRow = xCellRangeAddressable.getRangeAddress().StartRow;
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor();
		    
		 // Recorremos las celdas existentes por sus coordenadas
		    for (int y = startRow; y <= endRow; y++) {
				// Obtemos la celda actual
				XCell cell = cursorGeneral.getCellByPosition(numColumn, y);

				// Miramos el contenido de la celda
				if (cell.getType() == CellContentType.EMPTY) {
					lColumn.add("");
				} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
					// Celda con valor constante o formula: obtenemos su
					// valor
					String valorCelda = cell.getValue()+"";
					lColumn.add(valorCelda);
				} else if (cell.getType() == CellContentType.TEXT) {
					// Celda con texto: obtenemos su contenido
					XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
					lColumn.add(cellText.getString());
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return lColumn;
	}
	
	public static List<List<List<String>>> getAll(File documentoExcel){
		List<List<List<String>>> lAllExcel = new ArrayList<List<List<String>>>();
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			for (int i = 0; i < xIndexAccess.getCount(); i++) {
				List<List<String>> hoja = new ArrayList<List<String>>();
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(i));
					
			
				XSheetCellCursor cursor = xSheet.createCursor(); 
			    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
			    
			    xUsedAreaCursor.gotoStartOfUsedArea(false); 
			    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
		            xUsedAreaCursor); 
			    int startColumn = xCellRangeAddressable.getRangeAddress().StartColumn; 
			    int startRow = xCellRangeAddressable.getRangeAddress().StartRow;
		          
			    xUsedAreaCursor.gotoEndOfUsedArea(false); 
			    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
			    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
			    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
			    
			    XSheetCellCursor cursorGeneral = xSheet.createCursor(); 
			    
			 // Recorremos las celdas existentes por sus coordenadas
			    for (int y = startRow; y <= endRow; y++) {
					List<String> fila = new ArrayList<String>();
					for (int x = startColumn; x <= endColumn; x++) {
						// Obtemos la celda actual
						XCell cell = cursorGeneral.getCellByPosition(x, y);

						// Miramos el contenido de la celda
						if (cell.getType() == CellContentType.EMPTY) {
							fila.add("");
						} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
							// Celda con valor constante o formula: obtenemos su
							// valor
							String valorCelda = cell.getValue()+"";
							fila.add(valorCelda);
						} else if (cell.getType() == CellContentType.TEXT) {
							// Celda con texto: obtenemos su contenido
							XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
							fila.add(cellText.getString());
						}
					}
					hoja.add(fila);
				}
			    lAllExcel.add(hoja);
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return lAllExcel;
	}
	
	public static List<List<String>> getAllBySheet(File documentoExcel, int hoja){
		List<List<String>> lAllExcel = new ArrayList<List<String>>();
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			// Obtenemos la hoja actual
			xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(hoja));
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int startColumn = xCellRangeAddressable.getRangeAddress().StartColumn; 
		    int startRow = xCellRangeAddressable.getRangeAddress().StartRow;
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
		    int endRow = xCellRangeAddressable.getRangeAddress().EndRow;
		    
		    XSheetCellCursor cursorGeneral = xSheet.createCursor();
		 // Recorremos las celdas existentes por sus coordenadas
		    for (int y = startRow; y <= endRow; y++) {
				List<String> fila = new ArrayList<String>();
				for (int x = startColumn; x <= endColumn; x++) {
					// Obtemos la celda actual
					XCell cell = cursorGeneral.getCellByPosition(x, y);

					// Miramos el contenido de la celda
					if (cell.getType() == CellContentType.EMPTY) {
						fila.add("");
					} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
						// Celda con valor constante o formula: obtenemos su
						// valor
						String valorCelda = cell.getValue()+"";
						//Comprobamos los decimales, si son .00 los quitamos.
						String [] vCelda = valorCelda.split("\\.");
						if(vCelda!=null && vCelda.length==2){
							String decimales = vCelda[1];
							if("0".equals(decimales)){
								valorCelda =  vCelda[0];
							}
						}
						fila.add(valorCelda);
					} else if (cell.getType() == CellContentType.TEXT) {
						// Celda con texto: obtenemos su contenido
						XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
						String scellText = cellText.getString();
						fila.add(scellText);
					}
				}
				lAllExcel.add(fila);
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return lAllExcel;
	}
	
	public static boolean isEmptyRow (File documentoExcel, int numHoja, int numFila){
		boolean isEmptyRow = false;
		
		try{
			XSpreadsheets xSheets = getHojas(documentoExcel);
			
			XIndexAccess xIndexAccess = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			
			XSpreadsheet xSheet = null;
			//Numero de la hoja la tenemos
			try {
				// Obtenemos la hoja actual
				xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess.getByIndex(numHoja));
			} catch (IndexOutOfBoundsException e1) {
				LOGGER.error(e1.getMessage(), e1);
			} catch (WrappedTargetException e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		
			XSheetCellCursor cursor = xSheet.createCursor(); 
		    XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor);
		    
		    xUsedAreaCursor.gotoStartOfUsedArea(false); 
		    XCellRangeAddressable xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, 
	            xUsedAreaCursor); 
		    int startColumn = xCellRangeAddressable.getRangeAddress().StartColumn; 
	          
		    xUsedAreaCursor.gotoEndOfUsedArea(false); 
		    xCellRangeAddressable = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, xUsedAreaCursor); 
		    int endColumn = xCellRangeAddressable.getRangeAddress().EndColumn;
		    
		 // Recorremos las celdas existentes por sus coordenadas
			for (int x = startColumn; x <= endColumn && !isEmptyRow; x++) {
				// Obtemos la celda actual
				XCell cell = cursor.getCellByPosition(x, numFila);

				// Miramos el contenido de la celda
				if (cell.getType() == CellContentType.EMPTY) {
					isEmptyRow = true;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return isEmptyRow;
	}
}

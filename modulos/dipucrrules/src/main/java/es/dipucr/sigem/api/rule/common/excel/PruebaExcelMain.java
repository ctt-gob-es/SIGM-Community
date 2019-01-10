package es.dipucr.sigem.api.rule.common.excel;

import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.log4j.Logger;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XCellRangeAddressable;
import com.sun.star.sheet.XSheetCellCursor;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellContentType;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;

public class PruebaExcelMain {
	
	private static final Logger LOGGER = Logger.getLogger(PruebaExcelMain.class);

	public void init(String filePath) {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(new File(filePath));
			contentReading(fs);
		} catch (IOException e) {
			LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
		}finally {
			try {
				fs.close();
			} catch (IOException e) {
				LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
			}
		}
	}
 
	//Returns the Headings used inside the excel sheet
	public void getHeadingFromXlsFile(Sheet sheet) {
		int columnCount = sheet.getColumns();
		for (int i = 0; i < columnCount; i++) {
			System.out.println(sheet.getCell(i, 0).getContents());
		}
	}
 
	public void contentReading(InputStream fileInputStream) {
		WorkbookSettings ws = null;
		Workbook workbook = null;
		Sheet s = null;
		Cell rowData[] = null;
		int rowCount = '0';
		int columnCount = '0';
		int totalSheet = 0;
 
		try {
			ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			workbook = Workbook.getWorkbook(fileInputStream, ws);
 
			totalSheet = workbook.getNumberOfSheets();
			if(totalSheet > 0) {
				System.out.println("Total Sheet Found:" + totalSheet);
				for(int j=0;j<totalSheet ;j++) {
					System.out.println("Sheet Name:" + workbook.getSheet(j).getName());
				}
			}
 
			//Getting Default Sheet i.e. 0
			s = workbook.getSheet(0);
 
			//Reading Individual Cell
			getHeadingFromXlsFile(s);
 
			//Total Total No Of Rows in Sheet, will return you no of rows that are occupied with some data
			System.out.println("Total Rows inside Sheet:" + s.getRows());
			rowCount = s.getRows();
 
			//Total Total No Of Columns in Sheet
			System.out.println("Total Column inside Sheet:" + s.getColumns());
			columnCount = s.getColumns();
 
			//Reading Individual Row Content
			for (int i = 0; i < rowCount; i++) {
				//Get Individual Row
				rowData = s.getRow(i);
				if (rowData[0].getContents().length() != 0) { // the first date column must not null
					for (int j = 0; j < columnCount; j++) {
						switch (j) {
						case 0:
							System.out.println("Employee Id:" + rowData[j].getContents());
						case 1:
							System.out.println("Employee Name:" + rowData[j].getContents());
						case 2:
							System.out.println("Employee Designation:" + rowData[j].getContents());
						default:
							break;
						}
					}
				}
			}
			workbook.close();			
		} catch (IOException e) {
			LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
		} catch (BiffException e) {
			LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
		}
	}
	
	public static StringBuilder textoExtraido(XComponent xComp) {
		// Variable donde almacenaremos el texto
	    StringBuilder text = new StringBuilder();
		try{
			
		    // Hacemos casting del documento cargado a hoja de calculo
		    XSpreadsheetDocument xls = (XSpreadsheetDocument)UnoRuntime.queryInterface(
			com.sun.star.sheet.XSpreadsheetDocument.class, xComp);
	
		    // Obtenemos las hojas del documento y las recorremos por nombre
		    XSpreadsheets sheets = xls.getSheets();
		    String[] sheetNames = sheets.getElementNames();
		    for( String sheetName : sheetNames )
		    {
		      // Obtenemos la hoja actual 
		      XSpreadsheet sheet = (XSpreadsheet)UnoRuntime.queryInterface( 
			  XSpreadsheet.class, sheets.getByName(sheetName) );
	
		      // Obtenemos un cursor que representa todas las celdas de la hoja
		      XSheetCellCursor cursor = sheet.createCursor();
	
		      // Obtenemos el numero de filas y columnas del cursor (hoja)
		      XCellRangeAddressable range = (XCellRangeAddressable)UnoRuntime.queryInterface( XCellRangeAddressable.class, cursor );
		      CellRangeAddress addr = range.getRangeAddress();
		      
		      System.out.println("COlumn. "+addr.StartColumn+ " fin. "+addr.EndColumn);
		      System.out.println("COlumn. "+addr.StartRow+ " fin. "+addr.EndRow);
	
		      // Recorremos las celdas existentes por sus coordenadas
		      for( int x=addr.StartColumn ; x<=addr.EndColumn ; x++ )
		      {
			for( int y=addr.StartRow ; y<=addr.EndRow ; y++ )
			{
		          // Obtemos la celda actual
			  XCell cell = cursor.getCellByPosition(x,y);
	
		          // Miramos el contenido de la celda
			  if( cell.getType()==CellContentType.EMPTY )
			  {
		            // Celda vacia: no hacemos nada
			  }
			  else
			  if( cell.getType()==CellContentType.VALUE || cell.getType()==CellContentType.FORMULA )
			  {
		            // Celda con valor constante o formula: obtenemos su valor
			    text.append(" ");
			    text.append(cell.getValue());
			  }
			  else
			  if( cell.getType()==CellContentType.TEXT )
			  {
		            // Celda con texto: obtenemos su contenido
			    XText cellText = (XText)UnoRuntime.queryInterface(XText.class,cell);
			    text.append(" ");
			    text.append( cellText.getString() );
			  }
			}
		      }
		    }
	
		    // Imprimimos el resultado de la extraccion
		    System.out.println(text.toString());
		}
		catch (NoSuchElementException e) {
			LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
		} catch (WrappedTargetException e) {
			LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
		}

		return text;
	}
 
	public static void main(String[] args) {
		OpenOfficeHelper ooHelper = null;	
		try {
//			PruebaExcelMain xlReader = new PruebaExcelMain();
//			//xlReader.init("C:\\Users\\Teresa\\Desktop\\borrar\\InformeservicioTERE.xls");
//			xlReader.init("C:\\Users\\Teresa\\Desktop\\borrar\\prueba.ods");
			File file = new File ("C:\\Users\\Teresa\\Desktop\\borrar\\InformeservicioTERE.xls");
			
			ooHelper = OpenOfficeHelper.getInstance();
		    XComponent xComp = ooHelper.loadDocument("file://" + file.getPath());
		    
		    textoExtraido(xComp);
			 // Extraer el texto (esta parte depende del tipo de archivo que hayamos cargado). 
		    // Consultar los siguientes tres apartados para ver el codigo que habria que introducir aqui
		    //StringBuilder sbTextExt = textoExtraido(xComp);
		} catch (Exception e) {
			LOGGER.error("ERROR en PruebaExcelMain. " + e.getMessage(), e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
	}


}

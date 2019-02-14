package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.text.ParagraphVertAlign;
import com.sun.star.text.TableColumnSeparator;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XPageCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;


public class LibreOfficeUtil {

	public interface Fuentes{
		public static final String ARIAL = "Arial";
	}

	/**
	 * Mapeo de los tags de las referencias a las tablas
	 */
	public interface ReferenciasTablas{
		public static final String TABLA1 = "%TABLA1%";
		public static final String TABLA2 = "%TABLA2%";
		public static final String TABLA3 = "%TABLA3%";
		public static final String TABLA4 = "%TABLA4%";
		public static final String TABLA5 = "%TABLA5%";
		public static final String TABLA6 = "%TABLA6%";
		public static final String TABLA7 = "%TABLA7%";
		public static final String TABLA8 = "%TABLA8%";
		public static final String TABLA9 = "%TABLA9%";
	}

	public static String[] columnasCabeceraTablas = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	public static Logger LOGGER = Logger.getLogger(LibreOfficeUtil.class);
	
	//Busca la posición dentro del coumento, borra la cadena buscada en el documento y coloca el cursor ahí
	public static XTextRange buscaPosicion(XComponent component, String posicionBuscar){
		//Busca la posición de la tabla y coloca el cursor ahí	    
	    XTextRange posicionTabla = null;	    
	    
	    XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, component);
	    XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
	    xSearchDescriptor.setSearchString(posicionBuscar);
	    XInterface xSearchInterface = null;
	    xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
	    
	    if (xSearchInterface != null){
	        //Cadena encontrada, la borro antes de insertar la tabla
	        posicionTabla = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
	        posicionTabla.setString("");
	    }
	    return posicionTabla;
	}

	public static void colocaColumnas(XTextTable xTextTable, List<Integer> distribucionColumnas){
		
		double[] distribucionColumnasArray = new double[distribucionColumnas.size()];
		
		for (int i = 0; i < distribucionColumnas.size(); i++){
			distribucionColumnasArray[i] = distribucionColumnas.get(i);
		}
		
		colocaColumnas(xTextTable, distribucionColumnasArray);
	}
	
	/**
	 * Método que coloca las columnas de una tabla
	 * @param xTextTable
	 * @param distribucionColumnas vector con la distribución de las columnas en % y ya en orden de izquierda a derecha
	 *  p.e.: double [] distribucionColumnas = { 10, 20, 60, 10};
	 */
	public static void colocaColumnas(XTextTable xTextTable, double[] distribucionColumnas){
	    
	    XPropertySet xPS = ( XPropertySet ) UnoRuntime.queryInterface(XPropertySet.class, xTextTable);
	    int iWidth;
	    try {
	        iWidth = ( Integer ) xPS.getPropertyValue( "Width" );
	        
	        short dPosition = ( Short ) xPS.getPropertyValue( "TableColumnRelativeSum" );
	        double dRatio = ( double ) dPosition / ( double ) iWidth;
	        
	        double dRelativeWidth;            
	         
	        Object xObj = xPS.getPropertyValue( "TableColumnSeparators" );            
	        TableColumnSeparator[] xSeparators = ( TableColumnSeparator[] )UnoRuntime.queryInterface(TableColumnSeparator[].class, xObj );
	        int numSeparadores = xSeparators.length;
	        
	        for (int i = numSeparadores; i > 0; i--){
	            if(i < distribucionColumnas.length){
	            	dRelativeWidth = ( double ) (distribucionColumnas[i] * iWidth / 100) * dRatio;	            
	            	dPosition -= dRelativeWidth;
	            	xSeparators[i-1].Position = (short) Math.ceil( dPosition );
	            }
	        }
	        
	        xPS.setPropertyValue( "TableColumnSeparators", xSeparators );    
	    } catch (UnknownPropertyException e) {
	        LOGGER.error(e.getMessage(), e);
	    } catch (WrappedTargetException e) {
	        LOGGER.error(e.getMessage(), e);
	    } catch (PropertyVetoException e) {
	        LOGGER.error(e.getMessage(), e);
	    } catch (IllegalArgumentException e) {
	        LOGGER.error(e.getMessage(), e);
	    }
	}

	/**
	 * Devuelve el número de páginas de un documento
	 * 
	 * @param xComponent
	 * @return
	 * @throws ISPACException
	 * @author Felipe
	 */
	public static int getNumPages(XComponent xComponent) throws ISPACException {
		int nPages = 0;
		try {
			XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, xComponent);
			XText xText = xTextDocument.getText();
			XTextCursor xTextCursor = xText.createTextCursor();
			xTextCursor.gotoRange(xText.getEnd(), false);
	
			XModel xModel = (XModel) UnoRuntime.queryInterface(XModel.class, xComponent);
			XController xController = xModel.getCurrentController();
			XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier) UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController);
			XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor();
			xViewCursor.gotoRange(xTextCursor, false);
	
			XPageCursor xPageCursor = (XPageCursor) UnoRuntime.queryInterface( XPageCursor.class, xViewCursor);
			nPages = (int) xPageCursor.getPage();
			
		} catch (Exception e) {
			throw new ISPACException(e);
		}
		return nPages;
	}

	/**
	 * Inserta un salto de página en un fichero Word abierto previamente en OpenOffice.
	 *
	 * @param xComponent Fichero abierto en OpenOffice.
	 * @throws ISPACException Debido a errores en la API de SIGEM.
	 */    
	public static void insertaSaltoDePagina(XComponent xComponent) throws ISPACException {
	    //No se puede insertar un salto de página directamente.
	    //Los saltos de página son una propiedad de los párrafos
	    //Así que inserto un nuevo párrafo y luego le ajusto la propiedad
	    
	    try {
	        XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
	        XText xText = xTextDocument.getText();
	        XTextCursor xTextCursor = xText.createTextCursor();
	        xTextCursor.gotoRange(xText.getEnd(),false);
	
	        // Inserto un salto de línea (nuevo párrafo)
	        xText.insertControlCharacter(xTextCursor, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
	
	        //Le digo a este nuevo párrafo que comience con salto de página
	        XPropertySet xProps = (XPropertySet) UnoRuntime.queryInterface(
	        XPropertySet.class, xTextCursor);
	        xProps.setPropertyValue("BreakType", new Integer(com.sun.star.style.BreakType.PAGE_BEFORE_value));
	        
	    } catch(Exception e) {
	        LOGGER.error("Error al obtener el concatena.  - "+e.getMessage(), e);
	        throw new ISPACException("Error al obtener el concatena. - "+e.getMessage(), e);    
	    }
	}

	public static XTextTable insertaTabla(XComponent component, XTextRange posicion, int numFilas, int numColumnas) throws com.sun.star.uno.Exception{
		XTextTable xTable = null;
		
	    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, component);
		XText xText = xTextDocument.getText();
		
	    XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
	    Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
	    xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
	    
	    xTable.initialize(numFilas, numColumnas);
	    XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
	    xText.insertTextContent(posicion, xTextContent, false);
	    
	    return xTable;
	}

	public static XTextTable insertaTablaEnPosicion(XComponent component, String posicionBuscar, int numFilas, int numColumnas) throws com.sun.star.uno.Exception{
		XTextTable tabla = null;
		XTextRange posicionTabla = LibreOfficeUtil.buscaPosicion(component, posicionBuscar);
		if(null != posicionTabla){
			tabla = LibreOfficeUtil.insertaTabla(component, posicionTabla, numFilas, numColumnas);
		}
		return tabla;
	}

	public static XComponent loadDocumento(IClientContext clientContext, File file) {
		XComponent xComponent = null;
		OpenOfficeHelper ooHelper = null;
		
		try{
			ooHelper = OpenOfficeHelper.getInstance();
			xComponent = ooHelper.loadDocument("file://" + file.getPath());
		} catch (ISPACException e){
			LOGGER.error("ERROR al cargar el documento para su edición. " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("ERROR al cargar el documento para su edición. " + e.getMessage(), e);
		} finally {		
			if(null != ooHelper){
				ooHelper.dispose();
			}
		}
	
		return xComponent;
	}

	public static void setTextoCelda(XTextTable xTextTable, int columna, int fila, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		setTextoCelda(xTextTable, LibreOfficeUtil.columnasCabeceraTablas[columna-1] + fila, strText, null, null, 0);
	}

	public static void setTextoCelda(XTextTable xTextTable, int columna, int fila, String strText, Float tamanoFuente, String tipoLetra) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		setTextoCelda(xTextTable, LibreOfficeUtil.columnasCabeceraTablas[columna-1] + fila, strText, tamanoFuente, tipoLetra, 0);
	}

	public static void setTextoCelda(XTextTable xTextTable, int columna, int fila, String strText, Float tamanoFuente, String tipoLetra, int colorLetra) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		setTextoCelda(xTextTable, LibreOfficeUtil.columnasCabeceraTablas[columna-1] + fila, strText, tamanoFuente, tipoLetra, colorLetra);
	}

	public static void setTextoCelda(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		setTextoCelda(xTextTable, cellName, strText, null, null, 0);
	}

	public static void setTextoCelda(XTextTable xTextTable, String cellName, String strText, Float tamanoFuente, String tipoLetra, int colorLetra) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		if(StringUtils.isEmpty(strText)){
			strText = "";
		}		
	    XCell xCell = xTextTable.getCellByName(cellName);        
	    XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);
	
	    //Propiedades
	    XTextCursor xTC = xCellText.createTextCursor();
	    XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
	    if(StringUtils.isNotEmpty(tipoLetra)){
	    	xTPS.setPropertyValue("CharFontName", tipoLetra);
	    }
	    else {
	    	xTPS.setPropertyValue("CharFontName", LibreOfficeUtil.Fuentes.ARIAL);
	    }
	    if(null != tamanoFuente && tamanoFuente>0) {
	    	xTPS.setPropertyValue("CharHeight", tamanoFuente); 
	    }
	    else {
	    	xTPS.setPropertyValue("CharHeight", new Float(8.0));    
	    }
	    //long - This property contains the value of the text color in ARGB notation. ARGB has four bytes denoting alpha, 
	    //red, green and blue. In hex notation, this can be used conveniently: 0xAARRGGBB. The AA (Alpha) can be 00 or left out. 
	    if(colorLetra>0) {
	    	xTPS.setPropertyValue("CharColor", Integer.valueOf(colorLetra));
	    }
	    xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
	    xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
	    xTPS.setPropertyValue("ParaTopMargin", new Short((short)0));
	    xTPS.setPropertyValue("ParaBottomMargin", new Short((short)0));
	    XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
	    xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
	
	    //Texto de la celda
	    xCellText.setString(strText);
	}

	public static void setTextoCeldaCabecera(XTextTable xTextTable, int columna, int fila, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {	
		setTextoCeldaCabecera(xTextTable, LibreOfficeUtil.columnasCabeceraTablas[columna-1] + fila, strText, null, null);
	}

	public static void setTextoCeldaCabecera(XTextTable xTextTable, int columna, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {	
		setTextoCeldaCabecera(xTextTable, LibreOfficeUtil.columnasCabeceraTablas[columna-1] + "1", strText, null, null);
	}

	public static void setTextoCeldaCabecera(XTextTable xTextTable, int columna, String strText, Float tamanoFuente, String tipoLetra) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		setTextoCeldaCabecera(xTextTable, LibreOfficeUtil.columnasCabeceraTablas[columna-1] + "1", strText, tamanoFuente, tipoLetra);
	}

	public static void setTextoCeldaCabecera(XTextTable xTextTable, String cellName, String strText) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		setTextoCeldaCabecera(xTextTable, cellName, strText, null, null);
	}

	public static void setTextoCeldaCabecera(XTextTable xTextTable, String cellName, String strText, Float tamanoFuente, String tipoLetra) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
	    XCell xCell = xTextTable.getCellByName(cellName);
	    XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName));
	
	    //Propiedades        
	    XTextCursor xTC = xCellText.createTextCursor();
	    XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
	    if(StringUtils.isNotEmpty(tipoLetra)){
	    	xTPS.setPropertyValue("CharFontName", tipoLetra);
	    }
	    else {
	    	xTPS.setPropertyValue("CharFontName", LibreOfficeUtil.Fuentes.ARIAL);
	    }
	    if(null != tamanoFuente && tamanoFuente>0) {
	    	xTPS.setPropertyValue("CharHeight", tamanoFuente); 
	    }
	    else {
	    	xTPS.setPropertyValue("CharHeight", new Float(8.0));    
	    }   
	    xTPS.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
	    xTPS.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);
	    xTPS.setPropertyValue("ParaVertAlignment", ParagraphVertAlign.BOTTOM);
	    xTPS.setPropertyValue("ParaTopMargin", new Short((short)60));
	    xTPS.setPropertyValue("ParaBottomMargin", new Short((short)60));
	    XPropertySet xCPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xCell);
	    xCPS.setPropertyValue("VertOrient", new Short(VertOrientation.CENTER));
	    xCPS.setPropertyValue("BackColor", Integer.valueOf(0xC0C0C0));
	    
	    //Texto de la celda
	    xCellText.setString(strText);
	}

	private LibreOfficeUtil(){
	 // No se puede instanciar	
	}
}

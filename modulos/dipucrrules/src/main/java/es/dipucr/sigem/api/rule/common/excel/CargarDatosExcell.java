package es.dipucr.sigem.api.rule.common.excel;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.XIndexAccess;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
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
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class CargarDatosExcell {

	private OpenOfficeHelper ooHelper = null;

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(CargarDatosExcell.class);

	@SuppressWarnings("unused")
	public void extraerTextoXls(IRuleContext rulectx) {

		try {

			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE=" + rulectx.getTaskId(), "");
			Iterator<?> it = collection.iterator();
			String descripcionPlantilla = "";
			while (it.hasNext()) {
				IItem doc = (IItem) it.next();
				descripcionPlantilla = doc.getString("DESCRIPCION");

				// Definir URL del fichero a cargar
				String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, descripcionPlantilla);
				File file = DocumentosUtil.getFile(rulectx.getClientContext(), strInfoPag, null, null);

				// Cargar el documento en una nueva ventana oculta del XDesktop
				ooHelper = OpenOfficeHelper.getInstance();
				XComponent xComp = ooHelper.loadDocument("file://"+ file.getPath());
				// Extraer el texto (esta parte depende del tipo de archivo que
				// hayamos cargado).
				// Consultar los siguientes tres apartados para ver el codigo
				// que habria que introducir aqui
				StringBuilder sbTextExt = textoExtraido(xComp);

				// Cerrar el documento abierto
				xComp.dispose();
			}

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} catch (BootstrapException e) {
			logger.error(e.getMessage(), e);
		} catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		} catch (java.lang.Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally{
			ooHelper.dispose();
		}
	}

	/**
	 * una hoja de cálculo podemos recorrer todas las hojas y, dentro de cada
	 * una, todas las celdas e ir concatenando su contenido Este proceso es
	 * excesivamente lento y puede tardar mucho incluso para hojas muy sencillas
	 * @throws ISPACRuleException 
	 * 
	 * **/
	private StringBuilder textoExtraido(XComponent xComp) throws ISPACRuleException {
		// Variable donde almacenaremos el texto
		StringBuilder text = new StringBuilder();
		try {

			// Hacemos casting del documento cargado a hoja de calculo
			XSpreadsheetDocument xls = (XSpreadsheetDocument) UnoRuntime.queryInterface(XSpreadsheetDocument.class,xComp);

			// Obtenemos las hojas del documento y las recorremos por nombre
			XSpreadsheets xSheets = xls.getSheets();
			//Hoja actual
			XSpreadsheet xSheet = null;

			XIndexAccess xIndexAccess_ = (XIndexAccess) UnoRuntime.queryInterface(XIndexAccess.class, xSheets);
			for (int i = 0; i < xIndexAccess_.getCount(); i++) {
				try {
					// Obtenemos la hoja actual
					xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess_.getByIndex(i));
				} catch (IndexOutOfBoundsException e1) {
					logger.error("Se produjo una excepción", e1);
					throw new ISPACRuleException(e1);
				} catch (WrappedTargetException e) {
					logger.error("Se produjo una excepción", e);
					throw new ISPACRuleException(e);
				}
				/******************************************************************************************************/
				
				 XSheetCellCursor cursor1 = xSheet.createCursor(); 
			      XUsedAreaCursor xUsedAreaCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(XUsedAreaCursor.class, cursor1); 

			      xUsedAreaCursor.gotoStartOfUsedArea(false); 
			      xUsedAreaCursor.gotoEndOfUsedArea(false); 
				
				/******************************************************************************************************/
				
				// Obtenemos un cursor que representa todas las celdas de la hoja
				XSheetCellCursor cursor = xSheet.createCursor();
				
				// Obtenemos el numero de filas y columnas del cursor (hoja)
				XCellRangeAddressable range = (XCellRangeAddressable) UnoRuntime.queryInterface(XCellRangeAddressable.class, cursor);
							
				CellRangeAddress addr = range.getRangeAddress();

				// Recorremos las celdas existentes por sus coordenadas
				for (int x = addr.StartColumn; x <= addr.EndColumn; x++) {
					for (int y = addr.StartRow; y <= addr.EndRow; y++) {
						// Obtemos la celda actual
						XCell cell = cursor.getCellByPosition(x, y);

						// Miramos el contenido de la celda
						if (cell.getType() == CellContentType.EMPTY) {
							// Celda vacia: no hacemos nada
						} else if (cell.getType() == CellContentType.VALUE || cell.getType() == CellContentType.FORMULA) {
							// Celda con valor constante o formula: obtenemos su valor
							text.append(" ");
							text.append(cell.getValue());
						} else if (cell.getType() == CellContentType.TEXT) {
							// Celda con texto: obtenemos su contenido
							XText cellText = (XText) UnoRuntime.queryInterface(XText.class, cell);
							text.append(" ");
							text.append(cellText.getString());
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}

		return text;
	}
}

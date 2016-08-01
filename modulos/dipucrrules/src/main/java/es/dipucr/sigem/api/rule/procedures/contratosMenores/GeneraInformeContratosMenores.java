package es.dipucr.sigem.api.rule.procedures.contratosMenores;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.factura.domain.bean.ContratoMenorBean;
import es.dipucr.factura.services.factura.FacturaWSProxy;
import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

/**
 * [dipucr-Felipe #1211]
 * @author Felipe
 * @since 18.11.2015
 */
public class GeneraInformeContratosMenores  extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(GeneraInformeContratosMenores.class);
	private static final String PARTIDAS_EXCLUIDAS_SEPARATOR = ";";
	
	public IItem itemBusquedaFacturas = null;
	
	/**
	 * Inicialización de objetos heredados de la clase padre
	 */
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GeneraInformeContratosMenores.class);

		IClientContext cct = rulectx.getClientContext();
		
		plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
		
		if(StringUtils.isNotEmpty(plantilla)){
			tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
		}
		refTablas = "%TABLA1%";

		logger.warn("FIN - " + GeneraInformeContratosMenores.class);
		return true;
	}
	
	/**
	 * Validación de parámetros
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			IItemCollection collection = null;
			String numexp = rulectx.getNumExp();
			
			//Obtenemos los datos de la revision de facturas
			collection = entitiesAPI.getEntities("BUSQUEDA_FACTURAS", numexp);
			if (!collection.next()){
				rulectx.setInfoMessage("No se puede iniciar el trámite. Es necesario " +
						"rellenar algún dato de búsqueda en la pestaña 'Búsqueda facturas'.");
				return false;
			}
			
			itemBusquedaFacturas = (IItem) collection.iterator().next();
			
			//Dependiendo de si la factura ha sido o no conformada
			String sEjercicio = itemBusquedaFacturas.getString("EJERCICIO");
			if (StringUtils.isEmpty(sEjercicio)){
				rulectx.setInfoMessage("No se puede iniciar el trámite. Es necesario rellenar" +
						" el ejercicio al que hacen referencia las facturas.");
				return false;
			}
			
			//Dependiendo de si la factura ha sido o no conformada
			String sFechaInicio = itemBusquedaFacturas.getString("FECHA_INICIO");
			if (StringUtils.isEmpty(sFechaInicio)){
				rulectx.setInfoMessage("No se puede iniciar el trámite. Es necesario rellenar" +
						" al menos la fecha de inicio del periodo de búsqueda de facturas.");
				return false;
			}
		}
		catch (Exception e) {
			String error = "Error comprobar los parámetros de búsqueda en la creación"
					+ " del informe de facturas de contabilidad: " + rulectx.getNumExp() + ". " + e.getMessage();
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return true;
	}
	
	/**
	 * Método que crea el listado de facturas a partir de las facturas
	 * recuperadas del servicio web de Sical (via aplicación DPFactura)
	 */
	public void insertaTabla(IRuleContext rulectx, XComponent component,
			String refTabla, IEntitiesAPI entitiesAPI, String numexp)
	{
        try{
        	
        	if (refTabla.equals("%TABLA1%")){
			
        		IClientContext ctx = rulectx.getClientContext();
    			
//        		String idEntidad = "005";
        		String idEntidad = EntidadesAdmUtil.obtenerEntidad(ctx);
        		
        		String ejercicio = itemBusquedaFacturas.getString("EJERCICIO");
        		Date dFechaInicio = itemBusquedaFacturas.getDate("FECHA_INICIO");
        		Calendar calFechaInicio = Calendar.getInstance();
        		calFechaInicio.setTime(dFechaInicio);
        		Date dFechaFin = itemBusquedaFacturas.getDate("FECHA_FIN");
        		Calendar calFechaFin = null;
        		if (null != dFechaFin){
	        		calFechaFin = Calendar.getInstance();
	        		calFechaFin.setTime(dFechaFin);
        		}
        		String sImporteDesde = itemBusquedaFacturas.getString("IMPORTE_DESDE");
        		String sImporteHasta = itemBusquedaFacturas.getString("IMPORTE_HASTA");
        		
        		//Partidas excluídas separadas por punto y coma
        		String sPartidasExcluidas = itemBusquedaFacturas.getString("PARTIDAS_EXCLUIDAS");
        		String[] arrPartidasExcluidas = sPartidasExcluidas.split(PARTIDAS_EXCLUIDAS_SEPARATOR);
        		for (int i = 0; i < arrPartidasExcluidas.length; i++){
        			arrPartidasExcluidas[i] = arrPartidasExcluidas[i].trim();
        		}
        		
        		FacturaWSProxy ws = new FacturaWSProxy();
    			ContratoMenorBean[] arrContratos = ws.recuperarContratosMenores
    					(idEntidad, ejercicio, calFechaInicio, calFechaFin, sImporteDesde, sImporteHasta, arrPartidasExcluidas);
    			List<ContratoMenorBean> listContratos = Arrays.asList(arrContratos);
    			int numFilas = listContratos.size();

			    //Busca la posición de la tabla y coloca el cursor ahí
				//Usaremos el localizador %TABLA1%
				XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
			    XText xText = xTextDocument.getText();
		        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, component);
		        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
		        xSearchDescriptor.setSearchString(refTabla);
		        XInterface xSearchInterface = null;
		        XTextRange xSearchTextRange = null;
		        xSearchInterface = (XInterface) xSearchable.findFirst(xSearchDescriptor);
		        if (xSearchInterface != null) 
		        {
		        	//Cadena encontrada, la borro antes de insertar la tabla
			        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
			        xSearchTextRange.setString("");
			        
					//Inserta una tabla de 4 columnas y tantas filas
				    //como nuevas liquidaciones haya mas una de cabecera
					XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
					Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
					XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
					
					//Añadimos 3 filas más para las dos de la cabecera de la tabla y uno para la celda final
					xTable.initialize(numFilas + 1, 4);
					XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
					xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
					colocaColumnas1(xTable);

					//Rellena la cabecera de la tabla				
					setHeaderCellText(xTable, "A1", "CIF");	
					setHeaderCellText(xTable, "B1", "Nombre Proveedor");				
					setHeaderCellText(xTable, "C1", "Fecha pago");							
					setHeaderCellText(xTable, "D1", "Importe");
					
					//Formateador de importes
					DecimalFormat df = new DecimalFormat("#.00");
					
					int contador = 1;
					for (ContratoMenorBean contrato : listContratos){
						
						contador++;
						setCellText(xTable, "A" + String.valueOf(contador), contrato.getCifProveedor());
				    	setCellText(xTable, "B" + String.valueOf(contador), contrato.getNombreProveedor());
				    	setCellText(xTable, "C" + String.valueOf(contador), contrato.getFechaPago());
				    	String importe = df.format(Double.valueOf(contrato.getImporteFactura().replace(",",".")));
				    	setCellText(xTable, "D" + String.valueOf(contador), importe);
					}
		        }
			}
		} catch (Exception ex) {
			logger.error("Error en la generación de la tabla de contratos menores: " + ex.getMessage(), ex);
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
		xTPS.setPropertyValue("CharHeight", new Float(7.0));	
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
  
    private void colocaColumnas1(XTextTable xTextTable) throws ISPACRuleException{
    	
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
    		double dRelativeWidth = ( double ) 20000 * dRatio;
    		
    		// Last table column separator position
    		double dPosition = sTableColumnRelativeSum - dRelativeWidth;
    		 
    		// Set set new position for all column separators        
    		//Número de separadores
    		int i = xSeparators.length - 1;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		    		
    		i--;    		
    		dRelativeWidth = ( double ) 20000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		i--;    		
    		dRelativeWidth = ( double ) 55000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		// Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
    		xPS.setPropertyValue( "TableColumnSeparators", xSeparators );
    		
		} catch (UnknownPropertyException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (WrappedTargetException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
	}
    
    public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
    	
    	try{
	    	Date dFechaInicio = itemBusquedaFacturas.getDate("FECHA_INICIO");
	    	Date dFechaFin = itemBusquedaFacturas.getDate("FECHA_FIN");
	    	String sImporteDesde = itemBusquedaFacturas.getString("IMPORTE_DESDE");
	    	String sImporteHasta = itemBusquedaFacturas.getString("IMPORTE_HASTA");
	    	
	    	String sTextoFechas = "";
	    	String sTextoImportes = "";
	    	String fechasPattern = "d 'de' MMMM 'de' yyyy";
	    	
	    	//Fechas
	    	if (null == dFechaInicio && null == dFechaFin){
	    		sTextoFechas = "";
	    	}
	    	else{
	    		if (null == dFechaFin){
	    			sTextoFechas += "desde la fecha " + FechasUtil.getFormattedDate(dFechaInicio, fechasPattern);
	    		}
	    		if (null == dFechaInicio){
	        		sTextoFechas += "hasta la fecha " + FechasUtil.getFormattedDate(dFechaInicio, fechasPattern);
	        	}
	    	}
	    	
	    	if (StringUtils.isEmpty(sImporteDesde) && StringUtils.isEmpty(sImporteHasta)){
	    		sTextoImportes = "";
	    	}
	    	else if (StringUtils.isEmpty(sImporteHasta)){
	    		sTextoImportes = "con un importe superior a " + sImporteDesde + " euros";
	    	}
	    	else if (StringUtils.isEmpty(sImporteDesde)){
	    		sTextoImportes = "con un importe inferior a " + sImporteHasta + " euros";
	    	}
	    	else{
	    		sTextoImportes = " con un importe comprendido entre " + sImporteDesde + " y " + sImporteHasta + " euros";
	    	}
	    	
	    	cct.setSsVariable("TEXTO_FECHAS", sTextoFechas);
	    	cct.setSsVariable("TEXTO_IMPORTES", sTextoImportes);
    	}
    	catch (Exception ex){
    		logger.error("Error al cargar variables de sesión del documento de listado facturas: " + ex.getMessage(), ex);
    	}
	}

	public void deleteSsVariables(IClientContext cct) {
		try{
			cct.deleteSsVariable("TEXTO_FECHAS");
			cct.deleteSsVariable("TEXTO_IMPORTES");
		}
		catch (Exception ex){
    		logger.error("Error al borrar variables de sesión del documento de listado facturas: " + ex.getMessage(), ex);
    	}
	}
}



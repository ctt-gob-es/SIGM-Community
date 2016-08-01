package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

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

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class IniciaInformTecnico extends DipucrAutoGeneraDocIniTramiteRule {
	
	private static final Logger logger = Logger.getLogger(IniciaInformTecnico.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GeneraCertifRecepPlicas.class);

		tipoDocumento = "Propuesta Informe Técnico";
		plantilla = "Informe sobre Documentación Técnica";
		refTablas = "%TABLA1%";

		logger.warn("FIN - " + GeneraCertifRecepPlicas.class);
		return true;
	}

	
	@SuppressWarnings("unchecked")
	public void insertaTabla(IRuleContext rulectx, XComponent component,
			String refTabla, IEntitiesAPI entitiesAPI, String numexp)
	{
        String licitador = "";
        IRuleContext ruleCtx = (IRuleContext)rulectx;
        ClientContext cct = (ClientContext) ruleCtx.getClientContext();
        
        ArrayList<String> expedientesResolucion = new ArrayList<String>();
        
        try{
        	if (refTabla.equals("%TABLA1%")){
        		String sQuery = "WHERE NUMEXP_PADRE='"+numexp+"' AND RELACION='Petición Contrato' ORDER BY ID ASC";
        		IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, sQuery);
		        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
		        String numexpPadreContratacion = null;
		        while (exp_relacionadosIterator.hasNext()){
		        	numexpPadreContratacion = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");       	
		        }
        		if(numexpPadreContratacion!=null){
					 //Obtenemos los expedientes relacionados y aprobados, ordenados por ayuntamiento
			        exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+numexpPadreContratacion+"' AND RELACION='Plica' ORDER BY ID ASC");
			        exp_relacionadosIterator = exp_relacionadosCollection.iterator();
			        String query = "";
			        while (exp_relacionadosIterator.hasNext()){
			        	String numexpHijo = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");
			        	expedientesResolucion.add(numexpHijo);
			        	query += "'"+numexpHijo+"',";	        	
			        }
			        		
					if(query.length()>0){
						query = query.substring(0,query.length()-1);
				    }
					sQuery = "select IDENTIDADTITULAR from spac_expedientes as exp, contratacion_plica as contP where " +
							"exp.numexp = contP.numexp and exp.NUMEXP IN ("+query+") and contP.apto='SI' ORDER BY NREG";
					ResultSet contratacion = cct.getConnection().executeQuery(sQuery).getResultSet();
			        if(contratacion==null)
		          	{
		          		throw new ISPACInfo("No existe ninguna plica apta");
		          	}
		          	
					//sQuery = "WHERE NUMEXP IN ("+query+")";
					//IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, sQuery);
				   	//Iterator expedientesIterator = expedientesCollection.iterator();

			        int numFilas = numFilas(contratacion);
			        
			        contratacion = cct.getConnection().executeQuery(sQuery).getResultSet();
	
				    //Busca la posición de la tabla y coloca el cursor ahí
					//Usaremos el localizador %TABLA1%
					XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, component);
				    XText xText = xTextDocument.getText();
			        XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface( XSearchable.class, component);
			        XSearchDescriptor xSearchDescriptor = xSearchable.createSearchDescriptor();
			        xSearchDescriptor.setSearchString(refTabla);
			        XInterface xSearchInterface = null;
			        XTextRange xSearchTextRange = null;
			        xSearchInterface = (XInterface)xSearchable.findFirst(xSearchDescriptor);
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
						setHeaderCellText(xTable, "A1", "Licitadores");	
						setHeaderCellText(xTable, "B1", "MEMORIA TÉCNICA Y CONSTRUCTIVA (Máximo 14 puntos)");				
						setHeaderCellText(xTable, "C1", "PROGRAMA DE TRABAJO (Máximo 6 puntos)");							
						setHeaderCellText(xTable, "D1", "PUNTUACIÓN TOTAL");
						
						
					   	int i = 0;
					   	while (contratacion.next()){
					   		i++;
					    	if (contratacion.getString("IDENTIDADTITULAR")!=null) licitador = contratacion.getString("IDENTIDADTITULAR"); else licitador="";
						    setCellText(xTable, "A"+String.valueOf(i+1), licitador);
					 	}
					   	
			        }
        		}
			}
		}
        catch (ISPACException e) {
        	logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private int numFilas(ResultSet contratacion) {
		int numFilas = 0;
		try {
			while (contratacion.next()){
				numFilas++;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		return numFilas;
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
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		i--;    		
    		dRelativeWidth = ( double ) 40000 * dRatio;
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
}



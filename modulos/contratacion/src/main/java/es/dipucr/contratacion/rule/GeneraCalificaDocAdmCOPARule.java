package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
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
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GeneraCalificaDocAdmCOPARule  extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(GeneraCertifRecepPlicas.class);

	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GeneraCertifRecepPlicas.class);

		IClientContext cct = rulectx.getClientContext();
		
		plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
		
		if(StringUtils.isNotEmpty(plantilla)){
			tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
		}
		refTablas = "%TABLA1%";

		logger.warn("FIN - " + GeneraCertifRecepPlicas.class);
		return true;
	}

	@SuppressWarnings("unchecked")
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			
			int numBop = 0;
			Date fechaBop = null;
			
			 //Obtenemos los expedientes relacionados y aprobados, ordenados por ayuntamiento
	        IItemCollection exp_relacionadosCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='Anuncio BOP' ORDER BY ID DESC");
	        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
	        
	        String numexpHijo = "";
	        if (exp_relacionadosIterator.hasNext()){
	        	numexpHijo = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");       	
	        }
	        		
			IItemCollection expedientesCollection = cct.getAPI().getEntitiesAPI().queryEntities("BOP_SOLICITUD", "WHERE NUMEXP='"+numexpHijo+"'");
		   	Iterator<IItem> expedientesIterator = expedientesCollection.iterator();
			
		   	while (expedientesIterator.hasNext()){
		   		IItem bopSolicitud = (IItem) expedientesIterator.next();
		   		numBop = bopSolicitud.getInt("NUM_ANUNCIO_BOP");
		   		fechaBop = bopSolicitud.getDate("FECHA_PUBLICACION");
		   	}
			if(numBop > 0){
				cct.setSsVariable("NUMBOP", ""+ numBop);
			}
			cct.setSsVariable("FECHABOP", ""+ fechaBop);
			
			cct.setSsVariable("SUBSANACIONES", calcularSubsanados(rulectx, cct.getAPI().getEntitiesAPI()));

		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private String calcularSubsanados(IRuleContext rulectx, IEntitiesAPI entitiesAPI) throws ISPACException{
				
	    String licitador = "";
        String dni = "";							
		String escritura = "";
		String poder = "";
		String declarJurada = "";
		String solvenciaEconomica = "";
		String solvenciaTecnica = "";
		String declaraciónVinculación = "";
		String declaraciónCondiciones = "";
		StringBuffer subsanacion = new StringBuffer();
        
        ArrayList<String> expedientesResolucion = new ArrayList<String>();
		 //Obtenemos los expedientes relacionados y aprobados, ordenados por ayuntamiento
        IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='Plica' ORDER BY ID ASC");
        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
        String query = "";
        
        if(exp_relacionadosIterator.hasNext()){
        	while (exp_relacionadosIterator.hasNext()){
            	String numexpHijo = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");
            	expedientesResolucion.add(numexpHijo);
            	query += "'"+numexpHijo+"',";	        	
            }
            		
    		if(query.length()>0){
    			query = query.substring(0,query.length()-1);
    	    }
    		String sQuery = "WHERE NUMEXP IN ("+query+") ORDER BY NREG ASC";
    		IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, sQuery);
    	   	Iterator<IItem> expedientesIterator = expedientesCollection.iterator();
    			
    	   	int i = 0;
    	   	while (expedientesIterator.hasNext()){
    	   		i++;
    	    	IItem expediente = (IItem) expedientesIterator.next();
    	    	licitador = expediente.getString("IDENTIDADTITULAR");
    	    	
    	    	IItemCollection plicaCol = entitiesAPI.getEntities("CONTRATACION_PLICA", expediente.getString("NUMEXP"));
    	    	Iterator<IItem> itPlica = plicaCol.iterator();
    	    	
    	    	dni = null;
    	    	escritura = null;
    			poder = null;
    			declarJurada = null;
    			solvenciaEconomica = null;
    			solvenciaTecnica = null;
    			declaraciónVinculación = null;
    			declaraciónCondiciones = null;
    	    	
    	    	while(itPlica.hasNext()){
    	    		
    	    		IItem plica = (IItem)itPlica.next();
    	    		dni = plica.getString("DNI");							
    	    		escritura = plica.getString("ESCRITURA");
    	    		poder = plica.getString("PODER");
    	    		declarJurada = plica.getString("DECLARACION_JURADA");
    	    		solvenciaEconomica = plica.getString("SOLVENCIA_ECONOMICA");
    	    		solvenciaTecnica = plica.getString("SOLVENCIA_TECNICA");
    	    		declaraciónVinculación = plica.getString("DECLARACION_VINCULACION");
    	    		declaraciónCondiciones = plica.getString("DECLARACION_CONDICIONES"); 
    	    	
    		    	StringBuffer subsanacionRes = new StringBuffer("No aporta ");
    		    	boolean dentro = false;
    		    	//Si este campo es vacio quiere decir que han entregado toda la documentacion
    		    	if(dni != null && (dni.equals("NO") || dni.equals(""))){
    		    		subsanacionRes.append("Dni; ");
    		    		dentro = true;
    		    	}
    		    	if(escritura != null && (escritura.equals("NO") || escritura.equals(""))){
    		    		subsanacionRes.append("Escritura; ");
    		    		dentro = true;
    		    	}
    		    	if(poder != null && (poder.equals("NO") || poder.equals(""))){
    		    		subsanacionRes.append("Poder; ");
    		    		dentro = true;
    		    	}
    		    	if(declarJurada != null && (declarJurada.equals("NO") || declarJurada.equals(""))){
    		    		subsanacionRes.append("Declaración Jurada; ");
    		    		dentro = true;
    		    	}
    		    	if(solvenciaEconomica != null && (solvenciaEconomica.equals("NO") || solvenciaEconomica.equals(""))){
    		    		subsanacionRes.append("Solvencia Económica; ");
    		    		dentro = true;
    		    	}
    		    	if(solvenciaTecnica != null && (solvenciaTecnica.equals("NO") || solvenciaTecnica.equals(""))){
    		    		subsanacionRes.append("Solvencia Técnica; ");
    		    		dentro = true;
    		    	}
    		    	if(declaraciónVinculación != null && (declaraciónVinculación.equals("NO") || declaraciónVinculación.equals(""))){
    		    		subsanacionRes.append("Declaración Vinculación; ");	
    		    		dentro = true;
    		    	}
    		    	if(declaraciónCondiciones != null && (declaraciónCondiciones.equals("NO") || declaraciónCondiciones.equals(""))){
    		    		subsanacionRes.append("Declaración Condiciones; ");	
    		    		dentro = true;
    		    	}
    		    	if(dentro == true){
    		    		subsanacion.append("- Plica nº "+i+" .- "+licitador+" .- "+subsanacionRes.toString()+".\n");	
    		    	}
    	    	}
            }
    	   	return subsanacion.toString();
        }
        else{
        	return "";
        }
        
        
		
	}

	public void deleteSsVariables(IClientContext cct) {
		try {
			cct.deleteSsVariable("NUMBOP");
			cct.deleteSsVariable("FECHABOP");
			cct.deleteSsVariable("SUBSANACIONES");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void insertaTabla(IRuleContext rulectx, XComponent component,
			String refTabla, IEntitiesAPI entitiesAPI, String numexp)
	{
        String licitador = "";
        String representante = "";
        String dni = "";							
		String escritura = "";
		String poder = "";
		String declarJurada = "";
		String solvenciaEconomica = "";
		String solvenciaTecnica = "";
		String declaraciónVinculación = "";
		String declaraciónCondiciones = "";
        
        ArrayList<String> expedientesResolucion = new ArrayList<String>();
        
        try{
        	if (refTabla.equals("%TABLA1%")){
				 //Obtenemos los expedientes relacionados y aprobados, ordenados por ayuntamiento
		        IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+numexp+"' AND RELACION='Plica' ORDER BY ID ASC");
		        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
		        String query = "";
		        while (exp_relacionadosIterator.hasNext()){
		        	String numexpHijo = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");
		        	expedientesResolucion.add(numexpHijo);
		        	query += "'"+numexpHijo+"',";	        	
		        }
		        		
				if(query.length()>0){
					query = query.substring(0,query.length()-1);
			    }
				String sQuery = "WHERE NUMEXP IN ("+query+")";
				IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, sQuery);
			   	Iterator<IItem> expedientesIterator = expedientesCollection.iterator();
			 
			   	int numFilas = expedientesCollection.toList().size();

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
					xTable.initialize(numFilas + 1, 10);
					XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
					xText.insertTextContent(xSearchTextRange, xTextContent, false);
	
					colocaColumnas1(xTable);

					//Rellena la cabecera de la tabla				
					setHeaderCellText(xTable, "A1", "Licitadores");	
					setHeaderCellText(xTable, "B1", "Representantes");				
					setHeaderCellText(xTable, "C1", "D.N.I");							
					setHeaderCellText(xTable, "D1", "Escritura");
					setHeaderCellText(xTable, "E1", "Poder");
					setHeaderCellText(xTable, "F1", "Declar. Jurada");
					setHeaderCellText(xTable, "G1", "Solvencia Económica");
					setHeaderCellText(xTable, "H1", "Solvencia Técnica");
					setHeaderCellText(xTable, "I1", "Declaración de vinculación");
					setHeaderCellText(xTable, "J1", "Declaración expresa reúne condiciones");
					
					
				   	int i = 0;
				   	while (expedientesIterator.hasNext()){
				   		i++;
				    	IItem expediente = (IItem) expedientesIterator.next();
				    	licitador = expediente.getString("IDENTIDADTITULAR");
				    	
				    	IItemCollection plicaCol = entitiesAPI.getEntities("CONTRATACION_PLICA", expediente.getString("NUMEXP"));
				    	Iterator<IItem> itPlica = plicaCol.iterator();
				    	
				    	dni = "";
				    	representante = "";
				    	escritura = "";
						poder = "";
						declarJurada = "";
						solvenciaEconomica = "";
						solvenciaTecnica = "";
						declaraciónVinculación = "";
						declaraciónCondiciones = "";
				    	
				    	while(itPlica.hasNext()){
				    		IItem plica = (IItem)itPlica.next();
				    		representante = plica.getString("REPRESENTANTE");
				    		dni = plica.getString("DNI");							
				    		escritura = plica.getString("ESCRITURA");
				    		poder = plica.getString("PODER");
				    		declarJurada = plica.getString("DECLARACION_JURADA");
				    		solvenciaEconomica = plica.getString("SOLVENCIA_ECONOMICA");
				    		solvenciaTecnica = plica.getString("SOLVENCIA_TECNICA");
				    		declaraciónVinculación = plica.getString("DECLARACION_VINCULACION");
				    		declaraciónCondiciones = plica.getString("DECLARACION_CONDICIONES");
				    	}
				    				    	
						setCellText(xTable, "A"+String.valueOf(i+1), licitador);
				    	setCellText(xTable, "B"+String.valueOf(i+1), representante);
				    	setCellText(xTable, "C"+String.valueOf(i+1), dni);
				    	setCellText(xTable, "D"+String.valueOf(i+1), escritura);
				    	setCellText(xTable, "E"+String.valueOf(i+1), poder);
				    	setCellText(xTable, "F"+String.valueOf(i+1), declarJurada);
				    	setCellText(xTable, "G"+String.valueOf(i+1), solvenciaEconomica);
				    	setCellText(xTable, "H"+String.valueOf(i+1), solvenciaTecnica);
				    	setCellText(xTable, "I"+String.valueOf(i+1), declaraciónVinculación);
				    	setCellText(xTable, "J"+String.valueOf(i+1), declaraciónCondiciones);
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
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		i--;    		
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		i--;    		
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		i--;    		
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		i--;    		
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		i--;    		
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
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



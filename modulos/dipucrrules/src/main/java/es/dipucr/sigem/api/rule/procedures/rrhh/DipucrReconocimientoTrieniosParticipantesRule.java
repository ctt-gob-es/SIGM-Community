package es.dipucr.sigem.api.rule.procedures.rrhh;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tecdoc.sgm.tram.thirdparty.SigemThirdPartyAPI;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.webempleado.model.mapping.InfoTrienios;
import es.dipucr.webempleado.services.trienios.CumplimientoTrieniosServiceProxy;

/**
 * [eCenpri-Agustin #635]
 * Regla que crea el trámite de "Documentos cumplimiento trienios"
 * @author Agustin
 * @since 25.07.2012
 */
public class DipucrReconocimientoTrieniosParticipantesRule extends DipucrAutoGeneraDocIniTramiteRule 
{
	
	private static final Logger logger = Logger.getLogger(DipucrReconocimientoTrieniosParticipantesRule.class);	
	private static final String _DATE_FORMAT = "dd/MM/yyyy";
	private static final String _RECURSO_FUNCIONARIOS = "Pers.Fis.-Empr.";
	private static final String _RECURSO_LABORALES = "Asun.Der.Laboral";
	
	InfoTrienios[] listaTrieniosFuncionarios;
	InfoTrienios[] listaTrieniosLaborales;
	
	ArrayList<InfoTrienios> listaTrieniosLaborales_al;	
	ArrayList<InfoTrienios> listaTrieniosFuncionarios_al;
	ArrayList<InfoTrienios> listaTrieniosQueNoEstanEnSigem;
	
	String numExp = "";
	
	IRuleContext rulectx_aux;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		logger.info("INICIO - " + this.getClass().getName());
		
		//*********************************************
		IClientContext cct = rulectx.getClientContext();
		//*********************************************

		tipoDocumento = "Listado participantes que faltan en sigem";
		plantilla = "Listado participantes que faltan en sigem trienios";
		refTablas = "%TABLA1%";
		rulectx_aux = rulectx;
		numExp = rulectx.getNumExp();
		
		//Llamada a WS que devuelve info de los trienios de funcionarios 
		CumplimientoTrieniosServiceProxy ctsp = new CumplimientoTrieniosServiceProxy();
		try 
		{			
			logger.warn("Solicitar listas de personal que cumple trienios - "+ ExpedientesUtil.dameFechaInicioExp(cct, numExp));
			listaTrieniosFuncionarios = ctsp.listaCumplimientoTrienios(0,ExpedientesUtil.dameFechaInicioExp(cct, numExp));		
			listaTrieniosLaborales = ctsp.listaCumplimientoTrienios(1,ExpedientesUtil.dameFechaInicioExp(cct, numExp));		
			
		} catch (RemoteException e) {
			logger.error("Error en la llamada al servicio web en la solicitud de personal que cumple trienios. " + e.getMessage(), e);
		} catch (ParseException e) {
			logger.error("Error al parsear la fecha de inicio del expediente en la solicitud de personal que cumple trienios. " + e.getMessage(), e);
		}		
		
		validarListasTerceros();

		logger.info("FIN - " + this.getClass().getName());
		return true;
	}
	
	public void insertaTabla(IRuleContext rulectx, XComponent component,
			String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
		logger.warn("Método insertaTabla de la clase: "+this.getClass().getName());
		
		//Antes de insertar la tabla aprovecho para insertar los participantes
		importarTraslados(rulectx_aux, numexp);		
		if(listaTrieniosFuncionarios_al!=null && listaTrieniosFuncionarios_al.size()>0 )
			importarParticipantes(listaTrieniosFuncionarios_al, rulectx_aux, numexp, "F");
		
		if(listaTrieniosLaborales_al!=null && listaTrieniosLaborales_al.size()>0 )
			importarParticipantes(listaTrieniosLaborales_al, rulectx_aux, numexp, "L");
		
		//Inserto tabla con los participantes que no estan dados de alta
		if(listaTrieniosQueNoEstanEnSigem!=null)
		{
			String nif = null;
			String categoria = null;
			String nombre = "";
			Date fechaTrienioNuevo = null;
			int numero_trienios = 0;
	        	        	        
	        try{
	        	if (refTabla.equals("%TABLA1%")){   
	        			
	        		int numFilas = listaTrieniosQueNoEstanEnSigem.size();

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
				        
						//Inserta una tabla de 5 columnas y tantas filas
					    //como trienios de funcionarios existan
						XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
						Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
						XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
						
						//Añadimos 1 filas más para las dos de la cabecera de la tabla y uno para la celda final
						xTable.initialize(numFilas + 1, 5);
						XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
						xText.insertTextContent(xSearchTextRange, xTextContent, false);
		
						colocaColumnasTrienios(xTable);

						//Rellena la cabecera de la tabla				
						setHeaderCellText(xTable, "A1", "DNI");	
						setHeaderCellText(xTable, "B1", "NOMBRE");	
						setHeaderCellText(xTable, "C1", "CATEGORIA");				
						setHeaderCellText(xTable, "D1", "FECHA");				
						setHeaderCellText(xTable, "E1", "TRIENIO");				
						
						//Rellena la tabla con los datos de trienios de funcionarios
						SimpleDateFormat df_ = new SimpleDateFormat(_DATE_FORMAT);
						
					   	int i = 0;
					   	Iterator<InfoTrienios> itListaFuncionarios = listaTrieniosQueNoEstanEnSigem.iterator();
					   	
					   	while (itListaFuncionarios.hasNext()){
					   		i++;
					   		InfoTrienios item = itListaFuncionarios.next();
					    	//Tomamos los valores		
							nif = item.getDni();
							nombre = item.getNombre_completo();
							categoria = item.getCategoria();
							fechaTrienioNuevo = item.getFechaTrienio().getTime();
							numero_trienios = item.getNumTrienios();					    	
				    				    	
							setCellText(xTable, "A"+String.valueOf(i+1), nif);
							setCellText(xTable, "B"+String.valueOf(i+1), nombre);
					    	setCellText(xTable, "C"+String.valueOf(i+1), categoria);
					    	setCellText(xTable, "D"+String.valueOf(i+1), df_.format(fechaTrienioNuevo));
							setCellText(xTable, "E"+String.valueOf(i+1), "" + numero_trienios);					
					 	}
			        }
				}
			}
	        catch(IllegalArgumentException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
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
  
    private void colocaColumnasTrienios(XTextTable xTextTable){
    	
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
    		
    		//Ancho columna trienio
    		double dRatio = ( double ) sTableColumnRelativeSum / ( double ) iWidth;
    		double dRelativeWidth = ( double ) 10000 * dRatio;
    		
    		// Last table column separator position
    		double dPosition = sTableColumnRelativeSum - dRelativeWidth;
    		 
    		// Set set new position for all column separators        
    		//Número de separadores
    		int i = xSeparators.length - 1;
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		//Ancho columna fecha trienio
    		i--;    		
    		dRelativeWidth = ( double ) 12000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );

    		//Ancho columna cargo
    		i--;    		
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		
    		//Ancho columna nombre
    		//El resto para el dni
    		i--;    		
    		dRelativeWidth = ( double ) 40000 * dRatio;
    		dPosition -= dRelativeWidth;    		    	
    		xSeparators[i].Position = (short) Math.ceil( dPosition );
    		    		
    		
    		// Do not forget to set TableColumnSeparators back! Otherwise, it doesn't work.
    		xPS.setPropertyValue( "TableColumnSeparators", xSeparators );	
		} catch (UnknownPropertyException e) {
			logger.error(e.getMessage(), e);
		} catch (WrappedTargetException e) {
			logger.error(e.getMessage(), e);
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		}
	}
    
    
    private void validarListasTerceros(){
    	
    	listaTrieniosFuncionarios_al = new ArrayList<InfoTrienios>();
    	listaTrieniosLaborales_al = new ArrayList<InfoTrienios>();
    	listaTrieniosQueNoEstanEnSigem = new ArrayList<InfoTrienios>();
    	SigemThirdPartyAPI servicioTerceros = null;
    	IThirdPartyAdapter [] terceros = null;
    	InfoTrienios aux = null;
    	
    	try {    	
    					
			servicioTerceros = new SigemThirdPartyAPI();			
			String nif="";
	    	
			if(listaTrieniosFuncionarios!=null)
			{
				logger.warn("Hay " + listaTrieniosFuncionarios.length + "trabajadores funcionarios que cumplen trienio este mes");
				
				for (int i = 0; i < listaTrieniosFuncionarios.length; i++)
			    {
			    		aux = listaTrieniosFuncionarios[i]; 
			    		nif = aux.getDni();
			    		terceros = servicioTerceros.lookup(nif);
			    		
			    		listaTrieniosFuncionarios_al.add(aux);
			    		if(terceros.length==0)
			    			listaTrieniosQueNoEstanEnSigem.add(aux);	
			   }
	    		
			}
	    	
	    	if(listaTrieniosLaborales!=null)
	    	{
	    		logger.warn("Hay " + listaTrieniosLaborales.length + "trabajadores laborales que cumplen trienio este mes");
	    		
	    		for (int i = 0; i < listaTrieniosLaborales.length; i++)
		    	{		    		
		    		aux = listaTrieniosLaborales[i]; 
		    		nif = aux.getDni();
		    		terceros = servicioTerceros.lookup(nif);
		    		
		    		listaTrieniosLaborales_al.add(aux);
		    		if(terceros.length==0)
		    			listaTrieniosQueNoEstanEnSigem.add(aux);						
				}	    		
	    	}
	    	
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
    }

    
    
	private void importarParticipantes(ArrayList<InfoTrienios> listaParticipantesTrienios,IRuleContext rulectx,String numexpDecreto,String recurso){
		
		try {
			
			if(listaParticipantesTrienios!=null){
			
				String strNif ="";
				
				for (int i = 0; i < listaParticipantesTrienios.size(); i++) {
					
					strNif = listaParticipantesTrienios.get(i).getDni();
					
					if(recurso.equals("F")){
						ParticipantesUtil.insertarParticipanteByNIF(
									rulectx, 
									numexpDecreto, 
									strNif,
									ParticipantesUtil._TIPO_INTERESADO, 
									ParticipantesUtil._TIPO_PERSONA_FISICA, 
									"",
									DipucrReconocimientoTrieniosParticipantesRule._RECURSO_FUNCIONARIOS
									);				
					}	
					
					else if(recurso.equals("L")){
						ParticipantesUtil.insertarParticipanteByNIF(
									rulectx, 
									numexpDecreto, 
									strNif,
									ParticipantesUtil._TIPO_INTERESADO, 
									ParticipantesUtil._TIPO_PERSONA_FISICA, 
									"",
									DipucrReconocimientoTrieniosParticipantesRule._RECURSO_LABORALES
									);			
					}
				}
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}	
	
	private void importarTraslados(IRuleContext rulectx, String numexpDecreto){
		
		try {
		
			ParticipantesUtil.insertarParticipanteByCodigo(
				    rulectx, 
				    numexpDecreto, 
				    "TESORERIA",
					ParticipantesUtil._TIPO_TRASLADO, 
					ParticipantesUtil._TIPO_PERSONA_FISICA
					);
			
			ParticipantesUtil.insertarParticipanteByCodigo(
					rulectx, 
					numexpDecreto, 
					"INTERVENCION",
					ParticipantesUtil._TIPO_TRASLADO, 
					ParticipantesUtil._TIPO_PERSONA_FISICA
					);	
			
			ParticipantesUtil.insertarParticipanteByCodigo(
					rulectx, 
					numexpDecreto, 
					"PERSONAL DPCR",
					ParticipantesUtil._TIPO_TRASLADO, 
					ParticipantesUtil._TIPO_PERSONA_FISICA
					);	
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}
}




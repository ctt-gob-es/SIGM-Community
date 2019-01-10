package es.dipucr.sigem.api.rule.procedures.rrhh;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tecdoc.sgm.tram.thirdparty.SigemThirdPartyAPI;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
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
public class DipucrGenerateNotificacionTrieniosRule extends DipucrAutoGeneraDocIniTramiteRule 
{
	 
	private static final Logger logger = Logger.getLogger(DipucrGenerateNotificacionTrieniosRule.class);	
	private static final String _DATE_FORMAT = "dd/MM/yyyy";
	
	InfoTrienios[] listaTrieniosFuncionarios;
	InfoTrienios[] listaTrieniosLaborales;
	
	ArrayList<InfoTrienios> listaTrieniosLaborales_al;	
	ArrayList<InfoTrienios> listaTrieniosFuncionarios_al;
	ArrayList<InfoTrienios> listaTrieniosQueNoEstanEnSigem;
	
	IRuleContext rulectx_aux;	
	@SuppressWarnings("rawtypes")
	List particitantes_aux;
	
	String numExp = "";
	String expRel = "";
	String nombre = "";
	String dirnot = "";
	String c_postal = "";
	String localidad = "";
	String caut = "";
	String recurso = "";
	String id_ext = "";
	String observaciones = "";
	String nif = "";	
	
	String fechaDecreto="";
	String numDecreto="";
	String extracto="";
	String anio="";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		logger.info("INICIO - "+this.getClass().getName());
		
		//*********************************************
		IClientContext cct = rulectx.getClientContext();
		//*********************************************

		tipoDocumento = "Notificacion decreto trienio";
		plantilla = "Notificacion decreto trienios";
		refTablas = "%TABLA1%";
		rulectx_aux = rulectx;
		numExp = rulectx.getNumExp();
				
		//Llamada a WS que devuelve info de los trienios de funcionarios 
		CumplimientoTrieniosServiceProxy ctsp = new CumplimientoTrieniosServiceProxy();
		try 
		{			
			listaTrieniosFuncionarios = ctsp.listaCumplimientoTrienios(0,ExpedientesUtil.dameFechaInicioExp(cct, numExp));		
			listaTrieniosLaborales = ctsp.listaCumplimientoTrienios(1,ExpedientesUtil.dameFechaInicioExp(cct, numExp));		
			
		} catch (RemoteException e) {
			logger.error("Error en la llamada al servicio web en la solicitud de personal que cumple trienios. " + e.getMessage(), e);
		} catch (ParseException e) {
			logger.error("Error al parsear la fecha de inicio del expediente en la solicitud de personal que cumple trienios. " + e.getMessage(), e);
		}			
		
		validarListasTerceros();

		logger.info("FIN - "+this.getClass().getName());
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			numExp = rulectx.getNumExp();
			cct.endTX(true);

			// Variables
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();

			IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");

			String numExp = rulectx.getNumExp();
			int documentId = 0;
			Object connectorSession = null;

			// 1. Obtener plantilla
			// Comprobar que el trámite tenga un tipo de documento asociado y
			// obtenerlo
			IItemCollection taskTpDocCollection = (IItemCollection) procedureAPI
					.getTaskTpDoc(idTramCtl);
			Iterator it = taskTpDocCollection.iterator();
			while (it.hasNext()) {
				IItem taskTpDoc = (IItem) it.next();
				if ((((String) taskTpDoc.get("CT_TPDOC:NOMBRE")).trim()
						.toUpperCase()).equals((tipoDocumento).trim()
						.toUpperCase())) {

					documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
				}
			}
			cct.beginTX();
			// Asignamos el nombre del trátime ya que si no no lo muestra
			setSsVariables(cct, rulectx);
			cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
			
			// 2. Obtener participantes del expediente actual, con relación != "Trasladado"
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL != 'TRAS' OR ROL IS NULL", "ID");
			
			// 3. Localizamos plantilla
			
			if(documentTypeId == 0)
				throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
				
			IItemCollection tpDocsTemplatesCollection = (IItemCollection) procedureAPI.getTpDocsTemplates(documentTypeId);
			if (tpDocsTemplatesCollection == null || tpDocsTemplatesCollection.toList().isEmpty())
				throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
			
			Iterator docs = tpDocsTemplatesCollection.iterator();
			boolean encontrado = false;
			while (docs.hasNext() && !encontrado) {
				IItem tpDocsTemplate = (IItem) docs.next();
				if (((String) tpDocsTemplate.get("NOMBRE")).trim().toUpperCase().equals(plantilla.trim().toUpperCase()))
				{
					templateId = tpDocsTemplate.getInt("ID");
					encontrado = true;
				}
			}
				
			// 4. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes!=null && participantes.toList().size()>=1) {
				
				particitantes_aux = participantes.toList();
				
				for (int i = 0; i <  particitantes_aux.size(); i++) {
					
					IItem participante_aux = (IItem) particitantes_aux.get(i);
					
					if (participante_aux!=null){
				        
						// Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
						if ((String)participante_aux.get("NDOC")!=null){
			        		nif = (String)participante_aux.get("NDOC");
			        	}
			        	if ((String)participante_aux.get("NOMBRE")!=null){
			        		nombre = (String)participante_aux.get("NOMBRE");
			        	}else{
			        		nombre = "";
			        	}
			        	if ((String)participante_aux.get("DIRNOT")!=null){
			        		dirnot = (String)participante_aux.get("DIRNOT");
			        	}else{
			        		dirnot = "";
			        	}
			        	if ((String)participante_aux.get("C_POSTAL")!=null){
			        		c_postal = (String)participante_aux.get("C_POSTAL");
			        	}else{
			        		c_postal = "";
			        	}
			        	if ((String)participante_aux.get("LOCALIDAD")!=null){
			        		localidad = (String)participante_aux.get("LOCALIDAD");
			        	}else{
			        		localidad = "";
			        	}
			        	if ((String)participante_aux.get("CAUT")!=null){
			        		caut = (String)participante_aux.get("CAUT");
			        	}else{
			        		caut = "";
			        	}
			        	if ((String)participante_aux.get("RECURSO")!=null){
			        		recurso = (String)participante_aux.get("RECURSO");
			        	}else{
			        		recurso = "";
			        	}
			        	
			        	if ((String)participante_aux.get("ID_EXT")!=null){
			        		id_ext = (String)participante_aux.get("ID_EXT");
			        	}else{
			        		id_ext = "";
			        	}
			        	/**
			        	 * FIN[Teresa] Ticket#106#: añadir el campo id_ext
			        	 * **/
			        	//if ((String)participante.get("RECURSO_TEXTO")!=null) recursoTexto = (String)participante.get("RECURSO_TEXTO");
			        	if ((String)participante_aux.get("OBSERVACIONES")!=null){
			        		observaciones = (String)participante_aux.get("OBSERVACIONES");
			        	}else{
			        		observaciones = "";
			        	}

			        	// Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
			        	String sqlQueryPart = "WHERE VALOR = '"+recurso+"'";
			        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
			        	if (colRecurso.iterator().hasNext()){
			        		IItem iRecurso = (IItem)colRecurso.iterator().next();
			        		recurso = iRecurso.getString("SUSTITUTO");
			        	}		        	
			        	
			        	cct.setSsVariable("NDOC", nif);
			        	cct.setSsVariable("NOMBRE", nombre);
			        	cct.setSsVariable("DIRNOT", dirnot);
			        	cct.setSsVariable("C_POSTAL", c_postal);
			        	cct.setSsVariable("LOCALIDAD", localidad);
			        	cct.setSsVariable("CAUT", caut);
			        	cct.setSsVariable("RECURSO", recurso);
			        	//cct.setSsVariable("RECURSO_TEXTO", recursoTexto);
			        	cct.setSsVariable("OBSERVACIONES", observaciones);
					
						//6.Obtener la info de su trienio	
			        	//Lo hago en la llamada de insertar tabla
			        	
			        	//7.Generar documento
						IItem entityDocumentT = gendocAPI.createTaskDocument(
								taskId, documentTypeId);
						int documentIdT = entityDocumentT.getKeyInt();
	
						IItem entityTemplateT = gendocAPI.attachTaskTemplate(
								connectorSession, taskId, documentIdT, templateId);
						
						String infoPagT = entityTemplateT.getString("INFOPAG");
						entityTemplateT.store(cct);
	
						entityDocument = gendocAPI.createTaskDocument(taskId,
								documentTypeId);
						documentId = entityDocument.getKeyInt();
	
						String sFileTemplate = DocumentosUtil.getFile(cct,infoPagT, null, null).getName();
	
						// Generar el documento a partir la plantilla
						IItem entityTemplate = gendocAPI.attachTaskTemplate(
								connectorSession, 
								taskId, 
								documentId, 
								templateId,
								sFileTemplate);
	
						String docref = entityTemplate.getString("INFOPAG");
						String sMimetype = gendocAPI.getMimeType(connectorSession,docref);
						entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
						entityTemplate.set("DESCRIPCION", nombre);
						entityTemplate.set("DESTINO", cct.getSsVariable("NOMBRE"));
	
						entityTemplate.store(cct);	
						
						cct.deleteSsVariable("NOMBRE_TRAMITE");
						
						//8. Insertar el trienio
						if(refTablas != null && !refTablas.equals("")){						
							editarContenidoDocumento(gendocAPI, docref, rulectx, documentId, refTablas, entitiesAPI, numExp);
						}
	
						entityTemplateT.delete(cct);
						entityDocumentT.delete(cct);
						DocumentosUtil.deleteFile(sFileTemplate);
					}				
				}				
			}

			deleteSsVariables(cct);
			cct.endTX(true);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}
	
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			 
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH,1);
			
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			this.expRel=dameExpRel(entitiesAPI); 
			
			this.cargaDatosDecreto(entitiesAPI);
			
			cct.setSsVariable("NUM_DECRETO", ""+numDecreto);
			cct.setSsVariable("FECHA_DECRETO", ""+ fechaDecreto);
			cct.setSsVariable("EXTRACTO_DECRETO", ""+ this.extracto);			
			
			cct.setSsVariable("MES_SIG", ""+ new SimpleDateFormat("MMMM").format(c.getTime()));			
			
			cct.setSsVariable("ANIODEC", ""+this.anio);
			
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int year_dic = year +1;
			
			if(Calendar.getInstance().get(Calendar.MONTH)==11)
				cct.setSsVariable("ANIO", ""+year_dic);
			else	
				cct.setSsVariable("ANIO", ""+year);
			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}

	
	public void deleteSsVariables(IClientContext cct) {
		try {
			cct.deleteSsVariable("NUM_DECRETO");
			cct.deleteSsVariable("FECHA_DECRETO");
			cct.deleteSsVariable("EXTRACTO_DECRETO");
			cct.deleteSsVariable("ANIO");
			cct.deleteSsVariable("MES_SIG");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
		try{
		
			logger.info("Método insertaTabla de la clase: "+this.getClass().getName());
			
			InfoTrienios item = dameTrienio(this.nif);
			
			if(item!=null){
				
				//Inserto tabla con los participantes que no estan dados de alta				
				String categoria = null;
				String nombre = "";
				Date fechaTrienioNuevo = null;
				int numero_trienios = 0;
		       
		        if (refTabla.equals("%TABLA1%")){   
		
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
			        if (xSearchInterface != null){
			        	//Cadena encontrada, la borro antes de insertar la tabla
				        xSearchTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, xSearchInterface);
				        xSearchTextRange.setString("");
				        
						//Inserta una tabla de 5 columnas y tantas filas
					    //como trienios de funcionarios existan
						XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
						Object xObject = xDocMSF.createInstance("com.sun.star.text.TextTable");
						XTextTable xTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, xObject);
						
						//2 FILAS Y 5 COLUMNAS
						xTable.initialize(2, 4);
						XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
						xText.insertTextContent(xSearchTextRange, xTextContent, false);
		
						colocaColumnasTrienios(xTable);
		
						//Rellena la cabecera de la tabla				
						//setHeaderCellText(xTable, "A1", "DNI");	
						//setHeaderCellText(xTable, "B1", "NOMBRE");	
						//setHeaderCellText(xTable, "C1", "CATEGORIA");				
						//setHeaderCellText(xTable, "D1", "FECHA");				
						//setHeaderCellText(xTable, "E1", "TRIENIO");
						
						setHeaderCellText(xTable, "A1", "NOMBRE");	
						setHeaderCellText(xTable, "B1", "CATEGORIA");				
						setHeaderCellText(xTable, "C1", "FECHA");				
						setHeaderCellText(xTable, "D1", "TRIENIO");
						
						//Rellena la tabla con los datos de trienios de funcionarios
//						NumberFormat df = NumberFormat.getCurrencyInstance();
						SimpleDateFormat df_ = new SimpleDateFormat(_DATE_FORMAT);
									
					    //Tomamos los valores		
						nif = item.getDni();
						nombre =  item.getNombre_completo();
						categoria = item.getCategoria();		
						fechaTrienioNuevo = item.getFechaTrienio().getTime();
						numero_trienios = item.getNumTrienios();					    	
				    				    	
						//setCellText(xTable, "A"+String.valueOf(2), nif);
						setCellText(xTable, "A"+String.valueOf(2), nombre);
					    setCellText(xTable, "B"+String.valueOf(2), categoria);
					    setCellText(xTable, "C"+String.valueOf(2), df_.format(fechaTrienioNuevo));
						setCellText(xTable, "D"+String.valueOf(2), "" + numero_trienios);
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
	
	private InfoTrienios dameTrienio(String nif)
	{
		InfoTrienios tercero_trienio = null;
		boolean encontrado = false;
		
		for (int i = 0; i < listaTrieniosFuncionarios_al.size() && !encontrado; i++)
		{
			tercero_trienio = listaTrieniosFuncionarios_al.get(i);
			if(tercero_trienio.getDni().equals(nif))
				encontrado = true;
		}
		
		for (int i = 0; i < listaTrieniosLaborales_al.size() && !encontrado; i++)
		{
			tercero_trienio = listaTrieniosLaborales_al.get(i);
			if(tercero_trienio.getDni().equals(nif))
				encontrado = true;
		}				
		return tercero_trienio;		
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
    		//i--;    		
    		//dRelativeWidth = ( double ) 40000 * dRatio;
    		//dPosition -= dRelativeWidth;    		    	
    		//xSeparators[i].Position = (short) Math.ceil( dPosition );
    		    		
    		
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
    
    private void cargaDatosDecreto(IEntitiesAPI entitiesAPI){
    	
    	try {
	    	String sqlQueryPart = "WHERE NUMEXP = '"+expRel+"'";
	    	IItemCollection decreto;
			
			decreto = entitiesAPI.queryEntities("SGD_DECRETO", sqlQueryPart);
			
	    	if (decreto.iterator().hasNext())
	    	{
	    		IItem iDecreto = (IItem)decreto.iterator().next();
	    		fechaDecreto = iDecreto.getString("FECHA_DECRETO");
	    		numDecreto = iDecreto.getString("NUMERO_DECRETO");
	    		extracto = iDecreto.getString("EXTRACTO_DECRETO");
	    		anio = iDecreto.getString("ANIO");
	    	}
    	} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
    	
    }
    
    private String dameExpRel(IEntitiesAPI entitiesAPI){
    	
    	try {
	    	String expeRel="";
	    	String sqlQueryPart = "WHERE NUMEXP_PADRE = '"+this.numExp+"'";
	    	IItemCollection expRel;
			
	    	expRel = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
			
	    	if (expRel.iterator().hasNext()){
	    		IItem iExpRel = (IItem)expRel.iterator().next();
	    		expeRel = iExpRel.getString("NUMEXP_HIJO");
	    	}
	    	
	    	return expeRel;
	    	
    	} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
    	
    	return null;	
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
				logger.info("Hay " + listaTrieniosFuncionarios.length + "trabajadores funcionarios que cumplen trienio este mes");
				for (int i = 0; i < listaTrieniosFuncionarios.length; i++)
			    {
			    		aux = (InfoTrienios)listaTrieniosFuncionarios[i]; 
			    		nif = aux.getDni();
			    		terceros = servicioTerceros.lookup(nif);
			    		
			    		listaTrieniosFuncionarios_al.add(listaTrieniosFuncionarios[i]);
			    		if(terceros.length==0)
			    			listaTrieniosQueNoEstanEnSigem.add(listaTrieniosFuncionarios[i]);	    			
						
				}
				
			}
	    	if(listaTrieniosLaborales!=null)
	    	{
	    		logger.info("Hay " + listaTrieniosLaborales.length + "trabajadores laborales que cumplen trienio este mes");
	    		logger.info(listaTrieniosLaborales[0].getClass().toString());
	    		for (int i = 0; i < listaTrieniosLaborales.length; i++)
			    {		    		
		    			aux = (InfoTrienios)listaTrieniosLaborales[i]; 
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
}




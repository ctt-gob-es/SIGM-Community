package es.dipucr.sigem.api.rule.procedures.expropiaciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;


public class DipucrFincasActaPreviaExpropiadoTagTablaRule implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrFincasActaPreviaExpropiadoTagTablaRule.class);

	protected String tipoDocumento = "EXPR-011 - Notificacion de Levantamiento de Actas Previas";
	protected String plantilla = "EXPR-011 - Notificacion de Levantamiento de Actas Previas";
	protected String refTablas = "%TABLA1%";
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			logger.info("INICIO - DipucrAutoGeneraDocIniTramiteRule");

			//Obtener las fincas relacionadas con el propietario
			//logger.warn("Expediente de Expropiado: " + rulectx.getNumExp());
			//logger.warn("Expediente de Exproiaciones: " + rulectx.getNumExp());
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			
			//String strQuery = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "' AND RELACION = 'Expropiado/Finca'";
			String strQuery = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator it = collection.iterator();
			IItem item = null;
			List<String> expFincas = new ArrayList<String>();
			
						
			while (it.hasNext()) {
			   item = (IItem)it.next();
			   //expFincas.add(item.getString("NUMEXP_PADRE"));
			   expFincas.add(item.getString("NUMEXP_HIJO"));
			   //logger.warn("Expediente de Finca: " + item.getString("NUMEXP_HIJO"));				
			}
			
			//Si la lista de fincas está vacía no dibujar la tabla
			if(expFincas.isEmpty()){
				return "La lista de fincas a expropiar está vacía\n";
			}
			
			//Obtiene los datos de las fincas
			Iterator itExpFincas = expFincas.iterator();
			strQuery="WHERE NUMEXP = '" + itExpFincas.next() + "'";
			while (itExpFincas.hasNext()) {
				strQuery+=" OR NUMEXP = '" + itExpFincas.next() + "'";				 		
			}
				
			
			//logger.warn("Fincas a buscar: " + strQuery);	
			/**
			 * #[Teresa - Ticket 200] INICIO SIGEM expropiaciones Modificar los listados para que aparezcan los metros a ocupar por propietario 
			 * **/
			//IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + "ORDER BY MUNICIPIO, NUM_POLIGONO ASC, NUM_PARCELA ASC");
			
			/*String clausulas = "ORDER BY MUNICIPIO, CASE WHEN NUM_POLIGONO < 'A' THEN LPAD(NUM_POLIGONO, 255, '0') ELSE NUM_POLIGONO END, " +
			"CASE WHEN NUM_PARCELA < 'A' THEN LPAD(NUM_PARCELA, 255, '0') ELSE NUM_PARCELA END";*/
			
			String clausulas = "ORDER BY LEVANTAMIENTO_FECHA, LEVANTAMIENTO_HORA ASC";
			IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + clausulas);
		
			/**
			 * #[Teresa - Ticket 200] FIN SIGEM expropiaciones Modificar los listados para que aparezcan los metros a ocupar por propietario 
			 * **/

			//Si la lista de fincas está vacía no dibujar la tabla
			if(collectionFincas.toList().isEmpty()){
				return "La lista de fincas a expropiar está vacía\n";
			}
			
			Iterator itFincas = collectionFincas.iterator();
			while (itFincas.hasNext()) {
				item = (IItem)itFincas.next();
				//Lista de listas de filas
				Vector<LevantamientoActaPrevia> contenido = new Vector <LevantamientoActaPrevia>();
				
				LevantamientoActaPrevia actaPrevia = new LevantamientoActaPrevia();
				
				actaPrevia.setNumexp(item.getString("NUMEXP"));
				actaPrevia.setFecha(item.getString("LEVANTAMIENTO_FECHA"));
				actaPrevia.setHora(item.getString("LEVANTAMIENTO_HORA"));
				
				//logger.warn("numexp. "+item.getString("NUMEXP"));

				
				//Código que extrae una lista de propietarios
				List listExpropiado = propietariosFinca(cct, item.getString("NUMEXP"));
				
				Iterator itlistExpropiado = listExpropiado.iterator();
				
				while (itlistExpropiado.hasNext()) {
					
					String expr = (String)itlistExpropiado.next();
					String [] expropiado = expr.split("->");
					actaPrevia.setPropietarios(expropiado[0]);
									
					//Código que extrae una lista de propietarios
					//fila.add(PropietariosUtil.propietariosFinca(entitiesAPI,item.getString("NUMEXP")));
					if(item.getString("NUM_POLIGONO")!=null)actaPrevia.setPoligono(item.getString("NUM_POLIGONO")); 
					if(item.getString("NUM_PARCELA")!=null)actaPrevia.setParcela(item.getString("NUM_PARCELA"));
					if(item.getString("MUNICIPIO")!=null)actaPrevia.setMunicipioParcela(item.getString("MUNICIPIO"));
					if(item.getString("SUP_PARCELA")!=null)actaPrevia.setSuperficieParcela(item.getString("SUP_PARCELA"));
					if(item.getString("SUP_EXPROPIADA")!=null)actaPrevia.setSuperficieOcupar(item.getString("SUP_EXPROPIADA"));
					if(item.getString("APROVECHAMIENTO")!=null)actaPrevia.setCalificacion(item.getString("APROVECHAMIENTO"));
					
					
					contenido.add(actaPrevia);				
					//logger.warn("Detalle de Expediente de Finca: " + item.getString("NUMEXP") + " Num Finca: " + item.getString("NUM_FINCA"));
					
					//creo el documento de las notificaciones
					if(expropiado.length > 1){
						IItem itemDocume = DocumentosUtil.generarDocumento(rulectx, tipoDocumento, expropiado[1]);
						itemDocume.set("DESTINO", expropiado[1]);
						/**
						 * INICIO[Teresa] Ticket #106 añadir el destino_id al documento
						 * **/
						if(expropiado[2]!=null && !expropiado[2].trim().toUpperCase().equals("null")){
							itemDocume.set("DESTINO_ID", expropiado[2]);
						}
						
						/**
						 * FIN[Teresa] Ticket #106 añadir el destino_id al documento
						 * **/
						
						String docref = itemDocume.getString("INFOPAG");
						int documentId = itemDocume.getInt("ID");
						if(refTablas != null && !refTablas.equals("")){	
							for(int i = 0; i< contenido.size(); i ++){
								LevantamientoActaPrevia acta = contenido.get(i);
								insertaTablas(gendocAPI, docref, rulectx, documentId, refTablas, entitiesAPI, rulectx.getNumExp(), acta);
							}
							
						}
						
						itemDocume.store(cct);
					}

					contenido = new Vector<LevantamientoActaPrevia>();
				}
				
			}
			logger.info("FIN - DipucrAutoGeneraDocIniTramiteRule");
		} catch (ISPACException e) {
			logger.error("Se produjo una excepciÃ³n", e);
			throw new ISPACRuleException(e);
		} catch (Exception e) {
			logger.error("Se produjo una excepciÃ³n", e);
			throw new ISPACRuleException(e);
		}
		return true;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private List<String> propietariosFinca(IClientContext cct, String numExpFinca) throws Exception, ISPACException{
		
		List<String> expropiado = new ArrayList<String>();
	
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		//Buscar los expedientes de los Expropiados
		////logger.warn("Expediente de Finca: " + numExpFinca);
		String strQuery = "WHERE NUMEXP_PADRE = '" + numExpFinca + "' AND RELACION = 'Expropiado/Finca'";
		IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
		Iterator<IItem> it = collection.iterator();
		IItem item = null;	
		IItem itemP = null;	
		List<String> expExpropiados = new ArrayList<String>();		
		
		while (it.hasNext()) {
		   item = (IItem)it.next();
		   expExpropiados.add(item.getString("NUMEXP_HIJO"));
		   ////logger.warn("Expediente de Expropiado: " + item.getString("NUMEXP_HIJO"));			
		}
		
		//Si la lista de expropiados está vacía devolver Desconocido
		if(expExpropiados.isEmpty()){
			expropiado.add("Desconocido");
			return expropiado;
			//return "Desconocido";
		}
		
		//Obtiene los datos de los expropiados
		Iterator<String> itExpExpropiados = expExpropiados.iterator();
		strQuery="WHERE NUMEXP = '" + itExpExpropiados.next() + "'";
		while (itExpExpropiados.hasNext()) {
			strQuery+=" OR NUMEXP = '" + itExpExpropiados.next() + "'";				 		
		}			
		
		//logger.warn("Expropiados a buscar: " + strQuery);	
		
		IItemCollection collectionExpropiados = ParticipantesUtil.queryParticipantes(cct, strQuery);
		
		
		// Consulta que debe devolver tantos porcentajes como propietarios haya, ya que hay una entrada para cada uno.
		String strQueryPor = "WHERE NUMEXP = '" + numExpFinca + "'";
		IItemCollection collectionPorcentajes = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQueryPor);
		
		//Si la lista de expropiados está vacía devolver Desconocido
		if(collectionExpropiados.toList().isEmpty()){
			expropiado.add("Desconocido");
			return expropiado;
			//return "Desconocido";
		}
		
		Iterator<IItem> itExpropiados = collectionExpropiados.iterator();
		Iterator<IItem> itPorcentajes = collectionPorcentajes.iterator();
		
		String expropiados = "";
		String nombre = "";
		String id_ext = "";
		while (itExpropiados.hasNext()) {			 
			item = (IItem)itExpropiados.next();
			if(itPorcentajes.hasNext()){
				itemP = (IItem)itPorcentajes.next();
			}
			
			expropiados+=item.getString("NOMBRE");
			nombre = item.getString("NOMBRE");
			/**
			 * INICIO[Teresa] Ticket #106 añadir el destino_id al documento
			 * **/
			id_ext = item.getString("ID_EXT");
			/**
			 * FIN[Teresa] Ticket #106 añadir el destino_id al documento
			 * **/
			//Descomentar esto para que salgan los datos completos de los expropiados
			
			expropiados+=" ";
			expropiados+=item.getString("DIRNOT");
			expropiados+=" ";
			expropiados+=item.getString("LOCALIDAD");
			expropiados+=" (";
			expropiados+=item.getString("C_POSTAL");
			expropiados+=") ";
			if (item.getString("CAUT") != null){
				expropiados+=item.getString("CAUT");
			}
			
			expropiados+=" ";
			String porcentaje = "Desconocido";
			if(itemP!=null){
				porcentaje = itemP.getString("PORCENTAJE_PROP");
			}
			
			if (!porcentaje.equals("100,00")){
				expropiados+="Porcentaje de propiedad: ";
				expropiados+=porcentaje;
			}
			
			expropiado.add(expropiados+"->"+nombre+"->"+id_ext);
			expropiados = "";
			
		}			
		//añado al final el nombre de la persona a expropiar
		//Si no se encuentra, es desconocido
		return expropiado;
		//return expropiados+"->"+nombre;
		
	}

	public void insertaTablas(IGenDocAPI gendocAPI, String docref, IRuleContext rulectx, int documentId, String string, IEntitiesAPI entitiesAPI, String numexp, LevantamientoActaPrevia acta) throws ISPACRuleException {
		
		Object connectorSession = null;
    	OpenOfficeHelper ooHelper = null;
    	try {
			//Abre el documento
			String extension = "odt";
			String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
		
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
		
			OutputStream out = new FileOutputStream(fileName);
			connectorSession = gendocAPI.createConnectorSession();
			gendocAPI.getDocument(connectorSession, docref, out);
			File file = new File(fileName);
			ooHelper = OpenOfficeHelper.getInstance();
			XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
			
			String[] refTabla = refTablas.split(",");			
			for(int i = 0; i<refTabla.length; i++){						
				insertaTabla(rulectx, xComponent, refTabla[i], entitiesAPI, numexp, acta);
			}
		    
		    //Guarda el documento
			String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
			fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
			String mime = "application/vnd.oasis.opendocument.text";
			OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
			File fileOut = new File(fileNameOut);
			InputStream in = new FileInputStream(fileOut);
			gendocAPI.setDocument(connectorSession, documentId, docref, in, (int)(fileOut.length()), mime);
			
			//Borra archivos temporales
			file.delete();
			fileOut.delete();
			if(in!=null) in.close();
			if(out != null) out.close();
			ooHelper.dispose();
			
    	} catch (ISPACException e) {
    		logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		} catch (FileNotFoundException e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		} catch (java.lang.Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}	

	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp, LevantamientoActaPrevia acta) throws ISPACRuleException {
		logger.info("Método insertaTabla de la clase: "+this.getClass().getName());
	        
	        try{
	        	if (refTabla.equals("%TABLA1%")){
					 //Obtenemos los expedientes relacionados y aprobados, ordenados por ayuntamiento
			        
					
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
						xTable.initialize( 2, 10);
						XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xTable);
						xText.insertTextContent(xSearchTextRange, xTextContent, false);
		
						colocaColumnas1(xTable);

						//Rellena la cabecera de la tabla				
						setHeaderCellText(xTable, "A1", "Finca Num.");	
						setHeaderCellText(xTable, "B1", "Fecha");				
						setHeaderCellText(xTable, "C1", "Hora");							
						setHeaderCellText(xTable, "D1", "Propietarios");
						setHeaderCellText(xTable, "E1", "Polígono");
						setHeaderCellText(xTable, "F1", "Parcela");
						setHeaderCellText(xTable, "G1", "Municipio de Parcela");
						setHeaderCellText(xTable, "H1", "Superficie de Parcela (m2)");
						setHeaderCellText(xTable, "I1", "Superficie a ocupar (m2)");
						setHeaderCellText(xTable, "J1", "Calificación");
						
						String numexpActa = "";
						if(acta.getNumexp()!=null)numexpActa = acta.getNumexp();
						setCellText(xTable, "A"+String.valueOf(2), numexpActa); 
						String fechaActa = "";
						if(acta.getFecha()!=null)fechaActa = acta.getFecha();						
				    	setCellText(xTable, "B"+String.valueOf(2), fechaActa);
				    	String hora = "";
				    	if(acta.getHora()!=null) hora = acta.getHora();
				    	setCellText(xTable, "C"+String.valueOf(2), hora);
				    	String propietarios = "";
				    	if(acta.getPropietarios()!=null) propietarios = acta.getPropietarios();
				    	setCellText(xTable, "D"+String.valueOf(2), propietarios);
				    	String poligono = "";
				    	if(acta.getPoligono()!=null) poligono = acta.getPoligono();
				    	setCellText(xTable, "E"+String.valueOf(2), poligono);
				    	String parcela = "";
				    	if(acta.getParcela()!=null) parcela = acta.getParcela();
				    	setCellText(xTable, "F"+String.valueOf(2), parcela);
				    	String municipioParcela = "";
				    	if(acta.getMunicipioParcela()!=null) municipioParcela = acta.getMunicipioParcela();
				    	setCellText(xTable, "G"+String.valueOf(2), municipioParcela);
				    	String superficieParcela = "";
				    	if(acta.getSuperficieParcela()!=null) superficieParcela = acta.getSuperficieParcela();
				    	setCellText(xTable, "H"+String.valueOf(2), superficieParcela);
				    	String superficieOcupar = "";
				    	if(acta.getSuperficieOcupar()!=null) superficieOcupar = acta.getSuperficieOcupar();
				    	setCellText(xTable, "I"+String.valueOf(2), superficieOcupar);
				    	String calificacion = "";
				    	if(acta.getCalificacion()!=null) calificacion = acta.getCalificacion();
				    	setCellText(xTable, "J"+String.valueOf(2), calificacion);

			        }
	        	}
			}
	        catch (ISPACException e) {
	        	logger.error("Se produjo una excepciÃ³n", e);
				throw new ISPACRuleException(e);
			} catch (IllegalArgumentException e) {
				logger.error("Se produjo una excepciÃ³n", e);
				throw new ISPACRuleException(e);
			} catch (Exception e) {
				logger.error("Se produjo una excepciÃ³n", e);
				throw new ISPACRuleException(e);
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
			logger.error(e.getMessage());
			throw new ISPACRuleException("Error. ",e);
		} catch (WrappedTargetException e) {
			logger.error(e.getMessage());
			throw new ISPACRuleException("Error. ",e);
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage());
			throw new ISPACRuleException("Error. ",e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
			throw new ISPACRuleException("Error. ",e);
		}
	}

	/**
	 * Métodod para setear variables del sistema si deseamos incluir alguna en
	 * los documentos. Hay que sobreescribirlos y añadir las variables que
	 * deseemos, y no olvidar eliminarlas
	 * 
	 * @param cct
	 * @param rulectx
	 */
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
	}

	public void deleteSsVariables(IClientContext cct) {
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}

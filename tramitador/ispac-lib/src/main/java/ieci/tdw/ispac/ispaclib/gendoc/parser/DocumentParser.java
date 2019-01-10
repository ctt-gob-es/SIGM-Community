package ieci.tdw.ispac.ispaclib.gendoc.parser;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.tageval.ITagTranslator;
import ieci.tdw.ispac.ispaclib.templates.TemplateDocumentInfo;
import ieci.tdw.ispac.ispaclib.templates.TemplateGraphicInfo;
import ieci.tdw.ispac.ispaclib.templates.TemplateTableInfo;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sun.star.awt.Point;
import com.sun.star.awt.Size;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.ElementExistException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.drawing.XShape;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.sheet.XCellRangeData;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCellRange;
import com.sun.star.text.HoriOrientation;
import com.sun.star.text.TextContentAnchorType;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

/**
 * Combinador de documentos. Su labor consiste buscar y sustituir las etiquetas de la plantilla por
 * sus valores correspondientes. Una vez terminada la combinaci&oacute;n guarda el documento resultante.
 *
 * DocumentParser utiliza los servicios de ofim&aacute;tica del servidor OpenOffice para realizar
 * la combinaci&oacute;n.
 *
 */
public class DocumentParser
{
	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(DocumentParser.class);

	private OpenOfficeHelper ooHelper = null;

	//ESTE PATRON RECOGE TEXTO FUERA DE UN TAG
	//private static final String ISPACTAG="<[ \\t\\n\\x0B\\f\\r]*ispactag[ \\t\\n\\x0B\\f\\r]+.*/>";

	//ESTE PATRON NO ACEPTA PARENTESIS DENTRO DE LOS TAG, NECESARIO PARA CONSULTAS DEL ESTILO WHERE ... IN (...)
	//private static final String ISPACTAG="<[ \\t\\n\\x0B\\f\\r]*ispactag[ \\t\\n\\x0B\\f\\r]+[^(/>)]*/>";

	//ESTE PATRON NO ACEPTA EL CARACTER / DENTRO DE UN TAG, p ej: para el patron de una fecha: dateFormat="dd/MM/yyyy".
	//private static final String ISPACTAG="<[ \\t\\n\\x0B\\f\\r]*ispactag[ \\t\\n\\x0B\\f\\r]+[^/>]*/>";

	private static final String ISPACTAG=  "<[ \\t\\n\\x0B\\f\\r]*ispactag[ \\t\\n\\x0B\\f\\r]+[^>]*/>";

	//private static final String TEMPLATE_ATTRIBUTE = "template";

	//private static final String ISPACTAG_TEMPLATE="<[ \\t\\n\\x0B\\f\\r]*ispactag[ \\t\\n\\x0B\\f\\r]+"+TEMPLATE_ATTRIBUTE+"[^(/>)]*/>";

	private final int DEFAULT_TAGS_NESTING_LEVEL = 2;

	/**
	 * Documento de salida en formato <code>OpenOffice</code>
	 */
	public static final int OpenOffice = 1;

	/**
	 * Documento de salida en formato <code>RTF</code>
	 */
	public static final int RichTextFormat = 2;

	/**
	 * Documento de salida en formato <code>Microsoft Word</code>
	 */
	public static final int MicrosoftWord = 3;

	/**
	 * Documento de salida en formato <code>PDF</code>
	 */
	public static final int PDFWriter = 4;

	/**
	 * Documento de salida en formato <code>Microsoft Excel</code>
	 */
	public static final int MicrosoftExcel = 5;
	
	//[Manu Ticket #475] Modificaciones para que reconozca los ODS
	public static final int Calc = 6;
//	[Manu Ticket #475] Modificaciones para que reconozca los ODS


	/**
	 * Inicializa el componente de combinaci&oacute;n conect&aacute;ndolo con el servicio de combinaci&oacute;n
	 * (OpenOffice).
	 *
	 * @param cnt Cadena de conexi&oacute;n que indentifica el servicio OpenOffice.
	 * <br/>Formato:
	 * <ul>
	 * <li>
	 * 	<code>
	 * 		uno:socket,host=<b>nombrehost</b>,port=<b>puerto</b>;urp;StarOffice.NamingService
	 * 	</code>
	 * </li>
	 * </ul>
	 * @exception Exception Error al conectarse con el servicio OpenOffice.
	 *
	 */
	public DocumentParser(String cnt) throws ISPACException {
		ooHelper = OpenOfficeHelper.getInstance(cnt);
	}

	/**
	 * Sustituye las etiquetas de combinaci&oacute;n de la plantilla por sus valores calculados
	 * por ITagTranslator. Una vez terminado guarda el documento en la URL indicada por
	 * strTargetURL con el tipo de archivo (MSWord, OpenOffice, RTF) especificado por targetfilter
	 * <br/>
	 *
	 * @param strTemplateURL  URL para la plantilla que contiene el documento con los tags que
	 * hay que interpretar.
	 * @param strTargetURL URL para el documento combinado.
	 * @param tagtranslator Traductor de tags a utilizar en la combinaci&oacute;n
	 * @param targetMimeType Tipo MIME para el documento combinado.
	 * @exception ISPACException Si ocurre alg&uacute;n error durante la combinación
	 */
	public void mergeDocument(String strTemplateURL, String strTargetURL,
			ITagTranslator tagtranslator, String targetMimeType)
			throws ISPACException {

		merge(strTemplateURL, strTargetURL, tagtranslator,
				OpenOfficeHelper.getFilterName(targetMimeType));
	}

	/**
	 * Sustituye las etiquetas de combinaci&oacute;n de la plantilla por sus valores calculados
	 * por ITagTranslator. Una vez terminado guarda el documento en la URL indicada por
	 * strTargetURL con el tipo de archivo (MSWord, OpenOffice, RTF) especificado por targetfilter
	 * <br/>
	 *
	 * @param strTemplateURL  URL para la plantilla que contiene el documento con los tags que
	 * hay que interpretar.
	 * @param strTargetURL URL para el documento combinado.
	 * @param tagtranslator Traductor de tags a utilizar en la combinaci&oacute;n
	 * @param targetfilter Determina el tipo de documento resultado de la combinaci&oacute;n del documento:
	 * @exception ISPACException Si ocurre alg&uacute;n error durante la combinación
	 * @deprecated
	 */
	public void mergeDocument(
	String strTemplateURL,
	String strTargetURL,
	ITagTranslator tagtranslator,
	int targetfilter)
	throws ISPACException
	{
		String strFilter;

		if (logger.isInfoEnabled()) {
			logger.info("mergeDocument: strTemplateURL=[" + strTemplateURL
					+ "], strTargetURL=[" + strTargetURL
					+ "], tagtranslator=[" + tagtranslator
					+ "], targetfilter=[" + targetfilter + "]");
		}

		switch (targetfilter)
		{
		case DocumentParser.OpenOffice:
			strFilter = "";
			break;
		case DocumentParser.MicrosoftWord:
			strFilter = "MS Word 97";
			break;
		case DocumentParser.MicrosoftExcel:
			strFilter =  "MS Excel 97";
			break;
		case DocumentParser.RichTextFormat:
			strFilter = "Rich Text Format";
			break;
		case DocumentParser.PDFWriter:
			strFilter = "writer_pdf_Export";
			break;
//		[Manu Ticket #475] Modificaciones para que reconozca los ODS
		case DocumentParser.Calc:
			strFilter = "Calc8";
			break;
//		[Manu Ticket #475] Modificaciones para que reconozca los ODS
		default:
		    /* OpenOffice */
			strFilter = "";
			break;
		}

		merge(strTemplateURL, strTargetURL, tagtranslator, strFilter);
	}


	protected void merge(String strTemplateURL, String strTargetURL,
			ITagTranslator tagtranslator, String strFilter)
			throws ISPACException	{

		XComponent xComponent = null;

		if (logger.isInfoEnabled()) {
			logger.info("mergeDocument: strTemplateURL=[" + strTemplateURL
					+ "], strTargetURL=[" + strTargetURL
					+ "], tagtranslator=[" + tagtranslator
					+ "], targetfilterName=[" + strFilter + "]");
		}

        try{
			if (logger.isDebugEnabled()) {
				logger.debug("Cargando documento: " + strTemplateURL);
			}

			if ((xComponent = ooHelper.loadDocument(strTemplateURL)) == null){
				throw new ISPACException("exception.documents.templates.load",new Object[]{strTemplateURL}, true);
			}

			//generateDocument(xComponent,tagtranslator);

			if (logger.isDebugEnabled()) {
				logger.debug("Generando documento...");
			}

//			[Manu Ticket #475] Modificaciones para que reconozca los ODS
			if ("MS Excel 97".equalsIgnoreCase(strFilter) || "Calc8".equalsIgnoreCase(strFilter)) {
				generateDocumentExcel(xComponent,tagtranslator);
			} else {
				generateDocument(xComponent,tagtranslator);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Guardando documento: " + strTargetURL);
			}

			OpenOfficeHelper.saveDocument(xComponent, strTargetURL, strFilter);

			if (logger.isDebugEnabled()) {
				logger.debug("Documento salvado: " + strTargetURL);
			}

			// mxDesktop.terminate();
		}catch (DisposedException e){
			logger.warn("El servidor OpenOffice no está disponible - "+e.getMessage(), e);
        	ooHelper.dispose();
        	throw e;
        }catch (ISPACException e){
        	logger.error("Error al mezclar el documento - "+e.getMessage(), e);
			throw e;
		}catch (Exception e){
			logger.error("Error al mezclar el documento - "+e.getMessage(), e);
			throw new ISPACException("Error al mezclar el documento - "+e.getMessage(), e);
		}finally{
		    if (xComponent != null) xComponent.dispose();
		}
	}

	/**
	 * El filtro de salida para la combinaci&oacute;n de documentos correspondiente al mimetype
	 * proporcionado.
	 * @param mimetype El mimetype deseado para el documento destino
	 * @return Filtro OpenOffice a utilizar
	 * @deprecated
	 */
	public int getTargetDocumentFilter(String mimetype)
	{
		if (mimetype.equalsIgnoreCase("application/msword"))
			return DocumentParser.MicrosoftWord;
		else if (mimetype.equalsIgnoreCase("application/vnd.sun.xml.writer"))
			return DocumentParser.OpenOffice;
		else if (mimetype.equalsIgnoreCase("application/pdf"))
			return DocumentParser.PDFWriter;
		else if (mimetype.equalsIgnoreCase("application/vnd.ms-excel")
				|| mimetype.equalsIgnoreCase("application/x-excel")
				|| mimetype.equalsIgnoreCase("application/x-msexcel")
				|| mimetype.equalsIgnoreCase("application/excel")
				|| mimetype.equalsIgnoreCase("application/vndms-excel"))
			return DocumentParser.MicrosoftExcel;
//		[Manu Ticket #475] Modificaciones para que reconozca los ODS
		//MQE si es ods no queremos que tire del parseador de odt, hacemos que utilice el del excell, 
		//ya que hemos configurado para que arranque ODS con scalc.exe
		else if(mimetype.equals("application/vnd.oasis.opendocument.spreadsheet"))
			return DocumentParser.Calc;

		return OpenOffice;
	}

	private void generateDocumentExcel(XComponent xComponent,ITagTranslator tagtranslator)
	throws ISPACException
	{
    	XReplaceable xReplaceable = null;
    	XReplaceDescriptor xReplaceDescriptor = null;
		XIndexAccess xIndexAccess_ = null;
		XSpreadsheetDocument xDocument = (XSpreadsheetDocument)UnoRuntime.queryInterface(XSpreadsheetDocument.class, xComponent);
		//CARGAMOS LA COLECCION DE HOJAS DEL LIBRO
		com.sun.star.sheet.XSpreadsheets xSheets = xDocument.getSheets();
		com.sun.star.sheet.XSpreadsheet xSheet= null;
		//String[] names = xSheets.getElementNames();
        xIndexAccess_ = (XIndexAccess)UnoRuntime.queryInterface(XIndexAccess.class, xSheets );


        //ACCEDEMOS A CADA UNA DE LAS HOJAS PARA BUSCAR LOS TAGS DECLARADOS Y SUSTITUIRLOS POR SU VALOR
        for (int x = 0; x < xIndexAccess_.getCount(); x++){

        	try {
        		xSheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class, xIndexAccess_.getByIndex(x));
        	} catch (Exception e) {
				logger.warn("Error al obtener el interfaz - "+e.getMessage(), e);
			}

			//TODO Cargar el limite del rango de alguna constante que debe existe, aunque no la encontre,
			//ya que el rango maximo dependera de la version, p.e. para OOffice 1.1.3 es: 'A1:IV32000', para OOffice 2.0 es 'A1:IV32000'
			//cargamos todas la celdas de la hoja
 			com.sun.star.table.XCellRange xCellRange = xSheet.getCellRangeByName( "A1:IV32000");
 			xReplaceable = (XReplaceable) UnoRuntime.queryInterface( XReplaceable.class,xCellRange);
 			// Cescriptor para establecer las propiedades del reemplazo
 			xReplaceDescriptor = (XReplaceDescriptor) xReplaceable.createReplaceDescriptor();
 			xReplaceDescriptor.setSearchString(ISPACTAG);
 			Object object = new Boolean(true);

 			try
			{
				xReplaceDescriptor.setPropertyValue("SearchRegularExpression", object);

			}catch(Exception e)
			{
				logger.error("Error al establecer la propiedad [SearchRegularExpression]: " + object +" - "+e.getMessage(), e);
				throw new ISPACException("Error al generar el documento - "+e.getMessage(), e);
			}

				//variables de acceso a datos
				XIndexAccess xIndexAccess = null;

				String cellText = null;
				Object tag;

				// Busca todos los tags declarados en el documento
				xIndexAccess = xReplaceable.findAll(xReplaceDescriptor);
			    ArrayList<String> tagslist = new ArrayList<String>();
				//Comprobamos que hay Tags declarados en la hoja activa antes de recorrerla
				if (xIndexAccess!=null){

					for (int i = 0; i < xIndexAccess.getCount(); i++)
						{
							try
							{

							tag = xIndexAccess.getByIndex(i);
							}catch(Exception e)
							{
								logger.error("Error al obtener el tag", e);
								throw new ISPACException(e);
							}
							//Accedemos al rango de la hoja indexdo por el objeto tag
                            XCellRange rango= (XCellRange) UnoRuntime.queryInterface(XCellRange.class,tag);
                            XCellRangeData datos = (XCellRangeData) UnoRuntime.queryInterface(XCellRangeData.class,rango);

							//Accedemos al contenido de las celdas del rango
                            Object [][] contenido =datos.getDataArray();
                            for (int j = 0; j < contenido.length; j++) {
								for (int k = 0; k < contenido[j].length; k++) {
									 cellText=contenido [j][k].toString();
									 if (StringUtils.isNotEmpty(cellText)) {
										//tagslist.add(cellText);
										tagslist.addAll(getAllTgas(cellText));
									}
							   }//fin for para el segundo indice del array
							}//fin for para el primer indice del array

						}

						List<?> translatedtags = tagtranslator.translateTags(tagslist);

						object = new Boolean(false);
					try
					{
						xReplaceDescriptor.setPropertyValue("SearchRegularExpression", object);
					}catch(Exception e)
					{
						logger.error("Error al establecer la propiedad [SearchRegularExpression]: " + object, e);
						throw new ISPACException(e);
					}

						// Los dos conjuntos estan ordenados.
					Iterator<String> it = tagslist.iterator();
					Iterator<?> ittranslated = translatedtags.iterator();

					while (ittranslated.hasNext()&&it.hasNext())
						{

						String stagvalue =(String) ittranslated.next();
						if (stagvalue==null)
							stagvalue="";

						xReplaceDescriptor.setSearchString(it.next());
						xReplaceDescriptor.setReplaceString(stagvalue);
						xReplaceable.replaceAll(xReplaceDescriptor);
						}
				}//fin de if que comprobueba que hay tags declarados
       } //final del recorrido de todas las hojas del libro (primer for abierto en la funcion)

}


	/**
	 * @param cellText Contenido de una celda que contiene algún tag
	 * @return listado de tags contenidos en la celda <code>cellText</code>
	 */
	private List<String> getAllTgas(String cellText) {
		List<String> list = new ArrayList<String>();

		Pattern pattern = Pattern.compile(ISPACTAG);
		Matcher matcher = pattern.matcher(cellText);
		while (matcher.find()) {
			String key = matcher.group();
			list.add(key);
		}
		return list;
	}

	// [Dipucr-Manu Ticket #478] -INICIO - ALSIGM3 Nueva opción Repositorio Común
	private void insertaImagen(XComponent xComponent, XTextRange xTextRange, int count, TemplateGraphicInfo graphicInfo){
		 try {
			 XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
			 XMultiServiceFactory docServices = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
			 
			 Object graphicShape = null;
			 graphicShape = docServices.createInstance("com.sun.star.drawing.GraphicObjectShape");
			 
			 // Customizing graphic shape position and size
			 XShape shapeSettings = (XShape)UnoRuntime.queryInterface(XShape.class, graphicShape);			 
			 shapeSettings.setSize(new Size(graphicInfo.getWidth(), graphicInfo.getHeight()));
			 shapeSettings.setPosition(new Point(20, 20));
			 
			 // Creating bitmap container service
			 XNameContainer bitmapContainer = (XNameContainer) UnoRuntime.queryInterface(XNameContainer.class, docServices.createInstance("com.sun.star.drawing.BitmapTable"));
			 bitmapContainer.insertByName("img" + count, graphicInfo.getGraphicUrl());
			 
			 XPropertySet xPropSet = (XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, graphicShape);
			 xPropSet.setPropertyValue("AnchorType", TextContentAnchorType.AT_PARAGRAPH);
			 
			 // Probamos para que centre la imagen en una celda
			 // xPropSet.setPropertyValue("VertOrient", VertOrientation.CENTER);
			 if (graphicInfo.isCentered()) {
				 xPropSet.setPropertyValue("HoriOrient", HoriOrientation.CENTER);				 
			 }
			 
			 xPropSet.setPropertyValue("GraphicURL", bitmapContainer.getByName("img" + count));
			 
			 XTextContent xTextContent = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, graphicShape);
			 
			 XText xText = xTextRange.getText();
			 
			 XTextCursor xTextCursor = xTextRange.getText().createTextCursor();			 
			 xTextCursor.gotoRange(xTextRange,false);
			 
			 xText.insertTextContent( xTextCursor, xTextContent, true);
		} catch (IllegalArgumentException e) {
			logger.error("Error al reemplazar la imagen. " + e.getMessage(), e);			
		} catch (ElementExistException e) {
			logger.error("Error al reemplazar la imagen. " + e.getMessage(), e);
		} catch (WrappedTargetException e) {
			logger.error("Error al reemplazar la imagen. " + e.getMessage(), e);
		} catch (PropertyVetoException e) {
			logger.error("Error al reemplazar la imagen. " + e.getMessage(), e);
		} catch (UnknownPropertyException e) {
			logger.error("Error al reemplazar la imagen. " + e.getMessage(), e);
		} catch (NoSuchElementException e) {
			logger.error("Error al reemplazar la imagen. " + e.getMessage(), e);
		} catch (com.sun.star.uno.Exception e) {
			logger.error("Error al reemplazar la imagen. " + e.getMessage(), e);
		}
	}
	
	// [Dipucr-Manu Ticket #478] - FIN - ALSIGM3 Nueva opción Repositorio Común
	private void generateDocument(XComponent xComponent,ITagTranslator tagtranslator)throws ISPACException{
		try{

			//Para permitir que al incluir dentro de una plantilla un documento este documento pueda contener a su vez
			//tags, se realiza un bucle. Se incluye un nivel de anidamiento para que no se puedan producir bucles infinitos
			int nivelAnidamiento = ISPACConfiguration.getInstance().getInt(
					ISPACConfiguration.PARSER_CONNECTOR_TAGS_NESTING_LEVEL,
					DEFAULT_TAGS_NESTING_LEVEL);

			int contador = 0;
			for(;;contador++){

				if (contador > nivelAnidamiento){
					logger.warn("Se ha superado el nivel de anidamiento de marcadores en plantillas. Comprobar la definición de la plantilla");
					break;
				}

				XReplaceable xReplaceable = null;
				XReplaceDescriptor xReplaceDescriptor = null;
				xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xComponent);
				// Descriptor to set properies for replace
				xReplaceDescriptor = xReplaceable.createReplaceDescriptor();
				xReplaceDescriptor.setSearchString(ISPACTAG);

				Object object = new Boolean(true);
				xReplaceDescriptor.setPropertyValue("SearchRegularExpression", object);

				XIndexAccess xIndexAccess = null;
				String strTag = null;
				// Busca todos los tags declarados en el documento
				xIndexAccess = xReplaceable.findAll(xReplaceDescriptor);

				ArrayList<String> tagslist = new ArrayList<String>();
				ArrayList<XTextRange> xTextRangeList = new ArrayList<XTextRange>();
				for (int i = 0; i < xIndexAccess.getCount(); i++){
					object = xIndexAccess.getByIndex(i);
					XTextRange xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, object);
					strTag = xTextRange.getString();
					tagslist.add(strTag);
					xTextRangeList.add(xTextRange);
				}
				if (tagslist.size() == 0){
					break;
				}

				List<?> translatedtags = tagtranslator.translateTags(tagslist);
				object = new Boolean(false);
				xReplaceDescriptor.setPropertyValue("SearchRegularExpression", object);

				// Los dos conjuntos estan ordenados.
				Iterator<String> it = tagslist.iterator();
				Iterator<?> ittranslated = translatedtags.iterator();
				Iterator<XTextRange> itXTextRange = xTextRangeList.iterator();
				
				int count = 0;

				while (ittranslated.hasNext()&&it.hasNext() && itXTextRange.hasNext()){					
					Object stagvalue = ittranslated.next();
					XTextRange xTextRangeReplace = itXTextRange.next();
					String tag = it.next();
					if (stagvalue==null){
						xReplaceDescriptor.setSearchString(tag);
						xReplaceDescriptor.setReplaceString("");
						xReplaceable.replaceAll(xReplaceDescriptor);
					}else if (stagvalue instanceof TemplateDocumentInfo){
			        	TemplateDocumentInfo documentInfo = (TemplateDocumentInfo)stagvalue;
			        	if (StringUtils.isNotEmpty(documentInfo.getBokkmark())){
							ooHelper.insertContentBookmark(documentInfo, tag, xReplaceDescriptor, xReplaceable);
			        		//insertContentBookmark(documentInfo, xComponent, xTextRangeReplace);
			        	}else if (documentInfo.isAsText()){
				        	//Insertar el contenido del documento como texto => Si el documento tiene imagenes no las inserta
				        	XComponent xComponent1 = ooHelper.loadDocument(((TemplateDocumentInfo)stagvalue).getUrl());
				        	XTextDocument xtextdocument = ( XTextDocument ) UnoRuntime.queryInterface(XTextDocument.class, xComponent1 );
				        	xReplaceDescriptor.setSearchString(tag);
				        	xReplaceDescriptor.setReplaceString(xtextdocument.getText().getString());
				        	xReplaceable.replaceAll(xReplaceDescriptor);
			        	}else{
						//Insertar el documento
			        		ooHelper.insertDocument(documentInfo.getUrl(), xTextRangeReplace.getEnd(), tag, xReplaceDescriptor,xReplaceable);
							//Se borra el salto de pagina que se inserta con el documento
			        		ooHelper.deletePageBreak(xComponent, xTextRangeReplace.getStart());
			        	}
					}else if (stagvalue instanceof TemplateGraphicInfo){
						count++;
						TemplateGraphicInfo graphicInfo = ((TemplateGraphicInfo)stagvalue);
						
						insertaImagen(xComponent, xTextRangeReplace, count, graphicInfo);
					}else if (stagvalue instanceof TemplateTableInfo){
						TemplateTableInfo tableInfo = ((TemplateTableInfo)stagvalue);
						ooHelper.insertTable(tableInfo, xComponent, xTextRangeReplace);
					}else{
						xReplaceDescriptor.setSearchString(tag);
						xReplaceDescriptor.setReplaceString((String)stagvalue);
						xReplaceable.replaceAll(xReplaceDescriptor);
					}
				}
			}
        } catch (ISPACException e) {
        	logger.error("Error al generar el documento - "+e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error("Error al generar el documento - "+e.getMessage(), e);
			throw new ISPACException("Error al generar el documento - "+e.getMessage(), e);
		}
	}
}
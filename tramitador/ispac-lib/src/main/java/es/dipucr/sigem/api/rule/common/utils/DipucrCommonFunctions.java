package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;

import es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrCommonFunctions 
{
	public static final Logger logger = Logger.getLogger(DipucrCommonFunctions.class);
	
	//[eCenpri-Felipe Ticket R#25] 29.07.2010 MÈtodo Concatena se come el primer car·cter de los odt
	//Se crean las constantes para las extensiones DOC y ODT
	public static String _FORMAT_DOC = Constants._EXTENSION_DOC;
	public static String _FORMAT_ODT = Constants._EXTENSION_ODT;
	
	
	
	/**
	* FunciÛn que elimina acentos y caracteres especiales de
	* una cadena de texto.
	* @param input
	* @return cadena de texto limpia de acentos y caracteres especiales.
	*/
	public static String limpiarCaracteresEspeciales(String input) {

		 // Cadena de caracteres original a sustituir.
		 String original = " ·‡‰ÈËÎÌÏÔÛÚˆ˙˘uÒ¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹—Á«";
		 // Cadena de caracteres ASCII que reemplazar·n los originales.
		 String ascii    = "_aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		 String output = input;
	
		 for (int i=0; i<original.length(); i++) {
			 // Reemplazamos los caracteres especiales.
			 output = output.replace(original.charAt(i), ascii.charAt(i));
		 }
		 return output;
	}
	
	
	/**
	 * [eCenpri-Felipe Ticket R#25] Este mÈtodo har· los mismo que el antiguo Concatena
	 * Se pasa directamente la extensiÛn doc
	 * @param xComponent
	 * @param file
	 * @param ooHelper
	 * @throws ISPACException
	 * @author Felipe
	 * @since 30.08.2010
	 */
	public static void Concatena(XComponent xComponent , String file, OpenOfficeHelper ooHelper)throws ISPACException
	{
		//[eCenpri-Felipe Ticket #95] 08.10.2010 
		//Llamamos directamente al ConcatenaODT
		//Concatena(xComponent, file, ooHelper, _FORMAT_DOC);
		ConcatenaODT(xComponent, file);
	}
	
	/**
	 * [eCenpri-Felipe Ticket R#25]
	 * Llamada desde fuera al mÈtodo privado Concatena
	 * @param xComponent
	 * @param file
	 * @param ooHelper
	 * @throws ISPACException
	 * @author Felipe
	 * @since 30.08.2010
	 */
	public static void ConcatenaByFormat(XComponent xComponent , String file, String fileFormat)throws ISPACException
	{
		if (fileFormat.equals(_FORMAT_DOC)){
			ConcatenaDOC(xComponent, file);
		}
		else if (fileFormat.equals(_FORMAT_ODT)){
			ConcatenaODT(xComponent, file);
		}
		
	}
	
	/**
	 * [eCenpri-Felipe Ticket #95] 08.10.2010 Error mÈtodo concatena
	 * MÈtodo copiado del OpenOfficeHelper.concatFiles pero insertando un salto de parrafo para que conserve el estilo
	 * Este mÈtodo sirve para los odt, aunque a veces pierde el estilo de p·rrafo de la lÌnea
	 * @param xComponent
	 * @param file
	 * @param ooHelper
	 * @param fileFormat
	 * @throws ISPACException
	 */
	public static void ConcatenaODT(XComponent xComponent , String fileName) throws ISPACException{
		try{
			
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    
		    // create a text cursor
		    XTextCursor xTextCursor = xText.createTextCursor();
		    xTextCursor.gotoRange(xText.getEnd(),false);
		    
		    //Insertar un salto de p·gina para que conserve el estilo
		    xText.insertControlCharacter(xTextCursor, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
		    
		    XDocumentInsertable xDocInsert = (XDocumentInsertable)UnoRuntime.queryInterface(XDocumentInsertable.class, xTextCursor);
			xDocInsert.insertDocumentFromURL(fileName, null);
			
		}catch(Exception e){
			
			throw new ISPACException(e);
		}
	}	
	
	/**
	 * En este mÈtodo se hicieron todas las guarrerÌas de los Bookmarks, etc.. para evitar
	 * que concatenara los archivos .doc con un salto de p·gina
	 * @param xComponent
	 * @param file
	 * @throws ISPACException
	 */
	private static void ConcatenaDOC(XComponent xComponent , String file)throws ISPACException
	{
		// La funciÛn ooHelper.concatFiles mete saltos de p·gina cuando quiere. 
		// Yo quiero concatenar a continuaciÛn asÌ que no la puedo usar y tengo
		// que hacer un apaÒo a mano. M·s info en:
		//      http://www.oooforum.org/forum/viewtopic.phtml?t=28376
		//ooHelper.concatFiles(xComponent, file);
		try
		{
			//Obtengo el texto del documento y muevo el cursor al final del mismo
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    XTextCursor xTextCursor = xText.createTextCursor();
		    xTextCursor.gotoRange(xText.getEnd(),false);

		    //Marco el comienzo del nuevo documento con un bookmark
		    //Para anclar el bookmark uso un caracter, por ejemplo 'X'
		    xText.insertString(xTextCursor, "X", false);
		    xTextCursor.goLeft((short)1, false);
		    Random rand = new Random(); //No hace falta pero asÌ aseguro que no se repiten bookmarks
		    String bookmarkName = "dummyBookmark" + String.valueOf(rand.nextInt());
		    insertBookmark(xTextDocument, xTextCursor, bookmarkName);
		    xTextCursor.gotoRange(xText.getEnd(),false);
		    
		    //Inserto el documento. Esto aÒadir· un salto de p·gina
		    XDocumentInsertable xDocInsert = (XDocumentInsertable)UnoRuntime.queryInterface(XDocumentInsertable.class, xTextCursor);
			xDocInsert.insertDocumentFromURL(file, null);
			//Esperamos un tiempo a que concluya la operaciÛn
			//para evitar problemas de sincronizaciÛn.
			//Thread.sleep(3000);
			
			//Recupero el bookmark y tomo su ancla, que se sit˙a sobre el caracter 'X'
			XBookmarksSupplier xBookmarksSupplier = (XBookmarksSupplier)UnoRuntime.queryInterface(XBookmarksSupplier.class, xComponent);
			XNameAccess xNamedBookmarks = xBookmarksSupplier.getBookmarks();
			Object foundBookmark = xNamedBookmarks.getByName(bookmarkName);
			XTextContent xFoundBookmark = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, foundBookmark);
			XTextRange xAncla = xFoundBookmark.getAnchor();
			
			//Coloco el ViewCursor en el ancla
			XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent); 
			XController xController = xModel.getCurrentController(); 
			XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController); 
			XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor(); 
			xViewCursor.gotoRange(xAncla, false);
			
		    //Comienzo el nuevo documento en una lÌnea nueva
			//Meto el salto de linea entre el salto de secciÛn y el principio de la nueva secciÛn
			//porque de este modo protejo la hoja de estilos del primer p·rrafo del documento
			xViewCursor.goRight((short)2,false);
			xText.insertControlCharacter(xViewCursor, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
			xViewCursor.goLeft((short)3,false);
			
			//Pulso 'delete' por medio de un dispatcher para borrar el salto de p·gina
			//El primer delete borra la 'X' y con ello el bookmark
			//El segundo delete borra el salto de p·gina
			//(el mÈtodo deletePageBreak en realidad no tiene nada que ver con saltos de p·gina)
			//ooHelper.deletePageBreak(xComponent, xViewCursor);
			//ooHelper.deletePageBreak(xComponent, xViewCursor);
			xViewCursor.goRight((short)2,true);
			xViewCursor.setString("");
			
			//Devuelvo el viewcursor al final del documento
			xTextCursor.gotoEnd(false);

			//[Manu Ticket #356] INICIO - SIGEM Secretaria Insertar en las propuestas los certificados y las notificaciones.
			//xViewCursor.gotoRange(xTextCursor, false);
			//[Manu Ticket #356] FIN - SIGEM Secretaria Insertar en las propuestas los certificados y las notificaciones.
			
			//liberar memoria
//			xTextDocument.dispose();
//			xTextDocument = null;
//			xText = null;

		}
		catch(Exception e)
		{
			logger.error("Error en el mÈtodo concatenaDOC: " + e.getMessage(), e);
			throw new ISPACException("Error en el mÈtodo concatenaDOC: " + e.getMessage(), e);
		}
	}
	
	private static void insertBookmark(XTextDocument xTextDocument, XTextRange xTextRange, String name)throws ISPACException
	{
		try
		{
			//Creo un bookmark vacio
			XMultiServiceFactory xDocMSF = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDocument);
			Object xObject = xDocMSF.createInstance("com.sun.star.text.Bookmark");

			//Asigno el nombre al bookmark
			XNamed xNameAccess = (XNamed) UnoRuntime.queryInterface(XNamed.class, xObject);
			xNameAccess.setName(name);

			//Inserto el bookmark en el sitio indicado
			XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(XTextContent.class, xNameAccess);
			xTextDocument.getText().insertTextContent(xTextRange, xTextContent, false);
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}
	
	
	/**
	 * [eCenpri-Felipe Ticket#164] Se usa por primera vez en el libro de decretos
	 * Devuelve el nombre del mes en Castellano a partir de su numeral
	 * @param numMes
	 * @return
	 * @throws ISPACException
	 */
	public static String getNombreMes(int numMes){
		
		String mes = null;
		
		switch(numMes){
			case 1: mes = "Enero";
			break;
			case 2: mes = "Febrero";
			break;
			case 3: mes = "Marzo";
			break;
			case 4: mes = "Abril";
			break;
			case 5: mes = "Mayo";
			break;
			case 6: mes = "Junio";
			break;
			case 7: mes = "Julio";
			break;
			case 8: mes = "Agosto";
			break;
			case 9: mes = "Septiembre";
			break;
			case 10: mes = "Octubre";
			break;
			case 11: mes = "Noviembre";
			break;
			case 12: mes = "Diciembre";
			break;
			default:
				//throw new ISPACException("El mes introducido no es v·lido");
		}
		
		return mes;
	}
	
	public static void adjuntarContenido(IRuleContext rulectx, String numexp, XComponent xComponent, OpenOfficeHelper ooHelper) throws ISPACException{
		try
		{
	        IClientContext cct = (ClientContext) rulectx.getClientContext();
			
			//Obtenemos los docuementos de la propuesta
	        IItemCollection collection = DocumentosUtil.getDocumentos(cct, numexp);
	        
	        Iterator<?> it = collection.iterator();
	        boolean found = false;
	        while (it.hasNext() && !found)
	        {
	        	IItem iDoc = (IItem)it.next();
	        	
	        	//El contenido de la propuesta tiene que estar en formato Word (.doc)
	        	String extension = iDoc.getString("EXTENSION");
	        	if ( (extension.toUpperCase().compareTo("DOC")==0) || (extension.toUpperCase().compareTo("ODT") == 0))
	        	{
	        		//En concreto busco documentos de tipo Anexo a la Solicitud (propuestas desde Registro Telem·tico)
	        		//o de tipo Contenido de la propuesta (propuestas iniciadas desde escritorio de tramitaciÛn)
		        	String nombre = iDoc.getString("NOMBRE");
		        	if ( nombre.compareTo("Anexo a Solicitud" )== 0 ||
		        		 nombre.compareTo("Contenido de la propuesta") == 0)
		        	{
		        		found = true;
			        	String strInfoPag = iDoc.getString("INFOPAG");
			        	if (strInfoPag != null)
			        	{
			        		File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			        		DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
			        		file.delete();
			        	}
		        	}
	        	}
	        }
		}
		catch(Exception e)
		{
			throw new ISPACException(e); 	
		}
	}	
	
	public static void concatenaPartes(IRuleContext rulectx, String numexp_origen, String strNombreDocCab, String strNombreDocPie, String strNombreDoc, String descripcion, OpenOfficeHelper ooHelper) throws ISPACException{
		concatenaPartes(rulectx, numexp_origen, strNombreDocCab, strNombreDocPie, strNombreDoc, descripcion, ooHelper, Constants._EXTENSION_ODT);
	}

	public static IItem concatenaPartes(IRuleContext rulectx, String numexp_origen, String strNombreDocCab, String strNombreDocPie, String strNombreDoc, String descripcion, OpenOfficeHelper ooHelper, String extension) throws ISPACException
	{
		IItem docResultado = null;
		//Com˙n a Notificaciones y Traslados
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
	        //----------------------------------------------------------------------------------------------
			
			logger.info("CABECERA GENERADA ");
	        //Cabecera
        	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, strNombreDocCab);
        	File file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
    		
    		//Contenido
    		logger.info("CONTENIDO GENERADO ");
    		DipucrCommonFunctions.adjuntarContenido(rulectx, numexp_origen, xComponent, ooHelper);
    		
    		//Pie
    		logger.info("PIE GENERADO ");
        	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, strNombreDocPie);
    		File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
    		//DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
			DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
    		file.delete();
    		
    		//Guarda el resultado
    		logger.info("RESULTADO GUARDADO");
    		docResultado = DipucrCommonFunctions.guardarDocumento(rulectx, xComponent, strNombreDoc, descripcion, extension);
    		file1.delete();
    		
    		//Borra los documentos intermedios del gestor documental
	        IItemCollection collection = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), " (DESCRIPCION LIKE '" + strNombreDocCab + "%'" + " OR DESCRIPCION LIKE '" + strNombreDocPie + "%')", "");
	        Iterator<?> it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
	        ooHelper.dispose();
        } 
		catch(Exception e)
		{
        	throw new ISPACException(e);
		}
    	return docResultado;
	}
	
	public static IItem guardarDocumento(IRuleContext rulectx, XComponent xComponent, String strNombreDoc, String descripcion, String extension) throws ISPACException
	{
		IItem doc = null;
		try
		{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
	        //----------------------------------------------------------------------------------------------
			
    		//Guarda el resultado en repositorio temporal	
	        
	     // Guarda el resultado en repositorio temporal
 			String fileName = FileTemporaryManager.getInstance().newFileName(extensionEntidad);
 			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
 			File file = new File(fileName);
	     			
			String filter = DocumentosUtil.getFiltroOpenOffice(extensionEntidad);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(), filter);
    		
    		//Guarda el resultado en gestor documental
	        int tpdoc = DocumentosUtil.getTipoDoc(cct, strNombreDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);    
	        	        
	        if (StringUtils.isNotEmpty(descripcion)) strNombreDoc = strNombreDoc + " - " + descripcion;
    		
	        doc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, strNombreDoc, file, extensionEntidad); 

    		if(file != null && file.exists()) file.delete();
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return doc;
	}

	public static void guardarDocumento(IRuleContext rulectx, XComponent xComponent, String strNombreDoc, String descripcion) throws ISPACException
	{
		guardarDocumento(rulectx, xComponent, strNombreDoc, descripcion, Constants._EXTENSION_ODT);
	}
	/**Transforma la cadena a la primera letra en mayusculas y
	 * el resto en minusculas
	 * @throws ISPACException **/
	public static String transformarMayusMinus(String nombreParti) throws ISPACException {

		Vector<String> resultado = new Vector<String>();
		try {
			int iEspacioNombreParti = nombreParti.indexOf(" ");
			while (iEspacioNombreParti != -1) {
				String nombre = nombreParti.substring(0, iEspacioNombreParti);
				nombre = nombre.toLowerCase();
				String cNombre = nombre.substring(0, 1);
				cNombre = cNombre.toUpperCase();
				nombre = cNombre + nombre.substring(1, nombre.length());
				resultado.add(nombre);
				nombreParti = nombreParti.substring(iEspacioNombreParti + 1, nombreParti.length());
				iEspacioNombreParti = nombreParti.indexOf(" ");
			}
			nombreParti = nombreParti.toLowerCase();
			String cNombre = nombreParti.substring(0, 1);
			cNombre = cNombre.toUpperCase();
			nombreParti = cNombre + nombreParti.substring(1, nombreParti.length());
			resultado.add(nombreParti);

		} catch (Exception e) {
			logger.error("Error al subtituir en mayusculas al nombre. " + nombreParti + " - " + e.getMessage(), e);
			throw new ISPACException(e);
		}

		return mostrar(resultado);
	}

	@SuppressWarnings("rawtypes")
	private static String mostrar(Vector resultado) {
		String nombre = "";
		for(int i= 0; i<resultado.size();i++){
			nombre+=resultado.get(i)+" ";
		}
		return nombre;
	}
	
	/**
	 * Metodo que se encarga de insertar en la bbdd los datos del envÌo del email
	 * @param entitiesAPI 
	 * @param rulectx 
	 * @param cct 
	 * @throws ISPACRuleException 
	 * **/
	public static boolean insertarAcuseEmail(String nombreNotif, Date fechaEnvÌo,
			String nombreDoc, String descripcionDoc, boolean enviadoEmail, String emailNotif,
			String descripError, IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			String numexp = "";
			if(rulectx.getNumExp() == null){
				 int idDoc = rulectx.getInt("ID_DOCUMENTO");
					//int idCircuito = rulectx.getInt("ID_CIRCUITO");//TODO
					IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);//TODO
					numexp = itemDocumento.getString("NUMEXP");
			}
			else{
				numexp = rulectx.getNumExp();
			}
			IItem entidad = entitiesAPI.createEntity(Constants.TABLASBBDD.DPCR_ACUSE_EMAIL, numexp);
			if(StringUtils.isNotEmpty(nombreNotif) && nombreNotif.length()>128){
				entidad.set("NOMBRE_NOTIF", nombreNotif.substring(0, 127));
			}
			else{
				entidad.set("NOMBRE_NOTIF", nombreNotif);
			}
			
			if(StringUtils.isNotEmpty(nombreDoc) && nombreDoc.length()>128){
				entidad.set("NOMBRE_DOC", nombreDoc.substring(0, 127));
			}
			else{
				entidad.set("NOMBRE_DOC", nombreDoc);
			}
		    
	        entidad.set("FECHA_ENVIO", fechaEnvÌo);
	        entidad.set("DESCRIPCION_DOCUMENTO", descripcionDoc);
	        
	        if(enviadoEmail == true){
	        	entidad.set("ENVIADO", 1);
	        }
	        else{
	        	entidad.set("ENVIADO", 0);
	        }
	        
	        entidad.set("EMAIL", emailNotif);
	        
	        entidad.set("DESCRIPCION_ERROR", descripError);
	        entidad.store(cct);
		} catch (ISPACRuleException e) {

			 throw new ISPACRuleException("Error. "+e.getMessage(), e);
		} catch (ISPACException e) {

			 throw new ISPACRuleException("Error. "+e.getMessage(), e);
		}
		return new Boolean(true);
		
	}
	
	/**
	 * MQE MÈtodo que devuelve una colecciÛn con todos los procedimientos para los que un usuario tiene permiso (iniciar expediente)
	 * comprueba tanto sus grupos, como su departamento, y los permisos individuales
	 * @param cct 
	 * @param idAutor 
	 * @param entitiesAPI 
	 * @return Los ids de todos los procedimientos para los que un usuario tiene permiso
	 */
	public static IItemCollection getProcedimientosPermisoIniciarExpUsuario(ClientContext cct, IEntitiesAPI entitiesAPI, ICatalogAPI catalogAPI, int idAutor){
		IItemCollection procedimientos = null;
		ResultSet procedimientosPermisos = null;
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		try{
			AccesoBBDDRegistro accsRegistro = new AccesoBBDDRegistro(entidad);
			
			Vector<?> groupsUser = accsRegistro.getGruposPerteneceUsuario(idAutor);
			//Recibimos los ids, montammos el id del grupo que es de la forma 3-IDGRUPO
			Vector<String> idGroupsUser = new Vector<String>();
			for (int i=0; i< groupsUser.size(); i++){
				idGroupsUser.add("3-"+groupsUser.get(i));
			}
			String idDepartamentoUser = "2-"+accsRegistro.getDepartamentoPerteneceUsuario(idAutor);
			
			//Ya tenemos los grupos, el departamento y el id del usuario, ya podemos mirar para quÈ procedimientos tiene permisos
			//AÒadimos primero los grupos
			String consultaPermisoProced = "SELECT ID_PCD FROM SPAC_SS_PERMISOS WHERE UID_USR IN(";
			for (int i=0; i<idGroupsUser.size(); i++){
				consultaPermisoProced += "'"+idGroupsUser.get(i)+"'";
				if(i<idGroupsUser.size()) consultaPermisoProced += ",";
			}
			//AÒadimos el departamento
			consultaPermisoProced += "'"+idDepartamentoUser+"'";
			//AÒadimos el usuario
			consultaPermisoProced += ",";
			consultaPermisoProced += "'1-"+idAutor+"'";
			consultaPermisoProced += ")";
			procedimientosPermisos = cct.getConnection().executeQuery(consultaPermisoProced).getResultSet();
			
			if(procedimientosPermisos != null){
				String consulta = "WHERE ID IN (";
	
				int i=0;
				while(procedimientosPermisos.next()){
					if(i>0)
						consulta += ",";
					consulta += procedimientosPermisos.getInt("ID_PCD");
					i++;
				}
				consulta += ")";
				procedimientos = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_P_PROCEDURE,consulta);
			}						
		}
		catch(ISPACException e){
			logger.error(e.getMessage(), e);
		}
		catch(Exception e1){
			logger.error(e1.getMessage(), e1);
		}
		return procedimientos;
	}
	/** 
	 * MQE fin mÈtodo
	 */
	
	public static String getVarGlobal(String varName) {
		String valor = "";
		try{
			ClientContext cct = new ClientContext();
			IInvesflowAPI invesFlowAPI = new InvesflowAPI(cct);
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_VARS", "WHERE NOMBRE = '" + DBUtil.replaceQuotes(varName) + "'");
			
			Iterator<?> it = collection.iterator();
			if (it.hasNext()) {
				valor = ((IItem)it.next()).getString("VALOR");
			}
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return valor;
	}
	
	/**
	 * Convierte una lista en un IN SQL ('a1','a2',...,'an')
	 * @param lista
	 * @return
	 */
	public static String convertirListaInSQL(List<String> lista){

		StringBuffer sb = new StringBuffer(); 
		if (null != lista && lista.size() > 0){
			sb.append("(");
			for (int i=0; i<lista.size(); i++){
				sb.append("'");
				sb.append(lista.get(i));
				sb.append("'");
				if (i < (lista.size() - 1)){
					sb.append(",");
				}
			}
			sb.append(")");
		}
		return sb.toString();
	}

}
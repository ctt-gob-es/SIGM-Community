package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sun.star.beans.XPropertySet;
import com.sun.star.connection.NoConnectException;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.DisposedException;
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

class CommonFunctions 
{
	public static List getPropuestas(IRuleContext rulectx, IEntitiesAPI entitiesAPI) throws ISPACException 
	{
		List list=null, numbers=null, sorted=null;
		IItem item=null;
		Integer number;
		int orden;
		String strOrden;
		
		try
		{
			//Obtención de la lista de propuestas y urgencias
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        list = collection.toList();
	        collection = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
	        list.addAll(collection.toList());
	        
	        //Ordenación de la lista
	        numbers = new ArrayList();
	        Iterator it = list.iterator();
	        while (it.hasNext())
	        {
	        	item = (IItem)it.next();
	        	strOrden = item.getString("ORDEN");
	        	if (strOrden != null)
	        	{
	        		orden = Integer.parseInt(strOrden); 
	        	}
	        	else
	        	{
	        		orden = Integer.MAX_VALUE;
	        	}
	        	numbers.add(new Integer(orden));
	        }
	        Collections.sort(numbers);
	        sorted = new ArrayList();
	        it = numbers.iterator();
	        while (it.hasNext())
	        {
	        	number = (Integer)it.next();
	        	orden = number.intValue();
	        	moveItem(list, sorted, orden);
	        }	        
		}
		catch(ISPACException e)
		{
        	throw new ISPACException(e);
        }
		return sorted;
	}

	private static void moveItem(List origen, List destino, int orden) throws ISPACException 
	{
		IItem item = null;
		int n;
		String strOrden;

		try
		{
			boolean found = false;
			Iterator it = origen.iterator();
			while (!found && it.hasNext())
			{
				item = (IItem)it.next();
	        	strOrden = item.getString("ORDEN");
	        	if (strOrden != null)
	        	{
	        		n = Integer.parseInt(strOrden); 
	        	}
	        	else
	        	{
	        		n = Integer.MAX_VALUE;
	        	}
				found = n==orden; 
			}
			if(found)
			{
				destino.add(item);
				it.remove();
			}
		}
		catch(ISPACException e)
		{
        	throw new ISPACException(e);
        }
	}

	public static File getFile(IGenDocAPI gendocAPI, String strInfoPag) throws ISPACException
	{
		File file = null;
		Object connectorSession = null;
		
		try
		{
			connectorSession = gendocAPI.createConnectorSession();

			String extension = "doc";
				
			String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			
			OutputStream out = new FileOutputStream(fileName);
			gendocAPI.getDocument(connectorSession, strInfoPag, out);
			
			file = new File(fileName);
		}
		catch(Exception e)
		{
			throw new ISPACException(e); 	
		}
		return file;
	}
	
	public static String getInfoPag(IRuleContext rulectx, IEntitiesAPI entitiesAPI, String descripcion) throws ISPACException
	{
		String strInfoPag = "";
		
		try
		{
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION = '" + descripcion + "'";
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        Iterator it = collection.iterator();
	        if (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	strInfoPag = doc.getString("INFOPAG");
	        }
			
		}
		catch(Exception e)
		{
			throw new ISPACException(e); 	
		}
		return strInfoPag;
	}
	
	public static void Concatena(XComponent xComponent , String file, OpenOfficeHelper ooHelper)throws ISPACException
	{
		// La función ooHelper.concatFiles mete saltos de página cuando quiere. 
		// Yo quiero concatenar a continuación así que no la puedo usar y tengo
		// que hacer un apaño a mano. Más info en:
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
		    Random rand = new Random(); //No hace falta pero así aseguro que no se repiten bookmarks
		    String bookmarkName = "dummyBookmark" + String.valueOf(rand.nextInt());
		    insertBookmark(xTextDocument, xTextCursor, bookmarkName);
		    xTextCursor.gotoRange(xText.getEnd(),false);
		    
		    //Inserto el documento. Esto añadirá un salto de página
		    XDocumentInsertable xDocInsert = (XDocumentInsertable)UnoRuntime.queryInterface(XDocumentInsertable.class, xTextCursor);
			xDocInsert.insertDocumentFromURL(file, null);
			//Esperamos un tiempo a que concluya la operación
			//para evitar problemas de sincronización.
			//Thread.sleep(3000);
			
			//Recupero el bookmark y tomo su ancla, que se sitúa sobre el caracter 'X'
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
			
		    //Comienzo el nuevo documento en una línea nueva
			//Meto el salto de linea entre el salto de sección y el principio de la nueva sección
			//porque de este modo protejo la hoja de estilos del primer párrafo del documento
			xViewCursor.goRight((short)2,false);
			xText.insertControlCharacter(xViewCursor, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
			xViewCursor.goLeft((short)3,false);
			
			//Pulso 'delete' por medio de un dispatcher para borrar el salto de página
			//El primer delete borra la 'X' y con ello el bookmark
			//El segundo delete borra el salto de página
			//(el método deletePageBreak en realidad no tiene nada que ver con saltos de página)
			//ooHelper.deletePageBreak(xComponent, xViewCursor);
			//ooHelper.deletePageBreak(xComponent, xViewCursor);
			xViewCursor.goRight((short)2,true);
			xViewCursor.setString("");
			
			
			//Devuelvo el viewcursor al final del documento
			xTextCursor.gotoEnd(false);
			xViewCursor.gotoRange(xTextCursor, false);
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
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

	public static void adjuntarContenido(IRuleContext rulectx, String numexp, XComponent xComponent, OpenOfficeHelper ooHelper) throws ISPACException
	{
		try
		{
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			
			//Obtenemos los docuementos de la propuesta
			String strQuery = "WHERE NUMEXP = '" + numexp + "'";
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        Iterator it = collection.iterator();
	        boolean found = false;
	        while (it.hasNext() && !found)
	        {
	        	IItem iDoc = (IItem)it.next();
	        	
	        	//El contenido de la propuesta tiene que estar en formato Word (.doc)
	        	String extension = iDoc.getString("EXTENSION");
	        	if ( extension.toUpperCase().compareTo("DOC")==0)
	        	{
	        		//En concreto busco documentos de tipo Anexo a la Solicitud (propuestas desde Registro Telemático)
	        		//o de tipo Contenido de la propuesta (propuestas iniciadas desde escritorio de tramitación)
		        	String nombre = iDoc.getString("NOMBRE");
		        	if ( nombre.compareTo("Anexo a Solicitud" )== 0 ||
		        		 nombre.compareTo("Contenido de la propuesta") == 0)
		        	{
		        		found = true;
			        	String strInfoPag = iDoc.getString("INFOPAG");
			        	if (strInfoPag != null)
			        	{
			        		File file = CommonFunctions.getFile(gendocAPI, strInfoPag);
			        		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
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
	
	public static void insertaSaltoDePagina(XComponent xComponent) throws ISPACException
	{
		//No se puede insertar un salto de página directamente.
		//Los saltos de página son una propiedad de los párrafos
		//Así que inserto un nuevo párrafo y luego le ajusto la propiedad
		
		try
		{
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
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}
	
	public static boolean existeTramite(IRuleContext rulectx, String tramite) throws ISPACException
	{
		//Devuelve 'true' si ya existía con anterioridad otro trámite con el nombre 'tramite'
		boolean existe = false;
		try
		{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND NOMBRE = '" + tramite + "'";
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_TRAMITES", strQuery);
	        Iterator it = collection.iterator();
	        if (it.hasNext())
	        {
	        	//Parece que existe pero puede ser este mismo trámite
	        	String strId = ((IItem)it.next()).getString("ID");
	        	int id = Integer.parseInt(strId);
	        	existe = id != rulectx.getTaskId();
	        }
		}
		catch(Exception e)
		{
			throw new ISPACException(e); 	
		}
		
		return existe;
	}

	public static String getTipoSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String tipo = "";
		try
		{
			IItem sesion = getSesion(rulectx, numexp);
			String strTipo = sesion.getString("TIPO"); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR='" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_TIPOSESION", strQuery);
	        Iterator it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return tipo;
	}

	public static String getOrganoSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String strOrgano = "";
		try
		{
			IItem sesion = getSesion(rulectx, numexp);
			strOrgano = sesion.getString("ORGANO"); 
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return strOrgano;
	}

	public static String getNombreOrganoSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String tipo = "";
		try
		{
			String strTipo = getOrganoSesion(rulectx, numexp); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR='" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_ORGANOS", strQuery);
	        Iterator it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return tipo;
	}

	public static String getAreaSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String strOrgano = "";
		try
		{
			IItem sesion = getSesion(rulectx, numexp);
			strOrgano = sesion.getString("AREA"); 
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return strOrgano;
	}

	public static String getNombreAreaSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		String tipo = "";
		try
		{
			String strTipo = getAreaSesion(rulectx,numexp); 
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String strQuery = "WHERE VALOR='" + strTipo + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_AREAS", strQuery);
	        Iterator it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem item = (IItem)it.next();
	        	tipo = item.getString("SUSTITUTO");
	        }
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return tipo;
	}
	
	public static IItem getSesion(IRuleContext rulectx, String numexp)  throws ISPACException
	{
		IItem sesion = null;
		try
		{
			if (numexp==null) numexp=rulectx.getNumExp();
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String strQuery = "WHERE NUMEXP='"+numexp+"'";
			IItemCollection sesiones = entitiesAPI.queryEntities("SECR_SESION", strQuery);
			Iterator it = sesiones.iterator();
			if (it.hasNext())
			{
				sesion = (IItem)it.next();
			}
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return sesion;
	}

	public static String createNumConvocatoria(IRuleContext rulectx) throws ISPACException
	{
		String numconv = "?";
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);

	        String strQuery = "WHERE YEAR='"+String.valueOf(year)+"'";
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_CONVOCATORIA", strQuery);
	        int numero = collection.toList().size();
	        numero = numero + 1;
	        numconv = String.valueOf(year) + "/" + String.valueOf(numero);
	        
	        IItem iConv = entitiesAPI.createEntity("SECR_CONVOCATORIA", rulectx.getNumExp());
	        iConv.set("YEAR", year);
	        iConv.set("NUMERO", numero);
	        iConv.set("NUMEXP_ORIGEN", rulectx.getNumExp());
	        iConv.store(cct);
	        
        	return numconv;
    		
        } 
		catch(ISPACException e)
		{
        	throw new ISPACException(e);
		}
	}
	
	public static void createPropuestaAprobacionActaAnterior(IRuleContext rulectx) throws ISPACException
	{
		try
		{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------

	        //Creación del expediente
	        String strQuery = "WHERE NOMBRE='Propuesta'";
	        IItemCollection coll = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", strQuery);
	        Iterator it = coll.iterator();
	        int nProcedure = 0;
	        IItem proc = null;
	        int n;
	        String cod_pcd = "";
	        while (it.hasNext()) {
	        	proc = (IItem)it.next();
	        	n = proc.getInt("ID");
	        	if ( n > nProcedure )
	        	{
	        		nProcedure = n;
	        		cod_pcd = proc.getString("COD_PCD");
	        	}
	        }
	        Map params = new HashMap();
			params.put("COD_PCD", cod_pcd);
	        int idExp = tx.createProcess(nProcedure, params);
			IProcess process = invesFlowAPI.getProcess(idExp);
			String numexp = process.getString("NUMEXP");	        
			IItem iProp = entitiesAPI.createEntity("SECR_PROPUESTA", numexp);

			//Inicialización de datos de la propuesta
			String strOrgano = getOrganoSesion(rulectx,null);
			iProp.set("ORIGEN", "0001");
			iProp.set("DESTINO", strOrgano);
			iProp.set("EXTRACTO", "Conocimiento y aprobación, si procede, del borrador del acta de la sesión anterior");
			iProp.store(cct);
			
			//Campo Asunto del expediente de la propuesta
        	String strEstado = getEstadoAdmPropuesta(rulectx);
	        strQuery = "WHERE NUMEXP='" + numexp + "'";
			coll = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", strQuery);
	        it = coll.iterator();
	        if (it.hasNext()) 
	        {
	        	IItem iExp = (IItem)it.next();
	        	iExp.set("ASUNTO", "Propuesta aprobación acta anterior");
	        	iExp.set("ESTADOADM", strEstado);
	        	iExp.store(cct);
	        }
			
	        //Relación con expediente de sesión
	        strQuery = "WHERE NOMBRE='SPAC_EXP_RELACIONADOS'";
	        coll = entitiesAPI.queryEntities("SPAC_CT_ENTIDADES", strQuery);
	        it = coll.iterator();
	        if (it.hasNext())
	        {
	        	IItem iExpRel = (IItem)it.next();
	        	int id = iExpRel.getInt("ID");
		        IItem iRelacion = entitiesAPI.createEntity(id);
		        iRelacion.set("NUMEXP_PADRE", numexp);
		        iRelacion.set("NUMEXP_HIJO", rulectx.getNumExp());
		        iRelacion.set("RELACION", "Sesión/Propuesta");
		        iRelacion.store(cct);
	        }
		}
		catch (Exception e)
		{
        	throw new ISPACException(e);
		}
	}
	
	public static String getEstadoAdmPropuesta(IRuleContext rulectx) throws ISPACException
	{
		String strEstado = "PR";
		try
		{
			String strOrgano = getOrganoSesion(rulectx,null);
        	if (strOrgano.compareTo("PLEN")==0)
        	{
        		strEstado = "SEC_PL";
        	}
        	else if (strOrgano.compareTo("JGOB")==0)
        	{
        		strEstado = "SEC_JG";
        	}
        	else if (strOrgano.compareTo("COMI")==0)
        	{
        		strEstado = "SEC_CI";
        	}
        	else if (strOrgano.compareTo("MESA")==0)
        	{
        		strEstado = "SEC_MS";
        	}
		}
		catch (Exception e)
		{
        	throw new ISPACException(e);
		}
		return strEstado;
	}
	
	public static String getNombreDoc(IRuleContext rulectx, String strCodTpDoc) throws ISPACException
	{
		String strNombreTramite = "";
		try
		{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			String strQuery = "WHERE COD_TPDOC='"+strCodTpDoc+"'";
			IItemCollection tramites = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
			Iterator it = tramites.iterator();
			if (it.hasNext())
			{
				IItem tramite = (IItem)it.next();
				strNombreTramite = tramite.getString("NOMBRE");
			}
		}
		catch (Exception e)
		{
        	throw new ISPACException(e);
		}
		return strNombreTramite;
	}

	public static void generarDocumento(IRuleContext rulectx, String nombrePlantilla, String descripcion) throws ISPACException
	{
		try
		{
			//APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();

			// Obtención de los tipos de documento asociados al trámite
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
	    	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	    	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty())
	    	{
	    		throw new ISPACInfo("No hay ningún tipo de documento asociado al trámite");
	    	}

    		// Búsqueda de la plantilla indicada
        	Iterator itTpDocs = taskTpDocCollection.iterator();
        	while(itTpDocs.hasNext())
        	{
		    	IItem taskTpDoc = (IItem)itTpDocs.next();
	    		int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	    		
	        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
	        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty())
	        	{
	        		throw new ISPACInfo("No hay ninguna plantilla asociada al tipo de documento");
	        	}
	    		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
	        	int templateId = tpDocsTemplate.getInt("ID");

	        	String nombre = tpDocsTemplate.getString("NOMBRE");
	        	if (nombre.compareTo(nombrePlantilla)==0)
	        	{
	        		Object connectorSession = null;
	        		try
	        		{
						connectorSession = gendocAPI.createConnectorSession();
						// Abrir transacción para que no se pueda generar un documento sin fichero
				        cct.beginTX();
					
						int taskId = rulectx.getTaskId();
			        	IItem entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
						int documentId = entityDocument.getKeyInt();
		
						// Generar el documento a partir de la plantilla
						IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
						entityTemplate.set("EXTENSION", "doc");
						if ( descripcion != null)
						{
							String templateDescripcion = entityTemplate.getString("DESCRIPCION");
							templateDescripcion = templateDescripcion + " - " + descripcion;
							entityTemplate.set("DESCRIPCION", templateDescripcion);
							entityTemplate.store(cct);
						}
						entityTemplate.store(cct);
					}
					catch (Throwable e)
					{
						// Si se produce algún error se hace rollback de la transacción
						cct.endTX(false);
						
						String message = "exception.documents.generate";
						String extraInfo = null;
						Throwable eCause = e.getCause();
						
						if (eCause instanceof ISPACException)
						{
							if (eCause.getCause() instanceof NoConnectException) 
							{
								extraInfo = "exception.extrainfo.documents.openoffice.off"; 
							}
							else
							{
								extraInfo = eCause.getCause().getMessage();
							}
						}
						else if (eCause instanceof DisposedException)
						{
							extraInfo = "exception.extrainfo.documents.openoffice.stop";
						}
						else
						{
							extraInfo = e.getMessage();
						}			
						throw new ISPACInfo(message, extraInfo);
						
					}
					finally
					{
						if (connectorSession != null)
						{
							gendocAPI.closeConnectorSession(connectorSession);
						}
					}
			    	
		        	// Si todo ha sido correcto se hace commit de la transacción
					cct.endTX(true);
	        	}
        	}
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}

	public static void concatenaPartes(IRuleContext rulectx, String numexp_origen, String strNombreDocCab, String strNombreDocPie, String strNombreDoc, String descripcion, OpenOfficeHelper ooHelper) throws ISPACException
	{
		//Común a Notificaciones y Traslados
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
	        //----------------------------------------------------------------------------------------------

	        //Cabecera
        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, strNombreDocCab);
        	File file1 = CommonFunctions.getFile(gendocAPI, strInfoPag);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
    		
    		//Contenido
    		CommonFunctions.adjuntarContenido(rulectx, numexp_origen, xComponent, ooHelper);
    		
    		//Pie
        	strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, strNombreDocPie);
    		File file = CommonFunctions.getFile(gendocAPI, strInfoPag);
    		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		
    		//Guarda el resultado
    		CommonFunctions.guardarDocumento(rulectx, xComponent, strNombreDoc, descripcion);
    		file1.delete();
    		
    		//Borra los documentos intermedios del gestor documental
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" +
				" AND (DESCRIPCION LIKE '" + strNombreDocCab + "%'" +
				" OR DESCRIPCION LIKE '" + strNombreDocPie + "%')" ;				
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        Iterator it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
        } 
		catch(Exception e)
		{
        	throw new ISPACException(e);
		}
	}

	public static void guardarDocumento(IRuleContext rulectx, XComponent xComponent, String strNombreDoc, String descripcion) throws ISPACException
	{
		try
		{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
			
    		//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			File file = new File(fileName);
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
    		
    		//Guarda el resultado en gestor documental
			String strQuery = "WHERE NOMBRE = '"+strNombreDoc+"'";
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
	        Iterator it = collection.iterator();
	        int tpdoc = 0;
	        if (it.hasNext())
	        {
	        	IItem tpd = (IItem)it.next();
	        	tpdoc = tpd.getInt("ID");
	        }
    		IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
    		FileInputStream in = new FileInputStream(file);
    		int docId = newdoc.getInt("ID");
    		Object connectorSession = gendocAPI.createConnectorSession();
    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strNombreDoc);
    		entityDoc.set("EXTENSION", "doc");
			if ( descripcion != null)
			{
				String templateDescripcion = entityDoc.getString("DESCRIPCION");
				templateDescripcion = templateDescripcion + " - " + descripcion;
				entityDoc.set("DESCRIPCION", templateDescripcion);
			}
			entityDoc.store(cct);
    		file.delete();
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}
}
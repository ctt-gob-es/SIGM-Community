package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
//import ieci.tdw.ispac.api.IProcedureAPI;
//import ieci.tdw.ispac.api.errors.ISPACException;
//import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
//import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

//import com.sun.star.connection.NoConnectException;
//import com.sun.star.frame.XController;
//import com.sun.star.frame.XModel;
//import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;
//import com.sun.star.text.XPageCursor;
//import com.sun.star.text.XText;
//import com.sun.star.text.XTextCursor;
//import com.sun.star.text.XTextDocument;
//import com.sun.star.text.XTextViewCursor;
//import com.sun.star.text.XTextViewCursorSupplier;
//import com.sun.star.uno.UnoRuntime;

public class PrepararAnuncioBopRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(GenerateBopRule.class);
	
//	protected String STR_DocActa = "Borrador de Acta de Pleno";
	protected String STR_Entity = "BOP_PUBLICACION";
	protected String STR_Doc = "BOP - Anuncio";
//	protected String STR_DocInicio = "BOP - Inicio";
//	protected String STR_DocFinal = "BOP - Final";
//	protected String STR_DocFinActa = "BOP - Fin anuncio";
	
	private OpenOfficeHelper ooHelper = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
        try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
	        //----------------------------------------------------------------------------------------------

//	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
//	        IItemCollection bops = entitiesAPI.queryEntities(STR_Entity, strQuery);
//	        Iterator it = bops.iterator();
//        	IItem bop = null;
//	        if(it.hasNext()) {
//	        	bop = ((IItem)it.next());
//	        }
	        //Generación de la diligencia de inicio
//	        generarDocumento(rulectx, STR_DocInicio, null);
//        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, STR_DocInicio);
//	        generarDocumento(rulectx, STR_DocLibro, null);
	        CommonFunctions.generarDocumento(rulectx, STR_Doc, STR_Doc, "borrar");
        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, STR_Doc + " - borrar");
			logger.warn("InfoPag del documento generado: " + strInfoPag);
        	File file1 = CommonFunctions.getFile(rulectx, strInfoPag);
//    		String cnt = "uno:socket,host=localhost,port=8100;urp;StarOffice.NamingService";
//    		ooHelper = OpenOfficeHelper.getInstance(cnt);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
    		
    		File file = null;
//    		String descr = "";
//    		int nPagesBefore = 0;
//    		int nPagesAfter = 0;
			String strQuery2 = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND NOMBRE='Anexo a Solicitud' AND EXTENSION='DOC'";
			IItemCollection docs = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
//	        IItemCollection actas = entitiesAPI.getEntities("SPAC_DT_DOCUMENTOS", rulectx.getNumExp());
	        Iterator it = docs.iterator();
//    		int nActa = 0;
//	        int nActas = actas.toList().size(); 
        	
	        while (it.hasNext())
	        {
//	        	nActa++;
	        	IItem doc = (IItem)it.next();
//	        	IItem doc = getDocument(rulectx, acta);
//	        	IItem sesion = CommonFunctions.getSesion(rulectx, doc.getString("NUMEXP"));
	        	
	        	//Inserta el acta
	        	//---------------
	        	if (doc != null)
	        	{
	        		file = CommonFunctions.getFile(rulectx, doc.getString("INFOPAG"));
	    			logger.warn("InfoPag del documento a concatenar: " + doc.getString("INFOPAG"));
	//        		nPagesBefore = getNumPages(xComponent);
	        		CommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
	//        		nPagesAfter = getNumPages(xComponent);
	        		file.delete();
	        	}
	        }

    		//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
    		file1.delete();
    		
    		//Guarda el resultado en gestor documental
			String strQuery = "WHERE NOMBRE = '" + STR_Doc + "'";
			logger.warn("Query: " + strQuery);
	        IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
	        it = collection.iterator();
	        int tpdoc = 0;
	        if (it.hasNext())
	        {
	        	IItem tpd = (IItem)it.next();
	        	tpdoc = tpd.getInt("ID");
				logger.warn("Documento guardado en el gestor documental (ID): " + tpdoc);
	        }
    		IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
    		FileInputStream in = new FileInputStream(file);
    		int docId = newdoc.getInt("ID");
    		Object connectorSession = gendocAPI.createConnectorSession();
    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", STR_Doc);
    		entityDoc.set("EXTENSION", "doc");
    		entityDoc.store(cct);
    		file.delete();
    		
    		//Borra los documentos intermedios del gestor documental
			strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION LIKE '%borrar%'";
	        collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	logger.warn("Borrar (ID): " + doc.getString("ID"));
	        	logger.warn("Borrar (NUMEXP): " + doc.getString("NUMEXP"));
	        	logger.warn("Borrar (NOMBRE): " + doc.getString("NOMBRE"));
	        	logger.warn("Borrar (DESCRIPCION): " + doc.getString("DESCRIPCION"));
	        	entitiesAPI.deleteDocument(doc);
	        }

        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido crear el anuncio del BOP",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

	/*private IItem getDocument(IRuleContext rulectx, IItem acta) throws ISPACException
	{
		IItem doc = null;
		try
		{
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
//			String strId = acta.getString("IDDOC");
//			if ((strId == null) || strId.equals(""))
//			{
//				return null;
//			}
//			String strQuery = "WHERE ID='" + strId + "' AND NOMBRE='Anexo a Solicitud' AND EXTENSION='DOC'";
			String strQuery = "WHERE NOMBRE='Anexo a Solicitud' AND EXTENSION='DOC'";
			IItemCollection docs = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
			Iterator it = docs.iterator();
			if (it.hasNext())
			{
				doc = (IItem)it.next();
			}
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return doc;
	}*/
	
	/*private int getNumPages(XComponent xComponent)  throws ISPACException
	{
		int nPages = 0;
		try
		{
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    XTextCursor xTextCursor = xText.createTextCursor();
		    xTextCursor.gotoRange(xText.getEnd(),false);

			XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent); 
			XController xController = xModel.getCurrentController(); 
			XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController); 
			XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor(); 
			xViewCursor.gotoRange(xTextCursor, false);
		    
	        XPageCursor xPageCursor = (XPageCursor)UnoRuntime.queryInterface(XPageCursor.class, xViewCursor);
	        nPages = (int)xPageCursor.getPage();
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
		return nPages;
	}*/
}

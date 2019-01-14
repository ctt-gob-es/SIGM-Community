package es.dipucr.sigem.api.rule.procedures.lanza;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class PrepararAnuncioLanzaRule implements IRule 
{
	public static final String NOMBRE_DOC_ANUNCIO = "LANZA - Anuncio";
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(PrepararAnuncioLanzaRule.class);
	
	private OpenOfficeHelper ooHelper = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
        try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

			IItem itemDocumento = DocumentosUtil.generarDocumento(rulectx, NOMBRE_DOC_ANUNCIO, "borrar");
			String strInfoPag = itemDocumento.getString("INFOPAG");
			File file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null);

    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
    		
    		File file = null;
    		
			IItemCollection docs = entitiesAPI.getDocuments(rulectx.getNumExp(), " NOMBRE='Anexo a Solicitud' AND (EXTENSION='DOC' OR EXTENSION='ODT')", "");
	        Iterator it = docs.iterator();
        	
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	//Concatenamos el anuncio
	        	if (doc != null)
	        	{
	        		file = DocumentosUtil.getFile(cct, doc.getString("INFOPAG"), null, null);
	        		DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), doc.getString("EXTENSION").toLowerCase());
	        		file.delete();
	        	}
	        }

    		//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName("." + Constants._EXTENSION_ODT);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
    		file1.delete();
    		
    		//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, NOMBRE_DOC_ANUNCIO, DocumentosUtil.BUSQUEDA_EXACTA, false);

    		DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, NOMBRE_DOC_ANUNCIO, file, Constants._EXTENSION_ODT);
    		file.delete();
    		
    		//Borra los documentos intermedios del gestor documental
	        IItemCollection collection = DocumentosUtil.getDocumentsByDescripcion(rulectx.getNumExp(), rulectx, "borrar");
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }

        	return new Boolean(true);
    		
        } catch(Exception e) {
        	logger.error("No se ha podido crear el anuncio del LANZA. " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido crear el anuncio del LANZA. " + e.getMessage(), e);
        } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

}

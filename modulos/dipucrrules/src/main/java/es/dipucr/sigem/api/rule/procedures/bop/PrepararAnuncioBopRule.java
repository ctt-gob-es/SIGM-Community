package es.dipucr.sigem.api.rule.procedures.bop;

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

public class PrepararAnuncioBopRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(PrepararAnuncioBopRule.class);
	
	protected String STR_Entity = "BOP_PUBLICACION";
	protected String STR_Anuncio = Constants.BOP._DOC_ANUNCIO;
	protected final String _EXTENSION = Constants._EXTENSION_ODT;
	protected final String _MIMETYPE = Constants._MIMETYPE_ODT;
	
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

			IItem itemDocumento = DocumentosUtil.generarDocumento(rulectx, STR_Anuncio, "borrar");
			String strInfoPag = itemDocumento.getString("INFOPAG");
			File file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null);

    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
    		
    		File file = null;
    		
    		//[eCenpri-Felipe Ticket #63] 29.07.2010 Se añade la extensión odt
    		//Buscamos el anuncio entre los documentos adjuntos
			IItemCollection docs = entitiesAPI.getDocuments(rulectx.getNumExp(), " TP_REG = 'ENTRADA' "
					+ " AND (UPPER(EXTENSION) = UPPER('" + Constants._EXTENSION_DOC + "') OR UPPER(EXTENSION) = UPPER('" + Constants._EXTENSION_ODT + "'))", "");
	        Iterator it = docs.iterator();
        	
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	//Concatenamos el anuncio
	        	if (doc != null)
	        	{
	        		file = DocumentosUtil.getFile(cct, doc.getString("INFOPAG"), null, null);
	    			//[eCenpri-Felipe Ticket #63] 29.07.2010 Se añade la extensión odt
	    			//[eCenpri-Felipe Ticket R#25] 29.07.2010 En los odt se estaba comiendo el primer carácter
	    			//al concatenar. Se soluciona el problema en la clase DipucrCommonFunctions
	        		DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), doc.getString("EXTENSION").toLowerCase());
	        		file.delete();
	        	}
	        }

    		//Guarda el resultado en repositorio temporal
	        //[eCenpri-Felipe Ticket #28] 07.09.2010 Cambiamos la extensión de todas la plantillas
			//String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
			String fileName = FileTemporaryManager.getInstance().newFileName("." + _EXTENSION);//[eCenpri-Felipe Ticket #28]
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");//[eCenpri-Felipe Ticket #28]
    		file1.delete();
    		
    		//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, STR_Anuncio, DocumentosUtil.BUSQUEDA_EXACTA, false);

    		DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, STR_Anuncio, file, _EXTENSION);
    		file.delete();
    		
    		//Borra los documentos intermedios del gestor documental
	        IItemCollection collection = DocumentosUtil.getDocumentsByDescripcion(rulectx.getNumExp(), cct, "borrar");
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }

        	return new Boolean(true);
    		
        } catch(Exception e) {
        	logger.error("No se ha podido crear el anuncio del BOP. " +e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido crear el anuncio del BOP. " +e.getMessage(), e);
        } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

}

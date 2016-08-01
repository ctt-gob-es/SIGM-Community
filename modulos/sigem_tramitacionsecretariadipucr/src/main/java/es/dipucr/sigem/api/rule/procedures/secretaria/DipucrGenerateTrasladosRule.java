package es.dipucr.sigem.api.rule.procedures.secretaria;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class DipucrGenerateTrasladosRule implements IRule {

	private OpenOfficeHelper ooHelper = null;
	private static final Logger logger = Logger.getLogger(DipucrGenerateTrasladosRule.class);
	
	String strNombreDoc = "";
	String strNombreDocCab = "";
	String strNombreDocPie = "";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	String numexp = "";
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
            //----------------------------------------------------------------------------------------------
	        numexp = rulectx.getNumExp();
            
            boolean urgencia = false;
            Vector <IItem> vPropuesta = null;
            int i = 1;
    		
    		//GENERACION DE LAS PROPUESTAS
    		//logger.warn("GENERACION DE LAS PROPUESTAS");    		
    		IItemCollection collection = DocumentosUtil.getDocumentsByNombre(numexp, rulectx, "Propuesta", "DESCRIPCION");

    		if(!collection.iterator().hasNext()){
    			//logger.warn("CARGA DE URGENCIAS");
        		//Ya no hay mas propuestas ahora se mira las urgencias
   	    		collection = DocumentosUtil.getDocumentsByNombre(numexp, rulectx, "Propuesta Urgencia");

  	  		  	//logger.warn("collection.toList().size() "+collection.toList().size());
  	  		  	if(collection.toList().size()!=0){
	  	  		  	urgencia = true;
	  	  		  	vPropuesta = SecretariaUtil.orderUrgencias(collection);
	  	  		  	i = 0;
  	  		  	}
    		}
    		else{
    			vPropuesta = SecretariaUtil.orderPropuestas(collection);
    		}
    		    		
    		SimpleDateFormat dateformat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
    		String fecha = dateformat.format(new Date());
    		
    		
    		if(vPropuesta != null){
	 	        while( i < vPropuesta.size()) {
	 	        	if(vPropuesta.get(i)!=null){
	 	        		ooHelper = OpenOfficeHelper.getInstance();

	 	        		cct.setSsVariable("FECHA", fecha);
	 	        		
	 	        		IItem propuesta = ((IItem)vPropuesta.get(i));
	 	    		
		            	IItem doc = DocumentosUtil.generarDocumento(rulectx, strNombreDocCab, null);
		            	String strInfoPag = doc.getString("INFOPAG");
		            	File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
		            	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
		        		if(file != null && file.exists()) file.delete();
		        		
		        		//Cuerpo
		 	        	strInfoPag = propuesta.getString("INFOPAG");
		 	        	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
		 	        	DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
		 	        	if(file != null && file.exists()) file.delete();
		        		
		        		//Pie
		    	    	IItem docPie = DocumentosUtil.generarDocumento(rulectx, strNombreDocPie, null);
		    	    	strInfoPag = docPie.getString("INFOPAG");
		    	    	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
		    	    	DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
		    	    	if(file != null && file.exists()) file.delete();
		    	
		    			//Guarda el resultado en repositorio temporal
		    			String fileName = FileTemporaryManager.getInstance().newFileName("."+extensionEntidad);
		    			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
		    			file = new File(fileName);
		    			
		    			String filter = DocumentosUtil.getFiltroOpenOffice(extensionEntidad);
		    			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(), filter);
		    			
		    			//Guarda el resultado en gestor documental
		    			int tpdoc = DocumentosUtil.getTipoDoc(cct, strNombreDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);

		    			IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, strNombreDoc, file, extensionEntidad);
		    			
		    			if(!urgencia){
		    				entityDoc.set("DESCRIPCION", i +".-"+strNombreDoc);
		    			}
		    			else{
		    				entityDoc.set("DESCRIPCION", i +".- Urgencia "+strNombreDoc);
		    			}
		    			
		    			entityDoc.store(cct);
		    			if(file != null && file.exists()) file.delete();
		    			
		    			//Borra los documentos intermedios del gestor documental		    	        
		    	        collection = entitiesAPI.getDocuments(numexp, "DESCRIPCION LIKE '" + strNombreDoc + " -%' OR DESCRIPCION LIKE 'Notificación de acuerdos -%' OR DESCRIPCION LIKE 'Dictamen de acuerdos -%'", "");
		    	        
		    	        Iterator it = collection.iterator();
		    	        while (it.hasNext())
		    	        {
		    	        	IItem doc3 = (IItem)it.next();
		    	        	entitiesAPI.deleteDocument(doc3);
		    	        }
		 	    		
		 	    		cct.deleteSsVariable("FECHA");
		 	    	
		 	    		if(ooHelper != null) ooHelper.dispose();
		 	    		if (xComponent != null) xComponent.dispose();
			        }
	 	        	i=i+1;
	 	        	
	 	        	if(i == vPropuesta.size() && !urgencia){
		        		//logger.warn("CARGA DE URGENCIAS");
		        		//Ya no hay mas propuestas ahora se mira las urgencias		  	  		  	
		   	    		collection = DocumentosUtil.getDocumentsByNombre(numexp, rulectx, "Propuesta Urgencia");

		  	  		  	if(collection.toList().size()!=0){
			  	  		  	urgencia = true;
			  	  		  	vPropuesta = SecretariaUtil.orderUrgencias(collection);
			  	  		  	i = 0;
		  	  		  	}
		        	}
	 	        }
	 	    }	        	
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	logger.error("No se han podido generar las certificaciones del expediente: " + numexp + ". " + e.getMessage(), e);        	
        	throw new ISPACRuleException("No se han podido generar las certificaciones del expediente: " + numexp + ". " + e.getMessage(), e);
        }
    	finally{
    		if(ooHelper != null) ooHelper.dispose();
    	}
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
}

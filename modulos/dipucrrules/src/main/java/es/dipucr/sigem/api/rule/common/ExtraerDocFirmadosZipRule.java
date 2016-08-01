package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ExtraerDocFirmadosZipRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(ExtraerDocFirmadosZipRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
			
			 //Actualiza el campo estado de la entidad
	        //de modo que permita mostrar los enlaces para crear Propuesta/Decreto
	        String numexp = rulectx.getNumExp();
	        IItemCollection col = entitiesAPI.getEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, numexp);
	        Iterator<?> itConv = col.iterator();
	        if (itConv.hasNext())
	        {
		        IItem entidad = (IItem)itConv.next();
		        entidad.set("ESTADO", "Inicio");
		        entidad.store(cct);
	        }
	        else{
	        	IItem item = entitiesAPI.createEntity(Constants.TABLASBBDD.SUBV_CONVOCATORIA,"");
				item.set("NUMEXP", rulectx.getNumExp()); 
				item.set("ESTADO", "Inicio");
		        item.store(rulectx.getClientContext());
	        }
			
			//Crea un ZIP con la documentación para la propuesta
	        //--------------------------------------------------
	
	        //Obtenemos los documentos a partir de su nombre
			List<IItem> filesConcatenate = new ArrayList<IItem>();
			int i=0;

			String STR_queryDocumentos = "ID_FASE = "+rulectx.getStageId()+" AND ESTADOFIRMA = '02'";
			IItemCollection documentsCollection = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), STR_queryDocumentos, "FDOC DESC");
			
			Iterator<?> it  = documentsCollection.iterator();
	        IItem itemDoc = null;
	        while (it.hasNext() && i<3){
	        	itemDoc = (IItem)it.next();
	        	filesConcatenate.add(itemDoc);
				i++;
			}
			
			//Obtenemos el id del tipo de documento de "Contenido de la propuesta" para
	        //ponerle el nombre del documento al zip
			int idTypeDocument = DocumentosUtil.getTipoDoc(cct, Constants.TIPODOC.DOCUMENTACION_PROPUESTA, DocumentosUtil.BUSQUEDA_EXACTA, false);
			if (idTypeDocument == Integer.MIN_VALUE){
				throw new ISPACInfo("Error al obtener el tipo de documento.");
			}
			
			// Crear el zip con los documentos
			//    http://www.manual-java.com/codigos-java/compresion-decompresion-archivos-zip-java.html
			File zipFile = DocumentosUtil.createDocumentsZipFile(genDocAPI, filesConcatenate);
			
			//Saco el nombre de la fase
			String sFase = FasesUtil.getFase(rulectx, entitiesAPI, rulectx.getStageProcedureId());
			
	        //Generamos el documento zip
			String sExtension = "zip";
			String sName = sFase+"& Numexp = "+numexp;//Campo descripción del documento
			String sMimeType = MimetypeMapping.getMimeType(sExtension);
			if (sMimeType == null) {
			    throw new ISPACInfo("Error al obtener el tipo Mime");
			}
					
			// Ejecución en un contexto transaccional
			boolean bCommit = false;
			
			try {
		        // Abrir transacción para que no se pueda generar un documento sin fichero
		        cct.beginTX();

		        DocumentosUtil.generaYAnexaDocumento(rulectx, idTypeDocument, sName, zipFile, sExtension);
				
				// Si todo ha sido correcto se hace commit de la transacción
				bCommit = true;
			}
			catch (Exception e) {
				logger.error("Error al generar el documento. " + e.getMessage(), e);
				throw new ISPACInfo("Error al generar el documento. " + e.getMessage(), e);
			}
			finally {
				cct.endTX(bCommit);
			}
		        
	        // Eliminar el zip
	        FileUtils.deleteFile(zipFile);
			
    	}
    	catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido generar la propuesta de concesión.",e);
        }
    	return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }
	
	

}

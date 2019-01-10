package es.dipucr.sigem.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EdicionDocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GeneratePropuestaConvocatoriaIntervencionRule implements IRule {

	private static final Logger logger = Logger.getLogger(GeneratePropuestaConvocatoriaIntervencionRule.class);
	
	private OpenOfficeHelper ooHelper = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	String numexp = "";
    	try{
			//----------------------------------------------------------------------------------------------
	        IClientContext cct = rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        numexp = rulectx.getNumExp();
	        //Obtención de la información de la convocatoria
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SUBV_CONVOCATORIA", strQuery);
	        
        	//MQE Si está rellena la convocatoria, generamos el documento, si no, no hacemos nada

	        if(coll != null && coll.iterator().hasNext()){
		        Iterator<?> it = coll.iterator();
	        	IItem conv = null;        	
	
		        if(it.hasNext()) {
		        	conv = ((IItem)it.next());
		        }
				String strTitulo = conv.getString("TITULO");
				String strContenido = conv.getString("CONTENIDO");
				strTitulo = strTitulo.toUpperCase();
				strContenido = strContenido.replaceAll("\r\n", "\r"); //Evita saltos de línea duplicados
				cct.setSsVariable("TITULO", strTitulo);
				cct.setSsVariable("CONTENIDO", strContenido);
		        
		        //Generación del comienzo del documento
				String strNombreTpDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-PropApr");
				String strNombrePlant = getPlantilla(rulectx, strNombreTpDoc);
		        DocumentosUtil.generarDocumento(rulectx, strNombreTpDoc, strNombrePlant, "intermedio");
	        	String strInfoPag = DocumentosUtil.getInfoPagByNombre(rulectx.getNumExp(), rulectx, strNombreTpDoc);
	        	File file1 = DocumentosUtil.getFile(rulectx.getClientContext(), strInfoPag, null, null);
	    		ooHelper = OpenOfficeHelper.getInstance();
	    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
				
	    		//Generación de las bases
	    		File file = null;
	    		String descr = "";
				strNombreTpDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunBases");
				strNombrePlant = strNombreTpDoc;
				strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' ORDER BY NUMERO ASC";
		        IItemCollection bases = entitiesAPI.queryEntities("SUBV_BASES", strQuery);
		        it = bases.iterator();
		        while (it.hasNext())
		        {
		        	IItem norma = (IItem)it.next();
		        	int nNumero = norma.getInt("NUMERO");
		        	String strNumeroNorma = String.valueOf(nNumero);
		        	String strExtractoNorma = norma.getString("EXTRACTO");
		        	String strContenidoNorma = norma.getString("CONTENIDO");
		        	strContenidoNorma = strContenidoNorma.replaceAll("\r\n", "\r");
		        	cct.setSsVariable("NUMERO_NORMA", strNumeroNorma);
		        	cct.setSsVariable("EXTRACTO_NORMA", strExtractoNorma);
		        	cct.setSsVariable("CONTENIDO_NORMA", strContenidoNorma);
	
		        	DocumentosUtil.generarDocumento(rulectx, strNombreTpDoc, strNombrePlant, strNumeroNorma);
		        	
		        	cct.deleteSsVariable("NUMERO_NORMA");
		        	cct.deleteSsVariable("EXTRACTO_NORMA");
		        	cct.deleteSsVariable("CONTENIDO_NORMA");
	
		        	descr = strNombreTpDoc + " - " + strNumeroNorma;
	            	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, descr);
	        		file = DocumentosUtil.getFile(rulectx.getClientContext(), strInfoPag, null, null);
	        		EdicionDocumentosUtil.Concatena(xComponent, "file://" + file.getPath());
	        		file.delete();
		        }
	    		/**
	    		 * INICIO [Teresa] Ticket#28# Poner formato en fichero en formato odt
	    		 * **/
	    		//Guarda el resultado en repositorio temporal
				String fileName = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_ODT);
				/**
	    		 * INICIO [Teresa] Ticket#28# Poner formato en fichero en formato odt
	    		 * **/
				fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
				file = new File(fileName);
	    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
	    		/**
	    		 * FIN [Teresa] Ticket#28# Poner formato en fichero en formato odt
	    		 * **/
	    		file1.delete();
	    		
	    		//Guarda el resultado en gestor documental
				strNombreTpDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-PropApr");
				int tpdoc = DocumentosUtil.getTipoDoc(cct, strNombreTpDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);

	    		DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, strNombreTpDoc, file, Constants._EXTENSION_ODT);
	    		if(file != null && file.exists()) file.delete();
	    		
	    		//Borra los documentos intermedios del gestor documental
		        IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION LIKE '" + DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunBases") + "%' OR DESCRIPCION LIKE '%intermedio'", "");
		        it = collection.iterator();
		        while (it.hasNext())
		        {
		        	IItem doc = (IItem)it.next();
		        	entitiesAPI.deleteDocument(doc);
		        }
	    		
		        cct.deleteSsVariable("TITULO");
		        cct.deleteSsVariable("CONTENIDO");	
	        }//MQE fin de generar el documento
			
        	return new Boolean(true);
    		
        } catch(ISPACRuleException e) {
        	logger.error("No se ha podido crear la propuesta de convocatoria del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido crear la propuesta de convocatoria del expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (ISPACException e) {
        	logger.error("No se ha podido crear la propuesta de convocatoria del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido crear la propuesta de convocatoria del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (com.sun.star.uno.Exception e) {
			logger.error("No se ha podido crear la propuesta de convocatoria del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido crear la propuesta de convocatoria del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("No se ha podido crear la propuesta de convocatoria del expediente: " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido crear la propuesta de convocatoria del expediente: " + numexp + ". " + e.getMessage(), e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

	private String getPlantilla(IRuleContext rulectx, String nombreTpDoc) throws ISPACRuleException
	{
		String strTemplateName = "";
		
		try
		{
			//APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();

			//Obtención de los tipos de documento asociados al trámite
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
	    	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	    	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty())
	    	{
	    		throw new ISPACInfo("No hay ningún tipo de documento asociado al trámite");
	    	}

	    	//Busco el tipo de documento
        	Iterator<?> itTpDocs = taskTpDocCollection.iterator();
        	boolean found = false;
        	while(itTpDocs.hasNext() && !found)
        	{
		    	IItem taskTpDoc = (IItem)itTpDocs.next();
	    		int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	        	int tpDocId = DocumentosUtil.getIdTipoDocByNombre(cct, nombreTpDoc);
	    		if (tpDocId != documentTypeId)
	    		{
	    			//Este no es el Tipo de documento solicitado
	    			continue;
	    		}
	    		found = true;

	    		//Ahora busco la plantilla indicada
	        	IItemCollection plantillas = procedureAPI.getProcTemplates(tpDocId, rulectx.getProcedureId());
	        	if(plantillas==null || plantillas.toList().isEmpty())
	        	{
	        		//No hay ninguna plantilla asociada al tipo de documento
	        		continue;
	        	}
	        	Iterator<?> itTemplate = plantillas.iterator();
	        	if(itTemplate.hasNext())
	        	{
		    		IItem tpDocsTemplate = (IItem)itTemplate.next();
	        		strTemplateName = tpDocsTemplate.getString("NOMBRE");
	        	}
	    	}
		}
		catch(Exception e)
		{
			throw new ISPACRuleException(e);
		}
		
		return strTemplateName;
	}
}
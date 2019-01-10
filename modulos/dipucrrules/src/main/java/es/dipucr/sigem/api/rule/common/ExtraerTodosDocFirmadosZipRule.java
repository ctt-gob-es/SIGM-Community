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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ExtraerTodosDocFirmadosZipRule implements IRule {
	
	private static final Logger LOGGER = Logger.getLogger(ExtraerDocFirmadosZipRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
		// Nada que hacer al cancelar
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
	        			
			//Crea un ZIP con la documentación para la propuesta
	        //--------------------------------------------------
	
	        //Obtenemos los documentos a partir de su nombre
			List filesConcatenate = new ArrayList();
			//[dipucr-Felipe] Incluyo documentos firmados externamente que se anexan mediante "anexar fichero" 
			String queryDocumentos = "(ESTADOFIRMA = '02' OR FAPROBACION IS NOT NULL)";
			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), queryDocumentos, "FDOC DESC");
			
			Iterator it  = documentsCollection.iterator();
	        IItem itemDoc = null;
	        while (it.hasNext()){
	        	itemDoc = (IItem)it.next();
	        	filesConcatenate.add(itemDoc);
			}
			
			//Obtenemos el id del tipo de documento de "Contenido de la propuesta" para
	        //ponerle el nombre del documento al zip
			int idTypeDocument = 0;

			idTypeDocument = DocumentosUtil.getTipoDoc(cct, Constants.TIPODOC.DOCUMENTACION_PROPUESTA, DocumentosUtil.BUSQUEDA_EXACTA, false);

			if (idTypeDocument == 0){
				throw new ISPACInfo("Error al obtener el tipo de documento.");
			}
			
			
			// Crear el zip con los documentos
			//    http://www.manual-java.com/codigos-java/compresion-decompresion-archivos-zip-java.html
//			File zipFile = DocumentosUtil.createDocumentsZipFileInfoPagRDE(genDocAPI, filesConcatenate);//[dipucr-Felipe #868]
			File zipFile = DocumentosUtil.createDocumentsZipFileAll(genDocAPI, filesConcatenate);
			
	        //Generamos el documento zip
			String sExtension = "zip";
			String sName = "Documentos & Numexp = "+numexp;//Campo descripción del documento
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
			} catch (Exception e) {
				// Si se produce algún error
				throw new ISPACInfo("Error al generar el documento.", e);
			} finally {
				cct.endTX(bCommit);
			}
		        
	        // Eliminar el zip
	        FileUtils.deleteFile(zipFile);
	        
	        //Actualiza el campo estado de la entidad
	        //de modo que permita mostrar los enlaces para crear Propuesta/Decreto	        
	        IItemCollection col = entitiesAPI.getEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, numexp);
	        Iterator itConv = col.iterator();
	        
	        //Recuperamos los datos de la firma para pasarlos luego a la propuesta
	        String cargo = "";
	        
	        IItemCollection colProp = entitiesAPI.getDocuments(numexp, "UPPER(DESCRIPCION) LIKE '%"+Constants.CONSTANTES.PROPUESTA+"%'", "");
	        Iterator itProp = colProp.iterator();
	        while(itProp.hasNext()){
		        IItem iPropuesta = (IItem)itProp.next();
		        String idDocumento = iPropuesta.getString("ID");
		        
		        String strQuery = "WHERE ID_DOCUMENTO = "+idDocumento;
		        IItemCollection circuitoFirma = entitiesAPI.queryEntities("SPAC_CTOS_FIRMA", strQuery);
		        Iterator itCircuitos = circuitoFirma.iterator();
		        
		        if(itCircuitos.hasNext()){
		        	IItem circuito = (IItem) itCircuitos.next();
		        	int idCircuito = circuito.getInt("ID_CIRCUITO");
			        
			        strQuery="SELECT DESCRIPCION FROM SPAC_CTOS_FIRMA_CABECERA WHERE ID_CIRCUITO = "+idCircuito+"";
			        ResultSet circuitoFirmaDetalle = cct.getConnection().executeQuery(strQuery).getResultSet();
			        if(circuitoFirmaDetalle==null) {
		          		throw new ISPACInfo("No hay ningun procediento asociado");
		          	}
		          	if(circuitoFirmaDetalle.next()) if (circuitoFirmaDetalle.getString("DESCRIPCION")!=null) {
		          		cargo = circuitoFirmaDetalle.getString("DESCRIPCION");
		          	} else {
		          		cargo="";
		          	}
		          	if(cargo.length()>20) {
		          		cargo = cargo.substring(0,19);
		          	}
		          	circuitoFirmaDetalle.close();
		        }
	        }
	        
	         IItem expediente = (IItem)ExpedientesUtil.getExpediente(cct, numexp);
	         if(expediente != null){
		         String titulo = expediente.getString("ASUNTO");
		        
		        if (itConv.hasNext()){	        	
			        IItem entidad = (IItem)itConv.next();
			        entidad.set("ESTADO", "Inicio");		        
			        entidad.set("NUM_VICEP", cargo);
			        entidad.set("TITULO", titulo);
			        entidad.store(cct);
		        } else {
		        	IItem item = entitiesAPI.createEntity(Constants.TABLASBBDD.SUBV_CONVOCATORIA,numexp);
					item.set("NUMEXP", rulectx.getNumExp()); 
					item.set("ESTADO", "Inicio");
					item.set("NUM_VICEP", cargo);
					item.set("TITULO", titulo);
			        item.store(rulectx.getClientContext());
		        }
	         }			
    	} catch(Exception e) {
    		LOGGER.error(e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido generar la propuesta.",e);
        }
    	return true;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }
}

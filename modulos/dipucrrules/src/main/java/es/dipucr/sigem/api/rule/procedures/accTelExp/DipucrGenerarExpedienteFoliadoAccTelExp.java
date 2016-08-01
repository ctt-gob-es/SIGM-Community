package es.dipucr.sigem.api.rule.procedures.accTelExp;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrGenerarExpedienteFoliadoConIndiceRule;
import es.dipucr.sigem.api.rule.common.expFol.DocIncluir;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrGenerarExpedienteFoliadoAccTelExp extends DipucrGenerarExpedienteFoliadoConIndiceRule {

	private static final Logger logger = Logger.getLogger(DipucrGenerarExpedienteFoliadoAccTelExp.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        tipoDoc = "Anexo";
        		
        añadePortada = true;
        añadeContraportada = true;
        añadeIndice = true;
		return true;
	}
	
	public String getNumExpFoliar(IRuleContext rulectx, IEntitiesAPI entitiesAPI){
		String resultado = "";
		try {
			resultado = rulectx.getNumExp();
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar el expediente a foliar.", e);
		} 
		
		return resultado;
	} 
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try {
			logger.info("INICIO - " + this.getClass().getName());

			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			// Variables
			String numExp = rulectx.getNumExp();
			
			//Generamos el foliado
			numExpPadre = getNumExpFoliar(rulectx, entitiesAPI);
			
			ArrayList<DocIncluir> docsDelExp = new ArrayList<DocIncluir>();

			FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
			File documentoResumen = ftMgr.newFile(".pdf");
			
			errores = ftMgr.newFile(".txt");
			erroresFW = new FileWriter(errores);
			
			erroresFW.write("\t\tDocumentos el expediente '" + numExpPadre + "' que no se han incluido en el expediente foliado:\n\n");

			String consultaDocumentos = getConsultaDocumentos(entitiesAPI, numExp);							
			
			IItemCollection documentsCollection = DocumentosUtil.queryDocumentos(cct, consultaDocumentos);
			docsDelExp = getDocsDelExp(documentsCollection);				
			
			String varColorAsunto =  DipucrCommonFunctions.getVarGlobal("COLOR_ASUNTO_EXPEDIENTE_FOLIADO");
			if(StringUtils.isEmpty(varColorAsunto)){
				logger.error("Error al obtener el color del texto del asunto de la portada del expediente " + numExpPadre + ". Revise el valor de la varibale de sistema: COLOR_ASUNTO_EXPEDIENTE_FOLIADO");
				throw new ISPACRuleException("Error al obtener el color del texto del asunto de la portada del expediente " + numExpPadre + ". Avise al administrador del sistema");
			}
			else{
				String[] varColorAsuntoSplit = varColorAsunto.split(",");
				try{
					colorAsunto = new Color( Integer.parseInt(varColorAsuntoSplit[0]), Integer.parseInt(varColorAsuntoSplit[1]), Integer.parseInt(varColorAsuntoSplit[2]), Integer.parseInt(varColorAsuntoSplit[3]));
					bColorAsunto = new Color( Integer.parseInt(varColorAsuntoSplit[0]), Integer.parseInt(varColorAsuntoSplit[1]), Integer.parseInt(varColorAsuntoSplit[2]), Integer.parseInt(varColorAsuntoSplit[3]));
				}	
				catch(Exception e){
					logger.error("Error al obtener el color del texto del asunto de la portada del expediente " + numExpPadre + ". Revise el valor de la varibale de sistema: COLOR_ASUNTO_EXPEDIENTE_FOLIADO");
					throw new ISPACRuleException("Error al obtener el color del texto del asunto de la portada del expediente " + numExpPadre + ". Avise al administrador del sistema");					
				}
			}
			
			String varColorNumPag =  DipucrCommonFunctions.getVarGlobal("COLOR_NUM_PAG_INDICE_FOLIADO");
			if(StringUtils.isEmpty(varColorNumPag)){
				logger.error("Error al obtener el color del texto del asunto de la portada del expediente " + numExpPadre + ". Revise el valor de la varibale de sistema: COLOR_ASUNTO_EXPEDIENTE_FOLIADO");
				throw new ISPACRuleException("Error al obtener el color del texto del asunto de la portada del expediente " + numExpPadre + ". Avise al administrador del sistema");
			}
			else{
				String[] varColorNumPagSplit = varColorNumPag.split(",");
				try{
					colorNumPag = new Color( Integer.parseInt(varColorNumPagSplit[0]), Integer.parseInt(varColorNumPagSplit[1]), Integer.parseInt(varColorNumPagSplit[2]), Integer.parseInt(varColorNumPagSplit[3]));
					bColorNumPag = new Color( Integer.parseInt(varColorNumPagSplit[0]), Integer.parseInt(varColorNumPagSplit[1]), Integer.parseInt(varColorNumPagSplit[2]), Integer.parseInt(varColorNumPagSplit[3]));
				}	
				catch(Exception e){
					logger.error("Error al obtener el color del texto del asunto de la portada del expediente " + numExpPadre + ". Revise el valor de la varibale de sistema: COLOR_ASUNTO_EXPEDIENTE_FOLIADO");
					throw new ISPACRuleException("Error al obtener el color del texto del asunto de la portada del expediente " + numExpPadre + ". Avise al administrador del sistema");					
				}
			}
			
			documentoResumen = generarPdf(docsDelExp, documentoResumen.getPath(), entitiesAPI, cct, rulectx);

			erroresFW.flush();
			erroresFW.close();

			int idTipoDoc = DocumentosUtil.getTipoDoc(cct, tipoDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);
			String descripcionDocumento = ExpedientesUtil.getExpediente(cct, numExpPadre).getString("ASUNTO");

			DocumentosUtil.generaYAnexaDocumento(rulectx, idTipoDoc, descripcionDocumento, documentoResumen, "pdf");
			
			documentoResumen.delete();
			documentoResumen = null;
			cct.endTX(true);
			
			logger.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) {
			logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR generando expediente foliado: " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR generando expediente foliado: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR generando expediente foliado: " + e.getMessage(), e);
		}
		catch (Exception e) {
			logger.error("ERROR generando expediente foliado: " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR generando expediente foliado: " + e.getMessage(), e);
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public String getConsultaDocumentos(IEntitiesAPI entitiesAPI, String numexp)
			throws ISPACException {

		String nif = "";

		//Finalmente tomamos todos los documentos del expediente en el que nos encontramos
		String consultaDocumentos = "";
		String idDocsExpOriginal = "";
		IItemCollection docsExpCollection = entitiesAPI.getDocuments(numexp, "ID_TRAMITE IN (SELECT ID_TRAM_EXP FROM SPAC_DT_TRAMITES WHERE NUMEXP = '"+ numexp + "' AND (UPPER(NOMBRE) = 'RECUPERAR DOCUMENTOS' OR UPPER(NOMBRE) = 'ANEXAR DOCUMENTACIÓN'))", "");
		Iterator docsExpIterator = docsExpCollection.iterator();
		while(docsExpIterator.hasNext()){
			IItem docExp = (IItem) docsExpIterator.next();
			if(!docExp.getString("NOMBRE").toUpperCase().equals("DOCUMENTACIÓN DE PROPUESTA") && !docExp.getString("NOMBRE").toUpperCase().equals("Propuesta Anuncio".toUpperCase())){
				if(nif == null || nif.equals("")){
					if(!docExp.getString("NOMBRE").toUpperCase().contains("NOTIFICACI") && !docExp.getString("NOMBRE").toUpperCase().contains("ACUSE")) 
						if(idDocsExpOriginal.equals("")) idDocsExpOriginal = docExp.getString("ID");
						else idDocsExpOriginal += ", " + docExp.getString("ID");
				}
				else{
					if(idDocsExpOriginal.equals("")) idDocsExpOriginal = docExp.getString("ID");
					else idDocsExpOriginal += ", " + docExp.getString("ID");
				}
			}			
		}
		if (!idDocsExpOriginal.equals("")) {
			consultaDocumentos += " (ID IN (" + idDocsExpOriginal + "))";
		}
		
		if(!consultaDocumentos.equals("")){
			consultaDocumentos += " ORDER BY CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION";		

			consultaDocumentos = " WHERE " + consultaDocumentos;
		} else
			consultaDocumentos = " WHERE 1=2 ";

		return consultaDocumentos;
	}	

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
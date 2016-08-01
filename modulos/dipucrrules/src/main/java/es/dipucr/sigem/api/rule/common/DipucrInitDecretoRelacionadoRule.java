package es.dipucr.sigem.api.rule.common;

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
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrInitDecretoRelacionadoRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrInitDecretoRelacionadoRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        String strQuery = null;
	        IItemCollection col = null;
	        
	        //Obtiene el expediente de la entidad
	        String numexpDecreto = rulectx.getNumExp();	
	        strQuery = "WHERE NUMEXP_HIJO='" + numexpDecreto + "'";
	        col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
        	IItem itemExpRelacionados = (IItem)col.iterator().next();
        	String numexpInforme = itemExpRelacionados.getString("NUMEXP_PADRE");
			
			//Creo el tramite 'Creación del Decreto'
        	//[dipucr-Felipe #1091] Antes se hacía más artesanalmente, buscando el primer trámite de la fase
        	// Ahora tenemos que crear el segundo trámite, por lo que lo creamos introduciendo el código directamente
//			int idTramiteCreacionDecreto = transaction.createTask(idFase, idTramite);
        	int idTramiteCreacionDecreto = TramitesUtil.crearTramite(cct, "CREAR_DECRETOS", numexpDecreto);
	        
			//Se crea el documento de decreto con el Informe adjunto
			DatosPlantilla cabecera = new DatosPlantilla
				(Constants.PLANTILLADOC.DECRETO_CABECERA, Constants.TIPODOC.DECRETOS_CONVOCATORIA);
			DatosPlantilla pie = new DatosPlantilla
				(Constants.PLANTILLADOC.DECRETO_PIE, Constants.TIPODOC.DECRETOS_CONVOCATORIA);
			String nombreDocInforme = cct.getSsVariable("nombreDocInforme");
			String nombreDocFinal = Constants.DECRETOS._DOC_DECRETO;
			generarDocumentoConInforme(rulectx, cabecera, nombreDocInforme, pie, 
					numexpInforme, idTramiteCreacionDecreto, nombreDocFinal);
			
			//Se crea el documento de notificaciones con el Informe adjunto
			cabecera = new DatosPlantilla (Constants.PLANTILLADOC.NOTIFICACIONES_CABECERA,
					Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION);
			pie = new DatosPlantilla(Constants.PLANTILLADOC.NOTIFICACIONES_PIE,
					Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION);
			nombreDocFinal = Constants.DECRETOS._DOC_NOTIFICACIONES;
			generarDocumentoConInforme(rulectx, cabecera, nombreDocInforme, pie, 
					numexpInforme, idTramiteCreacionDecreto, nombreDocFinal);
			
			//[Teresa] INICIO Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
			//Importar participantes.
			ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexpInforme, numexpDecreto);
			//[Teresa] FIN Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
		
	        return new Boolean(true);
        }
    	catch(Exception e) 
        {
    		logger.error("No se ha podido inicializar el decreto.",e);

        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar el decreto.",e);
        }
	}
	
	/**
	 * A partir de un informe, genera el decreto o la plantilla de notificaciones
	 * adjuntando el informe entre la cabecera y el pie definidos
	 * @param rulectx
	 * @param cabecera
	 * @param nombreDocInforme
	 * @param pie
	 * @param numexpInforme
	 * @param idTramiteActual
	 * @param nombreDocFinal
	 * @throws ISPACRuleException
	 */
	@SuppressWarnings("rawtypes")
	private void generarDocumentoConInforme(IRuleContext rulectx, DatosPlantilla cabecera, 
			String nombreDocInforme, DatosPlantilla pie, String numexpInforme, int idTramiteActual, String nombreDocFinal) throws ISPACRuleException {

		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        OpenOfficeHelper ooHelper = OpenOfficeHelper.getInstance();
	        //----------------------------------------------------------------------------------------------
			
	        IItemCollection col = null;
	        
			//Cabecera
	    	DocumentosUtil.generarDocumento(rulectx, cabecera.getNombrePlantilla(), cabecera.getTipoDoc(), null, numexpInforme, idTramiteActual);
	    	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, cabecera.getNombrePlantilla());
	    	File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
	    	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
			file.delete();
			
			//Cuerpo
    		//Cojo el contenido de la propuesta
    		col = entitiesAPI.getDocuments(numexpInforme, "NOMBRE LIKE '%" + nombreDocInforme + "%'", "");
    		IItem iPropuesta = (IItem)col.iterator().next();
	        String infopag = iPropuesta.getString("INFOPAG");
	        
	        //Concatenamos el cuerpo
        	file = DocumentosUtil.getFile(cct, infopag, null, null);
        	DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		
    		//Pie
	    	DocumentosUtil.generarDocumento(rulectx, pie.getNombrePlantilla(), 
	    			pie.getTipoDoc(), null, numexpInforme, idTramiteActual);
	    	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, pie.getNombrePlantilla());
	    	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
	    	DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
			file.delete();
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_ODT);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			
			//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, nombreDocFinal, DocumentosUtil.BUSQUEDA_EXACTA, false);

			DocumentosUtil.generaYAnexaDocumento(rulectx, idTramiteActual, tpdoc, nombreDocFinal, file, Constants._EXTENSION_ODT);
			
			if(file != null && file.exists()) file.delete();
			
			//Borra los documentos intermedios del gestor documental
	        col = entitiesAPI.getDocuments(rulectx.getNumExp(), "(NOMBRE LIKE '%" +	cabecera.getTipoDoc() + "%' OR NOMBRE='" + cabecera.getNombrePlantilla() + "')", "");
	        Iterator it = col.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
	        ooHelper.dispose();
		}
		catch (Exception e) {
			logger.error("Error al generar el documento " + nombreDocFinal, e);
			throw new ISPACRuleException("Error al generar el documento " + nombreDocFinal, e);
		}
		
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		 return true;
	}

}

package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

/**
 * [dipucr-Felipe #546]
 * Genera las notificaciones individualizadas de la ayuda de estudios
 */
public class GenerateNotificacionAyudasAbstractRule extends DipucrAutoGeneraDocIniTramiteRule 
{
	 
	private static final Logger LOGGER = Logger.getLogger(GenerateNotificacionAyudasAbstractRule.class);	

	String numexp;
	String nifParticipante;
	String tipoRegimen;
	String fechaDecreto;
	String numDecreto;
	String extracto;
	String anio;
	
	 @Override
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
    	
    	try {
    		
    		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
    		
    		Vector<String> vectorDecreto = ExpedientesRelacionadosUtil.getExpedientesRelacionados(entitiesAPI, numexp);
    		
    		if (null == vectorDecreto || vectorDecreto.size() == 0){
    			rulectx.setInfoMessage("No existe ningún decreto entre los expedientes relacionados");
    			return false;
    		}
    		else{
    			String numexpDecreto = vectorDecreto.firstElement();
    			String sqlQueryPart = "WHERE NUMEXP = '" + numexpDecreto + "'";
    			
    	    	IItemCollection colDatosDecretos = entitiesAPI.queryEntities("SGD_DECRETO", sqlQueryPart);
    	    	
    	    	if (null == colDatosDecretos || colDatosDecretos.toList().size() == 0){
    	    		rulectx.setInfoMessage("El decreto relacionado todavía no ha sido firmado");
        			return false;
    	    	}
    	    	else{
    	    		IItem itemDecreto = (IItem) colDatosDecretos.iterator().next();

    	    		numDecreto = itemDecreto.getString("NUMERO_DECRETO");
    	    		if (StringUtils.isEmpty(numDecreto)){
    	    			rulectx.setInfoMessage("El decreto relacionado todavía no ha sido firmado");
            			return false;
    	    		}
    	    		else{
	    	    		fechaDecreto = FechasUtil.getFormattedDate(itemDecreto.getDate("FECHA_DECRETO"), FechasUtil.DOC_DATE_PATTERN);
	    	    		extracto = itemDecreto.getString("EXTRACTO_DECRETO");
	    	    		anio = itemDecreto.getString("ANIO");
    	    		}
    	    	}
    		}
    			
    	} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			//******************************************
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			Object connectorSession = null;
			//******************************************
			
			// 1. Obtener plantilla
			documentTypeId = DocumentosUtil.getTipoDocumentoByPlantillaIItem((ClientContext) cct, plantilla).getKeyInt();
			
			// 2. Seteamos las variables globales
        	int taskId = rulectx.getTaskId();
			IItem itemDtTramite = TramitesUtil.getTramite(cct, numexp, taskId);
			setearSsVariables(cct, rulectx);
			cct.setSsVariable("NOMBRE_TRAMITE", itemDtTramite.getString("NOMBRE"));
			
			// 3. Localizamos plantilla
			templateId = DocumentosUtil.getTemplateId(cct, plantilla, documentTypeId);
				
			// 4. Recorremos los participantes
			IItemCollection colParticipantes = ParticipantesUtil.getParticipantes
					(cct, numexp, "ROL = '" + ParticipantesUtil._TIPO_INTERESADO + "'", "NOMBRE");
			
			if (colParticipantes!=null && colParticipantes.toList().size()>=1) {
				
				@SuppressWarnings("unchecked")
				List<IItem> listParticipantes = colParticipantes.toList();
				
				for (IItem itemParticipante : listParticipantes) {
					
					if (itemParticipante != null){
				        
						// Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
						String recurso = StringUtils.nullToEmpty(itemParticipante.getString("RECURSO"));
			        	String sqlQueryPart = "WHERE VALOR = '" + recurso +"'";
			        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
			        	if (colRecurso.iterator().hasNext()){
			        		IItem iRecurso = (IItem)colRecurso.iterator().next();
			        		recurso = iRecurso.getString("SUSTITUTO");
			        	}		        	
			        	
			        	nifParticipante = StringUtils.nullToEmpty(itemParticipante.getString("NDOC"));
			        	String nombreParticipante = StringUtils.nullToEmpty(itemParticipante.getString("NOMBRE"));
						LOGGER.warn("Ayudas. Generando notificación para el empleado: " + nombreParticipante);
						
			        	cct.setSsVariable("NDOC", nifParticipante);
			        	cct.setSsVariable("NOMBRE", nombreParticipante);
			        	cct.setSsVariable("DIRNOT", StringUtils.nullToEmpty(itemParticipante.getString("DIRNOT")));
			        	cct.setSsVariable("C_POSTAL", StringUtils.nullToEmpty(itemParticipante.getString("C_POSTAL")));
			        	cct.setSsVariable("LOCALIDAD", StringUtils.nullToEmpty(itemParticipante.getString("LOCALIDAD")));
			        	cct.setSsVariable("CAUT", StringUtils.nullToEmpty(itemParticipante.getString("CAUT")));
			        	cct.setSsVariable("RECURSO", recurso);
			        	//cct.setSsVariable("RECURSO_TEXTO", recursoTexto);
			        	cct.setSsVariable("OBSERVACIONES", StringUtils.nullToEmpty(itemParticipante.getString("OBSERVACIONES")));
					
						//6. Obtener la info de sus ayudas
			        	//Lo hago en la llamada de insertar tabla
			        	
			        	//7. Generar documento
						IItem itemTramite = gendocAPI.createTaskDocument(taskId, documentTypeId);
						int idDocumentoTram = itemTramite.getKeyInt();
	
						IItem itemPlantilla = gendocAPI.attachTaskTemplate(connectorSession, taskId, idDocumentoTram, templateId);
						String infoPagPlantilla = itemPlantilla.getString("INFOPAG");
						itemPlantilla.store(cct);
	
						IItem itemDocumento = gendocAPI.createTaskDocument(taskId, documentTypeId);
						int documentId = itemDocumento.getKeyInt();
	
						String sFileTemplate = DocumentosUtil.getFile(cct,infoPagPlantilla, null, null).getName();
	
						// Generar el documento a partir la plantilla
						IItem entityTemplate = gendocAPI.attachTaskTemplate
								(connectorSession, taskId, documentId, templateId, sFileTemplate);
	
						String docref = entityTemplate.getString("INFOPAG");
						String sMimetype = gendocAPI.getMimeType(connectorSession,docref);
						entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
						entityTemplate.set("DESCRIPCION", nombreParticipante);
						entityTemplate.set("DESTINO", nombreParticipante);
	
						entityTemplate.store(cct);	
						
						cct.deleteSsVariable("NOMBRE_TRAMITE");
						
						//8. Insertar las ayudas del empleado
						if(refTablas != null && !refTablas.equals("")){						
							editarContenidoDocumento(gendocAPI, docref, rulectx, documentId, refTablas, entitiesAPI, numexp);
						}
	
						itemPlantilla.delete(cct);
						itemTramite.delete(cct);
						DocumentosUtil.deleteFile(sFileTemplate);
					}				
				}				
			}

			deleteSsVariables(cct);
			cct.endTX(true);
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return true;
	}
	
	public void setearSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			 
			cct.setSsVariable("NUM_DECRETO", numDecreto);
			cct.setSsVariable("FECHA_DECRETO", fechaDecreto);
			cct.setSsVariable("EXTRACTO_DECRETO", extracto);			
			cct.setSsVariable("ANIODEC", anio);
			if (!StringUtils.isEmpty(tipoRegimen)){
	            cct.setSsVariable("TIPO_REGIMEN", AyudasssUtil.getDescRegimen(tipoRegimen));
	            cct.setSsVariable("CONVENIO", AyudasssUtil.getDescConvenio(tipoRegimen));
			}
			
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	
	public void borrarSsVariables(IClientContext cct) {
		try {
			cct.deleteSsVariable("NUM_DECRETO");
			cct.deleteSsVariable("FECHA_DECRETO");
			cct.deleteSsVariable("EXTRACTO_DECRETO");
			cct.deleteSsVariable("ANIODEC");
			if (!StringUtils.isEmpty(tipoRegimen)){
				cct.deleteSsVariable("TIPO_REGIMEN");
				cct.deleteSsVariable("CONVENIO");
			}
			
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}


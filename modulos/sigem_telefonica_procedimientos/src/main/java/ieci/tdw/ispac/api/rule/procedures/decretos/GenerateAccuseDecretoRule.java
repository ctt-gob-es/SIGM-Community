package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.TemplateDAO;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * 
 * @author diezp
 * @date 30/03/2009
 * @propósito Genera los acuses de recibo para el expediente de Decreto actual.
 */
public class GenerateAccuseDecretoRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(GenerateAccuseDecretoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
		
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			
			int taskId = rulectx.getTaskId();
			String numExp = rulectx.getNumExp();
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
	    	Object connectorSession = null;
	    	
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			String sqlQueryPart = "WHERE (ROL != 'TRAS' OR ROL IS NULL) AND NUMEXP = '"+numExp+"' ORDER BY ID";	
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			// 2. Comprobar que hay algún participante para el cual generar su acuse
			if (participantes!=null && participantes.toList().size()>=1) {	
	    		// 4. Para cada participante generar un acuse de recibo
				for (int i=0;i<participantes.toList().size();i++){
					try {
						connectorSession = gendocAPI.createConnectorSession();
						IItem participante = (IItem) participantes.toList().get(i);
						// Abrir transacción para que no se pueda generar un documento sin fichero
				        cct.beginTX();
					
						if (participante!=null){
				        
							// Añadir a la session los datos para poder utilizar <ispatag sessionvar='var'> en la plantilla
				        	if ((String)participante.get("NOMBRE")!=null) nombre = (String)participante.get("NOMBRE");
				        	if ((String)participante.get("DIRNOT")!=null) dirnot = (String)participante.get("DIRNOT");
				        	if ((String)participante.get("C_POSTAL")!=null) c_postal = (String)participante.get("C_POSTAL");
				        	if ((String)participante.get("LOCALIDAD")!=null) localidad = (String)participante.get("LOCALIDAD");
				        	if ((String)participante.get("CAUT")!=null) caut = (String)participante.get("CAUT");
				        	cct.setSsVariable("NOMBRE", nombre);
				        	cct.setSsVariable("DIRNOT", dirnot);
				        	cct.setSsVariable("C_POSTAL", c_postal);
				        	cct.setSsVariable("LOCALIDAD", localidad);
				        	cct.setSsVariable("CAUT", caut);
				        	
				        	//[Ticket 1300 Teresa] Cambio para que genere el documento que se especifique en datos específicos del catálogo de procedimientos.
				        	String plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
							String tipoDocumento = "";
							if(!StringUtils.isNotEmpty(plantilla)){
								plantilla = "Acuse de Recibo";
							}
							tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
							int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
				        	IItem template = TemplateDAO.getTemplate(cct.getConnection(), plantilla, documentTypeId);
				        	
				        	if(template != null){
								int templateId = template.getInt("ID");

								IItem entityTemplate = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, "", "");
								
								// Referencia al fichero del documento en el gestor documental
								String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
								entityTemplate.set("EXTENSION", extensionEntidad);
								String templateDescripcion = plantilla + " - " + cct.getSsVariable("NOMBRE");
								entityTemplate.set("DESCRIPCION", templateDescripcion);
								entityTemplate.store(cct);
								
					        	// 5. Actualizar el campo 'Acuse_Generado' con valor 'Y'
								IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
								participanteAActualizar.set("ACUSE_GENERADO", "Y");
								participanteAActualizar.store(cct);
						        
								// Si todo ha sido correcto borrar las variables de la session
								cct.deleteSsVariable("NOMBRE");
								cct.deleteSsVariable("DIRNOT");
								cct.deleteSsVariable("C_POSTAL");
								cct.deleteSsVariable("LOCALIDAD");
								cct.deleteSsVariable("CAUT");
				        	}
				        	else{
				        		logger.error("Falta introducir el id de la plantilla '"+plantilla+"' en el trámite del Catalogo de procedimientos");
				        		throw new ISPACInfo("Falta introducir el id de la plantilla '"+plantilla+"' en el trámite del Catalogo de procedimientos");
				        	}
					    }
					}catch (Throwable e) {
						
						logger.error("Error al generar el Acuse de recibo. "+e.getMessage(), e);
						
						// Si se produce algún error se hace rollback de la transacción
						cct.endTX(false);
						
						String message = "exception.documents.generate";
						String extraInfo = null;
						Throwable eCause = e.getCause();
						
						if (eCause instanceof ISPACException) {
							
							if (eCause.getCause() instanceof NoConnectException) {
								extraInfo = "exception.extrainfo.documents.openoffice.off"; 
							}
							else {
								extraInfo = eCause.getCause().getMessage();
							}
						}
						else if (eCause instanceof DisposedException) {
							extraInfo = "exception.extrainfo.documents.openoffice.stop";
						}
						else {
							extraInfo = e.getMessage();
						}			
						throw new ISPACInfo(message, extraInfo);
						
					}finally {
						
						if (connectorSession != null) {
							gendocAPI.closeConnectorSession(connectorSession);
						}
					}
				}// for
			}
			// Si todo ha sido correcto se hace commit de la transacción
			cct.endTX(true);
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

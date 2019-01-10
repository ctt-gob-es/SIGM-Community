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
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * 
 * @author diezp
 * @date 30/03/2009
 * @propósito Genera los acuses de recibo para el expediente de Decreto actual.
 */
public class GenerateAccuseDecretoRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(GenerateAccuseDecretoRule.class);

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
            Object connectorSession = null;
            
            // 1. Obtener participantes del expediente actual, con relación != "Trasladado"
            IItemCollection participantes = ParticipantesUtil.getParticipantesByRol(cct, numExp, ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil.ID);
            
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
                        
                            DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                            
                            //[Ticket 1300 Teresa] Cambio para que genere el documento que se especifique en datos específicos del catálogo de procedimientos.
                            String plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
                            String tipoDocumento = "";
                            
                            if(!StringUtils.isNotEmpty(plantilla)){
                                plantilla = "Acuse de Recibo";
                            }
                            
                            tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
                            int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
                            IItem template = TemplateDAO.getTemplate(cct, plantilla, documentTypeId);
                            
                            if(template != null){
                                int templateId = template.getInt("ID");

                                IItem entityTemplate = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, "", "");
                                
                                // Referencia al fichero del documento en el gestor documental
                                String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
                                entityTemplate.set(DocumentosUtil.EXTENSION, extensionEntidad);
                                String templateDescripcion = plantilla + " - " + cct.getSsVariable(ParticipantesUtil.NOMBRE);
                                entityTemplate.set(DocumentosUtil.DESCRIPCION, templateDescripcion);
                                entityTemplate.store(cct);
                                
                                // 5. Actualizar el campo 'Acuse_Generado' con valor 'Y'
                                IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
                                participanteAActualizar.set("ACUSE_GENERADO", "Y");
                                participanteAActualizar.store(cct);

                                DocumentosUtil.borraParticipanteSsVariable(cct);
                                
                            } else{
                                LOGGER.error("Falta introducir el id de la plantilla '"+plantilla+"' en el trámite del Catalogo de procedimientos");
                                throw new ISPACInfo("Falta introducir el id de la plantilla '"+plantilla+"' en el trámite del Catalogo de procedimientos");
                            }
                        }
                    }catch (Exception e) {
                        
                        LOGGER.error("Error al generar el Acuse de recibo. "+e.getMessage(), e);
                        
                        // Si se produce algún error se hace rollback de la transacción
                        cct.endTX(false);
                        
                        String message = "exception.documents.generate";
                        String extraInfo = null;
                        Throwable eCause = e.getCause();
                        
                        if (eCause instanceof ISPACException) {
                            
                            if (eCause.getCause() instanceof NoConnectException) {
                                extraInfo = "exception.extrainfo.documents.openoffice.off";
                                
                            } else {
                                extraInfo = eCause.getCause().getMessage();
                            }
                            
                        } else if (eCause instanceof DisposedException) {
                            extraInfo = "exception.extrainfo.documents.openoffice.stop";
                            
                        } else {
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
            // Si ha sido correcto se hace commit de la transacción
            cct.endTX(true);
        } catch(ISPACRuleException e) {
            LOGGER.info("No mostraba nada, solo muesta el mensaje. El error es: " + e.getMessage(), e);
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

}

package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * 
 * @author diezp
 * @proposito Envía un email a cada departamento/servicio seleccionado en la entidad Participantes,
 *               y que tenga la relación Trasladado.
 * 
 */
public class TrasladarDecretoRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(TrasladarDecretoRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            try{
                
                //Comprobar si el campo motivo rechazo tiene asignado un valor
                
                //----------------------------------------------------------------------------------------------
                IClientContext cct =  rulectx.getClientContext();
                IInvesflowAPI invesFlowAPI = cct.getAPI();
                IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
                //----------------------------------------------------------------------------------------------
        
                IItem exp = null;
                String motivoRechazo = null;
                String numExp = rulectx.getNumExp();
                IItemCollection collExps = entitiesAPI.getEntities("SGD_RECHAZO_DECRETO", numExp);
                Iterator<?> itExps = collExps.iterator();
                
                if (itExps.hasNext()) {
                    exp = (IItem)itExps.next();
                    motivoRechazo = exp.getString("RECHAZO_DECRETO");
                    
                    if (StringUtils.isNotEmpty(motivoRechazo)){
                        return null;
                    }
                }
            } catch (Exception e){
                LOGGER.info("No mostraba nada, solo muesta el mensaje. El error es: " + e.getMessage(), e);

                try {
                    throw new ISPACInfo("Se ha producido un error al ejecutar la regla de trasladar decreto.");
                } catch (ISPACInfo e1) {
                    LOGGER.error("ERROR. " + e1.getMessage(), e1);
                }
            }
            
            //APIs
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
                        
            // 1. Comprobar que el decreto esté firmado -->
            // --> Está ya hecho en ValidateFirmaDocRule, ejecutada antes que esta regla
            
            // 2. Obtener Participantes del expediente actual, con relación "Trasladado"
            String numExp = rulectx.getNumExp();
            IItemCollection participantes = ParticipantesUtil.getParticipantesByRol(cct, numExp, ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil.ID);
            
            if (participantes != null && participantes.toList().size() > 0){
                
                // 3. Obtener el documento decreto para anexarlo al email 
                int taskId = rulectx.getTaskId();
                String sqlQueryDoc = "ID_TRAMITE = " + taskId + " AND ESTADOFIRMA = '02'";
                IItemCollection documentos = entitiesAPI.getDocuments(numExp, sqlQueryDoc, "");
                
                if (documentos == null || documentos.toList().size()==0){
                    throw new ISPACRuleException("No se pueden enviar los traslados: No hay ningún documento firmado en el trámite.");
                    
                } else if (documentos.toList().size() > 1 ) {
                    throw new ISPACRuleException("No se pueden enviar los traslados: Hay más de un documento anexado al trámite.");
                    
                } else if (documentos.toList().size() == 1) {
                    
                    // 4. Enviar email con el decreto adjunto
    
                    int numDecreto = DecretosUtil.getNumDecreto(cct, numExp);
                    
                    // Fichero a adjuntar
                    IItem doc = (IItem)documentos.iterator().next();
                    String infoPagRde = doc.getString(DocumentosUtil.INFOPAG_RDE);
                    File file = DocumentosUtil.getFile(cct, infoPagRde);
                    
                    //expediente
                    IItem exp = entitiesAPI.getExpedient(rulectx.getNumExp());
                    
                    /**
                     * [Ticket #357#[Teresa] INICIO SIGEM Traslados cambiar el asuntos de los email que se mandan]
                     * **/
                    
                    String cContenido = "<br/> Número de expediente del decreto: "+rulectx.getNumExp() +
                            ", con asunto: "+exp.getString("ASUNTO") +
                            ", con fecha de aprobación: "+doc.getDate("FAPROBACION")+". \n"+
                            " Adjunto se envía el Decreto Nº"+numDecreto;
                    String cAsunto= "[SIGEM] Traslado de "+exp.getString("NOMBREPROCEDIMIENTO")+" Nº"+numDecreto;
                    
                    /**
                     * [Ticket #357#[Teresa] FIN SIGEM Traslados cambiar el asuntos de los email que se mandan]
                     * **/            
                    
                    // Para cada participante seleccionado --> enviar email y actualizar el campo DECRETO_TRASLADADO en la BBDD
                    for (int i=0; i<participantes.toList().size(); i++){
                        
                        IItem participante = (IItem) participantes.toList().get(i);
                        String direccionesCorreo = participante.getString(ParticipantesUtil.DIRECCIONTELEMATICA);
                        
                        if (direccionesCorreo != null){
                            StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
    
                            while (tokens.hasMoreTokens()) {
                                String cCorreoDestino = tokens.nextToken();    
                
                                if (participante!=null && StringUtils.isNotEmpty(cCorreoDestino)) {
                                    MailUtil.enviarCorreo(rulectx, direccionesCorreo, cAsunto, cContenido, file);
                                }
                            }
                            
                            // Y actualizar el campo 'DECRETO_TRASLADADO' con valor 'Y'
                            IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
                            participanteAActualizar.set("DECRETO_TRASLADADO", "Y");
                            participanteAActualizar.store(cct);
                        }
                    }
                    // Eliminar el fichero temporal una vez enviado por correo
                    if( null != file && file.exists()){
                        file.delete();
                    }
                }
            }
            return null;
            
        } catch(ISPACRuleException e) {            
            LOGGER.info("No mostraba nada, solo muesta el mensaje. El error es: " + e.getMessage(), e);
               throw new ISPACRuleException(e);
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }
        
    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

}

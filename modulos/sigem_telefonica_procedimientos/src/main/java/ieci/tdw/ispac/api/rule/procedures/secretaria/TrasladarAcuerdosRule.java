package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class TrasladarAcuerdosRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(TrasladarAcuerdosRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
                        
            String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
            boolean esAcuerdo = strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0;

            //Comprobar que el acuerdo esté firmado
            // --> Está ya hecho en ValidateFirmaRule, ejecutada antes que esta regla
            
            //Obtener las propuestas y urgencias incluidas en la sesión
            List<?> listPropuestas = SecretariaUtil.getPropuestasYUrgencias(cct, rulectx.getNumExp());
            Iterator<?> it = listPropuestas.iterator();
            IItem iProp = null;

            //Para cada propuesta se envía un email a sus participantes trasladados
            while (it.hasNext()) {
                iProp = (IItem)it.next();
                String numexpOrigen = iProp.getString("NUMEXP_ORIGEN");
                String strTabla = esAcuerdo? "SECR_ACUERDO":"SECR_DICTAMEN";
                String numAcuerdo = SecretariaUtil.getNumero(cct, numexpOrigen, strTabla);
                
                //Obtener Participantes de la propuesta actual, con relación "Trasladado"
                IItemCollection participantes = ParticipantesUtil.getParticipantesByRol(cct, numexpOrigen, ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil.ID);
                
                //Obtener el documento Certificado de acuerdos para anexarlo al email 
                int taskId = rulectx.getTaskId();
                String sqlQueryDoc = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA = '02' AND DESCRIPCION LIKE '%"+numAcuerdo+"%'";
                IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQueryDoc, "");
                
                if (documentos.toList().size() == 1) {
                    //Enviar email con el decreto adjunto
                    
                    String cContenido = "<br/>Adjunto se envía el Acuerdo Nº"+numAcuerdo;
                    String cAsunto= "[SIGEM] Traslado de Acuerdo Nº"+numAcuerdo;

                    if (!esAcuerdo) {
                        cContenido = "<br/>Adjunto se envía el Dictamen Nº"+numAcuerdo;
                        cAsunto= "[SIGEM] Traslado de Dictamen Nº"+numAcuerdo;
                    }
                    
                    // Fichero a adjuntar
                    IItem doc = (IItem)documentos.iterator().next();
                    String infoPag = doc.getString(DocumentosUtil.INFOPAG_RDE);
                    File file = DocumentosUtil.getFile(cct, infoPag);
                    
                    // Para cada participante seleccionado --> enviar email y actualizar el campo ACUERDO_TRASLADADO en la BBDD
                    for (int i=0; i<participantes.toList().size(); i++) {                        
                        IItem participante = (IItem) participantes.toList().get(i);
                        String direccionesCorreo = participante.getString(ParticipantesUtil.DIRECCIONTELEMATICA);
                        
                        if (direccionesCorreo != null) {
                            StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
                            
                            while (tokens.hasMoreTokens()){
                                String cCorreoDestino = tokens.nextToken();
                                
                                if (participante!=null && StringUtils.isNotEmpty(cCorreoDestino)) {
                                    MailUtil.enviarCorreo(rulectx, direccionesCorreo, cAsunto, cContenido, file);
                                }
                            }
                        }
                    }
                    // Eliminar el fichero temporal una vez enviado por correo
                    if(null != file && file.exists()){
                        file.delete();
                    }
                }
            }
            return null;
            
        } catch(ISPACRuleException e) {
            LOGGER.info("No mostraba nada, el error es: " + e.getMessage(), e);
            throw new ISPACRuleException(e);
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}

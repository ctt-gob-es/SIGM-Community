package es.dipucr.sigem.api.rule.procedures.ayudassociales.planvg;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class DipucrDatosSolRechazadosVG implements IRule{
    private static final Logger LOGGER = Logger .getLogger(DipucrDatosSolRechazadosVG.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct = rulectx.getClientContext();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO +this.getClass().getName());
            String numexp = rulectx.getNumExp();
            
            //Tenemos que devolver la siguiente cadena:
            /**    
                       1.LOCALIDAD
                Talleres de educación en la igualdad:
                Actividades de educación en interculturalidad:
                Talleres contra bullying y ciberbullying:                 
             */
            
            int contador = 0;
            String beneficiario = "";
            String motivo = "";
            String fechaSolicitud = "";
            
            StringBuilder salida = new StringBuilder();
            
            List<String> expedientesResolucionOrdenadosList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RC, ExpedientesUtil.IDENTIDADTITULAR);
            
            for(String numexpHijo :  expedientesResolucionOrdenadosList){
                    
                contador++;

                ObjetoSolictudConvocatoriaSubvencion solicitudConvocatoria = new ObjetoSolictudConvocatoriaSubvencion(rulectx.getClientContext(), numexpHijo);
                
                beneficiario = solicitudConvocatoria.getBeneficiario();
                fechaSolicitud = solicitudConvocatoria.getFechaSolicitud();
                
                if(solicitudConvocatoria.esRechazadoGrupo1()){

                    salida.append("\n");
                    salida.append("\t" + contador + ". " + beneficiario + "\n\n");
                    
                    motivo = solicitudConvocatoria.getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);

                    salida.append("\tMotivo: " + motivo + "\n");
                    if(motivo.toUpperCase().contains("PLAZO") || motivo.toUpperCase().contains("FUERA")){
                        salida.append("Solicitud presentada: ");
                        salida.append(fechaSolicitud);
                    }
                    
                    solicitudConvocatoria.getInteresado().setTipoPersona(ParticipantesUtil._TIPO_PERSONA_FISICA);
                    solicitudConvocatoria.getInteresado().setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                    solicitudConvocatoria.insertaParticipante(cct, numexp);
                }            
            }
                        
            LOGGER.info(ConstantesString.FIN +this.getClass().getName());
            return salida.toString();
            
        } catch (ISPACRuleException e){
            throw new ISPACRuleException(e);
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
}
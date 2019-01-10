package es.dipucr.sigem.api.rule.procedures.ayudassociales.planvg;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.ParticipantesSubvencionesUtil;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrDatosSolAprobadosVG implements IRule{
    private static final Logger LOGGER = Logger .getLogger(DipucrDatosSolAprobadosVG.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
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
            String ayuntamiento = "";
            String cifAyto = "";
            int numTalleres = 0;
            String motivoTalleres = "";
            int numActividades = 0;
            String motivoActividades = "";
            int numBullying = 0;
            String motivoBullying = "";
            
            StringBuilder salida = new StringBuilder("");

            List<String> expedientesResolucionOrdenadosList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.IDENTIDADTITULAR);

            //Para cada uno escribimos la salida
            for(String numexpResolucion : expedientesResolucionOrdenadosList){
            
                IItem expedientesResolucionOrdenados = ExpedientesUtil.getExpediente(cct, numexpResolucion);
                contador++;
                ayuntamiento = SubvencionesUtils.getString(expedientesResolucionOrdenados, ExpedientesUtil.IDENTIDADTITULAR);
                cifAyto = SubvencionesUtils.getString(expedientesResolucionOrdenados, ExpedientesUtil.NIFCIFTITULAR);
                
                IItemCollection resolucionCollection = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, numexpResolucion);
                Iterator<?> resolucionIterator = resolucionCollection.iterator();
                
                if(resolucionIterator.hasNext()){
                    IItem resolucion = (IItem)resolucionIterator.next();
                    
                    numTalleres = 0;
                    numActividades = 0;
                    numBullying = 0;
                    
                    numTalleres += SubvencionesUtils.getInt(resolucion, ConstantesSubvenciones.DatosResolucion.TALLERES_EDU);
                    motivoTalleres = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOT_RECHAZO_TALLERES_IGU);

                    numActividades += SubvencionesUtils.getInt(resolucion, ConstantesSubvenciones.DatosResolucion.ACTIVIDADES_INTERCULT);
                    motivoActividades = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOT_RECHAZO_ACTIVIDAD_INTERCUL);

                    numBullying += SubvencionesUtils.getInt(resolucion, ConstantesSubvenciones.DatosResolucion.TALLERES_BULLYING);
                    motivoBullying = SubvencionesUtils.getString(resolucion, ConstantesSubvenciones.DatosResolucion.MOT_RECHAZO_BULLYING);
                    
                    salida.append("\n");
                    salida.append("\t" +contador+". " +ayuntamiento+"\n\n");

                    salida.append("\tNúmero de talleres de educación en la igualdad: " + numTalleres + "\n");
                    if(StringUtils.isNotEmpty(motivoTalleres)){
                        salida.append("\tMotivo: " +motivoTalleres+"\n");
                    }

                    salida.append("\tNúmero de talleres de educación en interculturalidad: " + numActividades + "\n");
                    if(StringUtils.isNotEmpty(motivoActividades)){
                        salida.append("\tMotivo: " +motivoActividades+"\n");
                    }
                    
                    salida.append("\tNúmero de talleres contra bullying y ciberbullying: " +numBullying+"\n");
                    if(StringUtils.isNotEmpty(motivoBullying)){
                        salida.append("\tMotivo: " +motivoBullying+"\n");
                    }
                    
                    //Para cada expediente insertamos al ayuntamiento como interesado                    
                    ParticipantesSubvencionesUtil participante = new ParticipantesSubvencionesUtil(cifAyto, ayuntamiento);

                    participante.setRol(ParticipantesUtil._TIPO_INTERESADO);
                    participante.setRecurso(ParticipantesUtil.RECURSO_AYTOS_ADM_PUBL);
                    participante.setTipoDireccion(ParticipantesUtil.TIPO_DIRECCION_TELEMATICA);
                    participante.setTipoPersona(ParticipantesUtil._TIPO_PERSONA_FISICA);

                    SubvencionesUtils.insertaParticipante(cct, numexp, participante);
                }        
            }
                        
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return salida.toString();
            
        } catch (ISPACRuleException e){
            throw new ISPACRuleException(e);
        }catch(Exception e) {            
            throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
}
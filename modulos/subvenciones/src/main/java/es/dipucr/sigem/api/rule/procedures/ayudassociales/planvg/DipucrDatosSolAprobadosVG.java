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

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

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
             */
            
            int contador = 0;
            String ayuntamiento = "";
            String cifAyto = "";
            int numTalleres = 0;
            String motivoTalleres = "";
            int numActividades = 0;
            String motivoActividades = "";
            String expedientes = "";
            
            StringBuilder salida = new StringBuilder();
            
            //Recuperamos los expedientes relacionados
            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
            IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt = expRelCol.iterator();                  
            if(expRelIt.hasNext()){
                while (expRelIt.hasNext()){
                    IItem expRel = (IItem)expRelIt.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    String numexpHijo = expRel.getString("NUMEXP_HIJO");
                    
                    IItem expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo); 
                    
                    if(expHijo != null && "RS".equals(expHijo.get("ESTADOADM"))){
                        expedientes += "'" +numexpHijo+"',";
                    }                    
                }
            }    
            
            if(expedientes.length()>0){
                expedientes = expedientes.substring(0,expedientes.length()-1);
            }
            
            //Ordenamos los expedientes por ayuntamiento
            IItemCollection expedientesResolucionOrdenadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (" +expedientes+") ORDER BY IDENTIDADTITULAR");
            Iterator<?> expedientesResolucionOrdenadosIterator = expedientesResolucionOrdenadosCollection.iterator();
            
            //Para cada uno escribimos la salida
            while(expedientesResolucionOrdenadosIterator.hasNext()){
                
                IItem expedientesResolucionOrdenados = (IItem) expedientesResolucionOrdenadosIterator.next();
                contador++;
                ayuntamiento = expedientesResolucionOrdenados.getString("IDENTIDADTITULAR");
                cifAyto = expedientesResolucionOrdenados.getString("NIFCIFTITULAR");
                
                IItemCollection resolucionCollection = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expedientesResolucionOrdenados.getString("NUMEXP"));
                Iterator<?> resolucionIterator = resolucionCollection.iterator();
                
                if(resolucionIterator.hasNext()){
                    IItem resolucion = (IItem)resolucionIterator.next();
                    
                    numTalleres = 0;
                    numActividades = 0;
                    
                    try{
                        numTalleres += Integer.parseInt(resolucion.getString("TALLERES_EDU"));                        
                    } catch(Exception e){
                        numTalleres = 0;
                        LOGGER.debug("El campo TALLERES_EDU es nulo, vacío o no numérico. " + e.getMessage(), e);
                    }
                    motivoTalleres = resolucion.getString("MOT_RECHAZO_TALLERES_IGU");
                    try{
                        numActividades += Integer.parseInt(resolucion.getString("ACTIVIDADES_INTERCULT"));
                    } catch(Exception e){
                        numActividades = 0;
                        LOGGER.debug("El campo ACTIVIDADES_INTERCULT es nulo, vacío o no numérico. " + e.getMessage(), e);
                    }
                    motivoActividades = resolucion.getString("MOT_RECHAZO_ACTIVIDAD_INTERCUL");
                    
                    salida.append("\n");
                    salida.append("\t" +contador+". " +ayuntamiento+"\n\n");
                    try{
                        salida.append("\tNúmero de talleres de educación en la igualdad: " +numTalleres+"\n");
                        if(StringUtils.isNotEmpty(motivoTalleres)){
                            salida.append("\tMotivo: " +motivoTalleres+"\n");                            
                        }
                    } catch(Exception e){
                        LOGGER.debug("Ha habido un error al recuperar el número de talleres o el motivo. " + e.getMessage(), e);
                    }
                    try{
                        salida.append("\tNúmero de talleres de educación en interculturalidad: " +numActividades+"\n");
                        if(StringUtils.isNotEmpty(motivoActividades)){
                            salida.append("\tMotivo: " +motivoActividades+"\n");                            
                        }
                    } catch(Exception e){
                        LOGGER.debug("Ha habido un error al recuperar el número de actividades o el motivo. " + e.getMessage(), e);
                    }
                    
                //Para cada expediente insertamos al ayuntamiento como interesado
                    //Comprobamos que no esté                
                    if(StringUtils.isNotEmpty(cifAyto)){
                        IItemCollection nuevoParticipanteCol = ParticipantesUtil.getParticipantes( cct, numexp, "NDOC='" +expedientesResolucionOrdenados.getString("NIFCIFTITULAR")+"'", "");
                        Iterator<?> nuevoParticipanteIt = nuevoParticipanteCol.iterator();
                        if(!nuevoParticipanteIt.hasNext()){
    
                            IItem nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, numexp);
                            
                            nuevoParticipante.set("ROL", "INT");
                            nuevoParticipante.set("TIPO_PERSONA", "F");
                            nuevoParticipante.set("NDOC", cifAyto);
                            nuevoParticipante.set("NOMBRE", ayuntamiento);                
                            nuevoParticipante.set("TIPO_DIRECCION", "T");
                            nuevoParticipante.set("RECURSO","Aytos.Adm.Publ.");
                            try{
                                nuevoParticipante.store(cct);
                            } catch(Exception e){
                                LOGGER.error(e.getMessage(), e);
                            }
                        }
                    }
                }            
            }
                        
            LOGGER.info(ConstantesString.FIN +this.getClass().getName());
            return salida.toString();
            
        } catch (ISPACRuleException e){
            throw new ISPACRuleException(e);
        }catch(Exception e) {            
            throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

}
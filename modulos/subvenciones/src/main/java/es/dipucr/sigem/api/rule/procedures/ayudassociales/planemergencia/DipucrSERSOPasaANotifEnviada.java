package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrSERSOPasaANotifEnviada implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOPasaANotifEnviada.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        String numexpHijo = "";
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName()
                    );
            StringBuilder listado = new StringBuilder();  //Listado de liquidaciones 
            String numexp = rulectx.getNumExp();
            ArrayList<String> expedientesResolucion = new ArrayList<String>();
            
            //Recuperamos los expedientes relacionados
            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
            IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt = expRelCol.iterator();                  
            if(expRelIt.hasNext()){
                while (expRelIt.hasNext()){
                    IItem expRel = (IItem)expRelIt.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    numexpHijo = expRel.getString("NUMEXP_HIJO");
                    
                    IItem expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo); 
                    if((expHijo != null) && ("AP".equals(expHijo.get("ESTADOADM")))){
                        expedientesResolucion.add(numexpHijo);
                    }                    
                }
            }    
            //Ya tenemos los expedientes que vamos a decretar en esta iteración
            if(!expedientesResolucion.isEmpty()){
                strQuery = "WHERE NUMEXP IN (";
                Iterator<?> expedientesResolucionIt = expedientesResolucion.listIterator();
                while(expedientesResolucionIt.hasNext()){
                    strQuery += "'" +expedientesResolucionIt.next()+"',";
                }
                strQuery = strQuery.substring(0,strQuery.length()-1);
                strQuery += ")";
                 
                //Cambiamos el estado
                IItemCollection expedientesCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, strQuery);
                Iterator<?> expedientesIt = expedientesCol.iterator();
                while (expedientesIt.hasNext()){
                    IItem expediente = (IItem)expedientesIt.next();
                    expediente.set("ESTADOADM", "NE");
                    expediente.store(cct);
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return listado.toString();
            
        } catch(Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al pasar a notificado el expediente: " + numexpHijo + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al pasar a notificado el expediente: " + numexpHijo + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

}
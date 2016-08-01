package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
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

public class DipucrSERSORecalculaValesPE implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSORecalculaValesPE.class);

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {    
        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        String numexp = "";
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            numexp = rulectx.getNumExp();     
            
            double totalSemestre1 = 0;
            double totalSemestre2 = 0;
            double totalSemestre3 = 0;
            double totalSemestre4 = 0;
            
            int semestre1Impresos = 0;
            int semestre2Impresos = 0;
            int semestre3Impresos = 0;
            int semestre4Impresos = 0;
            
            int semestre1ImpresosAct = 0;
            int semestre2ImpresosAct = 0;
            int semestre3ImpresosAct = 0;
            int semestre4ImpresosAct = 0;        
            
            ArrayList<String> expedientes = new ArrayList<String>();
            
            //recuperamos los vales del expediente anterior
            IItemCollection expAntCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='" +numexp+"' OR NUMEXP_HIJO ='" +numexp+"'");
            Iterator<?> expAntIterator = expAntCollection.iterator();
            while(expAntIterator.hasNext()){                
                IItem expAnt = (IItem)expAntIterator.next();
                if( expAnt.getString("NUMEXP_PADRE").equals(numexp)){
                    //Sólo tomamos los expedientes anteriores al actual
                    String expaux1 = expAnt.getString("NUMEXP_HIJO");
                    try{
                        if(Integer.parseInt(expaux1.substring(9,expaux1.length()))<Integer.parseInt(numexp.substring(9,numexp.length()))){
                            expedientes.add(expaux1);
                        }
                    } catch(Exception e){
                        expedientes.add(expAnt.getString("NUMEXP_HIJO"));
                        LOGGER.debug("Ha ocurrido alguna incidencia con el expediente hijo: " + expaux1 + ". " + e.getMessage(), e);
                    }
                } else{
                    String expaux1 = expAnt.getString("NUMEXP_PADRE");
                    try{
                        if(Integer.parseInt(expaux1.substring(9,expaux1.length()))<Integer.parseInt(numexp.substring(9,numexp.length()))){
                            expedientes.add(expaux1);
                        }
                    } catch(Exception e){
                        expedientes.add(expAnt.getString("NUMEXP_PADRE"));
                        LOGGER.debug("Ha ocurrido alguna incidencia con el expediente padre: " + expaux1 + ". " + e.getMessage(), e);
                    }
                }
            }
                
            if(!expedientes.isEmpty()){
                String strQuery = "WHERE NUMEXP IN (";
                Iterator<String> expedientesResolucionIt = expedientes.listIterator();
                while(expedientesResolucionIt.hasNext()){
                    strQuery += "'" +expedientesResolucionIt.next()+"',";
                }
                strQuery = strQuery.substring(0,strQuery.length()-1);
                strQuery += ") ORDER BY SUBSTR(NUMEXP, 5,4)::INT, SUBSTR(NUMEXP, 10)::INT DESC";
                
                IItemCollection valesAntCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, strQuery);
                Iterator<?> valesAntIterator = valesAntCollection.iterator();
                
                boolean salir = false;
                while(valesAntIterator.hasNext() && !salir){
                    IItem valesAnt = (IItem)valesAntIterator.next();

                    //Tomamos sólo los que no están rechazados y no son de excepcionales ni de libros ni de comedor
                    IItem exp = ExpedientesUtil.getExpediente(cct, valesAnt.getString("NUMEXP"));
                    if(!"RC".equals(exp.getString("ESTADOADM")) && exp.getString("ASUNTO").toUpperCase().indexOf(ConstantesPlanEmergencia.EXCEPCIONAL)<0
                            && exp.getString("ASUNTO").toUpperCase().indexOf(ConstantesPlanEmergencia.LIBROS)<0 && exp.getString("ASUNTO").toUpperCase().indexOf(ConstantesPlanEmergencia.COMEDOR)<0){
                        salir = true;
                        try{
                            semestre1Impresos = valesAnt.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                        } catch(Exception e){
                            semestre1Impresos = 0;
                            LOGGER.debug("El campo SEMESTRE1IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                        }
                        try{
                            semestre2Impresos = valesAnt.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                        } catch(Exception e){
                            semestre2Impresos = 0;
                            LOGGER.debug("El campo SEMESTRE2IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                        }
                        try{
                            semestre3Impresos = valesAnt.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                        } catch(Exception e){
                            semestre3Impresos = 0;
                            LOGGER.debug("El campo SEMESTRE3IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                        }
                        try{
                            semestre4Impresos = valesAnt.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                        } catch(Exception e){
                            semestre4Impresos = 0;
                            LOGGER.debug("El campo SEMESTRE4IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                        }
                    }
                }                
            }
            
            IItemCollection concesionCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexp);
            Iterator<?> concesionIterator = concesionCollection.iterator();
            
            if (concesionIterator.hasNext()){
                IItem solicitud = (IItem)concesionIterator.next();
                
                totalSemestre1 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO));
                totalSemestre2 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2));
                totalSemestre3 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3));
                totalSemestre4 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4));

                IItemCollection valesCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexp);
                Iterator<?> valesIterator = valesCollection.iterator();
                
                IItem vales = null;
                if(valesIterator.hasNext()){
                    vales = (IItem) valesIterator.next();    
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4, 0);
                    try{
                        semestre1ImpresosAct = vales.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                    } catch(Exception e){
                        semestre1ImpresosAct = 0;
                        LOGGER.debug("El campo SEMESTRE1IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                    }
                    try{
                        semestre2ImpresosAct = vales.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                    } catch(Exception e){
                        semestre2ImpresosAct = 0;
                        LOGGER.debug("El campo SEMESTRE2IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                    }
                    try{
                        semestre3ImpresosAct = vales.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                    } catch(Exception e){
                        semestre3ImpresosAct = 0;
                        LOGGER.debug("El campo SEMESTRE3IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                    }
                    try{
                        semestre4ImpresosAct = vales.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                    } catch(Exception e){
                        semestre4ImpresosAct = 0;
                        LOGGER.debug("El campo SEMESTRE4IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                    }
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS, (semestre1ImpresosAct==0 || semestre1Impresos>semestre1ImpresosAct)? semestre1Impresos:semestre1ImpresosAct);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS, (semestre2ImpresosAct==0 || semestre2Impresos>semestre2ImpresosAct)? semestre2Impresos:semestre2ImpresosAct);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS, (semestre3ImpresosAct==0 || semestre3Impresos>semestre3ImpresosAct)? semestre3Impresos:semestre3ImpresosAct);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS, (semestre4ImpresosAct==0 || semestre4Impresos>semestre4ImpresosAct)? semestre4Impresos:semestre4ImpresosAct);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1, totalSemestre1/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2, totalSemestre2/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3, totalSemestre3/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4, totalSemestre4/30);
                    int aux = 0;
                    try{
                        aux = vales.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                    } catch(Exception e){
                        aux = 0;
                        LOGGER.debug("El campo SEMESTRE3IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                    }
                    if(aux < 0){
                        aux = 0;
                    }
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS, aux);
                    try{
                        aux = vales.getShort(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                    } catch(Exception e){
                        aux = 0;
                        LOGGER.debug("El campo SEMESTRE4IMPRESOS es nulo o vacío. " + e.getMessage(), e);
                    }
                    if(aux < 0){
                        aux = 0;
                    }
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS, aux);
                } else{
                    vales = entitiesAPI.createEntity(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexp);

                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS, semestre1Impresos);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS, semestre2Impresos);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS, semestre3Impresos);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS, semestre4Impresos);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1, totalSemestre1/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2, totalSemestre2/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3, totalSemestre3/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4, totalSemestre4/30);
                }            
                
                vales.store(cct);
            }            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recalcular los cheques del espediente: " +numexp + ". " + e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recalcular los cheques del espediente: " +numexp + ". " + e.getMessage(), e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }

}

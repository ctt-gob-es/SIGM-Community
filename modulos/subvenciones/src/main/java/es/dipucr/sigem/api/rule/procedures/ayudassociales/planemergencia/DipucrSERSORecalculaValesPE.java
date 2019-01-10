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
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSORecalculaValesPE implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSORecalculaValesPE.class);

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {    
        //No se da nunca este caso
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
            IItemCollection expAntCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, ConstantesString.WHERE + ExpedientesRelacionadosUtil.NUMEXP_PADRE + " = '" +numexp+"' OR " + ExpedientesRelacionadosUtil.NUMEXP_HIJO + " = '" +numexp+"'");
            Iterator<?> expAntIterator = expAntCollection.iterator();
            while(expAntIterator.hasNext()){                
                IItem expAnt = (IItem)expAntIterator.next();
                
                //Sólo tomamos los expedientes anteriores al actual
                if( numexp.equals(SubvencionesUtils.getString(expAnt, ExpedientesRelacionadosUtil.NUMEXP_PADRE))){

                    String expaux1 = SubvencionesUtils.getString(expAnt, ExpedientesRelacionadosUtil.NUMEXP_HIJO);

                    if(esAnterior(expaux1, numexp)){
                        expedientes.add(expaux1);
                    }
                } else{
                    String expaux1 = SubvencionesUtils.getString(expAnt, ExpedientesRelacionadosUtil.NUMEXP_PADRE);
                    
                    if(esAnterior(expaux1, numexp)){
                        expedientes.add(expaux1);
                    }                    
                }
            }
                
            if(!expedientes.isEmpty()){
                String strQuery = ConstantesString.WHERE + " NUMEXP IN (";
                Iterator<String> expedientesResolucionIt = expedientes.listIterator();
                while(expedientesResolucionIt.hasNext()){
                    strQuery += "'" +expedientesResolucionIt.next() + "' ";
                    if(expedientesResolucionIt.hasNext()){
                        strQuery += ",";
                    }
                }
                strQuery += ") ORDER BY SUBSTR(NUMEXP, 5,4)::INT, SUBSTR(NUMEXP, 10)::INT DESC";
                
                IItemCollection valesAntCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, strQuery);
                Iterator<?> valesAntIterator = valesAntCollection.iterator();
                
                boolean salir = false;
                while(valesAntIterator.hasNext() && !salir){
                    IItem valesAnt = (IItem)valesAntIterator.next();

                    //Tomamos sólo los que no están rechazados y no son de excepcionales ni de libros ni de comedor
                    String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, valesAnt.getString(ConstantesPlanEmergencia.DpcrSERSONVales.NUMEXP));
                    String asunto = ExpedientesUtil.getAsunto(cct, valesAnt.getString(ConstantesPlanEmergencia.DpcrSERSONVales.NUMEXP));
                    
                    if(!ExpedientesUtil.EstadoADM.RC.equals(estadoAdm) 
                            && asunto.toUpperCase().indexOf(ConstantesPlanEmergencia.EXCEPCIONAL) < 0
                            && asunto.toUpperCase().indexOf(ConstantesPlanEmergencia.LIBROS) < 0 
                            && asunto.toUpperCase().indexOf(ConstantesPlanEmergencia.COMEDOR) < 0){
                        salir = true;
                        
                           semestre1Impresos = SubvencionesUtils.getShortAsInt(valesAnt, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                           semestre2Impresos = SubvencionesUtils.getShortAsInt(valesAnt, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                           semestre3Impresos = SubvencionesUtils.getShortAsInt(valesAnt, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                           semestre4Impresos = SubvencionesUtils.getShortAsInt(valesAnt, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                    }
                }                
            }
            if(0 > semestre1Impresos){
                semestre1Impresos = 0;
            }
            if(0 > semestre2Impresos){
                semestre2Impresos = 0;
            }
            if(0 > semestre3Impresos){
                semestre3Impresos = 0;
            }
            if(0 > semestre4Impresos){
                semestre4Impresos = 0;
            }
            
            IItemCollection concesionCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexp);
            Iterator<?> concesionIterator = concesionCollection.iterator();
            
            if (concesionIterator.hasNext()){
                IItem solicitud = (IItem)concesionIterator.next();
                
                totalSemestre1 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO);
                totalSemestre2 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2);
                totalSemestre3 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3);
                totalSemestre4 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4);

                IItemCollection valesCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexp);
                Iterator<?> valesIterator = valesCollection.iterator();
                
                IItem vales = null;
                if(valesIterator.hasNext()){
                    vales = (IItem) valesIterator.next();    
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4, 0);

                    semestre1ImpresosAct = SubvencionesUtils.getShortAsInt(vales, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                    semestre2ImpresosAct = SubvencionesUtils.getShortAsInt(vales, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                    semestre3ImpresosAct = SubvencionesUtils.getShortAsInt(vales, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                    semestre4ImpresosAct = SubvencionesUtils.getShortAsInt(vales, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);

                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS, (semestre1ImpresosAct == 0 || semestre1Impresos > semestre1ImpresosAct)? semestre1Impresos : semestre1ImpresosAct);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS, (semestre2ImpresosAct == 0 || semestre2Impresos > semestre2ImpresosAct)? semestre2Impresos : semestre2ImpresosAct);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS, (semestre3ImpresosAct == 0 || semestre3Impresos > semestre3ImpresosAct)? semestre3Impresos : semestre3ImpresosAct);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS, (semestre4ImpresosAct == 0 || semestre4Impresos > semestre4ImpresosAct)? semestre4Impresos : semestre4ImpresosAct);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1, totalSemestre1/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2, totalSemestre2/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3, totalSemestre3/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4, totalSemestre4/30);
                    
                    int aux = SubvencionesUtils.getShortAsInt(vales, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);

                    if(0 > aux){
                        aux = 0;
                    }
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS, aux);

                    aux = SubvencionesUtils.getShortAsInt(vales, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                    
                    if(0 > aux){
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

    public boolean esAnterior(String numExp1, String numExp2) {
        
        boolean resultado = false;
        int numExp1Numero = Integer.MIN_VALUE;
        int numExp2Numero = Integer.MAX_VALUE;
        
        if(StringUtils.isNotEmpty(numExp1)){
            String[] numExp1Split = numExp1.split("/");
            
            if(2 == numExp1Split.length && StringUtils.isNumeric(numExp1Split[1])){
                numExp1Numero = Integer.parseInt(numExp1Split[1]);
            }            
        }
        
        if(StringUtils.isNotEmpty(numExp2)){
            String[] numExp2Split = numExp2.split("/");
            
            if(2 == numExp2Split.length && StringUtils.isNumeric(numExp2Split[1])){
                numExp2Numero = Integer.parseInt(numExp2Split[1]);
            }            
        }
        
        if(numExp1Numero < numExp2Numero){
            resultado = true;
        }

        return resultado;
    }

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }

}

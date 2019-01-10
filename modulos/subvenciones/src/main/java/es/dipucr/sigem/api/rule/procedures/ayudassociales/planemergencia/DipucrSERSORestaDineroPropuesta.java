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
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSORestaDineroPropuesta implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSORestaDineroPropuesta.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        String numexp = "";
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            
            numexp = rulectx.getNumExp();
            List<String> expedientesResolucion = new ArrayList<String>();
                                
            String tipoAyuda = "";
            String trimestre = "";
            
            int numCheques = 0;
            int semestreImpresos = 0;
            int importeCheques = 0;
            
            int numCheques1 = 0;
            int semestreImpresos1 = 0;
            int importeCheques1 = 0;
            
            int numCheques2 = 0;
            int semestreImpresos2 = 0;
            int importeCheques2 = 0;
            
            int numCheques3 = 0;
            int semestreImpresos3 = 0;
            int importeCheques3 = 0;
            
            int numCheques4 = 0;
            int semestreImpresos4 = 0;
            int importeCheques4 = 0;
            
            int total = 0;
            
            expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS);
            
            //Ya tenemos los expedientes que vamos a decretar en esta iteración
            if(!expedientesResolucion.isEmpty()){

                String strQuery = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " IN " + SubvencionesUtils.getWhereInFormat(expedientesResolucion);               
                IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery);
                
                String strQuery2 = ConstantesString.WHERE + ConstantesSubvenciones.MunicipiosValidationTable.VALOR + " IN " + SubvencionesUtils.getWhereInFormat(expSolicitudesCiudadCol, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD) + ConstantesString.ORDER_BY + ConstantesSubvenciones.MunicipiosValidationTable.SUSTITUTO;                
                IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities(ConstantesSubvenciones.MunicipiosValidationTable.NOMBRE_TABLA, strQuery2);
                Iterator<?> ciudadOrdenIt = ciudadOrdenCol.iterator();
                
                while(ciudadOrdenIt.hasNext()){
                    
                    IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery+" AND CIUDAD = '" +((IItem)ciudadOrdenIt.next()).getString("VALOR")+"' ORDER BY TIPOAYUDA, NOMBRESOLICITANTE");
                    Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                    while (expSolicitudesIt.hasNext()){
                        
                        numCheques = 0;
                        semestreImpresos = 0;
                        importeCheques = 0;                
                        numCheques1 = 0;
                        semestreImpresos1 = 0;
                        importeCheques1 = 0;                        
                        numCheques2 = 0;
                        semestreImpresos2 = 0;
                        importeCheques2 = 0;
                        numCheques3 = 0;
                        semestreImpresos3 = 0;
                        importeCheques3 = 0;
                        numCheques4 = 0;
                        semestreImpresos4 = 0;
                        importeCheques4 = 0;
                        
                        IItem expSolicitud = (IItem) expSolicitudesIt.next();
                        trimestre = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.TRIMESTRE);
                                                
                        tipoAyuda = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);                                     
    
                        IItemCollection numChequesCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, "NUMEXP"));
                        Iterator<?> numChequesIt = numChequesCol.iterator();
                        
                        if(numChequesIt.hasNext()){   
                            IItem numCheq = (IItem)numChequesIt.next();
                            
                            numCheques1 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1);
                            semestreImpresos1 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                            importeCheques1 = (numCheques - semestreImpresos) * 30;
                            
                            numCheques2 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2);
                            semestreImpresos2 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                            importeCheques2 = (numCheques2 - semestreImpresos2) * 30;
                            
                            numCheques3 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3);
                            semestreImpresos3 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                            importeCheques3 = (numCheques3 - semestreImpresos3) * 30;
                            
                            numCheques4 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4);
                            semestreImpresos4 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                            importeCheques4 = (numCheques4 - semestreImpresos4) * 30;
                        }
                                            
                        if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                            numCheques = numCheques1;
                            semestreImpresos = semestreImpresos1;
                            importeCheques = importeCheques1;
                        } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                            numCheques = numCheques2;
                            semestreImpresos = semestreImpresos2;
                            importeCheques = importeCheques2;
                        } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                            numCheques = numCheques3;
                            semestreImpresos = semestreImpresos3;
                            importeCheques = importeCheques3;     
                        } else{
                            numCheques = numCheques4;
                            semestreImpresos = semestreImpresos4;
                            importeCheques = importeCheques4;
                        }
                        
                        if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){                            
                            total += importeCheques;                            
                        }
                    }
                }            
            }
            
            //Comprobamos que no nos hayamos pasado de los 500000
            //Recuperamos las cantidades del ayuntamiento en cuestión
            IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, ConstantesString.WHERE + " LOCALIDAD = '999' AND NUMEXPCONVOCATORIA = '" +numexp+"'");
            Iterator<?> cantidadesIt = cantidadesCol.iterator();
            if(cantidadesIt.hasNext()){                        
                IItem cantidades = (IItem)cantidadesIt.next();
                double primerTrim = SubvencionesUtils.getDouble( cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.PRIMERTRIMESTRE);
                double segundoTrim = SubvencionesUtils.getDouble( cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.SEGUNDOTRIMESTRE); 
                double tercerTrim = SubvencionesUtils.getDouble( cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TERCERTRIMESTRE);
                double cuartoTrim = SubvencionesUtils.getDouble( cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.CUARTOTRIMESTRE);
                                
                if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                    primerTrim -= total;
                } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                    segundoTrim -= total;
                } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                    tercerTrim -= total;
                } else{
                    cuartoTrim -= total;
                }

                cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.PRIMERTRIMESTRE, "" + primerTrim);
                cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.SEGUNDOTRIMESTRE, "" + segundoTrim); 
                cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TERCERTRIMESTRE, "" + tercerTrim);
                cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.CUARTOTRIMESTRE, "" +cuartoTrim);
                cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTAL, "" + (primerTrim + segundoTrim + tercerTrim + cuartoTrim));
                cantidades.store(cct);
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;
            
        } catch(Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al restar dinero al eliminar la propuesta del expediente: " + numexp + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al restar dinero al eliminar la propuesta del expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}
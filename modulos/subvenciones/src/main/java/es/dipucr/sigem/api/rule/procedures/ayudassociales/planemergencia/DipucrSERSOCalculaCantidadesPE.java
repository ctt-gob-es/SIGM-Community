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

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOCalculaCantidadesPE implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSOCalculaCantidadesPE.class);

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
                        
            String numexp = rulectx.getNumExp();
            
            String nifBenef = "";
            String numexpConvocatoria = "";

            int numMiembros1 = 0;
            String menores3anios1 = "NO";
            String numMenores = "0";
            
            int porMiembros = 0;
            int porMenores = 0; 
            
            double porMiembros1 = 0;
            double porMenores1 = 0; 
            double porMiembros2 = 0;
            double porMenores2 = 0;            
            double porMiembros3 = 0;
            double porMenores3 = 0;            
            double porMiembros4 = 0;
            double porMenores4 = 0;
            
            double propuesta11 = 0;            
            double propuesta12 = 0;
            double propuesta1Exc = 0;
            double countAyuda11 = 0;
            double countAyuda1E = 0;

            double propuesta21 = 0;
            double propuesta22 = 0;
            double propuesta2Exc = 0;
            double countAyuda21 = 0;
            double countAyuda2E = 0;
            
            double propuesta31 = 0;
            double propuesta32 = 0;
            double propuesta3Exc = 0;
            double countAyuda31 = 0;
            double countAyuda3E = 0;
            
            double propuesta41 = 0;
            double propuesta42 = 0;
            double propuesta4Exc = 0;
            double countAyuda41 = 0;
            double countAyuda4E = 0;
            
            double totalMes1 = 0;
            double maximosemestre1 = 0;
            double totalMes2 = 0;
            double maximosemestre2 = 0;
            double totalMes3 = 0;
            double maximosemestre3 = 0;
            double totalMes4 = 0;     
            double maximosemestre4 = 0;
                        
            double total11 = 0;
            double total12 = 0;
            double total1E = 0;
            double totalSemestre1 = 0;
            
            double total21 = 0;
            double total22 = 0;
            double total2E = 0;
            double totalSemestre2 = 0;
            
            double total31 = 0;
            double total32 = 0;
            double total3E = 0;
            double totalSemestre3 = 0;
            
            double total41 = 0;
            double total42 = 0;
            double total4E = 0;
            double totalSemestre4 = 0;
            
            double totalTotal = 0;          
            
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();
            
            if (solicitudIterator.hasNext()){
                IItem solicitud = (IItem)solicitudIterator.next();
                
                nifBenef = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                numexpConvocatoria = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                
                //Buscamos todas las solicitudes que tiene este beneficiario en esta convocatoria
                IItemCollection solicitudesBenefCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, ConstantesString.WHERE + " UPPER(NIF) = UPPER('" +nifBenef+"') AND CONVOCATORIA = '" +numexpConvocatoria+"' ORDER BY SUBSTR(NUMEXP, 5,4)::INT, SUBSTR(NUMEXP, 10)::INT DESC");
                Iterator<?> solicitudesBenefIterator = solicitudesBenefCollection.iterator();
                
                //Comprobamos si han sido rechazadas, si no, seguimos con ellas
                boolean fin = false;
                
                while(solicitudesBenefIterator.hasNext() && !fin){
                    IItem solicitudBenf = (IItem) solicitudesBenefIterator.next();
                    String numexpSolicitudesBenef = SubvencionesUtils.getString(solicitudBenf, "NUMEXP");
                    
                    String estado = ExpedientesUtil.getEstadoAdm(cct, numexpSolicitudesBenef);
                    //Cojemos el último no rechazado y que no sea la convocatoria
                    if(!ExpedientesUtil.EstadoADM.RC.equals(estado) && ExpedientesUtil.esMayor(numexp, numexpSolicitudesBenef)){
                        fin = true;
                        IItemCollection expedienteSolicitudAteriorCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, ConstantesString.WHERE + " NUMEXP = '" +numexpSolicitudesBenef+"' ORDER BY SUBSTR(NUMEXP, 5,4)::INT, SUBSTR(NUMEXP, 10)::INT DESC");                        
                        Iterator<?> expedienteSolicitudAnteriorIterator = expedienteSolicitudAteriorCollection.iterator();
                        
                        if(expedienteSolicitudAnteriorIterator.hasNext()){
                            
                            IItem expedienteSolicitudAnterior =  (IItem)expedienteSolicitudAnteriorIterator.next();    
                            numMiembros1 = SubvencionesUtils.getInt(expedienteSolicitudAnterior, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR);
                            menores3anios1 = SubvencionesUtils.getString(expedienteSolicitudAnterior, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS);
                            
                            if(StringUtils.isEmpty(menores3anios1)){
                                menores3anios1 = "NO";
                            }
                        }
                        
                        IItemCollection expedienteCantidadesAnteriorCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexpSolicitudesBenef);
                        Iterator<?> expedienteCantidadesAnteriorIterator = expedienteCantidadesAnteriorCollection.iterator();                        
                        IItem expedienteCantidadesAnterior = null;
                        if(expedienteCantidadesAnteriorIterator.hasNext()){
                            expedienteCantidadesAnterior = (IItem)expedienteCantidadesAnteriorIterator.next();
                        } else{
                            expedienteCantidadesAnterior = entitiesAPI.createEntity(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexpSolicitudesBenef);
                        }

                        porMiembros1 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS);
                        porMenores1 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES);
                        totalMes1 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES);
                        maximosemestre1 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE);
                        propuesta11 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA11);
                        propuesta12 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA12);
                        propuesta1Exc = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA13);
                        total11 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL11);
                        total12 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL12);
                        totalSemestre1 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO);
                        total1E = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE1);
                        countAyuda11 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA1);
                        countAyuda1E = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC1);
                        
                        propuesta21 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA21);
                        propuesta22 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA22);
                        propuesta2Exc = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA23);
                        total21 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL21);
                        total22 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL22);
                        totalSemestre2 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO2);
                        porMiembros2 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS2);
                        porMenores2 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES2);
                        totalMes2 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES2);
                        maximosemestre2 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE2);
                        total2E = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE2);
                        countAyuda21 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA2);
                        countAyuda2E = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC2);

                        porMiembros3 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS3);
                        porMenores3 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES3);
                        totalMes3 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES3);
                        maximosemestre3 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE3);
                        propuesta31 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA31);
                        propuesta32 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA32);
                        propuesta3Exc = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA33);
                        total31 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL31);
                        total32 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL32);
                        total3E = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE3);
                        totalSemestre3 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO3);
                        countAyuda31 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA3);
                        countAyuda3E = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC3);

                        porMiembros4 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS4);
                        porMenores4 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES4);
                        totalMes4 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES4);
                        maximosemestre4 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE4);
                        propuesta41 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA41);
                        propuesta42 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA42);
                        propuesta4Exc = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA43);
                        total41 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL41);
                        total42 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL42);
                        total4E = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE4);
                        totalSemestre4 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO4);
                        countAyuda41 = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA4);
                        countAyuda4E = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC4);

                        totalTotal = SubvencionesUtils.getDouble(expedienteCantidadesAnterior, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALTOTAL);
                    }
                }
                String trimestre = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.TRIMESTRE);
                String tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                
                //Miramos si han cambiado los datos de la solicitud, si no han cambiado nos quedamos con los que había
                if(StringUtils.isNotEmpty(SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR))){
                    numMiembros1 = SubvencionesUtils.getInt(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR);
                } else {
                    solicitud.set(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR, numMiembros1);
                    solicitud.store(cct);
                }
                
                //Miramos si han cambiado los datos de la solicitud, si no han cambiado nos quedamos con los que había
                if(StringUtils.isNotEmpty(SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS))){
                    menores3anios1 = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS);
                    numMenores = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMMENORES);
                } else {
                    solicitud.set(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS, menores3anios1);
                    solicitud.set(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMMENORES, numMenores);
                    solicitud.store(cct);
                }
                
                //[Manu Ticket #1028] * INICIO - SIGEM Modificar cantidades del plan de emergencia.                
                if(numMiembros1 == 1){
                    porMiembros = 90;
                } else if(numMiembros1 == 2){
                    porMiembros = 120;
                } else if(numMiembros1 == 3 || numMiembros1 == 4){
                    porMiembros = 150;
                } else if(numMiembros1 == 5 || numMiembros1 == 6){
                    porMiembros = 180;
                } else if(numMiembros1 == 7 || numMiembros1 == 8){
                    porMiembros = 210;                
                } else{
                    porMiembros = 240;
                }
                //[Manu Ticket #1028] * FIN - SIGEM Modificar cantidades del plan de emergencia.                
                
                if(null != menores3anios1 && "SI".equalsIgnoreCase(menores3anios1) || "SÍ".equalsIgnoreCase(menores3anios1)){
                    if(numMenores != null && "1".equalsIgnoreCase(numMenores)){
                        porMenores = 30;
                    } else if(numMenores != null && "2".equalsIgnoreCase(numMenores)){
                        porMenores = 60;
                    } else{
                        porMenores = 0;                    
                    }
                }
                
                if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                    porMiembros1 = porMiembros;
                    porMenores1 = porMenores;
                    totalMes1 = porMiembros1 + porMenores1;
                    maximosemestre1 = totalMes1*2;
                    
                    if(ConstantesPlanEmergencia.EXCEPCIONAL.equals(tipoAyuda)){
                        propuesta1Exc += SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                        countAyuda1E++;
                    } else if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                        if(propuesta11 == 0){
                            propuesta11 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                            
                            if(propuesta11 <= maximosemestre1){
                                total11 = propuesta11;
                            } else {
                                total11 = maximosemestre1;
                            }
                            total12 = 0;
                            
                        } else if(propuesta12 == 0){
                            propuesta12 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                            
                            double resto = maximosemestre1 - total11;
                            if(propuesta12 <= resto){
                                total12 = propuesta12;
                            } else{
                                total12 = resto;
                            }
                        }
                        countAyuda11++;
                    }                    
                    
                    total1E = propuesta1Exc;                
                    totalSemestre1 = total11 + total12;
                    
                } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                    porMiembros2 = porMiembros;
                    porMenores2 = porMenores;
                                    
                    totalMes2 = porMiembros2 + porMenores2;
                    maximosemestre2 = totalMes2*2;
                    
                    if(ConstantesPlanEmergencia.EXCEPCIONAL.equals(tipoAyuda)){
                        propuesta2Exc += SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                        countAyuda2E++;
                    } else if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                        if(propuesta21 == 0){
                            propuesta21 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                            
                            if(propuesta21 <= maximosemestre2){
                                total21 = propuesta21;
                            } else{
                                total21 = maximosemestre2;
                            }
                            total22 = 0;
                        } else if(propuesta22 == 0){
                            propuesta22 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                            double resto = maximosemestre2 - total21;
                            
                            if(propuesta22 <= resto){
                                total22 = propuesta22;
                            } else{
                                total22 = resto;
                            }
                        }
                        countAyuda21++;
                    }                     
                    
                    total2E = propuesta2Exc;                
                    totalSemestre2 = total21 + total22;
                } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                    porMiembros3 = porMiembros;
                    porMenores3 = porMenores;
                    
                    totalMes3 = porMiembros3 + porMenores3;
                    maximosemestre3 = totalMes3*2;

                    if(ConstantesPlanEmergencia.EXCEPCIONAL.equals(tipoAyuda)){
                        propuesta3Exc += SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                        countAyuda3E++;
                    } else if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                        if(propuesta31 == 0){
                            propuesta31 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                            
                            if(propuesta31 <= maximosemestre3){
                                total31 = propuesta31;
                            } else{
                                total31 = maximosemestre3;
                            }
                            total32 = 0;
                        } else if(propuesta32 == 0){
                            propuesta32 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                            double resto = maximosemestre3 - total31;
                            
                            if(propuesta32 <= resto){
                                total32 = propuesta32;
                            } else{
                                total32 = resto;
                            }
                        }
                        countAyuda31++;
                    }   
                    total3E = propuesta3Exc;                
                    totalSemestre3 = total31 + total32;
                } else{
                    porMiembros4 = porMiembros;
                    porMenores4 = porMenores;
                    
                    totalMes4 = porMiembros4 + porMenores4;
                    maximosemestre4 = totalMes4*2;
                    
                    if(ConstantesPlanEmergencia.EXCEPCIONAL.equals(tipoAyuda)){
                        propuesta4Exc += SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                        countAyuda4E++;
                    } else if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                        if(propuesta41 == 0){
                            propuesta41 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                            if(propuesta41 <= maximosemestre4){
                                total41 = propuesta41;
                            } else{
                                total41 = maximosemestre4;
                            }
                            total42 = 0;
                        } else if(propuesta42 == 0){
                            propuesta42 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                            double resto = maximosemestre4 - total41;
                            if(propuesta42 <= resto){
                                total42 = propuesta42;
                            } else{
                                total42 = resto;
                            }
                        }
                        countAyuda41++;
                    }
                    
                    total4E = propuesta4Exc;                
                    totalSemestre4 = total41 + total42;     
                }
                
                IItemCollection cantidadesCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexp);
                Iterator<?> cantidadesIterator = cantidadesCollection.iterator();
                
                IItem cantidades = null;
                if(cantidadesIterator.hasNext()){
                    cantidades = (IItem) cantidadesIterator.next();
                } else{
                    cantidades = entitiesAPI.createEntity(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexp);
                }
                
                //Primer Trimestre
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS, redondear(porMiembros1));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES, redondear(porMenores1));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES, redondear(totalMes1));                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE, redondear(maximosemestre1));                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA1, redondear(countAyuda11));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC1, redondear(countAyuda1E));
                                           
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA11, redondear(propuesta11));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA12, redondear(propuesta12));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA13, redondear(propuesta1Exc));
                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL11, redondear(total11));                                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL12, redondear(total12));                                     
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE1, redondear(total1E));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO, redondear(totalSemestre1));
                
                //Segundo Trimestre
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS2, redondear(porMiembros2));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES2, redondear(porMenores2));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES2, redondear(totalMes2));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE2, redondear(maximosemestre2));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA2, redondear(countAyuda21));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC2, redondear(countAyuda2E));
                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA21, redondear(propuesta21));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA22, redondear(propuesta22));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA23, redondear(propuesta2Exc));
                                                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL21, redondear(total21));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL22, redondear(total22));
                cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2, redondear(total2E));
                cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2,redondear(totalSemestre2));
                
                //Tercer Trimestre
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS3, redondear(porMiembros3));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES3, redondear(porMenores3));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES3, redondear(totalMes3));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE3, redondear(maximosemestre3));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA3, redondear(countAyuda31));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC3, redondear(countAyuda3E));
                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA31, redondear(propuesta31));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA32, redondear(propuesta32));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA33, redondear(propuesta3Exc));
                                                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL31, redondear(total31));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL32, redondear(total32));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE3, redondear(total3E));       
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO3, redondear(totalSemestre3));
                                
                //Cuarto Trimestre
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS4, redondear(porMiembros4));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES4, redondear(porMenores4));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES4, redondear(totalMes4));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE4, redondear(maximosemestre4));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA4, redondear(countAyuda41));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC4, redondear(countAyuda4E));
                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA41, redondear(propuesta41));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA42, redondear(propuesta42));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA43, redondear(propuesta4Exc));
                                                
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL41, redondear(total41));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL42, redondear(total42));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE4, redondear(total4E));
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO4, redondear(totalSemestre4));
                
                totalTotal = totalSemestre1 + totalSemestre2 + totalSemestre3 + totalSemestre4 + total1E + total2E + total3E + total4E;
                cantidades.set(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALTOTAL, redondear(totalTotal));
                
                cantidades.store(cct);

                //Creamos el control de vales
                
                IItemCollection valesCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexp);
                Iterator<?> valesIterator = valesCollection.iterator();
                
                IItem vales = null;
                if(valesIterator.hasNext()){
                    vales = (IItem) valesIterator.next();
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1, vales.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1));
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2, vales.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2));
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3, vales.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3));
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4, vales.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4));
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS, vales.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS));
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS, vales.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS));
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS, vales.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS));
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS, vales.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS));
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1, totalSemestre1/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2, totalSemestre2/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3, totalSemestre3/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4, totalSemestre4/30);
                } else{
                    vales = entitiesAPI.createEntity(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexp);

                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS, 0);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1, totalSemestre1/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2, totalSemestre2/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3, totalSemestre3/30);
                    vales.set(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4, totalSemestre4/30);
                }            
                
                vales.store(cct);
                                
                if(comparaDosSolicitudes(countAyuda11, ConstantesPlanEmergencia.PRIMER_TRIMESTRE, trimestre) ||
                        comparaDosSolicitudes(countAyuda21, ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE, trimestre)|| 
                        comparaDosSolicitudes(countAyuda31, ConstantesPlanEmergencia.TERCER_TRIMESTRE, trimestre) ||                        
                        comparaDosSolicitudes(countAyuda41, ConstantesPlanEmergencia.CUARTO_TRIMESTRE, trimestre)){
                    
                    SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_2_SOL);
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " " + e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " " + e.getMessage(), e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }
    
    private boolean comparaDosSolicitudes(double numAyuda, String trimestreControl, String trimestre){
        return numAyuda > 2 && trimestreControl.equals(trimestre);
    }

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }
    
    public double redondear(double numero){
        return Math.rint(numero*100)/100;
    }
}

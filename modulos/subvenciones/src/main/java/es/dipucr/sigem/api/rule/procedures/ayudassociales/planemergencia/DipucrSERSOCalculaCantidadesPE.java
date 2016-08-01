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

public class DipucrSERSOCalculaCantidadesPE implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSOCalculaCantidadesPE.class);

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
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
                nifBenef = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                numexpConvocatoria = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                //Buscamos todas las solicitudes que tiene este beneficiario en esta convocatoria
                IItemCollection solicitudesBenefCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, "WHERE UPPER(NIF) = UPPER('" +nifBenef+"') AND CONVOCATORIA = '" +numexpConvocatoria+"' ORDER BY SUBSTR(NUMEXP, 5,4)::INT, SUBSTR(NUMEXP, 10)::INT DESC");
                Iterator<?> solicitudesBenefIterator = solicitudesBenefCollection.iterator();
                
                //Comprobamos si han sido rechazadas, si no, seguimos con ellas
                boolean fin = false;
                while(solicitudesBenefIterator.hasNext() && !fin){
                    IItem solicitudBenf = (IItem) solicitudesBenefIterator.next();
                    String numexpSolicitudesBenef = solicitudBenf.getString("NUMEXP");
                    
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexpSolicitudesBenef);
                    if(expediente != null){
                        String estado = expediente.getString("ESTADOADM");
                        //Cojemos el último no rechazado y que no sea la convocatoria
                        if(!"RC".equals(estado) && ExpedientesUtil.esMayor(numexp, numexpSolicitudesBenef)){
                            fin = true;
                            IItemCollection expedienteSolicitudAteriorCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, "WHERE NUMEXP='" +numexpSolicitudesBenef+"' ORDER BY SUBSTR(NUMEXP, 5,4)::INT, SUBSTR(NUMEXP, 10)::INT DESC");
                            IItemCollection expedienteCantidadesAnteriorCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexpSolicitudesBenef);
                            Iterator<?> expedienteSolicitudAnteriorIterator = expedienteSolicitudAteriorCollection.iterator();
                            Iterator<?> expedienteCantidadesAnteriorIterator = expedienteCantidadesAnteriorCollection.iterator();
                            
                            if(expedienteSolicitudAnteriorIterator.hasNext()){
                                
                                IItem expedienteSolicitudAnterior = null;
                                try{
                                    expedienteSolicitudAnterior = (IItem)expedienteSolicitudAnteriorIterator.next();    
                                } catch(Exception e){
                                    LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                }
                                IItem expedienteCantidadesAnterior = null;
                                try{
                                    if(expedienteCantidadesAnteriorIterator.hasNext()){
                                        expedienteCantidadesAnterior = (IItem)expedienteCantidadesAnteriorIterator.next();
                                    } else{
                                        expedienteCantidadesAnterior = entitiesAPI.createEntity(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexpSolicitudesBenef);
                                    }
                                } catch(Exception e){
                                    LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                }
                                if(expedienteCantidadesAnterior != null && expedienteSolicitudAnterior!= null){
                                    try{
                                        numMiembros1 = Integer.parseInt(expedienteSolicitudAnterior.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR).trim());
                                    } catch(Exception e){
                                        numMiembros1 = 0;
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                    }                            
                                    try{
                                        menores3anios1 = expedienteSolicitudAnterior.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS);
                                        numMenores = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMMENORES);
                                    } catch(Exception e){
                                        menores3anios1 = "NO";
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                    }                                                                                    
                                    try{
                                        porMiembros1 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS).trim());
                                        porMenores1 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES).trim());
                                        totalMes1 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES).trim());
                                        maximosemestre1 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE).trim());
                                        propuesta11 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA11).trim());
                                        propuesta12 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA12).trim());
                                        propuesta1Exc = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA13).trim());
                                        total11 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL11).trim());
                                        total12 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL12).trim());
                                        totalSemestre1 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO).trim());
                                        total1E = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE1).trim());
                                        countAyuda11 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA1).trim());
                                        countAyuda1E = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC1).trim());
                                    } catch(Exception e){
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                    }
                                    try{
                                        propuesta21 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA21).trim());
                                        propuesta22 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA22).trim());
                                        propuesta2Exc = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA23).trim());
                                        total21 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL21).trim());
                                        total22 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL22).trim());
                                        totalSemestre2 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO2).trim());
                                        porMiembros2 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS2).trim());
                                        porMenores2 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES2).trim());
                                        totalMes2 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES2).trim());
                                        maximosemestre2 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE2).trim());
                                        total2E = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE2).trim());
                                        countAyuda21 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA2).trim());
                                        countAyuda2E = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC2).trim());
                                    } catch(Exception e){
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                    }
                                    try{
                                        porMiembros3 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS3).trim());
                                        porMenores3 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES3).trim());
                                        totalMes3 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES3).trim());
                                        maximosemestre3 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE3).trim());
                                        propuesta31 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA31).trim());
                                        propuesta32 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA32).trim());
                                        propuesta3Exc = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA33).trim());
                                        total31 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL31).trim());
                                        total32 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL32).trim());
                                        total3E = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE3).trim());
                                        totalSemestre3 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO3).trim());
                                        countAyuda31 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA3).trim());
                                        countAyuda3E = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC3).trim());
                                    } catch(Exception e){
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                    }
                                    try{
                                        porMiembros4 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMIEMBROS4).trim());
                                        porMenores4 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PORMENORES4).trim());
                                        totalMes4 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALMES4).trim());
                                        maximosemestre4 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE4).trim());
                                        propuesta41 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA41).trim());
                                        propuesta42 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA42).trim());
                                        propuesta4Exc = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.PROPUESTA43).trim());
                                        total41 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL41).trim());
                                        total42 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTAL42).trim());
                                        total4E = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALSEMESTRE4).trim());
                                        totalSemestre4 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALCONCEDIDO4).trim());
                                        countAyuda41 = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA4).trim());
                                        countAyuda4E = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMEXC4).trim());
                                    } catch(Exception e){
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                    }
                                    try{
                                        totalTotal = Double.parseDouble(expedienteCantidadesAnterior.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.TOTALTOTAL).trim());
                                    } catch(Exception e){
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                                    }
                                }
                            }
                        }
                    }
                }//While
                String trimestre = solicitud.getString(ConstantesPlanEmergencia.TRIMESTRE);
                String tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                //Miramos si han cambiado los datos de la solicitud, si no han cambiado nos quedamos con los que había
                if(StringUtils.isNotEmpty(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR))){
                    try{
                        numMiembros1 = Integer.parseInt(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR).trim());                        
                    } catch(Exception e){    
                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                    }                    
                } else{
                    try{
                        solicitud.set(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR, numMiembros1);
                        solicitud.store(cct);
                    } catch(Exception e){
                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                    }
                }
                //Miramos si han cambiado los datos de la solicitud, si no han cambiado nos quedamos con los que había
                if(StringUtils.isNotEmpty(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS))){
                    try{                    
                        menores3anios1 = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS);
                        numMenores = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMMENORES);
                    } catch(Exception e){    
                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                    }                    
                } else{
                    try{
                        solicitud.set(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS, menores3anios1);
                        solicitud.set(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMMENORES, numMenores);
                        solicitud.store(cct);
                    } catch(Exception e){
                        LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                    }
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
                        try{
                            propuesta1Exc += Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                        } catch(Exception e){
                            LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                        }
                        countAyuda1E++;
                    } else if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                        try{
                            if(propuesta11 == 0){
                                propuesta11 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());        
                                if(propuesta11 <= maximosemestre1){
                                    total11 = propuesta11;
                                } else {
                                    total11 = maximosemestre1;
                                }
                                total12 = 0;
                            } else if(propuesta12 == 0){
                                propuesta12 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());        
                                double resto = maximosemestre1 - total11;
                                if(propuesta12 <= resto){
                                    total12 = propuesta12;
                                } else{
                                    total12 = resto;
                                }
                            }
                        } catch(Exception e){    
                            LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
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
                        try{
                            propuesta2Exc += Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                        } catch(Exception e){    
                            LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                        }
                        countAyuda2E++;
                    } else if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                        try{
                            if(propuesta21 == 0){                            
                                propuesta21 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                                if(propuesta21 <= maximosemestre2){
                                    total21 = propuesta21;
                                } else{
                                    total21 = maximosemestre2;
                                }
                                total22 = 0;
                            } else if(propuesta22 == 0){
                                propuesta22 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                                double resto = maximosemestre2 - total21;
                                if(propuesta22 <= resto){
                                    total22 = propuesta22;
                                } else{
                                    total22 = resto;
                                }
                            }
                        } catch(Exception e){
                            LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
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
                        try{
                            propuesta3Exc += Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                        } catch(Exception e){
                            LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                        }
                        countAyuda3E++;
                    } else if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                        try{
                            if(propuesta31 == 0){
                                propuesta31 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                                if(propuesta31 <= maximosemestre3){
                                    total31 = propuesta31;
                                } else{
                                    total31 = maximosemestre3;
                                }
                                total32 = 0;
                            } else if(propuesta32 == 0){
                                propuesta32 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                                double resto = maximosemestre3 - total31;
                                if(propuesta32 <= resto){
                                    total32 = propuesta32;
                                } else{
                                    total32 = resto;
                                }
                            }
                        } catch(Exception e){    
                            LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
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
                        try{
                            propuesta4Exc += Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                        } catch(Exception e){
                            LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                        }
                        countAyuda4E++;
                    } else if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                        try{
                            if(propuesta41 == 0){
                                propuesta41 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                                if(propuesta41 <= maximosemestre4){
                                    total41 = propuesta41;
                                } else{
                                    total41 = maximosemestre4;
                                }
                                total42 = 0;
                            } else if(propuesta42 == 0){
                                propuesta42 = Double.parseDouble(solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE).trim());
                                double resto = maximosemestre4 - total41;
                                if(propuesta42 <= resto){
                                    total42 = propuesta42;
                                } else{
                                    total42 = resto;
                                }
                            }
                        } catch(Exception e){    
                            LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(),e);
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
                                
                //if(mas2Solicitudes){
                if(comparaDosSolicitudes(countAyuda11, ConstantesPlanEmergencia.PRIMER_TRIMESTRE, trimestre) ||
                        comparaDosSolicitudes(countAyuda21, ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE, trimestre)|| 
                        comparaDosSolicitudes(countAyuda31, ConstantesPlanEmergencia.TERCER_TRIMESTRE, trimestre) ||                        
                        comparaDosSolicitudes(countAyuda41, ConstantesPlanEmergencia.CUARTO_TRIMESTRE, trimestre)){
                    IItem expediente = ExpedientesUtil.getExpediente(cct,  numexp);

                    if (expediente != null){
                        String asunto = expediente.getString("ASUNTO");
                        if(!asunto.contains(" - AVISO MÁS DE 2 SOLICITUDES EN UN TRIMESTRE")){
                            asunto += " - AVISO MÁS DE 2 SOLICITUDES EN UN TRIMESTRE";
                            expediente.set("ASUNTO", asunto);
                        }
                        expediente.store(cct);                        
                    }
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

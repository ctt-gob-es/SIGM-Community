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

import com.ibm.icu.text.DecimalFormat;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrSERSODatosSolPlanEmer implements IRule{
    private static final Logger LOGGER = Logger
            .getLogger(DipucrSERSODatosSolPlanEmer.class);

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
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            StringBuilder listado = new StringBuilder();  //Listado de liquidaciones 
            String numexp = rulectx.getNumExp();
            ArrayList<String> expedientesResolucion = new ArrayList<String>();
            
            //Tenemos que devolver la siguiente cadena:
            /**    
                1.LOCALIDAD
                Trabajador/a Social:
                
                Beneficiario/a:                     NIF:
                Número miembros de la unidad familiar:
                Nº cheques: 
                Importe cheques:                
             */
            int contadorBeneficiario = 1;
            String ciudad = "";
            String descripcionCiudad = "";
            int countCiudad = 0;
            String beneficiario = "";
            String nifBeneficiario = "";
            String trabajadorSocial = "";
            String nifTrabajadorSocial = "";
            String numMiembrosFamilia = "";
                        
            String tipoAyuda = "";
            String trimestre = "";
            
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
            
            //Recuperamos los expedientes relacionados
            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
            IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt = expRelCol.iterator();                  
            if(expRelIt.hasNext()){
                while (expRelIt.hasNext()){
                    IItem expRel = (IItem)expRelIt.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    String numexpHijo = expRel.getString("NUMEXP_HIJO");
                    
                    IItem expHijo = ExpedientesUtil.getExpediente(cct,  numexpHijo); 
                    if(expHijo != null && "RS".equals(expHijo.get("ESTADOADM"))){
                        expedientesResolucion.add(numexpHijo);
                    }                    
                }
            }    
            //Ya tenemos los expedientes que vamos a decretar en esta iteración
            if(!expedientesResolucion.isEmpty()){
                strQuery = "WHERE NUMEXP IN (";
                Iterator<String> expedientesResolucionIt = expedientesResolucion.listIterator();
                while(expedientesResolucionIt.hasNext()){
                    strQuery += "'" +expedientesResolucionIt.next()+"',";
                }
                strQuery = strQuery.substring(0,strQuery.length()-1);
                strQuery += ")";
                 
                //Recuperamos las solicitudes
                
                IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery);
                Iterator<?> expSolicitudesCiudadIt = expSolicitudesCiudadCol.iterator();
                String strQuery2 = "";
                strQuery2 = "WHERE VALOR IN (";
                while(expSolicitudesCiudadIt.hasNext()){
                    strQuery2 +="'" +((IItem) expSolicitudesCiudadIt.next()).getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD)+"',";                                                        
                }
                strQuery2 = strQuery2.substring(0,strQuery2.length()-1);
                strQuery2 += ") ORDER BY SUSTITUTO";
                
                IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities("REC_VLDTBL_MUNICIPIOS", strQuery2);
                Iterator<?> ciudadOrdenIt = ciudadOrdenCol.iterator();
                while(ciudadOrdenIt.hasNext()){
                    
                    IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery+" AND CIUDAD = '" +((IItem)ciudadOrdenIt.next()).getString("VALOR")+"' ORDER BY TIPOAYUDA, NOMBRESOLICITANTE, NOMBRE");
                    Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                    while (expSolicitudesIt.hasNext()){
                        
                        beneficiario = "";
                        nifBeneficiario = "";
                        trimestre = "";           
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
                        trimestre = expSolicitud.getString(ConstantesPlanEmergencia.TRIMESTRE);
                        
                        //Recuperamos el municipio si es distinto
                        if(!ciudad.equals(expSolicitud.get(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))){
                            ciudad = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                            
                            tipoAyuda = "";
                            trabajadorSocial = "";
                            IItemCollection ciudadCol = entitiesAPI.queryEntities("REC_VLDTBL_MUNICIPIOS", "WHERE VALOR='" +expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD)+"'");
                            Iterator<?> ciudadIt = ciudadCol.iterator();
                            if(ciudadIt.hasNext()){
                                countCiudad++;
                                descripcionCiudad = ((IItem)ciudadIt.next()).getString("SUSTITUTO");
                            }
                            listado.append("\n");
                            listado.append(countCiudad+". " +descripcionCiudad+"\n");
                        }
                        if(!tipoAyuda.equals(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA))){
                            trabajadorSocial = "";
                            tipoAyuda = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);                            
                        }
                        if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                            if(!trabajadorSocial.equals(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))){
                                trabajadorSocial = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);
                                nifTrabajadorSocial = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DOCUMENTOIDENTIDAD);
                                listado.append("\n");
                                listado.append("  Trabajador/a Social: " +trabajadorSocial+"\n");
                            }
                            listado.append("\n");
                            
                            beneficiario = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                            nifBeneficiario = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                            listado.append("\t" + contadorBeneficiario + " - Beneficiario/a: " +beneficiario+"\t\tNIF: " +nifBeneficiario+"\n");                    
                            contadorBeneficiario++;
                            IItemCollection numChequesCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                            Iterator<?> numChequesIt = numChequesCol.iterator();
                            if(numChequesIt.hasNext()){   
                                IItem numCheq = (IItem)numChequesIt.next(); 
                                numCheques1 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1);
                                try{
                                    semestreImpresos1 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                                } catch(Exception e){
                                    LOGGER.debug("El campo SEEMSTRE1IMPRESOS es nulo, vacío o no numérico. " + e.getMessage(), e);
                                    semestreImpresos1 = 0;
                                }
                                importeCheques1 = (numCheques1 - semestreImpresos1) * 30;
                                
                                numCheques2 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2);
                                try{
                                    semestreImpresos2 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                                } catch(Exception e){
                                    LOGGER.debug("El campo SEEMSTRE2IMPRESOS es nulo, vacío o no numérico. " + e.getMessage(), e);
                                    semestreImpresos2 = 0;
                                }
                                importeCheques2 = (numCheques2 - semestreImpresos2) * 30;
                                
                                numCheques3 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3);
                                try{
                                    semestreImpresos3 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                                } catch(Exception e){
                                    LOGGER.debug("El campo SEEMSTRE3IMPRESOS es nulo, vacío o no numérico. " + e.getMessage(), e);
                                    semestreImpresos3 = 0;
                                }
                                importeCheques3 = (numCheques3 - semestreImpresos3) * 30;
                                
                                numCheques4 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4);
                                try{
                                    semestreImpresos4 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                                } catch(Exception e){
                                    LOGGER.debug("El campo SEEMSTRE4IMPRESOS es nulo, vacío o no numérico. " + e.getMessage(), e);
                                    semestreImpresos4 = 0;
                                }
                                importeCheques4 = (numCheques4 - semestreImpresos4) * 30;
                            }
                            
                            if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                                importeCheques = importeCheques1;
                            } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                                importeCheques = importeCheques2;
                            } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                                importeCheques = importeCheques3;     
                            } else{
                                importeCheques = importeCheques4;
                            }
                            
                            numMiembrosFamilia = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR);
                            
                            listado.append("\tNúmero de miembros de la unidad familiar: " +numMiembrosFamilia+"\n");
                            if("SI".equals(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS))){
                                listado.append("\tMenor: SÍ\n");
                            }
                            listado.append("\tNº cheques: " +(importeCheques/30)+"\n");
                            listado.append("\tImporte Cheques: " +new DecimalFormat("#,##0.00").format(importeCheques)+" €\n");
                            total += importeCheques;                            
                        }
                        
                        //Para cada expediente insertamos al trabajador/a como interesado
                        //Comprobamos que no esté                
                        IItemCollection nuevoParticipanteCol = ParticipantesUtil.getParticipantes( cct, numexp, "NDOC='" +nifTrabajadorSocial+"'", "");
                        Iterator<?> nuevoParticipanteIt = nuevoParticipanteCol.iterator();
                        if(!nuevoParticipanteIt.hasNext()){

                            IItem nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, numexp);
                            
                            nuevoParticipante.set("ROL", "INT");
                            nuevoParticipante.set("TIPO_PERSONA", "F");
                            nuevoParticipante.set("NDOC", nifTrabajadorSocial);
                            nuevoParticipante.set("NOMBRE", trabajadorSocial);                
                            nuevoParticipante.set("TIPO_DIRECCION", "T");                
                            try{
                                nuevoParticipante.store(cct);
                            } catch(Exception e){
                                LOGGER.error(ConstantesString.LOGGER_ERROR + ". " + e.getMessage(), e);
                            }
                        }
                    }
                }            
            }
            
            //Comprobamos que no nos hayamos pasado de los 500000
            //Recuperamos las cantidades del ayuntamiento en cuestión
            IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, " WHERE LOCALIDAD = '999' AND NUMEXPCONVOCATORIA = '" +numexp+"'");
            Iterator<?> cantidadesIt = cantidadesCol.iterator();
            if(cantidadesIt.hasNext()){                        
                IItem cantidades = (IItem)cantidadesIt.next();
                double primerTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.PRIMERTRIMESTRE));
                double segundoTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.SEGUNDOTRIMESTRE)); 
                double tercerTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TERCERTRIMESTRE));
                double cuartoTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.CUARTOTRIMESTRE));
                
                double trimestral = cantidades.getDouble(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TRIMESTRAL);
                
                if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                    primerTrim += total;
                } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                    segundoTrim += total;
                } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                    tercerTrim += total;
                } else{
                    cuartoTrim += total;
                }
                boolean supera = false;
                if(primerTrim > trimestral){
                    supera = true;
                    listado = new StringBuilder();
                    listado.append("\n\tSe ha superado el máximo trimestral de " +trimestral+" €\n");
                    listado.append("\tEl total trimestral suma " +primerTrim+" €\n");
                }
                if(segundoTrim > trimestral){
                    supera = true;
                    listado = new StringBuilder();
                    listado.append("\n\tSe ha superado el máximo trimestral de " +trimestral+" €\n");
                    listado.append("\tEl total trimestral suma " +segundoTrim+" €\n");
                }
                if(tercerTrim > trimestral){
                    supera = true;
                    listado = new StringBuilder();
                    listado.append("\n\tSe ha superado el máximo trimestral de " +trimestral+" €\n");
                    listado.append("\tEl total trimestral suma " +tercerTrim+" €\n");
                }
                if(cuartoTrim > trimestral){
                    supera = true;
                    listado = new StringBuilder();
                    listado.append("\n\tSe ha superado el máximo trimestral de " +trimestral+" €\n");
                    listado.append("\tEl total trimestral suma " +cuartoTrim+" €\n");
                }
                if(!supera){
                    cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.PRIMERTRIMESTRE, "" + primerTrim);
                    cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.SEGUNDOTRIMESTRE, "" + segundoTrim); 
                    cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TERCERTRIMESTRE, "" + tercerTrim);
                    cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.CUARTOTRIMESTRE, "" + cuartoTrim);
                    cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTAL, "" + (primerTrim + segundoTrim + tercerTrim + cuartoTrim));
                    cantidades.store(cct);
                }
            }
            listado.append("\n\tEl importe total de la presente propuesta es: " +new DecimalFormat("#,##0.00").format(total)+" €\n");
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return listado.toString();
            
        } catch(Exception e) {
            LOGGER.error("No se han podido recuperar los datos de las solicitudes. " + e.getMessage(),e);
            throw new ISPACRuleException("No se han podido recuperar los datos de las solicitudes. " + e.getMessage(),e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

}
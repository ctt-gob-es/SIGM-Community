package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
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

import com.ibm.icu.text.DecimalFormat;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrSERSODatosSolPlanEmerComedor75 implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSODatosSolPlanEmerComedor75.class);

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
                Número de menores en edad escolar:
                Empresa:
                Nombre menor:
                Importe:
                Mes de Inicio:                
             */
            String ciudad = "";
            String descripcionCiudad = "";
            int countCiudad = 0;
            int contadorBeneficiario = 1;
            
            String beneficiario = "";
            String nifBeneficiario = "";
            String trabajadorSocial = "";
            String nifTrabajadorSocial = "";

            String numMenores = "";

            String concepto = "";
            String empresa1 = "";
            String nombreMenor1 = "";
            String empresa2 = "";
            String nombreMenor2 = "";
            double importe = 0;
            String mesInicio = "";
            
            double total = 0;
            
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
                    if(expHijo != null && "NT25".equals(expHijo.get("ESTADOADM"))){
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
                    
                    IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery+" AND TIPOAYUDA = 'COMEDOR' AND CIUDAD = '" +((IItem)ciudadOrdenIt.next()).getString("VALOR")+"' ORDER BY TIPOAYUDA, NOMBRESOLICITANTE, NOMBRE");
                    Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                    while (expSolicitudesIt.hasNext()){
                                            
                        beneficiario = "";
                        nifBeneficiario = "";                                              
                        numMenores = "";
                        concepto = "BECA COMEDOR";
                        empresa1 = "";
                        nombreMenor1 = "";
                        empresa2 = "";
                        nombreMenor2 = "";
                        importe = 0;
                        mesInicio = "";

                        IItem expSolicitud = (IItem) expSolicitudesIt.next();
                        
                        //Recuperamos el municipio si es distinto
                        if(!ciudad.equals(expSolicitud.get(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))){
                            ciudad = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                            
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
                        
                        if(!trabajadorSocial.equals(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))){
                            trabajadorSocial = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);
                            nifTrabajadorSocial = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DOCUMENTOIDENTIDAD);

                            listado.append("\tTrabajador/a Social: " +trabajadorSocial+"\n");
                        }
                        listado.append("\n");
    
                        beneficiario = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                        nifBeneficiario = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                        
                        listado.append("\t" + contadorBeneficiario + " - Beneficiario/a: " +beneficiario+"\t\tNIF: " +nifBeneficiario+"\n");                    
                        contadorBeneficiario++;
                        
                        IItemCollection concesionItemCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                        Iterator<?> concesionIterator = concesionItemCollection.iterator();
                        
                        if(concesionIterator.hasNext()){
                            IItem concesion = (IItem) concesionIterator.next();
                            try{
                                empresa1 = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.EMPRESACOMEDOR1);
                                nombreMenor1 = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE1);
                                empresa2 = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.EMPRESACOMEDOR2);
                                nombreMenor2 = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE2);                            
                                mesInicio = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.MESINICIO);
                                                        
                                importe = (Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR)) - ((Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR))) * 0.25));
                            } catch(Exception e){
                                LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar el importe de la ayuda de libros del el expediente: " + expSolicitud.getString("NUMEXP") +". " + e.getMessage(), e);
                                throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar el importe de la ayuda de libros del el expediente: " + expSolicitud.getString("NUMEXP") +". " + e.getMessage(), e);
                            }
                        }                        

                        if(StringUtils.isNotBlank(nombreMenor2)){
                            numMenores = "2";
                        } else{
                            numMenores = "1";
                        }
                        
                        listado.append("\tNúmero de menores becados: " + numMenores + "\n");                        
                        listado.append("\tConcepto de la factura: " +concepto+"\n");                            
                        listado.append("\tEmpresa de comedor: " +empresa1+"\n");
                        listado.append("\tNombre del menor: " +nombreMenor1+"\n");
                        if(StringUtils.isNotEmpty(empresa2)){
                            listado.append("\tEmpresa de comedor: " +empresa2+"\n");
                        }
                        if(StringUtils.isNotEmpty(nombreMenor2)){
                            listado.append("\tNombre del menor: " +nombreMenor2+"\n");
                        }
                        listado.append("\tAyuda por importe de: " +new DecimalFormat("#,##0.00").format(Math.rint(importe*100)/100)+" €\n");
                        listado.append("\tMes de inicio: " +mesInicio+"\n");

                        total += (Math.rint(importe*100)/100);
                        
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
                                LOGGER.error(e.getMessage(), e);
                            }
                        }
                    }
                }            
            }
            //Probelmas con el redondeo en la máquina de java
            
            listado.append("\n\tEl importe total de la presente propuesta es: " +new DecimalFormat("#,##0.00").format(Math.rint(total*100)/100)+" €\n");
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
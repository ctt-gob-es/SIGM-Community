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

public class DipucrSERSODatosSolPlanEmerExce implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSODatosSolPlanEmerExce.class);

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
            String ciudad = "";
            String descripcionCiudad = "";
            int countCiudad = 0;
            int contadorBeneficiario = 1;
            
            String beneficiario = "";
            String nifBeneficiario = "";
            String trabajadorSocial = "";
            String nifTrabajadorSocial = "";
                        
            String tipoAyuda = "";
            String descripcionTipoAyuda = "";
            String trimestre = "";

            String nfactura = "";
            String fechaFactura = "";
            String concepto = "";
            String proveedor = "";
            
            double importeExcepcional = 0;

            double importeExcepcional1 = 0;

            double importeExcepcional2 = 0;

            double importeExcepcional3 = 0;

            double importeExcepcional4 = 0;
            
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
                String strQuery2 = "WHERE VALOR IN (";
                while(expSolicitudesCiudadIt.hasNext()){
                    strQuery2 +="'" +((IItem) expSolicitudesCiudadIt.next()).getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD)+"',";                                                        
                }
                strQuery2 = strQuery2.substring(0,strQuery2.length()-1);
                strQuery2 += ") ORDER BY SUSTITUTO";
                
                IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities("REC_VLDTBL_MUNICIPIOS", strQuery2);
                Iterator<?> ciudadOrdenIt = ciudadOrdenCol.iterator();
                while(ciudadOrdenIt.hasNext()){
                    
                    IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery+" AND TIPOAYUDA = 'EXCEPCIONAL' AND CIUDAD = '" +((IItem)ciudadOrdenIt.next()).getString("VALOR")+"' ORDER BY TIPOAYUDA, NOMBRESOLICITANTE, NOMBRE");
                    Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                    while (expSolicitudesIt.hasNext()){
                                            
                        beneficiario = "";
                        nifBeneficiario = "";                      
                        trimestre = "";
                        nfactura = "";
                        fechaFactura = "";
                        concepto = "";
                        proveedor = "";
                        importeExcepcional = 0;
                        importeExcepcional1 = 0;
                        importeExcepcional2 = 0;
                        importeExcepcional3 = 0;
                        importeExcepcional4 = 0;

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
                            descripcionTipoAyuda = expSolicitud.getString("DESCRIPCION_TIPOAYUDA");

                            listado.append("\tTipo de Ayuda: " +descripcionTipoAyuda+"\n");
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
                        
                        IItemCollection concesionExcCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                        Iterator<?> concesionExcIt = concesionExcCol.iterator();
                        if(concesionExcIt.hasNext()){   
                            IItem concesionExc = (IItem)concesionExcIt.next(); 
                            nfactura = concesionExc.getString(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.NFACTURA);
                            fechaFactura = concesionExc.getString(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.FECHAFACTURA);
                            concepto = concesionExc.getString(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.CONCEPTO);
                            proveedor = concesionExc.getString(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.PROVEEDOR);
                        }
                        
                        IItemCollection concesionItemCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                        Iterator<?> concesionIterator = concesionItemCollection.iterator();
                        
                        if(concesionIterator.hasNext()){
                            IItem concesion = (IItem) concesionIterator.next();
                            String cantidad = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1);
                            if(StringUtils.isNotEmpty(cantidad)){
                                try{
                                    importeExcepcional1 = Double.parseDouble(cantidad.trim());
                                } catch(Exception e){
                                    LOGGER.debug("El campo TOTALSEMESTRE1 es nulo, vacío o no numérico. " + e.getMessage(), e);
                                    importeExcepcional1 = Double.parseDouble(cantidad.trim().replace(",","."));
                                }
                            }
                            
                            cantidad = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2);
                            if(StringUtils.isNotEmpty(cantidad)){
                                try{
                                    importeExcepcional2 = Double.parseDouble(cantidad.trim());
                                } catch(Exception e){
                                    LOGGER.debug("El campo TOTALSEMESTRE2 es nulo, vacío o no numérico. " + e.getMessage(), e);
                                    importeExcepcional2 = Double.parseDouble(cantidad.trim().replace(",","."));
                                }
                            }
                            
                            cantidad = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3);
                            if(StringUtils.isNotEmpty(cantidad)){
                                try{
                                    importeExcepcional3 = Double.parseDouble(cantidad.trim());
                                } catch(Exception e){
                                    LOGGER.debug("El campo TOTALSEMESTRE3 es nulo, vacío o no numérico. " + e.getMessage(), e);
                                    importeExcepcional3 = Double.parseDouble(cantidad.trim().replace(",","."));
                                }
                            }
                            
                            cantidad = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4);
                            if(StringUtils.isNotEmpty(cantidad)){
                                try{
                                    importeExcepcional4 = Double.parseDouble(cantidad.trim());
                                } catch(Exception e){
                                    LOGGER.debug("El campo TOTALSEMESTRE4 es nulo, vacío o no numérico. " + e.getMessage(), e);
                                    importeExcepcional4 = Double.parseDouble(cantidad.trim().replace(",","."));
                                }
                            }
                        }
                        
                        if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                            importeExcepcional = importeExcepcional1;
                        } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                            importeExcepcional = importeExcepcional2;
                        } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                            importeExcepcional = importeExcepcional3;
                        } else{
                            importeExcepcional = importeExcepcional4;
                        }

                        listado.append("\tFactura Nº: " +nfactura+"\t\tFecha: " +fechaFactura+"\n");                        
                        listado.append("\tConcepto de la factura: " +concepto+"\n");                            
                        listado.append("\tProveedor: " +proveedor+"\n");
                        listado.append("\tAyuda por importe de: " +new DecimalFormat("#,##0.00").format(importeExcepcional)+" €\n");

                        total += importeExcepcional;
                        
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
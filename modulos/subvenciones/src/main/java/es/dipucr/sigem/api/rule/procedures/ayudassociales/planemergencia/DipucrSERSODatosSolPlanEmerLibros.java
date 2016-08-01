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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ibm.icu.text.DecimalFormat;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrSERSODatosSolPlanEmerLibros implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSODatosSolPlanEmerLibros.class);

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
                Concepto:
                Proveedor:
                Cuantía ayuda:            
             */
            String ciudad = "";
            String descripcionCiudad = "";
            int countCiudad = 0;
            int contadorBeneficiario = 1;
            
            String beneficiario = "";
            String nifBeneficiario = "";
            String trabajadorSocial = "";
            String nifTrabajadorSocial = "";

//            int numMenores = 0;
            String haPagado = "";

//            String concepto = "";
            
            double totallibros = 0;
            double importeMaterial = 0;
            double importeLibros = 0;
            
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
                strQuery = " " + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ".NUMEXP IN (";
                Iterator<?> expedientesResolucionIt = expedientesResolucion.listIterator();
                while(expedientesResolucionIt.hasNext()){
                    strQuery += "'" +expedientesResolucionIt.next()+"',";
                }
                strQuery = strQuery.substring(0,strQuery.length()-1);
                strQuery += ")";
                 
                //Recuperamos las solicitudes
                
                IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, "WHERE " + strQuery);
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
                    Map<String, String> tablas = new HashMap<String, String>();
                    tablas.put(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA);
                    tablas.put(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA);
                    
                    String consulta = " WHERE " + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ".NUMEXP = " + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ".NUMEXP AND " + strQuery+" AND " + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ".TIPOAYUDA = 'LIBROS' AND " + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + "." + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD + " = '" + ((IItem)ciudadOrdenIt.next()).getString("VALOR") + "' ORDER BY " + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + "." + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA + ", NOMBRESOLICITANTE, " + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ".PAGADO, NOMBRE";
                    
                    IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(tablas, consulta);
                    Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                    while (expSolicitudesIt.hasNext()){
                                            
                        beneficiario = "";
                        nifBeneficiario = "";                      
                        haPagado = "NO";
                        
                        totallibros = 0;
                        importeMaterial = 0;
                        importeLibros = 0;

                        IItem expSolicitud = (IItem) expSolicitudesIt.next();
                        
                        LOGGER.warn("El expediente actual es " + expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":NUMEXP"));
                        
                        //Recuperamos el municipio si es distinto
                        if(!ciudad.equals(expSolicitud.get(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))){
                            ciudad = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                            
                            trabajadorSocial = "";
                            IItemCollection ciudadCol = entitiesAPI.queryEntities("REC_VLDTBL_MUNICIPIOS", "WHERE VALOR='" +expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD)+"'");
                            Iterator<?> ciudadIt = ciudadCol.iterator();
                            if(ciudadIt.hasNext()){
                                countCiudad++;
                                descripcionCiudad = ((IItem)ciudadIt.next()).getString("SUSTITUTO");
                            }
                            listado.append("\n");
                            listado.append(countCiudad+". " +descripcionCiudad+"\n");
                        }
                        
                        if(!trabajadorSocial.equals(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))){
                            trabajadorSocial = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);
                            nifTrabajadorSocial = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DOCUMENTOIDENTIDAD);

                            listado.append("\tTrabajador/a Social: " +trabajadorSocial+"\n");
                        }
                        listado.append("\n");
    
                        beneficiario = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                        nifBeneficiario = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                        
                        listado.append("\t" + contadorBeneficiario + " - Beneficiario/a: " +beneficiario+"\t\tNIF: " +nifBeneficiario+"\n");                    
                        contadorBeneficiario++;
                        
                        totallibros = Double.parseDouble(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS));
                        haPagado = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.PAGADO);
                        
                        for (int i = 1; i<9; i++){
                            String material = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.CURSO + i);
                            double dMaterial = 0;
                            if(StringUtils.isNotEmpty(material)){
                                dMaterial = Double.parseDouble(material);
                            }
                            
                            importeMaterial += dMaterial;
                        }
                        
                        importeLibros = totallibros - importeMaterial;

                        listado.append("\tImporte de material escolar: " +new DecimalFormat("#,##0.00").format(Math.rint(importeMaterial*100)/100)+" €, importe de libros: " +new DecimalFormat("#,##0.00").format(Math.rint(importeLibros*100)/100)+" €\n");
                        
                        if(StringUtils.isNotEmpty(haPagado) && "F".equalsIgnoreCase(haPagado.trim())){
                            listado.append("\tLa familia HA PAGADO los libros.\n");
                        } else if(StringUtils.isNotEmpty(haPagado) && "L".equalsIgnoreCase(haPagado.trim())){
                            listado.append("\tLa familia NO HA PAGADO los libros.\n");
                        } else{
                            listado.append("\tError al comprobar si la familia ha pagado o no los libros.\n");
                        }
                            
                        listado.append("\tAYUDA POR IMPORTE DE: " +new DecimalFormat("#,##0.00").format(Math.rint(totallibros*100)/100)+" €\n");

                        total += totallibros;
                        
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
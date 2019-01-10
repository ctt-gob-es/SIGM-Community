package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

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
            IClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            StringBuilder listado = new StringBuilder();
            String numexp = rulectx.getNumExp();
            List<String> expedientesResolucion = new ArrayList<String>();
            
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

            String haPagado = "";

            
            double totallibros = 0;
            double importeMaterial = 0;
            double importeLibros = 0;
            
            double total = 0;
            
            //Recuperamos los expedientes relacionados
            expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS);
            
            //Ya tenemos los expedientes que vamos a decretar en esta iteración
            if(!expedientesResolucion.isEmpty()){

                String strQuery = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " IN " + SubvencionesUtils.getWhereInFormat(expedientesResolucion);               
                IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery);
                
                String strQuery2 = ConstantesString.WHERE + ConstantesSubvenciones.MunicipiosValidationTable.VALOR + " IN " + SubvencionesUtils.getWhereInFormat(expSolicitudesCiudadCol, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD) + ConstantesString.ORDER_BY + ConstantesSubvenciones.MunicipiosValidationTable.SUSTITUTO;                
                IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities(ConstantesSubvenciones.MunicipiosValidationTable.NOMBRE_TABLA, strQuery2);
                Iterator<?> ciudadOrdenIt = ciudadOrdenCol.iterator();
                
                while(ciudadOrdenIt.hasNext()){
                    Map<String, String> tablas = new HashMap<String, String>();
                    tablas.put(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA);
                    tablas.put(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA);
                    
                    String consulta = ConstantesString.WHERE + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ".NUMEXP = " + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ".NUMEXP AND " + strQuery+ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ".TIPOAYUDA = 'LIBROS' AND " + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + "." + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD + " = '" + ((IItem)ciudadOrdenIt.next()).getString("VALOR") + "' ORDER BY " + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + "." + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA + ", NOMBRESOLICITANTE, " + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ".PAGADO, NOMBRE";
                    
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
                        
                        LOGGER.warn("El expediente actual es " + SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":NUMEXP"));
                        
                        //Recuperamos el municipio si es distinto
                        if(!ciudad.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))){
                            ciudad = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                            
                            trabajadorSocial = "";

                            descripcionCiudad = SubvencionesUtils.getMunicipioByValor(cct, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD));
                            if(StringUtils.isNotEmpty(descripcionCiudad)){
                                countCiudad++;
                            }
                            
                            listado.append("\n");
                            listado.append(countCiudad+". " +descripcionCiudad+"\n");
                        }
                        
                        if(!trabajadorSocial.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))){
                            trabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);
                            nifTrabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DOCUMENTOIDENTIDAD);

                            listado.append("\tTrabajador/a Social: " +trabajadorSocial+"\n");
                        }
                        listado.append("\n");
    
                        beneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                        nifBeneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                        
                        listado.append("\t" + contadorBeneficiario + " - Beneficiario/a: " +beneficiario+"\t\tNIF: " +nifBeneficiario+"\n");                    
                        contadorBeneficiario++;
                        
                        totallibros = SubvencionesUtils.getDouble(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS);
                        haPagado = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.PAGADO);
                        
                        for (int i = 1; i<9; i++){
                            double dMaterial = SubvencionesUtils.getDouble(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA + ":" + ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.CURSO + i);
                            
                            importeMaterial += dMaterial;
                        }
                        
                        importeLibros = totallibros - importeMaterial;

                        listado.append("\tImporte de material escolar: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(importeMaterial*100)/100) + " €, importe de libros: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(importeLibros*100)/100) + " €\n");
                        
                        if(StringUtils.isNotEmpty(haPagado) && "F".equalsIgnoreCase(haPagado.trim())){
                            listado.append("\tLa familia HA PAGADO los libros.\n");
                        } else if(StringUtils.isNotEmpty(haPagado) && "L".equalsIgnoreCase(haPagado.trim())){
                            listado.append("\tLa familia NO HA PAGADO los libros.\n");
                        } else{
                            listado.append("\tError al comprobar si la familia ha pagado o no los libros.\n");
                        }
                            
                        listado.append("\tAYUDA POR IMPORTE DE: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(totallibros*100)/100)+" €\n");

                        total += totallibros;
                        
                        
                        SubvencionesUtils.insertaTabajadorSocialComoParticipante(cct, numexp, nifTrabajadorSocial, nifTrabajadorSocial);
                    }
                }            
            }
            //Probelmas con el redondeo en la máquina de java
            
            listado.append("\n\tEl importe total de la presente propuesta es: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(total*100)/100)+" €\n");
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return listado.toString();
            
        } catch(Exception e) {
            LOGGER.error("No se han podido recuperar los datos de las solicitudes. " + e.getMessage(),e);
            throw new ISPACRuleException("No se han podido recuperar los datos de las solicitudes. " + e.getMessage(),e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}
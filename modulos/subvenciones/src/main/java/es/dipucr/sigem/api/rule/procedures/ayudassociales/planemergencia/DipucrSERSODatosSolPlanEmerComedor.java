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
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSODatosSolPlanEmerComedor implements IRule{
    
    public static final Logger LOGGER = Logger.getLogger(DipucrSERSODatosSolPlanEmerComedor.class);
    
    public static final double PORCENTAJE_RESOLUCION = 1;

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
            
            expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.NT25);
               
            //Ya tenemos los expedientes que vamos a decretar en esta iteración
            if(!expedientesResolucion.isEmpty()){

                String strQuery = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " IN " + SubvencionesUtils.getWhereInFormat(expedientesResolucion);               
                IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery);
                
                String strQuery2 = ConstantesString.WHERE + ConstantesSubvenciones.MunicipiosValidationTable.VALOR + " IN " + SubvencionesUtils.getWhereInFormat(expSolicitudesCiudadCol, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD) + ConstantesString.ORDER_BY + ConstantesSubvenciones.MunicipiosValidationTable.SUSTITUTO;                
                IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities(ConstantesSubvenciones.MunicipiosValidationTable.NOMBRE_TABLA, strQuery2);
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
                        if(!ciudad.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))){
                            ciudad = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                            
                            trabajadorSocial = "";

                            descripcionCiudad = SubvencionesUtils.getMunicipioByValor(cct, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD));
                            if(StringUtils.isNotEmpty(descripcionCiudad)){
                                countCiudad++;
                            }
                            
                            listado.append("\n");
                            listado.append(countCiudad+". " +descripcionCiudad+"\n");
                        }
                        
                        if(!trabajadorSocial.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))){
                            trabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);
                            nifTrabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DOCUMENTOIDENTIDAD);

                            listado.append("\tTrabajador/a Social: " +trabajadorSocial+"\n");
                        }
                        listado.append("\n");
    
                        beneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                        nifBeneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                        
                        listado.append("\t" + contadorBeneficiario + " - Beneficiario/a: " +beneficiario+"\t\tNIF: " +nifBeneficiario+"\n");                    
                        contadorBeneficiario++;
                        
                        IItemCollection concesionItemCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
                        Iterator<?> concesionIterator = concesionItemCollection.iterator();
                        
                        if(concesionIterator.hasNext()){
                            IItem concesion = (IItem) concesionIterator.next();

                            empresa1 = SubvencionesUtils.getString(concesion, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.EMPRESACOMEDOR1);
                            nombreMenor1 = SubvencionesUtils.getString(concesion, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE1);
                            empresa2 = SubvencionesUtils.getString(concesion, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.EMPRESACOMEDOR2);
                            nombreMenor2 = SubvencionesUtils.getString(concesion, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE2);
                            mesInicio = SubvencionesUtils.getString(concesion, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.MESINICIO);
                            
                            importe = SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR) * PORCENTAJE_RESOLUCION;
                        }                        

                        if(StringUtils.isNotBlank(nombreMenor2)){
                            numMenores = "2";
                        } else{
                            numMenores = "1";
                        }
                        
                        listado.append("\tNúmero de menores becados: " + numMenores + "\n");                        
                        listado.append("\tConcepto de la factura: " + concepto + "\n");                            
                        listado.append("\tEmpresa de comedor: " + empresa1 + "\n");
                        listado.append("\tNombre del menor: " + nombreMenor1 + "\n");
                        
                        if(StringUtils.isNotEmpty(empresa2)){
                            listado.append("\tEmpresa de comedor: " + empresa2 + "\n");
                        }
                        if(StringUtils.isNotEmpty(nombreMenor2)){
                            listado.append("\tNombre del menor: " + nombreMenor2 + "\n");
                        }
                        
                        listado.append("\tAyuda por importe de: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(importe*100)/100) + " €\n");
                        listado.append("\tMes de inicio: " + mesInicio + "\n");

                        total += (Math.rint(importe*100)/100);

                        SubvencionesUtils.insertaTabajadorSocialComoParticipante(cct, numexp, nifTrabajadorSocial, trabajadorSocial);                        
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
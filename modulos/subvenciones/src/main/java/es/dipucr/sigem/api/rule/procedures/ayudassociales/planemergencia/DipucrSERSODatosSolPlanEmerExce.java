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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

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

            String nfactura = "";
            String fechaFactura = "";
            String concepto = "";
            String proveedor = "";
            
            double importeExcepcional = 0;
            double total = 0;
            
            expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.RS);
            //Ya tenemos los expedientes que vamos a decretar en esta iteración
            if(!expedientesResolucion.isEmpty()){

                String strQuery = ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " IN " + SubvencionesUtils.getWhereInFormat(expedientesResolucion);               
                IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery);
                
                String strQuery2 = ConstantesString.WHERE + ConstantesSubvenciones.MunicipiosValidationTable.VALOR + " IN " + SubvencionesUtils.getWhereInFormat(expSolicitudesCiudadCol, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD) + ConstantesString.ORDER_BY + ConstantesSubvenciones.MunicipiosValidationTable.SUSTITUTO;                
                IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities(ConstantesSubvenciones.MunicipiosValidationTable.NOMBRE_TABLA, strQuery2);
                Iterator<?> ciudadOrdenIt = ciudadOrdenCol.iterator();
                
                while(ciudadOrdenIt.hasNext()){
                    
                    IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery+" AND TIPOAYUDA = 'EXCEPCIONAL' AND CIUDAD = '" +((IItem)ciudadOrdenIt.next()).getString("VALOR")+"' ORDER BY TIPOAYUDA, NOMBRESOLICITANTE, NOMBRE");
                    Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                    while (expSolicitudesIt.hasNext()){

                        beneficiario = "";
                        nifBeneficiario = "";                      
                        nfactura = "";
                        fechaFactura = "";
                        concepto = "";
                        proveedor = "";
                        importeExcepcional = 0;

                        IItem expSolicitud = (IItem) expSolicitudesIt.next();
                        
                        //Recuperamos el municipio si es distinto
                        if(!ciudad.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))){
                            ciudad = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                            
                            tipoAyuda = "";
                            trabajadorSocial = "";

                            descripcionCiudad = SubvencionesUtils.getMunicipioByValor(cct, ciudad);
                            countCiudad++;
                            
                            listado.append("\n");
                            listado.append(countCiudad + ". " + descripcionCiudad + "\n");
                        }
                        
                        if(!tipoAyuda.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA))){
                            trabajadorSocial = "";
                            tipoAyuda = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                            descripcionTipoAyuda = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DESCRIPCION_TIPOAYUDA);

                            listado.append("\tTipo de Ayuda: " + descripcionTipoAyuda + "\n");
                        }
                        
                        if(!trabajadorSocial.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))){
                            trabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);
                            nifTrabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DOCUMENTOIDENTIDAD);

                            listado.append("\tTrabajador/a Social: " + trabajadorSocial + "\n");
                        }
                        
                        listado.append("\n");
    
                        beneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                        nifBeneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                        
                        listado.append("\t" + contadorBeneficiario + " - Beneficiario/a: " + beneficiario + "\t\tNIF: " + nifBeneficiario + "\n");
                        
                        contadorBeneficiario++;
                        
                        String[] datosConcesion = getDatosConcesion(entitiesAPI, expSolicitud);                        
                        nfactura = datosConcesion[0];
                        fechaFactura = datosConcesion[1];
                        concepto = datosConcesion[2];
                        proveedor = datosConcesion[3];
                        
                        importeExcepcional = getImporte(entitiesAPI, expSolicitud);
                        total += importeExcepcional;
                        
                        listado.append("\tFactura Nº: " + nfactura + "\t\tFecha: " + fechaFactura + "\n");                        
                        listado.append("\tConcepto de la factura: " + concepto + "\n");                            
                        listado.append("\tProveedor: " + proveedor + "\n");
                        listado.append("\tAyuda por importe de: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, importeExcepcional) + " €\n");

                        SubvencionesUtils.insertaTabajadorSocialComoParticipante(cct, numexp, nifTrabajadorSocial, nifTrabajadorSocial);
                    }
                }            
            }
            //Probelmas con el redondeo en la máquina de java
            
            listado.append("\n\tEl importe total de la presente propuesta es: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(total*100)/100) + " €\n");
            
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            
            return listado.toString();
            
        } catch(Exception e) {
            LOGGER.error("No se ha podido recuperar los datos de las solicitudes. " + e.getMessage(),e);
            throw new ISPACRuleException("No se ha podido recuperar los datos de las solicitudes. " + e.getMessage(),e);
        }
    }

    public String[] getDatosConcesion(IEntitiesAPI entitiesAPI, IItem expSolicitud) throws ISPACRuleException {
        String[] datosConcesion = new String [4];
        
        String nfactura = "";
        String fechaFactura = "";
        String concepto = "";
        String proveedor = "";
        
        try{
            IItemCollection concesionExcCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
            Iterator<?> concesionExcIt = concesionExcCol.iterator();
            
            if(concesionExcIt.hasNext()){   
                IItem concesionExc = (IItem)concesionExcIt.next(); 
                
                nfactura = SubvencionesUtils.getString(concesionExc, ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.NFACTURA);
                fechaFactura = SubvencionesUtils.getString(concesionExc, ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.FECHAFACTURA);
                concepto = SubvencionesUtils.getString(concesionExc, ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.CONCEPTO);
                proveedor = SubvencionesUtils.getString(concesionExc, ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.PROVEEDOR);
            }
        } catch(Exception e) {
            LOGGER.error("No se ha podido recuperar el los datos de la concesión del expediente: " + SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP) + ". " + e.getMessage(), e);
            throw new ISPACRuleException("No se ha podido recuperar los datos de la concesión del expediente: " + SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP) + ". " + e.getMessage(), e);
        }
        
        datosConcesion[0] = nfactura;
        datosConcesion[1] = fechaFactura;
        datosConcesion[2] = concepto;
        datosConcesion[3] = proveedor;

        return datosConcesion;
    }

    public double getImporte( IEntitiesAPI entitiesAPI, IItem expSolicitud) throws ISPACRuleException {
        double importe = 0;
        String columnaImporte = "";
        String trimestre = ""; 
        
        try{
            trimestre = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.TRIMESTRE);
            
            if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                columnaImporte = ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1;
            } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                columnaImporte = ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2;
            } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                columnaImporte = ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3;
            } else{
                columnaImporte = ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4;
            }
            
            IItemCollection concesionItemCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
            Iterator<?> concesionIterator = concesionItemCollection.iterator();
            
            if(concesionIterator.hasNext()){
                IItem concesion = (IItem) concesionIterator.next();
                importe = SubvencionesUtils.getDouble(concesion, columnaImporte);
            }
            
        } catch(Exception e) {
            LOGGER.error("No se ha podido recuperar el importe del expdiente: " + SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP) + ". " + e.getMessage(), e);
            throw new ISPACRuleException("No se ha podido recuperar el importe del expdiente: " + SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP) + ". " + e.getMessage(), e);
        }
        
        return importe;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}
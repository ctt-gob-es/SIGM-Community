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
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrGeneraNotificacionesPlanEmergencia implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesPlanEmergencia.class);
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.AP, ExpedientesUtil.EstadoADM.AP25};

    protected String tipoDocumento = "Notificación";
    protected String plantilla = "Notificación Plan Emeregencia Social";
    protected int templateId = 0;
    protected int documentTypeId = 0;
    
    protected int numeroTramites = 0;

    String textoAlternativo = "";
    String textoAlternativo2 = "";
    String textoAlternativo3 = "";
    String textoAlternativo4 = "";
    String textoAlternativo5 = "";
    String textoAlternativo6 = "";
    String textoAlternativoPuntoTercero = "";
    String textoAlternativoPuntoTerceroContenido = "";
    String textoAlternativoPuntoTerceroContenido2 = "";

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
         LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
         try {
             IClientContext cct = rulectx.getClientContext();
             
             documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
             
             templateId = DocumentosUtil.getTemplateId(cct, plantilla, documentTypeId);
             
         } catch (ISPACException e) {
             LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
             throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
         }
         LOGGER.info(ConstantesString.FIN + this.getClass().getName());
         return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------

            String numexp = rulectx.getNumExp();

            List<String> expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(estadosAdm));

            if (!expedientesResolucion.isEmpty()){
                numeroTramites = TramitesUtil.cuentaTramites(cct, numexp, rulectx.getTaskProcedureId());
                for (String numexpHijo : expedientesResolucion){
                    generaNotificacion(numexpHijo, rulectx, cct, entitiesAPI);
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    private void generaNotificacion(String numexp, IRuleContext rulectx, ClientContext cct, IEntitiesAPI entitiesAPI) {
        try {
            int taskId = rulectx.getTaskId();
            
            IItemCollection participantesCollection = ParticipantesUtil.getParticipantes(cct, numexp, "", "");
            Iterator<?> participantesIterator = participantesCollection.iterator();
            if (participantesIterator.hasNext()) {
                IItem participante = (IItem) participantesIterator.next();
                
                DocumentosUtil.setParticipanteAsSsVariable(cct, participante);
                
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO, "" + Calendar.getInstance().get(Calendar.YEAR));
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE, TramitesUtil.getNombreTramite(cct, rulectx.getTaskId()));
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD, numexp);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.DATOSSOLICITUD, getDatosAlimentacion(rulectx, numexp, cct, entitiesAPI));

                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO, textoAlternativo);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO2, textoAlternativo2);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO3, textoAlternativo3);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO4, textoAlternativo4);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO5, textoAlternativo5);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO6, textoAlternativo6);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVOPUNTOTERCERO, textoAlternativoPuntoTercero);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO, textoAlternativoPuntoTerceroContenido);
                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO2, textoAlternativoPuntoTerceroContenido2);

                cct.setSsVariable(ConstantesSubvenciones.VariablesSesion.NRESOLUCIONPARCIAL, "" + numeroTramites);
                
                IItem documento = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, taskId, documentTypeId, templateId, numexp, "");
                documento.store(cct);
                
                DocumentosUtil.borraParticipanteSsVariable(cct);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.ANIO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NOMBRE_TRAMITE);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.NUMEXPSOLICITUD);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.DATOSSOLICITUD);

                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO2);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO3);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO4);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO5);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVO6);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVOPUNTOTERCERO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO);
                cct.deleteSsVariable(ConstantesSubvenciones.VariablesSesion.TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO2);
            }
            cct.endTX(true);
        } catch (ISPACRuleException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar la notificación del plan de emeregencia para el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar la notificación del plan de emeregencia para el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public String getDatosAlimentacion(IRuleContext rulectx, String numexp, IClientContext cct, IEntitiesAPI entitiesAPI) throws ISPACRuleException {
        try {
            StringBuilder listado = new StringBuilder();

            String ciudad = "";
            String descripcionCiudad = "";
            int countCiudad = 0;
            String beneficiario = "";
            String nifBeneficiario = "";
            String trabajadorSocial = "";
            String numMiembrosFamilia = "";

            String tipoAyuda = "";
            String descripcionTipoAyuda = "";
            String trimestre = "";

            int numCheques = 0;
            int importeCheques = 0;
            double importeExcepcional = 0;

            int numCheques1 = 0;
            int semestreImpresos1 = 0;
            int importeCheques1 = 0;
            double importeExcepcional1 = 0;

            int numCheques2 = 0;
            int semestreImpresos2 = 0;
            int importeCheques2 = 0;
            double importeExcepcional2 = 0;

            int numCheques3 = 0;
            int semestreImpresos3 = 0;
            int importeCheques3 = 0;
            double importeExcepcional3 = 0;

            int numCheques4 = 0;
            int semestreImpresos4 = 0;
            int importeCheques4 = 0;
            double importeExcepcional4 = 0;

            String nfactura = "";
            String fechaFactura = "";
            String concepto = "";
            String proveedor = "";

            String numMenores = "";
            int iNumMenores = 0;
            String haPagado = "";

            double totallibros = 0;
            double importeMaterial = 0;
            double importeLibros = 0;

            String empresa1 = "";
            String nombreMenor1 = "";
            String empresa2 = "";
            String nombreMenor2 = "";
            String mesInicio = "";

            double importe = 0;

            String strQuery = ConstantesString.WHERE + " NUMEXP = '" + numexp + "'";

            // Recuperamos las solicitud
            IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery);
            String strQuery2 = ConstantesString.WHERE + ConstantesSubvenciones.MunicipiosValidationTable.VALOR + " IN " + SubvencionesUtils.getWhereInFormat(expSolicitudesCiudadCol, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD) + ConstantesString.ORDER_BY + ConstantesSubvenciones.MunicipiosValidationTable.SUSTITUTO;
            
            IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities(ConstantesSubvenciones.MunicipiosValidationTable.NOMBRE_TABLA, strQuery2);
            Iterator<?> ciudadOrdenIt = ciudadOrdenCol.iterator();
            
            while (ciudadOrdenIt.hasNext()) {
                IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery + " AND CIUDAD = '" + ((IItem) ciudadOrdenIt.next()).getString("VALOR") + "' ORDER BY TIPOAYUDA, NOMBRESOLICITANTE");
                Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                while (expSolicitudesIt.hasNext()) {
                    IItem expSolicitud = (IItem) expSolicitudesIt.next();
                    trimestre = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.TRIMESTRE);

                    // Recuperamos el municipio si es distinto
                    if (!ciudad.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))) {
                        ciudad = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                        numMiembrosFamilia = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR);

                        tipoAyuda = "";
                        trabajadorSocial = "";
                        
                        descripcionCiudad = SubvencionesUtils.getMunicipioByValor(cct, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD));                        
                        if (StringUtils.isNotEmpty(descripcionCiudad)) {
                            countCiudad++;                            
                        }
                        
                        listado.append("\n");
                        listado.append("\t" + countCiudad + ". " + descripcionCiudad + "\n");
                    }
                    if (!tipoAyuda.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA))) {
                        trabajadorSocial = "";
                        tipoAyuda = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                        descripcionTipoAyuda = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DESCRIPCION_TIPOAYUDA);

                        listado.append("\tTipo de Ayuda: " + descripcionTipoAyuda + "\n");
                    }

                    if (!trabajadorSocial.equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))) {
                        trabajadorSocial = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);

                        listado.append("\tTrabajador/a Social: " + trabajadorSocial + "\n");
                    }
                    listado.append("\n");

                    beneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                    nifBeneficiario = SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                    listado.append("\t- Beneficiario/a: " + beneficiario + "\t\tNIF: " + nifBeneficiario + "\n");

                    IItemCollection numChequesCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud,  ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
                    Iterator<?> numChequesIt = numChequesCol.iterator();
                    
                    if (numChequesIt.hasNext()) {
                        IItem numCheq = (IItem) numChequesIt.next();
                        
                        numCheques1 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1);
                        semestreImpresos1 = SubvencionesUtils.getInt(numCheq, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                        importeCheques1 = (numCheques1 - semestreImpresos1) * 30;

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

                    IItemCollection concesionItemCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
                    Iterator<?> concesionIterator = concesionItemCollection.iterator();

                    if (concesionIterator.hasNext()) {
                        IItem concesion = (IItem) concesionIterator.next();

                        importeExcepcional1 = SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1);
                        importeExcepcional2 = SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2);
                        importeExcepcional3 = SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3);
                        importeExcepcional4 = SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4);
                    }

                    if (ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)) {
                        numCheques = importeCheques1 / 30;
                        importeCheques = importeCheques1;
                        importeExcepcional = importeExcepcional1;
                    } else if (ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)) {
                        numCheques = importeCheques2 / 30;
                        importeCheques = importeCheques2;
                        importeExcepcional = importeExcepcional2;
                    } else if (ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)) {
                        numCheques = importeCheques3 / 30;
                        importeCheques = importeCheques3;
                        importeExcepcional = importeExcepcional3;
                    } else {
                        numCheques = importeCheques4 / 30;
                        importeCheques = importeCheques4;
                        importeExcepcional = importeExcepcional4;
                    }

                    if (ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)) {
                        listado.append("\tNúmero de miembros de la unidad familiar: " + numMiembrosFamilia + "\n");
                        if ("SI".equals(SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS))) {
                            listado.append("\tMenor: SÍ\n");
                        }
                        listado.append("\tNº cheques: " + numCheques + "\n");
                        listado.append("\tImporte Cheques: " + importeCheques + " €\n");

                    } else if (ConstantesPlanEmergencia.EXCEPCIONAL.equals(tipoAyuda)) {
                        IItemCollection concesionExcCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
                        Iterator<?> concesionExcIt = concesionExcCol.iterator();
                        if (concesionExcIt.hasNext()) {
                            IItem concesionExc = (IItem) concesionExcIt.next();
                            
                            nfactura = SubvencionesUtils.getString(concesionExc, ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.NFACTURA);
                            fechaFactura = SubvencionesUtils.getString(concesionExc, ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.FECHAFACTURA);
                            concepto = SubvencionesUtils.getString(concesionExc, ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.CONCEPTO);
                            proveedor = SubvencionesUtils.getString(concesionExc, ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.PROVEEDOR);
                        }

                        listado.append("\tFactura Nº: " + nfactura + "\t\tFecha: " + fechaFactura + "\n");
                        listado.append("\tConcepto de la factura: " + concepto + "\n");
                        listado.append("\tProveedor: " + proveedor + "\n");
                        listado.append("\tAyuda por importe de: " + importeExcepcional + " €\n");
                    } else if (ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda)) {
                        iNumMenores = 0;

                        double dMaterial = 0;
                        double dLibros = 0;
                        importeMaterial = 0;
                        importeLibros = 0;

                        textoAlternativo = "\n";
                        textoAlternativo += "\tPara proceder al pago de la citada ayuda, será necesario aportar originales y en soporte papel, según se indica en la Base VI de la Convocatoria, la siguiente documentación:\n";
                        textoAlternativo += "\t- Anexo II.\n";
                        textoAlternativo += "\t- Facturas originales del material escolar o libros.\n";
                        textoAlternativo += "\t- Ficha de Tercero, sellada por la entidad bancaria (tanto para beneficiarios/as a título particular como para proveedores).";
                        
                        textoAlternativoPuntoTercero = "\n";
                        textoAlternativoPuntoTercero += "\tTercero.-";
                        
                        textoAlternativoPuntoTerceroContenido = " De conformidad con la Base V de la Convocatoria \"Obligaciones de los/as beneficiarios/as de la Ayuda\": En aquellos casos en que los Servicios Sociales o los Centros Educativos reclamen la entrega del material o";
                        textoAlternativoPuntoTerceroContenido2 = " libros adquiridos con la presente ayuda, los/as beneficiarios/as tendrán la obligación de entregarlos con la finalidad de incorporarlos a un fondo solidario (banco de libros) que permita su reutilización en los siguientes cursos escolares.";

                        textoAlternativo3 = "\t- ADJUNTAR LA PRESENTE NOTIFICACIÓN A LA FACTURA.\n";

                        textoAlternativo4 = "\tEnviar todo ello a la siguiente dirección:\n";
                        textoAlternativo4 += "\n";
                        textoAlternativo4 += "\tDIPUTACIÓN PROVINCIAL DE CIUDAD REAL\n";
                        textoAlternativo4 += "\t(Servicios Sociales)\n";
                        textoAlternativo4 += "\tPlaza de la Constitución, 1\n";
                        textoAlternativo4 += "\t13071 - CIUDAD REAL\n";

                        textoAlternativo5 = "\tSe informa al/a beneficiario/a que una vez terminado el curso escolar, y siempre que sea posible, pongan a disposición del Centro Social o Escolar correspondiente, los libros adquiridos con esta subvención.";

                        IItemCollection concesionLibrosCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
                        Iterator<?> concesionLibrosIterator = concesionLibrosCollection.iterator();

                        if (concesionLibrosIterator.hasNext()) {
                            IItem concesionLibros = (IItem) concesionLibrosIterator.next();
                            
                            totallibros = SubvencionesUtils.getDouble(concesionLibros, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS);
                            haPagado = concesionLibros.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.PAGADO);

                            for (int i = 1; i < 9; i++) {
                                dMaterial = SubvencionesUtils.getDouble(concesionLibros, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.CURSO + i);
                                dLibros = SubvencionesUtils.getDouble(concesionLibros, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.COSTELIBROS + i);

                                importeMaterial += dMaterial;

                                if (dMaterial > 0 || dLibros > 0){
                                    iNumMenores++;
                                }
                            }
                        }

                        importeLibros = totallibros - importeMaterial;

                        listado.append("\tNúmero de menores becados: " + iNumMenores + "\n");
                        listado.append("\tImporte de material escolar: "
                                + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(importeMaterial * 100) / 100) + " €, importe de libros: "
                                + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(importeLibros * 100) / 100) + " €\n");

                        if (StringUtils.isNotEmpty(haPagado) && "F".equalsIgnoreCase(haPagado.trim())) {
                            listado.append("\tLa familia HA PAGADO los libros.\n");
                        } else if (StringUtils.isNotEmpty(haPagado) && "L".equalsIgnoreCase(haPagado.trim())) {
                            listado.append("\tLa familia NO HA PAGADO los libros.\n");
                        } else{
                            listado.append("\tError al comprobar si la familia ha pagado o no los libros.\n");
                        }

                        listado.append("\tAyuda por importe de: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(totallibros * 100) / 100) + " €\n");
                    } else if (ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)) {

                        IItemCollection concesionComedorCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, SubvencionesUtils.getString(expSolicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP));
                        Iterator<?> concesionComedorIterator = concesionComedorCollection.iterator();

                        if (concesionComedorIterator.hasNext()) {
                            IItem concesionComedor = (IItem) concesionComedorIterator.next();

                            empresa1 = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.EMPRESACOMEDOR1);
                            nombreMenor1 = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE1);
                            empresa2 = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.EMPRESACOMEDOR2);
                            nombreMenor2 = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE2);
                            mesInicio = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.MESINICIO);
                            
                            importe = SubvencionesUtils.getDouble(concesionComedor, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR);
                        }

                        textoAlternativo = "\n";
                        textoAlternativo += "\tPara proceder al pago de la citada ayuda, será necesario aportar originales y en soporte papel, según se indica en la Base VIII de la Convocatoria, la siguiente documentación:\n";
                        textoAlternativo += "\t- Anexo III.\n";
                        textoAlternativo += "\t- Ficha de Tercero, sellada por la entidad bancaria.\n";
                        textoAlternativo6 = "\t- Facturas originales del periodo facturado relativas a la prestación del Servicio de comedor y documento en el que se recojan las incidencias surgidas en el mismo periodo.\n";

                        textoAlternativo3 = "\t- ADJUNTAR LA PRESENTE NOTIFICACIÓN A LA FACTURA.\n";

                        textoAlternativo4 = "\tEnviar todo ello a la siguiente dirección:\n";
                        textoAlternativo4 += "\n";
                        textoAlternativo4 += "\tDIPUTACIÓN PROVINCIAL DE CIUDAD REAL\n";
                        textoAlternativo4 += "\t(Servicios Sociales)\n";
                        textoAlternativo4 += "\tPlaza de la Constitución, 1\n";
                        textoAlternativo4 += "\t13071 - CIUDAD REAL\n";

                        if (StringUtils.isNotBlank(nombreMenor2)){
                            numMenores = "2";
                        } else{
                            numMenores = "1";
                        }

                        listado.append("\tNúmero de menores becados: " + numMenores + "\n");
                        listado.append("\tConcepto de la factura: " + concepto + "\n");
                        listado.append("\tEmpresa de comedor: " + empresa1 + "\n");
                        listado.append("\tNombre del menor: " + nombreMenor1 + "\n");
                        if (StringUtils.isNotEmpty(empresa2)) {
                            listado.append("\tEmpresa de comedor: " + empresa2 + "\n");
                        }
                        if (StringUtils.isNotEmpty(nombreMenor2)) {
                            listado.append("\tNombre del menor: " + nombreMenor2 + "\n");
                        }
                        listado.append("\tAyuda por importe de: " + SubvencionesUtils.formateaDouble(ConstantesString.FORMATO_IMPORTE, Math.rint(importe * 100) / 100) + " €\n");
                        listado.append("\tMes de inicio: " + mesInicio + "\n");
                    }
                }
            }
            return listado.toString();

        } catch (ISPACRuleException e){
            throw new ISPACRuleException(e);
        }catch (Exception e) {            
            throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes", e);
        }
    }    
}

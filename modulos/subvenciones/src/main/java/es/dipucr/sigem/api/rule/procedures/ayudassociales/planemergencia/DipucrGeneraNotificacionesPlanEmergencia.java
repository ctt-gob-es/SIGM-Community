package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.util.Calendar;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraNotificacionesPlanEmergencia implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesPlanEmergencia.class);

    protected String tipoDocumento = "Notificación";
    protected String plantilla = "Notificación Plan Emeregencia Social";
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
        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
            // ----------------------------------------------------------------------------------------------

            String numexp = rulectx.getNumExp();

            ArrayList<String> expedientesResolucion = new ArrayList<String>();

            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
            IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt = expRelCol.iterator();
            if (expRelIt.hasNext()) {
                while (expRelIt.hasNext()) {
                    IItem expRel = (IItem) expRelIt.next();
                    String numexpHijo = expRel.getString("NUMEXP_HIJO");

                    // IItem expHijo = entitiesAPI.getExpedient(numexpHijo);
                    IItem expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo);
                    if ((expHijo != null) && ("AP".equals(expHijo.get("ESTADOADM")) || "AP25".equals(expHijo.get("ESTADOADM")))) {
                        expedientesResolucion.add(numexpHijo);
                    }
                }
            }
            if (!expedientesResolucion.isEmpty()) {
                for (int i = 0; i < expedientesResolucion.size(); i++){
                    generaNotificacion(expedientesResolucion.get(i), rulectx, cct, entitiesAPI, genDocAPI, procedureAPI);
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

    private void generaNotificacion(String numexp, IRuleContext rulectx, ClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, IProcedureAPI procedureAPI) {
        try {
            IItem entityDocument = null;
            int documentId = 0;
            int documentTypeId = 0;
            int templateId = 0;
            int taskId = rulectx.getTaskId();
            Object connectorSession = null;
            String sFileTemplate = null;

            String nombre = "";
            String recurso = "";
            String idExt = "";

            connectorSession = genDocAPI.createConnectorSession();

            IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");

            IItemCollection taskTpDocCollection = (IItemCollection) procedureAPI.getTaskTpDoc(idTramCtl);
            Iterator<?> it = taskTpDocCollection.iterator();
            while (it.hasNext()) {
                IItem taskTpDoc = (IItem) it.next();
                if ((taskTpDoc.getString("CT_TPDOC:NOMBRE").trim()).equalsIgnoreCase((tipoDocumento).trim())) {
                    documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                }
            }
            cct.beginTX();
            // Comprobamos que haya encontrado el Tipo de documento
            if (documentTypeId != 0) {
                // Comprobar que el tipo de documento tiene asociado una
                // plantilla
                IItemCollection tpDocsTemplatesCollection = (IItemCollection) procedureAPI.getTpDocsTemplates(documentTypeId);
                if (tpDocsTemplatesCollection == null || tpDocsTemplatesCollection.toList().isEmpty()) {
                    throw new ISPACInfo(Messages.getString(ConstantesString.LOGGER_ERROR + ".decretos.acuses.tpDocsTemplates"));
                } else {
                    Iterator<?> docs = tpDocsTemplatesCollection.iterator();
                    boolean encontrado = false;
                    while (docs.hasNext() && !encontrado) {
                        IItem tpDocsTemplate = (IItem) docs.next();
                        if (((String) tpDocsTemplate.get("NOMBRE")).trim().equalsIgnoreCase(plantilla.trim())) {
                            templateId = tpDocsTemplate.getInt("ID");
                            encontrado = true;
                        }
                    }

                    // Recuperamos el participante del expediente que estamos
                    // resolviendo
                    // IItemCollection participantesCollection =
                    // entitiesAPI.getParticipants(numexp, "", "");
                    IItemCollection participantesCollection = ParticipantesUtil.getParticipantes(cct, numexp, "", "");
                    Iterator<?> participantesIterator = participantesCollection.iterator();
                    if (participantesIterator.hasNext()) {
                        IItem participante = (IItem) participantesIterator.next();
                        if (participante != null) {
                            // Añadir a la session los datos para poder utilizar
                            // <ispactag sessionvar='var'> en la plantilla
                            if ((String) participante.get("NOMBRE") != null) {
                                nombre = (String) participante.get("NOMBRE");
                            } else {
                                nombre = "";
                            }

                            if ((String) participante.get("RECURSO") != null) {
                                recurso = (String) participante.get("RECURSO");
                            } else {
                                recurso = "";
                            }
                            /**
                             * INICIO[Teresa] Ticket#106#: añadir el campo
                             * id_ext
                             * **/
                            if ((String) participante.get("ID_EXT") != null) {
                                idExt = (String) participante.get("ID_EXT");
                            } else {
                                idExt = "";
                            }
                            /**
                             * FIN[Teresa] Ticket#106#: añadir el campo id_ext
                             * **/
                            // if
                            // ((String)participante.get("RECURSO_TEXTO")!=null)
                            // recursoTexto =
                            // (String)participante.get("RECURSO_TEXTO");

                            // Obtener el sustituto del recurso en la tabla
                            // SPAC_VLDTBL_RECURSOS
                            String sqlQueryPart = "WHERE VALOR = 'Pers.Fis.-Empr.'";
                            IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
                            if (colRecurso.iterator().hasNext()) {
                                IItem iRecurso = (IItem) colRecurso.iterator().next();
                                recurso = iRecurso.getString("SUSTITUTO");
                            }
                            /**
                             * INICIO ##Ticket #172 SIGEM decretos y secretaria,
                             * modificar el recurso
                             * **/
                            if ("".equals(recurso)) {
                                recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
                            } else {
                                recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
                            }

                            cct.setSsVariable("ANIO", "" + Calendar.getInstance().get(Calendar.YEAR));
                            cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
                            cct.setSsVariable("NOMBRETRABAJADOR", nombre);
                            cct.setSsVariable("NOMBREBENEFICIARIO", idExt);
                            cct.setSsVariable("NUMEXPSOLICITUD", numexp);
                            cct.setSsVariable("DATOSSOLICITUD", getDatosAlimentacion(rulectx, numexp, cct, entitiesAPI));
                            cct.setSsVariable("RECURSO", recurso);
                            cct.setSsVariable("TEXTO_ALTERNATIVO", textoAlternativo);
                            cct.setSsVariable("TEXTO_ALTERNATIVO2", textoAlternativo2);
                            cct.setSsVariable("TEXTO_ALTERNATIVO3", textoAlternativo3);
                            cct.setSsVariable("TEXTO_ALTERNATIVO4", textoAlternativo4);
                            cct.setSsVariable("TEXTO_ALTERNATIVO5", textoAlternativo5);
                            cct.setSsVariable("TEXTO_ALTERNATIVO6", textoAlternativo6);
                            cct.setSsVariable("TEXTO_ALTERNATIVOPUNTOTERCERO", textoAlternativoPuntoTercero);
                            cct.setSsVariable("TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO",    textoAlternativoPuntoTerceroContenido);
                            cct.setSsVariable("TEXTO_ALTERNATIVOPUNTOTERCEROCONTENIDO2", textoAlternativoPuntoTerceroContenido2);

                            String tramite = "Notificación 2ª";

                            IItemCollection tramitesCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES,"WHERE NUMEXP='" + rulectx.getNumExp() + "' AND NOMBRE='" + tramite + "'");
                            List<?> tramitesList = tramitesCollection.toList();
                            cct.setSsVariable("NRESOLUCIONPARCIAL", "" + (tramitesList.size()));

                            connectorSession = genDocAPI.createConnectorSession();

                            IItem entityDocumentT = genDocAPI.createTaskDocument(taskId, documentTypeId);
                            int documentIdT = entityDocumentT.getKeyInt();

                            IItem entityTemplateT = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentIdT, templateId);

                            String infoPagT = entityTemplateT.getString("INFOPAG");
                            entityTemplateT.store(cct);

                            entityDocument = genDocAPI.createTaskDocument(taskId, documentTypeId);
                            documentId = entityDocument.getKeyInt();

                            sFileTemplate = DocumentosUtil.getFile(cct, infoPagT, null, null).getName();

                            // Generar el documento a partir la plantilla
                            IItem entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);

                            // Referencia al fichero del documento en el gestor
                            // documental
                            String docref = entityTemplate.getString("INFOPAG");
                            String sMimetype = genDocAPI.getMimeType(connectorSession, docref);
                            entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
                            String templateDescripcion = entityTemplate.getString("DESCRIPCION");
                            templateDescripcion = templateDescripcion + " - " + numexp;
                            entityTemplate.set("DESCRIPCION", templateDescripcion);
                            entityTemplate.set("DESTINO", nombre);
                            entityTemplate.set("DESTINO_ID", idExt);

                            entityTemplate.store(cct);

                            cct.deleteSsVariable("ANIO");
                            cct.deleteSsVariable("");
                            cct.deleteSsVariable("NOMBRE_TRAMITE");
                            cct.deleteSsVariable("NOMBRETRABAJADOR");
                            cct.deleteSsVariable("NOMBREBENEFICIARIO");
                            cct.deleteSsVariable("NUMEXPSOLICITUD");
                            cct.deleteSsVariable("DATOSSOLICITUD");
                            cct.deleteSsVariable("RECURSO");
                            cct.deleteSsVariable("TEXTO_ALTERNATIVO");
                            cct.deleteSsVariable("TEXTO_ALTERNATIVO2");
                            cct.deleteSsVariable("TEXTO_ALTERNATIVO3");
                            cct.deleteSsVariable("TEXTO_ALTERNATIVO4");
                            cct.deleteSsVariable("TEXTO_ALTERNATIVO5");
                            cct.deleteSsVariable("TEXTO_ALTERNATIVO6");

                            entityTemplateT.delete(cct);
                            entityDocumentT.delete(cct);

                            DocumentosUtil.deleteFile(sFileTemplate);
                        }
                    }
                }
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

            String strQuery = "WHERE NUMEXP = '" + numexp + "'";

            // Recuperamos las solicitud
            IItemCollection expSolicitudesCiudadCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery);
            Iterator<?> expSolicitudesCiudadIt = expSolicitudesCiudadCol.iterator();
            String strQuery2 = "";
            strQuery2 = "WHERE VALOR IN (";
            while (expSolicitudesCiudadIt.hasNext()){
                strQuery2 += "'" + ((IItem) expSolicitudesCiudadIt.next()).getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD) + "',";
            }
            strQuery2 = strQuery2.substring(0, strQuery2.length() - 1);
            strQuery2 += ") ORDER BY SUSTITUTO";

            IItemCollection ciudadOrdenCol = entitiesAPI.queryEntities("REC_VLDTBL_MUNICIPIOS", strQuery2);
            Iterator<?> ciudadOrdenIt = ciudadOrdenCol.iterator();
            while (ciudadOrdenIt.hasNext()) {

                IItemCollection expSolicitudesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, strQuery + " AND CIUDAD = '"
                        + ((IItem) ciudadOrdenIt.next()).getString("VALOR") + "' ORDER BY TIPOAYUDA, NOMBRESOLICITANTE");
                Iterator<?> expSolicitudesIt = expSolicitudesCol.iterator();
                while (expSolicitudesIt.hasNext()) {
                    IItem expSolicitud = (IItem) expSolicitudesIt.next();
                    trimestre = expSolicitud.getString(ConstantesPlanEmergencia.TRIMESTRE);

                    // Recuperamos el municipio si es distinto
                    if (!ciudad.equals(expSolicitud.get(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD))) {
                        ciudad = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                        numMiembrosFamilia = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NFAMILIAR);

                        tipoAyuda = "";
                        trabajadorSocial = "";
                        IItemCollection ciudadCol = entitiesAPI.queryEntities("REC_VLDTBL_MUNICIPIOS",
                                "WHERE VALOR='" + expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD) + "'");
                        Iterator<?> ciudadIt = ciudadCol.iterator();
                        if (ciudadIt.hasNext()) {
                            countCiudad++;
                            descripcionCiudad = ((IItem) ciudadIt.next()).getString("SUSTITUTO");
                        }
                        listado.append("\n");
                        listado.append("\t" + countCiudad + ". " + descripcionCiudad + "\n");
                    }
                    if (!tipoAyuda.equals(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA))) {
                        trabajadorSocial = "";
                        tipoAyuda = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                        descripcionTipoAyuda = expSolicitud.getString("DESCRIPCION_TIPOAYUDA");

                        listado.append("\tTipo de Ayuda: " + descripcionTipoAyuda + "\n");
                    }

                    if (!trabajadorSocial.equals(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE))) {
                        trabajadorSocial = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRESOLICITANTE);

                        listado.append("\tTrabajador/a Social: " + trabajadorSocial + "\n");
                    }
                    listado.append("\n");

                    beneficiario = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                    nifBeneficiario = expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                    listado.append("\t- Beneficiario/a: " + beneficiario + "\t\tNIF: " + nifBeneficiario + "\n");

                    IItemCollection numChequesCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                    Iterator<?> numChequesIt = numChequesCol.iterator();
                    if (numChequesIt.hasNext()) {
                        IItem numCheq = (IItem) numChequesIt.next();
                        numCheques1 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1);
                        try {
                            semestreImpresos1 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                        } catch (Exception e) {
                            semestreImpresos1 = 0;
                            LOGGER.debug("El campo ConstantesPlanEmergencia.SEMESTRE1IMPRESOS es nulo, vacío o no númerico. " + e.getMessage(), e);
                        }
                        importeCheques1 = (numCheques1 - semestreImpresos1) * 30;

                        numCheques2 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2);
                        try {
                            semestreImpresos2 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                        } catch (Exception e) {
                            LOGGER.debug("El campo SEMESTRE2IMPRESOS es nulo, vacío o no númerico. " + e.getMessage(), e);
                            semestreImpresos2 = 0;
                        }
                        importeCheques2 = (numCheques2 - semestreImpresos2) * 30;

                        numCheques3 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3);
                        try {
                            semestreImpresos3 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                        } catch (Exception e) {
                            LOGGER.debug("El campo SEMESTRE3IMPRESOS es nulo, vacío o no númerico. " + e.getMessage(), e);
                            semestreImpresos3 = 0;
                        }
                        importeCheques3 = (numCheques3 - semestreImpresos3) * 30;

                        numCheques4 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4);
                        try {
                            semestreImpresos4 = numCheq.getInt(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                        } catch (Exception e) {
                            LOGGER.debug("El campo SEMESTRE4IMPRESOS es nulo, vacío o no númerico. " + e.getMessage(), e);
                            semestreImpresos4 = 0;
                        }
                        importeCheques4 = (numCheques4 - semestreImpresos4) * 30;
                    }

                    IItemCollection concesionItemCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                    Iterator<?> concesionIterator = concesionItemCollection.iterator();

                    if (concesionIterator.hasNext()) {
                        IItem concesion = (IItem) concesionIterator.next();
                        String cantidad = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1);
                        if (StringUtils.isNotEmpty(cantidad)) {
                            try {
                                importeExcepcional1 = Double.parseDouble(cantidad.trim());
                            } catch (Exception e) {
                                LOGGER.debug("El campo TOTALSEMESTRE1 es nulo, vacío o no númerico. " + e.getMessage(), e);
                                importeExcepcional1 = Double.parseDouble(cantidad.trim().replace(",", "."));
                            }
                        }

                        cantidad = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2);
                        if (StringUtils.isNotEmpty(cantidad)) {
                            try {
                                importeExcepcional2 = Double.parseDouble(cantidad.trim());
                            } catch (Exception e) {
                                LOGGER.debug("El campo TOTALSEMESTRE2 es nulo, vacío o no númerico. " + e.getMessage(), e);
                                importeExcepcional2 = Double.parseDouble(cantidad.trim().replace(",", "."));
                            }
                        }

                        cantidad = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3);
                        if (StringUtils.isNotEmpty(cantidad)) {
                            try {
                                importeExcepcional3 = Double.parseDouble(cantidad.trim());
                            } catch (Exception e) {
                                LOGGER.debug("El campo TOTALSEMESTRE3 es nulo, vacío o no númerico. " + e.getMessage(), e);
                                importeExcepcional3 = Double.parseDouble(cantidad.trim().replace(",", "."));
                            }
                        }

                        cantidad = concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4);
                        if (StringUtils.isNotEmpty(cantidad)) {
                            try {
                                importeExcepcional4 = Double.parseDouble(cantidad.trim());
                            } catch (Exception e) {
                                LOGGER.debug("El campo TOTALSEMESTRE4 es nulo, vacío o no númerico. " + e.getMessage(), e);
                                importeExcepcional4 = Double.parseDouble(cantidad.trim().replace(",", "."));
                            }
                        }
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
                        if ("SI".equals(expSolicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.MENORES3ANIOS))) {
                            listado.append("\tMenor: SÍ\n");
                        }
                        listado.append("\tNº cheques: " + numCheques + "\n");
                        listado.append("\tImporte Cheques: " + importeCheques + " €\n");

                    } else if (ConstantesPlanEmergencia.EXCEPCIONAL.equals(tipoAyuda)) {
                        IItemCollection concesionExcCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                        Iterator<?> concesionExcIt = concesionExcCol.iterator();
                        if (concesionExcIt.hasNext()) {
                            IItem concesionExc = (IItem) concesionExcIt.next();
                            nfactura = concesionExc.getString(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.NFACTURA);
                            fechaFactura = concesionExc.getString(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.FECHAFACTURA);
                            concepto = concesionExc.getString(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.CONCEPTO);
                            proveedor = concesionExc.getString(ConstantesPlanEmergencia.DpcrSERSOPeConcesionExce.PROVEEDOR);
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

                        IItemCollection concesionLibrosCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                        Iterator<?> concesionLibrosIterator = concesionLibrosCollection.iterator();

                        if (concesionLibrosIterator.hasNext()) {
                            IItem concesionLibros = (IItem) concesionLibrosIterator.next();
                            try {
                                totallibros = Double.parseDouble(concesionLibros.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS));
                                haPagado = concesionLibros.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.PAGADO);

                                for (int i = 1; i < 9; i++) {
                                    dMaterial = 0;
                                    dLibros = 0;
                                    String material = concesionLibros.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.CURSO + i);
                                    if (StringUtils.isNotEmpty(material)){
                                        dMaterial = Double.parseDouble(material);
                                    }

                                    String libros = concesionLibros.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.COSTELIBROS + i);
                                    if (StringUtils.isNotEmpty(libros)){
                                        dLibros = Double.parseDouble(libros);
                                    }

                                    importeMaterial += dMaterial;

                                    if (dMaterial > 0 || dLibros > 0){
                                        iNumMenores++;
                                    }
                                }
                            } catch (Exception e) {
                                LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar el importe de la ayuda de libros del el expediente: " + expSolicitud.getString("NUMEXP") + ". " + e.getMessage(), e);
                                throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar el importe de la ayuda de libros del el expediente: " + expSolicitud.getString("NUMEXP") + ". " + e.getMessage(), e);
                            }
                        }

                        importeLibros = totallibros - importeMaterial;

                        listado.append("\tNúmero de menores becados: " + iNumMenores + "\n");
                        listado.append("\tImporte de material escolar: "
                                + new DecimalFormat("#,##0.00").format(Math.rint(importeMaterial * 100) / 100) + " €, importe de libros: "
                                + new DecimalFormat("#,##0.00").format(Math.rint(importeLibros * 100) / 100) + " €\n");

                        if (StringUtils.isNotEmpty(haPagado) && "F".equalsIgnoreCase(haPagado.trim())) {
                            listado.append("\tLa familia HA PAGADO los libros.\n");
                        } else if (StringUtils.isNotEmpty(haPagado) && "L".equalsIgnoreCase(haPagado.trim())) {
                            listado.append("\tLa familia NO HA PAGADO los libros.\n");
                        } else{
                            listado.append("\tError al comprobar si la familia ha pagado o no los libros.\n");
                        }

                        listado.append("\tAyuda por importe de: " + new DecimalFormat("#,##0.00").format(Math.rint(totallibros * 100) / 100) + " €\n");
                    } else if (ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)) {
                        // IItem expediente =
                        // entitiesAPI.getExpedient(expSolicitud.getString("NUMEXP"));
                        // IItem expediente = ExpedientesUtil.getExpediente(cct,
                        // expSolicitud.getString("NUMEXP"));
                        // String estadoadm = expediente.getString("ESTADOADM");

                        IItemCollection concesionComedorCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, expSolicitud.getString("NUMEXP"));
                        Iterator<?> concesionComedorIterator = concesionComedorCollection.iterator();

                        if (concesionComedorIterator.hasNext()) {
                            IItem concesionComedor = (IItem) concesionComedorIterator.next();
                            try {
                                empresa1 = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.EMPRESACOMEDOR1);
                                nombreMenor1 = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE1);
                                empresa2 = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.EMPRESACOMEDOR2);
                                nombreMenor2 = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE2);
                                mesInicio = concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.MESINICIO);

                                importe = Double.parseDouble(concesionComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR));
                            } catch (Exception e) {
                                LOGGER.error( ConstantesString.LOGGER_ERROR + " al recuperar el importe de la ayuda de libros del el expediente: " + expSolicitud.getString("NUMEXP") + ". " + e.getMessage(), e);
                                throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar el importe de la ayuda de libros del el expediente: " + expSolicitud.getString("NUMEXP") + ". " + e.getMessage(), e);
                            }
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
                        listado.append("\tAyuda por importe de: " + new DecimalFormat("#,##0.00").format(Math.rint(importe * 100) / 100) + " €\n");
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

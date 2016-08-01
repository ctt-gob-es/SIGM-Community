package es.dipucr.sigem.api.rule.procedures.ayudassociales.planvg;

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

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraNotificacionesPlanVG implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrGeneraNotificacionesPlanVG.class);
        protected String tipoDocumento = "";
        protected String plantilla = "";

    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());

        try{
            IClientContext cct = rulectx.getClientContext();
            
            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
            
            if(StringUtils.isNotEmpty(plantilla)){
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
        } catch(ISPACException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al generar el documento. " + e.getMessage(), e);
        }

        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
            //----------------------------------------------------------------------------------------------
            
            String numexp = rulectx.getNumExp();            
            String extractoDecreto = "";
                        
            ArrayList<String> expedientesResolucion = new ArrayList<String>();            
            
            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
             IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
             Iterator<?> expRelIt = expRelCol.iterator();                  
             if(expRelIt.hasNext()){
                 while (expRelIt.hasNext()){
                     IItem expRel = (IItem)expRelIt.next();
                     //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                     String numexpHijo = expRel.getString("NUMEXP_HIJO");
                     
                     IItem expHijo = ExpedientesUtil.getExpediente(cct,  numexpHijo); 
                     if(expHijo != null && ("AP".equals(expHijo.get("ESTADOADM")) || "RC".equals(expHijo.get("ESTADOADM")))){
                        expedientesResolucion.add(numexpHijo);
                     }                    
                 }
             } 
             
                strQuery = "WHERE NUMEXP_PADRE='" + numexp + "' ORDER BY NUMEXP_HIJO DESC";
            IItemCollection expRelCol2 = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt2 = expRelCol2.iterator();                  
            if(expRelIt2.hasNext()){
                while (expRelIt2.hasNext() && "".equals(extractoDecreto)){
                    IItem expRel2 = (IItem)expRelIt2.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    String numexpHijo = expRel2.getString("NUMEXP_HIJO");
                    
                    if("".equals(extractoDecreto)){
                        IItemCollection expHijoCol2 = entitiesAPI.getEntities("SGD_DECRETO", numexpHijo); 
                        Iterator<?> expHijoIt = expHijoCol2.iterator();
                        if(expHijoIt.hasNext()){
                            extractoDecreto = ((IItem)expHijoIt.next()).getString("EXTRACTO_DECRETO");
                        }
                    }
                }
            }
            if(!expedientesResolucion.isEmpty()){
                for(int i = 0;i<expedientesResolucion.size(); i++){
                    generaNotificacion(expedientesResolucion.get(i), rulectx, cct, entitiesAPI, genDocAPI, procedureAPI, extractoDecreto);
                }
             }            
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(e.getMessage(), e);
        }
                
        LOGGER.info(ConstantesString.FIN +this.getClass().getName());
        return true;
    }

    private void generaNotificacion(String numexp, IRuleContext rulectx, ClientContext cct, IEntitiesAPI entitiesAPI, IGenDocAPI genDocAPI, IProcedureAPI procedureAPI, String extractoDecreto) {
        try{
            IItem entityDocument = null;
            int documentId = 0;
            int documentTypeId = 0;
            int templateId = 0;
            int taskId = rulectx.getTaskId();
            Object connectorSession = null;
            String sFileTemplate = null;
            
            String nombre = "";
            String dirnot = "";
            String cPostal = "";
            String localidad = "";
            String caut = "";
            String recurso = "";
            String idExt = "";
                
            connectorSession = genDocAPI.createConnectorSession();
            
            IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");
            
            IItemCollection taskTpDocCollection = (IItemCollection) procedureAPI.getTaskTpDoc(idTramCtl);
            Iterator<?> it = taskTpDocCollection.iterator();
            while (it.hasNext()) {
                IItem taskTpDoc = (IItem) it.next();
                if (( taskTpDoc.getString("CT_TPDOC:NOMBRE").trim()).equalsIgnoreCase((tipoDocumento).trim())) {
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
                    
                    //Recuperamos el participante del expediente que estamos resolviendo
                    IItemCollection participantesCollection = ParticipantesUtil.getParticipantes( cct, numexp, "", "");
                    Iterator<?> participantesIterator = participantesCollection.iterator();
                    if(participantesIterator.hasNext()){
                        IItem participante = (IItem) participantesIterator.next();
                        if (participante!=null){                    
                            // Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
                            if ((String)participante.get("NOMBRE") != null){
                                nombre = (String)participante.get("NOMBRE");
                            } else{
                                nombre = "";
                            }
                            if ((String)participante.get("DIRNOT") != null){
                                dirnot = (String)participante.get("DIRNOT");
                            } else{
                                dirnot = "";
                            }
                            if ((String)participante.get("C_POSTAL") != null){
                                cPostal = (String)participante.get("C_POSTAL");
                            } else{
                                cPostal = "";
                            }
                            if ((String)participante.get("LOCALIDAD") != null){
                                localidad = (String)participante.get("LOCALIDAD");
                            } else{
                                localidad = "";
                            }
                            if ((String)participante.get("CAUT") != null){
                                caut = (String)participante.get("CAUT");
                            } else{
                                caut = "";
                            }
                            if ((String)participante.get("RECURSO") != null){
                                recurso = (String)participante.get("RECURSO");
                            } else{
                                recurso = "";
                            }
                            /**
                             * INICIO[Teresa] Ticket#106#: añadir el campo id_ext
                             * **/
                            if ((String)participante.get("ID_EXT") != null){
                                idExt = (String)participante.get("ID_EXT");
                            } else{
                                idExt = "";
                            }
                            /**
                             * FIN[Teresa] Ticket#106#: añadir el campo id_ext
                             * **/
                            
                            // Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
                            String sqlQueryPart = "'";
                            if("".equals(recurso)){
                                sqlQueryPart = "WHERE VALOR = 'Pers.Fis.-Empr.'";
                            } else{
                                sqlQueryPart = "WHERE VALOR = '" +recurso+"'";
                            }
                            IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
                            if (colRecurso.iterator().hasNext()){
                                IItem iRecurso = (IItem)colRecurso.iterator().next();
                                recurso = iRecurso.getString("SUSTITUTO");
                            }
                            /**
                             * INICIO
                             * ##Ticket #172 SIGEM decretos y secretaria, modificar el recurso
                             * **/
                            if ("".equals(recurso)){
                                recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
                            } else{
                                recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
                            }                            
                            
                            cct.setSsVariable("ANIO", "" +Calendar.getInstance().get(Calendar.YEAR));
                            cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
                            cct.setSsVariable("NUMEXPSOLICITUD", numexp);
                            cct.setSsVariable("DATOSSOLICITUD", getDatosResolucion(rulectx, numexp, cct, entitiesAPI));
                            cct.setSsVariable("RECURSO", recurso);
                            cct.setSsVariable("EXTRACTO_DECRETO", extractoDecreto);
                            
                            cct.setSsVariable("NOMBRE", nombre);
                            cct.setSsVariable("DIRNOT", dirnot);
                            cct.setSsVariable("C_POSTAL", cPostal);
                            cct.setSsVariable("LOCALIDAD", localidad);
                            cct.setSsVariable("CAUT", caut);
                        
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
                            
                            // Referencia al fichero del documento en el gestor documental
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
                            cct.deleteSsVariable("NOMBRE_TRAMITE");
                            cct.deleteSsVariable("NUMEXPSOLICITUD");
                            cct.deleteSsVariable("DATOSSOLICITUD");
                            cct.deleteSsVariable("RECURSO");                            
                            cct.deleteSsVariable("EXTRACTO_DECRETO");
                            cct.deleteSsVariable("NOMBRE");
                            cct.deleteSsVariable("DIRNOT");
                            cct.deleteSsVariable("C_POSTAL");
                            cct.deleteSsVariable("LOCALIDAD");
                            cct.deleteSsVariable("CAUT");
                            
                            entityTemplateT.delete(cct);
                            entityDocumentT.delete(cct);
                        }
                    }    
                }
            }
            cct.endTX(true);
        } catch(ISPACRuleException e){
            LOGGER.error(e.getMessage(), e);
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }

    public String getDatosResolucion(IRuleContext rulectx, String numexp, IClientContext cct, IEntitiesAPI entitiesAPI) throws ISPACRuleException{
        try{
            IItem expSol = ExpedientesUtil.getExpediente(cct, numexp); 
             if(expSol != null){
                 if("AP".equals(expSol.get("ESTADOADM"))){
                     return getDatosResolucionAprobado(entitiesAPI, expSol);
                 } else{
                     return getDatosResolucionRechazado(entitiesAPI, expSol);
                 }
             }            
        } catch (ISPACRuleException e){
            throw new ISPACRuleException(e);
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes", e);
        }
        return "";
    }
    
    private String getDatosResolucionAprobado(IEntitiesAPI entitiesAPI, IItem expSol) {
        StringBuilder salida = new StringBuilder();
        
        String ayuntamiento = "";
        int numTalleres = 0;
        String motivoTalleres = "";
        int numActividades = 0;
        String motivoActividades = "";
        
        try{        
            ayuntamiento = expSol.getString("IDENTIDADTITULAR");
            
            IItemCollection resolucionCollection = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expSol.getString("NUMEXP"));
            Iterator<?> resolucionIterator = resolucionCollection.iterator();
            
            if(resolucionIterator.hasNext()){
                IItem resolucion = (IItem)resolucionIterator.next();
                
                numTalleres = 0;
                numActividades = 0;
                
                numTalleres += parseInt(expSol.getString("NUMEXP"), resolucion.getString("TALLERES_EDU"));                        
                motivoTalleres = resolucion.getString("MOT_RECHAZO_TALLERES_IGU");

                numActividades += parseInt(expSol.getString("NUMEXP"), resolucion.getString("ACTIVIDADES_INTERCULT"));
                motivoActividades = resolucion.getString("MOT_RECHAZO_ACTIVIDAD_INTERCUL");
                
                salida.append("\n");
                salida.append("\t" +ayuntamiento+"\n\n");

                salida.append("\tNúmero de talleres de educación en la igualdad: " + numTalleres + "\n");
                if(StringUtils.isNotEmpty(motivoTalleres)){
                    salida.append("\tMotivo: " +motivoTalleres+"\n");                            
                }
                   salida.append("\tNúmero de talleres de educación en interculturalidad: " +numActividades+"\n");
                   if(StringUtils.isNotEmpty(motivoActividades)){
                       salida.append("\tMotivo: " +motivoActividades+"\n");                            
                   }
            }
        } catch(Exception e){
            LOGGER.debug("Ha habido algún problema al recuperar lso datos de la resolución aprobada. Expediente: " + expSol + ". " + e.getMessage(), e);
        }
        return salida.toString();
    }

    private String getDatosResolucionRechazado(IEntitiesAPI entitiesAPI, IItem expSol) {
        StringBuilder salida = new StringBuilder();
        try{        
            String ayuntamiento = expSol.getString("IDENTIDADTITULAR");
            String fechaSolicitud = expSol.getDate("FREG").toString();
            String sMotivo = "";
            int iMotivo = 0;
            
            int contador = 1;            
            
            IItemCollection resolucionCollection = entitiesAPI.getEntities("DPCR_RESOL_SOL_CONV_SUB", expSol.getString("NUMEXP"));
            Iterator<?> resolucionIterator = resolucionCollection.iterator();
            
            if(resolucionIterator.hasNext()){
                IItem resolucion = (IItem)resolucionIterator.next();
                
                iMotivo = parseInt(expSol.getString("NUMEXP"), resolucion.getString("MOTIVO_RECHAZO"));                        
                if(iMotivo == 0){
                    sMotivo = "";
                    LOGGER.debug("El campo MOTIVO_RECHAZO es nulo o vacío o no numérico en el expediente: " + expSol.getString("NUMEXP"));
                } else {
                    sMotivo = "" + iMotivo;
                }
                
                salida.append("\n");
                salida.append("\t- Así mismo se propone la denegación de los siguientes por el motivo que se indica: ");
                salida.append("\n");
                salida.append("\t" +contador+". " +ayuntamiento+"\n");                    

                if(StringUtils.isNotEmpty(sMotivo)){
                    salida.append("Motivo: " +sMotivo+"\n");
                    if(sMotivo.toUpperCase().contains("PLAZO") || sMotivo.toUpperCase().contains("FUERA")){
                        salida.append("Solicitud presentada: ");
                        salida.append(fechaSolicitud);
                    }
                }
            }
        } catch(Exception e){
            LOGGER.debug("Ha habido algún problema al recuperar lso datos de la resolución rechazada. Expediente: " + expSol + ". " + e.getMessage(), e);
        }
            
        return salida.toString();
    }
    
    private int parseInt (String numexp, String valor){
        int valorInt = 0;
        try{
            valorInt = Integer.parseInt(valor);                        
        } catch(NumberFormatException e){
            valorInt = 0;
            LOGGER.debug("El campo es nulo o vacío o no numérico en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
        return valorInt;
    }
}
package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import com.sun.star.connection.NoConnectException;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XPageCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GenerateLibroActasRule implements IRule {
    
    protected String strEntity = "SECR_LIBROACTAS";
    protected String strDocLibro = "Libro de Actas";
    protected String strDocInicio = "Libro de Actas - Inicio";
    protected String strDocFinal = "Libro de Actas - Final";
    protected String strDocFinActa = "Libro de Actas - Fin acta";
    
    private OpenOfficeHelper ooHelper = null;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------

            IItemCollection libros = entitiesAPI.getEntities(strEntity, rulectx.getNumExp());
            Iterator<?> it = libros.iterator();
            IItem libro = null;
            
            if(it.hasNext()) {
                libro = (IItem)it.next();
            }
            
            int year = libro.getInt("YEAR");
            cct.setSsVariable("YEAR", String.valueOf(year));
            int numeracion = libro.getInt("NUMERACION");
            cct.setSsVariable("NUMERACION", String.valueOf(numeracion));
            
            String strOrgano = getOrgano(rulectx);
            String descr1Organo = getDescripcion1Organo(strOrgano);
            String descr2Organo = getDescripcion2Organo(strOrgano);
            cct.setSsVariable("DESCRIPCION_ORGANO", descr1Organo);
            cct.setSsVariable("DESCRIPCION2_ORGANO", descr2Organo);

            //Generación de la diligencia de inicio
            generarDocumento(rulectx, strDocInicio, null);
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strDocInicio, "");
            File file1 = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
            //Es más práctico meter el salto de página en la plantilla
            //CommonFunctions.insertaSaltoDePagina(xComponent);
            
            //Generación de las diligencias por cada acta
            File file = null;
            String descr = "";
            int nPagesBefore = 0;
            int nPagesAfter = 0;
            IItemCollection actas = entitiesAPI.getEntities("SECR_ACTAS", rulectx.getNumExp());
            it = actas.iterator();
            int nActa = 0;
            int nActas = actas.toList().size();
            
            while (it.hasNext()) {
                nActa++;
                IItem acta = (IItem)it.next();
                IItem doc = getDocument(rulectx, acta);
                IItem sesion = SecretariaUtil.getSesion(rulectx, doc.getString("NUMEXP"));
                
                //Inserta el acta
                //---------------
                file = DocumentosUtil.getFile(cct, doc.getString(DocumentosUtil.INFOPAG), "", "doc");
                nPagesBefore = getNumPages(xComponent);
                CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
                nPagesAfter = getNumPages(xComponent);
                file.delete();
                
                //Genera la diligencia
                //--------------------
                Date dFecha = sesion.getDate("FECHA");
                SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
                String strFecha = dateformat.format(dFecha);
                cct.setSsVariable("FECHA", strFecha);
                String convocatoria = sesion.getString("NUMCONV");
                String tipoSesion = SecretariaUtil.getTipoSesion(rulectx, doc.getString("NUMEXP"));
                cct.setSsVariable("TIPO", tipoSesion);
                if ( nActa == 1 )
                {
                    cct.setSsVariable("FECHA_INI", strFecha);
                    cct.setSsVariable("TIPO_INI", tipoSesion);
                }
                if ( nActa == nActas )
                {
                    cct.setSsVariable("FECHA_FIN", strFecha);
                    cct.setSsVariable("TIPO_FIN", tipoSesion);
                }
                int nFolios = nPagesAfter - nPagesBefore + 1;
                int numeracion_ini = numeracion + nPagesBefore - 1;
                int numeracion_fin = numeracion + nPagesAfter - 1;
                cct.setSsVariable("NUM_FOLIOS", String.valueOf(nFolios));
                cct.setSsVariable("FOLIO_INI", String.valueOf(nPagesBefore));
                cct.setSsVariable("FOLIO_FIN", String.valueOf(nPagesAfter));
                cct.setSsVariable("NUMERACION_INI", String.valueOf(numeracion_ini));
                cct.setSsVariable("NUMERACION_FIN", String.valueOf(numeracion_fin));
                
                generarDocumento(rulectx, strDocFinActa, convocatoria);

                cct.deleteSsVariable("FECHA");
                cct.deleteSsVariable("TIPO");
                cct.deleteSsVariable("NUM_FOLIOS");
                cct.deleteSsVariable("FOLIO_INI");
                cct.deleteSsVariable("FOLIO_FIN");
                cct.deleteSsVariable("NUMERACION_INI");
                cct.deleteSsVariable("NUMERACION_FIN");
                

                //Inserta la diligencia
                //---------------------
                descr = strDocFinActa + " - " + convocatoria;
                strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), descr, "");
                file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
                CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
                file.delete();
            }

            //Generación de la diligencia final
            int nFolios = getNumPages(xComponent) + 1; // Una más por el salto de línea que vendrá más abajo
            int numeracion_ini = numeracion;
            int numeracion_fin = numeracion + nFolios - 1;
            cct.setSsVariable("NUM_ACTAS", String.valueOf(nActas));
            cct.setSsVariable("NUM_FOLIOS", String.valueOf(nFolios));
            cct.setSsVariable("NUMERACION_INI", String.valueOf(numeracion_ini));
            cct.setSsVariable("NUMERACION_FIN", String.valueOf(numeracion_fin));
            generarDocumento(rulectx, strDocFinal, null);
            cct.deleteSsVariable("NUM_ACTAS");
            cct.deleteSsVariable("NUM_FOLIOS");
            cct.deleteSsVariable("NUMERACION_INI");
            cct.deleteSsVariable("NUMERACION_FIN");
            cct.deleteSsVariable("FECHA_INI");
            cct.deleteSsVariable("TIPO_INI");
            cct.deleteSsVariable("FECHA_FIN");
            cct.deleteSsVariable("TIPO_FIN");

            //Inserción
            strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strDocFinal, "");
            file = DocumentosUtil.getFile(cct, strInfoPag, "", "doc");
            LibreOfficeUtil.insertaSaltoDePagina(xComponent);
            CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
            file.delete();

            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            file1.delete();
            
            //Guarda el resultado en gestor documental
            String strQuery = "WHERE NOMBRE = '"+strDocLibro+"'";
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            it = collection.iterator();
            int tpdoc = 0;
            
            if (it.hasNext()) {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
            }
            
            IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            FileInputStream in = new FileInputStream(file);
            int docId = newdoc.getInt("ID");
            Object connectorSession = gendocAPI.createConnectorSession();
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strDocLibro);
            entityDoc.set(DocumentosUtil.EXTENSION, "doc");
            entityDoc.store(cct);
            file.delete();
            
            //Borra los documentos intermedios del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION LIKE '"+strDocLibro+" -%'";
            collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            it = collection.iterator();
            
            while (it.hasNext()) {
                IItem doc = (IItem)it.next();
                entitiesAPI.deleteDocument(doc);
            }
            
            cct.deleteSsVariable("YEAR");
            cct.deleteSsVariable("NUMERACION");
            cct.deleteSsVariable("DESCRIPCION_ORGANO");
            cct.deleteSsVariable("DESCRIPCION2_ORGANO");
            
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido crear el libro de actas",e);
        } finally {
            if(null != ooHelper){
                ooHelper.dispose();
            }
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }

    private void generarDocumento(IRuleContext rulectx, String nombrePlantilla, String descripcion) throws ISPACException{
        try {
            //APIs
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();

            // Obtención de los tipos de documento asociados al trámite
            IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");
            IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
            
            if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()) {
                throw new ISPACInfo("No hay ningún tipo de documento asociado al trámite");
            }

            // Búsqueda de la plantilla indicada
            Iterator<?> itTpDocs = taskTpDocCollection.iterator();
            
            while(itTpDocs.hasNext()) {
                IItem taskTpDoc = (IItem)itTpDocs.next();
                int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                
                IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
                if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty())
                {
                    throw new ISPACInfo("No hay ninguna plantilla asociada al tipo de documento");
                }
                IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
                int templateId = tpDocsTemplate.getInt("ID");

                String nombre = tpDocsTemplate.getString("NOMBRE");
                
                if (nombre.compareTo(nombrePlantilla)==0) {
                    Object connectorSession = null;
                    
                    try {
                        connectorSession = gendocAPI.createConnectorSession();
                        // Abrir transacción para que no se pueda generar un documento sin fichero
                        cct.beginTX();
                    
                        int taskId = rulectx.getTaskId();
                        IItem entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
                        int documentId = entityDocument.getKeyInt();
        
                        // Generar el documento a partir de la plantilla
                        IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
                        entityTemplate.set(DocumentosUtil.EXTENSION, "doc");
                        
                        if ( descripcion != null) {
                            String templateDescripcion = entityTemplate.getString(DocumentosUtil.DESCRIPCION);
                            templateDescripcion = templateDescripcion + " - " + descripcion;
                            entityTemplate.set(DocumentosUtil.DESCRIPCION, templateDescripcion);
                            entityTemplate.store(cct);
                        }
                        entityTemplate.store(cct);
                        
                    } catch (Exception e) {
                        // Si se produce algún error se hace rollback de la transacción
                        cct.endTX(false);
                        
                        String message = "exception.documents.generate";
                        String extraInfo = null;
                        Throwable eCause = e.getCause();
                        
                        if (eCause instanceof ISPACException) {
                            if (eCause.getCause() instanceof NoConnectException){
                                extraInfo = "exception.extrainfo.documents.openoffice.off";
                                
                            } else {
                                extraInfo = eCause.getCause().getMessage();
                            }
                            
                        } else if (eCause instanceof DisposedException) {
                            extraInfo = "exception.extrainfo.documents.openoffice.stop";
                            
                        } else {
                            extraInfo = e.getMessage();
                        }            
                        throw new ISPACInfo(message, extraInfo);
                        
                    } finally {
                        if (connectorSession != null) {
                            gendocAPI.closeConnectorSession(connectorSession);
                        }
                    }
                    
                    // Si ha sido correcto se hace commit de la transacción
                    cct.endTX(true);
                }
            }
        } catch(Exception e) {
            throw new ISPACException(e);
        }
    }

    private IItem getDocument(IRuleContext rulectx, IItem acta) throws ISPACException {
        IItem doc = null;
        
        try {
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            String strId = acta.getString("IDDOC");
            String strQuery = "WHERE ID='"+strId+"'";
            IItemCollection docs = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            Iterator<?> it = docs.iterator();
            
            if (it.hasNext()) {
                doc = (IItem)it.next();
            }
            
        } catch(Exception e) {
            throw new ISPACException(e);
        }
        return doc;
    }
    
    private int getNumPages(XComponent xComponent)  throws ISPACException {
        int nPages = 0;
        
        try {
            XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
            XText xText = xTextDocument.getText();
            XTextCursor xTextCursor = xText.createTextCursor();
            xTextCursor.gotoRange(xText.getEnd(),false);

            XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent); 
            XController xController = xModel.getCurrentController(); 
            XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(XTextViewCursorSupplier.class, xController); 
            XTextViewCursor xViewCursor = xViewCursorSupplier.getViewCursor(); 
            xViewCursor.gotoRange(xTextCursor, false);
            
            XPageCursor xPageCursor = (XPageCursor)UnoRuntime.queryInterface(XPageCursor.class, xViewCursor);
            nPages = (int)xPageCursor.getPage();
        } catch(Exception e) {
            throw new ISPACException(e);
        }
        return nPages;
    }
    
    private String getOrgano(IRuleContext rulectx) throws ISPACException {
        String strOrgano = "";
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            IItemCollection coll = entitiesAPI.getEntities("SECR_LIBROACTAS", rulectx.getNumExp());
            Iterator<?> it = coll.iterator();
            
            if (it.hasNext()) {
                IItem libro = (IItem)it.next();
                strOrgano = libro.getString("ORGANO");
            }
            
        } catch(Exception e) {
            throw new ISPACException(e);
        }
        return strOrgano;
    }

    private String getDescripcion1Organo(String strOrgano) {
        String descr = "";
        
        if (strOrgano.compareTo("PLEN")==0) {
            descr = "DEL PLENO";
            
        } else if (strOrgano.compareTo("JGOB")==0) {
            descr = "DE LA JUNTA DE GOBIERNO";
            
        } else if (strOrgano.compareTo("MESA")==0) {
            descr = "DE LA MESA DE CONTRATACIÓN";
            
        } else if (strOrgano.compareTo("COMI")==0) {
            descr = "DE LA COMISIÓN INFORMATIVA";
        }
        
        return descr;
    }

    private String getDescripcion2Organo(String strOrgano) {
        String descr = "";
        
        if (strOrgano.compareTo("PLEN")==0) {
            descr = "EL PLENO";
            
        } else if (strOrgano.compareTo("JGOB")==0) {
            descr = "LA JUNTA DE GOBIERNO";
            
        } else if (strOrgano.compareTo("MESA")==0) {
            descr = "LA MESA DE CONTRATACIÓN";
            
        } else if (strOrgano.compareTo("COMI")==0) {
            descr = "LA COMISIÓN INFORMATIVA";
        }
        
        return descr;
    }
}
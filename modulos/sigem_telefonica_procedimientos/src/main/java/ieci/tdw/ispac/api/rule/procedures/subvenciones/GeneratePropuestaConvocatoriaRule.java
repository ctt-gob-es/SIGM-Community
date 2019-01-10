package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
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
import java.util.Iterator;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GeneratePropuestaConvocatoriaRule implements IRule {

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

            //Obtención de la información de la convocatoria
            IItemCollection coll = entitiesAPI.getEntities("SUBV_CONVOCATORIA", rulectx.getNumExp());
            Iterator<?> it = coll.iterator();
            IItem conv = null;
            
            if(it.hasNext()) {
                conv = (IItem)it.next();
            }
            
            String strTitulo = conv.getString("TITULO");
            String strContenido = conv.getString("CONTENIDO");
            strTitulo = strTitulo.toUpperCase();
            strContenido = strContenido.replaceAll("\r\n", "\r"); //Evita saltos de línea duplicados
            cct.setSsVariable("TITULO", strTitulo);
            cct.setSsVariable("CONTENIDO", strContenido);
            
            //Generación del comienzo del documento
            String strNombreTpDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-PropApr");
            String strNombrePlant = getPlantilla(rulectx, strNombreTpDoc);
            CommonFunctions.generarDocumento(rulectx, strNombreTpDoc, strNombrePlant, "intermedio");
            String strInfoPag = DocumentosUtil.getInfoPagByNombre(cct, rulectx.getNumExp(), strNombreTpDoc, "ID DESC");
            File file1 = DocumentosUtil.getFile(cct, strInfoPag);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
            
            //Generación de las bases
            File file = null;
            String descr = "";
            strNombreTpDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunBases");
            strNombrePlant = strNombreTpDoc;
            String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' ORDER BY NUMERO ASC";
            IItemCollection bases = entitiesAPI.queryEntities("SUBV_BASES", strQuery);
            it = bases.iterator();
            
            while (it.hasNext()) {
                IItem norma = (IItem)it.next();
                int nNumero = norma.getInt("NUMERO");
                String strNumeroNorma = String.valueOf(nNumero);
                String strExtractoNorma = norma.getString("EXTRACTO");
                String strContenidoNorma = norma.getString("CONTENIDO");
                strContenidoNorma = strContenidoNorma.replaceAll("\r\n", "\r");
                cct.setSsVariable("NUMERO_NORMA", strNumeroNorma);
                cct.setSsVariable("EXTRACTO_NORMA", strExtractoNorma);
                cct.setSsVariable("CONTENIDO_NORMA", strContenidoNorma);

                CommonFunctions.generarDocumento(rulectx, strNombreTpDoc, strNombrePlant, strNumeroNorma);
                
                cct.deleteSsVariable("NUMERO_NORMA");
                cct.deleteSsVariable("EXTRACTO_NORMA");
                cct.deleteSsVariable("CONTENIDO_NORMA");

                descr = strNombreTpDoc + " - " + strNumeroNorma;
                strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), descr, " ID DESC");
                file = DocumentosUtil.getFile(cct, strInfoPag);
                CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
                file.delete();
            }
            /**
             * INICIO [Teresa] Ticket#28# Poner formato en fichero en formato odt
             * **/
            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_ODT);
            /**
             * INICIO [Teresa] Ticket#28# Poner formato en fichero en formato odt
             * **/
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
            /**
             * FIN [Teresa] Ticket#28# Poner formato en fichero en formato odt
             * **/
            file1.delete();
            
            //Guarda el resultado en gestor documental
            strNombreTpDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-PropApr");
            strQuery = "WHERE NOMBRE = '"+strNombreTpDoc+"'";
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
            /**
             * INICIO [Teresa] Ticket#28# Poner formato en fichero en formato odt
             * **/
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/vnd.oasis.opendocument.text", strNombreTpDoc);
            entityDoc.set(DocumentosUtil.EXTENSION, Constants._EXTENSION_ODT);
            /**
             * FIN [Teresa] Ticket#28# Poner formato en fichero en formato odt
             * **/
            entityDoc.store(cct);
            file.delete();
            
            //Borra los documentos intermedios del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" +
                " AND (DESCRIPCION LIKE '" + DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunBases") + "%'" + 
                " OR DESCRIPCION LIKE '%intermedio')" ;                
            collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            it = collection.iterator();
            
            while (it.hasNext()) {
                IItem doc = (IItem)it.next();
                entitiesAPI.deleteDocument(doc);
            }
            
            cct.deleteSsVariable("TITULO");
            cct.deleteSsVariable("CONTENIDO");
            
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido crear la propuesta de convocatoria", e);
            
        } finally {
            if(null != ooHelper){
                ooHelper.dispose();
            }
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }

    private String getPlantilla(IRuleContext rulectx, String nombreTpDoc) throws ISPACRuleException {
        String strTemplateName = "";
        
        try {
            //APIs
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();

            //Obtención de los tipos de documento asociados al trámite
            IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
            int idTramCtl = processTask.getInt("ID_TRAM_CTL");
            IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
            if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty())
            {
                throw new ISPACInfo("No hay ningún tipo de documento asociado al trámite");
            }

            //Busco el tipo de documento
            Iterator<?> itTpDocs = taskTpDocCollection.iterator();
            boolean found = false;
            
            while(itTpDocs.hasNext() && !found) {
                IItem taskTpDoc = (IItem)itTpDocs.next();
                int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
                int tpDocId = DocumentosUtil.getIdTipoDocByNombre(cct, nombreTpDoc);
            
                if (tpDocId != documentTypeId) {
                    //Este no es el Tipo de documento solicitado
                    continue;
                }
                found = true;

                //Ahora busco la plantilla indicada
                IItemCollection plantillas = procedureAPI.getProcTemplates(tpDocId, rulectx.getProcedureId());
                
                if(plantillas==null || plantillas.toList().isEmpty()) {
                    //No hay ninguna plantilla asociada al tipo de documento
                    continue;
                }
                Iterator<?> itTemplate = plantillas.iterator();
                if(itTemplate.hasNext())             {
                    IItem tpDocsTemplate = (IItem)itTemplate.next();
                    strTemplateName = tpDocsTemplate.getString("NOMBRE");
                }
            }
        }
        catch(Exception e) {
            throw new ISPACRuleException(e);
        }
        
        return strTemplateName;
    }
}
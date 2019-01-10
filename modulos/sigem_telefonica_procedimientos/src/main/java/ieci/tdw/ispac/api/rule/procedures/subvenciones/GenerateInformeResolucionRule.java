package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;

public class GenerateInformeResolucionRule implements IRule {

    private OpenOfficeHelper ooHelper = null;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------

            //Obtención de las solicitudes asociadas a la convocatoria
            int nSolicitudes = 0;
            
            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosHijos(entitiesAPI, rulectx.getNumExp());

            for(String numexpHijo: expedientesRelacionados){
                IItemCollection solicitudes = entitiesAPI.getEntities("SUBV_SOLICITUD", numexpHijo);
                Iterator<?> it2 = solicitudes.iterator();
                
                if(it2.hasNext()) {
                    nSolicitudes++;
                }
            }

            //Creación de variables de sesión para las plantillas
            int taskId = rulectx.getTaskId();
            ITask iTask = invesFlowAPI.getTask(taskId);
            String strTaskName = iTask.getString("NOMBRE");
            cct.setSsVariable("NOMBRE_TRAMITE", strTaskName);
            
            //Generación del informe a partir de la plantilla
            String strTpDocName = "Informe del servicio";
            String strTemplateName = "Informe del servicio resolución convocatoria";
            CommonFunctions.generarDocumento(rulectx, strTpDocName, strTemplateName, "previo");
            
            //Abro el documento con OppenOffice para añadir la tabla
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strTemplateName+" - previo", " ID DESC");
            File file1 = DocumentosUtil.getFile(cct, strInfoPag);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());

            XTextTable xTable = LibreOfficeUtil.insertaTablaEnPosicion(xComponent,  "%TABLA1%", nSolicitudes + 1, 8);
            
            if(null != xTable){
                
                //Rellena la cabecera de la tabla
                LibreOfficeUtil.setTextoCeldaCabecera(xTable, "A1", "Solicitante");                
                LibreOfficeUtil.setTextoCeldaCabecera(xTable, "B1", "Criterio 1");                
                LibreOfficeUtil.setTextoCeldaCabecera(xTable, "C1", "Criterio 2");                
                LibreOfficeUtil.setTextoCeldaCabecera(xTable, "D1", "Criterio 3");                
                LibreOfficeUtil.setTextoCeldaCabecera(xTable, "E1", "Criterio 4");                
                LibreOfficeUtil.setTextoCeldaCabecera(xTable, "F1", "Criterio 5");                
                LibreOfficeUtil.setTextoCeldaCabecera(xTable, "G1", "Total puntos");                
                LibreOfficeUtil.setTextoCeldaCabecera(xTable, "H1", "Total subvención");                
                
                //Rellena la tabla con los datos de las solicitudes
                int nSolicitud = 0;
                for(String numexpHijo : expedientesRelacionados){
                    IItemCollection solicitudes = entitiesAPI.getEntities("SUBV_SOLICITUD", numexpHijo);
                    Iterator<?> it2 = solicitudes.iterator();
                    
                    if(it2.hasNext()) {
                        nSolicitud++;

                        IItem solicitud = (IItem)it2.next();
                        IItemCollection entidades = entitiesAPI.getEntities("SUBV_ENTIDAD", numexpHijo);
                        IItem entidad = (IItem)entidades.iterator().next();
    
                        LibreOfficeUtil.setTextoCelda(xTable, "A" + (nSolicitud+1), entidad.getString("NOMBRE"));
                        LibreOfficeUtil.setTextoCelda(xTable, "B" + (nSolicitud+1), solicitud.getString("CRITERIO1"));
                        LibreOfficeUtil.setTextoCelda(xTable, "C" + (nSolicitud+1), solicitud.getString("CRITERIO2"));
                        LibreOfficeUtil.setTextoCelda(xTable, "D" + (nSolicitud+1), solicitud.getString("CRITERIO3"));
                        LibreOfficeUtil.setTextoCelda(xTable, "E" + (nSolicitud+1), solicitud.getString("CRITERIO4"));
                        LibreOfficeUtil.setTextoCelda(xTable, "F" + (nSolicitud+1), solicitud.getString("CRITERIO5"));
                        LibreOfficeUtil.setTextoCelda(xTable, "G" + (nSolicitud+1), solicitud.getString("TOTAL_PUNTOS"));
                        LibreOfficeUtil.setTextoCelda(xTable, "H" + (nSolicitud+1), solicitud.getString("CANTIDAD_RECIBIDA"));
                    }
                }
            }    
            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            File file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            file1.delete();
            
            //Guarda el resultado en gestor documental
            String strQuery = "WHERE NOMBRE = '"+strTpDocName+"'";
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            Iterator <? >it = collection.iterator();
            int tpdoc = 0;
            
            if (it.hasNext()) {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
            }
            
            IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            FileInputStream in = new FileInputStream(file);
            int docId = newdoc.getInt("ID");
            Object connectorSession = gendocAPI.createConnectorSession();
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strTemplateName);
            entityDoc.set(DocumentosUtil.EXTENSION, "doc");
            entityDoc.store(cct);
            file.delete();
            
            //Borra el documento previo del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" + " AND DESCRIPCION='" + strTemplateName + " - previo'" ;
            collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            it = collection.iterator();
            
            while (it.hasNext()) {
                IItem doc = (IItem)it.next();
                entitiesAPI.deleteDocument(doc);
            }
            
            //Borra las variables de sesion utilizadas
            cct.deleteSsVariable("NOMBRE_TRAMITE");

            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido generar el Informe del servicio.", e);
            
        } finally {
            if(null != ooHelper){
                ooHelper.dispose();
            }
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}

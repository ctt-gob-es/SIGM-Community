package es.dipucr.sigem.api.rule.procedures;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.subvenciones.convocatorias.solicitudes.ObjetoSolictudConvocatoriaSubvencion;

public class FuncionesComunesDocumentos extends DipucrAutoGeneraDocIniTramiteRule{
    
    public static final Logger LOGGER = Logger.getLogger(FuncionesComunesDocumentos.class);
    
    private static final String PROTOCOLO_FILE = "file://";
    
    public void insertaTablas(IGenDocAPI gendocAPI, String docref, IRuleContext rulectx, int documentId, String refTablas, IEntitiesAPI entitiesAPI, String[] datos) {
        
        XComponent xComponent = null;
        File file = null;
        
        try {
            file = DocumentosUtil.getFile(rulectx.getClientContext(), docref, null, null);
            xComponent = LibreOfficeUtil.loadDocumento(rulectx.getClientContext(), file);
            
            if(null != xComponent){                
                String[] refTabla = refTablas.split(",");
                for(int i = 0; i<refTabla.length; i++){
                    insertaTabla(rulectx, xComponent, refTabla[i], entitiesAPI, datos);
                }
                
                guardaDocumento(gendocAPI, xComponent, documentId, docref);
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        } finally {
            if(null != xComponent){
                xComponent.dispose();
            }
            if(null != file && file.exists()){
                file.delete();
            }
        }
    }
    
    public void insertaTablas(IGenDocAPI gendocAPI, String docref, IRuleContext rulectx, int documentId, String refTablas, IEntitiesAPI entitiesAPI, String numexp) {
        
        XComponent xComponent = null;
        File file = null;

        try {            
            file = DocumentosUtil.getFile(rulectx.getClientContext(), docref, null, null);
            xComponent = LibreOfficeUtil.loadDocumento(rulectx.getClientContext(), file);
            
            if(null != xComponent){
                String[] refTabla = refTablas.split(",");
                for(int i = 0; i<refTabla.length; i++){                        
                    insertaTabla(rulectx, xComponent, refTabla[i], entitiesAPI, numexp);
                }
                
                guardaDocumento(gendocAPI, xComponent, documentId, docref);
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        } finally {
            if(null != xComponent){
                xComponent.dispose();
            }
            if(null != file && file.exists()){
                file.delete();
            }
        }
    }
    
    public void insertaTablas(IGenDocAPI gendocAPI, String docref, IRuleContext rulectx, int documentId, IEntitiesAPI entitiesAPI, ObjetoSolictudConvocatoriaSubvencion datosSolicitud, int tipo) {
        
        XComponent xComponent = null;
        File file = null;
        try {            
            file = DocumentosUtil.getFile(rulectx.getClientContext(), docref, null, null);
            xComponent = LibreOfficeUtil.loadDocumento(rulectx.getClientContext(), file);
            
            if(null != xComponent){
                String[] refTabla = refTablas.split(",");
                for(int i = 0; i < refTabla.length; i++){
                    insertaTabla(rulectx, xComponent, refTabla[i], entitiesAPI, datosSolicitud, tipo);
                }
                
                guardaDocumento(gendocAPI, xComponent, documentId, docref);
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(),e);
        } finally {
            if(null != xComponent){
                xComponent.dispose();
            }
            if(null != file && file.exists()){
                file.delete();
            }
        }
    }
    
    public void guardaDocumento(IGenDocAPI gendocAPI, XComponent xComponent, int documentId, String docref){
        try{
            String fileNameOut = FileTemporaryManager.getInstance().newFileName(DocumentosUtil.Extensiones.ODT);
            fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;            
            OpenOfficeHelper.saveDocument(xComponent, PROTOCOLO_FILE + fileNameOut,"");
            File fileOut = new File(fileNameOut);
            InputStream in = new FileInputStream(fileOut);
            gendocAPI.setDocument(null, documentId, docref, in, (int)(fileOut.length()), DocumentosUtil.MimeTypes.ODT);
        
            if( null != fileOut && fileOut.exists()){
                fileOut.delete();
            }
            if(null != in){
                in.close();
            }
        } catch(ISPACException e){
            LOGGER.error("ERROR al guardar el documento. " + e.getMessage(), e);
        } catch (FileNotFoundException e) {
            LOGGER.error("ERROR al guardar el documento. " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("ERROR al guardar el documento. " + e.getMessage(), e);
        }
    }
    
    /**
     * Método de inserción de tablas a sobreescribir en los hijos
     * @param rulectx
     * @param component
     * @param refTabla
     * @param entitiesAPI
     * @param datosSolicitud los datos a incluir en la tabla
     * @param tipo de los datos
     */
    public void insertaTabla(IRuleContext rulectx, XComponent xComponent, String refTabla, IEntitiesAPI entitiesAPI, ObjetoSolictudConvocatoriaSubvencion datosSolicitud, int tipo) {
        LOGGER.debug("Método insertaTabla de la clase: "+this.getClass().getName());        
    }

    /**
     * Método de inserción de tablas a sobreescribir en los hijos
     * @param rulectx
     * @param component
     * @param refTabla
     * @param entitiesAPI
     * @param datos a incluir en la tabla
     */
    public void insertaTabla(IRuleContext rulectx, XComponent xComponent, String refTabla, IEntitiesAPI entitiesAPI, String[] datos) {
        LOGGER.debug("Método insertaTabla de la clase: "+this.getClass().getName());
    }
}

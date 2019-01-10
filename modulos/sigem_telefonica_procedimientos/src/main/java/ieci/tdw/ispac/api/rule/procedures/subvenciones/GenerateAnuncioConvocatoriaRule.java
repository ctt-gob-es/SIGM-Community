package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
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

public class GenerateAnuncioConvocatoriaRule implements IRule {

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

            
            //Actualiza el campo estado de la entidad
            //de modo que permita mostrar los enlaces para crear la Solicitud BOP
            String numexp = rulectx.getNumExp();
            IItemCollection coll = entitiesAPI.getEntities("SUBV_CONVOCATORIA", numexp);
            Iterator<?> it = coll.iterator();
            
            if (it.hasNext()) {
                IItem entidad = (IItem)it.next();
                entidad.set("ESTADO", "Inicio");
                entidad.store(cct);
            }
            
            //Generación del documento de anuncio a partir de plnatilla
            //---------------------------------------------------------
            
            //Obtención de la información de la convocatoria
            coll = entitiesAPI.getEntities("SUBV_CONVOCATORIA", rulectx.getNumExp());
            it = coll.iterator();
            IItem conv = null;
            if(it.hasNext()) {
                conv = ((IItem)it.next());
            }
            String strTitulo = conv.getString("TITULO");
            String strContenido = conv.getString("CONTENIDO");
            strTitulo = strTitulo.toUpperCase();
            strContenido = strContenido.replaceAll("\r\n", "\r"); //Evita saltos de línea duplicados
            cct.setSsVariable("TITULO", strTitulo);
            cct.setSsVariable("CONTENIDO", strContenido);
            
            //Generación del comienzo del documento
            String strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunCab");
            CommonFunctions.generarDocumento(rulectx, strNombreDoc, strNombreDoc, null);
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strNombreDoc, " ID DESC");
            File file1 = DocumentosUtil.getFile(cct, strInfoPag);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
            
            //Generación de las bases
            File file = null;
            String descr = "";
            strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunBases");
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

                CommonFunctions.generarDocumento(rulectx, strNombreDoc, strNombreDoc, strNumeroNorma);
                
                cct.deleteSsVariable("NUMERO_NORMA");
                cct.deleteSsVariable("EXTRACTO_NORMA");
                cct.deleteSsVariable("CONTENIDO_NORMA");

                descr = strNombreDoc + " - " + strNumeroNorma;
                strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), descr, " ID DESC");
                file = DocumentosUtil.getFile(cct, strInfoPag);
                CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
                file.delete();
            }

            //Generación del pie del documento
            strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunPie");
            CommonFunctions.generarDocumento(rulectx, strNombreDoc, strNombreDoc, null);
            strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strNombreDoc, " ID DESC");
            file = DocumentosUtil.getFile(cct, strInfoPag);
            CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
            file.delete();
            
            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            file1.delete();
            
            //Guarda el resultado en gestor documental
            strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-Anuncio");
            strQuery = "WHERE NOMBRE = '"+strNombreDoc+"'";
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
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strNombreDoc);
            entityDoc.set(DocumentosUtil.EXTENSION, "doc");
            entityDoc.store(cct);
            file.delete();
            
            //Borra los documentos intermedios del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" +
                " AND (DESCRIPCION LIKE '" + DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunCab") + "%'" +
                " OR DESCRIPCION LIKE '" + DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunBases") + "%'" + 
                " OR DESCRIPCION LIKE '" + DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AnunPie") + "%')" ;                
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
            throw new ISPACRuleException("No se ha podido crear el anuncio de convocatoria", e);
            
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
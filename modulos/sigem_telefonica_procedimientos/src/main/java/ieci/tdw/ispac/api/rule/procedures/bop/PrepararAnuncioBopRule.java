package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
//import ieci.tdw.ispac.api.IProcedureAPI;
//import ieci.tdw.ispac.api.errors.ISPACException;
//import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
//import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;


//import com.sun.star.connection.NoConnectException;
//import com.sun.star.frame.XController;
//import com.sun.star.frame.XModel;
//import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;
//import com.sun.star.text.XPageCursor;
//import com.sun.star.text.XText;
//import com.sun.star.text.XTextCursor;
//import com.sun.star.text.XTextDocument;
//import com.sun.star.text.XTextViewCursor;
//import com.sun.star.text.XTextViewCursorSupplier;
//import com.sun.star.uno.UnoRuntime;


import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class PrepararAnuncioBopRule implements IRule 
{
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(GenerateBopRule.class);
    
//    protected String strDocActa = "Borrador de Acta de Pleno";
    protected String strEntity = "BOP_PUBLICACION";
    protected String strDoc = "BOP - Anuncio";
//    protected String strDocInicio = "BOP - Inicio";
//    protected String strDocFinal = "BOP - Final";
//    protected String strDocFinActa = "BOP - Fin anuncio";
    
    private OpenOfficeHelper ooHelper = null;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
        try
        {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------

//            String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
//            IItemCollection bops = entitiesAPI.queryEntities(strEntity, strQuery);
//            Iterator it = bops.iterator();
//            IItem bop = null;
//            if(it.hasNext()) {
//                bop = ((IItem)it.next());
//            }
            //Generación de la diligencia de inicio
//            generarDocumento(rulectx, strDocInicio, null);
//            String strInfoPag = CommonFunctions.getInfoPag(rulectx, entitiesAPI, strDocInicio);
//            generarDocumento(rulectx, strDocLibro, null);
            CommonFunctions.generarDocumento(rulectx, strDoc, strDoc, "borrar");
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strDoc + " - borrar", " ID DESC");
            LOGGER.warn("InfoPag del documento generado: " + strInfoPag);
            File file1 = DocumentosUtil.getFile(cct, strInfoPag);
//            String cnt = "uno:socket,host=localhost,port=8100;urp;StarOffice.NamingService";
//            ooHelper = OpenOfficeHelper.getInstance(cnt);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
            
            File file = null;
//            String descr = "";
//            int nPagesBefore = 0;
//            int nPagesAfter = 0;
            String strQuery2 = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND NOMBRE='Anexo a Solicitud' AND EXTENSION='DOC'";
            IItemCollection docs = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
//            IItemCollection actas = entitiesAPI.getEntities("SPAC_DT_DOCUMENTOS", rulectx.getNumExp());
            Iterator<?> it = docs.iterator();
//            int nActa = 0;
//            int nActas = actas.toList().size(); 
            
            while (it.hasNext())
            {
//                nActa++;
                IItem doc = (IItem)it.next();
//                IItem doc = getDocument(rulectx, acta);
//                IItem sesion = CommonFunctions.getSesion(rulectx, doc.getString("NUMEXP"));
                
                //Inserta el acta
                //---------------
                if (doc != null)
                {
                    file = DocumentosUtil.getFile(cct, doc.getString(DocumentosUtil.INFOPAG));
                    LOGGER.warn("InfoPag del documento a concatenar: " + doc.getString(DocumentosUtil.INFOPAG));
    //                nPagesBefore = getNumPages(xComponent);
                    CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
    //                nPagesAfter = getNumPages(xComponent);
                    file.delete();
                }
            }

            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            file1.delete();
            
            //Guarda el resultado en gestor documental
            String strQuery = "WHERE NOMBRE = '" + strDoc + "'";
            LOGGER.warn("Query: " + strQuery);
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            it = collection.iterator();
            int tpdoc = 0;
            if (it.hasNext())
            {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
                LOGGER.warn("Documento guardado en el gestor documental (ID): " + tpdoc);
            }
            IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            FileInputStream in = new FileInputStream(file);
            int docId = newdoc.getInt("ID");
            Object connectorSession = gendocAPI.createConnectorSession();
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strDoc);
            entityDoc.set(DocumentosUtil.EXTENSION, "doc");
            entityDoc.store(cct);
            file.delete();
            
            //Borra los documentos intermedios del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION LIKE '%borrar%'";
            collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            it = collection.iterator();
            while (it.hasNext())
            {
                IItem doc = (IItem)it.next();
                LOGGER.warn("Borrar (ID): " + doc.getString("ID"));
                LOGGER.warn("Borrar (NUMEXP): " + doc.getString("NUMEXP"));
                LOGGER.warn("Borrar (NOMBRE): " + doc.getString("NOMBRE"));
                LOGGER.warn("Borrar (DESCRIPCION): " + doc.getString(DocumentosUtil.DESCRIPCION));
                entitiesAPI.deleteDocument(doc);
            }

            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido crear el anuncio del BOP",e);
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

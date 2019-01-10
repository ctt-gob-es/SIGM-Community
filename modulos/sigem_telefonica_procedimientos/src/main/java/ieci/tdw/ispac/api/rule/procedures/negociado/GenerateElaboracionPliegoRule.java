package ieci.tdw.ispac.api.rule.procedures.negociado;

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

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;

/**
 * @author teresa
 * @date 18/11/2009
 * @propósito Genera el documento de Elaboración del pliego para el expediente de Convocatoria de contratación al iniciar
 *  el trámite de Elaboración del Pliego de cláusulas Económico-Administrativas y le concatena los documentos de Prescripciones técnicas
 *  y Valoración de la oferta.
 */
public class GenerateElaboracionPliegoRule implements IRule {

    /** Logger de la clase. */
    protected static final Logger logger = Logger.getLogger(GenerateElaboracionPliegoRule.class);
    
    protected String strDocValoracionOferta = "Valor oferta";
    protected String strDocElaboracionPliego = "Elab pliego";
    protected String strDocPrescripcionesTecnicas = "Pres tecnicas";
    protected String strTemplateElaboracionPliego = "Elaboración del pliego";
    
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
            
            String nameDocValoracionOferta = "";
            String nameDocElaboracionPliego = "";
            String nameDocPrescripcionesTecnicas = "";
            File file = null;

            // Obtener el documento de "Valoración de la oferta"
            
            //Obtenemos el nombre del documento de "Valoración de la oferta" a partir de su código
            nameDocValoracionOferta = DocumentosUtil.getNombreTipoDocByCod(cct, strDocValoracionOferta);
            
            //Obtenemos el nombre del documento de "Elaboración del pliego" a partir de su código
            nameDocElaboracionPliego = DocumentosUtil.getNombreTipoDocByCod(cct, strDocElaboracionPliego);
            
            //Obtenemos el nombre del documento de "Prescripciones técnicas" a partir de su código
            nameDocPrescripcionesTecnicas = DocumentosUtil.getNombreTipoDocByCod(cct, strDocPrescripcionesTecnicas);
            

            //Generar documento de "Elaboración del pliego" desde plantilla.
            //Después concatenar el file de "Valoración de la oferta" y el de "Prescripciones técnicas"
            
            //Generación del documento a partir de la plantilla
            CommonFunctions.generarDocumento(rulectx, nameDocElaboracionPliego, strTemplateElaboracionPliego, "previo");
            
            //Abro el documento con OppenOffice para concatenarle los otros dos documentos
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strTemplateElaboracionPliego+" - previo", " ID DESC");
            File file1 = DocumentosUtil.getFile(cct, strInfoPag);
            //String cnt = "uno:socket,host=localhost,port=8100;urp;StarOffice.NamingService";
            //ooHelper = OpenOfficeHelper.getInstance(cnt);
            ooHelper = OpenOfficeHelper.getInstance();
            XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());

            
            //Es más práctico meter el salto de página en la plantilla
            LibreOfficeUtil.insertaSaltoDePagina(xComponent);
            
            //Obtenemos el documento de "Valoración de la oferta" a partir de su nombre
            String infoPagValoracionOferta = DocumentosUtil.getInfoPagByNombre(cct, rulectx.getNumExp(), nameDocValoracionOferta, "ID DESC");
            file = DocumentosUtil.getFile(cct, infoPagValoracionOferta);
            
            //Concatenar el documento de "Valoración de la oferta" al de "Elaboración del pliego"
            CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
            file.delete();
            LibreOfficeUtil.insertaSaltoDePagina(xComponent);
            
            //Obtenemos el documento de "Prescripciones técnicas" a partir de su nombre
            String infoPagPrescripcionesTecnicas = DocumentosUtil.getInfoPagByNombre(cct, rulectx.getNumExp(), nameDocPrescripcionesTecnicas, "ID DESC");
            file = DocumentosUtil.getFile(cct, infoPagPrescripcionesTecnicas);
            
            //Concatenar el documento de "Prescripciones técnicas" al de "Elaboración del pliego"
            CommonFunctions.Concatena(xComponent, "file://" + file.getPath());
            file.delete();
            //CommonFunctions.insertaSaltoDePagina(xComponent);
        
            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            file1.delete();

            //Guarda el resultado en gestor documental
            String strQuery = "WHERE NOMBRE = '"+nameDocElaboracionPliego+"'";
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
            Iterator<?> it = collection.iterator();
            int tpdoc = 0;
            if (it.hasNext())
            {
                IItem tpd = (IItem)it.next();
                tpdoc = tpd.getInt("ID");
            }
            IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
            FileInputStream in = new FileInputStream(file);
            int docId = newdoc.getInt("ID");
            Object connectorSession = gendocAPI.createConnectorSession();
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", nameDocElaboracionPliego);
            entityDoc.set(DocumentosUtil.EXTENSION, "doc");
            entityDoc.store(cct);
            file.delete();
            
            //Borra el documento previo del gestor documental
            strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'" +
                    " AND DESCRIPCION = '"+strTemplateElaboracionPliego+" - previo'";
            collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
            it = collection.iterator();
            while (it.hasNext())
            {
                IItem doc = (IItem)it.next();
                entitiesAPI.deleteDocument(doc);
            }
            
            return Boolean.TRUE;

        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido crear el documento de Elaboración del pliego", e);
            
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
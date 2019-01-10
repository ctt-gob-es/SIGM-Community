package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
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

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * Regla para ser utilizada desde una plantilla. Recibe el parámetro CLASIFICACION que puede tomar los siguientes valores:
 * 
 * diputacion
 * ayuntamiento
 * consejeria
 * ministerio
 * otra
 * particular
 * 
 * Devuelve una cadena con las entidades de cada grupo de clasificacion y sus anuncios correspondientes.
 *
 */
public class GenerateResumenFacturasConCreditoRule implements IRule 
{
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(GenerateLiquidacionRecibosRule.class);
    
    protected String strDocFactura = "BOP - Resumen";

    private OpenOfficeHelper ooHelper = null;

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {

        IClientContext cct = null;
        String strQuery = null;
        IItemCollection collection = null;
        Iterator<?> it = null;
        IItem item = null;
        String fechaInicio = null;
        String fechaFin = null;

        String entidad = null;
        String numexp = null;
        String sumario = null;
        double coste = -1;
        double costeEntidad = 0;
        int anunciosEntidad = 0;
        Date fechaPublicacion = null;
        XComponent xComponent = null;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        File file = null;
        File file1 = null;

        LOGGER.warn("INICIO.");
        
        try{
            //----------------------------------------------------------------------------------------------
            cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
            //----------------------------------------------------------------------------------------------

            // Abrir transacción
            cct.beginTX();

            LOGGER.warn("Query: " + strQuery);
            collection = entitiesAPI.getEntities("BOP_LIQUIDACION", cct.getStateContext().getNumexp());    
            it = collection.iterator();
            if (it.hasNext()){
                item = (IItem)it.next();
                
                fechaInicio = item.getString("FECHA_INICIO"); 
                fechaFin = item.getString("FECHA_FIN"); 
            }

            //FECHA_PUBLICACION != NULL

            strQuery = "WHERE TIPO_FACTURACION = 'Pago con crédito' AND FECHA_PUBLICACION BETWEEN '" + fechaInicio +"' AND '" + fechaFin + "' ORDER BY ENTIDAD";
            collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);    
            it = collection.iterator();

            // Crear un nuevo documento de factura
            CommonFunctions.generarDocumento(rulectx, strDocFactura, strDocFactura, "borrar");
            String strInfoPag = DocumentosUtil.getInfoPagByDescripcionEquals(cct, rulectx.getNumExp(), strDocFactura + " - borrar", " ID DESC");
            LOGGER.warn("InfoPag del documento generado: " + strInfoPag);
            file1 = DocumentosUtil.getFile(cct, strInfoPag);
            ooHelper = OpenOfficeHelper.getInstance();
            xComponent = ooHelper.loadDocument("file://" + file1.getPath());

            while (it.hasNext())
            {
                item = (IItem)it.next();
                
                entidad = item.getString("ENTIDAD");
                LOGGER.warn("Entidad: " + entidad);
                
                if (entidad!=null)
                {
                    numexp = item.getString("NUMEXP");

                    sumario = item.getString("SUMARIO");
                    if (sumario == null)
                    {
                        sumario = "";
                    }

                    coste = item.getDouble("COSTE");
                    if (coste < 0)
                    {
                        coste = 0.0;
                    }

                    fechaPublicacion = item.getDate("FECHA_PUBLICACION");
                    
                    anunciosEntidad ++;
                    costeEntidad = costeEntidad + coste;

                    meterFactura(xComponent, entidad, numexp, sumario, String.valueOf(coste), df.format(fechaPublicacion));
                }
            }

            meterTotal(xComponent, String.valueOf(anunciosEntidad), String.valueOf(costeEntidad));

            //Guarda el resultado en repositorio temporal
            String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
            fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
            file = new File(fileName);
            OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
            file1.delete();
            
            //Guarda el resultado en gestor documental
            strQuery = "WHERE NOMBRE = '" + strDocFactura + "'";
            LOGGER.warn("Query: " + strQuery);
            collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
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
            IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", strDocFactura);
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
            
        } catch (Exception e) {
            // Si se produce algún error se hace rollback de la transacción
            try {
                cct.endTX(false);
            } catch (ISPACException e1) {
                LOGGER.error("ERROR. " + e1.getMessage(), e1);
            }

            throw new ISPACRuleException("Error al obtener el resumen de las facturas.", e);
        } finally {
            if(null != ooHelper){
                ooHelper.dispose();
            }
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
    
    private void meterFactura (XComponent xComponent, String entidad, String numexp, String sumario, String coste, String fechaPublicacion) throws ISPACException
    {
        try
        {
            XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
            XText xText = xTextDocument.getText();
            XTextCursor xTextCursor = xText.createTextCursor();
            xTextCursor.gotoRange(xText.getEnd(),false);

            LOGGER.warn("Factura: " + fechaPublicacion + "\t" + numexp + "\t" + sumario + "\t\t" + coste);
            xText.insertString(xTextCursor, "\r\r" + entidad + "\t" + fechaPublicacion + "\t" + numexp + "\t" + sumario + "\t\t" + coste, false);
        }
        catch(Exception e)
        {
            throw new ISPACException(e);
        }
    }

    private void meterTotal (XComponent xComponent, String numFacturas, String costeTotal) throws ISPACException
    {
        try
        {
            XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
            XText xText = xTextDocument.getText();
            XTextCursor xTextCursor = xText.createTextCursor();
            xTextCursor.gotoRange(xText.getEnd(),false);

            Log.warn("\t\tNúmero de facturas: " + numFacturas + "\t\tTOTAL: " + costeTotal);
            xText.insertString(xTextCursor, "\r\r\r\t\tNúmero de facturas: " + numFacturas + "\t\tTOTAL: " + costeTotal, false);
        }
        catch(Exception e)
        {
            throw new ISPACException(e);
        }
    }
}
